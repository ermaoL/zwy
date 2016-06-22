package com.sinotrans.transport.dto.vo;

import com.sinotrans.transport.common.enums.OrderStateType;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emi on 2016/5/13.
 * 进口订单 查询 返回 Vo
 */
public class OrderImportSearchRespVo {

    private int index;

    private String orderId;

    private String orderBillNo;//提单号

    private String orderWharf;//码头                  export,import
    private String orderWharfCode;//码头              export,import

    private String orderShipName;//船名               export,import
    private String orderShipNameCode;//船名           export,import

    private String orderSailing;//船次,航次            export,import
    private String orderSailingCode;//船次,航次        export,import

    private String orderState;//单据状态
    private String orderStateDesc;//单据状态

    private String orderArrivalDate;//抵港日期

    private String orderFreeUseDay;//免费使用日期

    private String orderShipAgency;//船代

    private String orderTotalCase;//总箱量                export,import

    private String orderSurplusTank;//剩余柜量             export,import

    public OrderImportSearchRespVo() {
        super();
    }

    public OrderImportSearchRespVo(int index, Long orderId, String orderBillNo, String orderWharf, String orderShipName, String orderSailing, Integer orderState, Date orderArrivalDate,  String orderShipAgency, Integer orderTotalCase, SimpleDateFormat sdf) {
        this();
        this.index = index;
        this.orderId = String.valueOf(orderId);
        this.orderBillNo = orderBillNo;
        this.orderWharf = orderWharf;
        this.orderShipName = orderShipName;
        this.orderSailing = orderSailing;
        this.orderState = String.valueOf(orderState);
        this.orderStateDesc = OrderStateType.descOf(orderState);
        this.orderArrivalDate = null == orderArrivalDate ? "" : sdf.format(orderArrivalDate);
        this.orderShipAgency = orderShipAgency;
        this.orderTotalCase = null == orderTotalCase ? "" :String.valueOf(orderTotalCase);
    }

//    public OrderImportSearchRespVo(int index, Long orderId, String orderBillNo, String orderWharf, String orderWharfCode, String orderShipName, String orderShipNameCode, String orderSailing, String orderSailingCode, Integer orderState, Date orderArrivalDate, String orderFreeUseDay, String orderShipAgency, Integer orderTotalCase, Integer orderSurplusTank, SimpleDateFormat sdf) {
//        this();
//        this.index = index;
//        this.orderId = String.valueOf(orderId);
//        this.orderBillNo = orderBillNo;
//        this.orderWharf = orderWharf;
//        this.orderWharfCode = orderWharfCode;
//        this.orderShipName = orderShipName;
//        this.orderShipNameCode = orderShipNameCode;
//        this.orderSailing = orderSailing;
//        this.orderSailingCode = orderSailingCode;
//        this.orderState = String.valueOf(orderState);
//        this.orderStateDesc = OrderStateType.descOf(orderState);
//        this.orderArrivalDate = null == orderArrivalDate ? "" : sdf.format(orderArrivalDate);
//        this.orderFreeUseDay = orderFreeUseDay;
//        this.orderShipAgency = orderShipAgency;
//        this.orderTotalCase = null == orderTotalCase ? "" :String.valueOf(orderTotalCase);
//        this.orderSurplusTank = null == orderSurplusTank ? "" : String.valueOf(orderSurplusTank);
//    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public String getOrderSailingCode() {
        return orderSailingCode;
    }

    public void setOrderSailingCode(String orderSailingCode) {
        this.orderSailingCode = orderSailingCode;
    }

    public String getOrderWharf() {
        return orderWharf;
    }

    public void setOrderWharf(String orderWharf) {
        this.orderWharf = orderWharf;
    }

    public String getOrderWharfCode() {
        return orderWharfCode;
    }

    public void setOrderWharfCode(String orderWharfCode) {
        this.orderWharfCode = orderWharfCode;
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

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderStateDesc() {
        return orderStateDesc;
    }

    public void setOrderStateDesc(String orderStateDesc) {
        this.orderStateDesc = orderStateDesc;
    }

    public String getOrderArrivalDate() {
        return orderArrivalDate;
    }

    public void setOrderArrivalDate(String orderArrivalDate) {
        this.orderArrivalDate = orderArrivalDate;
    }

    public String getOrderFreeUseDay() {
        return orderFreeUseDay;
    }

    public void setOrderFreeUseDay(String orderFreeUseDay) {
        this.orderFreeUseDay = orderFreeUseDay;
    }

    public String getOrderShipAgency() {
        return orderShipAgency;
    }

    public void setOrderShipAgency(String orderShipAgency) {
        this.orderShipAgency = orderShipAgency;
    }

    public String getOrderTotalCase() {
        return orderTotalCase;
    }

    public void setOrderTotalCase(String orderTotalCase) {
        this.orderTotalCase = orderTotalCase;
    }

    public String getOrderSurplusTank() {
        return orderSurplusTank;
    }

    public void setOrderSurplusTank(String orderSurplusTank) {
        this.orderSurplusTank = orderSurplusTank;
    }
}
