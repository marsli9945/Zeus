package com.tuyoo.framework.grow.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * request 日志记录
 */
@Slf4j
@Component
public class RequestLogGatewayFilter implements GlobalFilter, Ordered
{

    @Override
    public Mono filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        //POST和get的处理逻辑不一样
        if ("POST".equals(exchange.getRequest().getMethodValue()))
        {
            ServerRequest serverRequest = new DefaultServerRequest(exchange);

            Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(requestBody ->
            {
                //打印请求报文
                log.info("requestBody:{}", requestBody);

                return Mono.just(requestBody);
            });

            BodyInserter<Mono<String>, org.springframework.http.ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);



            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());

            // the new content type will be computed by bodyInserter
            // and then set in the request decorator
            headers.remove(HttpHeaders.CONTENT_LENGTH);

            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);


            return bodyInserter.insert(outputMessage, new BodyInserterContext())
                    // .log("modify_request", Level.INFO)
                    .then(Mono.defer(() ->
                    {
                        ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
                        return chain.filter(exchange.mutate().request(decorator).build());
                    }));

        }
        else
        {
            //打印请求报文
            chain.filter(exchange);
        }
        return chain.filter(exchange);
    }

    ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage)
    {
        return new ServerHttpRequestDecorator(exchange.getRequest())
        {
            @Override
            public HttpHeaders getHeaders()
            {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0)
                {
                    httpHeaders.setContentLength(contentLength);
                }
                else
                {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody()
            {
                return outputMessage.getBody();
            }
        };
    }

    @Override
    public int getOrder()
    {
        return 0;
    }
}
