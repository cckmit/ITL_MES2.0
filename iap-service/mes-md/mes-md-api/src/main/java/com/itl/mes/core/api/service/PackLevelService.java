package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.PackLevel;
import com.itl.mes.core.api.vo.PackLevelVo;

import java.util.List;

/**
 * <p>
 * 包装级别表 服务类
 * </p>
 *
 * @author space
 * @since 2019-07-12
 */
public interface PackLevelService extends IService<PackLevel> {

    List<PackLevel> selectList();

    void savePackLevels(String packGrade, String packingBO, List<PackLevelVo> packLevelVos)throws CommonException;

    void savePackLevelP(String packGrade, PackLevelVo packLevelVo)throws CommonException;

    void savePackLevelM(PackLevelVo packLevelVo)throws CommonException;

    void delete(String packingBO)throws CommonException;

    List<PackLevel> getPackLevels(String packingBO)throws CommonException;

    void IsExits(String packingBO, String objectBO)throws CommonException;
}