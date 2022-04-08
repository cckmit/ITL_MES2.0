package com.itl.iap.common.base.aop;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.itl.iap.common.base.dto.IapOpsLogTDto;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 接口、日志切面类
 *
 * @author mjl
 * @date 2020-06-20 22:45
 * @since jdk1.8
 */
@Component
//@Aspect
@Slf4j
public class ApiAspect {

    public ApiAspect() {
        log.info("----> ApiAspect开始执行");
    }

    @Resource
    WebApplicationContext applicationContext;

    @Resource
    JdbcConfig jdbcConfig;

    @Value("${spring.application.name}")
    private String modelName;

    /**
     * 获取 nacos 的命名空间名称
     */
    @Value("${spring.cloud.nacos.config.namespace}")
    private String nameSpace;

    /**
     * 获取本服务的端口号
     */
    @Value("${server.port}")
    private String serverPort;

    /**
     * 统一定义切点  @Pointcut("execution(* com..*.*Controller.*(..))")
     * 第一个 * 表示要拦截的目标方法返回值任意（也可以明确指定返回值类型(public)
     * 第二个 * 表示包中的任意类（也可以明确指定类
     * 第三个 * 表示类中的任意方法
     * 最后面的两个点表示方法参数任意，个数任意，类型任意
     */
//    @Pointcut("execution(* com.itl.iap..*.controller..*.*(..))")
//    @Pointcut("execution(* com.itl..*.controller..*.*(..))")
    @Pointcut("execution(* com..*.*Controller.*(..))")
    public void pointcut() {

    }

    /**
     * 在有导入common-core包服务启动的时候，都会先去扫描该服务的所有Controller的接口，并且把所有接口保存到数据库
     * 保存到数据库操作中，首先进行的是删除该服务的所有接口，然后保存所有新扫描到的接口（包含被禁用的接口）
     * 保存到数据库接口的规则为：
     * 1. 保存之前被禁言的接口
     * 2. 新增的接口
     *
     * @throws Exception
     */
    @PostConstruct
    private void initApi() throws Exception {

        /**
         * 初始化前先判断数据库中是否有api信息
         * 如果数据库中存在该微服务的api,则不进行初始化
         */
        // 1. 根据模块名查询该模块全部接口
        List<IapSysApiT> iapSysApiList = jdbcConfig.selectList(modelName);
        //  1.1 获取当前模块全部接口
        List<IapSysApiT> iapSysApiListNew = this.getThisModelNameApiList();
        if (iapSysApiList != null && iapSysApiList.size() != 0) {
            // 2. 过滤获得禁用列表
            List<IapSysApiT> disableList = iapSysApiList.stream().filter(x -> x.getEnabled() == 1).collect(Collectors.toList());
            // 3. 删除该模块全部接口
            jdbcConfig.deleteApiModelName(modelName);
            // 4. 当前最新的接口列表
            if (!disableList.isEmpty()) {
                Integer disableSize = disableList.size();
                log.info(modelName + "--> 被禁用接口的个数: " + disableSize);
                // iapSysApiTListNew 移除被禁用的接口
                iapSysApiListNew.removeAll(disableList);
                // iapSysApiTListNew 添加被禁用的接口（保留了之前的禁用信息）
                iapSysApiListNew.addAll(disableList);
            }
            // 该服务之前存在被禁用接口，保留禁用接口，插入数据库
            installApi(iapSysApiListNew);
        } else {
            // 该服务没有存在被禁用的接口，先把数据库中该服务的所有接口删除，再保存该服务最新的接口信息列表
            jdbcConfig.deleteApiModelName(modelName);
            installApi(iapSysApiListNew);
        }
        log.info("----> ApiAspect执行结束");
    }

    /**
     * 保存传入的接口列表到数据库
     *
     * @param iapSysApiList 需要保存的数据库的接口列表
     * @throws Exception
     */
    private void installApi(List<IapSysApiT> iapSysApiList) throws Exception {

        long startTime = System.currentTimeMillis();
        log.info(modelName + "-->共有接口: " + iapSysApiList.size() + " 个");
        Integer rowResult = jdbcConfig.insertList(iapSysApiList);
        long endTime = System.currentTimeMillis();
        log.info("插入执行时间：" + (endTime - startTime) + "ms");
    }

    /**
     * 1. 判断接口的状态（启用 / 禁用），如果被禁用，则在环绕通知中不放行该操作
     * 2. 记录接口操作
     * 在 @Aspect 注解开启的情况下，每次操作都会来调用本方法
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "pointcut()")
    public Object getApiState(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ResponseData<String> responseData = new ResponseData<String>();
        IapOpsLogTDto iapOpsLogDto = new IapOpsLogTDto();

        Signature signature = joinPoint.getSignature();
        try {
            log.info("模块名称: " + modelName + ",类名称: " + signature.getDeclaringTypeName() + ", 方法名称: " + signature.getName() + ", 请求方法: " + request.getMethod());
            IapSysApiT iapSysApiT = new IapSysApiT();
            iapSysApiT.setModelName(modelName).setClassName(signature.getDeclaringTypeName()).setMethodName(signature.getName()).setRequestType(request.getMethod());
            IapSysApiT iapSysApiT1 = jdbcConfig.selectOne(iapSysApiT);
            // 日志
            Object[] args = joinPoint.getArgs();
            String strArgs = Arrays.toString(args);
            // 保存请求参数
            iapOpsLogDto.setRequestParams(strArgs);
            //方法说明
            iapOpsLogDto.setMethodDesc(iapSysApiT1.getMethodDesc());
            if (iapSysApiT1 != null && iapSysApiT1.getEnabled() != null && iapSysApiT1.getEnabled() == 0) {
                Object proceed = joinPoint.proceed();
                iapOpsLogDto.setLogData(new Gson().toJson(proceed)).setMethodType(IapOpsLogTDto.METHOD_TYPE0);
                installIapLog(iapOpsLogDto, request);
                log.info("==============接口正常================>" + request.getRequestURL());
                return proceed;
            } else if (iapSysApiT1.getId() == null) {
                // 调式，接口不存在时，就放行接口，不再拦截
                Object proceed = joinPoint.proceed();
                responseData.setCode("200");
                responseData.setMsg("接口不存在: -> " + request.getRequestURL());
                iapOpsLogDto.setLogData(JSON.toJSONString(responseData));
                iapOpsLogDto.setMethodType(IapOpsLogTDto.METHOD_TYPE0);
                installIapLog(iapOpsLogDto, request);
                log.info("接口不存在: -> " + request.getRequestURL());
                return proceed;
            } else {
                responseData.setCode("200");
                responseData.setMsg("接口被禁用: -> " + request.getRequestURL());
                iapOpsLogDto.setLogData(JSON.toJSONString(responseData)).setMethodType(IapOpsLogTDto.METHOD_TYPE0);
                installIapLog(iapOpsLogDto, request);
                log.info("接口被禁用: -> " + request.getRequestURL());
                return responseData;
            }
        } catch (Exception e) {
            iapOpsLogDto.setLogData("出现异常：" + CommonExceptionDefinition.getCurrentClassError()).setMethodType(IapOpsLogTDto.METHOD_TYPE1);
            installIapLog(iapOpsLogDto, request);
            throw e;
        }
    }

    /**
     * 记录接口日志并保存到数据库
     *
     * @param iapOpsLogDto
     * @param request
     */
    private void installIapLog(IapOpsLogTDto iapOpsLogDto, HttpServletRequest request) {
        Date date = new Date();
        // 记录日志
        iapOpsLogDto.setId(uuid32()).setCreateDate(date).setLastUpdateDate(date);
        // 访问IP地址
        iapOpsLogDto.setServiceIp(IpUtil.getIpAddr(request));
        // 请求路径url
        iapOpsLogDto.setRequestMethod(request.getRequestURI());
        // 请求方法
        iapOpsLogDto.setRequestFunction(request.getMethod());
        // 服务名称
        iapOpsLogDto.setServiceName(modelName);
        // 用户使用的浏览器
        iapOpsLogDto.setRequestProxy(IpUtil.getBrowser(request.getHeader("user-agent")));
        // nacos命名空间
        iapOpsLogDto.setNamespace(nameSpace);
        // 服务id
        iapOpsLogDto.setServiceId(serverPort);
        try {
            jdbcConfig.insertSystemLog(iapOpsLogDto);
        } catch (Exception e) {
            log.error("===========================>日志存储错误:" + e.getMessage());
        }
    }

    /**
     * 扫描该服务下，当前的所有接口
     *
     * @return List<IapSysApiT> 当前的接口列表
     */
    public List<IapSysApiT> getThisModelNameApiList() {

        List<IapSysApiT> iapSysApiList = new ArrayList<IapSysApiT>();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodEntry : map.entrySet()) {
            HandlerMethod handlerMethod = mappingInfoHandlerMethodEntry.getValue();
            // 只添加 com.itl包下的接口
            String className = handlerMethod.getMethod().getDeclaringClass().getName();
            // 在这里输入需要扫描的包名前缀 如 className.startsWith("com.itl") || className.startsWith("com.xx.xx")
            if (className.startsWith("com")) {
                IapSysApiT iapSysApiT = new IapSysApiT();
                iapSysApiT.setId(uuid32()).setLastUpdateDate(new Date()).setEnabled((short) 0);
                RequestMappingInfo requestMappingInfo = mappingInfoHandlerMethodEntry.getKey();
                // 模块名称
                iapSysApiT.setModelName(modelName);
                // 类名
                iapSysApiT.setClassName(className);

                Annotation[] parentAnnotations = handlerMethod.getBeanType().getAnnotations();
                for (Annotation annotation : parentAnnotations) {
                    if (annotation instanceof Api) {
                        Api api = (Api) annotation;
                        // 类描述信息
                        iapSysApiT.setClassDesc(api.value());

                    } else if (annotation instanceof RequestMapping) {
                        RequestMapping requestMapping = (RequestMapping) annotation;
                        if (null != requestMapping.value() && requestMapping.value().length > 0) {
                            // 类 url
                            iapSysApiT.setClassUrl(requestMapping.value()[0]);
                        }
                    }
                }
                // 方法名
                iapSysApiT.setMethodName(handlerMethod.getMethod().getName());

                String systemCode = modelName + "." + className + "." + handlerMethod.getMethod().getName();
                iapSysApiT.setSystemCode(systemCode);

                Annotation[] annotations = handlerMethod.getMethod().getDeclaredAnnotations();
                if (annotations != null) {
                    // 处理具体的方法信息
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof ApiOperation) {
                            ApiOperation methodDesc = (ApiOperation) annotation;
                            // 接口描述
                            iapSysApiT.setMethodDesc(methodDesc.value());
                        }
                    }
                }
                PatternsRequestCondition p = requestMappingInfo.getPatternsCondition();
                for (String url : p.getPatterns()) {
                    // 请求 url
                    iapSysApiT.setMethodUrl(url);
                }
                RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
                for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                    // 请求方式：POST/PUT/GET/DELETE
                    iapSysApiT.setRequestType(requestMethod.toString());
                }
                iapSysApiList.add(iapSysApiT);
            }
        }
        return iapSysApiList;
    }

    /**
     * 生成UUID
     *
     * @return String
     */
    public static String uuid32() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }


}
