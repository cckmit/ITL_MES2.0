package com.itl.iap.workflow.workflow.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.workflow.api.dto.ActHiNotifyDto;
import com.itl.iap.workflow.api.entity.ActHiNotify;

/**
 * 抄送通知(ActHiNotify)表服务接口
 *
 * @author 罗霄
 * @date 2020-09-16
 * @since jdk1.8
 */
public interface IActHiNotifyService extends IService<ActHiNotify> {

    /**
     * 分页查询
     *
     * @param actHiNotifyDto 抄送通知实例
     * @return IPage<ActHiNotifyDto>
     */
    IPage<ActHiNotifyDto> pageQuery(ActHiNotifyDto actHiNotifyDto);
}