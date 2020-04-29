package com.tuyoo.framework.grow.auth.controller;

import com.tuyoo.framework.grow.auth.entities.UserEntities;
import com.tuyoo.framework.grow.auth.form.UserForm;
import com.tuyoo.framework.grow.auth.repository.UserRepository;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户信息相关接口")
public class UserController
{
    @Autowired
    UserRepository userRepository;

    @PostMapping
    @ApiOperation(value = "添加用户", notes = "添加用户接口", response = ResultEntities.class)
    public ResultEntities<UserEntities> addUser(@RequestBody @Validated UserForm userForm)
    {
        UserEntities user = userRepository.findByUsername(userForm.getUsername());
        if (user != null)
        {
            return ResultEntities.failed("用户已存在");
        }

        UserEntities save = userRepository.save(userForm.entities());
        return ResultEntities.success(save);
    }
}
