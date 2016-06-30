package com.sinotrans.transport.service.impl;

import com.sinotrans.framework.mybatis.support.FilterCondition;
import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.support.PagingInfo;
import com.sinotrans.framework.service.mybatis.base.impl.MybatisManagerImpl;
import com.sinotrans.transport.common.enums.*;
import com.sinotrans.transport.common.exception.RecordNotFoundException;
import com.sinotrans.transport.dto.PositionContainerResponse;
import com.sinotrans.transport.dto.PositionListRequest;
import com.sinotrans.transport.dto.PositionListResponse;
import com.sinotrans.transport.dto.vo.*;
import com.sinotrans.transport.model.*;
import com.sinotrans.transport.service.PositionService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emi on 2016/6/15.
 */
@Service
public class PositionServiceImpl extends MybatisManagerImpl implements PositionService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Logger logger = Logger.getLogger(PositionServiceImpl.class);

    @Override
    public PositionListResponse positionList(Long userId, Long orderId) {

        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        OrderInfoRespVo orderVo = new OrderInfoRespVo(orderId, order.getOrdeBillNo(), order.getOrdeType(), order.getOrdeShipName(), order.getOrdeShipNameCode(), order.getOrdeSailing(), order.getOrdeSailingCode());
        List<Container> containerList = myBatisDao.findByCondition(Container.class, "contId desc", null, this.fetchContainerByOrder(orderId, true));
        if (null == containerList || 0 == containerList.size()) {
            return new PositionListResponse(orderVo, new ArrayList<ContainerStateRespVo>(0), new ArrayList<ContainerInfoRespVo>(0), new ArrayList<String>(0), new ArrayList<PositionHistRespVo>(0));
        }
        List<ContainerStateRespVo> stateVoList = new ArrayList<ContainerStateRespVo>(containerList.size());
        List<ContainerInfoRespVo> infoVoList;
        Long firstContainerId = containerList.get(0).getContId();
        String firstGbsContainerId = containerList.get(0).getContGbsId();
        int orderType = order.getOrdeType();
        ContainerStateRespVo stateVo = null;
        List<PositionTimeLog> positionLogList;
        for (Container c : containerList) {
            positionLogList = myBatisDao.findByCondition(PositionTimeLog.class, "posiId asc", null, this.fetchPositionByState(orderType, c.getContId()));//
            stateVo = this.generateContainerStateVo(c, positionLogList, stateVo);//
            stateVoList.add(stateVo);
        }
        positionLogList = myBatisDao.findByCondition(PositionTimeLog.class, "posiTime asc", null, this.fetchPositionByContainer(firstContainerId));
        if (null == positionLogList || 0 == positionLogList.size()) {
            infoVoList = new ArrayList<ContainerInfoRespVo>(0);
        } else {
            infoVoList = new ArrayList<ContainerInfoRespVo>(positionLogList.size());
            infoVoList = this.positionLog2Vo(positionLogList, infoVoList);
        }

        List<PositionHistRespVo> histVoList;
        if (StringUtils.isEmpty(firstGbsContainerId)) {
            histVoList = new ArrayList<PositionHistRespVo>(0);
        } else {
            List<HistTranTracking> histList = myBatisDao.findByCondition(HistTranTracking.class, "histId asc", null, this.fetchHistByContainer(firstGbsContainerId, true));
            histVoList = this.generateHistVo(histList);
        }

        return new PositionListResponse(orderVo, stateVoList, infoVoList, this.generateContainerImg(), histVoList);
    }

    @Override
    public PositionListResponse positionContainerInfo(Long userId, Long containerId, String containerGbsId) {
//        Container container = myBatisDao.get(Container.class, containerId);
//        this.checkNull(DomainType.Container, container, containerId);
//        String firstGbsContainerId = container.getContGbsId();
        List<ContainerInfoRespVo> infoVoList;
        List<PositionTimeLog> positionLogList;
        positionLogList = myBatisDao.findByCondition(PositionTimeLog.class, "posiTime asc", null, this.fetchPositionByContainer(containerId));
        if (null == positionLogList || 0 == positionLogList.size()) {
            infoVoList = new ArrayList<ContainerInfoRespVo>(0);
        } else {
            infoVoList = new ArrayList<ContainerInfoRespVo>(positionLogList.size());
            infoVoList = this.positionLog2Vo(positionLogList, infoVoList);
        }

        List<PositionHistRespVo> histVoList;
        if (StringUtils.isEmpty(containerGbsId)) {
            histVoList = new ArrayList<PositionHistRespVo>(0);
        } else {
            List<HistTranTracking> histList = myBatisDao.findByCondition(HistTranTracking.class, "histId asc", null, this.fetchHistByContainer(containerGbsId, true));
            histVoList = this.generateHistVo(histList);
        }
        return new PositionListResponse(infoVoList, this.generateContainerImg(), histVoList);
    }

    @Override
    public PositionContainerResponse singleContainerPosition(Long userId, Long containerId) {//1: 暂无任务   2:任务正在执行中  3:子任务结束  4:整个箱子任务结束

        Container container = myBatisDao.get(Container.class, containerId);
        this.checkNull(DomainType.Container, container, containerId);
        String containerGbsId = container.getContGbsId();
        if (StringUtils.isEmpty(container.getContGbsId())) {
            return new PositionContainerResponse(containerId, 1, null, null);
        }

        List<TaskOperateInfo> taskList = myBatisDao.findByCondition(TaskOperateInfo.class, "tkoiId asc", null, this.fetchTaskByContainer(containerGbsId, true));
        if (null == taskList || 0 == taskList.size()) {
            return new PositionContainerResponse(containerId, 1, null, null);
        }
        PositionContainerRespVo containerVo;
        int currentState = 2;
        TaskOperateInfo task = taskList.get(taskList.size() - 1);//最后一个任务
        if (TaskStateType.Finish.getValue() == task.getTkoiState()) {
            if (GbsTruckingActType.ExportInWharf.getValue() == task.getTkoiTaskType() || GbsTruckingActType.ImportGiveBack.getValue() == task.getTkoiTaskType()) {
//                String[] taskIdArr = this.generateTaskId()
//                List<HistTranTracking> histList = myBatisDao.findByCondition(HistTranTracking.class, "histId asc", null, this.fetchHistByTaskId(taskList, true));
                List<HistTranTracking> histList = myBatisDao.findByCondition(HistTranTracking.class, "histId asc", null, this.fetchHistByContainer(containerGbsId, true));
                List<PositionHistRespVo> histVoList = this.generateHistVo(histList);
                containerVo = new PositionContainerRespVo(task.getTkoiEndTime(), null, sdf2);
                return new PositionContainerResponse(containerId, 4, containerVo, histVoList);
            }
            currentState = 3;
        }
        //以下是执行中 和 子任务完成
        String firstLongitude = "";
        String firstLatitude = "";
        String preLongitude = "";
        String preLatitude = "";
        long preTime = 0;
        String nextLongitude = "";
        String nextLatitude = "";
        long nextTime = 0;


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
                    preTime = histList.get(0).getHistTime().getTime();
                    twoPointTime = nextTime - preTime + "";
                }
            }//todo:预计到达时间还没从gbs库中拿到
            containerVo = new PositionContainerRespVo(task.getTkoiTaskType(), TaskStateType.descOf(task.getTkoiTaskType()), firstLongitude, firstLatitude, preLongitude, preLatitude, nextLongitude, nextLatitude, twoPointTime, null, sdf2);

        } else {//子任务结束
            histList = myBatisDao.findByCondition(HistTranTracking.class, "histId asc", new PagingInfo(1, 1), this.fetchHistByTaskId(taskId, true));
            if (0 != histList.size()) {//1
                nextLongitude = histList.get(0).getHistLongitude();
                nextLatitude = histList.get(0).getHistLatitude();
            }//todo:预计到达时间还没从gbs库中拿到
            containerVo = new PositionContainerRespVo(task.getTkoiTaskType(), GbsTruckingActType.descOf(task.getTkoiTaskType()), firstLongitude, firstLatitude, nextLongitude, nextLatitude, task.getTkoiEndTime(), null, sdf2);
        }
        return new PositionContainerResponse(containerId, currentState, containerVo, null);
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


    private List<ContainerInfoRespVo> positionLog2Vo(List<PositionTimeLog> positionLogList, List<ContainerInfoRespVo> infoVoList) {

        ContainerInfoRespVo infoVo;
        for (PositionTimeLog p : positionLogList) {
            infoVo = new ContainerInfoRespVo(sdf2.format(p.getPosiTime()), p.getPosiDetail());
            infoVoList.add(infoVo);
        }
        return infoVoList;
    }

    private ContainerStateRespVo generateContainerStateVo(Container c, List<PositionTimeLog> positionLogList, ContainerStateRespVo stateVo) {
        int currentState = 0;
        String time1 = "",time2 = "",time3 = "",time4 = "",time5 = "";
        for (PositionTimeLog p : positionLogList) {
            if (PositionLogType.ImVerify.getValue() == p.getPosiValue() || PositionLogType.ExVerify.getValue() == p.getPosiValue()) {
                time1 = sdf2.format(p.getPosiTime());
                currentState = currentState > 1 ? currentState : 1;
            } else if (PositionLogType.ImSendLocker.getValue() == p.getPosiValue() || PositionLogType.ExEmptyFinish.getValue() == p.getPosiValue()) {
                time2 = sdf2.format(p.getPosiTime());
                currentState = currentState > 2 ? currentState : 2;
            } else if (PositionLogType.ImArriveFactory.getValue() == p.getPosiValue() || PositionLogType.ExArriveFactory.getValue() == p.getPosiValue()) {
                time3 = sdf2.format(p.getPosiTime());
                currentState = currentState > 3 ? currentState : 3;
            } else if (PositionLogType.ImLeaveFactory.getValue() == p.getPosiValue() || PositionLogType.ExLeaveFactory.getValue() == p.getPosiValue()) {
                time4 = sdf2.format(p.getPosiTime());
                currentState = currentState > 4 ? currentState : 4;
            } else if (PositionLogType.ImEmptyGiveBack.getValue() == p.getPosiValue() || PositionLogType.ExInPortReport.getValue() == p.getPosiValue()) {
                time5 = sdf2.format(p.getPosiTime());
                currentState = 5;
            }
        }
        stateVo = new ContainerStateRespVo(c.getContId(), c.getContGbsId(), c.getContType(), c.getContCartonSize(), c.getContCaseNo(), c.getContSealNo(), currentState, time1, time2, time3, time4, time5);
        return stateVo;
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

    private List<FilterCondition> fetchHistByContainer(String firstGbsContainerId, boolean checkStatus) {

        List filter = new ArrayList();
        filter.add(new FilterCondition("histGbsContainerId", firstGbsContainerId, "="));
        if (checkStatus) {
            filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));//likeAnywhere
        }
        return filter;
    }

    private List<FilterCondition> fetchContainerByOrder(Long orderId, boolean checkStatus) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("contOrdeId", orderId, "="));
        if (checkStatus) {
            filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));//likeAnywhere
        }
        return filter;
    }

    private List<FilterCondition> fetchPositionByState(int orderType, Long contId) {
        List filter = new ArrayList();
        if (OrderType.Import.getValue() == orderType) {
            filter.add(new FilterCondition("posiContId", contId, "="));
            filter.add(new FilterCondition("posiValue", new Integer[] {PositionLogType.ImVerify.getValue(), PositionLogType.ImSendLocker.getValue(), PositionLogType.ImArriveFactory.getValue(), PositionLogType.ImLeaveFactory.getValue(), PositionLogType.ImEmptyGiveBack.getValue()}, "in"));
        } else {
            filter.add(new FilterCondition("posiValue", new Integer[] {PositionLogType.ExVerify.getValue(), PositionLogType.ExEmptyFinish.getValue(), PositionLogType.ExArriveFactory.getValue(), PositionLogType.ExLeaveFactory.getValue(), PositionLogType.ExInPortReport.getValue()}, "in"));
        }
        return filter;
    }

    private List<FilterCondition> fetchPositionByContainer(Long containerId) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("posiContId", containerId, "="));
        return filter;
    }

    private List<String> generateContainerImg() {
        List<String> imgList = new ArrayList<String>(4);
        imgList.add("http://o8e8gfpur.bkt.clouddn.com/task_zwy_imgf0caf300a26c474b8760fd83d5f3a26b.jpg");
        imgList.add("http://o8e8gfpur.bkt.clouddn.com/task_zwy_img4bfb7612ccd84436bf29345a8941763e.jpg");
        imgList.add("http://o8e8gfpur.bkt.clouddn.com/data/sino/android/56da07b3ed1042d391430b4b11b38ed3.jpg");
        imgList.add("http://o8e8gfpur.bkt.clouddn.com/task_zwy_img047e87003dae4f27a656fc4bed85dd90.jpg");
        return imgList;
    }

    private void checkNull(DomainType domain, BaseModel model, Long domainId) {
        if (null == model) {
            throw new RecordNotFoundException(domain, domainId, logger);
        }
    }

    private PagingInfo fetchPositionListPagingInfo(PositionListRequest request) {
        PagingInfo pagingInfo = request.fetchPagingInfo();
        if(pagingInfo != null) {
            int counts = this.myBatisDao.getQueryCountByCondition("SearchPositionList", request.fetchCondition(), null);
            pagingInfo.setTotalRows(counts);
        }
        return pagingInfo;
    }

//    private List<ContainerStateRespVo> generateStateVoList(List<Map<String, Object>> resultList) {
//
//        List<ContainerStateRespVo> stateVoList = new ArrayList<ContainerStateRespVo>();
//        if (null == resultList || 0 == resultList.size()) {
//            return stateVoList;
//        } else {
//            ContainerStateRespVo containerStateVo;
//            for (Map<String, Object> m : resultList) {
//                ContainerStateType stateType = ContainerStateType.valueOf(((BigDecimal)m.get("contState")).intValue());
//                containerStateVo = new ContainerStateRespVo(((BigDecimal)m.get("contId")).longValue(), (String)m.get("contGbsId"), (String)m.get("ordeBillNo"), (String)m.get("contCaseNo"), ((BigDecimal)m.get("ordeType")).intValue(), stateType.getValue(), stateType.getDesc());
//                stateVoList.add(containerStateVo);
//            }
//        }
//        return stateVoList;
//    }

}
