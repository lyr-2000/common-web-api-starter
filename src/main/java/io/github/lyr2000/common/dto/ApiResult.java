package io.github.lyr2000.common.dto;

/**
 * @Author lyr
 * @create 2021/2/1 21:44
 */
public interface ApiResult {
    /**
     * 获取数据
     * @return
     */
    Object getData();

    /**
     * 前端提示信息
     * @return
     */
    String getMessage();

    /**
     * Api状态码
     * @return
     */
    Integer getCode();
}
