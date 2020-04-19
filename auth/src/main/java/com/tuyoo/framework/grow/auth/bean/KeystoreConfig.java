package com.tuyoo.framework.grow.auth.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keystore")
public class KeystoreConfig
{
    private String keystore;
    private String keystorePassword;
    private String alias;

    @Override
    public String toString()
    {
        return "KeystoreConfig{" +
                "keystore='" + keystore + '\'' +
                ", keystorePassword='" + keystorePassword + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }

    public String getKeystore()
    {
        return keystore;
    }

    public void setKeystore(String keystore)
    {
        this.keystore = keystore;
    }

    public String getKeystorePassword()
    {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword)
    {
        this.keystorePassword = keystorePassword;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }
}
