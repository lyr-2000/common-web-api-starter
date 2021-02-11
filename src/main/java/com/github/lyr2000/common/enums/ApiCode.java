package com.github.lyr2000.common.enums;

/**
 * 通用状态码 抽象，实现 ApiCode 可以自定义自己的抽象
 * 和 业务规范
 * @Author lyr
 * @create 2021/2/1 21:36
 */
public interface ApiCode {
    /**
     * @return message
     */
    String getMessage();

    /**
     * @return code
     */
    Integer getCode();
}
