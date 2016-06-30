package com.sinotrans.transport.service;

import com.sinotrans.transport.dto.PositionContainerResponse;
import com.sinotrans.transport.dto.PositionListRequest;
import com.sinotrans.transport.dto.PositionListResponse;

/**
 * Created by emi on 2016/6/15.
 */
public interface PositionService {

//    PositionListResponse positionList(Long userId, PositionListRequest positionRequest);

    PositionListResponse positionList(Long userId, Long orderId);

    PositionListResponse positionContainerInfo(Long userId, Long containerId, String containerGbsId);

    PositionContainerResponse singleContainerPosition(Long userId, Long containerId);
}
