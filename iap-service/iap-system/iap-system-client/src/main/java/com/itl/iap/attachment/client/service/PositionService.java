package com.itl.iap.attachment.client.service;

import com.itl.iap.attachment.client.config.FallBackConfig;
import com.itl.iap.attachment.client.service.impl.PositionServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 岗位服务远程调用
 *
 * @author 黄建明
 * @date 2020-7-03
 * @since jdk 1.8
 */
@FeignClient(value = "iap-system-provider", fallback = PositionServiceImpl.class, configuration = FallBackConfig.class)
public interface PositionService {

    @GetMapping("/iapPositionT/queryUserListByPositionId/{positionId}")
    @ApiOperation(value = "查询职位的用户", notes = "通过职位对象查询用户列表")
    public ResponseData<List<IapSysUserTDto>> queryUserById(@PathVariable("positionId") String positionId);

}
