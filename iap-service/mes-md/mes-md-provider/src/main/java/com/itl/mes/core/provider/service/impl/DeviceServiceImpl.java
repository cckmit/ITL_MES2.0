package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.*;
import com.itl.mes.core.api.bo.DeviceHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.DeviceService;
import com.itl.mes.core.api.service.DeviceTypeItemService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.DeviceTypeSimplifyVo;
import com.itl.mes.core.api.vo.DeviceTypeVo;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.provider.mapper.*;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 设备表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Service
@Transactional
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {


    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private DeviceTypeItemService deviceTypeItemService;

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private ProductLineServiceImpl productLineService;

    @Autowired
    private StationServiceImpl stationService;

    @Resource
    private UserUtil userUtil;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private WorkShopMapper workShopMapper;

    @Autowired
    private ProductLineMapper productLineMapper;


    @Override
    public IPage<Device> selectDeviceWorkshop(
            DeviceDto deviceDto) {
        if (ObjectUtil.isEmpty(deviceDto.getPage())) {
            deviceDto.setPage(new Page(0, 10));
        }
        if (StringUtils.isNotEmpty(deviceDto.getWorkShop())) {
            deviceDto.setWorkShop(new WorkShopHandleBO(UserUtils.getSite(), deviceDto.getWorkShop()).getBo());
        }
        deviceDto.setSite(UserUtils.getSite());
        return deviceMapper.selectDeviceWorkshop(deviceDto.getPage(), deviceDto);
    }

    @Override
    public IPage<Device> query(DeviceDto deviceDto) {
        if (ObjectUtil.isEmpty(deviceDto.getPage())) {
            deviceDto.setPage(new Page(0, 10));
        }
        deviceDto.setSite("dongyin");
        IPage<Device> page = new com.itl.mes.core.provider.service.impl.Page();
        List<Device> records = deviceMapper.selectByCondition(deviceDto);
        Map<String,Device> map=new HashMap<>();
        int t=0;


        // 拼接设备类型
        for (Device record : records) {
            String filePath = record.getDeviceImage();
            WorkShop workShop = workShopMapper.selectOne(new QueryWrapper<WorkShop>().eq("WORK_SHOP",record.getWorkShop()).eq("SITE","dongyin"));
            if (workShop!=null){
                record.setWorkShopBo(workShop.getBo());
            }
            // map中无此设备bo，新增
           if(map.get(record.getBo()) == null){
               DeviceType deviceType=new DeviceType();
               deviceType.setBo(record.getDeviceTypeBo());
               deviceType.setDeviceType(record.getDeviceType());
               deviceType.setDeviceTypeName(record.getDeviceTypeName());
               record.setDeviceTypeList(new ArrayList<DeviceType>());
               if(deviceType.getBo()!=null || deviceType.getDeviceType() !=null || deviceType.getDeviceTypeName()!=null){
                   record.getDeviceTypeList().add(deviceType);
               }
               map.put(record.getBo(),record);
           }else {
               // 拼接设备类型
               String deviceTypeName=map.get(record.getBo()).getDeviceTypeName();
               map.get(record.getBo()).setDeviceTypeName(deviceTypeName+","+record.getDeviceTypeName());
               DeviceType deviceType=new DeviceType();
               deviceType.setBo(record.getDeviceTypeBo());
               deviceType.setDeviceType(record.getDeviceType());
               deviceType.setDeviceTypeName(record.getDeviceTypeName());
               if( map.get(record.getBo()).getDeviceTypeList()!=null){
                   map.get(record.getBo()).getDeviceTypeList().add(deviceType);
               }else {
                   map.get(record.getBo()).setDeviceTypeList(new ArrayList<>());
                   map.get(record.getBo()).getDeviceTypeList().add(deviceType);
               }

           }
        }

        // 将map中的值放入List中
        List<Device> deviceList=new ArrayList<>();
        map.forEach((key,value)->{
            deviceList.add(value);
        });
        List<Device> subList=new ArrayList<>();
        int current=Integer.parseInt(deviceDto.getPage().getCurrent()+"");
        int size =Integer.parseInt(deviceDto.getPage().getSize()+"");
        if(deviceList !=null &&deviceList.size()>0){
            if(current*size<=deviceList.size()){
                subList=deviceList.subList((current-1)*size,current*size);
            }else {
                subList=deviceList.subList((current-1)*size,deviceList.size());
            }

        }
        //截取子字符串
        page.setCurrent(current);
        page.setSize(size);
        page.setPages( new Double(Math.ceil((double) deviceList.size()/size)).longValue());
        page.setTotal(deviceList.size());
        page.setRecords(subList);
        return page;
    }

    @Override
    public IPage getScrewByLine(Map<String, Object> params) {
        Page page = new Page(Integer.valueOf(params.get("page").toString()),Integer.valueOf(params.get("pageSize").toString()));
        IPage screwByLine = deviceMapper.getScrewByLine(page, params);
        return screwByLine;
    }

    /**
     * 验证设备数据是否合规
     *
     * @param device
     * @throws CommonException
     */
    void validateDevice(Device device) throws CommonException {

        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(device);
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

    }

    /**
     * 验证设备指定属性数据格式是否合规
     *
     * @param device
     * @param fields
     * @throws CommonException
     */
    void validateDeviceFields(Device device, String... fields) throws CommonException {
        ValidationUtil.ValidResult validResult = null;
        for (int i = 0; i < fields.length; i++) {
            validResult = ValidationUtil.validateProperty(device, fields[i]);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
    }

    @Override
    public List<Device> selectList() {
        QueryWrapper<Device> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, device);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveDevice(DeviceVo deviceVo) throws CommonException {
        String site = UserUtils.getSite(); //获取工厂

        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(UserUtils.getSite(), deviceVo.getDevice());
        Device deviceEntity = super.getById(deviceHandleBO.getBo());//查询设备数据

        if (deviceEntity == null) {
            //新增
            insertDevice(deviceVo);
        } else {
            //修改
            updateDevice(deviceEntity, deviceVo);
        }

        deviceTypeItemService.save(deviceHandleBO.getBo(), deviceVo.getAssignedDeviceTypeList());


        //保存自定义数据
        if (deviceVo.getCustomDataValVoList() != null) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(deviceHandleBO.getBo());
            customDataValRequest.setSite(site);
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.DEVICE.getDataType());
            customDataValRequest.setCustomDataValVoList(deviceVo.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
    }

    private void updateDevice(Device deviceEntity, DeviceVo deviceVo) throws CommonException {
//        Date frontModifyDate = deviceVo.getModifyDate(); //前台传递的时间值
//        CommonUtil.compareDateSame(frontModifyDate, deviceEntity.getModifyDate()); //比较时间是否相等

        Date newModifyDate = new Date();
        String site = UserUtils.getSite();
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(site, deviceVo.getDevice());
        Device device = new Device();

        device.setBo(deviceHandleBO.getBo());
        device.setSite(UserUtils.getSite());
        device.setDevice(deviceVo.getDevice());
        device.setDeviceName(deviceVo.getDeviceName());
        device.setDeviceDesc(deviceVo.getDeviceDesc());
        device.setDeviceModel(deviceVo.getDeviceModel());
        device.setState(deviceVo.getState());
        device.setWorkShop(deviceVo.getWorkShop());
        device.setWorkShopName(deviceVo.getWorkShopName());
        device.setItemBo(deviceVo.getItemBo());
        if (!StrUtil.isBlank(deviceVo.getProductLine())) {
            device.setProductLineBo(productLineService.getExistProductLineByHandleBO(new ProductLineHandleBO(site, deviceVo.getProductLine())).getBo());
        }
        if (!StrUtil.isBlank(deviceVo.getStation())) {
            device.setStationBo(stationService.selectByStation(deviceVo.getStation()).getBo());
        }
        device.setLocation(deviceVo.getLocation());
        device.setAssetsCode(deviceVo.getAssetsCode());
        device.setManufacturer(deviceVo.getManufacturer());
        device.setValidStartDate(deviceVo.getValidStartDate());

        device.setMemo(deviceVo.getMemo());
        device.setModifyUser(userUtil.getUser().getUserName());
        device.setModifyDate(newModifyDate);

        device.setDeviceType(deviceVo.getDeviceType());
        device.setProductionDate(deviceVo.getProductionDate());
        device.setOperationBo(deviceVo.getOperationBo());
        device.setJoinFactoryDate(deviceVo.getJoinFactoryDate());
        device.setResponsiblePerson(deviceVo.getResponsiblePerson());
        device.setDeviceImage(deviceVo.getDeviceImage());


        validateDeviceFields(device, "deviceName", "deviceDesc", "deviceModel", "state");
        boolean successFlag = super.updateById(device);
        if (!successFlag) {
            throw new CommonException("设备编号" + deviceVo.getDevice() + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    private void insertDevice(DeviceVo deviceVo) throws CommonException {
        String site = UserUtils.getSite();
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(site, deviceVo.getDevice());
        Device device = new Device();

        device.setBo(deviceHandleBO.getBo());
        device.setSite(UserUtils.getSite());
        device.setDevice(deviceVo.getDevice());
        device.setDeviceName(deviceVo.getDeviceName());
        device.setDeviceDesc(deviceVo.getDeviceDesc());
        device.setDeviceModel(deviceVo.getDeviceModel());
        device.setState(deviceVo.getState());
        device.setWorkShop(deviceVo.getWorkShop());
        device.setWorkShopName(deviceVo.getWorkShopName());
        device.setItemBo(deviceVo.getItemBo());
        if (!StrUtil.isBlank(deviceVo.getProductLine())) {
            device.setProductLineBo(productLineService.getExistProductLineByHandleBO(new ProductLineHandleBO(site, deviceVo.getProductLine())).getBo());
        }
        if (!StrUtil.isBlank(deviceVo.getStation())) {
            device.setStationBo(stationService.selectByStation(deviceVo.getStation()).getBo());
        }
        device.setLocation(deviceVo.getLocation());
        device.setAssetsCode(deviceVo.getAssetsCode());
        device.setManufacturer(deviceVo.getManufacturer());
        device.setValidStartDate(deviceVo.getValidStartDate());

        device.setMemo(deviceVo.getMemo());
        device.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());

        device.setDeviceType(deviceVo.getDeviceType());
        device.setProductionDate(deviceVo.getProductionDate());
        device.setOperationBo(deviceVo.getOperationBo());
        device.setJoinFactoryDate(deviceVo.getJoinFactoryDate());
        device.setResponsiblePerson(deviceVo.getResponsiblePerson());
        device.setDeviceImage(deviceVo.getDeviceImage());

        validateDevice(device);
        deviceMapper.insert(device);
    }

    //通过 device 设备编号查询
    @Override
    public DeviceVo getDeviceVoByDevice(String device) throws CommonException {
        Device deviceEntity = selectByDevice(device);
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), deviceEntity.getBo(), CustomDataTypeEnum.DEVICE.getDataType());
        DeviceVo deviceVo = new DeviceVo();
        BeanUtils.copyProperties(deviceEntity, deviceVo);
        deviceVo.setCustomDataAndValVoList(customDataAndValVos);
        List<Map<String, Object>> availableDeviceTypeList = deviceMapper.getAvailableDeviceTypeList(UserUtils.getSite(), deviceEntity.getBo());
        List<DeviceTypeSimplifyVo> available = new ArrayList<>();
        if (!availableDeviceTypeList.isEmpty()) {
            for (Map<String, Object> map : availableDeviceTypeList) {
                DeviceTypeSimplifyVo deviceTypeSimplifyVo = new DeviceTypeSimplifyVo();
                if(map.get("DEVICE_TYPE") !=null && map.get("DEVICE_TYPE") !=""){
                    deviceTypeSimplifyVo.setDeviceType(map.get("DEVICE_TYPE").toString());
                }
                if(map.get("DEVICE_TYPE_NAME") !=null && map.get("DEVICE_TYPE_NAME") !=""){
                    deviceTypeSimplifyVo.setDeviceTypeName(map.get("DEVICE_TYPE_NAME").toString());
                }
                if(map.get("DEVICE_TYPE_DESC") !=null && map.get("DEVICE_TYPE_DESC") !=""){
                    deviceTypeSimplifyVo.setDeviceTypeDesc(map.get("DEVICE_TYPE_DESC").toString());
                }
                available.add(deviceTypeSimplifyVo);
            }
        }
        List<Map<String, Object>> assignedDeviceTypeList = deviceMapper.getAssignedDeviceTypeList(UserUtils.getSite(), deviceEntity.getBo());
        List<DeviceTypeSimplifyVo> assigned = new ArrayList<>();
        if (!assignedDeviceTypeList.isEmpty()) {
            for (Map<String, Object> map : assignedDeviceTypeList) {
                DeviceTypeSimplifyVo deviceTypeSimplifyVo = new DeviceTypeSimplifyVo();
                if(map.get("DEVICE_TYPE") !=null && map.get("DEVICE_TYPE") !=""){
                    deviceTypeSimplifyVo.setDeviceType(map.get("DEVICE_TYPE").toString());
                }
                if(map.get("DEVICE_TYPE_NAME") !=null && map.get("DEVICE_TYPE_NAME") !=""){
                    deviceTypeSimplifyVo.setDeviceTypeName(map.get("DEVICE_TYPE_NAME").toString());
                }
                if(map.get("DEVICE_TYPE_DESC") !=null && map.get("DEVICE_TYPE_DESC") !=""){
                    deviceTypeSimplifyVo.setDeviceTypeDesc(map.get("DEVICE_TYPE_DESC").toString());
                }
                assigned.add(deviceTypeSimplifyVo);
            }
        }
        deviceVo.setAssignedDeviceTypeList(assigned);
        deviceVo.setAvailableDeviceTypeList(available);
        deviceVo.setDevice(deviceEntity.getDevice());
        deviceVo.setDeviceName(deviceEntity.getDeviceName());
        deviceVo.setDeviceDesc(deviceEntity.getDeviceDesc());
        deviceVo.setDeviceModel(deviceEntity.getDeviceModel());
        deviceVo.setState(deviceEntity.getState());
        if (!StrUtil.isBlank(deviceEntity.getProductLineBo())) {
            deviceVo.setProductLine(split(deviceEntity.getProductLineBo()));
        }
        if (!StrUtil.isBlank(deviceEntity.getStationBo())) {
            deviceVo.setStation(split(deviceEntity.getStationBo()));
        }
        deviceVo.setLocation(deviceEntity.getLocation());
        deviceVo.setAssetsCode(deviceEntity.getAssetsCode());
        deviceVo.setManufacturer(deviceEntity.getManufacturer());
        deviceVo.setValidStartDate(deviceEntity.getValidStartDate());
        deviceVo.setMemo(deviceEntity.getMemo());
        deviceVo.setModifyDate(deviceEntity.getModifyDate());
        return deviceVo;
    }

    //删除
    @Override
    public void deleteDevice(String device) throws CommonException {
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(UserUtils.getSite(), device);
        Device deviceEntity = deviceMapper.selectById(deviceHandleBO.getBo());
        if (deviceEntity == null) {
            throw new CommonException("设备" + device + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
      //  CommonUtil.compareDateSame(modifyDate, deviceEntity.getModifyDate());
        deviceMapper.deleteById(deviceEntity.getBo());
        deviceTypeItemService.deleteByDeviceBO(deviceEntity.getBo());
    }

    //查询500条设备类型数据供页面初始化使用
    @Override
    public List<DeviceTypeVo> getDeviceTypeVoList() {
        //RowBounds rowBounds = new RowBounds(0, CustomCommonConstants.EVERY_TIMES_SEARCH_MOST_SIZE);
        //EntityWrapper<DeviceType> entityWrapper = new EntityWrapper<DeviceType>();
        //entityWrapper.eq(DeviceType.SITE, UserUtils.getSite());
        //List<DeviceType> deviceTypes = deviceTypeMapper.selectPage(rowBounds, entityWrapper);
        List<DeviceType> deviceTypes = deviceTypeMapper.selectTop(UserUtils.getSite());
        List<DeviceTypeVo> deviceTypeVos = new ArrayList<>();
        if (!deviceTypes.isEmpty()) {
            for (DeviceType deviceType : deviceTypes) {
                DeviceTypeVo deviceTypeVo = new DeviceTypeVo();
                BeanUtils.copyProperties(deviceType, deviceTypeVo);
                deviceTypeVos.add(deviceTypeVo);
            }
        }
        return deviceTypeVos;
    }

    public Device selectByDevice(String device) throws CommonException {
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(UserUtils.getSite(), device);
        Device deviceEntity = deviceMapper.selectById(deviceHandleBO.getBo());
        if (deviceEntity == null) {
            throw new CommonException("设备编号:" + device + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return deviceEntity;
    }

    //切割BO 获取 第二个值
    public String split(String bo) {
        String[] split = bo.split(",");
        return split[1];
    }

    //通过设备编号查询已分配设备类型和可分配设备类型
    @Override
    public DeviceVo getDeviceVoTypeByDevice(String device) throws CommonException {
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(UserUtils.getSite(), device);
        DeviceVo deviceVo = new DeviceVo();
        List<Map<String, Object>> availableDeviceTypeList = deviceMapper.getAvailableDeviceTypeList(UserUtils.getSite(), deviceHandleBO.getBo());
        List<DeviceTypeSimplifyVo> available = new ArrayList<>();
        if (!availableDeviceTypeList.isEmpty()) {
            for (Map<String, Object> map : availableDeviceTypeList) {
                DeviceTypeSimplifyVo deviceTypeSimplifyVo = new DeviceTypeSimplifyVo();
                if(map.get("DEVICE_TYPE") !=null && map.get("DEVICE_TYPE") !=""){
                    deviceTypeSimplifyVo.setDeviceType(map.get("DEVICE_TYPE").toString());
                }
                if(map.get("DEVICE_TYPE_NAME") !=null && map.get("DEVICE_TYPE_NAME") !=""){
                    deviceTypeSimplifyVo.setDeviceTypeName(map.get("DEVICE_TYPE_NAME").toString());
                }
                if(map.get("DEVICE_TYPE_DESC") !=null && map.get("DEVICE_TYPE_DESC") !=""){
                    deviceTypeSimplifyVo.setDeviceTypeDesc(map.get("DEVICE_TYPE_DESC").toString());
                }

                available.add(deviceTypeSimplifyVo);
            }
        }
        List<Map<String, Object>> assignedDeviceTypeList = deviceMapper.getAssignedDeviceTypeList(UserUtils.getSite(), deviceHandleBO.getBo());
        List<DeviceTypeSimplifyVo> assigned = new ArrayList<>();
        if (!assignedDeviceTypeList.isEmpty()) {
            for (Map<String, Object> map : assignedDeviceTypeList) {
                DeviceTypeSimplifyVo deviceTypeSimplifyVo = new DeviceTypeSimplifyVo();
                if(map.get("DEVICE_TYPE") !=null && map.get("DEVICE_TYPE") !=""){
                    deviceTypeSimplifyVo.setDeviceType(map.get("DEVICE_TYPE").toString());
                }
                if(map.get("DEVICE_TYPE_NAME") !=null && map.get("DEVICE_TYPE_NAME") !=""){
                    deviceTypeSimplifyVo.setDeviceTypeName(map.get("DEVICE_TYPE_NAME").toString());
                }
                if(map.get("DEVICE_TYPE_DESC") !=null && map.get("DEVICE_TYPE_DESC") !=""){
                    deviceTypeSimplifyVo.setDeviceTypeDesc(map.get("DEVICE_TYPE_DESC").toString());
                }
                assigned.add(deviceTypeSimplifyVo);
            }
        }
        deviceVo.setAssignedDeviceTypeList(assigned);
        deviceVo.setAvailableDeviceTypeList(available);
        return deviceVo;
    }

    @Override
    public Device getDeviceById(String id) {
        Device device= deviceMapper.selectById(id);
        // 获取工序名称
        if(device.getOperationBo()!=null && !device.getOperationBo().isEmpty()) {
            Operation operation=operationMapper.selectById(device.getOperationBo());
            if(operation !=null){
                String operationName=operation.getOperationName();
                device.setOperationName(operationName);
            }
        }
        // 获取产线名称
        if(StringUtils.isNotBlank(device.getProductLineBo())){
            ProductLine productLine= productLineMapper.selectById(device.getProductLineBo());
            if(productLine !=null){
                device.setProductLineDesc(productLine.getProductLineDesc());
            }
        }
        // 获取物料名称
        if (device.getItemBo()!=null && !device.getItemBo().isEmpty()) {
            Item itemEntity=itemMapper.selectById(device.getItemBo());
            String itemName = itemEntity.getItemName();
            String item=itemEntity.getItem();
            device.setItemName(itemName);
            device.setItem(item);
        }
        // 根据设备bo 从设备类型明细表获取已分配的设备类型
        List<DeviceTypeSimplifyVo> assignedDeviceTypeList= null;
        try {
            assignedDeviceTypeList = getDeviceVoTypeByDevice(device.getDevice()).getAssignedDeviceTypeList();
        } catch (CommonException e) {
            e.printStackTrace();
        }
        String str="";
        int i=0;
        if(assignedDeviceTypeList !=null) {
            for(DeviceTypeSimplifyVo deviceTypeSimplifyVo:assignedDeviceTypeList){
                if(i==assignedDeviceTypeList.size()-1){
                    str+=deviceTypeSimplifyVo.getDeviceType();
                }else{
                    str+=deviceTypeSimplifyVo.getDeviceType()+",";
                }
                i++;
            }
        }
        device.setDeviceType(str);
        //设备类型集合
        List<DeviceTypeItem> deviceTypeItemList = deviceTypeItemService.list(new QueryWrapper<DeviceTypeItem>().eq("device_bo", id));
        List<DeviceType> deviceTypeList = Lists.newArrayList();//设备类型集合
        for (DeviceTypeItem deviceTypeItem : deviceTypeItemList) {
            DeviceType deviceType = deviceTypeMapper.selectOne(new QueryWrapper<DeviceType>().eq("bo", deviceTypeItem.getDeviceTypeBo()));
            deviceTypeList.add(deviceType);
        }
        device.setDeviceTypeList(deviceTypeList);
        return device;
    }

}