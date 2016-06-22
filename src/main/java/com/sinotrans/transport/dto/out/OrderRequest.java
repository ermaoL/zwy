package com.sinotrans.transport.dto.out;


import java.util.Date;

/**
 * Created by emi on 2016/5/10.
 */
public class OrderRequest {

    //business
    private String docCode;
    private String branchCode;
    private String crmCode;
    private String crmName;
    private String canvasserId;
    private String createdByCode;
    private Date createdTime;


    //share
//    private String docCode;
    private String carrierId;
    private String vessel;
    private String voyage;
    private String placeOfDelivery;
    private String planceOfReceipt;
    private String shipId;
    private String portOfLoading;
    private String portLoadingCode;
    private String portDestineCode;
    private String finalDestination;
    private String claimNo;
//    private String createdByCode;
//    private Date createdTime;
    private Date etd;
    private Double importExport;
    private String placeDeliveryCode;
    private String placeReceiptCode;
    private String checkDept;

    //SContainer
    private String containerId;
    private String containerOwner;
    private String containerType;
    private String containerSize;
    private String temper;
    private String temperUnit;
    private Double weightKg;
//    private String docCode;
    private String containerCheck;
    private Double overWeightYn;

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setCrmCode(String crmCode) {
        this.crmCode = crmCode;
    }

    public void setCrmName(String crmName) {
        this.crmName = crmName;
    }

    public void setCanvasserId(String canvasserId) {
        this.canvasserId = canvasserId;
    }

    public void setCreatedByCode(String createdByCode) {
        this.createdByCode = createdByCode;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public void setPlaceOfDelivery(String placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
    }

    public void setPlanceOfReceipt(String planceOfReceipt) {
        this.planceOfReceipt = planceOfReceipt;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public void setPortLoadingCode(String portLoadingCode) {
        this.portLoadingCode = portLoadingCode;
    }

    public void setPortDestineCode(String portDestineCode) {
        this.portDestineCode = portDestineCode;
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public void setImportExport(Double importExport) {
        this.importExport = importExport;
    }

    public void setPlaceDeliveryCode(String placeDeliveryCode) {
        this.placeDeliveryCode = placeDeliveryCode;
    }

    public void setPlaceReceiptCode(String placeReceiptCode) {
        this.placeReceiptCode = placeReceiptCode;
    }

    public void setCheckDept(String checkDept) {
        this.checkDept = checkDept;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public void setContainerOwner(String containerOwner) {
        this.containerOwner = containerOwner;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public void setContainerSize(String containerSize) {
        this.containerSize = containerSize;
    }

    public void setTemper(String temper) {
        this.temper = temper;
    }

    public void setTemperUnit(String temperUnit) {
        this.temperUnit = temperUnit;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public void setContainerCheck(String containerCheck) {
        this.containerCheck = containerCheck;
    }

    public void setOverWeightYn(Double overWeightYn) {
        this.overWeightYn = overWeightYn;
    }

    public String getDocCode() {
        return docCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getCrmCode() {
        return crmCode;
    }

    public String getCrmName() {
        return crmName;
    }

    public String getCanvasserId() {
        return canvasserId;
    }

    public String getCreatedByCode() {
        return createdByCode;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public String getVessel() {
        return vessel;
    }

    public String getVoyage() {
        return voyage;
    }

    public String getPlaceOfDelivery() {
        return placeOfDelivery;
    }

    public String getPlanceOfReceipt() {
        return planceOfReceipt;
    }

    public String getShipId() {
        return shipId;
    }

    public String getPortOfLoading() {
        return portOfLoading;
    }

    public String getPortLoadingCode() {
        return portLoadingCode;
    }

    public String getPortDestineCode() {
        return portDestineCode;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public Date getEtd() {
        return etd;
    }

    public Double getImportExport() {
        return importExport;
    }

    public String getPlaceDeliveryCode() {
        return placeDeliveryCode;
    }

    public String getPlaceReceiptCode() {
        return placeReceiptCode;
    }

    public String getCheckDept() {
        return checkDept;
    }

    public String getContainerId() {
        return containerId;
    }

    public String getContainerOwner() {
        return containerOwner;
    }

    public String getContainerType() {
        return containerType;
    }

    public String getContainerSize() {
        return containerSize;
    }

    public String getTemper() {
        return temper;
    }

    public String getTemperUnit() {
        return temperUnit;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public String getContainerCheck() {
        return containerCheck;
    }

    public Double getOverWeightYn() {
        return overWeightYn;
    }
}
