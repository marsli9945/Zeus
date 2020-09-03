package com.tuyoo.framework.grow.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.form.LoginForm;
import com.tuyoo.framework.grow.admin.form.user.EditUserForm;
import com.tuyoo.framework.grow.admin.ga.GaConfig;
import com.tuyoo.framework.grow.admin.ga.service.GaUserService;
import com.tuyoo.framework.grow.admin.service.AuthService;
import com.tuyoo.framework.grow.admin.service.UserService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("ga/system")
@Api(tags = "GA登陆、注册相关接口")
public class GaController
{
    @Autowired
    GaConfig gaConfig;

    @Resource
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    GaUserService gaUserService;

    @PostMapping("/login")
    @ApiOperation(value = "登陆", notes = "登陆接口", response = ResultEntities.class)
    public ResultEntities<Object> login(@RequestBody @Valid LoginForm loginForm)
    {
        // 查询用户名匹配切拥有GA_WEB角色状态为开的用户是否存在
        UserEntities user = userService.findByUsernameAndStatusAndRoleEntitiesList(
                loginForm.getUsername(),
                1,
                new RoleEntities(gaConfig.getRoleId(), null)
        );

        // 用户存在且密码正确进行下一步
        if (user == null || !new BCryptPasswordEncoder().matches(loginForm.getPassword(), user.getPassword()))
        {
            return ResultEntities.failed("账号或密码错误");
        }

        // 用auth服务登陆接口获取access_token
        loginForm.setClientId(gaConfig.getClientId());
        loginForm.setClientSecret(gaConfig.getClientSecret());
        return authService.login(loginForm);
    }

    @PostMapping("sign")
    @ApiOperation(value = "注册", notes = "注册接口", response = ResultEntities.class)
    public ResultEntities<Object> sign(@RequestBody @Validated EditUserForm editUserForm)
    {
        editUserForm.setStatus(1); // 开启用户状态
        if (userService.update(editUserForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @GetMapping("loginOut")
    @ApiOperation(value = "登出", notes = "登出接口", response = ResultEntities.class)
    public ResultEntities<Object> loginOut()
    {
        return ResultEntities.success();
    }

    @GetMapping("reset")
    @ApiOperation(value = "重置密码", notes = "重置密码邮件发送接口", response = ResultEntities.class)
    public ResultEntities<Object> mail(@RequestParam String username)
    {
        if (gaUserService.sendResetEmail(username))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed("该账号还未注册为有效用户，请进行核实");
    }

    @GetMapping("token")
    @ApiOperation(value = "校验token", notes = "token校验接口", response = ResultEntities.class)
    public ResultEntities<Object> token(@RequestParam String token)
    {
        UserEntities userEntities = gaUserService.validToken(token);
        if (userEntities == null)
        {
            return ResultEntities.failed("token校验失败，已超出有效期");
        }
        return ResultEntities.success(userEntities);
    }

    @GetMapping("pass")
    public String pass(@RequestParam String password)
    {
        return new BCryptPasswordEncoder().encode(password);
    }
}
