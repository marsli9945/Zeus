package com.tuyoo.framework.grow.api.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.tuyoo.framework.grow.api.service.IoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.commons.util.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

    @HystrixCommand(fallbackMethod = "IoCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60") //失败率达到多少后跳闸
    })
    public String IoCircuitBreaker(@PathVariable("id") Integer id)
    {
        if (id < 0)
        {
            throw new RuntimeException("id 不能为负数");
        }

        return "id:{} " + id;
    }

    public String IoCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能为负数，请稍后再试！";
    }
}
