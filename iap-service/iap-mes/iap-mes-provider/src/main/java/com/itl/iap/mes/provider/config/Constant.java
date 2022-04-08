
package com.itl.iap.mes.provider.config;

/**
 * 常量
 *
 */
public class Constant {

    public static final String SYS_ADMIN = "sysadmin";

    public static final Integer QUERY_PARAM = 3;

    public static final Integer BAR_CODE= 4;

    public static final Integer QR_CODE= 5;

    public static final Integer MAX_THREAD = 200;

    public enum ScheduleStatus implements BaseEnum{
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

    public enum IsYesEnum implements BaseEnum{
        YES(0,"是"),
        NO(1,"否");

        private int item;

        private String itemName;

        IsYesEnum(int item, String itemName) {
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
            for (IsYesEnum es : IsYesEnum.values()){
                if(item == es.getItem()){
                    return es.getItemName();
                }
            }
            return "";
        }
    }

    public enum SuccessEnum implements BaseEnum{
        SUCCESS(0,"成功"),
        FAIL(1,"失败");

        private int item;

        private String itemName;

        SuccessEnum(int item, String itemName) {
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
            for (SuccessEnum es : SuccessEnum.values()){
                if(item == es.getItem()){
                    return es.getItemName();
                }
            }
            return "";
        }
    }

    public enum EnabledEnum implements BaseEnum{
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

    public enum RepairEnum implements BaseEnum{
        YES(0,"未维修"),
        NO(1,"维修中"),ED(2,"已维修");

        private int item;

        private String itemName;

        RepairEnum(int item, String itemName) {
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
            for (RepairEnum es : RepairEnum.values()){
                if(item == es.getItem()){
                    return es.getItemName();
                }
            }
            return "";
        }
    }

    public enum YtdEnum implements BaseEnum{
        MONTH(0,"月"),
        DAY(1,"天"),
        HOUR(2,"时");

        private int item;

        private String itemName;

        YtdEnum(int item, String itemName) {
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
            for (YtdEnum es : YtdEnum.values()){
                if(item == es.getItem()){
                    return es.getItemName();
                }
            }
            return "";
        }
    }
}
