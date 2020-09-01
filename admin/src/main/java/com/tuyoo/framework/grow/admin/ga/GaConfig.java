package com.tuyoo.framework.grow.admin.ga;

import com.tuyoo.framework.grow.admin.ga.entities.GaPermissionEntities;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ToString
@Component
@ConfigurationProperties(prefix = "ga")
public class GaConfig
{
    private Integer roleId;
    private Integer adminId;
    private String roleName;
    private String clientId;
    private String clientSecret;
    private String host;
    private String gaHost;
    private Integer mailTokenExp;
    private List<GaPermissionEntities> permission;
}
