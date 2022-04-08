package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.dto.MouldDto;
import com.itl.mes.core.api.entity.Mould;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.service.MouldService;
import com.itl.mes.core.api.vo.MouldVo;
import com.itl.mes.core.provider.mapper.MouldMapper;
import com.itl.mes.core.provider.mapper.WorkShopMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MouldServiceImpl extends ServiceImpl<MouldMapper, Mould> implements MouldService {

    @Autowired
    private MouldMapper mouldMapper;

    @Autowired
    private WorkShopMapper workShopMapper;

    @Override
    public void insert(Mould mould) {
        String uuid= UUID.uuid32();
        mould.setBo(uuid);
        mould.setState("0");
        // 根据车间编码 查询车间bo
        String workShopBo= workShopMapper.selectOne(new QueryWrapper<WorkShop>().eq("WORK_SHOP",mould.getWorkShop())).getBo();
        mould.setWorkShopBo(workShopBo);
        mouldMapper.insert(mould);
    }

    @Override
    public Mould getById(String bo) {
        Mould mould=mouldMapper.selectByBo(bo);
        return mould;
    }

    @Override
    public Mould returnMould(Mould mould) {
        //归还模具，将模具状态改为1
        mould.setState("1");
        mouldMapper.update(mould,new QueryWrapper<Mould>().eq("BO",mould.getBo()));
        return mouldMapper.selectById(mould.getBo());
    }

    @Override
    public void batchDelete(List<String> bos) throws CommonException {
        if(bos !=null && bos.size()>0){
            mouldMapper.deleteBatchIds(bos);
        }else {
            throw new CommonException("请选择要删除的模具", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    public IPage<Mould> queryPage(MouldDto mouldDto) {
        return mouldMapper.queryPage(mouldDto.getPage(),mouldDto);
    }
}
