package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.me.api.dto.InstructorOperationDto;
import com.itl.mes.me.api.entity.InstructorOperation;

import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
public interface InstructorOperationService extends IService<InstructorOperation> {
    /**
     * 查询已分配工序和未分配的工序
     * @param instructorBo
     * @return
     */
    Map<String, List<Operation>> getOperations(String instructorBo);

    /**
     * 保存工序
     * @param instructorOperationDto
     */
    void saveOperations(InstructorOperationDto instructorOperationDto);
}
