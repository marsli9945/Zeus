package com.tuyoo.framework.grow.security.controller;

import com.tuyoo.framework.grow.common.entities.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController
{
    @GetMapping("hello")
    public CommonResult<String> hello() {
        return CommonResult.success("hello");
    }

    @GetMapping("/admin/hello")
    public CommonResult<String> admin() {
        return CommonResult.success("admin");
    }

    @GetMapping("/user/hello")
    public CommonResult<String> user() {
        return CommonResult.success("user");
    }
}
