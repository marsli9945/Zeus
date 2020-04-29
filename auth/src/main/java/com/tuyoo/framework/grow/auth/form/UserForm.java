package com.tuyoo.framework.grow.auth.form;

import com.tuyoo.framework.grow.auth.entities.RoleEntities;
import com.tuyoo.framework.grow.auth.entities.UserEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@ApiModel("用户添加表单")
public class UserForm
{
    @ApiModelProperty(value = "主键ID", name = "id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "用户账号", name = "username", required = true, example = "admin")
    @NotBlank(message = "授权ID不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", name = "password", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 18, message = "密码的长度范围为6-18位")
    private String password;

    @ApiModelProperty(value = "手机号", name = "phone", required = true, example = "13622114309")
    @NotBlank(message = "密码不能为空")
    @Size(min = 11, max = 11, message = "手机号号码的长度为11位")
    private String phone;

    @ApiModelProperty(value = "邮箱地址", name = "email", required = true, example = "13622114309@168.cn")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$", message = "请输入正确的邮箱")
    private String email;

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

        if (this.phone != null)
        {
            userEntities.setPhone(this.phone);
        }

        if (this.email != null)
        {
            userEntities.setEmail(this.email);
        }

        if (this.roleList.size() > 0) {
            ArrayList<RoleEntities> roleList = new ArrayList<>();
            for (Integer id: this.roleList)
            {
                roleList.add(new RoleEntities(id));
            }
            userEntities.setRoleEntitiesList(roleList);
        }

        return userEntities;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public List<Integer> getRoleList()
    {
        return roleList;
    }

    public void setRoleList(List<Integer> roleList)
    {
        this.roleList = roleList;
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

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
