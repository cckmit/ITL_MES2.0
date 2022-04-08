package com.itl.iap.auth.controller.oauth;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.auth.util.RedisUtil;
import com.itl.iap.common.base.config.Constants;
import com.itl.iap.auth.entity.*;
import com.itl.iap.auth.mapper.IapUserLoginLogTMapper;
import com.itl.iap.auth.shiro.jwt.JWTGenerator;
import com.itl.iap.auth.shiro.jwt.JWTToken;
import com.itl.iap.auth.util.PassWordUtil;
import com.itl.iap.auth.service.IAuthAccessTokenService;
import com.itl.iap.auth.service.IAuthClientService;
import com.itl.iap.auth.service.IAuthCodeService;
import com.itl.iap.auth.service.ISysUserService;
import com.itl.iap.common.base.aop.IpUtil;
import com.itl.iap.common.util.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 令牌生成Controller
 *
 * @author 汤俊
 * @date 2020/10/22
 * @since jdk1.8
 */

@Api("Auth-令牌生成控制层")
@Slf4j
@RestController
public class AccessTokenController {

    @Autowired
    private IAuthCodeService authCodeService;
    @Autowired
    private IAuthClientService authClientService;
    @Autowired
    private IAuthAccessTokenService authAccessTokenService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired(required = false)
    private IapUserLoginLogTMapper iapUserLoginLogMapper;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    @ApiOperation(value = "登录IAP并获取accessToken", notes = "登录IAP并获取accessToken")
    public HttpEntity token(HttpServletRequest request) throws OAuthSystemException {
        IapUserLoginLogT iapUserLoginLogT = new IapUserLoginLogT();
        AuthAccessToken authAccessToken = new AuthAccessToken();
        HttpHeaders headers = new HttpHeaders();
        SysUser user = new SysUser();
        headers.set("Content-Type", "application/json; charset=utf-8");
        try {
            // 转为OAuth请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            //检查提交的客户端ID是否正确
            AuthClient client = authClientService.getById(oauthRequest.getClientId());
            if (null == client) {
                log.error("获取accessToken时，客户端ID错误 client=" + oauthRequest.getClientId());
                OAuthResponse response = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            // 检查客户端安全KEY是否正确
            if (!oauthRequest.getClientSecret().equalsIgnoreCase(client.getClientSecret())) {
                log.error("ClientSecret不合法 client_secret=" + oauthRequest.getClientSecret());
                OAuthResponse response = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                        .buildJSONMessage();
                return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
            //TODO 新增authcode校验
            AuthCode code = authCodeService.getOne(new QueryWrapper<AuthCode>().eq("code", authCode));
            if (client.getState() != null && client.getState().equals(AuthCode.STATE_1)) {
                throw new OAuthSystemException("客户端已停止使用，请联系管理员");
            }
            // 获取登录客户端ID
            iapUserLoginLogT.setClient(authCode);
            /**
             * 此处只校验 AUTHORIZATION_CODE 类型，其他的还有PASSWORD 、REFRESH_TOKEN 和 CLIENT_CREDENTIALS
             * 具体查看 {@link GrantType}
             * */
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                authAccessToken.setUserId(code.getUserId());
                if (!code.getCode().equals(authCode)) {
                    OAuthResponse response = OAuthResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription(Constants.INVALID_CODE_DESCRIPTION)
                            .buildJSONMessage();
                    iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE0); // 编码第三方登陆
                    iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
                    loinLog(iapUserLoginLogT, request);
                    return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
                }
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equalsIgnoreCase(GrantType.PASSWORD.toString())) {
                //TODO 校验用户名密码
                if (StringUtils.isEmpty(request.getParameter("username")) && StringUtils.isEmpty(request.getParameter("password")) && StringUtils.isEmpty(request.getParameter("userCardNumber"))) {
                    OAuthResponse response = OAuthResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                            .setErrorDescription(Constants.INVALID_USER_DESCRIPTION)
                            .buildJSONMessage();
                    iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE3); // 编码第三方登陆
                    iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);  // 登陆失败
                    loinLog(iapUserLoginLogT, request);
                    return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
                } else {

                    if (StrUtil.isNotEmpty(request.getParameter("userCardNumber")) && !request.getParameter("userCardNumber").equals("null")){
                        user = sysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUserCardNumber,request.getParameter("userCardNumber")));
                        if (user == null) {
                            iapUserLoginLogT.setUserId(request.getParameter("username"));//账号
                            iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE4);  // 登陆方式
                            iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND4);  // 登陆失败
                            iapUserLoginLogT.setMessage("该卡号未绑定用户");
                            loinLog(iapUserLoginLogT, request);
                            throw new OAuthSystemException("该卡号未绑定用户");
                        }
                    }
                    else {
                        user = sysUserService.getOne(new QueryWrapper<SysUser>().lambda()
                                .eq(SysUser::getUserName, request.getParameter("username"))
                                .eq(SysUser::getUserPsw, PassWordUtil.encrypt(request.getParameter("password"), request.getParameter("username"))));
                    }
                    if (user == null) {
                        iapUserLoginLogT.setUserId(request.getParameter("username"));//账号
                        iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE3);  // 登陆方式
                        iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);  // 登陆失败
                        iapUserLoginLogT.setMessage("账号或密码错误");
                        loinLog(iapUserLoginLogT, request);
                        throw new OAuthSystemException("账号或密码错误");
                    }
                    if (user.getState() != null && user.getState().equals(SysUser.STATE_1) || user.getValidity() != null && user.getValidity().before(new Date())) {
                        iapUserLoginLogT.setUserId(request.getParameter("username"));//账号
                        iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE3);  // 登陆方式
                        iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);  // 登陆失败
                        iapUserLoginLogT.setMessage("账号已过期或已被锁定，请联系管理员");
                        loinLog(iapUserLoginLogT, request);
                        throw new OAuthSystemException("账号已过期或已被锁定，请联系管理员");
                    }
                    authAccessToken.setUserId(user.getUserName());
                    iapUserLoginLogT.setLoginType(IapUserLoginLogT.LOGIN_TYPE3); // 密码模式登陆
                    if (null == user) {
                        OAuthResponse response = OAuthResponse
                                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                                .setErrorDescription(Constants.INVALID_USER_DESCRIPTION)
                                .buildJSONMessage();
                        iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
                        loinLog(iapUserLoginLogT, request);
                        return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
                    }
                    iapUserLoginLogT.setUserId(user.getUserName());
                }
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.REFRESH_TOKEN.toString())) {
                //TODO 校验 refresh_token 是否存在
                //TODO 校验 refresh_token 是否过期
                //不允许刷新TOKEN模式
                OAuthResponse response = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                        .setErrorDescription(Constants.INVALID_USER_DESCRIPTION)
                        .buildJSONMessage();

                iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
                loinLog(iapUserLoginLogT, request);
                return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.CLIENT_CREDENTIALS.toString())) {
                OAuthResponse response = null;
                //1.判断客户端身份
                boolean isClientCredential = false;
                String[] grantTypes = client.getGrantTypes().split(",");
                for (String grantType : grantTypes) {
                    if (grantType.equals(GrantType.CLIENT_CREDENTIALS.toString())) {
                        isClientCredential = true;
                        break;
                    }
                }
                //2.如果该客户端不允许使用客户端模式登陆
                if (!isClientCredential) {
                    //不允许客户端模式登陆
                    response = OAuthResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                            .setErrorDescription(Constants.INVALID_USER_DESCRIPTION)
                            .buildJSONMessage();
                    iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
                    loinLog(iapUserLoginLogT, request);
                    return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
                }
            } else {
                //不允许客户端模式登陆
                OAuthResponse response = OAuthResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.OAUTH_ERROR_DESCRIPTION)
                        .setErrorDescription(Constants.INVALID_GRAND_TYPE_NOTFOUND)
                        .buildJSONMessage();
                iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
                loinLog(iapUserLoginLogT, request);
                return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
            }
            // 生成Access Token
//            String token = JwtUtil.sign(request.getParameter("username"), request.getParameter("username"));
            String userName = "";
            // 客户端凭证
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.CLIENT_CREDENTIALS.toString())) {
                userName = client.getClientId();
                // auth2 code模式
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                userName = code.getUserId();
            } else {
                // 密码模式
                userName = user.getUserName();
            }
            JWTGenerator jwtGenerator = new JWTGenerator();
            jwtGenerator.setSalt(userName);
            jwtGenerator.setUsername(userName);
            jwtGenerator.setClientId(client.getClientId());
            OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(jwtGenerator);
            String accessToken = oAuthIssuer.accessToken();
//            String token = JwtUtil.sign(username, username);
            log.info("服务器生成的accessToken=" + accessToken);
            //保存token
            authAccessToken.setId(UUID.uuid32());
            authAccessToken.setClientId(client.getClientId());
            authAccessToken.setTokenId(accessToken);
            authAccessToken.setCreateDate(new Date());
            authAccessToken.setAuthenticationId(authCode);
            authAccessToken.setTokenExpired(Constants.EXPIRES_IN);
//            authAccessToken.setUserId()
            System.out.println("1.---->>>SecurityUtils.getSubject().isAuthenticated() =" + SecurityUtils.getSubject().isAuthenticated());
            JWTToken jwtToken = JWTToken.build(accessToken, userName, userName, Constants.EXPIRES_IN, oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE), client.getClientId());
            SecurityUtils.getSubject().login(jwtToken);
            authAccessTokenService.save(authAccessToken);
            // 将用户信息token放进Redis
            redisUtil.set(userName, authAccessToken.getTokenId(), Constants.EXPIRES_IN);
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
                code.setState(1); //设置code失效，每个code只能使用一次
                authCodeService.saveOrUpdate(code);
                log.info("服务器移除auth_code=" + authCode);
            }
            // 生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(Constants.EXPIRES_IN))
                    .buildJSONMessage();
            System.out.println("2.---->>>SecurityUtils.getSubject().isAuthenticated() =" + SecurityUtils.getSubject().isAuthenticated());
            // 根据OAuthResponse 生成ResponseEntity
            iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND1);
            loinLog(iapUserLoginLogT, request);
            return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));

        } catch (OAuthProblemException e) {
            log.error("获取accessToken发生异常e=", e);
            // 构建错误响应
            OAuthResponse response = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            iapUserLoginLogT.setCommand(IapUserLoginLogT.LOGIN_COMMAND3);
            loinLog(iapUserLoginLogT, request);
            return new ResponseEntity(response.getBody(), headers, HttpStatus.valueOf(response.getResponseStatus()));
        }
    }

    /**
     * 设置用户登录日志并保存到数据库中
     *
     * @param iapUserLoginLogT 用户登录表的实体类
     * @param request HttpServletRequest
     */
    private void loinLog(IapUserLoginLogT iapUserLoginLogT, HttpServletRequest request) {
        Date date = new Date();
        iapUserLoginLogT.setCreateDate(date);
        iapUserLoginLogT.setLastUpdateDate(date);
        iapUserLoginLogT.setId(UUID.uuid32());
        // 获取ip地址
        String ipAddr = IpUtil.getIpAddr(request);
        iapUserLoginLogT.setLastIp(ipAddr);
        // 获取浏览器信息
        String browser = IpUtil.getBrowser(request.getHeader("user-agent"));
        // 获取登录设备
        iapUserLoginLogT.setLoginOs(System.getProperty("os.name"));
        iapUserLoginLogT.setOsver(System.getProperty("os.version"));
        iapUserLoginLogT.setVersion("1.0");
        iapUserLoginLogT.setClient(browser);
        iapUserLoginLogMapper.insert(iapUserLoginLogT);
    }


}
