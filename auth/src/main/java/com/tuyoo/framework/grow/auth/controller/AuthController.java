package com.tuyoo.framework.grow.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.tuyoo.framework.grow.auth.form.LoginForm;
import com.tuyoo.framework.grow.auth.form.RefreshForm;
import com.tuyoo.framework.grow.auth.response.TokenResponse;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
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
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;

    @Value("${server.port}")
    private String port;

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "使用账号密码登录以获取token", response = TokenResponse.class)
    public ResultEntities<Object> login(@RequestBody @Valid LoginForm loginForm)
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", getHttpBasic(loginForm.getClientId(), loginForm.getClientSecret()));

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", loginForm.getUsername());
        body.add("password", loginForm.getPassword());

        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try
        {
            return sendSecurity(entity);
        }
        catch (Exception e)
        {
            return ResultEntities.failed("令牌申请失败");
        }
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "刷新令牌", notes = "使用已有令牌刷新令牌有效时间", response = TokenResponse.class)
    public ResultEntities<Object> refresh(@RequestBody @Validated RefreshForm refreshForm)
    {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", getHttpBasic(refreshForm.getClientId(), refreshForm.getClientSecret()));

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshForm.getRefreshToken());

        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try
        {
            return sendSecurity(entity);
        }
        catch (Exception e)
        {
            return ResultEntities.failed("令牌刷新失败");
        }
    }

    @DeleteMapping("/signOut")
    @ApiOperation(value = "令牌注销", notes = "使用已有令牌无法刷新，只能重新登录", response = ResultEntities.class)
    @ApiImplicitParam(name = "token", type = "String", value = "令牌", required = true, paramType = "query")
    public ResultEntities<String> signOut(String token)
    {
        if (!consumerTokenServices.revokeToken(token))
        {
            return ResultEntities.failed("注销失败");
        }

        return ResultEntities.success(null);
    }

    private ResultEntities<Object> sendSecurity(HttpEntity<LinkedMultiValueMap<String, String>> entity)
    {
        // security令牌申请和刷新地址
        String url = "http://127.0.0.1:" + port + "/oauth/token";
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, JSONObject.class);

        // 判断security反馈信息
        if (exchange.getStatusCodeValue() != 200 || exchange.getBody() == null)
        {
            return ResultEntities.failed("令牌刷新失败");
        }

        return ResultEntities.success(exchange.getBody());
    }

    private String getHttpBasic(String clientId, String clientSecret)
    {
        String str = clientId + ":" + clientSecret;
        return "Basic " + Base64Utils.encodeToString(str.getBytes());
    }
}
