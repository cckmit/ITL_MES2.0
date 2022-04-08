package com.itl.mes.andon.api.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.util.PageUtils;
import com.itl.mes.andon.api.entity.Record;

import java.util.Map;

/**
 * 安灯日志
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
public interface RecordService extends IService<Record> {

    PageUtils queryPage(Map<String, Object> params);
}

