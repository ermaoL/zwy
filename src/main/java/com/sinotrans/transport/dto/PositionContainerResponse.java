package com.sinotrans.transport.dto;

import com.sinotrans.transport.dto.common.RestResponse;
import com.sinotrans.transport.dto.vo.PositionContainerRespVo;
import com.sinotrans.transport.dto.vo.PositionHistRespVo;

import java.util.List;

/**
 * Created by emi on 2016/6/15.
 * 单票跟踪 返回接口
 */
public class PositionContainerResponse extends RestResponse {

    private String containerGbsId;//gbs箱主键

    private String currentState;//1: 暂无任务   2:任务正在执行中  3:子任务结束  4:整个箱子任务结束

    private PositionContainerRespVo containerVo;//当currentState = 1 或4 时，该字段为空，当currentState = 2 或 3 时，部分字段为空

    private List<PositionHistRespVo> histVoList;//当currentState != 4 时，该字段为空

    public PositionContainerResponse(String containerGbsId, int currentState, PositionContainerRespVo containerVo, List<PositionHistRespVo> histVoList) {
        this();
        this.containerGbsId = containerGbsId;
        this.currentState = currentState + "";
        this.containerVo = containerVo;
        this.histVoList = histVoList;
    }

    public PositionContainerResponse() {
        super();
    }

    public String getContainerGbsId() {
        return containerGbsId;
    }

    public void setContainerGbsId(String containerGbsId) {
        this.containerGbsId = containerGbsId;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public PositionContainerRespVo getContainerVo() {
        return containerVo;
    }

    public void setContainerVo(PositionContainerRespVo containerVo) {
        this.containerVo = containerVo;
    }

    public List<PositionHistRespVo> getHistVoList() {
        return histVoList;
    }

    public void setHistVoList(List<PositionHistRespVo> histVoList) {
        this.histVoList = histVoList;
    }
}
