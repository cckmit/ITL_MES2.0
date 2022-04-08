package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.dto.SnLogDto;
import com.itl.mes.me.api.entity.SnLog;

import javax.servlet.http.HttpServletResponse;


/**
 * SN日志表
 *
 * @author cuichonghe
 * @date 2020-12-25 12:36:32
 */
public interface SnLogService extends IService<SnLog> {

    /**
     * 分页查询
     * @param snLogDto
     * @return
     */
    IPage<SnLog> getAll(SnLogDto snLogDto);

    /**
     * 保存或修改
     * @param snLog
     */
    void saveSnLogs(SnLog snLog);

    /**
     * 导出excel
     * @param response
     */
    void exportOperaton(HttpServletResponse response);


}

