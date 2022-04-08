package com.itl.mes.core.api.service;

import java.util.List;
import java.util.Map;

public interface CommonReportService {

    //List<Map<String,Object>> selectAllCallDragData( String site, String sectionCodes );

    List<Map<String,Object>> selectManualSql( String sql, Map<String,Object> params );
}
