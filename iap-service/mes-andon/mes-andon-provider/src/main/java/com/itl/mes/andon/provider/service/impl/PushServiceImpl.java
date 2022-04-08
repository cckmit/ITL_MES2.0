package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.bo.PushHandleBO;
import com.itl.mes.andon.api.entity.Grade;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.api.entity.Push;
import com.itl.mes.andon.api.service.PushService;
import com.itl.mes.andon.provider.mapper.GradeMapper;
import com.itl.mes.andon.provider.mapper.GradePushMapper;
import com.itl.mes.andon.provider.mapper.PushMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("pushService")
public class PushServiceImpl extends ServiceImpl<PushMapper, Push> implements PushService {
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private GradePushMapper gradePushMapper;
    @Autowired
    private GradeMapper gradeMapper;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void savePush(Push push){

        /*Push push1 =
                pushMapper.selectById(new PushHandleBO(UserUtils.getSite(), push.getAndonPush()).getBo());

        push.setBo(new PushHandleBO(UserUtils.getSite(), push.getAndonPush()).getBo());
        push.setModifyUser(userUtil.getUser().getUserName());
        push.setModifyDate(new Date());
        if (push1 == null) {
            push.setCreateUser(userUtil.getUser().getUserName());
            push.setSite(UserUtils.getSite());
            pushMapper.insert(push);
        } else {
            pushMapper.updateById(push);
        }
        if (CollectionUtil.isNotEmpty(push.getGrades())) {

            gradePushMapper.delete(
                    new QueryWrapper<GradePush>().lambda().eqGradePush::getAndonPushBo, push.getBo()));
            List<GradePush> gradePushes = push.getGrades();
            gradePushes.forEach(x -> {
                x.setSite(UserUtils.getSite());
                x.setAndonPushBo(new PushHandleBO(UserUtils.getSite(), push.getAndonPush()).getBo());
                gradePushMapper.insert(x);
                Grade grade = gradeMapper.selectOne(new QueryWrapper<Grade>().eq("BO",x.getAndonGradeBo()));
                if (!StringUtils.equals(grade.getState(), x.getState())) {
                    grade.setState(x.getState());
                    gradeMapper.updateById(grade);
                }
            });
        }*/
    }

    @Override
    public Push getByBo(String bo) {
        return pushMapper.getByBo(bo);
    }
}
