package com.github.lyr2000.common.shiro.util;

import cn.hutool.core.codec.Base64;

import com.github.lyr2000.common.shiro.JwtResult;
import com.github.lyr2000.common.shiro.config.ShiroCustomProperties;
import com.github.lyr2000.common.shiro.entity.JwtToken;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;

/**
 * @Author lyr
 * @create 2021/2/9 21:22
 */
@RequiredArgsConstructor
public class JwtUtil {
    private final ShiroCustomProperties shiroCustomProperties;
    // private final SecretKey secretKey = generalKey();
    private SecretKey generateKey;
    /**
     * 生成加密秘钥
     *
     * @return 秘钥
     */
    private SecretKey generalKey() {
        if (generateKey==null) {
            synchronized (this) {
                if (generateKey==null) {
                    String stringKey =  shiroCustomProperties.getJwtSecret();
                    // 秘钥
                    byte[] encodedKey = Base64.decode( stringKey);
                    this.generateKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
                }
            }
        }

        return generateKey;
    }


    /**
     * 生成 jwt
     * @param jwtData
     */
    public String sign(Map<String,String> jwtData, long ttlMillis) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        // 签发jwt的时间
        // secretKey
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder().setIssuedAt(new Date());
        // 签发时间
        jwtData.forEach((k,v) -> {
            builder.claim(k,v.toString());
        });
        builder.signWith(signatureAlgorithm, key).setExpiration(new Date(nowMillis + ttlMillis));

        return builder.compact();


        // return JwtAdapter.createJWT(jwtData,ttl);
    }



    public JwtToken decodeJwtToken(String jwtToken) {
        SecretKey key = generalKey();
        JwtToken.JwtTokenBuilder builder = JwtToken.builder();
        builder.token(jwtToken);
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody();
            builder.data(claims);
            builder.result(JwtResult.OK);
        }catch (ExpiredJwtException ex) {
            builder.result(JwtResult.OVERDUE);
        } catch (Exception e) {
            builder.result(JwtResult.Fail);
        }


        return  builder.build()

                ;
    }
}
