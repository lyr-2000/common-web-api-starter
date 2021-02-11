package io.github.lyr2000.common.shiro;

/**
 * @Author lyr
 * @create 2021/2/9 21:13
 */
public enum JwtResult {
    /**
     * 过期
     */
    OVERDUE,

    /**
     * 校验失败
     */
    Fail,
    /**
     * 校验通过
     */
    OK
}
