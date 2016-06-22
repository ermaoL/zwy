package com.sinotrans.transport.dto.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emi on 2016/6/21.
 */
public class PositionContainerRespVo {

    private String containerState;//箱状态(int 类型) 15:重箱移场  25:进口放重  35:进口放空  45:进口还空  10:外提空箱  20:出口放箱  30:出口回箱  40:进码头 (2,3)

    private String containerStateDesc;//箱状态描述(String 类型) 15:重箱移场  25:进口放重  35:进口放空  45:进口还空  10:外提空箱  20:出口放箱  30:出口回箱  40:进码头 (2,3)

    private String firstLongitude;//始发地经度 (2,3)     //todo:子任务的始发地是当前任务的  还是第一个任务的

    private String firstLatitude;//始发地纬度 (2,3)

    private String preLongitude;//上一个地点经度 (2,)

    private String preLatitude;//上一个地点纬度 (2,)

    private String nextLongitude;//下一个地点经度，即当前位置 (2,3)

    private String nextLatitude;//下一个地点纬度，即当前位置 (2,3)

    private String twoPointTime;//最近两个坐标点的时间差，单位：秒 (2)

    private String expectArriveDate;//预计到达日 例如：2016.06.15 (2)

    private String expectArriveTime;//预计到达时间 例如：14:30 (2)

    private String realArriveDate;//实际到达日 例如：2016.06.15 (,3)

    private String realArriveTime;//实际到达时间 例如 15:20 (,3)

    //2
    public PositionContainerRespVo(int containerState, String containerStateDesc, String firstLongitude, String firstLatitude, String preLongitude, String preLatitude, String nextLongitude, String nextLatitude, String twoPointTime, Date expectArriveDate, SimpleDateFormat sdf2) {
        this();
        this.containerState = containerState + "";
        this.containerStateDesc = containerStateDesc;
        this.firstLongitude = firstLongitude;
        this.firstLatitude = firstLatitude;
        this.preLongitude = preLongitude;
        this.preLatitude = preLatitude;
        this.nextLongitude = nextLongitude;
        this.nextLatitude = nextLatitude;
        this.twoPointTime = twoPointTime;
        if (null == expectArriveDate) {
            this.expectArriveDate = "";
            this.expectArriveTime = "";
        } else {
            String dateStr = sdf2.format(expectArriveDate);
            this.expectArriveDate = dateStr.substring(0, 10);
            this.expectArriveTime = dateStr.substring(11);
        }
    }

    //3
    public PositionContainerRespVo(int containerState, String containerStateDesc, String firstLongitude, String firstLatitude, String nextLongitude, String nextLatitude, Date realArriveDate, Date expectArriveDate, SimpleDateFormat sdf2) {
        this();
        this.containerState = containerState + "";
        this.containerStateDesc = containerStateDesc;
        this.firstLongitude = firstLongitude;
        this.firstLatitude = firstLatitude;
        this.nextLongitude = nextLongitude;
        this.nextLatitude = nextLatitude;
        if (null == realArriveDate) {
            this.realArriveDate = "";
            this.realArriveTime = "";
        } else {
            String dateStr = sdf2.format(realArriveDate);//2016-07-12 12:00
            this.realArriveDate = dateStr.substring(0, 10);
            this.realArriveTime = dateStr.substring(11);
        }
        if (null == expectArriveDate) {
            this.expectArriveDate = "";
            this.expectArriveTime = "";
        } else {
            String dateStr = sdf2.format(expectArriveDate);
            this.expectArriveDate = dateStr.substring(0, 10);
            this.expectArriveTime = dateStr.substring(11);
        }
    }

    public PositionContainerRespVo() {
        super();
    }

    public String getContainerState() {
        return containerState;
    }

    public void setContainerState(String containerState) {
        this.containerState = containerState;
    }

    public String getContainerStateDesc() {
        return containerStateDesc;
    }

    public void setContainerStateDesc(String containerStateDesc) {
        this.containerStateDesc = containerStateDesc;
    }

    public String getFirstLongitude() {
        return firstLongitude;
    }

    public void setFirstLongitude(String firstLongitude) {
        this.firstLongitude = firstLongitude;
    }

    public String getFirstLatitude() {
        return firstLatitude;
    }

    public void setFirstLatitude(String firstLatitude) {
        this.firstLatitude = firstLatitude;
    }

    public String getPreLongitude() {
        return preLongitude;
    }

    public void setPreLongitude(String preLongitude) {
        this.preLongitude = preLongitude;
    }

    public String getPreLatitude() {
        return preLatitude;
    }

    public void setPreLatitude(String preLatitude) {
        this.preLatitude = preLatitude;
    }

    public String getNextLongitude() {
        return nextLongitude;
    }

    public void setNextLongitude(String nextLongitude) {
        this.nextLongitude = nextLongitude;
    }

    public String getNextLatitude() {
        return nextLatitude;
    }

    public void setNextLatitude(String nextLatitude) {
        this.nextLatitude = nextLatitude;
    }

    public String getTwoPointTime() {
        return twoPointTime;
    }

    public void setTwoPointTime(String twoPointTime) {
        this.twoPointTime = twoPointTime;
    }

    public String getExpectArriveDate() {
        return expectArriveDate;
    }

    public void setExpectArriveDate(String expectArriveDate) {
        this.expectArriveDate = expectArriveDate;
    }

    public String getExpectArriveTime() {
        return expectArriveTime;
    }

    public void setExpectArriveTime(String expectArriveTime) {
        this.expectArriveTime = expectArriveTime;
    }

    public String getRealArriveDate() {
        return realArriveDate;
    }

    public void setRealArriveDate(String realArriveDate) {
        this.realArriveDate = realArriveDate;
    }

    public String getRealArriveTime() {
        return realArriveTime;
    }

    public void setRealArriveTime(String realArriveTime) {
        this.realArriveTime = realArriveTime;
    }
}
