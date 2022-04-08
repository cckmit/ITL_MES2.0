package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.me.api.dto.ItemWithTemplateDto;
import com.itl.mes.me.api.entity.InstructorItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业指导书内容项
 *
 * @author yaoxiang
 * @date 2020-12-25
 */
@Mapper
public interface InstructorItemMapper extends BaseMapper<InstructorItem> {

    List<ItemWithTemplateDto> selectWithTemplate(@Param("instructorBo") String instructorBo);
}
