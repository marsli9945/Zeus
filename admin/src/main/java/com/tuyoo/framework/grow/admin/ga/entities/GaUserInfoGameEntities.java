package com.tuyoo.framework.grow.admin.ga.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户详情游戏权限实体")
public class GaUserInfoGameEntities
{
    @ApiModelProperty(value = "游戏ID", name = "id", example = "20249")
    private Integer id;

    @ApiModelProperty(value = "游戏名", name = "name", example = "GrowAnalytics")
    private String name;

    @ApiModelProperty(value = "游戏图标", name = "icon", example = "http://analytics.tuyoo.com/api/img/0b933762c947ec7eaacdcdacaa93dbe9.png")
    private String icon;

    @ApiModelProperty(value = "游戏默认时区", name = "time_zone", example = "+8")
    private String time_zone;

    @ApiModelProperty(value = "游戏权限列表", name = "permission", example = "{\"data\": [\"config\", \"analysis_tool\"], \"game\": [\"is_auto\"], \"admin\": [\"page_sit\", \"is_distribute\", \"is_access_game\", \"edit_preset_overview\"]}")
    private String permission;
}
