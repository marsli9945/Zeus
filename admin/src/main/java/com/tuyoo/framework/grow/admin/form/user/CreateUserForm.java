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
    @ApiModelProperty(value = "用户账号", name = "username", required = true, example = "tuyoo@tuyoogame.com")
    @NotBlank(message = "用户账号不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", name = "password", required = true, example = "Tuyoo@123")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\s\\S]{8,}$", message = "密码为至少8位的大小字母+数字+特殊符号")
    private String password;

    @ApiModelProperty(value = "用户名称", name = "name", required = true, example = "途游")
    @NotBlank(message = "密码不能为空")
    private String name;

    @ApiModelProperty(value = "部门", name = "level", required = true, example = "20")
    @NotNull(message = "用户部门不能为空")
    private Integer level;

    @ApiModelProperty(value = "用户状态 1正常 0禁用", name = "status", example = "1")
    private Integer status;

    @ApiModelProperty(value = "角色列表", name = "roleList")
    @NotNull(message = "用户至少拥有一个角色")
    private ArrayList<Integer> roleList;

    public UserEntities entities(UserEntities user)
    {
        return setValue(user, this.username, this.password, this.name, this.level, this.status, this.roleList);
    }

    static UserEntities setValue(UserEntities userEntities, String username, String password, String name, Integer level, Integer status, ArrayList<Integer> roleList)
    {
        if (username != null)
        {
            userEntities.setUsername(username);
        }

        if (password != null)
        {
            userEntities.setPassword(new BCryptPasswordEncoder().encode(password));
        }

        if (name != null)
        {
            userEntities.setName(name);
        }

        if (level != null)
        {
            userEntities.setLevel(level);
        }

        if (status != null)
        {
            userEntities.setStatus(status);
        }

        if (roleList != null && roleList.size() > 0)
        {
            ArrayList<RoleEntities> roleEntitiesList = new ArrayList<>();
            for (Integer id : roleList)
            {
                roleEntitiesList.add(new RoleEntities(id, null));
            }
            userEntities.setRoleEntitiesList(roleEntitiesList);
        }

        return userEntities;
    }
}
