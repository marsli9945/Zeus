package com.tuyoo.framework.grow.nacosProvider.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/config")
@RefreshScope // 支持nacos的动态刷新功能
public class ConfigController
{
    @Value("${config.info}")
    private String info;

    @ResponseBody
    @GetMapping(value = "/get")
    public String get() {
        System.out.println("访问进来了**********************************");
        HashMap<String, Object> map = new HashMap<>();
        map.put("info", info);
        return JSON.toJSONString(map);
    }
}
