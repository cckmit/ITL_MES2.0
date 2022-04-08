package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.Attached;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzh
 * @since 2019-08-30
 */
public interface AttachedService extends IService<Attached> {

    List<Attached> selectList();
}