package com.github.lyr2000.common.shiro.config;

/**
 * 定义一些 shiro的常量值，避免直接 使用魔法值
 * @Author lyr
 * @create 2021/2/9 22:18
 */
public interface ShiroConstant {
    /**
     * jwt 解密获得 Map , 设置到 attribute的名字
     */
    String requestAttrName = "___token_data";
    /**
     * 任何人可以访问
     */
    String anon = "anon";
    /**
     *登录后才能访问
     */
    String authc = "authc";
    /**
     * 必须拥有对应的权限
     */
    String perms = "perms[]";
    /**
     * 经过 jwt拦截器才能访问
     */
    String jwt = "jwt";
    /**
     * 登录用户才能访问
     */
    String user = "user";

    /**
     * 必须是ssl
     */
    String ssl = "ssl";
}
