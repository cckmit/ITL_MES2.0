package com.itl.iap.system.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.SimLovDto;
import com.itl.iap.system.api.entity.SimLov;
import com.itl.iap.system.api.entity.SimLovDetail;
import com.itl.iap.system.api.service.SimLovService;
import com.itl.iap.system.provider.mapper.SimLovDetailMapper;
import com.itl.iap.system.provider.mapper.SimLovMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author EbenChan
 * @date 2020/11/21 18:15
 **/
@Service
public class SimLovServiceImpl extends ServiceImpl<SimLovMapper, SimLov> implements SimLovService {
    @Autowired
    private SimLovMapper simLovMapper;
    @Autowired
    private SimLovDetailMapper simLovDetailMapper;
    @Override
    public IPage<SimLovDto> pageQuery(SimLovDto simLovDto) {
        if (null == simLovDto.getPage()) {
            simLovDto.setPage(new Page(0, 10));
        }
        return simLovMapper.pageQuery(simLovDto.getPage(), simLovDto);
    }

    @Override
    public SimLovDto getByCode(String code) {
        return simLovMapper.getByCode(code);
    }

    @Override
    public Boolean insertLov(SimLov simLov) throws CommonException {
        if (simLovMapper.selectCount(new QueryWrapper<SimLov>().eq("code", simLov.getCode())) > 0) {
            throw new CommonException("该Lov编码已存在，请重新输入", 500);
        }
        simLov.setId(UUID.uuid32());
        return simLovMapper.insert(simLov) != 0 ? true : false;
    }

    @Override
    public Boolean updateLov(SimLov simLov) throws CommonException {
        if (simLovMapper.selectOne(new QueryWrapper<SimLov>().eq("id", simLov.getId())) == null) {
            throw new CommonException("不存在该Lov", 500);
        }
        return simLovMapper.updateById(simLov) != 0 ? true : false;
    }

    @Override
    @Transactional
    public Boolean removeByIds(List<String> ids) {
        ids.forEach( id -> {
            List<SimLovDetail> lovList = simLovDetailMapper.selectList(new QueryWrapper<SimLovDetail>().eq("lov_id", id));
            lovList.forEach(simLovDetail -> {
                simLovDetailMapper.deleteById(simLovDetail.getId());
            });
        });
        return simLovMapper.deleteBatchIds(ids) != 0 ? true : false;
    }
}
