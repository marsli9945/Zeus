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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if (client != null) {
            return ResultEntities.failed("该客户端账号已存在");
        }

        return ResultEntities.success(clientRepository.save(clientForm.entities()));
    }
}
