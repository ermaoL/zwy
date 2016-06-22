package com.sinotrans.transport.service;

import com.sinotrans.transport.dto.PositionContainerResponse;
import com.sinotrans.transport.dto.PositionListRequest;
import com.sinotrans.transport.dto.PositionListResponse;

/**
 * Created by emi on 2016/6/15.
 */
public interface PositionService {

    PositionListResponse positionList(Long userId, PositionListRequest positionRequest);

    PositionContainerResponse containerPosition(Long userId, String containerGbsId);
}
