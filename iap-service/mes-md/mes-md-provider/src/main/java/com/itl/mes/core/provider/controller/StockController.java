package com.itl.mes.core.provider.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.QualityCheckListDTO;
import com.itl.mes.core.api.dto.StockDTO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.QualityCheckList;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.entity.Stock;
import com.itl.mes.core.api.service.SfcService;
import com.itl.mes.core.api.service.StockService;
import com.itl.mes.core.provider.mapper.ItemMapper;
import com.itl.mes.core.provider.mapper.QualityCheckListMapper;
import com.itl.mes.core.provider.mapper.StockMapper;
import com.itl.mes.core.provider.service.impl.StockServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock")
@Api(tags = "入库" )
public class StockController {

    private final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private StockService stockService;

    @Autowired
    private SfcService sfcService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private QualityCheckListMapper qualityCheckListMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockServiceImpl stockServiceImpl;
    /**
     * 根据id查询
     *
     * @param sfc 主键
     * @return
     */
    @GetMapping("/{sfc}")
    @ApiOperation(value="通过Sfc查询数据")
    public ResponseData<Sfc> getSfc(@PathVariable String sfc) throws CommonException {
        Sfc sfcEntity = sfcService.selectBySfc(sfc);
        QueryWrapper<QualityCheckList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("SFC",sfc);
        queryWrapper.eq("CHECK_TYPE","2");
        queryWrapper.eq("CHECK_RESULT","合格");
        List<QualityCheckList> qualityCheckList = qualityCheckListMapper.selectList(queryWrapper);
        if (qualityCheckList!=null && qualityCheckList.size()>0){
            sfcEntity.setCheckUser(qualityCheckList.get(0).getCheckUser());
            sfcEntity.setCheckUserName(qualityCheckList.get(0).getCheckUserName());
        }
        return ResponseData.success(sfcEntity);
    }

    /**
     * 根据id查询
     *
     * @param params 主键
     * @return
     */
    @PostMapping("/applyStock")
    @ApiOperation(value="生成入库申请单")
    public ResponseData<Sfc> applyInStock(@RequestBody Map<String, Object> params) throws CommonException {

        String sfc = params.getOrDefault("sfc", "").toString();
        String wareHouse = params.getOrDefault("wareHouse", "").toString();
        BigDecimal doneQty = new BigDecimal(params.getOrDefault("doneQty", "").toString());
        BigDecimal scrapQty = new BigDecimal(params.getOrDefault("scrapQty", "").toString());
        Sfc sfcEntity = sfcService.selectBySfc(sfc);

        // 校验入库良品数+入库报废数<=sfc数量
        BigDecimal sumQty=doneQty.add(scrapQty);
        if(sumQty.compareTo(sfcEntity.getSfcQty())==1){
            return ResponseData.error("500", "入库良品数("+doneQty.intValue()+")与入库报废数("+scrapQty+")之和("+sumQty+")不能大于sfc的数量("+sfcEntity.getSfcQty()+")");
        }

        List<Stock> stockList = stockMapper.selectList(new QueryWrapper<Stock>().eq("sfc", sfc).eq("success_flag", "1"));
        if (CollectionUtil.isNotEmpty(stockList)){
            return ResponseData.error("500","该SFC入库单在MES已经入库，不必重复操作");
        }
        if (!sfcEntity.getState().equals("已完成")){
            return ResponseData.error("500","该SFC未完成，不能入库申请");
        }

        sfcEntity.setDifQty(doneQty.subtract(sfcEntity.getDoneQty()));
        sfcEntity.setDoneQty(doneQty);
        String result = stockService.applyStock(sfcEntity, wareHouse,1);
        if (result.indexOf("SUCCESS！")==-1){
            return ResponseData.error("500",result);
        }
        return ResponseData.success(sfcEntity);
    }

    /**
     * ERP重复入库
     * @return
     */
    @GetMapping("/repeatInStock")
    @ApiOperation(value="ERP重复入库")
    public ResponseData repeatInStock(@RequestParam("stockNo") String stockNo) throws CommonException {
        Stock stock = stockService.getById(stockNo);
        if (Stock.IN_STOCK_FAILED.equals(stock.getSuccessFlag())){
            throw new CommonException("请选择一条成功的入库单进行二次入库",504);
        }
        Sfc sfcEntity = sfcService.selectBySfc(stock.getSfc());
        sfcEntity.setDifQty(BigDecimal.ZERO);
        sfcEntity.setDoneQty(new BigDecimal(stock.getOkQty()));
        String result = stockServiceImpl.applyStock(sfcEntity, stock.getWarehouse(),0);
        if (result.indexOf("SUCCESS！")==-1){
            return ResponseData.error("500",result);
        }
        return ResponseData.success();
    }
    /**
     * 精确查询
     * @param stockDTO
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "查询入库单数据")
    public ResponseData<IPage<Stock>> getStock(@RequestBody StockDTO stockDTO) throws CommonException {
        //QualityPlanAtParameterVo qualityPlanAtParameterVo =  qualityPlanService.getQpapVoByQualityPlan(qualityPlan,version);
        if(ObjectUtil.isEmpty(stockDTO.getPage())){
            stockDTO.setPage(new Page(0, 10));
        }
        return ResponseData.success(stockMapper.getStock(stockDTO.getPage(),stockDTO));
    }

    /**
     * 精确查询
     * @param stockDTO
     * @return
     */
    @PostMapping("/page")
    @ApiOperation(value = "入库申请单")
    public ResponseData<IPage<Stock>> page(@RequestBody StockDTO stockDTO) throws CommonException {
        //QualityPlanAtParameterVo qualityPlanAtParameterVo =  qualityPlanService.getQpapVoByQualityPlan(qualityPlan,version);
        if(ObjectUtil.isEmpty(stockDTO.getPage())){
            stockDTO.setPage(new Page(0, 10));
        }
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<Stock>();
        if (StrUtil.isNotEmpty(stockDTO.getSfc())){
            queryWrapper.eq("SFC",stockDTO.getSfc());
        }
        if (StrUtil.isNotEmpty(stockDTO.getItem())){
            queryWrapper.eq("ITEM_BO",stockDTO.getItem());
        }
        IPage<Stock> list = stockService.page(stockDTO.getPage(),queryWrapper);
        return ResponseData.success(list);
    }
}
