package org.marsclub.test;

/**
 * 简单业务编号枚举
 * select * from chery_iotd_config.t_support_code_pattern;
 *
 * @author 00440498
 */

public enum SimpleBizCodeEnum {


    PART_STOCK_IN_ORDER_CODE("IB", "part_stock_in_order_code"),

    PART_STOCK_OUT_ORDER_CODE("OB", "part_stock_out_order_code"),

    PART_STOCK_TAKE_ORDER_CODE("IC", "part_stock_take_order_code"),

    PART_STOCK_ALLOCATE_ORDER_CODE("ST", "part_stock_allocate_order_code"),

    /**
     * 活动订单-活动订单固定前缀
     */
    ACTIVITY_ORDER_CODE("MA", "activity_order_code"),

    /**
     * 线索编码LI
     */
    LEADS_CODE("LI","leads_code"),
    /**
     * 实销单编码
     */
    ACTUAL_SO_CODE("SO","actual_sales_order_code"),

    ACTUAL_SALES_RETURN_CODE("SR","actual_sales_return_order_code"),


    MOTORFINITY_VIN_UNIT_NUMBER("","motorfinity_vin_unit_number")

    //预定采购单号
    ,PRE_PURCHASE_ORDER_CODE("PREPO","pre_purchase_order_code")

    //虚拟vin
    ,VIRTUAL_VIN("PV","virtual_vin")


    ;

    private static final String dataFormat = "yyMM";
    private String orderNoHead;

    private String scene;

    SimpleBizCodeEnum(String orderNoHead, String scene) {
        this.orderNoHead = orderNoHead;
        this.scene = scene;
    }

    public String getOrderNoHead() {
        return orderNoHead;
    }

    public String getScene() {
        return scene;
    }
}
