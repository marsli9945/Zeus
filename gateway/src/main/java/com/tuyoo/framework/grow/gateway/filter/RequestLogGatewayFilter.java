package com.tuyoo.framework.grow.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * request 日志记录
 */
@Component
public class RequestLogGatewayFilter implements GlobalFilter, Ordered
{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        System.out.println("********************** this is log filter" + new Date());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder()
    {
        return 0;
    }
}