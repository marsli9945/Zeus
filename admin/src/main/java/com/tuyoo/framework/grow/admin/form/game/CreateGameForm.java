package com.tuyoo.framework.grow.admin.form.game;

import com.tuyoo.framework.grow.admin.entities.GameEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("游戏添加表单")
public class CreateGameForm
{
    @NotBlank(message = "项目ID不能为空")
    @ApiModelProperty(value = "项目ID", name = "projectId", required = true, example = "20249")
    private String projectId;

    @NotBlank(message = "游戏名不能为空")
    @ApiModelProperty(value = "游戏名", name = "name", required = true, example = "GrowAnalytics")
    private String name;

    @ApiModelProperty(value = "游戏图标", name = "icon", required = true, example = "http://analytics.tuyoo.com/api/img/0b933762c947ec7eaacdcdacaa93dbe9.png")
    private String icon;

    @NotBlank(message = "游戏时区不能为空")
    @ApiModelProperty(value = "游戏时区", name = "timeZone", required = true, example = "+8")
    private String timeZone;

    @NotBlank(message = "货币类型不能为空")
    @ApiModelProperty(value = "货币类型", name = "timeZone", required = true, example = "USD")
    private String currency;

    @ApiModelProperty(value = "游戏状态", name = "status", required = true, example = "1")
    private Integer status;

    public GameEntities entities(GameEntities gameEntities)
    {
        return setValue(gameEntities, this.projectId, this.name, this.icon, this.timeZone, this.currency, this.status);
    }

    static GameEntities setValue(GameEntities gameEntities, String projectId, String name, String icon, String timeZone, String currency, Integer status)
    {
        if (projectId != null)
        {
            gameEntities.setProjectId(projectId);
        }

        if (name != null)
        {
            gameEntities.setName(name);
        }

        if (icon != null)
        {
            gameEntities.setIcon(icon);
        }

        if (timeZone != null)
        {
            gameEntities.setTimeZone(timeZone);
        }

        if (currency != null)
        {
            gameEntities.setCurrency(currency);
        }

        if (status != null)
        {
            gameEntities.setStatus(status);
        }

        return gameEntities;
    }
}
