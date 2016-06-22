package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/5/12.
 */
public enum UserStateType {

    Checking(1,"审核中"),
    Failed(2,"未通过"),
    Passed(3,"通过"),
    Run(4,"启用"),
    Stop(5,"停用");

    private int value;
    private String desc;

    UserStateType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return UserStateType.Checking.desc;
            case 2:
                return UserStateType.Failed.desc;
            case 3:
                return UserStateType.Passed.desc;
            case 4:
                return UserStateType.Run.desc;
            case 5:
                return UserStateType.Stop.desc;
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
