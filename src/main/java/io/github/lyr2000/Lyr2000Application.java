package io.github.lyr2000;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
