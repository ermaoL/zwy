package com.sinotrans.transport.dto.out;


import com.sinotrans.transport.dto.common.RestResponse;

import java.util.Map;

/**
 * Created by emi on 2016/6/1.
 * 向GBS 下发订单 的 返回接口
 */
public class OrderGbsSendResponse extends RestResponse{

    private String orderGbsDocCode;                         //gbs订单表主键

    private Map<String, String> containerMap;   //gbs 箱表返回信息, key是oms箱Id，  todo:value是gbs箱的？？？   这个要找eff问清楚是PB_S_CONTAINER还是DOC_CODE

    public OrderGbsSendResponse(String orderGbsDocCode, Map<String, String> containerMap) {
        this.orderGbsDocCode = orderGbsDocCode;
        this.containerMap = containerMap;
    }

    public OrderGbsSendResponse(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public OrderGbsSendResponse() {
        super();
    }

    public String getOrderGbsDocCode() {
        return orderGbsDocCode;
    }

    public void setOrderGbsDocCode(String orderGbsDocCode) {
        this.orderGbsDocCode = orderGbsDocCode;
    }

    public Map<String, String> getContainerMap() {
        return containerMap;
    }

    public void setContainerMap(Map<String, String> containerMap) {
        this.containerMap = containerMap;
    }
}
