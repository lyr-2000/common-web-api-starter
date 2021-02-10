package com.github.lyr2000.common.dto;

import com.github.lyr2000.common.enums.ApiCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;

/**
 * 统一返回数据  DTO 类
 * 结合 hashmap
 * @Author lyr
 * @create 2020/9/20 11:01
 */
@Data
@Accessors(chain = true)
public class ViewObject implements ApiResult{
    private final HashMap<String, Object> data = new HashMap<>();

    private String message;

    private Integer code;

    public static ViewObject of(ApiCode apiCode) {
        return new ViewObject()
                .setCode(apiCode.getCode())
                .setMessage(apiCode.getMessage());
    }

    public ViewObject put(String x,Object v) {
        data.put(x,v);
        return this
                ;
    }





}
