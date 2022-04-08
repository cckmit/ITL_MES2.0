package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.me.api.bo.InstructorHandleBo;
import com.itl.mes.me.api.bo.InstructorItemHandleBo;
import com.itl.mes.me.api.dto.InstructorItemDto;
import com.itl.mes.me.api.dto.ItemWithTemplateDto;
import com.itl.mes.me.api.entity.InstructorItem;
import com.itl.mes.me.api.entity.InstructorItemTemplate;
import com.itl.mes.me.api.entity.InstructorVar;
import com.itl.mes.me.api.service.InstructorItemService;
import com.itl.mes.me.provider.mapper.InstructorItemMapper;
import com.itl.mes.me.provider.mapper.InstructorItemTemplateMapper;
import com.itl.mes.me.provider.mapper.InstructorVarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
@Service
public class InstructorItemServiceImpl extends ServiceImpl<InstructorItemMapper, InstructorItem> implements InstructorItemService {

    @Resource
    private InstructorItemMapper itemMapper;

    @Resource
    private InstructorItemTemplateMapper templateMapper;

    @Resource
    private InstructorVarMapper varMapper;

    @Autowired
    private UserUtil userUtil;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveAndUpdate(InstructorItemDto instructorItemDto) throws CommonException {
        String version = new InstructorHandleBo(instructorItemDto.getInstructorBo()).getVersion();
        // 视为新增操作
        if (StrUtil.isBlank(instructorItemDto.getBo())) {
            String bo = new InstructorItemHandleBo(UserUtils.getSite(), instructorItemDto.getInstructorItem(), version).getBo();
            InstructorItem instructorItem_db = getById(bo);
            if (instructorItem_db == null) {
                InstructorItem toSave = new InstructorItem();
                BeanUtil.copyProperties(instructorItemDto,toSave);
                Date newDate = new Date();
                toSave.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), newDate);
                toSave.setBo(bo).setSite(UserUtils.getSite());

                itemMapper.insert(toSave);
            } else {
                throw new CommonException("该编号数据已存在", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
        else {
            InstructorItem byId = getById(instructorItemDto.getBo());
            InstructorItem toUpdate = new InstructorItem();
            BeanUtil.copyProperties(instructorItemDto,toUpdate);
            Date newDate = new Date();
            toUpdate.setCreateUser(byId.getCreateUser());
            toUpdate.setCreateDate(byId.getCreateDate());
            toUpdate.setModifyUser(userUtil.getUser().getUserName());
            toUpdate.setModifyDate(newDate);
            toUpdate.setSite(UserUtils.getSite());
            int updateInt = itemMapper.updateById(toUpdate);
            if (updateInt == 0) {
                throw new CommonException("数据已修改,请重新查询在执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
    }

    @Override
    public List<InstructorItem> listAll(String instructorBo) {
        List<InstructorItem> instructorItems = itemMapper.selectList(new QueryWrapper<InstructorItem>().eq("INSTRUCTOR_BO", instructorBo));
        return instructorItems;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void delete(String itemBo) throws CommonException {
        try {
            InstructorItemTemplate template = templateMapper.selectById(itemBo);
            if (template != null) {
                templateMapper.deleteById(itemBo);
            }
            itemMapper.deleteById(itemBo);
        } catch (Exception e) {
            throw new CommonException("删除失败", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    public List<ItemWithTemplateDto> listWithTemplate(String instructorBo) throws CommonException {
        List<ItemWithTemplateDto> ret = itemMapper.selectWithTemplate(instructorBo);
        // 模板内容中的变量编号用变量内容替代
        List<InstructorVar> vars = varMapper.selectList(new QueryWrapper<InstructorVar>().eq("INSTRUCTOR_BO", instructorBo));
        ret.forEach(data -> vars.forEach(var -> {
            if (!StrUtil.isBlank(var.getVarCode()) && !StrUtil.isBlank(var.getVarDesc())) {
                if (!StrUtil.isBlank(data.getTemplate())) {
                    data.setTemplate(data.getTemplate().replace(var.getVarCode(), var.getVarDesc()));
                }
            }
        }));
        return ret;
    }
}
