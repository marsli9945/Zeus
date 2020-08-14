package com.tuyoo.framework.grow.auth.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel("登录表单")
public class LoginForm
{
    @ApiModelProperty(value = "授权ID", name = "client_id", required = true, example = "test_client")
    @NotBlank(message = "授权ID不能为空")
    private String clientId;

    @ApiModelProperty(value = "授权密码", name = "client_secret", required = true, example = "123456")
    @NotBlank(message = "授权密码不能为空")
    private String clientSecret;

    @ApiModelProperty(value = "登录账号", name = "username", required = true, example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", name = "password", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
}
