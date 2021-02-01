package com.github.lyr2000.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.lyr2000.common.enums.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @Author lyr
 * @create 2021/2/1 21:43
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T>  implements ApiResult{


    private Integer code;//返回码

    private String message;//返回消息


    private T data;//返回数据

    public static <T>Result<T> from(ApiCode apiCode) {
        return (Result<T>) Result.builder()
                .code(apiCode.getCode())
                .message(apiCode.getMessage())
                .build();
    }
    public static <T>Result<T> of(ApiCode apiCode, T data) {
        return (Result<T>) Result.builder()
                .message(apiCode.getMessage())
                .code(apiCode.getCode())
                .data(data)
                .build();
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
