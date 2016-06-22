package com.sinotrans.transport.dto.vo;

import com.sinotrans.transport.common.enums.ContainerStateType;

/**
 * Created by emi on 2016/5/17.
 * 进口容器 查询 返回 Vo
 */
public class ContainerImportSearchRespVo {

    private String index;//序号

    private String containerId;// id，  注意：只有在更新的状况下才使用该字段

    private String containerOwner;//箱主                           ,import

    private String containerOwnerCode;//箱主代码                    ,import

    private String containerCaseNo;//箱号                           ,import     todo:唯一否

    private String containerCount;//数量 ,int类型             export,import

    private String containerDrayageDepot;//提箱点                  export,import
    private String containerDrayageDepotCode;//提箱点              export,import

    private String containerReturnDepot;//返箱点                   export,import
    private String containerReturnDepotCode;//返箱点               export,import

    private String containerSealNo;//铅封号                         ,import

    private String containerTradeName;//品名                        ,import

    private String containerType;//箱型                       export,import

    private String containerCartonSize;//尺寸                 export,import

    private String containerGrossWeight;//毛重 ,int类型       export,import

    private String containerRfTemp;//冻柜温度 ,int类型        export,import

    private String containerTemperUnit;//温度类型 0:华氏T 1:摄氏C export,import

    private String containerDynamo;//是否带电机                export,import

    private String containerIsMoreUnload;//否多地卸                  ,import

    private String containerCost;//费用  double类型，长度12,2         ,import

    private String containerState;

    private String containerStateDesc;

    public ContainerImportSearchRespVo() {
        super();
    }

    public ContainerImportSearchRespVo(int index, Long containerId, String containerOwner, String containerOwnerCode, String containerCaseNo, Integer containerCount, String containerDrayageDepot, String containerDrayageDepotCode, String containerReturnDepot, String containerReturnDepotCode, String containerSealNo, String containerTradeName, String containerType, String containerCartonSize, Double containerGrossWeight, Integer containerRfTemp, Integer containerTemperUnit, String containerDynamo, String containerIsMoreUnload, Double containerCost, Integer containerState) {
        this();
        this.index = String.valueOf(index);
        this.containerId = String.valueOf(containerId);
        this.containerOwner = containerOwner;
        this.containerOwnerCode = containerOwnerCode;
        this.containerCaseNo = containerCaseNo;
        this.containerCount = null == containerCount ? "" : String.valueOf(containerCount);
        this.containerDrayageDepot = containerDrayageDepot;
        this.containerDrayageDepotCode = containerDrayageDepotCode;
        this.containerReturnDepot = containerReturnDepot;
        this.containerReturnDepotCode = containerReturnDepotCode;
        this.containerSealNo = containerSealNo;
        this.containerTradeName = containerTradeName;
        this.containerType = containerType;
        this.containerCartonSize = containerCartonSize;
        this.containerGrossWeight = null == containerGrossWeight ? "" : String.valueOf(containerGrossWeight);
        this.containerRfTemp = null == containerRfTemp ? "" : String.valueOf(containerRfTemp);
        this.containerTemperUnit = null == containerTemperUnit ? "" : String.valueOf(containerTemperUnit);
        this.containerDynamo = containerDynamo;
        this.containerIsMoreUnload = containerIsMoreUnload;
        this.containerCost = null == containerCost ? "" : String.valueOf(containerCost);
        this.containerState = String.valueOf(containerState);
        this.containerStateDesc = ContainerStateType.descOf(containerState);
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerOwner() {
        return containerOwner;
    }

    public void setContainerOwner(String containerOwner) {
        this.containerOwner = containerOwner;
    }

    public String getContainerOwnerCode() {
        return containerOwnerCode;
    }

    public void setContainerOwnerCode(String containerOwnerCode) {
        this.containerOwnerCode = containerOwnerCode;
    }

    public String getContainerCaseNo() {
        return containerCaseNo;
    }

    public void setContainerCaseNo(String containerCaseNo) {
        this.containerCaseNo = containerCaseNo;
    }

    public String getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(String containerCount) {
        this.containerCount = containerCount;
    }

    public String getContainerDrayageDepot() {
        return containerDrayageDepot;
    }

    public void setContainerDrayageDepot(String containerDrayageDepot) {
        this.containerDrayageDepot = containerDrayageDepot;
    }

    public String getContainerDrayageDepotCode() {
        return containerDrayageDepotCode;
    }

    public void setContainerDrayageDepotCode(String containerDrayageDepotCode) {
        this.containerDrayageDepotCode = containerDrayageDepotCode;
    }

    public String getContainerReturnDepot() {
        return containerReturnDepot;
    }

    public void setContainerReturnDepot(String containerReturnDepot) {
        this.containerReturnDepot = containerReturnDepot;
    }

    public String getContainerReturnDepotCode() {
        return containerReturnDepotCode;
    }

    public void setContainerReturnDepotCode(String containerReturnDepotCode) {
        this.containerReturnDepotCode = containerReturnDepotCode;
    }

    public String getContainerSealNo() {
        return containerSealNo;
    }

    public void setContainerSealNo(String containerSealNo) {
        this.containerSealNo = containerSealNo;
    }

    public String getContainerTradeName() {
        return containerTradeName;
    }

    public void setContainerTradeName(String containerTradeName) {
        this.containerTradeName = containerTradeName;
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

    public String getContainerGrossWeight() {
        return containerGrossWeight;
    }

    public void setContainerGrossWeight(String containerGrossWeight) {
        this.containerGrossWeight = containerGrossWeight;
    }

    public String getContainerRfTemp() {
        return containerRfTemp;
    }

    public void setContainerRfTemp(String containerRfTemp) {
        this.containerRfTemp = containerRfTemp;
    }

    public String getContainerTemperUnit() {
        return containerTemperUnit;
    }

    public void setContainerTemperUnit(String containerTemperUnit) {
        this.containerTemperUnit = containerTemperUnit;
    }

    public String getContainerDynamo() {
        return containerDynamo;
    }

    public void setContainerDynamo(String containerDynamo) {
        this.containerDynamo = containerDynamo;
    }

    public String getContainerIsMoreUnload() {
        return containerIsMoreUnload;
    }

    public void setContainerIsMoreUnload(String containerIsMoreUnload) {
        this.containerIsMoreUnload = containerIsMoreUnload;
    }

    public String getContainerCost() {
        return containerCost;
    }

    public void setContainerCost(String containerCost) {
        this.containerCost = containerCost;
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
}
