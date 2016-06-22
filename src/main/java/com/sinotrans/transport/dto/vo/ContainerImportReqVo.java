package com.sinotrans.transport.dto.vo;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/5/18.
 * 进口容器 创建修改 请求 Vo
 */
public class ContainerImportReqVo {

    @Pattern(regexp = "\\d*", message = "Id格式错误")
    private String containerId;// id，  注意：只有在更新的状况下才使用该字段

    private String containerOwner;//箱主                                  ,import

    private String containerOwnerCode;//箱主代码                          ,import

    private String containerCaseNo;//箱号                                 ,import     todo:唯一否

    @Pattern(regexp = "\\d*", message = "箱子数量格式错误")
    private String containerCount;//数量 ,int类型                   export,import

    private String containerDrayageDepot;//提箱点                  export,import
    private String containerDrayageDepotCode;//提箱点              export,import

    private String containerReturnDepot;//返箱点                   export,import
    private String containerReturnDepotCode;//返箱点               export,import

    private String containerSealNo;//铅封号                                ,import

    private String containerTradeName;//品名                              ,import

    private String containerType;//箱型                               export,import

    private String containerCartonSize;//尺寸                         export,import

//    @Digits(integer = 16, fraction = 3, message = "箱子重量格式错误")
    private String containerGrossWeight;//毛重 ,int类型              export,import

    private String containerRfTemp;//冻柜温度 ,int类型                 export,import

    private String containerTemperUnit;//温度类型 0:华氏F 1:摄氏C      export,import

    private String containerDynamo;//是否带电机 0:否  1:是             export,import

    private String containerIsMoreUnload;//否多地卸 0:否 1:是               ,import

    private String containerCost;//费用  double类型，长度12,2               ,import

    private String containerOther;//其他,可为空                       export

    private String containerRemark;//送柜要求及备注,                   export


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

    public String getContainerOther() {
        return containerOther;
    }

    public void setContainerOther(String containerOther) {
        this.containerOther = containerOther;
    }

    public String getContainerRemark() {
        return containerRemark;
    }

    public void setContainerRemark(String containerRemark) {
        this.containerRemark = containerRemark;
    }
}
