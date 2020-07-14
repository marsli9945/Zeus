package com.tuyoo.framework.grow.auth.form;

import com.tuyoo.framework.grow.auth.entities.RoleEntities;
import com.tuyoo.framework.grow.auth.entities.UserEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("用户添加表单")
public class UserForm
{
    @ApiModelProperty(value = "用户账号", name = "username", required = true, example = "admin")
    @NotBlank(message = "授权ID不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", name = "password", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 18, message = "密码的长度范围为6-18位")
    private String password;

    @ApiModelProperty(value = "角色列表", name = "roleList")
    private List<Integer> roleList;

    public UserEntities entities()
    {
        UserEntities userEntities = new UserEntities();

        if (this.username != null)
        {
            userEntities.setUsername(this.username);
        }

        if (this.password != null)
        {
            userEntities.setPassword(new BCryptPasswordEncoder().encode(this.password));
        }

        if (this.roleList != null && this.roleList.size() > 0) {
            ArrayList<RoleEntities> roleList = new ArrayList<>();
            for (Integer id: this.roleList)
            {
                roleList.add(new RoleEntities(id,null));
            }
            userEntities.setRoleEntitiesList(roleList);
        }

        return userEntities;
    }
}
