package com.tuyoo.framework.grow.auth;

import com.tuyoo.framework.grow.auth.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = AuthApplication.class)
public class TestRedis
{
    @Resource
    RedisUtil redisUtil;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void set()
    {
        boolean set = redisUtil.set("token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODc0NDQxMjgsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiI5YTZlZjU3MC0zOTJiLTQxZGUtYTkzYi1hMmIyNjY2NWE5MzIiLCJjbGllbnRfaWQiOiJuZXQ1aWp5Iiwic2NvcGUiOlsiYWxsIiwicmVhZCIsIndyaXRlIl19.NRGJYgEuOe3L2YcxhsftE2gaXqJgi7oEx31aPftS05ryedh2Xl9orGzuiWJcCFXGxZNvi6UKYIJmwAIw70GOV5tDPttHLVzAXQclaQtdwMPDTEkaVX5FNkICEulxy48dwedg6awyPhQa3p9NhVh1AJGdYSs6KrHyB-sNhjdx3w-fsvS7NThOAA6PqT8GtEJxpX-BmcKOq3lSBp6wi2QwTvKrDHxb8C-sruwmQydnGl_g8ET_ao7h5Pgmz7-YavgYWf9P1xZdfkNfIkdOifIlaBHBfyiiBbKJ2fQN39zJRXdhHFPPpmwGfNfD34ehXSpYJfVHiqt_VoGX8zAHsPeP9A");
        log.info("*********" + set);
    }

    public void get()
    {
        Object token = redisUtil.get("token");
        log.info("************** " + token);
    }

    @Test
    public void setToken() {
        //定义key
        String key = "user_token:abde2951-76ab-4a24-8f5e-08604bb46f97";
        //定义value
        Map<String,String> value = new HashMap<>();
        value.put("jwt","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTUzODE1Mzc5NSwianRpIjoiYWJkZTI5NTEtNzZhYi00YTI0LThmNWUtMDg2MDRiYjQ2Zjk3IiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.SaJyy_-wMFcXhMyYK2D8dZ38vXZz1usbA58fraqnCv9fFvWrbPJ_5aG2QxY7yGOUKJY93SPntE3Y9K3qy2MdGGFMGWFHMcpgiNfOHLJNliOr-EnY2CP-EPZv9iaz2gRX2--kn3rqM9LWwev_ELWcnvwR3g2HDbvQjZ7TudiTMoOaMfdkUbGTYOwB2-Pa9FD46DTN3V5AtyJEyi72rIrPM2xY2JDjSTcFEb6KWdyTx8ELBNCkY0U7p6ioNxApGjmmV5Z1b1iMcNM1ipuSlqr0sjg88dAb38QX5JUzWB9Sn-qobHg1pQjj4A3mDt-Vs1jqB3YBM0KAlylzEz50vaCXIw");
        value.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiJhYmRlMjk1MS03NmFiLTRhMjQtOGY1ZS0wODYwNGJiNDZmOTciLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTUzODE1Mzc5NSwianRpIjoiZTcyYWExODYtZjBiMC00NDU0LTk3N2EtYWVmODQ4MDRhZjJiIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.ZIEvVhiautHYREhVXDfRYVcSfjbxGeu-YXvqZyqe3QEZN9iNcmnn6QDbCy9IHzjyw5mOhUHB3VhZHc3wOv6lkOOiSUINTDsr4ZR-PeuIU2-GSU-aAqhX3iamG2lKWFWLkXeqKJqJc9K25oAmtRpisdO6_uJuJq0vygxAbS_tE-dDYedUYYxD86mFRQPCTmDlqAlxN40TLQBACQeHsHuGtzkaOdfCLMKI0c_4Or1uLOUkhofpPHPE4_ZTJ5QhrkJui-5s51gKWqcReyVh3X9thorU5KuMwFd0bwT3jspsQV5UyO9FVxqmns5iI9zn-xya16FIO_RR-1uIi9ruKJ3llA");
//        String jsonString = JSON.toJSONString(value);
        String jsonString = JSONObject.toJSONString(value);
        //校验key是否存在，如果不存在则返回-2
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        System.out.println(expire);
        //存储数据
        stringRedisTemplate.boundValueOps(key).set(jsonString,30, TimeUnit.SECONDS);
        //获取数据
        String string = stringRedisTemplate.opsForValue().get(key);
        System.out.println(string);
    }
}
