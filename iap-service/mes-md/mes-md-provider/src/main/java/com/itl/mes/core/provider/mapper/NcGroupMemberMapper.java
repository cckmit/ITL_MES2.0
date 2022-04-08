package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.NcGroupMember;
import com.itl.mes.core.api.vo.CodeGroupVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 不合格组成员表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-15
 */

public interface NcGroupMemberMapper extends BaseMapper<NcGroupMember> {

    /**
     * 查询可分配不良代码
     * @param site
     * @param ncGroupBO
     * @return
     */
    List<CodeGroupVo> getAvailableNcCodeList(@Param("site") String site, @Param("ncGroupBO") String ncGroupBO);

    /**
     * 查询已分配不良代码
     * @param site
     * @param ncGroupBO
     * @return
     */
    List<CodeGroupVo> getAssignedNcCodeList(@Param("site") String site, @Param("ncGroupBO") String ncGroupBO);

}