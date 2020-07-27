package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.form.InitForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(value = "INIT")
public interface InitService
{
    @PostMapping("/init")
    String initProject(@RequestHeader InitForm initForm);
}
