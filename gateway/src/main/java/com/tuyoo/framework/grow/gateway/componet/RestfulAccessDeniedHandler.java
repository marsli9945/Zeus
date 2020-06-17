package com.tuyoo.framework.grow.gateway.componet;

import com.tuyoo.framework.grow.common.entities.ResultEntities;
import com.alibaba.fastjson.JSON;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 当访问接口没有权限时，自定义的返回结果
 * Created by macro on 2018/4/26.
 */
@Component
public class RestfulAccessDeniedHandler implements ServerAccessDeniedHandler
{
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException e)
    {
        ServerHttpResponse response = serverWebExchange.getResponse();

        // 设置头信息，防止中文乱码
        HttpHeaders headers = response.getHeaders();
        headers.add("Content-Type", "text/html; charset=UTF-8");

        // 返回自定义的统一错误内容
        ResultEntities<Object> failed = ResultEntities.forbidden(null);
        String jsonString = JSON.toJSONString(failed);
        byte[] bytes = (jsonString.getBytes(StandardCharsets.UTF_8));
        DataBuffer wrap = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(wrap));
    }
}
