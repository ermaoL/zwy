package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/6/14.
 */
public enum TranStateType {

    Running(1,"执行中"),
    Finish(2, "结束");

    private int value;
    private String desc;

    TranStateType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return TranStateType.Running.desc;
            case 2:
                return TranStateType.Finish.desc;
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
