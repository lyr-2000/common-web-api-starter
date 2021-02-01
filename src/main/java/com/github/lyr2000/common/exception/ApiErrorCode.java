package com.github.lyr2000.common.exception;

import com.github.lyr2000.common.enums.ApiCode;

/**
 * 异常码封装
 * @Author lyr
 * @create 2021/1/28 1:51
 */
public interface ApiErrorCode extends ApiCode {
    /**
     * @return 异常消息
     */
    @Override
    String getMessage();

    /**
     * @return 异常码
     */
    @Override
    Integer getCode();
}
