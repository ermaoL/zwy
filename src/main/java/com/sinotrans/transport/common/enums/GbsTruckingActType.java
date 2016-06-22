package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/6/6.
 */
public enum GbsTruckingActType {

    ExportOutEmpty(10,"外提空箱"),
    ExportLayWeight(20,"出口放箱"),
    ExportGoBack(30,"出口回箱"),
    ExportInWharf(40,"进码头"),
    ImportWeightMoved(15,"重箱移场"),
    ImportLayWeight(25,"进口放重"),
    ImportLayEmpty(35,"进口放空"),
    ImportGiveBack(45,"进口还空");

    private int value;
    private String desc;

    GbsTruckingActType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 10:
                return GbsTruckingActType.ExportOutEmpty.desc;
            case 20:
                return GbsTruckingActType.ExportLayWeight.desc;
            case 30:
                return GbsTruckingActType.ExportGoBack.desc;
            case 40:
                return GbsTruckingActType.ExportInWharf.desc;

            case 15:
                return GbsTruckingActType.ImportWeightMoved.desc;
            case 25:
                return GbsTruckingActType.ImportLayWeight.desc;
            case 35:
                return GbsTruckingActType.ImportLayEmpty.desc;
            case 45:
                return GbsTruckingActType.ImportGiveBack.desc;

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
