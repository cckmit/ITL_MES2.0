package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.dto.InstructorItemDto;
import com.itl.mes.me.api.dto.ItemWithTemplateDto;
import com.itl.mes.me.api.entity.InstructorItem;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
public interface InstructorItemService extends IService<InstructorItem> {
    /**
     * 保存
     * @param instructorItemDto
     * @throws CommonException
     */
    void saveAndUpdate(InstructorItemDto instructorItemDto) throws CommonException;

    /**
     * 查询全部
     * @return
     */
    List<InstructorItem> listAll(String instructorBo);

    /**
     * 删除
     * @param itemBo
     * @throws CommonException
     */
    void delete(String itemBo) throws CommonException;

    /**
     *
     * @param instructorBo
     * @return
     * @throws CommonException
     */
    List<ItemWithTemplateDto> listWithTemplate(String instructorBo) throws CommonException;
}
