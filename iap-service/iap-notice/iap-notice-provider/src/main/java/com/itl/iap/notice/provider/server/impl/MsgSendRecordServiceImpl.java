package com.itl.iap.notice.provider.server.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.notice.api.dto.MsgSendRecordDto;
import com.itl.iap.notice.api.entity.MsgSendRecord;
import com.itl.iap.notice.api.service.MsgSendRecordService;
import com.itl.iap.notice.provider.mapper.MsgSendRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * 消息发送记录表(MsgSendRecord)表服务实现类
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
@Service("msgSendRecordService")
public class MsgSendRecordServiceImpl extends ServiceImpl<MsgSendRecordMapper, MsgSendRecord> implements MsgSendRecordService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MsgSendRecordMapper msgSendRecordMapper;

    @Override
    public MsgSendRecordDto getById(String id) {
        return msgSendRecordMapper.getById(id, MsgSendRecordDto.STATUS,
                MsgSendRecordDto.SERVICE_MODULE,
                MsgSendRecordDto.SEND_TYPE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReadFlag(MsgSendRecord msgSendRecord) {
        msgSendRecordMapper.updateReadFlag(msgSendRecord);
    }

    @Override
    public IPage<MsgSendRecord> getMsgSendRecordList(MsgSendRecordDto query, Boolean isList) {
//        logger.info("---------------isList:  " + isList);
//        if (!isList) {
//            String sysUserId = accessTokenUtils.getUserInfo().getID();
//            String reUserId = userClient.getUserBySysUserId(sysUserId).getData();
//            query.setReceiverUid(reUserId);
//        } else {
//            query.setReceiverUid(null);
//        }
        query.setReceiverUid(null);
        return msgSendRecordMapper.getMsgSendRecordList(new Page(query.getPageNum(), query.getPageSize()), query);
    }

    public List<MsgSendRecordDto> parseCode(List<MsgSendRecord> msgSendRecords) {
        List<MsgSendRecordDto> msgSendRecordDtos = new ArrayList<>();
        msgSendRecords.forEach(msgSendRecord -> {
            MsgSendRecordDto msgSendRecordDto = parseCodeOne(msgSendRecord);
            msgSendRecordDtos.add(msgSendRecordDto);
        });
        return msgSendRecordDtos;
    }

    public MsgSendRecordDto parseCodeOne(MsgSendRecord msgSendRecord) {
//        MsgSendRecordDto msgSendRecordDto = new MsgSendRecordDto();
//        BeanUtils.copyProperties(msgSendRecord, msgSendRecordDto);
//        if(msgSendRecord.getNoticeTypeCode() != null){
//            msgSendRecordDto.setNoticeTypeName(coreClient.getNameByCodeAndVal("NOTICE_TYPE_CODE", msgSendRecord.getNoticeTypeCode()+""));
//        }
//
//        if(msgSendRecord.getSendType() != null){
//            msgSendRecordDto.setSendTypeName(coreClient.getNameByCodeAndVal("MESSAGE_TYPE_CODE", msgSendRecord.getSendType()+""));
//        }
//
//        if(msgSendRecord.getMsgPublicTemplateId() != null){
//            MsgPublicTemplate msgPublicTemplate = new MsgPublicTemplate();
//            msgPublicTemplate.setId(msgSendRecord.getMsgPublicTemplateId());
//            msgPublicTemplate = msgPublicTemplateMapper.selectByPrimaryKey(msgPublicTemplate);
//            msgSendRecordDto.setServiceModuleName(coreClient.getNameByCodeAndVal("SERVICE_MODULE_CODE", msgPublicTemplate.getServiceModuleCode()+""));
//        }
//
//        return msgSendRecordDto;
        return null;
    }

    /**
     * 查询系统发送未读数量
     *
     * @param userId   接收人id
     * @param page     当前页
     * @param pageSize 页面大小
     * @return Map
     */
    @Override
    public Map getMsgSendRecordListByUser(String userId, Integer page, Integer pageSize) {
//        if(page != null && pageSize !=null) {
//            PageHelper.startPage(page, pageSize);
//        }
        QueryWrapper<MsgSendRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("send_time");
        queryWrapper.eq("receiverUid", userId);

//        List<MsgSendRecord> list =  msgSendRecordMapper.selectPage( ,queryWrapper);
//        PageInfo pageInfo = new PageInfo(list);
        queryWrapper.eq("readFlag", 0);
        Integer unReadCount = msgSendRecordMapper.selectCount(queryWrapper);
        queryWrapper.eq("createName", "系统发送");
        Integer systemUnReadCount = msgSendRecordMapper.selectCount(queryWrapper);
        Map<String, Object> map = new HashMap<>(2);
//        map.put("pageInfo",pageInfo);
        map.put("unReadCount", unReadCount);
        map.put("systemUnReadCount", systemUnReadCount);
        return map;
    }

    /**
     * 分页查询
     *
     * @param query 消息发送实例
     * @return IPage<MsgSendRecordDto>
     */
    @Override
    public IPage<MsgSendRecordDto> query(MsgSendRecordDto query) {
        return msgSendRecordMapper.findList(query.getPage(), query,
                MsgSendRecordDto.STATUS,
                MsgSendRecordDto.SERVICE_MODULE,
                MsgSendRecordDto.SEND_TYPE);
    }

    /**
     * 根据用户id获取发送的消息
     *
     * @param userId     用户id
     * @param noticeType 消息类型
     * @param page       当前页
     * @param pageNum    页面大小
     * @return IPage<Map < String, Object>>
     */
    @Override
    public IPage<Map<String, Object>> getMessageByUserName(String userId, Integer noticeType, Integer page, Integer pageNum) {
        IPage<Map<String, Object>> ret = msgSendRecordMapper.findListByUsername(new Page(page, pageNum), userId, noticeType);
        ret.getRecords().stream().forEach(x -> {
            x.put("businessId",Optional.ofNullable(x.get("businessId")).orElse(""));
            String timeStr = x.get("sendTime").toString();
            x.put("sendTime", timeStr.substring(0, timeStr.indexOf('.')));
        });
        return ret;
    }

    /**
     * 分页查询消息发送记录(首页)
     *
     * @param query 消息发送实例
     * @return IPage<MsgSendRecordDto>
     */
    @Override
    public IPage<MsgSendRecordDto> getReceiveListByUsername(MsgSendRecordDto query) {
        return msgSendRecordMapper.findReceiveListByUsername(query.getPage(), query,
                MsgSendRecordDto.STATUS,
                MsgSendRecordDto.SERVICE_MODULE,
                MsgSendRecordDto.SEND_TYPE);
    }

    /**
     * 查询接收人消息未读数
     *
     * @param username 接收人
     * @return Integer
     */
    @Override
    public Integer getUnread(String username) {
        QueryWrapper<MsgSendRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_uid", username).eq("read_flag", 0);
        return msgSendRecordMapper.selectCount(queryWrapper);
    }
}
