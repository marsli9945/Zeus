package com.tuyoo.framework.grow.admin.ga.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("权限父级实体")
public class GaPermissionEntities
{
    @ApiModelProperty(value = "权限编码", name = "id", example = "admin")
    private String id;

    @ApiModelProperty(value = "权限描述名称", name = "name", example = "授权管理员权限")
    private String name;

    List<GaPermissionChildrenEntities> children;
}
