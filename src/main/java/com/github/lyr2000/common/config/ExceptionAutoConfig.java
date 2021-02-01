package com.github.lyr2000.common.config;

import com.github.lyr2000.common.handler.BasicExceptionHandler;
import com.github.lyr2000.common.handler.DefaultBasicExceptionHandlerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lyr
 * @create 2021/2/1 23:39
 */
@Configuration
public class ExceptionAutoConfig {

    // @Bean
    // public BasicExceptionHandler basicExceptionHandler() {
    //     return new BasicExceptionHandler();
    // }
    @Bean
    @ConditionalOnMissingBean
    public DefaultBasicExceptionHandlerImpl defaultBasicExceptionHandler() {
        return new DefaultBasicExceptionHandlerImpl();
    }
}
