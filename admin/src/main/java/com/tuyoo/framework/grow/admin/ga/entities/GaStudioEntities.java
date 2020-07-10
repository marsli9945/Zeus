package com.tuyoo.framework.grow.admin.ga.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString
@ApiModel("Ga工作室权限实体")
public class GaStudioEntities
{
    @ApiModelProperty(value = "工作室ID", name = "id", example = "1")
    @NotNull(message = "缺少工作室ID")
    private Integer id;

    @ApiModelProperty(value = "工作室名称", name = "name", example = "BI工作室")
    private String name;

    private List<GaGameEntities> game;

    private List<GaPermissionEntities> permission;
}
