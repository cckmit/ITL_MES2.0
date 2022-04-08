package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.entity.SiteUsr;
import com.itl.mes.core.api.service.SiteService;
import com.itl.mes.core.api.service.SiteUsrService;
import com.itl.mes.core.api.vo.SiteUsrRequestVo;
import com.itl.mes.core.api.vo.SiteUsrResponseVo;
import com.itl.mes.core.api.vo.SiteVo;
import com.itl.mes.core.provider.mapper.SiteUsrMapper;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工厂用户关系维护 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-07-17
 */
@Service
@Transactional
public class SiteUsrServiceImpl extends ServiceImpl<SiteUsrMapper, SiteUsr> implements SiteUsrService {


    @Autowired
    private SiteUsrMapper siteUsrMapper;

    @Autowired
    private SiteService siteService;

    @Resource
    private UserUtil userUtil;

    @Override
    public List<SiteUsr> selectList() {
        QueryWrapper<SiteUsr> entityWrapper = new QueryWrapper<SiteUsr>();
        //getEntityWrapper(entityWrapper, siteUsr);
        return super.list(entityWrapper);
    }

    /**
     * 查询用户
     *
     * @param usr 用户名
     * @return Map<String,Object>
     */
    @Override
    public Map<String,Object> getUsr(String usr){
        return siteUsrMapper.getUsrInfoByUsr( usr );
    }

    /**
     * 获取存在的用户
     *
     * @param usr 用户名
     * @return  Map<String,Object>
     * @throws CommonException 异常
     */
    @Override
    public Map<String,Object> getExistUsr(String usr) throws CommonException {
        Map<String,Object> usrMap = getUsr( usr );
        if( usrMap==null ){
            throw new CommonException( "用户"+usr+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return usrMap;
    }

    /**
     * 用户查询工厂数据
     *
     * @param usr 用户
     * @return SiteUsrResponseVo
     * @throws CommonException 异常
     */
    @Override
    public SiteUsrResponseVo getSiteUsrByUsr(String usr, String type) throws CommonException {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (type.equals("0")){
            getExistUsr( usr );
            mapList = siteUsrMapper.getSiteUsrByUsr( usr );
        }
        else if (type.equals("1")){
            getExistUsrByCard( usr );
            mapList = siteUsrMapper.getSiteUsrByCard( usr );
        }

        if( mapList.size()==0 ){
            throw new CommonException( "用户工厂关系未维护", CommonExceptionDefinition.BASIC_EXCEPTION );
        }
        SiteUsrResponseVo siteUsrResponseVo = new SiteUsrResponseVo();
        siteUsrResponseVo.setUsr( usr );
        SiteVo siteVo = null;
        List<SiteVo> siteVoList = new ArrayList<>();
        for( Map<String, Object> map:mapList ){
            siteVo = new SiteVo();
            siteVo.setSite( map.getOrDefault( "SITE","").toString() );
            siteVo.setSiteDesc( map.getOrDefault( "SITE_DESC","").toString() );
            if( map.getOrDefault( "IS_DEFAULT_SITE","").toString().equals( "Y" ) ){
                siteUsrResponseVo.setDefaultSites( map.getOrDefault( "SITE","").toString() );
            }
            if( siteUsrResponseVo.getModifyDate()==null && map.get( "MODIFY_DATE")!=null ){
                siteUsrResponseVo.setModifyDate( (Date) map.get( "MODIFY_DATE" ) );
            }
            siteVoList.add( siteVo );
        }
        siteUsrResponseVo.setSiteVoList( siteVoList );
        return siteUsrResponseVo;
    }

    private Map<String,Object> getExistUsrByCard(String usr) throws CommonException{
        Map<String,Object> usrMap = getUsrByCard( usr );
        if( usrMap==null ){
            throw new CommonException( "用户"+usr+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return usrMap;
    }

    private Map<String, Object> getUsrByCard(String usr) {
        return siteUsrMapper.getUsrInfoByUsrByCard( usr );
    }


    /**
     * 验证用户工厂关系是否维护
     *
     * @param usr 用户
     * @param site 工厂
     * @throws CommonException 异常
     */
    @Override
    public void validateUsrAndSite( String usr, String site ) throws CommonException {

        SiteUsrResponseVo siteUsrResponseVo = getSiteUsrByUsr(  usr,null );
        List<SiteVo> siteVoList = siteUsrResponseVo.getSiteVoList();
        boolean existFlag = false;
        for( SiteVo siteVo:siteVoList ){
            if( siteVo.getSite()!=null && siteVo.getSite().equals( site ) ){
                existFlag = true;
                break;
            }
        }
        if( !existFlag ){
            throw new CommonException( "用户"+usr+ "工厂"+site+"关系未维护",CommonExceptionDefinition.BASIC_EXCEPTION );
        }
    }

    /**
     *保存用户工厂关系
     *
     * @param siteUsrRequestVo siteUsrRequestVo
     * @throws CommonException 异常
     */
    @Override
    @Transactional( rollbackFor = {Exception.class,RuntimeException.class})
    public void saveUsrSite( SiteUsrRequestVo siteUsrRequestVo ) throws CommonException {

        getExistUsr( siteUsrRequestVo.getUsr() );
        QueryWrapper<SiteUsr> wrapper = new QueryWrapper<>();
        wrapper.eq( SiteUsr.USR, siteUsrRequestVo.getUsr() );
        List<SiteUsr> siteUsrList = siteUsrMapper.selectList( wrapper );
        if( siteUsrList.size()==0 ){ //新增
            insertSiteUsr( siteUsrRequestVo );
        }else{ //更新
            updateSiteUsr( siteUsrList.get( 0 ), siteUsrRequestVo );
        }


    }

    //新增
    private void insertSiteUsr(SiteUsrRequestVo siteUsrRequestVo) throws CommonException{

        List<SiteUsr> siteUsrList = new ArrayList<>();
        List<String> sitesList = siteUsrRequestVo.getSiteList();
        SiteUsr siteUsr = null;
        boolean sitesListContainsFlag = false; //工厂列表是否包含默认工厂标识
        Date createDate = new Date();
        for( String sites:sitesList ){
            siteService.getExistSiteBySite( sites ); //验证工厂是否存在
            siteUsr = new SiteUsr();
            siteUsr.setSite( sites );
            siteUsr.setUsr( siteUsrRequestVo.getUsr() );
            siteUsr.setObjectSetBasicAttribute( userUtil.getUser().getUserName(),createDate );
            if( sites.equals( siteUsrRequestVo.getDefaultSites()) ){
                siteUsr.setIsDefaultSite( "Y" );
                sitesListContainsFlag = true;
            }else{
                siteUsr.setIsDefaultSite( "N" );
            }
            ValidationUtil.validateBeanData( siteUsr ); //验证数据格式规范
            siteUsrList.add( siteUsr );
        }
        if( !sitesListContainsFlag ){
            siteService.getExistSiteBySite( siteUsrRequestVo.getDefaultSites() ); //验证工厂是否存在
            siteUsr = new SiteUsr();
            siteUsr.setSite( siteUsrRequestVo.getDefaultSites() );
            siteUsr.setUsr( siteUsrRequestVo.getUsr() );
            siteUsr.setIsDefaultSite( "Y" );
            siteUsr.setObjectSetBasicAttribute( userUtil.getUser().getUserName(),createDate );
            ValidationUtil.validateBeanData( siteUsr ); //验证数据格式规范
            siteUsrList.add( siteUsr );
        }
        super.saveBatch( siteUsrList );
    }

    //更新
    private void updateSiteUsr(SiteUsr siteUsr, SiteUsrRequestVo siteUsrRequestVo) throws CommonException {

       // CommonUtil.compareDateSame( siteUsrRequestVo.getModifyDate(),siteUsr.getModifyDate() );
        if (siteUsrRequestVo.getModifyDate() == null) {
            throw new CommonException("前台传送的时间为空!", CommonExceptionDefinition.TIME_VERIFY_EXCEPTION);
        }
        //删除已存在用户工厂的关系
        QueryWrapper<SiteUsr> wrapper = new QueryWrapper<>();
        wrapper.eq( SiteUsr.USR,siteUsrRequestVo.getUsr() );
        Integer delInt = siteUsrMapper.delete( wrapper );
        if( delInt<0 ){
            throw new CommonException( "数据已修改，请查询后再执行保存操作", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        insertSiteUsr( siteUsrRequestVo );

    }

    /**
     * 删除用户工厂关系
     *
     * @param usr 用户
     * @param modifyDate 修改时间
     */
    @Override
    public void deleteUsrSite(String usr, Date modifyDate ) throws CommonException {

        getExistUsr( usr );

        QueryWrapper<SiteUsr> wrapper = new QueryWrapper<>();
        wrapper.eq( SiteUsr.USR, usr );
        List<SiteUsr> siteUsrList = siteUsrMapper.selectList( wrapper );
        CommonUtil.compareDateSame( modifyDate,siteUsrList.get( 0 ).getModifyDate() );

        //删除用户工厂对应关系
        QueryWrapper<SiteUsr> delWrapper = new QueryWrapper<>();
        delWrapper.eq( SiteUsr.USR, usr ).eq( SiteUsr.MODIFY_DATE, modifyDate );
        Integer delInt = siteUsrMapper.delete( delWrapper );
        if( delInt<0 ){
            throw new CommonException(  "数据已修改，请查询后再执行保存操作", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }

    }



}