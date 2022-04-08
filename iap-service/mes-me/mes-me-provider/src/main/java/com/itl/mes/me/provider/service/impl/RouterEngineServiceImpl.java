package com.itl.mes.me.provider.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.api.entity.RouterProcess;
import com.itl.mes.core.client.service.RouterService;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.service.MeSfcService;
import com.itl.mes.me.api.service.RouterEngineService;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 工艺路线引擎
 * </p>
 *
 * @author linjl
 * @since 2020-02-04
 */
@Service
public class RouterEngineServiceImpl implements RouterEngineService {

    @Autowired
    protected RouterService routerService;

    @Autowired
    protected MeSfcService meSfcService;

    /**
     * 解析工艺路线信息
     */
    protected JsonObject analysisRouteProcess(RouterProcess routerProcess) {
        if (null == routerProcess || null == routerProcess.getProcessInfo())
            return null;

        JsonObject jsonObject = JsonParser.parseString(routerProcess.getProcessInfo()).getAsJsonObject();
        return jsonObject;
    }

    @Override
    public HashMap<String,String> executeRouter(String sfcBo) throws Exception {
        LambdaQueryWrapper<MeSfc> query = new QueryWrapper<MeSfc>().lambda()
                .eq(MeSfc::getBo, sfcBo);
        MeSfc meSfc = meSfcService.getOne(query);
        return executeRouter(meSfc);
    }

    @Override
    public HashMap<String,String> executeRouter(MeSfc sfc) throws Exception {
        HashMap<String,String> stepOperation = new HashMap<>();

        String routerBo = sfc.getSfcRouterBo();
        ResponseData<Router> responseData = routerService.getRouter(routerBo);
        Router router = (Router) responseData.getData();
        if (null == router || null == router.getRouterProcess())
            return null;

        RouterProcess routerProcess = router.getRouterProcess();
        String jsonData = routerProcess.getProcessInfo();
        List<String> nextSteps = findNextStep2(sfc, jsonData);
        stepOperation = findOperationByStep(jsonData, nextSteps);

        //记录当前步骤和工序
//        sfc.setSfcStepId(StringUtils.join(stepOperation.keySet(), ";"));
//        sfc.setOperationBo(StringUtils.join(stepOperation.values(), ";"));
//        meSfcService.updateLast(sfc);


        return stepOperation;
    }

    /**
     * 找到下一个步骤
     * */
    protected List<String> findNextStep(MeSfc sfc, String jsonData) throws IOException {
        List<String> nextSteps = new ArrayList<>();
        String curOperation = sfc.getOperationBo();
        String from = null, to = null, name = null;

        // 得到JsonReader对象，并将要解析的JSON对象数组传进去
        JsonReader jr = new JsonReader(new StringReader(jsonData));
        jr.setLenient(true);

        try {
            while (jr.hasNext()) {

                name = jr.nextName();
                if (name.equals("lineList")) {
                    // 解析连线信息
                    jr.beginArray();// 开始解析数组
                    while (jr.hasNext()) {
                        jr.beginObject();
                        while (jr.hasNext()) {
                            if (jr.nextName().equals("from")) {
                                from = jr.nextString();
                            } else {
                                to = jr.nextString();
                            }
                        }
                        //curOperation包含多个工序，用;分割
                        if (from.equals(curOperation) || curOperation.startsWith(from + ";")
                                || curOperation.endsWith(";" + from) || curOperation.contains(";" + from + ";")) {
                            nextSteps.add(to);
                        }
                        jr.endObject();
                    }
                    jr.endArray();
                }
            }
            return nextSteps;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            jr.close();
        }
    }

    /**
     * 找到下一个步骤
     * */
    protected List<String> findNextStep2(MeSfc sfc, String jsonData) {
        List<String> nextSteps = new ArrayList<>();

        String jsonPath = "$.lineList[?(@.from =~ /%s/)].to";
        String[] steps = sfc.getSfcStepId().split(";");
        for (String step : steps){
            List<String> tos = JsonPath.read(jsonData, String.format(jsonPath, step));
            nextSteps.addAll(tos);
        }

        return nextSteps;
    }

    protected HashMap<String,String> findOperationByStep(String jsonData, List<String> steps) throws Exception {
        HashMap<String,String> stepOperation = new HashMap<>();

        String jsonPath = "$.nodeList[?(@.id =~ /%s/)].operation";
        List<String> operations=null;
        for (String step: steps) {
            operations = JsonPath.read(jsonData, String.format(jsonPath, step));
            if (operations.isEmpty()) {
                throw new Exception(String.format("工艺路线图有误，无法找到步骤{0}对应的工序", step));
            }

            stepOperation.put(step, operations.get(0));
        }


        return stepOperation;
    }


//    @Override
//    public List<Operation> getExecuteInfo(MeSfc sfc) {
//        return null;
//    }

    @Override
    public boolean pauseRouter(MeSfc sfc) {
        return false;
    }

    @Override
    public boolean hangupRouter(MeSfc sfc) {
        return false;
    }
}
