package com.tuyoo.framework.grow.admin.form.game;

import com.tuyoo.framework.grow.admin.entities.GameEntities;
import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull(message = "工作室ID不能为空")
    @ApiModelProperty(value = "工作室ID", name = "studio", required = true, example = "2")
    private Integer studio;

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

    @ApiModelProperty(value = "游戏所属区域", name = "region", example = "3")
    private String region;

    @ApiModelProperty(value = "游戏类型", name = "type", example = "3")
    private String type;

    public GameEntities entities(GameEntities gameEntities)
    {
        return setValue(gameEntities, this.projectId, this.name, this.studio, this.icon, this.timeZone, this.currency, this.status, this.region, this.type);
    }

    static GameEntities setValue(GameEntities gameEntities, String projectId, String name, Integer studio, String icon, String timeZone, String currency, Integer status, String region, String type)
    {
        if (projectId != null)
        {
            gameEntities.setProjectId(projectId);
        }

        if (name != null)
        {
            gameEntities.setName(name);
        }

        if (studio != null)
        {
            gameEntities.setStudio(new StudioEntities(studio, null, null, null, null));
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

        if (region != null) {
            gameEntities.setRegion(region);
        }

        if (type != null) {
            gameEntities.setType(type);
        }

        return gameEntities;
    }
}
