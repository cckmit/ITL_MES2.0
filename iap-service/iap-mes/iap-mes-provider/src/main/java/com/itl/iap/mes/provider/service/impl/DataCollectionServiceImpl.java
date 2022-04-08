package com.itl.iap.mes.provider.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.DataCollectionVo;
import com.itl.iap.mes.api.entity.DataCollection;
import com.itl.iap.mes.api.entity.DataCollectionType;
import com.itl.iap.mes.provider.mapper.DataCollectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DataCollectionServiceImpl {

    @Autowired
    private DataCollectionMapper dataCollectionMapper;

    public Object findList(String type, String name, Integer pageNum, Integer pageSize) {

        Page page = new Page(pageNum,pageSize);
        Map<String,Object> params = new HashMap<>();
        params.put("type",type);
        params.put("name",name);
        params.put("siteId", UserUtils.getSite());
        return dataCollectionMapper.findList(page,params);
    }

    public Object findListByState(String type, String name, Integer pageNum, Integer pageSize) {

        Page page = new Page(pageNum,pageSize);
        Map<String,Object> params = new HashMap<>();
        params.put("type",type);
        params.put("name",name);
        params.put("siteId", UserUtils.getSite());
        return dataCollectionMapper.findListByState(page,params);
    }


    public void save(DataCollection dataCollection) {
        dataCollection.setSiteId(UserUtils.getSite());
        if(dataCollection.getId() != null){
            dataCollectionMapper.updateById(dataCollection);
        }else{
            dataCollectionMapper.insert(dataCollection);
        }
    }


    public DataCollectionVo findById(String id) {
        return dataCollectionMapper.getById(id);
    }

    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            dataCollectionMapper.deleteById(id);
        });
    }


    public Object queryForLov(Map<String, Object> params) {
        Map<String,Object> map = (Map<String,Object>)params.get("page");
        params.remove("page");
        DataCollection dataCollection = JSONObject.parseObject(JSONObject.toJSONString(params), DataCollection.class);
        return findList(dataCollection.getDataCollectionTypeId(),dataCollection.getName(), Integer.valueOf(map.get("current").toString()), Integer.valueOf(map.get("size").toString()));
    }
}
