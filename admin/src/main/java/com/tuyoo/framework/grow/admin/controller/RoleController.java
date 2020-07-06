package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.form.PageForm;
import com.tuyoo.framework.grow.admin.form.role.CreateRoleForm;
import com.tuyoo.framework.grow.admin.form.role.EditRoleForm;
import com.tuyoo.framework.grow.admin.service.RoleService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@Api(tags = "角色管理接口")
public class RoleController
{
    @Autowired
    private RoleService roleService;

    @GetMapping
    @ApiOperation(value = "获取角色列表", notes = "获取角色列表接口", response = ResultEntities.class)
    public ResultEntities<Object> fetch(@Validated PageForm pageForm, String name)
    {
        return ResultEntities.success(roleService.fetch(pageForm.getPage(), pageForm.getSize(), name));
    }

    @PostMapping
    @ApiOperation(value = "添加角色", notes = "添加角色接口", response = ResultEntities.class)
    public ResultEntities<Object> create(@RequestBody @Validated CreateRoleForm createRoleForm)
    {
        if (roleService.create(createRoleForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @PutMapping
    @ApiOperation(value = "编辑角色", notes = "编辑角色接口", response = ResultEntities.class)
    public ResultEntities<Object> edit(@RequestBody @Validated EditRoleForm editRoleForm)
    {
        if (roleService.update(editRoleForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @DeleteMapping
    @ApiOperation(value = "删除角色", notes = "删除角色接口", response = ResultEntities.class)
    public ResultEntities<Object> delete(@RequestParam Integer id){
        if (roleService.delete(id))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }
}
