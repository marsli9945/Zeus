package com.tuyoo.framework.grow.admin.form.role;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("角色编辑表单")
public class EditRoleForm
{
    @ApiModelProperty(value = "角色ID", name = "id", required = true, example = "1")
    @NotNull(message = "请指定需要修改角色的ID")
    private Integer id;

    @ApiModelProperty(value = "角色名称", name = "name", example = "ROLE_USER")
    private String name;

    public RoleEntities entities() {
        RoleEntities roleEntities = new RoleEntities();

        if (this.id != null) {
            roleEntities.setId(this.id);
        }

        if (this.name != null)
        {
            roleEntities.setName(this.name);
        }

        return roleEntities;
    }
}
