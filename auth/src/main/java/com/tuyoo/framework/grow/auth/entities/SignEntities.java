package com.tuyoo.framework.grow.auth.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("注册表单")
public class SignEntities
{
    @ApiModelProperty(value = "用户角色", name = "role", example = "manager")
    private String role;

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }
}
