package com.sinotrans.transport.controller;

import com.sinotrans.transport.dto.out.*;
import com.sinotrans.transport.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by emi on 2016/5/20.
 */
@Controller
@RequestMapping(value = "/api")
public class BaseController {

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = "queryCrmTypeIdIsCU", method = RequestMethod.GET)
    @ResponseBody
    public QueryCrmResponse queryUser(String codeOrName) {
        return baseService.queryCustomer(codeOrName);
    }


    @RequestMapping(value = "queryCrmTypeIdIsCW", method = RequestMethod.GET)
    @ResponseBody
    public QueryCrmResponse queryContainerOwner(String codeOrName) {
        return baseService.queryContainerOwner(codeOrName);
    }

    @RequestMapping(value = "queryContainerType", method = RequestMethod.GET)
    @ResponseBody
    public QueryTypeCodeResponse queryContainerType() {
        return baseService.queryTypeCode("queryContainerType");
    }


    @RequestMapping(value = "queryContainerSize", method = RequestMethod.GET)
    @ResponseBody
    public QueryTypeCodeResponse queryContainerSize() {
        return baseService.queryTypeCode("queryContainerSize");
    }

    /**
     * 获取船名信息 */
    @RequestMapping(value = "queryVessel", method = RequestMethod.GET)
    @ResponseBody
    public QueryVesselResponse queryVessel(String codeOrEnname) {
        return baseService.queryVessel(codeOrEnname);
    }

    /**
     * 获取航次信息 */
    @RequestMapping(value = "queryVesselEtd", method = RequestMethod.GET)
    @ResponseBody
    public QueryVesselEtdResponse queryVesselEtd(String vesselCode, String voyage) {
        return baseService.queryVesselEtd(vesselCode, voyage);
    }

    /**
     * 获取港口信息 */
    @RequestMapping(value = "queryPortCode", method = RequestMethod.GET)
    @ResponseBody
    public QueryPortCodeResponse queryPortCode(String codeOrEnname) {
        return baseService.queryPortCode(codeOrEnname);
    }

    /**
     * 获取提箱点 返箱点信息 */
    @RequestMapping(value = "queryDepot", method = RequestMethod.GET)
    @ResponseBody
    public QueryDepotResponse queryDepot(String codeOrName) {
        return baseService.queryDepot(codeOrName);
    }

    @RequestMapping(value = "/queryBillList", method = RequestMethod.GET)
    @ResponseBody
    public QueryBillResponse queryBillList(QueryBillRequest billRequest) {
        return baseService.queryBillList(billRequest);
    }

}
