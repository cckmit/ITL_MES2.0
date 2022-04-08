package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.service.CommonReportService;
import com.itl.mes.core.provider.mapper.CommonReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 报表通用入口
 */
@Service
public class CommonReportServiceImpl implements CommonReportService {

    @Autowired
    private CommonReportMapper commonReportMapper;


    /**
     * 拉环报表数据查询
     *
     * @param site 工厂
     * @param sectionCodes 多个工段已逗号隔开
     * @return List<Map<String, Object>>
     */
    /*@Override
    public List<Map<String, Object>> selectAllCallDragData( String site, String sectionCodes ) {

        List<String> list = null;
        if( !StrUtil.isBlank( sectionCodes ) ){
            list = ( Arrays.asList( sectionCodes.split( "," ) ) );
        }
        return commonReportMapper.selectAllCallDragData( site, list );
    }*/

    @Override
    public List<Map<String, Object>> selectManualSql( String sql, Map<String,Object> params ) {
        if( params!=null && !params.containsKey( "site" ) ){
            params.put( "site", UserUtils.getSite() );
        }
        return commonReportMapper.selectManualSql( sql, params );
    }

}
