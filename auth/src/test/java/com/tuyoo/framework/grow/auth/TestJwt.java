package com.tuyoo.framework.grow.auth;

import com.tuyoo.framework.grow.auth.bean.KeystoreConfig;
import net.minidev.json.JSONObject;
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
import java.util.HashMap;
import java.util.Map;

@Ignore
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
//        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk2ULSmOH2ZdkYFeUCm7wKHolHfp36NVJogt0ZucT/DLp4PVEYdBtl8JrTVCoi4CToPfTOXvKvsXm5Cbt/OQfEWA92HXSv2VJRx55kqTswh0qsATKBXqH2XQRinkuxrYOV9KDYf4hHpXTjVHtirTG4LDF91FFXMwxzvyx/8g0ZfEGOzA6O/YMz3teGLjbMqbWP/cYyvagIS6fKQvy6vmFh1+azj/YkwFIKnZcqf4+fz6d4FMMKjXf5BcCF9DxiSxc65n4myIpsEvdvwDjucEG24rxW1YAKyBfiDDrND7lCTM/jHCx/rI/lLi6PUDx/R1AaTrRLUCATaFOS+73PCXAWQIDAQAB-----END PUBLIC KEY-----";
//        String publickey = "-----BEGIN PUBLIC KEY----- MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA63RCCWPGq01eiBJIhMlP 1IAxtfPg2bjQHH6zbkjnyAouit6WUf2dpppQs50WkCjrkdjMBLpOznuOUYImoQgG qLU7JN0kM5bkL5bG86op0TEbLtjZDKM+ZX8YJ5TpKhjgMgpISkbKpSpbhKHeu0Kt DtQ6w/ihZsNvMSAfWTWcsrZ8ZAz3bvkCUCSOS26XahoDQmjtao1ghj0cNjTz1os0 S0+ZQFJ/tO8RaEflHuMlsscGftsZiG/JD2c8E10i8TjSCCGWwHX5MDk81klDBrcb iallJZkvqyxqsfcn+YInQUmfaPHQ57MHce8oL78J+Z48VDQGU/T8cBAzUhY6Ijg0 qwIDAQAB-----END PUBLIC KEY-----";
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk2ULSmOH2ZdkYFeUCm7wKHolHfp36NVJogt0ZucT/DLp4PVEYdBtl8JrTVCoi4CToPfTOXvKvsXm5Cbt/OQfEWA92HXSv2VJRx55kqTswh0qsATKBXqH2XQRinkuxrYOV9KDYf4hHpXTjVHtirTG4LDF91FFXMwxzvyx/8g0ZfEGOzA6O/YMz3teGLjbMqbWP/cYyvagIS6fKQvy6vmFh1+azj/YkwFIKnZcqf4+fz6d4FMMKjXf5BcCF9DxiSxc65n4myIpsEvdvwDjucEG24rxW1YAKyBfiDDrND7lCTM/jHCx/rI/lLi6PUDx/R1AaTrRLUCATaFOS+73PCXAWQIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
//        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODc0NDQxMjgsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiI5YTZlZjU3MC0zOTJiLTQxZGUtYTkzYi1hMmIyNjY2NWE5MzIiLCJjbGllbnRfaWQiOiJuZXQ1aWp5Iiwic2NvcGUiOlsiYWxsIiwicmVhZCIsIndyaXRlIl19.NRGJYgEuOe3L2YcxhsftE2gaXqJgi7oEx31aPftS05ryedh2Xl9orGzuiWJcCFXGxZNvi6UKYIJmwAIw70GOV5tDPttHLVzAXQclaQtdwMPDTEkaVX5FNkICEulxy48dwedg6awyPhQa3p9NhVh1AJGdYSs6KrHyB-sNhjdx3w-fsvS7NThOAA6PqT8GtEJxpX-BmcKOq3lSBp6wi2QwTvKrDHxb8C-sruwmQydnGl_g8ET_ao7h5Pgmz7-YavgYWf9P1xZdfkNfIkdOifIlaBHBfyiiBbKJ2fQN39zJRXdhHFPPpmwGfNfD34ehXSpYJfVHiqt_VoGX8zAHsPeP9A";
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTk1NTU2NjgsInVzZXJfbmFtZSI6ImxpdXlhbmdAdHV5b29nYW1lLmNvbSIsImF1dGhvcml0aWVzIjpbIlBST0pFQ1RfMjAzOTQiLCJST0xFX0dBX1dFQiIsIlBST0pFQ1RfMjA0NjIiXSwianRpIjoiYzliYzExZDYtYWExYS00MDAzLWIzYmYtNjZjYWY4ZGRhM2UyIiwiY2xpZW50X2lkIjoibmV0NWlqeSIsInNjb3BlIjpbImFsbCJdfQ.ToZ_uOFdkuAFZY0G_v4b1-0izoV5YH6MkCAuhCSaVel3nQWvnLW4hhCRnHqNd9fduRqM-G3sLu7LWkvnGbd3pJmHS7voPEORv2ztHOQkZtBRhxzwFn4wakGg9f5emBXYOMwqwmuGMLPPo9ZmOzCRIv6D5B1-2RsYZPXeCwJySt_CUAYJzLUnZENpqkELCC_Ue1Ge3bMmqHywrPbFh9gcFPlKFFSXw_hkMa-du_KUDWvweQbcGe9suJfrfRl9MMrFY_ToteQj6_lOvJzRrO9vYZTKnLxDrFVcc4a3ZeJpsCR81WR0j4u95Id15E2NlRDn5ebAfrF4f4cNQ18rT9yapw";
        //校验jwt令牌
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        //拿到jwt令牌中自定义的内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
