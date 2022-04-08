package com.itl.iap.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.auth.dto.AuthClientDto;
import com.itl.iap.auth.entity.AuthAccessToken;
import com.itl.iap.auth.entity.AuthClient;
import com.itl.iap.auth.service.IAuthAccessTokenService;
import com.itl.iap.auth.service.IAuthClientService;
import com.itl.iap.common.base.controller.BaseController;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * (IapAuthClient)表控制层
 *
 * @author 汤俊
 * @date 2020/10/22
 * @since jdk1.8
 */

@Api("Auth-IapAuthClient表控制层")
@RestController
@RequestMapping("/authClient")
public class AuthClientController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 服务对象
     */
    @Autowired
    private IAuthClientService authClientService;
    @Autowired
    private IAuthAccessTokenService authAccessTokenService;

    @PostMapping("/add")
    @ApiOperation(value = "新增（iap_auth_client_t）表记录", notes = "新增（iap_auth_client_t）表记录")
    public ResponseData add(@RequestBody AuthClient authClient) {
        logger.info("IapAuthClientTDto add Record...");
        String clientId = UUID.uuid32();
        authClient.setId(clientId);
        authClient.setCreateDate(new Date());
        authClient.setLastUpdateDate(new Date());
        authClient.setClientId(clientId);
        authClient.setClientSecret(UUID.uuid32());
        return ResponseData.success(authClientService.saveOrUpdate(authClient));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "根据AuthClientID删除（iap_auth_client_t）表记录", notes = "根据AuthClientID删除（iap_auth_client_t）表记录")
    public ResponseData delete(@RequestBody AuthClient authClient) {
        logger.info("IapAuthClientTDto delete Record...");
        return ResponseData.success(authClientService.removeById(authClient.getId()));
    }

    @PostMapping("/deleteBatch")
    @ApiOperation(value = "根据数组（AuthClientID）批量删除（iap_auth_client_t）表记录", notes = "根据数组（AuthClientID）批量删除（iap_auth_client_t）表记录")
    public ResponseData deleteBatch(@RequestBody List<String> ids) {
        logger.info("IapAuthClientTDto deleteBatch Record...");
//        for (String id : ids) {
//            authClientService.removeById(id);
//            logger.info("delete Record id: {}", id);
//        }
//        authClientService.removeByIds(ids);
        return ResponseData.success(authClientService.removeByIds(ids));
    }

    @PostMapping("/update")
    @ApiOperation(value = "根据AuthClientID修改（iap_auth_client_t）表记录", notes = "根据AuthClientID修改（iap_auth_client_t）表记录")
    public ResponseData update(@RequestBody AuthClient authClient) {
        logger.info("IapAuthClientTDto updateRecord...");
        if (authClient.getState() != null && authClient.getState().equals(AuthClientDto.STATE_1)) {
            authAccessTokenService.remove(new QueryWrapper<AuthAccessToken>().eq("client_id", authClient.getClientId()));
        }
        return ResponseData.success(authClientService.updateById(authClient));
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询（iap_auth_client_t）表记录", notes = "分页查询（iap_auth_client_t）表记录")
    public ResponseData queryRecord(@RequestBody AuthClientDto authClientDto) {
        logger.info("IapAuthClientTDto queryRecord...");

        return ResponseData.success(authClientService.pageQuery(authClientDto));
    }
    @PostMapping("/queryByState")
    @ApiOperation(value = "分页查询（iap_auth_client_t）表记录ByState", notes = "分页查询（iap_auth_client_t）表记录ByState")
    public ResponseData queryRecordByState(@RequestBody AuthClientDto authClientDto) {
        logger.info("IapAuthClientTDto queryRecord...");

        return ResponseData.success(authClientService.pageQueryByState(authClientDto));
    }

    @PostMapping("/queryDetail")
    @ApiOperation(value = "根据传入AuthClientDto对象详情查询", notes = "根据传入AuthClientDto对象详情查询")
    public ResponseData queryDetail(@RequestBody AuthClientDto authClientDto) {
        logger.info("IapAuthClientTDto queryRecord...");
        AuthClient authClient = new AuthClient();
        BeanUtils.copyProperties(authClientDto, authClient);
        return ResponseData.success(authClientService.getOne(new QueryWrapper<AuthClient>(authClient)));
    }
}