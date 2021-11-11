package io.github.lyr2000.common.shiro.config;


import cn.hutool.core.codec.Base64;
import io.github.lyr2000.common.shiro.filter.JwtFilter;
import io.github.lyr2000.common.shiro.realm.JwtRealm;
import io.github.lyr2000.common.shiro.realm.SessionRealm;
import io.github.lyr2000.common.shiro.util.JwtUtil;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lyr
 * @create 2021/2/9 22:13
 */
@Configuration
public class ShiroAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public ShiroCustomProperties shiroProperties() {
        ShiroCustomProperties prop = new ShiroCustomProperties("lyr-2000.blog", "token");
        return prop.setFilterFactoryList(Arrays.asList(new CustomFilterFactory() {
            @Override
            public String getFilterName() {
                return "jwt";
            }

            @Override
            public BasicHttpAuthenticationFilter getFilter() {
                return new JwtFilter(prop,jwtUtil(prop));
            }
        }));
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtUtil jwtUtil(ShiroCustomProperties shiroCustomProperties) {
        return new JwtUtil(shiroCustomProperties);
    }

    // @Bean
    // @ConditionalOnMissingBean
    // public JwtFilter jwtFilter(ShiroCustomProperties shiroCustomProperties, JwtUtil jwtUtil) {
    //
    //     return new JwtFilter(shiroCustomProperties,jwtUtil);
    // }
    @Bean
    @ConditionalOnMissingBean
    public JwtRealm jwtRealm() {
        return new JwtRealm();
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionRealm sessionRealm() {
        return new SessionRealm();
    }

    // /**
    //  * 系统自带的Realm管理，主要针对多realm 认证
    //  */
    // @Bean
    // @ConditionalOnMissingBean
    // public ModularRealmAuthenticator modularRealmAuthenticator() {
    //     //自己重写的ModularRealmAuthenticator
    //     ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator(){
    //
    //         @Override
    //         protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
    //             Collection<Realm> realms =  getRealms();
    //             for (Realm realm: realms) {
    //                 if (realm.supports(authenticationToken)) {
    //                     return doSingleRealmAuthentication(realm,authenticationToken);
    //                 }
    //             }
    //             return super.doAuthenticate(authenticationToken);
    //         }
    //     };
    //     modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
    //     return modularRealmAuthenticator;
    // }

    /**
     * 配置使用自定义Realm，关闭Shiro自带的session
     * 详情见文档 http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
     *
     * @return org.apache.shiro.web.mgt.DefaultWebSecurityManager
     * @author dolyw.com
     * @date 2018/8/31 10:55
     */
    @Bean("securityManager")
    @ConditionalOnMissingBean
    public DefaultWebSecurityManager defaultWebSecurityManager( RememberMeManager rememberMeManager,
                                                                ShiroCustomProperties properties
                                                                // SessionRealm sessionRealm
            /* WebSessionManager webSessionManager*/) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 使用自定义Realm
        // defaultWebSecurityManager.setRealm(jwtRealm);
        if(properties.getRealmList()!=null && properties.getRealmList().size()>0) {
            defaultWebSecurityManager.setRealms(properties.getRealmList());
        }else {
            //使用自带的 jwtRealm 和 sessionRealm
            defaultWebSecurityManager.setRealms(Arrays.asList(jwtRealm(),sessionRealm()));
        }
        // defaultWebSecurityManager.setAuthenticator(modularRealmAuthenticator);
        // defaultWebSecurityManager.setL
        // defaultWebSecurityManager.set
        //defaultWebSecurityManager.setSessionManager(webSessionManager);
        //设置 rememberMe
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager);

        // sessionRealm.setCacheManager(webSessionManager);
        // // 关闭Shiro自带的session
        // DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        // DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // //禁用缓存
        // // defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        // subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        // defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        // 设置自定义Cache缓存

        return defaultWebSecurityManager;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题，https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    @ConditionalOnMissingBean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public Cookie cookieDAO() {
        Cookie cookie = new org.apache.shiro.web.servlet.SimpleCookie();
        cookie.setName("WEBSID");
        cookie.setHttpOnly(true);

        return cookie;
    }


    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager,
                                                         JwtUtil jwtUtil,
                                                         ShiroCustomProperties shiroCustomProperties,
                                                         ShiroCustomProperties properties) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap = new HashMap<>(16);
        //TODO: 改为用工厂模式创建
        // filterMap.put("jwt", new JwtFilter(shiroCustomProperties, jwtUtil));
        if (shiroCustomProperties.getFilterFactoryList()!=null && shiroCustomProperties.getFilterFactoryList().size()>0) {
            for (CustomFilterFactory factory: shiroCustomProperties.getFilterFactoryList()) {
                //设置过滤器
                filterMap.put(factory.getFilterName(),factory.getFilter());
            }
        }
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        if (properties.getLoginUrl() != null) {
            factoryBean.setLoginUrl(properties.getLoginUrl());
        }
        if (properties.getUnauthorizedUrl() != null) {
            factoryBean.setUnauthorizedUrl(properties.getUnauthorizedUrl());
        }
        if (properties.getSuccessUrl() != null) {
            factoryBean.setSuccessUrl(properties.getSuccessUrl());
        }
        // 添加自己的过滤器取名为jwt
        //
        // System.out.println("---------------------------------------------");
        // System.out.println(properties.getCustomFilterChain());

        // 自定义url规则使用LinkedHashMap有序Map
        // LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>(16);
        // JwtProperties properties = jwtProperties();
        // 登录接口放开
        // filterChainDefinitionMap.put("/user/login", "anon");
        // "/api/getToken","/api/login/**","/api/register/**"
        // filterChainDefinitionMap.put("/api/login/email", ShiroConstants.Anon.getContent());
        // filterChainDefinitionMap.put("/api/register/email",ShiroConstants.Anon.getContent());
        // filterChainDefinitionMap.put("/api/login/**","anon");
        // filterChainDefinitionMap.put("/api/login","anon");
        // filterChainDefinitionMap.put("/api/register/**",ShiroConstants.Anon.getContent());
        // filterChainDefinitionMap.put("/api/video/_search",ShiroConstants.Anon.getContent());
        // // 所有请求通过我们自己的JWTFilter
        // filterChainDefinitionMap.put("/api/**", "jwt");
        if (properties.getCustomFilterChain() != null) {
            factoryBean.setFilterChainDefinitionMap(properties.getCustomFilterChain());
        }

        // factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);


        // factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return factoryBean;
    }


    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilterFactoryBean");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }
    //不要使用 defaultWebSessionManager ，因为 在 freemarker 中获取不到值
  /*  @Bean
    @ConditionalOnMissingBean
    public DefaultWebSessionManager getDefaultWebSessionManager(Cookie cookie) {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        //超时时间为2天
        defaultWebSessionManager.setGlobalSessionTimeout(Duration.ofDays(9).toMillis());// 会话过期时间，单位：毫秒(在无操作时开始计时)--->一分钟,用于测试
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        defaultWebSessionManager.setSessionIdCookie(cookie);
        return defaultWebSessionManager;
    }*/

    @Bean
    @ConditionalOnMissingBean
    public RememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        //注入自定义cookie(主要是设置寿命, 默认的一年太长)
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        //设置RememberMe的cookie有效期为7天
        simpleCookie.setMaxAge((int)Duration.ofDays(7).toMillis());
        rememberMeManager.setCookie(simpleCookie);
        //手动设置一个加解密密钥  防止 shiro报错
        rememberMeManager.setCipherKey(Base64.encode("6ZmI6I2j3YR1aSnphpAdmin".getBytes()).getBytes());
        return rememberMeManager;
    }





}
