package com.itl.mes.pp.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.pp.api.dto.scheduleplan.*;
import com.itl.mes.pp.api.entity.CapacityLoadEntity;
import com.itl.mes.pp.api.entity.scheduleplan.ClassShiftBreakEntity;
import com.itl.mes.pp.api.entity.scheduleplan.ResourcesCalendarEntity;
import com.itl.mes.pp.api.service.ResourcesCalendarService;
import com.itl.mes.pp.provider.config.Constant;
import com.itl.mes.pp.provider.mapper.CapacityLoadMapper;
import com.itl.mes.pp.provider.mapper.ClassShiftBreakMapper;
import com.itl.mes.pp.provider.mapper.ResourcesCalendarMapper;
import com.itl.mes.pp.provider.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author liuchenghao
 * @date 2020/12/1 11:36
 */
@Service
public class ResourcesCalendarServiceImpl implements ResourcesCalendarService {



    @Autowired
    ResourcesCalendarMapper resourcesCalendarMapper;

    @Autowired
    ClassShiftBreakMapper classShiftBreakMapper;

    @Autowired
    CapacityLoadMapper capacityLoadMapper;


    @Override
    public List<WorkShopUnderResourcesRespDTO> workShopUnderResources(String workShopBo) {

        return resourcesCalendarMapper.workShopUnderResources(workShopBo);
    }

    @Override
    public List<ResourcesCalendarClassRespDTO> classResources() {

        List<ClassSystemFrequencyDTO> classSystemFrequencyList= resourcesCalendarMapper.classResources();

        List<ResourcesCalendarClassRespDTO> respDTOS = new ArrayList<>();

        classSystemFrequencyList.forEach(classSystemFrequencyDTO -> {
            //查询出班制信息
            if("1".equals(classSystemFrequencyDTO.getLevel())){
                ResourcesCalendarClassRespDTO resourcesCalendarClassRespDTO = new ResourcesCalendarClassRespDTO();
                resourcesCalendarClassRespDTO.setClassSystemId(classSystemFrequencyDTO.getId());
                resourcesCalendarClassRespDTO.setClassSystemName(classSystemFrequencyDTO.getName());
                List<ClassFrequencyDTO> classFrequencyDTOList = new ArrayList<>();
                //第二次遍历将班次信息绑定到班制信息里
                classSystemFrequencyList.forEach(classFrequency ->{

                    if(classSystemFrequencyDTO.getId().equals(classFrequency.getClassSystemId())){
                        ClassFrequencyDTO classFrequencyDTO = new ClassFrequencyDTO();
                        classFrequencyDTO.setClassFrequencyId(classFrequency.getId());
                        classFrequencyDTO.setClassFrequencyName(classFrequency.getName());
                        classFrequencyDTO.setStartDateStr(classFrequency.getStartDateStr());
                        classFrequencyDTO.setEndDateStr(classFrequency.getEndDateStr());
                        classFrequencyDTO.setFatherId(classFrequency.getClassSystemId());
                        classFrequencyDTOList.add(classFrequencyDTO);
                    }

                });
                resourcesCalendarClassRespDTO.setClassFrequencyDTOList(classFrequencyDTOList);
                respDTOS.add(resourcesCalendarClassRespDTO);
            }
        });

        return respDTOS;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ResourcesCalendarRespDTO> createResourcesCalendars(CreateResourcesCalendarDTO createResourcesCalendarDTO) {

        List<ResourcesCalendarRespDTO> resourcesCalendarRespDTOList = new ArrayList<>();
        createResourcesCalendarDTO.getDates().forEach(date -> {
            int sort = 0;
            for(ClassFrequencyDTO classFrequencyDTO:createResourcesCalendarDTO.getClassFrequencyDTOList()){
                QueryWrapper<ClassShiftBreakEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("CLASS_FREQUENCY_ID",classFrequencyDTO.getClassFrequencyId());
                queryWrapper.orderByAsc("SORT_MARK");
                List<ClassShiftBreakEntity> classShiftBreakEntities = classShiftBreakMapper.selectList(queryWrapper);
                if(CollUtil.isNotEmpty(classShiftBreakEntities)){
                    List<TimeDTO> timeDTOList = conversionTime(classShiftBreakEntities,classFrequencyDTO.getStartDateStr(),classFrequencyDTO.getEndDateStr());
                    for(TimeDTO timeDTO : timeDTOList){
                        ResourcesCalendarRespDTO resourcesCalendarRespDTO = generateResourcesCalendarRespDTO(createResourcesCalendarDTO,classFrequencyDTO);
                        resourcesCalendarRespDTO.setDate(date);
                        resourcesCalendarRespDTO.setStartDateStr(timeDTO.getStartDateStr());
                        resourcesCalendarRespDTO.setEndDateStr(timeDTO.getEndDateStr());
                        String dateStr = DateUtil.format(date,"yyyy-MM-dd");
                        String startDate = dateStr+" "+timeDTO.getStartDateStr()+":00";
                        String endDate = dateStr+" "+timeDTO.getEndDateStr()+":00";
                        Double workHours = getWorkHour(startDate,endDate);
                        resourcesCalendarRespDTO.setWorkHour(workHours);
                        resourcesCalendarRespDTO.setSort(sort);
                        resourcesCalendarRespDTOList.add(resourcesCalendarRespDTO);
                        sort+=1;
                    }
                }else {
                    ResourcesCalendarRespDTO resourcesCalendarRespDTO = generateResourcesCalendarRespDTO(createResourcesCalendarDTO,classFrequencyDTO);
                    resourcesCalendarRespDTO.setDate(date);
                    resourcesCalendarRespDTO.setStartDateStr(classFrequencyDTO.getStartDateStr());
                    resourcesCalendarRespDTO.setEndDateStr(classFrequencyDTO.getEndDateStr());
                    String dateStr = DateUtil.format(date,"yyyy-MM-dd");
                    String startDate = dateStr+" "+classFrequencyDTO.getStartDateStr()+":00";
                    String endDate = dateStr+" "+classFrequencyDTO.getEndDateStr()+":00";
                    Double workHours = getWorkHour(startDate,endDate);
                    resourcesCalendarRespDTO.setWorkHour(workHours);
                    resourcesCalendarRespDTO.setSort(sort);
                    resourcesCalendarRespDTOList.add(resourcesCalendarRespDTO);
                    sort+=1;
                }
            }
        });

        return resourcesCalendarRespDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<ResourcesCalendarEntity> resourcesCalendarEntities) throws Exception {

        if(resourcesCalendarEntities.size()>0){

            Set<Date> dates = new HashSet<>();
            ResourcesCalendarEntity entity = resourcesCalendarEntities.get(0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(entity.getDate());
            QueryWrapper<ResourcesCalendarEntity> queryWrapper = getSaveWrapper(entity,calendar);
            //校验数据是否被引用，被引用不可删除
            QueryWrapper<ResourcesCalendarEntity> checkStateWrapper = (QueryWrapper<ResourcesCalendarEntity>) queryWrapper.clone();
            checkStateWrapper.eq("state",Constant.ResourcesCalendarState.USE.getValue());
            Integer count = resourcesCalendarMapper.selectCount(checkStateWrapper);
            if(count>0){
                throw new Exception("保存失败，有数据已被引用，请检查数据");
            }
            resourcesCalendarMapper.delete(queryWrapper);
            resourcesCalendarEntities.forEach(resourcesCalendarEntity -> {
                Calendar saveCalendar = Calendar.getInstance();
                saveCalendar.setTime(resourcesCalendarEntity.getDate());
                resourcesCalendarEntity.setYear(saveCalendar.get(Calendar.YEAR));
                resourcesCalendarEntity.setMonth(saveCalendar.get(Calendar.MONTH)+1);
                resourcesCalendarEntity.setDay(saveCalendar.get(Calendar.DAY_OF_MONTH));
                resourcesCalendarEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
                resourcesCalendarEntity.setCreateDate(new Date());
                resourcesCalendarMapper.insert(resourcesCalendarEntity);
                //日期集合用于计算机台产能负荷报表
                dates.add(resourcesCalendarEntity.getDate());
            });

            //当资源类型为产线时，记录每天的可用工时，目前锦湖是以产线为维度
            if(entity.getResourcesType().equals(Constant.PRODUCT_LINE)){
                saveCapacityLoad(resourcesCalendarEntities,dates);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResourcesCalendarEntity resourcesCalendarEntity) {

        resourcesCalendarEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
        resourcesCalendarEntity.setModifyDate(new Date());
        resourcesCalendarMapper.updateById(resourcesCalendarEntity);

    }


    @Override
    public ResourcesCalendarGatherRespDTO resourcesCalendars(ResourcesCalendarQueryDTO resourcesCalendarQueryDTO) {

        ResourcesCalendarGatherRespDTO resourcesCalendarGatherRespDTO = new ResourcesCalendarGatherRespDTO();
        List<ResourcesCalendarRespDTO> respDTOList = null;

        if(Constant.SITE.equals(resourcesCalendarQueryDTO.getResourceType())){
            respDTOList=resourcesCalendarMapper.getSiteResourcesCalendars(resourcesCalendarQueryDTO);
        }
        if(Constant.WORK_SHOP.equals(resourcesCalendarQueryDTO.getResourceType())){
            respDTOList=resourcesCalendarMapper.getWorkShopResourcesCalendars(resourcesCalendarQueryDTO);
        }
        if(Constant.PRODUCT_LINE.equals(resourcesCalendarQueryDTO.getResourceType())){
            respDTOList = resourcesCalendarMapper.getProductLineResourcesCalendars(resourcesCalendarQueryDTO);
        }
        if(Constant.DEVICE.equals(resourcesCalendarQueryDTO.getResourceType())){
            respDTOList = resourcesCalendarMapper.getDeviceResourcesCalendars(resourcesCalendarQueryDTO);
        }
        if(Constant.USER.equals(resourcesCalendarQueryDTO.getResourceType())){
            respDTOList = resourcesCalendarMapper.getUserResourcesCalendars(resourcesCalendarQueryDTO);
        }
        if(respDTOList.size()>0){
            Set<ResourcesCalendarDateRespDTO> dateResp = new HashSet<>();
            Set<String> classFrequencyIds = new TreeSet<>();
            respDTOList.forEach(resourcesCalendarRespDTO -> {
                ResourcesCalendarDateRespDTO resourcesCalendarDateRespDTO = new ResourcesCalendarDateRespDTO();
                resourcesCalendarDateRespDTO.setDay(resourcesCalendarRespDTO.getDay());
                resourcesCalendarDateRespDTO.setState(resourcesCalendarRespDTO.getState());
                dateResp.add(resourcesCalendarDateRespDTO);
                classFrequencyIds.add(resourcesCalendarRespDTO.getClassFrequencyId());
            });
            resourcesCalendarGatherRespDTO.setClassSystemId(respDTOList.get(0).getClassSystemId());
            resourcesCalendarGatherRespDTO.setResourcesCalendarRespDTOList(respDTOList);
            resourcesCalendarGatherRespDTO.setClassFrequencyIds(classFrequencyIds);
            resourcesCalendarGatherRespDTO.setDateResp(dateResp);
        }
        return resourcesCalendarGatherRespDTO;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyResourcesCalendars(ResourceCopySaveDTO resourceCopySaveDTO) throws Exception {

        //复制资源日历，先查询出被复制的资源数据
        if(StrUtil.isEmpty(resourceCopySaveDTO.getResourceType())){
           throw new CommonException("无法识别该资源类型",CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        QueryWrapper<ResourcesCalendarEntity> copiedWrapper = getCopiedWrapper(resourceCopySaveDTO);
        List<ResourcesCalendarEntity> list = resourcesCalendarMapper.selectList(copiedWrapper);
        //遍历需要复制的资源
        for (ResourceCopyDTO resourcesCopy : resourceCopySaveDTO.getResourceCopys()){

            Map<String,Object> map = getCopyMap(resourcesCopy,resourceCopySaveDTO.getYear(),resourceCopySaveDTO.getMonth());
            QueryWrapper<ResourcesCalendarEntity> copyWrapper = (QueryWrapper<ResourcesCalendarEntity>)map.get("queryWrapper");
            QueryWrapper<ResourcesCalendarEntity> checkWrapper = (QueryWrapper<ResourcesCalendarEntity>) copyWrapper.clone();
            //定义日期集合
            Set<Date> dates = new HashSet<>();
            //获取需要复制的对象
            ResourcesCalendarEntity copyData = (ResourcesCalendarEntity) map.get("entity");
            checkWrapper.eq("state", Constant.ResourcesCalendarState.USE.getValue());
            //校验资源数据是否有被引用，如果有，直接抛出异常，没有则先删除原有数据再保存
            Integer count = resourcesCalendarMapper.selectCount(checkWrapper);
            if(count>0){
                throw new CommonException("复制失败，有数据已被引用，请检查数据",CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            resourcesCalendarMapper.delete(copyWrapper);
            list.forEach(resourcesCalendarEntity -> {
                resourcesCalendarEntity.setSite(copyData.getSite());
                resourcesCalendarEntity.setWorkShopBo(copyData.getWorkShopBo());
                resourcesCalendarEntity.setProductLineBo(copyData.getProductLineBo());
                resourcesCalendarEntity.setDeviceBo(copyData.getDeviceBo());
                resourcesCalendarEntity.setUserBo(copyData.getUserBo());
                resourcesCalendarEntity.setResourcesType(copyData.getResourcesType());
                resourcesCalendarEntity.setState(Constant.ResourcesCalendarState.NO_USE.getValue());
                resourcesCalendarEntity.setCreateDate(new Date());
                resourcesCalendarEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
                resourcesCalendarEntity.setId(null);
                resourcesCalendarMapper.insert(resourcesCalendarEntity);
                dates.add(resourcesCalendarEntity.getDate());
            });
            //当资源类型为产线时，统计每天的可用工时并存到数据库，目前锦湖是以产线为维度
            if(copyData.getResourcesType().equals(Constant.PRODUCT_LINE)){
                saveCapacityLoad(list,dates);
            }
        }

    }

    @Override
    public List<ResourcesCalendarEntity> getResourcesCalendarByTerm(ResourcesCalendarFeignQueryDTO resourcesCalendarFeignQueryDTO) {

        QueryWrapper<ResourcesCalendarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_line_bo",resourcesCalendarFeignQueryDTO.getProductLineBo());
        queryWrapper.between("date",resourcesCalendarFeignQueryDTO.getBeginDate(),resourcesCalendarFeignQueryDTO.getEndDate());
        return resourcesCalendarMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void occupyResourcesCalendar(List<Map<String, Object>> maps) {

        maps.forEach(map ->{
            //资源日历修改为占用
            UpdateWrapper<ResourcesCalendarEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("product_line_bo",map.get("productLineBo"));
            String mapStartDateStr = DateUtil.format((Date) map.get("startDate"),"yyyy-MM-dd");
            String mapEndDateStr = DateUtil.format((Date) map.get("endDate"),"yyyy-MM-dd");
            updateWrapper.between("date",DateUtil.parse(mapStartDateStr),DateUtil.parse(mapEndDateStr));
            ResourcesCalendarEntity resourcesCalendarEntity = new ResourcesCalendarEntity();
            resourcesCalendarEntity.setWorkShopBo(null);
            resourcesCalendarEntity.setProductLineBo(map.get("productLineBo").toString());
            resourcesCalendarEntity.setState(Constant.ResourcesCalendarState.USE.getValue());
            resourcesCalendarMapper.update(resourcesCalendarEntity,updateWrapper);

            QueryWrapper<CapacityLoadEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("product_line_bo",map.get("productLineBo"));
            queryWrapper.between("date",DateUtil.parse(mapStartDateStr),DateUtil.parse(mapEndDateStr));
            queryWrapper.orderByAsc("date");
            List<CapacityLoadEntity> capacityLoadEntities = capacityLoadMapper.selectList(queryWrapper);

            //组装查询条件，查询一天的资源日历，将时间段按照顺序查询出来，用于计算各种情况
            QueryWrapper<ResourcesCalendarEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("product_line_bo",map.get("productLineBo"));

            wrapper.between("date",mapStartDateStr,mapStartDateStr);
            wrapper.orderByAsc("sort");
            List<ResourcesCalendarEntity> resourcesCalendarEntities = resourcesCalendarMapper.selectList(wrapper);

            //拼装休息时间的时间段
            List<TimeDTO> initTimeDTOList = new ArrayList<>();
            String startDate = "";
            String endDate = "";
            for(ResourcesCalendarEntity calendarEntity : resourcesCalendarEntities){
                if(calendarEntity.equals(resourcesCalendarEntities.get(0))){
                    startDate = calendarEntity.getEndDateStr();
                }else if(calendarEntity.equals(resourcesCalendarEntities.get(resourcesCalendarEntities.size()-1))){
                    endDate = calendarEntity.getStartDateStr();
                }else {
                    TimeDTO timeDTO = new TimeDTO();
                    timeDTO.setStartDateStr(calendarEntity.getStartDateStr());
                    timeDTO.setEndDateStr(calendarEntity.getEndDateStr());
                    initTimeDTOList.add(timeDTO);
                }
            }
            List<TimeDTO> timeDTOList = splitTimeAndMargin(initTimeDTOList,startDate,endDate);

            //需要将产能负荷表的已排时间更新
            if(CollUtil.isNotEmpty(capacityLoadEntities)){
                capacityLoadEntities.forEach(capacityLoadEntity -> {
                    //当是第一条数据和最后一条数据时，时间会有出入，需要重新计算,获取到的时间只有几种情况
                    //第一种，在开始时间之前，那么取开始时间
                    //第二种在工作时间之内，那么取传进来的时间为开始时间
                    //第三种在休息时间之内，那么取接下来那个时间段的第一个开始时间
                    if(capacityLoadEntities.get(0).equals(capacityLoadEntity)){
                        Date date = (Date) map.get("startDate");
                        String startDateStr = DateUtil.format(date,"yyyy-MM-dd HH:mm:ss");
                        String strStartDate = DateUtil.format(capacityLoadEntity.getDate(),"yyyy-MM-dd");
                        Double datePoor = TimeUtils.getDatePoor(startDateStr,strStartDate+" "+resourcesCalendarEntities.get(0).getStartDateStr()+":00");
                        if(datePoor<0){
                            capacityLoadEntity.setArrangedWorkHour(capacityLoadEntity.getAvailableWorkHour());
                        }else {
                            Double arangedWorkHour = Double.valueOf(0);
                            boolean checkIsWorkTime = false;
                            //如果日期不在工作时间范围内，则需要去判断在那个休息时间之内
                            for(int i = 0;i<resourcesCalendarEntities.size();i++){
                                boolean effectiveDate = TimeUtils.isEffectiveDate(startDateStr, strStartDate+" "+resourcesCalendarEntities.get(i).getStartDateStr( )+":00",strStartDate+" "+resourcesCalendarEntities.get(i).getEndDateStr( )+":00");
                                if(effectiveDate == true){
                                    checkIsWorkTime = true;
                                    for(int j = i;j<resourcesCalendarEntities.size();j++){
                                        if(j==i){
                                            Double timePoor =  TimeUtils.getDatePoor(startDateStr,strStartDate+" "+resourcesCalendarEntities.get(j).getEndDateStr()+":00");
                                            arangedWorkHour += timePoor;
                                        }else {
                                            arangedWorkHour += resourcesCalendarEntities.get(j).getWorkHour();
                                        }
                                    }
                                    capacityLoadEntity.setArrangedWorkHour(arangedWorkHour);
                                    break;
                                }
                            }
                            if(checkIsWorkTime == false){
                                for(int i = 0;i<timeDTOList.size();i++){
                                    boolean effectiveDate = TimeUtils.isEffectiveDate(startDateStr,strStartDate+" "+timeDTOList.get(i).getStartDateStr()+":00",strStartDate+" "+timeDTOList.get(i).getEndDateStr()+":00");
                                    if(effectiveDate==true){
                                        boolean check = false;
                                        for(ResourcesCalendarEntity calendarEntity : resourcesCalendarEntities){
                                            if(check == true){
                                                arangedWorkHour += calendarEntity.getWorkHour();
                                            }
                                            if(calendarEntity.getStartDateStr().equals(timeDTOList.get(i).getEndDateStr())){
                                                check = true;
                                                arangedWorkHour += calendarEntity.getWorkHour();
                                            }
                                        }

                                        break;
                                    }
                                }
                            }
                            capacityLoadEntity.setArrangedWorkHour(arangedWorkHour);
                        }

                    }else if(capacityLoadEntities.get(capacityLoadEntities.size()-1).equals(capacityLoadEntity)){
                        Date date = (Date) map.get("endDate");
                        String endDateStr = DateUtil.format(date,"yyyy-MM-dd HH:mm:ss");
                        String strEndDate = DateUtil.format(capacityLoadEntity.getDate(),"yyyy-MM-dd");
                        Double arangedWorkHour = Double.valueOf(0);
                        boolean checkIsWorkTime = false;
                        //如果日期不在工作时间范围内，则需要去判断在那个休息时间之内
                        for(int i = 0;i<resourcesCalendarEntities.size();i++){
                            boolean effectiveDate = TimeUtils.isEffectiveDate(endDateStr,strEndDate+" "+resourcesCalendarEntities.get(i).getStartDateStr( )+":00",strEndDate+" "+resourcesCalendarEntities.get(i).getEndDateStr( )+":00");
                            if(effectiveDate == true){
                                checkIsWorkTime = true;
                                for(int j = 0;j<=i;j++){
                                    if(j==i){
                                        Double timePoor =  TimeUtils.getDatePoor(strEndDate+" "+resourcesCalendarEntities.get(j).getStartDateStr()+":00",endDateStr);
                                        arangedWorkHour += timePoor;
                                    }else {
                                        arangedWorkHour += resourcesCalendarEntities.get(j).getWorkHour();
                                    }
                                }
                                capacityLoadEntity.setArrangedWorkHour(arangedWorkHour);
                                break;
                            }
                        }
                        if(checkIsWorkTime == false){
                            for(int i = 0;i<timeDTOList.size();i++){
                                boolean effectiveDate = TimeUtils.isEffectiveDate(endDateStr,strEndDate+" "+timeDTOList.get(i).getStartDateStr()+":00",strEndDate+" "+timeDTOList.get(i).getEndDateStr()+":00");
                                if(effectiveDate==true){
                                    for(ResourcesCalendarEntity calendarEntity : resourcesCalendarEntities){

                                        if(calendarEntity.getEndDateStr().equals(timeDTOList.get(i).getEndDateStr())){
                                            break;
                                        }else {
                                            arangedWorkHour += calendarEntity.getWorkHour();
                                        }
                                    }

                                    break;
                                }
                            }
                        }
                        capacityLoadEntity.setArrangedWorkHour(arangedWorkHour);

                    }else {
                        capacityLoadEntity.setArrangedWorkHour(capacityLoadEntity.getAvailableWorkHour());
                    }
                    capacityLoadMapper.updateById(capacityLoadEntity);
                });
            }

        });

    }


    /**
     *组装保存的查询条件
     * @param entity
     * @return
     */
    private QueryWrapper<ResourcesCalendarEntity> getSaveWrapper(ResourcesCalendarEntity entity,Calendar calendar){


        QueryWrapper<ResourcesCalendarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resources_type",entity.getResourcesType());
        if(Constant.SITE.equals(entity.getResourcesType())){
            queryWrapper.eq("site",entity.getSite());
        }
        if(Constant.WORK_SHOP.equals(entity.getResourcesType())){
            queryWrapper.eq("site",entity.getSite());
            queryWrapper.eq("work_shop_bo",entity.getWorkShopBo());
        }
        if(Constant.PRODUCT_LINE.equals(entity.getResourcesType())){

            queryWrapper.eq("site",entity.getSite());
            queryWrapper.eq("work_shop_bo",entity.getWorkShopBo());
            queryWrapper.eq("product_line_bo",entity.getProductLineBo());
        }
        if(Constant.DEVICE.equals(entity.getResourcesType())){

            queryWrapper.eq("site",entity.getSite());
            queryWrapper.eq("work_shop_bo",entity.getWorkShopBo());
            queryWrapper.eq("device_bo",entity.getDeviceBo());

        }
        if(Constant.USER.equals(entity.getResourcesType())){

            queryWrapper.eq("site",entity.getSite());
            queryWrapper.eq("work_shop_bo",entity.getWorkShopBo());
            queryWrapper.eq("user_bo",entity.getUserBo());

        }
        queryWrapper.eq("year",calendar.get(Calendar.YEAR));
        queryWrapper.eq("month",calendar.get(Calendar.MONTH)+1);
        return queryWrapper;
    }

    /**
     * 组装被拷贝的对象查询条件
     * @param resourceCopySaveDTO
     * @return
     */
    private QueryWrapper<ResourcesCalendarEntity> getCopiedWrapper(ResourceCopySaveDTO resourceCopySaveDTO){
        QueryWrapper<ResourcesCalendarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year",resourceCopySaveDTO.getYear());
        queryWrapper.eq("month",resourceCopySaveDTO.getMonth());
        queryWrapper.eq("resources_type",resourceCopySaveDTO.getResourceType());
        //根据不同的资源类型拼接查询语句
        if(Constant.SITE.equals(resourceCopySaveDTO.getResourceType())){
            queryWrapper.eq("site",resourceCopySaveDTO.getSite());
        }
        if(Constant.WORK_SHOP.equals(resourceCopySaveDTO.getResourceType())){
            queryWrapper.eq("site",resourceCopySaveDTO.getSite());
            queryWrapper.eq("work_shop_bo",resourceCopySaveDTO.getWorkShopBo());
        }
        if(Constant.PRODUCT_LINE.equals(resourceCopySaveDTO.getResourceType())){

            queryWrapper.eq("site",resourceCopySaveDTO.getSite());
            queryWrapper.eq("work_shop_bo",resourceCopySaveDTO.getWorkShopBo());
            queryWrapper.eq("product_line_bo",resourceCopySaveDTO.getResourceBo());
        }
        if(Constant.DEVICE.equals(resourceCopySaveDTO.getResourceType())){

            queryWrapper.eq("site",resourceCopySaveDTO.getSite());
            queryWrapper.eq("work_shop_bo",resourceCopySaveDTO.getWorkShopBo());
            queryWrapper.eq("device_bo",resourceCopySaveDTO.getResourceBo());

        }
        if(Constant.USER.equals(resourceCopySaveDTO.getResourceType())){

            queryWrapper.eq("site",resourceCopySaveDTO.getSite());
            queryWrapper.eq("work_shop_bo",resourceCopySaveDTO.getWorkShopBo());
            queryWrapper.eq("user_bo",resourceCopySaveDTO.getResourceBo());

        }
        queryWrapper.orderByAsc("create_date");
        return queryWrapper;
    }


    private Map<String,Object> getCopyMap(ResourceCopyDTO resourceCopyDTO,Integer year,Integer month){

        Map<String,Object> map = new HashMap<>();
        ResourcesCalendarEntity resourcesCalendarEntity = new ResourcesCalendarEntity();
        QueryWrapper<ResourcesCalendarEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year",year);
        queryWrapper.eq("month",month);
        if(Constant.SITE.equals(resourceCopyDTO.getCopyResourceType())){
            queryWrapper.eq("site",resourceCopyDTO.getCopySite());
            resourcesCalendarEntity.setSite(resourceCopyDTO.getCopySite());
        }
        if(Constant.WORK_SHOP.equals(resourceCopyDTO.getCopyResourceType())){
            queryWrapper.eq("site",resourceCopyDTO.getCopySite());
            queryWrapper.eq("work_shop_bo",resourceCopyDTO.getCopyWorkShopBo());
            resourcesCalendarEntity.setSite(resourceCopyDTO.getCopySite());
            resourcesCalendarEntity.setWorkShopBo(resourceCopyDTO.getCopyWorkShopBo());
        }
        if(Constant.PRODUCT_LINE.equals(resourceCopyDTO.getCopyResourceType())){

            queryWrapper.eq("site",resourceCopyDTO.getCopySite());
            queryWrapper.eq("work_shop_bo",resourceCopyDTO.getCopyWorkShopBo());
            queryWrapper.eq("product_line_bo",resourceCopyDTO.getCopyResourceBo());

            resourcesCalendarEntity.setSite(resourceCopyDTO.getCopySite());
            resourcesCalendarEntity.setWorkShopBo(resourceCopyDTO.getCopyWorkShopBo());
            resourcesCalendarEntity.setProductLineBo(resourceCopyDTO.getCopyResourceBo());
        }
        if(Constant.DEVICE.equals(resourceCopyDTO.getCopyResourceType())){

            queryWrapper.eq("site",resourceCopyDTO.getCopySite());
            queryWrapper.eq("work_shop_bo",resourceCopyDTO.getCopyWorkShopBo());
            queryWrapper.eq("device_bo",resourceCopyDTO.getCopyResourceBo());
            resourcesCalendarEntity.setSite(resourceCopyDTO.getCopySite());
            resourcesCalendarEntity.setWorkShopBo(resourceCopyDTO.getCopyWorkShopBo());
            resourcesCalendarEntity.setDeviceBo(resourceCopyDTO.getCopyResourceBo());
        }
        if(Constant.USER.equals(resourceCopyDTO.getCopyResourceType())){

            queryWrapper.eq("site",resourceCopyDTO.getCopySite());
            queryWrapper.eq("work_shop_bo",resourceCopyDTO.getCopyWorkShopBo());
            queryWrapper.eq("user_bo",resourceCopyDTO.getCopyResourceBo());
            resourcesCalendarEntity.setSite(resourceCopyDTO.getCopySite());
            resourcesCalendarEntity.setWorkShopBo(resourceCopyDTO.getCopyWorkShopBo());
            resourcesCalendarEntity.setUserBo(resourceCopyDTO.getCopyResourceBo());
        }
        resourcesCalendarEntity.setResourcesType(resourceCopyDTO.getCopyResourceType());

        map.put("queryWrapper",queryWrapper);
        map.put("entity",resourcesCalendarEntity);
        return map;
    }

    private List<TimeDTO> conversionTime(List<ClassShiftBreakEntity> classShiftBreakEntities,String startDate,String endDate){

        List<TimeDTO> initTimeDtoList = new ArrayList<>();
        classShiftBreakEntities.forEach(classShiftBreakEntity -> {
            TimeDTO timeDTO = new TimeDTO();
            timeDTO.setStartDateStr(classShiftBreakEntity.getStartDateStr());
            timeDTO.setEndDateStr(classShiftBreakEntity.getEndDateStr());
            initTimeDtoList.add(timeDTO);
        });
        List<TimeDTO> timeDTOList = splitTimeAndMargin(initTimeDtoList,startDate,endDate);

        return timeDTOList;
    }

    private List<TimeDTO> splitTimeAndMargin(List<TimeDTO> timeList, String startDate, String endDate) {

        List<TimeDTO> timeDTOList = new ArrayList<>();
        List<String> strList = new ArrayList<>();
        for(int n = 0; n<timeList.size(); n++){
            if(n == 0) {
                strList.add(startDate+"-"+timeList.get(0).getStartDateStr());
            }
            if(timeList.size()>1&&n < timeList.size()-1){
                strList.add(timeList.get(n).getEndDateStr()+"-"+timeList.get(n+1).getStartDateStr());
            }
            if(n==timeList.size()-1){
                strList.add(timeList.get(n).getEndDateStr()+"-"+endDate);
            }
        }
        strList.forEach(str ->{
            String[] strings = str.split("-");
            TimeDTO timeDTO = new TimeDTO();
            timeDTO.setStartDateStr(strings[0]);
            timeDTO.setEndDateStr(strings[1]);
            timeDTOList.add(timeDTO);
        });
        return timeDTOList;
    }

    private Double getWorkHour(String startDate,String endDate){

        Double workHour;
        Double datePoor = TimeUtils.getDatePoor(endDate, startDate);
        if(datePoor>0){
            workHour = datePoor;
        }else {
            endDate = TimeUtils.getAfterTime(endDate,Double.valueOf(24));
            datePoor = TimeUtils.getDatePoor(endDate, startDate);
            workHour = datePoor;
        }
        return workHour;
    }


    private ResourcesCalendarRespDTO generateResourcesCalendarRespDTO(CreateResourcesCalendarDTO createResourcesCalendarDTO,ClassFrequencyDTO classFrequencyDTO){

        ResourcesCalendarRespDTO resourcesCalendarRespDTO = new ResourcesCalendarRespDTO();
        resourcesCalendarRespDTO.setSite(createResourcesCalendarDTO.getSite());
        resourcesCalendarRespDTO.setWorkShopBo(createResourcesCalendarDTO.getWorkShopBo());
        resourcesCalendarRespDTO.setResourceBo(createResourcesCalendarDTO.getResourceBo());
        resourcesCalendarRespDTO.setSiteName(createResourcesCalendarDTO.getSiteName());
        resourcesCalendarRespDTO.setWorkShopName(createResourcesCalendarDTO.getWorkShopName());
        resourcesCalendarRespDTO.setResourceName(createResourcesCalendarDTO.getResourceName());
        resourcesCalendarRespDTO.setResourceType(createResourcesCalendarDTO.getResourceType());
        resourcesCalendarRespDTO.setClassFrequencyName(classFrequencyDTO.getClassFrequencyName());
        resourcesCalendarRespDTO.setIsWorkOvertime("0");
        resourcesCalendarRespDTO.setClassFrequencyId(classFrequencyDTO.getClassFrequencyId());
        return resourcesCalendarRespDTO;
    }


    private void saveCapacityLoad(List<ResourcesCalendarEntity> resourcesCalendarEntities,Set<Date> dates){
        UpdateWrapper<CapacityLoadEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("product_line_bo",resourcesCalendarEntities.get(0).getProductLineBo());
        updateWrapper.in("date",dates);
        capacityLoadMapper.delete(updateWrapper);

        dates.forEach(date -> {
            CapacityLoadEntity capacityLoadEntity = new CapacityLoadEntity();
            capacityLoadEntity.setDate(date);
            capacityLoadEntity.setProductLineBo(resourcesCalendarEntities.get(0).getProductLineBo());
            Double availableWorkHour = 0.0;
            for(ResourcesCalendarEntity resourcesCalendarEntity:resourcesCalendarEntities){
                if(date.equals(resourcesCalendarEntity.getDate())){
                    availableWorkHour += resourcesCalendarEntity.getWorkHour();
                }else {
                    continue;
                }
            }
            capacityLoadEntity.setAvailableWorkHour(availableWorkHour);
            capacityLoadMapper.insert(capacityLoadEntity);
        });
    }

}
