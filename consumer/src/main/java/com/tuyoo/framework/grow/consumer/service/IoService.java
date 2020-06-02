package com.tuyoo.framework.grow.consumer.service;

import com.tuyoo.framework.grow.common.entities.ResultEntities;
import com.tuyoo.framework.grow.consumer.service.fallback.IoFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "API", fallback = IoFallbackService.class)
public interface IoService
{
    @GetMapping("/io0")
    public ResultEntities<String> io0();

    @GetMapping("/io500")
    public ResultEntities<String> io500();

    @GetMapping("/io1000")
    public ResultEntities<String> io1000();

    @GetMapping("/io3000")
    public ResultEntities<String> io3000();

    @GetMapping("/io5000")
    public ResultEntities<String> io5000();
}
