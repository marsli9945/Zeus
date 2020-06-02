package com.tuyoo.framework.grow.api.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.tuyoo.framework.grow.api.service.IoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DefaultProperties(defaultFallback = "IoFallbackMethod", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
})
public class IoServiceImpl implements IoService
{
    public static Integer counter = 0;

    @Override
    public String io0()
    {
        counter++;
        log.info("io0 counter:{}" + counter);
        return "this is io0";
    }

    @Override
    public String io500() throws InterruptedException
    {
        Thread.sleep(500);
        counter++;
        log.info("io500 counter:{}" + counter);
        return "this is io500";
    }

    @Override
    public String io1000() throws InterruptedException
    {
        Thread.sleep(1000);
        counter++;
        log.info("io1000 counter:{}" + counter);
        return "this is io1000";
    }

    @Override
    public String io3000() throws InterruptedException
    {
        Thread.sleep(3000);
        counter++;
        log.info("io3000 counter:{}" + counter);
        return "this is io3000";
    }

    @Override
    @HystrixCommand
    public String io5000() throws InterruptedException
    {
        Thread.sleep(5000);
        counter++;
        log.info("io5000 counter:{}" + counter);
        return "this is io5000";
    }

    public String IoFallbackMethod()
    {
        counter++;
        log.info("IoFallbackMethod counter:{}" + counter);
        return "8001处理超时：this is IoFallbackMethod";
    }
}
