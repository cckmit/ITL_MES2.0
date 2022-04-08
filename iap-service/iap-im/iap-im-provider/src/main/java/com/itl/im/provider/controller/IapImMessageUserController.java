package com.itl.im.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import iap.im.api.entity.IapImMessage;
import iap.im.api.entity.IapImMessageUser;
import iap.im.api.service.IapImMessageService;
import iap.im.api.service.IapImMessageUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息列表Controller
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
@Api("IM-消息列表控制层")
@RestController
@RequestMapping("im/messageUser")
public class IapImMessageUserController {

    @Resource
    private IapImMessageUserService iapImMessageUserService;
    @Resource
    private IapImMessageService iapImMessageService;


    /**
     * 改变消息列表状态 （隐藏/置顶/删除）
     *
     * @param iapImMessageUser
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "改变消息列表状态", notes = "改变消息列表状态")
    public ResponseData updateIapImMessageUserItem(@RequestBody IapImMessageUser iapImMessageUser) {

        return ResponseData.success(iapImMessageUserService.updateIapImMessageUser(iapImMessageUser));
    }

    /**
     * 保存两个人到消息列表
     *
     * @param iapImMessage
     * @return
     */
    @PostMapping("/saveMessageUser")
    @ApiOperation(value = "保存两个人到消息列表", notes = "保存两个人到消息列表")
    public ResponseData<IapImMessageUser> saveOrUpdateMessageUserItem(@RequestBody IapImMessage iapImMessage) {
        return ResponseData.success(iapImMessageService.saveOrUpdateUser(iapImMessage));
    }
}
