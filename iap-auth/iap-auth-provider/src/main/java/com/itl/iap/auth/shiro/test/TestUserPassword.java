package com.itl.iap.auth.shiro.test;

import com.itl.iap.auth.entity.SysUser;
import com.itl.iap.auth.util.PassWordUtil;
import com.itl.iap.auth.util.SaltUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 测试
 *
 * @author 汤俊
 * @date 2020-6-27 10:02
 * @since jdk1.8
 */
public class TestUserPassword {

    public static void main(String[] args) {
        SysUser sysUser = new SysUser();
        sysUser.setUserName("admin");
        sysUser.setSalt("admin");
        // 生成盐值
        String salt = null;
        String password = sysUser.getUserPsw();
        // 如果密码为空，则设置默认密码
        if (StringUtils.isBlank(password)) {
            salt = "admin";
            password = "123456";
        } else {
            salt = SaltUtil.generateSalt();
        }
        // 密码加密
        sysUser.setSalt(salt);
        sysUser.setUserPsw(PassWordUtil.encrypt(password, salt));
        System.out.println("setUserPsw = " + sysUser.getUserPsw());
    }

}
