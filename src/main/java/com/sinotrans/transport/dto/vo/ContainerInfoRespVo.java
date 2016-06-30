package com.sinotrans.transport.dto.vo;

/**
 * Created by emi on 2016/6/15.
 */
public class ContainerInfoRespVo {

    private String time;

    private String desc;

    public ContainerInfoRespVo() {
        super();
    }

    public ContainerInfoRespVo(String time, String desc) {
        this();
        this.time = time;
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
