package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.DataCollectionItem;
import com.itl.iap.mes.api.entity.DataCollectionItemListing;
import com.itl.iap.mes.api.entity.Fault;
import com.itl.iap.mes.provider.mapper.DataCollectionItemListingMapper;
import com.itl.iap.mes.provider.mapper.DataCollectionItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DataCollectionItemServiceImpl {

    @Autowired
    private DataCollectionItemMapper dataCollectionItemMapper;

    @Autowired
    private DataCollectionItemListingMapper dataCollectionItemListingMapper;

    public IPage<DataCollectionItem> findList(String dataCollectionId, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum,pageSize);
        return dataCollectionItemMapper.pageQuery(page, dataCollectionId);
    }


    public DataCollectionItem findById(String id) {
        DataCollectionItem dataCollectionItem = dataCollectionItemMapper.selectById(id);
        QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataCollectionItemId",dataCollectionItem.getId());
        List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
        List<DataCollectionItemListing> list = dataCollectionItemListingList.stream().sorted(Comparator.comparing(DataCollectionItemListing::getSerial)).collect(Collectors.toList());
        dataCollectionItem.setDataCollectionItemListingList(list);
        return dataCollectionItem;
    }

    @Transactional
    public void save(DataCollectionItem dataCollectionItem) {
        if(StringUtils.isNotBlank(dataCollectionItem.getId())){
            dataCollectionItemMapper.updateById(dataCollectionItem);
        }else{
            dataCollectionItemMapper.insert(dataCollectionItem);
        }
        saveItemListing(dataCollectionItem);
    }

    private void saveItemListing(DataCollectionItem dataCollectionItem) {
        List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItem.getDataCollectionItemListingList();
        QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dataCollectionItemId",dataCollectionItem.getId());
        dataCollectionItemListingMapper.delete(queryWrapper);
        if(dataCollectionItemListingList != null && !dataCollectionItemListingList.isEmpty()){
            dataCollectionItemListingList.forEach(dataCollectionItemListing -> {
                dataCollectionItemListing.setId(null);
                dataCollectionItemListing.setDataCollectionItemId(dataCollectionItem.getId());
                dataCollectionItemListingMapper.insert(dataCollectionItemListing);
            });
        }
    }

    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dataCollectionItemId",id);
            List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
            if(dataCollectionItemListingList != null && !dataCollectionItemListingList.isEmpty()){
                dataCollectionItemListingList.forEach(dataCollectionItemListing -> {
                    dataCollectionItemListingMapper.deleteById(dataCollectionItemListing.getId());
                });
            }
            dataCollectionItemMapper.deleteById(id);
        });
    }
}
