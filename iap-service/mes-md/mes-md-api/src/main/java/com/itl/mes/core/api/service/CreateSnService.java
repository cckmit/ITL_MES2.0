package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.entity.SnRule;
import com.itl.mes.core.api.vo.SfcVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author sky,
 * @date 2019/8/5
 * @time 15:36
 */
public interface CreateSnService {
    List<SnRule> querySnRuleByT() throws CommonException;

    List<SnRule> querySnRuleByS() throws CommonException;

    void createSn(String number, String codeRuleType, Boolean whether,String shopOrder) throws CommonException;

    IPage<Map<String,Object>> selectPageByShape(IPage<Map<String, Object>> page, Map<String, Object> params);

    String selectTypeByShopOrder(String shopOrder) throws CommonException;

    void importExcel(MultipartFile file) throws CommonException;

    void exportExcel(String site, HttpServletResponse response) throws CommonException;
    List<Sfc> createSnByRule(String number, String codeRuleType, String sfcQty, String operationOrder,String dispatchCode,int flag) throws CommonException;

    IPage<Sfc> selectSfcByOperationOrder(SfcDto sfcDto) throws CommonException;

}
