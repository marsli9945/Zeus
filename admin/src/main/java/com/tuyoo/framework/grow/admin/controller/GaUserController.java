package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.form.PageForm;
import com.tuyoo.framework.grow.admin.ga.GaConfig;
import com.tuyoo.framework.grow.admin.ga.entities.GaSelectEntities;
import com.tuyoo.framework.grow.admin.ga.entities.GaStudioEntities;
import com.tuyoo.framework.grow.admin.ga.entities.GaUserInfoEntities;
import com.tuyoo.framework.grow.admin.ga.form.GaUserForm;
import com.tuyoo.framework.grow.admin.ga.service.GaUserService;
import com.tuyoo.framework.grow.admin.service.UserService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("ga/user")
@Api(tags = "GA用户管理接口")
public class GaUserController
{
    @Autowired
    private GaUserService gaUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private GaConfig gaConfig;

    @GetMapping
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表接口", response = ResultEntities.class)
    public ResultEntities<Object> fetch(@Valid PageForm pageForm, String name)
    {
        return ResultEntities.success(gaUserService.fetch(pageForm.getPage(), pageForm.getSize(), name));
    }

    @GetMapping("userInfo")
    @ApiOperation(value = "获取当前用户的详细信息含游戏和权限", notes = "当前用户的详细信息接口", response = GaUserInfoEntities.class)
    public ResultEntities<Object> userInfo()
    {
        return ResultEntities.success(gaUserService.userInfo());
    }

    @GetMapping("getPermission")
    @ApiOperation(value = "获取当前用户可分配的所有权限", notes = "获取权限列表接口", response = GaStudioEntities.class)
    public ResultEntities<Object> getPermission(String username)
    {
        if (username != null)
        {
            GaUserForm gaUserForm = new GaUserForm();
            UserEntities user = userService.findByUsernameAndStatusAndRoleEntitiesList(
                    username,
                    1,
                    new RoleEntities(gaConfig.getRoleId(), null)
            );
            if (user == null) {
                return ResultEntities.failed("用户还未注册");
            }
            gaUserForm.setName(user.getName());
            gaUserForm.setUsername(user.getUsername());
            gaUserForm.setLevel(user.getLevel());
            gaUserForm.setPermission(gaUserService.getPermission(username));
            return ResultEntities.success(gaUserForm);
        }
        return ResultEntities.success(gaUserService.getPermission(username));
    }

    @GetMapping("getStudioSelect")
    @ApiOperation(value = "获取当前用户可接入游戏的所有工作室下拉框数据", notes = "获取用户可接入游戏的所有工作室接口", response = GaSelectEntities.class)
    public ResultEntities<Object> getStudioSelect()
    {
        return ResultEntities.success(gaUserService.getStudioSelect());
    }

    @PostMapping
    @ApiOperation(value = "添加用户", notes = "添加用户接口", response = ResultEntities.class)
    public ResultEntities<Object> create(@RequestBody @Valid GaUserForm gaUserForm)
    {
        if (gaUserService.create(gaUserForm))
        {
            // 发注册邮件
            gaUserService.sendSignEmail(gaUserForm.getUsername());
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @GetMapping("resend")
    @ApiOperation(value = "注册邮件重新发送", notes = "注册邮件重发接口", response = ResultEntities.class)
    public ResultEntities<Object> resend(@RequestParam String username)
    {
        gaUserService.sendSignEmail(username);
        return ResultEntities.success();
    }

    @PutMapping
    @ApiOperation(value = "编辑用户", notes = "编辑用户接口", response = ResultEntities.class)
    public ResultEntities<Object> edit(@RequestBody @Valid GaUserForm gaUserForm)
    {
        if (gaUserService.update(gaUserForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户", notes = "删除用户接口", response = ResultEntities.class)
    public ResultEntities<Object> delete(@RequestParam String username)
    {
        if (gaUserService.delete(username))
        {
            // 清理处理后没有权限记录的用户
            gaUserService.clearNoPermissionUser(username);
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }
}
