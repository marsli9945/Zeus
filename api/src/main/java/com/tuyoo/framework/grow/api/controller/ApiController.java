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

    public static Integer counter = 0;


    @GetMapping("/user")
    public ResultEntities<String> user()
    {
        counter++;
        log.info("counter:{}"+counter);
        return ResultEntities.success(request.getHeader("search_user"));
    }

    @GetMapping("/api")
    public ResultEntities<String> api()
    {
        counter++;
        log.info("api counter:{}"+counter);
        return ResultEntities.success("this is api");
    }

    @GetMapping("/io500")
    public ResultEntities<String> io500() throws InterruptedException
    {
        Thread.sleep(500);
        counter++;
        log.info("io500 counter:{}"+counter);
        return ResultEntities.success("this is io");
    }

    @GetMapping("/io1000")
    public ResultEntities<String> io1000() throws InterruptedException
    {
        Thread.sleep(1000);
        counter++;
        log.info("io1000 counter:{}"+counter);
        return ResultEntities.success("this is io");
    }

    @GetMapping("/io3000")
    public ResultEntities<String> io3000() throws InterruptedException
    {
        Thread.sleep(3000);
        return ResultEntities.success("this is io");
    }
}
