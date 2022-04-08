package com.itl.mes.core.provider.webService;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 同步PLM工艺路线接口
 */
@WebService(name = "ERPToMESRouterInfo",
        targetNamespace = "http://webservice.provider.core.mes.itl.com/"// 命名空间
)
public interface SyncRouterWebService {
    /**
     * 同步工艺路线信息
     * @param info
     * @return
     */
    String syncRouterInfo(@WebParam(name = "info") String info);
}
