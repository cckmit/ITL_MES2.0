package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.SimLovDto;
import com.itl.iap.system.api.entity.SimLov;
import org.apache.ibatis.annotations.Param;

/**
 * @author EbenChan
 * @date 2020/11/21 18:16
 **/
public interface SimLovMapper extends BaseMapper<SimLov> {
    /**
     * 分页查询
     *
     * @param simLovDto Lov实例
     * @return IPage<SimLovDto>
     */
    IPage<SimLovDto> pageQuery(Page<SimLov> page, @Param("simLovDto") SimLovDto simLovDto);

    /**
     * 根据code获取详情
     *
     * @param code Lov实例
     * @return SimLovDto
     */
    SimLovDto getByCode(@Param("code") String code);
}
