package io.github.lyr2000.common.dto;

import cn.hutool.core.util.StrUtil;
import io.github.lyr2000.common.web.Request;
import com.github.pagehelper.IPage;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.servlet.http.HttpServletRequest;

/**
 * 对前端 请求的 Page  size 等参数进行了封装，方便接口处理
 * @Author lyr
 * @create 2021/2/2 1:44
 */
@Data
@Accessors(chain = true)
public class PageParam implements IPage {
    private Integer page;
    private Integer size;

    public static PageParam of(int page,int size) {
        PageParam pageParam = new PageParam();
        pageParam.page = page;
        pageParam.size = size;
        return pageParam;
    }
    public static PageParam of(String page,String size){
        return of(Integer.parseInt(page),Integer.parseInt(size));
    }


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
    public static PageParam from(Request request) {
        return request.getPageParam();
    }

    @Override
    public Integer getPageNum() {
        return page;
    }

    @Override
    public Integer getPageSize() {
        return size;
    }

    @Override
    public String getOrderBy() {
        return null;
    }
}
