package com.tuyoo.framework.grow.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
public class LoginController
{

    @RequestMapping("/login")
    public ResponseEntity<Object> login()
    {
        log.info("*********************** login");
        return new ResponseEntity<>("this is login", HttpStatus.OK);
    }

    @RequestMapping("/map")
    public ResponseEntity<Object> map(){

        log.info("*********************** map");

        final HashMap<String, Object> map = new HashMap<>();

        map.put("age",22);
        map.put("name", "Jack");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
