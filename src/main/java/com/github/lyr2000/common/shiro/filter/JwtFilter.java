package com.github.lyr2000.common.shiro.filter;

import cn.hutool.core.util.StrUtil;

import com.github.lyr2000.common.dto.Result;
import com.github.lyr2000.common.enums.DefaultApiCode;
import com.github.lyr2000.common.exception.ApiException;
import com.github.lyr2000.common.shiro.JwtResult;
import com.github.lyr2000.common.shiro.config.JwtProperties;
import com.github.lyr2000.common.shiro.config.ShiroConstant;
import com.github.lyr2000.common.shiro.entity.JwtToken;
import com.github.lyr2000.common.shiro.util.JwtUtil;
import com.github.lyr2000.common.shiro.util.ShiroWebUtil;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author lyr
 * @create 2021/2/9 21:20
 */
@AllArgsConstructor
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private final JwtProperties jwtProperties;
    private final JwtUtil jwtUtil;
    /**
     * 拦截器预处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //options 请求，直接放行
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        //判断请求的请求头是否带上 "Token"

        //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
        try {
            return executeLogin(request, response);
        } catch (Exception e) {
            return false;
        }
        // return false;
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true

    }

    /**
     * 阻止自动重定向
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        // Subject subject = this.getSubject(request, response);
        // if (subject.getPrincipal() == null) {
        //     this.saveRequestAndRedirectToLogin(request, response);
        // } else {
        //     String unauthorizedUrl = this.getUnauthorizedUrl();
        //     if (StringUtils.hasText(unauthorizedUrl)) {
        //         WebUtils.issueRedirect(request, response, unauthorizedUrl);
        //     } else {
        //         WebUtils.toHttp(response).sendError(401);
        //     }
        // }

        return false;
    }


    /**
     * 拦截器逻辑
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String token = WebUtils.toHttp(request).getHeader(jwtProperties.getTokenHeader());
        if (null == token || StrUtil.isBlank(token)) {
            ShiroWebUtil.renderJson((HttpServletResponse) response, Result.of(DefaultApiCode.NO_TOKEN,"请登录验证"));
        }
        JwtToken jwtToken = jwtUtil.decodeJwtToken(token);
        JwtResult result = jwtToken.getResult();
        if(result == null || result == JwtResult.Fail) {
            throw  new ApiException(DefaultApiCode.TokenCheckFail);
        }else if (result == JwtResult.OVERDUE) {
            throw new ApiException(DefaultApiCode.TokenExpired);
        }
        request.setAttribute(ShiroConstant.requestAttrName,jwtToken);
        //执行登录逻辑
        getSubject(request,response).login(jwtToken);
        return true;





    }
}
