package io.github.lyr2000.common.shiro.entity;

import io.github.lyr2000.common.shiro.JwtResult;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

/**
 * @Author lyr
 * @create 2021/2/9 21:11
 */
@Data
@Builder
@EqualsAndHashCode
public class JwtToken implements AuthenticationToken {
    /**
     * TOKEN
     */
    private String token;
    private Map<String,Object> data;
    private JwtResult result;

    @Override
    public Object getPrincipal() {
        return this;
    }


    @Override
    public Object getCredentials() {
        return this;
    }
}
