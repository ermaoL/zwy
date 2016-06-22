package com.sinotrans.transport.controller;

import com.sinotrans.transport.controller.handler.ExceptionController;
import com.sinotrans.transport.dto.PositionContainerResponse;
import com.sinotrans.transport.dto.PositionListRequest;
import com.sinotrans.transport.dto.PositionListResponse;
import com.sinotrans.transport.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by emi on 2016/6/14.
 */
@Controller
@RequestMapping(value = "/api/position")
public class PositionController extends ExceptionController {

    @Autowired
    private PositionService positionService;

    /** 整票跟踪坐标点 */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public PositionListResponse positionList(PositionListRequest positionRequest, HttpServletRequest request) {

        Long userId = (Long)request.getAttribute("userId");
        return positionService.positionList(userId, positionRequest);
    }

    @RequestMapping(value = "/container/{containerGbsId}", method = RequestMethod.GET)
    @ResponseBody
    public PositionContainerResponse containerPosition(@PathVariable("containerGbsId") String containerGbsId, HttpServletRequest request) {

        Long userId = (Long)request.getAttribute("userId");
        return positionService.containerPosition(userId, containerGbsId);
    }

//    @RequestMapping(value = "/search/current", method = RequestMethod.GET)
//    @ResponseBody
//    public PositionCurrentResponse current() {
//
//    }

}
