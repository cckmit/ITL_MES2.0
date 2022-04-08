package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.PassWordUtil;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.QualityPlanHandleBO;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.OperationDTO;
import com.itl.mes.core.api.dto.QualityCheckListDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.QualityCheckListService;
import com.itl.mes.core.api.service.QualityCheckResultService;
import com.itl.mes.core.api.service.QualityPlanService;
import com.itl.mes.core.api.service.SfcService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.OperationParamsVo;
import com.itl.mes.core.api.vo.QualityCheckAtParameterVO;
import com.itl.mes.core.api.vo.QualityCheckListVO;
import com.itl.mes.core.provider.mapper.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class QualityCheckListServiceImpl extends ServiceImpl<QualityCheckListMapper,QualityCheckList> implements QualityCheckListService {

    @Autowired
    private QualityCheckListMapper qualityCheckListMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private QualityPlanParameterMapper qualityPlanParameterMapper;

    @Autowired
    private SfcMapper sfcMapper;

    @Autowired
    private WorkShopMapper workShopMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private QualityPlanMapper qualityPlanMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private RouterProcessMapper routerProcessMapper;

    @Autowired
    private QualityPlanService qualityPlanService;

    @Autowired
    private QualityCheckResultMapper qualityCheckResultMapper;

    @Autowired
    private SfcServiceImpl sfcService;

    @Autowired
    private MyDeviceMapper myDeviceMapper;

    @Autowired
    private CustomDataValMapper customDataValMapper;

    @Override
    public synchronized QualityCheckList saveInUpdate(QualityCheckListVO qclVO) throws CommonException {
        QualityCheckList qualityCheckList = new QualityCheckList();
        BeanUtils.copyProperties(qclVO,qualityCheckList);
        qualityCheckList.setOperationBo(qclVO.getOperationBo());
        qualityCheckList.setOperationName(qclVO.getOperationName());
        String parameterBo = qclVO.getParameterName();
        qualityCheckList.setParameterBO(parameterBo);
        Date date = new Date();
        String processInfo;
        Sfc sfc = new Sfc();
        if (StrUtil.isEmpty(qclVO.getCheckCode())){
            sfc = qclVO.getSfcEntity();
            // 验证是否是首工序
            if(sfc==null || sfc.getSfc() ==null || sfc.getSfc() ==""){
                boolean isFirst=false;
                if(sfc.getShopOrder() !="" && sfc.getShopOrder() !=null){
                    // 检验当前工序是否是工单对应工艺路线的首工序
                    Map<String,String> map=routerProcessMapper.selectByShopOrder(sfc.getShopOrder());
                    if(map !=null){
                        // 解析json
                        String rprocessInfo = map.get("process_info");
                        if(rprocessInfo !=null && rprocessInfo!="") {
                            JSONObject jsonObject = JSON.parseObject(rprocessInfo);
                            JSONArray nodeList = JSONArray.parseArray(jsonObject.get("nodeList").toString());
                            JSONArray lineList = JSONArray.parseArray(jsonObject.get("lineList").toString());
                            String startId=sfcService.getStartId(nodeList);
                            String nextId=sfcService.getNextId(lineList,startId);
                            for (Object obj : nodeList) {
                                if(JSON.parseObject(obj.toString()).get("id").toString().equals(nextId)){
                                    if (JSON.parseObject(obj.toString()).get("operation") != null) {
                                        // 首工序
                                        String firstOperation = JSON.parseObject(obj.toString()).get("operation").toString();
                                        if (firstOperation.equals(qclVO.getOperationBo())) {
                                            isFirst=true;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else{
                    throw new CommonException("首工序检验,工单为空,请检查",30002);
                }
                if(!isFirst){
                    throw new CommonException("当前工序非首工序，请输入批次条码",30002);
                }
            }
            HandleSfc(sfc);
            qualityCheckList.setCheckUser(userUtil.getUser().getUserName());
            qualityCheckList.setCreateUser(userUtil.getUser().getUserName());
            qualityCheckList.setCreateDate(date);
            qualityCheckList.setCreateName(userUtil.getUser().getRealName());
            if(qclVO.getCheckType().equals("0") && qclVO.getCheckState().equals("3")){
                // 如果是品质管理/检验记录查询中的的新建首检
                qualityCheckList.setQualityCheckDate(date);
                qualityCheckList.setQualityCheckUser(userUtil.getUser().getRealName());
            }else {
                qualityCheckList.setSelfCheckUser(userUtil.getUser().getRealName());
                qualityCheckList.setSelfCheckDate(date);
            }
            qualityCheckList.setCheckUserName(userUtil.getUser().getRealName());
            if(qclVO.getSfcEntity() !=null){
                qualityCheckList.setSfc(qclVO.getSfcEntity().getSfc());
                qualityCheckList.setShopOrder(qclVO.getSfcEntity().getShopOrder());
            }
            if(!qualityCheckList.getCheckType().equals("0")){
                qualityCheckList.setQualityCheckDate(date);
                qualityCheckList.setQualityCheckUser(userUtil.getUser().getRealName());
                qualityCheckList.setWorkshopBo(sfc.getWorkShopBo());
                qualityCheckList.setWorkshopName(sfc.getWorkshopName());
                qualityCheckList.setItem(sfc.getItem());
                qualityCheckList.setItemDesc(sfc.getItemDesc());
                qualityCheckList.setItemName(sfc.getItemName());
                qualityCheckList.setItemBo(sfc.getItemBo());
            }
            String str = qualityCheckListMapper.selectMaxCode();
            int qnum = 0;
            if (StrUtil.isNotBlank(str)){
                qnum = Integer.parseInt(str.substring(str.length()- 5,str.length()));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
            String format = String.format("%05d", qnum+1);
            String checkCode = "DYQC"+sdf.format(new Date())+format;//生成检验单编号
            qualityCheckList.setCheckCode(checkCode);
            int row=0;
            try{
                row=qualityCheckListMapper.insert(qualityCheckList);
            }catch (Exception e){
                e.getMessage();
            }finally {
                if(row !=1){
                    // 存在当前的检验单编号
                    //重新获取检验单编号
                    str = qualityCheckListMapper.selectMaxCode();
                    qnum = 0;
                    if (StrUtil.isNotBlank(str)){
                        qnum = Integer.parseInt(str.substring(str.length()- 5,str.length()));
                    }
                    format = String.format("%05d", qnum+1);
                    checkCode = "DYQC"+sdf.format(new Date())+format;
                    qualityCheckList.setCheckCode(checkCode);
                    qualityCheckListMapper.insert(qualityCheckList);
                }
            }

            // 向m_my_device 更新检验单编码字段
            if(qclVO.getMyDeviceId()!=null && qclVO.getMyDeviceId() !=""){
                MyDevice myDevice=new MyDevice();
                myDevice.setId(qclVO.getMyDeviceId());
                myDevice.setCheckCode(qualityCheckList.getCheckCode());
                myDeviceMapper.updateById(myDevice);
            }
            List<OperationParamsVo> operationParamsVoList = qclVO.getOperationList();
            if (operationParamsVoList!=null){
                for (OperationParamsVo operationParamsVo : operationParamsVoList) {
                    List<QualityPlanParameter> parameterList = operationParamsVo.getParameters();
                    QualityCheckParameter qualityCheckParameter = new QualityCheckParameter();
                    for (QualityPlanParameter qualityPlanParameter : parameterList) {
                        BeanUtils.copyProperties(qualityPlanParameter,qualityCheckParameter);
                        qualityCheckParameter.setBo(UUID.uuid32());
                        qualityCheckParameter.setCheckCode(checkCode);
                        qualityCheckParameter.setSfc(qclVO.getSfcEntity().getSfc());
                        qualityCheckResultMapper.insert(qualityCheckParameter);
                    }
                }
            }
        }
        else {
            if (qualityCheckListMapper.selectOne(new QueryWrapper<QualityCheckList>().eq("CHECK_CODE",qclVO.getCheckCode()))==null){;
                qualityCheckList.setCheckUser(userUtil.getUser().getUserName());
                qualityCheckList.setCheckUserName(userUtil.getUser().getRealName());
                qualityCheckListMapper.insert(qualityCheckList);
            }
            List<OperationParamsVo> operationParamsVoList = qclVO.getOperationList();
            if (qclVO.getSfcEntity()!=null){
                processInfo = sfcMapper.checkOperation(qclVO.getSfcEntity().getSfc());
                sfc = sfcMapper.selectBySfc(qclVO.getSfcEntity().getSfc());
            }
            else {
                processInfo = sfcMapper.checkOperation(qclVO.getSfc());
                sfc = sfcMapper.selectBySfc(qclVO.getSfc());
            }
            List<String> operationBos = new ArrayList<>();
            if (!qclVO.getCheckType().equals("2")){
                if (StrUtil.isNotEmpty(processInfo)){
                    JSONObject jsonObj = JSON.parseObject(processInfo);
                    JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
                    if (nodeList.size() > 0){
                        for (int i=0;i<nodeList.size();i++){
                            JSONObject operationObj = JSON.parseObject(nodeList.get(i).toString());
                            //获取工序
                            String operation = operationObj.getString("operation");
                            operationBos.add(operation);
                        }
                    }
                }
            }
            if (operationParamsVoList!=null){
                for (OperationParamsVo operationParamsVo : operationParamsVoList) {
                    List<QualityPlanParameter> parameterList = operationParamsVo.getParameters();
                    List<QualityCheckParameter> parameters = new ArrayList<>();
                    for (QualityPlanParameter qualityPlanParameter : parameterList) {
                        QualityCheckParameter qualityCheckParameter = new QualityCheckParameter();
                        BeanUtils.copyProperties(qualityPlanParameter,qualityCheckParameter);
                        qualityCheckResultMapper.updateById(qualityCheckParameter);
                    }
                }
            }
            if (qualityCheckList.getCheckState().equals("2")){
                qualityCheckList.setSelfCheckUser(userUtil.getUser().getRealName());
                qualityCheckList.setSelfCheckDate(date);
            }
            if (qualityCheckList.getCheckState().equals("3")){
                qualityCheckList.setQualityCheckDate(date);
                qualityCheckList.setQualityCheckUser(userUtil.getUser().getRealName());
            }
            qualityCheckListMapper.update(qualityCheckList,new QueryWrapper<QualityCheckList>().eq("ID",qualityCheckList.getId()));
            // 根据检验结果修改品检状态
            if(qclVO.getCheckState().equals("3")){
                MyDevice myDevice=new MyDevice();
                if(qclVO.getCheckResult().equals("不合格")){
                    myDevice.setFirstInsState("3");
                }else{
                    myDevice.setFirstInsState("2");
                }
                myDeviceMapper.update(myDevice,new QueryWrapper<MyDevice>().eq("check_code",qclVO.getCheckCode()));
            }
        }
        return qualityCheckList;
    }

    @Override
    public IPage<QualityCheckList> getQualityCheckList(QualityCheckListDTO qualityCheckListDTO) {
        if(ObjectUtil.isEmpty(qualityCheckListDTO.getPage())){
            qualityCheckListDTO.setPage(new Page(0, 10));
        }
        QueryWrapper<QualityCheckList> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotEmpty(qualityCheckListDTO.getCheckCode())){
            queryWrapper.like("CHECK_CODE",qualityCheckListDTO.getCheckCode());
        }
        if (StrUtil.isNotEmpty(qualityCheckListDTO.getItemBo())){
            queryWrapper.eq("ITEM_BO",qualityCheckListDTO.getItemBo());
        }
        if (StrUtil.isNotEmpty(qualityCheckListDTO.getCheckState())){
            queryWrapper.eq("CHECK_STATE",qualityCheckListDTO.getCheckState());
        }
        if (StrUtil.isNotEmpty(qualityCheckListDTO.getCheckType())){
            queryWrapper.eq("CHECK_TYPE",qualityCheckListDTO.getCheckType());
        }
        if (StrUtil.isNotEmpty(qualityCheckListDTO.getSfc())){
            queryWrapper.like("SFC",qualityCheckListDTO.getSfc());
        }
        if (StrUtil.isNotEmpty(qualityCheckListDTO.getOperationBo())){
            queryWrapper.eq("OPERATION_BO",qualityCheckListDTO.getOperationBo());
        }
        if (StrUtil.isNotEmpty(qualityCheckListDTO.getWorkshopBo())){
            queryWrapper.eq("WORKSHOP_BO",qualityCheckListDTO.getWorkshopBo());
        }
        if (StrUtil.isNotEmpty(qualityCheckListDTO.getStartTime())){
            queryWrapper.gt("CREATE_DATE",qualityCheckListDTO.getStartTime());
        }
        if (StrUtil.isNotEmpty(qualityCheckListDTO.getEndTime())){
            queryWrapper.lt("CREATE_DATE",qualityCheckListDTO.getEndTime());
        }
        if(StrUtil.isNotEmpty(qualityCheckListDTO.getQualityCheckUser())){
            queryWrapper.eq("QUALITY_CHECK_USER",qualityCheckListDTO.getQualityCheckUser());
        }
        if(StrUtil.isNotEmpty(qualityCheckListDTO.getQcStartTime())){
            queryWrapper.gt("QUALITY_CHECK_DATE",qualityCheckListDTO.getQcStartTime());
        }
        if(StrUtil.isNotEmpty(qualityCheckListDTO.getQcEndTime())){
            queryWrapper.lt("QUALITY_CHECK_DATE",qualityCheckListDTO.getQcEndTime());
        }
        if(StrUtil.isNotEmpty(qualityCheckListDTO.getCheckResult())){
            queryWrapper.eq("CHECK_RESULT",qualityCheckListDTO.getCheckResult());
        }
        queryWrapper.orderByDesc("CREATE_DATE");
        return qualityCheckListMapper.selectPage(qualityCheckListDTO.getPage(),queryWrapper);
    }

    @Override
    public void deleteQualityCheckList(String id) throws CommonException {
        QueryWrapper<QualityCheckList> queryWrapper = new QueryWrapper<QualityCheckList>();
        queryWrapper.eq("ID",id);
        QualityCheckList qualityCheckList = qualityCheckListMapper.selectById(queryWrapper);
        qualityCheckListMapper.delete(queryWrapper);
        qualityCheckResultMapper.delete(new QueryWrapper<QualityCheckParameter>().eq("CHECK_CODE",qualityCheckList.getCheckCode()));
    }

    @Override
    public QualityCheckAtParameterVO getQualityCheckAtParameterVo(QualityCheckListDTO dto) {
        QualityCheckAtParameterVO qcoVO = new QualityCheckAtParameterVO();
        QualityCheckList qualityCheckList = qualityCheckListMapper.selectById(dto.getId());
        String checkType = dto.getCheckType();
        String operationBo = dto.getOperationBo();
        String parameterBo = dto.getItemBo();
        if (StrUtil.isEmpty(dto.getSfc())){
            BeanUtils.copyProperties(qualityCheckList,qcoVO);
            return qcoVO;
        }
        Sfc sfc = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("SFC", dto.getSfc()));
        List<QualityPlanParameter> qualityPlanParameter = qualityPlanParameterMapper.selectList(new QueryWrapper<QualityPlanParameter>().eq("QUALITY_PLAN_BO",parameterBo).eq("OPERATION_BO",operationBo));
        if (qualityPlanParameter.size()==0){
            return null;
        }
        String qualityPlan = qualityPlanParameter.get(0).getQualityPlan();
        if (checkType.equals("2")){
            List<String> operationList = qualityPlanMapper.selectOperation(qualityPlan);
            List<OperationParamsVo> operations = new ArrayList<>();
            for (int i = 0; i < operationList.size(); i++) {
                OperationParamsVo vo = new OperationParamsVo();
                Operation operation = operationMapper.selectById(operationList.get(i));
                vo.setOperationName(operation.getOperationName());
                vo.setOperationBo(operation.getBo());
                operations.add(vo);
            }
            List<QualityPlanParameter> parameters = null;
            BeanUtils.copyProperties(qualityCheckList,qcoVO);
            for (OperationParamsVo operation : operations) {
                parameters = qualityPlanParameterMapper.selectList(new QueryWrapper<QualityPlanParameter>().eq("QUALITY_PLAN", qualityPlan).eq("OPERATION_BO",operation.getOperationBo()));
                operation.setParameters(parameters);
            }
            Sfc sfcEntity = HandleSfc(sfc);
            if (StrUtil.isNotEmpty(sfc.getDeviceBo())){
                Device device = deviceMapper.selectById(sfc.getDeviceBo());
                qcoVO.setCheckDevice(device.getDevice());
                qcoVO.setCheckDeviceName(device.getDeviceName());
            }
            qcoVO.setSfcEntity(sfcEntity);
            qcoVO.setOperationList(operations);
            return qcoVO;
        }
        else {
            if (qualityPlanParameter != null){
                List<OperationParamsVo> operations = new ArrayList<>();
                List<QualityPlanParameter> parameters = qualityPlanParameterMapper.selectList(new QueryWrapper<QualityPlanParameter>().eq("OPERATION_BO",operationBo));
                OperationParamsVo operationParamsVo = new OperationParamsVo();
                operationParamsVo.setOperationName(qualityCheckList.getOperationName());
                operationParamsVo.setParameters(parameters);
                operations.add(operationParamsVo);
                BeanUtils.copyProperties(qualityCheckList,qcoVO);
                Sfc sfcEntity = HandleSfc(sfc);
                if (StrUtil.isNotEmpty(sfc.getDeviceBo())){
                    Device device = deviceMapper.selectById(sfc.getDeviceBo());
                    qcoVO.setCheckDevice(device.getDevice());
                    qcoVO.setCheckDeviceName(device.getDeviceName());
                }
                qcoVO.setSfcEntity(sfcEntity);
                qcoVO.setOperationList(operations);
                return qcoVO;
            }
        }
        return null;
    }

    @Override
    public QualityCheckAtParameterVO getQcoVOBySfcOperation(OperationDTO operationDTO) throws CommonException {
        QualityCheckAtParameterVO qcoVO = new QualityCheckAtParameterVO();
        QueryWrapper<QualityCheckList> queryWrapper = new QueryWrapper();
        QualityCheckList qualityCheckList = new QualityCheckList();
        if (StrUtil.isNotEmpty(operationDTO.getOperationBo())){
            queryWrapper.eq("OPERATION_BO",operationDTO.getOperationBo());
        }
        if (StrUtil.isNotEmpty(operationDTO.getCheckCode())){
            queryWrapper.eq("CHECK_CODE",operationDTO.getCheckCode());
            qualityCheckList = qualityCheckListMapper.selectOne(queryWrapper);
        }
        String checkType = null;
        String operationBo = null;
        if (qualityCheckList!=null){
            checkType = qualityCheckList.getCheckType();
            operationBo = qualityCheckList.getOperationBo();
        }
        if (StrUtil.isNotEmpty(operationDTO.getCheckType())){
            checkType = operationDTO.getCheckType();
        }
        Sfc sfc = new Sfc();
        if (StrUtil.isNotEmpty(operationDTO.getSfc())){
            sfc = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("SFC", operationDTO.getSfc()));
        }
        if (qualityCheckList!=null){
            if (StrUtil.isNotEmpty(qualityCheckList.getSfc())){
                sfc = sfcMapper.selectOne(new QueryWrapper<Sfc>().eq("SFC", qualityCheckList.getSfc()));
            }
        }
        String qualityPlan = null;
        if (StrUtil.isEmpty(qualityCheckList.getParameterBO())){
            QualityPlan qualityPlan1 = qualityPlanService.getDefaultPlan();
            qualityPlan = qualityPlan1.getQualityPlan();
        }
        else {
            qualityPlan = qualityCheckList.getParameterBO();
        }
        if (checkType.equals("2")){
            String processInfo = null;
            if(sfc !=null) {
                processInfo=sfcMapper.checkOperation(sfc.getSfc());
            }
            List<String> operationList = new ArrayList<>();
            /*if (StrUtil.isNotEmpty(processInfo)){
                JSONObject jsonObj = JSON.parseObject(processInfo);
                JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
                if (nodeList.size() > 0){
                    for (int i=0;i<nodeList.size();i++){
                        JSONObject operationObj = JSON.parseObject(nodeList.get(i).toString());
                        //获取工序
                        String operation = operationObj.getString("operation");
                        if (StrUtil.isNotEmpty(operation)){
                            operationList.add(operation);
                        }
                    }
                }
            }*/
            if(StringUtils.isNotBlank(processInfo)){
                JSONObject jsonObj = JSON.parseObject(processInfo);
                JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
                JSONArray lineList = JSONArray.parseArray(jsonObj.getString("lineList"));
                //找到开始流程对应的流程id
                String startId = sfcService.getStartId(nodeList);
                //根据当前id找到对应的下一个流程id
                String nextId = sfcService.getNextId(lineList, startId);
                //根据id找到对应工序
                String operationBot = sfcService.getOperationById(nodeList, nextId);//第一个工序
                operationList.add(operationBot);
                while (StringUtils.isNotBlank(operationBot)){
                    nextId = sfcService.getNextId(lineList, nextId);
                    operationBot = sfcService.getOperationById(nodeList, nextId);
                    if (StringUtils.isNotBlank(operationBot)){
                        operationList.add(operationBot);
                    }
                }
            }
            String lastOperationBo=null;
            // 验证sfc对应工序是否是最后一个工序
            if(sfc !=null){
                if(sfc.getOperationBo() !=null) {
                    if (!sfc.getOperationBo().equals(operationList.get(operationList.size() - 1))) {
                        throw new CommonException("该批次条码未完成", 30002);
                    }else {
                        lastOperationBo=operationList.get(operationList.size() - 1);
                    }
                }
            }

            List<QualityCheckParameter> qualityCheckParameter = new ArrayList<>();
            List<OperationParamsVo> operations = new ArrayList<>();
            for (int i = 0; i < operationList.size(); i++) {
                OperationParamsVo vo = new OperationParamsVo();
                Operation operation = operationMapper.selectById(operationList.get(i));
                if(operation !=null){
                    vo.setOperationName(operation.getOperationName());
                }
                vo.setOperationBo(operationList.get(i));
                operations.add(vo);
            }

            BeanUtils.copyProperties(qualityCheckList,qcoVO);
            if (StrUtil.isNotEmpty(operationDTO.getCheckCode())){
                // 终检单
                for (OperationParamsVo operation : operations) {
                    List<QualityPlanParameter> parameters = new ArrayList<>();
                    qualityCheckParameter = qualityCheckResultMapper.selectList(new QueryWrapper<QualityCheckParameter>().eq("QUALITY_PLAN", qualityPlan).eq("OPERATION_BO",operation.getOperationBo()).eq("CHECK_CODE",qualityCheckList.getCheckCode()));
                    for (QualityCheckParameter checkParameter : qualityCheckParameter) {
                        QualityPlanParameter qualityPlanParameter = new QualityPlanParameter();
                        BeanUtils.copyProperties(checkParameter,qualityPlanParameter);
                        parameters.add(qualityPlanParameter);
                    }
                    operation.setParameters(parameters);
                    List<QualityCheckList> checkList = qualityCheckListMapper.selectList(new QueryWrapper<QualityCheckList>().eq("SFC",sfc.getSfc()).eq("OPERATION_BO",operation.getOperationBo()));
                    List<String> checkCode = new ArrayList<>();
                    if (checkList.size()!=0){
                        for (QualityCheckList list : checkList) {
                            checkCode.add(list.getCheckCode());
                        }
                    }
                    operation.setCheckCode(checkCode);
                }
            }
            else {
                for (int i = 0; i < operations.size(); i++) {
                    OperationParamsVo operation = operations.get(i);
                    // 最后一个工序有点检项
                    if(operation.getOperationBo().equals(lastOperationBo)){
                        List<QualityPlanParameter> parameters = new ArrayList<>();
                        parameters = qualityPlanParameterMapper.selectList(new QueryWrapper<QualityPlanParameter>().eq("QUALITY_PLAN", qualityPlan).eq("OPERATION_BO",operation.getOperationBo()).eq("ITEM_BO",sfc.getItemBo()).eq("ENABLED","1"));
                        // 补全点击项
                        parameters = getAllParameter(parameters,operation.getOperationBo(),sfc.getItemBo(),qualityPlan);
                        operation.setParameters(parameters);
                    }
                    List<QualityCheckList> checkList = qualityCheckListMapper.selectList(new QueryWrapper<QualityCheckList>().eq("SFC",operationDTO.getSfc()).eq("OPERATION_BO",operation.getOperationBo()));
                    List<String> checkCode = new ArrayList<>();
                    if (checkList.size()!=0){
                        for (QualityCheckList list : checkList) {
                            checkCode.add(list.getCheckCode());
                        }
                    }
                    // 是否是必填项
                    CustomDataAndValVo customDataAndValVo=customDataValMapper.selectOperationInspectionNotRequire(UserUtils.getSite(), operation.getOperationBo(), CustomDataTypeEnum.OPERATION.getDataType());
                    if(customDataAndValVo !=null){
                        operation.setIsMust(customDataAndValVo.getVals());
                    }
                    operation.setCheckCode(checkCode);
                }
            }
            if(sfc !=null){
                Sfc sfcEntity = HandleSfc(sfc);
                if (StrUtil.isNotEmpty(sfc.getDeviceBo())){
                    Device device = deviceMapper.selectById(sfc.getDeviceBo());
                    qcoVO.setCheckDevice(device.getDevice());
                    qcoVO.setCheckDeviceName(device.getDeviceName());
                }
                if(sfcEntity !=null){
                    qcoVO.setItem(sfcEntity.getItem());
                    qcoVO.setItemBo(sfcEntity.getItemBo());
                    qcoVO.setItemDesc(sfcEntity.getItemDesc());
                    qcoVO.setItemName(sfcEntity.getItemName());
                     if(sfcEntity.getOperationBo() !=null){
                         sfcEntity.setShopOrder(new ShopOrderHandleBO(sfcEntity.getShopOrderBo()).getShopOrder());
                     }
                }
                qcoVO.setSfcEntity(sfcEntity);
            }
            Operation operation = operationMapper.selectById(qualityCheckList.getOperationBo());
            qcoVO.setParameterName(qualityPlan);
            qcoVO.setOperationList(operations);
            return qcoVO;
        }
        else {
            List<OperationParamsVo> operations = new ArrayList<>();
            OperationParamsVo opVO = new OperationParamsVo();
            List<QualityPlanParameter> parameters = new ArrayList<>();
            OperationParamsVo operationParamsVo = new OperationParamsVo();
            operationParamsVo.setOperationName(qualityCheckList.getOperationName());
            BeanUtils.copyProperties(qualityCheckList,qcoVO);
            Sfc sfcEntity = HandleSfc(sfc);
            if (StrUtil.isNotEmpty(sfc.getDeviceBo())){
                Device device = deviceMapper.selectById(sfc.getDeviceBo());
                qcoVO.setCheckDevice(device.getDevice());
                qcoVO.setCheckDeviceName(device.getDeviceName());
            }
            Operation operation = operationMapper.selectById(qualityCheckList.getOperationBo());
            qcoVO.setOperationName(operation.getOperationName());
            // 是否是必填项
            if(operation !=null){
                CustomDataAndValVo customDataAndValVo=customDataValMapper.selectOperationInspectionNotRequire(UserUtils.getSite(), operation.getBo(), CustomDataTypeEnum.OPERATION.getDataType());
                if(customDataAndValVo !=null){
                    opVO.setIsMust(customDataAndValVo.getVals());
                }
            }
            // 校验
            if(StrUtil.isNotEmpty(sfcEntity.getShopOrderBo())){
                sfcEntity.setShopOrder(new ShopOrderHandleBO(sfcEntity.getShopOrderBo()).getShopOrder());
            }else {
                sfcEntity.setShopOrder(qualityCheckList.getShopOrder());
            }
            qcoVO.setSfcEntity(sfcEntity);
            if (StrUtil.isNotEmpty(operationDTO.getCheckCode())){
                List<QualityCheckParameter> qualityCheckParameters = qualityCheckResultMapper.selectList(new QueryWrapper<QualityCheckParameter>().eq("OPERATION_BO",operationBo).eq("CHECK_CODE",operationDTO.getCheckCode()));
                for (QualityCheckParameter checkParameter : qualityCheckParameters) {
                    QualityPlanParameter qualityPlanParameter = new QualityPlanParameter();
                    BeanUtils.copyProperties(checkParameter,qualityPlanParameter);
                    parameters.add(qualityPlanParameter);
                }
            }
            else {
                parameters = qualityPlanParameterMapper.selectList(new QueryWrapper<QualityPlanParameter>().eq("OPERATION_BO",operationBo));
            }
            opVO.setOperationName(operation.getOperationName());
            opVO.setParameters(parameters);
            operations.add(opVO);
            qcoVO.setParameterName(qualityPlan);
            qcoVO.setOperationList(operations);
            return qcoVO;
        }
    }

    private Sfc HandleSfc(Sfc sfc) {
        if (StrUtil.isNotEmpty(sfc.getItemBo())){
            Item item = itemMapper.selectById(sfc.getItemBo());
            sfc.setItem(item.getItem());
            sfc.setItemName(item.getItemName());
            sfc.setItemDesc(item.getItemDesc());
        }
        if (StrUtil.isNotEmpty(sfc.getWorkShopBo())){
            WorkShop workShop = workShopMapper.selectById(sfc.getWorkShopBo());
            sfc.setWorkshopName(workShop.getWorkShopDesc());
        }
        if (StrUtil.isNotEmpty(sfc.getDeviceBo())){
            Device device = deviceMapper.selectById(sfc.getDeviceBo());
            if(device !=null){
                sfc.setDevice(device.getDevice());
                sfc.setDeviceName(device.getDeviceName());
            }
        }
        return sfc;
    }

    @Override
    public String userVerify(Map<String, Object> params) {
        String userName = params.getOrDefault("userName", "").toString();
        String pwd = params.getOrDefault("pwd", "").toString();
        Map<String,String> param = new HashMap();
        param.put("userName", userName);
        param.put("pwd", PassWordUtil.encrypt(pwd,userName));
        return qualityCheckListMapper.userVerify(param);
    }

    @Override
    public QualityCheckAtParameterVO getQcoVOByOperation(OperationDTO operationDTO) throws CommonException {
        QualityCheckAtParameterVO qcoVO = new QualityCheckAtParameterVO();
        QueryWrapper<QualityCheckList> queryWrapper = new QueryWrapper();
        String itemBo=null;
        Operation operation = operationMapper.selectById(operationDTO.getOperationBo());
        Sfc sfcEntity = new Sfc();
        if(operationDTO.getShopOrder() !="" && operationDTO.getShopOrder() !=null){
            // 检验当前工序是否是工单对应工艺路线的首工序
            Map<String,String> map=routerProcessMapper.selectByShopOrder(operationDTO.getShopOrder());
            if(map !=null){
                // 解析json
                String processInfo = map.get("process_info");
                if(processInfo !=null && processInfo!="") {
                    JSONObject jsonObject = JSON.parseObject(processInfo);
                    JSONArray nodeList = JSONArray.parseArray(jsonObject.get("nodeList").toString());
                    JSONArray lineList = JSONArray.parseArray(jsonObject.get("lineList").toString());
                    String startId=sfcService.getStartId(nodeList);
                    String nextId=sfcService.getNextId(lineList,startId);
                    for (Object obj : nodeList) {
                        if(JSON.parseObject(obj.toString()).get("id").toString().equals(nextId)){
                            if (JSON.parseObject(obj.toString()).get("operation") != null) {
                                // 首工序
                                String firstOperation = JSON.parseObject(obj.toString()).get("operation").toString();
                                if (firstOperation.equals(operation.getBo())) {
                                    String shopOrderBo=map.get("bo");
                                    if(shopOrderBo !=null && shopOrderBo !=""){
                                        // 通过工单带出物料等信息
                                        ShopOrder shopOrder=shopOrderMapper.selectById(shopOrderBo);
                                        if(shopOrder !=null){
                                            Item item= itemMapper.selectById(shopOrder.getItemBo());
                                            if(item !=null){
                                                itemBo = item.getBo();
                                                sfcEntity.setItem(item.getItem());
                                                sfcEntity.setItemDesc(item.getItemDesc());
                                                sfcEntity.setItemName(item.getItemName());
                                                sfcEntity.setItemBo(item.getBo());
                                                qcoVO.setItem(item.getItem());
                                                qcoVO.setItemDesc(item.getItemDesc());
                                                qcoVO.setItemName(item.getItemName());
                                                qcoVO.setItemBo(item.getBo());
                                            }
                                        }
                                        WorkShop workShop = workShopMapper.selectOne(new QueryWrapper<WorkShop>().eq("WORK_SHOP",shopOrder.getWorkShop()));
                                        if (workShop !=null){
                                            sfcEntity.setWorkShopBo(workShop.getBo());
                                            sfcEntity.setWorkshopName(workShop.getWorkShopDesc());
                                            qcoVO.setWorkshopBo(workShop.getBo());
                                            qcoVO.setWorkshopName(workShop.getWorkShopDesc());
                                        }
                                /*qcoVO.setCheckDevice();
                                qcoVO.setCheckDeviceName();*/
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (StrUtil.isNotEmpty(operationDTO.getSfc())){
            sfcEntity = sfcMapper.selectBySfc(operationDTO.getSfc());
            /*if(sfcEntity !=null){
                // 校验sfc和工序是否对应
                if(!sfcEntity.getOperationBo().contains(operationDTO.getOperationBo())){
                    throw new CommonException("该批次条码不在本工序",30002);
                }
            }*/
            if(sfcEntity !=null){
                HandleSfc(sfcEntity);
                if (StrUtil.isNotEmpty(sfcEntity.getShopOrderBo())){
                    WorkShop workShop = workShopMapper.selectById(sfcEntity.getWorkShopBo());
                    sfcEntity.setWorkshopName(workShop.getWorkShopDesc());
                }
                sfcEntity.setShopOrder(new ShopOrderHandleBO(sfcEntity.getShopOrderBo()).getShopOrder());
                qcoVO.setItem(sfcEntity.getItem());
                qcoVO.setItemDesc(sfcEntity.getItemDesc());
                qcoVO.setItemName(sfcEntity.getItemName());
                itemBo = sfcEntity.getItemBo();
                qcoVO.setItemBo(sfcEntity.getItemBo());
                qcoVO.setWorkshopBo(sfcEntity.getWorkShopBo());
                qcoVO.setWorkshopName(sfcEntity.getWorkshopName());
                qcoVO.setCheckDevice(sfcEntity.getDevice());
                qcoVO.setCheckDeviceName(sfcEntity.getDeviceName());
            }
        }
        QualityPlan quality = qualityPlanService.getDefaultPlan();
        String qualityPlan = quality.getQualityPlan();
        List<OperationParamsVo> operations = new ArrayList<>();
        OperationParamsVo opVO = new OperationParamsVo();
        List<QualityPlanParameter> parameters = qualityPlanParameterMapper.selectList(new QueryWrapper<QualityPlanParameter>().eq("operation_bo",operationDTO.getOperationBo()).eq("ITEM_BO",itemBo).eq("ENABLED","1"));
        parameters=getAllParameter(parameters,operationDTO.getOperationBo(),itemBo,null);

        OperationParamsVo operationParamsVo = new OperationParamsVo();
        if(operation !=null){
            operationParamsVo.setOperationName(operation.getOperationName());
            qcoVO.setOperationName(operation.getOperationName());
            opVO.setOperationName(operation.getOperationName());
            // 是否是必填项
            if(operation.getBo() !=null && operation.getBo() !=""){
                CustomDataAndValVo customDataAndValVo=customDataValMapper.selectOperationInspectionNotRequire(UserUtils.getSite(), operation.getBo(), CustomDataTypeEnum.OPERATION.getDataType());
                if(customDataAndValVo !=null){
                    opVO.setIsMust(customDataAndValVo.getVals());
                }
            }
        }
        // 获取工位信息
        UserInfo userInfo=sfcService.getUserInfo();
        if(userInfo !=null){
            qcoVO.setStationName(userInfo.getStationName());
        }
        qcoVO.setSfcEntity(sfcEntity);
        opVO.setParameters(parameters);
        operations.add(opVO);
        qcoVO.setParameterName(qualityPlan);
        qcoVO.setOperationList(operations);
        qcoVO.setOperationBo(operationDTO.getOperationBo());
        return qcoVO;
    }

    public List<QualityPlanParameter> getAllParameter(List<QualityPlanParameter> parameters,String operationBo,String itemBo,String qualityPlan){
        if(operationBo !=null && operationBo !="") {
            // 该工序下的所有点检项（去重）
            List<String> parameterDescs = qualityPlanParameterMapper.selectDistinctInfo(operationBo,"1");
            // 工序，物料筛选出的点检项为空
            if (parameters == null || parameters.size() == 0) {
                parameters = new ArrayList<>();
                for (String str : parameterDescs) {
                    QualityPlanParameter qualityPlanParameter = new QualityPlanParameter();
                    qualityPlanParameter.setParameterDesc(str);
                    qualityPlanParameter.setAimVal("0");
                    qualityPlanParameter.setUpperLimit("0");
                    qualityPlanParameter.setLowerLimit("0");
                    qualityPlanParameter.setOperationBO(operationBo);
                    qualityPlanParameter.setItemBo(itemBo);
                    qualityPlanParameter.setQualityPlan(qualityPlan);
                    parameters.add(qualityPlanParameter);
                }
            } else if (parameters.size() != parameterDescs.size()) {
                //存在部分点检项缺失
                List<String> parameterDesc = qualityPlanParameterMapper.selectOriginInfo(operationBo, itemBo,"1");
                for (String str : parameterDesc) {
                    QualityPlanParameter qualityPlanParameter = new QualityPlanParameter();
                    qualityPlanParameter.setParameterDesc(str);
                    qualityPlanParameter.setAimVal("0");
                    qualityPlanParameter.setUpperLimit("0");
                    qualityPlanParameter.setLowerLimit("0");
                    qualityPlanParameter.setOperationBO(operationBo);
                    qualityPlanParameter.setItemBo(itemBo);
                    parameters.add(qualityPlanParameter);
                }
            }
        }
        return  parameters;
    }
}
