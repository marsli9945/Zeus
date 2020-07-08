package com.tuyoo.framework.grow.admin.form;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginForm
{
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
}
