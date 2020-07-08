package com.tuyoo.framework.grow.admin.form.map;

import com.tuyoo.framework.grow.admin.entities.MapEntities;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditMapForm
{
    @NotNull(message = "字典ID不能为空")
    @ApiModelProperty(value = "字典ID", name = "type", required = true, example = "1")
    private Integer id;

    @ApiModelProperty(value = "字典类型", name = "type", example = "time_zone")
    private String type;

    @ApiModelProperty(value = "字典名称", name = "label", example = "东8区")
    private String label;

    @ApiModelProperty(value = "字典值", name = "value", example = "+8")
    private String value;

    @ApiModelProperty(value = "字典状态", name = "status", example = "1")
    private Integer status;

    public MapEntities entities(MapEntities mapEntities)
    {
        return CreateMapForm.setValue(mapEntities, this.type, this.label, this.value, this.status);
    }
}
