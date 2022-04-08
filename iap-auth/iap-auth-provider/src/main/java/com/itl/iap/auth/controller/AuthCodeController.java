package com.itl.iap.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.auth.dto.AuthCodeDto;
import com.itl.iap.auth.entity.AuthCode;
import com.itl.iap.auth.service.IAuthCodeService;
import com.itl.iap.common.base.controller.BaseController;

import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (IapAuthCode)表控制层
 *
 * @author 汤俊
 * @date 2020/10/22
 * @since jdk1.8
 */

@Api("Auth-IapAuthCode表控制层")
@RestController
@RequestMapping("/authCode")
public class AuthCodeController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 服务对象
     */
    @Autowired
    private IAuthCodeService authCodeService;

    @PostMapping("/add")
    @ApiOperation(value = "新增（iap_auth_code_t）表记录", notes = "新增（iap_auth_code_t）表记录")
    public ResponseData add(@RequestBody AuthCode authCode) {
        logger.info("IapAuthCodeTDto add Record...");
        return ResponseData.success(authCodeService.saveOrUpdate(authCode));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据AuthCode的ID删除（iap_auth_code_t）表记录", notes = "根据AuthCode的ID删除（iap_auth_code_t）表记录")
    public ResponseData delete(@RequestBody AuthCode authCode) {
        logger.info("IapAuthCodeTDto delete Record...");
        return ResponseData.success(authCodeService.remove(new QueryWrapper<AuthCode>().lambda().eq(AuthCode::getCode, authCode.getCode())));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据AuthCode的ID修改（iap_auth_code_t）表记录", notes = "根据AuthCode的ID修改（iap_auth_code_t）表记录")
    public ResponseData update(@RequestBody AuthCode authCode) {
        logger.info("IapAuthCodeTDto updateRecord...");
        return ResponseData.success(authCodeService.updateById(authCode));
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询（iap_auth_code_t）表记录", notes = "分页查询（iap_auth_code_t）表记录")
    public ResponseData queryRecord(@RequestBody AuthCodeDto authCodeDto) {
        logger.info("IapAuthCodeTDto queryRecord...");
        return ResponseData.success(authCodeService.pageQuery(authCodeDto));
    }
}