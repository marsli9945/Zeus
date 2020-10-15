package com.tuyoo.framework.grow.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
@Component
public class ResultGatewayFilter implements GatewayFilter, Ordered
{
    @Override
    public int getOrder()
    {
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        try
        {
            //获取response的 返回数据
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK)
            {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse)
                {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body)
                    {
                        // 判断服务返回的数据类型进行拦截，根据自己的业务进行修改
                        if (body instanceof Flux)
                        {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.buffer().map(dataBuffers ->
                            {
                                DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                                DataBuffer join = dataBufferFactory.join(dataBuffers);
                                byte[] content = new byte[join.readableByteCount()];
                                join.read(content);
                                DataBufferUtils.release(join);
                                String responseData = new String(content, StandardCharsets.UTF_8);
                                Integer rawStatusCode = originalResponse.getRawStatusCode();
                                HashMap<String, Object> jsonLog = new HashMap<>();
                                jsonLog.put("search_user", exchange.getRequest().getHeaders().getFirst("search_user"));
                                jsonLog.put("rawStatusCode", rawStatusCode);
                                jsonLog.put("响应转前", responseData);

                                if (rawStatusCode != null && rawStatusCode >= 200 && rawStatusCode < 300)
                                {
                                    try
                                    {
                                        responseData = JSON.toJSONString(ResultEntities.success(JSON.parse(responseData)), SerializerFeature.WriteMapNullValue);
                                        jsonLog.put("json check", 1);
                                    }
                                    catch (Exception e)
                                    {
                                        responseData = JSON.toJSONString(ResultEntities.success(responseData), SerializerFeature.WriteMapNullValue);
                                        jsonLog.put("json check", 2);
                                    }
                                }
                                else
                                {
                                    responseData = JSON.toJSONString(ResultEntities.init(
                                            rawStatusCode,
                                            responseData,
                                            null
                                    ), SerializerFeature.WriteMapNullValue);
                                    jsonLog.put("json check", 3);

                                    // 返回的httpCode全为200，异常状态吗内置到code
                                    originalResponse.setRawStatusCode(200);
                                }
                                jsonLog.put("响应转后", responseData);

                                // 重新设置content-length避免body被截断
                                String resultLen = String.valueOf(responseData.getBytes().length);
                                originalResponse.getHeaders().set("Content-length", resultLen);
                                jsonLog.put("response_path", exchange.getRequest().getPath().toString());
                                if (!exchange.getRequest().getPath().toString().equals("/v1/grow-analytics-log-server/log/send"))
                                {
                                    log.info(JSON.toJSONString(jsonLog));
                                }

                                // 设置跨域
                                originalResponse.getHeaders().set("Access-Control-Allow-Origin", "*");
                                originalResponse.getHeaders().set("Access-Control-Allow-Methods", "*");
                                originalResponse.getHeaders().set("Access-Control-Allow-Headers", "*");

                                byte[] uppedContent = responseData.getBytes(StandardCharsets.UTF_8);
                                return bufferFactory.wrap(uppedContent);
                            }));
                        }
                        else
                        {
                            return chain.filter(exchange);
                        }
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);
        }
        catch (Exception e)
        {
            log.error(" ReplaceNullFilter 异常", e);
            return chain.filter(exchange);
        }
    }
}
