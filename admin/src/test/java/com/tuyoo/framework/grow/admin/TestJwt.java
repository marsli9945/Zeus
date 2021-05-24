package com.tuyoo.framework.grow.admin;

import com.alibaba.fastjson.JSON;
import com.tuyoo.framework.grow.admin.jwt.ClaimsEntities;
import com.tuyoo.framework.grow.admin.jwt.KeystoreConfig;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;

@Ignore
@SpringBootTest
public class TestJwt
{

    @Autowired
    KeystoreConfig keystoreConfig;

    //创建jwt令牌
    @Test
    void testCreateJwt(){
        //密钥库文件
        String keystore = keystoreConfig.getKeystore();
        //密钥库的密码
        String keystore_password = keystoreConfig.getKeystorePassword();

        //密钥库文件路径
        ClassPathResource classPathResource = new ClassPathResource(keystore);
        //密钥别名
        String alias  = keystoreConfig.getAlias();
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource,keystore_password.toCharArray());
        //密钥对（公钥和私钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias);
        //获取私钥
        RSAPrivateKey rSAPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        //jwt令牌的内容
        ClaimsEntities claimsEntities = new ClaimsEntities();
        claimsEntities.setUserName("mmm");
        claimsEntities.setExp("22222");
        String bodyString = JSON.toJSONString(claimsEntities);
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(bodyString, new RsaSigner(rSAPrivateKey));
        //生成jwt令牌编码
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

        //校验jwt令牌
        Jwt jwtPub = JwtHelper.decodeAndVerify(encoded, new RsaVerifier(keystoreConfig.getPubKey()));
        //拿到jwt令牌中自定义的内容
        String claims = jwtPub.getClaims();
        System.out.println(claims);
    }

    //校验jwt令牌
    @Test
    void testVerify(){
        //公钥
        String publickey = keystoreConfig.getPubKey();
        System.out.println(publickey);
        //jwt令牌
//        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODc0NDQxMjgsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiI5YTZlZjU3MC0zOTJiLTQxZGUtYTkzYi1hMmIyNjY2NWE5MzIiLCJjbGllbnRfaWQiOiJuZXQ1aWp5Iiwic2NvcGUiOlsiYWxsIiwicmVhZCIsIndyaXRlIl19.NRGJYgEuOe3L2YcxhsftE2gaXqJgi7oEx31aPftS05ryedh2Xl9orGzuiWJcCFXGxZNvi6UKYIJmwAIw70GOV5tDPttHLVzAXQclaQtdwMPDTEkaVX5FNkICEulxy48dwedg6awyPhQa3p9NhVh1AJGdYSs6KrHyB-sNhjdx3w-fsvS7NThOAA6PqT8GtEJxpX-BmcKOq3lSBp6wi2QwTvKrDHxb8C-sruwmQydnGl_g8ET_ao7h5Pgmz7-YavgYWf9P1xZdfkNfIkdOifIlaBHBfyiiBbKJ2fQN39zJRXdhHFPPpmwGfNfD34ehXSpYJfVHiqt_VoGX8zAHsPeP9A";
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDgwNTYzODksInVzZXJfbmFtZSI6InphbnpoaWtlQHR1eW9vZ2FtZS5jb20iLCJhdXRob3JpdGllcyI6WyJQUk9KRUNUXzIwMjQ5IiwiUk9MRV9HQV9XRUIiXSwianRpIjoiNTI0YTgzMWYtZTc5MC00YmI0LWFlYjItNzc4N2NmOWUwODllIiwiY2xpZW50X2lkIjoibmV0NWlqeSIsInNjb3BlIjpbImFsbCJdfQ.ek0fTv9djGvuNojmVfnBHbgYPv3WFRlvwygrvEXaVpIMPNI1grRIKh8tooZC8Gma_PBXYopGtD6Egc5xVdd0QyV1hkWEoJLnCAuJIq6MvgdYF4iDb-mnVxVQKcOeW9h4ZVNn5KmRLkCqcJS418YcrN2eLpK8Q5e08hBAeJKmoUB_7qqqiA7WCoSZlLeHLCiOMw-WIwZIvjgoXjXwydsSvFXM1o9C9n5AndWvEXGigpBRWWayfEEPXF7vusXZZw-Kr1vPcWDzBh-2NOsh6yqXXmdkcLwPlfL8hWLD3O6ayLPYRLuYut1jrhc8N8plxseMLvVUZzDmnzFj3tSzR05G0A";
        //校验jwt令牌
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        //拿到jwt令牌中自定义的内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
