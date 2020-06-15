package com.tuyoo.framework.grow.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class ApiController
{
    @GetMapping("/testA")
    public String testA()
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return "this is testA";
    }

    @GetMapping("/testB")
    public String testB()
    {
        log.info(Thread.currentThread().getName() + "\t" + "testB");
        return "this is testB";
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
    public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
                             @RequestParam(value = "p2", required = false) String p2)
    {
        return "this is testHotKey";
    }

    public String deal_testHotKey(String p1, String p2, BlockException e)
    {
        return "this is deal_testHotKey";
    }
}
