package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.entity.CustomDataType;
import com.itl.mes.core.api.service.CustomDataService;
import com.itl.mes.core.api.service.CustomDataTypeService;
import com.itl.mes.core.api.vo.CustomDataTypeVo;
import com.itl.mes.core.api.vo.CustomDataVo;
import com.itl.mes.core.api.vo.CustomFullDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author space
 * @since 2019-05-28
 */
@RestController
@RequestMapping("/customDatas")
@Api(tags = " 自定义数据维护")
public class CustomDataController {
    private final Logger logger = LoggerFactory.getLogger(CustomDataController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public CustomDataService customDataService;

    @Autowired
    public CustomDataTypeService customDataTypeService;


    @GetMapping("/getCustomDataTypeList")
    @ApiOperation(value = "查询自定义数据类别")
    public ResponseData<List<CustomDataTypeVo>> getCustomDataTypeList() {
        List<CustomDataTypeVo> customDataTypeVoList = new ArrayList<>();
        QueryWrapper<CustomDataType> wrapper = new QueryWrapper<>();
        wrapper.eq(CustomDataType.SITE, UserUtils.getSite()).orderByAsc(CustomDataType.CREATE_DATE);
        List<CustomDataType> customDataTypeList = customDataTypeService.list(wrapper);
        if (customDataTypeList.size() > 0) {
            CustomDataTypeVo customDataTypeVo = null;
            for (int i = 0; i < customDataTypeList.size(); i++) {
                customDataTypeVo = new CustomDataTypeVo();
                BeanUtils.copyProperties(customDataTypeList.get(i), customDataTypeVo);
                customDataTypeVoList.add(customDataTypeVo);
            }
        }
        return ResponseData.success(customDataTypeVoList);
    }


    /**
     * 根据customDataType查询
     *
     * @param customDataType 自定义数据类型
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "通过类型查询自定义数据")
    public ResponseData<List<CustomDataVo>> getCustomDataById(String customDataType) {

        return ResponseData.success(customDataService.selectCustomDataVoListByDataType(customDataType));

    }


    @PostMapping("/save")
    @ApiOperation(value = "自定义数据保存")
    public ResponseData<List<CustomDataVo>> saveCustomData(@RequestBody CustomFullDataVo customFullDataVo) throws CommonException {

        List<CustomDataVo> customDataVoList = new ArrayList<>();

        if (StrUtil.isBlank(customFullDataVo.getCustomDataType())) {
            throw new CommonException("数据类别不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (customFullDataVo.getCustomDataVoList() != null && customFullDataVo.getCustomDataVoList().size() == 0) {
            throw new CommonException("自定义数据明细内容不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ValidationUtil.ValidResult validResult = null;
        for (CustomDataVo customDataVo : customFullDataVo.getCustomDataVoList()) {
            validResult = ValidationUtil.validateBean(customDataVo); //验证数据是否合规
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        List<CustomDataVo> customDataVos = customFullDataVo.getCustomDataVoList();
        //判断序号和数据字段不能重复
        for (int i = 0; i < customDataVos.size(); i++) {
            for (int j = (i + 1); j < customDataVos.size(); j++) {
                if (customDataVos.get(i).getCdField().equals(customDataVos.get(j).getCdField())) {
                    throw new CommonException("数据字段" + customDataVos.get(i).getCdField() + "重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                if (customDataVos.get(i).getSequence().equals(customDataVos.get(j).getSequence())) {
                    throw new CommonException("序号" + customDataVos.get(i).getSequence().intValue() + "重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }
        }
        //数据字段转换为大写
        for (int i = 0; i < customDataVos.size(); i++) {
            customDataVos.get(i).setCdField(customDataVos.get(i).getCdField().toUpperCase());
        }
        customDataService.saveCustomData(customFullDataVo);
        customDataVoList = customDataService.selectCustomDataVoListByDataType(customFullDataVo.getCustomDataType());

        return ResponseData.success(customDataVoList);
    }


    /**
     * 删除自定义数据
     *
     * @param customDataType 自定义数据类型
     * @return RestResult<String>
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除自定义数据类型自定义数据")
    @ApiImplicitParam(name = "customDataType", value = "自定义数据类型", dataType = "string", paramType = "query")
    public ResponseData<String> deleteCustomData(String customDataType) throws CommonException {

        if (StrUtil.isBlank(customDataType)) {
            throw new CommonException("自定义数据类型不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        customDataService.deleteCustomData(customDataType);

        return  ResponseData.success("success");
    }


}