package com.github.lyr2000.common.dto;

/**
 * @Author lyr
 * @create 2021/2/1 21:44
 */
public interface ApiResult {
    Object getData();
    String getMessage();
    Integer getCode();
}
