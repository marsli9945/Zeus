package com.tuyoo.framework.grow.api.controller;

import com.tuyoo.framework.grow.api.entities.LoginEntities;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RestController
public class ApiController
{
    @Autowired
    HttpServletRequest request;

    @GetMapping("/user")
    public ResultEntities<String> user()
    {
        log.info("***********send user11");
        return ResultEntities.success(request.getHeader("search_user"));
    }

    @GetMapping("/api")
    public ResultEntities<Object> api()
    {
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", "Jack");
        data.put("age", 22);
        return ResultEntities.success(data);
    }

    @PostMapping("post")
    public ResultEntities<Object> post(LoginEntities loginEntities)
    {
        log.info("push login********************");
        HashMap<String, String> result = new HashMap<>();
        result.put("username", loginEntities.getUsername());
        result.put("password", loginEntities.getPassword());
        return ResultEntities.success(result);
    }


}
