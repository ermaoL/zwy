package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/5/13.
 */
public enum OrderType {

    Import(1,"进口"),
    Export(2,"出口");

    private int value;
    private String desc;

    OrderType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static OrderType valueOf(int value) {
        switch (value) {
            case 1:
                return OrderType.Import;
            case 2:
                return OrderType.Export;
            default:
                throw new IllegalArgumentException(String.format("value:%s is illegal value", value));
        }
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return OrderType.Import.desc;
            case 2:
                return OrderType.Export.desc;
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
