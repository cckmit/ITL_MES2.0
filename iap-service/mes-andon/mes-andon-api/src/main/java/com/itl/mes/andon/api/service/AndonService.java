package com.itl.mes.andon.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.andon.api.dto.AndonQueryDTO;
import com.itl.mes.andon.api.dto.AndonSaveDTO;
import com.itl.mes.andon.api.entity.Andon;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.andon.api.vo.AndonVo;

import java.util.List;

/**
 * 安灯
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
public interface AndonService extends IService<Andon> {

    /**
     * 保存安灯信息
     * @param andonSaveDTO
     */
    void save(AndonSaveDTO andonSaveDTO);

    /**
     * 查询安灯列表
     * @param andonQueryDTO
     * @return
     */
    IPage<AndonVo> findList(AndonQueryDTO andonQueryDTO);


    /**
     * 根据ID查询安灯信息
     * @param id
     * @param
     * @return
     */
    AndonVo findById(String id);


    void deleteByIds(List<String> ids);

    List<Record> findByLine(String line);

    /**
     * 查询可用的安灯列表
     * @param andonQueryDTO
     * @return
     */
    IPage<AndonVo> findListForUse(AndonQueryDTO andonQueryDTO);
}
