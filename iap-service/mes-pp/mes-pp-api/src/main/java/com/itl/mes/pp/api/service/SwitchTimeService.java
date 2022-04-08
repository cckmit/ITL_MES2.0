package com.itl.mes.pp.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.pp.api.dto.SwitchTimeSaveDto;
import com.itl.mes.pp.api.entity.SwitchTimeEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/17
 * @since JDK1.8
 */
public interface SwitchTimeService extends IService<SwitchTimeEntity> {
    /**
     * 分页查询生产切换时间
     * @param params
     * @return
     * @throws CommonException
     */
    IPage<SwitchTimeEntity> queryPage(Map<String, Object> params) throws CommonException;

    /**
     * 删除生产切换时间数据
     * @param bos
     * @return
     * @throws CommonException
     */
    int delete(String[] bos) throws CommonException;

    /**
     * 保存生产切换时间数据
     * @param switchTimeSaveDto
     * @throws CommonException
     */
    void saveAndUpdate(SwitchTimeSaveDto switchTimeSaveDto) throws CommonException;

    /**
     * 导出文件
     * @param response
     */
    void exportOperation(HttpServletResponse response);
}
