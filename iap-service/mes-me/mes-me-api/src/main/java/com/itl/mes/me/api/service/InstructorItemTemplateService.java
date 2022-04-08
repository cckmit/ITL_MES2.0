package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.entity.InstructorItemTemplate;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
public interface InstructorItemTemplateService extends IService<InstructorItemTemplate> {
    /**
     * 保存内容项模板
     * @param template
     * @throws CommonException
     */
    void saveTo(InstructorItemTemplate template) throws CommonException;
}
