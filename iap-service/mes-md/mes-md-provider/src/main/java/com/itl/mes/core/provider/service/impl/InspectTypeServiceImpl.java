package com.itl.mes.core.provider.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.*;
import com.itl.mes.core.api.bo.InspectTypeHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.CustomDataVal;
import com.itl.mes.core.api.entity.InspectType;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.InspectTypeService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.InspectTypeVo;
import com.itl.mes.core.provider.mapper.InspectTypeMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 检验类型维护 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2019-08-28
 */
@Service
@Transactional
public class InspectTypeServiceImpl extends ServiceImpl<InspectTypeMapper, InspectType> implements InspectTypeService {

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private InspectTypeMapper inspectTypeMapper;

    @Resource
    private UserUtil userUtil;

    /**
     * 分页查询检验类型数据
     * @param page
     * @param params
     * @return
     * @throws CommonException
     */
    @Override
    public IPage<Map> selectPageInspectTypeList(IPage<Map> page, Map<String, Object> params) throws CommonException {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map> InspectTypeList = inspectTypeMapper.selectPageInspectTypeList(page,params);
        page.setRecords(InspectTypeList);
        return page;
    }

    @Override
    public List<Map> selectInspectTypeList(Map<String, Object> params) throws CommonException {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map> InspectTypeList = inspectTypeMapper.selectInspectTypeList(params);
        return InspectTypeList;
    }
   @Override
    public IPage<Map> selectPageInspectTypeListByState(IPage<Map> page, Map<String, Object> params) throws CommonException {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map> InspectTypeList = inspectTypeMapper.selectPageInspectTypeListByState(page,params);
        page.setRecords(InspectTypeList);
        return page;
    }

    /**
     * 保存或更新数据
     * @throws CommonException
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public InspectType saveInspectType(InspectTypeVo inspectType) throws CommonException {

        if(StrUtil.isBlank(inspectType.getRequiredItem())) {
            inspectType.setRequiredItem("0");
        }
        if(StrUtil.isBlank(inspectType.getRequiredOperation())) {
            inspectType.setRequiredOperation("0");
        }
        if(StrUtil.isBlank(inspectType.getRequiredProductLine())) {
            inspectType.setRequiredProductLine("0");
        }
        if(StrUtil.isBlank(inspectType.getRequiredSfc())) {
            inspectType.setRequiredSfc("0");
        }
        if(StrUtil.isBlank(inspectType.getRequiredShopOrder())) {
            inspectType.setRequiredShopOrder("0");
        }
        if(StrUtil.isBlank(inspectType.getRequiredStation())) {
            inspectType.setRequiredStation("0");
        }
        if(StrUtil.isBlank(inspectType.getRequiredWorkShop())) {
            inspectType.setRequiredWorkShop("0");
        }
        String bo =  new InspectTypeHandleBO( UserUtils.getSite(),inspectType.getInspectType()).getBo();
        InspectType inspectTypeEntity =  inspectTypeMapper.selectById(bo);
        InspectType newIt = new InspectType();
        //找不到则插入
        Date newDate=new Date();
        if(inspectTypeEntity==null){
            newIt.setBo(bo);
            newIt.setInspectType(inspectType.getInspectType());
            newIt.setInspectTypeDesc(inspectType.getInspectTypeDesc());
            newIt.setInspectTypeName(inspectType.getInspectTypeName());
            newIt.setState(inspectType.getState());
            newIt.setSite(UserUtils.getSite());
            newIt.setRequiredItem(inspectType.getRequiredItem());
            newIt.setRequiredProductLine(inspectType.getRequiredProductLine());
            newIt.setRequiredSfc(inspectType.getRequiredSfc());
            newIt.setRequiredOperation(inspectType.getRequiredOperation());
            newIt.setRequiredShopOrder(inspectType.getRequiredShopOrder());
            newIt.setRequiredStation(inspectType.getRequiredStation());
            newIt.setRequiredWorkShop(inspectType.getRequiredWorkShop());
            newIt.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(newIt);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            inspectTypeMapper.insert(newIt);
        }else{
            //否则更新数据库中对应的数据
            CommonUtil.compareDateSame(inspectType.getModifyDate(),inspectTypeEntity.getModifyDate());

            newIt.setBo(inspectTypeEntity.getBo());
            newIt.setInspectType(inspectType.getInspectType());
            newIt.setInspectTypeDesc(inspectType.getInspectTypeDesc());
            newIt.setInspectTypeName(inspectType.getInspectTypeName());
            newIt.setState(inspectType.getState());
            newIt.setSite(UserUtils.getSite());
            newIt.setRequiredItem(inspectType.getRequiredItem());
            newIt.setRequiredProductLine(inspectType.getRequiredProductLine());
            newIt.setRequiredOperation(inspectType.getRequiredOperation());
            newIt.setRequiredSfc(inspectType.getRequiredSfc());
            newIt.setRequiredShopOrder(inspectType.getRequiredShopOrder());
            newIt.setRequiredStation(inspectType.getRequiredStation());
            newIt.setRequiredWorkShop(inspectType.getRequiredWorkShop());
            newIt.setCreateUser(inspectTypeEntity.getCreateUser());
            newIt.setCreateDate(inspectTypeEntity.getCreateDate());

            newIt.setModifyDate(newDate);
            newIt.setModifyUser(userUtil.getUser().getUserName());
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(newIt);
            if(validResult.isHasErrors()){
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            inspectTypeMapper.updateById(newIt);
        }

        if(inspectType.getCustomDataValVoList() !=null){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( bo );
            customDataValRequest.setSite( UserUtils.getSite() );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.INSPECT_TYPE.getDataType() );
            customDataValRequest.setCustomDataValVoList( inspectType.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }
        return newIt;
    }

    /**
     * 查询检验类型数据Entity
     * @param inspectType
     * @return
     * @throws CommonException
     */
    @Override
    public InspectType selectByInspectType(String inspectType)throws CommonException{

        QueryWrapper<InspectType> entityWrapper = new QueryWrapper<InspectType>();
        entityWrapper.eq(InspectType.INSPECT_TYPE,inspectType);
        entityWrapper.eq(InspectType.SITE,UserUtils.getSite());
        List<InspectType> iTypes = inspectTypeMapper.selectList(entityWrapper);
        if(iTypes.isEmpty()){
            throw new CommonException("检验类型编号:"+inspectType+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return iTypes.get(0);
        }
    }

    /**
     * 精确查询
     * @param inspectTypeval
     * @return
     * @throws CommonException
     */
    @Override
    public InspectTypeVo getInspectTypeVoByInspectType(String inspectTypeval) throws CommonException {
        InspectType inspectTypeEntity = selectByInspectType(inspectTypeval);
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), inspectTypeEntity.getBo(), CustomDataTypeEnum.INSPECT_TYPE.getDataType());
        InspectTypeVo inspectTypeVo = new InspectTypeVo();
        BeanUtils.copyProperties(inspectTypeEntity,inspectTypeVo);
        inspectTypeVo.setCustomDataAndValVoList(customDataAndValVos);
        return inspectTypeVo;
    }

    /**
     * 根据inspectType删除对应数据
     * @param inspectTypeVal
     * @param modifyDate
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void deleteInspectType(String inspectTypeVal, Date modifyDate) throws CommonException {
        String inspectTypeHandleBO = new InspectTypeHandleBO(UserUtils.getSite(), inspectTypeVal).getBo();
        InspectType inspectType =  inspectTypeMapper.selectById(inspectTypeHandleBO);
        if (inspectType == null) {
            throw new CommonException("检验类型" + inspectTypeVal + "数据未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        CommonUtil.compareDateSame(modifyDate, inspectType.getModifyDate()); //验证时间
        inspectTypeMapper.deleteById(inspectTypeHandleBO);
    }

    /**
     * 导出文件
     * @param site
     * @param response
     * @throws CommonException
     */
    @Override
    public void exportInspectFile(String site, HttpServletResponse response) throws CommonException {
        QueryWrapper<InspectType> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(InspectType.SITE, site);
        List<InspectType> inspectTypeList = inspectTypeMapper.selectList(entityWrapper);

        // 创建参数对象（用来设定excel得sheet得内容等信息）
        ExportParams inspectTypeExport = new ExportParams();
        // 设置名称
        inspectTypeExport.setSheetName("检验任务类型表");
        // 创建InspectType -> map
        Map<String, Object> inspectExportMap = new HashMap<>();
        // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
        inspectExportMap.put("title", inspectTypeExport);
        // 模版导出对应得实体类型
        inspectExportMap.put("entity", InspectType.class);
        // sheet填充的数据
        inspectExportMap.put("data", inspectTypeList);

        // 创建自定义数据-> map
        List<CustomDataVal> customDataValList = new ArrayList<>();
        for ( InspectType inspectType: inspectTypeList) {
            String bo = new InspectTypeHandleBO(site,inspectType.getInspectType()).getBo();
            QueryWrapper<CustomDataVal> customWrapper = new QueryWrapper<CustomDataVal>();
            customWrapper.eq(CustomDataVal.BO, bo);
            customDataValList.addAll(customDataValService.list(customWrapper));
        }
        ExportParams customExport = new ExportParams();
        customExport.setSheetName("自定义数据表");
        Map<String, Object> customMap = new HashMap<>();
        customMap.put("title", customExport);
        customMap.put("entity", CustomDataVal.class);
        customMap.put("data", customDataValList);

        // 将sheet1、sheet2使用的map进行包装
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        sheetsList.add(inspectExportMap);
        sheetsList.add(customMap);

        ExcelUtils.exportExcel(sheetsList,"检验任务类型表",response);
    }

    /**
     * 导入文件
     * @param file
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void importExcel(MultipartFile file) throws CommonException {
        List<InspectType> inspectTypeList = ExcelUtils.importExcel(file, 0, 1, InspectType.class);
        List<CustomDataVal> customDataValList = ExcelUtils.importExcel(file, 1,0, 1, CustomDataVal.class);
        if (inspectTypeList.size() == 0) {
            throw new CommonException("质量控制计划表 数据不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        } else {
            super.saveBatch( inspectTypeList );
        }
        if (customDataValList.size() != 0) {
            customDataValService.saveBatch(customDataValList);
        }
    }

    @Override
    public List<InspectType> selectList() {
        QueryWrapper<InspectType> entityWrapper = new QueryWrapper<InspectType>();
        //getEntityWrapper(entityWrapper, inspectType);
        return super.list(entityWrapper);
    }
}
