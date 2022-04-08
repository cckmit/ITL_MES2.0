package com.itl.mes.pp.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.pp.api.dto.schedule.*;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleProductionLineEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleReceiveEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleShopOrderEntity;
import com.itl.mes.pp.api.service.ResourcesCalendarService;
import com.itl.mes.pp.api.service.ScheduleService;
import com.itl.mes.pp.provider.config.Constant;
import com.itl.mes.pp.provider.mapper.ScheduleMapper;
import com.itl.mes.pp.provider.mapper.ScheduleProductionLineMapper;
import com.itl.mes.pp.provider.mapper.ScheduleReceiveMapper;
import com.itl.mes.pp.provider.mapper.ScheduleShopOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author liuchenghao
 * @date 2020/11/12 9:55
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {


    @Autowired
    ScheduleMapper scheduleMapper;


    @Autowired
    ScheduleProductionLineMapper scheduleProductionLineMapper;


    @Autowired
    ScheduleShopOrderMapper scheduleShopOrderMapper;

    @Autowired
    ScheduleReceiveMapper scheduleReceiveMapper;

    @Autowired
    ResourcesCalendarService resourcesCalendarService;



    @Override
    public IPage<ScheduleRespDTO> findList(ScheduleQueryDTO scheduleQueryDTO) {

        if(ObjectUtil.isEmpty(scheduleQueryDTO.getPage())){
            scheduleQueryDTO.setPage(new Page(0, 10));
        }
        scheduleQueryDTO.getPage().setDesc("CREATE_DATE");
        scheduleQueryDTO.setSite(UserUtils.getSite());
        return scheduleMapper.findList(scheduleQueryDTO.getPage(),scheduleQueryDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(ScheduleSaveDTO scheduleSaveDTO) throws CommonException {

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        BeanUtil.copyProperties(scheduleSaveDTO,scheduleEntity);

        if(StrUtil.isNotEmpty(scheduleSaveDTO.getBo())){

            if(Constant.ScheduleState.RECEIVE.getValue() == scheduleSaveDTO.getState()){
                throw new CommonException("接收状态不允许修改",CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            scheduleEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
            scheduleEntity.setModifyDate(new Date());
            scheduleMapper.updateById(scheduleEntity);
            QueryWrapper<ScheduleProductionLineEntity> productionLineEntityQueryWrapper = new QueryWrapper();
            productionLineEntityQueryWrapper.eq("schedule_bo",scheduleEntity.getBo());
            scheduleProductionLineMapper.delete(productionLineEntityQueryWrapper);
            scheduleSaveDTO.getScheduleProductionLineSaveDTOList().forEach(scheduleProductionLineSaveDTO -> {
                ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
                BeanUtil.copyProperties(scheduleProductionLineSaveDTO, scheduleProductionLineEntity);
                scheduleProductionLineEntity.setBo(null);
                scheduleProductionLineEntity.setScheduleBo(scheduleSaveDTO.getBo());
                scheduleProductionLineMapper.insert(scheduleProductionLineEntity);
            });
            QueryWrapper<ScheduleShopOrderEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("schedule_bo", scheduleSaveDTO.getBo());
            scheduleShopOrderMapper.delete(queryWrapper);
            scheduleSaveDTO.getShopOrders().forEach(shopOrder -> {
                ScheduleShopOrderEntity scheduleShopOrderEntity = new ScheduleShopOrderEntity();
                scheduleShopOrderEntity.setScheduleBo(scheduleSaveDTO.getBo());
                scheduleShopOrderEntity.setShopOrderBo(shopOrder);
                scheduleShopOrderMapper.insert(scheduleShopOrderEntity);
            });

        }else{
            scheduleEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
            scheduleEntity.setState(Constant.ScheduleState.CREATE.getValue());
            scheduleMapper.insert(scheduleEntity);
            scheduleSaveDTO.getScheduleProductionLineSaveDTOList().forEach(scheduleProductionLineSaveDTO -> {
                ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
                BeanUtil.copyProperties(scheduleProductionLineSaveDTO,scheduleProductionLineEntity);
                scheduleProductionLineEntity.setScheduleBo(scheduleEntity.getBo());
                scheduleProductionLineMapper.insert(scheduleProductionLineEntity);
            });
            scheduleSaveDTO.getShopOrders().forEach(shopOrder -> {
                ScheduleShopOrderEntity scheduleShopOrderEntity = new ScheduleShopOrderEntity();
                scheduleShopOrderEntity.setScheduleBo(scheduleEntity.getBo());
                scheduleShopOrderEntity.setShopOrderBo(shopOrder);
                scheduleShopOrderMapper.insert(scheduleShopOrderEntity);
            });
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<String> ids) throws CommonException {

        for(String id:ids) {
            QueryWrapper<ScheduleEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("state", 1);
            queryWrapper.eq("bo",id);
            Integer count = scheduleMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new CommonException("有排程数据已被下达，无法删除，请检查数据", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            QueryWrapper<ScheduleProductionLineEntity> deleteProductionLineWrapper = new QueryWrapper<>();
            deleteProductionLineWrapper.eq("bo",id);
            QueryWrapper<ScheduleShopOrderEntity> deleteShopOrderWrapper = new QueryWrapper<>();
            deleteShopOrderWrapper.eq("bo",id);
            scheduleMapper.deleteById(id);
            scheduleProductionLineMapper.delete(deleteProductionLineWrapper);
            scheduleShopOrderMapper.delete(deleteShopOrderWrapper);
        }
    }

    @Override
    public ScheduleDetailRespDTO findById(String id){

        ScheduleDetailRespDTO scheduleRespDTO = scheduleMapper.findById(id);
        List<String> shopOrderBos = new ArrayList<>();
        shopOrderBos.add(scheduleRespDTO.getShopOrderBo());
        scheduleRespDTO.setShopOrderBos(shopOrderBos);
        return scheduleRespDTO;
    }

    @Override
    public ScheduleDetailRespDTO findByIdWithCount(String id){
        ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(UserUtils.getSite(), UserUtils.getProductLine());
        ScheduleDetailRespDTO scheduleRespDTO = scheduleMapper.findByIdWithCount(id, productLineHandleBO.getBo());
        if (ObjectUtil.isNotNull(scheduleRespDTO)) {
            List<String> shopOrderBos = new ArrayList<>();
            shopOrderBos.add(scheduleRespDTO.getShopOrderBo());
            scheduleRespDTO.setShopOrderBos(shopOrderBos);
        }
        return scheduleRespDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateState(String bo) {

       //排程下达，先更新排程表状态，再将排程表和排程工单关联表和排程产线关联表的数据保存排程派工表
       ScheduleEntity updateEntity = new ScheduleEntity();
       updateEntity.setState(Constant.ScheduleState.RELEASE.getValue());
//       updateEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
       updateEntity.setModifyDate(new Date());
       updateEntity.setBo(bo);
       scheduleMapper.updateById(updateEntity);
       saveScheduleReceive(bo);

    }

    @Override
    public void batchRelease(List<String> bos) {
        bos.forEach(bo ->{
            ScheduleEntity scheduleEntity = new ScheduleEntity();
            scheduleEntity.setState(Constant.ScheduleState.RELEASE.getValue());
            scheduleEntity.setBo(bo);
            scheduleMapper.updateById(scheduleEntity);
            saveScheduleReceive(bo);
        });
    }

    @Override
    public IPage<ProductionLineResDTO> getInitScheduleProductionLineList(ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO) {

        if(ObjectUtil.isEmpty(scheduleProductionLineQueryDTO.getPage())){
            scheduleProductionLineQueryDTO.setPage(new Page(0, 10));
        }

        return scheduleMapper.findProductionLine(scheduleProductionLineQueryDTO.getPage(),scheduleProductionLineQueryDTO.getWorkShopBo());
    }

    @Override
    public List<StationResDTO> getStationList(String productionLineBo) {
        return scheduleMapper.findProductionLineStation(productionLineBo);
    }

    @Override
    public IPage<ScheduleProductionLineRespDTO> getScheduleProductionLine(ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO) {

        if(ObjectUtil.isEmpty(scheduleProductionLineQueryDTO.getPage())){
            scheduleProductionLineQueryDTO.setPage(new Page(0, 10));
        }

        return scheduleProductionLineMapper.findList(scheduleProductionLineQueryDTO.getPage(),scheduleProductionLineQueryDTO);
    }

    @Override
    public IPage<Map<String, Object>> getByProductLine(ScheduleShowDto scheduleShowDto) {
        scheduleShowDto.setPage(
                Optional.ofNullable(scheduleShowDto.getPage())
                .orElse(new Page(0, 10))
        );
        scheduleShowDto.setSite(UserUtils.getSite());
        return scheduleMapper.getByProductLine(scheduleShowDto.getPage() , scheduleShowDto);
    }


    private void saveScheduleReceive(String scheduleBo){
        //获取排程数据
        ScheduleEntity scheduleEntity = scheduleMapper.selectById(scheduleBo);
        //获取排程产线表的数据
        QueryWrapper<ScheduleProductionLineEntity> scheduleProductionLineEntityQueryWrapper = new QueryWrapper<>();
        scheduleProductionLineEntityQueryWrapper.eq("schedule_bo",scheduleBo);
        List<ScheduleProductionLineEntity> scheduleProductionLineEntities = scheduleProductionLineMapper.selectList(scheduleProductionLineEntityQueryWrapper);
        //获取排程工单数据,目前版本只是一个工单对应一个排程，后面如果有拓展需修改成一次性查询所有数据的sql,以优化性能
        QueryWrapper<ScheduleShopOrderEntity> scheduleShopOrderEntityQueryWrapper = new QueryWrapper<>();
        scheduleShopOrderEntityQueryWrapper.eq("schedule_bo",scheduleBo);
        List<ScheduleShopOrderEntity> scheduleShopOrderEntities = scheduleShopOrderMapper.selectList(scheduleShopOrderEntityQueryWrapper);

        //定义产线的开始时间和结束时间的map集合，用于计算机台产能的已排时间
        List<Map<String,Object>> mapList = new ArrayList<>();
        scheduleProductionLineEntities.forEach(scheduleProductionLineEntity -> {
            ScheduleReceiveEntity scheduleReceiveEntity = new ScheduleReceiveEntity();
            scheduleReceiveEntity.setShopOrderBo(scheduleShopOrderEntities.get(0).getShopOrderBo());
            scheduleReceiveEntity.setScheduleBo(scheduleEntity.getBo());
            scheduleReceiveEntity.setScheduleQty(scheduleProductionLineEntity.getQuantity());
            scheduleReceiveEntity.setState(Constant.ScheduleReceiveState.CREATE.getValue());
            scheduleReceiveEntity.setProductionLineBo(scheduleProductionLineEntity.getProductionLineBo());
            scheduleReceiveEntity.setCreateDate(new Date());
//            scheduleReceiveEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
            if(StrUtil.isNotBlank(scheduleProductionLineEntity.getStationBo())){
                scheduleReceiveEntity.setStationBo(scheduleProductionLineEntity.getStationBo());
            }
            scheduleReceiveEntity.setWorkShopBo(scheduleEntity.getWorkshopBo());
            scheduleReceiveMapper.insert(scheduleReceiveEntity);

            //定义产线的开始时间和结束时间的map
            Map<String,Object> map = new HashMap<>();
            map.put("productLineBo",scheduleProductionLineEntity.getProductionLineBo());
            map.put("startDate",scheduleProductionLineEntity.getStartDate());
            map.put("endDate",scheduleProductionLineEntity.getEndDate());
            mapList.add(map);
        });

        resourcesCalendarService.occupyResourcesCalendar(mapList);

    }
}
