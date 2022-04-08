package com.itl.mes.andon.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.PageUtils;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.andon.api.service.RecordService;
import com.itl.mes.andon.provider.mapper.RecordMapper;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("recordService")
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return null;
    }

}