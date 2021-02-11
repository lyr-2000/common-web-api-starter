package io.github.lyr2000.common.dto;


import io.github.lyr2000.common.enums.DefaultApiCode;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于工厂模式创建对象，类名足够短，不怕麻烦
 *
 * @Author lyr
 * @create 2021/2/10 12:57
 */
@Data
public class R {
    Result<Map<String,Object>> result;
    private R() {

    }

    /**
     * 私有化构造器，防止外部 直接 new
     * @param result
     */
    private R(Result<Map<String, Object>> result) {
        this.result = result;
    }


    public static  R res() {
        return new  R(Result.of(DefaultApiCode.OK,new HashMap<>(8)));
    }
    public  R put(String k, Object value) {
        this.result
                .getData()
                .put(k,value);
        return this;
    }
    public R with(String k,Object v) {
        return put(k,v);
    }
    public <T>R withPage(String k, List<T> list) {
        this.result
                .getData()
                .put(k,PageResult.from(list));
        return this;
    }
    public <T> R withPage(String k, PageInfo<T> pageInfo) {
        this.result
                .getData()
                .put(k,PageResult.from(pageInfo));
        return this;
    }


    /**
     *
     *  Map data = Maps.newHashMap();
     *  data.put("user_id",info.getUserId());
     *  return R.res()
     *          .put("token",jwtUtil.sign(data, Duration.ofDays(3).toMillis()))
     *          .end();
     *
     *
     * @return
     */
    public Result<Map<String, Object>> end() {
        return result;
    }

    /**
     * @return OK
     */
    public static Result ok() {
        return Result.from(DefaultApiCode.OK);
    }

    /**
     * @return 失败状态码 发生了什么异常
     */
    public static Result fail() {
        return Result.from(DefaultApiCode.BAD_REQUEST);
    }

    /**
     * @return 找不到 想要的资源
     */
    public static Result absent() {
        return Result.from(DefaultApiCode.NO_RESOURCE);
    }





}
