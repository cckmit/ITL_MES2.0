package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CodeRuleFullVo;
import com.itl.mes.core.api.vo.InspectTaskVo;
import com.itl.mes.core.api.vo.InspectTypeVo;
import com.itl.mes.core.provider.mapper.InspectTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 检验任务 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-08-30
 */
@Service
@Transactional
public class InspectTaskServiceImpl extends ServiceImpl<InspectTaskMapper, InspectTask> implements InspectTaskService {


    @Autowired
    private InspectTaskMapper inspectTaskMapper;

    @Autowired
    private InspectTypeService inspectTypeService;

    @Autowired
    private QualityPlanService qualityPlanService;

    @Autowired
    private QualityPlanParameterService qualityPlanParameterService;

    @Autowired
    private WorkShopService workShopService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private SnService snService;

    @Autowired
    private CodeRuleService ruleService;

    @Resource
    private UserUtil userUtil;


    @Override
    public List<InspectTask> selectList() {
        QueryWrapper<InspectTask> QueryWrapper = new QueryWrapper<InspectTask>();
        //getQueryWrapper(QueryWrapper, inspectTask);
        return super.list(QueryWrapper);
    }

    @Override
    public IPage<InspectTask> selectinspectTaskPage(IPage<InspectTask> page, Map<String, Object> params) {
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        List<InspectTask> inspectTaskLiat = inspectTaskMapper.selectinspectTaskPage(page, params);
        page.setRecords(inspectTaskLiat);
        return page;
    }

    @Override
    public InspectTypeVo getInspectType(String inspectType) throws CommonException {
        InspectTypeVo inspectTypeVo = inspectTypeService.getInspectTypeVoByInspectType(inspectType);
        return inspectTypeVo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void createInspectTask(InspectTaskVo inspectTaskVo) throws CommonException {
        InspectTypeVo inspectTypeVo = inspectTypeService.getInspectTypeVoByInspectType(inspectTaskVo.getInspectType());
        if ("1".equals(inspectTypeVo.getRequiredWorkShop()) && StrUtil.isBlank(inspectTaskVo.getWorkShop())) {
            throw new CommonException("车间是必填条件!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if ("1".equals(inspectTypeVo.getRequiredProductLine()) && StrUtil.isBlank(inspectTaskVo.getProductLine())) {
            throw new CommonException("线位是必填条件!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if ("1".equals(inspectTypeVo.getRequiredItem()) && StrUtil.isBlank(inspectTaskVo.getItem())) {
            throw new CommonException("物料是必填条件!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if ("1".equals(inspectTypeVo.getRequiredShopOrder()) && StrUtil.isBlank(inspectTaskVo.getShopOrder())) {
            throw new CommonException("工单是必填条件!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if ("1".equals(inspectTypeVo.getRequiredOperation()) && StrUtil.isBlank(inspectTaskVo.getOperation())) {
            throw new CommonException("工序是必填条件!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if ("1".equals(inspectTypeVo.getRequiredSfc()) && StrUtil.isBlank(inspectTaskVo.getSn())) {
            throw new CommonException("产品条码是必填条件!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        //一个条码 只 对应 一个检验任务(新建 进行中)
        if (!StrUtil.isBlank(inspectTaskVo.getSn())) {
            QueryWrapper<InspectTask> inspectTaskQueryWrapper = new QueryWrapper<>();
            inspectTaskQueryWrapper.eq(InspectTask.SN, inspectTaskVo.getSn()).eq(InspectTask.SITE, UserUtils.getSite())
                    .in(InspectTask.STATE, new String[]{"0", "1"});
            List<InspectTask> inspectTasks = inspectTaskMapper.selectList(inspectTaskQueryWrapper);
            if (inspectTasks.size() > 0) {
                throw new CommonException("产品条码:" + inspectTaskVo.getSn() + "已存在进行中的检验任务!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }

        String site = UserUtils.getSite();
        Map<String, Object> params = new HashMap<>();
        int num = 0;
        if (!StrUtil.isBlank(inspectTaskVo.getWorkShop())) {
            WorkShop workShop = workShopService.validateWorkShopIsExist(site, inspectTaskVo.getWorkShop());
            params.put("workShop", workShop.getBo());
            num++;
        }
        if (!StrUtil.isBlank(inspectTaskVo.getProductLine())) {
            ProductLine existProductLine = productLineService.getExistProductLineByHandleBO(new ProductLineHandleBO(site, inspectTaskVo.getProductLine()));
            params.put("productLine", existProductLine.getBo());
            num++;
        }
        if (!StrUtil.isBlank(inspectTaskVo.getItem())) {
            Item item = itemService.selectByItem(inspectTaskVo.getItem());
            params.put("item", item.getBo());
            num++;
        }
        if (!StrUtil.isBlank(inspectTaskVo.getShopOrder())) {
            ShopOrder existShopOrder = shopOrderService.getExistShopOrder(new ShopOrderHandleBO(site, inspectTaskVo.getShopOrder()));
            params.put("shopOrder", existShopOrder.getBo());
            num++;
        }
        if (!StrUtil.isBlank(inspectTaskVo.getOperation())) {
            QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
            operationQueryWrapper.eq(Operation.SITE, site).eq(Operation.OPERATION, inspectTaskVo.getOperation()).eq(Operation.IS_CURRENT_VERSION, "Y");
            List<Operation> operations = operationService.list(operationQueryWrapper);
            if (operations.size() == 0) {
                throw new CommonException("当前版本工序:" + inspectTaskVo.getOperation() + "未维护!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            params.put("operation", operations.get(0).getBo());
            num++;
        }
        if (!StrUtil.isBlank(inspectTaskVo.getSn())) {
            Sn exitsSn = snService.getExitsSn(new SnHandleBO(site, inspectTaskVo.getSn()));
            params.put("sn", exitsSn.getBo());
            num++;
        }
        if (params != null && !params.containsKey("site")) {
            params.put("site", UserUtils.getSite());
        }
        params.put("num", num);
        params.put("inspectType", inspectTaskVo.getInspectType());
        List<Attached> attachedList = inspectTaskMapper.selectAttached(params);

        Map<String, List<Attached>> map = new HashMap<>();
        String key = null;
        for (Attached attached : attachedList) {
            key = attached.getAttachedFromBo() + "," + attached.getSeq().toString();
            if (map.containsKey(key)) {
                map.get(key).add(attached);
            } else {
                map.put(key, new ArrayList<Attached>() {{
                    add(attached);
                }});
            }
        }
        Map<String, List<Attached>> map2 = new HashMap<>();
        for (String key2 : map.keySet()) {

            if (map.get(key2).size() == map.get(key2).get(0).getCountTotal()) {
                map2.put(key2, map.get(key2));
            }

        }

        Map<String, List<Attached>> map3;
        if (map2.size() == 0) {
            map3 = getAttachedSize(map, num);
        } else {
            map3 = getAttachedSize(map2, num);
        }

        Map<String, Object> map4 = new HashMap<>();
        for (String key3 : map3.keySet()) {
            for (Attached attached : map3.get(key3)) {
                if (map4.containsKey(attached.getAttachedFromBo())) {
                    continue;
                }
                map4.put(attached.getAttachedFromBo(), attached.getAttachedFromBo());
            }
        }

        if (map4.isEmpty()) {
            throw new CommonException("检验类型未匹配到质量控制计划!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        List<QualityPlan> qualityPlans = new ArrayList<>();
        for (String key4 : map4.keySet()) {
            qualityPlans.add(qualityPlanService.getById(map4.get(key4).toString()));
        }

        InspectTask task = new InspectTask(); //保存InspectTask

        //用质量控制计划查询明细获取 检验任务类型 对比 前端传来的检验任务类型
        QualityPlan affirmQualityPlan = null;
        if (qualityPlans.size() > 0) {
            for (QualityPlan qualityPlan : qualityPlans) {
                QueryWrapper<QualityPlanParameter> qualityPlanParameterQueryWrapper = new QueryWrapper<>();
                qualityPlanParameterQueryWrapper.eq(QualityPlanParameter.QUALITY_PLAN_BO, qualityPlan.getBo())
                        .eq(QualityPlanParameter.SITE, site).eq(QualityPlanParameter.INSPECT_TYPE, inspectTaskVo.getInspectType())
                        .eq(QualityPlanParameter.ENABLED, "1");
                List<QualityPlanParameter> qualityPlanParameters = qualityPlanParameterService.list(qualityPlanParameterQueryWrapper);
                if (qualityPlanParameters.size() > 0) {
                    affirmQualityPlan = qualityPlan;
                    break;
                }
            }
        }

        if (affirmQualityPlan == null) {
            throw new CommonException("检验类型未匹配到质量控制计划!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        task.setQualityPlanBo(affirmQualityPlan.getBo());

        CodeRuleFullVo fullVo = ruleService.getCodeRuleType("INSPECTTASK");
        Map<String, Object> paramMap = new HashMap<>();
        fullVo.getCodeRuleItemVoList().forEach(ruleItem -> {
            if ("4".equals(ruleItem.getSectType())) {
                paramMap.put(ruleItem.getSectParam(), inspectTaskVo.getInspectType());
            }
        });

        String nextNumber = ruleService.generatorNextNumber(new CodeRuleHandleBO(site, "INSPECTTASK").getBo(), paramMap);
        task.setBo(new InspectTaskHandleBO(site, nextNumber).getBo());
        task.setSite(site);
        task.setInspectTask(nextNumber);
        task.setInspectType(inspectTaskVo.getInspectType());
        //task.setInspectResult();
        task.setWorkShop(inspectTaskVo.getWorkShop());
        task.setProductLine(inspectTaskVo.getProductLine());
        task.setOperation(inspectTaskVo.getOperation());
        task.setItem(inspectTaskVo.getItem());
        task.setShopOrder(inspectTaskVo.getShopOrder());
        task.setSn(inspectTaskVo.getSn());
        task.setState("0");
        task.setCreateMethod("M");
        task.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
        inspectTaskMapper.insert(task);
    }


    @Override
    public void deleteInspectTask(List<InspectTaskVo> inspectTaskVos) throws CommonException {
        if (inspectTaskVos.size() > 0) {
            for (InspectTaskVo inspectTaskVo : inspectTaskVos) {
                InspectTask existInspectTask = getExistInspectTask(inspectTaskVo.getInspectTask());
                CommonUtil.compareDateSame(inspectTaskVo.getModifyDate(), existInspectTask.getModifyDate());
                inspectTaskMapper.deleteById(existInspectTask);
            }

        } else {
            throw new CommonException("请选择删除项!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
    }


    public InspectTask getExistInspectTask(String inspectTask) throws CommonException {
        InspectTaskHandleBO inspectTaskHandleBO = new InspectTaskHandleBO(UserUtils.getSite(), inspectTask);
        InspectTask task = inspectTaskMapper.selectById(inspectTaskHandleBO);
        if (task == null) {
            throw new CommonException("检验任务编号:" + inspectTask + "未维护!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return task;
    }

    @Override
    public void createInspectTaskByFqc(InspectTaskVo inspectTaskVo) throws CommonException {
        //获取SN
        Sn exitsSn = snService.getExitsSn(new SnHandleBO(UserUtils.getSite(), inspectTaskVo.getSn()));
        //获取检所有验任务类型
        List<InspectType> inspectTypes = inspectTypeService.selectList();
        for (InspectType inspectType : inspectTypes) {
            //获取检验任务类型自定义数据 -是否自动创建FQC任务
            String val = snService.getSelfDefiningData(UserUtils.getSite(), "INSPECT_TYPE", "IS_AUTO", inspectType.getBo());
            //获取检验任务类型自定义数据 工序
            String val2 = snService.getSelfDefiningData(UserUtils.getSite(), "INSPECT_TYPE", "OPERATION", inspectType.getBo());
            InspectTaskVo inspectTaskVo2 = new InspectTaskVo();
            inspectTaskVo2.setWorkShop("JBCJ");//捡包车间
            inspectTaskVo2.setInspectType(inspectType.getInspectType());
            inspectTaskVo2.setItem(new ItemHandleBO(exitsSn.getItemBo()).getItem());
            inspectTaskVo2.setOperation(val2);
            if ("TRUE".equals(val)) {
                //自动创建 判断SN物料是否已创建
                QueryWrapper<InspectTask> inspectTaskQueryWrapper = new QueryWrapper<>();
                inspectTaskQueryWrapper.eq(InspectTask.ITEM, inspectTaskVo2.getItem())
                        .eq(InspectTask.SITE, UserUtils.getSite());
                List<InspectTask> inspectTasks = inspectTaskMapper.selectList(inspectTaskQueryWrapper);
                //判断是否已创建 没创建自动创建
                if (inspectTasks.size() == 0) {
                    createInspectTask(inspectTaskVo2);
                }
            }
        }


    }


    //导入创建检验任务
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void importExcel(MultipartFile file) throws CommonException {
        List<InspectTaskVo> inspectTaskVos = ExcelUtils.importExcel(file, 1, 1, InspectTaskVo.class);
        if (inspectTaskVos.size() > 0) {
            for (InspectTaskVo inspectTaskVo : inspectTaskVos) {
                if (inspectTaskVo != null) {
                    if (inspectTaskVo.getInspectType() != null){
                        createInspectTask(inspectTaskVo);
                    }
                }
            }
        }
    }

    @Override
    public void closeInspectTask(List<InspectTaskVo> inspectTaskVos) throws CommonException {
        if (inspectTaskVos.size() > 0) {
            for (InspectTaskVo inspectTaskVo : inspectTaskVos) {
                InspectTask existInspectTask = getExistInspectTask(inspectTaskVo.getInspectTask());
                InspectTask inspectTask = new InspectTask();
                inspectTask.setState("3");
                inspectTaskMapper.update(inspectTask, new QueryWrapper<InspectTask>()
                        .eq(InspectTask.BO, existInspectTask.getBo()));
            }

        } else {
            throw new CommonException("请选择关闭项!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
    }

    @Override
    public void exportInspectTask(String site, HttpServletResponse response, Map<String, Object> params) throws CommonException {
        params.put("site", site);
        List<InspectTask> inspectTasks = inspectTaskMapper.selectinspectTaskPage(params);
        ExcelUtils.exportExcel(inspectTasks, "检验计划查看", "检验计划查看", InspectTask.class, "检验计划查看", response);
    }


    private Map<String, List<Attached>> getAttachedSize(Map<String, List<Attached>> listMap, int size) {

        Map<String, List<Attached>> map = new HashMap<>();
        if (size <= 0) {
            return map;
        }
        for (String key : listMap.keySet()) {
            if (listMap.get(key).size() == size) {
                map.put(key, listMap.get(key));
            }
        }
        while (map.isEmpty() && size >= 0) {
            size--;
            map = getAttachedSize(listMap, size);
        }
        return map;
    }

}
