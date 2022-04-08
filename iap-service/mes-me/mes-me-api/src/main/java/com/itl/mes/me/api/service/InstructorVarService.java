package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.dto.InstructorVarDto;
import com.itl.mes.me.api.entity.InstructorVar;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
public interface InstructorVarService extends IService<InstructorVar> {
    /**
     * 查询
     * @param instructorBo
     * @throws CommonException
     * @return
     */
    List<InstructorVar> listAll(String instructorBo) throws CommonException;

    /**
     * 保存
     * @param instructorVarDto
     * @throws CommonException
     */
    void saveAndUpdate(InstructorVarDto instructorVarDto) throws CommonException;
}
