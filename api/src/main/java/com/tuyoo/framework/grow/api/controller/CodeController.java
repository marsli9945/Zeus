package com.tuyoo.framework.grow.api.controller;

import com.tuyoo.framework.grow.api.entities.UserEntities;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class CodeController
{
    @GetMapping("/code200")
    public String code200() {
        return "ok";
    }

    @GetMapping("/code400")
    public String code400(HttpServletResponse response) {
        response.setStatus(400);
        return "bade request";
    }

    @GetMapping("/code500")
    public String code500(HttpServletResponse response) {
        response.setStatus(500);
        return "server error";
    }

    @GetMapping("json")
    public UserEntities json() {
        UserEntities userEntities = new UserEntities();
        userEntities.setName("大磊子");
        userEntities.setAge(22);
        userEntities.setSex("男");
        return userEntities;
    }

    @GetMapping("boolean")
    public boolean toBoolean() {
        return false;
    }

    @GetMapping("int")
    public Integer toInt() {
        return 8;
    }
}
