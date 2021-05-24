package com.tuyoo.framework.grow.nacosProvider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("provider")
public class ProviderController
{
    @GetMapping("echo")
    public String echo() {
        return "this is provider echo";
    }
}
