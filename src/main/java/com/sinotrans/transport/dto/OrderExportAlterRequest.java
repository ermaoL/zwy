package com.sinotrans.transport.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/5/16.
 * 出口订单 创建修改 请求接口
 */
public class OrderExportAlterRequest {

    @Pattern(regexp = "\\d*", message = "Id格式错误")
    private String orderId;// id，  注意：只有在更新的状况下才使用该字段

    @NotEmpty(message = "提单号不能为空")
    private String orderBillNo;//提单号            export,import

//    private String orderCompany;//下发公司         export,import

    private String orderWharf;//码头                  export,import
    private String orderWharfCode;//码头code          export,import

    private String orderShipName;//船名               export,import
    private String orderShipNameCode;//船名code       export,import

    private String orderSailing;//船次,航次            export,import
    private String orderSailingCode;//船次,航次code    export,import

    private String orderShipAgency;//船代             export,import

//    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "开航日期格式错误")
    private String orderSailingDate;//开航日期            export

//    private String orderPort;//起运港                      export
//    private String orderPortCode;//起运港code              export

    private String orderEnterPort;//中转港                 export
    private String orderEnterPortCode;//中转港code         export

    private String orderDestinationPort;//目的港           export
    private String orderDestinationPortCode;//目的港code   export

    private String orderShippingCode;//货代代码         export

    private String orderBookingNumber;//电子订舱号       export

    @NotEmpty(message = "总箱量不能为空")
    @Pattern(regexp = "\\d{0,11}", message = "总箱量格式错误")
    private String orderTotalCase;//总箱量               export,import

    private String orderOwner;//箱主                     export
    private String orderOwnerCode;//箱主代码             export

    private String orderGoodOwner;//货主
    private String orderGoodOwnerCode;//货主代码

//    private String orderFreeUseDay;//免费使用日期        export,import
//
//    private String orderCheckContainer;//是否验柜         export

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

    public String getOrderShipAgency() {
        return orderShipAgency;
    }

    public void setOrderShipAgency(String orderShipAgency) {
        this.orderShipAgency = orderShipAgency;
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

    public String getOrderEnterPortCode() {
        return orderEnterPortCode;
    }

    public void setOrderEnterPortCode(String orderEnterPortCode) {
        this.orderEnterPortCode = orderEnterPortCode;
    }

    public String getOrderDestinationPort() {
        return orderDestinationPort;
    }

    public void setOrderDestinationPort(String orderDestinationPort) {
        this.orderDestinationPort = orderDestinationPort;
    }

    public String getOrderDestinationPortCode() {
        return orderDestinationPortCode;
    }

    public void setOrderDestinationPortCode(String orderDestinationPortCode) {
        this.orderDestinationPortCode = orderDestinationPortCode;
    }

    public String getOrderShippingCode() {
        return orderShippingCode;
    }

    public void setOrderShippingCode(String orderShippingCode) {
        this.orderShippingCode = orderShippingCode;
    }

    public String getOrderBookingNumber() {
        return orderBookingNumber;
    }

    public void setOrderBookingNumber(String orderBookingNumber) {
        this.orderBookingNumber = orderBookingNumber;
    }

    public String getOrderTotalCase() {
        return orderTotalCase;
    }

    public void setOrderTotalCase(String orderTotalCase) {
        this.orderTotalCase = orderTotalCase;
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
