package com.tuyoo.framework.grow.auth.controller;

import com.tuyoo.framework.grow.auth.entities.ClientEntities;
import com.tuyoo.framework.grow.auth.form.ClientForm;
import com.tuyoo.framework.grow.auth.repository.ClientRepository;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/client")
@Api(tags = "客户端ID相关接口")
public class ClientController
{
    @Autowired
    ClientRepository clientRepository;

    @GetMapping
    @ApiOperation(value = "获取客户端账号列表", notes = "获取客户端账号列表接口", response = ClientForm.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", type = "String", value = "客户端账号", paramType = "query"),
            @ApiImplicitParam(name = "page", type = "Integer", value = "当前页码", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", type = "Integer", value = "每页条数", paramType = "query", defaultValue = "2")
    })
    public ResultEntities<Object> getList(String clientId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "2")Integer size) {


        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<ClientEntities> specification = new Specification<ClientEntities>() {
            @Override
            public Predicate toPredicate(Root<ClientEntities> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(clientId)) {
                    predicates.add(cb.like(root.get("clientId").as(String.class), "%" + clientId + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<ClientEntities> all = clientRepository.findAll(specification, pageable);
        return ResultEntities.success(all);
    }

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
    @ApiImplicitParam(name = "clientId", type = "String", value = "客户端账号", required = true, paramType = "query")
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
