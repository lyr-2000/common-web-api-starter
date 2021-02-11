package io.github.lyr2000.common.aop;

import io.github.lyr2000.common.enums.Unit;

import java.lang.annotation.*;

/**
 * @Author lyr
 * @create 2021/2/1 22:29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LocalCacheExpire {

    String cacheKey();
    long duration() default 0L;
    Unit unit();
    // String expireCondition() default "";

}
