package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;
import com.sinotrans.transport.common.enums.TaskLogType;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by emi on 2016/6/1.
 * 任务相关的日志表
 */
@Entity
@Table(name = "TASK_ISSUED_LOG")
public class TaskIssuedLog extends BaseModel implements OperationLog {//9223372036854775807

    private Long tkilId;

    private String tkilTruckingActId;       //GBS任务id

    private String tkilIssuedUser;         //任务下发时：该字段是任务下发人，拒绝接受时：该字段是司机

    private Date tkilIssuedTime;            //操作时间

    private Integer tkilType;               //log的类型，1：下发 2：接受 3：拒绝

    private String creator;//创建人

    private Date createTime;//创建时间

    private String modifier;//最后修改人

    private Date modifyTime;//最后修改时间

    private Long recVer;//数据行版本号

    private Integer recStatus;//数据行状态(0:正常/1:删除)

    public TaskIssuedLog(String tkilTruckingActId, String tkilIssuedUser, Date tkilIssuedTime, String tkilType) {
        this();
        this.tkilTruckingActId = tkilTruckingActId;
        this.tkilIssuedUser = tkilIssuedUser;
        this.tkilIssuedTime = tkilIssuedTime;
        this.tkilType = Integer.parseInt(tkilType);
    }

    public TaskIssuedLog(String tkilTruckingActId, String driverCode, boolean ifAccept) {
        this();
        this.tkilTruckingActId = tkilTruckingActId;
        this.tkilIssuedUser = driverCode;
        this.tkilIssuedTime = new Date();
        this.tkilType = ifAccept ? TaskLogType.Accept.getValue() : TaskLogType.Refuse.getValue();
    }

    public TaskIssuedLog() {
        super();
        this.createTime = new Date();
        this.modifyTime = this.createTime;
        this.recVer = 0L;
        this.recStatus = 0;
    }

    @Id
    @Column(name = "TKIL_ID", length = 19)
    @SequenceGenerator(name="SEQ_TASK_ISSUED_LOG", sequenceName="SEQ_TASK_ISSUED_LOG")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TASK_ISSUED_LOG")
    public Long getTkilId() {
        return tkilId;
    }

    public void setTkilId(Long tkilId) {
        this.tkilId = tkilId;
        addValidField("tkilId");
    }

    @Column(name = "TKIL_TRUCKING_ACT_ID", length = 64)
    public String getTkilTruckingActId() {
        return tkilTruckingActId;
    }

    public void setTkilTruckingActId(String tkilTruckingActId) {
        this.tkilTruckingActId = tkilTruckingActId;
        addValidField("tkilTruckingActId");
    }

    @Column(name = "TKIL_ISSUED_USER", length = 64)
    public String getTkilIssuedUser() {
        return tkilIssuedUser;
    }

    public void setTkilIssuedUser(String tkilIssuedUser) {
        this.tkilIssuedUser = tkilIssuedUser;
        addValidField("tkilIssuedUser");
    }

    @Column(name = "TKIL_ISSUED_TIME")
    public Date getTkilIssuedTime() {
        return tkilIssuedTime;
    }

    public void setTkilIssuedTime(Date tkilIssuedTime) {
        this.tkilIssuedTime = tkilIssuedTime;
        addValidField("tkilIssuedTime");
    }

    @Column(name = "TKIL_TYPE", length = 2)
    public Integer getTkilType() {
        return tkilType;
    }

    public void setTkilType(Integer tkilType) {
        this.tkilType = tkilType;
        addValidField("tkilType");
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

    @Column(name = "REC_STATUS")
    public Integer getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(Integer recStatus) {
        this.recStatus = recStatus;
        addValidField("recStatus");
    }


}
