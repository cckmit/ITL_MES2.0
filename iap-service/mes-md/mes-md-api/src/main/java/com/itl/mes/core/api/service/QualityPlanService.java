package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.QualityPlanParameterDTO;
import com.itl.mes.core.api.entity.QualityPlan;
import com.itl.mes.core.api.entity.QualityPlanParameter;
import com.itl.mes.core.api.vo.QualityPlanExcelInfoVo;
import com.itl.mes.core.api.vo.QualityPlanVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lzh
 * @since 2019-08-29
 */
public interface QualityPlanService extends IService<QualityPlan> {

    /**
     * 获取编号
     *
     * @return
     */
    public String getQPnumber() throws CommonException;


    /**
     * 保存或新增控制质量数据
     *
     * @param qualityPlanVO
     * @return
     */
    public QualityPlanParameter saveInUpdate(QualityPlanVO qualityPlanVO) throws CommonException;

    /**
     * 根据编号和版本进行删除
     *
     * @param qualityPlan
     */
    public void deleteQuality(String qualityPlan) throws CommonException;

    /**
     * 分页查询
     *
     * @return
     */
    IPage<Map> selectQualityPlanPage(IPage<Map> page, Map<String, Object> params);

    /**
     * 精确查询
     *
     * @param qualityPlanParameterDTO
     * @return
     */
    public IPage<QualityPlanParameter> getQpapVoByQualityPlan(QualityPlanParameterDTO qualityPlanParameterDTO) throws CommonException;

    /**
     * 查询是否存在该编号
     *
     * @param qppDto
     * @return
     * @throws CommonException
     */
    public QualityPlan selectByInspectType(QualityPlanParameterDTO qppDto) throws CommonException;

    /**
     * 获取明细数据列表
     *
     * @param qualityPlan
     * @return
     */
    public IPage<QualityPlanParameter> getParameterList(QualityPlanParameterDTO qualityPlanParameterDTO , QualityPlan qualityPlan) throws CommonException;

    List<QualityPlan> selectList();

    /**
     * 导出文件
     * @param site
     * @param response
     * @throws CommonException
     */
    void exportQplan(String site, HttpServletResponse response) throws CommonException;

    /**
     * 上传文件
     * @param file
     * @throws CommonException
     */
    void importExcel(MultipartFile file) throws CommonException;

    boolean setDefaultPlan(String qualityPlan);

    QualityPlan getDefaultPlan();

    QualityPlan saveQualityPlan(QualityPlan qualityPlan) throws CommonException;

    /**
     * 根据物料编码导出文件
     * @param item
     * @param response
     * @throws CommonException
     */
    void exportByItem(String item, HttpServletResponse response) throws CommonException;

    /**
     * 新增或修改质量检验项信息
     * @param qualityPlanExcelInfoVo
     */
    void saveQualityPlanParameter(QualityPlanExcelInfoVo qualityPlanExcelInfoVo,int row) throws CommonException;

}