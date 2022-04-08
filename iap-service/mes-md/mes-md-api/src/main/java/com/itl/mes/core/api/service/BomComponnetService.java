package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.BomComponnet;
import com.itl.mes.core.api.vo.BomComponnetVo;


import java.util.List;

/**
 * <p>
 * 物料清单组件 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-05
 */
public interface BomComponnetService extends IService<BomComponnet> {

    List<BomComponnet> selectList();
    void  save(String bomBO, BomComponnetVo bomComponnetVo)throws CommonException;
    List<BomComponnet> select(String bomBO)throws CommonException;
    int delete(String bomBO)throws CommonException;
}