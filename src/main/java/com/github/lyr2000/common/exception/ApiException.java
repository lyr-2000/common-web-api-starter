package com.github.lyr2000.common.exception;

import com.github.lyr2000.common.enums.ApiCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * https://www.bilibili.com/video/BV1r4411r7au?p=38
 * @Author lyr
 * @create 2021/1/28 1:55
 */
@Getter
public class ApiException extends RuntimeException{
    private String message;
    private Integer code;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiException that = (ApiException) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return code != null ? code.equals(that.code) : that.code == null;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }
    public ApiException(@NonNull ApiCode errCode) {
        this.code = errCode.getCode();
        this.message = errCode.getMessage();
    }
    public ApiException(@NonNull ApiErrorCode errCode) {
        this.code = errCode.getCode();
        this.message = errCode.getMessage();
    }
    public static ApiException from(@NonNull ApiErrorCode customErrCode) {
        return new ApiException(customErrCode);
    }
}
