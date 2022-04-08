package com.itl.im.provider.controller;

import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import iap.im.api.dto.IapImMessageSettingDto;
import iap.im.api.service.IapImMessageSettingServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户IM配置Controller
 *
 * @author tanq
 * @date 2020/9/25
 * @since jdk1.8
 */
@Api("IM-用户配置控制层")
@RestController
@RequestMapping("im/setting")
public class IapImMessageSettingController {

    @Autowired
    private IapImMessageSettingServer messageSettingServer;


    /**
     * 获取当前用户的Im配置信息
     *
     * @param iapImMessageSettingDto
     * @return
     */
    @GetMapping("/getSetting")
    @ApiOperation(value = "获取当前用户的Im配置信息", notes = "获取当前用户的Im配置信息")
    public ResponseData<IapImMessageSettingDto> selectByUser(IapImMessageSettingDto iapImMessageSettingDto) {
        return ResponseData.success(messageSettingServer.selectByUserId(iapImMessageSettingDto));

    }

    /**
     * 新增或修改当前用户的Im配置信息
     *
     * @return
     */
    @PostMapping("/intoOrUpSetting")
    @ApiOperation(value = "新增或修改当前用户的Im配置信息", notes = "新增或修改当前用户的Im配置信息")
    public ResponseData<Boolean> intoOrUpSetting(@RequestBody IapImMessageSettingDto messageSetting) {
        return ResponseData.success(messageSettingServer.intoOrUpSetting(messageSetting));
    }

    /**
     * 删除当前用户自定义消息回复内容
     *
     * @return
     */
    @PostMapping("/delCustomById")
    @ApiOperation(value = "删除当前用户自定义消息回复内容", notes = "删除当前用户自定义消息回复内容")
    public ResponseData<Boolean> deleteCustomSetting(@RequestBody List<String> customIds) {
        return ResponseData.success(messageSettingServer.deleteCustomSetting(customIds));
    }
}
