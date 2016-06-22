package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/6/2.
 */
public enum OperateType {
    SentOrder(1,"gbs下单"),
    AdminLogin(2,"gbs登录");

    private int value;
    private String desc;

    OperateType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static OperateType valueOf(int value) {
        switch (value) {
            case 1:
                return OperateType.SentOrder;
            case 2:
                return OperateType.SentOrder;
            default:
                throw new IllegalArgumentException(String.format("value:%s is illegal value", value));
        }
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return OperateType.SentOrder.desc;
            case 2:
                return OperateType.SentOrder.desc;
            default:
                throw new IllegalArgumentException(String.format("value:%s is illegal value", value));
        }
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
