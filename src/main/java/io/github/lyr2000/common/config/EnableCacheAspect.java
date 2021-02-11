package io.github.lyr2000.common.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启 自定义缓存组件
 * @Author lyr
 * @create 2021/2/1 23:40
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(CachingAspectJAutoConfig.class)
public @interface EnableCacheAspect {
}
