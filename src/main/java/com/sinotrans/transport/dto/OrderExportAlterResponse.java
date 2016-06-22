package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;

/**
 * Created by emi on 2016/5/16.
 * 出口订单 创建修改 返回接口
 */
public class OrderExportAlterResponse extends RestResponse{

    private String orderId;

//    private String orderSystemNo;//系统订单号

    public OrderExportAlterResponse() {
        super();
    }

    public OrderExportAlterResponse(long orderId) {
        this();
        this.orderId = String.valueOf(orderId);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
