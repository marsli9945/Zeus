package com.tuyoo.framework.grow.auth.controller;

import com.tuyoo.framework.grow.auth.service.IoService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IoController
{
    @Autowired
    private IoService ioService;

    @GetMapping("/io0")
    public ResultEntities<String> io0()
    {
        return ResultEntities.success(ioService.io0());
    }

    @GetMapping("/io200")
    public ResultEntities<String> io200() throws InterruptedException
    {
        return ResultEntities.success(ioService.io200());
    }

    @GetMapping("/io500")
    public ResultEntities<String> io500() throws InterruptedException
    {
        return ResultEntities.success(ioService.io500());
    }

    @GetMapping("/io1000")
    public ResultEntities<String> io1000() throws InterruptedException
    {
        return ResultEntities.success(ioService.io1000());
    }

    @GetMapping("/io3000")
    public ResultEntities<String> io3000() throws InterruptedException
    {
        return ResultEntities.success(ioService.io3000());
    }

    @GetMapping("/io5000")
    public ResultEntities<String> io5000() throws InterruptedException
    {
        return ResultEntities.success(ioService.io5000());
    }
}
