package com.itl.iap.notice.api.enums;

/**
 * 通知渠道类型
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
public enum SendStatusEnum {
    /**
     * 发送中
     */
    SENDING(1,"发送中"),
    /**
     * 发送成功
     */
    SEND_SUCCESS(2,"发送成功"),
    /**
     * 发送失败
     */
    SEND_FAIL(3,"发送失败");

    private int item;

    private String itemName;

    SendStatusEnum(int item, String itemName) {
        this.item = item;
        this.itemName = itemName;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public static String getItemName(int item){
        for (SendStatusEnum es : SendStatusEnum.values()){
            if(item == es.getItem()){
                return es.getItemName();
            }
        }
        return "";
    }
}
