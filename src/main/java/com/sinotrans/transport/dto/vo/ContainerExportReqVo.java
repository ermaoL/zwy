package com.sinotrans.transport.dto.vo;

import javax.validation.constraints.Pattern;

/**
 * Created by emi on 2016/5/19.
 * 出口容器 创建修改 请求 Vo
 */
public class ContainerExportReqVo {

    @Pattern(regexp = "\\d*", message = "Id格式错误")
    private String containerId;// id，  注意：只有在更新的状况下才使用该字段

    private String containerOwner;//箱主                              ,import
    private String containerOwnerCode;//箱主代码                       ,import

    private String containerCaseNo;//柜型                       export,import

    @Pattern(regexp = "\\d*", message = "箱子数量格式错误")
    private String containerCount;//数量                    export,import

    private String containerDrayageDepot;//提箱点                  export,import
    private String containerDrayageDepotCode;//提箱点              export,import

    private String containerReturnDepot;//返箱点                   export,import
    private String containerReturnDepotCode;//返箱点               export,import

    private String containerGrossWeight;//毛重              export,import

    private String containerRfTemp;//冻柜温度                 export,import

    private String containerTemperUnit;//温度类型  0:华氏T 1:摄氏C     export,import

    private String containerType;//箱型                   export,import

    private String containerCartonSize;//尺寸                   export,import

    private String containerIsOverWeight;//是否超重            export

    private String containerIsCheck;//是否柜检                 export

    private String containerIsCheckliest;//是否需要柜检单      export

    private String containerIsReplace;//是否冻代干              export

    private String containerDynamo;//是否带电机                export,import

    private String containerIsMoreUnload;//是否带电机                export,import

    private String containerOther;//其他,可为空                              export,import

    private String containerRemark;//送柜要求及备注,可为空


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

    public String getContainerIsOverWeight() {
        return containerIsOverWeight;
    }

    public void setContainerIsOverWeight(String containerIsOverWeight) {
        this.containerIsOverWeight = containerIsOverWeight;
    }

    public String getContainerIsCheck() {
        return containerIsCheck;
    }

    public void setContainerIsCheck(String containerIsCheck) {
        this.containerIsCheck = containerIsCheck;
    }

    public String getContainerIsCheckliest() {
        return containerIsCheckliest;
    }

    public void setContainerIsCheckliest(String containerIsCheckliest) {
        this.containerIsCheckliest = containerIsCheckliest;
    }

    public String getContainerIsReplace() {
        return containerIsReplace;
    }

    public void setContainerIsReplace(String containerIsReplace) {
        this.containerIsReplace = containerIsReplace;
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
