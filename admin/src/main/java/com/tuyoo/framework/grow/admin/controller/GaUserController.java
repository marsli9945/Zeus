package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.form.PageForm;
import com.tuyoo.framework.grow.admin.ga.form.GaUserForm;
import com.tuyoo.framework.grow.admin.service.GaUserService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ga/user")
@Api(tags = "GA用户管理接口")
public class GaUserController
{
    @Autowired
    private GaUserService gaUserService;

    @GetMapping
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表接口", response = ResultEntities.class)
    public ResultEntities<Object> fetch(@Validated PageForm pageForm, String name)
    {
        return ResultEntities.success(gaUserService.fetch(pageForm.getPage(), pageForm.getSize(), name));
    }

    @PostMapping
    @ApiOperation(value = "添加用户", notes = "添加用户接口", response = ResultEntities.class)
    public ResultEntities<Object> create(@RequestBody @Validated GaUserForm gaUserForm)
    {
        if (gaUserService.create(gaUserForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @PutMapping
    @ApiOperation(value = "编辑用户", notes = "编辑用户接口", response = ResultEntities.class)
    public ResultEntities<Object> edit(@RequestBody @Validated GaUserForm gaUserForm)
    {
        if (gaUserService.update(gaUserForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户", notes = "删除用户接口", response = ResultEntities.class)
    public ResultEntities<Object> delete(@RequestParam String username){
        if (gaUserService.delete(username))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }
}
