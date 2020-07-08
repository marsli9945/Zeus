package com.tuyoo.framework.grow.admin.form.studio;

import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("工作室编辑表单")
public class EditStudioForm
{
    @NotNull(message = "工作室ID不能为空")
    @ApiModelProperty(value = "工作室ID", name = "id", required = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "工作室名称", name = "name", example = "BI工作室")
    private String name;

    @ApiModelProperty(value = "工作室管理员", name = "admin", example = "tuyoo@tuyoogame.com")
    private String admin;

    @ApiModelProperty(value = "工作室状态", name = "status", example = "1")
    private Integer status;

    public StudioEntities entities(StudioEntities studioEntities)
    {
        return CreateStudioForm.setValue(studioEntities, this.name, this.admin, this.status);
    }
}
