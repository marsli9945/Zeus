package com.tuyoo.framework.grow.auth.config;

import com.tuyoo.framework.grow.auth.bean.KeystoreConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;

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

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }



    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
    {
        // 数据库管理access_token和refresh_token
        TokenStore tokenStore = jwtTokenStore();

        endpoints
                .userDetailsService(userDetailsService) // 用户信息查询服务
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore) // token存入mysql
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
        return new JwtTokenStore(accessTokenConverter());
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
