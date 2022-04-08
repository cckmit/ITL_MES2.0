package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.dto.SnDto;
import com.itl.mes.me.api.entity.Sn;

import java.util.Map;


/**
 * 条码信息表
 *
 * @author cuichonghe
 * @date 2020-12-25 12:36:32
 */
public interface SnService extends IService<Sn> {
    /**
     * 分页
     *
     * @param snDto
     * @return
     */
    IPage<Sn> getAll(SnDto snDto);

    /**
     * 保存
     *
     * @param sn
     */
    void saveSn(Sn sn);

    /**
     * 根据sn获取item
     *
     * @param sn
     * @return
     */
    Map<String, Object> getItem(String sn);
}

