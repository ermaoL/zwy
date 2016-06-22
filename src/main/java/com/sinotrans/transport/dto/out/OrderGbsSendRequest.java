package com.sinotrans.transport.dto.out;

import com.sinotrans.transport.dto.out.vo.ContainerGbsSendReqVo;
import com.sinotrans.transport.dto.out.vo.OrderGbsSendReqVo;

import java.util.List;

/**
 * Created by emi on 2016/6/1.
 * 向GBS 下发订单 的 请求接口
 */
public class OrderGbsSendRequest {

    private String customerCode;//XMZZ FZZZ             //PB_BUSINESS： CRM_CODE

    private String customerName;//裕雄  裕国            //PB_BUSINESS： CRM_NAME

    private OrderGbsSendReqVo orderVo;                  //oms平台 订单相关信息

    private List<ContainerGbsSendReqVo> containerVoList;    //oms平台 箱子相关信息

    public OrderGbsSendRequest(String customerCode, String customerName, OrderGbsSendReqVo orderVo, List<ContainerGbsSendReqVo> containerVoList) {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.orderVo = orderVo;
        this.containerVoList = containerVoList;
    }

    public OrderGbsSendRequest() {
        super();
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public OrderGbsSendReqVo getOrderVo() {
        return orderVo;
    }

    public void setOrderVo(OrderGbsSendReqVo orderVo) {
        this.orderVo = orderVo;
    }

    public List<ContainerGbsSendReqVo> getContainerVoList() {
        return containerVoList;
    }

    public void setContainerVoList(List<ContainerGbsSendReqVo> containerVoList) {
        this.containerVoList = containerVoList;
    }
}
