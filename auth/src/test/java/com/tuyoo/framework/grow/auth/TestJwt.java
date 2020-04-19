package com.tuyoo.framework.grow.auth;

import com.tuyoo.framework.grow.auth.bean.KeystoreConfig;
import net.minidev.json.JSONObject;
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
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestJwt {

    @Autowired
    KeystoreConfig keystoreConfig;

    //创建jwt令牌
    @Test
    public void testCreateJwt(){
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
        Map<String,String> body = new HashMap<>();
        body.put("name","itcast");
//        String bodyString = JSON.toJSONString(body);
        String bodyString = JSONObject.toJSONString(body);
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(bodyString, new RsaSigner(rSAPrivateKey));
        //生成jwt令牌编码
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

    //校验jwt令牌
    @Test
    public void testVerify(){
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk2ULSmOH2ZdkYFeUCm7wKHolHfp36NVJogt0ZucT/DLp4PVEYdBtl8JrTVCoi4CToPfTOXvKvsXm5Cbt/OQfEWA92HXSv2VJRx55kqTswh0qsATKBXqH2XQRinkuxrYOV9KDYf4hHpXTjVHtirTG4LDF91FFXMwxzvyx/8g0ZfEGOzA6O/YMz3teGLjbMqbWP/cYyvagIS6fKQvy6vmFh1+azj/YkwFIKnZcqf4+fz6d4FMMKjXf5BcCF9DxiSxc65n4myIpsEvdvwDjucEG24rxW1YAKyBfiDDrND7lCTM/jHCx/rI/lLi6PUDx/R1AaTrRLUCATaFOS+73PCXAWQIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiaXRjYXN0In0.Kmpb_fchn37I8nqfWxf5PkfaWjvQK0hYHYrc1367hddh8gPGp64bg402huh-IiwRqaZ2WVvxi6dQm7X2vzLrTBRPhaE5BcEYrpRkJhHKfPRVD55tOqbdeU01WIunUsScfNlC8jXJAyW1HHyU6oyEJU995wyqSuzTlAIcVfDmHxWNAJOw20Z1-SLEvcqmGBY0pfgtU0mzyr_FCzSDeIKJ7fa_KebR9qU6wL6f33EiabnFCExNaZU7cDDWfeWJRcnuwyZtQHQResTK36SidRUjLBHESqoj-4vXgJUPtnQU2dMlWxpHUgVdQMiS0uolNFz2wTZTQe43-iim9pRd8pO7-Q";
        //校验jwt令牌
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        //拿到jwt令牌中自定义的内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
