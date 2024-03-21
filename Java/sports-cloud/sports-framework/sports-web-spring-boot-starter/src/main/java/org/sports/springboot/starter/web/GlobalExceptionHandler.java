package org.sports.springboot.starter.web;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.sports.springboot.starter.base.exception.enums.GlobalErrorCodeConstants;
import org.sports.springboot.starter.base.utils.MessageUtils;
import org.sports.springboot.starter.convention.exception.AbstractException;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.convention.result.Result;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理 SpringMVC 请求参数缺失
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result<Void> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.error("MissingServletRequestParameterException", ex);
        return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     * <p>
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException", ex);
        return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), String.format("请求参数类型错误:%s", ex.getMessage()));
    }

    /**
     * 处理 SpringMVC 参数校验不正确
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException", ex);
        FieldError fieldError = ex.getFieldError();
        if (Objects.nonNull(fieldError)) {
            String defaultMessage = fieldError.getDefaultMessage();
            String field = fieldError.getField();
            return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), field + ":" + defaultMessage);
        }
        return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
    }

    /**
     * 处理 SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> bindExceptionHandler(BindException ex) {
        log.error("BindException", ex);
        FieldError fieldError = ex.getFieldError();
        return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), String.format(MessageUtils.get("401-1"), fieldError.getDefaultMessage()));
    }

    /**
     * 处理 Dubbo Consumer 本地参数校验时，抛出的 ValidationException 异常
     */
    @ExceptionHandler(value = ValidationException.class)
    public Result<Void> validationException(ValidationException ex) {
        log.error("ValidationException", ex);
        //TODO 无法拼接明细的错误信息，因为 Dubbo Consumer 抛出 ValidationException 异常时，是直接的字符串信息，且人类不可读
        return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
    }

    /**
     * 处理 SpringMVC 请求地址不存在
     * <p>
     * 注意，它需要设置如下两个配置项：
     * 1. spring.mvc.throw-exception-if-no-handler-found 为 true
     * 2. spring.mvc.static-path-pattern 为 /statics/**
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Void> noHandlerFoundExceptionHandler(NoHandlerFoundException ex) {
        log.error("NoHandlerFoundException", ex);
        return Results.failure(GlobalErrorCodeConstants.NOT_FOUND);
    }

    /**
     * 处理 SpringMVC 请求方法不正确
     * <p>
     * 例如说，A 接口的方法为 GET 方式，结果请求方法为 POST 方式，导致不匹配
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException", ex);
        return Results.failure(GlobalErrorCodeConstants.METHOD_NOT_ALLOWED);
    }


    /**
     * 处理业务异常 ServiceException
     * <p>
     * 例如说，商品库存不足，用户手机号已存在。
     */
    @ExceptionHandler(value = ServiceException.class)
    public Result<Void> serviceExceptionHandler(ServiceException ex) {
        log.error("ServiceException", ex);
        return Results.failure(ex.getErrorCode(), ex.getErrorMessage());
    }

    /**
     * 处理未登录 NotLoginException
     */
    @ExceptionHandler(value = NotLoginException.class)
    public Result<Void> notLoginExceptionHandler(NotLoginException ex) {
        log.error("NotLoginException", ex);
        return Results.failure(GlobalErrorCodeConstants.UNAUTHORIZED);
    }

    /**
     * 处理无角色权限 NotRoleException
     */
    @ExceptionHandler(value = NotRoleException.class)
    public Result<Void> notRoleExceptionHandler(NotRoleException ex) {
        log.error("NotRoleException", ex);
        return Results.failure(GlobalErrorCodeConstants.FORBIDDEN);
    }

    /**
     * 处理无操作权限 NotPermissionException
     */
    @ExceptionHandler(value = NotPermissionException.class)
    public Result<Void> notPermissionExceptionHandler(NotPermissionException ex) {
        log.error("NotPermissionException", ex);
        return Results.failure(GlobalErrorCodeConstants.FORBIDDEN);
    }

    /**
     * 处理系统异常，兜底处理所有的一切
     */
    @ExceptionHandler(value = Exception.class)
    public Result<Void> defaultExceptionHandler(HttpServletRequest req, Exception ex) {
        log.error("Exception", ex);
        return Results.failure(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理系统异常，兜底处理所有的一切
     */
    @ExceptionHandler(value = InvalidFormatException.class)
    public Result<Void> defaultExceptionHandler(HttpServletRequest req, InvalidFormatException ex) {
        log.error("Exception", ex);
        return Results.failure(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
    }

    /**
     * JSON格式转换问题
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result<Void> defaultExceptionHandler(HttpServletRequest req, HttpMessageNotReadableException ex) {
        log.error("Exception", ex);
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException exception = ((InvalidFormatException) cause);
            List<JsonMappingException.Reference> path = exception.getPath();
            for (JsonMappingException.Reference reference : path) {
                String fieldName = reference.getFieldName();
                if (Objects.nonNull(fieldName)) {
                    return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST.getCode(), "参数【" + fieldName + "】值格式有误:" + exception.getValue());
                }
            }
        }
        return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
    }

    /**
     * 拦截应用内抛出的异常
     */
    @ExceptionHandler({AbstractException.class})
    public Result<Void> abstractException(AbstractException ex) {
        log.error("AbstractException", ex);
        return Results.failure(ex.getErrorCode(), ex.getErrorMessage());
    }

}
