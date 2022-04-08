package com.itl.mes.me.provider.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itl.mes.me.provider.mapper.ItemRuleLabelDetailMapper;
import com.itl.mes.me.api.entity.ItemRuleLabelDetail;
import com.itl.mes.me.api.service.ItemRuleLabelDetailService;

import javax.annotation.Resource;


@Service
public class ItemRuleLabelDetailServiceImpl extends ServiceImpl<ItemRuleLabelDetailMapper, ItemRuleLabelDetail> implements ItemRuleLabelDetailService {
    @Resource
    private ItemRuleLabelDetailMapper itemRuleLabelDetailMapper;
}
