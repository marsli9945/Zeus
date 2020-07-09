package com.tuyoo.framework.grow.admin.jwt;

import com.alibaba.fastjson.JSON;
import com.tuyoo.framework.grow.admin.ga.GaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class JwtUtil
{
    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    GaConfig gaConfig;

    public ClaimsEntities getClaims()
    {
        String claims = httpServletRequest.getHeader("claims");
        return JSON.parseObject(claims, ClaimsEntities.class);
    }

    public List<String> getRoleList()
    {
        return getClaims().getAuthorities();
    }

    public boolean isGaAdmin()
    {
        List<String> roleList = getRoleList();
        for (String role : roleList)
        {
            if (role.equals(gaConfig.getRoleName()))
            {
                return true;
            }
        }
        return false;
    }
}
