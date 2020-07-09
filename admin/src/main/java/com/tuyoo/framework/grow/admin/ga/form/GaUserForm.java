package com.tuyoo.framework.grow.admin.ga.form;

import com.tuyoo.framework.grow.admin.ga.entities.GaStudioEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("Ga用户添加/编辑表单")
public class GaUserForm
{
    @ApiModelProperty(value = "用户账号", name = "username", required = true, example = "tuyoo@tuyoogame.com")
    @NotBlank(message = "用户账号不能为空")
    private String username;

    @ApiModelProperty(value = "用户名称", name = "name", required = true, example = "途游")
    @NotBlank(message = "用户名称不能为空")
    private String name;

    @ApiModelProperty(value = "部门", name = "level", required = true, example = "20")
    @NotNull(message = "用户部门不能为空")
    private Integer level;

    @NotNull(message = "用户部门不能为空")
    private GaStudioEntities permission;
}
