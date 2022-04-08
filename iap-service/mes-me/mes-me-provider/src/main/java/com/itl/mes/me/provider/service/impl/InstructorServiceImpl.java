package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.me.api.bo.InstructorHandleBo;
import com.itl.mes.me.api.bo.InstructorItemHandleBo;
import com.itl.mes.me.api.dto.InstructorQueryDto;
import com.itl.mes.me.api.dto.InstructorSaveDto;
import com.itl.mes.me.api.entity.*;
import com.itl.mes.me.api.service.InstructorService;
import com.itl.mes.me.provider.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
@Service
public class InstructorServiceImpl extends ServiceImpl<InstructorMapper, Instructor> implements InstructorService {

    @Resource
    private InstructorMapper instructorMapper;

    @Resource
    private InstructorItemMapper itemMapper;

    @Resource
    private InstructorItemTemplateMapper templateMapper;

    @Resource
    private InstructorVarMapper varMapper;

    @Resource
    private InstructorOperationMapper instructorOperationMapper;

    @Autowired
    private UserUtil userUtil;

    private static final String INSTRUCTOR_BO = "INSTRUCTOR_BO";
    private static final String BASE_ITEM = "BASE_INFO";
    private static final String BASE_ITEM_NAME = "基础信息";

    @Override
    public IPage<Instructor> queryPage(InstructorQueryDto queryDto) throws CommonException {
        if (queryDto.getPage() == null) {
            queryDto.setPage(new Page(0, 10));
        }
        queryDto.getPage().setDesc("CREATE_DATE");
        queryDto.setSite(UserUtils.getSite());
        try {
            return instructorMapper.queryList(queryDto.getPage(), queryDto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IPage<Instructor> queryPageByState(InstructorQueryDto queryDto) throws CommonException {
        if (queryDto.getPage() == null) {
            queryDto.setPage(new Page(0, 10));
        }
        queryDto.getPage().setDesc("CREATE_DATE");
        queryDto.setSite(UserUtils.getSite());
        try {
            return instructorMapper.queryListByState(queryDto.getPage(), queryDto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveAndUpdate(InstructorSaveDto instructorSaveDto) throws CommonException {
        String userName = userUtil.getUser().getUserName();
        // 视为新增操作
        if (StrUtil.isBlank(instructorSaveDto.getBo())) {
            String bo = new InstructorHandleBo(UserUtils.getSite(), instructorSaveDto.getInstructor(), instructorSaveDto.getVersion()).getBo();
            Instructor instructor_db = getById(bo);
            if (instructor_db == null) {
                Instructor version_db = getOne(new QueryWrapper<Instructor>().lambda().eq(Instructor::getVersion, instructorSaveDto.getVersion()));
                if (version_db != null) {
                    throw new CommonException("该版本号数据库中已存在", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                Instructor instructor = new Instructor();
                BeanUtil.copyProperties(instructorSaveDto, instructor);
                Date newDate = new Date();
                instructor.setObjectSetBasicAttribute(userName, newDate);
                instructor.setSite(UserUtils.getSite()).setBo(bo);
                instructorMapper.insert(instructor);

                // 初始化指导书内容
                InstructorItem baseItem = new InstructorItem().setSite(UserUtils.getSite())
                        .setInstructorBo(bo)
                        .setInstructorItem(BASE_ITEM)
                        .setInstructorItemName(BASE_ITEM_NAME)
                        .setState(1)
                        .setDefaultShow(1);
                baseItem.setBo(new InstructorItemHandleBo(UserUtils.getSite(), BASE_ITEM, instructorSaveDto.getVersion()).getBo())
                        .setObjectSetBasicAttribute(userName, newDate);
                itemMapper.insert(baseItem);
            } else {
                throw new CommonException("该编号版本数据已存在", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
        // 视为修改操作
        else {
            Instructor instructor = new Instructor();
            BeanUtil.copyProperties(instructorSaveDto, instructor);
            Date newDate = new Date();
            Instructor byId = getById(instructorSaveDto.getBo());
            instructor.setCreateUser(byId.getCreateUser());
            instructor.setCreateDate(byId.getCreateDate());
            instructor.setModifyUser(userName);
            instructor.setModifyDate(newDate);
            instructor.setSite(UserUtils.getSite());
            int updateInt = instructorMapper.updateById(instructor);
            if (updateInt == 0) {
                throw new CommonException("数据已修改,请重新查询再执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void delete(String[] bos) throws CommonException {
        try {
            // 获取指导书内容
            List<InstructorItem> items = itemMapper.selectList(new QueryWrapper<InstructorItem>().in(INSTRUCTOR_BO, bos));
            if (items.size() > 0) {
                // 获取内容Bo
                List<String> itemBos = new ArrayList<>();
                items.forEach(item -> itemBos.add(item.getBo()));

                // 获取内容模板
                if (templateMapper.selectBatchIds(itemBos).size() > 0) {
                    templateMapper.deleteBatchIds(itemBos);
                }
                itemMapper.deleteBatchIds(itemBos);
            }

            // 获取指导书变量
            List<InstructorVar> vars = varMapper.selectList(new QueryWrapper<InstructorVar>().in(INSTRUCTOR_BO, bos));
            if (vars.size() > 0) {
                List<String> varBos = new ArrayList<>();
                vars.forEach(var -> varBos.add(var.getBo()));

                varMapper.deleteBatchIds(varBos);
            }

            // 获取工序
            QueryWrapper<InstructorOperation> queryOperation = new QueryWrapper<InstructorOperation>().in(INSTRUCTOR_BO, bos);
            List<InstructorOperation> operations = instructorOperationMapper.selectList(queryOperation);
            if (operations.size() > 0) {
                instructorOperationMapper.delete(queryOperation);
            }

            instructorMapper.deleteBatchIds(Arrays.asList(bos));
        } catch (Exception e) {
            throw new CommonException("删除失败", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }
}
