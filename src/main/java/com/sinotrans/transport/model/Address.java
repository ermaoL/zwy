package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;
import com.sinotrans.transport.common.enums.DomainStatus;
import com.sinotrans.transport.common.enums.DomainType;
import com.sinotrans.transport.common.exception.DateFormatException;
import com.sinotrans.transport.common.exception.DomainNotBelongException;
import com.sinotrans.transport.common.exception.RecordHasDeleteException;
import org.apache.log4j.Logger;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emi on 2016/5/12.
 */
@Entity
@Table(name = "ADDRESS")
public class Address extends BaseModel implements OperationLog {

    private Long addrId;

    private Long addrContId;

    private String addrPlace;//交货地               export,import

    private Date addrDeliveryTime;//送达时间                ,import

    private Date addrArriveTime;//到柜时间           export

    private String addrContacts;//工厂联系人          export,import

    private String addrTel;//联系电话                export,import

    private String addrProvince;//省                  export,import

    private String addrCity;//市                      export,import

    private String addrDistrict;//区                  export,import

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modifyTime;

    private Long recVer;//数据行版本号

    private Integer recStatus;// false:正常   true：被软删除

    public Address() {
        super();
        this.createTime = new Date();
        this.modifyTime = this.createTime;
        this.recVer = 0L;
        this.recStatus = 0;
    }

    //进口
    public Address(Long addrContId, String addrPlace, String addrDeliveryTime, String addrContacts, String addrTel, String addrProvince, String addrCity, String addrDistrict, String creator, SimpleDateFormat sdf) {
        this();
        this.addrContId = addrContId;
        this.addrPlace = addrPlace;

        try {
            this.addrDeliveryTime = StringUtils.isEmpty(addrDeliveryTime) ? null : sdf.parse(addrDeliveryTime);
        } catch (ParseException e) {
            throw new DateFormatException(sdf, Logger.getLogger(Address.class));
        }
        this.addrContacts = addrContacts;
        this.addrTel = addrTel;
        this.addrProvince = addrProvince;
        this.addrCity = addrCity;
        this.addrDistrict = addrDistrict;
        this.creator = creator;
    }

    //出口
    public Address(SimpleDateFormat sdf, Long addrContId, String addrPlace, String addrArriveTime, String addrContacts, String addrTel, String addrProvince, String addrCity, String addrDistrict, String creator) {
        this();
        this.addrContId = addrContId;
        this.addrPlace = addrPlace;
        try {
            this.addrArriveTime = StringUtils.isEmpty(addrArriveTime) ? null : sdf.parse(addrArriveTime);
        } catch (ParseException e) {
            throw new DateFormatException(sdf, Logger.getLogger(Address.class));
        }
        this.addrContacts = addrContacts;
        this.addrTel = addrTel;
        this.addrProvince = addrProvince;
        this.addrCity = addrCity;
        this.addrDistrict = addrDistrict;
        this.creator = creator;
    }

    @Id
    @Column(name = "ADDR_ID", length = 19)
    @SequenceGenerator(name="SEQ_ADDRESS", sequenceName="SEQ_ADDRESS")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ADDRESS")
    public Long getAddrId() {
        return addrId;
    }

    public void setAddrId(Long addrId) {
        this.addrId = addrId;
        addValidField("addrId");
    }

    @Column(name = "ADDR_CONT_ID", length = 19)
    public Long getAddrContId() {
        return addrContId;
    }

    public void setAddrContId(Long addrContId) {
        this.addrContId = addrContId;
        addValidField("addrContId");
    }

    @Column(name = "ADDR_PLACE", length = 256)
    public String getAddrPlace() {
        return addrPlace;
    }

    public void setAddrPlace(String addrPlace) {
        this.addrPlace = addrPlace;
        addValidField("addrPlace");
    }

    @Column(name = "ADDR_DELIVERY_TIME")
    public Date getAddrDeliveryTime() {
        return addrDeliveryTime;
    }

    public void setAddrDeliveryTime(Date addrDeliveryTime) {
        this.addrDeliveryTime = addrDeliveryTime;
        addValidField("addrDeliveryTime");
    }

    @Column(name = "ADDR_ARRIVE_TIME")
    public Date getAddrArriveTime() {
        return addrArriveTime;
    }

    public void setAddrArriveTime(Date addrArriveTime) {
        this.addrArriveTime = addrArriveTime;
        addValidField("addrArriveTime");
    }

    @Column(name = "ADDR_CONTACTS", length = 32)
    public String getAddrContacts() {
        return addrContacts;
    }

    public void setAddrContacts(String addrContacts) {
        this.addrContacts = addrContacts;
        addValidField("addrContacts");
    }

    @Column(name = "ADDR_TEL", length = 32)
    public String getAddrTel() {
        return addrTel;
    }

    public void setAddrTel(String addrTel) {
        this.addrTel = addrTel;
        addValidField("addrTel");
    }

    @Column(name = "ADDR_PROVINCE", length = 16)
    public String getAddrProvince() {
        return addrProvince;
    }

    public void setAddrProvince(String addrProvince) {
        this.addrProvince = addrProvince;
        addValidField("addrProvince");
    }

    @Column(name = "ADDR_CITY", length = 16)
    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
        addValidField("addrCity");
    }

    @Column(name = "ADDR_DISTRICT", length = 16)
    public String getAddrDistrict() {
        return addrDistrict;
    }

    public void setAddrDistrict(String addrDistrict) {
        this.addrDistrict = addrDistrict;
        addValidField("addrDistrict");
    }

    @Column(name = "CREATOR", length = 32)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
        addValidField("creator");
    }

    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        addValidField("createTime");
    }

    @Column(name = "MODIFIER", length = 32)
    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
        addValidField("modifier");
    }

    @Column(name = "MODIFY_TIME")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        addValidField("modifyTime");
    }

    @Column(name = "REC_VER", length = 19)
    public Long getRecVer() {
        return recVer;
    }

    public void setRecVer(Long recVer) {
        this.recVer = recVer;
        addValidField("recVer");
    }

    @Column(name = "REC_STATUS", length = 1)
    public Integer getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(Integer recStatus) {
        this.recStatus = recStatus;
        addValidField("recStatus");
    }

    public void checkDelete(Logger logger) {
        if (DomainStatus.Delete.getValue() == recStatus.intValue()) {//todo: 其他地方需要陆陆续续判断是否被删除
            throw new RecordHasDeleteException(DomainType.Order, this.addrId, logger);
        }
    }

//    public void checkBelongCreator(Long userId, Logger logger) {
//        if (userId.longValue() != Long.parseLong(this.creator)) {
//            throw new DomainNotBelongException(DomainType.Address, this.addrId, this.creator, userId, logger);
//        }
//    }
}
