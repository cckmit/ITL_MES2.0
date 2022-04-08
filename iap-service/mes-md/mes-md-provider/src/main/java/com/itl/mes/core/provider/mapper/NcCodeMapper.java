package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.vo.NcDispRouterVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 不合格代码表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */

public interface NcCodeMapper extends BaseMapper<NcCode> {

    /**
     * 查询可分配不合格代码组
     * @param site
     * @param ncCodeBO
     * @return
     */
    List<Map<String, Object>> getAvailableNcGroupList(@Param("site") String site, @Param("ncCodeBO") String ncCodeBO);

    /**
     * 查询已分配不合格代码组
     * @param site
     * @param ncCodeBO
     * @return
     */
    List<Map<String, Object>> getAssignedNcGroupList(@Param("site") String site, @Param("ncCodeBO") String ncCodeBO);

    /**
     * 查询可分配处置工艺路线
     * @param site
     * @param ncCodeBO
     * @return
     */
    List<NcDispRouterVo> getAvailableNcDispRouterVos(@Param("site") String site, @Param("ncCodeBO") String ncCodeBO);

    /**
     * 查询已分配处置工艺路线
     * @param site
     * @param ncCodeBO
     * @return
     */
    List<NcDispRouterVo> getAssignedNcDispRouterVos(@Param("site") String site, @Param("ncCodeBO") String ncCodeBO);

    List<NcCode> selectTop(@Param("site")String site, @Param("ncCode")String ncCode, @Param("ncName")String ncName);
}