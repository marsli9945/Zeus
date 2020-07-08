package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.form.LoginForm;
import com.tuyoo.framework.grow.admin.service.fallback.AuthFallBack;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(value = "AUTH",fallback = AuthFallBack.class)
public interface AuthService
{
    @PostMapping("/login")
    ResultEntities<Object> login(LoginForm loginForm);
}
