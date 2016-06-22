package com.sinotrans.transport.dto.vo;

import com.sinotrans.transport.common.enums.OrderStateType;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emi on 2016/5/13.
 * 出口订单 查询 返回 Vo
 */
public class OrderExportSearchRespVo {

    private int index;

    private String orderId;

    private String orderBillNo;//提单号

    private String orderWharf;//码头
//    private String orderWharfCode;//码头code

    private String orderShipName;//船名
//    private String orderShipNameCode;//船名code

    private String orderSailing;//船次,航次
//    private String orderSailingCode;//船次,航次code

    private String orderState;//单据状态

    private String orderStateDesc;//单据状态

    private String orderSailingDate;//开航日期

//    private String orderPort;//起运港
//    private String orderPortCode;//起运港code

    private String orderEnterPort;//中转港
//    private String orderEnterPortCode;//中转港code

    private String orderDestinationPort;//目的港
//    private String orderDestinationPortCode;//目的港code

    private String orderShippingCode;//货代代码

    public OrderExportSearchRespVo() {
        super();
    }

    public OrderExportSearchRespVo(int index, Long orderId, String orderBillNo, String orderWharf, String orderShipName, String orderSailing, Integer orderState, Date orderSailingDate, String orderEnterPort, String orderDestinationPort, String orderShippingCode, SimpleDateFormat sdf) {
        this();
        this.index = index;
        this.orderId = String.valueOf(orderId);
        this.orderBillNo = orderBillNo;
        this.orderWharf = orderWharf;
        this.orderShipName = orderShipName;
        this.orderSailing = orderSailing;
        this.orderState = String.valueOf(orderState);
        this.orderStateDesc = OrderStateType.descOf(orderState);
        this.orderSailingDate = null == orderSailingDate ? "" : sdf.format(orderSailingDate);
        this.orderEnterPort = orderEnterPort;
        this.orderDestinationPort = orderDestinationPort;
        this.orderShippingCode = orderShippingCode;
    }

    //    public OrderExportSearchRespVo(int index, Long orderId, String orderBillNo, String orderWharf, String orderWharfCode, String orderShipName, String orderShipNameCode, String orderSailing, String orderSailingCode, Integer orderState, Date orderSailingDate, String orderPort, String orderPortCode, String orderEnterPort, String orderEnterPortCode, String orderDestinationPort, String orderDestinationPortCode, String orderShippingCode, SimpleDateFormat sdf) {
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
//        this.orderSailingDate = null == orderSailingDate ? "" : sdf.format(orderSailingDate);
//        this.orderPort = orderPort;
//        this.orderPortCode = orderPortCode;
//        this.orderEnterPort = orderEnterPort;
//        this.orderEnterPortCode = orderEnterPortCode;
//        this.orderDestinationPort = orderDestinationPort;
//        this.orderDestinationPortCode = orderDestinationPortCode;
//        this.orderShippingCode = orderShippingCode;
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

    public String getOrderWharf() {
        return orderWharf;
    }

    public void setOrderWharf(String orderWharf) {
        this.orderWharf = orderWharf;
    }

    public String getOrderShipName() {
        return orderShipName;
    }

    public void setOrderShipName(String orderShipName) {
        this.orderShipName = orderShipName;
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

    public String getOrderSailingDate() {
        return orderSailingDate;
    }

    public void setOrderSailingDate(String orderSailingDate) {
        this.orderSailingDate = orderSailingDate;
    }

    public String getOrderEnterPort() {
        return orderEnterPort;
    }

    public void setOrderEnterPort(String orderEnterPort) {
        this.orderEnterPort = orderEnterPort;
    }

    public String getOrderDestinationPort() {
        return orderDestinationPort;
    }

    public void setOrderDestinationPort(String orderDestinationPort) {
        this.orderDestinationPort = orderDestinationPort;
    }

    public String getOrderShippingCode() {
        return orderShippingCode;
    }

    public void setOrderShippingCode(String orderShippingCode) {
        this.orderShippingCode = orderShippingCode;
    }
}
