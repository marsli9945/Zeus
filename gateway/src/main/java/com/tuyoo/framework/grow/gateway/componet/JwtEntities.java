package com.tuyoo.framework.grow.gateway.componet;

import java.util.List;

public class JwtEntities
{
    private String exp;
    private String jti;
    private String client_id;
    private String user_name;

    private List<String> scope;
    private List<String> authorities;

    public String getExp()
    {
        return exp;
    }

    public void setExp(String exp)
    {
        this.exp = exp;
    }

    public String getJti()
    {
        return jti;
    }

    public void setJti(String jti)
    {
        this.jti = jti;
    }

    public String getClient_id()
    {
        return client_id;
    }

    public void setClient_id(String client_id)
    {
        this.client_id = client_id;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }

    public List<String> getScope()
    {
        return scope;
    }

    public void setScope(List<String> scope)
    {
        this.scope = scope;
    }

    public List<String> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(List<String> authorities)
    {
        this.authorities = authorities;
    }
}
