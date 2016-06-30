package com.sinotrans.transport.dto.vo;

/**
 * Created by emi on 2016/6/29.
 */
public class OrderInfoRespVo {

    private String orderId;

    private String orderBillNo;

    private String orderType;//1：进口  2：出口

    private String orderShipName;//船名                        export,import
    private String orderShipNameCode;//船名代码                  export,import

    private String orderSailing;//船次,航次                      export,import
    private String orderSailingCode;//船次,航次                  export,import

    public OrderInfoRespVo() {
        super();
    }

    public OrderInfoRespVo(Long orderId, String orderBillNo, Integer orderType, String orderShipName, String orderShipNameCode, String orderSailing, String orderSailingCode) {
        this();
        this.orderId = String.valueOf(orderId);
        this.orderBillNo = orderBillNo;
        this.orderType = String.valueOf(orderType);
        this.orderShipName = orderShipName;
        this.orderShipNameCode = orderShipNameCode;
        this.orderSailing = orderSailing;
        this.orderSailingCode = orderSailingCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderBillNo() {
        return orderBillNo;
    }

    public void setOrderBillNo(String orderBillNo) {
        this.orderBillNo = orderBillNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderShipName() {
        return orderShipName;
    }

    public void setOrderShipName(String orderShipName) {
        this.orderShipName = orderShipName;
    }

    public String getOrderShipNameCode() {
        return orderShipNameCode;
    }

    public void setOrderShipNameCode(String orderShipNameCode) {
        this.orderShipNameCode = orderShipNameCode;
    }

    public String getOrderSailing() {
        return orderSailing;
    }

    public void setOrderSailing(String orderSailing) {
        this.orderSailing = orderSailing;
    }

    public String getOrderSailingCode() {
        return orderSailingCode;
    }

    public void setOrderSailingCode(String orderSailingCode) {
        this.orderSailingCode = orderSailingCode;
    }
}
