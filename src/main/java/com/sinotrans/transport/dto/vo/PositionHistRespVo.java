package com.sinotrans.transport.dto.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emi on 2016/6/21.
 * 单票跟踪 运行轨迹历史
 */
public class PositionHistRespVo {

    private String longitude;//经度

    private String latitude;//纬度

    private String time;//当前采集时间

    public PositionHistRespVo(String longitude, String latitude, Date time, SimpleDateFormat sdf2) {
        this();
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = null == time ? "" : sdf2.format(time);
    }

    public PositionHistRespVo() {
        super();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
