package com.tuyoo.framework.grow.admin.ga.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Ga游戏实体")
public class GaGameEntities
{
    @ApiModelProperty(value = "游戏projectId", name = "id", example = "20249")
    @NotNull(message = "缺少游戏ID")
    private Integer id;

    private String projectId;

    @ApiModelProperty(value = "游戏名称", name = "name", example = "GrowAnalytics")
    private String name;

    @ApiModelProperty(value = "游戏图标", name = "icon", example = "http://analytics.tuyoo.com/api/img/0b933762c947ec7eaacdcdacaa93dbe9.png")
    private String icon;

    @ApiModelProperty(value = "是否拥有游戏", name = "is_own", example = "false")
    @NotNull(message = "缺少游戏拥有字段")
    private Boolean is_own;
}
