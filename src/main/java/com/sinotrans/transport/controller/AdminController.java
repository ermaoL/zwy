package com.sinotrans.transport.controller;

import com.sinotrans.transport.common.enums.CompanyType;
import com.sinotrans.transport.common.util.IpUtils;
import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.dto.common.CommonListResponse;
import com.sinotrans.transport.dto.common.CommonNoPageListResponse;
import com.sinotrans.transport.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by emi on 2016/5/30.
 */
@Controller
@RequestMapping(value = "/base/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    /**裕雄 裕国  超管 登录接口 */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public AdminLoginResponse login(@Valid AdminLoginRequest loginRequest, HttpServletRequest request) {

        return adminService.login(loginRequest.getAdminName(), loginRequest.getAdminPass(), loginRequest.getAdminType(), IpUtils.getIpAddress(request));
    }

    /**查询进口订单  待审核  列表*/
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    @ResponseBody
    public CommonListResponse searchImport(HttpServletRequest request, OrderImportSearchRequest searchRequest) { //list的data 见OrderImportSearchRespVo

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.searchImport(companyType, searchRequest);
    }


    /**查询进口维护页面 初始所有数据*/
    @RequestMapping(value = "/import/{orderId:\\d+}/all", method = RequestMethod.GET)
    @ResponseBody
    public OrderImportAllResponse importAllInfo(HttpServletRequest request, @PathVariable("orderId") Long orderId) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.searchImportAll(companyType, orderId);
    }

    /**修改进口订单*/
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public OrderImportAlterResponse importModify(HttpServletRequest request, @Valid OrderImportAlterRequest alterRequest) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");
        return adminService.importModify(adminCode, companyType, alterRequest);
    }

    /** 创建和修改公用此接口， 区别在于Id是否为空*/
    /**创建或保存容器*/
    @RequestMapping(value = "/import/container", method = RequestMethod.POST)//newnew
    @ResponseBody
    public CommonNoPageListResponse modifyImportContainers(HttpServletRequest request, @Valid @RequestBody ContainerImportAlterRequest alterRequest) {
        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");
        return adminService.modifyImportContainers(adminCode, companyType, alterRequest);
    }

    /**删除容器*/
    @RequestMapping(value = "/import/container/drop", method = RequestMethod.POST)
    @ResponseBody
    public ContainerDropResponse dropImportContainers(HttpServletRequest request, @RequestBody DomainsDropRequest dropRequest) {
        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");
        return adminService.dropImportContainers(adminCode, companyType, dropRequest);
    }

    /**根据容器Id查询地址列表*/
    @RequestMapping(value = "/import/{containerId:\\d+}/address", method = RequestMethod.GET)
    @ResponseBody
    public CommonNoPageListResponse importAddress(HttpServletRequest request, @PathVariable("containerId") Long containerId) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.importAddress(containerId);
    }

    /** 创建和修改公用此接口， 区别在于Id是否为空*/
    /**创建修改地址*/
    @RequestMapping(value = "/import/address", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse modifyImportAddress(HttpServletRequest request, @Valid @RequestBody AddressImportAlterRequest alterRequest) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");
        return adminService.modifyImportAddress(adminCode, companyType, alterRequest);
    }

    /**删除地址*/
    @RequestMapping(value = "/import/address/drop", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse dropImportAddresses(HttpServletRequest request, @RequestBody DomainsDropRequest dropRequest) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");
        return adminService.dropImportAddresses(adminCode, companyType, dropRequest);
    }





    /**查询出口订单 待审核 列表*/
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public CommonListResponse searchExport(HttpServletRequest request, OrderExportSearchRequest searchRequest) { //list的data 见OrderImportSearchRespVo

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.searchExport(companyType, searchRequest);
    }

    /**查询进口维护页面 初始所有数据*/
    @RequestMapping(value = "/export/{orderId:\\d+}/all", method = RequestMethod.GET)
    @ResponseBody
    public OrderExportAllResponse exportAllInfo(HttpServletRequest request, @PathVariable("orderId") Long orderId) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.searchExportAll(companyType, orderId);
    }


    /**修改出口订单*/
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ResponseBody
    public OrderExportAlterResponse exportModify(HttpServletRequest request, @Valid OrderExportAlterRequest alterRequest) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.exportModify(adminCode, companyType, alterRequest);
    }

    /**创建或保存容器*/
    @RequestMapping(value = "/export/container", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse modifyExportContainer (HttpServletRequest request, @Valid @RequestBody ContainerExportAlterRequest alterRequest) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");
        return adminService.modifyExportContainer(adminCode, companyType, alterRequest);
    }

    /**删除容器*/
    @RequestMapping(value = "/export/container/drop", method = RequestMethod.POST)
    @ResponseBody
    public ContainerDropResponse dropExportContainers(HttpServletRequest request, @RequestBody DomainsDropRequest dropRequest) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");
        return adminService.dropExportContainers(adminCode, companyType, dropRequest);
    }

    /**根据容器Id查询地址列表*/
    @RequestMapping(value = "/export/{containerId:\\d+}/address", method = RequestMethod.GET)
    @ResponseBody
    public CommonNoPageListResponse exportAddress(HttpServletRequest request, @PathVariable("containerId") Long containerId) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.exportAddress(containerId);
    }

    /** 创建和修改公用此接口， 区别在于Id是否为空*/
    /**创建修改地址*/
    @RequestMapping(value = "/export/address", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse modifyExportAddress(HttpServletRequest request, @Valid @RequestBody AddressExportAlterRequest alterRequest) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");
        return adminService.modifyExportAddress(adminCode, companyType, alterRequest);
    }

    /**删除地址*/
    @RequestMapping(value = "/export/address/drop", method = RequestMethod.POST)
    @ResponseBody
    public CommonNoPageListResponse dropExportAddresses(HttpServletRequest request, @RequestBody DomainsDropRequest dropRequest) {

        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");
        return adminService.dropExportAddresses(adminCode, companyType, dropRequest);
    }




    /**审核 进出口 订单*/
    @RequestMapping(value = "/order/{orderId:\\d+}/verifier", method = RequestMethod.POST)
    @ResponseBody
    public OrderVerifierResponse verifier(HttpServletRequest request, @PathVariable("orderId") Long orderId, @Valid OrderVerifierRequest verifierRequest) {
        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.verifierOrder(adminCode, adminName, companyType, orderId, verifierRequest);
    }


    @RequestMapping(value = "/user/checking/search", method = RequestMethod.GET)
    @ResponseBody
    public CommonListResponse checkList(HttpServletRequest request, UserSearchRequest searchRequest) {
        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.checkList(adminCode, adminName, companyType, searchRequest);
    }

    @RequestMapping(value = "/user/{userId:\\d+}/verifier", method = RequestMethod.POST)
    @ResponseBody
    public UserVerifierResponse verifierUser(HttpServletRequest request, @PathVariable("userId")long userId, @Valid UserVerifierRequest verifierRequest) {
        String adminCode = (String)request.getAttribute("adminCode");
        String adminName = (String)request.getAttribute("adminName");
        CompanyType companyType = (CompanyType)request.getAttribute("companyType");

        return adminService.verifierUser(adminCode, adminName, companyType, userId, verifierRequest);
    }

}
