package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.InspectType;
import com.itl.mes.core.api.vo.InspectTypeVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 检验类型维护 服务类
 * </p>
 *
 * @author lzh
 * @since 2019-08-28
 */
public interface InspectTypeService extends IService<InspectType> {

    /**
     * 分页查询
     * @param params
     * @return
     * @throws CommonException
     */
    IPage<Map> selectPageInspectTypeList(IPage<Map> page, Map<String, Object> params) throws CommonException;
    List<Map> selectInspectTypeList(Map<String, Object> params) throws CommonException;
    IPage<Map> selectPageInspectTypeListByState(IPage<Map> page, Map<String, Object> params) throws CommonException;

    /**
     * 保存或更新数据
     * @throws CommonException
     * @return
     */
    public InspectType saveInspectType(InspectTypeVo inspectType) throws CommonException;

    /**
     * 根据参数值查询对应检验类型数据
     * @param inspectType
     * @return
     * @throws CommonException
     */
    public InspectType selectByInspectType(String inspectType)throws CommonException;

    /**
     * 精确查询
     * @param inspectType
     * @return
     * @throws CommonException
     */
    public InspectTypeVo getInspectTypeVoByInspectType(String inspectType)throws CommonException;

    /**
     * 根据检验类型编号删除数据
     * @param inspectType
     * @param modifyDate
     * @throws CommonException
     */
    public void deleteInspectType(String inspectType, Date modifyDate) throws CommonException;

    /**
     * 导出文件
     * @param site
     * @param response
     * @throws CommonException
     */
    public void exportInspectFile(String site, HttpServletResponse response) throws CommonException;

    /**
     * 上传文件
     * @param file
     */
    void importExcel(MultipartFile file) throws CommonException;

    List<InspectType> selectList();
}
