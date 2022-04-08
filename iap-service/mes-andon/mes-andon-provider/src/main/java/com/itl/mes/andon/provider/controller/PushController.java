package com.itl.mes.andon.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.api.entity.Push;
import com.itl.mes.andon.api.entity.Type;
import com.itl.mes.andon.api.service.PushService;
import com.itl.mes.andon.provider.mapper.GradePushMapper;
import com.itl.mes.andon.provider.mapper.TypeMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 安灯推送设置
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@RestController
@RequestMapping("/push")
@Api(tags = "安灯推送设置")
public class PushController {
    @Autowired
    private PushService pushService;
    @Autowired
    private GradePushMapper gradePushMapper;
    @Autowired
    private TypeMapper typeMapper;

    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询安灯等级信息")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "PushRequestModel",
                    properties = {
                            @DynamicParameter(name = "page", value = "页面，默认为1"),
                            @DynamicParameter(name = "limit", value = "分页大小，默20，可不填"),
                            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
                            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
                            @DynamicParameter(name = "andonPush", value = "安灯推送编号，可不填"),
                            @DynamicParameter(name = "andonPushName", value = "安灯推送名称，可不填"),
                            @DynamicParameter(name = "state", value = "是否启用，可不填")
                    }))
    public ResponseData<IPage<Push>> page(@RequestBody Map<String, Object> params) {

        QueryWrapper<Push> wrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank(params.getOrDefault("andonPush", "").toString())) {
            wrapper.like("ANDON_PUSH", params.get("andonPush").toString());
        }
        if (!StrUtil.isBlank(params.getOrDefault("andonPushName", "").toString())) {
            wrapper.like("ANDON_PUSH_NAME", params.get("andonPushName").toString());
        }
        if (!StrUtil.isBlank(params.getOrDefault("state", "").toString())) {
            wrapper.eq(
                    "STATE", params.get("state"));
        }
        params.put("orderByField", "CREATE_DATE");
        params.put("isAsc", false);
        IPage<Push> page = pushService.page(new QueryPage<>(params), wrapper);
        return ResponseData.success(page);
    }

    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/pageByState")
    @ApiOperation(value = "分页查询可用的安灯等级信息")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "PushRequestModel",
                    properties = {
                            @DynamicParameter(name = "page", value = "页面，默认为1"),
                            @DynamicParameter(name = "limit", value = "分页大小，默20，可不填"),
                            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
                            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
                            @DynamicParameter(name = "andonPush", value = "安灯推送编号，可不填"),
                            @DynamicParameter(name = "andonPushName", value = "安灯推送名称，可不填"),
                            @DynamicParameter(name = "state", value = "是否启用，可不填")
                    }))
    public ResponseData<IPage<Push>> page2(@RequestBody Map<String, Object> params) {

        QueryWrapper<Push> wrapper = new QueryWrapper<>();
        if (params.get("andonPush") != null) {
            wrapper.like("ANDON_PUSH", params.get("andonPush").toString());
        }
        if (params.get("andonPushName") != null) {
            wrapper.like("ANDON_PUSH_NAME", params.get("andonPushName").toString());
        }
        if (params.get("state") != null) {
            wrapper.eq(
                    "STATE", params.get("state"));
        }
        params.put("orderByField", "CREATE_DATE");
        params.put("isAsc", false);
        wrapper.lambda().eq(Push::getState, "1");
        IPage<Push> page = pushService.page(new QueryPage<>(params), wrapper);
        return ResponseData.success(page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "根据id查")
    @GetMapping("/{bo}")
    public ResponseData info(@PathVariable("bo") String bo) {
        Push push = pushService.getByBo(bo);
        return ResponseData.success(push);
    }

    /**
     * 保存或更新
     */
    @ApiOperation(value = "保存或更新")
    @PostMapping("/saveOrUpdate")
    public ResponseData saveOrUpdate(@RequestBody Push push) {

        pushService.savePush(push);
        return ResponseData.success();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public ResponseData delete(@RequestBody String[] bos) throws CommonException {
        /*try {
            for (String bo : bos) {
                gradePushMapper.delete(new QueryWrapper<GradePush>().lambda().eq(GradePush::getAndonPushBo, bo));
                typeMapper.delete(new QueryWrapper<Type>().lambda().eq(Type::getAndonPushBo, bo));
            }
            pushService.removeByIds(Arrays.asList(bos));

        } catch (Exception e) {
            throw new CommonException("本次修改的数据中有关联项,请先去删除关联项!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }*/

        return ResponseData.success();
    }
}
