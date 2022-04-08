package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.SiteUsr;
import com.itl.mes.core.api.vo.SiteUsrRequestVo;
import com.itl.mes.core.api.vo.SiteUsrResponseVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工厂用户关系维护 服务类
 * </p>
 *
 * @author space
 * @since 2019-07-17
 */
public interface SiteUsrService extends IService<SiteUsr> {

    List<SiteUsr> selectList();

    /**
     * 查询用户
     *
     * @param usr 用户名
     * @return Map<String,Object>
     */
    Map<String,Object> getUsr(String usr);

    /**
     * 获取存在的用户
     *
     * @param usr 用户名
     * @return  Map<String,Object>
     * @throws CommonException 异常
     */
    Map<String,Object> getExistUsr(String usr) throws CommonException;

    /**
     * 用户查询工厂数据
     *
     * @param usrOrCardNumber 用户
     * @return SiteUsrResponseVo
     * @throws CommonException 异常
     */
    SiteUsrResponseVo getSiteUsrByUsr(String usrOrCardNumber,String type) throws CommonException;

    /**
     * 验证用户工厂关系是否维护
     *
     * @param usr 用户
     * @param site 工厂
     * @throws CommonException 异常
     */
    void validateUsrAndSite(String usr, String site) throws CommonException;

    /**
     *保存用户工厂关系
     *
     * @param siteUsrRequestVo siteUsrRequestVo
     * @throws CommonException 异常
     */
    void saveUsrSite(SiteUsrRequestVo siteUsrRequestVo) throws CommonException;

    /**
     * 删除用户工厂关系
     *
     * @param usr 用户
     * @param modifyDate 修改时间
     */
    void deleteUsrSite(String usr, Date modifyDate) throws CommonException;
}