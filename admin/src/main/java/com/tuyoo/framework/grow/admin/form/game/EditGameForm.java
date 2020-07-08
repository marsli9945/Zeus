package com.tuyoo.framework.grow.admin.form.game;

import com.tuyoo.framework.grow.admin.entities.GameEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("游戏编辑表单")
public class EditGameForm
{
    @NotBlank(message = "项目ID不能为空")
    @ApiModelProperty(value = "项目ID", name = "projectId", required = true, example = "20249")
    private String projectId;

    @ApiModelProperty(value = "游戏名", name = "name", example = "GrowAnalytics")
    private String name;

    @ApiModelProperty(value = "工作室ID", name = "studio", required = true, example = "2")
    private Integer studio;

    @ApiModelProperty(value = "游戏图标", name = "icon", example = "http://analytics.tuyoo.com/api/img/0b933762c947ec7eaacdcdacaa93dbe9.png")
    private String icon;

    @ApiModelProperty(value = "游戏时区", name = "timeZone", example = "+8")
    private String timeZone;

    @ApiModelProperty(value = "货币类型", name = "timeZone", example = "USD")
    private String currency;

    @ApiModelProperty(value = "游戏状态", name = "status", example = "1")
    private Integer status;

    public GameEntities entities(GameEntities gameEntities)
    {
        return CreateGameForm.setValue(gameEntities, this.projectId, this.name, this.studio, this.icon, this.timeZone, this.currency, this.status);
    }
}
