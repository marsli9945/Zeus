package com.tuyoo.framework.grow.admin.ga.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("权限子级实体")
public class GaPermissionChildrenEntities
{
    @ApiModelProperty(value = "权限编码", name = "id", example = "is_access_game")
    @NotBlank(message = "缺少子级权限ID")
    private String id;

    @ApiModelProperty(value = "权限描述名称", name = "name", example = "从该账户添加或删除APP")
    private String name;

    @ApiModelProperty(value = "是否拥有权限", name = "is_own", example = "false")
    @NotNull(message = "缺少子级权限拥有字段")
    private Boolean is_own;
}
