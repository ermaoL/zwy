package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/6/24.
 */
public enum PositionLogType {

    ImSet(1, "SEND_ORDER", "下单"),
    ImVerify(2, "VERIFY", "审核通过"),//1:下单完成
    ImMakePlan(3, "MAKE_PLAN", "制作计划"),
    ImHandOut(4, "HAND_OUT", "下发司机"),
    ImAccept(5, "ACCEPT", "司机接受"),
    ImSetOutHeavy(6, "SET_OUT_MOVE_HEAVY", "出发移重"),
    ImCarryHeavy(7, "CARRY_HEAVY", "提重箱"),
    ImOutGate(8, "OUT_GATE", "出闸"),
    ImSendLocker(9, "SEND_LOCKER", "寄柜"),//2:出闸
    ImGoToFactory(10, "GO_TO_FACTORY", "出发去工厂"),
    ImArriveFactory(11, "ARRIVE_FACTORY", "到达工厂"),//3:到厂
    ImLeaveFactory(12, "LEAVE_FACTORY", "离开工厂"),//4:卸货
    ImEmptyGiveBack(13, "EMPTY_GIVE_BACK", "空箱回厂"),//5还空完成
    ImFreeDate(14, "FREE_DEADLINE", "免费期截止日期"),

    ExSet(31, "SEND_ORDER", "下单"),
    ExVerify(32, "VERIFY", "审核通过"),//1:下单完成
    ExMakePlan(33, "MAKE_PLAN", "制作计划"),
    ExHandOut(34, "HAND_OUT", "下发司机"),
    ExAccept(35, "ACCEPT", "司机接受"),
    ExSetOutEmpty(36, "SET_OUT_EMPTY", "出发提空"),
    ExEmptyFinish(37, "EMPTY_FINISH", "提空完成"),//2提空
    ExArriveFactory(38, "ARRIVE_FACTORY", "到达工厂"),//到达工厂
    ExLeaveFactory(39, "LEAVE_FACTORY", "离开工厂"),//装货完成
    ExSendLocker(40, "SEND_LOCKER", "寄柜"),
    ExInPort(41, "IN_PORT", "进港"),
    ExInPortReport(42, "IN_PORT_REPORT", "进港报"),//进港完成
    ExDeadline(43, "In_DEADLINE", "截进港时间")
    ;

    private int value;
    private String code;
    private String desc;

    PositionLogType(int value, String code, String desc) {
        this.value = value;
        this.code = code;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String checkerServiceDesc(PositionLogType positionLogType, String adminName, String service) {
        return positionLogType.getDesc() + "(审核人：" + adminName + ",客服电话：" + service + ")";
    }
}
