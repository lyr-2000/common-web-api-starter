package io.github.lyr2000.common.shiro.config;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.servlet.ShiroFilter;

/**
 * @author lyr
 * @description 工厂类
 * @create 2021-11-11 23:05
 */
public interface CustomFilterFactory {
    String getFilterName();

    BasicHttpAuthenticationFilter getFilter();
}
