package com.tuyoo.framework.grow.auth.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户角色信息")
@Entity(name = "oauth_role")
public class RoleEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键ID", required = true, example = "1")
    private Integer id;

    @Column
    @ApiModelProperty(value = "角色名", required = true, example = "admin")
    private String name;
}
