package com.itl.iap.notice.provider.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.service.SmsService;
import com.alibaba.fastjson.JSONObject;
import com.itl.iap.notice.provider.mapper.MsgPublicTemplateMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.w3c.dom.Document;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
@Transactional
public class SmsServiceImpl implements SmsService {
    static String  hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f" };

    @Value("${message.apId}")
    private String apId;

    @Value("${message.secretKey}")
    private String secretKey;

    @Value("${message.ecName}")
    private String ecName;

    @Value("${message.sign}")
    private String sign;

    @Value("${message.url}")
    private String url;

    @Resource
    private MsgPublicTemplateMapper msgPublicTemplateMapper;

    @Override
    public String  sendMessage(Map<String, String> msg) {
        if (msg.get("templateCode") == null) {
            throw new RuntimeException("没有查找到模板");
        }
        QueryWrapper<MsgPublicTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", msg.get("templateCode").toString());
        MsgPublicTemplate msgPublicTemplate = msgPublicTemplateMapper.selectOne(queryWrapper);
        if (msgPublicTemplate.getMessageTypeCode() == null) {
            throw new RuntimeException("没有查找到模板");
        }
        if(msgPublicTemplate.getNoticeEnabledFlag() == 0){
            throw new RuntimeException("该模板未启用");
        }
        if(msgPublicTemplate.getContent() == null){
            throw new RuntimeException("没有设置模板内容");
        }
        //logger.info("--------调用webservice接口begin-------");
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        //对方的wsdl地址
        Client client = dcf.createClient(url);
        String json = null;
        try {
            String phone=msg.get("phone");
            String con=msgPublicTemplate.getContent();
            //填充模板内容
            String content=analysisContent(con,msg);
            String str1=ecName+apId+secretKey+phone+content+sign;
            String ms=MD5Encode(str1,"UTF-8");
            String str="<?xml version=\"1.0\" encoding=\"utf-8\"?><WsSubmitReq><apId>dy01</apId><secretKey>123456</secretKey><ecName>浙江东音科技有限公司</ecName><mobiles><string>"+phone+"</string></mobiles><content>"+content+"</content><sign>yy9TzEuJp</sign><addSerial></addSerial><mac>"+ms+"</mac></WsSubmitReq>";
            Object[] objects1= client.invoke("sendSms",str); //参数1，参数2，参数3......按顺序放就看可以
            json = JSONObject.toJSONString(objects1[0]);
            System.out.println("返回数据:" + json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            // logger.info("服务器断开连接，请稍后再试");
        }
        // logger.info("--------调用webservice接口end-------");
        return json;


    }

    public synchronized static final byte[] toMd5(String data,String encodingType) {
        MessageDigest digest = null;
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException nsae) {
                System.err.println("Failed to load the MD5 MessageDigest. ");
                nsae.printStackTrace();
            }
        }
        if (StringUtils.isBlank(data)) {
            return null;
        }
        try {
            //最重要的是这句,需要加上编码类型
            digest.update(data.getBytes(encodingType));
        } catch (UnsupportedEncodingException e) {
            digest.update(data.getBytes());
        }
        return digest.digest();
    }
    public static String MD5Encode(String origin,String encodingType) {
        byte[] md5Bytes = toMd5(origin,encodingType);
        return byteArrayToHexString( md5Bytes);
    }
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    private static String bytes2Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder resultSB = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() < 2) {
                resultSB.append("0");
            }
            resultSB.append(hex);
        }
        return resultSB.toString();
    }

    //解析模板内容
    public String analysisContent(String content,Map<String, String> msg){
        String startLog="${";
        String endLog="}";
        //定位startLog
        int offset=0;
        //首个startLog位置
        int start=content.indexOf(startLog,offset);
        char[] str=content.toCharArray();
        StringBuilder result=new StringBuilder();
        while(start > -1){
           StringBuilder param=new StringBuilder();
           result.append(str,offset,start-offset);
           offset=start+startLog.length();

           int end=content.indexOf(endLog,offset);
           param.append(str,offset,end-offset);
           result.append(msg.get(param.toString()));

           offset=end+endLog.length();
           start=content.indexOf(startLog,offset);

        }
        if(offset<str.length){
            result.append(str,offset,str.length-offset);
        }
        return result.toString();

    }


}
