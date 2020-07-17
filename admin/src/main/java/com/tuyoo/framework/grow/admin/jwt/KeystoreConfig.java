package com.tuyoo.framework.grow.admin.jwt;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "keystore")
public class KeystoreConfig
{
    private String keystore;
    private String keystorePassword;
    private String alias;
    private String pubKey;
}
