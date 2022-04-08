package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.util.CodeUtils;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.dto.IapDictItemTDto;
import com.itl.iap.system.api.entity.IapDictItemT;
import com.itl.iap.system.api.entity.IapDictT;
import com.itl.iap.system.provider.mapper.IapDictMapper;
import com.itl.iap.system.api.dto.IapDictTDto;
import com.itl.iap.system.api.service.IapDictItemTService;
import com.itl.iap.system.api.service.IapDictTService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 字典表主表实现类
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
@Service
@Slf4j
public class IapDictTServiceImpl extends ServiceImpl<IapDictMapper, IapDictT> implements IapDictTService {
    @Autowired
    private IapDictMapper iapDictMapper;
    @Autowired
    private IapDictItemTService iapDictItemService;

    /**
     * 新建字典
     *
     * @param iapDictDto 字典表对象
     * @return 字典表对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IapDictTDto insertIapDictT(IapDictTDto iapDictDto) throws Exception {
        try {
            IapDictT iapDictT = new IapDictT();
            iapDictDto.setId(UUID.uuid32());
            if(StringUtils.isBlank(iapDictDto.getCode())) this.setCode(iapDictDto, new Date(), 0);
            if (iapDictDto.getEnabled() == null) {
                iapDictDto.setEnabled((short) 1);
            }
            BeanUtils.copyProperties(iapDictDto, iapDictT);
            List<IapDictT> iapDictTS = iapDictMapper.selectList(new QueryWrapper<IapDictT>().lambda().eq(IapDictT::getCode, iapDictT.getCode()));
            if (CollectionUtil.isNotEmpty(iapDictTS)) {
                throw new Exception();
            }
            iapDictMapper.insert(iapDictT);
            return iapDictDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("字典编号不能重复!");
        }
    }

    /**
     * 修改字典
     *
     * @param iapDictDto 字典表对象
     * @return 字典表id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateIapDictT(IapDictTDto iapDictDto) {
        IapDictT iapDictT = new IapDictT();
        BeanUtils.copyProperties(iapDictDto, iapDictT);
        iapDictMapper.updateById(iapDictT);
        return iapDictDto.getId();
    }

    /**
     * 通过id批量删除字典
     *
     * @param ids 字典id集合
     * @return 删除成功与否
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByIds(List<String> ids) {
        List<String> deleteList = new ArrayList<>();
        //根据idList查询所有要删除的所有子表数据
        List<IapDictItemTDto> iapDictItemDtoList = iapDictItemService.selectByIapDictIdList(ids);
        if (iapDictItemDtoList != null) {
            deleteList.addAll(ids);
            this.deleteByParentId(iapDictItemDtoList, deleteList);
            iapDictItemService.removeByIds(deleteList);
        }
        boolean b = this.removeByIds(ids);
        return b;
    }

    /**
     * 递归删除
     *
     * @param idList     传入的父idList
     * @param deleteList 需要删除的list，每次递归都会往里面加入要删除的数据
     */
    private void deleteByParentId(List<IapDictItemTDto> idList, List<String> deleteList) {
        List<IapDictItemTDto> parentListDto = null;
        List<IapDictItemT> parentList = null;
        if (idList != null && idList.size() > 0) {
            for (IapDictItemTDto iapDictItemDto : idList) {
                parentList = iapDictItemService.list(new QueryWrapper<IapDictItemT>().eq("parent_id", iapDictItemDto.getId()));
                if (parentList != null && parentList.size() > 0) {
                    deleteList.add(iapDictItemDto.getId());
                     parentList.stream().map(x->{
                         return deleteList.add(x.getId());
                     });
                    this.deleteByParentId(parentListDto, deleteList);
                }
            }
        }
    }

    /**
     * 分页查询
     *
     * @param iapDictDto 字典表对象
     * @return 字典分页对象
     */
    @Override
    public IPage<IapDictTDto> query(IapDictTDto iapDictDto) {
        return iapDictMapper.query(iapDictDto.getPage(), iapDictDto);
    }  @Override
    public IPage<IapDictTDto> queryByState(IapDictTDto iapDictDto) {
        return iapDictMapper.queryByState(iapDictDto.getPage(), iapDictDto);
    }

    /**
     * 通过字典编号和名称查询
     *
     * @param iapDictDto 字典表对象
     * @return 字典集合
     */
    @Override
    public List<IapDictTDto> queryDictCodeOrName(IapDictTDto iapDictDto) {
        return iapDictMapper.queryDictCodeOrName(iapDictDto);
    }

    /**
     * 设置编码值
     *
     * @param obj  需要设置code属性的实体类
     * @param date 当前时间
     * @param num  默认0 大于 20跳出
     */
    private void setCode(IapDictTDto obj, Date date, Integer num) throws CommonException {
        obj.setCode(CodeUtils.dateToCode("NX", date));
        if (num > CodeUtils.NUM) {
            log.error(CommonExceptionDefinition.getCurrentClassError() + "编码设置失败!");
            throw new CommonException(new CommonExceptionDefinition(500, "编码设置失败!"));
        }
        if (iapDictMapper.selectCount(new QueryWrapper<IapDictT>().ne("id", obj.getId()).eq("code", obj.getCode())) != 0) {
            num++;
            this.setCode(obj, date, num);
        }
    }

}
