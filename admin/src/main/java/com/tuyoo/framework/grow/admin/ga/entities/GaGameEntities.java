package com.tuyoo.framework.grow.admin.ga.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel("Ga游戏实体")
public class GaGameEntities
{
    @ApiModelProperty(value = "游戏projectId", name = "id", example = "20249")
    private Integer id;

    @ApiModelProperty(value = "游戏名称", name = "name", example = "GrowAnalytics")
    private String name;

    @ApiModelProperty(value = "游戏图表", name = "icon", example = "http://analytics.tuyoo.com/api/img/0b933762c947ec7eaacdcdacaa93dbe9.png")
    private String icon;

    @ApiModelProperty(value = "是否拥有游戏", name = "is_own", example = "false")
    private Boolean is_own;
}
