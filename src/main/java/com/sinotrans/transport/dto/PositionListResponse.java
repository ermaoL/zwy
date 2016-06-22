package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;
import com.sinotrans.transport.dto.vo.ContainerInfoVo;
import com.sinotrans.transport.dto.vo.ContainerStateVo;

import java.util.List;

/**
 * Created by emi on 2016/6/15.
 */
public class PositionListResponse extends RestResponse {

    List<ContainerStateVo> stateVoList;

    ContainerInfoVo infoVo;

    public PositionListResponse(List<ContainerStateVo> stateVoList, ContainerInfoVo infoVo) {
        this();
        this.stateVoList = stateVoList;
        this.infoVo = infoVo;
    }

    public PositionListResponse() {
        super();
    }

    public List<ContainerStateVo> getStateVoList() {
        return stateVoList;
    }

    public void setStateVoList(List<ContainerStateVo> stateVoList) {
        this.stateVoList = stateVoList;
    }

    public ContainerInfoVo getInfoVo() {
        return infoVo;
    }

    public void setInfoVo(ContainerInfoVo infoVo) {
        this.infoVo = infoVo;
    }
}
