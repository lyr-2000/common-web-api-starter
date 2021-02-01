package com.github.lyr2000.common.aop.aspect;

import cn.hutool.cache.Cache;
import cn.hutool.core.util.StrUtil;
import com.github.lyr2000.common.aop.LocalCache;
import com.github.lyr2000.common.aop.LocalCacheExpire;
import com.github.lyr2000.common.util.SpelUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;


import java.lang.reflect.Method;


/**
 * @Author lyr
 * @create 2021/2/1 22:25
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LocalCacheAspect {

    @Pointcut("@annotation(com.github.lyr2000.common.aop.LocalCache)")
    public void localCacheAop() {

    }


    @Pointcut("@annotation(com.github.lyr2000.common.aop.LocalCacheExpire)")
    public void expireCache() {

    }

    final Cache cache;

    @Around("localCacheAop()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            Method curMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
            LocalCache cacheConfig = curMethod.getAnnotation(LocalCache.class);
            String condition = cacheConfig.condition();

            String keyName = SpelUtil.generateKeyBySpEL(cacheConfig.cacheKey(),joinPoint);
            String express = cacheConfig.condition();

            log.info("keyName = {}",keyName);
             if (StrUtil.isNotBlank(express)){
                String test = SpelUtil.generateKeyBySpEL(express,joinPoint);
                if ("false".equals(test)) {
                    //如果条件不满足
                    log.info("eq false cache");
                    return joinPoint.proceed();
                }
            }
            Object result = cache.get(keyName);

            if (result==null) {
                result = joinPoint.proceed();
                if (result!=null) {
                    cache.put(keyName,result,cacheConfig.unit().toDuration(cacheConfig.duration()).toMillis());
                }
            }
            return result;
        }catch (Exception ex) {
            log.error("aop 异常 {}",ex.getMessage());
            throw ex;
        }


    }


    @Around("expireCache()")
    public Object around22(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            Method curMethod = ((MethodSignature)joinPoint.getSignature()).getMethod();
            LocalCacheExpire cacheConfig = curMethod.getAnnotation(LocalCacheExpire.class);
            String keyName = SpelUtil.generateKeyBySpEL(cacheConfig.cacheKey(),joinPoint);
            if (cacheConfig.duration()==0) {
                cache.remove(keyName);
            }else{
                Object obj = cache.get(keyName);
                if (obj!=null ) {
                    cache.put(keyName,obj,cacheConfig.unit().toDuration(cacheConfig.duration()).toMillis());
                }
            }
            return joinPoint.proceed();
        }catch (Exception ex) {
            log.error("aop 异常 {}",ex.getMessage());
            throw ex;
        }


    }

}
