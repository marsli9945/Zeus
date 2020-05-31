package com.tuyoo.framework.grow.api.controller;

import com.tuyoo.framework.grow.common.entities.ResultEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class ApiController
{
    @Autowired
    HttpServletRequest request;


    @GetMapping("/user")
    public ResultEntities<String> user()
    {
        return ResultEntities.success(request.getHeader("search_user"));
    }

    @GetMapping("/api")
    public ResultEntities<String> api()
    {
        return ResultEntities.success("this is api");
    }
}
