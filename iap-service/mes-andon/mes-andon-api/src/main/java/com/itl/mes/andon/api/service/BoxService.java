package com.itl.mes.andon.api.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.andon.api.entity.Box;
import com.itl.mes.andon.api.vo.BoxForShowVo;
import com.itl.mes.andon.api.vo.BoxQueryVo;
import com.itl.mes.andon.api.vo.BoxVo;

import java.util.List;
import java.util.Map;

/**
 * 安灯灯箱
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
public interface BoxService extends IService<Box> {

    /**
     * 保存灯箱数据
     * @param boxVo
     * @throws CommonException
     */
    void saveByBoxVo(BoxVo boxVo) throws CommonException;

    /**
     * 根据box灯箱编号获取BoxVo
     * @param box
     * @return
     * @throws CommonException
     */
    BoxVo getBoxVoByBox(String box) throws CommonException;

    /**
     * 根据box灯箱编号获取Box
     * @param box
     * @return
     * @throws CommonException
     */
    Box selectByBox(String box) throws CommonException;

    /**
     * 模糊查询BoxVo
     * @param boxVo
     * @return
     * @throws CommonException
     */
    List<Box> selectByBoxVo(BoxVo boxVo) throws CommonException;

    /**
     * 删除灯箱数据
     * @param boxes
     * @return
     * @throws CommonException
     */
    int delete(String[] boxes) throws CommonException;

    /**
     * 分页查询灯箱数据
     * @param boxQueryVo
     * @return
     * @throws CommonException
     */
    IPage<BoxForShowVo> queryPage(BoxQueryVo boxQueryVo) throws CommonException;

    /**
     * 多条件模糊查询灯箱数据
     * @param box
     * @param boxName
     * @param boxDesc
     * @return
     * @throws CommonException
     */
    List<Box> selectBox(String box, String boxName, String boxDesc) throws CommonException;

    /**
     * 分页查询可用灯箱数据
     * @param boxQueryVo
     * @return
     * @throws CommonException
     */
    IPage<BoxForShowVo> queryPageForUse(BoxQueryVo boxQueryVo) throws CommonException;
}

