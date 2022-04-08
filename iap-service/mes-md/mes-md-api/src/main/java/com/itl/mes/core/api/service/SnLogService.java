package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.SnLog;
import com.itl.mes.core.api.vo.SnLogVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SN日志表 服务类
 * </p>
 *
 * @author space
 * @since 2019-09-25
 */
public interface SnLogService extends IService<SnLog> {

    List<SnLog> selectList();

    IPage<SnLogVo> selectPageByDate(IPage<SnLogVo> page, Map<String, Object> params) throws CommonException;

    void exportMouldGroup(String site, HttpServletResponse response,String item, String startNumber, String endNumber, String startCreateDate, String endCreateDate) throws CommonException;

    void deleteSnLog(List<SnLogVo> snLogs) throws CommonException;
}