package com.sinotrans.transport.common.enums;

/**
 * Created by emi on 2016/5/18.
 */
public enum DomainType {

    User(1,"用户"),
    Order(2,"订单"),
    Container(3,"容器"),
    Address(4,"地址"),
    Customer(5,"运输客户")
    ;

    private int value;
    private String desc;

    DomainType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String descOf(int value) {
        switch (value) {
            case 1:
                return DomainType.User.desc;
            case 2:
                return DomainType.Order.desc;
            case 3:
                return DomainType.Container.desc;
            case 4:
                return DomainType.Address.desc;
            case 5:
                return DomainType.Customer.desc;
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
