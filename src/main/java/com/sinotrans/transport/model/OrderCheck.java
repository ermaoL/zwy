package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by emi on 2016/5/30.
 */
@Entity
@Table(name = "ORDER_CHECK")
public class OrderCheck extends BaseModel implements OperationLog {

    private Long checId;

    private Long checOrdeId;        //审核订单Id

    private Long checContId;        //箱表外键，预留字段， 目前为空

    private String checVerifier;    //审核人

    private Integer checState;      //审核状态 1:通过 0:不通过

    private String checRemark;      //审核内容

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modifyTime;

    private Long recVer;

    private Integer recStatus;

    public OrderCheck(Long checOrdeId, String checVerifier, Integer checState, String checRemark, String creator) {
        this();
        this.checOrdeId = checOrdeId;
        this.checVerifier = checVerifier;
        this.checState = checState;
        this.checRemark = checRemark;
        this.creator = creator;
    }

    public OrderCheck() {
        super();
        this.createTime = new Date();
        this.modifyTime = this.createTime;
        this.recVer = 0L;
        this.recStatus = 0;
    }

    @Id
    @Column(name = "CHEC_ID", length = 19)
    @SequenceGenerator(name="SEQ_CHECK", sequenceName="SEQ_CHECK")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CHECK")
    public Long getChecId() {
        return checId;
    }

    public void setChecId(Long checId) {
        this.checId = checId;
        addValidField("checId");
    }

    @Column(name = "CHEC_ORDE_ID", length = 19)
    public Long getChecOrdeId() {
        return checOrdeId;
    }

    public void setChecOrdeId(Long checOrdeId) {
        this.checOrdeId = checOrdeId;
        addValidField("checOrdeId");
    }

    @Column(name = "CHEC_CONT_ID", length = 19)
    public Long getChecContId() {
        return checContId;
    }

    public void setChecContId(Long checContId) {
        this.checContId = checContId;
    }


    @Column(name = "CHEC_VERIFIER", length = 64)
    public String getChecVerifier() {
        return checVerifier;
    }

    public void setChecVerifier(String checVerifier) {
        this.checVerifier = checVerifier;
        addValidField("checVerifier");
    }

    @Column(name = "CHEC_STATE", length = 1)
    public Integer getChecState() {
        return checState;
    }

    public void setChecState(Integer checState) {
        this.checState = checState;
        addValidField("checState");
    }

    @Column(name = "CHEC_REMARK", length = 256)
    public String getChecRemark() {
        return checRemark;
    }

    public void setChecRemark(String checRemark) {
        this.checRemark = checRemark;
        addValidField("checRemark");
    }

    @Column(name = "CREATOR", length = 64)
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

    @Column(name = "MODIFIER", length = 64)
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

    @Column(name = "REC_STATUS")
    public Integer getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(Integer recStatus) {
        this.recStatus = recStatus;
        addValidField("recStatus");
    }
}
