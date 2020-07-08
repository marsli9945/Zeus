package com.tuyoo.framework.grow.admin.form.studio;

import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("工作室添加表单")
public class CreateStudioForm
{
    @NotBlank(message = "工作室名称不能为空")
    @ApiModelProperty(value = "工作室名称", name = "name", required = true, example = "BI工作室")
    private String name;

    @NotBlank(message = "工作室管理员不能为空")
    @ApiModelProperty(value = "工作室管理员", name = "admin", required = true, example = "tuyoo@tuyoogame.com")
    private String admin;

    @ApiModelProperty(value = "工作室状态", name = "status", required = true, example = "1")
    private Integer status;

    public StudioEntities entities(StudioEntities studioEntities)
    {
        return setValue(studioEntities, this.name, this.admin, this.status);
    }

    static StudioEntities setValue(StudioEntities studioEntities, String name, String admin, Integer status)
    {
        if (name != null)
        {
            studioEntities.setName(name);
        }

        if (admin != null)
        {
            studioEntities.setAdmin(admin);
        }

        if (status != null)
        {
            studioEntities.setStatus(status);
        }

        return studioEntities;
    }
}
