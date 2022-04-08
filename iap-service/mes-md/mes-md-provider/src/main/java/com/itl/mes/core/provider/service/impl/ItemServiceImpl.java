package com.itl.mes.core.provider.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.api.vo.OpeartionItemVo;
import com.itl.mes.core.provider.mapper.ItemMapper;
import com.itl.mes.core.provider.mapper.RouterMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 物料表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@Service
@Transactional
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Autowired
    ItemService itemService;
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private ItemGroupService itemGroupService;

    @Autowired
    private ItemGroupMemberService itemGroupMemberService;

    //todo 去掉工艺路线相关逻辑
    //@Autowired
    //private RouterService routerService;

    @Autowired
    private BomService bomService;

    @Autowired
    private BomComponnetService bomComponnetService;

    @Autowired
    private WarehouseService warehouseService;

    @Resource
    private UserUtil userUtil;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private RouterMapper routerMapper;

    @Override
    public List<Item> selectList() {
        QueryWrapper<Item> entityWrapper = new QueryWrapper<Item>();
        //getEntityWrapper(entityWrapper, item);
        return super.list(entityWrapper);
    }


    //根据物料编号查询
    @Override
    public Item selectByItem( String item ) throws CommonException {

        QueryWrapper<Item> entityWrapper = new QueryWrapper<Item>();
        entityWrapper.eq( Item.SITE, UserUtils.getSite() ).eq( Item.ITEM, item ).eq( Item.IS_CURRENT_VERSION, "Y" );

        List<Item> items = super.list( entityWrapper );
        if(items.isEmpty()){
            throw new CommonException("物料编号"+item+"未维护或不是当前版本", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return items.get(0);
        }
    }

    //导出用 根据工厂 物料 查询当前版本 物料
    @Override
    public Item selectByItemAndSite(String item, String site) throws CommonException {
        QueryWrapper<Item> entityWrapper = new QueryWrapper<Item>();
        entityWrapper.eq( Item.SITE, site ).eq( Item.ITEM, item ).eq( Item.IS_CURRENT_VERSION, "Y" );

        List<Item> items = super.list( entityWrapper );
        if(items.isEmpty()){
            throw new CommonException("物料编号"+item+"未维护或不是当前版本", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return items.get(0);
        }
    }

    /**
     * 通过ItemHandleBO查询物料编号数据
     *
     * @param itemHandleBO itemHandleBO
     * @return Item
     * @throws CommonException 异常
     */
    private Item getSelectItemByItemHandleBO( ItemHandleBO itemHandleBO )throws CommonException{

        Item itemEntity = super.getById( itemHandleBO.getBo() );
        if( itemEntity==null ){
            throw new CommonException( "物料编号"+itemHandleBO.getItem()+"，版本"+itemHandleBO.getVersion()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        return itemEntity;
    }

    /**
     * 通过物料和版本查询物料相关数据
     *
     * @param item 物料
     * @param version 版本
     * @return ItemFullVo
     * @throws CommonException 异常
     */
    @Override
    public ItemFullVo getItemFullVoByItemAndVersion(String item, String version) throws CommonException{

        ItemHandleBO itemHandleBO = new ItemHandleBO( UserUtils.getSite(), item, version );
        //查询物料
        Item itemEntity = getSelectItemByItemHandleBO( itemHandleBO );

        ItemFullVo itemFullVo = new ItemFullVo();
        BeanUtils.copyProperties( itemEntity, itemFullVo );
        itemFullVo.setRouter(itemEntity.getRouterName());
        itemFullVo.setItemState( new StatusHandleBO( itemEntity.getItemStateBo() ).getState() );
        //设置自定义数据和值
        List<CustomDataAndValVo> customDataAndValVoList = customDataValService
                .selectCustomDataAndValByBoAndDataType( UserUtils.getSite(), itemHandleBO.getBo(), CustomDataTypeEnum.ITEM.getDataType() );
        itemFullVo.setCustomDataAndValVoList( customDataAndValVoList );
        //设置可分配物料组
        itemFullVo.setAvailableItemGroupList( itemGroupService.getAvailableItemGroupListByItemHandleBO( itemHandleBO ) );
        //设置已分配物料组
        itemFullVo.setAssignedItemGroupList( itemGroupService.getAssignedItemGroupListBySiteAndItemBO( itemHandleBO ) );

        if( !StrUtil.isBlank( itemEntity.getBomBo() ) ){
            BomHandleBO bomHandleBO = new BomHandleBO( itemEntity.getBomBo() );
            itemFullVo.setBom( bomHandleBO.getBom() );
            itemFullVo.setBomVersion( bomHandleBO.getVersion() );
        }
        if( !StrUtil.isBlank( itemEntity.getRouterBo() ) ){
            RouterHandleBO routerHandleBO = new RouterHandleBO( itemEntity.getRouterBo() );
            itemFullVo.setRouter( routerHandleBO.getRouter() );
            itemFullVo.setRouterVersion( routerHandleBO.getVersion() );
        }
        if( !StrUtil.isBlank( itemEntity.getWarehouseBo() ) ){
            itemFullVo.setWareHouse( new WarehouseHandleBO( itemEntity.getWarehouseBo() ).getWareHouse() );
        }
        return itemFullVo;
    }

    /**
     * 保存物料
     *
     * @param itemFullVo itemFullVo
     * @throws CommonException 异常
     */
    @Override
    @Transactional( rollbackFor = {Exception.class,RuntimeException.class} )
    public void saveItem( ItemFullVo itemFullVo ) throws CommonException {

        String site = UserUtils.getSite();

        ItemHandleBO itemHandleBO = new ItemHandleBO( UserUtils.getSite(), itemFullVo.getItem(), itemFullVo.getVersion() );
        Item itemEntity = super.getById( itemHandleBO.getBo() );
        if( itemEntity==null ){ //新增物料

            insertItem( itemFullVo );
        }else{ //更新物料

            updateItem( itemEntity, itemFullVo );
        }

        //保存物料和物料组关系
        if( itemFullVo.getAssignedItemGroupList()!=null ){
            itemGroupMemberService.saveItemAndItemGroups( site,itemHandleBO.getBo(),itemFullVo.getAssignedItemGroupList() );
        }

        //保存自定义数据
        if( itemFullVo.getCustomDataValVoList()!=null ){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( itemHandleBO.getBo() );
            customDataValRequest.setSite( site );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.ITEM.getDataType());
            customDataValRequest.setCustomDataValVoList( itemFullVo.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }

    }

    @Override
    public List<Item> selectItem(String item, String itemName, String itemDesc, String version) {
        QueryWrapper<Item> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(Item.SITE,UserUtils.getSite());
        entityWrapper.like(Item.ITEM,item);
        entityWrapper.like(Item.ITEM_NAME,itemName);
        entityWrapper.like(Item.ITEM_DESC,itemDesc);
        entityWrapper.like(Item.VERSION,version);
        return itemMapper.selectList(entityWrapper);
    }


    /**
     * 新增物料
     *
     * @param itemFullVo itemFullVo
     * @throws QueryWrapper 异常
     */
    private void insertItem( ItemFullVo itemFullVo ) throws CommonException{

        String site = UserUtils.getSite();
        //保存物料
        Item item = new Item();
        ItemHandleBO itemHandleBO = new ItemHandleBO( UserUtils.getSite(), itemFullVo.getItem(), itemFullVo.getVersion() );
        item.setBo( itemHandleBO.getBo() );
        item.setSite( itemHandleBO.getSite() );
        item.setItem( itemFullVo.getItem() );
        item.setLotSize( itemFullVo.getLotSize() );
        item.setVersion( itemFullVo.getVersion() );
        item.setIsCurrentVersion( itemFullVo.getIsCurrentVersion() );
        item.setItemName( itemFullVo.getItemName() );
        item.setItemDesc( itemFullVo.getItemDesc() );
        /*if( !StrUtil.isBlank( itemFullVo.getRouter() ) && !StrUtil.isBlank( itemFullVo.getRouterVersion() ) ){
            item.setRouterBo( routerService.getExistRouterByHandleBO( new RouterHandleBO( item.getSite(), itemFullVo.getRouter(),
                    itemFullVo.getRouterVersion() ) ).getBo() );
        }else{
            item.setRouterBo( "" );
        }*/
        // 存工艺路线BO
        if( !StrUtil.isBlank( itemFullVo.getRouter() ) && !StrUtil.isBlank( itemFullVo.getRouterVersion() ) ){
            Router router=routerMapper.selectOne(new QueryWrapper<Router>().eq("ROUTER",itemFullVo.getRouter()).eq("VERSION",itemFullVo.getRouterVersion()));
            if(router !=null){
                item.setRouterBo(router.getBo());
            }
        }
        item.setRouterName( itemFullVo.getRouter() );
        item.setRouterVersion( itemFullVo.getRouterVersion() );
        item.setDrawingNo(itemFullVo.getDrawingNo());
        item.setItemStateBo( new StatusHandleBO( site,itemFullVo.getItemState() ).getBo() );
        item.setItemType( itemFullVo.getItemType() );
        item.setItemUnit( StrUtil.isBlank( itemFullVo.getItemUnit() )?"":itemFullVo.getItemUnit() );
        if( !StrUtil.isBlank( itemFullVo.getBom() ) && !StrUtil.isBlank( itemFullVo.getBomVersion() ) ){
            item.setBomBo( bomService.getRouterByRouterAndVersion( site, itemFullVo.getBom(), itemFullVo.getBomVersion() ).getBo() );
        }else{
            item.setBomBo( "" );
        }
        if( !StrUtil.isBlank( itemFullVo.getWareHouse() )  ){
            Warehouse warehouse = warehouseService.selectWarehouse( itemFullVo.getWareHouse() );
            item.setWarehouseBo( warehouse.getBo() );
        }
        item.setObjectSetBasicAttribute( userUtil.getUser().getUserName(), new Date() );
        validateItem( item ); //验证物料数据格式是否合规
        super.save( item );
        if( "Y".equalsIgnoreCase( itemFullVo.getIsCurrentVersion() ) ){
            setOtherItemNotCurrentVersion( itemHandleBO.getBo() );
        }

    }

    /**
     * 更新物料
     *
     * @param item 物料
     * @param itemFullVo itemFullVo
     * @throws CommonException 异常
     */
    private void updateItem( Item item, ItemFullVo itemFullVo ) throws CommonException {

//        Date frontModifyDate = itemFullVo.getModifyDate(); //前台传递的时间值
//        CommonUtil.compareDateSame( frontModifyDate, item.getModifyDate() ); //比较时间是否相等

        Date newModifyDate = new Date();
        Item itemEntity = new Item();
        ItemHandleBO itemHandleBO = new ItemHandleBO( UserUtils.getSite(), itemFullVo.getItem(), itemFullVo.getVersion() );
        itemEntity.setBo( itemHandleBO.getBo() );
        itemEntity.setLotSize( itemFullVo.getLotSize() );
        itemEntity.setItemName( itemFullVo.getItemName() );
        itemEntity.setItemDesc( StrUtil.isBlank( itemFullVo.getItemDesc() )?"":itemFullVo.getItemDesc() );
        /*if( !StrUtil.isBlank( itemFullVo.getRouter() ) && !StrUtil.isBlank( itemFullVo.getRouterVersion() ) ){
            itemEntity.setRouterBo( routerService.getExistRouterByHandleBO( new RouterHandleBO( item.getSite(), itemFullVo.getRouter(),
                    itemFullVo.getRouterVersion() ) ).getBo() );
        }else{
            itemEntity.setRouterBo( "" );
        }*/
        // 存工艺路线BO
        if(!StrUtil.isBlank(itemFullVo.getRouter() ) && !StrUtil.isBlank(itemFullVo.getRouterVersion())){
            Router router=routerMapper.selectOne(new QueryWrapper<Router>().eq("ROUTER",itemFullVo.getRouter()).eq("VERSION",itemFullVo.getRouterVersion()));
            if(router !=null){
                itemEntity.setRouterBo(router.getBo());
            }
            itemEntity.setRouterName( itemFullVo.getRouter());
            itemEntity.setRouterVersion( itemFullVo.getRouterVersion());
        }else {
            itemEntity.setRouterName("");
            itemEntity.setRouterVersion("");
            itemEntity.setRouterBo("");
        }
        itemEntity.setIsCurrentVersion( itemFullVo.getIsCurrentVersion() );
        itemEntity.setItemStateBo( new StatusHandleBO( item.getSite(),itemFullVo.getItemState() ).getBo() );
        itemEntity.setItemType( itemFullVo.getItemType() );
        itemEntity.setItemUnit( StrUtil.isBlank( itemFullVo.getItemUnit() )?"":itemFullVo.getItemUnit() );
        if( !StrUtil.isBlank( itemFullVo.getBom() ) && !StrUtil.isBlank( itemFullVo.getBomVersion() ) ){
            itemEntity.setBomBo( bomService.getRouterByRouterAndVersion( item.getSite(), itemFullVo.getBom(), itemFullVo.getBomVersion() ).getBo() );
        }else{
            itemEntity.setBomBo( "" );
        }
        if( !StrUtil.isBlank( itemFullVo.getWareHouse() )  ){
            itemEntity.setWarehouseBo( warehouseService.selectWarehouse( itemFullVo.getWareHouse() ).getBo() );
        }else{
            itemEntity.setWarehouseBo( "" );
        }
        itemEntity.setModifyDate( newModifyDate );
        itemEntity.setModifyUser( userUtil.getUser().getUserName() );
        itemEntity.setDrawingNo(itemFullVo.getDrawingNo());
        //验证物料数据格式是否合规
        ValidationUtil.ValidResult validResult =
                ValidationUtil.validateProperties( itemEntity,"itemName","itemDesc","itemUnit","itemStateBo","itemType" );
        if( validResult.hasErrors() ){
            throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        boolean successFlag = super.updateById( itemEntity );
        if( !successFlag ){
            throw new CommonException("物料编号"+item.getItem()+"，版本"+item.getVersion()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if( "Y".equalsIgnoreCase( itemFullVo.getIsCurrentVersion() ) ){
            setOtherItemNotCurrentVersion( itemHandleBO.getBo() );
        }

    }

    /**
     * 验证物料数据是否合规
     *
     * @param item 物料
     * @throws CommonException 异常
     */
    private void validateItem( Item item ) throws CommonException{

        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean( item ); //验证物料数据是否合规
        if( validResult.hasErrors() ){
            throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION );
        }

    }

    /**
     * 设置其它版本物料为非当前版本
     *
     * @param itemBo itemBo
     */
    private void setOtherItemNotCurrentVersion( String itemBo ){

        ItemHandleBO itemHandleBO = new ItemHandleBO( itemBo );
        Item item = new Item();
        item.setIsCurrentVersion( "N" );
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq( Item.SITE, itemHandleBO.getSite() ).eq( Item.ITEM, itemHandleBO.getItem() )
                .ne( Item.VERSION, itemHandleBO.getVersion() ).eq( Item.IS_CURRENT_VERSION, "Y" );
        super.update( item, wrapper );

    }

    /**
     * 验证物料是否被使用 true/false
     *
     * @param itemHandleBO itemHandleBO
     * @return boolean
     */
    private boolean validateItemIsUsed( ItemHandleBO itemHandleBO ){

        //物料组是否引用
        QueryWrapper<ItemGroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq( ItemGroupMember.ITEM_BO, itemHandleBO.getBo() );
        if( itemGroupMemberService.list( wrapper ).size()>0 ) return true;

        //BOM是否引用
        QueryWrapper<BomComponnet> bomComponnetWrapper = new QueryWrapper<>();
        bomComponnetWrapper.eq( BomComponnet.COMPONENT_BO,itemHandleBO.getBo() );
        if( bomComponnetService.list( bomComponnetWrapper ).size()>0 ) return true;

        //todo 去掉工艺路线相关逻辑
        //工单是否使用
        QueryWrapper<ShopOrder> shopOrderWrapper = new QueryWrapper<>();
        shopOrderWrapper.eq( ShopOrder.ITEM_BO, itemHandleBO.getBo() );
        if( shopOrderService.list( shopOrderWrapper ).size()>0 ) return true ;

        //工艺路线是否使用
        //Wrapper<Router> routerWrapper = new EntityWrapper<>();
        //routerWrapper.eq( Router.ITEM_BO, itemHandleBO.getBo() );
        //if( routerService.selectList( routerWrapper ).size()>0) return true;

        //等等

        return false;
    }

    /**
     * 删除物料
     *
     * @param item 物料
     * @param version 版本
     * @throws CommonException 异常
     */
    @Override
    public void deleteItem(String item, String version) throws CommonException {

        ItemHandleBO itemHandleBO = new ItemHandleBO( UserUtils.getSite(),item, version );
        Item itemEntity = getSelectItemByItemHandleBO( itemHandleBO );
        //验证物料是否已被使用
        if( validateItemIsUsed( itemHandleBO ) ){
            throw new CommonException( "物料"+item+"，版本"+version +"已被使用，不能删除", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        QueryWrapper<Item> delWrapper = new QueryWrapper<>();
        delWrapper.eq( Item.BO, itemHandleBO.getBo() );

        //删除物料数据
        Integer integer = itemMapper.delete( delWrapper );
        if( integer==0 ){
            throw new CommonException( "数据已修改，请查询后再执行删除操作", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        //删除物料自定义数据值
        customDataValService.deleteCustomDataValByBoAndType( itemHandleBO.getSite(), itemHandleBO.getBo(), CustomDataTypeEnum.ITEM );

    }

    /**
     * 查询物料数据
     *
     * @param itemHandleBO itemHandleBO
     * @return Item
     */
    @Override
    public Item getItemByItemHandleBO(ItemHandleBO itemHandleBO) {
        return super.getById( itemHandleBO.getBo() );
    }

    /**
     * 查询存在的物料数据，不存在则报错
     *
     * @param itemHandleBO itemHandleBO
     * @return Item
     * @throws CommonException 异常
     */
    @Override
    public Item getExitsItemByItemHandleBO(ItemHandleBO itemHandleBO) throws CommonException {
        Item item = getItemByItemHandleBO( itemHandleBO );
        if ( item ==null ){
            throw new CommonException( "物料"+itemHandleBO.getItem()+"，版本"+itemHandleBO.getVersion()+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        return item;
    }

    /**
     * 通过物料更改物料ItemHandleBO，更新物料对应的工艺路线
     *
     * @param itemHandleBO itemHandleBO
     * @param routerHandleBO routerHandleBO
     */
    @Override
    public void updateItemRouterByItemBO( ItemHandleBO itemHandleBO, RouterHandleBO routerHandleBO ){

        Item itemEntity = new Item();
        itemEntity.setBo( itemHandleBO.getBo() );
        itemEntity.setRouterBo( routerHandleBO.getBo() );
        itemMapper.updateById( itemEntity );

    }

    @Override
    public List<Item> getItemByBomBO(String bomBO) {
        QueryWrapper<Item> entityWrapper = new QueryWrapper<Item>();
        entityWrapper.eq(Item.SITE,UserUtils.getSite());
        entityWrapper.eq(Item.BOM_BO,bomBO);
        List<Item> items = itemMapper.selectList(entityWrapper);
        return items;
    }


    //导出
    @Override
    public void exportItem(String site, HttpServletResponse response) throws CommonException {
        export(site,response);
    }

    public void export(String site, HttpServletResponse response) {

        Workbook workBook = null;
        try {
            //获取物料数据源
            QueryWrapper<Item> itemEntityWrapper = new QueryWrapper<>();
            itemEntityWrapper.eq(Item.SITE, site);
            List<Item> items = itemMapper.selectList(itemEntityWrapper);
            // 创建参数对象（用来设定excel得sheet得内容等信息）
            ExportParams itemExportParams = new ExportParams();
            // 设置sheet得名称
            itemExportParams.setSheetName("物料");
            // 创建sheet1使用得map
            Map<String, Object> itemExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            itemExportMap.put("title", itemExportParams);
            // 模版导出对应得实体类型
            itemExportMap.put("entity", Item.class);
            // sheet中要填充得数据
            itemExportMap.put("data", items);

            //获取item_group数据源
            QueryWrapper<ItemGroup> groupEntityWrapper = new QueryWrapper<>();
            groupEntityWrapper.eq(ItemGroup.SITE, site);
            List<ItemGroup> itemGroups = itemGroupService.list(groupEntityWrapper);
            ExportParams itemGroupExportParams = new ExportParams();
            itemGroupExportParams.setSheetName("物料组");
            // 创建sheet2使用得map
            Map<String, Object> itemGroupExportMap = new HashMap<>();
            itemGroupExportMap.put("title", itemGroupExportParams);
            itemGroupExportMap.put("entity", ItemGroup.class);
            itemGroupExportMap.put("data", itemGroups);

            //获取item_group_member数据源
            List<ItemGroupMember> itemGroupMembers = itemGroupMemberService.selectList();
            ExportParams itemGroupMemberExportParams = new ExportParams();
            itemGroupMemberExportParams.setSheetName("物料组成员表");
            // 创建sheet3使用得map
            Map<String, Object> itemGroupMemberExportMap = new HashMap<>();
            itemGroupMemberExportMap.put("title", itemGroupMemberExportParams);
            itemGroupMemberExportMap.put("entity", ItemGroupMember.class);
            itemGroupMemberExportMap.put("data", itemGroupMembers);

            //获取customDataVal数据源
            List<CustomDataVal> customDataVals = customDataValService.selectList();
            ExportParams customDataValExportParams = new ExportParams();
            customDataValExportParams.setSheetName("自定义数据的值");
            // 创建sheet4使用得map
            Map<String, Object> customDataValExportMap = new HashMap<>();
            customDataValExportMap.put("title", customDataValExportParams);
            customDataValExportMap.put("entity", CustomDataVal.class);
            customDataValExportMap.put("data", customDataVals);

            // 将sheet1、sheet2、sheet3使用得map进行包装
            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(itemExportMap);
            sheetsList.add(itemGroupExportMap);
            sheetsList.add(itemGroupMemberExportMap);
            sheetsList.add(customDataValExportMap);

            // 执行方法
            workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("物料表.xls", "UTF-8") );
            workBook.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新物料的BOM，如果物料不存在，则不更新
     * 如果BOM不存在也不更新，不报错，只会返回是否成功 true/false
     *
     * @param itemHandleBO 物料
     * @param bomHandleBO 工单
     * @return boolean
     * @throws CommonException 异常
     */
    @Override
    public boolean updateOrSaveItemBom(ItemHandleBO itemHandleBO, BomHandleBO bomHandleBO) {

        Item item = getItemByItemHandleBO( itemHandleBO );
        if( item==null ){
            return false;
        }
        Bom bom = bomService.getBomByHandleBO( bomHandleBO );
        if ( bom==null ){
            return false;
        }
        Item itemEntity = new Item();
        itemEntity.setBomBo( bomHandleBO.getBo() );
        itemEntity.setModifyDate( new Date() );
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq( Item.BO, itemHandleBO.getBo() );
        return  super.update( itemEntity, wrapper );
    }

    @Override
    public Map<String, Object> getParams(ItemForParamQueryDto queryDto) {
        String site = queryDto.getSite();
        Map<String, Object> ret = new HashMap<>();
        List<String> columns = queryDto.getColumns();
        String isCustom = queryDto.getIsCustom();
        String itemBo = queryDto.getItemBo();

        if ("Y".equals(isCustom)) { //自定义字段
            List<CustomDataAndValVo> customs = customDataValService.selectCustomDataAndValByBoAndDataType(site, itemBo, CustomDataTypeEnum.ITEM.getDataType());
            customs.forEach(custom -> {
                if (columns.contains(custom.getAttribute())) {
                    ret.put(custom.getAttribute(), custom.getVals());
                }
            });
        } else if ("N".equals(isCustom)) { //主字段
            Map<String, Object> item = BeanUtil.beanToMap(getById(itemBo));
            columns.forEach(column -> {
                Object o = item.get(lineToHump(column.toLowerCase()));
                if (ObjectUtil.isNotNull(o)) {
                    ret.put(column, o);
                }
            });
        }

        return ret;
    }

    @Override
    public List<Item> selectByItemBack(String item) throws CommonException {
        QueryWrapper<Item> entityWrapper = new QueryWrapper<Item>();
        entityWrapper.eq( Item.ITEM, item );

        List<Item> items = super.list( entityWrapper );
        return items;
    }

    @Override
    public OpeartionItemVo selectOpeartionOrderItem(String operationOrder) throws CommonException {
        Item item=itemMapper.selectOperationOrderItem(operationOrder);
        OpeartionItemVo opeartionItemVo=new OpeartionItemVo();
        if(item != null){
            opeartionItemVo.setItem(item.getItem());
            opeartionItemVo.setItemName(item.getItemName());
            opeartionItemVo.setItemDesc(item.getItemDesc());
            opeartionItemVo.setOpeartionOrderQty(item.getOperationOrderQty());

        }
        opeartionItemVo.setOperationOrder(operationOrder);
        return opeartionItemVo;
    }

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰,正常输出
     * @param str
     * @return
     */
    private static String lineToHump(String str) {
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


}
