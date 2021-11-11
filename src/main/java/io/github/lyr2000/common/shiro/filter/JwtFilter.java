package io.github.lyr2000.common.shiro.filter;

import cn.hutool.core.util.StrUtil;

import io.github.lyr2000.common.dto.Result;
import io.github.lyr2000.common.enums.DefaultApiCode;
import io.github.lyr2000.common.shiro.JwtResult;
import io.github.lyr2000.common.shiro.config.ShiroConstant;
import io.github.lyr2000.common.shiro.config.ShiroCustomProperties;
import io.github.lyr2000.common.shiro.entity.JwtToken;
import io.github.lyr2000.common.shiro.util.JwtUtil;
import io.github.lyr2000.common.shiro.util.ShiroWebUtil;
import io.github.lyr2000.common.util.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
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
@Slf4j
@AllArgsConstructor
public class JwtFilter extends BasicHttpAuthenticationFilter {

    protected final ShiroCustomProperties shiroCustomProperties;
    protected final JwtUtil jwtUtil;
    /**
     * 拦截器预处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin")); //标识允许哪个域到请求，直接修改成请求头的域
        httpServletResponse.setHeader("Access-Control-Allow-Methods", httpServletRequest.getMethod());//标识允许的请求方法
        // 响应首部 Access-Control-Allow-Headers 用于 preflight request （预检请求）中，列出了将会在正式请求的 Access-Control-Expose-Headers 字段中出现的首部信息。修改为请求首部
        //参考：https://cloud.tencent.com/developer/section/1189900
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        //options 请求，直接放行
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            // log.info("options is OK");
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断请求的请求头是否带上 "Token"

        //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
        // try {
        try {
            return executeLogin(request, response);
        } catch (Exception e) {
            return false;
        }
        // } catch (Exception e) {
        //     return false;
        // }
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
        String token = WebUtils.toHttp(request).getHeader(shiroCustomProperties.getTokenHeader());
        log.info("token={}",token);
        if (null == token || StrUtil.isBlank(token)) {
            log.info("token is null ,url = {}",WebUtils.toHttp(request).getRequestURI());
            // log.info("丢出异常");
            // throw new ApiException(DefaultApiCode.NO_TOKEN);
            ShiroWebUtil.renderJson((HttpServletResponse) response, Result.of(DefaultApiCode.NO_TOKEN,"请登录验证"));
            return false;
        }
        try {
            JwtToken jwtToken = jwtUtil.decodeJwtToken(token);
            JwtResult result = jwtToken.getResult();
            if(result == null || result == JwtResult.Fail) {
                ShiroWebUtil.renderJson((HttpServletResponse) response, Result.from(DefaultApiCode.TokenCheckFail));
                return  false;
            }else if (result == JwtResult.OVERDUE) {
                WebUtil.renderJson((HttpServletResponse) response, Result.from(DefaultApiCode.TOKEN_EXPIRED));
                return false;
            }
            request.setAttribute(ShiroConstant.requestAttrName,jwtToken);
            //执行登录逻辑
            getSubject(request,response).login(jwtToken);
            return true;
        }catch (Exception e) {
            //捕获异常并且打印
            log.error("JwtFilter.executeLogin msg = {}",e.getMessage());
        }
        return  false;

    }
}
