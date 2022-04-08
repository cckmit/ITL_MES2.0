package com.itl.mes.core.provider.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.OperationHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.OperationVo;
import com.itl.mes.core.provider.mapper.OperationMapper;
import com.itl.mes.core.provider.mapper.QualityPlanParameterMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>
 * 工序表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
@Service
@Transactional
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements OperationService {


    @Autowired
    private OperationMapper operationMapper;
    @Autowired
    private ProductLineService productLineService;
    @Autowired
    private NcGroupService ncGroupService;
    @Autowired
    private NcCodeService ncCodeService;
    @Autowired
    private StationService stationService;
    @Autowired
    private StationTypeService stationTypeService;
    @Autowired
    private CustomDataValService customDataValService;
    @Autowired
    private QualityPlanParameterMapper planParameterMapper;
    @Resource
    private UserUtil userUtil;
    @Autowired
    private WorkShopService workShopService;
    @Override
    public List<Operation> selectList() {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        //getEntityWrapper(entityWrapper, operation);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void saveByOperationVo(OperationVo operationVo) throws CommonException {
        String lineBo =null;
        QualityPlanParameter qualityPlanParameter = new QualityPlanParameter();
        if(!StrUtil.isBlank(operationVo.getProductionLine())){
            ProductLine productLine = productLineService.getExistProductLineByHandleBO(new ProductLineHandleBO( UserUtils.getSite(),operationVo.getProductionLine()));
            lineBo = productLine.getBo();
        };
        String ncGroupBo =null;
        if(!StrUtil.isBlank(operationVo.getNcGroup())){
            NcGroup ncGroup = ncGroupService.selectByNcGroup(operationVo.getNcGroup());
            ncGroupBo = ncGroup.getBo();
        }

        String ncCodeBo =null;
        if(!StrUtil.isBlank(operationVo.getDefaultNcCode())){
            NcCode ncCode = ncCodeService.selectByNcCode(operationVo.getDefaultNcCode());
            ncCodeBo = ncCode.getBo();
        }

        String stationBO =null;
        if(!StrUtil.isBlank(operationVo.getDefaultStation())){
            Station station = stationService.selectByStation(operationVo.getDefaultStation());
            stationBO =station.getBo();
        }

        String stationTypeBO =null;
                if(!StrUtil.isBlank(operationVo.getStationType())){
                    StationType stationType = stationTypeService.selectByStationType(operationVo.getStationType());
                    stationTypeBO = stationType.getBo();
                }
        OperationHandleBO operationHandleBO = new OperationHandleBO(UserUtils.getSite(),operationVo.getOperation(),operationVo.getVersion());
        String bo = operationHandleBO.getBo();
        Operation entityOperation = operationMapper.selectById(bo);
        Operation operation = new Operation();
        if(entityOperation==null){
            operation.setBo(bo);
            operation.setSite(UserUtils.getSite());
            operation.setOperation(operationVo.getOperation());
            operation.setOperationName(operationVo.getOperationName());
            operation.setOperationDesc(operationVo.getOperationDesc());
            operation.setOperationType(operationVo.getOperationType());
            operation.setVersion(operationVo.getVersion());
            operation.setProductionLineBo(lineBo);
            operation.setDefaultStationBo(stationBO);
            operation.setStationTypeBo(stationTypeBO);
            operation.setIsCurrentVersion(operationVo.getIsCurrentVersion());
            operation.setState(operationVo.getState());
            operation.setDefaultNcCodeBo(ncCodeBo);
            operation.setMaxTimes(operationVo.getMaxTimes());
            operation.setRepeatTestTimes(operationVo.getRepeatTestTimes());
            operation.setNcGroupBo(ncGroupBo);
            Date newDate = new Date();
            operation.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(operation);
            if( validResult.hasErrors() ){
                throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if(operation.getIsCurrentVersion().equals("Y")){
               updateOperationVersionType(operation.getOperation());
            }
            operation.setWorkShop(operationVo.getWorkShop());
            //当前工序是否被引用，默认为0未被引用
            operation.setIsUsed(0);
            operationMapper.insert(operation);
            operationVo.setModifyDate(newDate);
        }else{
            Date frontModifyDate = operationVo.getModifyDate();
            Date modifyDate = entityOperation.getModifyDate();
            /*CommonUtil.compareDateSame(frontModifyDate,modifyDate);*/
            operation.setBo(entityOperation.getBo());
            operation.setSite(UserUtils.getSite());
            operation.setOperation(operationVo.getOperation());
            operation.setOperationName(operationVo.getOperationName());
            operation.setOperationDesc(operationVo.getOperationDesc());
            operation.setOperationType(operationVo.getOperationType());
            operation.setVersion(operationVo.getVersion());
            operation.setProductionLineBo(lineBo);
            operation.setDefaultStationBo(stationBO);
            operation.setStationTypeBo(stationTypeBO);
            operation.setIsCurrentVersion(operationVo.getIsCurrentVersion());
            operation.setState(operationVo.getState());
            operation.setDefaultNcCodeBo(ncCodeBo);
            operation.setMaxTimes(operationVo.getMaxTimes());
            operation.setRepeatTestTimes(operationVo.getRepeatTestTimes());
            operation.setNcGroupBo(ncGroupBo);
            Date newDate = new Date();
            operation.setCreateDate(entityOperation.getCreateDate());
            operation.setCreateUser(entityOperation.getCreateUser());
            operation.setModifyUser(userUtil.getUser().getUserName());
            operation.setModifyDate(newDate);
            ValidationUtil.ValidResult validResult =ValidationUtil.validateBean(operation);
            if( validResult.hasErrors() ){
                throw new CommonException( validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION );
            }
            if(operation.getIsCurrentVersion().equals("Y")){
                updateOperationVersionType(operation.getOperation());
            }
            operation.setWorkShop(operationVo.getWorkShop());
            Integer update = operationMapper.updateById(operation);
            qualityPlanParameter.setOperationName(operation.getOperationName());
            planParameterMapper.update(qualityPlanParameter,new QueryWrapper<QualityPlanParameter>().eq("OPERATION_BO",operation.getBo()));
            if(update==0){
                throw new CommonException( "数据已修改，请重新查询再执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION );
            }
            operationVo.setModifyDate(newDate);
        }

        //保存自定义数据
        if( operationVo.getCustomDataValVoList()!=null ){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( bo );
            customDataValRequest.setSite( UserUtils.getSite() );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.OPERATION.getDataType() );
            customDataValRequest.setCustomDataValVoList( operationVo.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }

    }

    @Override
    public Operation selectByOperation(String operation,String version) throws CommonException {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        entityWrapper.eq(Operation.OPERATION,operation);
        entityWrapper.eq(Operation.VERSION,version);
        entityWrapper.eq(Operation.SITE,UserUtils.getSite());
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        if(operations.isEmpty()){
            throw new CommonException("工序:"+operation+","+version+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return operations.get(0);
        }

    }

    /**
     * 查询工序当前版本
     *
     * @param site 工厂
     * @param operation 工序
     * @return Operation
     */
    @Override
    public Operation getBasicCurrentOperation( String site, String operation ){

        QueryWrapper<Operation> wrapper = new QueryWrapper<>();
        wrapper.eq( Operation.SITE, site ).eq( Operation.OPERATION, operation ).eq( Operation.IS_CURRENT_VERSION,"Y" );
        List<Operation> operationList = operationMapper.selectList( wrapper );
        if( operationList.size() >0 ){
            return operationList.get( 0 );
        }
        return null;
    }

    @Override
    public OperationVo getOperationVoByOperationAndVersion(String operation, String version) throws CommonException {
        Operation operationEntity = selectByOperation(operation, version);
        ProductLine productLine = productLineService.getById(operationEntity.getProductionLineBo());
        StationType stationType = stationTypeService.getById(operationEntity.getStationTypeBo());
        Station station = stationService.getById(operationEntity.getDefaultStationBo());
        NcCode ncCode = ncCodeService.getById(operationEntity.getDefaultNcCodeBo());
        NcGroup ncGroup = ncGroupService.getById(operationEntity.getNcGroupBo());
        String workShopDesc = null;
        WorkShop workShopEntity = workShopService.getOne(new QueryWrapper<WorkShop>().eq("work_shop", operationEntity.getWorkShop()));
        if (ObjectUtil.isNotEmpty(workShopEntity)){
            workShopDesc = workShopEntity.getWorkShopDesc();
        }
        OperationVo operationVo = new OperationVo();
        BeanUtils.copyProperties(operationEntity,operationVo);
        if(productLine !=null){
            operationVo.setProductionLine(productLine.getProductLine());
        }
        if(stationType !=null){
           operationVo.setStationType(stationType.getStationType());
        }
        if(station !=null){
           operationVo.setDefaultStation(station.getStation());
        }
        if(ncCode !=null){
            operationVo.setDefaultNcCode(ncCode.getNcCode());
        }
        if(ncGroup !=null){
           operationVo.setNcGroup(ncGroup.getNcGroup());
        }
        operationVo.setWorkShopName(workShopDesc);
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), operationEntity.getBo(), CustomDataTypeEnum.OPERATION.getDataType());
        operationVo.setCustomDataAndValVoList(customDataAndValVos);
        return operationVo;
    }

    @Override
    public List<Operation> selectOperation(String operation, String operationName, String operationDesc, String operationType, String productionLine, String version) {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        entityWrapper.eq(Operation.SITE,UserUtils.getSite());
        entityWrapper.like(Operation.OPERATION,operation);
        entityWrapper.like(Operation.OPERATION_NAME,operationName);
        entityWrapper.like(Operation.OPERATION_DESC,operationDesc);
        entityWrapper.like(Operation.OPERATION_TYPE,operationType);
        entityWrapper.like(Operation.PRODUCTION_LINE_BO,productionLine);
        entityWrapper.like(Operation.VERSION,version);
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        return operations;
    }

    @Override
    public List<Operation> selectByOperation(String operation) {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        entityWrapper.eq(Operation.SITE,UserUtils.getSite());
        entityWrapper.like(Operation.OPERATION,operation);
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        return operations;
    }

    @Override
    public List<Operation> selectByOperation(Map<String,String> map) {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
        String operation = map.get("operation");
        String operationName = map.get("operationName");
        String operationDesc = map.get("operationDesc");
        String version = map.get("version");
        entityWrapper.eq(Operation.SITE,UserUtils.getSite());
        entityWrapper.like(Operation.OPERATION,operation);
        entityWrapper.like(Operation.OPERATION_NAME,operationName);
        entityWrapper.like(Operation.OPERATION_DESC,operationDesc);
        entityWrapper.like(Operation.VERSION,version);
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        return operations;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public int delete(String operation,String version,String modifyDate) throws CommonException {
      OperationHandleBO operationHandleBO = new OperationHandleBO(UserUtils.getSite(),operation,version);
        Operation operationEntity = operationMapper.selectById(operationHandleBO.getBo());
        if(operationEntity==null)throw new CommonException("此工序:"+operation+"版本:"+version+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
/*
        CommonUtil.compareDateSame(DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS),operationEntity.getModifyDate());
*/
        // 删除前检测当前工序是否被引用
        if(operationEntity.getIsUsed()==1){
            throw new CommonException("当前工序被引用不允许删除",30002);
        }
        Integer deleteInt = operationMapper.deleteById(operationEntity.getBo());
        return deleteInt;
    }



    @Override
    public List<NcCode> selectByNcCode(String ncCode, String ncName,String ncDesc) throws CommonException {
        List<NcCode> ncCodes = ncCodeService.selectNcCode(ncCode, ncName, ncDesc);
        return ncCodes;
    }

    @Override
    public List<NcGroup> selectByNcGroup(String ncGroup, String ncGroupName ,String ncGroupDesc) throws CommonException {
        List<NcGroup> ncGroups = ncGroupService.selectNcGroup(ncGroup, ncGroupName, ncGroupDesc);
        return ncGroups;
    }

    @Override
    public List<Station> selectStation(String station, String stationName, String stationDesc) throws CommonException {
        List<Station> stations = stationService.selectStation(station, stationName, stationDesc);
        return stations;
    }

    @Override
    public List<StationType> selectStationType(String stationType, String stationTypeName, String stationTypeDesc) throws CommonException {
        List<StationType> stationTypes = stationTypeService.selectStationType(stationType, stationTypeName, stationTypeDesc);
        return stationTypes;
    }

    @Override
    public void updateOperationVersionType(String operation) throws CommonException {
        Integer integer = operationMapper.updateOperationVersionType(UserUtils.getSite(), operation);
    }

    /**
     * 获取当前版本工序，返回字段包含：OPERATION：工序，OPERATION_DESC：工序描述
     *
     * @param site 工厂
     * @return List<Map<String,Object>>
     */
    @Override
    public List<Map<String, Object>> selectCurrentVersionOperations( String site ) {

       return operationMapper.selectCurrentVersionOperations( site );
    }

    @Override
    public List<Operation> getOperationByStationTypeBO(String stationTypeBO) throws CommonException {
        QueryWrapper<Operation> entityWrapper = new QueryWrapper<Operation>();
         entityWrapper.eq(Operation.STATION_TYPE_BO, stationTypeBO);
        List<Operation> operations = operationMapper.selectList(entityWrapper);
        return operations;
    }



    @Override
    public void exportOperation(String site, HttpServletResponse response) {
        Workbook workBook = null;
        try {
            //获取物料数据源
            QueryWrapper<Operation> operationEntityWrapper = new QueryWrapper<>();
            operationEntityWrapper.eq(Item.SITE, site);
            List<Operation> operations = operationMapper.selectList(operationEntityWrapper);
            // 创建参数对象（用来设定excel得sheet得内容等信息）
            ExportParams operationExportParams = new ExportParams();
            // 设置sheet得名称
            operationExportParams.setSheetName("工序");
            // 创建sheet1使用得map
            Map<String, Object> operationExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            operationExportMap.put("title", operationExportParams);
            // 模版导出对应得实体类型
            operationExportMap.put("entity", Operation.class);
            // sheet中要填充得数据
            operationExportMap.put("data", operations);

            //获取customDataVal数据源
            List<CustomDataVal> customDataVals = customDataValService.selectList();
            ExportParams customDataValExportParams = new ExportParams();
            customDataValExportParams.setSheetName("自定义数据的值");
            // 创建sheet4使用得map
            Map<String, Object> customDataValExportMap = new HashMap<>();
            customDataValExportMap.put("title", customDataValExportParams);
            customDataValExportMap.put("entity", CustomDataVal.class);
            customDataValExportMap.put("data", customDataVals);

            // 将sheet1、sheet2、sheet3使用得map进行包装
            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(operationExportMap);
            sheetsList.add(customDataValExportMap);

            // 执行方法
            workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            //String fileName = URLEncoder.encode("物料表.xls", "UTF-8");
            String fileName = "工序表.xls";
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workBook.write(response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public IPage<OperationOrder> selectAllOrderOperation(SfcDto sfcDto) {
        if (ObjectUtil.isEmpty(sfcDto.getPage())) {
            sfcDto.setPage(new Page(0, 1));
        }
        String workShop = operationMapper.selectWorkShopByUserName(userUtil.getUser().getUserName());
        sfcDto.setWorkShop(workShop);
        IPage<OperationOrder> page=operationMapper.selectAllOrderOperation(sfcDto.getPage(),sfcDto);
        return page;
    }

    @Override
    public String selectShopOrderByOperationOrder(String operationOrder) {
        return operationMapper.selectShopOrderByOperationOrder(operationOrder);
    }

}