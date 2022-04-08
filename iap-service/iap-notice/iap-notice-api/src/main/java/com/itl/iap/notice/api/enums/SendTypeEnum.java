package com.itl.iap.notice.api.enums;

/**
 * 通知渠道类型
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
public enum SendTypeEnum {
    /**
     * 通知渠道类型
     */
    STATION(1,"站内信"),
    EMAIL(2,"邮件"),
    SMS(3,"短信");

    private int item;

    private String itemName;

    SendTypeEnum(int item, String itemName) {
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
        for (SendTypeEnum es : SendTypeEnum.values()){
            if(item == es.getItem()){
                return es.getItemName();
            }
        }
        return "";
    }
}
