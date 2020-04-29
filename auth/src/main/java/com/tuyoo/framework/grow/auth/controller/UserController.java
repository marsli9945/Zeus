package com.tuyoo.framework.grow.auth.controller;

import com.tuyoo.framework.grow.auth.entities.UserEntities;
import com.tuyoo.framework.grow.auth.form.UserForm;
import com.tuyoo.framework.grow.auth.repository.UserRepository;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

        return ResultEntities.success(userRepository.save(userForm.entities()));
    }

    @PutMapping
    @ApiOperation(value = "修改用户", notes = "修改用户信息接口", response = ResultEntities.class)
    public ResultEntities<UserEntities> editUser(@RequestBody @Validated UserForm userForm)
    {
        UserEntities user = userRepository.findByUsername(userForm.getUsername());
        if (user == null)
        {
            return ResultEntities.failed("该用户还未注册");
        }

        UserEntities userEntities = userForm.entities();
        userEntities.setId(user.getId());

        return ResultEntities.success(userRepository.save(userEntities));
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户", notes = "删除用户信息接口", response = ResultEntities.class)
    @ApiImplicitParam(name = "username", type = "String", value = "用户账号", required = true, paramType = "query")
    public ResultEntities<String> delUser(String username)
    {
        UserEntities user = userRepository.findByUsername(username);
        if (user == null)
        {
            return ResultEntities.failed("该用户还未注册");
        }

        try
        {
            userRepository.deleteById(user.getId());
            return ResultEntities.success(null);
        }
        catch (Exception e)
        {
            return ResultEntities.failed("删除失败");
        }
    }
}
