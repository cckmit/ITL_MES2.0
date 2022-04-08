package com.itl.iap.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.auth.dto.AuthAccessTokenDto;
import com.itl.iap.auth.entity.AuthAccessToken;
import com.itl.iap.auth.service.IAuthAccessTokenService;
import com.itl.iap.common.base.controller.BaseController;

import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (IapAuthAccessToken)表控制层
 *
 * @author 汤俊
 * @date 2020/10/22
 * @since jdk1.8
 */

@Api("Auth-IapAuthAccessToken表控制层")
@RestController
@RequestMapping("/authAccessToken")
public class AuthAccessTokenController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 服务对象
     */
    @Autowired
    private IAuthAccessTokenService authAccessTokenService;

    @PostMapping("/add")
    @ApiOperation(value = "新增（iap_auth_access_token）表记录", notes = "新增（iap_auth_access_token）表记录")
    public ResponseData add(@RequestBody AuthAccessToken authAccessToken) {
        logger.info("IapAuthAccessTokenTDto add Record...");
        return ResponseData.success(authAccessTokenService.saveOrUpdate(authAccessToken));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据AuthAccessToken的ID删除（iap_auth_access_token）表记录", notes = "根据AuthAccessToken的ID删除（iap_auth_access_token）表记录")
    public ResponseData delete(@RequestBody AuthAccessToken authAccessToken) {
        logger.info("IapAuthAccessTokenTDto delete Record...");
        return ResponseData.success(authAccessTokenService.remove(new QueryWrapper<AuthAccessToken>().lambda()
                .eq(AuthAccessToken::getTokenId, authAccessToken.getTokenId())));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据AuthAccessToken的ID修改（iap_auth_access_token）表记录", notes = "根据AuthAccessToken的ID（iap_auth_access_token）表记录")
    public ResponseData update(@RequestBody AuthAccessToken authAccessToken) {
        logger.info("IapAuthAccessTokenTDto updateRecord...");
        return ResponseData.success(authAccessTokenService.updateById(authAccessToken));
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询（iap_auth_access_token）表记录", notes = "分页查询（iap_auth_access_token）表记录")
    public ResponseData queryRecord(@RequestBody AuthAccessTokenDto authAccessTokenDto) {
        logger.info("IapAuthAccessTokenTDto queryRecord...");
        return ResponseData.success(authAccessTokenService.pageQuery(authAccessTokenDto));
    }
}