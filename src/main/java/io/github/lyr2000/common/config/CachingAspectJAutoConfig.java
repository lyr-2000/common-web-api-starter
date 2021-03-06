package io.github.lyr2000.common.config;


import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import io.github.lyr2000.common.aop.aspect.LocalCacheAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 自己实现 的本地缓存抽象
 *
 *
 * @Author lyr
 * @create 2021/2/1 22:26
 */
@Configuration
@EnableAspectJAutoProxy
public class CachingAspectJAutoConfig {
    
    @Bean
    @ConditionalOnMissingBean
    public Cache<String, Object> loadingCacheProxy() {
        return CacheUtil.newLRUCache(60);
    }


    @Bean
    @ConditionalOnMissingBean
    public LocalCacheAspect localCacheAspect() {

        return new LocalCacheAspect(loadingCacheProxy());
    }
}
