package com.itl.iap.notice.provider.exchanger;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.util.UUID;

import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.entity.MsgSendRecord;
import com.itl.iap.notice.api.entity.MsgType;
import com.itl.iap.notice.api.service.MsgTypeService;
import com.itl.iap.notice.provider.mapper.MsgPublicTemplateMapper;
import com.itl.iap.notice.provider.mapper.MsgSendRecordMapper;
import com.itl.iap.notice.provider.util.FreemarkerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 通知适配器底层接口
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
@Slf4j
public abstract class AbstractNoticeExchanger {

    @Autowired
    private MsgTypeService msgTypeService;
    @Resource
    private MsgSendRecordMapper msgSendRecordMapper;

    @Autowired
    private MsgPublicTemplateMapper msgPublicTemplateMapper;


    /**
     * 匹配通知内容类型
     *
     * @param notice
     * @return
     */
    public abstract boolean match(Map<String, Object> notice);

    /**
     * 分派请求
     *
     * @param notice
     * @return
     */
    public abstract boolean exchanger(Map<String, Object> notice) throws Exception;


    public Map<String, Object> parseMes(String code, Map<String, Object> params) {

        MsgPublicTemplate msgPublicTemplate = new MsgPublicTemplate();
        msgPublicTemplate.setCode(code);
        QueryWrapper<MsgPublicTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        msgPublicTemplate = msgPublicTemplateMapper.selectOne(queryWrapper);

        if (msgPublicTemplate == null) {
            log.error("------------------>没有查找到模板");
            throw new RuntimeException("没有查找到模板");
        }
        if (msgPublicTemplate.getNoticeEnabledFlag().intValue() == 0) {
            log.error("------------------>未开启该模板");
            throw new RuntimeException("未开启该模板");
        }
        MsgType msgType = msgTypeService.selectById(msgPublicTemplate.getMsgTypeId());
        //freemarker解析模板，填充模板内容
        //标题
        String title = msgPublicTemplate.getTitle();
        //内容
        String sysNoticeContent = msgPublicTemplate.getContent();
        try {
            title = FreemarkerUtils.generateContent(params, title);
            sysNoticeContent = FreemarkerUtils.generateContent(params, sysNoticeContent);
        } catch (Exception e) {
            log.error("------------------>转化模板失败");
            e.printStackTrace();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("msgType", msgType);
        result.put("title", title);
        result.put("sysNoticeContent", sysNoticeContent);
        result.put("msgPublicTemplate", msgPublicTemplate);

        return result;
    }

    public void saveSendMessage(MsgType msgType, MsgPublicTemplate msgPublicTemplate, String title, String sysNoticeContent,
                                Integer type, Integer status) {
        MsgSendRecord msgSendRecord = new MsgSendRecord();
        if (msgType != null) {
            msgSendRecord.setMsgType(msgType.getName());
        }
        System.out.println("用户ID" + msgPublicTemplate.getUserId());
        if (StringUtils.isEmpty(msgPublicTemplate.getUserId())) {
            return;
        }
        msgSendRecord.setSendTime(new Date());
        msgSendRecord.setMsgPublicTemplateId(msgPublicTemplate.getId());
        msgSendRecord.setTitle(title);
        msgSendRecord.setContent(sysNoticeContent);
        msgSendRecord.setNoticeTypeCode(msgPublicTemplate.getNoticeTypeCode());
        msgSendRecord.setStatus(status);
        msgSendRecord.setCreateTime(new Date());
        msgSendRecord.setCreateName("系统发送");
        msgSendRecord.setSendType(type);
        System.out.println("类型" + type);
        String[] ids = msgPublicTemplate.getUserId().split(",");
        String[] names = msgPublicTemplate.getUserName().split(",");
        //是否多个用户
        if (ids.length == 0) {
            msgSendRecord.setReceiverName(msgPublicTemplate.getUserName())
                    .setReceiverUid(msgPublicTemplate.getUserId());
            msgSendRecord.setId(UUID.uuid32());
            msgSendRecordMapper.insert(msgSendRecord);
        }
        if (ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                msgSendRecord.setReceiverUid(ids[i]);
                msgSendRecord.setReceiverName(names[i]);
                msgSendRecord.setId(UUID.uuid32());
                msgSendRecordMapper.insert(msgSendRecord);
            }
        }
    }
}
