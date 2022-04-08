package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.prove.ProveQueryDTO;
import com.itl.iap.mes.api.dto.prove.StationProveQueryDTO;
import com.itl.iap.mes.api.dto.prove.UserProveQueryDTO;
import com.itl.iap.mes.api.entity.label.LabelEntity;
import com.itl.iap.mes.api.entity.prove.ProveEntity;
import com.itl.iap.mes.api.service.ProveService;
import com.itl.iap.mes.provider.mapper.ProveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuchenghao
 * @date 2020/11/3 17:58
 */
@Service
public class ProveServiceImpl implements ProveService {

    @Autowired
    ProveMapper proveMapper;


    @Override
    public IPage<ProveEntity> findList(ProveQueryDTO proveQueryDTO) {

        if(ObjectUtil.isEmpty(proveQueryDTO.getPage())){
            proveQueryDTO.setPage(new Page(0, 10));
        }
        proveQueryDTO.getPage().setDesc("CREATION_DATE");
        QueryWrapper queryWrapper = new QueryWrapper<ProveEntity>();
        if(StrUtil.isNotEmpty(proveQueryDTO.getProveCode())){
            queryWrapper .like("prove_code",proveQueryDTO.getProveCode());
        }
        if(StrUtil.isNotEmpty(proveQueryDTO.getSite())){
            queryWrapper.eq("site",proveQueryDTO.getSite());
        }
        return proveMapper.selectPage(proveQueryDTO.getPage(),queryWrapper);
    }
    @Override
    public IPage<ProveEntity> findListByState(ProveQueryDTO proveQueryDTO) {

        if(ObjectUtil.isEmpty(proveQueryDTO.getPage())){
            proveQueryDTO.setPage(new Page(0, 10));
        }
        proveQueryDTO.getPage().setDesc("CREATION_DATE");
        QueryWrapper queryWrapper = new QueryWrapper<ProveEntity>();
        if(StrUtil.isNotEmpty(proveQueryDTO.getProveCode())){
            queryWrapper .like("prove_code",proveQueryDTO.getProveCode());
        }
        if(StrUtil.isNotEmpty(proveQueryDTO.getSite())){
            queryWrapper.eq("site",proveQueryDTO.getSite());
        }
        queryWrapper.eq("STATE", "1");
        ArrayList<Object> objects = new ArrayList<>();
        List<Object> collect = objects.stream().filter(x -> true).collect(Collectors.toList());
        return proveMapper.selectPage(proveQueryDTO.getPage(),queryWrapper);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(ProveEntity proveEntity) {

        QueryWrapper queryWrapper = new QueryWrapper<LabelEntity>();
        queryWrapper.eq("prove_code",proveEntity.getProveCode());
        Integer count = proveMapper.selectCount(queryWrapper);

        if(count==1){
            proveEntity.setLastUpdateDate(new Date());
            proveMapper.update(proveEntity,queryWrapper);
        }else{
            proveEntity.setObjectSetBasicAttribute(new Date());
            proveEntity.setSite(UserUtils.getSite());
            proveMapper.insert(proveEntity);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            proveMapper.deleteById(id);
        });
    }

    @Override
    public ProveEntity findById(String id){

        return proveMapper.selectById(id);
    }

    @Override
    public IPage<ProveEntity>  findUserProveList(UserProveQueryDTO userProveQueryDTO){

        if (null == userProveQueryDTO.getPage()) {
            userProveQueryDTO.setPage(new Page(0, 10));
        }
        return proveMapper.findUserProveList(userProveQueryDTO.getPage(), userProveQueryDTO);
    }
    @Override
    public IPage<ProveEntity>  findNotUserProveList(UserProveQueryDTO userProveQueryDTO){

        if (null == userProveQueryDTO.getPage()) {
            userProveQueryDTO.setPage(new Page(0, 10));
        }
        return proveMapper.findNotUserProveList(userProveQueryDTO.getPage(),userProveQueryDTO);
    }
    @Override
    public IPage<ProveEntity>  findStationProveList(StationProveQueryDTO stationProveQueryDTO){

        if (null == stationProveQueryDTO.getPage()) {
            stationProveQueryDTO.setPage(new Page(0, 10));
        }
        return proveMapper.findStationProveList(stationProveQueryDTO.getPage(),stationProveQueryDTO);
    }
    @Override
    public IPage<ProveEntity>  findNotStationProveList(StationProveQueryDTO stationProveQueryDTO){

        if (null == stationProveQueryDTO.getPage()) {
            stationProveQueryDTO.setPage(new Page(0, 10));
        }
        return proveMapper.findNotStationProveList(stationProveQueryDTO.getPage(),stationProveQueryDTO);
    }
}
