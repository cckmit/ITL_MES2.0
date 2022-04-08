package com.xxl.job.admin.controller;

import com.xxl.job.admin.core.model.IapXxlJobGroup;
import com.xxl.job.admin.core.model.IapXxlJobRegistry;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.mapper.IapXxlJobGroupDao;
import com.xxl.job.admin.mapper.IapXxlJobInfoDao;
import com.xxl.job.admin.mapper.IapXxlJobRegistryDao;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.RegistryConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 该类主要是执行器管理控制层：包括执行器的查询、保存、更新、删除。涉及xxl_job_group表。
 * 注意：
 * 当选择自动注册时，需要根据appname从xxl_job_registry表匹配出最近90秒更新的执行器地址列表。
 * 删除的时候会进行校验，如果挂载了任务则不能删除；如果执行器列表只有这一个也不能删除。
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Controller
@RequestMapping("/jobgroup")
public class IapJobGroupController {

    @Resource
    public IapXxlJobInfoDao iapXxlJobInfoDao;
    @Resource
    public IapXxlJobGroupDao iapXxlJobGroupDao;
    @Resource
    private IapXxlJobRegistryDao iapXxlJobRegistryDao;

    @RequestMapping
    public String index(Model model) {
        return "jobgroup/jobgroup.index";
    }

    /**
     * 执行器管理页面的查询接口
     *
     * @param request
     * @param start   偏移量
     * @param length  每页大小
     * @param appname 执行器AppName
     * @param title   执行器名称
     * @return
     */
    @RequestMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(HttpServletRequest request,
                                        @RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String appname, String title) {
        if(start!=0){
            start = start-1;
        }
        // page query
        List<IapXxlJobGroup> list = iapXxlJobGroupDao.pageList(start, length, appname, title);
        int listCount = iapXxlJobGroupDao.pageListCount(start, length, appname, title);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        // 总记录数
        maps.put("recordsTotal", listCount);
        // 过滤后的总记录数
        maps.put("recordsFiltered", listCount);
        // 分页列表
        maps.put("data", list);
        return maps;
    }

    /**
     * 保存 XxlJobGroup
     *
     * @param iapXxlJobGroup
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public ReturnT<String> save(IapXxlJobGroup iapXxlJobGroup) {

        // valid
        if (iapXxlJobGroup.getAppname() == null || iapXxlJobGroup.getAppname().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (iapXxlJobGroup.getAppname().length() < 4 || iapXxlJobGroup.getAppname().length() > 64) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appname_length"));
        }
        if (iapXxlJobGroup.getTitle() == null || iapXxlJobGroup.getTitle().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (iapXxlJobGroup.getAddressType() != 0) {
            if (iapXxlJobGroup.getAddressList() == null || iapXxlJobGroup.getAddressList().trim().length() == 0) {
                return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresss = iapXxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        int ret = iapXxlJobGroupDao.save(iapXxlJobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    /**
     * 更新 xxlJobGroup
     *
     * @param iapXxlJobGroup
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ReturnT<String> update(IapXxlJobGroup iapXxlJobGroup) {
        // valid
        if (iapXxlJobGroup.getAppname() == null || iapXxlJobGroup.getAppname().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + "AppName"));
        }
        if (iapXxlJobGroup.getAppname().length() < 4 || iapXxlJobGroup.getAppname().length() > 64) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appname_length"));
        }
        if (iapXxlJobGroup.getTitle() == null || iapXxlJobGroup.getTitle().trim().length() == 0) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")));
        }
        if (iapXxlJobGroup.getAddressType() == 0) {
            // 0=自动注册
            List<String> registryList = findRegistryByAppName(iapXxlJobGroup.getAppname());
            String addressListStr = null;
            if (registryList != null && !registryList.isEmpty()) {
                Collections.sort(registryList);
                addressListStr = "";
                for (String item : registryList) {
                    addressListStr += item + ",";
                }
                addressListStr = addressListStr.substring(0, addressListStr.length() - 1);
            }
            iapXxlJobGroup.setAddressList(addressListStr);
        } else {
            // 1=手动录入
            if (iapXxlJobGroup.getAddressList() == null || iapXxlJobGroup.getAddressList().trim().length() == 0) {
                return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresss = iapXxlJobGroup.getAddressList().split(",");
            for (String item : addresss) {
                if (item == null || item.trim().length() == 0) {
                    return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        int ret = iapXxlJobGroupDao.update(iapXxlJobGroup);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    private List<String> findRegistryByAppName(String appnameParam) {
        HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
        List<IapXxlJobRegistry> list = iapXxlJobRegistryDao.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
        if (list != null) {
            for (IapXxlJobRegistry item : list) {
                if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                    String appname = item.getRegistryKey();
                    List<String> registryList = appAddressMap.get(appname);
                    if (registryList == null) {
                        registryList = new ArrayList<String>();
                    }

                    if (!registryList.contains(item.getRegistryValue())) {
                        registryList.add(item.getRegistryValue());
                    }
                    appAddressMap.put(appname, registryList);
                }
            }
        }
        return appAddressMap.get(appnameParam);
    }

    /**
     * 删除 XxlJobGroup
     *
     * @param id XxlJobGroup的ID
     * @return
     */
    @RequestMapping("/remove")
    @ResponseBody
    public ReturnT<String> remove(int id) {

        // valid
        int count = iapXxlJobInfoDao.pageListCount(0, 10, id, -1, null, null, null);
        if (count > 0) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_0"));
        }

        List<IapXxlJobGroup> allList = iapXxlJobGroupDao.findAll();
        if (allList.size() == 1) {
            return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_1"));
        }

        int ret = iapXxlJobGroupDao.remove(id);
        return (ret > 0) ? ReturnT.SUCCESS : ReturnT.FAIL;
    }

    /**
     * 查找 XxlJobGroup
     *
     * @param id XxlJobGroup的ID
     * @return
     */
    @RequestMapping("/loadById")
    @ResponseBody
    public ReturnT<IapXxlJobGroup> loadById(int id) {
        IapXxlJobGroup jobGroup = iapXxlJobGroupDao.load(id);
        return jobGroup != null ? new ReturnT<IapXxlJobGroup>(jobGroup) : new ReturnT<IapXxlJobGroup>(ReturnT.FAIL_CODE, null);
    }

}
