package com.tuyoo.framework.grow.api.controller;

import com.tuyoo.framework.grow.common.entities.ResultEntities;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class ApiController
{

    @RequestMapping("/user")
    public ResultEntities<String> user()
    {
        return ResultEntities.success("this is user");
    }

    @RequestMapping("/api")
    public ResultEntities api()
    {
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", "Jack");
        data.put("age", 22);
        return ResultEntities.success(data);
    }


}
