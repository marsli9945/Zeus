package com.tuyoo.framework.grow.admin.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Table
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
    private String authorizedGrantTypes = "authorization_code,refresh_token,password";

    @Column
    private String authorities = "ROLE_TRUSTED_CLIENT";

    @Column
    private Integer accessTokenValidity;

    @Column
    private Integer refreshTokenValidity;
}
