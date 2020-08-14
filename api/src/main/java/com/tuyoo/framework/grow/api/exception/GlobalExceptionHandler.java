package com.tuyoo.framework.grow.api.exception;

import com.tuyoo.framework.grow.common.entities.ResultCode;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler
{

    /**
     * -------- 通用异常处理方法 --------
     **/
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultEntities<Object> error(Exception e)
    {
        e.printStackTrace();
        return ResultEntities.failed(e.getMessage());    // 通用异常结果
    }

    /**
     * -------- 指定异常处理方法 --------
     * 空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResultEntities<Object> error(NullPointerException e)
    {
        e.printStackTrace();
        return ResultEntities.failed(ResultCode.NULL_POINT);
    }

    /**
     * http连接异常
     */
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public ResultEntities<Object> error(HttpClientErrorException e)
    {
        e.printStackTrace();
        return ResultEntities.failed(ResultCode.HTTP_CLIENT_ERROR);
    }

    /**
     * RequestBody 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultEntities<Object> error(MethodArgumentNotValidException ex)
    {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null;
        return ResultEntities.validateFailed(fieldError.getField() + fieldError.getDefaultMessage());
    }

    /**
     * query 参数校验
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResultEntities<Object> error(BindException bindException)
    {
        ArrayList<String> messages = new ArrayList<>();
        for (ObjectError error : bindException.getBindingResult().getAllErrors())
        {
            messages.add(error.getDefaultMessage());
        }
        return ResultEntities.validateFailed(messages);
    }




    /**
     * -------- 自定义定异常处理方法 --------
     **/
    @ExceptionHandler(CMSException.class)
    @ResponseBody
    public ResultEntities<Object> error(CMSException e)
    {
        e.printStackTrace();
        return ResultEntities.init(e.getCode(), e.getMessage(), null);
    }
}
