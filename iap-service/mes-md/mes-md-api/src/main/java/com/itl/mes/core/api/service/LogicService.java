package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.LogicHandleBO;
import com.itl.mes.core.api.entity.Logic;
import com.itl.mes.core.api.vo.LogicVo;

import java.util.List;

/**
 * <p>
 * SQL语句表 服务类
 * </p>
 *
 * @author space
 * @since 2019-11-21
 */
public interface LogicService extends IService<Logic> {

    List<Logic> selectList();

    Logic getLogicByLogicHandleBO(LogicHandleBO logicHandleBO) throws CommonException;

    Logic getExistLogicByLogicHandleBO(LogicHandleBO logicHandleBO) throws CommonException;

    Logic getLogicByLogicNoAndVersion(String logicNo, String version)throws CommonException;

    String getCurrentLogicNoContent(String logicNo) throws CommonException;

    String getLogicNoAndVersionContent(String logicNo, String version) throws CommonException;

    void saveLogic(LogicVo logicVo) throws CommonException;
}