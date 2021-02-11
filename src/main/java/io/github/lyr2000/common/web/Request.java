package io.github.lyr2000.common.web;

import io.github.lyr2000.common.dto.PageParam;

/**
 * @Author lyr
 * @create 2021/2/11 14:54
 */
public interface Request {
    /**
     * 获取page分页参数
     * @return
     */
    PageParam getPageParam();

}
