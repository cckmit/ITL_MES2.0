
package com.itl.mes.pp.provider.config;

import java.math.BigDecimal;

/**
 * 常量
 *
 */
public class Constant {

    public static final BigDecimal ORDER_NUM = new BigDecimal("20");

    public static final String BIG_LINE = "A";//大机台
    public static final String SMALL_LINE = "B";//小机台

    public static final String AUTO_SCHEDULE_TYPE = "A";

    public static final Integer IS_DEFAULT = 2;

    public static final Integer IS_MOVE = 1;
    public static final Integer IS_NOT_MOVE = 2;

    /**
     * 资源类型，1为工厂，2为车间，3为产线，为设备，5为用户
     */
    public static final String SITE="1";

    public static final String WORK_SHOP="2";

    public static final String PRODUCT_LINE = "3";

    public static final String DEVICE = "4";

    public static final String USER = "5";


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

        public static String getItemName(String item){
            for (andonResourceType es : andonResourceType.values()){
                if(item.equals(es.getValue())){
                    return es.getDesc();
                }
            }
            return "";
        }
    }

    /**
     * 资源日历状态，0为未使用，1为被引用，被引用的数据将不可删除
     */

    public enum ResourcesCalendarState {
        NO_USE("0","未使用"),
        USE("1","被引用");

        private String value;

        private String desc;

        private ResourcesCalendarState(String value,String desc){
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
     * 排程状态，1为新建，2为下达，3为接收
     */

    public enum ScheduleState {
        CREATE(1,"创建"),
        RELEASE(2,"下达"),
        RECEIVE(3,"接收");

        private int value;

        private String desc;

        private ScheduleState(int value,String desc){
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ScheduleReceiveState {
        CREATE(1,"创建"),
        RECEIVE(2,"接收");

        private int value;

        private String desc;

        private ScheduleReceiveState(int value,String desc){
            this.value = value;
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public int getValue() {
            return value;
        }
    }

//    public static void main(String[] args) {
//        System.out.print(Constant.andonResourceType.getItemName("4"));
//    }


}
