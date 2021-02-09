package com.github.lyr2000.common.shiro.config;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

/**
 * @Author lyr
 * @create 2021/2/9 21:27
 */
@Data
@Accessors(chain = true)
public class JwtProperties {
    @NonNull
    private String jwtSecret;
    @NonNull
    private String tokenHeader;

    private LinkedHashMap<String,String> customFilterChain;


}
