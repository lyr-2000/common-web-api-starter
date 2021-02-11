package com.github.lyr2000.common.aop.aspect;

import cn.hutool.cache.Cache;
import cn.hutool.core.util.StrUtil;
import com.github.lyr2000.common.aop.LocalCache;
import com.github.lyr2000.common.aop.LocalCacheExpire;
import com.github.lyr2000.common.util.SpelUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

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
            //解析 spel表达式
            String keyName = SpelUtil.generateKeyBySpEL(cacheConfig.cacheKey(),joinPoint);
            String express = cacheConfig.condition();

            log.debug("keyName = {}",keyName);
            //如果表达式为 false ，直接执行
             if (StrUtil.isNotBlank(express)){
                String test = SpelUtil.generateKeyBySpEL(express,joinPoint);
                if ("false".equals(test)) {
                    //如果条件不满足
                    log.debug("eq false cache");
                    return joinPoint.proceed();
                }
            }
             //从缓存中读取数据
            Object result = cache.get(keyName);

            if (result==null) {
                //如果缓存 中没有数据，执行方法
                result = joinPoint.proceed();

                if (result!=null) {
                    //将方法 运行结果缓存起来
                    cache.put(keyName,result,cacheConfig.unit().toDuration(cacheConfig.duration()).toMillis());
                }
            }
            //返回 缓存结果 或者 方法运行的结果
            return result;
        }catch (Exception ex) {
            log.error("aop 异常 {}",ex.getMessage());
            throw ex;
        }


    }


    /**
     * 缓存设置过期
     * @param joinPoint
     * @return
     * @throws Throwable
     */
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
