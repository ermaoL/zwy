package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/6/2.
 */
public enum CheckStateType {

    Failed(0,"未通过"),
    Pass(1,"通过");

    private int value;
    private String desc;

    CheckStateType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 0:
                return CheckStateType.Failed.desc;
            case 1:
                return CheckStateType.Pass.desc;
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
