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
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.UserStationQueryDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.StationProveVo;
import com.itl.mes.core.api.vo.StationVo;
import com.itl.mes.core.provider.mapper.StationMapper;
import com.itl.mes.core.provider.mapper.StationProveMapper;
import com.itl.mes.core.provider.mapper.StationTypeItemMapper;
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
 * 工位表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-30
 */
@Service
@Transactional
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {


    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private OperationService operationService;
    @Autowired
    public StationTypeService stationTypeService;
    @Autowired
    private CustomDataValService customDataValService;
    @Autowired
    private ProductLineService productLineService;
    @Autowired
    private StationTypeItemMapper stationTypeItemMapper;

    @Autowired
    private StationProveMapper stationProveMapper;

    @Resource
    private UserUtil userUtil;


    @Override
    public List<Station> selectList() {
        QueryWrapper<Station> entityWrapper = new QueryWrapper<Station>();
        //getEntityWrapper(entityWrapper, station);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public void saveByStationVo(StationVo stationVo) throws CommonException {
        String operationBo =null;
        if(!StrUtil.isBlank(stationVo.getOperation())&&!StrUtil.isBlank(stationVo.getOperationVersion())){
            List<Operation> operations = operationService.selectByOperation(stationVo.getOperation());
            if(operations.isEmpty())throw new CommonException("工序不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
            operationBo = new OperationHandleBO(UserUtils.getSite(),stationVo.getOperation(),stationVo.getOperationVersion()).getBo();
        }

        String stationTypeBO=null;
        if(!StrUtil.isBlank(stationVo.getStationType())){
            StationType stationType = stationTypeService.selectByStationType(stationVo.getStationType());
            stationTypeBO = stationType.getBo();
        }

        String productLineBO = null;
        if(!StrUtil.isBlank(stationVo.getProductLine())){
            ProductLine productLine = productLineService.getExistProductLineByHandleBO(new ProductLineHandleBO(UserUtils.getSite(), stationVo.getProductLine()));
            productLineBO =  productLine.getBo();
        }

        StationHandleBO stationHandleBO = new StationHandleBO(UserUtils.getSite(),stationVo.getStation());
        String bo = stationHandleBO.getBo();
        Station entityStation = stationMapper.selectById(bo);

        if(entityStation==null){
            Station station = new Station();
            station.setBo(bo);
            station.setSite(UserUtils.getSite());
            station.setStation(stationVo.getStation());
            station.setStationName(stationVo.getStationName());
            station.setStationDesc(stationVo.getStationDesc());
            station.setOperationBo(operationBo);
            station.setStationTypeBo(stationTypeBO);
            station.setState(stationVo.getState());
            station.setProductLineBo(productLineBO);
            station.setWorkstationBo(stationVo.getWorkstationBo());
            Date newDate = new Date();
            station.setObjectSetBasicAttribute(userUtil.getUser().getUserName(),newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(station);
            stationMapper.insert(station);
            stationVo.setModifyDate(newDate);
        }else{
            Date frontModifyDate = stationVo.getModifyDate();
            Date modifyDate = entityStation.getModifyDate();
            /*CommonUtil.compareDateSame(frontModifyDate,modifyDate);*/
            Station station = new Station();
            station.setBo(bo);
            station.setSite(UserUtils.getSite());
            station.setStation(stationVo.getStation());
            station.setStationName(stationVo.getStationName());
            station.setStationDesc(stationVo.getStationDesc());
            station.setOperationBo(operationBo);
            station.setStationTypeBo(stationTypeBO);
            station.setState(stationVo.getState());
            station.setProductLineBo(productLineBO);
            station.setWorkstationBo(stationVo.getWorkstationBo());
            Date newDate = new Date();
            station.setCreateUser(entityStation.getCreateUser());
            station.setCreateDate(entityStation.getCreateDate());
            station.setModifyUser(userUtil.getUser().getUserName());
            station.setModifyDate(newDate);
            ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(station);
            Integer updateInt = stationMapper.updateById(station);
            if(updateInt==0){
                throw new CommonException( "数据已修改，请重新查询再执行保存操作", CommonExceptionDefinition.BASIC_EXCEPTION );
            }
            stationVo.setModifyDate(newDate);
        }
        //保存自定义数据
        if( stationVo.getCustomDataValVoList()!=null ){
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo( bo );
            customDataValRequest.setSite( UserUtils.getSite() );
            customDataValRequest.setCustomDataType( CustomDataTypeEnum.STATION.getDataType() );
            customDataValRequest.setCustomDataValVoList( stationVo.getCustomDataValVoList() );
            customDataValService.saveCustomDataVal( customDataValRequest );
        }

    }

    @Override
    public Station selectByStation(String station) throws CommonException {
        QueryWrapper<Station> entityWrapper = new QueryWrapper<Station>();
        entityWrapper.eq(Operation.SITE,UserUtils.getSite());
        entityWrapper.eq(Station.STATION,station);
        List<Station> stations = stationMapper.selectList(entityWrapper);
        if(stations.isEmpty()){
           throw new CommonException("工位:"+station+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            return stations.get(0);
        }

    }

    @Override
    public StationVo getStationVoByStation(String station) throws CommonException {
        Station stationEntity = selectByStation(station);
        StationVo stationVo = new StationVo();
        BeanUtils.copyProperties(stationEntity,stationVo);
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), stationEntity.getBo(), CustomDataTypeEnum.STATION.getDataType());
        if(stationEntity.getOperationBo()!=null&&!stationEntity.getOperationBo().equals("")) {
            OperationHandleBO operation = new OperationHandleBO(stationEntity.getOperationBo());
            if(operation !=null) {
                stationVo.setOperation(operation.getOperation());
                stationVo.setOperationVersion(operation.getVersion());
            }
        }
        if(stationEntity.getStationTypeBo()!=null&&!stationEntity.getStationTypeBo().equals("")) {
            StationType stationType = stationTypeService.getById(stationEntity.getStationTypeBo());
            if (stationType != null){
                stationVo.setStationType(stationType.getStationType());
            }
        }
        if(stationEntity.getProductLineBo()!=null&&!stationEntity.getProductLineBo().equals("")) {
            ProductLine productLine = productLineService.getById(stationEntity.getProductLineBo());
            if(productLine != null){
                stationVo.setProductLine(productLine.getProductLine());
            }
        }
        stationVo.setCustomDataAndValVoList(customDataAndValVos);
            return stationVo;
    }


    @Override
    public List<Station> selectByStationVo(StationVo stationVo) {
        QueryWrapper<Station> entityWrapper = new QueryWrapper<Station>();
        entityWrapper.eq(Operation.SITE,UserUtils.getSite());
        entityWrapper.like(Station.STATION,stationVo.getStation());
        entityWrapper.like(Station.STATION_NAME,stationVo.getStationName());
        entityWrapper.like(Station.STATION_DESC,stationVo.getStationDesc());
        List<Station> stations = stationMapper.selectList(entityWrapper);
        return stations;
    }

    @Override
    public List<Station> selectStation(String station,String stationName,String stationDesc) throws CommonException {
        QueryWrapper<Station> entityWrapper = new QueryWrapper<Station>();
        entityWrapper.eq(Operation.SITE,UserUtils.getSite());
        entityWrapper.like(Station.STATION,station);
        entityWrapper.like(Station.STATION_NAME,stationName);
        entityWrapper.like(Station.STATION_DESC,stationDesc);
        List<Station> stations = stationMapper.selectList(entityWrapper);
        return stations;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class} )
    public int delete(String station,String modifyDate) throws CommonException {
        StationHandleBO stationHandleBO = new StationHandleBO(UserUtils.getSite(),station);
        Station entityStation = stationMapper.selectById(stationHandleBO.getBo());
        if(entityStation==null) throw new CommonException("此工序:"+station+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        CommonUtil.compareDateSame( DateUtil.parse( modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS ),entityStation.getModifyDate());
        Integer deleteInt = stationMapper.deleteById(stationHandleBO.getBo());
        return deleteInt;
    }

    @Override
    public List<Operation> selectByOpertion(String operation, String operationName, String operationDesc, String version) throws CommonException {
        Map<String,String> map = new HashMap<String,String>();
        map.put("operation",operation);
        map.put("operationName",operationName);
        map.put("operationDesc",operationDesc);
        map.put("version",version);
        List<Operation> operations = operationService.selectByOperation(map);
        return operations;
    }

    @Override
    public List<StationTypeItem> selectStationByStationTypeBO(String stationTypeBO) {
        QueryWrapper<StationTypeItem> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(StationTypeItem.STATION_TYPE_BO,stationTypeBO);
        List<StationTypeItem> stationTypeItems = stationTypeItemMapper.selectList(entityWrapper);
        return stationTypeItems;
    }



    @Override
    public void exportOperation(String site, HttpServletResponse response) {
        Workbook workBook = null;
        try {
            //获取物料数据源
            QueryWrapper<Station> stationEntityWrapper = new QueryWrapper<>();
            stationEntityWrapper.eq(Item.SITE, site);
            List<Station> stations = stationMapper.selectList(stationEntityWrapper);
            // 创建参数对象（用来设定excel得sheet得内容等信息）
            ExportParams stationExportParams = new ExportParams();
            // 设置sheet得名称
            stationExportParams.setSheetName("工位");
            // 创建sheet1使用得map
            Map<String, Object> stationExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            stationExportMap.put("title", stationExportParams);
            // 模版导出对应得实体类型
            stationExportMap.put("entity", Station.class);
            // sheet中要填充得数据
            stationExportMap.put("data", stations);

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
            sheetsList.add(stationExportMap);
            sheetsList.add(customDataValExportMap);

            // 执行方法
            workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            //String fileName = URLEncoder.encode("物料表.xls", "UTF-8");
            String fileName = "工位表.xls";
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
    public void saveStationProve(StationProveVo stationProveVo) {

        QueryWrapper<StationProve> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("station_bo",stationProveVo.getStationBo());
        stationProveMapper.delete(queryWrapper);
        stationProveVo.getProveIds().forEach(proveId -> {

            StationProve stationProve = new StationProve();
            stationProve.setProveId(proveId);
            stationProve.setStationBo(stationProveVo.getStationBo());
            stationProveMapper.insert(stationProve);
        });

    }

    @Override
    public Map<String, Object> getByStation(String station) {
        return stationMapper.getByStation(station,UserUtils.getSite());
    }

    @Override
    public IPage<Station> findUserStations(UserStationQueryDTO userStationQueryDTO) {
        if(ObjectUtil.isEmpty(userStationQueryDTO.getPage())){
            userStationQueryDTO.setPage(new Page(0, 10));
        }
        return stationMapper.findUserStations(userStationQueryDTO.getPage(),userStationQueryDTO);
    }

    @Override
    public IPage<Station> findUncorrelatedUserStations(UserStationQueryDTO userStationQueryDTO) {
        if(ObjectUtil.isEmpty(userStationQueryDTO.getPage())){
            userStationQueryDTO.setPage(new Page(0, 10));
        }
        return stationMapper.findUncorrelatedUserStations(userStationQueryDTO.getPage(),userStationQueryDTO);
    }

    @Override
    public List<Station> getStationBySiteAndUserName(String site, String userNameOrCardNumber) {
        return stationMapper.getStationBySiteAndUserName(site,userNameOrCardNumber);
    }

    @Override
    public List<Station> getStationBySiteAndCardNumber(String site, String userNameOrCardNumber) {
        return stationMapper.getStationBySiteAndCardNumber(site,userNameOrCardNumber);
    }
}
