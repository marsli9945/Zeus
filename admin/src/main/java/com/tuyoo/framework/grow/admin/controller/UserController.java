package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.common.entities.ResultEntities;
import org.springframework.web.bind.annotation.*;

@RestController("/user")
public class UserController
{
    @GetMapping
    public ResultEntities<Object> getList() {
        return ResultEntities.success("this is getlist");
    }

    @PostMapping
    public ResultEntities<Object> create() {
        return ResultEntities.success("this is create");
    }

    @PutMapping
    public ResultEntities<Object> edit() {
        return ResultEntities.success("this is edit");
    }

    @DeleteMapping
    public ResultEntities<Object> delete() {
        return ResultEntities.success("this is delete");
    }
}
