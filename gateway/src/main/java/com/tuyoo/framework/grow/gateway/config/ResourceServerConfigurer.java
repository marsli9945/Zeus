package com.tuyoo.framework.grow.gateway.config;

import com.tuyoo.framework.grow.gateway.componet.RestAuthenticationEntryPoint;
import com.tuyoo.framework.grow.gateway.componet.RestfulAccessDeniedHandler;
import com.tuyoo.framework.grow.gateway.componet.SecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 鉴权设置
 */
@EnableWebFluxSecurity
public class ResourceServerConfigurer {

    @Autowired
    RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
                .pathMatchers("/auth/**").permitAll()
                .anyExchange().authenticated();

        // 添加JWT filter
        http.securityContextRepository(securityContextRepository);

        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        return http.build();
    }
}
