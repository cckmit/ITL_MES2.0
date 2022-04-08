
package com.itl.mes.andon.provider.config;

/**
 * 常量
 *
 */
public class Constant {


    public static final String SYS_ADMIN = "sysadmin";

    /**
     * 安灯类型标识,0代表物料，1代表设备
     */
    public static final String andonTypeTagItem = "0";

    public static final String andonTypeTagDevice = "1";


    /**
     * 安灯资源类型常量,0代表其他，4代表设备，6代表物料
     */
    public static final String resourceTypeOther = "0";

    public static final String resourceTypeDevice = "4";

    public static final String resourceTypeItem = "6";



    public enum andonState{

        ENABLE("1","启用"),
        DISABLE("0","禁用");


        private String value;

        private String desc;

        private andonState(String value,String desc){
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

    }

    /**
     * 安灯资源类型
     */
    public enum andonResourceType {

        OTHER("0","其他"),
        SITE("1","工厂"),
        WORK_SHOP("2","车间"),
        PRODUCT_LINE("3","产线"),
        DEVICE("4","设备"),
        STATION("5","工位"),
        ITEM("6","物料"),
        QUALITY("7","质量");

        private String value;

        private String desc;

        private andonResourceType(String value,String desc){
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 安灯类型标识
     */
    public enum andonTypeTag{

        ITEM("0","物料安灯"),
        DEVICE("1","设备安灯");

        private String value;

        private String desc;

        private andonTypeTag(String value, String desc){
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }


    public enum recordResourceType {

        SITE("1","工厂"),
        WORK_SHOP("2","车间"),
        PRODUCT_LINE("3","产线"),
        DEVICE("4","设备"),
        STATION("5","工位");

        private String value;

        private String desc;

        private recordResourceType(String value,String desc){
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }
    }


    public enum recordState{

        TRIGGER("1","触发异常"),
        REPAIT("2","修复异常");


        private String value;

        private String desc;

        private recordState(String value,String desc){
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public String getValue() {
            return value;
        }

    }

    public enum ScheduleStatus{
        /**
         * 通知渠道类型
         */
        NORMAL(0,"正常"),
        PAUSE(1,"暂停");

        private int item;

        private String itemName;

        ScheduleStatus(int item, String itemName) {
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
            for (ScheduleStatus es : ScheduleStatus.values()){
                if(item == es.getItem()){
                    return es.getItemName();
                }
            }
            return "";
        }
    }


    public enum EnabledEnum{
        USE(0,"启用"),
        CLOSE(1,"关闭");

        private int item;

        private String itemName;

        EnabledEnum(int item, String itemName) {
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
            for (EnabledEnum es : EnabledEnum.values()){
                if(item == es.getItem()){
                    return es.getItemName();
                }
            }
            return "";
        }
    }

    public enum TriggerEnum {
        YES(0,"未签到"),
        NO(1,"未接除"),ED(2,"已解除");

        private int item;

        private String itemName;

        TriggerEnum(int item, String itemName) {
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
            for (TriggerEnum es : TriggerEnum.values()){
                if(item == es.getItem()){
                    return es.getItemName();
                }
            }
            return "";
        }
    }
}
