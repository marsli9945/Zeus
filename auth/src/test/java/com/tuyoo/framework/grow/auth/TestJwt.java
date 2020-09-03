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
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTkxMTExMDIsInVzZXJfbmFtZSI6ImJtQHR1eW9vZ2FtZS5jb20iLCJhdXRob3JpdGllcyI6WyJST0xFX0FsbF9QUk9KRUNUIl0sImp0aSI6ImZhNDQzMzI5LTE3NDUtNDU2ZC1hMmQxLWYwM2JkODgyZDJkYiIsImNsaWVudF9pZCI6IndlYi1xRjQqY0JRYiIsInNjb3BlIjpbImZlYXR1cmUiLCJhZG1pbiJdfQ.b6DrpK_GUwUhxd9ZuouLrRuAj0V12i3xYttORh42SpEYcwbpG0_aqAt5Eq7zOeGTMDcju-piXdJAS5a4zwSudRp26ASx7odVOxzi7bEjA_nx8t8s7N7fiV0cldTKlxZpvjFdT86POubG-47qckVB2pYOaF_dpLSjxSl3tpwfTjLsLK-PBzr6Yu-8xBDcGm79fW4K9E4dpLnIIU6z_f_Bi4AgppknKFfphY9nNfFl8t-64P3bp2BhjYTUWhUdDB_MOBMOAMS0X4M0uux6sSYwAdDMNp03k2BL-gdWgeqQ58vCMMVAtyKWugJs9fGkBs5NKcg1wXijLvcuN6d7tkY9-w";
        //校验jwt令牌
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        //拿到jwt令牌中自定义的内容
        String claims = jwt.getClaims();
        System.out.println(claims);
    }
}
