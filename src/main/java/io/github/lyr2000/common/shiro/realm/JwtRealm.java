package io.github.lyr2000.common.shiro.realm;
 
import io.github.lyr2000.common.shiro.entity.JwtToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * @Author lyr
 * @create 2021/2/9 21:08
 */
public class JwtRealm extends  AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    public void doAuthorizationCustom(SimpleAuthorizationInfo per,JwtToken jwtToken) {
        //per.addRole

    }
    /**
     * 角色权限信息
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo per = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        doAuthorizationCustom(per, (JwtToken) subject.getPrincipal());
        return per;

    }


    /**
     * 登录认证的信息
     * @param
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken stoken) throws AuthenticationException {
        JwtToken token = ((JwtToken) stoken);
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
