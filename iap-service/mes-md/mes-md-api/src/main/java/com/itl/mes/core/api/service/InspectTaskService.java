package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.InspectTask;
import com.itl.mes.core.api.vo.InspectTaskVo;
import com.itl.mes.core.api.vo.InspectTypeVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检验任务 服务类
 * </p>
 *
 * @author space
 * @since 2019-08-30
 */
public interface InspectTaskService extends IService<InspectTask> {

    List<InspectTask> selectList();

    IPage<InspectTask> selectinspectTaskPage(IPage<InspectTask> page, Map<String, Object> params);

    InspectTypeVo getInspectType(String inspectType) throws CommonException;

    void createInspectTask(InspectTaskVo inspectTaskVo) throws CommonException;

    void deleteInspectTask(List<InspectTaskVo> inspectTaskVos) throws CommonException;

    InspectTask getExistInspectTask(String inspectTask) throws CommonException;

    void createInspectTaskByFqc(InspectTaskVo inspectTaskVo) throws CommonException;

    void importExcel(MultipartFile file) throws CommonException;

    void closeInspectTask(List<InspectTaskVo> inspectTaskVos) throws CommonException;

    void exportInspectTask(String site, HttpServletResponse response, Map<String, Object> params) throws CommonException;
}