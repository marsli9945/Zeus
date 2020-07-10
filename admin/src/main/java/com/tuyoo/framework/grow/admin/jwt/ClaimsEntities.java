package com.tuyoo.framework.grow.admin.jwt;

import lombok.Data;

import java.util.List;

@Data
public class ClaimsEntities
{
    private String exp;
    private String jti;
    private String clientId;
    private String userName;

    private List<String> scope;
    private List<String> authorities;
}
