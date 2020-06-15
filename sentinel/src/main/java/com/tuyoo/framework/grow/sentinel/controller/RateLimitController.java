package com.tuyoo.framework.grow.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import com.tuyoo.framework.grow.sentinel.handler.CustomerBlockHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController
{
    @GetMapping("/byResource")
    @SentinelResource(value = "byResource", blockHandler = "handleException")
    public ResultEntities<Object> byResource()
    {
        return ResultEntities.success("this is byResource");
    }

    public ResultEntities<Object> handleException(BlockException e)
    {
        return ResultEntities.failed(e.getClass().getCanonicalName() + "\t 服务不可用");
    }

    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException2")
    public ResultEntities<Object> customerBlockHandler()
    {
        return ResultEntities.success("this is customerBlockHandler");
    }
}
