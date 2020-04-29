package com.tuyoo.framework.grow.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.tuyoo.framework.grow.auth.form.LoginForm;
import com.tuyoo.framework.grow.auth.form.RefreshForm;
import com.tuyoo.framework.grow.auth.util.RedisUtil;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(tags = "登录操作相关接口")
public class AuthController
{
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Value("${server.port}")
    private String port;

    // security令牌申请和刷新地址
    private final String url = "http://127.0.0.1:8100/oauth/token";

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "使用账号密码登录以获取token", response = ResultEntities.class)
    public ResultEntities<Object> login(@RequestBody @Valid LoginForm loginForm)
    {
        // 先判断username+client_id组合是否已经登录
        // 已登录的账号只能通过刷新接口
        log.info("username_to_access:" + loginForm.getClientId() + ":" + loginForm.getUsername());
        boolean hasKey = redisUtil.hasKey("username_to_access:" + loginForm.getClientId() + ":" + loginForm.getUsername());
//        log.info("hasKey:{}" + hasKey);
        if (hasKey)
        {
            return ResultEntities.failed("该用户已登录");
        }

        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", getHttpBasic(loginForm.getClientId(), loginForm.getClientSecret()));

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", loginForm.getUsername());
        body.add("password", loginForm.getPassword());

        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try
        {
            ResponseEntity<JSONObject> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, JSONObject.class);

//            log.info("login - exchange.getBody():{}"+exchange.getStatusCode());

            // 获取回调信息，少一样都不行
            JSONObject authResult = exchange.getBody();
            if (
                    authResult == null ||
                            authResult.getString("access_token") == null ||
                            authResult.getString("refresh_token") == null ||
                            authResult.getString("scope") == null ||
                            authResult.getString("token_type") == null ||
                            authResult.getString("expires_in") == null ||
                            authResult.getString("jti") == null
            )
            {
                return ResultEntities.failed("令牌申请失败");
            }

            return ResultEntities.success(authResult.getString("access_token"));
        }
        catch (Exception e)
        {
            return ResultEntities.failed("令牌申请失败");
        }
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "刷新令牌", notes = "使用已有令牌刷新令牌有效时间", response = ResultEntities.class)
    public ResultEntities<Object> refresh(@RequestBody @Validated RefreshForm refreshForm)
    {
        String token = refreshForm.getToken();
        Object refresh = redisUtil.get("access_to_refresh:" + token);
        if (refresh == null)
        {
            return ResultEntities.failed("令牌已超出有效期，请重新登录");
        }

        try
        {
            LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Authorization", getHttpBasic(refreshForm.getClientId(), refreshForm.getClientSecret()));

            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "refresh_token");
            body.add("refresh_token", refresh.toString());

            HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<JSONObject> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, JSONObject.class);

//            log.info("refresh - exchange.getBody():{}"+exchange.getStatusCode());

            // 获取回调信息，少一样都不行
            JSONObject authResult = exchange.getBody();
            if (
                    authResult == null ||
                            authResult.getString("access_token") == null ||
                            authResult.getString("refresh_token") == null ||
                            authResult.getString("scope") == null ||
                            authResult.getString("token_type") == null ||
                            authResult.getString("expires_in") == null ||
                            authResult.getString("jti") == null
            )
            {
                return ResultEntities.failed("令牌刷新失败");
            }
            return ResultEntities.success(authResult.getString("access_token"));
        }
        catch (Exception e)
        {
            return ResultEntities.failed("令牌刷新失败");
        }
    }

    private String getHttpBasic(String clientId, String clientSecret)
    {
        String str = clientId + ":" + clientSecret;
        return "Basic " + Base64Utils.encodeToString(str.getBytes());
    }
}
