package com.itl.mes.andon.api.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.andon.api.entity.Push;

/**
 * 安灯推送设置
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
public interface PushService extends IService<Push> {

    /**
     * 更新或保存
     * @param push
     */
    void savePush(Push push);

    /**
     * 根据bo查询
     * @param bo
     * @return
     */
    Push getByBo(String bo);
}

