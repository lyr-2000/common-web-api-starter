package com.github.lyr2000.common.handler;

import com.github.lyr2000.common.dto.ViewObject;
import com.github.lyr2000.common.enums.DefaultApiCode;
import com.github.lyr2000.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author lyr
 * @create 2021/2/1 21:55
 */
@Slf4j
@RestControllerAdvice
public class DefaultBasicExceptionHandlerImpl {


    public static final String ERROR_INFO = "errorInfo";



    /**
     * 参数校验异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ViewObject validateException(MethodArgumentNotValidException ex) {
        String frontInfo = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        //将参数校验信息反馈给前端
        return ViewObject.of(DefaultApiCode.BAD_REQUEST)
                .put(ERROR_INFO,frontInfo);
    }

    /**
     * 参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ViewObject ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(";"));
        return ViewObject.of(DefaultApiCode.BAD_REQUEST)
                .put(ERROR_INFO,message);
    }

    /**
     * 前端 json报文有问题
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ViewObject notReadable(HttpMessageNotReadableException e) {

        return ViewObject.of(DefaultApiCode.BAD_REQUEST)
                .put(ERROR_INFO,e.getMessage())
                .put("tips","请求的语法出错[字段不符合格式需求]");
    }


    /**
     * 不支持对应的 url 方法
     * @param ex
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ViewObject validateExceptionsss(HttpRequestMethodNotSupportedException ex) {
        log.info("http method not support");
        //将参数校验信息反馈给前端
        return ViewObject.of(DefaultApiCode.NO_RESOURCE)
                .put(ERROR_INFO,"请求方法不对")

                ;
    }

    /**
     *  基本异常，不知道什么异常
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ViewObject validateExceptionsssss(Exception ex) {

        //将参数校验信息反馈给前端
        return ViewObject.of(DefaultApiCode.BASIC_ERROR).put("errInfo",ex.getMessage());
    }


    // /**
    //  * 业务层 手动抛的异常
    //  * @param e
    //  * @return
    //  */
    // @ExceptionHandler(BusinessException.class)
    // public  ViewObject bussiness(BusinessException e) {
    //
    //     log.error("出现了runtime异常 {}",e.getMessage());
    //
    //     // System.out.println(e.getClass());
    //
    //
    //     return ViewObject.of(ApiInfo.SERVICE_UN_AVAILABLE)
    //             .put(ERROR_INFO,e.getMessage());
    // }


    /**
     * 运行时异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public  ViewObject runtimeEx(RuntimeException e) {

        log.error("出现了runtime异常 {}",e.getMessage());


        // System.out.println(e.getClass());


        return ViewObject.of(DefaultApiCode.BASIC_ERROR)
                .put(ERROR_INFO,e.getMessage());
    }


    /**
     * 空指针异常
     * @param e
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public  ViewObject nullPointerEx(Exception e) {
        if( AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null) {
            //如果异常类上被打上了 ResponseStatus 注解，那么按照异常注解上的状态执行，抛出异常，交给 springboot 自动处理
            throw new RuntimeException(e);
        }
        log.error(Arrays.toString(e.getStackTrace()));
        log.error("出现了 空指针异常 {}",e.getMessage());

        // System.out.println(e.getClass());


        return ViewObject.of(DefaultApiCode.SERVER_ERROR)
                .put(ERROR_INFO,e.getMessage())
                .put("info","空指针异常");
    }

    // /**
    //  * shiro 权限校验异常
    //  * 权限不足
    //  * @param exception
    //  * @return
    //  */
    // @ExceptionHandler(UnauthorizedException.class)
    // public ViewObject unAuthError(UnauthorizedException exception) {
    //     return ViewObject
    //             .of(ApiInfo.FORBIDDEN_REQUEST)
    //             .put(ERROR_INFO,exception.getMessage());
    // }


    // /**
    //  * json 格式异常
    //  * @param e
    //  * @return
    //  */
    // @ExceptionHandler(RequestBodyErrorException.class)
    // public ViewObject jsonBodyErr(RequestBodyErrorException e) {
    //
    //     return ViewObject.of(ApiInfo.BAD_REQUEST)
    //             .put(ERROR_INFO,e.getMessage())
    //             .put("tips","请求体不符合要求，不是json或者不是formData");
    // }

    /**
     * 运行时异常
     * @param e
     * @return
     */
    @ExceptionHandler(ApiException.class)
    public ViewObject runtimeEx(ApiException e) {

        log.error("出现了runtime异常 {}",e.getMessage());


        // System.out.println(e.getClass());


        return new ViewObject()
                .setCode(e.getCode())
                .setMessage(e.getMessage());

    }
}
