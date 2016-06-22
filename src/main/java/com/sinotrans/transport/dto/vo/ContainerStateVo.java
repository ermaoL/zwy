package com.sinotrans.transport.dto.vo;

/**
 * Created by emi on 2016/6/15.
 */
public class ContainerStateVo {

    private String containerId;//箱主键

    private String containerGbsId;//gbs库箱Id

    private String orderBillNo;//提单号

    private String containerCaseNo;//箱号

    private String orderType;//1：进口  2：出口

    private String containerState;//箱状态(int 类型) 15:重箱移场  25:进口放重  35:进口放空  45:进口还空  10:外提空箱  20:出口放箱  30:出口回箱  40:进码头

    private String containerStateDesc;//箱状态描述(String 类型) 15:重箱移场  25:进口放重  35:进口放空  45:进口还空  10:外提空箱  20:出口放箱  30:出口回箱  40:进码头

    public ContainerStateVo(Long containerId, String containerGbsId, String orderBillNo, String containerCaseNo, int orderType, int containerState, String containerStateDesc) {
        this();
        this.containerId = containerId + "";
        this.containerGbsId =containerGbsId;
        this.orderBillNo = orderBillNo;
        this.containerCaseNo = containerCaseNo;
        this.orderType = orderType + "";
        this.containerState = containerState + "";
        this.containerStateDesc = containerStateDesc;
    }

    public ContainerStateVo() {
        super();
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerGbsId() {
        return containerGbsId;
    }

    public void setContainerGbsId(String containerGbsId) {
        this.containerGbsId = containerGbsId;
    }

    public String getOrderBillNo() {
        return orderBillNo;
    }

    public void setOrderBillNo(String orderBillNo) {
        this.orderBillNo = orderBillNo;
    }

    public String getContainerCaseNo() {
        return containerCaseNo;
    }

    public void setContainerCaseNo(String containerCaseNo) {
        this.containerCaseNo = containerCaseNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getContainerState() {
        return containerState;
    }

    public void setContainerState(String containerState) {
        this.containerState = containerState;
    }

    public String getContainerStateDesc() {
        return containerStateDesc;
    }

    public void setContainerStateDesc(String containerStateDesc) {
        this.containerStateDesc = containerStateDesc;
    }
}
