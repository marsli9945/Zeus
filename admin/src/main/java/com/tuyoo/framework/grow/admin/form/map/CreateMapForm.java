package com.tuyoo.framework.grow.admin.form.map;

import com.tuyoo.framework.grow.admin.entities.MapEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("字典添加表单")
public class CreateMapForm
{
    @NotBlank(message = "字典类型不能为空")
    @ApiModelProperty(value = "字典类型", name = "type", required = true, example = "time_zone")
    private String type;

    @NotBlank(message = "字典名称不能为空")
    @ApiModelProperty(value = "字典名称", name = "label", required = true, example = "东8区")
    private String label;

    @NotBlank(message = "字典值不能为空")
    @ApiModelProperty(value = "字典值", name = "value", required = true, example = "+8")
    private String value;

    @ApiModelProperty(value = "字典状态", name = "status", required = true, example = "1")
    private Integer status;

    public MapEntities entities (MapEntities mapEntities) {
        return setValue(mapEntities, this.type, this.label, this.value, this.status);
    }

    static MapEntities setValue(MapEntities mapEntities, String type, String label, String value, Integer status)
    {
        if (type != null) {
            mapEntities.setType(type);
        }

        if (label != null) {
            mapEntities.setLabel(label);
        }

        if (value != null) {
            mapEntities.setValue(value);
        }

        if (status != null) {
            mapEntities.setStatus(status);
        }

        return mapEntities;
    }
}
