package com.sinotrans.transport.dto.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emi on 2016/5/19.
 * 出口地址 查询 返回 Vo
 */
public class AddressExportSearchRespVo {

    private String index;//序号

    private String addressId;// id

    private String addressPlace;//交货地               export,import

    private String addressArriveTime;//到柜时间                ,import

    private String addressContacts;//工厂联系人          export,import

    private String addressTel;//联系电话                export,import

    private String addressProvince;//省                  export,import

    private String addressCity;//市                      export,import

    private String addressDistrict;//区                  export,import

    public AddressExportSearchRespVo(int index, Long addressId, String addressPlace, Date addressArriveTime, String addressContacts, String addressTel, String addressProvince, String addressCity, String addressDistrict, SimpleDateFormat sdf) {

        this();
        this.index = String.valueOf(index);
        this.addressId = String.valueOf(addressId);
        this.addressPlace = addressPlace;
        this.addressArriveTime = null == addressArriveTime ? "" : sdf.format(addressArriveTime);
        this.addressContacts = addressContacts;
        this.addressTel = addressTel;
        this.addressProvince = addressProvince;
        this.addressCity = addressCity;
        this.addressDistrict = addressDistrict;
    }

    public AddressExportSearchRespVo() {
        super();
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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
