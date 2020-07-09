package com.tuyoo.framework.grow.admin.ga.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GaPermissionChildrenEntities
{
    @ApiModelProperty(value = "权限编码", name = "id", example = "is_access_game")
    private String id;

    @ApiModelProperty(value = "权限描述名称", name = "name", example = "从该账户添加或删除APP")
    private String name;

    @ApiModelProperty(value = "是否拥有权限", name = "is_own", example = "false")
    private Boolean is_own;
}
