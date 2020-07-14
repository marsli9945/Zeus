package com.tuyoo.framework.grow.auth.form;

import com.tuyoo.framework.grow.auth.entities.ClientEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("客户端账号表单")
public class ClientForm
{
    @ApiModelProperty(value = "客户端id", name = "clientId", required = true, example = "test_client")
    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    @ApiModelProperty(value = "客户端密码", name = "clientSecret", required = true, example = "123456")
    @NotBlank(message = "客户端密码不能为空")
    private String clientSecret;

    @ApiModelProperty(value = "客户端权限", name = "scope", example = "read,write", required = true)
    @NotBlank(message = "权限列表不能为空")
    private String scope;

    @ApiModelProperty(value = "可用授权类型", name = "authorizedGrantTypes", example = "authorization_code,refresh_token,password")
    private String authorizedGrantTypes;

    @ApiModelProperty(value = "客户端角色", name = "authorities", example = "ROLE_TRUSTED_CLIENT")
    private String authorities;

    @ApiModelProperty(value = "access_token有效时间", name = "accessTokenValidity", required = true, example = "7200")
    @NotNull(message = "access_token有效时间为必填项")
    private Integer accessTokenValidity;

    @ApiModelProperty(value = "refresh_token有效时间", name = "refreshTokenValidity", required = true, example = "7200")
    @NotNull(message = "refresh_token有效时间为必填项")
    private Integer refreshTokenValidity;

    public ClientEntities entities()
    {
        ClientEntities clientEntities = new ClientEntities();

        if (this.clientId != null)
        {
            clientEntities.setClientId(this.clientId);
        }

        if (this.clientSecret != null)
        {
            clientEntities.setClientSecret(new BCryptPasswordEncoder().encode(this.clientSecret));
        }

        if (this.scope != null)
        {
            clientEntities.setScope(this.scope);
        }

        if (this.authorizedGrantTypes != null)
        {
            clientEntities.setAuthorizedGrantTypes(this.authorizedGrantTypes);
        }

        if (this.authorities != null)
        {
            clientEntities.setAuthorities(this.authorities);
        }

        if (this.accessTokenValidity != null)
        {
            clientEntities.setAccessTokenValidity(this.accessTokenValidity);
        }

        if (this.refreshTokenValidity != null)
        {
            clientEntities.setRefreshTokenValidity(this.refreshTokenValidity);
        }

        return clientEntities;
    }
}
