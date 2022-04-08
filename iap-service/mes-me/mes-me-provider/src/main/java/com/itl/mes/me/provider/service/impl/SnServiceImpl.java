package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.me.api.dto.SnDto;
import com.itl.mes.me.api.entity.Sn;
import com.itl.mes.me.api.service.SnService;
import com.itl.mes.me.provider.mapper.SnMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service("zSnService")
public class SnServiceImpl extends ServiceImpl<SnMapper, Sn> implements SnService {

    @Autowired
    private SnMapper snMapper;
    @Autowired
    private UserUtil userUtil;

    @Override
    public IPage<Sn> getAll(SnDto snDto) {
        if (ObjectUtil.isEmpty(snDto.getPage())) {
            snDto.setPage(new Page(0, 10));
        }
        IPage<Sn> all = snMapper.getAll(snDto.getPage(), snDto);
        all.getRecords().forEach(x -> {
            if(StringUtils.isNotEmpty(x.getItemBo())){
                x.setItemBo(new ItemHandleBO(x.getItemBo()).getItem());
            }
            if(StringUtils.isNotEmpty(x.getWorkShopBo())){
                x.setWorkShop(new WorkShopHandleBO(x.getWorkShopBo()).getWorkShop());
            }
        });
        return all;

    }

    @Override
    public void saveSn(Sn sn) {
        String[] split = sn.getSn().split(",");
        for (String s : split) {
            String bo = new SnHandleBO(UserUtils.getSite(), s).getBo();
            Sn sn1 = snMapper.selectById(bo);
            sn.setBo(bo);
            sn.setSn(s);
            sn.setWorkShopBo(new WorkShopHandleBO(UserUtils.getSite(),sn.getWorkShop()).getBo());
            if (StringUtils.isNotEmpty(sn.getProductLine())) {
                sn.setProductLineBo(new ProductLineHandleBO(UserUtils.getSite(), sn.getProductLine()).getBo());
            }else {
                sn.setProductLineBo(sn.getProductLine());
            }
            sn.setGetTime(sn.getGetTime() == null ? new Date() : sn.getGetTime());
            if (sn1 != null) {
                sn.setModifyDate(new Date());
                sn.setModifyUser(userUtil.getUser().getUserName());
                snMapper.updateById(sn);
            }
        }

    }

    @Override
    public Map<String, Object> getItem(String sn) {
        return snMapper.getItem(sn);
    }
}
