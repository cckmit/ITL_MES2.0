package com.itl.iap.auth.controller.oauth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.config.Constants;
import com.itl.iap.auth.entity.AuthAccessToken;
import com.itl.iap.auth.service.IAuthAccessTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

/**
 * 用户登出Controller
 *
 * @author 汤俊
 * @date 2020-6-21 19:42
 * @since jdk1.8
 */

@Api("Auth-用户登出控制层")
@Slf4j
@RestController
public class LogoutController {

    @Autowired
    private IAuthAccessTokenService tokenService;

    @RequestMapping(value = "/authorize/logout", method = RequestMethod.GET)
    @ApiOperation(value = "登出操作，注销用户", notes = "登出操作，注销用户")
    public Object authroize(Model model, HttpServletRequest request) throws OAuthSystemException, URISyntaxException, OAuthProblemException {
        //注销用户
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //构建 OAuth2 资源请求
        OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request);
        // 获取Access Token
        String accessToken = oauthRequest.getAccessToken();
        // 获取Access Token
        AuthAccessToken token = tokenService.getOne(new QueryWrapper<AuthAccessToken>().lambda().eq(AuthAccessToken::getTokenId, accessToken));
        if (null == token) {
            log.info("toke is null");
            OAuthResponse response = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                    .setErrorDescription(Constants.INVALID_TOKEN_NOTFOUND)
                    .buildJSONMessage();
            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        }
        //设置token过期分钟数为0
        token.setTokenExpired(0L);
        tokenService.update(token, new QueryWrapper<AuthAccessToken>().lambda().eq(AuthAccessToken::getTokenId, token.getTokenId()));
        // 返回结果
        OAuthResponse response = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(accessToken)
                .buildJSONMessage();
        // 根据OAuthResponse 生成ResponseEntity
        return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
    }

}
