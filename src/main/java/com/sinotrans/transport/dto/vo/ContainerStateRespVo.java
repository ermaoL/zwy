package com.sinotrans.transport.dto.vo;

/**
 * Created by emi on 2016/6/15.
 */
public class ContainerStateRespVo {

    private String containerId;//箱主键

    private String containerGbsId;//gbs库箱Id

    private String containerType;//箱型

    private String containerCartonSize;//尺寸

    private String containerCaseNo;//箱号

    private String containerSealNo;//铅封号

    private String containerState;//箱状态(int 类型)  0 1 2 3 4 5

    private String time1;

    private String time2;

    private String time3;

    private String time4;

    private String time5;

    public ContainerStateRespVo(Long containerId, String containerGbsId, String containerType, String containerCartonSize, String containerCaseNo, String containerSealNo, int containerState, String time1, String time2, String time3, String time4, String time5) {
        this();
        this.containerId = String.valueOf(containerId);
        this.containerGbsId = containerGbsId;
        this.containerType = containerType;
        this.containerCartonSize = containerCartonSize;
        this.containerCaseNo = containerCaseNo;
        this.containerSealNo = containerSealNo;
        this.containerState = String.valueOf(containerState);
        this.time1 = time1;
        this.time2 = time2;
        this.time3 = time3;
        this.time4 = time4;
        this.time5 = time5;
    }

    public ContainerStateRespVo() {
        super();
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerGbsId() {
        return containerGbsId;
    }

    public void setContainerGbsId(String containerGbsId) {
        this.containerGbsId = containerGbsId;
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

    public String getContainerState() {
        return containerState;
    }

    public void setContainerState(String containerState) {
        this.containerState = containerState;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getTime4() {
        return time4;
    }

    public void setTime4(String time4) {
        this.time4 = time4;
    }

    public String getTime5() {
        return time5;
    }

    public void setTime5(String time5) {
        this.time5 = time5;
    }
}
