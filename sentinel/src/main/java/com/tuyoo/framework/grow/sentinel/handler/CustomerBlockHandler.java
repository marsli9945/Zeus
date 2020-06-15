package com.tuyoo.framework.grow.sentinel.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.tuyoo.framework.grow.common.entities.ResultEntities;

public class CustomerBlockHandler
{
    public static ResultEntities<Object> handlerException(BlockException exception)
    {
        return ResultEntities.failed("Global handlerException");
    }

    public static ResultEntities<Object> handlerException2(BlockException exception)
    {
        return ResultEntities.failed("Global handlerException2");
    }
}
