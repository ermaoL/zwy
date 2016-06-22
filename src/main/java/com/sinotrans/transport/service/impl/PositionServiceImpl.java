package com.sinotrans.transport.service.impl;

import com.sinotrans.framework.mybatis.support.FilterCondition;
import com.sinotrans.framework.orm.support.PagingInfo;
import com.sinotrans.framework.service.mybatis.base.impl.MybatisManagerImpl;
import com.sinotrans.transport.common.enums.*;
import com.sinotrans.transport.common.exception.DateFormatException;
import com.sinotrans.transport.dto.PositionContainerResponse;
import com.sinotrans.transport.dto.PositionListRequest;
import com.sinotrans.transport.dto.PositionListResponse;
import com.sinotrans.transport.dto.vo.*;
import com.sinotrans.transport.model.HistTranTracking;
import com.sinotrans.transport.model.Order;
import com.sinotrans.transport.model.TaskOperateInfo;
import com.sinotrans.transport.service.PositionService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by emi on 2016/6/15.
 */
@Service
public class PositionServiceImpl extends MybatisManagerImpl implements PositionService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Logger logger = Logger.getLogger(PositionServiceImpl.class);


    @Override
    public PositionListResponse positionList(Long userId, PositionListRequest request) {

        PagingInfo pagingInfo = this.fetchPositionListPagingInfo(request);
        List<Map<String, Object>> resultList = myBatisDao.queryByCondition("SearchPositionList", request.fetchMapCondition(), "ct.contId desc", pagingInfo, null);
        List<ContainerStateVo> stateVoList = this.generateStateVoList(resultList);
        ContainerInfoVo infoVo = null;
        if (0 != stateVoList.size()) {
            Map<String, Object> map = resultList.get(0);
            List<TaskOperateInfo> taskList = myBatisDao.findByCondition(TaskOperateInfo.class, null, null, this.fetchTaskByContainer((String)map.get("contGbsId"), true));
            infoVo = this.defineContainerInfo(infoVo, (String)map.get("ordeWharf"), ((BigDecimal)map.get("ordeType")).intValue(), taskList);

        }
        return null;
    }


    @Override
    public PositionContainerResponse containerPosition(Long userId, String containerGbsId) {//1: 暂无任务   2:任务正在执行中  3:子任务结束  4:整个箱子任务结束

        List<TaskOperateInfo> taskList = myBatisDao.findByCondition(TaskOperateInfo.class, "tkoiId asc", null, this.fetchTaskByContainer(containerGbsId, true));
        if (null == taskList || 0 == taskList.size()) {
            return new PositionContainerResponse(containerGbsId, 1, null, null);
        }
        int currentState = 2;
        TaskOperateInfo task = taskList.get(taskList.size() - 1);//最后一个任务
        if (TaskStateType.Finish.getValue() == task.getTkoiState()) {
            if (GbsTruckingActType.ExportInWharf.getValue() == task.getTkoiTaskType() || GbsTruckingActType.ImportGiveBack.getValue() == task.getTkoiTaskType()) {
//                String[] taskIdArr = this.generateTaskId()
                List<HistTranTracking> histList = myBatisDao.findByCondition(HistTranTracking.class, "histId asc", null, this.fetchHistByTaskId(taskList, true));
                List<PositionHistRespVo> histVoList = this.generateHistVo(histList);
                return new PositionContainerResponse(containerGbsId, 4, null, histVoList);
            }
            currentState = 3;
        }
        //以下是执行中 和 子任务完成
        String firstLongitude = "";
        String firstLatitude = "";
        String preLongitude = "";
        String preLatitude = "";
        long pretTime = 0;
        String nextLongitude = "";
        String nextLatitude = "";
        long nextTime = 0;

        PositionContainerRespVo containerVo;
        Long taskId = taskList.get(0).getTkoiId();//箱子首个任务Id
        List<HistTranTracking> histList = myBatisDao.findByCondition(HistTranTracking.class, "histId asc", new PagingInfo(1, 1), this.fetchHistByTaskId(taskId, true));
        if (0 != histList.size()) {
            firstLongitude = histList.get(0).getHistLongitude();
            firstLatitude = histList.get(0).getHistLatitude();
        }

        taskId = taskList.get(taskList.size() - 1).getTkoiId();//箱子当前任务Id
        if (2 == currentState) {//执行中
            String twoPointTime = "";
            histList = myBatisDao.findByCondition(HistTranTracking.class, "histId asc", new PagingInfo(2, 1), this.fetchHistByTaskId(taskId, true));
            if (0 != histList.size()) {
                nextLongitude = histList.get(histList.size() - 1).getHistLongitude();
                nextLatitude = histList.get(histList.size() - 1).getHistLatitude();
                nextTime = histList.get(histList.size() - 1).getHistTime().getTime();
                if (2 == histList.size()) {
                    preLongitude = histList.get(0).getHistLongitude();
                    preLatitude = histList.get(0).getHistLatitude();
                    pretTime = histList.get(0).getHistTime().getTime();
                    twoPointTime = nextTime - pretTime + "";
                }
            }//todo:预计到达时间还没从gbs库中拿到
            containerVo = new PositionContainerRespVo(task.getTkoiTaskType(), TaskStateType.descOf(task.getTkoiTaskType()), firstLongitude, firstLatitude, preLongitude, preLatitude, nextLongitude, nextLatitude, twoPointTime, null, sdf2);

        } else {//子任务结束
            histList = myBatisDao.findByCondition(HistTranTracking.class, "histId asc", new PagingInfo(1, 1), this.fetchHistByTaskId(taskId, true));
            if (0 != histList.size()) {//1
                nextLongitude = histList.get(0).getHistLongitude();
                nextLatitude = histList.get(0).getHistLatitude();
            }//todo:预计到达时间还没从gbs库中拿到
            containerVo = new PositionContainerRespVo(task.getTkoiTaskType(), TaskStateType.descOf(task.getTkoiTaskType()), firstLongitude, firstLatitude, nextLongitude, nextLatitude, task.getTkoiEndTime(), null, sdf2);
        }
        return new PositionContainerResponse(containerGbsId, currentState, containerVo, null);
    }

    private List<PositionHistRespVo> generateHistVo(List<HistTranTracking> histList) {

        PositionHistRespVo histVo;
        List<PositionHistRespVo> histVoList = new ArrayList<PositionHistRespVo>(histList.size());
        for (HistTranTracking h : histList) {
            histVo = new PositionHistRespVo(h.getHistLongitude(), h.getHistLatitude(), h.getHistTime(), sdf2);
            histVoList.add(histVo);
        }
        return histVoList;
    }

    private ContainerInfoVo defineContainerInfo(ContainerInfoVo infoVo, String orderWharf, int orderType, List<TaskOperateInfo> taskList) {

        if (OrderType.Import.getValue() == orderType) {//进口
            infoVo = new ContainerInfoImportVo();
            for (TaskOperateInfo t : taskList) {
                int taskType = t.getTkoiTaskType();
                if (GbsTruckingActType.ImportWeightMoved.getValue() == taskType) {
                    ((ContainerInfoImportVo)infoVo).defineImport15(orderWharf, t.getTkoiStartTime(), t.getTkoiEndTime(), null, null, sdf2);
                } else if (GbsTruckingActType.ImportLayWeight.getValue() == taskType) {
                    ((ContainerInfoImportVo)infoVo).defineImport25(t.getTkoiStartTime(), t.getTkoiArriveLeaveTime1(), t.getTkoiArriveLeaveTime2(), t.getTkoiArriveLeaveTime3(), t.getTkoiArriveLeaveTime4(), t.getTkoiArriveLeaveTime5(), sdf2);
                } else if (GbsTruckingActType.ImportLayEmpty.getValue() == taskType) {
                    ((ContainerInfoImportVo)infoVo).defineImport35(t.getTkoiArriveLeaveTime1(), t.getTkoiArriveLeaveTime2(), t.getTkoiArriveLeaveTime3(), t.getTkoiArriveLeaveTime4(), t.getTkoiArriveLeaveTime5(), sdf2);
                } else if (GbsTruckingActType.ImportGiveBack.getValue() == taskType) {
                    ((ContainerInfoImportVo)infoVo).defineImport45(null, null, null, null, sdf2);
                }
            }
        } else {

        }

        return null;
    }

    private List<FilterCondition> fetchTaskByContainer(String containerGbsId, boolean checkStatus) {

        List filter = new ArrayList();
        filter.add(new FilterCondition("tkoiGbsContainerId", containerGbsId, "="));
        filter.add(new FilterCondition("tkoiState", new Integer[] {TaskStateType.Accepted.getValue(), TaskStateType.Finish.getValue()}, "in"));
        if (checkStatus) {
            filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));//likeAnywhere
        }
        return filter;
    }

    private List<FilterCondition> fetchHistByTaskId(List<TaskOperateInfo> taskList, boolean checkStatus) {

        Long[] taskIdArr = new Long[taskList.size()];
        for (int i = 0; i < taskList.size(); i ++) {
            taskIdArr[i] = taskList.get(i).getTkoiId();
        }
        List filter = new ArrayList();
        filter.add(new FilterCondition("histTaskId", taskIdArr, "in"));
        if (checkStatus) {
            filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));//likeAnywhere
        }
        return filter;
    }

    private List<FilterCondition> fetchHistByTaskId(Long taskId, boolean checkStatus) {

        List filter = new ArrayList();
        filter.add(new FilterCondition("histTaskId", taskId, "="));
        if (checkStatus) {
            filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));//likeAnywhere
        }
        return filter;
    }



    private PagingInfo fetchPositionListPagingInfo(PositionListRequest request) {
        PagingInfo pagingInfo = request.fetchPagingInfo();
        if(pagingInfo != null) {
            int counts = this.myBatisDao.getQueryCountByCondition("SearchPositionList", request.fetchMapCondition(), null);
            pagingInfo.setTotalRows(counts);
        }

        return pagingInfo;
    }

    private List<ContainerStateVo> generateStateVoList(List<Map<String, Object>> resultList) {

        List<ContainerStateVo> stateVoList = new ArrayList<ContainerStateVo>();
        if (null == resultList || 0 == resultList.size()) {
            return stateVoList;
        } else {
            ContainerStateVo containerStateVo;
            for (Map<String, Object> m : resultList) {
                ContainerStateType stateType = ContainerStateType.valueOf(((BigDecimal)m.get("contState")).intValue());
                containerStateVo = new ContainerStateVo(((BigDecimal)m.get("contId")).longValue(), (String)m.get("contGbsId"), (String)m.get("ordeBillNo"), (String)m.get("contCaseNo"), ((BigDecimal)m.get("ordeType")).intValue(), stateType.getValue(), stateType.getDesc());
                stateVoList.add(containerStateVo);
            }
        }
        return stateVoList;
    }

}
