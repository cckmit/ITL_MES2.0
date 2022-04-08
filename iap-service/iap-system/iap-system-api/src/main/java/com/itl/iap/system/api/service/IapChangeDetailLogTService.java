package com.itl.iap.system.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.system.api.dto.IapChangeDetailLogTDto;
import com.itl.iap.system.api.entity.IapChangeDetailLogT;

/**
 * 操作变动明细日志(IapChangeDetailLogT)表服务接口
 *
 * @author linjs
 * @since 2020-10-30 11:06:41
 */
public interface IapChangeDetailLogTService extends IService<IapChangeDetailLogT>{

    IPage<IapChangeDetailLogTDto> pageQuery(IapChangeDetailLogTDto iapChangeDetailLogTDto);
}