package io.github.lyr2000.common.shiro.config;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

/**
 * shiro 通用配置类
 *
 * @Author lyr
 * @create 2021/2/9 21:27
 */
@Data
@Accessors(chain = true)
public class ShiroCustomProperties {
    /**
     * jwt密钥
     */
    @NonNull
    private String jwtSecret;
    /**
     * 请求头 Token 字段
     */
    @NonNull
    private String tokenHeader;

    /**
     * api 路径权限定义
     *
     *  // 登录接口放开
     *  filterChainDefinitionMap.put("/user/login", "anon");
     *
     */
    private LinkedHashMap<String,String> customFilterChain;


    private String loginUrl;

    private String unauthorizedUrl;
    private String successUrl;

}
