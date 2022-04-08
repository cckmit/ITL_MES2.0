package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.vo.NcCodeVo;
import com.itl.mes.core.api.vo.NcGroupVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 不合格代码表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
public interface NcCodeService extends IService<NcCode> {

    List<NcCode> selectList();

    void saveNcCode(NcCodeVo ncCodeVo)throws CommonException;

    void deleteNcCode(String ncCode, Date modifyDate)throws  CommonException;

    NcCode selectByNcCode(String ncCode)throws CommonException;

    List<NcCode> selectByNcCodeVo(NcCodeVo ncCodeVo)throws CommonException;

    List<NcCode> selectNcCode(String ncCode, String ncName, String ncDesc)throws CommonException;

    NcCodeVo getNcCodeVoByNcCode(String ncCode)throws CommonException;

    List<NcGroupVo> getNcGroupVoList()throws CommonException;

}