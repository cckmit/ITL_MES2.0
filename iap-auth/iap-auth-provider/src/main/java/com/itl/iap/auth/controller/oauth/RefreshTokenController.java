package com.itl.iap.auth.controller.oauth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 刷新令牌Controller
 *
 * @author 汤俊
 * @date 2020-6-20 17:49
 * @since jdk1.8
 */

@Api("Auth-刷新令牌控制层")
@Slf4j
@RestController
public class RefreshTokenController {

    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @ApiOperation(value = "刷新令牌",notes = "刷新令牌")
    public HttpEntity token(HttpServletRequest request) throws OAuthSystemException {
        return null;
    }

//    public  JwtClaims createRefreshToken(String issuer, String username){
//
//        JwtClaims claims = new JwtClaims();
//        //refreshToken
//        claims.setExpirationTimeMinutesInTheFuture(RefreshToken.REFRESH_TOKEN_VALIDITY_SECONDS);
//        claims.setGeneratedJwtId();
//        claims.setIssuedAtToNow();
//        claims.setIssuer(issuer);
//        claims.setSubject(username);
//
//        return   claims;
//    }


    /*    @RequestMapping(value = "refresh_token", method = RequestMethod.POST)
    public ResponseEntity refreshToken(String refresh_token){

        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setClientId(resourceClientId);
        resource.setClientSecret(resourceClientSecret);
        resource.setGrantType("refresh_token");
        resource.setAccessTokenUri(tokenUri);

        ResourceOwnerPasswordAccessTokenProvider provider = new ResourceOwnerPasswordAccessTokenProvider();
        OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refresh_token);
        OAuth2AccessToken accessToken = provider.refreshAccessToken(resource,refreshToken, new DefaultAccessTokenRequest());
        BaseResponse response = null;
        try {
            response = BaseResponse.createResponse(HttpStatusMsg.OK, accessToken);
        } catch (Exception e){
            response = BaseResponse.createResponse(HttpStatusMsg.AUTHENTICATION_EXCEPTION, e.toString());
        }
        return ResponseEntity.ok(response);
    }*/

}
