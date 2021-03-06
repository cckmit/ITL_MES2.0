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
 * ????????????Controller
 *
 * @author ??????
 * @date 2020/10/22
 * @since jdk1.8
 */

@Api("Auth-?????????????????????")
@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private IAuthCodeService authCodeService;

    @Autowired
    private IAuthClientService clientService;

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    @ApiOperation(value = "????????????????????????ID????????????????????????????????????", notes = "????????????????????????ID????????????????????????????????????")
    public Object authroize(Model model, HttpServletRequest request) throws OAuthSystemException, URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        try {
            // ??????OAuth ????????????`
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            // ????????????????????????ID????????????
            AuthClient client = clientService.getById(oauthRequest.getClientId());
            if (null == client) {
                log.error("???????????????ID?????????ClientID=" + oauthRequest.getClientId());
                OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildBodyMessage();
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            if (client.getState() != null && client.getState().equals(AuthCode.STATE_1)) {
                throw new OAuthSystemException("?????????????????????????????????????????????");
            }
            //???????????????????????????
            if (!client.getRedirectUri().equalsIgnoreCase(oauthRequest.getRedirectURI())) {
                log.error("???????????????RedirectUri?????????ClientID=" + oauthRequest.getClientId());
                OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_REDIRECTURI)
                        .buildBodyMessage();
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            //???????????????grant_type
            String[] grantTypes = client.getGrantTypes().split(",");
            boolean grantTypeFlag = false;
            for (String grantType : grantTypes) {
                if (grantType.equalsIgnoreCase(oauthRequest.getResponseType())) {
                    grantTypeFlag = true;
                }
            }
            if (!grantTypeFlag) {
                log.error("???????????????grantType?????????ClientID=" + oauthRequest.getClientId());
                OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_GRANT_TYPES)
                        .buildBodyMessage();
                return new ResponseEntity<>(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            Subject subject = SecurityUtils.getSubject();
            // ?????????????????????????????????????????????
            if (!subject.isAuthenticated()) {
                if (!login(subject, request, client.getClientId())) { // ????????????
                    model.addAttribute("client", client);
                    return "oauth2login";
                }
            }
//            String username = ((SysUser) subject.getPrincipal()).getUserName();
            JWTToken jwtToken = (JWTToken) subject.getPrincipal();
            String username = jwtToken.getUsername();
            // ???????????????
            String authorizationCode = null;
            // resopnseType ???????????????CODE??? ????????????TOKEN
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if (responseType.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = oAuthIssuer.authorizationCode();
                log.info("???????????????????????????=" + authorizationCode);
                AuthCode code = new AuthCode();
                code.setClientId(client.getClientId());
                code.setCode(authorizationCode);
                code.setCreateDate(new Date());
                code.setUserId(username);
                authCodeService.save(code);
            }
            // ??????OAuth??????
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
            // ???????????????
            builder.setCode(authorizationCode);
            // ?????? ??????????????????????????????redirect_uri???????????????
            String redirectUri = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            // ????????????
            OAuthResponse response = builder.location(redirectUri).buildQueryMessage();
            //??????OAuthResponse??????ResponseEntity??????
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity<>(headers, HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) {
            // ????????????
            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                // ???????????????????????????????????????
                return new ResponseEntity<>("OAuth callback url needs to be provider by client!!", HttpStatus.NOT_FOUND);
            }
            // ?????????????????????????error=???
            OAuthResponse response = OAuthResponse.errorResponse(HttpServletResponse.SC_FOUND).error(e).location(redirectUri).buildQueryMessage();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity<>(headers, HttpStatus.valueOf(response.getResponseStatus()));
        }

    }

    /**
     * ??????JWT???????????? Shiro Subject ?????????
     *
     * @param subject Shiro Subject??????
     * @param request HttpServletRequest
     * @param clientId ?????????ID
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
            log.error("????????????e={}", e);
            request.setAttribute("error", "????????????:" + e.getClass().getName());
            return false;
        }
    }
}
