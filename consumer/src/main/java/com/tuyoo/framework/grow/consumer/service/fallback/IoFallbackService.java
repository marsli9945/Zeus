package com.tuyoo.framework.grow.consumer.service.fallback;

import com.tuyoo.framework.grow.common.entities.ResultEntities;
import com.tuyoo.framework.grow.consumer.service.IoService;
import org.springframework.stereotype.Component;

@Component
public class IoFallbackService implements IoService
{
    @Override
    public ResultEntities<String> io0()
    {
        return ResultEntities.failed("8002调用超时：this is ioFallbackService io0");
    }

    @Override
    public ResultEntities<String> io500()
    {
        return ResultEntities.failed("8002调用超时：this is ioFallbackService io500");
    }

    @Override
    public ResultEntities<String> io1000()
    {
        return ResultEntities.failed("8002调用超时：this is ioFallbackService io1000");
    }

    @Override
    public ResultEntities<String> io3000()
    {
        return ResultEntities.failed("8002调用超时：this is ioFallbackService io3000");
    }

    @Override
    public ResultEntities<String> io5000()
    {
        return ResultEntities.failed("8002调用超时：this is ioFallbackService io5000");
    }
}
