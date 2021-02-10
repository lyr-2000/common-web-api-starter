# 工程简介

自己封装的 项目通用 api 模块 ，目前还没完善

# 延伸阅读

```xml


<!-- https://mvnrepository.com/artifact/com.github.lyr-2000/common-web-api -->
<dependency>
    <groupId>com.github.lyr-2000</groupId>
    <artifactId>common-web-api</artifactId>
    <version>2.0</version>
</dependency>






```

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





