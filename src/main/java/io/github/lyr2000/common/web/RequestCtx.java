package io.github.lyr2000.common.web;

import io.github.lyr2000.common.dto.PageParam;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @Author lyr
 * @create 2021/2/11 14:55
 */
@AllArgsConstructor
@Component
public class RequestCtx implements Request{
    private final HttpServletRequest request;
    /**
     * 获取page分页参数
     *
     * @return
     */
    @Override
    public PageParam getPageParam() {
        return PageParam.from(request);
    }

}
