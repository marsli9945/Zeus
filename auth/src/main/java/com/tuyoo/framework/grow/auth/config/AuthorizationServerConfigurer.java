package com.tuyoo.framework.grow.auth.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;

@EnableAuthorizationServer
@Configuration
@AllArgsConstructor
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter
{

    private final AuthenticationManager authenticationManagerBean;
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;

    //客户端配置
    @Bean
    public ClientDetailsService clientDetails()
    {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients.jdbc(this.dataSource).clients(this.clientDetails());
//        clients.inMemory()
//                .withClient("test-client")
//                .secret(passwordEncoder.encode("test-secret"))
//                .authorizedGrantTypes("refresh_token", "password")
//                .scopes("default-scope");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
    {
        endpoints
                .authenticationManager(authenticationManagerBean)
                .accessTokenConverter(accessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
    {
        security
                .allowFormAuthenticationForClients();
    }

    @Bean
    public AccessTokenConverter accessTokenConverter()
    {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }


    @Bean
    public KeyPair keyPair()
    {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("demojwt.jks"), "keystorepass".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "keypairpass".toCharArray());
    }
}
