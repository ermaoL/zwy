package com.sinotrans.transport.controller;

import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.dto.common.CommonListResponse;
import com.sinotrans.transport.dto.common.CommonNoPageListResponse;
import com.sinotrans.transport.dto.common.RestResponse;
import com.sinotrans.transport.service.OrderImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;

/**
 * Created by emi on 2016/5/13.
 */
@Controller
@RequestMapping(value = "/api/import")
public class OrderImportController {

    @Autowired
    private OrderImportService importService;

    /**查询进口订单列表*/
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public CommonListResponse search(HttpServletRequest request, OrderImportSearchRequest searchRequest) { //list的data 见OrderImportSearchRespVo
        Long userId = (Long)request.getAttribute("userId");
        return importService.search(userId, searchRequest);
    }

    /**查询进口维护页面 初始所有数据*/
    @RequestMapping(value = "/order/{orderId:\\d+}/all", method = RequestMethod.GET)
    @ResponseBody
    public OrderImportAllResponse allInfo(HttpServletRequest request, @PathVariable("orderId") Long orderId) {
        Long userId = (Long)request.getAttribute("userId");
        return importService.searchAll(userId, orderId);
    }

    /** 创建和修改公用此接口， 区别在于Id是否为空*/
    /**创建或修改进口订单*/
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public OrderImportAlterResponse create(HttpServletRequest request, @Valid OrderImportAlterRequest alterRequest) {
        String orderIdStr = alterRequest.getOrderId();
        Long userId = (Long)request.getAttribute("userId");
        if (StringUtils.isEmpty(orderIdStr) || "0".equals(orderIdStr)) {
            return importService.create(userId, alterRequest);
        } else {
            return importService.alter(userId, Long.parseLong(orderIdStr), alterRequest);
        }
    }

    /**进口订单 下单(下发) 请求*/
    @RequestMapping(value = "/order/{orderId:\\d+}/sending", method = RequestMethod.POST)
    @ResponseBody
    public OrderImportAllResponse send(HttpServletRequest request, @PathVariable("orderId") Long orderId) {
        Long userId = (Long)request.getAttribute("userId");
        return importService.sendOrder(userId, orderId);
    }

    /**根据订单Id 查询容器列表*/
    @RequestMapping(value = "/order/{orderId:\\d+}/container", method = RequestMethod.GET)
    @ResponseBody
    public CommonNoPageListResponse searchContainer(HttpServletRequest request, @PathVariable("orderId") Long orderId) {
        Long userId = (Long)request.getAttribute("userId");
        return importService.searchContainer(userId, orderId);
    }

    /** 创建和修改公用此接口， 区别在于Id是否为空*/
    /**创建或保存容器*/
    @RequestMapping(value = "/container", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse modifyContainer(HttpServletRequest request, @Valid @RequestBody ContainerImportAlterRequest alterRequest) {
        Long userId = (Long)request.getAttribute("userId");
        return importService.modifyContainers(userId, alterRequest);
    }

    /**删除容器*/
    @RequestMapping(value = "/container/drop", method = RequestMethod.POST)
    @ResponseBody
    public ContainerDropResponse dropContainer(HttpServletRequest request, @RequestBody DomainsDropRequest dropRequest) {
        Long userId = (Long)request.getAttribute("userId");
        return importService.dropContainers(userId, dropRequest);
    }

    /**根据容器Id查询地址列表*/
    @RequestMapping(value = "/container/{containerId:\\d+}/address", method = RequestMethod.GET)
    @ResponseBody
    public CommonNoPageListResponse searchAddress(HttpServletRequest request, @PathVariable("containerId") Long containerId) {
        Long userId = (Long)request.getAttribute("userId");
        return importService.searchAddress(userId, containerId);
    }

    /** 创建和修改公用此接口， 区别在于Id是否为空*/
    /**创建修改地址*/
    @RequestMapping(value = "/address", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse modifyAddress(HttpServletRequest request, @Valid @RequestBody AddressImportAlterRequest alterRequest) {
        Long userId = (Long)request.getAttribute("userId");
        return importService.modifyAddress(userId, alterRequest);
    }

    /**删除地址*/
    @RequestMapping(value = "/address/drop", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse dropAddress(HttpServletRequest request, @Valid @RequestBody DomainsDropRequest dropRequest) {
        Long userId = (Long)request.getAttribute("userId");
        return importService.dropAddresses(userId, dropRequest);
    }


    @RequestMapping(value = "/test/xml", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse testXml(@RequestParam(value = "xmlFile", required = false) MultipartFile xmlFile, HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = xmlFile.getOriginalFilename();
        System.out.println(path);
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        //保存
        try {
            xmlFile.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RestResponse response = importService.textXml(path + "\\" + fileName);
        if (response.isSuccess()) {
            targetFile.delete();
        }
        return response;
    }
}
