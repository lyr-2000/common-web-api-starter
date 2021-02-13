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

    /**
     * 创建 pageParam对象
     * @param request
     * @return
     */
    private static PageParam create(HttpServletRequest request) {
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        PageParam pageParam = new PageParam();
        if (StrUtil.isBlank(pageStr)) {
            pageParam.page = 1;
        } else {
            pageParam.page = Integer.valueOf(pageStr);
        }

        if (StrUtil.isBlank(sizeStr)) {
            pageParam.size = 10;
        } else {
            pageParam.size = Integer.valueOf(sizeStr);
        }

        return pageParam;
    }

    /**
     * 默认最大返回 30 ，对前端参数进行校验
     * @param request
     * @return
     */
    public static PageParam from(HttpServletRequest request) {
        return of(request,30);

    }

    /**
     * 默认最大页数 由用户设置
     * @param request
     * @param maxSize
     * @return
     */
    public static PageParam of(HttpServletRequest request,int maxSize) {
        PageParam pageParam = create(request);
        if (pageParam.size>maxSize) {
            pageParam.size = maxSize;
        }
        return pageParam;
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
