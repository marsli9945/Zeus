package com.tuyoo.framework.grow.auth;


import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Weixin
{
    private String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApFTEAL78BDzkhrJiIhqRAXXZztuq7+sFxvg3Obe6fB+roioZ6EbfR0JR4lXn3JRunad9kT+hEcdr9Nas9k3S9XFiwc/cAvzMA2C4UB44hUNNYxvYvUdVrL0bgYtGhqhDvTKF/DhoIn79W9EbkdjPgyH2iU4TrRW0P7J+AJ6NXg2PzmdPU+o5KsUL7222h7g5tb9llkWWF5IQh7ViiIJJyiaLBIDlV1qCPMXtgyYEaoWqIRvr+EfAok+SkX9o9TO4O+Utms4wScOAJTDDBzr1rQ/NBc1jFjer24qQZMtJK1RO9+CWMltR53Igxb+y/PiO7FoXpkO9A6KQ941K5qry0wIDAQAB-----END PUBLIC KEY-----";
    public String privateKey = "-----BEGIN RSA PRIVATE KEY-----MIIEpAIBAAKCAQEApFTEAL78BDzkhrJiIhqRAXXZztuq7+sFxvg3Obe6fB+roioZ6EbfR0JR4lXn3JRunad9kT+hEcdr9Nas9k3S9XFiwc/cAvzMA2C4UB44hUNNYxvYvUdVrL0bgYtGhqhDvTKF/DhoIn79W9EbkdjPgyH2iU4TrRW0P7J+AJ6NXg2PzmdPU+o5KsUL7222h7g5tb9llkWWF5IQh7ViiIJJyiaLBIDlV1qCPMXtgyYEaoWqIRvr+EfAok+SkX9o9TO4O+Utms4wScOAJTDDBzr1rQ/NBc1jFjer24qQZMtJK1RO9+CWMltR53Igxb+y/PiO7FoXpkO9A6KQ941K5qry0wIDAQABAoIBAF7BFceMWEqlRew5HYampOgfqTiyxyzR6qHg7A8W1qvNoyy9c/TQMNyo7AhDGo/A8strEWdYNirHP4OM6WyYUUT/XT99+FGsrIJiGcTwuAx3Uz8Lhn0VjdwtqpeW1FK+4VhNsp5NdBY3/SUdPPVq+7SUN9DSOVw7QJTd+fOneMI6yRhR6f603jctKlj8UzLSqV2zNWLuM+YCmg+broo/C8TSfWkaS4iXSEOUuRmzhlbYR6Vhrc/GQ+8fRZK3uAqV1IFM7eyeeGfohdWgpwuoTBLjdDCOgLJHy7HYXrjOzKioxTfxRphxjWnphn8AxmbUATQgjrQhktAKL/DFHj2/O6kCgYEA0JrFcxcsd6FA/7rMrmXUfhN1n7ppBISIhXw3eiDMPz5CL0pJnVXx+vlUG4YgEqiXRkXpbtJjq1AAc3X3dqmIEK7KrhkuhJgeIxzd2lnNZTT2svvqhTxEY6ZB6aOmPLSfw+bVuCRgOvfjIVzXa7dTR26N6hLvSVbASkMRQZGpwP0CgYEAyariC9EdyNOA/SUarDVxDbssc9NzVB8vErqlrf8I9CTrI1G6i67gV6kHF0Eb66GEnD/GgLAfjSwwE4Q6csOslP0ARcqAK1Xwyr5svfuTaITWVQYUvsdFEfzhFGJjCCUvbsdD55oKr3PpXb9QT57xm1u5QhO+oNmb3o/CgRk+dA8CgYEAnAPVPtGLIWHKEjksUha4yR5Wr4Kkfot865DOkRmsiHRQ3buKuRCJbTJCdF9o52uN0DIDdy4E+yBXZgNJdEcxC4Ee9vzAbMy0ZYB2cJMPae0lwRFmAgXkBD9ZNpt9fhN9FfAz0YfrLM8u4Unll5tBvr/xhappqokmQaRNaKO/uSUCgYAovQty5aiFpCw/dxpfRWB0LKZ7M3Dtwmro9ql3b7ioUTjq5pyvkQEi1DNpgC6auAMo6T05S9rj0tRXbPO9sUeQxVcjwheIKZE985/V9Rc/Gu6NOHBCZdDPJG13h7SHtbNSRpCyHY0hCmJPHNGlc+9Muge/kGuG5M629AWPvMDAAwKBgQCNzeohpFFQx7/Lv+rJW+sHlFF0ln0nrTeWnwmrPoK2c1tLo92WjuWJf8KFWwzcoALLxtkll7XlVfAM4np1Bk7gOX1xVkteIklOK5GyrU9OUUUu0PzAh3uzzY57ndA6JV1YrsvuQ0a+1njzm6NzQznSKg9MaqHgZOKlfjeUkSCTdg==-----END RSA PRIVATE KEY-----";

    @Test
    void aa() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException
    {
        String str = "aaa";
        byte[] decodeBase64 = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").
                generatePublic(new X509EncodedKeySpec(decodeBase64));

        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));

        System.out.println(outStr);
    }
}
