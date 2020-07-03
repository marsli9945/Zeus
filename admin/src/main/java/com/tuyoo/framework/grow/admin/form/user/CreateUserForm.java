package com.tuyoo.framework.grow.admin.form.user;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import com.tuyoo.framework.grow.admin.entities.UserEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;

@Data
@ApiModel("用户添加表单")
public class CreateUserForm
{
    @ApiModelProperty(value = "用户账号", name = "username", required = true, example = "admin")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", name = "password", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?![A-z0-9]+$)(?![A-z~@*()_]+$)(?![0-9~@*()_]+$)([A-z0-9~@*()_]{8,})$", message = "密码为至少8位的大小字母+数字+特殊符号")
    private String password;

    @ApiModelProperty(value = "用户名称", name = "name", required = true, example = "张三")
    @NotBlank(message = "密码不能为空")
    private String name;

    @ApiModelProperty(value = "部门", name = "level", required = true, example = "20")
    @NotNull(message = "用户部门不能为空")
    private Integer level;

    @ApiModelProperty(value = "用户状态 1正常 0禁用", name = "status", example = "1")
    private Integer status;

    public UserEntities entities(UserEntities user) {

        UserEntities userEntities = setValue(user, this.username, this.password, this.name, this.level, this.status);


        ArrayList<RoleEntities> roleList = new ArrayList<>();
        roleList.add(new RoleEntities(1,null));
        userEntities.setRoleEntitiesList(roleList);

        return userEntities;
    }

    static UserEntities setValue(UserEntities userEntities, String username, String password, String name, Integer level, Integer status)
    {
        if (username != null) {
            userEntities.setUsername(username);
        }

        if (password != null) {
            userEntities.setPassword(new BCryptPasswordEncoder().encode(password));
        }

        if (name != null) {
            userEntities.setName(name);
        }

        if (level != null) {
            userEntities.setLevel(level);
        }

        if (status != null) {
            userEntities.setStatus(status);
        }

        return userEntities;
    }
}
