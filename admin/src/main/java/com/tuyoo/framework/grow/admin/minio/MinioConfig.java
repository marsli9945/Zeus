package com.tuyoo.framework.grow.admin.minio;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig
{
    private String host;
    private String accessKey;
    private String secretKey;
    private String bucketName;

}
