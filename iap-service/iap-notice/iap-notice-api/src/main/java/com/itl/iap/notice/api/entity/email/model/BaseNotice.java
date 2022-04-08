package com.itl.iap.notice.api.entity.email.model;

import lombok.Data;

import java.io.*;
import java.util.Map;

/**
 * 通知内容基类
 *
 * @author liaochengdian
 * @date  2020/3/19
 * @since jdk1.8
 */
@Data
public  class BaseNotice implements Serializable{
    private int type;
    /**
     * 失败重发唯一标识
     */
    private String uid;
    /**
     * 接收人Id
     */
    private String userId;
    /**
     * 接收人姓名
     */
    private String userName;
    /**
     * 模板code
     */
    private String code;
    /**
     * 指定生产者发送的主题
     */
    private String topic;
    /**
     * 模板对应参数
     */
    private Map<String,Object> params;
    /**
     * 业务ID
     */
    private String businessId;
//    /**
//     * 消息模板编码
//     */
//    private String messageCode;


    public byte[] toBytes(){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bo);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bo.toByteArray();
    }

    public BaseNotice toBaseNotice(byte[] bytes){
        BaseNotice baseNotice = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            baseNotice = (BaseNotice) ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return baseNotice;
    }

}
