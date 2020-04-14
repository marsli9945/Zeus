package com.tuyoo.framework.grow.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * 局部过滤
 * 角色过滤器
 */
@Slf4j
@Component
public class AuthGatewayFilter implements GatewayFilter, Ordered
{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        log.info("********************* this is auth filter");
//        String token = exchange.getResponse().getHeaders().getFirst("ga-token");
//
//        if (token == null)
//        {
//            log.info("********************** 没有token " + new Date());
//            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//            return exchange.getResponse().setComplete();
//        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder()
    {
        return 1;
    }
}
