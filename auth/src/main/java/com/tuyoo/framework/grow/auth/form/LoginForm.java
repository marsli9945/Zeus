package com.tuyoo.framework.grow.auth.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel("登录表单")
public class LoginForm
{
    @ApiModelProperty(value = "授权ID", name = "client_id", required = true, example = "test_client")
    @NotBlank(message = "授权ID不能为空")
    private String clientId;

    @ApiModelProperty(value = "授权密码", name = "client_secret", required = true, example = "123456")
    @NotBlank(message = "授权密码不能为空")
    @Size(min = 6,max = 18,message = "密码的长度范围为6-18位")
    private String clientSecret;

    @ApiModelProperty(value = "登录账号", name = "username", required = true, example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", name = "password", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6,max = 18,message = "密码的长度范围为6-18位")
    private String password;

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getClientSecret()
    {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret)
    {
        this.clientSecret = clientSecret;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
