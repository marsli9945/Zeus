package com.tuyoo.framework.grow.auth.controller;

import com.tuyoo.framework.grow.auth.entities.LoginEntities;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@Api(tags = "登录操作相关接口")
public class AuthController
{
    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "使用账号密码登录以获取token", response = ResultEntities.class)
    public ResultEntities<Object> login(@Valid LoginEntities loginEntities)
    {
        HashMap<String, String> result = new HashMap<>();
        result.put("username", loginEntities.getUsername());
        result.put("password", loginEntities.getPassword());
        return ResultEntities.success(result);
    }

    @RequestMapping(value = "/login2", method = RequestMethod.POST)
    @ApiOperation(value = "登录2", notes = "使用账号密码登录以获取token", response = ResultEntities.class)
    public ResultEntities<Object> login2(@RequestBody @Valid LoginEntities loginEntities)
    {
        HashMap<String, String> result = new HashMap<>();
        result.put("username", loginEntities.getUsername());
        result.put("password", loginEntities.getPassword());
        result.put("role", loginEntities.getSignEntities().getRole());
        return ResultEntities.success(result);
    }

//    @GetMapping("/division")
//    public ResultEntities<Object> division(Integer a, Integer b)
//    {
//
//    }
}
