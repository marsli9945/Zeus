package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.service.fallback.InitFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(value = "INIT", fallback = InitFallBack.class)
public interface InitService
{
    @PostMapping("/init")
    String initProject(
            @RequestHeader String ga_user_id,
            @RequestHeader String ga_username,
            @RequestHeader String ga_request_id,
            @RequestHeader String web_request_id,
            @RequestHeader String ga_socket_name,
            @RequestHeader String ga_model_name,
            @RequestHeader String ga_project_id
    );
}
