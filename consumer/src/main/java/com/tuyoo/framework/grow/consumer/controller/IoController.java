package com.tuyoo.framework.grow.consumer.controller;

import com.tuyoo.framework.grow.common.entities.ResultEntities;
import com.tuyoo.framework.grow.consumer.service.IoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class IoController
{
    @Resource
    private IoService ioService;

    @GetMapping("/io0")
    public ResultEntities<String> io0()
    {
        return ioService.io0();
    }

    @GetMapping("/io500")
    public ResultEntities<String> io500()
    {
        return ioService.io500();
    }

    @GetMapping("/io1000")
    public ResultEntities<String> io1000()
    {
        return ioService.io1000();
    }

    @GetMapping("/io3000")
    public ResultEntities<String> io3000()
    {
        return ioService.io3000();
    }

    @GetMapping("/io5000")
    public ResultEntities<String> io5000()
    {
        return ioService.io5000();
    }
}
