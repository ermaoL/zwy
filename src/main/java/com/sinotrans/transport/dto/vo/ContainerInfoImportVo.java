package com.sinotrans.transport.dto.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by emi on 2016/6/15.
 */
public class ContainerInfoImportVo implements ContainerInfoVo {

    //15重箱移场
    private String orderWharf;//码头  来源gbs todo:where

    private String beginTime;//开始操作时间

    private String outTime;//出闸时间  todo:是不是重箱移场的结束时间？

    private String ifLocker;//是否寄柜 todo:待定

    private String lockerPlace;//寄柜地点 todo:待定


    //25进口放重
    private String setOutTime;//启程时间 todo: 是不是进口放重的开始时间？

    private String arriveTime;//到达工厂1~n时间, 以横线“-”隔开


    //35进口放空
    private String leaveTime;//离开工厂1~n时间, 以横线“-”隔开


    //进口还空
    private String giveBackPlace;//还空时间

    private String giveBackTime;//还空地点

    private String containerStatus;//箱况

    private String ifOverDue;//是否超期

    public ContainerInfoImportVo() {
        super();
    }

    public ContainerInfoImportVo(String orderWharf, String beginTime, String outTime, String ifLocker, String lockerPlace, String setOutTime, String arriveTime, String leaveTime, String giveBackPlace, String giveBackTime, String containerStatus, String ifOverDue) {
        this();
        this.orderWharf = orderWharf;
        this.beginTime = beginTime;
        this.outTime = outTime;
        this.ifLocker = ifLocker;
        this.lockerPlace = lockerPlace;
        this.setOutTime = setOutTime;
        this.arriveTime = arriveTime;
        this.leaveTime = leaveTime;
        this.giveBackPlace = giveBackPlace;
        this.giveBackTime = giveBackTime;
        this.containerStatus = containerStatus;
        this.ifOverDue = ifOverDue;
    }

    public String getOrderWharf() {
        return orderWharf;
    }

    public void setOrderWharf(String orderWharf) {
        this.orderWharf = orderWharf;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getIfLocker() {
        return ifLocker;
    }

    public void setIfLocker(String ifLocker) {
        this.ifLocker = ifLocker;
    }

    public String getLockerPlace() {
        return lockerPlace;
    }

    public void setLockerPlace(String lockerPlace) {
        this.lockerPlace = lockerPlace;
    }

    public String getSetOutTime() {
        return setOutTime;
    }

    public void setSetOutTime(String setOutTime) {
        this.setOutTime = setOutTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getGiveBackPlace() {
        return giveBackPlace;
    }

    public void setGiveBackPlace(String giveBackPlace) {
        this.giveBackPlace = giveBackPlace;
    }

    public String getGiveBackTime() {
        return giveBackTime;
    }

    public void setGiveBackTime(String giveBackTime) {
        this.giveBackTime = giveBackTime;
    }

    public String getContainerStatus() {
        return containerStatus;
    }

    public void setContainerStatus(String containerStatus) {
        this.containerStatus = containerStatus;
    }

    public String getIfOverDue() {
        return ifOverDue;
    }

    public void setIfOverDue(String ifOverDue) {
        this.ifOverDue = ifOverDue;
    }

    public void defineImport15(String orderWharf, Date beginTime, Date outTime, String ifLocker, String lockerPlace, SimpleDateFormat sdf2) {
        this.orderWharf = orderWharf;
        this.beginTime = null == beginTime ? "" : sdf2.format(beginTime);
        this.outTime = null == outTime ? "" : sdf2.format(outTime);
        this.ifLocker = ifLocker;
        this.lockerPlace = lockerPlace;
    }

    public void defineImport25(Date tkoiStartTime, Date tkoiArriveLeaveTime1, Date tkoiArriveLeaveTime2, Date tkoiArriveLeaveTime3, Date tkoiArriveLeaveTime4, Date tkoiArriveLeaveTime5, SimpleDateFormat sdf2) {
        this.setOutTime  = null == tkoiStartTime ? "/" : sdf2.format(tkoiStartTime);
        this.arriveTime = "";//todo:现在为空   写死
    }

    public void defineImport35(Date tkoiArriveLeaveTime1, Date tkoiArriveLeaveTime2, Date tkoiArriveLeaveTime3, Date tkoiArriveLeaveTime4, Date tkoiArriveLeaveTime5, SimpleDateFormat sdf2) {
        this.leaveTime = "";//todo:现在为空   写死
    }

    public void defineImport45(String giveBackPlace, Date giveBackTime, String containerStatus, String ifOverDue, SimpleDateFormat sdf2) {
        this.giveBackPlace = giveBackPlace;
        this.giveBackTime = null == giveBackTime ? "" : sdf2.format(giveBackTime);
        this.containerStatus = containerStatus;
        this.ifOverDue = ifOverDue;
    }
}
