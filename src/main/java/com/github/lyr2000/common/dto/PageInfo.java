package com.github.lyr2000.common.dto;

import com.github.pagehelper.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author lyr
 * @create 2021/2/4 23:26
 */
@Data
@Accessors(chain = true)
public class PageInfo<T> {

    private Integer totalPage;
    private Integer curPage;
    private Integer size;
    private Long totalCount;

    private List<T> list;

    public static <T> PageInfo<T> of(Page<T> ipage) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.curPage = ipage.getPageNum();
        pageInfo.size = ipage.getPageSize();
        pageInfo.totalCount = ipage.getTotal();
        pageInfo.list = ipage.getResult();
        pageInfo.totalPage = ipage.getPages();
        return pageInfo;
    }


    // public static <T> PageInfo<T> of(org.springframework.data.domain.Page<T> ipage) {
    //     PageInfo<T> pageInfo = new PageInfo<>();
    //     pageInfo.curPage = ipage.getNumber() + 1;
    //     pageInfo.size = ipage.getSize();
    //     pageInfo.totalCount = ipage.getTotalElements();
    //     pageInfo.list = ipage.getContent();
    //     pageInfo.totalPage = ipage.getTotalPages();
    //     return pageInfo;
    // }

}
