package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctc.wstx.util.StringUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.andon.api.bo.DetailHandleBO;
import com.itl.mes.andon.api.dto.AndonDetailDTO;
import com.itl.mes.andon.api.entity.Detail;
import com.itl.mes.andon.api.service.DetailService;
import com.itl.mes.andon.api.vo.DetailVo;
import com.itl.mes.andon.provider.mapper.DetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service("DetailService")
@Transactional
public class DetailServiceImpl extends ServiceImpl<DetailMapper, Detail> implements DetailService {

    @Autowired
    private DetailMapper detailMapper;

    @Autowired
    private UserUtil userUtil;

    @Override
    public Detail saveInUpdate(DetailVo detailVO) {
        Detail detail = new Detail();
        BeanUtils.copyProperties(detailVO,detail);
        Date date = new Date();
        if (detailVO.getId() == null){
            detail.setAndonDescBo(new DetailHandleBO(UserUtils.getSite(), detail.getAndonDesc()).getBo());
            detail.setId(UUID.uuid32());
            detail.setCreateUser(userUtil.getUser().getRealName());
            detailMapper.insert(detail);
            return detailMapper.selectOne(new QueryWrapper<Detail>().eq("andon_desc_bo",detail.getAndonDescBo()));
        }
        detail.setModifyUser(userUtil.getUser().getRealName());
        detail.setModifyDate(date);
        detailMapper.update(detail,new QueryWrapper<Detail>().eq("id",detailVO.getId()));
        return detailMapper.selectOne(new QueryWrapper<Detail>().eq("id",detail.getId()));
    }

    @Override
    public IPage<Detail> queryAndonDetail(AndonDetailDTO andonDetailDTO) {
        if(ObjectUtil.isEmpty(andonDetailDTO.getPage())){
            andonDetailDTO.setPage(new Page(0, 10));
        }
        QueryWrapper<Detail> queryWrapper = new QueryWrapper<Detail>();
        if (StrUtil.isNotEmpty(andonDetailDTO.getAndonDesc())){
            queryWrapper.like("andon_desc",andonDetailDTO.getAndonDesc());
        }
        if (StrUtil.isNotEmpty(andonDetailDTO.getAndonName())){
            queryWrapper.like("andon_name",andonDetailDTO.getAndonName());
        }
        return detailMapper.selectPage(andonDetailDTO.getPage(),queryWrapper);
    }
}
