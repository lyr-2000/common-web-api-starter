package com.github.lyr2000;

import com.github.lyr2000.common.aop.LocalCache;
import com.github.lyr2000.common.dto.PageResult;
import com.github.lyr2000.common.dto.Result;
import com.github.lyr2000.common.enums.DefaultApiCode;
import com.github.lyr2000.common.enums.Unit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableAspectJAutoProxy
@SpringBootApplication
@RestController
public class Lyr2000Application {

    public static void main(String[] args) {
        SpringApplication.run(Lyr2000Application.class, args);
    }


    // @GetMapping("/hello")
    // @LocalCache(unit = Unit.Day, duration = 1, cacheKey = "'ss'", condition = "false")
    // public Result foo() {
    //
    //     return PageResult.from(DefaultApiCode.OK);
    // }

}
