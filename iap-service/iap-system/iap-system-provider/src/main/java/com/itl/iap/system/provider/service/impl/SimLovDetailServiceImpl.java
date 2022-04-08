package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.SimLovDetailDto;
import com.itl.iap.system.api.entity.SimLov;
import com.itl.iap.system.api.entity.SimLovDetail;
import com.itl.iap.system.api.service.SimLovDetailService;
import com.itl.iap.system.provider.mapper.SimLovDetailMapper;
import com.itl.iap.system.provider.mapper.SimLovMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author EbenChan
 * @date 2020/11/21 20:33
 **/
@Service
public class SimLovDetailServiceImpl extends ServiceImpl<SimLovDetailMapper, SimLovDetail> implements SimLovDetailService {
    @Autowired
    private SimLovDetailMapper simLovDetailMapper;
    @Autowired
    private SimLovMapper simLovMapper;
    @Override
    public IPage<SimLovDetailDto> pageQuery(SimLovDetailDto simLovDetailDto) {
        if (null == simLovDetailDto.getPage()) {
            simLovDetailDto.setPage(new Page(0, 10));
        }
        return simLovDetailMapper.pageQuery(simLovDetailDto.getPage(), simLovDetailDto);
    }

    @Override
    public Boolean insertLovDetail(SimLovDetail simLovDetail) throws CommonException{
        if (simLovDetail.getLovId() == null) {
            throw new CommonException("lovId不能为空", 500);
        }
        if (simLovMapper.selectOne(new QueryWrapper<SimLov>().eq("id", simLovDetail.getLovId())) == null) {
            throw new CommonException("不存在关联的Lov", 500);
        }
        simLovDetail.setId(UUID.uuid32());
        return simLovDetailMapper.insert(simLovDetail) != 0 ? true : false;
    }

    @Override
    public Boolean updateLovDetail(SimLovDetail simLovDetail) throws CommonException {
        if (simLovDetailMapper.selectOne(new QueryWrapper<SimLovDetail>().eq("id", simLovDetail.getId())) == null) {
            throw new CommonException("不存在该Lov的字段或属性", 500);
        }
        return simLovDetailMapper.updateById(simLovDetail) != 0 ? true : false;
    }

    @Override
    public Boolean removeByIds(List<String> ids) {
        return simLovDetailMapper.deleteBatchIds(ids) != 0 ? true : false;
    }
}
