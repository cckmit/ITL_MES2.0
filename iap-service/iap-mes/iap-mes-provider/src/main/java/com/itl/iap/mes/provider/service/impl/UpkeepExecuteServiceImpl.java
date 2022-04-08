package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.mes.api.dto.DeviceCheckDto;
import com.itl.iap.mes.api.dto.UpkeepTableDto;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.vo.CheckPlanVo;
import com.itl.iap.mes.provider.annotation.EnablePaging;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class UpkeepExecuteServiceImpl {

    @Autowired
    private UpkeepExecuteMapper upkeepExecuteMapper;

    @Autowired
    private UpkeepPlanMapper upkeepPlanMapper;

    @Autowired
    private UpkeepTableMapper upkeepTableMapper;

    @Autowired
    private UpkeepTableItemMapper upkeepTableItemMapper;

    @Autowired
    private UpkeepExecuteItemMapper upkeepExecuteItemMapper;

    @Autowired
    private DataCollectionItemMapper dataCollectionItemMapper;

    @Autowired
    private UserUtil userUtil;

    public Object findList(UpkeepExecute upkeepExecute, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String time=null;
        if(upkeepExecute.getPlanExecuteTime() !=null){
            time=sdf.format(upkeepExecute.getPlanExecuteTime());
        }
        return upkeepExecuteMapper.findList(page, upkeepExecute,time);
    }

    @Autowired
    DataCollectionItemServiceImpl dataCollectionItemService;
    @Autowired
    DataCollectionItemListingMapper dataCollectionItemListingMapper;
    public UpkeepExecute findById(String id) {
        UpkeepExecute upkeepExecute = upkeepExecuteMapper.selectById(id);
        List<UpkeepExecuteItem> select = upkeepExecuteItemMapper.selectList(new QueryWrapper<UpkeepExecuteItem>().eq("upkeepExecuteId", id));
        List<UpkeepExecuteItem> upkeepExecuteItems = new ArrayList<>();
        IPage<DataCollectionItem> list = dataCollectionItemService.findList(upkeepExecute.getDataCollectionId(), 1, 1000);
        List<DataCollectionItem> records = list.getRecords();
        if(records != null && !records.isEmpty()){
            records.forEach(dataCollectionItem -> {
                UpkeepExecuteItem upkeepExecuteItem = new UpkeepExecuteItem();
                upkeepExecuteItem.setItemCode(dataCollectionItem.getItemNo());
                upkeepExecuteItem.setItemName(dataCollectionItem.getItemName());
                upkeepExecuteItem.setType(dataCollectionItem.getItemType());
                upkeepExecuteItem.setMaxNum(dataCollectionItem.getMaxNum());
                upkeepExecuteItem.setMinNum(dataCollectionItem.getMinNum());
                QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("dataCollectionItemId",dataCollectionItem.getId());
                List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
                upkeepExecuteItem.setDataCollectionItemListingList(dataCollectionItemListingList);
                upkeepExecuteItems.add(upkeepExecuteItem);
            });
        }
        if(select != null && !select.isEmpty() ){
            upkeepExecuteItems.forEach(item->{
                List<UpkeepExecuteItem> collect = select.stream().filter(e -> e.getItemCode() != null && e.getItemCode().equals(item.getItemCode()) && e.getItemName() != null && e.getItemName().equals(item.getItemName())).collect(Collectors.toList());
                if(collect != null && !collect.isEmpty()){
                    UpkeepExecuteItem upkeepExecuteItem = collect.get(0);
                    if(upkeepExecuteItem != null){
                        item.setItemValue(upkeepExecuteItem.getItemValue());
                    }
                }


            });

        }
        upkeepExecute.setUpkeepExecuteItemList(upkeepExecuteItems);
        return upkeepExecute;
    }


    @Transactional
    public void save(UpkeepExecute upkeepExecute) {
        List<UpkeepExecuteItem> upkeepExecuteItemList = upkeepExecute.getUpkeepExecuteItemList();
        if(upkeepExecute.getId() != null){
            upkeepExecuteMapper.updateById(upkeepExecute);
        }else{
            upkeepExecuteMapper.insert(upkeepExecute);
        }
        QueryWrapper<UpkeepExecuteItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("upkeepExecuteId",upkeepExecute.getId());
        upkeepExecuteItemMapper.delete(queryWrapper);
        if(upkeepExecuteItemList != null && !upkeepExecuteItemList.isEmpty()){
            upkeepExecuteItemList.forEach(upkeepExecuteItem -> {
                upkeepExecuteItem.setId(null);
                upkeepExecuteItem.setUpkeepExecuteId(upkeepExecute.getId());
                saveItem(upkeepExecuteItem);
            });
        }
    }

    public void saveItem(UpkeepExecuteItem upkeepExecuteItem) {
        if(upkeepExecuteItem.getId() != null){
            upkeepExecuteItemMapper.updateById(upkeepExecuteItem);
        }else{
            upkeepExecuteItemMapper.insert(upkeepExecuteItem);
        }
    }

    public UpkeepTable queryById(){

        UpkeepTable upkeepTable=new UpkeepTable();
        String repairId=createCode();
        upkeepTable.setRepairId(repairId);
        upkeepTable.setRepairTime(new Date());
        upkeepTable.setRepairRealName(userUtil.getUser().getRealName());
        return upkeepTable;

    }

    public void saveRepairTable(UpkeepTable upkeepTable){
        String id= UUID.randomUUID().toString();
        String userName=userUtil.getUser().getUserName();//当前登录账号
        String realName=userUtil.getUser().getRealName();//当前登录用户名
        upkeepTable.setId(id);
        upkeepTable.setRepairUserName(userName);
        upkeepTable.setRepairRealName(realName);
        UpkeepTable upkeepTable1=upkeepTableMapper.selectOne(new QueryWrapper<UpkeepTable>().eq("repairId",upkeepTable.getRepairId()));
        if(upkeepTable1 ==null){
            if (StrUtil.isEmpty(upkeepTable.getRepairId())){
                upkeepTable.setRepairId(createCode());
                upkeepTable.setRepairTime(new Date());
            }
            upkeepTableMapper.insert(upkeepTable);
            //向保养计划表中新增保养单编号
            //判断是否是提交状态
            /*if(upkeepTable.getState().equals("1")){
                UpkeepExecute upkeepExecute=upkeepExecuteMapper.selectById(upkeepTable.getExecuteId());
                UpkeepPlan upkeepPlan=upkeepPlanMapper.selectById(upkeepExecute.getUpkeepPlanId());
                upkeepPlan.setRepairId(upkeepTable.getRepairId());
                upkeepPlanMapper.updateById(upkeepPlan);
            }*/


        }else{
            upkeepTableMapper.update(upkeepTable,new QueryWrapper<UpkeepTable>().eq("repairId",upkeepTable.getRepairId()));
            //向保养计划表中新增保养单编号
            //判断是否是提交状态
            /*if(upkeepTable.getState().equals("1")){
                UpkeepExecute upkeepExecute=upkeepExecuteMapper.selectById(upkeepTable.getExecuteId());
                UpkeepPlan upkeepPlan=upkeepPlanMapper.selectById(upkeepExecute.getUpkeepPlanId());
                upkeepPlan.setRepairId(upkeepTable.getRepairId());
                upkeepPlanMapper.updateById(upkeepPlan);
            }*/

        }
        //保存保养单明细表信息
        List<DataCollectionItem> dataCollectionItems=upkeepTable.getDataCollectionItems();
        //删除保养单明细表中repairId对应的保养项目
        upkeepTableItemMapper.delete(new QueryWrapper<UpkeepTableItem>().eq("repairId",upkeepTable.getRepairId()));
        for(DataCollectionItem dataCollectionItem:dataCollectionItems){
            UpkeepTableItem upkeepTableItem=new UpkeepTableItem();
            String uid= UUID.randomUUID().toString();
            upkeepTableItem.setId(uid);
            upkeepTableItem.setRepairId(upkeepTable.getRepairId());
            upkeepTableItem.setItemName(dataCollectionItem.getItemName());
            upkeepTableItem.setRemark(dataCollectionItem.getRemark());
            upkeepTableItem.setOperation(dataCollectionItem.getOperation());
            upkeepTableItem.setComments(dataCollectionItem.getComments());
            upkeepTableItemMapper.insert(upkeepTableItem);
        }
    }

    public IPage<UpkeepTable> selectRepairTable(UpkeepTableDto upkeepTableDto){

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ksTime=null;
        String jsTime=null;
        if((upkeepTableDto.getStartDate() !=null ) && (upkeepTableDto.getEndDate()==null)){
            ksTime=simpleDateFormat.format(upkeepTableDto.getStartDate());
        }else if((upkeepTableDto.getStartDate() !=null ) && (upkeepTableDto.getEndDate() !=null)){
            ksTime=sdf.format(upkeepTableDto.getStartDate());
            jsTime=sdf.format(upkeepTableDto.getEndDate());
        }
        String deviceName=upkeepTableDto.getDeviceName();
        String repairRealName=upkeepTableDto.getRepairRealName();
        String deviceCode = upkeepTableDto.getDeviceCode();
        return  upkeepTableMapper.selectRepairTablePage(upkeepTableDto.getPage(),deviceName,repairRealName,ksTime,jsTime,deviceCode);

    }

    public UpkeepTable queryByRepairId(String repairId){
        //UpkeepTable upkeepTable=upkeepTableMapper.selectOne(new QueryWrapper<UpkeepTable>().eq("repairId",repairId));
        UpkeepTable upkeepTable=upkeepTableMapper.selectByRepairId(repairId);
        List<UpkeepTableItem> upkeepTableItems=upkeepTableItemMapper.selectList(new QueryWrapper<UpkeepTableItem>().eq("repairId",repairId));
        upkeepTable.setUpkeepTableItems(upkeepTableItems);
        return upkeepTable;
    }

    //生成点检流水单编号
    public String createCode(){
        String code=upkeepTableMapper.selectMaxCode();
        String resultCode=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String pfix= sdf.format(new Date());
        if(code !=null && code.contains(pfix)){
            String end=code.substring(15,19);
            int endNum= Integer.parseInt(end);
            String format = String.format("%04d", endNum+1);
            resultCode= "DYMEDY-"+pfix+format;
        }else{
            resultCode="DYMEDY-"+pfix+"0001";
        }

        return  resultCode;
    }

    @Transactional
    public void delete(List<String> ids) throws CommonException {
        if(ids !=null){
            upkeepExecuteMapper.deleteBatchIds(ids);
        }else{
            throw new CustomException(CommonCode.CHOICE_DELETE);
        }

    }

    @Transactional
    public void deleteByRepairId(List<String> repairIds) throws CommonException {
        if(repairIds !=null){
            repairIds.forEach(repairId ->{
                upkeepTableMapper.delete(new QueryWrapper<UpkeepTable>().eq("repairId",repairId));
                upkeepTableItemMapper.delete(new QueryWrapper<UpkeepTableItem>().eq("repairId",repairId));
            });
        }else{
            throw new CustomException(CommonCode.CHOICE_DELETE);
        }

    }
    public UpkeepTable queryByDeviceType(String deviceType,String upkeepPlanName) throws CommonException {
        UpkeepTable upkeepTable=new UpkeepTable();

        UpkeepPlan upkeepPlan=new UpkeepPlan();
        //判断是否存在多个类型
        if(deviceType.contains(",")){
            //根据逗号分割设备类型
            String[] deviceTypes=deviceType.split(",");
            //根据设备类型找到对应的收集组信息
            for(int i=0;i<deviceTypes.length;i++) {
                upkeepPlan = upkeepPlanMapper.selectByDeviceType(deviceTypes[i], upkeepPlanName);
                if(upkeepPlan !=null && StrUtil.isNotEmpty(upkeepPlan.getId())){
                    break;
                }
            }
        }else{
            upkeepPlan = upkeepPlanMapper.selectByDeviceType(deviceType, upkeepPlanName);
        }


        if(upkeepPlan == null){
            throw new CustomException(CommonCode.UPKEEP_CREATE);
        }
        if(upkeepPlan.getState() == 0){
            throw new CustomException(CommonCode.UPKEEP_STATE);
        }
        upkeepTable.setUpkeepPlanName(upkeepPlanName);
        upkeepTable.setDeviceType(upkeepPlan.getType());
        upkeepTable.setDeviceTypeName(upkeepPlan.getDeviceTypeName());
        upkeepTable.setDataCollectionName(upkeepPlan.getDataCollectionName());
        //根据设备收集组id查询对应的点检项
        List<DataCollectionItem>  dataCollectionItems=dataCollectionItemMapper.selectList(new QueryWrapper<DataCollectionItem>().eq("dataCollectionId",upkeepPlan.getDataCollectionId()));
        upkeepTable.setDataCollectionItems(dataCollectionItems);
        return upkeepTable;
    }
}
