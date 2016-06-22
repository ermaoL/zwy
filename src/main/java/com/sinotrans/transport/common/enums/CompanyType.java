package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/5/13.
 */
public enum CompanyType {

    YuXiong(1,"裕雄", "XMZZ"),
    YuGuo(2,"裕国", "FZZZ"),
    AdminAll(3, "管理员", "ADMIN");

    private int value;
    private String desc;
    private String code;

    CompanyType(int value, String desc, String code) {
        this.value = value;
        this.desc = desc;
        this.code = code;
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return CompanyType.YuXiong.desc;
            case 2:
                return CompanyType.YuGuo.desc;
            case 3:
                return CompanyType.AdminAll.desc;
            default:
                throw new IllegalArgumentException(String.format("value:%s is illegal value", value));
        }
    }

    public static String codeOf(int value) {
        switch (value) {
            case 1:
                return CompanyType.YuXiong.code;
            case 2:
                return CompanyType.YuGuo.code;
            case 3:
                return CompanyType.AdminAll.code;
            default:
                throw new IllegalArgumentException(String.format("value:%s is illegal value", value));
        }
    }

    public static CompanyType valueOf(int value) {
        switch (value) {
            case 1:
                return CompanyType.YuXiong;
            case 2:
                return CompanyType.YuGuo;
            case 3:
                return CompanyType.AdminAll;
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

    public String getCode() {
        return code;
    }
}
