package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;

/**
 * Created by emi on 2016/5/16.
 * 进口订单 创建修改 返回接口
 */
public class OrderImportAlterResponse extends RestResponse {

    private String orderId;

//    private String orderSystemNo;//系统订单号

    public OrderImportAlterResponse() {
        super();
    }

    public OrderImportAlterResponse(long orderId) {
        this();
        this.orderId = String.valueOf(orderId);
//        this.orderSystemNo = orderSystemNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
