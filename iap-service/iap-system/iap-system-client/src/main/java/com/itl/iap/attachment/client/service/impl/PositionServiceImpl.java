/**
 * Copyright (C):  Evergrande Group
 * FileName: PositionServiceImpl
 * Author:   huangjianming
 * Date:     2020-07-03 16:56
 * Description:
 */
package com.itl.iap.attachment.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.attachment.client.service.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色服务远程调用
 *
 * @author 黄建明
 * @date 2020-7-03
 * @since jdk 1.8
 */
@Slf4j
@Component
public class PositionServiceImpl implements PositionService {

    @Override
    public ResponseData<List<IapSysUserTDto>> queryUserById(String positionId) {
        log.error("sorry,position feign fallback positionId:{}", positionId);
        return null;
    }
}
