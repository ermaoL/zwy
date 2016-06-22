package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;
import com.sinotrans.transport.common.enums.CompanyType;
import com.sinotrans.transport.common.enums.DomainStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by emi on 2016/5/12.
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer extends BaseModel implements OperationLog {

    private Long custId;

    private Long custUserId;

    private Integer custType;

    private String custCode;

    private String custName;

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modifyTime;

    private Long recVer;//数据行版本号

    private Integer recStatus;// false:正常   true：被软删除

    public Customer() {
        super();
        this.createTime = new Date();
        this.modifyTime = this.createTime;
        this.recVer = 0L;
        this.recStatus = DomainStatus.Normal.getValue();
    }

    public Customer(Long custUserId, CompanyType companyType) {
        this();
        this.custUserId = custUserId;
        this.custType = companyType.getValue();
        this.custCode = companyType.getCode();
        this.custName = companyType.getDesc();
    }

    @Id
    @Column(name = "CUST_ID", length = 19)
    @SequenceGenerator(name="SEQ_CUSTOMER", sequenceName="SEQ_CUSTOMER")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CUSTOMER")
    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
        addValidField("custId");
    }

    @Column(name = "CUST_USER_ID", length = 19)
    public Long getCustUserId() {
        return custUserId;
    }

    public void setCustUserId(Long custUserId) {
        this.custUserId = custUserId;
        addValidField("custUserId");
    }

    @Column(name = "CUST_TYPE", length = 1)
    public Integer getCustType() {
        return custType;
    }

    public void setCustType(Integer custType) {
        this.custType = custType;
    }

    @Column(name = "CUST_CODE", length = 64)
    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
        addValidField("custCode");
    }

    @Column(name = "CUST_NAME", length = 64)
    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
        addValidField("custName");
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
}
