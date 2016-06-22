package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/5/13.
 * 单据状态
 */
public enum OrderStateType {

    Create(1,"未下单"),
    Send(2,"已下发"),
    Failed(3,"未通过"),
    Running(4,"执行中"),
    Finish(5,"完成");

    private int value;
    private String desc;

    OrderStateType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return OrderStateType.Create.desc;
            case 2:
                return OrderStateType.Send.desc;
            case 3:
                return OrderStateType.Failed.desc;
            case 4:
                return OrderStateType.Running.desc;
            case 5:
                return OrderStateType.Finish.desc;
            default:
                throw new IllegalArgumentException(String.format("value:%s is illegal value", value));
        }
    }

    public static OrderStateType valueOf(int value) {
        switch (value) {
            case 1:
                return OrderStateType.Create;
            case 2:
                return OrderStateType.Send;
            case 3:
                return OrderStateType.Failed;
            case 4:
                return OrderStateType.Running;
            case 5:
                return OrderStateType.Finish;
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
