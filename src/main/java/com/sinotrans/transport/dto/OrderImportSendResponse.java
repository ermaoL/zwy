package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;
import com.sinotrans.transport.dto.vo.AddressImportSearchRespVo;
import com.sinotrans.transport.dto.vo.ContainerImportSearchRespVo;
import com.sinotrans.transport.dto.vo.OrderImportDetailRespVo;

import java.util.List;

/**
 * Created by emi on 2016/5/31.
 * 进口订单 下发 返回接口(订单状态和箱状态改变了)
 */
public class OrderImportSendResponse extends RestResponse {

    private OrderImportDetailRespVo orderVo;

    private List<ContainerImportSearchRespVo> containerVoList;

    private String firstContainerId;

    private List<AddressImportSearchRespVo> addressVoList;

    public OrderImportSendResponse(OrderImportDetailRespVo orderVo, List<ContainerImportSearchRespVo> containerVoList, Long firstContainerId, List<AddressImportSearchRespVo> addressVoList) {
        this();
        this.orderVo = orderVo;
        this.containerVoList = containerVoList;
        this.firstContainerId = (null == firstContainerId || 0 == firstContainerId) ? "" : String.valueOf(firstContainerId);
        this.addressVoList = addressVoList;
    }

    public OrderImportSendResponse() {
        super();
    }

    public OrderImportDetailRespVo getOrderVo() {
        return orderVo;
    }

    public void setOrderVo(OrderImportDetailRespVo orderVo) {
        this.orderVo = orderVo;
    }

    public List<ContainerImportSearchRespVo> getContainerVoList() {
        return containerVoList;
    }

    public void setContainerVoList(List<ContainerImportSearchRespVo> containerVoList) {
        this.containerVoList = containerVoList;
    }

    public String getFirstContainerId() {
        return firstContainerId;
    }

    public void setFirstContainerId(String firstContainerId) {
        this.firstContainerId = firstContainerId;
    }

    public List<AddressImportSearchRespVo> getAddressVoList() {
        return addressVoList;
    }

    public void setAddressVoList(List<AddressImportSearchRespVo> addressVoList) {
        this.addressVoList = addressVoList;
    }
}
