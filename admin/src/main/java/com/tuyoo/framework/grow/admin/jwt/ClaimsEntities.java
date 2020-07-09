package com.tuyoo.framework.grow.admin.jwt;

import lombok.Data;

import java.util.List;

@Data
public class ClaimsEntities
{
    private String exp;
    private String jti;
    private String client_id;
    private String user_name;

    private List<String> scope;
    private List<String> authorities;
}
