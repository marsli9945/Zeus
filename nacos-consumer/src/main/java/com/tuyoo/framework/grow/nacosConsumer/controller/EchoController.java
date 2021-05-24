package com.tuyoo.framework.grow.nacosConsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("echo")
public class EchoController
{
    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    public String echo() {
        System.out.println("1111");
        return restTemplate.getForObject("http://nacos-provider/config/get", String.class);
    }
}
