package com.itl.iap.auth.controller.oauth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.auth.entity.AuthAccessToken;
import com.itl.iap.auth.entity.SysUser;
import com.itl.iap.auth.service.IAuthAccessTokenService;
import com.itl.iap.auth.service.ISysUserService;
import com.itl.iap.common.base.config.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取用户信息Controller
 *
 * @author 汤俊
 * @date 2020/10/22
 * @since jdk1.8
 */

@Api("Auth-获取用户信息控制层")
@Slf4j
@RestController
public class UserInfoController {

    @Autowired
    private IAuthAccessTokenService tokenService;

    @Autowired
    private ISysUserService userService;

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ApiOperation(value = "客户端根据code可获取user信息（这里只返回username）", notes = "客户端根据code可获取user信息（这里只返回username）")
    public HttpEntity userInfo(HttpServletRequest request) throws OAuthSystemException {
        try {
            System.out.println("--->>> SecurityUtils.getSubject().getPrincipal()" + SecurityUtils.getSubject().getPrincipal());
            System.out.println("SecurityUtils.getSubject().isAuthenticated() = " + SecurityUtils.getSubject().isAuthenticated());
            //构建 OAuth2 资源请求
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request);
            // 获取Access Token
            String accessToken = oauthRequest.getAccessToken();
            // 验证Access Token
            AuthAccessToken token = tokenService.getOne(new QueryWrapper<AuthAccessToken>().lambda().eq(AuthAccessToken::getTokenId, accessToken));
            long expiredTime = token.getCreateDate().getTime() + token.getTokenExpired();
            long currentTime = System.currentTimeMillis();
            if (expiredTime < currentTime) {
                log.info("accessToken 已过期  accessToken=" + accessToken);
                // 不存在（过期），则返回未验证，需重新验证
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm(Constants.RESOURCE_SERVER_NAME)
                        .setError(OAuthError.ResourceResponse.EXPIRED_TOKEN)
                        .buildHeaderMessage();
                HttpHeaders headers = new HttpHeaders();
                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));

                return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
            }
            // 返回用户名
            String username = token.getUserId();
            SysUser user = userService.getOne(new QueryWrapper<SysUser>().eq("user_name", username));
            List<String> resources_code = userService.getAllMenuByType(username).stream().map(x -> x.get("resources_code")).collect(Collectors.toList());
            user.setButtonRoles(resources_code);
            return new ResponseEntity(user, HttpStatus.OK);
        } catch (OAuthProblemException e) {
            e.printStackTrace();
            // 检查是否设置了错误码
            String errorCode = e.getError();
            if (OAuthUtils.isEmpty(errorCode)) {
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm(Constants.RESOURCE_SERVER_NAME)
                        .buildHeaderMessage();
                HttpHeaders headers = new HttpHeaders();
                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));

                return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
            }

            OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm(Constants.RESOURCE_SERVER_NAME)
                    .setError(e.getError())
                    .setErrorDescription(e.getDescription())
                    .setErrorUri(e.getUri())
                    .buildHeaderMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                    oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    //    @RequiresPermissions("admin:testAuthc")
    @RequiresRoles("admin")
//    @RequiresAuthentication
    @RequestMapping(value = "/testAuthc", method = RequestMethod.GET)
    @ApiOperation(value = "测试admin权限", notes = "测试admin权限")
    public HttpEntity testAuthc() {
        System.out.println("==============================================进来了===============================");
       /* try {
            SecurityUtils.getSubject().isPermitted("admin:user:add");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
//        SecurityUtils.getSubject().checkRole("admin");
        System.out.println("---------->>>>>>>SecurityUtils.getSubject().hasRole(\"admin\"):" + SecurityUtils.getSubject().hasRole("admin"));
        return new ResponseEntity("test authc", HttpStatus.OK);
    }

}
