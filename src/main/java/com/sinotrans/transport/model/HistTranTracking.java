package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.model.OperationLog;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by emi on 2016/6/3.
 * 运输轨迹表
 */
@Entity
@Table(name = "HIST_TRAN_TRACKING")
public class HistTranTracking extends BaseModel {

    private Long histId;

//    private String histGbsActId;//gbs任务单号

    private Long histTranId;//TranTruckPosition 主键

    private Long histTaskId;//任务Id   TaskOperateInfo主键

    private String histGbsContainerId;//箱Id

    private String histLongitude;//经度

    private String histLatitude;//纬度

    private Date histTime;////采集时间

    private Long recVer;//数据行版本号

    private Integer recStatus;//数据行状态(0:正常/1:删除)

    public HistTranTracking() {
        super();
        this.recVer = 0L;
        this.recStatus = 0;
        this.histTime = new Date();
    }

    public HistTranTracking(Long histTranId, Long histTaskId, String histGbsContainerId, String histLongitude, String histLatitude) {
        this();
        this.histTranId = histTranId;
        this.histTaskId = histTaskId;
        this.histGbsContainerId = histGbsContainerId;
        this.histLongitude = histLongitude;
        this.histLatitude = histLatitude;
    }

    @Id
    @Column(name = "HIST_ID", length = 19)
    @SequenceGenerator(name="SEQ_HIST_TRAN_TRACKING", sequenceName="SEQ_HIST_TRAN_TRACKING")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_HIST_TRAN_TRACKING")
    public Long getHistId() {
        return histId;
    }

    public void setHistId(Long histId) {
        this.histId = histId;
        addValidField("histId");
    }

//    @Column(name = "HIST_GBS_ACT_ID", length = 64)
//    public String getHistGbsActId() {
//        return histGbsActId;
//    }
//
//    public void setHistGbsActId(String histGbsActId) {
//        this.histGbsActId = histGbsActId;
//        addValidField("histGbsActId");
//    }

    @Column(name = "HIST_TRAN_ID", length = 19)
    public Long getHistTranId() {
        return histTranId;
    }

    public void setHistTranId(Long histTranId) {
        this.histTranId = histTranId;
        addValidField("histTranId");
    }

    @Column(name = "HIST_TASK_ID", length = 19)
    public Long getHistTaskId() {
        return histTaskId;
    }

    public void setHistTaskId(Long histTaskId) {
        this.histTaskId = histTaskId;
        addValidField("histTaskId");
    }

    @Column(name = "HIST_GBS_CONTAINER_ID", length = 19)
    public String getHistGbsContainerId() {
        return histGbsContainerId;
    }

    public void setHistGbsContainerId(String histGbsContainerId) {
        this.histGbsContainerId = histGbsContainerId;
        addValidField("histGbsContainerId");
    }

    @Column(name = "HIST_LONGITUDE", length = 12)
    public String getHistLongitude() {
        return histLongitude;
    }

    public void setHistLongitude(String histLongitude) {
        this.histLongitude = histLongitude;
        addValidField("histLongitude");
    }

    @Column(name = "HIST_LATITUDE", length = 12)
    public String getHistLatitude() {
        return histLatitude;
    }

    public void setHistLatitude(String histLatitude) {
        this.histLatitude = histLatitude;
        addValidField("histLatitude");
    }

    @Column(name = "HIST_TIME")
    public Date getHistTime() {
        return histTime;
    }

    public void setHistTime(Date histTime) {
        this.histTime = histTime;
        addValidField("histTime");
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