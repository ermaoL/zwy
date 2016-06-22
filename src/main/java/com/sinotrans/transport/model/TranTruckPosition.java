package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;
import com.sinotrans.transport.common.enums.TranStateType;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by emi on 2016/6/3.
 * 运输跟踪实时位置表
 */
@Entity
@Table(name = "TRAN_TRUCK_POSITION")
public class TranTruckPosition extends BaseModel implements OperationLog {

    private Long tranId;

//    private String tranGbsActId;//gbs任务单号

    private Long tranTaskId;//oms任务表主键         oms-TaskOperateInfo-tkoiId

    private Integer tranState;// 1:执行中  2:结束

//    private Long tranDriverId;//oms司机主键         oms-Driver-drivId

    private String tranGbsDriverCode;//gbs司机主键

//    private String tranGbsOrderDocCode;//gbs订单主键

    private Long transOrderId;//oms订单主键         oms-Orderd-ordeId

//    private String tranGbsContainerId;//gbs箱表主键

    private Long tranContainerId;//oms箱表主键      oms-Container-contId

    private String tranUserCompanyCode;//公司code   oms-user-userCompanyCode

    private Integer tranOrderType;//1: 进口  2:出口  oms-order-orderType

    private String tranBillNo;//oms提单号           oms-Orderd-ordeBillNo

    private String tranLongitude;//经度

    private String tranLatitude;//纬度

    private Date tranTime;//采集时间

    private String creator;//创建人

    private Date createTime;//创建时间

    private String modifier;//最后修改人

    private Date modifyTime;//最后修改时间

    private Long recVer;//数据行版本号

    private Integer recStatus;//数据行状态(0:正常/1:删除)

    public TranTruckPosition() {
        super();
        this.recVer = 0L;
        this.recStatus = 0;
        this.tranState = TranStateType.Running.getValue();
    }

    public TranTruckPosition(Long tranTaskId, String tranGbsDriverCode, Long transOrderId, Long tranContainerId, String tranUserCompanyCode, Integer tranOrderType, String tranBillNo, String tranLongitude, String tranLatitude) {
        this();
        this.tranTaskId = tranTaskId;
        this.tranGbsDriverCode = tranGbsDriverCode;
        this.transOrderId = transOrderId;
        this.tranContainerId = tranContainerId;
        this.tranUserCompanyCode = tranUserCompanyCode;
        this.tranOrderType = tranOrderType;
        this.tranBillNo = tranBillNo;
        this.tranLongitude = tranLongitude;
        this.tranLatitude = tranLatitude;
        this.tranTime = new Date();
    }

    @Id
    @Column(name = "TRAN_ID", length = 19)
    @SequenceGenerator(name="SEQ_TRAN_TRUCK_POSITION", sequenceName="SEQ_TRAN_TRUCK_POSITION")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_TRAN_TRUCK_POSITION")
    public Long getTranId() {
        return tranId;
    }

    public void setTranId(Long tranId) {
        this.tranId = tranId;
        addValidField("tranId");
    }


    //    @Column(name = "TRAN_GBS_ACT_ID", length = 64)
//    public String getTranGbsActId() {
//        return tranGbsActId;
//    }
//
//    public void setTranGbsActId(String tranGbsActId) {
//        this.tranGbsActId = tranGbsActId;
//        addValidField("tranGbsActId");
//    }

    @Column(name = "TRAN_TASK_ID", length = 19)
    public Long getTranTaskId() {
        return tranTaskId;
    }

    public void setTranTaskId(Long tranTaskId) {
        this.tranTaskId = tranTaskId;
        addValidField("tranTaskId");
    }

    @Column(name = "TRAN_STATE", length = 1)
    public Integer getTranState() {
        return tranState;
    }

    public void setTranState(Integer tranState) {
        this.tranState = tranState;
        addValidField("tranState");
    }

//    @Column(name = "TRAN_DRIVER_ID", length = 19)
//    public Long getTranDriverId() {
//        return tranDriverId;
//    }
//
//    public void setTranDriverId(Long tranDriverId) {
//        this.tranDriverId = tranDriverId;
//        addValidField("tranDriverId");
//    }

    @Column(name = "TRAN_GBS_DRIVER_CODE", length = 64)
    public String getTranGbsDriverCode() {
        return tranGbsDriverCode;
    }

    public void setTranGbsDriverCode(String tranGbsDriverCode) {
        this.tranGbsDriverCode = tranGbsDriverCode;
        addValidField("tranGbsDriverCode");
    }


//    @Column(name = "TRAN_GBS_ORDER_DOC_CODE", length = 64)
//    public String getTranGbsOrderDocCode() {
//        return tranGbsOrderDocCode;
//    }
//
//    public void setTranGbsOrderDocCode(String tranGbsOrderDocCode) {
//        this.tranGbsOrderDocCode = tranGbsOrderDocCode;
//        addValidField("tranGbsOrderDocCode");
//    }

    @Column(name = "TRAN_ORDER_ID", length = 19)
    public Long getTransOrderId() {
        return transOrderId;
    }

    public void setTransOrderId(Long transOrderId) {
        this.transOrderId = transOrderId;
        addValidField("transOrderId");
    }


//    @Column(name = "TRAN_GBS_CONTAINER_ID", length = 64)
//    public String getTranGbsContainerId() {
//        return tranGbsContainerId;
//    }
//
//    public void setTranGbsContainerId(String tranGbsContainerId) {
//        this.tranGbsContainerId = tranGbsContainerId;
//        addValidField("tranGbsContainerId");
//    }


    @Column(name = "TRAN_CONTAINER_ID", length = 19)
    public Long getTranContainerId() {
        return tranContainerId;
    }

    public void setTranContainerId(Long tranContainerId) {
        this.tranContainerId = tranContainerId;
        addValidField("tranContainerId");
    }

    @Column(name = "TRAN_USER_COMPANY_CODE", length = 32)
    public String getTranUserCompanyCode() {
        return tranUserCompanyCode;
    }

    public void setTranUserCompanyCode(String tranUserCompanyCode) {
        this.tranUserCompanyCode = tranUserCompanyCode;
        addValidField("tranUserCompanyCode");
    }

    @Column(name = "TRAN_ORDER_TYPE", length = 1)
    public Integer getTranOrderType() {
        return tranOrderType;
    }

    public void setTranOrderType(Integer tranOrderType) {
        this.tranOrderType = tranOrderType;
        addValidField("tranOrderType");
    }

    @Column(name = "TRAN_BILL_NO", length = 64)
    public String getTranBillNo() {
        return tranBillNo;
    }

    public void setTranBillNo(String tranBillNo) {
        this.tranBillNo = tranBillNo;
        addValidField("tranBillNo");
    }

    @Column(name = "TRAN_LONGITUDE", length = 12)
    public String getTranLongitude() {
        return tranLongitude;
    }

    public void setTranLongitude(String tranLongitude) {
        this.tranLongitude = tranLongitude;
        addValidField("tranLongitude");
    }

    @Column(name = "TRAN_LATITUDE", length = 12)
    public String getTranLatitude() {
        return tranLatitude;
    }

    public void setTranLatitude(String tranLatitude) {
        this.tranLatitude = tranLatitude;
        addValidField("tranLatitude");
    }

    @Column(name = "TRAN_TIME")
    public Date getTranTime() {
        return tranTime;
    }

    public void setTranTime(Date tranTime) {
        this.tranTime = tranTime;
        addValidField("tranTime");
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

    public void definePosition(String longitude, String latitude) {
        setTranLongitude(longitude);
        setTranLatitude(latitude);
        setTranTime(new Date());
    }
}
