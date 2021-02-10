package com.github.lyr2000.common.dto;

import com.github.pagehelper.PageInfo;
import lombok.*;

/**
 * @Author lyr
 * @create 2021/2/2 1:19
 */
@Data
@NoArgsConstructor
public class PageResult {
    Integer curPage;
    Integer size;
    Integer totalPage;
    Long totalCount;
    Object data;


    public static PageResult from(@NonNull PageInfo pageInfo) {
        PageResult result = new PageResult();
        //当前页面
        result.curPage = pageInfo.getPageNum();
        result.totalPage = pageInfo.getPages();
        //总记录数
        result.totalCount = pageInfo.getTotal();
        result.setData(pageInfo.getList());

        return result;

    }


}
