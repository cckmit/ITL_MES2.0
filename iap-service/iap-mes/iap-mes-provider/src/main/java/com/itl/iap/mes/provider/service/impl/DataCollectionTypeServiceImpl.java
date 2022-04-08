package com.itl.iap.mes.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.DataCollectionType;
import com.itl.iap.mes.provider.annotation.ParseState;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.DataCollectionTypeMapper;
import com.mchange.v2.beans.BeansUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class DataCollectionTypeServiceImpl{

    @Autowired
    private DataCollectionTypeMapper dataCollectionTypeMapper;

    @ParseState(Constant.EnabledEnum.class)
    public Object findList(DataCollectionType dataCollectionType, Integer pageNum, Integer pageSize) {
        dataCollectionType.setSite(UserUtils.getSite());
        Page page = new Page(pageNum,pageSize);
        return dataCollectionTypeMapper.findList(page,dataCollectionType);
    }

    @ParseState(Constant.EnabledEnum.class)
    public Object findListByState(DataCollectionType dataCollectionType, Integer pageNum, Integer pageSize) {
        dataCollectionType.setSite(UserUtils.getSite());
        Page page = new Page(pageNum,pageSize);
        return dataCollectionTypeMapper.findListByState(page,dataCollectionType);
    }


    public void save(DataCollectionType dataCollection) {
        dataCollection.setSite(UserUtils.getSite());
        QueryWrapper<DataCollectionType> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("type",dataCollection.getType()).eq("site",UserUtils.getSite());
        if(dataCollection.getId() != null) entityWrapper.ne("id",dataCollection.getId());
        List<DataCollectionType> dataCollectionTypes = dataCollectionTypeMapper.selectList(entityWrapper);
        if(!dataCollectionTypes.isEmpty()){
            throw new CustomException(CommonCode.TYPE_REPEAT);
        }
        if(dataCollection.getId() != null){
            dataCollection.setCreateDate(new Date());
            dataCollectionTypeMapper.updateById(dataCollection);
        }else{
            dataCollectionTypeMapper.insert(dataCollection);
        }
    }


    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            dataCollectionTypeMapper.deleteById(id);
        });
    }


    public DataCollectionType findById(String id) {
        return dataCollectionTypeMapper.selectById(id);
    }

    public Object queryForLov(Map<String, Object> params) {
        Map<String,Object> map = (Map<String,Object>)params.get("page");
        params.remove("page");
        DataCollectionType dataCollectionType = JSONObject.parseObject(JSONObject.toJSONString(params), DataCollectionType.class);
        return findList(dataCollectionType, Integer.valueOf(map.get("current").toString()), Integer.valueOf(map.get("size").toString()));
    }
}
