package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.DataCollectionAdjoin;
import com.itl.iap.mes.api.entity.DataCollectionItem;
import com.itl.iap.mes.api.entity.DataCollectionItemListing;
import com.itl.iap.mes.provider.mapper.DataCollectionAdjoinMapper;
import com.itl.iap.mes.provider.mapper.DataCollectionItemListingMapper;
import com.itl.iap.mes.provider.mapper.DataCollectionItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DataCollectionAdjoinServiceImpl {

    @Autowired
    private DataCollectionAdjoinMapper dataCollectionAdjoinMapper;

    public IPage<DataCollectionAdjoin> findList(String dataCollectionId, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum,pageSize);
        return dataCollectionAdjoinMapper.findList(page, dataCollectionId);
    }

    public DataCollectionAdjoin findById(String id) {
        return dataCollectionAdjoinMapper.selectById(id);
    }

    public void save(DataCollectionAdjoin dataCollectionAdjoin) {
        if(StringUtils.isNotBlank(dataCollectionAdjoin.getId())){
            dataCollectionAdjoinMapper.updateById(dataCollectionAdjoin);
        }else{
            dataCollectionAdjoinMapper.insert(dataCollectionAdjoin);
        }
    }

    public void delete(List<String> ids) {
        ids.forEach(id ->{
            dataCollectionAdjoinMapper.deleteById(id);
        });
    }
}
