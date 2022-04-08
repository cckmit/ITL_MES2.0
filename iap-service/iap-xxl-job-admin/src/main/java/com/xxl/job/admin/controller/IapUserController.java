package com.xxl.job.admin.controller;

import com.xxl.job.admin.controller.annotation.PermissionLimit;
import com.xxl.job.admin.core.model.IapXxlJobGroup;
import com.xxl.job.admin.core.model.IapXxlJobUser;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.mapper.IapXxlJobGroupDao;
import com.xxl.job.admin.mapper.IapXxlJobUserDao;
import com.xxl.job.admin.service.IapLoginService;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 该类主要负责用户的新增、更新和删除
 *
 * @author 李虎
 * @date 2020-06-28
 * @since jdk1.8
 */
@Controller
@RequestMapping("/user")
public class IapUserController {

    @Resource
    private IapXxlJobUserDao iapXxlJobUserDao;
    @Resource
    private IapXxlJobGroupDao iapXxlJobGroupDao;

    @RequestMapping
    @PermissionLimit(adminuser = true)
    public String index(Model model) {

        // 执行器列表
        List<IapXxlJobGroup> groupList = iapXxlJobGroupDao.findAll();
        model.addAttribute("groupList", groupList);

        return "user/user.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String username, int role) {

        // page list
        List<IapXxlJobUser> list = iapXxlJobUserDao.pageList(start, length, username, role);
        int listCount = iapXxlJobUserDao.pageListCount(start, length, username, role);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", listCount);        // 总记录数
        maps.put("recordsFiltered", listCount);    // 过滤后的总记录数
        maps.put("data", list);                    // 分页列表
        return maps;
    }

    @RequestMapping("/add")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<String> add(IapXxlJobUser iapXxlJobUser) {

        // valid username
        if (!StringUtils.hasText(iapXxlJobUser.getUsername())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_please_input") + I18nUtil.getString("user_username"));
        }
        iapXxlJobUser.setUsername(iapXxlJobUser.getUsername().trim());
        if (!(iapXxlJobUser.getUsername().length() >= 4 && iapXxlJobUser.getUsername().length() <= 20)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
        }
        // valid password
        if (!StringUtils.hasText(iapXxlJobUser.getPassword())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_please_input") + I18nUtil.getString("user_password"));
        }
        iapXxlJobUser.setPassword(iapXxlJobUser.getPassword().trim());
        if (!(iapXxlJobUser.getPassword().length() >= 4 && iapXxlJobUser.getPassword().length() <= 20)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
        }
        // md5 password
        iapXxlJobUser.setPassword(DigestUtils.md5DigestAsHex(iapXxlJobUser.getPassword().getBytes()));

        // check repeat
        IapXxlJobUser existUser = iapXxlJobUserDao.loadByUserName(iapXxlJobUser.getUsername());
        if (existUser != null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("user_username_repeat"));
        }

        // write
        iapXxlJobUserDao.save(iapXxlJobUser);
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<String> update(HttpServletRequest request, IapXxlJobUser iapXxlJobUser) {

        // avoid opt login seft
        IapXxlJobUser loginUser = (IapXxlJobUser) request.getAttribute(IapLoginService.LOGIN_IDENTITY_KEY);
        if (loginUser.getUsername().equals(iapXxlJobUser.getUsername())) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), I18nUtil.getString("user_update_loginuser_limit"));
        }

        // valid password
        if (StringUtils.hasText(iapXxlJobUser.getPassword())) {
            iapXxlJobUser.setPassword(iapXxlJobUser.getPassword().trim());
            if (!(iapXxlJobUser.getPassword().length() >= 4 && iapXxlJobUser.getPassword().length() <= 20)) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
            }
            // md5 password
            iapXxlJobUser.setPassword(DigestUtils.md5DigestAsHex(iapXxlJobUser.getPassword().getBytes()));
        } else {
            iapXxlJobUser.setPassword(null);
        }

        // write
        iapXxlJobUserDao.update(iapXxlJobUser);
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/remove")
    @ResponseBody
    @PermissionLimit(adminuser = true)
    public ReturnT<String> remove(HttpServletRequest request, int id) {

        // avoid opt login seft
        IapXxlJobUser loginUser = (IapXxlJobUser) request.getAttribute(IapLoginService.LOGIN_IDENTITY_KEY);
        if (loginUser.getId() == id) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), I18nUtil.getString("user_update_loginuser_limit"));
        }

        iapXxlJobUserDao.delete(id);
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public ReturnT<String> updatePwd(HttpServletRequest request, String password) {

        // valid password
        if (password == null || password.trim().length() == 0) {
            return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码不可为空");
        }
        password = password.trim();
        if (!(password.length() >= 4 && password.length() <= 20)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
        }

        // md5 password
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        // update pwd
        IapXxlJobUser loginUser = (IapXxlJobUser) request.getAttribute(IapLoginService.LOGIN_IDENTITY_KEY);

        // do write
        IapXxlJobUser existUser = iapXxlJobUserDao.loadByUserName(loginUser.getUsername());
        existUser.setPassword(md5Password);
        iapXxlJobUserDao.update(existUser);

        return ReturnT.SUCCESS;
    }

}
