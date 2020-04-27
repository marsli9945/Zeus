package com.tuyoo.framework.grow.auth.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ApiModel("登录表单")
public class LoginEntities implements Serializable
{
    @ApiModelProperty(value = "登录账号", name = "username", required = true, example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", name = "password", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 2,max = 4,message = "密码的长度范围为2-4")
    private String password;

    SignEntities signEntities;

    public SignEntities getSignEntities()
    {
        return signEntities;
    }

    public void setSignEntities(SignEntities signEntities)
    {
        this.signEntities = signEntities;
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
