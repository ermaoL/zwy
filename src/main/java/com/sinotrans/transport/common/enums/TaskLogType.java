package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/6/1.
 */
public enum  TaskLogType {

    Send(1,"下发"),
    Accept(2,"接受"),
    Refuse(3,"拒绝");

    private int value;
    private String desc;

    TaskLogType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return TaskLogType.Send.desc;
            case 2:
                return TaskLogType.Accept.desc;
            case 3:
                return TaskLogType.Refuse.desc;
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
