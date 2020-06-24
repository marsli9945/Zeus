package com.tuyoo.framework.grow.auth.service.impl;

import com.tuyoo.framework.grow.auth.service.IoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IoServiceImpl implements IoService
{
    public static Integer counter = 0;

    @Override
    public String io0()
    {
        counter++;
        log.info("io0 counter:{}" + counter.toString());
        return "this is io0";
    }

    @Override
    public String io200() throws InterruptedException
    {
        Thread.sleep(200);
        counter++;
        log.info("io200 counter:{}" + counter.toString());
        return "this is io200";
    }

    @Override
    public String io500() throws InterruptedException
    {
        Thread.sleep(500);
        counter++;
        log.info("io500 counter:{}" + counter.toString());
        return "this is io500";
    }

    @Override
    public String io1000() throws InterruptedException
    {
        Thread.sleep(1000);
        counter++;
        log.info("io1000 counter:{}" + counter.toString());
        return "this is io1000";
    }

    @Override
    public String io3000() throws InterruptedException
    {
        Thread.sleep(3000);
        counter++;
        log.info("io3000 counter:{}" + counter.toString());
        return "this is io3000";
    }

    @Override
    public String io5000() throws InterruptedException
    {
        Thread.sleep(5000);
        counter++;
        log.info("io5000 counter:{}" + counter.toString());
        return "this is io5000";
    }
}
