package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.mes.core.api.entity.Site;
import com.itl.mes.core.api.service.SiteService;
import com.itl.mes.core.api.vo.SiteVo;
import com.itl.mes.core.provider.mapper.SiteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-20
 */
@Service
@Transactional
public class SiteServiceImpl extends ServiceImpl<SiteMapper, Site> implements SiteService {


    @Autowired
    private SiteMapper siteMapper;
    @Resource
    private UserUtil userUtil;

    @Override
    public List<Site> selectList() {
        QueryWrapper<Site> entityWrapper = new QueryWrapper<Site>();
        //getEntityWrapper(entityWrapper, site);
        return super.list(entityWrapper);
    }

    /**
     * 通过工厂查询工厂数据
     *
     * @param site 工厂
     * @return Site
     */
    @Override
    public Site getSiteBySite( String site ){

        return  siteMapper.selectById( site );

    }

    /**
     * 通过工厂查询工厂数据，不存在则报错
     *
     * @param site 工厂
     * @return Site
     * @throws CommonException 异常
     */
    @Override
    public Site getExistSiteBySite(String site) throws CommonException {

        Site siteEntity = getSiteBySite( site );
        if( siteEntity==null ){
            throw new CommonException( "工厂"+site+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return siteEntity;
    }

    /**
     * 工厂数据保存
     *
     * @param siteVo siteVo
     * @throws CommonException 异常
     */
    @Override
    @Transactional( rollbackFor = {Exception.class,RuntimeException.class} )
    public void saveSite( SiteVo siteVo ) throws CommonException {

        String siteStr = siteVo.getSite();
        if( StrUtil.isBlank( siteStr ) ){
            throw new CommonException( "工厂不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        Site siteEntity = siteMapper.selectById( siteStr );
        if( siteEntity==null ){ //新增

            Date createDate = new Date();
            Site site = new Site();

            site.setSite( siteStr );
            site.setSiteDesc( StrUtil.isBlank( siteVo.getSiteDesc() )?siteStr:siteVo.getSiteDesc() );
            site.setSiteType( siteVo.getSiteType() );
            site.setEnableFlag( siteVo.getEnableFlag() );
            site.setObjectSetBasicAttribute( userUtil.getUser().getUserName(), createDate );
            siteMapper.insert( site );

            //设置修改时间和描述
            siteVo.setModifyDate( createDate );
            siteVo.setSiteDesc( site.getSiteDesc() );

        }else { //更新

            Date frontModifyDate = siteVo.getModifyDate(); //前台传递的时间值
            Date modifyDate = siteEntity.getModifyDate(); //后台数据库的值
            CommonUtil.compareDateSame( frontModifyDate, modifyDate ); //比较时间是否相等

            Date newModifyDate = new Date();
            Site site = new Site();

            site.setSite( siteStr );
            site.setSiteType( siteVo.getSiteType() );
            site.setSiteDesc( StrUtil.isBlank( siteVo.getSiteDesc() )?siteStr:siteVo.getSiteDesc() );
            site.setEnableFlag( siteVo.getEnableFlag() );
            site.setModifyUser( userUtil.getUser().getUserName() );
            site.setModifyDate( newModifyDate );
            Integer updateInt = siteMapper.updateById( site ); //更新工厂数据
            if( updateInt==0 ){
                throw new CommonException( "数据已修改，请查询后再执行操作", CommonExceptionDefinition.VERIFY_EXCEPTION );
            }
            //设置修改时间和描述
            siteVo.setModifyDate( newModifyDate );
            siteVo.setSiteDesc( site.getSiteDesc() );

        }


    }


}