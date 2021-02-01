package com.github.lyr2000.common.handler;

import com.github.lyr2000.common.dto.ViewObject;
import com.github.lyr2000.common.enums.DefaultApiCode;
import com.github.lyr2000.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author lyr
 * @create 2021/2/1 21:54
 */
@Slf4j
@RestControllerAdvice
public class BasicExceptionHandler {

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
