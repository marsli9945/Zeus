package com.tuyoo.framework.grow.admin.ga.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("用户信息详情实体")
public class GaUserInfoEntities
{
    @ApiModelProperty(value = "用户ID", name = "id", example = "10001")
    private Integer id;

    @ApiModelProperty(value = "用户昵称", name = "name", example = "张三")
    private String name;

    @ApiModelProperty(value = "用户账号", name = "username", example = "zhangsan@tuyoogame.com")
    private String username;

    @ApiModelProperty(value = "是否是管理员", name = "is_admin", example = "1")
    private Integer is_admin = 0;

    @ApiModelProperty(value = "是否可以新增编辑用户", name = "is_distribute", example = "1")
    private Integer is_distribute = 0;

    @ApiModelProperty(value = "是否可以接入游戏", name = "is_access_game", example = "1")
    private Integer is_access_game = 0;

    private List<GaUserInfoGameEntities> permission;
}
