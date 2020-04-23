package com.tuyoo.framework.grow.gateway.componet;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository
{
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.pubKey}")
    private String pubKey;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext securityContext)
    {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange)
    {
        ServerHttpRequest request = exchange.getRequest();
        // 头信息中取出jwt令牌,并且开头为制定字符串
        String authHeader = request.getHeaders().getFirst(tokenHeader);

        log.info("authHeader:{}" + authHeader);

        if (authHeader == null || !authHeader.startsWith(tokenHead))
        {
            return Mono.empty();
        }

        String authToken = authHeader.substring(this.tokenHead.length() + 1);// The part after "Bearer "
        log.info("authToken:{}" + authToken);
        try
        {
            //校验jwt令牌
            Jwt jwt = JwtHelper.decodeAndVerify(authToken, new RsaVerifier(pubKey));
            //拿到jwt令牌中自定义的内容
            JwtEntities parse = JSON.parseObject(jwt.getClaims(), JwtEntities.class);
            log.info("checking username:{}", parse.getUser_name());

            if (parse.getUser_name() == null)
            {
                return Mono.empty();
            }

            // 头信息中添加查询用户
            request.mutate().header("search_user", parse.getUser_name()).build();
            exchange.getAttributes().put("search_user", parse.getUser_name());

            //此处应该列出token中携带的角色表。
            List<String> roles=parse.getAuthorities();
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    parse.getUser_name(),
                    null,
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
            return Mono.just(authentication).map(SecurityContextImpl::new);
        }
        catch (Exception e)
        {
            return Mono.empty();
        }
    }
}
