package com.xxl.job.admin.controller;

import com.xxl.job.admin.controller.annotation.PermissionLimit;
import com.xxl.job.admin.service.IapLoginService;
import com.xxl.job.admin.service.IapXxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 包括登录、图表展示等方法
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Controller
public class IapIndexController {

    @Resource
    private IapXxlJobService iapXxlJobService;
    @Resource
    private IapLoginService iapLoginService;


    @RequestMapping("/")
    public String index(Model model) {

        Map<String, Object> dashboardMap = iapXxlJobService.dashboardInfo();
        model.addAllAttributes(dashboardMap);

        return "index";
    }

    /**
     * 运行报表
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @return
     */
    @RequestMapping("/chartInfo")
    @ResponseBody
    public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {
        ReturnT<Map<String, Object>> chartInfo = iapXxlJobService.chartInfo(startDate, endDate);
        return chartInfo;
    }

    /**
     * 重定向到登录操作
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return
     */
    @RequestMapping("/toLogin")
    @PermissionLimit(limit = false)
    public String toLogin(HttpServletRequest request, HttpServletResponse response) {
        if (iapLoginService.ifLogin(request, response) != null) {
            return "redirect:/";
        }
        return "login";
    }

    /**
     * 登录
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param userName   用户名
     * @param password   密码
     * @param ifRemember 记住我操作
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember) {
        boolean ifRem = (ifRemember != null && ifRemember.trim().length() > 0 && "on".equals(ifRemember)) ? true : false;
        return iapLoginService.login(request, response, userName, password, ifRem);
    }

    /**
     * 登出
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return iapLoginService.logout(request, response);
    }

    @RequestMapping("/help")
    public String help() {

		/*if (!PermissionInterceptor.ifLogin(request)) {
			return "redirect:/toLogin";
		}*/

        return "help";
    }

    /**
     * 数据绑定方法：
     * Spring在绑定请求参数到HandlerMethod的时候会借助WebDataBinder进行数据转换，"yyyy-MM-dd HH:mm:ss"这种类型的字符串直接使用Date类型接收。
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
