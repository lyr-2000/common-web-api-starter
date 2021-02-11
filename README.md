# 工程简介

自己封装的 项目通用 api 模块 ，目前还没完善

# 延伸阅读

```xml


<!-- https://mvnrepository.com/artifact/com.github.lyr-2000/common-web-api -->
<dependency>
  <groupId>com.github.lyr-2000</groupId>
  <artifactId>common-web-api</artifactId>
  <version>3.0</version>
</dependency>




```

## 最新 3.0 版本将 com.github 改成了 io.github , 展示的代码是旧版本的


## 对shiro模块的封装
### 1. 自定义 realm
```java

/**
 * 使用 session进行会话管理
 * @Author lyr
 * @create 2021/2/10 15:34
 */
@Slf4j
@AllArgsConstructor
public class SessionRealmImpl extends SessionRealm {
    final UserMapperCustom userMapperCustom;


    /**
     * 获取权限
     * @param username 
     * @return
     */
    public List<String> getPermissions(String username) {

        return userMapperCustom
                .getUserPermission(username)
                .stream()
                .map(Menu::getPermissionName)
                .collect(Collectors.toList());
    }


    /**
     * 获取角色
     * @param username 
     * @return
     */
    public List<String> getRoles(String username) {
        List<Role> roles = userMapperCustom.getUserRole(username);
        return roles.stream()
                .map(Role::getRoleKey)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public void doAuthorizationCustom(SimpleAuthorizationInfo per, String username) {
        per.addRoles(getRoles(username));
        per.addStringPermissions(getPermissions(username));
    }

    /**
     * 登录
     * @param username  用户名字
     * @param password 密码
     * @return
     */
    @Override
    public boolean check(String username, String password) {
        // System.out.println("username="+username);
        // System.out.println("pwd="+password);
        UserPasswordInfo x = userMapperCustom.getUserPasswordInfo(username);
        if (x==null) {
            //获取用户的 身份，如果数据库查出为 null，直接返回 false
            return false;
        }
        String pwd = PwdUtil.passwordMd5(password,x.getSalt());
        // System.out.println(x);
        //如果密码和 数据库密码对上了，就认为登录成功
        return pwd.equals(x.getPassword());
    }
}



```

### 2. 自定义JWT 拦截器

```java

@EnableExceptionHandler
@Configuration
public class ShiroConfig {
    @Bean
    public ShiroCustomProperties jwtProperties() {
        ShiroCustomProperties properties =  new ShiroCustomProperties("slxxooosfjlafj","token");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        properties.setCustomFilterChain(map);
        map.put("/api/**","jwt");
        map.put("/doc.html","authc");
        map.put("/login","anon");
        map.put("/actuator/**","authc");
        map.put("/css/**","anon");
        map.put("/js/**","anon");
        map.put("/layui/**","anon");
        map.put("/page/**","anon");
        map.put("/images/**","anon");
        map.put("/**","anon");
        
      
        properties
                .setLoginUrl("/admin/login");
        return properties;
    }
    @Bean
    public SessionRealmImpl sessionRealm(@Autowired UserMapperCustom userMapperCustom) {
        return new SessionRealmImpl(userMapperCustom);
    }
}



```

## 继承 JwtRealm
```java

package com.github.lyr.blog.admin.config;

import com.github.lyr.blog.basic.mapper.custom.UserMapperCustom;
import com.github.lyr.blog.basic.pojo.po.Menu;
import com.github.lyr.blog.basic.pojo.po.Role;
import com.github.lyr.common.shiro.entity.JwtToken;
import com.github.lyr.common.shiro.realm.JwtRealm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author lyr
 * @create 2021/2/10 20:22
 */
@Slf4j
@AllArgsConstructor
public class JwtRealmImpl extends JwtRealm {
    final UserMapperCustom userMapperCustom;

    @Override
    @Transactional(readOnly = true)
    public void doAuthorizationCustom(SimpleAuthorizationInfo per, JwtToken jwtToken) {
        // super.doAuthorizationCustom(per, jwtToken);
        Map map = jwtToken.getData();
        String email = (String) map.get("user_id");
        List<String> permissions = userMapperCustom.getUserPermission(email)
                .stream()
                .map(Menu::getPermissionName).collect(Collectors.toList());
        per.addStringPermissions(permissions);
        List<String > roles = userMapperCustom.getUserRole(email)
                .stream()
                .map(Role::getRoleKey)
                .collect(Collectors.toList());
        per.addRoles(roles);


    }
}


```


##  shiro简单的配置 【开箱即用】
```java


@Bean
    public SessionRealm sessionRealm() {
        return new SessionRealm(){
            @Override
            public boolean check(String username, String password) {
                // System.out.println(username);
                // System.out.println(password);
                return "666".equals(username) && "123456".equals(password);
            }
        };
    }


```





## pageHelper 通用 pagebean 结果集
```java

 @Test
    void lllll() {
        PageHelper.startPage(1,10);
        //list = query from mysql....
        PageResult res = PageResult.from(list);
        // new PageResult();
    }
    
    

```
返回结果如下
```json

{
  "curPage": 0,
  "size": 0,
  "totalPage": 0,
  "totalCount": 0,
  "data": {}
}
```



## fluent式编程
```java



     *  Map data = Maps.newHashMap();
     *  data.put("user_id",info.getUserId());
     *  return R.res()
     *          .put("token",jwtUtil.sign(data, Duration.ofDays(3).toMillis()))
     *          .end();
     *
     *   
     *
     *    Maps.<String,Object>hashMap().put("ctx","hello world").put("user","Tomcat").getMap())
     *


  /**
     * 获取博客信息
     * @param request
     * @return
     */
    @GetMapping("/blogs_detail")
    public Result blogs(HttpServletRequest request) {
        return R.res()
                .put("msg","it is ok")
                .end();
    }
    
    /**
     * 获取博客信息
     * @param request
     * @return
     */
    @GetMapping("/blogs")
    public Result blogs(HttpServletRequest request) {
        return R.res()
                .put("msg","it is ok")
                .withPage("blogs",blogService.listBlogs(PageParam.from(request)))
                .end();
    }




```


##  自定义异常处理
尽量满足接口的开闭原则，因此 这里定义了接口抽象
实现 ApiErrorCode 接口，将异常交给切面处理，减少重复代码
#### example

```java

package com.github.lyr.common.exception;

import ApiErrorCode;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author lyr
 * @create 2021/2/5 14:00
 */
@Getter
@AllArgsConstructor
@ApiModel
public enum ApiCustomErrorCode implements ApiErrorCode {

    RouterBindingException("路由参数绑定异常", 10000+1),
    NoHandlerException("没有找到对应访问路径", 10000+2),
    TokenCheckFail("token校验失败",          10000+3),
    TokenExpired("token过期，请重新签名",10000+4),
    LoginFail("登录失败，请检查用户名，密码",10000+4),
    ;



    String message;
    Integer code;
    // public static int base = 10000;
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}


```

```java

 /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    @Override
    public Result<Map<String, Object>> getTokenInfo(LoginDTO loginDTO) {
        UserPasswordInfo info = userMapperCustom.getUserPasswordInfo(loginDTO.getEmail());
        if (info!=null && info.getPassword()!=null && info.getPassword().equals(PwdUtil.passwordMd5(loginDTO.getPassword(),info.getSalt()))) {
            // return Result.of(DefaultApiCode.OK,jwtUtil.sign(new HashMap<>(),10000) );
            Map data = Maps.newHashMap();
            data.put("user_id",info.getUserId());
            return R.res()
                    .put("token",jwtUtil.sign(data, Duration.ofDays(3).toMillis()))
                    .end();
        }

        throw new ApiException(ApiCustomErrorCode.TokenCheckFail);
    }



```


## patternUtil 解析 {} 花括号表达式

```java


 @Test
    void testTemplate() {
        String x = "{user} ,<abc> ,(ttt) [xxx1],{ctx} hello";

        x = PatternUtil.parseNullAsBlank(x, Maps.<String,Object>hashMap().put("ctx","hello world").put("user","Tomcat").getMap());
        System.out.println(x);
        //result:Tomcat ,<abc> ,(ttt) [xxx1],hello world hello
    }

```




### 需要依赖的库

```java




<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-aop -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
            <version>2.4.2</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/cn.hutool/hutool-all -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.5.7</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
        </dependency>





        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
            <version>2.4.2</version>
        </dependency>


        <!-- https://mvnrepository.com/artifact/com.google.guava/guava -->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>30.1-jre</version>
        </dependency>


        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>

        </dependency>

        <!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper-spring-boot-starter -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.3.0</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.75</version>
        </dependency>

        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>1.3.2</version>

        </dependency>

        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.3.0</version>
        </dependency>



    </dependencies>


```







