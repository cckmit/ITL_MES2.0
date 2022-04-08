package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.me.api.bo.InstructorHandleBo;
import com.itl.mes.me.api.bo.InstructorVarHandleBo;
import com.itl.mes.me.api.dto.InstructorVarDto;
import com.itl.mes.me.api.entity.InstructorVar;
import com.itl.mes.me.api.service.InstructorVarService;
import com.itl.mes.me.provider.mapper.InstructorVarMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
@Service
public class InstructorVarServiceImpl extends ServiceImpl<InstructorVarMapper, InstructorVar> implements InstructorVarService {
    @Resource
    private InstructorVarMapper instructorVarMapper;

    @Override
    public List<InstructorVar> listAll(String instructorBo) throws CommonException {
        try {
            List<InstructorVar> vars = instructorVarMapper.selectList(new QueryWrapper<InstructorVar>().eq("INSTRUCTOR_BO", instructorBo));
            return vars;
        } catch (Exception e) {
            throw new CommonException("查询失败", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    public void saveAndUpdate(InstructorVarDto instructorVarDto) throws CommonException {
        String version = new InstructorHandleBo(instructorVarDto.getInstructorBo()).getVersion();
        // 视为新增操作
        if (StrUtil.isBlank(instructorVarDto.getBo())) {
            String bo = new InstructorVarHandleBo(UserUtils.getSite(), instructorVarDto.getVarCode(), version).getBo();
            InstructorVar var_db = getById(bo);
            if (var_db == null) {
                InstructorVar toSave = new InstructorVar();
                BeanUtil.copyProperties(instructorVarDto, toSave);
                toSave.setBo(bo).setSite(UserUtils.getSite());

                instructorVarMapper.insert(toSave);
            } else {
                throw new CommonException("该数据已存在", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
        // 视为更新操作
        else {
            InstructorVar toUpdate = new InstructorVar();
            BeanUtil.copyProperties(instructorVarDto, toUpdate);
            toUpdate.setSite(UserUtils.getSite());

            int updateInt = instructorVarMapper.updateById(toUpdate);
            if (updateInt == 0) {
                throw new CommonException("数据已修改,请重新查询在执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
    }
}
