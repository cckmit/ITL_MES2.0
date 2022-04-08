package com.xxl.job.admin.controller;

import com.xxl.job.admin.core.model.IapXxlJobInfo;
import com.xxl.job.admin.core.model.IapXxlJobLogGlue;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.mapper.IapXxlJobInfoDao;
import com.xxl.job.admin.mapper.IapXxlJobLogGlueDao;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.glue.GlueTypeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 该类主要是GLUE(xxx)模式的可在线编辑的任务的查询、修改和保存；
 * 涉及：xxl_job_info任务表和xxl_job_logglue任务日志表（glue类型）；
 * 注意：
 * 每次保存会清除30次之前的备份记录。
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Controller
@RequestMapping("/jobcode")
public class IapJobCodeController {

    @Resource
    private IapXxlJobInfoDao iapXxlJobInfoDao;
    @Resource
    private IapXxlJobLogGlueDao iapXxlJobLogGlueDao;

    @RequestMapping
    public String index(HttpServletRequest request, Model model, int jobId) {
        IapXxlJobInfo jobInfo = iapXxlJobInfoDao.loadById(jobId);
        List<IapXxlJobLogGlue> jobLogGlues = iapXxlJobLogGlueDao.findByJobId(jobId);

        if (jobInfo == null) {
            throw new RuntimeException(I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }
        if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
            throw new RuntimeException(I18nUtil.getString("jobinfo_glue_gluetype_unvalid"));
        }

        // valid permission
        IapJobInfoController.validPermission(request, jobInfo.getJobGroup());

        // Glue类型-字典
        model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());

        model.addAttribute("jobInfo", jobInfo);
        model.addAttribute("jobLogGlues", jobLogGlues);
        return "jobcode/jobcode.index";
    }

    @RequestMapping("/save")
    @ResponseBody
    public ReturnT<String> save(Model model, int id, String glueSource, String glueRemark) {
        // valid
        if (glueRemark == null) {
            return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")));
        }
        if (glueRemark.length() < 4 || glueRemark.length() > 100) {
            return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_remark_limit"));
        }
        IapXxlJobInfo existsJobInfo = iapXxlJobInfoDao.loadById(id);
        if (existsJobInfo == null) {
            return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }

        // update new code
        existsJobInfo.setGlueSource(glueSource);
        existsJobInfo.setGlueRemark(glueRemark);
        existsJobInfo.setGlueUpdatetime(new Date());

        existsJobInfo.setUpdateTime(new Date());
        iapXxlJobInfoDao.update(existsJobInfo);

        // log old code
        IapXxlJobLogGlue iapXxlJobLogGlue = new IapXxlJobLogGlue();
        iapXxlJobLogGlue.setJobId(existsJobInfo.getId());
        iapXxlJobLogGlue.setGlueType(existsJobInfo.getGlueType());
        iapXxlJobLogGlue.setGlueSource(glueSource);
        iapXxlJobLogGlue.setGlueRemark(glueRemark);

        iapXxlJobLogGlue.setAddTime(new Date());
        iapXxlJobLogGlue.setUpdateTime(new Date());
        iapXxlJobLogGlueDao.save(iapXxlJobLogGlue);

        // remove code backup more than 30
        iapXxlJobLogGlueDao.removeOld(existsJobInfo.getId(), 30);

        return ReturnT.SUCCESS;
    }

}
