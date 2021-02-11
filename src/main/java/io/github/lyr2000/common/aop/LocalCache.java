package io.github.lyr2000.common.aop;

import io.github.lyr2000.common.enums.Unit;

import java.lang.annotation.*;

/**
 * @Author lyr
 * @create 2021/2/1 22:15
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LocalCache {
    Unit unit();
    long duration();
    String cacheKey();
    String condition() default "";
}
