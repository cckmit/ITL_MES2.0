package com.itl.iap.auth.controller.oauth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.config.Constants;
import com.itl.iap.auth.entity.AuthClient;
import com.itl.iap.auth.entity.AuthCode;
import com.itl.iap.auth.shiro.jwt.JWTToken;
import com.itl.iap.common.base.utils.JWTUtil;
import com.itl.iap.auth.service.IAuthClientService;
import com.itl.iap.auth.service.IAuthCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * 授权处理Controller
 *
 * @author 汤俊
 * @date 2020/10/22
 * @since jdk1.8
 */

@Api("Auth-授权处理控制层")
@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private IAuthCodeService authCodeService;

    @Autowired
    private IAuthClientService clientService;

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    @ApiOperation(value = "通过传入的客户端ID，校验客户端并生成授权码", notes = "通过传入的客户端ID，校验客户端并生成授权码")
    public Object authroize(Model model, HttpServletRequest request) throws OAuthSystemException, URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        try {
            // 构建OAuth 授权请求`
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            // 检查传入的客户端ID是否正确
            AuthClient client = clientService.getById(oauthRequest.getClientId());
            if (null == client) {
                log.error("校验客户端ID失败，ClientID=" + oauthRequest.getClientId());
                OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildBodyMessage();
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            if (client.getState() != null && client.getState().equals(AuthCode.STATE_1)) {
                throw new OAuthSystemException("客户端已停止使用，请联系管理员");
            }
            //校验客户端跳转地址
            if (!client.getRedirectUri().equalsIgnoreCase(oauthRequest.getRedirectURI())) {
                log.error("校验客户端RedirectUri失败，ClientID=" + oauthRequest.getClientId());
                OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_REDIRECTURI)
                        .buildBodyMessage();
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            //校验客户端grant_type
            String[] grantTypes = client.getGrantTypes().split(",");
            boolean grantTypeFlag = false;
            for (String grantType : grantTypes) {
                if (grantType.equalsIgnoreCase(oauthRequest.getResponseType())) {
                    grantTypeFlag = true;
                }
            }
            if (!grantTypeFlag) {
                log.error("校验客户端grantType失败，ClientID=" + oauthRequest.getClientId());
                OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_GRANT_TYPES)
                        .buildBodyMessage();
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            Subject subject = SecurityUtils.getSubject();
            // 如果用户没有登录，跳转登录页面
            if (!subject.isAuthenticated()) {
                if (!login(subject, request, client.getClientId())) { // 登录失败
                    model.addAttribute("client", client);
                    return "oauth2login";
                }
            }
//            String username = ((SysUser) subject.getPrincipal()).getUserName();
            JWTToken jwtToken = (JWTToken) subject.getPrincipal();
            String username = jwtToken.getUsername();
            // 生成授权码
            String authorizationCode = null;
            // resopnseType 目前仅支持CODE， 另外还有TOKEN
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if (responseType.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = oAuthIssuer.authorizationCode();
                log.info("服务端生成的授权码=" + authorizationCode);
                AuthCode code = new AuthCode();
                code.setClientId(client.getClientId());
                code.setCode(authorizationCode);
                code.setCreateDate(new Date());
                code.setUserId(username);
                authCodeService.save(code);
            }
            // 构建OAuth响应
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
            // 设置授权码
            builder.setCode(authorizationCode);
            // 得到 到客户端请求地址中的redirect_uri重定向地址
            String redirectUri = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            // 构建响应
            OAuthResponse response = builder.location(redirectUri).buildQueryMessage();
            //根据OAuthResponse返回ResponseEntity响应
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity<>(headers, HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) {
            // 处理出错
            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                // 告诉客户端没有传入回调地址
                return new ResponseEntity<>("OAuth callback url needs to be provider by client!!", HttpStatus.NOT_FOUND);
            }
            // 返回消息错误（如?error=）
            OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND).error(e).location(redirectUri).buildQueryMessage();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity<>(headers, HttpStatus.valueOf(response.getResponseStatus()));
        }

    }

    /**
     * 生成JWT并添加到 Shiro Subject 对象中
     *
     * @param subject Shiro Subject对象
     * @param request HttpServletRequest
     * @param clientId 客户端ID
     * @return boolean
     */
    private boolean login(Subject subject, HttpServletRequest request, String clientId) {
        if ("get".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String username = request.getParameter("username");
        if (StringUtils.isEmpty(username)) {
            return false;
        }
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        String token = JWTUtil.sign(username, clientId, username);
//        JwtToken jwtToken = new JwtToken(token);
        String userName = request.getParameter("username");
        JWTToken jwtToken = JWTToken.build(token, userName, userName, Constants.EXPIRES_IN, GrantType.AUTHORIZATION_CODE.toString(), null);
        try {
//            token.setRememberMe(true);
            subject.login(jwtToken);
            return true;
        } catch (Exception e) {
            log.error("登录异常e={}", e);
            request.setAttribute("error", "登录失败:" + e.getClass().getName());
            return false;
        }
    }
}
