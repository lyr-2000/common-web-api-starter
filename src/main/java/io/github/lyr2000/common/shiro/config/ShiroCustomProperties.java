package io.github.lyr2000.common.shiro.config;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import java.util.LinkedHashMap;
import java.util.List;

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
    /**
     * 自定义 realm
     */
    private List<Realm> realmList;

    private List<CustomFilterFactory> filterFactoryList;


    private String loginUrl;

    private String unauthorizedUrl;
    private String successUrl;

}
