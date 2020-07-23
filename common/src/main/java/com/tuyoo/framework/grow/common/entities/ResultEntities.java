package com.tuyoo.framework.grow.common.entities;

import lombok.ToString;

import java.io.Serializable;

/**
 * 通用返回对象
 * Created by macro on 2019/4/19.
 */
@ToString
public class ResultEntities<T> implements Serializable
{
    private Integer code;
    private String message;
    private T data;

    protected ResultEntities()
    {
    }

    protected ResultEntities(Integer code, String message, T data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 结构体初始化方法，代替构造函数
     *
     * @param code    状态码
     * @param message 提示消息
     * @param data    返回结果
     * @return 统一返回消息
     */
    public static <T> ResultEntities<T> init(Integer code, String message, T data)
    {
        return new ResultEntities<>(code, message, data);
    }

    /**
     * 成功返回结果
     *
     */
    public static <T> ResultEntities<T> success()
    {
        return new ResultEntities<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ResultEntities<T> success(T data)
    {
        return new ResultEntities<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ResultEntities<T> success(T data, String message)
    {
        return new ResultEntities<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> ResultEntities<T> failed(IErrorCode errorCode)
    {
        return new ResultEntities<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> ResultEntities<T> failed(IErrorCode errorCode, String message)
    {
        return new ResultEntities<>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> ResultEntities<T> failed(String message)
    {
        return new ResultEntities<>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> ResultEntities<T> failed()
    {
        return failed(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> ResultEntities<T> validateFailed()
    {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> ResultEntities<T> validateFailed(String message)
    {
        return new ResultEntities<>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param data 提示信息集合
     */
    public static <T> ResultEntities<T> validateFailed(T data)
    {
        return new ResultEntities<>(ResultCode.VALIDATE_FAILED.getCode(), ResultCode.VALIDATE_FAILED.getMessage(), data);
    }

    /**
     * 未登录返回结果
     */
    public static <T> ResultEntities<T> unauthorized(T data)
    {
        return new ResultEntities<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> ResultEntities<T> forbidden(T data)
    {
        return new ResultEntities<>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}
