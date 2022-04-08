package com.itl.iap.common.base.aop;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP 工具类
 *
 * @author : tanq
 * @date : 2020-06-28 14:27
 * @since jdk1.8
 **/
public class IpUtil {
    private static String pattern = "^Mozilla/\\d\\.\\d\\s+\\(+.+?\\)";
    private static String pattern2 = "\\(+.+?\\)";
    private static Pattern r = Pattern.compile(pattern);
    private static Pattern r2 = Pattern.compile(pattern2);

    /**
     * 获取登录用户的IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取浏览器信息
     *
     * @param userAgent
     * @return
     */
    public static String getBrowser(String userAgent) {
        String browser = "";
        String version = "";
        Integer startLen = 0;
        Integer endLen = 0;
        if (userAgent.toLowerCase().indexOf("msie") != -1) {
            browser = "IE";
            startLen = userAgent.toLowerCase().indexOf("msie");
            endLen = userAgent.indexOf(";", startLen);
            version = userAgent.substring(startLen + 5, endLen);
        } else if (userAgent.toLowerCase().indexOf("trident/7") != -1) {
            browser = "IE";
            startLen = userAgent.toLowerCase().indexOf("rv:") + 3;
            endLen = userAgent.indexOf(")", startLen);
            version = userAgent.substring(startLen, endLen);
        } else if (userAgent.toLowerCase().indexOf("chrome") != -1) {
            browser = "CHROME";
            startLen = userAgent.toLowerCase().indexOf("chrome") + 7;
            endLen = userAgent.indexOf(" ", startLen);
            version = userAgent.substring(startLen, endLen);
        } else if (userAgent.toLowerCase().indexOf("firefox") != -1) {
            browser = "FIREFOX";
            startLen = userAgent.toLowerCase().indexOf("firefox") + 8;
            endLen = userAgent.length();
            version = userAgent.substring(startLen, endLen);
        } else if (userAgent.toLowerCase().indexOf("safari") != -1) {
            browser = "SAFARI";
            startLen = userAgent.toLowerCase().indexOf("version") + 8;
            endLen = userAgent.indexOf(" ", startLen);
            version = userAgent.substring(startLen, endLen);
        } else if (userAgent.toLowerCase().indexOf("opera") != -1) {
            browser = "OPERA";
            startLen = userAgent.toLowerCase().indexOf("opera") + 6;
            endLen = userAgent.length();
            version = userAgent.substring(startLen, endLen);
        } else {
            browser = "OTHER";
        }
        return browser + "_" + version;
    }

    /**
     * 获得操作系统信息
     *
     * @return String
     */
    public static String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return getDeviceInfo(userAgent);
    }

    /**
     * 获得操作系统信息
     *
     * @return String
     */
    private static String getDeviceInfo(String userAgent) {
        Matcher m = r.matcher(userAgent);
        String result = null;
        if (m.find()) {
            result = m.group(0);
        }

        Matcher m2 = r2.matcher(result);
        if (m2.find()) {
            result = m2.group(0);
        }
        result = result.replace("(", "");
        result = result.replace(")", "");
        return filterDeviceInfo(result);
    }

    /**
     * 获得操作系统信息
     *
     * @return String
     */
    public static String filterDeviceInfo(String result) {
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        result = result.replace(" U;", "");
        result = result.replace(" zh-cn;", "");
        return result;
    }
}
