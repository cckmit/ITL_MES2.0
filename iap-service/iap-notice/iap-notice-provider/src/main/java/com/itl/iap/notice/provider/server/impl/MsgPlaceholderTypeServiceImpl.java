package com.itl.iap.notice.provider.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.UUID;

import com.itl.iap.notice.api.entity.MsgPlaceholderType;
import com.itl.iap.notice.api.service.MsgPlaceholderTypeService;
import com.itl.iap.notice.provider.mapper.MsgPlaceholderTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


/**
 * (MsgPlaceholderType)表服务实现类
 *
 * @author liaochengdian
 * @date  2020-04-07
 * @since jdk1.8
 */
@Service("msgPlaceholderTypeService")
public class MsgPlaceholderTypeServiceImpl extends ServiceImpl<MsgPlaceholderTypeMapper, MsgPlaceholderType> implements MsgPlaceholderTypeService {
    
    @Resource
    private MsgPlaceholderTypeMapper msgPlaceholderTypeMapper;

    /**
     * 新增
     * @param typeName
     * @return MsgPlaceholderType
     */
    @Override
    public MsgPlaceholderType updateTypeData(String typeName) {
        MsgPlaceholderType msgPlaceholderType=msgPlaceholderTypeMapper.selectByName(typeName);
        if(msgPlaceholderType==null){
            MsgPlaceholderType msgPlaceholderType2=new MsgPlaceholderType();
            msgPlaceholderType2.setId(UUID.uuid32());
            msgPlaceholderType2.setName(typeName);
            msgPlaceholderType2.setCreateTime(new Date());
             msgPlaceholderTypeMapper.insert(msgPlaceholderType2);
             return msgPlaceholderType2;
        }else {
            return msgPlaceholderType;
        }
    }
}