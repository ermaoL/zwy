package com.sinotrans.transport.dto.vo;

import com.sinotrans.transport.common.enums.OrderStateType;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emi on 2016/5/20.
 * 进口订单 查询全页面数据中头部订单信息 返回 Vo
 */
public class OrderImportDetailRespVo {

    private String orderId;//id，  注意：只有在更新的状况下才使用该字段

    private String orderBillNo;//提单号            export,import

//    private String orderSystemNo;//系统订单号        export,import

    private String orderState;//单据状态 int类型
    private String orderStateDesc;//单据状态中文

//    private String orderCompany;//下发公司 int类型 1 2      export,import         delete
//    private String orderCompanyDesc;//下发公司中文       export,import            delete

    private String orderWharf;//码头                  export,import
    private String orderWharfCode;//码头              export,import

    private String orderShipName;//船名               export,import
    private String orderShipNameCode;//船名           export,import

    private String orderSailing;//船次,航次            export,import
    private String orderSailingCode;//船次,航次        export,import

    private String orderOwner;//箱主                      export,import
    private String orderOwnerCode;//箱主代码              export,import

    private String orderGoodOwner;//货主                  export,import
    private String orderGoodOwnerCode;//货主代码           export,import

    private String orderShipAgency;//船代             export,import

    private String orderArrivalDate;//抵港日期                ,import

//    private String orderFreeUseDay;//免费使用日期        export,import

    private String orderTotalCase;//总箱量                export,import

//    private String orderSurplusTank;//剩余柜量             export,import

    private String verifierContent;//审核不通过的理由， 只有状态为审核未通过的时候该字段才有值

    private String orderRemark;//备注

    private String orderConnects;//订单联系人

    private String orderConnectEmail;//联系人邮箱

    private String orderConnectPhone;//联系人手机

    public OrderImportDetailRespVo(Long orderId, String orderBillNo, Integer orderState, String orderWharf, String orderWharfCode, String orderShipName, String orderShipNameCode, String orderSailing, String orderSailingCode, String orderOwnerCode,
                                   String orderOwner, String orderGoodOwner,String orderGoodOwnerCode, String orderShipAgency, Date orderArrivalDate, Integer orderTotalCase, String verifierContent, String orderRemark, String orderConnects, String orderConnectEmail, String orderConnectPhone, SimpleDateFormat sdf) {
        this();
        this.orderId = String.valueOf(orderId);
        this.orderBillNo = orderBillNo;
//        this.orderSystemNo = orderSystemNo;
        this.orderState = String.valueOf(orderState);
        this.orderStateDesc = OrderStateType.descOf(orderState);
//        this.orderCompany = String.valueOf(orderCompany);
//        this.orderCompanyDesc = CompanyType.descOf(orderCompany);
        this.orderWharf = orderWharf;
        this.orderWharfCode = orderWharfCode;
        this.orderShipName = orderShipName;
        this.orderShipNameCode = orderShipNameCode;
        this.orderSailing = orderSailing;
        this.orderSailingCode = orderSailingCode;
        this.orderOwnerCode = orderOwnerCode;
        this.orderOwner = orderOwner;
        this.orderGoodOwner = orderGoodOwner;
        this.orderGoodOwnerCode = orderGoodOwnerCode;
        this.orderShipAgency = orderShipAgency;
        this.orderArrivalDate = null == orderArrivalDate ? "" : sdf.format(orderArrivalDate);
//        this.orderFreeUseDay = orderFreeUseDay;
        this.orderTotalCase = null == orderTotalCase ? "" : String.valueOf(orderTotalCase);
//        this.orderSurplusTank = String.valueOf(orderSurplusTank);
        this.verifierContent = StringUtils.isEmpty(verifierContent) ? "" : verifierContent;
        this.orderRemark = orderRemark;
        this.orderConnects = orderConnects;
        this.orderConnectEmail = orderConnectEmail;
        this.orderConnectPhone = orderConnectPhone;
    }

    public OrderImportDetailRespVo() {
        super();
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

    public String getOrderSailingCode() {
        return orderSailingCode;
    }

    public void setOrderSailingCode(String orderSailingCode) {
        this.orderSailingCode = orderSailingCode;
    }

    public String getOrderOwner() {
        return orderOwner;
    }

    public void setOrderOwner(String orderOwner) {
        this.orderOwner = orderOwner;
    }

    public String getOrderOwnerCode() {
        return orderOwnerCode;
    }

    public void setOrderOwnerCode(String orderOwnerCode) {
        this.orderOwnerCode = orderOwnerCode;
    }

    public String getOrderGoodOwner() {
        return orderGoodOwner;
    }

    public void setOrderGoodOwner(String orderGoodOwner) {
        this.orderGoodOwner = orderGoodOwner;
    }

    public String getOrderGoodOwnerCode() {
        return orderGoodOwnerCode;
    }

    public void setOrderGoodOwnerCode(String orderGoodOwnerCode) {
        this.orderGoodOwnerCode = orderGoodOwnerCode;
    }

    public String getOrderShipAgency() {
        return orderShipAgency;
    }

    public void setOrderShipAgency(String orderShipAgency) {
        this.orderShipAgency = orderShipAgency;
    }

    public String getOrderArrivalDate() {
        return orderArrivalDate;
    }

    public void setOrderArrivalDate(String orderArrivalDate) {
        this.orderArrivalDate = orderArrivalDate;
    }

    public String getOrderTotalCase() {
        return orderTotalCase;
    }

    public void setOrderTotalCase(String orderTotalCase) {
        this.orderTotalCase = orderTotalCase;
    }

    public String getVerifierContent() {
        return verifierContent;
    }

    public void setVerifierContent(String verifierContent) {
        this.verifierContent = verifierContent;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderConnects() {
        return orderConnects;
    }

    public void setOrderConnects(String orderConnects) {
        this.orderConnects = orderConnects;
    }

    public String getOrderConnectEmail() {
        return orderConnectEmail;
    }

    public void setOrderConnectEmail(String orderConnectEmail) {
        this.orderConnectEmail = orderConnectEmail;
    }

    public String getOrderConnectPhone() {
        return orderConnectPhone;
    }

    public void setOrderConnectPhone(String orderConnectPhone) {
        this.orderConnectPhone = orderConnectPhone;
    }
}
