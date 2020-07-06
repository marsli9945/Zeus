package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.form.PageForm;
import com.tuyoo.framework.grow.admin.form.client.CreateClientForm;
import com.tuyoo.framework.grow.admin.form.client.EditClientForm;
import com.tuyoo.framework.grow.admin.service.ClientService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@Api(tags = "客户端ID管理接口")
public class ClientController
{
    @Autowired
    private ClientService clientService;

    @GetMapping
    @ApiOperation(value = "获取客户端ID列表", notes = "获取客户端ID列表接口", response = ResultEntities.class)
    public ResultEntities<Object> fetch(@Validated PageForm pageForm, String clientId)
    {
        return ResultEntities.success(clientService.fetch(pageForm.getPage(), pageForm.getSize(), clientId));
    }

    @PostMapping
    @ApiOperation(value = "添加客户端ID", notes = "添加客户端ID接口", response = ResultEntities.class)
    public ResultEntities<Object> create(@RequestBody @Validated CreateClientForm clientForm)
    {
        if (clientService.create(clientForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @PutMapping
    @ApiOperation(value = "编辑客户端ID", notes = "编辑客户端ID接口", response = ResultEntities.class)
    public ResultEntities<Object> edit(@RequestBody @Validated EditClientForm editClientForm)
    {
        if (clientService.update(editClientForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @DeleteMapping
    @ApiOperation(value = "删除客户端ID", notes = "删除客户端ID接口", response = ResultEntities.class)
    public ResultEntities<Object> delete(@RequestParam String clientId){
        if (clientService.delete(clientId))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }
}
