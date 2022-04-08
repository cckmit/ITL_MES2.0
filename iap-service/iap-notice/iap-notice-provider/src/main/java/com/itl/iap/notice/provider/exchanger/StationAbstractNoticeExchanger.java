package com.itl.iap.notice.provider.exchanger;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.util.JsonUtils;
import com.itl.iap.common.util.UUID;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.entity.MsgSendRecord;
import com.itl.iap.notice.api.entity.MsgType;
import com.itl.iap.notice.api.entity.email.model.StationNotice;
import com.itl.iap.notice.api.enums.SendStatusEnum;
import com.itl.iap.notice.api.enums.SendTypeEnum;
import com.itl.iap.notice.api.service.MsgSendRecordService;
import com.itl.iap.notice.provider.core.websocket.WebSocket;
import com.itl.iap.notice.provider.mapper.MsgSendRecordMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 站内信通知适配器类
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
@Component
public class StationAbstractNoticeExchanger extends AbstractNoticeExchanger {
    private Logger logger = LoggerFactory.getLogger(StationAbstractNoticeExchanger.class);
    @Autowired
    private WebSocket webSocket;
    @Resource
    private MsgSendRecordMapper msgSendRecordMapper;

    @Override
    public boolean match(Map<String, Object> notice) {

        if (notice.get("type") == null || !String.valueOf(SendTypeEnum.STATION.getItem()).equals(notice.get("type").toString())) {
            return false;
        }
        return true;

    }
//
//    @Autowired
//    protected AccessTokenUtils accessTokenUtils;

    @Override
    public boolean exchanger(Map<String, Object> map) throws Exception {
        StationNotice notice = new StationNotice();
        BeanUtils.populate(notice, map);
        boolean flag = false;
        String userId = notice.getUserId();
        Assert.notNull(userId, "receiverUid is null");
        String code = notice.getCode();
        Map<String, Object> params = notice.getParams();

        Map<String, Object> result = parseMes(code, params);
        MsgType msgType = (MsgType) result.get("msgType");
        MsgPublicTemplate msgPublicTemplate = (MsgPublicTemplate) result.get("msgPublicTemplate");
        String title = result.get("title") == null ? "" : result.get("title").toString();
        String sysNoticeContent = result.get("sysNoticeContent") == null ? "" : result.get("sysNoticeContent").toString();
        //发送站内信
        MsgSendRecord msgSendRecord = new MsgSendRecord();
        if (msgType != null) {
            msgSendRecord.setMsgType(msgType.getName());
        }
        msgSendRecord.setSendTime(new Date());
        msgSendRecord.setMsgPublicTemplateId(msgPublicTemplate.getId());
        msgSendRecord.setTitle(title);
        msgSendRecord.setContent(sysNoticeContent);
        msgSendRecord.setNoticeTypeCode(msgPublicTemplate.getNoticeTypeCode());
        msgSendRecord.setReceiverUid(notice.getUserId());
        msgSendRecord.setReceiverName(notice.getUserName());
        //发送成功
        msgSendRecord.setStatus(SendStatusEnum.SEND_SUCCESS.getItem());
        msgSendRecord.setId(UUID.uuid32());
        msgSendRecord.setCreateTime(new Date());
        msgSendRecord.setCreateName("系统发送");
        msgSendRecord.setSendType(SendTypeEnum.STATION.getItem());
        msgSendRecordMapper.insert(msgSendRecord);
        flag = true;
        //发送websocket提醒到客户端
        /*String socketMessage = JsonUtils.deserializer(msgSendRecord);
        webSocket.sendMessageTo(socketMessage, userId);
        logger.info("send success!");*/

        // 向前端发送未读个数
        QueryWrapper<MsgSendRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_uid", userId).eq("read_flag", 0);
        int count=msgSendRecordMapper.selectCount(queryWrapper);
        webSocket.sendMessageTo(count+"",userId);


        return flag;
    }
}
