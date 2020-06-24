package com.tuyoo.framework.grow.auth.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Table
@ApiModel("用户角色信息")
@Entity(name = "oauth_role")
public class RoleEntities
{
    public RoleEntities() {}

    public RoleEntities(Integer id)
    {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键ID", required = true, example = "1")
    private Integer id;

    @Column
    @ApiModelProperty(value = "角色名", required = true, example = "admin")
    private String name;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
