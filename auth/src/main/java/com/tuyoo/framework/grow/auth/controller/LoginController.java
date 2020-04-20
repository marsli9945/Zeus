package com.tuyoo.framework.grow.auth.controller;

import com.tuyoo.framework.grow.auth.repository.UserRepository;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
public class LoginController
{

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/login")
    public ResponseEntity<Object> login()
    {
        log.info("*********************** login");
        return new ResponseEntity<>("this is login", HttpStatus.OK);
    }

    @RequestMapping("/map")
    public ResponseEntity<Object> map()
    {

        log.info("*********************** map");

        final HashMap<String, Object> map = new HashMap<>();

        map.put("age", 22);
        map.put("name", "Jack");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("user/{username}")
    public ResultEntities<Object> getUser(@PathVariable String username)
    {
        return ResultEntities.success(userRepository.findByUsername(username));
    }

}
