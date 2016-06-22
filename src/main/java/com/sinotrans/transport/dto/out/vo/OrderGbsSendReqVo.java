package com.sinotrans.transport.dto.out.vo;

import com.sinotrans.transport.common.exception.ParamNullException;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by emi on 2016/6/1.
 * 向GBS 下发订单 的 订单相关信息 请求Vo
 */
public class OrderGbsSendReqVo {

    private Long orderId;//oms订单主键                    //该字段目前用不到

    private String orderShipNameCode;//船名code                     //PB_SHARE表 VESSEL

    private String orderSailing;//航次                     //PB_SHARE表 VOYAGE                !

    private String orderWharfCode;//码头                   //表（pb_share）,字段（load_unload_dock）

    private String orderBillNo;//提单号                    //PB_SHARE表 CLAIM_NOCLAIM_NO

    private String orderPortCode;//起运港代码              //PB_SHARE表 PORT_LOADING_CODE

    private String orderPort;//起运港                      //PB_SHARE表 PORT_OF_LOADING

    private String orderDestinationPortCode;//目的港代码     //PB_SHARE表 PORT_DESTINE_CODE

    private String orderDestinationPort;//目的港           //PB_SHARE表 FINAL_DESTINATION

    private Date orderDate;//出口开航/进口抵港              //PB_SHARE表 ETD

    private Integer orderType;                              //PB_SHARE表 IMPORT_EXPORT

    private String orderCompanyCode;                        //BRANCH_CODE表 BRANCH_CODE

    private String orderGoodOwnerCode;                        //BRANCH_CODE表 SHIP_ID   //todo:货主  !

    private String orderOwnerCode;//箱主                   //PB_SHARE表 CARRIER_ID                     !

    private String creator;                                //PB_SHARE表 CREATED_BY_CODE

    private Date createTime;                              //PB_SHARE表 CREATED_TIME

    private String canvasserId;                             //审核人的code,  对应管理员登录返回的employeeCode

    private String checkDept;                              //XMZZ: GTU， FZZZ:DYD        !

    public OrderGbsSendReqVo(Long orderId, String orderShipNameCode, String orderSailing, String orderWharfCode, String orderBillNo, String orderPortCode, String orderPort, String orderDestinationPortCode, String orderDestinationPort, Date orderDate, Integer orderType, String orderCompanyCode, String orderGoodOwnerCode, String orderOwnerCode, String creator, Date createTime, String canvasserId) {
        this();
        this.orderId = orderId;
        this.orderShipNameCode = orderShipNameCode;
        this.orderSailing = this.checkNull(orderSailing, "航次");
        this.orderWharfCode = orderWharfCode;
        this.orderBillNo = orderBillNo;
        this.orderPortCode = orderPortCode;
        this.orderPort = orderPort;
        this.orderDestinationPortCode = orderDestinationPortCode;
        this.orderDestinationPort = orderDestinationPort;
        this.orderDate = orderDate ;
        this.orderType = orderType == 1 ? 1 : 0;
        this.orderCompanyCode = orderCompanyCode;
        this.orderGoodOwnerCode = this.checkNull(orderGoodOwnerCode, "货主");
        this.orderOwnerCode = this.checkNull(orderOwnerCode, "箱主");
        this.creator = creator;
        this.createTime = createTime;
        this.canvasserId = canvasserId;
        this.checkDept = orderCompanyCode.equals("XMZZ") ? "GTU" : "DYD";
    }

    public OrderGbsSendReqVo() {
        super();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getOrderWharfCode() {
        return orderWharfCode;
    }

    public void setOrderWharfCode(String orderWharfCode) {
        this.orderWharfCode = orderWharfCode;
    }

    public String getOrderBillNo() {
        return orderBillNo;
    }

    public void setOrderBillNo(String orderBillNo) {
        this.orderBillNo = orderBillNo;
    }

    public String getOrderPortCode() {
        return orderPortCode;
    }

    public void setOrderPortCode(String orderPortCode) {
        this.orderPortCode = orderPortCode;
    }

    public String getOrderPort() {
        return orderPort;
    }

    public void setOrderPort(String orderPort) {
        this.orderPort = orderPort;
    }

    public String getOrderDestinationPortCode() {
        return orderDestinationPortCode;
    }

    public void setOrderDestinationPortCode(String orderDestinationPortCode) {
        this.orderDestinationPortCode = orderDestinationPortCode;
    }

    public String getOrderDestinationPort() {
        return orderDestinationPort;
    }

    public void setOrderDestinationPort(String orderDestinationPort) {
        this.orderDestinationPort = orderDestinationPort;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderCompanyCode() {
        return orderCompanyCode;
    }

    public void setOrderCompanyCode(String orderCompanyCode) {
        this.orderCompanyCode = orderCompanyCode;
    }

    public String getOrderGoodOwnerCode() {
        return orderGoodOwnerCode;
    }

    public void setOrderGoodOwnerCode(String orderGoodOwnerCode) {
        this.orderGoodOwnerCode = orderGoodOwnerCode;
    }

    public String getOrderOwnerCode() {
        return orderOwnerCode;
    }

    public void setOrderOwnerCode(String orderOwnerCode) {
        this.orderOwnerCode = orderOwnerCode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getCanvasserId() {
        return canvasserId;
    }

    public void setCanvasserId(String canvasserId) {
        this.canvasserId = canvasserId;
    }

    public String getCheckDept() {
        return checkDept;
    }

    public void setCheckDept(String checkDept) {
        this.checkDept = checkDept;
    }


    private String checkNull(String orderShipName, String param) {
        if (StringUtils.isEmpty(orderShipName)) {
            throw new ParamNullException(param, null);
        }
        return orderShipName;
    }

}
