package com.tuyoo.framework.grow.gateway.factory;

import com.tuyoo.framework.grow.gateway.filter.ResultGatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class ResultGatewayFilterFactory extends AbstractGatewayFilterFactory<Object>
{
    @Override
    public GatewayFilter apply(Object config)
    {
        return new ResultGatewayFilter();
    }
}
