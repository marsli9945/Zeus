package com.tuyoo.framework.grow.admin.form.client;

import com.tuyoo.framework.grow.admin.entities.ClientEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("客户端ID编辑表单")
public class EditClientForm
{
    @ApiModelProperty(value = "客户端id", name = "clientId", required = true, example = "test_client")
    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    @ApiModelProperty(value = "客户端密码", name = "clientSecret", required = true, example = "L@3qXoChfI$@UH7l")
    @Pattern(regexp = "^(?![A-z0-9]+$)(?![A-z~@*()_]+$)(?![0-9~@*()_]+$)([A-z0-9~@*()_]{8,})$", message = "密码为至少8位的大小字母+数字+特殊符号")
    private String clientSecret;

    @ApiModelProperty(value = "客户端权限", name = "scope", example = "auth,api", required = true)
    private String scope;

    @ApiModelProperty(value = "access_token有效时间", name = "accessTokenValidity", required = true, example = "7200")
    private Integer accessTokenValidity;

    @ApiModelProperty(value = "refresh_token有效时间", name = "refreshTokenValidity", required = true, example = "7200")
    private Integer refreshTokenValidity;

    public ClientEntities entities(ClientEntities clientEntities)
    {
        return CreateClientForm.setValue(clientEntities, this.clientId, this.clientSecret, this.scope, this.accessTokenValidity, this.refreshTokenValidity);
    }
}
