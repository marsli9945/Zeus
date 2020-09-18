package com.tuyoo.framework.grow.admin.ga.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("GA游戏详情实体")
public class GaGameInfoEntities
{
    @ApiModelProperty(value = "游戏自增id", name = "id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "游戏projectId", name = "projectId", example = "20249")
    private String projectId;

    @ApiModelProperty(value = "游戏名称", name = "name", example = "GA游戏")
    private String name;

    @ApiModelProperty(value = "游戏图标", name = "icon", example = "http://analytics.tuyoo.com/api/img/0b933762c947ec7eaacdcdacaa93dbe9.png")
    private String icon;

    @ApiModelProperty(value = "游戏主时区", name = "timeZone", example = "+8")
    private String timeZone;

    @ApiModelProperty(value = "游戏结算货币", name = "currency", example = "USD")
    private String currency;

    @ApiModelProperty(value = "游戏状态", name = "status", example = "1")
    private Integer status;

    @ApiModelProperty(value = "游戏所属工作室ID", name = "studio", example = "3")
    private Integer studio;

    @ApiModelProperty(value = "游戏所属区域", name = "region", example = "3")
    private String region;

    @ApiModelProperty(value = "游戏类型", name = "type", example = "3")
    private String type;
}
