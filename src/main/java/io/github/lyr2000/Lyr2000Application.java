package io.github.lyr2000;

import io.github.lyr2000.common.aop.LocalCache;
import io.github.lyr2000.common.dto.PageParam;
import io.github.lyr2000.common.dto.PageResult;
import io.github.lyr2000.common.dto.R;
import io.github.lyr2000.common.enums.DefaultApiCode;
import io.github.lyr2000.common.enums.Unit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@EnableAspectJAutoProxy
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
@RequestMapping
public class Lyr2000Application {

    public static void main(String[] args) {
        SpringApplication.run(Lyr2000Application.class, args);
    }


    @GetMapping("/hello")
    // @LocalCache(unit = Unit.Day, duration = 1, cacheKey = "'ss'", condition = "false")
    public R foo(HttpServletRequest request) {

        return R.res().put("page_param", PageParam.from(request))
                ;
                // .put("page_res",PageResult.from());
    }

}
