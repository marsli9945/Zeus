package com.tuyoo.framework.grow.auth.controller;

import com.tuyoo.framework.grow.auth.entities.ClientEntities;
import com.tuyoo.framework.grow.auth.form.ClientForm;
import com.tuyoo.framework.grow.auth.repository.ClientRepository;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/client")
@Api(tags = "客户端ID相关接口")
public class ClientController
{
    @Autowired
    ClientRepository clientRepository;

    @PostMapping
    @ApiOperation(value = "添加客户端账号", notes = "添加客户端账号接口", response = ResultEntities.class)
    public ResultEntities<ClientEntities> addClient(@RequestBody @Validated ClientForm clientForm)
    {
        ClientEntities client = clientRepository.findByClientId(clientForm.getClientId());
        if (client != null)
        {
            return ResultEntities.failed("该客户端账号已存在");
        }

        return ResultEntities.success(clientRepository.save(clientForm.entities()));
    }

    @PutMapping
    @ApiOperation(value = "编辑客户端账号", notes = "编辑客户端账号信息接口", response = ResultEntities.class)
    public ResultEntities<ClientEntities> editClient(@RequestBody @Validated ClientForm clientForm)
    {
        ClientEntities client = clientRepository.findByClientId(clientForm.getClientId());
        if (client == null)
        {
            return ResultEntities.failed("该客户端账号还未注册");
        }

        ClientEntities clientEntities = clientForm.entities();
        clientEntities.setId(client.getId());

        return ResultEntities.success(clientRepository.save(clientEntities));
    }

    @DeleteMapping
    @ApiOperation(value = "删除客户端账号", notes = "删除客户端账号信息接口", response = ResultEntities.class)
    public ResultEntities<String> delClient(String clientId)
    {
        ClientEntities client = clientRepository.findByClientId(clientId);
        if (client == null)
        {
            return ResultEntities.failed("该客户端账号还未注册");
        }

        try
        {
            clientRepository.deleteById(client.getId());
            return ResultEntities.success(null);
        }
        catch (Exception e)
        {
            return ResultEntities.failed("删除失败");
        }
    }
}
