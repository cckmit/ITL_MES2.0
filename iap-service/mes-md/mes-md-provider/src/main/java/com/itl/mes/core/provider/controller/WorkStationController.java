package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.WorkStationDTO;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.WorkStation;
import com.itl.mes.core.api.service.OperationService;
import com.itl.mes.core.api.service.WorkStationService;
import com.itl.mes.core.api.vo.WorkStationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author pwy
 * @since 2021-3-11
 */
@RestController
@RequestMapping("/workStations")
@Api(tags = " 工步表" )
public class WorkStationController {
    private final Logger logger = LoggerFactory.getLogger(WorkStationController.class);


    @Autowired
    private OperationService operationService;

    @Autowired
    private WorkStationService workStationService;



//    /**
//     * 根据id查询
//     * @param id 主键
//     * @return
//     */
//    @GetMapping("/{id}")
//    @ApiOperation(value="通过主键查询工步数据")
//    public ResponseData<WorkStation> getWorkStationById(@PathVariable String id) {
//        return ResponseData.success(workStationService.getById(id));
//    }

    /**
     *
     * @param
     * @return
     */
    @GetMapping("/queryOperations")
    @ApiOperation(value="下拉查询工序信息")
    public ResponseData<List<WorkStation>> getOperations() {
        QueryWrapper queryWrapper = new QueryWrapper<Operation>();
        return ResponseData.success(workStationService.list(queryWrapper));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存工步数据")
    public ResponseData<String> saveByOperationVo(@RequestBody WorkStationVo workStationVo) throws CommonException {
        if(workStationVo==null) throw  new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        workStationService.saveByWorkStationVo(workStationVo);
        return ResponseData.success("工步数据保存成功！");
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除工步信息")
    public ResponseData<String> delete(@RequestParam(value = "bo") String bo) throws CommonException {
        if(StrUtil.isBlank(bo)) throw new CommonException("工步不存在", CommonExceptionDefinition.VERIFY_EXCEPTION);
        QueryWrapper wrapper = new QueryWrapper<WorkStation>();
        wrapper.eq("bo",bo);
        boolean result = workStationService.remove(wrapper);
        if(!result)throw new CommonException("工步不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        return ResponseData.success("删除成功！");
    }
    /**
     * 分页查询设备信息
     * @param
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value="分页查询工步信息")
    public ResponseData<IPage<WorkStation>> query(@RequestBody WorkStationDTO workStationDTO){
        return ResponseData.success(workStationService.query(workStationDTO));
    }
}
