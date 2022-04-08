package com.itl.im.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.im.provider.util.SnowIdUtil;
import iap.im.api.dto.IapImGroupMemberDto;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.entity.IapImGroup;
import iap.im.api.entity.IapImGroupMember;
import iap.im.api.service.IapImGroupMessageService;
import iap.im.api.service.IapImGroupService;
import iap.im.api.service.IapImMessageService;
import iap.im.api.variable.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 群组Controller
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Api("IM-群组控制层")
@RestController
@RequestMapping("im/groupMessage")
public class IapImGroupController {

    @Resource
    private UserUtil userUtil;

    @Resource
    private IapImGroupService imGroupService;

    @Autowired
    private IapImGroupMessageService iapImGroupMessageService;

    /**
     * 创建群聊
     *
     * @param memberList
     * @return
     */
    @PostMapping("/createGroup")
    @ApiOperation(value = "创建群聊", notes = "创建群聊")
    public ResponseData<IapImMessageListDto> createGroup(@RequestBody List<IapImGroupMemberDto> memberList) {
        return ResponseData.success(iapImGroupMessageService.createGroup(memberList));

    }

    /**
     * 添加群成员
     *
     * @param groupId
     * @param memberList
     * @return
     */
    @PostMapping("/addGroupMember/{groupId}")
    @ApiOperation(value = "添加群成员", notes = "添加群成员")
    public ResponseData addGroupMember(@PathVariable("groupId") String groupId, @RequestBody List<IapImGroupMemberDto> memberList) {
        iapImGroupMessageService.addGroupMember(groupId, memberList);
        return ResponseData.success(200);
    }


    /**
     * 通过群ID，解散该群聊
     *
     * @param groupId 群ID
     * @return
     */
    @PostMapping(value = "/removeGroup/{groupId}")
    @ApiOperation(value = "通过群ID，解散该群聊", notes = "通过群ID，解散该群聊")
    public ResponseData removeGroup(@PathVariable String groupId) {
        return ResponseData.success(imGroupService.removeGroup(userUtil.getUser().getUserName(), groupId));
    }

    /**
     * 更新群的相关信息
     *
     * @param iapImGroup
     * @return
     */
    @PostMapping(value = "/updateGroupInfo")
    @ApiOperation(value = "更新群的相关信息",notes = "更新群的相关信息")
    public ResponseData updateGroupInfo(@RequestBody IapImGroup iapImGroup) {

        return ResponseData.success(imGroupService.updateGroupInfo(iapImGroup));
    }

    /**
     * 查询与我相关的所有群列表
     *
     * @return
     */
    @PostMapping(value = "/getMyAllGroupList")
    @ApiOperation(value = "查询与我相关的所有群列表",notes = "查询与我相关的所有群列表")
    public ResponseData getMyAllGroupList() {
        return ResponseData.success(imGroupService.getMyAllGroupList(userUtil.getUser().getUserName()));
    }

}
