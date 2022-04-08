package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.vo.RouterVo;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import org.springframework.util.RouteMatcher;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工艺路线
 * </p>
 *
 * @author linjl
 * @since 2020-01-28
 */
public interface RouterService extends IService<Router> {

    /**
     * 获取工艺路线信息(带路线图)
     *
     * @return
     */
    Router getRouter(String bo) throws CommonException;

    /**
     * 保存工艺路线
     * */
    boolean saveRouter(Router router) throws Exception;

    /**
     * 删除工艺路线
     *
     * @param router 工艺路线
     * @throws CommonException
     */
    void deleteRouter(Router router) throws CommonException;

    /**
     * 根据工艺路线编号查询当前版本的工艺路线
     */
    Router getRouterByRouter(String router) throws  CommonException;

    String importRouter(MultipartFile file) throws IOException, CommonException;

}