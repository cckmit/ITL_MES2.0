package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.provider.mapper.CheckExecuteItemMapper;
import com.itl.iap.mes.provider.mapper.CheckExecuteMapper;
import com.itl.iap.mes.provider.mapper.DataCollectionItemListingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class CheckExecuteServiceImpl {

    @Autowired
    private CheckExecuteMapper checkExecuteMapper;

    @Autowired
    private CheckExecuteItemMapper checkExecuteItemMapper;


    public Object findList(CheckExecute checkExecute, Integer pageNum, Integer pageSize) {
        checkExecute.setSiteId(UserUtils.getSite());
        Page page = new Page(pageNum, pageSize);
        return checkExecuteMapper.pageQuery(page, checkExecute);
    }

    @Autowired
    DataCollectionItemServiceImpl dataCollectionItemService;
    @Autowired
    DataCollectionItemListingMapper dataCollectionItemListingMapper;

    public CheckExecute findById(String id) {
        CheckExecute checkExecute = checkExecuteMapper.selectById(id);
        List<CheckExecuteItem> select = checkExecuteItemMapper.selectList(new QueryWrapper<CheckExecuteItem>().eq("checkExecuteId", id));

        if(select != null && !select.isEmpty() ){
            for (CheckExecuteItem checkExecuteItem : select) {
                if (checkExecuteItem.getType()==3){
                    IPage<DataCollectionItem> list = dataCollectionItemService.findList(checkExecute.getDataCollectionId(), 1, 1000);
                    List<DataCollectionItem> records = list.getRecords();
                    if(records != null && !records.isEmpty()){
                        records.forEach(dataCollectionItem -> {
                            CheckExecuteItem executeItem = new CheckExecuteItem();
                            executeItem.setItemCode(dataCollectionItem.getItemNo());
                            executeItem.setItemName(dataCollectionItem.getItemName());
                            executeItem.setType(dataCollectionItem.getItemType());
                            executeItem.setMaxNum(dataCollectionItem.getMaxNum());
                            executeItem.setMinNum(dataCollectionItem.getMinNum());
                            QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("dataCollectionItemId",dataCollectionItem.getId());
                            List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
                            checkExecuteItem.setDataCollectionItemListingList(dataCollectionItemListingList);
                        });
                    }
                }
            }
            checkExecute.setCheckExecuteItemList(select);
        }else{
            List<CheckExecuteItem> checkExecuteItems = new ArrayList<>();
            IPage<DataCollectionItem> list = dataCollectionItemService.findList(checkExecute.getDataCollectionId(), 1, 1000);
            List<DataCollectionItem> records = list.getRecords();
            if(records != null && !records.isEmpty()){
                records.forEach(dataCollectionItem -> {
                    CheckExecuteItem checkExecuteItem = new CheckExecuteItem();
                    checkExecuteItem.setItemCode(dataCollectionItem.getItemNo());
                    checkExecuteItem.setItemName(dataCollectionItem.getItemName());
                    checkExecuteItem.setType(dataCollectionItem.getItemType());
                    checkExecuteItem.setMaxNum(dataCollectionItem.getMaxNum());
                    checkExecuteItem.setMinNum(dataCollectionItem.getMinNum());
                    QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("dataCollectionItemId",dataCollectionItem.getId());
                    List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
                    checkExecuteItem.setDataCollectionItemListingList(dataCollectionItemListingList);
                    checkExecuteItems.add(checkExecuteItem);
                });
                checkExecute.setCheckExecuteItemList(checkExecuteItems);
            }
        }
        return checkExecute;
    }


    @Transactional
    public void save(CheckExecute checkExecute) {
        checkExecute.setSiteId(UserUtils.getSite());
        List<CheckExecuteItem> checkExecuteItemList = checkExecute.getCheckExecuteItemList();
        if(checkExecute.getId() != null){
            checkExecuteMapper.updateById(checkExecute);
        }else{
            checkExecuteMapper.insert(checkExecute);
        }
        QueryWrapper<CheckExecuteItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("checkExecuteId",checkExecute.getId());
        checkExecuteItemMapper.delete(queryWrapper);
        if(checkExecuteItemList != null && !checkExecuteItemList.isEmpty()){
            checkExecuteItemList.forEach(checkExecuteItem -> {
                checkExecuteItem.setId(null);
                checkExecuteItem.setCheckExecuteId(checkExecute.getId());
                saveItem(checkExecuteItem);
            });
        }
    }

    public void saveItem(CheckExecuteItem checkExecuteItem) {
        if (checkExecuteItem.getItemValue()!=null&&checkExecuteItem.getItemValue().equals("NG")){
            checkExecuteItem.setItemValue("未通过");
        }
        else if (checkExecuteItem.getItemValue()!=null&&checkExecuteItem.getItemValue().equals("OK")){
            checkExecuteItem.setItemValue("通过");
        }
        if(checkExecuteItem.getId() != null){
            checkExecuteItemMapper.updateById(checkExecuteItem);
        }else{
            checkExecuteItemMapper.insert(checkExecuteItem);
        }
    }
}
