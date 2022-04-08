package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.BomHandleBO;
import com.itl.mes.core.api.dto.BomDto;
import com.itl.mes.core.api.entity.Bom;
import com.itl.mes.core.api.vo.BomVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 物料清单表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-05
 */
public interface BomService extends IService<Bom> {

    List<Bom> selectList();
    IPage<Bom> getBom(BomDto bomDto);

    /**
     * 查询BOM
     *
     * @param bomHandleBO bomHandleBO
     * @return Bom
     */
    Bom getBomByHandleBO(BomHandleBO bomHandleBO);

    /**
     * 查询存在的BOM，不存在则报错
     *
     * @param bomHandleBO bomHandleBO
     * @return Bom
     * @throws CommonException 异常
     */
    Bom getExitsBomByHandleBO(BomHandleBO bomHandleBO) throws CommonException;

    /**
     * 通过工厂 物料清单 版本查询物料清单数据
     *
     * @param site 工厂
     * @param bom 物料清单
     * @param version 版本
     * @return Bom
     * @throws CommonException 异常
     */
    Bom getRouterByRouterAndVersion(String site, String bom, String version) throws CommonException;

    public Bom selectByBom(String bom, String version)throws CommonException;

    public void save(BomVo bomVo)throws CommonException;

    public List<Bom> select(String bom, String bomDesc, String zsType, String state, String version)throws CommonException;

    public int delete(String bom, String version, Date modifyDate)throws CommonException;

    public BomVo getBomVoByBomAndVersion(String bom, String version)throws CommonException;

    public BomVo getBomVoByBo(String bo)throws CommonException;
}
