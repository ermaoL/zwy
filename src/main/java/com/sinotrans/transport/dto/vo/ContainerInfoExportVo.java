package com.sinotrans.transport.dto.vo;

/**
 * Created by emi on 2016/6/15.
 * 目前没用到
 */
public class ContainerInfoExportVo extends ContainerInfoRespVo {

    private String beginTime;//开始操作时间

    private String emptyPlace;//提空地点

    private String containerCaseNo;//箱号

    private String carGiveLisence;//送箱车牌号


    private String contSealNo;//铅封号

    private String emptyStarTime;//空箱启程时间


    private String arriveTime;//到达工厂时间

    private String leaveTime;//离开工厂时间

    private String carBackLisence;//收箱车牌号

    private String ifLocker;//是否寄柜

    private String lockerPlace;//寄柜地点


    private String inPortTime;//截进港时间

    private String inWeightTime;//进重时间

    private String fieldPosition;//场位

    private String weight;//重量

    private String orderWharf;//码头

    public ContainerInfoExportVo(String beginTime, String emptyPlace, String containerCaseNo, String carGiveLisence, String contSealNo, String emptyStarTime, String arriveTime, String leaveTime, String carBackLisence, String ifLocker, String lockerPlace, String inPortTime, String inWeightTime, String fieldPosition, String weight, String orderWharf) {
        this();
        this.beginTime = beginTime;
        this.emptyPlace = emptyPlace;
        this.containerCaseNo = containerCaseNo;
        this.carGiveLisence = carGiveLisence;
        this.contSealNo = contSealNo;
        this.emptyStarTime = emptyStarTime;
        this.arriveTime = arriveTime;
        this.leaveTime = leaveTime;
        this.carBackLisence = carBackLisence;
        this.ifLocker = ifLocker;
        this.lockerPlace = lockerPlace;
        this.inPortTime = inPortTime;
        this.inWeightTime = inWeightTime;
        this.fieldPosition = fieldPosition;
        this.weight = weight;
        this.orderWharf = orderWharf;
    }

    public ContainerInfoExportVo() {
        super();
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEmptyPlace() {
        return emptyPlace;
    }

    public void setEmptyPlace(String emptyPlace) {
        this.emptyPlace = emptyPlace;
    }

    public String getContainerCaseNo() {
        return containerCaseNo;
    }

    public void setContainerCaseNo(String containerCaseNo) {
        this.containerCaseNo = containerCaseNo;
    }

    public String getCarGiveLisence() {
        return carGiveLisence;
    }

    public void setCarGiveLisence(String carGiveLisence) {
        this.carGiveLisence = carGiveLisence;
    }

    public String getContSealNo() {
        return contSealNo;
    }

    public void setContSealNo(String contSealNo) {
        this.contSealNo = contSealNo;
    }

    public String getEmptyStarTime() {
        return emptyStarTime;
    }

    public void setEmptyStarTime(String emptyStarTime) {
        this.emptyStarTime = emptyStarTime;
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

    public String getCarBackLisence() {
        return carBackLisence;
    }

    public void setCarBackLisence(String carBackLisence) {
        this.carBackLisence = carBackLisence;
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

    public String getInPortTime() {
        return inPortTime;
    }

    public void setInPortTime(String inPortTime) {
        this.inPortTime = inPortTime;
    }

    public String getInWeightTime() {
        return inWeightTime;
    }

    public void setInWeightTime(String inWeightTime) {
        this.inWeightTime = inWeightTime;
    }

    public String getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(String fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getOrderWharf() {
        return orderWharf;
    }

    public void setOrderWharf(String orderWharf) {
        this.orderWharf = orderWharf;
    }
}
