package io.github.lyr2000.common.dto;

import com.github.pagehelper.PageInfo;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 对 pageHelper 结果集的封装
 * @Author lyr
 * @create 2021/2/2 1:19
 */
@Data
@Accessors(chain = true)
public class PageResult<T> {
    Integer curPage;
    Integer size;
    Integer totalPage;
    Long totalCount;
    List<T> data;

    /**
     * 私有化构造器，没办法直接 new
     */
    private PageResult() {

    }


    /**
     * 封装pageInfo 参数
     * @param pageInfo
     * @return
     */
    public static <T> PageResult<T> from(@NonNull PageInfo<T> pageInfo) {
        PageResult<T> result = new PageResult<>();
        //当前页面
        result.curPage = pageInfo.getPageNum();
        result.totalPage = pageInfo.getPages();
        //总记录数
        result.size = pageInfo.getPageSize();
        result.totalCount = pageInfo.getTotal();
        result.setData(pageInfo.getList());

        return result;

    }
    public static <T> PageResult<T> from(@NonNull List<T> data) {
        return from(new PageInfo(data));
    }


}
