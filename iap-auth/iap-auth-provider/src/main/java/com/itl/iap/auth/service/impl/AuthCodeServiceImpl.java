package com.itl.iap.auth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.auth.dto.AuthCodeDto;
import com.itl.iap.auth.entity.AuthCode;
import com.itl.iap.auth.mapper.IAuthCodeMapper;
import com.itl.iap.auth.service.IAuthCodeService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;


/**
 * (IapAuthCodeT)表服务实现类
 *
 * @author 汤俊
 * @date 2020-06-17 19:49:17
 * @since jdk1.8
 */
@Service("iapAuthCodeTService")
public class AuthCodeServiceImpl extends ServiceImpl<IAuthCodeMapper, AuthCode> implements IAuthCodeService {

    @Resource
    private IAuthCodeMapper iAuthCodeMapper;

    /**
     * 分页查询
     *
     * @param authCodeDto 需要查询的条件
     * @return IPage<AuthCodeDto> 查询结果的分页对象
     */
    @Override
    public IPage<AuthCodeDto> pageQuery(AuthCodeDto authCodeDto) {
        if (null == authCodeDto.getPage()) {
            authCodeDto.setPage(new Page(0, 10));
        }
        return iAuthCodeMapper.pageQuery(authCodeDto.getPage(), authCodeDto);
    }


    private String currentUsername() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

/*    @Override
    public String retrieveAuthCode(AuthClient clientDetails) throws OAuthSystemException {
        final String clientId = clientDetails.getClientId();
        final String username = currentUsername();
        authCodeMapper.selectOne(new QueryWrapper<>().lambda().eq(AuthCode::getUserId,username).eq(AuthCode::getUser));
        AuthCode oauthCode = authCodeMapper.findOauthCodeByUsernameClientId(username, clientId);
        if (oauthCode != null) {
            //Always delete exist
            LOG.debug("OauthCode ({}) is existed, remove it and create a new one", oauthCode);
            oauthRepository.deleteOauthCode(oauthCode);
        }
        //create a new one
        oauthCode = createOauthCode(clientDetails);

        return oauthCode.code();
    }


    @Override
    public AccessToken retrieveAccessToken(ClientDetails clientDetails, Set<String> scopes, boolean includeRefreshToken) throws OAuthSystemException {
        String scope = OAuthUtils.encodeScopes(scopes);
        final String username = currentUsername();
        final String clientId = clientDetails.getClientId();

        final String authenticationId = authenticationIdGenerator.generate(clientId, username, scope);

        AccessToken accessToken = oauthRepository.findAccessToken(clientId, username, authenticationId);
        if (accessToken == null) {
            accessToken = createAndSaveAccessToken(clientDetails, includeRefreshToken, username, authenticationId);
            LOG.debug("Create a new AccessToken: {}", accessToken);
        }

        return accessToken;
    }

    //Always return new AccessToken, exclude refreshToken
    @Override
    public AccessToken retrieveNewAccessToken(ClientDetails clientDetails, Set<String> scopes) throws OAuthSystemException {
        String scope = OAuthUtils.encodeScopes(scopes);
        final String username = currentUsername();
        final String clientId = clientDetails.getClientId();

        final String authenticationId = authenticationIdGenerator.generate(clientId, username, scope);

        AccessToken accessToken = oauthRepository.findAccessToken(clientId, username, authenticationId);
        if (accessToken != null) {
            LOG.debug("Delete existed AccessToken: {}", accessToken);
            oauthRepository.deleteAccessToken(accessToken);
        }
        accessToken = createAndSaveAccessToken(clientDetails, false, username, authenticationId);
        LOG.debug("Create a new AccessToken: {}", accessToken);

        return accessToken;
    }*/

}