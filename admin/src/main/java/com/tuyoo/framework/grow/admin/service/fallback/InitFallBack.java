package com.tuyoo.framework.grow.admin.service.fallback;

import com.tuyoo.framework.grow.admin.service.InitService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
public class InitFallBack implements InitService
{
    @Override
    public String initProject(
            @RequestHeader String ga_user_id,
            @RequestHeader String ga_username,
            @RequestHeader String ga_request_id,
            @RequestHeader String web_request_id,
            @RequestHeader String ga_socket_name,
            @RequestHeader String ga_model_name,
            @RequestHeader String ga_project_id
    )
    {
        return "Init 请求超时";
    }
}
