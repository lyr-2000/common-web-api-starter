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

@Slf4j
@AllArgsConstructor
public class SessionRealmImpl extends SessionRealm {
    final UserMapperCustom userMapperCustom;

    @Override
    public List<String> getPermissions(String username) {

        return userMapperCustom
                .getUserPermission(username)
                .stream()
                .map(Menu::getPermissionName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoles(String username) {
        List<Role> roles = userMapperCustom.getUserRole(username);
        return roles.stream()
                .map(Role::getRoleKey)
                .collect(Collectors.toList());
    }

    @Override
    public boolean check(String username, String password) {
        System.out.println("username="+username);
        System.out.println("pwd="+password);
        UserPasswordInfo x = userMapperCustom.getUserPasswordInfo(username);
        if (x==null) return false;
        String pwd = PwdUtil.passwordMd5(password,x.getSalt());
        System.out.println(x);
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






