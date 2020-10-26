package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.common.entities.ResultEntities;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController
{
    @PostMapping("post")
    public ResultEntities<Object> post() {
        return ResultEntities.success("111");
    }
}
