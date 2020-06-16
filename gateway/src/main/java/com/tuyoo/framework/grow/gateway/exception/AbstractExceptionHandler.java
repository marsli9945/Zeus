package com.tuyoo.framework.grow.gateway.exception;

import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

public class AbstractExceptionHandler
{
    private static final String DEFAULT_ERROR_CODE = "404";

    protected String formatMessage(Throwable ex)
    {
        String errorMessage;
        if (ex instanceof NotFoundException)
        {
            errorMessage = ((NotFoundException) ex).getReason();
        }
        else if (ex instanceof ResponseStatusException)
        {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            errorMessage = responseStatusException.getMessage();
        }
        else
        {
            errorMessage = ex.getMessage();
        }
        return errorMessage;
    }

    protected Map<String, Object> buildErrorMap(String errorMessage)
    {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", DEFAULT_ERROR_CODE);
        resMap.put("message", errorMessage);
        resMap.put("data", null);
        return resMap;
    }
}
