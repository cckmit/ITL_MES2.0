package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.api.entity.RouterProcess;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.api.vo.RouterVo;
import com.itl.mes.core.provider.mapper.RouterMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.RouteMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  工艺路线
 * @author linjl
 * @since 2021-01-28
 */
@RestController
@RequestMapping("/router")
@Api(tags = " 工艺路线" )
public class RouterController {
    private final Logger logger = LoggerFactory.getLogger(RouterController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public RouterService routerService;

    @Autowired
    public RouteStationService routeStationService;

    @Autowired
    public RouterMapper routerMapper;

    /**
     * 根据router查询
     *
     * @param router
     * @return RestResult<Router>
     */
    @GetMapping("/query")
    @ApiOperation(value="查找工艺路线")
    @ApiImplicitParam(name="router",value="工艺路线",dataType="string", paramType = "query")
    public ResponseData<Router> queryRouter(@RequestParam String router) throws CommonException {
        return ResponseData.success( routerService.getRouter(router) );
    }

    /**
     * 保存工艺路线
     *
     * @param routerVo
     * @return RestResult<Router>
     */
    @PostMapping("/save")
    @ApiOperation(value="保存工艺路线")
    public ResponseData<Router> saveRouter(@RequestBody RouterVo routerVo) throws Exception {
        Router router = new Router(routerVo.getSite(), routerVo.getRouter(), routerVo.getVersion());
        router.setSite(routerVo.getSite())
            .setRouter(routerVo.getRouter())
            .setRouterType(routerVo.getRouterType())
            .setRouterName(routerVo.getRouterName())
            .setRouterDesc(routerVo.getRouterDesc())
            .setState(routerVo.getState())
            .setVersion(routerVo.getVersion())
            .setItemBo(routerVo.getItemBo())
            .setIsCurrentVersion(routerVo.getIsCurrentVersion());

        if(null!=routerVo.getProcessInfo()) {
            RouterProcess routerProcess = new RouterProcess(router.getBo());
            routerProcess.setProcessInfo(routerVo.getProcessInfo());
            routerProcess.setSite(routerVo.getSite());
            router.setRouterProcess(routerProcess);
        }
        router.setCustomDataValVoList(routerVo.getCustomDataValVoList());

        boolean insertValue=routerService.saveRouter(router);
        if(insertValue==false){
            return ResponseData.error("已存在该路线");
        }

        return ResponseData.success(routerService.getRouter(router.getBo()));
    }

    /**
     * 删除工艺路线
     *
     * @param bo 工艺路线
     * @return RestResult<String>
     */
    @GetMapping("/delete")
    @ApiOperation(value="删除工艺路线")
    @ApiImplicitParam(name="bo",value="工艺路线bo",dataType="string", paramType = "query")
    public ResponseData<String> deleteRouter( String bo) throws CommonException {
        if( StrUtil.isBlank( bo ) ){
            throw new CommonException( "工艺路线BO不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        }

        Router router = new Router();
        router.setBo(bo);
        routerService.deleteRouter(router);
        return ResponseData.success( "success" );
    }


    @GetMapping("/updateRouterVersion")
    @ApiOperation("修改工艺路线当前版本")
    public ResponseData updateRouterVersion(@RequestParam("routerBo") String routerBo,@RequestParam("isCurrentVersion")String  isCurrentVersion,@RequestParam("router") String router){
        if(StrUtil.isNotBlank(routerBo) && StrUtil.isNotBlank(isCurrentVersion)){
            //判断此条工艺路线是否是当前版本
            if(Integer.parseInt(isCurrentVersion)==1){
                //工艺路线为当前版本，更新表中所有为此工艺路线编号的版本为0
                routerMapper.updateByRouter(router);
            }
            Router routerEntity=new Router();
            routerEntity.setBo(routerBo);
            routerEntity.setIsCurrentVersion(Integer.parseInt(isCurrentVersion));
            routerMapper.updateById(routerEntity);
        }
        return ResponseData.success();
    }

    /**
     * 导入工艺路线
     * @return
     */
    @ApiOperation(value = "importRouter", notes = "导入", httpMethod = "POST")
    @PostMapping("/importRouter")
    public ResponseData<String> importRouter(@RequestParam("file") MultipartFile file) throws Exception {

        return  ResponseData.success(routerService.importRouter(file));
    }

}
