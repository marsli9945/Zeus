package com.tuyoo.framework.grow.auth.config;

import com.tuyoo.framework.grow.auth.bean.KeystoreConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@EnableAuthorizationServer
@Configuration
@AllArgsConstructor
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter
{

    @Autowired
    AuthenticationManager authenticationManagerBean;

    @Autowired
    DataSource dataSource;

    @Qualifier("myUserDetailsService")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    KeystoreConfig keystoreConfig;

    @Autowired
    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }



    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
    {

        endpoints
                .userDetailsService(userDetailsService) // 用户信息查询服务
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(jwtTokenStore()) // 数据库管理access_token和refresh_token
                .reuseRefreshTokens(false) //该字段设置设置refresh token是否重复使用,不能重复使用否则和redis里的内容不匹配
                .authenticationManager(authenticationManagerBean)
                .authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource)) // 数据库管理授权码
                .approvalStore(new JdbcApprovalStore(dataSource)); // 数据库管理授权信息
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
    {
        security.allowFormAuthenticationForClients();
    }

    @Bean
    public TokenStore jwtTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter()
    {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair()
    {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keystoreConfig.getKeystore()), keystoreConfig.getKeystorePassword().toCharArray());
        return keyStoreKeyFactory.getKeyPair(keystoreConfig.getAlias());
    }
}
