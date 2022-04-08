package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.NcGroup;
import com.itl.mes.core.api.vo.NcGroupOperationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 不合格代码组表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */

public interface NcGroupMapper extends BaseMapper<NcGroup> {

    /**
     * 查询可分配工序
     * @param site
     * @param ncGroupBO
     * @return
     */
    List<NcGroupOperationVo> getAvailableOperationList(@Param("site") String site, @Param("ncGroupBO") String ncGroupBO);

    /**
     * 查询已分配工序
     * @param site
     * @param ncGroupBO
     * @return
     */
    List<NcGroupOperationVo> getAssignedOperationList(@Param("site") String site, @Param("ncGroupBO") String ncGroupBO);


    List<NcGroup> selectTop(@Param("site")String site);
}