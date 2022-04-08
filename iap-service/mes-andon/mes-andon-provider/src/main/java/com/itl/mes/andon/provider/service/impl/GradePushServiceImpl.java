package com.itl.mes.andon.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.PageUtils;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.api.service.GradePushService;
import com.itl.mes.andon.provider.mapper.GradePushMapper;
import org.springframework.stereotype.Service;
import java.util.Map;



@Service("gradePushService")
public class GradePushServiceImpl extends ServiceImpl<GradePushMapper, GradePush> implements GradePushService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return null;
    }

}