package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/6/3.
 */
public enum TemperUnitType {

    F(0,"F"),
    C(1,"C")
    ;

    private int value;
    private String desc;

    TemperUnitType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 0:
                return TemperUnitType.F.desc;
            case 1:
                return TemperUnitType.C.desc;
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
