package com.tuyoo.framework.grow.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
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
public class ResponseLogGatewayFilter implements GlobalFilter, Ordered
{
    @Override
    public int getOrder()
    {
        return -100;
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

                                // 设置跨域
                                originalResponse.getHeaders().set("Access-Control-Allow-Origin", "*");
                                originalResponse.getHeaders().set("Access-Control-Allow-Methods", "*");
                                originalResponse.getHeaders().set("Access-Control-Allow-Headers", "*");

                                HashMap<String, Object> jsonLog = new HashMap<>();
                                jsonLog.put("response_path", exchange.getRequest().getPath().toString());
                                jsonLog.put("search_user", exchange.getRequest().getHeaders().getFirst("search_user"));
                                jsonLog.put("rawStatusCode", rawStatusCode);
                                jsonLog.put("result", responseData);
                                jsonLog.put("headers:{}", originalResponse.getHeaders());

                                // 重新设置content-length避免body被截断
                                String resultLen = String.valueOf(responseData.getBytes().length);
                                originalResponse.getHeaders().set("Content-length", resultLen);
                                if (!exchange.getRequest().getPath().toString().equals("/v1/grow-analytics-log-server/log/send"))
                                {
                                    log.info("responseLog:{}", jsonLog);
                                }

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
