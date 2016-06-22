package com.sinotrans.transport.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/5/16.
 * 进口订单 创建修改 请求接口
 */
public class OrderImportAlterRequest {

    @Pattern(regexp = "\\d*", message = "Id格式错误")
    private String orderId;// id，  注意：只有在更新的状况下才使用该字段

    @NotEmpty(message = "提单号不能为空")
    private String orderBillNo;//提单号            export,import   //todo:是否可变？？

//    private String orderCompany;//下发公司         export,import

    private String orderWharf;//码头                  export,import
    private String orderWharfCode;//码头                  export,import

    private String orderShipName;//船名               export,import
    private String orderShipNameCode;//船名               export,import

    private String orderSailing;//船次,航次            export,import
    private String orderSailingCode;//船次,航次            export,import

    private String orderOwner;//箱主                      export,import
    private String orderOwnerCode;//箱主代码              export,import

    private String orderGoodOwner;//货主
    private String orderGoodOwnerCode;//货主

    private String orderShipAgency;//船代             export,import

//    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "抵港日期格式错误")
    private String orderArrivalDate;//抵港日期                    import

//    private String orderFreeUseDay;//免费使用日期        export,import

    @NotEmpty(message = "总箱量不能为空")
    @Pattern(regexp = "\\d{0,11}", message = "总箱量格式错误")
    private String orderTotalCase;//总箱量                export,import

//    private String orderSurplusTank;//剩余柜量             export,import

    @Length(max = 4000, message = "备注最多4000个字符")
    private String orderRemark;//备注

    @NotEmpty
    private String orderConnects;//订单联系人

    @NotEmpty
    @Email(message = "邮箱格式错误")
    private String orderConnectEmail;//联系人邮箱

    @NotEmpty
    private String orderConnectPhone;//联系人手机

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
