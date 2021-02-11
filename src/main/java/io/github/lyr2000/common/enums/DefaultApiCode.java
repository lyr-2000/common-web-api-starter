package io.github.lyr2000.common.enums;

/**
 * 这套接口规定的 默认规范，如果要改，只能 另外写一套实现了
 * 暂时没办法 完全解耦
 * @Author lyr
 * @create 2021/2/1 21:37
 */

public enum DefaultApiCode implements ApiCode{
    BUSINESS_ERROR_BUSY(505, "服务繁忙"),
    BASIC_ERROR(500, "服务运行异常"),


    OK(200, "OK"),

    NO_RESOURCE(404,"找不到资源"),

    BAD_REQUEST(400,"请求报文语法错误[参数校验失败]"),
    FORBIDDEN_REQUEST(403,"没有接口权限"),

    SERVER_ERROR(500,"服务异常"),
    SERVICE_UN_AVAILABLE(503,"服务不可用"),



    NO_TOKEN(1000, "没有token验证"),
    TOKEN_EXPIRED(1001, "token过期"),

    NEW_USER(1002, "新用户"),
    TOKEN_ERROR(1003,"token验证失败"),


    ACCOUNT_FREEZE(1600,"账号冻结"),

    TokenCheckFail(1611,"身份校验失败"),
    TokenExpired(1612,"身份校验过期"),
    RouterBindingException(1613,"路由参数绑定异常"),
    NoHandlerException(1614,"没有对应访问路径");




    Integer code;
    String message;

    DefaultApiCode(int i, String msg) {
        this.code = i;
        this.message = msg;
    }


    /**
     * @return message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @return code
     */
    @Override
    public Integer getCode() {
        return code;
    }
}
