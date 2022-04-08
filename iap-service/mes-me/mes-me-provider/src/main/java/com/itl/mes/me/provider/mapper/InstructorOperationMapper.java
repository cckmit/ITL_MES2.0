package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.me.api.entity.InstructorOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业指导书工序表
 *
 * @author renren
 * @date 2020-12-25
 */
@Mapper
public interface InstructorOperationMapper extends BaseMapper<InstructorOperation> {

    /**
     * 获取工序
     * @param operationBos
     * @return
     */
    List<Operation> getOperations(@Param("operationBos") List<String> operationBos,@Param("site") String site, @Param("inState") String in);
}
