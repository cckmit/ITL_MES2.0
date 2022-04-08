package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.ItemGroup;
import com.itl.mes.core.api.service.ItemGroupService;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.vo.ItemGroupVo;
import com.itl.mes.core.api.vo.ItemNameDescVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author space
 * @since 2019-05-31
 */
@RestController
@RequestMapping("/itemGroups")
@Api(tags = " 物料组" )
public class ItemGroupController {
    private final Logger logger = LoggerFactory.getLogger(ItemGroupController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public ItemGroupService itemGroupService;

    @Autowired
    public ItemService itemService;

    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value="分页查询物料组数据")
    @ApiOperationSupport( params = @DynamicParameters(name = "ItemGroupRequestModel",properties = {
            @DynamicParameter( name="page", value = "页面，默认为1" ),
            @DynamicParameter( name="limit", value = "分页大小，默20，可不填" ),
            @DynamicParameter( name="orderByField", value = "排序属性，可不填" ),
            @DynamicParameter( name="isAsc", value = "排序方式，true/false，可不填" ),
            @DynamicParameter( name="itemGroup", value = "工厂，可不填" ),
            @DynamicParameter( name="groupDesc", value = "物理组描述，可不填" )
    }) )
    public ResponseData<IPage<ItemGroup>> page(@RequestBody Map<String, Object> params ){

        String site = UserUtils.getSite();
        QueryWrapper<ItemGroup> wrapper = new QueryWrapper<>();
        wrapper.eq( ItemGroup.SITE, site );
        if( !StrUtil.isBlank( params.getOrDefault( "itemGroup","" ).toString() ) ){
            wrapper.like( ItemGroup.ITEM_GROUP, params.get( "itemGroup" ).toString() );
        }
        if( !StrUtil.isBlank( params.getOrDefault( "groupDesc","" ).toString() ) ){
            wrapper.like( ItemGroup.GROUP_DESC, params.get( "groupDesc" ).toString() );
        }
        IPage<ItemGroup> page = itemGroupService.page(new QueryPage<>(params), wrapper);
        return ResponseData.success(page);

    }

    /**
    * 根据itemGroup查询
    *
    * @param itemGroup 物料组
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value="通过物料组精确查询数据")
    @ApiImplicitParam(name="itemGroup",value="物料组",dataType="string", paramType = "query")
    public ResponseData<ItemGroupVo> getItemGroupByGroup(String itemGroup ) throws CommonException {
        if( StrUtil.isBlank( itemGroup ) ){
            throw new CommonException( "物料组参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return ResponseData.success(itemGroupService.getItemGroupVoByGroup( itemGroup ));
    }

    /**
     * 保存物料组数据
     *
     * @param itemGroupVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value="保存物料组")
    public ResponseData<ItemGroupVo> saveItemGroup( @RequestBody ItemGroupVo itemGroupVo ) throws CommonException {
        if(StrUtil.isBlank( itemGroupVo.getItemGroup() ) ){
            throw new CommonException( "物料组不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        itemGroupService.saveItemGroup( itemGroupVo );
        itemGroupVo = itemGroupService.getItemGroupVoByGroup( itemGroupVo.getItemGroup() );
        return ResponseData.success(itemGroupVo);
    }

    @GetMapping( "/itemGroupItemsByItem" )
    @ApiOperation(value="搜索")
    @ApiImplicitParam(name="item",value="物料,模糊查询",dataType="string", paramType = "query" )
    public ResponseData<List<ItemNameDescVo>> itemGroupItemsByItem(String item ){

        List<ItemNameDescVo> itemNameDescVoList = new ArrayList<>();
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq( Item.SITE, UserUtils.getSite() );
        if ( !StrUtil.isBlank( item ) ){
            wrapper.likeRight( Item.ITEM, item );
        }
        wrapper.select(" TOP "+ CustomCommonConstants.EVERY_TIMES_SEARCH_MOST_SIZE + " *"  );
        wrapper.orderByDesc(Item.CREATE_DATE);
        //wrapper.setSqlSelect( " TOP "+ CustomCommonConstants.EVERY_TIMES_SEARCH_MOST_SIZE + " *"  );
        List<Item> itemList = itemService.list( wrapper );
        ItemNameDescVo itemNameDescVo = null;
        for( int i=0; i<itemList.size(); i++ ){
            itemNameDescVo = new ItemNameDescVo();
            BeanUtils.copyProperties( itemList.get( i ), itemNameDescVo );
            itemNameDescVoList.add( itemNameDescVo );
        }
        return ResponseData.success( itemNameDescVoList );

    }

    /**
     * 删除物料组
     *
     * @param itemGroup
     * @param modifyDate
     * @throws CommonException
     */
    @GetMapping( "/delete")
    @ApiOperation( value="删除物料组" )
    @ApiImplicitParams({
            @ApiImplicitParam(name="itemGroup",value="物料组",dataType="string", paramType = "query" ),
            @ApiImplicitParam(name="modifyDate",value="修改时间",dataType="string", paramType = "query" )
    })
    public ResponseData<String> deleteItemGroup( String itemGroup, String modifyDate )throws CommonException {
        itemGroupService.deleteItemGroup( itemGroup, DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS ) );
        return ResponseData.success( "删除成功" );
    }


}
