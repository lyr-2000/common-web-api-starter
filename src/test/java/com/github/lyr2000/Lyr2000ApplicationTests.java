package com.github.lyr2000;

import com.github.lyr2000.common.aop.LocalCache;
import com.github.lyr2000.common.dto.PageResult;
import com.github.lyr2000.common.enums.Unit;
import com.github.lyr2000.common.util.SpelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


@SpringBootTest
class Lyr2000ApplicationTests {

    @SneakyThrows
    @Test
    void contextLoads() throws ExecutionException {
        Cache x = CacheBuilder
                .newBuilder()
                .maximumSize(200)
                .build();
        Object w = x.get("a",()-> {
            System.out.println("xx");
            return 1;
        });
        Object w1 = x.get("a",()-> {
            System.out.println("xx");
            return 1;
        });
        Object w2 = x.get("a",()-> {
            System.out.println("xx");
            return 1;
        });
        System.out.println(w1 == w2);
    }
    @LocalCache(unit = Unit.Day,duration = 1,condition = "#i == 1",cacheKey = "'hello world'")
    public int ll(int i) throws NoSuchMethodException {
        // SpelUtil.generateKeyBySpEL()
        System.out.println("x="+i);
        return i;

    }
    @Test
    void ss() throws NoSuchMethodException {
        String res = SpelUtil.getValueByMethod("#i>0",this.getClass().getMethod("ll",int.class),new Object[]{1});
        String res2 = SpelUtil.getValueByMethod("#i>0",this.getClass().getMethod("ll",int.class),new Object[]{ -1});
        Assertions.assertEquals("true",res);
        Assertions.assertEquals("false",res2);
        // PageHelper.startPage(1,1);
        // PageInfo
    }





    @Test
    void testAop() throws NoSuchMethodException {
        int x = ll(-1) ;
        int x1 = ll(-1) ;
        int x2 = ll(-1) ;
        int x3 = ll(1) ;
        int x4 = ll(1) ;


    }

    @Test
    void lllll() {
        PageHelper.startPage(1,10);
        //list = query from mysql....
        // PageResult res = PageResult.from(list);
        new PageResult();
    }
}
