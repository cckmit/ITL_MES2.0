package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.DataList;
import com.itl.mes.core.api.vo.DataListFullVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据列表表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-03
 */
public interface DataListService extends IService<DataList> {

    List<DataList> selectList();

    void saveDataList(DataListFullVo dataListVo) throws CommonException;

    void deleteDataListByDataList(String dataList, Date modifyDate) throws CommonException;

    DataList selectByDataList(String dataList) throws CommonException;

    DataListFullVo getDataListFullVoByDataList(String dataList) throws CommonException;
}