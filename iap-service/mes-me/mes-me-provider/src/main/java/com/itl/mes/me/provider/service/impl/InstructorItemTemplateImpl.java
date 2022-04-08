package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.mes.me.api.entity.InstructorItemTemplate;
import com.itl.mes.me.api.service.InstructorItemTemplateService;
import com.itl.mes.me.provider.mapper.InstructorItemTemplateMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yaoxiang
 * @date 2020/12/27
 * @since JDK1.8
 */
@Service
public class InstructorItemTemplateImpl extends ServiceImpl<InstructorItemTemplateMapper, InstructorItemTemplate> implements InstructorItemTemplateService {
    @Resource
    private InstructorItemTemplateMapper templateMapper;

    @Override
    public void saveTo(InstructorItemTemplate template) throws CommonException {
        InstructorItemTemplate template_db = templateMapper.selectById(template.getInstructorItemBo());
        if (template_db == null) {
            templateMapper.insert(template);
        } else {
            int updateInt = templateMapper.updateById(template);

            if (updateInt == 0) {
                throw new CommonException("数据已修改,请重新查询在执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
    }
}
