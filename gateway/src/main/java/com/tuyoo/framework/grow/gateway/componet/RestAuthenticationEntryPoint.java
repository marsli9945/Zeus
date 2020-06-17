package com.tuyoo.framework.grow.gateway.componet;

import com.alibaba.fastjson.JSON;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 当未登录或者token失效访问接口时，自定义的返回结果
 * Created by macro on 2018/5/14.
 */
@Component
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint
{
    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e)
    {
        ServerHttpResponse response = serverWebExchange.getResponse();

        // 设置头信息，防止中文乱码
        HttpHeaders headers = response.getHeaders();
        headers.add("Content-Type", "text/html; charset=UTF-8");

        // 返回自定义的统一错误内容
        String jsonString = JSON.toJSONString(ResultEntities.unauthorized(null));
        byte[] bytes = (jsonString.getBytes(StandardCharsets.UTF_8));
        DataBuffer wrap = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(wrap));
    }
}
