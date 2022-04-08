package com.itl.mes.core.api.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.vo.WorkShopVo;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 车间表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-23
 */
public interface WorkShopService extends IService<WorkShop> {

    List<WorkShop> selectList();

    /**
     * 验证车间是否存在
     *
     * @param site 工厂
     * @param workShop 车间
     * @return WorkShop
     * @throws CommonException
     */
    WorkShop validateWorkShopIsExist(String site, String workShop) throws CommonException;

    /**
     * 通过车间查询车间相关数据
     *
     * @param workShop 车间
     * @return WorkShopVo
     * @throws CommonException
     */
    WorkShopVo getWorkShopByWorkShop(String workShop) throws CommonException;

    /**
     * 车间数据保存
     *
     * @param workShopVo workShopVo
     * @throws CommonException
     */
    void saveWorkShop(WorkShopVo workShopVo) throws CommonException;

    /**
     * 删除车间数据
     *
     * @param workShop 车间
     * @param modifyDate 修改时间
     * @throws CommonException
     */
    void deleteWorkShop(String workShop, Date modifyDate) throws CommonException;

}