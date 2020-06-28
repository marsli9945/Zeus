package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.service.UserService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController()
public class UserController
{
    @Autowired
    private UserService userService;

//    @GetMapping
//    public ResultEntities<Object> getList() {
//        return ResultEntities.success("this is get list");
//    }
//
//    @PostMapping
//    public ResultEntities<Object> create() {
//        return ResultEntities.success("this is create");
//    }
//
//    @PutMapping
//    public ResultEntities<Object> edit() {
//        return ResultEntities.success("this is edit");
//    }
//
//    @DeleteMapping
//    public ResultEntities<Object> delete() {
//        return ResultEntities.success("this is delete");
//    }

    @GetMapping("/findOne")
    public ResultEntities<Object> findOne() {
        UserEntities one = userService.findOne(3L);
        return ResultEntities.success(one);
    }

    @GetMapping("/save")
    public ResultEntities<Object> save() {
        UserEntities user = new UserEntities(3L,"convertor","null",0);
        String username = userService.save(user).getUsername();
        return ResultEntities.success(username);
    }

    @GetMapping("/getAll")
    public ResultEntities<Object> getAll() {
        List<UserEntities> list = userService.queryAll();
        return ResultEntities.success(list);
    }
}
