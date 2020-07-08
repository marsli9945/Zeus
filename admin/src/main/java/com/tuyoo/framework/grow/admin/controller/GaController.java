package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.form.LoginForm;
import com.tuyoo.framework.grow.admin.form.user.EditUserForm;
import com.tuyoo.framework.grow.admin.service.AuthService;
import com.tuyoo.framework.grow.admin.service.UserService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("ga")
@Api(tags = "GA登陆、注册相关接口")
public class GaController
{
    @Value("${ga.roleId}")
    private Integer roleId;

    @Value("${ga.clientId}")
    private String clientId;

    @Value("${ga.clientSecret}")
    private String clientSecret;

    @Resource
    AuthService authService;

    @Autowired
    UserService userService;

    @GetMapping("/login")
    @ApiOperation(value = "登陆", notes = "登陆接口", response = ResultEntities.class)
    public ResultEntities<Object> login(LoginForm loginForm)
    {
        // 查询用户名匹配切拥有GA_WEB角色状态为开的用户是否存在
        UserEntities user = userService.findByUsernameAndStatusAndRoleEntitiesList(
                loginForm.getUsername(),
                1,
                new RoleEntities(roleId, null)
        );

        // 用户存在且密码正确进行下一步
        if (user == null || !new BCryptPasswordEncoder().matches(loginForm.getPassword(), user.getPassword()))
        {
            return ResultEntities.failed("账号或密码错误");
        }

        // 用auth服务登陆接口获取access_token
        loginForm.setClientId(clientId);
        loginForm.setClientSecret(clientSecret);
        return authService.login(loginForm);
    }

    @PostMapping("sign")
    @ApiOperation(value = "注册", notes = "注册接口", response = ResultEntities.class)
    public ResultEntities<Object> sign(@RequestBody @Validated EditUserForm editUserForm)
    {
        if (userService.update(editUserForm)) {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @GetMapping("loginOut")
    @ApiOperation(value = "登出", notes = "登出接口", response = ResultEntities.class)
    public ResultEntities<Object> loginOut() {
        return ResultEntities.success();
    }
}
