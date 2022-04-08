package com.itl.iap.notice.provider.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itl.iap.notice.api.dto.MsgTypeDto;
import com.itl.iap.notice.api.entity.MsgType;
import com.itl.iap.notice.api.service.MsgTypeService;
import com.itl.iap.notice.provider.mapper.MsgTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;


/**
 * 消息类型表(MsgType)表服务实现类
 *
 * @author liaochengdian
 * @date  2020-03-25
 * @since jdk1.8
 */
@Service("msgTypeService")
public class MsgTypeServiceImpl extends ServiceImpl<MsgTypeMapper, MsgType> implements MsgTypeService {
    
    @Resource
    private MsgTypeMapper msgTypeMapper;

    /**
     * 查询消息类型树
     * @param id 消息类型id
     * @return List<MsgType>
     */
    @Override
    public List<MsgType> selectMsgTypeTree(String id){
        return msgTypeMapper.selectMsgTypeTree(id);
    }

    /**
     * 获取消息类型详情
     * @param id 消息类型id
     * @return MsgType
     */
    @Override
    public MsgType selectById(String id) {
        return msgTypeMapper.selectById(id);
    }

    @Override
    public List<MsgType> selectDynamicMsgTypes(MsgTypeDto msgTypeDto) {
        return msgTypeMapper.selectDynamicMsgTypes(msgTypeDto);
    }

    @Override
    public boolean checkOwn(MsgType record) {
        QueryWrapper<MsgType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", record.getCode());
        //此查询条件是验证修改操作时，编码验证
        if(!StringUtils.isEmpty(record.getId())){
            queryWrapper.eq("id", record.getId());
        }
        List<MsgType> msgTypeList = msgTypeMapper.selectList(queryWrapper);
        if(!CollectionUtils.isEmpty(msgTypeList)){
            return true;
        }
        return false;
    }

}