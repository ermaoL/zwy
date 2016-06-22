package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;

import java.util.List;

/**
 * Created by emi on 2016/5/23.
 * 删除 进口 容器 返回 接口
 */
public class ContainerDropResponse extends RestResponse {

    private String firstContainerId;//第一条容器Id

    private List containerVoList;   //进口见ContainerImportSearchRespVo， 出口见ContainerExportSearchRespVo

    private List addressVoList;     //进口见AddressImportSearchRespVo， 出口见AddressExportSearchRespVo

    public ContainerDropResponse(Long containerId, List containerVoList, List addressVoList) {
        this();
        this.firstContainerId = (null == containerId || 0 == containerId) ? "" : String.valueOf(containerId);
        this.containerVoList = containerVoList;
        this.addressVoList = addressVoList;
    }

    public ContainerDropResponse() {
        super();
    }

    public String getFirstContainerId() {
        return firstContainerId;
    }

    public void setFirstContainerId(String firstContainerId) {
        this.firstContainerId = firstContainerId;
    }

    public List getContainerVoList() {
        return containerVoList;
    }

    public void setContainerVoList(List containerVoList) {
        this.containerVoList = containerVoList;
    }

    public List getAddressVoList() {
        return addressVoList;
    }

    public void setAddressVoList(List addressVoList) {
        this.addressVoList = addressVoList;
    }
}
