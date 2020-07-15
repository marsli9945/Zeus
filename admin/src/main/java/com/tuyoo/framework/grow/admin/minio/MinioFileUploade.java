package com.tuyoo.framework.grow.admin.minio;

import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Component
public class MinioFileUploade
{
    @Autowired
    private MinioConfig minioConfig;

    public String upload(MultipartFile file) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidArgumentException
    {
        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = new MinioClient(minioConfig.getHost(), minioConfig.getAccessKey(), minioConfig.getSecretKey());
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(minioConfig.getBucketName());
        if (!isExist)
        {
            // 创建一个存储桶
            minioClient.makeBucket(minioConfig.getBucketName());
        }
        String objectName = getObjectName(file.getOriginalFilename());
        // 使用putObject上传一个文件到存储桶中。
        minioClient.putObject(minioConfig.getBucketName(), objectName, file.getInputStream(), file.getSize(), file.getContentType());
        return minioConfig.getHost() + "/" + minioConfig.getBucketName() + "/" + objectName;
    }

    private String getObjectName(String fileName) {
        if (fileName == null) { return ""; }
        return UUID.randomUUID().toString().replace("-","") + "-" + fileName;
    }
}
