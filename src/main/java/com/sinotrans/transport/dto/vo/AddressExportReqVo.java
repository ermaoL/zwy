package com.sinotrans.transport.dto.vo;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/5/19.
 * 出口地址 创建修改 请求 Vo
 */
public class AddressExportReqVo {

    @Pattern(regexp = "\\d*", message = "Id格式错误")
    private String addressId;//id，  注意：只有在更新的状况下才使用该字段

    private String addressPlace;//交货地               export,import

//    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}", message = "抵港日期格式错误")
    private String addressArriveTime;//到柜时间                ,import

    private String addressContacts;//工厂联系人          export,import

    private String addressTel;//联系电话                export,import

    private String addressProvince;//省                  export,import

    private String addressCity;//市                      export,import

    private String addressDistrict;//区                  export,import

    public AddressExportReqVo() {
        super();
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressPlace() {
        return addressPlace;
    }

    public void setAddressPlace(String addressPlace) {
        this.addressPlace = addressPlace;
    }

    public String getAddressArriveTime() {
        return addressArriveTime;
    }

    public void setAddressArriveTime(String addressArriveTime) {
        this.addressArriveTime = addressArriveTime;
    }

    public String getAddressContacts() {
        return addressContacts;
    }

    public void setAddressContacts(String addressContacts) {
        this.addressContacts = addressContacts;
    }

    public String getAddressTel() {
        return addressTel;
    }

    public void setAddressTel(String addressTel) {
        this.addressTel = addressTel;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }
}
