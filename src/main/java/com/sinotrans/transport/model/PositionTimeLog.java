package com.sinotrans.transport.model;

import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.transport.common.enums.PositionLogType;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by emi on 2016/6/22.
 */
@Entity
@Table(name = "POSITION_TIME_LOG")
public class PositionTimeLog  extends BaseModel {

    private Long posiId;

    private Long posiContId;

    private Date posiTime;

    private String posiCode;

    private Integer posiValue;

    private String posiDetail;

    public PositionTimeLog() {
        super();
    }

    public PositionTimeLog(Long posiContId, PositionLogType logType, String desc) {
        this();
        this.posiTime = new Date();
        this.posiContId = posiContId;
        this.posiCode = logType.getCode();
        this.posiValue = logType.getValue();
        this.posiDetail = desc;
    }

    public PositionTimeLog(Long posiContId, Date posiTime, PositionLogType logType, String desc) {
        this();
        this.posiTime = posiTime;
        this.posiContId = posiContId;
        this.posiCode = logType.getCode();
        this.posiValue = logType.getValue();
        this.posiDetail = desc;
    }

    @Id
    @Column(name = "POSI_ID", length = 19)
    @SequenceGenerator(name="SEQ_POSITION_TIME_LOG", sequenceName="SEQ_POSITION_TIME_LOG")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_POSITION_TIME_LOG")
    public Long getPosiId() {
        return posiId;
    }

    public void setPosiId(Long posiId) {
        this.posiId = posiId;
    }

    @Column(name = "POSI_CONT_ID", length = 19)
    public Long getPosiContId() {
        return posiContId;
    }

    public void setPosiContId(Long posiContId) {
        this.posiContId = posiContId;
    }

    @Column(name = "POSI_TIME")
    public Date getPosiTime() {
        return posiTime;
    }

    public void setPosiTime(Date posiTime) {
        this.posiTime = posiTime;
    }

    @Column(name = "POSI_CODE", length = 64)
    public String getPosiCode() {
        return posiCode;
    }

    public void setPosiCode(String posiCode) {
        this.posiCode = posiCode;
    }

    @Column(name = "POSI_VALUE", length = 3)
    public Integer getPosiValue() {
        return posiValue;
    }

    public void setPosiValue(Integer posiValue) {
        this.posiValue = posiValue;
    }

    @Column(name = "POSI_DETAIL", length = 160)
    public String getPosiDetail() {
        return posiDetail;
    }

    public void setPosiDetail(String posiDetail) {
        this.posiDetail = posiDetail;
    }
}
