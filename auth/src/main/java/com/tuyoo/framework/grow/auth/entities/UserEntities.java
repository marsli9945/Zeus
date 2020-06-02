package com.tuyoo.framework.grow.auth.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Table
@ApiModel("用户信息内容")
@Entity(name = "springcloud_user")
public class UserEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    @ApiModelProperty(value = "主键ID", name = "id", required = true, example = "1")
    private Integer id;

    @Column
    @ApiModelProperty(value = "用户账号", name = "username", required = true, example = "admin")
    private String username;

    @Column
    private String password;

    @Column
    @ApiModelProperty(value = "电话号码", name = "phone", required = true, example = "13810647108")
    private String phone;

    @Column
    @ApiModelProperty(value = "用户邮箱", required = true, example = "13810647108@163.com")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "springcloud_user_role",joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntities> roleEntitiesList;

    public List<RoleEntities> getRoleEntitiesList()
    {
        return roleEntitiesList;
    }

    public void setRoleEntitiesList(List<RoleEntities> roleEntitiesList)
    {
        this.roleEntitiesList = roleEntitiesList;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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
