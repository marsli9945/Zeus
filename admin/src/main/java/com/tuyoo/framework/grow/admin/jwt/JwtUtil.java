package com.tuyoo.framework.grow.admin.jwt;

import com.alibaba.fastjson.JSON;
import com.tuyoo.framework.grow.admin.ga.GaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.List;

@Slf4j
@Component
public class JwtUtil
{
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    GaConfig gaConfig;

    @Autowired
    KeystoreConfig keystoreConfig;

    public String encode(ClaimsEntities claimsEntities) {
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
        String bodyString = JSON.toJSONString(claimsEntities);
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(bodyString, new RsaSigner(rSAPrivateKey));
        //生成jwt令牌编码
        return jwt.getEncoded();
    }

    public ClaimsEntities decode(String jwtString) {
        //校验jwt令牌
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(keystoreConfig.getPubKey()));
        //拿到jwt令牌中自定义的内容
        return JSON.parseObject(jwt.getClaims(), ClaimsEntities.class);
    }

    public ClaimsEntities getClaims()
    {
        String claims = httpServletRequest.getHeader("claims");
        return JSON.parseObject(claims, ClaimsEntities.class);
    }

    public String getUsername() {
        return getClaims().getUserName();
    }

    public List<String> getRoleList()
    {
        return getClaims().getAuthorities();
    }

    public boolean isGaAdmin()
    {
        List<String> roleList = getRoleList();
        for (String role : roleList)
        {
            if (role.equals(gaConfig.getRoleName()))
            {
                log.info("isAdmin:{}",true);
                return true;
            }
        }
        log.info("isAdmin:{}",false);
        return false;
    }
}
