package com.github.lyr2000.common.shiro.config;

import com.github.lyr2000.common.shiro.filter.JwtFilter;
import com.github.lyr2000.common.shiro.realm.JwtRealm;
import com.github.lyr2000.common.shiro.util.JwtUtil;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author lyr
 * @create 2021/2/9 22:13
 */
@Configuration
public class JwtAutoConfiguration {
    @Bean

    public JwtProperties jwtProperties() {
        return new JwtProperties("lyr-2000.blog","token");
    }
    @Bean

    public JwtUtil jwtUtil() {
        return new JwtUtil(jwtProperties());
    }
    @Bean

    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtProperties(),jwtUtil());
    }
    @Bean

    public JwtRealm jwtRealm() {
        return new JwtRealm();
    }


    /**
     * 配置使用自定义Realm，关闭Shiro自带的session
     * 详情见文档 http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
     * @return org.apache.shiro.web.mgt.DefaultWebSecurityManager
     * @author dolyw.com
     * @date 2018/8/31 10:55
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean("securityManager")

    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 使用自定义Realm
        defaultWebSecurityManager.setRealm(jwtRealm());
        // 关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        //禁用缓存
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        // 设置自定义Cache缓存

        return defaultWebSecurityManager;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean

    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题，https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean

    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean

    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    // @Resource
    // private JwtUtil jwtUtil;
    // @Resource
    // private JwtFilter jwtFilter;
    // @Resource
    // private JwtProperties jwtProperties;


    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // 添加自己的过滤器取名为jwt
        Map<String, Filter> filterMap = new HashMap<>(16);
        filterMap.put("jwt", jwtFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        // 自定义url规则使用LinkedHashMap有序Map
        // LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>(16);
        JwtProperties properties = jwtProperties();
        // 登录接口放开
        // filterChainDefinitionMap.put("/user/login", "anon");
        // "/api/getToken","/api/login/**","/api/register/**"
        // filterChainDefinitionMap.put("/api/login/email", ShiroConstants.Anon.getContent());
        // filterChainDefinitionMap.put("/api/register/email",ShiroConstants.Anon.getContent());
        // filterChainDefinitionMap.put("/api/login/**",ShiroConstants.Anon.getContent());
        // filterChainDefinitionMap.put("/api/register/**",ShiroConstants.Anon.getContent());
        // filterChainDefinitionMap.put("/api/video/_search",ShiroConstants.Anon.getContent());
        // // 所有请求通过我们自己的JWTFilter
        // filterChainDefinitionMap.put("/api/**", "jwt");
        if (properties.getCustomFilterChain()!=null) {
            factoryBean.setFilterChainDefinitionMap(properties.getCustomFilterChain());
        }


        // factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }











}
