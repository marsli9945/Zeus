package com.tuyoo.framework.grow.admin.form.user;

import com.tuyoo.framework.grow.admin.entities.UserEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;

@Data
@ApiModel("用户编辑表单")
public class EditUserForm
{
    @ApiModelProperty(value = "用户账号", name = "username", required = true, example = "tuyoo@tuyoogame.com")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", name = "password", example = "Tuyoo@123")
    @Pattern(regexp = "^(?![A-z0-9]+$)(?![A-z~@*()_]+$)(?![0-9~@*()_]+$)([A-z0-9~@*()_]{8,})$", message = "密码为至少8位的大小字母+数字+特殊符号")
    private String password;

    @ApiModelProperty(value = "用户名称", name = "name", example = "途游")
    private String name;

    @ApiModelProperty(value = "部门", name = "level", example = "20")
    private Integer level;

    @ApiModelProperty(value = "状态", name = "status", example = "1")
    private Integer status;

    @ApiModelProperty(value = "角色列表", name = "roleList")
    private ArrayList<Integer> roleList;

    public UserEntities entities(UserEntities user)
    {
        return CreateUserForm.setValue(user, this.username, this.password, this.name, this.level, this.status, this.roleList);
    }
}
