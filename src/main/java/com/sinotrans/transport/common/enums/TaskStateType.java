package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/6/6.
 */
public enum TaskStateType {

    WaitAccept(1, "待接收"),
    Accepted(2, "接受"),
    Refused(3, "拒绝"),
    Finish(4, "完成")
    ;

    private int value;
    private String desc;

    TaskStateType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static TaskStateType valueOf(int value) {
        switch (value) {
            case 1:
                return TaskStateType.WaitAccept;
            case 2:
                return TaskStateType.Accepted;
            case 3:
                return TaskStateType.Refused;
            case 4:
                return TaskStateType.Finish;
            default:
                throw new IllegalArgumentException(String.format("value:%s is illegal value", value));
        }
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return TaskStateType.WaitAccept.desc;
            case 2:
                return TaskStateType.Accepted.desc;
            case 3:
                return TaskStateType.Refused.desc;
            case 4:
                return TaskStateType.Finish.desc;
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
