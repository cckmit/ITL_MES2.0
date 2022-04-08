package com.itl.mes.andon.provider.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.GradeDTO;
import com.itl.mes.andon.api.entity.Grade;
import com.itl.mes.andon.api.entity.GradePush;
import com.itl.mes.andon.api.service.GradeService;
import com.itl.mes.andon.api.vo.AndonGradePushVo;
import com.itl.mes.andon.provider.mapper.GradePushMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 安灯等级
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@RestController
@RequestMapping("/grade")
@Api(tags = "安灯等级")
public class GradeController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private GradePushMapper gradePushMapper;

    /**
     * 信息
     */
    @ApiOperation(value = "根据id查询")
    @GetMapping("/{id}")
    public ResponseData<Grade> info(@PathVariable("id") int id) {
        Grade grade = gradeService.getById(id);
        return ResponseData.success(grade);
    }

    /**
     * 保存或更新
     */
    @ApiOperation(value = "保存或更新")
    @PostMapping("/saveOrUpdate")
    public ResponseData<Grade> saveOrUpdate(@RequestBody Grade grade) throws CommonException {
        gradeService.saveGrade(grade);
        return ResponseData.success();
    }

    /**
     * 分页查询信息
     *
     * @param gradeDTO 分页对象
     * @return 分页对象
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询安灯等级信息")
    public ResponseData<IPage<Grade>> page(@RequestBody GradeDTO gradeDTO) {

        QueryWrapper<Grade> wrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank(gradeDTO.getWorkShopBo())) {
            wrapper.like("WORKSHOP_BO", gradeDTO.getWorkShopBo());
        }
        if (!StrUtil.isBlank(gradeDTO.getWorkShopName())) {
            wrapper.like("WORKSHOP_NAME", gradeDTO.getWorkShopName());
        }
        if (gradeDTO.getAlertGrade() != 0 ) {
            wrapper.eq("ANDON_GRADE", gradeDTO.getAlertGrade());
        }
        if (!StrUtil.isBlank(gradeDTO.getAndonTypeName())) {
            wrapper.like("ANDON_TYPE_NAME", gradeDTO.getAndonTypeName());
        }
        IPage<Grade> page = gradeService.page(gradeDTO.getPage(), wrapper);
        return ResponseData.success(page);
    }


    /**
     * 分页查询信息
     *
     * @param gradeDTO 分页对象
     * @return 分页对象
     */
    @PostMapping("/pageByState")
    @ApiOperation(value = "分页查询可用的安灯等级信息")
    public ResponseData<IPage<Grade>> page2(@RequestBody GradeDTO gradeDTO) {

        QueryWrapper<Grade> wrapper = new QueryWrapper<>();
        if (!StrUtil.isBlank(gradeDTO.getWorkShopBo())) {
            wrapper.like("workshop_bo", gradeDTO.getWorkShopBo());
        }
        if (!StrUtil.isBlank(gradeDTO.getWorkShopName())) {
            wrapper.eq("workshop_name", gradeDTO.getWorkShopName());
        }
        wrapper.lambda().eq(Grade::getState, "1");
        IPage<Grade> page = gradeService.page(gradeDTO.getPage(), wrapper);
        return ResponseData.success(page);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    public ResponseData<Grade> delete(@RequestBody List<String> ids) {
        gradeService.removeByIds(ids);
        for (String id : ids) {     //删除对应的配置人员
            gradePushMapper.delete(new QueryWrapper<GradePush>().eq("ANDON_GRADE_ID",id));
        }
        return ResponseData.success();
    }

    /**
     * andon查看配置人员
     * @return List<GradePush>
     */
    @ApiOperation(value = "查看配置人员")
    @GetMapping("/queryGradePush/{id}")
    public ResponseData<AndonGradePushVo> queryGradePush(@PathVariable("id") String id) {
        AndonGradePushVo andonGradePushVo = new AndonGradePushVo();
        Grade grade = gradeService.getById(id);
        andonGradePushVo.setAndonGrade(grade.getAndonGrade());
        List<GradePush> gradePushes = gradePushMapper.selectList(new QueryWrapper<GradePush>().eq("ANDON_GRADE_ID", id));
        andonGradePushVo.setGradePushes(gradePushes);
        return ResponseData.success(andonGradePushVo);
    }

    /**
     * andon添加配置人员
     * @return string
     */
    @ApiOperation(value = "添加配置人员")
    @PostMapping("/saveGradePush")
    public ResponseData<String> saveGradePush(@RequestBody List<GradePush> gradePush) {
        gradeService.saveGradePush(gradePush);
        return ResponseData.success("success");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除配置人员")
    @DeleteMapping("/deletePush")
    public ResponseData<String> deletePush(@RequestBody List<Integer> ids) {
        gradePushMapper.deleteBatchIds(ids);
        return ResponseData.success("success");
    }
}

