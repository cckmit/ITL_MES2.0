package com.itl.im.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.dto.IapImMessageTopDto;
import iap.im.api.service.IapImMessageService;
import iap.im.api.service.IapImSendMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 消息Controller
 *
 * @author tanq
 * @date 2020/9/25
 * @since jdk1.8
 */
@Api("IM-消息控制层")
@RestController
@RequestMapping("im/message")
public class IapImMessageController {

    @Autowired
    private IapImMessageService iapImMessageService;
    @Autowired
    private IapImSendMessageService iapImSendMessageService;

    /**
     * 获取会话列表+消息
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取会话列表+消息", notes = "获取会话列表+消息")
    public ResponseData<List<IapImMessageListDto>> getConversationList() {
        return ResponseData.success(iapImMessageService.msgAll(""));
    }

    /**
     * 查看消息历史
     *
     * @param messageDto messageDto.sender 自己 messageDto.receiver 对方
     * @return
     */
    @PostMapping("/messageHistory")
    @ApiOperation(value = "查看消息历史", notes = "查看消息历史")
    public ResponseData<IPage<IapImMessageDto>> messageHistory(@RequestBody IapImMessageDto messageDto) {
        return ResponseData.success(iapImMessageService.messageHistory(messageDto));
    }

    /**
     * 查看消息历史
     *
     * @param messageDto messageDto.sender 自己 messageDto.receiver 对方
     * @return
     */
    @PostMapping("/imgMessageHistory")
    @ApiOperation(value = "查看消息历史", notes = "查看消息历史")
    public ResponseData<IPage<IapImMessageListDto>> imgMessageHistory(@RequestBody IapImMessageDto messageDto) {
        return ResponseData.success(iapImMessageService.imgMessageHistory(messageDto));
    }

    /**
     * 发送消息
     *
     * @param iapImMessageDto
     * @return
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送消息", notes = "发送消息")
    public ResponseData sendMessage(@RequestBody IapImMessageDto iapImMessageDto) {
        return ResponseData.success(iapImSendMessageService.sendMessage(iapImMessageDto));
    }

    /**
     * 消息转发
     *
     * @param iapImMessageDto
     * @return
     */
    @PostMapping("/forward")
    @ApiOperation(value = "消息转发", notes = "消息转发")
    public ResponseData<IapImMessageListDto> forwardAllMessage(@RequestBody IapImMessageDto iapImMessageDto) {
        return ResponseData.success(iapImSendMessageService.forwardAllMessage(iapImMessageDto));
    }


    /**
     * 撤回消息
     *
     * @param iapImMessageDto
     * @return
     */
    @DeleteMapping("/revoke")
    @ApiOperation(value = "撤回消息", notes = "撤回消息")
    public ResponseData revokeMessage(@RequestBody IapImMessageDto iapImMessageDto) {
        try {
            iapImMessageService.revokeMessage(iapImMessageDto);
            return ResponseData.success(200);
        } catch (Exception e) {
            return ResponseData.error("500", e.getMessage());
        }
    }

    /**
     * 查找用户信息（向对方发送自己的信息卡片）
     *
     * @return
     */
    @GetMapping(value = "/userinfo")
    @ApiOperation(value = "查找用户信息（向对方发送自己的信息卡片）", notes = "查找用户信息（向对方发送自己的信息卡片）")
    public ResponseData<Map<String, String>> findOnUser() {
        return ResponseData.success(iapImMessageService.findOnUser());
    }

    /**
     * 阅读消息并回执
     *
     * @param otherSide 对方
     * @param mark      消息标识
     * @return
     */
    @GetMapping(value = "/updateMsgState")
    @ApiOperation(value = "阅读消息并回执", notes = "阅读消息并回执")
    public ResponseData<String> updateMsgState(@RequestParam("otherSide") String otherSide, @RequestParam("mark") short mark) {
        String status = iapImMessageService.updateMsgState(otherSide, mark);
        return ResponseData.success(status);
    }

    /**
     * 消息内容置顶
     *
     * @param messageTopDto 置顶消息
     * @return
     */
    @PostMapping(value = "/addMessageTop")
    @ApiOperation(value = "消息内容置顶", notes = "消息内容置顶")
    public ResponseData addMessageTop(@RequestBody IapImMessageTopDto messageTopDto) {
        iapImMessageService.addMessageTop(messageTopDto);
        return ResponseData.success();
    }

    /**
     * 批量修改消息状态
     *
     * @param listIds
     * @return
     */
    @PostMapping("/updateListState")
    @ApiOperation(value = "批量修改消息状态", notes = "批量修改消息状态")
    public ResponseData<String> updateListState(@RequestBody List<String> listIds) {
        return ResponseData.success(iapImMessageService.updateListState(listIds));
    }

    /**
     * 取消 消息内容置顶
     *
     * @param id 置顶消息id
     * @return
     */
    @DeleteMapping(value = "/deleteMessageTop")
    @ApiOperation(value = "取消消息内容置顶", notes = "取消消息内容置顶")
    public ResponseData deleteMessageTop(@RequestParam("id") String id) {
        iapImMessageService.deleteMessageTop(id);
        return ResponseData.success();
    }

    /**
     * 删除消息内容
     *
     * @param id 消息ID
     * @return
     */
    @PostMapping(value = "/deleteMessage")
    @ApiOperation(value = "删除消息内容", notes = "删除消息内容")
    public ResponseData<Boolean> deleteMessages(@RequestBody() List<String> id) {
        return ResponseData.success(iapImMessageService.deleteMessages(id));
    }
}
