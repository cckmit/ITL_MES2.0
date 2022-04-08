package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.BomHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.RouterHandleBO;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.api.vo.OpeartionItemVo;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物料表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-3
 */
public interface ItemService extends IService<Item> {

    List<Item> selectList();

    Item selectByItem(String item) throws CommonException;

    //导出用
    Item selectByItemAndSite(String item, String site) throws CommonException;

    /**
     * 通过物料和版本查询物料相关数据
     *
     * @param item
     * @param version
     * @return
     * @throws CommonException
     */
    ItemFullVo getItemFullVoByItemAndVersion(String item, String version) throws CommonException;

    /**
     * 保存物料
     *
     * @param itemFullVo
     * @throws CommonException
     */
    void saveItem(ItemFullVo itemFullVo) throws CommonException;

    List<Item> selectItem(String item, String itemName, String itemDesc, String version)throws CommonException;

    /**
     * 删除物料
     *
     * @param item
     * @param version
     * @throws CommonException
     */
    void deleteItem(String item, String version)throws CommonException;

    /**
     * 查询物料数据
     *
     * @param itemHandleBO
     * @return
     */
    Item getItemByItemHandleBO(ItemHandleBO itemHandleBO);

    /**
     * 查询存在的物料数据，不存在则报错
     *
     * @param itemHandleBO
     * @return
     * @throws CommonException
     */
    Item getExitsItemByItemHandleBO(ItemHandleBO itemHandleBO)throws CommonException;

    /**
     * 通过物料更改物料ItemHandleBO，更新物料对应的工艺路线
     *
     * @param itemHandleBO itemHandleBO
     * @param routerHandleBO routerHandleBO
     */
    void updateItemRouterByItemBO(ItemHandleBO itemHandleBO, RouterHandleBO routerHandleBO);


    /**
     * 查询物料数据
     *
     * @param bomBO
     * @return
     */
    List<Item> getItemByBomBO(String bomBO);

    void exportItem(String site, HttpServletResponse response) throws CommonException;

    /**
     * 更新物料的BOM，如果物料不存在，则不更新
     * 如果BOM不存在也不更新，不报错，只会返回是否成功 true/false
     *
     * @param itemHandleBO 物料
     * @param bomHandleBO 工单
     * @return boolean
     * @throws CommonException 异常
     */
    boolean updateOrSaveItemBom(ItemHandleBO itemHandleBO, BomHandleBO bomHandleBO) throws CommonException;

    /**
     * 根据字段和物料id获取各个字段的值
     * @param queryDto
     * @return
     */
    Map<String, Object> getParams(ItemForParamQueryDto queryDto);


    /**
     * 从erp同步时判断物料是否存在
     * @param item
     * @return
     */
    List<Item> selectByItemBack(String item) throws CommonException;

    OpeartionItemVo selectOpeartionOrderItem(String opeartionOrder) throws CommonException;

}
