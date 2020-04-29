package com.tuyoo.framework.grow.auth.controller;

import com.tuyoo.framework.grow.auth.entities.ClientEntities;
import com.tuyoo.framework.grow.auth.entities.UserEntities;
import com.tuyoo.framework.grow.auth.form.UserForm;
import com.tuyoo.framework.grow.auth.repository.UserRepository;
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
@RequestMapping("/user")
@Api(tags = "用户信息相关接口")
public class UserController
{
    @Autowired
    UserRepository userRepository;

    @GetMapping
    @ApiOperation(value = "获取用户账号列表", notes = "获取用户列表接口", response = UserForm.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", type = "String", value = "用户名", paramType = "query"),
            @ApiImplicitParam(name = "page", type = "Integer", value = "当前页码", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", type = "Integer", value = "每页条数", paramType = "query", defaultValue = "2")
    })
    public ResultEntities<Object> getList(String username, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "2")Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<UserEntities> specification = new Specification<UserEntities>() {
            @Override
            public Predicate toPredicate(Root<UserEntities> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (!StringUtils.isEmpty(username)) {
                    predicates.add(cb.like(root.get("username").as(String.class), "%" + username + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<UserEntities> all = userRepository.findAll(specification, pageable);
        return ResultEntities.success(all);
    }

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
