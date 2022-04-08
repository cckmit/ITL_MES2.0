package com.itl.mes.andon.api.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.util.PageUtils;
import com.itl.mes.andon.api.entity.GradePush;

import java.util.Map;

/**
 * 安灯等级推送
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
public interface GradePushService extends IService<GradePush> {

    PageUtils queryPage(Map<String, Object> params);
}

