package com.itl.mes.andon.provider.controller;


import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.client.utils.IPUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.aop.IpUtil;
import com.itl.iap.common.base.utils.IPUtils;
import com.itl.mes.andon.api.dto.AndonExceptionDTO;
import com.itl.mes.andon.api.entity.AndonException;
import com.itl.mes.andon.api.service.AndonExceptionService;
import com.itl.mes.andon.api.vo.AndonExceptionVo;
import com.itl.mes.andon.provider.mapper.ExceptionMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/andonException")
@Api(value = "安灯异常",tags = "安灯异常")
public class AndonExceptionController {
    private final Logger logger = LoggerFactory.getLogger(AndonExceptionController.class);

    @Autowired
    private AndonExceptionService andonExceptionService;

    @Autowired
    private ExceptionMapper exceptionMapper;

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value="通过主键查询数据")
    public ResponseData<AndonException> getAndonExceptionById(@PathVariable String id) {
        return ResponseData.success(andonExceptionService.getAndonExceptionById(id));
    }

    /**
     * 精确查询
     * @param andonExceptionDTO
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "分页查询检验单数据")
    public ResponseData<IPage<AndonException>> getAndonException(@RequestBody AndonExceptionDTO andonExceptionDTO) throws CommonException {
        //QualityPlanAtParameterVo qualityPlanAtParameterVo =  qualityPlanService.getQpapVoByQualityPlan(qualityPlan,version);
        IPage<AndonException> list = andonExceptionService.getAndonException(andonExceptionDTO);
        return ResponseData.success(list);
    }

    /**
     * 保存或新增andon异常
     * @param andonExceptionVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存或新增andon异常")
    public ResponseData saveInUpdate(@RequestBody AndonExceptionVo andonExceptionVo, HttpServletRequest request) throws CommonException {
        if (andonExceptionVo == null) {
            throw new CommonException("参数值不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        andonExceptionVo.setIp(IpUtil.getIpAddr(request));
        AndonException andonException = andonExceptionService.saveInUpdate(andonExceptionVo);
        return ResponseData.success(andonException);
    }

    /**
     * 保存或新增设备维修
     * @param andonExceptionVo
     * @return
     */
    @PostMapping("/repair")
    @ApiOperation(value = "新建或修改设备维修")
    public ResponseData repair(@RequestBody AndonExceptionVo andonExceptionVo) throws CommonException {
        if (andonExceptionVo == null) {
            throw new CommonException("参数值不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        AndonException andonException = andonExceptionService.saveOrUpdateRepair(andonExceptionVo);
        return ResponseData.success(andonException);
    }

    /**
     * 精确查询
     * @param andonExceptionDTO
     * @return
     */
    @PostMapping("/repairList")
    @ApiOperation(value = "分页查询维修任务数据")
    public ResponseData<IPage<AndonException>> repairList(@RequestBody AndonExceptionDTO andonExceptionDTO) throws CommonException {
        IPage<AndonException> list = andonExceptionService.repairList(andonExceptionDTO);
        return ResponseData.success(list);
    }

    /**
     * 精确查询
     * @param andonExceptionDTO
     * @return
     */
    @PostMapping("/myRepairTask")
    @ApiOperation(value = "分页查询我的维修任务数据")
    public ResponseData<IPage<AndonException>> myRepairTask(@RequestBody AndonExceptionDTO andonExceptionDTO) throws CommonException {
        IPage<AndonException> list = andonExceptionService.myRepairTask(andonExceptionDTO);
        return ResponseData.success(list);
    }

    /**
     * 异常签到
     * @param exceptionVO
     * @return
     */
    @PostMapping("/getRepairTask")
    @ApiOperation(value = "接单")
    public ResponseData<String> getRepairTask(@RequestBody AndonExceptionVo exceptionVO) throws CommonException {
        andonExceptionService.getRepairTask(exceptionVO);
        return ResponseData.success("success");
    }

    /**
     * 退回维修任务
     * @param exceptionVO
     * @return
     */
    @PostMapping("/backRepairTask")
    @ApiOperation(value = "退回维修任务")
    public ResponseData<String> backRepairTask(@RequestBody AndonExceptionVo exceptionVO) throws CommonException {
        AndonException andonException = exceptionMapper.selectById(new QueryWrapper<AndonException>().eq("exception_code",exceptionVO.getExceptionCode()));
        andonException.setState("0");
        andonException.setCheckUser(null);
        andonException.setCheckUserName(null);
        exceptionMapper.updateById(andonException);
        return ResponseData.success("success");
    }

    /**
     * 删除或批量删除
     * @param Ids
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除安灯类型数据")
    public ResponseData<String> delete(@RequestBody List<Integer> Ids) throws CommonException {
        andonExceptionService.delete(Ids);
        return ResponseData.success("success");
    }

    /**
     * 异常签到
     * @param exceptionVO
     * @return
     */
    @PostMapping("/signin")
    @ApiOperation(value = "异常签到")
    public ResponseData<String> signin(@RequestBody AndonExceptionVo exceptionVO) throws CommonException {
        andonExceptionService.signin(exceptionVO);
        return ResponseData.success("success");
    }

    /**
     * 异常签到
     * @param exceptionVO
     * @return
     */
    @PostMapping("/relieve")
    @ApiOperation(value = "异常解除")
    public ResponseData<String> relieve(@RequestBody AndonExceptionVo exceptionVO) throws CommonException {
        andonExceptionService.relieve(exceptionVO);
        return ResponseData.success("success");
    }

    /**
     * 用户验证
     * @param params
     * @return
     */
    @PostMapping("/userVerify")
    @ApiOperation(value = "用户验证并进行签到或解除")
    @ApiOperationSupport(
            params =
            @DynamicParameters(
                    name = "userVerify",
                    properties = {
                            @DynamicParameter(name = "userName", value = ""),
                            @DynamicParameter(name = "pwd", value = ""),
                            @DynamicParameter(name = "id", value = ""),
                            @DynamicParameter(name = "type", value = ""),
                    }))
    public ResponseData userVerify(@RequestBody Map<String, Object> params) throws CommonException {
        String uname = andonExceptionService.userVerify(params);
        if (StrUtil.isEmpty(uname)){
            return ResponseData.error("500","用户名或密码错误！");
        }
        if (uname.equals("1")){
            return ResponseData.error("500","签到用户不匹配！");
        }
        if (uname.equals("2")){
            return ResponseData.error("500","解除用户不匹配！");
        }
        return ResponseData.success(uname);
    }
}
