package com.itl.mes.andon.api.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.util.PageUtils;
import com.itl.mes.andon.api.entity.Grade;
import com.itl.mes.andon.api.entity.GradePush;

import java.util.List;
import java.util.Map;

/**
 * 安灯等级
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
public interface GradeService extends IService<Grade> {

    /**
     * 保存或更新
     *
     * @param grade
     * @return
     */
    void saveGrade(Grade grade) throws CommonException;

    void saveGradePush(List<GradePush> gradePush);

}

