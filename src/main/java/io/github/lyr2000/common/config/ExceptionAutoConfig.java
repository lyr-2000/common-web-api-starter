package io.github.lyr2000.common.config;

import io.github.lyr2000.common.handler.DefaultBasicExceptionHandlerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义 异常 切面逻辑处理
 *
 *
 * @Author lyr
 * @create 2021/2/1 23:39
 */
@Configuration
public class ExceptionAutoConfig {


    /**
     * 对异常的切面 处理默认实现
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultBasicExceptionHandlerImpl defaultBasicExceptionHandler() {
        return new DefaultBasicExceptionHandlerImpl();
    }
}
