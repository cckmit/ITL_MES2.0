package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.SimLovDetailDto;
import com.itl.iap.system.api.entity.SimLovDetail;
import org.apache.ibatis.annotations.Param;

/**
 * @author EbenChan
 * @date 2020/11/21 18:16
 **/
public interface SimLovDetailMapper extends BaseMapper<SimLovDetail> {
    /**
     * 分页查询
     *
     * @param simLovDetailDto Lov实例
     * @return IPage<SimLovDetailDto>
     */
    IPage<SimLovDetailDto> pageQuery(Page<SimLovDetail> page, @Param("simLovDetailDto") SimLovDetailDto simLovDetailDto);

}
