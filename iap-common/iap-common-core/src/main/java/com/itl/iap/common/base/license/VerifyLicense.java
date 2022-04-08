package com.itl.iap.common.base.license;

import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 *
 *
 * @author 汤俊
 * @date 2020-1-9 14:38
 * @since 1.0.0
 */
@Slf4j
public class VerifyLicense {

    private String pubAlias;
    private String keyStorePwd;
    private String subject;
    private String licDir;
    private String pubPath;

    /**
     * 单网卡名称
     */
    private static final String NETWORK_CARD = "eth0";

    public VerifyLicense() {
        // 取默认配置
        setConf("/licenseVerify.properties");
    }

    public VerifyLicense(String confPath) {
        setConf(confPath);
    }

    /**
     * 通过外部配置文件获取配置信息
     *
     * @param confPath 配置文件路径
     */
    private void setConf(String confPath) {
        // 获取参数
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream(confPath);
        try {
            prop.load(in);
        } catch (IOException e) {
            log.error("VerifyLicense Properties load inputStream error.", e);
        }
        this.subject = prop.getProperty("subject");
        this.pubAlias = prop.getProperty("public.alias");
        this.keyStorePwd = prop.getProperty("key.store.pwd");
        this.licDir = prop.getProperty("license.dir");
        this.pubPath = prop.getProperty("public.store.path");
    }

    /**
     * 安装证书证书
     */
    public void install() {
        try {
            LicenseManager licenseManager = getLicenseManager();
            licenseManager.install(new File(licDir));
            log.info("安装证书成功!");
        } catch (Exception e) {
            log.error("安装证书失败!", e);
            Runtime.getRuntime().halt(1);
        }
    }

    private LicenseManager getLicenseManager() {
        return LicenseManagerHolder.getLicenseManager(initLicenseParams());
    }

    /**
     * 初始化证书的相关参数
     */
    private LicenseParam initLicenseParams() {
        Class<VerifyLicense> clazz = VerifyLicense.class;
        Preferences pre = Preferences.userNodeForPackage(clazz);
        CipherParam cipherParam = new DefaultCipherParam(keyStorePwd);
        KeyStoreParam pubStoreParam = new DefaultKeyStoreParam(clazz, pubPath, pubAlias, keyStorePwd, null);
        return new DefaultLicenseParam(subject, pre, pubStoreParam, cipherParam);
    }

    /**
     * 验证证书的合法性
     */
    public boolean vertify() {
        try {
            LicenseManager licenseManager = getLicenseManager();
            LicenseContent verify = licenseManager.verify();
            log.info("验证证书成功!");
            Map<String, String> extra = (Map) verify.getExtra();
            String ip = extra.get("ip");
            String localIp = getLocalIp();
            if (!Objects.equals(ip, localIp)) {
                log.error("IP 地址验证不通过");
                return false;
            }
            String mac = extra.get("mac");
            String localMac = getLocalMac(ip);
            if (!Objects.equals(mac, localMac)) {
                log.error("MAC 地址验证不通过");
                return false;
            }
            log.info("IP、MAC地址验证通过");
            return true;
        } catch (LicenseContentException ex) {
            log.error("证书已经过期!", ex);
            return false;
        } catch (Exception e) {
            log.error("验证证书失败!", e);
            return false;
        }
    }

    /**
     * Description: linux下获得本机IPv4 IP
     * @return
     */
    public static String getLocalIp() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> e1 = (Enumeration<NetworkInterface>)NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = e1.nextElement();
                //单网卡或者绑定双网卡
                if ((NETWORK_CARD.equals(ni.getName()))) {
                    Enumeration<InetAddress> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = e2.nextElement();
                        if (ia instanceof Inet6Address) {
                            continue;
                        }
                        ip = ia.getHostAddress();
                    }
                    break;
                }
            }
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 得到本机 mac 地址
     * @param ip IP地址
     * @throws SocketException, UnknownHostException
     */
    private String getLocalMac(String ip) throws SocketException, UnknownHostException {
        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getByName(ip)).getHardwareAddress();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            if (str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

}

