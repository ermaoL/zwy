package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;
import com.sinotrans.transport.common.enums.TaskStateType;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by emi on 2016/6/3.
 */
@Entity
@Table(name = "TASK_OPERATE_INFO")
public class TaskOperateInfo extends BaseModel implements OperationLog {

    private Long tkoiId;

    private String tkoiGbsTruckingActId;//gbs任务id，任务单号

    private String tkoiGbsDriverCode;//gbs司机代码，相当于gbs司机外键, 同Oms-Driver-drivGbsCode

    private String tkoiGbsContainerId;//GBS箱主键

    private Integer tkoiCompany;//1：裕雄  2：裕国

    private Integer tkoiTaskType;//gbs任务类型

    private Date tkoiActionTime;//下发时间 暂时对应 TaskRemoteFetchRequest-issuedTime     todo:??

    private String tkoiRegionFrom;//始发地

    private String tkoiRegionTo;//目的地

    private String tkoiPostage;//运费 表：tf_trucking_act 字段：ACTION_INCOME      todo:??

    private Date tkoiStartTime;//接受接单的时间
    private Date tkoiEndTime;//上传图片结束任务时的时间

    private Integer tkoiState;//1：待确认  2：已接受  3：已拒绝  4：已完成

    private Integer tkoiOrderType;//1:进口 2： 出口

    private Date tkoiLoadingTime;                   //todo: 这四个时间怎么来的？
    private Date tkoiLoadingEndTime;

    private Date tkoiUnloadingTime;
    private Date tkoiUnloadingEndTime;

    private String tkoiSealNo;//oms铅封号  oms-container-contSealNo

    private Date tkoiArriveLeaveTime1;//到达（进口放重）或离开（进口放空）时间
    private Date tkoiArriveLeaveTime2;//到达（进口放重）或离开（进口放空）时间
    private Date tkoiArriveLeaveTime3;//到达（进口放重）或离开（进口放空）时间
    private Date tkoiArriveLeaveTime4;//到达（进口放重）或离开（进口放空）时间
    private Date tkoiArriveLeaveTime5;//到达（进口放重）或离开（进口放空）时间

    private String tkoiUploadAddr1;
    private String tkoiUploadAddr2;
    private String tkoiUploadAddr3;
    private String tkoiUploadAddr4;
    private String tkoiUploadAddr5;

    private String creator;//创建人

    private Date createTime;//创建时间

    private String modifier;//最后修改人

    private Date modifyTime;//最后修改时间

    private Long recVer;//数据行版本号

    private Integer recStatus;//数据行状态(0:正常/1:删除)

    public TaskOperateInfo() {
        super();
        this.createTime = new Date();
        this.modifyTime = this.createTime;
        this.recVer = 0L;
        this.recStatus = 0;
        this.tkoiState = TaskStateType.WaitAccept.getValue();
    }

    public TaskOperateInfo(String truckingActId, String driverCode, String containerId, int tkoiCompany, String taskType, Date issuedTime, String regionFrom, String regionTo, String actionTncome, Integer orderType) {
        this();
        this.tkoiGbsTruckingActId = truckingActId;
        this.tkoiGbsDriverCode = driverCode;
        this.tkoiGbsContainerId = containerId;
        this.tkoiCompany = tkoiCompany;
        this.tkoiTaskType = Integer.parseInt(taskType);
        this.tkoiActionTime = issuedTime;
        this.tkoiRegionFrom = StringUtils.isEmpty(regionFrom) ? "" : regionFrom;
        this.tkoiRegionTo = StringUtils.isEmpty(regionTo) ? "" : regionTo;
        this.tkoiPostage = actionTncome;
        this.tkoiOrderType = orderType;
    }

    @Id
    @Column(name = "TKOI_ID", length = 19)
    @SequenceGenerator(name="SEQ_TASK_OPERATE_INFO", sequenceName="SEQ_TASK_OPERATE_INFO")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_TASK_OPERATE_INFO")
    public Long getTkoiId() {
        return tkoiId;
    }

    public void setTkoiId(Long tkoiId) {
        this.tkoiId = tkoiId;
        addValidField("tkoiId");
    }

    @Column(name = "TKOI_GBS_TRUCKING_ACT_ID", length = 64)
    public String getTkoiGbsTruckingActId() {
        return tkoiGbsTruckingActId;
    }

    public void setTkoiGbsTruckingActId(String tkoiGbsTruckingActId) {
        this.tkoiGbsTruckingActId = tkoiGbsTruckingActId;
        addValidField("tkoiGbsTruckingActId");
    }

    @Column(name = "TKOI_GBS_DRIVER_CODE", length = 64)
    public String getTkoiGbsDriverCode() {
        return tkoiGbsDriverCode;
    }

    public void setTkoiGbsDriverCode(String tkoiGbsDriverCode) {
        this.tkoiGbsDriverCode = tkoiGbsDriverCode;
        addValidField("tkoiGbsDriverCode");
    }

    @Column(name = "TKOI_GBS_CONTAINER_ID", length = 64)
    public String getTkoiGbsContainerId() {
        return tkoiGbsContainerId;
    }

    public void setTkoiGbsContainerId(String tkoiGbsContainerId) {
        this.tkoiGbsContainerId = tkoiGbsContainerId;
        addValidField("tkoiGbsContainerId");
    }

    @Column(name = "TKOI_COMPANY", length = 1)
    public Integer getTkoiCompany() {
        return tkoiCompany;
    }

    public void setTkoiCompany(Integer tkoiCompany) {
        this.tkoiCompany = tkoiCompany;
        addValidField("tkoiCompany");
    }

    @Column(name = "TKOI_TASK_TYPE", length = 2)
    public Integer getTkoiTaskType() {
        return tkoiTaskType;
    }

    public void setTkoiTaskType(Integer tkoiTaskType) {
        this.tkoiTaskType = tkoiTaskType;
        addValidField("tkoiTaskType");
    }

    @Column(name = "TKOI_ACTION_TIME")
    public Date getTkoiActionTime() {
        return tkoiActionTime;
    }

    public void setTkoiActionTime(Date tkoiActionTime) {
        this.tkoiActionTime = tkoiActionTime;
        addValidField("tkoiActionTime");
    }

    @Column(name = "TKOI_REGION_FROM", length = 128)
    public String getTkoiRegionFrom() {
        return tkoiRegionFrom;
    }

    public void setTkoiRegionFrom(String tkoiRegionFrom) {
        this.tkoiRegionFrom = tkoiRegionFrom;
        addValidField("tkoiRegionFrom");
    }

    @Column(name = "TKOI_REGION_TO", length = 128)
    public String getTkoiRegionTo() {
        return tkoiRegionTo;
    }

    public void setTkoiRegionTo(String tkoiRegionTo) {
        this.tkoiRegionTo = tkoiRegionTo;
        addValidField("tkoiRegionTo");
    }

    @Column(name = "TKOI_POSTAGE")//, precision = 12, scale = 2
    public String getTkoiPostage() {
        return tkoiPostage;
    }

    public void setTkoiPostage(String tkoiPostage) {
        this.tkoiPostage = tkoiPostage;
        addValidField("tkoiPostage");
    }

    @Column(name = "TKOI_STATE", length = 1)
    public Integer getTkoiState() {
        return tkoiState;
    }

    public void setTkoiState(Integer tkoiState) {
        this.tkoiState = tkoiState;
        addValidField("tkoiState");
    }

    @Column(name = "TKOI_ORDER_TYPE", length = 1)
    public Integer getTkoiOrderType() {
        return tkoiOrderType;
    }

    public void setTkoiOrderType(Integer tkoiOrderType) {
        this.tkoiOrderType = tkoiOrderType;
        addValidField("tkoiOrderType");
    }

    @Column(name = "TKOI_START_TIME")
    public Date getTkoiStartTime() {
        return tkoiStartTime;
    }

    public void setTkoiStartTime(Date tkoiStartTime) {
        this.tkoiStartTime = tkoiStartTime;
        addValidField("tkoiStartTime");
    }

    @Column(name = "TKOI_END_TIME")
    public Date getTkoiEndTime() {
        return tkoiEndTime;
    }

    public void setTkoiEndTime(Date tkoiEndTime) {
        this.tkoiEndTime = tkoiEndTime;
        addValidField("tkoiEndTime");
    }


    @Column(name = "TKOI_LOADING_TIME")
    public Date getTkoiLoadingTime() {
        return tkoiLoadingTime;
    }

    public void setTkoiLoadingTime(Date tkoiLoadingTime) {
        this.tkoiLoadingTime = tkoiLoadingTime;
        addValidField("tkoiLoadingTime");
    }

    @Column(name = "TKOI_LOADING_END_TME")
    public Date getTkoiLoadingEndTime() {
        return tkoiLoadingEndTime;
    }

    public void setTkoiLoadingEndTime(Date tkoiLoadingEndTime) {
        this.tkoiLoadingEndTime = tkoiLoadingEndTime;
        addValidField("tkoiLoadingEndTime");
    }

    @Column(name = "TKOI_UNLOADING_TIME")
    public Date getTkoiUnloadingTime() {
        return tkoiUnloadingTime;
    }

    public void setTkoiUnloadingTime(Date tkoiUnloadingTime) {
        this.tkoiUnloadingTime = tkoiUnloadingTime;
        addValidField("tkoiUnloadingTime");
    }

    @Column(name = "TKOI_UNLOADING_END_TIME")
    public Date getTkoiUnloadingEndTime() {
        return tkoiUnloadingEndTime;
    }

    public void setTkoiUnloadingEndTime(Date tkoiUnloadingEndTime) {
        this.tkoiUnloadingEndTime = tkoiUnloadingEndTime;
        addValidField("tkoiUnloadingEndTime");
    }

    @Column(name = "TKOI_SEAL_NO", length = 64)
    public String getTkoiSealNo() {
        return tkoiSealNo;
    }

    public void setTkoiSealNo(String tkoiSealNo) {
        this.tkoiSealNo = tkoiSealNo;
        addValidField("tkoiSealNo");
    }

    @Column(name = "TKOI_ARRIVE_LEAVE_TIME1")
    public Date getTkoiArriveLeaveTime1() {
        return tkoiArriveLeaveTime1;
    }

    public void setTkoiArriveLeaveTime1(Date tkoiArriveLeaveTime1) {
        this.tkoiArriveLeaveTime1 = tkoiArriveLeaveTime1;
        addValidField("tkoiArriveLeaveTime1");
    }

    @Column(name = "TKOI_ARRIVE_LEAVE_TIME2")
    public Date getTkoiArriveLeaveTime2() {
        return tkoiArriveLeaveTime2;
    }

    public void setTkoiArriveLeaveTime2(Date tkoiArriveLeaveTime2) {
        this.tkoiArriveLeaveTime2 = tkoiArriveLeaveTime2;
        addValidField("tkoiArriveLeaveTime2");
    }

    @Column(name = "TKOI_ARRIVE_LEAVE_TIME3")
    public Date getTkoiArriveLeaveTime3() {
        return tkoiArriveLeaveTime3;
    }

    public void setTkoiArriveLeaveTime3(Date tkoiArriveLeaveTime3) {
        this.tkoiArriveLeaveTime3 = tkoiArriveLeaveTime3;
        addValidField("tkoiArriveLeaveTime3");
    }

    @Column(name = "TKOI_ARRIVE_LEAVE_TIME4")
    public Date getTkoiArriveLeaveTime4() {
        return tkoiArriveLeaveTime4;
    }

    public void setTkoiArriveLeaveTime4(Date tkoiArriveLeaveTime4) {
        this.tkoiArriveLeaveTime4 = tkoiArriveLeaveTime4;
        addValidField("tkoiArriveLeaveTime4");
    }

    @Column(name = "TKOI_ARRIVE_LEAVE_TIME5")
    public Date getTkoiArriveLeaveTime5() {
        return tkoiArriveLeaveTime5;
    }

    public void setTkoiArriveLeaveTime5(Date tkoiArriveLeaveTime5) {
        this.tkoiArriveLeaveTime5 = tkoiArriveLeaveTime5;
        addValidField("tkoiArriveLeaveTime5");
    }

    @Column(name = "TKOI_UPLOAD_ADDR1", length = 500)
    public String getTkoiUploadAddr1() {
        return tkoiUploadAddr1;
    }

    public void setTkoiUploadAddr1(String tkoiUploadAddr1) {
        this.tkoiUploadAddr1 = tkoiUploadAddr1;
        addValidField("tkoiUploadAddr1");
    }

    @Column(name = "TKOI_UPLOAD_ADDR2")
    public String getTkoiUploadAddr2() {
        return tkoiUploadAddr2;
    }

    public void setTkoiUploadAddr2(String tkoiUploadAddr2) {
        this.tkoiUploadAddr2 = tkoiUploadAddr2;
        addValidField("tkoiUploadAddr2");
    }

    @Column(name = "TKOI_UPLOAD_ADDR3")
    public String getTkoiUploadAddr3() {
        return tkoiUploadAddr3;
    }

    public void setTkoiUploadAddr3(String tkoiUploadAddr3) {
        this.tkoiUploadAddr3 = tkoiUploadAddr3;
        addValidField("tkoiUploadAddr3");
    }

    @Column(name = "TKOI_UPLOAD_ADDR4")
    public String getTkoiUploadAddr4() {
        return tkoiUploadAddr4;
    }

    public void setTkoiUploadAddr4(String tkoiUploadAddr4) {
        this.tkoiUploadAddr4 = tkoiUploadAddr4;
        addValidField("tkoiUploadAddr4");
    }

    @Column(name = "TKOI_UPLOAD_ADDR5")
    public String getTkoiUploadAddr5() {
        return tkoiUploadAddr5;
    }

    public void setTkoiUploadAddr5(String tkoiUploadAddr5) {
        this.tkoiUploadAddr5 = tkoiUploadAddr5;
        addValidField("tkoiUploadAddr5");
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
