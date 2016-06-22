package com.sinotrans.transport.service.impl;

import com.sinotrans.framework.mybatis.support.FilterCondition;
import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.support.PagingInfo;
import com.sinotrans.framework.service.mybatis.base.impl.MybatisManagerImpl;
import com.sinotrans.transport.common.CommonConstant;
import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.cache.AdminResultVo;
import com.sinotrans.transport.common.cache.SingletonCacheAdmin;
import com.sinotrans.transport.common.enums.*;
import com.sinotrans.transport.common.exception.*;
import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.dto.common.CommonListRequest;
import com.sinotrans.transport.dto.common.CommonListResponse;
import com.sinotrans.transport.dto.common.CommonNoPageListResponse;
import com.sinotrans.transport.dto.out.CheckerLoginResponse;
import com.sinotrans.transport.dto.out.OrderGbsSendRequest;
import com.sinotrans.transport.dto.out.OrderGbsSendResponse;
import com.sinotrans.transport.dto.out.vo.ContainerGbsSendReqVo;
import com.sinotrans.transport.dto.out.vo.OrderGbsSendReqVo;
import com.sinotrans.transport.dto.vo.*;
import com.sinotrans.transport.model.*;
import com.sinotrans.transport.service.AdminService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by emi on 2016/5/31.
 */
@Service
@Transactional
public class AdminServiceImpl extends MybatisManagerImpl implements AdminService {

//    @Value("${oms.admin.name}")
//    private String adminName_;
//
//    @Value("${oms.admin.pass}")
//    private String adminPass_;

//    @Value("${gbs.send.order.url}")
//    private String gbsSendOrderUrl;
//
//    @Value("${gbs.xmzz.login.url}")
//    private String gbsXmzzLoginUrl;
//    @Value("${gbs.fzzz.login.url}")
//    private String gbsFzzzLoginUrl;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH");
    Logger logger = Logger.getLogger(AdminServiceImpl.class);

    @Override
    public AdminLoginResponse login(String adminName, String adminPass, String adminType, String ipAddress) {

        int adminTypeInt = Integer.parseInt(adminType);
        AdminResultVo resultVo = null;
        if (CompanyType.AdminAll.getValue() == adminTypeInt) {
            if (!adminName.equals(CommonConstant.ADMIN_NAME)) {
                return new AdminLoginResponse(ErrorCode.USER_NAME_NOT_EXIST, "管理员名字不存在");
            }
            if (!adminPass.equals(CommonConstant.ADMIN_PASS)) {
                return new AdminLoginResponse(ErrorCode.USER_PASS_ERROR, "管理员密码错误");
            }
            resultVo = SingletonCacheAdmin.putUpdateData(adminName, CompanyType.AdminAll.getDesc(), CompanyType.valueOf(adminTypeInt), ipAddress);
        } else {
            RestTemplate restTemplate = new RestTemplate();
            String url;
            if (CompanyType.YuXiong.getValue() == adminTypeInt) {
                url = CommonConstant.GBS_FZZZ_LOGIN_URL;
            } else if (CompanyType.YuGuo.getValue() == adminTypeInt) {
                url = CommonConstant.GBS_XMZZ_LOGIN_URL;
            } else {
                throw new CommonException(ErrorCode.AUTH_TYPE_NOT_EXIST, "登录平台异常", null, null);
            }

//            Map<String, String> map = new HashMap<String, String>(2);
//            map.put("employeeCode", adminName);
//            map.put("psword", adminPass);
            CheckerLoginResponse response = restTemplate.getForObject(url + "?employeeCode=" + adminName + "&psword=" + adminPass, CheckerLoginResponse.class);
            if (!response.isSuccess()) {
                throw new CommonException(response.getErrorCode(), response.getErrorMsg(), "远程访问用户库异常，登录失败：" + response.getErrorMsg(), logger);
            }
            resultVo = SingletonCacheAdmin.putUpdateData(response.getEmployeeCode(), response.getEmployeeName(), CompanyType.valueOf(adminTypeInt), ipAddress);
        }
        return new AdminLoginResponse(resultVo.getAdminName(), resultVo.getToken(), resultVo.getAuthType(), resultVo.getNickName());
    }

    @Override
    public CommonListResponse searchImport(CompanyType companyType, OrderImportSearchRequest request) {

        String orderBy = "ordeId desc";//
        PagingInfo pagingInfo = this.fetchPagingInfo(request, companyType);
        List<OrderImportSearchRespVo> importSearchVoList;
        if (StringUtils.isEmpty(request.getContainerCaseNo())) {
            List<Order> orderList = this.myBatisDao.findByCondition(Order.class, orderBy, pagingInfo, request.fetchFilter(null, companyType));
            importSearchVoList = this.toImportOrderVoList(orderList);
        } else {
            List<Map<String,Object>> result = this.myBatisDao.queryByCondition("SearchImportOrderList", request.fetchCondition(), orderBy, pagingInfo, null);
            importSearchVoList = this.mapToImportVoList(result);
        }
        return new CommonListResponse(pagingInfo, importSearchVoList);
    }


    @Override
    public OrderImportAllResponse searchImportAll(CompanyType companyType, Long orderId) {
        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkBelongCompany(companyType, logger);

        OrderImportDetailRespVo orderVo = new OrderImportDetailRespVo(orderId, order.getOrdeBillNo(), order.getOrdeState(), order.getOrdeWharf(), order.getOrdeWharfCode(), order.getOrdeShipName(), order.getOrdeShipNameCode(), order.getOrdeSailing(), order.getOrdeSailingCode(), order.getOrdeOwnerCode(), order.getOrdeOwner(), order.getOrdeGoodOwner(), order.getOrdeGoodOwnerCode(), order.getOrdeShipAgency(), order.getOrdeArrivalDate(), order.getOrdeTotalCase(), order.getOrdeVerifierContent(), order.getOrdeRemark(), order.getOrdeConnects(), order.getOrdeConnectEmail(), order.getOrdeConnectPhone(), sdf);
        String orderBy =  "createTime asc";
        Long containerId = null;
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, null));
        List<ContainerImportSearchRespVo> containerVoList = this.toImportContainerVoList(containerList);
        List<AddressImportSearchRespVo> addressVoList;
        if (null == containerList || containerList.size() == 0) {
            addressVoList = new ArrayList<AddressImportSearchRespVo>(0);
        } else {
            containerId = containerList.get(0).getContId();
            List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
            addressVoList = this.toImportAddressVoList(addressList);
        }
        return new OrderImportAllResponse(orderVo, containerVoList, containerId, addressVoList);
    }

    @Override
    public OrderImportAlterResponse importModify(String adminCode, CompanyType companyType, OrderImportAlterRequest request) {

        if (StringUtils.isEmpty(request.getOrderId())) {
            throw new ParamNullException("订单Id", null);
        }
        Long orderId = Long.parseLong(request.getOrderId());
        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkState(logger, OrderStateType.Send);
        order.checkBelongCompany(companyType, logger);
        order = this.compareModifyImportOrder(request.getOrderBillNo(), request.getOrderWharf(), request.getOrderWharfCode(), request.getOrderShipName(), request.getOrderShipNameCode(), request.getOrderSailing(), request.getOrderSailingCode(), request.getOrderOwnerCode(), request.getOrderOwner(), request.getOrderGoodOwnerCode(), request.getOrderGoodOwner(), request.getOrderShipAgency(), request.getOrderArrivalDate(), request.getOrderTotalCase(), request.getOrderRemark(), request.getOrderConnects(), request.getOrderConnectEmail(), request.getOrderConnectPhone(), order);
        if (null != order) {
            order.setModifier(adminCode);
            myBatisDao.save(order);
        }
        return new OrderImportAlterResponse(orderId);
    }


    @Override
    public CommonNoPageListResponse modifyImportContainers(String adminCode, CompanyType companyType, ContainerImportAlterRequest request) {

        List<ContainerImportReqVo> containVoList = request.getContainerVoList();

        Long orderId = Long.parseLong(request.getContainerOrderId());
        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkState(logger, OrderStateType.Send);
        order.checkBelongCompany(companyType, logger);

        if (null == containVoList || containVoList.size() == 0) {
            return new CommonNoPageListResponse(new ArrayList<String>(0));
        }
        String containerIdStr;
        Container container;
        for (ContainerImportReqVo c : containVoList) {
            containerIdStr = c.getContainerId();
            if (StringUtils.isEmpty(containerIdStr) || "0".equals(containerIdStr)) {
                container = new Container(orderId, c.getContainerOwner(), c.getContainerOwnerCode(), c.getContainerCaseNo(), c.getContainerCount(), c.getContainerDrayageDepot(), c.getContainerDrayageDepotCode(), c.getContainerReturnDepot(), c.getContainerReturnDepotCode(), c.getContainerSealNo(), c.getContainerTradeName(), c.getContainerType(), c.getContainerCartonSize(), c.getContainerGrossWeight(), c.getContainerRfTemp(), c.getContainerTemperUnit(), c.getContainerDynamo(), c.getContainerIsMoreUnload(), c.getContainerCost(), c.getContainerOther(), c.getContainerRemark(), adminCode);
                container.setContState(ContainerStateType.ImportSend.getValue());
                myBatisDao.save(container);
            } else {
                container = myBatisDao.get(Container.class, Long.parseLong(containerIdStr));
                this.checkNull(DomainType.Container, container, container.getContId());
                container.checkDelete(logger);
//                container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
                container = this.compareModifyImportContainer(c.getContainerOwner(), c.getContainerOwnerCode(), c.getContainerCaseNo(), c.getContainerCount(), c.getContainerDrayageDepot(), c.getContainerDrayageDepotCode(), c.getContainerReturnDepot(), c.getContainerReturnDepotCode(), c.getContainerSealNo(), c.getContainerTradeName(), c.getContainerType(), c.getContainerCartonSize(), c.getContainerGrossWeight(), c.getContainerRfTemp(), c.getContainerTemperUnit(), c.getContainerDynamo(), c.getContainerIsMoreUnload(), c.getContainerCost(), c.getContainerOther(), c.getContainerRemark(), container);
                if (null != container) {
                    container.setModifier(String.valueOf(adminCode));
                    myBatisDao.update(container);
                }
            }
        }

        String orderBy = "createTime asc";//
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, null));
        List<ContainerImportSearchRespVo> containerVoList = this.toImportContainerVoList(containerList);
        return new CommonNoPageListResponse(containerVoList);
    }


    @Override
    public ContainerDropResponse dropImportContainers(String adminCode, CompanyType companyType, DomainsDropRequest dropRequest) {

        List<String> idStrList = dropRequest.getIdList();
        if (null == idStrList || 0 == idStrList.size()) {
            throw new CommonException(ErrorCode.DATA_EMPTY_TO_DELETE, "请选择要删除的数据", "删除进口容器接口，请选择要删除的数据", logger);
        }
        Long orderId = 0L;
        Container container;
        Long containerId;
        for (int i = 0; i < idStrList.size(); i ++) {
            try {
                containerId = Long.parseLong(idStrList.get(i));
            } catch (NumberFormatException e) {
                throw new CommonException(ErrorCode.DATA_FORMAT_ERROR, "数据格式异常", String.format("数据[%s]无法转为long型", idStrList.get(i)), logger);
            }
            container = myBatisDao.get(Container.class, containerId);
            this.checkNull(DomainType.Container, container, containerId);
//            container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
            container.setRecStatus(DomainStatus.Delete.getValue());
            container.setModifier(adminCode);
            myBatisDao.update(container);
            if (0 == i) {
                orderId = container.getContOrdeId();
            } else {
                if (container.getContOrdeId().longValue() != orderId.longValue()) {
                    throw new DataException("要删除的容器数据 不属于同一个订单","传入的要删除的容器数据" + idStrList + "， 不属于同一个订单", logger);
                }
            }
        }

        String orderBy = "createTime asc";//
        containerId = null;//first container Id
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, null));
        List<ContainerImportSearchRespVo> containerVoList = this.toImportContainerVoList(containerList);
        List<AddressImportSearchRespVo> addressVoList;
        if (null == containerList || containerList.size() == 0) {
            addressVoList = new ArrayList<AddressImportSearchRespVo>(0);
        } else {
            containerId = containerList.get(0).getContId();
            List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
            addressVoList = this.toImportAddressVoList(addressList);
        }

//        Order order = myBatisDao.get(Order.class, orderId);
//        order.setOrdeTotalCase(containerList.size());
//        myBatisDao.update(order);

        return new ContainerDropResponse(containerId, containerVoList, addressVoList);
    }

    @Override
    public CommonNoPageListResponse modifyImportAddress(String adminCode, CompanyType companyType, AddressImportAlterRequest request) {

        List<AddressImportReqVo> addressVoList = request.getAddressVoList();
        if (null == addressVoList || addressVoList.size() == 0) {
            return new CommonNoPageListResponse(new ArrayList<AddressImportSearchRespVo>(0));
        }
        Long containerId = Long.parseLong(request.getContainerId());
        Container container = myBatisDao.get(Container.class, containerId);
        this.checkNull(DomainType.Container, container, containerId);
        container.checkState(logger, ContainerStateType.ImportSend);
        container.checkDelete(logger);
//        container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽

        String addressIdStr;
        Address address;
        for (AddressImportReqVo a : addressVoList) {
            addressIdStr = a.getAddressId();
            if (StringUtils.isEmpty(addressIdStr) || "0".equals(addressIdStr)) {
                address = new Address(containerId, a.getAddressPlace(), a.getAddressDeliveryTime(), a.getAddressContacts(), a.getAddressTel(), a.getAddressProvince(), a.getAddressCity(), a.getAddressDistrict(), adminCode, sdf3);
                myBatisDao.save(address);
            } else {
                address = myBatisDao.get(Address.class, Long.parseLong(addressIdStr));
                this.checkNull(DomainType.Address, address, address.getAddrId());
                address.checkDelete(logger);
//                address.checkBelongCreator(userId, logger);////现在管理员也能创建了,所以屏蔽
                address = this.compareModifyImportAddress(a.getAddressPlace(), a.getAddressDeliveryTime(), a.getAddressContacts(), a.getAddressTel(), a.getAddressProvince(), a.getAddressCity(), a.getAddressDistrict(), address);
                if (null != address) {
                    address.setModifier(adminCode);
                    myBatisDao.update(address);
                }
            }
        }
        String orderBy = "createTime asc";
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
        List<AddressImportSearchRespVo> resultVoList = this.toImportAddressVoList(addressList);
        return new CommonNoPageListResponse(resultVoList);
    }


    @Override
    public CommonNoPageListResponse dropImportAddresses(String adminCode, CompanyType companyType, DomainsDropRequest dropRequest) {

        List<String> idStrList = dropRequest.getIdList();
        if (null == idStrList || 0 == idStrList.size()) {
            throw new CommonException(ErrorCode.DATA_EMPTY_TO_DELETE, "请选择要删除的数据", "删除进口地址接口，请选择要删除的数据", logger);
        }
        Long containerId = 0L;
        Address address;
        Long addressId;
        for (int i = 0; i < idStrList.size(); i ++) {
            try {
                addressId = Long.parseLong(idStrList.get(i));
            } catch (NumberFormatException e) {
                throw new CommonException(ErrorCode.DATA_FORMAT_ERROR, "数据格式异常", String.format("数据[%s]无法转为long型", idStrList.get(i)), logger);
            }
            address = myBatisDao.get(Address.class, addressId);
            this.checkNull(DomainType.Address, address, addressId);
//            address.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
            address.setRecStatus(DomainStatus.Delete.getValue());
            address.setModifier(adminCode);
            myBatisDao.update(address);
            if (0 == i) {
                containerId = address.getAddrContId();
            } else {
                if (address.getAddrContId().longValue() != containerId.longValue()) {
                    throw new DataException("要删除的地址数据 不属于同一个容器","传入的要删除的地址数据" + idStrList + "， 不属于同一个容器", logger);
                }
            }

        }
        String orderBy = "createTime asc";
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
        List<AddressImportSearchRespVo> addressVoList = this.toImportAddressVoList(addressList);
        return new CommonNoPageListResponse(addressVoList);
    }

    @Override
    public CommonNoPageListResponse importAddress(Long containerId) {
        String orderBy = "createTime asc";
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
        List<AddressImportSearchRespVo> addressVoList = this.toImportAddressVoList(addressList);
        return new CommonNoPageListResponse(addressVoList);
    }




    @Override
    public CommonListResponse searchExport(CompanyType companyType, OrderExportSearchRequest request) {
        String orderBy = "ordeId desc";//
        PagingInfo pagingInfo = this.fetchPagingInfo(request, companyType);
        List<OrderExportSearchRespVo> exportSearchVoList;
        if (StringUtils.isEmpty(request.getContainerCaseNo())) {
            List<Order> orderList = this.myBatisDao.findByCondition(Order.class, orderBy, pagingInfo, request.fetchFilter(null, companyType));
            exportSearchVoList = this.toExportOrderVoList(orderList);
        } else {
            List<Map<String,Object>> result = this.myBatisDao.queryByCondition("SearchExportOrderList", request.fetchCondition(), orderBy, pagingInfo, null);
            exportSearchVoList = this.mapToExportVoList(result);
        }
        return new CommonListResponse(pagingInfo, exportSearchVoList);
    }


    @Override
    public OrderExportAllResponse searchExportAll(CompanyType companyType, Long orderId) {
        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkBelongCompany(companyType, logger);

        OrderExportDetailRespVo orderVo = new OrderExportDetailRespVo(orderId, order.getOrdeBillNo(), order.getOrdeState(), order.getOrdeWharf(),order.getOrdeWharfCode(), order.getOrdeShipName(), order.getOrdeShipNameCode(), order.getOrdeSailing(), order.getOrdeSailingCode(), order.getOrdeShipAgency(), order.getOrdeSailingDate(), order.getOrdeEnterPort(), order.getOrdeEnterPortCode(), order.getOrdeDestinationPort(), order.getOrdeDestinationPortCode(), order.getOrdeShippingCode(), order.getOrdeBookingNumber(), order.getOrdeTotalCase(), order.getOrdeOwner(), order.getOrdeOwnerCode(), order.getOrdeGoodOwner(), order.getOrdeGoodOwnerCode(), order.getOrdeVerifierContent(), order.getOrdeRemark(), order.getOrdeConnects(), order.getOrdeConnectEmail(), order.getOrdeConnectPhone(), sdf);
        String orderBy =  "createTime asc";
        Long containerId = null;
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, null));
        List<ContainerExportSearchRespVo> containerVoList = this.toExportContainerVoList(containerList);
        List<AddressExportSearchRespVo> addressVoList;
        if (null == containerList || containerList.size() == 0) {
            addressVoList = new ArrayList<AddressExportSearchRespVo>(0);
        } else {
            containerId = containerList.get(0).getContId();
            List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
            addressVoList = this.toExportAddressVoList(addressList);
        }
        return new OrderExportAllResponse(orderVo, containerVoList, containerId, addressVoList);
    }

    @Override
    public OrderExportAlterResponse exportModify(String adminCode, CompanyType companyType, OrderExportAlterRequest request) {

        if (StringUtils.isEmpty(request.getOrderId())) {
            throw new ParamNullException("订单Id", null);
        }
        Long orderId = Long.parseLong(request.getOrderId());
        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkState(logger, OrderStateType.Send);
        order.checkBelongCompany(companyType, logger);
        order = this.compareModifyExportOrder(request.getOrderBillNo(), request.getOrderWharf(), request.getOrderWharfCode(), request.getOrderShipName(), request.getOrderShipNameCode(), request.getOrderSailing(), request.getOrderSailingCode(), request.getOrderShipAgency(), request.getOrderSailingDate(), request.getOrderEnterPort(), request.getOrderEnterPortCode(), request.getOrderDestinationPort(), request.getOrderDestinationPortCode(), request.getOrderShippingCode(), request.getOrderBookingNumber(), request.getOrderTotalCase(), request.getOrderOwner(), request.getOrderOwnerCode(), request.getOrderGoodOwner(), request.getOrderGoodOwnerCode(), request.getOrderRemark(), request.getOrderConnects(), request.getOrderConnectEmail(), request.getOrderConnectPhone(), order);
        if (null != order) {
            order.setModifier(adminCode);
            myBatisDao.update(order);
        }
        return new OrderExportAlterResponse(orderId);
    }


    @Override
    public CommonNoPageListResponse modifyExportContainer(String adminCode, CompanyType companyType, ContainerExportAlterRequest request) {

        List<ContainerExportReqVo> containVoList = request.getContainerVoList();
        if (null == containVoList || containVoList.size() == 0) {
            return new CommonNoPageListResponse(new ArrayList<ContainerExportSearchRespVo>(0));
        }
        Long orderId = Long.parseLong(request.getContainerOrderId());
        Order order = myBatisDao.get(Order.class, orderId);
        order.checkDelete(logger);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkState(logger, OrderStateType.Send);
        order.checkBelongCompany(companyType, logger);

        String containerIdStr;
        Container container;
        for (ContainerExportReqVo c : containVoList) {
            containerIdStr = c.getContainerId();
            if (StringUtils.isEmpty(containerIdStr) || "0".equals(containerIdStr)) {
                container = new Container(c.getContainerOwner(), c.getContainerOwnerCode(), orderId, c.getContainerCaseNo(), c.getContainerCount(), c.getContainerDrayageDepot(), c.getContainerDrayageDepotCode(), c.getContainerReturnDepot(), c.getContainerReturnDepotCode(), c.getContainerGrossWeight(), c.getContainerRfTemp(), c.getContainerTemperUnit(), c.getContainerType(), c.getContainerCartonSize(), c.getContainerDynamo(), c.getContainerIsMoreUnload(), c.getContainerIsOverWeight(), c.getContainerIsCheck(), c.getContainerIsCheckliest(), c.getContainerIsReplace(), c.getContainerOther(), c.getContainerRemark(), adminCode);
                container.setContState(ContainerStateType.ExportSend.getValue());
                myBatisDao.save(container);
            } else {
                container = myBatisDao.get(Container.class, Long.parseLong(containerIdStr));
                this.checkNull(DomainType.Container, container, container.getContId());
                container.checkDelete(logger);
//                container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
                container = this.compareModifyExportContainer(c.getContainerOwner(), c.getContainerOwnerCode(), c.getContainerCaseNo(), c.getContainerCount(), c.getContainerDrayageDepot(), c.getContainerDrayageDepotCode(), c.getContainerReturnDepot(), c.getContainerReturnDepotCode(), c.getContainerGrossWeight(), c.getContainerRfTemp(), c.getContainerTemperUnit(), c.getContainerType(), c.getContainerCartonSize(), c.getContainerIsOverWeight(), c.getContainerIsCheck(), c.getContainerIsCheckliest(), c.getContainerIsReplace(), c.getContainerDynamo(), c.getContainerIsMoreUnload(), c.getContainerOther(), c.getContainerRemark(), container);
                if (null != container) {
                    order.setModifier(adminCode);
                    myBatisDao.update(container);
                }
            }
        }

        String orderBy = "createTime asc";//
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, null));
        List<ContainerExportSearchRespVo> containerVoList = this.toExportContainerVoList(containerList);
        return new CommonNoPageListResponse(containerVoList);
    }


    @Override
    public ContainerDropResponse dropExportContainers(String adminCode, CompanyType companyType, DomainsDropRequest dropRequest) {

        List<String> idStrList = dropRequest.getIdList();
        if (null == idStrList || 0 == idStrList.size()) {
            throw new CommonException(ErrorCode.DATA_EMPTY_TO_DELETE, "请选择要删除的数据", "删除进口容器接口，请选择要删除的数据", logger);
        }
        Long orderId = 0L;
        Container container;
        Long containerId;
        for (int i = 0; i < idStrList.size(); i ++) {
            try {
                containerId = Long.parseLong(idStrList.get(i));
            } catch (NumberFormatException e) {
                throw new CommonException(ErrorCode.DATA_FORMAT_ERROR, "数据格式异常", String.format("数据[%s]无法转为long型", idStrList.get(i)), logger);
            }
            container = myBatisDao.get(Container.class, containerId);
            this.checkNull(DomainType.Container, container, containerId);
//            container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
            container.setModifier(adminCode);
            container.setRecStatus(DomainStatus.Delete.getValue());
            myBatisDao.update(container);
            if (0 == i) {
                orderId = container.getContOrdeId();
            } else {
                if (container.getContOrdeId().longValue() != orderId.longValue()) {
                    throw new DataException("要删除的容器数据 不属于同一个订单","传入的要删除的容器数据" + idStrList + "， 不属于同一个订单", logger);
                }
            }
        }
        String orderBy = "createTime asc";//
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, null));
        List<ContainerExportSearchRespVo> containerVoList = this.toExportContainerVoList(containerList);
        containerId = null;//first container Id
        List<AddressExportSearchRespVo> addressVoList;
        if (null == containerList || containerList.size() == 0) {
            addressVoList = new ArrayList<AddressExportSearchRespVo>(0);
        } else {
            containerId = containerList.get(0).getContId();
            List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
            addressVoList = this.toExportAddressVoList(addressList);
        }

//        Order order = myBatisDao.get(Order.class, orderId);
//        order.setOrdeTotalCase(containerList.size());
//        myBatisDao.update(order);
        return new ContainerDropResponse(containerId, containerVoList, addressVoList);
    }

    @Override
    public CommonNoPageListResponse modifyExportAddress(String adminCode, CompanyType companyType, AddressExportAlterRequest request) {

        List<AddressExportReqVo> addressVoList = request.getAddressVoList();
        if (null == addressVoList || addressVoList.size() == 0) {
            return new CommonNoPageListResponse(new ArrayList<AddressExportSearchRespVo>(0));
        }
        Long containerId = Long.parseLong(request.getContainerId());
        Container container = myBatisDao.get(Container.class, containerId);
        this.checkNull(DomainType.Container, container, containerId);
        container.checkDelete(logger);
        container.checkState(logger, ContainerStateType.ExportSend);
//        container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽

        String addressIdStr;
        Address address;
        for (AddressExportReqVo a : addressVoList) {
            addressIdStr = a.getAddressId();
            if (StringUtils.isEmpty(addressIdStr) || "0".equals(addressIdStr)) {
                address = new Address(sdf3, containerId, a.getAddressPlace(), a.getAddressArriveTime(), a.getAddressContacts(), a.getAddressTel(), a.getAddressProvince(), a.getAddressCity(), a.getAddressDistrict(), adminCode);
                myBatisDao.save(address);
            } else {
                address = myBatisDao.get(Address.class, Long.parseLong(addressIdStr));
                this.checkNull(DomainType.Address, address, address.getAddrId());
                address.checkDelete(logger);
//                address.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
                address = this.compareModifyExportAddress(a.getAddressPlace(), a.getAddressArriveTime(), a.getAddressContacts(), a.getAddressTel(), a.getAddressProvince(), a.getAddressCity(), a.getAddressDistrict(), address);
                if (null != address) {
                    address.setModifier(adminCode);
                    myBatisDao.update(address);
                }
            }
        }
        String orderBy = "createTime asc";
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
        List<AddressExportSearchRespVo> resultVoList = this.toExportAddressVoList(addressList);
        return new CommonNoPageListResponse(resultVoList);
    }


    @Override
    public CommonNoPageListResponse dropExportAddresses(String adminCode, CompanyType companyType, DomainsDropRequest dropRequest) {

        List<String> idStrList = dropRequest.getIdList();
        if (null == idStrList || 0 == idStrList.size()) {
            throw new CommonException(ErrorCode.DATA_EMPTY_TO_DELETE, "请选择要删除的数据", "删除进口地址接口，请选择要删除的数据", logger);
        }
        Long containerId = 0L;
        Address address;
        Long addressId;
        for (int i = 0; i < idStrList.size(); i ++) {
            try {
                addressId = Long.parseLong(idStrList.get(i));
            } catch (NumberFormatException e) {
                throw new CommonException(ErrorCode.DATA_FORMAT_ERROR, "数据格式异常", String.format("数据[%s]无法转为long型", idStrList.get(i)), logger);
            }
            address = myBatisDao.get(Address.class, addressId);
            this.checkNull(DomainType.Address, address, address.getAddrId());
//            address.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
            address.setModifier(adminCode);
            address.setRecStatus(DomainStatus.Delete.getValue());
            myBatisDao.update(address);
            if (0 == i) {
                containerId = address.getAddrContId();
            } else {
                if (address.getAddrContId().longValue() != containerId.longValue()) {
                    throw new DataException("要删除的地址数据 不属于同一个容器","传入的要删除的地址数据" + idStrList + "， 不属于同一个容器", logger);
                }
            }
        }
        String orderBy = "createTime asc";
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
        List<AddressExportSearchRespVo> addressVoList = this.toExportAddressVoList(addressList);
        return new CommonNoPageListResponse(addressVoList);
    }

    @Override
    public CommonNoPageListResponse exportAddress(Long containerId) {
        String orderBy = "createTime asc";
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, null));
        List<AddressExportSearchRespVo> addressVoList = this.toExportAddressVoList(addressList);
        return new CommonNoPageListResponse(addressVoList);
    }

    @Override
    public OrderVerifierResponse verifierOrder(String adminCode, String adminName, CompanyType companyType, Long orderId, OrderVerifierRequest request) {

        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkBelongCompany(companyType, logger);
        order.checkState(logger, OrderStateType.Send);        //todo:测试阶段关闭

        List<Container> containerList = myBatisDao.findByCondition(Container.class, "contId asc", null, this.fetchContainerOrderIdFilter(orderId, null));
        if (Integer.parseInt(request.getIfPass()) != 1) {

            this.updateContainerCheckFailList(containerList, adminCode, OrderType.valueOf(order.getOrdeType()));
            String verifier = order.verifyFailed(companyType, adminCode, adminName, request.getFailedReason());
            OrderCheck orderCheck = new OrderCheck(orderId, verifier, CheckStateType.Failed.getValue(), request.getFailedReason(), adminCode);
            myBatisDao.update(order);
            myBatisDao.save(orderCheck);
        } else {
            User user = myBatisDao.get(User.class, Long.parseLong(order.getCreator()));
            OrderGbsSendRequest sendRequest = this.fetchSbsSendInfo(order, user.getUserCompanyCode(), user.getUserCompany(), containerList, adminCode);
            RestTemplate restTemplate = new RestTemplate();//todo: 抽离出来
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity<OrderGbsSendRequest> requestEntity = new HttpEntity<OrderGbsSendRequest>(sendRequest, headers);
            ResponseEntity<OrderGbsSendResponse> responseEntity = null;
            try {
                responseEntity = restTemplate.postForEntity(CommonConstant.GBS_SEND_ORDER_URL, requestEntity, OrderGbsSendResponse.class);
            } catch (RuntimeException e) {
                throw new RemoteServerException(OperateType.SentOrder, logger);
            }
            OrderGbsSendResponse sendResponse = responseEntity.getBody();
            if (sendResponse.isSuccess()) {

                this.updateContainerGbsList(containerList, sendResponse.getContainerMap(), adminCode, OrderType.valueOf(order.getOrdeType()));
                String verifier = order.verifySuccess(companyType, adminCode, adminName, sendResponse.getOrderGbsDocCode());
                OrderCheck orderCheck = new OrderCheck(orderId, verifier, CheckStateType.Pass.getValue(), null, adminCode);
                myBatisDao.update(order);
                myBatisDao.save(orderCheck);
            } else {
                return new OrderVerifierResponse(sendResponse.getErrorCode(), sendResponse.getErrorMsg());
            }

        }
        return new OrderVerifierResponse();
    }

    @Override
    public CommonListResponse checkList(String adminCode, String adminName, CompanyType companyType, UserSearchRequest searchRequest) {

        PagingInfo pagingInfo = this.fetchUserPagingInfo(searchRequest);
        List<User> userList = myBatisDao.findByCondition(User.class, "createTime desc", pagingInfo, searchRequest.fetchFilter());
        List<UserSearchResponseVo> responseVoList = this.toUserVoList(userList);
        return new CommonListResponse(pagingInfo, responseVoList);
    }


    @Override
    public UserVerifierResponse verifierUser(String adminCode, String adminName, CompanyType companyType, long userId, UserVerifierRequest request) {

        User user = myBatisDao.get(User.class, userId);
        this.checkNull(DomainType.User, user, userId);
        user.checkState(logger, UserStateType.Checking);
        if (Integer.parseInt(request.getIfPass()) == 1) {
            Customer customer = new Customer(userId, CompanyType.valueOf(Integer.parseInt(request.getTransCompany())));
            myBatisDao.save(customer);
            user.verifyUserPass(adminCode, request.getBelongCompanyCode(), request.getBelongCompany());
        } else {
            user.verifyUserFail(adminCode, request.getFailedReason());
        }
        myBatisDao.update(user);
        return new UserVerifierResponse();
    }

    private List<UserSearchResponseVo> toUserVoList(List<User> userList) {

        List<UserSearchResponseVo> responseVoList = new ArrayList<UserSearchResponseVo>();
        if (null == userList) {
            return responseVoList;
        }
        User user;
        UserSearchResponseVo userVo;
        for (int i = 0; i < userList.size(); i ++) {
            user = userList.get(i);
            userVo = new UserSearchResponseVo(i + 1, user.getUserId(), user.getUserName(), user.getUserTel(), user.getUserCompany());
            responseVoList.add(userVo);
        }
        return responseVoList;
    }


    private void updateContainerCheckFailList(List<Container> containerList, String adminCode, OrderType orderType) {
        ContainerStateType containerStateType = orderType == OrderType.Import ? ContainerStateType.ImportCheckFailed: ContainerStateType.ExportCheckFailed;
        for (Container c : containerList) {
            c.setContState(containerStateType.getValue());
            c.setModifier(adminCode);
            myBatisDao.update(c);
        }
    }

    private void updateContainerGbsList(List<Container> containerList, Map<String, String> containerMap, String adminCode, OrderType orderType) {

        String key;
        String containerDocCode;
        ContainerStateType containerStateType = orderType == OrderType.Import ? ContainerStateType.ImportChecked : ContainerStateType.ExportChecked;
        for (Container c : containerList) {
            key = String.valueOf(c.getContId());
            if (containerMap.containsKey(key)) {
                containerDocCode = containerMap.get(key);
                c.setContGbsId(containerDocCode);
                c.setContState(containerStateType.getValue());
                c.setModifier(adminCode);
                myBatisDao.update(c);
            } else {
                throw new CommonException(ErrorCode.REMOTE_DATA_ERROR, "GBS返回数据异常","GBS下发返回数据异常,缺失容器[" + key + "]的gbs主键数据", logger);
            }
        }
    }

    private OrderGbsSendRequest fetchSbsSendInfo(Order order, String companyCode, String companyName, List<Container> containerList, String adminCode) {

        Date orderDate = OrderType.Import.getValue() == order.getOrdeType() ? order.getOrdeArrivalDate() : order.getOrdeSailingDate();
        List<ContainerGbsSendReqVo> containerVoList = null;
        OrderGbsSendReqVo orderVo = new OrderGbsSendReqVo(order.getOrdeId(), order.getOrdeShipNameCode(), order.getOrdeSailing(), order.getOrdeWharfCode(), order.getOrdeBillNo(), order.getOrdePortCode(), order.getOrdePort(), order.getOrdeDestinationPortCode(), order.getOrdeDestinationPort(), orderDate, order.getOrdeType(), CompanyType.codeOf(order.getOrdeTranCompany()), order.getOrdeGoodOwnerCode(), order.getOrdeOwnerCode(), order.getCreator(), order.getCreateTime(), adminCode);
        if (null == containerList || 0 == containerList.size()) {
            containerVoList = new ArrayList<ContainerGbsSendReqVo>(0);
        } else {
            containerVoList = new ArrayList<ContainerGbsSendReqVo>(containerList.size());
            ContainerGbsSendReqVo containerVo = null;
            if (OrderType.Import.getValue() == order.getOrdeType()) {
                for (Container c : containerList) {
                    containerVo = new ContainerGbsSendReqVo(c.getContId(), c.getContCaseNo(), c.getContSealNo(), c.getContOwnerCode(), c.getContType(), c.getContCartonSize(), 0, 0, c.getContGrossWeight(), c.getContRfTemp(), c.getContTemperUnit(), c.getContDrayageDepotCode(), c.getContReturnDepotCode());
                    containerVoList.add(containerVo);
                }
            } else {
                for (Container c : containerList) {
                    int containerIsCheck = "是".equals(c.getContIsCheck()) ? 1 : 0;
                    int containerIsOverWeight = "是".equals(c.getContIsOverWeight()) ? 1 : 0;
                    containerVo = new ContainerGbsSendReqVo(c.getContId(), c.getContCaseNo(), c.getContSealNo(), c.getContOwnerCode(), c.getContType(), c.getContCartonSize(), containerIsCheck, containerIsOverWeight, c.getContGrossWeight(), c.getContRfTemp(), c.getContTemperUnit(), c.getContDrayageDepotCode(), c.getContReturnDepotCode());
                    containerVoList.add(containerVo);
                }
            }
        }
        return new OrderGbsSendRequest(companyCode, companyName, orderVo, containerVoList);
    }

    private void checkNull(DomainType domain, BaseModel model, Long domainId) {
        if (null == model) {
            throw new RecordNotFoundException(domain, domainId, logger);
        }
    }


    private List<FilterCondition> fetchContainerOrderIdFilter(Long orderId, Long userId) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("contOrdeId", orderId, "="));
        if (null != userId && 0 != userId) {
            filter.add(new FilterCondition("creator", String.valueOf(userId), "="));
        }
        filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));//likeAnywhere
        return filter;
    }




    private List<FilterCondition> fetchAddressContainerId(Long containerId, Long userId) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("addrContId", containerId, "="));
        if (null != userId && 0 != userId) {
            filter.add(new FilterCondition("creator", String.valueOf(userId), "="));
        }
        filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));//likeAnywhere
        return filter;
    }


    private List<FilterCondition> fetchCustomerByUser(long userId, boolean checkStatus) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("custUserId", userId, "="));
        if (checkStatus) {
            filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));//likeAnywhere
        }
        return filter;
    }


    private PagingInfo fetchUserPagingInfo(UserSearchRequest request) {

        PagingInfo pagingInfo = request.fetchPagingInfo();
        if(pagingInfo != null) {
            int counts = this.myBatisDao.getRowCountByCondition(User.class, request.fetchFilter());
            pagingInfo.setTotalRows(counts);
        }
        return pagingInfo;
    }

    private PagingInfo fetchPagingInfo(CommonListRequest request, CompanyType companyType) {
        PagingInfo pagingInfo = request.fetchPagingInfo();
        if(pagingInfo != null) {
            int counts = 0;
            try {
                counts = this.myBatisDao.getRowCountByCondition(Order.class, request.fetchFilter(null, companyType));
            } catch (ParseException e) {
                throw new DateFormatException(sdf, logger);
            }
            pagingInfo.setTotalRows(counts);
        }
        return pagingInfo;
    }


    private List<OrderImportSearchRespVo> toImportOrderVoList(List<Order> orderList) {

        if (null == orderList || 0 == orderList.size()) {
            return new ArrayList<OrderImportSearchRespVo>(0);
        }
        Order order;
        OrderImportSearchRespVo orderVo;
        List<OrderImportSearchRespVo> searchVoList = new ArrayList<OrderImportSearchRespVo>(orderList.size());
        for (int i = 0; i < orderList.size(); i ++) {
            order = orderList.get(i);
            orderVo = new OrderImportSearchRespVo(i + 1, order.getOrdeId(), order.getOrdeBillNo(), order.getOrdeWharf(), order.getOrdeShipName(), order.getOrdeSailing(), order.getOrdeState(), order.getOrdeArrivalDate(), order.getOrdeShipAgency(), order.getOrdeTotalCase(), sdf);
            searchVoList.add(orderVo);
        }
        return searchVoList;
    }


    private List<OrderImportSearchRespVo> mapToImportVoList(List<Map<String, Object>> result) {
        if (null == result || 0 == result.size()) {
            return new ArrayList<OrderImportSearchRespVo>(0);
        }
        Map<String, Object> m;
        OrderImportSearchRespVo exportVo;
        List<OrderImportSearchRespVo> exportSearchVoList = new ArrayList<OrderImportSearchRespVo>(result.size());
        for (int i = 0; i < result.size(); i ++) {
            m = result.get(i);
            exportVo = new OrderImportSearchRespVo(i + 1,((BigDecimal)m.get("ordeId")).longValue(), (String)m.get("ordeBillNo"), (String)m.get("ordeWharf"), (String)m.get("ordeShipName"), (String)m.get("ordeSailing"), ((BigDecimal)m.get("ordeState")).intValue(), (Date)m.get("ordeArrivalDate"), (String)m.get("ordeShipAgency"), ((BigDecimal)m.get("ordeTotalCase")).intValue(), sdf);
            exportSearchVoList.add(exportVo);
        }
        return exportSearchVoList;
    }

    private List<ContainerImportSearchRespVo> toImportContainerVoList(List<Container> containerList) {

        if (null == containerList || 0 == containerList.size()) {
            return new ArrayList<ContainerImportSearchRespVo>(0);
        }
        Container container;
        ContainerImportSearchRespVo containerVo;
        List<ContainerImportSearchRespVo> searchVoList = new ArrayList<ContainerImportSearchRespVo>(containerList.size());
        for (int i = 0; i < containerList.size(); i++) {
            container = containerList.get(i);
            containerVo = new ContainerImportSearchRespVo(i + 1, container.getContId(), container.getContOwner(), container.getContOwnerCode(), container.getContCaseNo(), container.getContCount(), container.getContDrayageDepot(), container.getContDrayageDepotCode(), container.getContReturnDepot(), container.getContReturnDepotCode(), container.getContSealNo(), container.getContTradeName(), container.getContType(), container.getContCartonSize(), container.getContGrossWeight(), container.getContRfTemp(), container.getContTemperUnit(), container.getContDynamo(), container.getContIsMoreUnload(), container.getContCost(), container.getContState());
            searchVoList.add(containerVo);
        }
        return searchVoList;
    }

    private List<AddressImportSearchRespVo> toImportAddressVoList(List<Address> addressList) {

        if (null == addressList || 0 == addressList.size()) {
            return new ArrayList<AddressImportSearchRespVo>(0);
        }
        Address address;
        AddressImportSearchRespVo addressVo;
        List<AddressImportSearchRespVo> searchVoList = new ArrayList<AddressImportSearchRespVo>(addressList.size());
        for (int i = 0; i < addressList.size(); i++) {
            address = addressList.get(i);
            addressVo = new AddressImportSearchRespVo(i + 1, address.getAddrId(), address.getAddrPlace(), address.getAddrDeliveryTime(), address.getAddrContacts(), address.getAddrTel(), address.getAddrProvince(), address.getAddrCity(), address.getAddrDistrict(), sdf3);
            searchVoList.add(addressVo);
        }
        return searchVoList;
    }

    private List<OrderExportSearchRespVo> toExportOrderVoList(List<Order> orderList) {

        if (null == orderList || 0 == orderList.size()) {
            return new ArrayList<OrderExportSearchRespVo>(0);
        }
        Order order;
        OrderExportSearchRespVo exportVo;
        List<OrderExportSearchRespVo> searchVoList = new ArrayList<OrderExportSearchRespVo>(orderList.size());
        for (int i = 0; i < orderList.size(); i ++) {
            order = orderList.get(i);
            exportVo = new OrderExportSearchRespVo(i + 1, order.getOrdeId(), order.getOrdeBillNo(), order.getOrdeWharf(), order.getOrdeShipName(), order.getOrdeSailing(), order.getOrdeState(), order.getOrdeSailingDate(), order.getOrdeEnterPort(), order.getOrdeDestinationPort(), order.getOrdeShippingCode(), sdf);
            searchVoList.add(exportVo);
        }
        return searchVoList;
    }


    private List<OrderExportSearchRespVo> mapToExportVoList(List<Map<String, Object>> result) {
        if (null == result || 0 == result.size()) {
            return new ArrayList<OrderExportSearchRespVo>(0);
        }
        Map<String, Object> m;
        OrderExportSearchRespVo exportVo;
        List<OrderExportSearchRespVo> exportSearchVoList = new ArrayList<OrderExportSearchRespVo>(result.size());
        for (int i = 0; i < result.size(); i ++) {
            m = result.get(i);
            exportVo = new OrderExportSearchRespVo(i + 1,((BigDecimal)m.get("ordeId")).longValue(), (String)m.get("ordeBillNo"), (String)m.get("ordeWharf"), (String)m.get("ordeShipName"), (String)m.get("ordeSailing"), ((BigDecimal)m.get("ordeState")).intValue(), (Date)m.get("ordeSailingDate"), (String)m.get("ordeEnterPort"), (String)m.get("ordeDestinationPort"), (String)m.get("ordeShippingCode"), sdf);
            exportSearchVoList.add(exportVo);
        }
        return exportSearchVoList;
    }


    private List<ContainerExportSearchRespVo> toExportContainerVoList(List<Container> containerList) {

        if (null == containerList || containerList.size() == 0) {
            return new ArrayList<ContainerExportSearchRespVo>(0);
        }
        Container container;
        ContainerExportSearchRespVo containerVo;
        List<ContainerExportSearchRespVo> searchVoList = new ArrayList<ContainerExportSearchRespVo>(containerList.size());
        for (int i = 0; i < containerList.size(); i++) {
            container = containerList.get(i);
            containerVo = new ContainerExportSearchRespVo(i + 1, container.getContId(), container.getContOwner(), container.getContOwnerCode(), container.getContCaseNo(), container.getContCount(), container.getContDrayageDepot(), container.getContDrayageDepotCode(), container.getContReturnDepot(), container.getContReturnDepotCode(), container.getContGrossWeight(), container.getContRfTemp(), container.getContTemperUnit(), container.getContType(), container.getContCartonSize(),  container.getContIsOverWeight(), container.getContIsCheck(), container.getContIsCheckliest(), container.getContIsReplace(), container.getContDynamo(), container.getContIsMoreUnload(), container.getContState(), container.getContOther(), container.getContRemark());
            searchVoList.add(containerVo);
        }
        return searchVoList;
    }

    private List<AddressExportSearchRespVo> toExportAddressVoList(List<Address> addressList) {

        if (null == addressList || 0 == addressList.size()) {
            return new ArrayList<AddressExportSearchRespVo>(addressList.size());
        }
        Address address;
        AddressExportSearchRespVo addressVo;
        List<AddressExportSearchRespVo> searchVoList = new ArrayList<AddressExportSearchRespVo>(addressList.size());
        for (int i = 0; i < addressList.size(); i++) {
            address = addressList.get(i);
            addressVo = new AddressExportSearchRespVo(i + 1, address.getAddrId(), address.getAddrPlace(), address.getAddrArriveTime(), address.getAddrContacts(), address.getAddrTel(), address.getAddrProvince(), address.getAddrCity(), address.getAddrDistrict(), sdf3);
            searchVoList.add(addressVo);
        }
        return searchVoList;
    }



    private Order compareModifyImportOrder(String orderBillNo, String orderWharf, String orderWharfCode, String orderShipName, String orderShipNameCode, String orderSailing, String orderSailingCode, String orderOwnerCode, String orderOwner, String orderGoodOwnerCode, String orderGoodOwner, String orderShipAgency, String orderArrivalDate, String orderTotalCase, String orderRemark, String orderConnects, String orderConnectEmail, String orderConnectPhone, Order order) {
        boolean change = false;
        if (!orderBillNo.equals(order.getOrdeBillNo())) {
            order.setOrdeBillNo(orderBillNo);
            change = true;
        }

        if (StringUtils.isEmpty(orderWharf)) {
            if (!StringUtils.isEmpty(order.getOrdeWharf())) {
                order.setOrdeWharf(null);
                change = true;
            }
        } else if (!orderWharf.equals(order.getOrdeWharf())) {
            order.setOrdeWharf(orderWharf);
            change = true;
        }

        if (StringUtils.isEmpty(orderWharfCode)) {
            if (!StringUtils.isEmpty(order.getOrdeWharfCode())) {
                order.setOrdeWharfCode(null);
                change = true;
            }
        } else if (!orderWharfCode.equals(order.getOrdeWharfCode())) {
            order.setOrdeWharfCode(orderWharfCode);
            change = true;
        }


        if (StringUtils.isEmpty(orderShipName)) {//
            if (!StringUtils.isEmpty(order.getOrdeShipName())) {
                order.setOrdeShipName(null);
                change = true;
            }
        } else if (!orderShipName.equals(order.getOrdeShipName())) {
            order.setOrdeShipName(orderShipName);
            change = true;
        }

        if (StringUtils.isEmpty(orderShipNameCode)) {
            if (!StringUtils.isEmpty(order.getOrdeShipNameCode())) {
                order.setOrdeShipNameCode(null);
                change = true;
            }
        } else if (!orderShipNameCode.equals(order.getOrdeShipNameCode())) {
            order.setOrdeShipNameCode(orderShipNameCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderSailing)) {
            if (!StringUtils.isEmpty(order.getOrdeSailing())) {
                order.setOrdeSailing(null);
                change = true;
            }
        } else if (!orderSailing.equals(order.getOrdeSailing())) {
            order.setOrdeSailing(orderSailing);
            change = true;
        }

        if (StringUtils.isEmpty(orderSailingCode)) {
            if (!StringUtils.isEmpty(order.getOrdeSailingCode())) {
                order.setOrdeSailingCode(null);
                change = true;
            }
        } else if (!orderSailingCode.equals(order.getOrdeSailingCode())) {
            order.setOrdeSailingCode(orderSailingCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderOwnerCode)) {
            if (!StringUtils.isEmpty(order.getOrdeOwnerCode())) {
                order.setOrdeOwnerCode(null);
                change = true;
            }
        } else if (!orderOwnerCode.equals(order.getOrdeOwnerCode())) {
            order.setOrdeOwnerCode(orderOwnerCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderOwner)) {
            if (!StringUtils.isEmpty(order.getOrdeOwner())) {
                order.setOrdeOwner(null);
                change = true;
            }
        } else if (!orderOwner.equals(order.getOrdeOwner())) {
            order.setOrdeOwner(orderOwner);
            change = true;
        }

        if (StringUtils.isEmpty(orderGoodOwnerCode)) {
            if (!StringUtils.isEmpty(order.getOrdeGoodOwnerCode())) {
                order.setOrdeGoodOwnerCode(null);
                change = true;
            }
        } else if (!orderGoodOwnerCode.equals(order.getOrdeGoodOwnerCode())) {
            order.setOrdeGoodOwnerCode(orderGoodOwnerCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderGoodOwner)) {
            if (!StringUtils.isEmpty(order.getOrdeGoodOwner())) {
                order.setOrdeGoodOwner(null);
                change = true;
            }
        } else if (!orderGoodOwner.equals(order.getOrdeGoodOwner())) {
            order.setOrdeGoodOwner(orderGoodOwner);
            change = true;
        }

        if (StringUtils.isEmpty(orderShipAgency)) {
            if (!StringUtils.isEmpty(order.getOrdeShipAgency())) {
                order.setOrdeShipAgency(null);
                change = true;
            }
        } else if (!orderShipAgency.equals(order.getOrdeShipAgency())) {
            order.setOrdeShipAgency(orderShipAgency);
            change = true;
        }

        if (StringUtils.isEmpty(orderArrivalDate)) {
            if (null != order.getOrdeArrivalDate()) {
                order.setOrdeArrivalDate(null);
                change = true;
            }
        } else if (null == order.getOrdeArrivalDate()) {
            try {
                order.setOrdeArrivalDate(sdf.parse(orderArrivalDate));
            } catch (ParseException e) {
                throw new DateFormatException(sdf, logger);
            }
            change = true;
        } else if (!orderArrivalDate.equals(sdf.format(order.getOrdeArrivalDate()))) {
            try {
                order.setOrdeArrivalDate(sdf.parse(orderArrivalDate));
            } catch (ParseException e) {
                throw new DateFormatException(sdf, logger);
            }
            change = true;
        }

//        if (!orderFreeUseDay.equals(order.getOrdeFreeUseDay())) {
//            order.setOrdeFreeUseDay(orderFreeUseDay);
//            change = true;
//        }

        if (StringUtils.isEmpty(orderTotalCase)) {
            if (0 != order.getOrdeTotalCase().doubleValue()) {
                order.setOrdeTotalCase(0);
                change = true;
            }
        } else if (!orderTotalCase.equals(String.valueOf(order.getOrdeTotalCase()))) {
            order.setOrdeTotalCase(Integer.parseInt(orderTotalCase));
            change = true;
        }

//        if (!orderSurplusTank.equals(String.valueOf(order.getOrdeSurplusTank()))) {
//            order.setOrdeSurplusTank(Integer.parseInt(orderSurplusTank));
//            change = true;
//        }
        if (StringUtils.isEmpty(orderRemark)) {
            if (!StringUtils.isEmpty(order.getOrdeRemark())) {
                order.setOrdeRemark(null);
                change = true;
            }
        } else if (!orderRemark.equals(order.getOrdeRemark())) {
            order.setOrdeRemark(orderRemark);
            change = true;
        }


        if (StringUtils.isEmpty(orderConnects)) {
            if (!StringUtils.isEmpty(order.getOrdeConnects())) {
                order.setOrdeConnects(null);
                change = true;
            }
        } else if (!orderConnects.equals(order.getOrdeConnects())) {
            order.setOrdeConnects(orderConnects);
            change = true;
        }

        if (StringUtils.isEmpty(orderConnectEmail)) {
            if (!StringUtils.isEmpty(order.getOrdeConnectEmail())) {
                order.setOrdeConnectEmail(null);
                change = true;
            }
        } else if (!orderConnectEmail.equals(order.getOrdeConnectEmail())) {
            order.setOrdeConnectEmail(orderConnectEmail);
            change = true;
        }

        if (StringUtils.isEmpty(orderConnectPhone)) {
            if (!StringUtils.isEmpty(order.getOrdeConnectPhone())) {
                order.setOrdeConnectPhone(null);
                change = true;
            }
        } else if (!orderConnectPhone.equals(order.getOrdeConnectPhone())) {
            order.setOrdeConnectPhone(orderConnectPhone);
            change = true;
        }
        if (change) {
            return order;
        } else {
            return null;
        }
    }

    private Container compareModifyImportContainer(String containerOwner, String containerOwnerCode, String containerCaseNo, String containerCount, String containerDrayageDepot, String containerDrayageDepotCode, String containerReturnDepot, String containerReturnDepotCode, String containerSealNo, String containerTradeName, String containerType, String containerCartonSize, String containerGrossWeight, String containerRfTemp, String containerTemperUnit, String containerDynamo, String containerIsMoreUnload, String containerCost, String containerOther, String containerRemark, Container container) {
        boolean change = false;
        if (StringUtils.isEmpty(containerOwner)) {
            if (!StringUtils.isEmpty(container.getContOwner())) {
                container.setContOwner(null);
                change = true;
            }
        } else if (!containerOwner.equals(container.getContOwner())) {
            container.setContOwner(containerOwner);
            change = true;
        }

        if (StringUtils.isEmpty(containerOwnerCode)) {
            if (!StringUtils.isEmpty(container.getContOwnerCode())) {
                container.setContOwnerCode(null);
                change = true;
            }
        }else if (!containerOwnerCode.equals(container.getContOwnerCode())) {
            container.setContOwnerCode(containerOwnerCode);
            change = true;
        }

        if (StringUtils.isEmpty(containerCaseNo)) {
            if (!StringUtils.isEmpty(container.getContCaseNo())) {
                container.setContCaseNo(null);
                change = true;
            }
        }else if (!containerCaseNo.equals(container.getContCaseNo())) {
            container.setContCaseNo(containerCaseNo);
            change = true;
        }

        if (StringUtils.isEmpty(containerDrayageDepot)) {
            if (!StringUtils.isEmpty(container.getContDrayageDepot())) {
                container.setContDrayageDepot(null);
                change = true;
            }
        }else if (!containerDrayageDepot.equals(container.getContDrayageDepot())) {
            container.setContDrayageDepot(containerDrayageDepot);
            change = true;
        }

        if (StringUtils.isEmpty(containerDrayageDepotCode)) {
            if (!StringUtils.isEmpty(container.getContDrayageDepotCode())) {
                container.setContDrayageDepotCode(null);
                change = true;
            }
        }else if (!containerDrayageDepotCode.equals(container.getContDrayageDepotCode())) {
            container.setContDrayageDepotCode(containerDrayageDepotCode);
            change = true;
        }

        if (StringUtils.isEmpty(containerReturnDepot)) {
            if (!StringUtils.isEmpty(container.getContReturnDepot())) {
                container.setContReturnDepot(null);
                change = true;
            }
        }else if (!containerReturnDepot.equals(container.getContReturnDepot())) {
            container.setContReturnDepot(containerReturnDepot);
            change = true;
        }

        if (StringUtils.isEmpty(containerReturnDepotCode)) {
            if (!StringUtils.isEmpty(container.getContReturnDepotCode())) {
                container.setContReturnDepotCode(null);
                change = true;
            }
        }else if (!containerReturnDepotCode.equals(container.getContReturnDepotCode())) {
            container.setContReturnDepotCode(containerReturnDepotCode);
            change = true;
        }

        if (StringUtils.isEmpty(containerCount)) {
            if (!StringUtils.isEmpty(String.valueOf(container.getContCount()))) {
                container.setContCount(null);
                change = true;
            }
        }else if (!containerCount.equals(String.valueOf(container.getContCount()))) {
            container.setContCount(Integer.parseInt(containerCount));
            change = true;
        }

        if (StringUtils.isEmpty(containerSealNo)) {
            if (!StringUtils.isEmpty(container.getContSealNo())) {
                container.setContSealNo(null);
                change = true;
            }
        }else if (!containerSealNo.equals(container.getContSealNo())) {
            container.setContSealNo(containerSealNo);
            change = true;
        }

        if (StringUtils.isEmpty(containerTradeName)) {
            if (!StringUtils.isEmpty(container.getContTradeName())) {
                container.setContTradeName(null);
                change = true;
            }
        }else if (!containerTradeName.equals(container.getContTradeName())) {
            container.setContTradeName(containerTradeName);
            change = true;
        }

        if (StringUtils.isEmpty(containerType)) {
            if (!StringUtils.isEmpty(container.getContType())) {
                container.setContType(null);
                change = true;
            }
        }else if (!containerType.equals(container.getContType())) {
            container.setContType(containerType);
            change = true;
        }

        if (StringUtils.isEmpty(containerCartonSize)) {
            if (!StringUtils.isEmpty(container.getContCartonSize())) {
                container.setContCartonSize(null);
                change = true;
            }
        }else if (!containerCartonSize.equals(container.getContCartonSize())) {
            container.setContCartonSize(containerCartonSize);
            change = true;
        }

        if (StringUtils.isEmpty(containerGrossWeight)) {
            if (!StringUtils.isEmpty(String.valueOf(container.getContGrossWeight()))) {
                container.setContGrossWeight(null);
                change = true;
            }
        }else if (!containerGrossWeight.equals(String.valueOf(container.getContGrossWeight()))) {
            container.setContGrossWeight(Double.parseDouble(containerGrossWeight));
            change = true;
        }

        if (StringUtils.isEmpty(containerRfTemp)) {
            if (!StringUtils.isEmpty(String.valueOf(container.getContRfTemp()))) {
                container.setContRfTemp(null);
                change = true;
            }
        }else if (!containerRfTemp.equals(String.valueOf(container.getContRfTemp()))) {
            container.setContRfTemp(Integer.parseInt(containerRfTemp));
            change = true;
        }

        if (StringUtils.isEmpty(containerTemperUnit)) {
            if (!StringUtils.isEmpty(String.valueOf(container.getContTemperUnit()))) {
                container.setContTemperUnit(null);
                change = true;
            }
        }else if (!containerTemperUnit.equals(String.valueOf(container.getContTemperUnit()))) {
            container.setContTemperUnit(Integer.parseInt(containerTemperUnit));
            change = true;
        }

        if (StringUtils.isEmpty(containerDynamo)) {
            if (!StringUtils.isEmpty(container.getContDynamo())) {
                container.setContDynamo(null);
                change = true;
            }
        }else if (!containerDynamo.equals(container.getContDynamo())) {
            container.setContDynamo(containerDynamo);
            change = true;
        }

        if (StringUtils.isEmpty(containerIsMoreUnload)) {
            if (!StringUtils.isEmpty(container.getContIsMoreUnload())) {
                container.setContIsMoreUnload(null);
                change = true;
            }
        }else if (!containerIsMoreUnload.equals(container.getContIsMoreUnload())) {
            container.setContIsMoreUnload(containerIsMoreUnload);
            change = true;
        }

        if (StringUtils.isEmpty(containerCost)) {
            if (!StringUtils.isEmpty(String.valueOf(container.getContCost()))) {
                container.setContCost(null);
                change = true;
            }
        }else if (!containerCost.equals(String.valueOf(container.getContCost()))) {
            container.setContCost(new BigDecimal(containerCost).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            change = true;
        }

        if (StringUtils.isEmpty(containerOther)) {
            if (!StringUtils.isEmpty(container.getContOther())) {
                container.setContOther(null);
                change = true;
            }
        } else if (!containerOther.equals(container.getContOther())) {
            container.setContOther(containerOther);
            change = true;
        }

        if (StringUtils.isEmpty(containerRemark)) {
            if (!StringUtils.isEmpty(container.getContRemark())) {
                container.setContRemark(containerRemark);
                change = true;
            }
        } else if (!containerRemark.equals(container.getContRemark())) {
            container.setContRemark(containerRemark);
            change = true;
        }
        if (change) {
            return container;
        } else {
            return null;
        }
    }

    private Address compareModifyImportAddress(String addressPlace, String addressDeliveryTime, String addressContacts, String addressTel, String addressProvince, String addressCity, String addressDistrict, Address address) {
        boolean change = false;
        if (StringUtils.isEmpty(addressPlace)) {
            if (!StringUtils.isEmpty(address.getAddrPlace())) {
                address.setAddrPlace(null);
                change = true;
            }
        } else if (!addressPlace.equals(address.getAddrPlace())) {
            address.setAddrPlace(addressPlace);
            change = true;
        }

        if (StringUtils.isEmpty(addressDeliveryTime)) {
            if (null != address.getAddrDeliveryTime()) {
                address.setAddrDeliveryTime(null);
                change = true;
            }
        } else if (null == address.getAddrDeliveryTime()) {
            try {
                address.setAddrDeliveryTime(sdf3.parse(addressDeliveryTime));
            } catch (ParseException e) {
                throw new DateFormatException(sdf3, logger);
            }
            change = true;
        } else if (!addressDeliveryTime.equals(sdf3.format(address.getAddrDeliveryTime()))) {
            try {
                address.setAddrDeliveryTime(sdf3.parse(addressDeliveryTime));
            } catch (ParseException e) {
                throw new DateFormatException(sdf3, logger);
            }
            change = true;
        }

        if (StringUtils.isEmpty(addressContacts)) {
            if (!StringUtils.isEmpty(address.getAddrContacts())) {
                address.setAddrContacts(null);
                change = true;
            }
        } else if (!addressContacts.equals(address.getAddrContacts())) {
            address.setAddrContacts(addressContacts);
            change = true;
        }

        if (StringUtils.isEmpty(addressTel)) {
            if (!StringUtils.isEmpty(address.getAddrTel())) {
                address.setAddrTel(null);
                change = true;
            }
        } else if (!addressTel.equals(address.getAddrTel())) {
            address.setAddrTel(addressTel);
            change = true;
        }

        if (StringUtils.isEmpty(addressProvince)) {
            if (!StringUtils.isEmpty(address.getAddrProvince())) {
                address.setAddrProvince(null);
                change = true;
            }
        } else if (!addressProvince.equals(address.getAddrProvince())) {
            address.setAddrProvince(addressProvince);
            change = true;
        }

        if (StringUtils.isEmpty(addressCity)) {
            if (!StringUtils.isEmpty(address.getAddrCity())) {
                address.setAddrCity(null);
                change = true;
            }
        } else if (!addressCity.equals(address.getAddrCity())) {
            address.setAddrCity(addressCity);
            change = true;
        }

        if (StringUtils.isEmpty(addressDistrict)) {
            if (!StringUtils.isEmpty(address.getAddrDistrict())) {
                address.setAddrDistrict(null);
                change = true;
            }
        } else if (!addressDistrict.equals(address.getAddrDistrict())) {
            address.setAddrDistrict(addressDistrict);
            change = true;
        }
        if (change) {
            return address;
        } else {
            return null;
        }
    }

    private Order compareModifyExportOrder(String orderBillNo, String orderWharf, String orderWharfCode, String orderShipName, String orderShipNameCode, String orderSailing, String orderSailingCode, String orderShipAgency, String orderSailingDate, String orderEnterPort, String orderEnterPortCode, String orderDestinationPort, String orderDestinationPortCode, String orderShippingCode, String orderBookingNumber, String orderTotalCase, String orderOwner, String orderOwnerCode, String orderGoodOwner, String orderGoodOwnerCode, String orderRemark, String orderConnects, String orderConnectEmail, String orderConnectPhone, Order order) {
        boolean change = false;
        if (StringUtils.isEmpty(orderBillNo)) {
            if (!StringUtils.isEmpty(order.getOrdeBillNo())) {
                order.setOrdeBillNo(null);
                change = true;
            }
        } else if (!orderBillNo.equals(order.getOrdeBillNo())) {
            order.setOrdeBillNo(orderBillNo);
            change = true;
        }
//        if (!orderCompany.equals(String.valueOf(order.getOrdeCompany()))) {
//            order.setOrdeCompany(Integer.parseInt(orderCompany));
//            change = true;
//        }
        if (StringUtils.isEmpty(orderWharf)) {
            if (!StringUtils.isEmpty(order.getOrdeWharf())) {
                order.setOrdeWharf(null);
                change = true;
            }
        } else if (!orderWharf.equals(order.getOrdeWharf())) {
            order.setOrdeWharf(orderWharf);
            change = true;
        }

        if (StringUtils.isEmpty(orderWharfCode)) {
            if (!StringUtils.isEmpty(order.getOrdeWharfCode())) {
                order.setOrdeWharfCode(null);
                change = true;
            }
        } else if (!orderWharfCode.equals(order.getOrdeWharfCode())) {
            order.setOrdeWharfCode(orderWharfCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderShipName)) {
            if (!StringUtils.isEmpty(order.getOrdeShipName())) {
                order.setOrdeShipName(null);
                change = true;
            }
        } else if (!orderShipName.equals(order.getOrdeShipName())) {
            order.setOrdeShipName(orderShipName);
            change = true;
        }

        if (StringUtils.isEmpty(orderShipNameCode)) {
            if (!StringUtils.isEmpty(order.getOrdeShipNameCode())) {
                order.setOrdeShipNameCode(null);
                change = true;
            }
        } else if (!orderShipNameCode.equals(order.getOrdeShipNameCode())) {
            order.setOrdeShipNameCode(orderShipNameCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderSailing)) {
            if (!StringUtils.isEmpty(order.getOrdeSailing())) {
                order.setOrdeSailing(null);
                change = true;
            }
        } else if (!orderSailing.equals(order.getOrdeSailing())) {
            order.setOrdeSailing(orderSailing);
            change = true;
        }

        if (StringUtils.isEmpty(orderSailingCode)) {
            if (!StringUtils.isEmpty(order.getOrdeSailingCode())) {
                order.setOrdeSailingCode(null);
                change = true;
            }
        } else if (!orderSailingCode.equals(order.getOrdeSailingCode())) {
            order.setOrdeSailingCode(orderSailingCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderShipAgency)) {
            if (!StringUtils.isEmpty(order.getOrdeShipAgency())) {
                order.setOrdeShipAgency(null);
                change = true;
            }
        } else if (!orderShipAgency.equals(order.getOrdeShipAgency())) {
            order.setOrdeShipAgency(orderShipAgency);
            change = true;
        }

        if (StringUtils.isEmpty(orderSailingDate)) {
            if (null != order.getOrdeSailingDate()) {
                order.setOrdeSailingDate(null);
                change = true;
            }
        } else if (null == order.getOrdeSailingDate()) {
            try {
                order.setOrdeSailingDate(sdf.parse(orderSailingDate));
            } catch (ParseException e) {
                throw new DateFormatException(sdf, logger);
            }
            change = true;
        } else if (!orderSailingDate.equals(sdf.format(order.getOrdeSailingDate()))) {
            try {
                order.setOrdeSailingDate(sdf.parse(orderSailingDate));
            } catch (ParseException e) {
                throw new DateFormatException(sdf, logger);
            }
            change = true;
        }
//        if (!orderPort.equals(order.getOrdePort())) {
//            order.setOrdePort(orderPort);
//            change = true;
//        }
//        if (!orderPortCode.equals(order.getOrdePortCode())) {
//            order.setOrdePortCode(orderPortCode);
//            change = true;
//        }

        if (StringUtils.isEmpty(orderEnterPort)) {
            if (!StringUtils.isEmpty(order.getOrdeEnterPort())) {
                order.setOrdeEnterPort(null);
                change = true;
            }
        } else if (!orderEnterPort.equals(order.getOrdeEnterPort())) {
            order.setOrdeEnterPort(orderEnterPort);
            change = true;
        }

        if (StringUtils.isEmpty(orderEnterPortCode)) {
            if (!StringUtils.isEmpty(order.getOrdeEnterPortCode())) {
                order.setOrdeEnterPortCode(null);
                change = true;
            }
        } else if (!orderEnterPortCode.equals(order.getOrdeEnterPortCode())) {
            order.setOrdeEnterPortCode(orderEnterPortCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderDestinationPort)) {
            if (!StringUtils.isEmpty(order.getOrdeDestinationPort())) {
                order.setOrdeDestinationPort(null);
                change = true;
            }
        } else if (!orderDestinationPort.equals(order.getOrdeDestinationPort())) {
            order.setOrdeDestinationPort(orderDestinationPort);
            change = true;
        }

        if (StringUtils.isEmpty(orderDestinationPortCode)) {
            if (!StringUtils.isEmpty(order.getOrdeDestinationPortCode())) {
                order.setOrdeDestinationPortCode(null);
                change = true;
            }
        } else if (!orderDestinationPortCode.equals(order.getOrdeDestinationPortCode())) {
            order.setOrdeDestinationPortCode(orderDestinationPortCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderShippingCode)) {
            if (!StringUtils.isEmpty(order.getOrdeShippingCode())) {
                order.setOrdeShippingCode(null);
                change = true;
            }
        } else if (!orderShippingCode.equals(order.getOrdeShippingCode())) {
            order.setOrdeShippingCode(orderShippingCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderBookingNumber)) {
            if (!StringUtils.isEmpty(order.getOrdeBookingNumber())) {
                order.setOrdeBookingNumber(null);
                change = true;
            }
        } else if (!orderBookingNumber.equals(order.getOrdeBookingNumber())) {
            order.setOrdeBookingNumber(orderBookingNumber);
            change = true;
        }

        if (StringUtils.isEmpty(orderTotalCase)) {
            if (!StringUtils.isEmpty(String.valueOf(order.getOrdeTotalCase()))) {
                order.setOrdeTotalCase(null);
                change = true;
            }
        } else if (!(Integer.parseInt(orderTotalCase) == order.getOrdeTotalCase().intValue())) {
            order.setOrdeTotalCase(Integer.parseInt(orderTotalCase));
            change = true;
        }

        if (StringUtils.isEmpty(orderOwner)) {
            if (!StringUtils.isEmpty(order.getOrdeOwner())) {
                order.setOrdeOwner(null);
                change = true;
            }
        } else if (!orderOwner.equals(order.getOrdeOwner())) {
            order.setOrdeOwner(orderOwner);
            change = true;
        }

        if (StringUtils.isEmpty(orderOwnerCode)) {
            if (!StringUtils.isEmpty(order.getOrdeOwnerCode())) {
                order.setOrdeOwnerCode(null);
                change = true;
            }
        } else if (!orderOwnerCode.equals(order.getOrdeOwnerCode())) {
            order.setOrdeOwnerCode(orderOwnerCode);
            change = true;
        }

        if (StringUtils.isEmpty(orderGoodOwner)) {
            if (!StringUtils.isEmpty(order.getOrdeGoodOwner())) {
                order.setOrdeGoodOwner(null);
                change = true;
            }
        } else if (!orderGoodOwner.equals(order.getOrdeGoodOwner())) {
            order.setOrdeGoodOwner(orderGoodOwner);
            change = true;
        }

        if (StringUtils.isEmpty(orderGoodOwnerCode)) {
            if (!StringUtils.isEmpty(order.getOrdeGoodOwnerCode())) {
                order.setOrdeGoodOwnerCode(null);
                change = true;
            }
        } else if (!orderGoodOwnerCode.equals(order.getOrdeGoodOwnerCode())) {
            order.setOrdeGoodOwnerCode(orderGoodOwnerCode);
            change = true;
        }
//        if (!orderFreeUseDay.equals(order.getOrdeFreeUseDay())) {
//            order.setOrdeFreeUseDay(orderFreeUseDay);
//            change = true;
//        }
//        if (!orderCheckContainer.equals(order.getOrdeCheckContainer())) {
//            order.setOrdeCheckContainer(orderCheckContainer);
//            change = true;
//        }

        if (StringUtils.isEmpty(orderRemark)) {
            if (!StringUtils.isEmpty(order.getOrdeRemark())) {
                order.setOrdeRemark(null);
                change = true;
            }
        } else if (!orderRemark.equals(order.getOrdeRemark())) {
            order.setOrdeRemark(orderRemark);
            change = true;
        }


        if (StringUtils.isEmpty(orderConnects)) {
            if (!StringUtils.isEmpty(order.getOrdeConnects())) {
                order.setOrdeConnects(null);
                change = true;
            }
        } else if (!orderConnects.equals(order.getOrdeConnects())) {
            order.setOrdeConnects(orderConnects);
            change = true;
        }

        if (StringUtils.isEmpty(orderConnectEmail)) {
            if (!StringUtils.isEmpty(order.getOrdeConnectEmail())) {
                order.setOrdeConnectEmail(null);
                change = true;
            }
        } else if (!orderConnectEmail.equals(order.getOrdeConnectEmail())) {
            order.setOrdeConnectEmail(orderConnectEmail);
            change = true;
        }

        if (StringUtils.isEmpty(orderConnectPhone)) {
            if (!StringUtils.isEmpty(order.getOrdeConnectPhone())) {
                order.setOrdeConnectPhone(null);
                change = true;
            }
        } else if (!orderConnectPhone.equals(order.getOrdeConnectPhone())) {
            order.setOrdeConnectPhone(orderConnectPhone);
            change = true;
        }

        if (change) {
            return order;
        } else {
            return null;
        }
    }

    private Container compareModifyExportContainer(String containerOwner, String containerOwnerCode, String containerCaseNo, String containerCount, String containerDrayageDepot, String containerDrayageDepotCode, String containerReturnDepot, String containerReturnDepotCode, String containerGrossWeight, String containerRfTemp, String containerTemperUnit, String containerType, String containerCartonSize, String containerIsOverWeight, String containerIsCheck, String containerIsCheckliest, String containerIsReplace, String containerDynamo, String containerIsMoreUnload, String containerOther, String containerRemark, Container container) {
        boolean change = false;
        if (StringUtils.isEmpty(containerOwner)) {
            if (!StringUtils.isEmpty(container.getContOwner())) {
                container.setContOwner(null);
                change = true;
            }
        } else if (!containerOwner.equals(container.getContOwner())) {
            container.setContOwner(containerOwner);
            change = true;
        }

        if (StringUtils.isEmpty(containerOwnerCode)) {
            if (!StringUtils.isEmpty(container.getContOwnerCode())) {
                container.setContOwnerCode(null);
                change = true;
            }
        } else if (!containerOwnerCode.equals(container.getContOwnerCode())) {
            container.setContOwnerCode(containerOwnerCode);
            change = true;
        }

        if (StringUtils.isEmpty(containerCaseNo)) {
            if (!StringUtils.isEmpty(container.getContCaseNo())) {
                container.setContCaseNo(null);
                change = true;
            }
        } else if (!containerCaseNo.equals(container.getContCaseNo())) {
            container.setContCaseNo(containerCaseNo);
            change = true;
        }

        if (StringUtils.isEmpty(containerCount)) {
            if (!StringUtils.isEmpty(String.valueOf(container.getContCount()))) {
                container.setContCount(null);
                change = true;
            }
        } else if (!containerCount.equals(String.valueOf(container.getContCount()))) {
            container.setContCount(Integer.parseInt(containerCount));
            change = true;
        }

        if (StringUtils.isEmpty(containerDrayageDepot)) {
            if (!StringUtils.isEmpty(container.getContDrayageDepot())) {
                container.setContDrayageDepot(null);
                change = true;
            }
        } else if (!containerDrayageDepot.equals(container.getContDrayageDepot())) {
            container.setContDrayageDepot(containerDrayageDepot);
            change = true;
        }

        if (StringUtils.isEmpty(containerDrayageDepotCode)) {
            if (!StringUtils.isEmpty(container.getContDrayageDepotCode())) {
                container.setContDrayageDepotCode(null);
                change = true;
            }
        } else if (!containerDrayageDepotCode.equals(container.getContDrayageDepotCode())) {
            container.setContDrayageDepotCode(containerDrayageDepotCode);
            change = true;
        }

        if (StringUtils.isEmpty(containerReturnDepot)) {
            if (!StringUtils.isEmpty(container.getContReturnDepot())) {
                container.setContReturnDepot(null);
                change = true;
            }
        } else if (!containerReturnDepot.equals(container.getContReturnDepot())) {
            container.setContReturnDepot(containerReturnDepot);
            change = true;
        }

        if (StringUtils.isEmpty(containerReturnDepotCode)) {
            if (!StringUtils.isEmpty(container.getContReturnDepotCode())) {
                container.setContReturnDepotCode(null);
                change = true;
            }
        } else if (!containerReturnDepotCode.equals(container.getContReturnDepotCode())) {
            container.setContReturnDepotCode(containerReturnDepotCode);
            change = true;
        }

        if (StringUtils.isEmpty(containerGrossWeight)) {
            if (!StringUtils.isEmpty(String.valueOf(container.getContGrossWeight()))) {
                container.setContGrossWeight(null);
                change = true;
            }
        } else if (!containerGrossWeight.equals(String.valueOf(container.getContGrossWeight()))) {
            container.setContGrossWeight(Double.parseDouble(containerGrossWeight));
            change = true;
        }

        if (StringUtils.isEmpty(containerRfTemp)) {
            if (!StringUtils.isEmpty(String.valueOf(container.getContRfTemp()))) {
                container.setContRfTemp(null);
                change = true;
            }
        } else if (!containerRfTemp.equals(String.valueOf(container.getContRfTemp()))) {
            container.setContRfTemp(Integer.parseInt(containerRfTemp));
            change = true;
        }

        if (StringUtils.isEmpty(containerTemperUnit)) {
            if (!StringUtils.isEmpty(String.valueOf(container.getContTemperUnit()))) {
                container.setContTemperUnit(null);
                change = true;
            }
        } else if (!containerTemperUnit.equals(String.valueOf(container.getContTemperUnit()))) {
            container.setContTemperUnit(Integer.parseInt(containerTemperUnit));
            change = true;
        }

        if (StringUtils.isEmpty(containerType)) {
            if (!StringUtils.isEmpty(container.getContType())) {
                container.setContType(null);
                change = true;
            }
        } else if (!containerType.equals(container.getContType())) {
            container.setContType(containerType);
            change = true;
        }

        if (StringUtils.isEmpty(containerCartonSize)) {
            if (!StringUtils.isEmpty(container.getContCartonSize())) {
                container.setContCartonSize(null);
                change = true;
            }
        } else if (!containerCartonSize.equals(container.getContCartonSize())) {
            container.setContCartonSize(containerCartonSize);
            change = true;
        }

        if (StringUtils.isEmpty(containerIsOverWeight)) {
            if (!StringUtils.isEmpty(container.getContIsOverWeight())) {
                container.setContIsOverWeight(null);
                change = true;
            }
        } else if (!containerIsOverWeight.equals(container.getContIsOverWeight())) {
            container.setContIsOverWeight(containerIsOverWeight);
            change = true;
        }

        if (StringUtils.isEmpty(containerIsCheck)) {
            if (!StringUtils.isEmpty(container.getContIsCheck())) {
                container.setContIsCheck(null);
                change = true;
            }
        } else if (!containerIsCheck.equals(container.getContIsCheck())) {
            container.setContIsCheck(containerIsCheck);
            change = true;
        }

        if (StringUtils.isEmpty(containerIsCheckliest)) {
            if (!StringUtils.isEmpty(container.getContIsCheckliest())) {
                container.setContIsCheckliest(null);
                change = true;
            }
        } else if (!containerIsCheckliest.equals(container.getContIsCheckliest())) {
            container.setContIsCheckliest(containerIsCheckliest);
            change = true;
        }

        if (StringUtils.isEmpty(containerIsReplace)) {
            if (!StringUtils.isEmpty(container.getContIsReplace())) {
                container.setContIsReplace(null);
                change = true;
            }
        } else if (!containerIsReplace.equals(container.getContIsReplace())) {
            container.setContIsReplace(containerIsReplace);
            change = true;
        }

        if (StringUtils.isEmpty(containerDynamo)) {
            if (!StringUtils.isEmpty(container.getContDynamo())) {
                container.setContDynamo(null);
                change = true;
            }
        } else if (!containerDynamo.equals(container.getContDynamo())) {
            container.setContDynamo(containerDynamo);
            change = true;
        }

        if (StringUtils.isEmpty(containerIsMoreUnload)) {
            if (!StringUtils.isEmpty(container.getContIsMoreUnload())) {
                container.setContIsMoreUnload(null);
                change = true;
            }
        }else if (!containerIsMoreUnload.equals(container.getContIsMoreUnload())) {
            container.setContIsMoreUnload(containerIsMoreUnload);
            change = true;
        }

        if (StringUtils.isEmpty(containerOther)) {
            if (!StringUtils.isEmpty(container.getContOther())) {
                container.setContOther(containerOther);
                change = true;
            }
        } else if (!containerOther.equals(container.getContOther())) {
            container.setContOther(containerOther);
            change = true;
        }
        if (StringUtils.isEmpty(containerRemark)) {
            if (!StringUtils.isEmpty(container.getContRemark())) {
                container.setContRemark(containerRemark);
                change = true;
            }
        } else if (!containerRemark.equals(container.getContRemark())) {
            container.setContRemark(containerRemark);
            change = true;
        }
        if (change) {
            return container;
        } else {
            return null;
        }
    }

    private Address compareModifyExportAddress(String addressPlace, String addressArriveTime, String addressContacts, String addressTel, String addressProvince, String addressCity, String addressDistrict, Address address) {
        boolean change = false;
        if (StringUtils.isEmpty(addressPlace)) {
            if (!StringUtils.isEmpty(address.getAddrPlace())) {
                address.setAddrPlace(null);
                change = true;
            }
        } else if (!addressPlace.equals(address.getAddrPlace())) {
            address.setAddrPlace(addressPlace);
            change = true;
        }

        if (StringUtils.isEmpty(addressArriveTime)) {
            if (null != address.getAddrArriveTime()) {
                address.setAddrArriveTime(null);
                change = true;
            }
        } else if (null == address.getAddrArriveTime()) {
            try {
                address.setAddrArriveTime(sdf3.parse(addressArriveTime));
            } catch (ParseException e) {
                throw new DateFormatException(sdf3, logger);
            }
            change = true;
        } else if (!addressArriveTime.equals(sdf3.format(address.getAddrArriveTime()))) {
            try {
                address.setAddrArriveTime(sdf3.parse(addressArriveTime));
            } catch (ParseException e) {
                throw new DateFormatException(sdf3, logger);
            }
            change = true;
        }

        if (StringUtils.isEmpty(addressContacts)) {
            if (!StringUtils.isEmpty(address.getAddrContacts())) {
                address.setAddrContacts(null);
                change = true;
            }
        } else if (!addressContacts.equals(address.getAddrContacts())) {
            address.setAddrContacts(addressContacts);
            change = true;
        }

        if (StringUtils.isEmpty(addressTel)) {
            if (!StringUtils.isEmpty(address.getAddrTel())) {
                address.setAddrTel(null);
                change = true;
            }
        } else if (!addressTel.equals(address.getAddrTel())) {
            address.setAddrTel(addressTel);
            change = true;
        }

        if (StringUtils.isEmpty(addressProvince)) {
            if (!StringUtils.isEmpty(address.getAddrProvince())) {
                address.setAddrProvince(null);
                change = true;
            }
        } else if (!addressProvince.equals(address.getAddrProvince())) {
            address.setAddrProvince(addressProvince);
            change = true;
        }

        if (StringUtils.isEmpty(addressCity)) {
            if (!StringUtils.isEmpty(address.getAddrCity())) {
                address.setAddrCity(null);
                change = true;
            }
        } else if (!addressCity.equals(address.getAddrCity())) {
            address.setAddrCity(addressCity);
            change = true;
        }

        if (StringUtils.isEmpty(addressDistrict)) {
            if (!StringUtils.isEmpty(address.getAddrDistrict())) {
                address.setAddrDistrict(null);
                change = true;
            }
        } else if (!addressDistrict.equals(address.getAddrDistrict())) {
            address.setAddrDistrict(addressDistrict);
            change = true;
        }

        if (change) {
            return address;
        } else {
            return null;
        }
    }
}
