package com.tuyoo.framework.grow.auth;

import com.tuyoo.framework.grow.auth.bean.KeystoreConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

@SpringBootTest
public class TestJks
{
    @Autowired
    KeystoreConfig keystore;

    @Test
    public void jks() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("demojwt.jks"), "keystorepass".toCharArray());
        System.out.println(keyStoreKeyFactory.getKeyPair("jwt", "keypairpass".toCharArray()));
    }

    @Test
    public void keystore() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("ga.keystore"), "GaKeystore".toCharArray());
        System.out.println(keyStoreKeyFactory.getKeyPair("gakey", "Tuyoogame123".toCharArray()));
    }

    @Test
    public void ga(){
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keystore.getKeystore()), keystore.getKeystorePassword().toCharArray());
        System.out.println(keyStoreKeyFactory.getKeyPair(keystore.getAlias()));
    }

    @Test
    public void config() {
        // $2a$10$9dFyv1Gr3/.1C.VdevQo4uN.jWZ/zp1VIJc3kFV9EXStVjue8fjBW
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }
}
