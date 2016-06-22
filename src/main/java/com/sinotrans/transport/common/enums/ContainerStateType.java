package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/5/17.
 */
public enum  ContainerStateType {

//    ImportCreate(1,"新增"),
//    ImportSend(2,"已下发"),
//    ImportCheckFailed(3,"审核未通过"),
//    ImportChecked(4,"已审核"),
//    ImportWeightMoved(5,"重箱移场"),
//    ImportLayWeight(6,"进口放重"),
//    ImportLayEmpty(7,"进口放空"),
//    ImportGiveBack(8,"进口还空"),
//    ImportFinish(9, "结束"),
//
//    ExportCreate(21,"新增"),
//    ExportSend(22,"已下发"),
//    ExportCheckFailed(23,"审核未通过"),
//    ExportChecked(24,"已审核"),
//    ExportOutEmpty(25,"外提空箱"),
//    ExportLayWeight(26,"出口放箱"),
//    ExportGoBack(27,"出口回箱"),
//    ExportInWharf(28,"进码头"),
//    ExportFinish(29,"结束");

    ImportCreate(1,"未下单"),
    ImportSend(2,"已下发"),
    ImportCheckFailed(3,"审核未通过"),
    ImportChecked(4,"已审核"),
    ImportWeightMoved(15,"重箱移场"),
    ImportLayWeight(25,"进口放重"),
    ImportLayEmpty(35,"进口放空"),
    ImportGiveBack(45,"进口还空"),
    Finish(9, "结束"),

    ExportCreate(21,"未下单"),
    ExportSend(22,"已下发"),
    ExportCheckFailed(23,"审核未通过"),
    ExportChecked(24,"已审核"),
    ExportOutEmpty(10,"外提空箱"),
    ExportLayWeight(20,"出口放箱"),
    ExportGoBack(30,"出口回箱"),
    ExportInWharf(40,"进码头");
//    ExportFinish(29,"结束");

    private int value;
    private String desc;

    ContainerStateType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return ContainerStateType.ImportCreate.desc;
            case 2:
                return ContainerStateType.ImportSend.desc;
            case 3:
                return ContainerStateType.ImportCheckFailed.desc;
            case 4:
                return ContainerStateType.ImportChecked.desc;
            case 15:
                return ContainerStateType.ImportWeightMoved.desc;
            case 25:
                return ContainerStateType.ImportLayWeight.desc;
            case 35:
                return ContainerStateType.ImportLayEmpty.desc;
            case 45:
                return ContainerStateType.ImportGiveBack.desc;
            case 9:
                return ContainerStateType.Finish.desc;
            case 21:
                return ContainerStateType.ExportCreate.desc;
            case 22:
                return ContainerStateType.ExportSend.desc;
            case 23:
                return ContainerStateType.ExportCheckFailed.desc;
            case 24:
                return ContainerStateType.ExportChecked.desc;
            case 10:
                return ContainerStateType.ExportOutEmpty.desc;
            case 20:
                return ContainerStateType.ExportLayWeight.desc;
            case 30:
                return ContainerStateType.ExportGoBack.desc;
            case 40:
                return ContainerStateType.ExportInWharf.desc;
//            case 29:
//                return ContainerStateType.ExportFinish.desc;
            default:
                throw new IllegalArgumentException(String.format("value:%s is illegal value", value));
        }
    }


    public static ContainerStateType valueOf(int value) {
        switch (value) {
            case 1:
                return ContainerStateType.ImportCreate;
            case 2:
                return ContainerStateType.ImportSend;
            case 3:
                return ContainerStateType.ImportCheckFailed;
            case 4:
                return ContainerStateType.ImportChecked;
            case 5:
                return ContainerStateType.ImportWeightMoved;
            case 6:
                return ContainerStateType.ImportLayWeight;
            case 7:
                return ContainerStateType.ImportLayEmpty;
            case 8:
                return ContainerStateType.ImportGiveBack;
            case 9:
                return ContainerStateType.Finish;
            case 21:
                return ContainerStateType.ExportCreate;
            case 22:
                return ContainerStateType.ExportSend;
            case 23:
                return ContainerStateType.ExportCheckFailed;
            case 24:
                return ContainerStateType.ExportChecked;
            case 25:
                return ContainerStateType.ExportOutEmpty;
            case 26:
                return ContainerStateType.ExportLayWeight;
            case 27:
                return ContainerStateType.ExportGoBack;
            case 28:
                return ContainerStateType.ExportInWharf;
//            case 29:
//                return ContainerStateType.ExportFinish;
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
