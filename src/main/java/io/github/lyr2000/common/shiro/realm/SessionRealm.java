package io.github.lyr2000.common.shiro.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @Author lyr
 * @create 2021/2/10 15:05
 */
@Slf4j
public class SessionRealm extends AuthorizingRealm {

   public List<String> getPermissions(String username) {
       return Collections.emptyList();
   }
   public List<String> getRoles(String username) {
       return Collections.emptyList();
   }

    /**
     * 角色权限信息
     *
     * @param principals
     * @return
     */
    @Override
    protected  AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo per = new SimpleAuthorizationInfo();
        // Subject subject = SecurityUtils.getSubject();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = (UsernamePasswordToken)subject.getPrincipal();
        doAuthorizationCustom(per,token.getUsername());
        // per.addRoles(getRoles(token.getUsername()));
        // per.addStringPermissions(getPermissions(token.getUsername()));
        return per;

    }

    /**
     * 获取用户权限信息
     * 需要重写方法
     * @param per
     * @param username
     */
    // @Override
    @Transactional(readOnly = true)
    public void doAuthorizationCustom(SimpleAuthorizationInfo per, String username) {
        per.addRoles(getRoles(username));
        per.addStringPermissions(getPermissions(username));
    }



    /**
     * 登录认证的信息
     *
     * @param
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken stoken) throws AuthenticationException {
        // log.info("session _realm__");
        UsernamePasswordToken token = (UsernamePasswordToken)stoken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        if (!check(username, password)) {
            return null;
        }

        return new SimpleAuthenticationInfo(username, password, getName());
    }

    public boolean check(String username,String password) {
        return false;
    }

}