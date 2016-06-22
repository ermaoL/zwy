package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/5/20.
 */
public enum DomainStatus {

    Normal(0,"正常"),
    Delete(1,"已删除");

    private int value;
    private String desc;

    DomainStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return DomainStatus.Normal.desc;
            case 2:
                return DomainStatus.Delete.desc;
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
