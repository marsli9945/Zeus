package com.tuyoo.framework.grow.gateway.filter;

import com.alibaba.fastjson.JSON;
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
                                String rowLen = String.valueOf(content.length);
                                log.info("rawStatusCode:{}", rawStatusCode);
                                log.info("rowLen:{}", rowLen);

                                log.info("响应转前:{}", responseData);
                                if (rawStatusCode != null && rawStatusCode >= 200 && rawStatusCode < 300)
                                {
                                    try
                                    {
                                        responseData = JSON.toJSONString(ResultEntities.init(
                                                rawStatusCode,
                                                "操作成功",
                                                JSON.parse(responseData)
                                        ));
                                        log.info("is json");
                                    } catch (Exception e)
                                    {
                                        responseData = JSON.toJSONString(ResultEntities.init(
                                                rawStatusCode,
                                                "操作成功",
                                                responseData
                                        ));
                                        log.info("not json");
                                    }
                                }
                                else
                                {
                                    responseData = JSON.toJSONString(ResultEntities.init(
                                            rawStatusCode,
                                            responseData,
                                            null
                                    ));
                                    log.info("no success");

                                    // 返回的httpCode全为200，异常状态吗内置到code
                                    originalResponse.setRawStatusCode(200);
                                }
                                log.info("响应转后:{}", responseData);

                                // 重新设置content-length避免body被截断
                                String resultLen = String.valueOf(responseData.getBytes().length);
                                originalResponse.getHeaders().set("Content-length", resultLen);
                                log.info("response path:{}", exchange.getRequest().getPath().toString());
                                log.info("resultLen:{}", resultLen);

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
        } catch (Exception e)
        {
            log.error(" ReplaceNullFilter 异常", e);
            return chain.filter(exchange);
        }
    }
}
