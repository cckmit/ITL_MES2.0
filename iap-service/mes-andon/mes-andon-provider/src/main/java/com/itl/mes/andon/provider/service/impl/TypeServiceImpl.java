package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.bo.PushHandleBO;
import com.itl.mes.andon.api.bo.TypeHandleBO;
import com.itl.mes.andon.api.dto.TypeDTO;
import com.itl.mes.andon.api.entity.Push;
import com.itl.mes.andon.api.entity.Type;
import com.itl.mes.andon.api.service.PushService;
import com.itl.mes.andon.api.service.TypeService;
import com.itl.mes.andon.api.vo.TypeVo;
import com.itl.mes.andon.provider.mapper.TypeMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("typeService")
@Transactional
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
    @Resource
    private TypeMapper typeMapper;

    @Autowired
    private PushService pushService;

    @Autowired
    private UserUtil userUtil;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveByTypeVo(TypeVo typeVo) throws CommonException {
        String andonPushBo = null;
        if (StrUtil.isNotBlank(typeVo.getAndonPush())) {
            andonPushBo = new PushHandleBO(UserUtils.getSite(), typeVo.getAndonPush()).getBo();
            Push push = pushService.getById(andonPushBo);
            if (push == null) {
                throw new CommonException("推送:" + andonPushBo + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }

        TypeHandleBO typeHandleBO = new TypeHandleBO(UserUtils.getSite(), typeVo.getAndonType());
        String bo = typeHandleBO.getBo();

        Type type_db = typeMapper.selectById(bo);

        if (type_db == null) {
            Type type = new Type();
            type.setBo(bo).setSite(UserUtils.getSite()).setAndonType(typeVo.getAndonType())
                    .setAndonTypeName(typeVo.getAndonTypeName())
                    .setAndonTypeDesc(typeVo.getAndonTypeDesc())
                    .setAndonPushBo(andonPushBo)
                    .setAndonTypeTag(typeVo.getAndonTypeTag())
                    .setState(typeVo.getState())
                    .setAbnormalType(typeVo.getAbnormalType())
                    .setIsCheck(typeVo.getIsCheck());
            Date newDate = new Date();
            type.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), newDate);

            typeMapper.insert(type);
            typeVo.setModifyDate(newDate);
        } else {
            Date frontModifyDate = typeVo.getModifyDate();
            Date modifyDate = type_db.getModifyDate();
            //CommonUtil.compareDateSame(frontModifyDate, modifyDate);
            Type type = new Type();
            type.setBo(bo).setSite(UserUtils.getSite()).setAndonType(typeVo.getAndonType())
                    .setAndonTypeName(typeVo.getAndonTypeName())
                    .setAndonTypeDesc(typeVo.getAndonTypeDesc())
                    .setAndonPushBo(andonPushBo)
                    .setAndonTypeTag(typeVo.getAndonTypeTag())
                    .setState(typeVo.getState())
                    .setIsCheck(typeVo.getIsCheck());
            Date newDate = new Date();
            type.setCreateUser(type_db.getCreateUser());
            type.setCreateDate(type_db.getCreateDate());
            type.setModifyUser(userUtil.getUser().getUserName());
            type.setModifyDate(newDate);
            type.setAbnormalType(typeVo.getAbnormalType());
            Integer updatInt = typeMapper.updateById(type);
            if (updatInt == 0) {
                throw new CommonException("数据已修改,请重新查询2在执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            typeVo.setModifyDate(newDate);
        }
    }

    @Override
    public TypeVo getTypeVoByType(String andonType) throws CommonException {
        Type type_db = selectByType(andonType);
        TypeVo typeVo = new TypeVo();
        BeanUtils.copyProperties(type_db, typeVo);
        if (type_db.getAndonPushBo() != null && !type_db.getAndonPushBo().equals("")) {
            PushHandleBO push = new PushHandleBO(type_db.getAndonPushBo());
            if (push != null) {
                typeVo.setAndonPush(push.getAndonPush());
            }
        }
        return typeVo;
    }

    @Override
    public Type selectByType(String type) throws CommonException {
        QueryWrapper<Type> query = new QueryWrapper<>();
        query.eq(Type.ANDON_TYPE, type);
        List<Type> types = typeMapper.selectList(query);
        if (types.isEmpty()) {
            throw new CommonException("安灯类型:" + type + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        } else {
            return types.get(0);
        }
    }

    @Override
    public List<Type> selectByTypeVo(TypeVo typeVo) throws CommonException {
        QueryWrapper<Type> query = new QueryWrapper<>();
        query.eq(Type.SITE, UserUtils.getSite());
        if (typeVo.getAndonType() != null) {
            query.like(Type.ANDON_TYPE, typeVo.getAndonType());
        }
        if (typeVo.getAndonTypeName() != null) {
            query.like(Type.ANDON_TYPE_NAME, typeVo.getAndonTypeName());
        }
        if (typeVo.getAndonTypeDesc() != null) {
            query.like(Type.ANDON_TYPE_DESC, typeVo.getAndonTypeDesc());
        }
        List<Type> types = typeMapper.selectList(query);
        return types;
    }

    @Override
    public List<Type> selectType(String type, String typeName, String typeDesc) throws CommonException {
        QueryWrapper<Type> query = new QueryWrapper<>();
        query.eq(Type.SITE, UserUtils.getSite());
        if (type != null) {
            query.like(Type.ANDON_TYPE, type);
        }
        if (typeName != null) {
            query.like(Type.ANDON_TYPE_NAME, typeName);
        }
        if (typeDesc != null) {
            query.like(Type.ANDON_TYPE_DESC, typeDesc);
        }
        List<Type> types = typeMapper.selectList(query);
        return types;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public int delete(List<String> bos) throws CommonException {
        int deleteInt = 0;
        try {
            deleteInt = typeMapper.deleteBatchIds(bos);
        } catch (Exception e) {
            throw new CommonException("本次修改的数据中有关联项,请先去删除关联项!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return deleteInt;
    }

    @Override
    public IPage<Type> queryPage(TypeDTO typeDTO) throws CommonException {
        Map<String , Object> params = new HashMap<>();
        params.put("type",typeDTO.getAndonType());
        params.put("typeName",typeDTO.getAndonTypeName());
        params.put("orderByField", "CREATE_DATE");
        params.put("isAsc", false);
        params.put("page",typeDTO.getPage().getCurrent());
        params.put("limit",typeDTO.getPage().getSize());
        QueryWrapper<Type> query = new QueryWrapper<>();
        query.eq(Type.SITE, UserUtils.getSite());
        setBasicQuery(query, params);

        IPage<Type> page = page(new QueryPage<>(params), query);
        page.getRecords().forEach(data -> {
            if (StrUtil.isNotBlank(data.getAndonPushBo())) {
                data.setAndonPushBo(new PushHandleBO(data.getAndonPushBo()).getAndonPush());
            }
        });
        return page;
    }

    @Override
    public IPage<Type> queryPageForUse(TypeDTO typeDTO) throws CommonException {
        Map<String , Object> params = new HashMap<>();
        params.put("type",typeDTO.getAndonType());
        params.put("typeName",typeDTO.getAndonTypeName());
        params.put("orderByField", "CREATE_DATE");
        params.put("isAsc", false);
        QueryWrapper<Type> query = new QueryWrapper<>();
        query.lambda()
                .eq(Type::getState, "1")
                .eq(Type::getSite, UserUtils.getSite());
        if (params.get("type") != null) {
            query.like(Type.ANDON_TYPE, params.get("type"));
        }
        if (params.get("typeName") != null) {
            query.like(Type.ANDON_TYPE_NAME, params.get("typeName"));
        }
        if (params.get("typeDesc") != null) {
            query.like(Type.ANDON_TYPE_DESC, params.get("typeDesc"));
        }

        IPage<Type> page = page(new QueryPage<>(params), query);
        page.getRecords().forEach(data -> {
            if (StrUtil.isNotBlank(data.getAndonPushBo())) {
                data.setAndonPushBo(new PushHandleBO(data.getAndonPushBo()).getAndonPush());
            }
        });
        return page;
    }

    private void setBasicQuery(QueryWrapper query, Map<String, Object> params) {
        if (params.get("type") != null) {
            query.like(Type.ANDON_TYPE, params.get("type"));
        }
        if (params.get("typeName") != null) {
            query.like(Type.ANDON_TYPE_NAME, params.get("typeName"));
        }
        if (params.get("typeDesc") != null) {
            query.like(Type.ANDON_TYPE_DESC, params.get("typeDesc"));
        }
    }
}
