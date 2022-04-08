package com.itl.iap.notice.api.enums;

/**
 * 通知类型
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
public enum NoticeTypeEnum {
    /**
     * 通知类型
     */
    NOTICE(1,"公告"),
    REMIND(2,"提醒"),
    notify(3,"知会"),
    urge(4,"催办");

    private int item;

    private String itemName;

    NoticeTypeEnum(int item, String itemName) {
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
        for (NoticeTypeEnum es : NoticeTypeEnum.values()){
            if(item == es.getItem()){
                return es.getItemName();
            }
        }
        return "";
    }
}
