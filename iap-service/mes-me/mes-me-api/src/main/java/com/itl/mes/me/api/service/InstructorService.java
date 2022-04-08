package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.dto.InstructorQueryDto;
import com.itl.mes.me.api.dto.InstructorSaveDto;
import com.itl.mes.me.api.entity.Instructor;

import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
public interface InstructorService extends IService<Instructor> {

    /**
     * 分页查询
     * @param queryDto
     * @return
     */
    IPage<Instructor> queryPage(InstructorQueryDto queryDto) throws CommonException;

    /**
     * 分页查询
     * @param queryDto
     * @return
     */
    IPage<Instructor> queryPageByState(InstructorQueryDto queryDto) throws CommonException;

    /**
     * 保存
     * @param instructorSaveDto
     */
    void saveAndUpdate(InstructorSaveDto instructorSaveDto) throws CommonException;

    /**
     * 删除
     * @param bos
     * @return
     * @throws CommonException
     */
    void delete(String[] bos) throws CommonException;
}
