package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.NcGroup;
import com.itl.mes.core.api.vo.NcCodeVo;
import com.itl.mes.core.api.vo.NcGroupVo;
import com.itl.mes.core.api.vo.OperationVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 不合格代码组表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
public interface NcGroupService extends IService<NcGroup> {

    List<NcGroup> selectList();

    void saveNcGroup(NcGroupVo ncGroupVo) throws CommonException;

    NcGroup selectByNcGroup(String ncGroup) throws CommonException;

    List<NcGroup> selectByNcGroupVo(NcGroupVo ncGroupVo) throws CommonException;

    void deleteByNcGroup(String ncGroup, Date modifyDate) throws CommonException;

    List<NcGroup> selectNcGroup(String ncGroup, String ncGroupName, String ncGroupDesc) throws CommonException;

    NcGroupVo getNcGroupVoByNcGroup(String ncGroup) throws CommonException;

    List<OperationVo> getOperationList();

    List<NcCodeVo> getNcCodeVoList(String ncCode, String ncName);


}