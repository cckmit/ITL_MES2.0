package com.itl.mes.core.provider.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.ItemGroupService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.ItemFullVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author space
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/items")
@Api(tags = " 物料表" )
public class ItemController {
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public ItemService itemService;

    @Autowired
    public ItemGroupService itemGroupService;

    @Autowired
    public CustomDataValService customDataValService;

    @GetMapping("/selectItemGroup")
    @ApiOperation( value = "页面打开时触发，显示物料组数据" )
    public ResponseData<List<String>> selectItemGroupList() throws CommonException {
        return ResponseData.success( itemGroupService.selectItemGroupListBySite() );
    }

    /**
    * 根据item查询
    *
    * @param item 物料
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value="根据物料和版本查询物料数据")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "item", value = "物料", dataType = "string", paramType = "query" ),
            @ApiImplicitParam( name = "version", value = "版本", dataType = "string", paramType = "query" )
    })
    public ResponseData<ItemFullVo> getItemByItemAndVersion(String item, String version ) throws CommonException {
        if( StrUtil.isBlank( item ) || StrUtil.isBlank( version ) ){
            throw new CommonException( "物料和版本不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ItemFullVo itemFullVo = itemService.getItemFullVoByItemAndVersion( item,version );
        return ResponseData.success(itemFullVo);
    }


    /**
     * 保存物料
     *
     * @param itemFullVo 前端物料相关数据
     * @return RestResult<ItemFullVo>
     */
    @PostMapping("/save")
    @ApiOperation(value="保存物料数据")
    public ResponseData<ItemFullVo> saveItem( @RequestBody ItemFullVo itemFullVo ) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean( itemFullVo ); //验证数据是否合规
        if( validResult.hasErrors() ){
            throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        itemService.saveItem( itemFullVo );
        itemFullVo = itemService.getItemFullVoByItemAndVersion( itemFullVo.getItem(),itemFullVo.getVersion() );
        return ResponseData.success( itemFullVo );
    }

    @GetMapping("/delete")
    @ApiOperation(value="删除物料数据")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "item", value = "物料", dataType = "string", paramType = "query" ),
            @ApiImplicitParam( name = "version", value = "版本", dataType = "string", paramType = "query" ),
    })
    public ResponseData<String> deleteItem( String item, String version ) throws CommonException {
        if( StrUtil.isBlank( item ) || StrUtil.isBlank( version ) ){
            throw new CommonException( "物料和版本不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        itemService.deleteItem( item,version );
        return ResponseData.success( "success" );
    }




    @GetMapping("/export")
    @ApiOperation(value = "导出文件")
    public void exportItem(String site, HttpServletResponse response) throws CommonException {
        itemService.exportItem(site, response);
    }


    @RequestMapping("importExcel")
    @ApiOperation(value = "上传文件")
    public ResponseData<String> importExcel(@RequestParam("file") MultipartFile file) throws CommonException {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("FileName", file.getOriginalFilename());
        //POIUtils.checkFile(file);
        List<ItemFullVo> itemFullVoList = ExcelUtils.importExcel(file, 1, 1, ItemFullVo.class);
        for (ItemFullVo itemFullVo : itemFullVoList) {
            itemService.saveItem(itemFullVo);
        }
        return ResponseData.success("success");
    }

    /**
     * 根据字段和物料id获取各个字段的值
     * @param queryDto
     * @return
     */
    @PostMapping("/getParams")
    @ApiOperation("根据字段和物料id获取各个字段的值")
    public Map<String, Object> getParams(@RequestBody ItemForParamQueryDto queryDto) {
        return itemService.getParams(queryDto);
    }
}
