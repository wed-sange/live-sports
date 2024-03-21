package org.sports.springboot.starter.web;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.sports.springboot.starter.base.exception.ErrorCode;
import org.sports.springboot.starter.base.exception.enums.GlobalErrorCodeConstants;
import org.sports.springboot.starter.convention.exception.AbstractException;
import org.sports.springboot.starter.convention.result.Result;

import java.util.Optional;

/**
 * 全局返回对象构造器
 * ErrorCode 代替 org.springframework.http.HttpStatus
 * ErrorCode 分为默认常规GlobalErrorCodeConstants 和自定义义务ServiceErrorCodeRange
 */
public final class Results {

    /**
     * 构造成功响应*
     */
    public static <T> Result<T> success() {
        return new Result<T>()
                .setCode(Result.SUCCESS_CODE)
                .setRequestId(TraceContext.traceId());
    }

    /**
     * 构造带返回数据的成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setCode(Result.SUCCESS_CODE)
                .setRequestId(TraceContext.traceId())
                .setData(data);
    }

    /**
     * 异常返回  默认500
     */
    public static <T> Result<T> failure() {
        return failure(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * 异常返回  默认500 自定义异常描述
     */
    public static <T> Result<T> failure(String errorMessage) {
        return failure(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(), errorMessage);
    }

    /**
     * 异常返回 自定义ErrorCode
     */
    public static <T> Result<T> failure(ErrorCode errorCode) {
        return failure(errorCode.getCode(), errorCode.getMsg());
    }

    /**
     * 异常返回 自定义 errorCode、errorMessage
     */
    public static <T> Result<T> failure(int errorCode, String errorMessage) {
        Result<T> objectResult = new Result<>();
        objectResult.setCode(errorCode)
                .setRequestId(TraceContext.traceId())
                .setMsg(errorMessage);
        return objectResult;
    }
}
