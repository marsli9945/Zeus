package com.tuyoo.framework.grow.auth.entities;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;

@Table
@ApiModel("客户端ID内容")
@Entity(name = "oauth_client_details")
public class ClientEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String clientId;

    @Column
    private String clientSecret;

    @Column
    private String scope;

    @Column
    private String authorizedGrantTypes;

    @Column
    private String authorities;

    @Column
    private Integer accessTokenValidity;

    @Column
    private Integer refreshTokenValidity;

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

    public void setClientId(String client_id)
    {
        this.clientId = client_id;
    }

    public String getClientSecret()
    {
        return clientSecret;
    }

    public void setClientSecret(String client_secret)
    {
        this.clientSecret = client_secret;
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

    public void setAuthorizedGrantTypes(String authorized_grant_types)
    {
        this.authorizedGrantTypes = authorized_grant_types;
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

    public void setAccessTokenValidity(Integer access_token_validity)
    {
        this.accessTokenValidity = access_token_validity;
    }

    public Integer getRefreshTokenValidity()
    {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refresh_token_validity)
    {
        this.refreshTokenValidity = refresh_token_validity;
    }
}
