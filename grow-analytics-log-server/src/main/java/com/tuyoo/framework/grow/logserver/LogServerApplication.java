package com.tuyoo.framework.grow.logserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LogServerApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(LogServerApplication.class, args);
    }
}
