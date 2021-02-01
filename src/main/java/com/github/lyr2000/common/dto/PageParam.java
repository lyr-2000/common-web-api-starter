package com.github.lyr2000.common.dto;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author lyr
 * @create 2021/2/2 1:44
 */
@Data
@Accessors(chain = true)
public class PageParam {
    private Integer page;
    private Integer size;

    public static PageParam from(HttpServletRequest request) {
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        PageParam pageParam = new PageParam();
        if (StrUtil.isBlank(pageStr)) pageParam.page = 1;
        else pageParam.page = Integer.valueOf(pageStr);

        if (StrUtil.isBlank(sizeStr)) pageParam.size = 10;
        else pageParam.size = Integer.valueOf(sizeStr);
        return pageParam;

    }
}
