package com.tuyoo.framework.grow.admin.service.fallback;

import com.tuyoo.framework.grow.admin.form.InitForm;
import com.tuyoo.framework.grow.admin.service.InitService;

public class InitFallBack implements InitService
{
    @Override
    public String initProject(InitForm initForm)
    {
        return "请求超时";
    }
}
