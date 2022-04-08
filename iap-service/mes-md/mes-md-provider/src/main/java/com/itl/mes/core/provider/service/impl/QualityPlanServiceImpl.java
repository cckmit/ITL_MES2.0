package com.itl.mes.core.provider.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.*;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.dto.QualityPlanParameterDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.QualityPlanAtParameterVo;
import com.itl.mes.core.api.vo.QualityPlanExcelInfoVo;
import com.itl.mes.core.api.vo.QualityPlanVO;
import com.itl.mes.core.api.vo.SnLogVo;
import com.itl.mes.core.provider.mapper.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lsl
 * @since 2019-08-29
 */
@Service
@Transactional
public class QualityPlanServiceImpl extends ServiceImpl<QualityPlanMapper, QualityPlan> implements QualityPlanService {

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private QualityPlanMapper qualityPlanMapper;

    @Autowired
    private QualityPlanParameterService qualityPlanParameterService;

    @Autowired
    private AttachedService attachedService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private StationService stationService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemGroupService itemGroupService;

    @Autowired
    private CodeRuleService codeRuleService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private WorkShopService workShopService;

    @Resource
    private UserUtil userUtil;

    @Autowired
    private QualityPlanParameterMapper qualityPlanParameterMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private RouterMapper routerMapper;


    @Override
    public String getQPnumber() throws CommonException {
        String str = qualityPlanMapper.selectMaxPlan();
        int qnum = Integer.parseInt(str.substring(str.length()- 4,str.length()));
        //String site = UserUtils.getSite();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
        String format = String.format("%04d", qnum+1);
        String QPnumber = "DyQcP_"+sdf.format(new Date())+format;
        return QPnumber;
    }

    /**
     * 保存或新增控制质量数据
     *
     * @param qualityPlanVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public QualityPlanParameter saveInUpdate(QualityPlanVO qualityPlanVO) throws CommonException {
        String QualityPlan = qualityPlanVO.getQualityPlan();
        String site = UserUtils.getSite();
        //控制明细表操作
        //判断请求内容是否重复
//        List<String> boList = new ArrayList<String>(); //存储明细表数据对应的BO
        String bo = new QualityPlanHandleBO(site, QualityPlan).getBo();
        Date date = new Date();
        QueryWrapper<QualityPlanParameter> queryWrapper = new QueryWrapper<QualityPlanParameter>();
        queryWrapper.eq("QUALITY_PLAN",qualityPlanVO.getQualityPlan());
        queryWrapper.eq("OPERATION_BO",qualityPlanVO.getOperationBO());
        queryWrapper.eq("PARAMETER_DESC",qualityPlanVO.getParameterDesc());
        QualityPlanParameter qpEntity = new QualityPlanParameter();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HHmmss");
        String bos = new QualityPlanParameterBO(site, sdf.format(date)).getBo();
        if (StrUtil.isNotEmpty(qualityPlanVO.getBo())){
            qpEntity.setBo(qualityPlanVO.getBo());
        }
        else{
            //qpEntity.setBo(bos);
            qpEntity.setBo(UUID.uuid32());
        }
        qpEntity.setSite(site);
        qpEntity.setQualityPlanBo(bo);
        qpEntity.setParameterDesc(qualityPlanVO.getParameterDesc());
        qpEntity.setAimVal(qualityPlanVO.getAimVal());
        qpEntity.setUpperLimit(qualityPlanVO.getUpperLimit());
        qpEntity.setLowerLimit(qualityPlanVO.getLowerLimit());
        qpEntity.setInspectType(qualityPlanVO.getInspectType());
        qpEntity.setInspectMethod(qualityPlanVO.getInspectMethod());
        qpEntity.setParameterType(qualityPlanVO.getParameterType());
        qpEntity.setInspectQty(qualityPlanVO.getInspectQty());
        qpEntity.setEnabled(qualityPlanVO.getEnabled());
        qpEntity.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), date);
//      qpEntity.setCreateDate(seletqpp.getCreateDate());
//      qpEntity.setCreateUser(seletqpp.getCreateUser());
        qpEntity.setModifyDate(date);
        qpEntity.setOperationBO(qualityPlanVO.getOperationBO());
        qpEntity.setInspectTool(qualityPlanVO.getInspectTool());
        qpEntity.setItemBo(qualityPlanVO.getItemBo());
        qpEntity.setOperationName(qualityPlanVO.getOperationName());
        qpEntity.setItemName(qualityPlanVO.getItemName());
        qpEntity.setQualityPlan(qualityPlanVO.getQualityPlan());
        qpEntity.setModifyUser(userUtil.getUser().getUserName());
        if (StrUtil.isEmpty(qualityPlanVO.getBo())){
            QualityPlanParameter qualityPlanParameter = qualityPlanParameterMapper.selectOne(queryWrapper);
            if (qualityPlanParameter != null){
                return null;
            }
            qualityPlanParameterMapper.insert(qpEntity);
        }
        else {
            qualityPlanParameterMapper.updateById(qpEntity);
        }
        return qualityPlanParameterMapper.selectById(qpEntity.getBo());
    }

    /**
     * 附加项分割数据验证
     * @param bo    控制质量计划BO ->ATTACHED_FROM_BO
     * @param right 编号 ->CONTEXT_BO
     * @param indx  判断对应哪个表数据 ->ATTACHED_KEY
     * @param index 同一条数据的序号 ->SEQ
     * @param count 附加总个数 ->COUNT_TOTAL
     */
    private void splitData(String bo, String right, int indx, int index, int count ,String type) throws CommonException {
        String childBO = "";
        if("PL".equals(type)){
            ProductLineHandleBO pBo = new ProductLineHandleBO(UserUtils.getSite(), right);
            productLineService.getExistProductLineByHandleBO(pBo);
            childBO = pBo.getBo();
        }
        if("OP".equals(type)){
            String[] itemOper = right.split("/");
            if (itemOper.length == 1) {
                String version = operationService.getBasicCurrentOperation(UserUtils.getSite(), itemOper[0]).getVersion();
                childBO = new OperationHandleBO(UserUtils.getSite(), itemOper[0], version).getBo();
            } else if (itemOper.length == 2) {
                operationService.selectByOperation(itemOper[0],itemOper[1]);
                childBO = new OperationHandleBO(UserUtils.getSite(), itemOper[0], itemOper[1]).getBo();
            }
        }
        if("ITEM".equals(type)){
            String[] items = right.split("/");
            if (items.length == 1) {
                String version = itemService.selectByItemAndSite(items[0], UserUtils.getSite()).getVersion();
                childBO = new ItemHandleBO(UserUtils.getSite(), items[0], version).getBo();
            } else if (items.length == 2) {
                itemService.getExitsItemByItemHandleBO(new ItemHandleBO(UserUtils.getSite(), items[0], items[1]));
                childBO = new ItemHandleBO(UserUtils.getSite(), items[0], items[1]).getBo();
            }
        }
        if("IG".equals(type)){
            ItemGroupHandleBO itemGroupHandleBO = new ItemGroupHandleBO(UserUtils.getSite(), right);
            itemGroupService.getItemGroupByItemGroupBO(itemGroupHandleBO);
            childBO = itemGroupHandleBO.getBo();
        }
        if("WS".equals(type)){
            WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO(UserUtils.getSite(),right);
            workShopService.getById(workShopHandleBO.getBo());
            childBO = workShopHandleBO.getBo();
        }
        Attached attached = new Attached();
        String newsbo = new AttachedBO(UserUtils.getSite(), bo, index+1, indx).getBo();
        attached.setCountTotal(count);
        attached.setAttachedType("Q");
        attached.setContextBo(childBO);
        attached.setSeq(index+1);
        attached.setAttachedKey(indx + 1);
        attached.setBo(newsbo);
        attached.setAttachedFromBo(bo);
        attached.setSite(UserUtils.getSite());
        attachedService.save(attached);
    }


    /**
     * 删除数据
     *
     * @param qualityPlan
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteQuality(String qualityPlan) throws CommonException {
        QualityPlanParameterDTO qppDto = new QualityPlanParameterDTO();
        qppDto.setQualityPlan(qualityPlan);
        QualityPlan qualityPlanEntity = selectByInspectType(qppDto);
        qualityPlanMapper.deleteById(qualityPlanEntity.getBo());

        QueryWrapper<QualityPlanParameter> QueryWrapper = new QueryWrapper<QualityPlanParameter>();
        QueryWrapper.eq(QualityPlanParameter.SITE, UserUtils.getSite());
        QueryWrapper.eq(QualityPlanParameter.QUALITY_PLAN_BO, qualityPlanEntity.getBo());
        qualityPlanParameterService.remove(QueryWrapper);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param params
     * @return
     */
    @Override
    public IPage<Map> selectQualityPlanPage(IPage<Map> page, Map<String, Object> params) {

        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<Map> qualityPlanPageList = qualityPlanMapper.selectQualityPlanPage(page, params);
        page.setRecords(qualityPlanPageList);
        return page;
    }

    /**
     * 查询检验类型数据Entity
     *
     * @param qppDto
     * @return
     * @throws CommonException
     */
    @Override
    public QualityPlan selectByInspectType(QualityPlanParameterDTO qppDto) throws CommonException {

        QueryWrapper<QualityPlan> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq(QualityPlan.QUALITY_PLAN, qppDto.getQualityPlan());
        QueryWrapper.eq(QualityPlan.SITE, UserUtils.getSite());
        List<QualityPlan> list = qualityPlanMapper.selectList(QueryWrapper);
        if (list.isEmpty()) {
            return null;
            //throw new CommonException("检验类型编号:" + qualityPlan + "未维护或不是当前版本", CommonExceptionDefinition.BASIC_EXCEPTION);
        } else {
            return list.get(0);
        }
    }

    /**
     * 获取明细列表数据
     *
     * @param qualityPlan
     * @param qppDto
     * @return
     */
    @Override
    public IPage<QualityPlanParameter> getParameterList(QualityPlanParameterDTO qppDto , QualityPlan qualityPlan) throws CommonException {
       /* QueryWrapper<QualityPlanParameter> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq(QualityPlanParameter.SITE, UserUtils.getSite());
        if (qppDto.getOperationName() != null){
            QueryWrapper.like("OPERATION_NAME",qppDto.getOperationName());
        }
        if (qppDto.getItemName() != null){
            QueryWrapper.like("ITEM_NAME",qppDto.getItemName());
        }
        if (qppDto.getParameterDesc() != null){
            QueryWrapper.like("PARAMETER_DESC",qppDto.getParameterDesc());
        }
        if (qppDto.getItem() !=null &&qppDto.getItem() !=""){
            Item itemEntity=itemMapper.selectOne(new QueryWrapper<Item>().eq("item",qppDto.getItem()));
            if(itemEntity !=null){
                QueryWrapper.eq("item_bo",itemEntity.getBo());
            }
        }
        if (qualityPlan!=null){
            QueryWrapper.eq(QualityPlanParameter.QUALITY_PLAN_BO, qualityPlan.getBo());
        }
        IPage<QualityPlanParameter> page = qualityPlanParameterMapper.selectPage(qppDto.getPage(), QueryWrapper);*/
        String qualityPlanBo = null;
        if (qualityPlan!=null){
            qualityPlanBo=qualityPlan.getBo();
        }
        IPage<QualityPlanParameter> page = qualityPlanParameterMapper.selectQualityPlanPage(qppDto.getPage(),qppDto,qualityPlanBo,UserUtils.getSite());
        return page;
    }

    /**
     * 精确查询
     *
     * @param qppDto
     * @return
     * @throws CommonException
     */
    @Override
    public IPage<QualityPlanParameter> getQpapVoByQualityPlan(QualityPlanParameterDTO qppDto) throws CommonException {
        QualityPlan qualityPlanEntity = selectByInspectType(qppDto);
        IPage<QualityPlanParameter> planParameterIPage = getParameterList(qppDto,qualityPlanEntity);
        QualityPlanAtParameterVo qualityPlanAtParameterVo = new QualityPlanAtParameterVo();
        //拷贝属性
        if (qualityPlanEntity!=null){
            BeanUtils.copyProperties(qualityPlanEntity, qualityPlanAtParameterVo);
            //附加项设置
            List<Attached> attacheds = getAttachList(qualityPlanEntity.getBo());
            qualityPlanAtParameterVo.setAttachedList(attacheds);
        }
        return planParameterIPage;
    }

    /**
     * 获取已附加对象
     *
     * @return
     */
    public List<Attached> getAttachList(String bo) {
        QueryWrapper<Attached> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq(Attached.SITE, UserUtils.getSite());
        QueryWrapper.eq(Attached.ATTACHED_FROM_BO, bo);
        return attachedService.list(QueryWrapper);
    }

    /**
     * 导出文件
     *
     * @param site
     * @param response
     * @throws CommonException
     */
    @Override
    public void exportQplan(String site, HttpServletResponse response) throws CommonException {
        QueryWrapper<QualityPlan> qpQueryWrapper = new QueryWrapper<QualityPlan>();
        qpQueryWrapper.eq(QualityPlan.SITE, site);
        List<QualityPlan> qPlanList = qualityPlanMapper.selectList(qpQueryWrapper);

        // 创建参数对象（用来设定excel得sheet得内容等信息）
        ExportParams QualityPlanExport = new ExportParams();
        // 设置sheet得名称
        QualityPlanExport.setSheetName("质量控制计划表");
        // 创建sheet1使用得map
        Map<String, Object> qpExportMap = new HashMap<>();
        // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
        qpExportMap.put("title", QualityPlanExport);
        // 模版导出对应得实体类型
        qpExportMap.put("entity", QualityPlan.class);
        // sheet中要填充得数据
        qpExportMap.put("data", qPlanList);

        // 创建sheet2
        QueryWrapper<QualityPlanParameter> qppQueryWrapper = new QueryWrapper<QualityPlanParameter>();
        qppQueryWrapper.eq(QualityPlanParameter.SITE, site);
        List<QualityPlanParameter> qualityPlanParameterList = qualityPlanParameterService.list(qppQueryWrapper);

        ExportParams QualityPlanParameterExport = new ExportParams();
        QualityPlanParameterExport.setSheetName("计划明细表");

        Map<String, Object> qppExportMap = new HashMap<>();
        qppExportMap.put("title", QualityPlanParameterExport);
        qppExportMap.put("entity", QualityPlanParameter.class);
        qppExportMap.put("data", qualityPlanParameterList);

        //创建sheet3
        QueryWrapper<Attached> attachWrapper = new QueryWrapper<Attached>();
        qppQueryWrapper.eq(Attached.SITE, site);
        List<Attached> attachedList = attachedService.list(attachWrapper);

        ExportParams AttachedExport = new ExportParams();
        AttachedExport.setSheetName("附加数据表");
        Map<String, Object> attExportMap = new HashMap<>();
        attExportMap.put("title", AttachedExport);
        attExportMap.put("entity", Attached.class);
        attExportMap.put("data", attachedList);

        // 创建自定义数据-> map
        List<CustomDataVal> customDataValList = new ArrayList<>();
        for ( QualityPlan qualityPlan: qPlanList) {
            String bo = new QualityPlanHandleBO(site,qualityPlan.getQualityPlan()).getBo();
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

        // 将sheet1、sheet2、sheet3使用得map进行包装
        List<Map<String, Object>> sheetsList = new ArrayList<>();
        sheetsList.add(qpExportMap);
        sheetsList.add(qppExportMap);
        sheetsList.add(attExportMap);
        sheetsList.add(customMap);
        // 执行方法
        ExcelUtils.exportExcel(sheetsList,"质量控制计划数据表",response);
    }
    /**
     * 导入文件
     *
     * @param file
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void importExcel(MultipartFile file) throws CommonException {

        List<QualityPlan> qualityPlanList = ExcelUtils.importExcel(file, 0, 1, QualityPlan.class);
        List<QualityPlanParameter> qualityPlanParameterList = ExcelUtils.importExcel(file, 1,0, 1, QualityPlanParameter.class);
        List<Attached> attachedList = ExcelUtils.importExcel(file, 2,0, 1, Attached.class);
        List<CustomDataVal> customDataValList = ExcelUtils.importExcel(file, 3,0, 1, CustomDataVal.class);
        if (qualityPlanList.size() == 0) {
            throw new CommonException("质量控制计划表 数据不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        } else {
        }
        if (qualityPlanParameterList.size() == 0) {
            throw new CommonException("质量控制计划明细项 数据不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        } else {
        }
        super.saveBatch( qualityPlanList );
        qualityPlanParameterService.saveBatch( qualityPlanParameterList );

        if (attachedList.size() != 0) {
//            for (Attached attached : attachedList) {
//                attachedService.insert(attached);
//            }
            attachedService.saveBatch(attachedList);
        }
        if (customDataValList.size() != 0) {
//            for (CustomDataVal customDataVal : customDataValList) {
//                customDataValService.insert(customDataVal);
//            }
            customDataValService.saveBatch(customDataValList);

        }
    }


    @Override
    public List<QualityPlan> selectList() {
        QueryWrapper<QualityPlan> QueryWrapper = new QueryWrapper<QualityPlan>();
        //getQueryWrapper(QueryWrapper, qualityPlan);
        return super.list(QueryWrapper);
    }

    @Override
    public boolean setDefaultPlan(String qualityPlan){
        QualityPlan plan = qualityPlanMapper.selectOne(new QueryWrapper<QualityPlan>().eq("QUALITY_PLAN", qualityPlan));
        plan.setIsCurrentVersion("Y");
        qualityPlanMapper.update(plan,new QueryWrapper<QualityPlan>().eq("QUALITY_PLAN", qualityPlan));
        Map<String,String> param = new HashMap();
        param.put("qualityPlan", qualityPlan);
        qualityPlanMapper.setDefaultPlan(param);
        return true;
    }

    @Override
    public QualityPlan getDefaultPlan(){
        return qualityPlanMapper.selectOne(new QueryWrapper<QualityPlan>().eq("IS_CURRENT_VERSION","Y"));
    }

    @Override
    public QualityPlan saveQualityPlan(QualityPlan qualityPlan) throws CommonException {
        String QualityPlan = qualityPlan.getQualityPlan();
        String site = UserUtils.getSite();
        //控制明细表操作
        //判断请求内容是否重复
        String bo = new QualityPlanHandleBO(site, QualityPlan).getBo();
        String quality = qualityPlan.getQualityPlan();
        QualityPlan qualityPlanEntity = qualityPlanMapper.selectOne(new QueryWrapper<QualityPlan>().eq("QUALITY_PLAN",quality));
        QualityPlan newQualityPlan = new QualityPlan();
        //质量控制表操作,找不到则插入
        if (qualityPlanEntity == null) {
            newQualityPlan.setBo(bo);
            newQualityPlan.setIsCurrentVersion(qualityPlan.getIsCurrentVersion());
            newQualityPlan.setQualityPlan(QualityPlan);
            newQualityPlan.setQualityPlanDesc(qualityPlan.getQualityPlanDesc());
            newQualityPlan.setSite(site);
            newQualityPlan.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
            qualityPlanMapper.insert(newQualityPlan);
            if (qualityPlan.getIsCurrentVersion().equals("Y")){
                setDefaultPlan(newQualityPlan.getQualityPlan());
            }
        } else {
            //否则更新数据库中对应的数据
            newQualityPlan.setBo(bo);
            newQualityPlan.setIsCurrentVersion(qualityPlan.getIsCurrentVersion());
            newQualityPlan.setQualityPlan(QualityPlan);
            newQualityPlan.setQualityPlanDesc(qualityPlan.getQualityPlanDesc());
            newQualityPlan.setSite(site);

            newQualityPlan.setCreateUser(qualityPlanEntity.getCreateUser());
            newQualityPlan.setCreateDate(qualityPlanEntity.getCreateDate());
            Date newDate = new Date();
            newQualityPlan.setModifyDate(newDate);
            newQualityPlan.setModifyUser(userUtil.getUser().getUserName());
            qualityPlanMapper.updateById(newQualityPlan);
            if (qualityPlan.getIsCurrentVersion().equals("Y")){
                setDefaultPlan(newQualityPlan.getQualityPlan());
            }
            qualityPlan.setModifyDate(newDate);
        }
        return qualityPlanMapper.selectById(bo);
    }

    @Override
    public void exportByItem(String item, HttpServletResponse response) throws CommonException {
        if(StringUtils.isBlank(item)){
            throw new CommonException("请选择物料",30002);
        }
        // 校验该物料下的是否存在工艺路线
        Item itemEntity=itemMapper.selectOne(new QueryWrapper<Item>().eq("item",item));
        if(StringUtils.isBlank(itemEntity.getRouterName())){
            throw new CommonException("该物料没有工艺配置工艺路线",30002);
        }
        // 查找当前版本的工艺路线
        QueryWrapper<Router> qw = new QueryWrapper<>();
        qw.eq("router",itemEntity.getRouterName());
        qw.eq("is_current_version",1);
        Router router = routerMapper.selectOne(qw);
        if(router == null){
            throw new CommonException("该物料配置的工艺路线未选择当前版本",30002);
        }
        String processInfo=itemMapper.selectProcessInfoByItem(router.getBo());
        if(StringUtils.isBlank(processInfo)){
            throw new CommonException("未配置工艺路线步骤",30002);
        }
        //获取数据
        List<QualityPlanExcelInfoVo> qualityPlanExcelInfoVos=getAllPatameterByItem(processInfo,item);
        ExcelUtils.exportExcel(qualityPlanExcelInfoVos,"检验项信息表","检验项信息表", QualityPlanExcelInfoVo.class, "检验项信息表", response);
    }

    @Override
    public void saveQualityPlanParameter(QualityPlanExcelInfoVo qualityPlanExcelInfoVo,int row) throws CommonException {
        String site = UserUtils.getSite();
        // 根据 物料BO，工序Bo，检验项名称判断m_quality_plan_parameter 是否存在该数据
        if(StringUtils.isNotBlank(qualityPlanExcelInfoVo.getItem()) && StringUtils.isNotBlank(qualityPlanExcelInfoVo.getOperation()) && StringUtils.isNotBlank(qualityPlanExcelInfoVo.getParameterDesc())){
            Item item=itemMapper.selectOne(new QueryWrapper<Item>().eq("item",qualityPlanExcelInfoVo.getItem()));
            Operation operation=operationMapper.selectOne(new QueryWrapper<Operation>().eq("operation",qualityPlanExcelInfoVo.getOperation()));
            if(item !=null && operation !=null){
                QualityPlanParameter qualityPlanParameterEntity=qualityPlanParameterMapper
                .selectOne(new QueryWrapper<QualityPlanParameter>()
                .eq("ITEM_BO",item.getBo()).eq("OPERATION_BO",operation.getBo()).eq("PARAMETER_DESC",qualityPlanExcelInfoVo.getParameterDesc()));
                if(qualityPlanParameterEntity ==null){
                    // 新增数据
                    Date date = new Date();
                    QualityPlanParameter qualityPlanParameter=new QualityPlanParameter();
                    // 拷贝
                    /*SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                    qualityPlanParameterMapper.selectCount(new QueryWrapper<>())
                    String bo = new QualityPlanParameterBO(site, sdf.format(date)).getBo();*/
                    String bo= UUID.uuid32();
                    bo="im_"+bo;
                    qualityPlanParameter.setBo(bo);
                    qualityPlanParameter.setSite(site);
                    // 获取默认的控制计划
                    QualityPlan qualityPlan =getDefaultPlan();
                    qualityPlanParameter.setQualityPlan(qualityPlan.getQualityPlan());
                    qualityPlanParameter.setQualityPlanBo(qualityPlan.getBo());
                    qualityPlanParameter.setItemBo(item.getBo());
                    qualityPlanParameter.setItemName(item.getItemName());
                    qualityPlanParameter.setOperationBO(operation.getBo());
                    qualityPlanParameter.setOperationName(operation.getOperationName());
                    qualityPlanParameter.setCreateDate(date);

                    qualityPlanParameter.setParameterDesc(qualityPlanExcelInfoVo.getParameterDesc());
                    qualityPlanParameter.setInspectTool(qualityPlanExcelInfoVo.getInspectTool());
                    qualityPlanParameter.setInspectMethod(qualityPlanExcelInfoVo.getInspectMethod());
                    if(qualityPlanExcelInfoVo.getUpperLimit() !=null && qualityPlanExcelInfoVo.getUpperLimit() !=""){
                        qualityPlanParameter.setUpperLimit(qualityPlanExcelInfoVo.getUpperLimit());
                    }
                    if(qualityPlanExcelInfoVo.getLowerLimit() !=null && qualityPlanExcelInfoVo.getLowerLimit() !=""){
                        qualityPlanParameter.setLowerLimit(qualityPlanExcelInfoVo.getLowerLimit());
                    }
                    qualityPlanParameter.setAimVal(qualityPlanExcelInfoVo.getAimVal());
                    qualityPlanParameterMapper.insert(qualityPlanParameter);
                }else {
                    // 修改数据
                    BeanUtils.copyProperties(qualityPlanExcelInfoVo,qualityPlanParameterEntity);
                    if(qualityPlanExcelInfoVo.getUpperLimit() !=null && qualityPlanExcelInfoVo.getUpperLimit() !=""){
                        qualityPlanParameterEntity.setUpperLimit(qualityPlanExcelInfoVo.getUpperLimit());
                    }
                    if(qualityPlanExcelInfoVo.getLowerLimit() !=null && qualityPlanExcelInfoVo.getLowerLimit() !=""){
                        qualityPlanParameterEntity.setLowerLimit(qualityPlanExcelInfoVo.getLowerLimit());
                    }
                    Date date = new Date();
                    qualityPlanParameterEntity.setModifyDate(date);
                    qualityPlanParameterMapper.updateById(qualityPlanParameterEntity);
                }
            }
        }else {
            // 判断是否整个对象属性都是null
            if(!allFieldIsNULL(qualityPlanExcelInfoVo)){
                if(StringUtils.isBlank(qualityPlanExcelInfoVo.getItem())){
                    throw new CommonException("表格中第"+row+"行未填写物料编码",30002);
                }
                if(StringUtils.isBlank(qualityPlanExcelInfoVo.getOperation())){
                    throw new CommonException("表格中第"+row+"行未填写工序编码",30002);
                }

                if(StringUtils.isBlank(qualityPlanExcelInfoVo.getParameterDesc())){
                    throw new CommonException("表格中第"+row+"行未填写检验项",30002);
                }
            }
        }
    }

    // 解析工艺路线
    public List<QualityPlanExcelInfoVo> getAllPatameterByItem(String processInfo, String item) throws CommonException {
        List<QualityPlanExcelInfoVo> list=new ArrayList<>();
        JSONObject processInfoObj= JSON.parseObject(processInfo);
        // 检查nodeList 是否为空
        if(processInfoObj.get("nodeList")==null || StringUtils.isBlank(processInfoObj.get("nodeList").toString())){
            throw new CommonException("未配置工艺路线步骤",30002);
        }
        JSONArray nodeArray=JSONArray.parseArray(processInfoObj.get("nodeList").toString());
        for(Object node:nodeArray){
            JSONObject operation=JSON.parseObject(node.toString());
            if(operation.get("operation") !=null && StringUtils.isNotBlank(operation.get("operation").toString())){
                String operationBo=operation.get("operation").toString();
                List<QualityPlanExcelInfoVo> parameters=getQualityPlanParameter(item,operationBo);
                list.addAll(parameters);
            }
        }
        return list;
    }
    // 带出检验项
    public List<QualityPlanExcelInfoVo> getQualityPlanParameter(String item, String operationBo){
        Item itemEntity=itemMapper.selectOne(new QueryWrapper<Item>().eq("item",item));
        Operation operation=operationMapper.selectById(operationBo);
        List<QualityPlanExcelInfoVo> parameters = qualityPlanParameterMapper.selectParams(operationBo,itemEntity.getBo());
        // 该工序下的所有点检项（去重）
        List<String> parameterDescs=qualityPlanParameterMapper.selectDistinctInfo(operationBo,"");
        // 工序，物料筛选出的点检项为空
        if(parameters == null || parameters.size()==0){
            parameters=new ArrayList<>();
            for(String str:parameterDescs){
                QualityPlanExcelInfoVo qualityPlanExcelInfoVo=new QualityPlanExcelInfoVo();
                qualityPlanExcelInfoVo.setParameterDesc(str);
                qualityPlanExcelInfoVo.setOperationName(operation.getOperationName());
                qualityPlanExcelInfoVo.setItem(item);
                qualityPlanExcelInfoVo.setOperation(operation.getOperation());
                parameters.add(qualityPlanExcelInfoVo);
            }
        }else if(parameters.size() !=parameterDescs.size()){
            //存在部分点检项缺失
            List<String> parameterDesc=qualityPlanParameterMapper.selectOriginInfo(operationBo,itemEntity.getBo(),"");
            for(String str:parameterDesc){
                QualityPlanExcelInfoVo qualityPlanExcelInfoVo=new QualityPlanExcelInfoVo();
                qualityPlanExcelInfoVo.setParameterDesc(str);
                qualityPlanExcelInfoVo.setOperationName(operation.getOperationName());
                qualityPlanExcelInfoVo.setItem(item);
                qualityPlanExcelInfoVo.setOperation(operation.getOperation());
                parameters.add(qualityPlanExcelInfoVo);
            }
        }
        return parameters;
    }
    public static boolean allFieldIsNULL(Object o){
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                Object object = field.get(o);
                if (object instanceof CharSequence) {
                    if (!org.springframework.util.ObjectUtils.isEmpty(object)) {
                        return false;
                    }
                } else {
                    if (null != object) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("判断对象属性为空异常");
        }
        return true;
    }


}
