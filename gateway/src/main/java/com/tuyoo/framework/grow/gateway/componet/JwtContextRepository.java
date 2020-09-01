package com.tuyoo.framework.grow.gateway.componet;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtContextRepository implements ServerSecurityContextRepository
{
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.sdkTokenHeader}")
    private String sdkTokenHeader;

    @Value("${jwt.projectHeader}")
    private String projectHeader;

    @Value("${jwt.pubKey}")
    private String pubKey;

    @Value("${jwt.sdkPubKey}")
    private String sdkPubKey;

//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext securityContext)
    {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange)
    {
        ServerHttpRequest request = exchange.getRequest();

        // 头信息中取出jwt令牌,并判断redis中是否存在token
        String authToken = request.getHeaders().getFirst(tokenHeader);
        String sdkToken = request.getHeaders().getFirst(sdkTokenHeader);

        if (authToken == null && sdkToken == null)
        {
            return Mono.empty();
        }

        // 优先校验sdk的令牌
        if (sdkToken != null)
        {
            return checkSdkToken(request, sdkToken);
        }

        return checkToken(request, authToken);
    }

    /**
     * 校验sdk的token
     *
     * @param request   请求信息
     * @param authToken 令牌
     * @return Mono结果
     */
    private Mono<SecurityContext> checkSdkToken(ServerHttpRequest request, String authToken)
    {
        try
        {
            //校验jwt令牌
            Jwt jwt = JwtHelper.decodeAndVerify(authToken, new RsaVerifier(sdkPubKey));
            //拿到jwt令牌中自定义的内容
            JwtEntities parse = JSON.parseObject(jwt.getClaims(), JwtEntities.class);

            // 无过期时间
            if (parse.getExp() == null)
            {
                return Mono.empty();
            }

            // 超过过期时间
            if (System.currentTimeMillis() / 1000 > Integer.parseInt(parse.getExp()))
            {
                return Mono.empty();
            }

            // 头信息中添加查询用户
            request.mutate().header("search_user", "sdk").build();

            //列出token中携带的角色表。
            List<String> roles = new ArrayList<>();
            roles.add("ADMIN");
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    "sdk",
                    null,
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
            return Mono.just(authentication).map(SecurityContextImpl::new);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Mono.empty();
        }
    }

    /**
     * 校验自己的token
     *
     * @param request   请求信息
     * @param authToken 令牌
     * @return Mono结果
     */
    private Mono<SecurityContext> checkToken(ServerHttpRequest request, String authToken)
    {
        try
        {
            // todo 保证高可用目前去掉redis校验
//            Boolean hasKey = redisTemplate.hasKey("access:" + authToken);
//            assert hasKey != null;
//            log.info("hasKey:{}" + hasKey.toString());
//            if (!hasKey)
//            {
//                return Mono.empty();
//            }

            //校验jwt令牌
            Jwt jwt = JwtHelper.decodeAndVerify(authToken, new RsaVerifier(pubKey));
            //拿到jwt令牌中自定义的内容
            JwtEntities parse = JSON.parseObject(jwt.getClaims(), JwtEntities.class);
            log.info("checking username:{}", parse.getUser_name());
            log.info("jwtPath:{}" + request.getPath().toString());
            log.info("checking claims:{}", jwt.getClaims());

            // 无用户名
            if (parse.getUser_name() == null)
            {
                return Mono.empty();
            }

            // 无过期时间
            if (parse.getExp() == null)
            {
                return Mono.empty();
            }

            // 超过过期时间
            if (System.currentTimeMillis() / 1000 > Integer.parseInt(parse.getExp()))
            {
                return Mono.empty();
            }

            //scope权限校验
            String[] pathArr = request.getPath().toString().split("/");
            if (pathArr.length > 2 && !scopeValid(pathArr[2], parse.getScope()))
            {
                return Mono.empty();
            }

            // 校验游戏权限
            List<String> roles = parse.getAuthorities();
            String projectId = request.getHeaders().getFirst(projectHeader);
            log.info("projectId:{}", projectId);
            log.info("roleList:{}", roles);
            if (projectId != null && !projectId.equals(""))
            {
                if (!projectValid(projectId, roles)) {
                    return Mono.empty();
                }
            }

            // 头信息中添加查询用户
            request.mutate().header("ga_username", parse.getUser_name()).build();
            request.mutate().header("ga_request_id", getRequestId()).build();
            request.mutate().header("claims", jwt.getClaims()).build();

            //列出token中携带的角色表。
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    parse.getUser_name(),
                    null,
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
            return Mono.just(authentication).map(SecurityContextImpl::new);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Mono.empty();
        }
    }

    /**
     * 构造头信息中需要到ga_request_id
     *
     * @return ga_request_id
     */
    private String getRequestId()
    {
        return System.currentTimeMillis() + "-" + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @param projectId 游戏ID
     * @param roleList  角色列表
     * @return 是/否
     */
    private boolean projectValid(String projectId, List<String> roleList)
    {
        for (String role :
                roleList)
        {
            if (role.equals("PROJECT_" + projectId) || role.equals("ROLE_GA_ADMAIN"))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有当初访问服务的权限
     *
     * @param serverName 服务名
     * @param scopeList  用户的权限列表
     * @return 是/否
     */
    private boolean scopeValid(String serverName, List<String> scopeList)
    {
        for (String scope : scopeList)
        {
            if (scope.equals(serverName) || scope.equals("all"))
            {
                return true;
            }
        }
        return false;
    }
}
