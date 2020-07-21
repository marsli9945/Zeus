package com.tuyoo.framework.grow.gateway.config;

import com.tuyoo.framework.grow.gateway.componet.RestAuthenticationEntryPoint;
import com.tuyoo.framework.grow.gateway.componet.RestfulAccessDeniedHandler;
import com.tuyoo.framework.grow.gateway.componet.JwtContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 鉴权设置
 */
@EnableWebFluxSecurity
public class SecurityServerConfigurer
{

    @Autowired
    RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    JwtContextRepository jwtContextRepository;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http)
    {
        http.authorizeExchange()
                .pathMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                ).permitAll() // 允许对于网站静态资源的无授权访问
                .pathMatchers(HttpMethod.OPTIONS).permitAll() //跨域请求会先进行一次options请求
                // 令牌操作的三个接口允许匿名访问
                .pathMatchers("/v1/auth/login").permitAll()
                .pathMatchers("/v1/auth/refresh").permitAll()
                .pathMatchers("/v1/auth/signOut").permitAll()
                .pathMatchers("/v1/auth/io200NoAuth").permitAll()
                // admin服务下ga系统登陆注册相关接口允许匿名访问
                .pathMatchers("/v1/admin/ga/system/**").permitAll()
                // 系统健康检查和对外监控允许匿名
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/monitor").permitAll()
//                .pathMatchers("/**").permitAll()
                .anyExchange().authenticated();

        // 禁用csrf防止拦截get意外对请求
        http.csrf().disable();

        // 添加JWT filter
        http.securityContextRepository(jwtContextRepository);

        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        return http.build();
    }
}
