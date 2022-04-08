package com.itl.mes.core.provider.webService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
/**
 * @author pwy
 * @date 2021/3/9
 */
@WebService(name = "ERPToMESMaterialInfo", // 暴露服务名称
        targetNamespace = "http://webservice.provider.core.mes.itl.com/"// 命名空间
)
public interface ItemWebService {

    String saveItemInfo(@WebParam(name = "info") String info);
}
