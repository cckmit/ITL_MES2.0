package com.itl.mes.core.provider.webService;

import com.itl.iap.common.base.exception.CommonException;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.text.ParseException;

/**
 * 工单维护webservice接口
 * @author ycw
 * @date 20210309
 */
@WebService(name = "ERPToMESWoInfo", // 暴露服务名称
        targetNamespace = "http://webservice.provider.core.mes.itl.com/"// 命名空间
)
public interface ShopOrderWebService {

    /**
     * 保存工单
     * @param info
     * @return
     */
    String saveShopOrderInfo(@WebParam(name = "info") String info) throws ParseException, CommonException;
}
