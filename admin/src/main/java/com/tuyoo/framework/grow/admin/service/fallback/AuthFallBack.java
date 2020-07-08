package com.tuyoo.framework.grow.admin.service.fallback;

import com.tuyoo.framework.grow.admin.form.LoginForm;
import com.tuyoo.framework.grow.admin.service.AuthService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import org.springframework.stereotype.Component;

@Component
public class AuthFallBack implements AuthService
{
    @Override
    public ResultEntities<Object> login(LoginForm loginForm)
    {
        return ResultEntities.failed("请求超时");
    }
}
