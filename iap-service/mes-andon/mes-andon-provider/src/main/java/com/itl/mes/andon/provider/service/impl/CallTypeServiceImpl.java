package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.andon.api.bo.CallTypeHandleBO;
import com.itl.mes.andon.api.bo.GradePushHandleBO;
import com.itl.mes.andon.api.dto.CallTypeDTO;
import com.itl.mes.andon.api.entity.CallType;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.api.service.CallTypeService;
import com.itl.mes.andon.provider.mapper.CallTypeMapper;
import com.itl.mes.andon.provider.mapper.GradePushMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CallTypeServiceImpl extends ServiceImpl<CallTypeMapper, CallType> implements CallTypeService {

    @Autowired
    private CallTypeMapper callTypeMapper;

    @Autowired
    private GradePushMapper gradePushMapper;

    @Autowired
    private UserUtil userUtil;

    @Override
    public void saveCallType(CallType callType) {
        CallType callType1 =
                callTypeMapper.selectById(callType.getId());
        if (callType1 == null) {
            String Bo = new CallTypeHandleBO(UserUtils.getSite(),callType.getWorkshopName(),callType.getAndonType()).getBo();
            callType.setId(Bo);
            callType.setCreateUser(userUtil.getUser().getUserName());
            callTypeMapper.insert(callType);
        } else {
            callTypeMapper.updateById(callType);
        }
    }

    @Override
    public void saveGradePush(List<GradePush> gradePush) {
        String site = UserUtils.getSite();
        CallType callType = callTypeMapper.selectById(gradePush.get(0).getCallTypeID());
        for (GradePush push : gradePush) {
            push.setBO(callType.getId());
            push.setSite(site);
            gradePushMapper.insert(push);
        }
    }

    @Override
    public IPage<CallType> queryPage(CallTypeDTO callTypeDTO) {
        QueryWrapper<CallType> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isEmpty(callTypeDTO.getPage())){
            callTypeDTO.setPage(new Page(1,10));
        }
        if (StrUtil.isNotEmpty(callTypeDTO.getAndonTypeBo())){
            queryWrapper.eq("andon_type",callTypeDTO.getAndonTypeBo());
        }
        if (StrUtil.isNotEmpty(callTypeDTO.getWorkShopBo())){
            queryWrapper.eq("workshop_bo",callTypeDTO.getWorkShopBo());
        }
        if (StrUtil.isNotEmpty(callTypeDTO.getAndonTypeName())){
            queryWrapper.like("andon_type_name",callTypeDTO.getAndonTypeName());
        }
        if (StrUtil.isNotEmpty(callTypeDTO.getWorkshopName())){
            queryWrapper.like("workshop_name",callTypeDTO.getWorkshopName());

        }
        return callTypeMapper.selectPage(callTypeDTO.getPage(),queryWrapper);
    }
}
