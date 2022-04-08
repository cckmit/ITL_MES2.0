package com.itl.iap.auth.shiro.filter;

import com.itl.iap.common.base.config.Constants;
import com.itl.iap.auth.service.IAuthCodeService;
import com.itl.iap.auth.shiro.jwt.JWTToken;
import com.itl.iap.common.base.utils.JWTUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.rs.extractor.BearerHeaderTokenExtractor;
import org.apache.oltu.oauth2.rs.extractor.TokenExtractor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 该 filter 的作用类似于 FormAuthenticationFilter 用于 oauth2 客户端的身份验证控制
 *
 * @author 汤俊
 * @date 2020-6-28
 * @since jdk1.8
 */
@Data
@Slf4j
public class OAuth2AuthenticationFilter extends AuthenticatingFilter {

    /**
     * oauth2 authc code参数名
     */
    private String authcCodeParam;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 服务器端登录 成功/失败 后重定向到的客户端地址
     */
    private String redirectUrl;

    /**
     * oauth2服务器响应类型
     */
    private String responseType;

    /**
     * 失败重定向地址url
     */
    private String failureUrl;

    @Autowired
    private IAuthCodeService authCodeService;

    private TokenExtractor extractor = new BearerHeaderTokenExtractor();

    /**
     * 判断是否登录
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        return null;
    }

    /**
     * 判断是否允许访问
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    /**
     * 是否是拒绝登录
     *
     * @param request
     * @param response
     * @return boolean
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Subject subject = getSubject(httpRequest, response);
        //判断是否登陆
        log.info("--->>> subject.isAuthenticated() == " + subject.isAuthenticated());
        if (!subject.isAuthenticated()) {
            String accessToken = this.extractor.getAccessToken(httpRequest);
            if (StringUtils.isEmpty(request.getParameter(authcCodeParam)) && StringUtils.isEmpty(accessToken)) {
                // 如果没有身份认证，且没有authCode,则重定向到服务端授权
                saveRequestAndRedirectToLogin(request, response);
            } else {
                //登陆处理，不走shiro自带的登陆方法
                String userName = JWTUtil.getUsername(accessToken).getUserName();
                JWTToken jwtToken = JWTToken.build(accessToken, userName, userName, Constants.EXPIRES_IN, GrantType.AUTHORIZATION_CODE.toString(), clientId);
                WebUtils.saveRequest(request);
                SecurityUtils.getSubject().login(jwtToken);
//                getSubject(request,response).login(jwtToken);
                return true;
            }
        }
        return false;
    }

    /**
     * 登录失败后的回调方法
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return false;
    }

    /**
     * 登录成功后的回调方法
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, getSuccessUrl());
        return false; // 表示执行链结束
    }
}
