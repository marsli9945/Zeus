package com.tuyoo.framework.grow.logserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
public class IndexController
{
    @PostMapping("/log/record")
    public ResultEntities<Object> record(@RequestBody JSONArray jsonArray)
    {
        ArrayList<String> list = (ArrayList<String>) JSON.parseArray(jsonArray.toJSONString(), String.class);
        log.info("list:{}", list);
        return ResultEntities.init(200, "success", null);
    }
}
