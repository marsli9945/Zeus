package com.tuyoo.framework.grow.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;

@Slf4j
@RestController
public class ApiController
{
    @Autowired
    HttpServletRequest request;


    @GetMapping("/user")
    public ResultEntities<String> user()
    {
        //获取请求头信息
        Enumeration<String> headerNames = request.getHeaderNames();
        //使用循环遍历请求头，并通过getHeader()方法获取一个指定名称的头字段
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            log.info(headerName + " : " + request.getHeader(headerName) + "<br/>");
        }
        return ResultEntities.success(request.getHeader("search_user"));
    }

    @GetMapping("/api")
    public ResultEntities<String> api()
    {
        return ResultEntities.success("this is api");
    }

    @PostMapping("/log/record")
    public ResultEntities<Object> record(@RequestBody JSONArray jsonArray) {
        ArrayList<String> list = (ArrayList<String>) JSON.parseArray(jsonArray.toJSONString(),String.class);
        log.info("list:{}", list);
        return ResultEntities.success();
    }
}
