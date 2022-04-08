package com.itl.iap.notice.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.UUID;
import com.itl.iap.notice.api.dto.MsgSendRecordDto;
import com.itl.iap.notice.api.entity.MsgSendRecord;
import com.itl.iap.notice.api.pojo.ResponseCode;
import com.itl.iap.notice.api.service.MsgSendRecordService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 消息发送记录表(MsgSendRecord)表控制层
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
@RestController
@RequestMapping("/msgSendRecord")
public class MsgSendRecordController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserUtil userUtil;
    @Autowired
    private MsgSendRecordService msgSendRecordService;

    @PostMapping("/add")
    @ApiOperation(value = "新增发送记录", notes = "新增发送记录")
    public ResponseData<Boolean> addRecord(@RequestBody MsgSendRecord record) {
        logger.info("新增发送记录");
        record.setId(UUID.uuid32());
//            if(currentUser!=null){
//                record.setCreateName(currentUser.getUserName());
//            }
        record.setCreateTime(new Date()).setCreateName("admin");
        return ResponseData.success(msgSendRecordService.save(record));
    }

    /**
     * 根据id获取发送记录详情
     *
     * @return
     */
    @GetMapping("/getMsgSendRecordInf/id/{id}")
    @ApiOperation(value = "根据id获取发送记录详情", notes = "根据id获取发送记录详情")
    public ResponseData<MsgSendRecordDto> getMsgSendRecordInf(@PathVariable("id") final String id) {
        return ResponseData.success(msgSendRecordService.getById(id));
    }

    /**
     * 根据用户id获取发送的消息
     *
     * @return
     */
    @GetMapping("/getMessageByUserName/{username}")
    @ApiOperation(value = "根据用户id获取发送的消息", notes = "根据用户id获取发送的消息")
    public ResponseData<IPage<Map<String, Object>>> getMessageByUserName(@PathVariable("username") String username, @RequestParam(value = "noticeType", required = false) Integer noticeType,
                                                                         @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        if ("undefined".equalsIgnoreCase(username)) {
            username = null;
        }
        return ResponseData.success(msgSendRecordService.getMessageByUserName(username, noticeType, page, pageSize));
    }

    @PostMapping("/table")
    @ApiOperation(value = "分页查询消息发送记录", notes = "分页查询消息发送记录")
    public ResponseData<IPage<MsgSendRecordDto>> getMsgSendRecordList(@RequestBody MsgSendRecordDto query) {
        return ResponseData.success(msgSendRecordService.query(query));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改记录", notes = "修改记录")
    public ResponseData<Boolean> updateRecord(@RequestBody MsgSendRecord record) {
        try {
            msgSendRecordService.updateReadFlag(record);
        } catch (Exception e) {
            logger.error("修改记录：" + e.getMessage());
            e.printStackTrace();
            return ResponseData.error(false);
        }
        return ResponseData.success(true);
    }

    /**
     * 根据用户id获取发送的消息数量
     *
     * @return
     */
    @PostMapping("/getUnread")
    @ApiOperation(value = "根据用户id获取发送的消息", notes = "根据用户id获取发送的消息")
    public ResponseData<Integer> getUnread() {
//        String username = accessTokenUtils.getUserInfo().getUserName();
//        String userId = "237f7ed50a864294864e0469df659084";
        UserTDto user = userUtil.getUser();
        return ResponseData.success(msgSendRecordService.getUnread(user.getUserName()));
    }

    @PostMapping("/queryReceive")
    @ApiOperation(value = "分页查询消息发送记录(首页)", notes = "分页查询消息发送记录(首页)")
    public ResponseData<IPage<MsgSendRecordDto>> queryReceive(@RequestBody MsgSendRecordDto query) {
        UserTDto user = userUtil.getUser();
        query.setUserName(user.getUserName());
        query.setUserId(user.getId());
//        query.setUserName(accessTokenUtils.getUserInfo().getUserName());
//        query.setUserId("237f7ed50a864294864e0469df659084");
        return ResponseData.success(msgSendRecordService.getReceiveListByUsername(query));
    }

}
