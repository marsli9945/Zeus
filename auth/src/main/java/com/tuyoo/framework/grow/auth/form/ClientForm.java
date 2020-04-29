package com.tuyoo.framework.grow.auth.form;

import com.tuyoo.framework.grow.auth.entities.ClientEntities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;

@ApiModel("客户端账号表单")
public class ClientForm
{
    private Integer id;

    @ApiModelProperty(value = "客户端id", name = "clientId", required = true, example = "test_client")
    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    @ApiModelProperty(value = "客户端密码", name = "clientSecret", required = true, example = "123456")
    @NotBlank(message = "客户端密码不能为空")
    private String clientSecret;

    @ApiModelProperty(value = "客户端权限", name = "scope", example = "read,write")
    private String scope;

    @ApiModelProperty(value = "可用授权类型", name = "authorizedGrantTypes", example = "authorization_code,refresh_token,password")
    private String authorizedGrantTypes;

    @ApiModelProperty(value = "客户端角色", name = "authorities", example = "ROLE_TRUSTED_CLIENT")
    private String authorities;

    @ApiModelProperty(value = "access_token有效时间", name = "accessTokenValidity", required = true, example = "7200")
    @NotBlank(message = "access_token有效时间为必填项")
    private Integer accessTokenValidity;

    @ApiModelProperty(value = "refresh_token有效时间", name = "refreshTokenValidity", required = true, example = "7200")
    @NotBlank(message = "refresh_token有效时间为必填项")
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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getClientSecret()
    {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret)
    {
        this.clientSecret = clientSecret;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getAuthorizedGrantTypes()
    {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes)
    {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(String authorities)
    {
        this.authorities = authorities;
    }

    public Integer getAccessTokenValidity()
    {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity)
    {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getRefreshTokenValidity()
    {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity)
    {
        this.refreshTokenValidity = refreshTokenValidity;
    }
}
