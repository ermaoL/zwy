package com.sinotrans.transport.controller;

import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.dto.common.CommonListResponse;
import com.sinotrans.transport.dto.common.CommonNoPageListResponse;
import com.sinotrans.transport.service.OrderExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by emi on 2016/5/13.
 */
@Controller
@RequestMapping(value = "/api/export")
public class OrderExportController {

    @Autowired
    private OrderExportService exportService;

    /**查询出口订单列表*/
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public CommonListResponse exportSearch(HttpServletRequest request, OrderExportSearchRequest searchRequest) {

        Long userId = (Long)request.getAttribute("userId");
        return exportService.search(userId, searchRequest);
    }

    /**查询出口维护页面 初始所有数据*/
    @RequestMapping(value = "/order/{orderId:\\d+}/all", method = RequestMethod.GET)
    @ResponseBody
    public OrderExportAllResponse allInfo(HttpServletRequest request, @PathVariable("orderId") Long orderId) {

        Long userId = (Long)request.getAttribute("userId");
        return exportService.searchAll(userId, orderId);
    }

    /**创建或修改出口订单*/
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public OrderExportAlterResponse create(HttpServletRequest request, @Valid OrderExportAlterRequest alterRequest) {

        Long userId = (Long)request.getAttribute("userId");
        String orderId = alterRequest.getOrderId();
        if (StringUtils.isEmpty(orderId) || "0".equals(orderId)) {
            return exportService.create(userId, alterRequest);
        } else {
            return exportService.alter(userId, Long.parseLong(orderId), alterRequest);
        }
    }

    /**出口订单 下单(下发) 请求*/
    @RequestMapping(value = "/order/{orderId:\\d+}/sending", method = RequestMethod.POST)
    @ResponseBody
    public OrderExportAllResponse send(HttpServletRequest request, @PathVariable("orderId") Long orderId) {
        Long userId = (Long)request.getAttribute("userId");
        return exportService.sendOrder(userId, orderId);
    }

    /**根据订单Id 查询容器列表*/
    @RequestMapping(value = "/order/{orderId:\\d+}/container", method = RequestMethod.GET)
    @ResponseBody
    public CommonNoPageListResponse searchContainer(HttpServletRequest request, @PathVariable("orderId") Long orderId) {

        Long userId = (Long)request.getAttribute("userId");
        return exportService.searchContainer(userId, orderId);
    }

    /**创建或保存容器*/
    @RequestMapping(value = "/container", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse modifyContainer (HttpServletRequest request, @Valid @RequestBody ContainerExportAlterRequest alterRequest) {

        Long userId = (Long)request.getAttribute("userId");
        return exportService.modifyContainer(userId, alterRequest);
    }

    /**删除容器*/
    @RequestMapping(value = "/container/drop", method = RequestMethod.POST)
    @ResponseBody
    public ContainerDropResponse dropContainer(HttpServletRequest request, @RequestBody DomainsDropRequest dropRequest) {

        Long userId = (Long)request.getAttribute("userId");
        return exportService.dropContainers(userId, dropRequest);
    }

    /**根据容器Id查询地址列表*/
    @RequestMapping(value = "/container/{containerId:\\d+}/address", method = RequestMethod.GET)
    @ResponseBody
    public CommonNoPageListResponse searchAddress(HttpServletRequest request, @PathVariable("containerId") Long containerId) {

        Long userId = (Long)request.getAttribute("userId");
        return exportService.searchAddress(userId, containerId);
    }

    /**创建修改地址*/
    @RequestMapping(value = "/address", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse modifyAddress(HttpServletRequest request, @Valid @RequestBody AddressExportAlterRequest alterRequest) {

        Long userId = (Long)request.getAttribute("userId");
        return exportService.modifyAddress(userId, alterRequest);
    }

    /**删除地址*/
    @RequestMapping(value = "/address/drop", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse dropAddress(HttpServletRequest request, @RequestBody DomainsDropRequest dropRequest) {

        Long userId = (Long)request.getAttribute("userId");
        return exportService.dropAddresses(userId, dropRequest);
    }
}
