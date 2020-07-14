package com.tuyoo.framework.grow.auth.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table
@ApiModel("用户信息内容")
@Entity(name = "oauth_user")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "oauth_user_role",joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntities> roleEntitiesList;
}
