package com.tuyoo.framework.grow.admin.form.role;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("角色添加表单")
public class CreateRoleForm
{
    @ApiModelProperty(value = "角色名称", name = "name", required = true, example = "ROLE_USER")
    @NotBlank(message = "角色名不能为空")
    private String name;

    public RoleEntities entities()
    {
        RoleEntities roleEntities = new RoleEntities();

        if (this.name != null)
        {
            roleEntities.setName(this.name);
        }

        return roleEntities;
    }
}
