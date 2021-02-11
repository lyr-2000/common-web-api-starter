package io.github.lyr2000;

import io.github.lyr2000.common.aop.LocalCache;
import io.github.lyr2000.common.dto.Maps;
import io.github.lyr2000.common.enums.Unit;
import io.github.lyr2000.common.util.PatternUtil;
import io.github.lyr2000.common.util.SpelUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;


// @SpringBootTest
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
        // PageHelper.startPage(1,10);
        // //list = query from mysql....
        // // PageResult res = PageResult.from(list);
        //


    }

    @Test
    void testTemplate() {
        String x = "{user} ,<abc> ,(ttt) [xxx1],{ctx} hello";

        x = PatternUtil.parseNullAsBlank(x, Maps.<String,Object>hashMap().put("ctx","hello world").put("user","Tomcat").getMap());
        System.out.println(x);
        //result:Tomcat ,<abc> ,(ttt) [xxx1],hello world hello
    }
}
