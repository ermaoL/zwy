package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;
import com.sinotrans.transport.dto.vo.ContainerInfoRespVo;
import com.sinotrans.transport.dto.vo.ContainerStateRespVo;
import com.sinotrans.transport.dto.vo.OrderInfoRespVo;
import com.sinotrans.transport.dto.vo.PositionHistRespVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emi on 2016/6/15.
 */
public class PositionListResponse extends RestResponse {

    private OrderInfoRespVo orderVo;//订单信息

    private List<ContainerStateRespVo> stateVoList;//订单下箱子列表

    private List<ContainerInfoRespVo> infoVoList;//某个箱子的时间轴

    private List<String> imgList;//图片列表

    private List<PositionHistRespVo> histVoList;//当currentState != 4 时，该字段为空

//    public PositionListResponse(OrderInfoRespVo orderVo, List<ContainerStateRespVo> stateVoList, List<ContainerInfoRespVo> infoVoList) {
//        this();
//        this.orderVo = orderVo;
//        this.stateVoList = stateVoList;
//        this.infoVoList = infoVoList;
//    }

    public PositionListResponse(OrderInfoRespVo orderVo, List<ContainerStateRespVo> stateVoList, List<ContainerInfoRespVo> infoVoList, List<String> imgList, List<PositionHistRespVo> histVoList) {
        this();
        this.orderVo = orderVo;
        this.stateVoList = stateVoList;
        this.infoVoList = infoVoList;
        this.imgList = imgList;
        this.histVoList = histVoList;
    }

    public PositionListResponse(List<ContainerInfoRespVo> infoVoList, List<String> imgList, List<PositionHistRespVo> histVoList) {
        this();
        this.orderVo = null;
        this.stateVoList = new ArrayList<ContainerStateRespVo>(0);
        this.infoVoList = infoVoList;
        this.imgList = imgList;
        this.histVoList = histVoList;
    }

    public PositionListResponse() {
        super();
    }

    public OrderInfoRespVo getOrderVo() {
        return orderVo;
    }

    public void setOrderVo(OrderInfoRespVo orderVo) {
        this.orderVo = orderVo;
    }

    public List<ContainerStateRespVo> getStateVoList() {
        return stateVoList;
    }

    public void setStateVoList(List<ContainerStateRespVo> stateVoList) {
        this.stateVoList = stateVoList;
    }

    public List<ContainerInfoRespVo> getInfoVoList() {
        return infoVoList;
    }

    public void setInfoVoList(List<ContainerInfoRespVo> infoVoList) {
        this.infoVoList = infoVoList;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public List<PositionHistRespVo> getHistVoList() {
        return histVoList;
    }

    public void setHistVoList(List<PositionHistRespVo> histVoList) {
        this.histVoList = histVoList;
    }
}
