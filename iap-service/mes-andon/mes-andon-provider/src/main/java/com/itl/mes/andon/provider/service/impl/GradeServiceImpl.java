package com.itl.mes.andon.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.bo.GradeHandleBO;
import com.itl.mes.andon.api.bo.GradePushHandleBO;
import com.itl.mes.andon.api.entity.Grade;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.api.service.GradeService;
import com.itl.mes.andon.provider.mapper.GradeMapper;
import com.itl.mes.andon.provider.mapper.GradePushMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("gradeService")
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {

  @Autowired GradeMapper gradeMapper;
  @Autowired private UserUtil userUtil;
  @Autowired
  private GradePushMapper gradePushMapper;

  @Override
  @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
  public void saveGrade(Grade grade) throws CommonException {

    Grade grade1 =
        gradeMapper.selectById(grade.getId());
    grade.setBo(new GradeHandleBO(UserUtils.getSite(), grade.getAndonGrade()).getBo());
    grade.setModifyUser(userUtil.getUser().getUserName());
    grade.setModifyDate(new Date());
    if (grade1 == null) {
      grade.setSite(UserUtils.getSite());
      grade.setCreateUser(userUtil.getUser().getUserName());
      gradeMapper.insert(grade);
    } else {
      gradeMapper.updateById(grade);
    }
  }

  @Override
  public void saveGradePush(List<GradePush> gradePush) {
    String site = UserUtils.getSite();
    Grade grade = gradeMapper.selectById(gradePush.get(0).getAndonGradeID());
    for (GradePush push : gradePush) {
      String BO = new GradePushHandleBO(site,"GRADE",grade.getAndonGrade()).getBo();
      push.setBO(BO);
      push.setSite(site);
      gradePushMapper.insert(push);
    }
  }
}
