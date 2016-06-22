package com.sinotrans.transport.dto.out.vo;

import com.sinotrans.transport.common.enums.TemperUnitType;

/**
 * Created by emi on 2016/6/1.
 * 向GBS 下发订单 的 箱子相关信息 请求Vo
 */
public class ContainerGbsSendReqVo {

    private Long containerId;//oms箱主键

    private String containerCaseNo;//oms箱号

    private String containerSealNo;//铅封号

    private String containerOwnerCode;//箱主代码                       //PB_S_CONTAINER表  CONTAINER_OWNER

    private String containerType;//箱型                                //PB_S_CONTAINER表  CONTAINER_TYPE

    private String containerCartonSize;//尺寸                          //PB_S_CONTAINER表  CONTAINER_SIZE

    private Integer containerIsCheck;//是否柜检 export 0否 1是          //PB_S_CONTAINER表 CONTAINER_CHECK

    private Integer containerIsOverWeight;//是否超重 export               //PB_S_CONTAINER表 WEIGHT_KG

    private Double containerGrossWeight;//重量                        //PB_S_CONTAINER表  OVER_WEIGHT_YN

    private Integer containerRfTemp;//冻柜温度                           //PB_S_CONTAINER表 ????

    private String containerTemperUnit;//温度单位  C F                 //PB_S_CONTAINER表 TEMPER_UNIT

    private String containerDrayageDepotCode;//提箱点

    private String containerReturnDepotCode;//返箱点


    public ContainerGbsSendReqVo(Long containerId, String containerCaseNo, String containerSealNo, String containerOwnerCode, String containerType, String containerCartonSize, Integer containerIsCheck, Integer containerIsOverWeight, Double containerGrossWeight, Integer containerRfTemp, Integer containerTemperUnit, String containerDrayageDepotCode, String containerReturnDepotCode) {
        this();
        this.containerId = containerId;
        this.containerCaseNo = containerCaseNo;
        this.containerSealNo = containerSealNo;
        this.containerOwnerCode = containerOwnerCode;
        this.containerType = containerType;
        this.containerCartonSize = containerCartonSize;
        this.containerIsCheck = containerIsCheck;
        this.containerIsOverWeight = containerIsOverWeight;
        this.containerGrossWeight =containerGrossWeight;
        this.containerRfTemp = containerRfTemp;
        this.containerTemperUnit = null == containerTemperUnit ? "" : TemperUnitType.descOf(containerTemperUnit);
        this.containerDrayageDepotCode = null == containerDrayageDepotCode ? "" : containerDrayageDepotCode;
        this.containerReturnDepotCode = null == containerReturnDepotCode ? "" : containerReturnDepotCode;
    }

    public ContainerGbsSendReqVo() {
        super();
    }

    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    public String getContainerCaseNo() {
        return containerCaseNo;
    }

    public void setContainerCaseNo(String containerCaseNo) {
        this.containerCaseNo = containerCaseNo;
    }

    public String getContainerSealNo() {
        return containerSealNo;
    }

    public void setContainerSealNo(String containerSealNo) {
        this.containerSealNo = containerSealNo;
    }

    public String getContainerOwnerCode() {
        return containerOwnerCode;
    }

    public void setContainerOwnerCode(String containerOwnerCode) {
        this.containerOwnerCode = containerOwnerCode;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getContainerCartonSize() {
        return containerCartonSize;
    }

    public void setContainerCartonSize(String containerCartonSize) {
        this.containerCartonSize = containerCartonSize;
    }

    public Integer getContainerIsCheck() {
        return containerIsCheck;
    }

    public void setContainerIsCheck(Integer containerIsCheck) {
        this.containerIsCheck = containerIsCheck;
    }

    public Integer getContainerIsOverWeight() {
        return containerIsOverWeight;
    }

    public void setContainerIsOverWeight(Integer containerIsOverWeight) {
        this.containerIsOverWeight = containerIsOverWeight;
    }

    public Double getContainerGrossWeight() {
        return containerGrossWeight;
    }

    public void setContainerGrossWeight(Double containerGrossWeight) {
        this.containerGrossWeight = containerGrossWeight;
    }

    public Integer getContainerRfTemp() {
        return containerRfTemp;
    }

    public void setContainerRfTemp(Integer containerRfTemp) {
        this.containerRfTemp = containerRfTemp;
    }

    public String getContainerTemperUnit() {
        return containerTemperUnit;
    }

    public void setContainerTemperUnit(String containerTemperUnit) {
        this.containerTemperUnit = containerTemperUnit;
    }

    public String getContainerDrayageDepotCode() {
        return containerDrayageDepotCode;
    }

    public void setContainerDrayageDepotCode(String containerDrayageDepotCode) {
        this.containerDrayageDepotCode = containerDrayageDepotCode;
    }

    public String getContainerReturnDepotCode() {
        return containerReturnDepotCode;
    }

    public void setContainerReturnDepotCode(String containerReturnDepotCode) {
        this.containerReturnDepotCode = containerReturnDepotCode;
    }
}
