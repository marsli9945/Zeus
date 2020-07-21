package com.tuyoo.framework.grow.admin.form;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class LoginForm
{
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String clientId;

    private String clientSecret;
}
