package com.sinotrans.transport.service.impl;

import com.sinotrans.framework.mybatis.support.FilterCondition;
import com.sinotrans.framework.orm.model.BaseModel;
import com.sinotrans.framework.orm.support.PagingInfo;
import com.sinotrans.framework.service.mybatis.base.impl.MybatisManagerImpl;
import com.sinotrans.transport.common.ErrorCode;
import com.sinotrans.transport.common.enums.*;
import com.sinotrans.transport.common.exception.*;
import com.sinotrans.transport.common.util.CommonUtils;
import com.sinotrans.transport.dto.*;
import com.sinotrans.transport.dto.common.CommonListResponse;
import com.sinotrans.transport.dto.common.CommonNoPageListResponse;
import com.sinotrans.transport.dto.common.RestResponse;
import com.sinotrans.transport.dto.vo.*;
import com.sinotrans.transport.model.*;
import com.sinotrans.transport.service.OrderImportService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by emi on 2016/5/13.
 */
@Service
@Transactional
public class OrderImportServiceImpl extends MybatisManagerImpl implements OrderImportService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH");
    Logger logger = Logger.getLogger(OrderImportServiceImpl.class);

    @Override
    public CommonListResponse search(Long userId, OrderImportSearchRequest searchRequest) {

        String orderBy = "ordeId desc";//
        PagingInfo pagingInfo;
        List<OrderImportSearchRespVo> importSearchVoList;
        if (StringUtils.isEmpty(searchRequest.getContainerCaseNo())) {
            pagingInfo = this.fetchPagingInfo(searchRequest, userId, true);
            List<Order> orderList = this.myBatisDao.findByCondition(Order.class, orderBy, pagingInfo, searchRequest.fetchFilter(userId, null));
            importSearchVoList = this.toImportVoList(orderList);
        } else {
            pagingInfo = this.fetchPagingInfo(searchRequest, userId, false);
            List<Map<String,Object>> result = this.myBatisDao.queryByCondition("SearchImportOrderList", searchRequest.fetchCondition(), orderBy, pagingInfo, null);
            importSearchVoList = this.mapToImportVoList(result);
        }

        return new CommonListResponse(pagingInfo, importSearchVoList);
    }


    @Override
    public OrderImportAllResponse searchAll(Long userId, Long orderId) {
        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkBelongCreator(userId, logger);

        OrderImportAllResponse response = this.generateAll(order, userId);
        return response;
    }

    @Override
    public OrderImportAlterResponse create(Long userId, OrderImportAlterRequest request) {

        //todo: 那些字段是unique？，先不考虑验证
        Order order = null;
        String systemNo = "SNI" + System.currentTimeMillis() + CommonUtils.generateRandom(5, 1);
        User user = myBatisDao.get(User.class, userId);
        this.checkNull(DomainType.User, user, userId);
        try {
            List<Customer> customerList = myBatisDao.findByCondition(Customer.class, null, null, this.fetchCustomerUserFilter(userId, true));
            if (1 != customerList.size()) {
                throw new RecordSizeException(DomainType.Customer, 1, customerList.size(), "custUserId", logger);
            }
            order = new Order(request.getOrderBillNo(), systemNo, customerList.get(0).getCustType() ,request.getOrderWharf(), request.getOrderWharfCode(), request.getOrderShipName(), request.getOrderShipNameCode(), request.getOrderSailing(), request.getOrderSailingCode(), request.getOrderOwnerCode(), request.getOrderOwner() ,request.getOrderGoodOwnerCode(), request.getOrderGoodOwner(), request.getOrderShipAgency(), request.getOrderArrivalDate(), user.getUserCompanyCode(), user.getUserCompany(), request.getOrderTotalCase(), request.getOrderRemark(), request.getOrderConnects(), request.getOrderConnectEmail(), request.getOrderConnectPhone(), userId, sdf);
        } catch (ParseException e) {
            throw new DateFormatException(sdf, logger);
        }
        order = myBatisDao.save(order);
        return new OrderImportAlterResponse(order.getOrdeId());
    }

    @Override
    public OrderImportAlterResponse alter(Long userId, Long orderId, OrderImportAlterRequest request) {
        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkState(logger, OrderStateType.Create, OrderStateType.Failed);
        order.checkBelongCreator(userId, logger);
        order = this.compareModifyOrder(request.getOrderBillNo(), request.getOrderWharf(), request.getOrderWharfCode(), request.getOrderShipName(), request.getOrderShipNameCode(), request.getOrderSailing(), request.getOrderSailingCode(), request.getOrderOwnerCode(), request.getOrderOwner(), request.getOrderGoodOwnerCode(), request.getOrderGoodOwner(), request.getOrderShipAgency(), request.getOrderArrivalDate(), request.getOrderTotalCase(), request.getOrderRemark(), request.getOrderConnects(), request.getOrderConnectEmail(), request.getOrderConnectPhone(), order);
        if (null != order) {
            order.setModifier(String.valueOf(userId));
            myBatisDao.save(order);
        }
        return new OrderImportAlterResponse(orderId);
    }

    //todo:订单和容器状态是不是改了
    @Override
    public OrderImportAllResponse sendOrder(Long userId, Long orderId) {

        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkState(logger, OrderStateType.Create, OrderStateType.Failed);
        order.checkBelongCreator(userId, logger);

        List<Container> containerList = myBatisDao.findByCondition(Container.class, null, null, this.fetchContainerOrderIdFilter(orderId, null));
        PositionTimeLog positionTimeLog;
        for (Container c : containerList) {
            c.setContState(ContainerStateType.ImportSend.getValue());
            c.setModifier(String.valueOf(userId));
            myBatisDao.update(c);

            positionTimeLog = new PositionTimeLog(c.getContId(), PositionLogType.ImSet, PositionLogType.ImSet.getDesc());
            myBatisDao.save(positionTimeLog);
        }
        order.setOrdeState(OrderStateType.Send.getValue());
        order.setModifier(String.valueOf(userId));
        myBatisDao.update(order);

        OrderImportAllResponse response = this.generateAll(order, userId);
        return response;
    }

    @Override
    public CommonNoPageListResponse searchContainer(Long userId, Long orderId) {

        String orderBy = "createTime asc";//
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, userId));
        List<ContainerImportSearchRespVo> containerVoList = this.toImportContainerVoList(containerList);
        return new CommonNoPageListResponse(containerVoList);
    }

    @Override
    public CommonNoPageListResponse modifyContainers(Long userId, ContainerImportAlterRequest request) {

        List<ContainerImportReqVo> containVoList = request.getContainerVoList();
        if (null == containVoList || containVoList.size() == 0) {
            return new CommonNoPageListResponse(new ArrayList<ContainerExportSearchRespVo>(0));
        }
        if (StringUtils.isEmpty(request.getContainerOrderId())) {
            throw new CommonException(ErrorCode.COMMON_ERROR, "请先创建订单再操作箱子", null, null);
        }
        Long orderId = Long.parseLong(request.getContainerOrderId());
        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkState(logger, OrderStateType.Create, OrderStateType.Failed);
        order.checkBelongCreator(userId, logger);

//        boolean ifOrderChange = order.checkOrderTotalCase(containVoList);
//        if (ifOrderChange) {
//            myBatisDao.update(order);
//        }

        String containerIdStr;
        Container container;
        for (ContainerImportReqVo c : containVoList) {
            containerIdStr = c.getContainerId();
            if (StringUtils.isEmpty(containerIdStr) || "0".equals(containerIdStr)) {
                container = new Container(orderId, c.getContainerOwner(), c.getContainerOwnerCode(), c.getContainerCaseNo(), c.getContainerCount(), c.getContainerDrayageDepot(), c.getContainerDrayageDepotCode(), c.getContainerReturnDepot(), c.getContainerReturnDepotCode(), c.getContainerSealNo(), c.getContainerTradeName(), c.getContainerType(), c.getContainerCartonSize(), c.getContainerGrossWeight(), c.getContainerRfTemp(), c.getContainerTemperUnit(), c.getContainerDynamo(), c.getContainerIsMoreUnload(), c.getContainerCost(), c.getContainerOther(), c.getContainerRemark(), String.valueOf(userId));
                myBatisDao.save(container);
            } else {
                container = myBatisDao.get(Container.class, Long.parseLong(containerIdStr));
                this.checkNull(DomainType.Container, container, container.getContId());
                container.checkDelete(logger);
//                container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
                container = this.compareModifyContainer(c.getContainerOwner(), c.getContainerOwnerCode(), c.getContainerCaseNo(), c.getContainerCount(), c.getContainerDrayageDepot(), c.getContainerDrayageDepotCode(), c.getContainerReturnDepot(), c.getContainerReturnDepotCode(), c.getContainerSealNo(), c.getContainerTradeName(), c.getContainerType(), c.getContainerCartonSize(), c.getContainerGrossWeight(), c.getContainerRfTemp(), c.getContainerTemperUnit(), c.getContainerDynamo(), c.getContainerIsMoreUnload(), c.getContainerCost(), c.getContainerOther(), c.getContainerRemark(), container);
                if (null != container) {
                    container.setModifier(String.valueOf(userId));
                    myBatisDao.update(container);
                }
            }
        }

        String orderBy = "createTime asc";//
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, userId));
        List<ContainerImportSearchRespVo> containerVoList = this.toImportContainerVoList(containerList);
        return new CommonNoPageListResponse(containerVoList);
    }

    @Override
    public ContainerDropResponse dropContainers(Long userId, DomainsDropRequest dropRequest) {

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
            container.setModifier(String.valueOf(userId));
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
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, userId));
        List<ContainerImportSearchRespVo> containerVoList = this.toImportContainerVoList(containerList);
        List<AddressImportSearchRespVo> addressVoList;
        if (null == containerList || containerList.size() == 0) {
            addressVoList = new ArrayList<AddressImportSearchRespVo>(0);
        } else {
            containerId = containerList.get(0).getContId();
            List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, userId));
            addressVoList = this.toImportAddressVoList(addressList);
        }

//        Order order = myBatisDao.get(Order.class, orderId);
//        order.setOrdeTotalCase(containerList.size());
//        myBatisDao.update(order);

        return new ContainerDropResponse(containerId, containerVoList, addressVoList);
    }

    @Override
    public CommonNoPageListResponse searchAddress(Long userId, Long containerId) {
        String orderBy = "createTime asc";
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, userId));
        List<AddressImportSearchRespVo> addressVoList = this.toImportAddressVoList(addressList);
        return new CommonNoPageListResponse(addressVoList);
    }

    @Override
    public CommonNoPageListResponse modifyAddress(Long userId, AddressImportAlterRequest request) {

        List<AddressImportReqVo> addressVoList = request.getAddressVoList();
        if (null == addressVoList || addressVoList.size() == 0) {
            return new CommonNoPageListResponse(new ArrayList<AddressImportSearchRespVo>(0));
        }
        if (StringUtils.isEmpty(request.getContainerId())) {
            throw new CommonException(ErrorCode.COMMON_ERROR, "请先创建箱子再操作地址", null, null);
        }
        Long containerId = Long.parseLong(request.getContainerId());
        Container container = myBatisDao.get(Container.class, containerId);
        this.checkNull(DomainType.Container, container, containerId);
        container.checkState(logger, ContainerStateType.ImportCreate, ContainerStateType.ImportCheckFailed);
        container.checkDelete(logger);
//        container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽

        String addressIdStr;
        Address address;
        for (AddressImportReqVo a : addressVoList) {
            addressIdStr = a.getAddressId();
            if (StringUtils.isEmpty(addressIdStr) || "0".equals(addressIdStr)) {
                address = new Address(containerId, a.getAddressPlace(), a.getAddressDeliveryTime(), a.getAddressContacts(), a.getAddressTel(), a.getAddressProvince(), a.getAddressCity(), a.getAddressDistrict(), String.valueOf(userId), sdf3);
                myBatisDao.save(address);
            } else {
                address = myBatisDao.get(Address.class, Long.parseLong(addressIdStr));
                this.checkNull(DomainType.Address, address, address.getAddrId());
                address.checkDelete(logger);
//                address.checkBelongCreator(userId, logger);////现在管理员也能创建了,所以屏蔽
                address = this.compareModifyAddress(a.getAddressPlace(), a.getAddressDeliveryTime(), a.getAddressContacts(), a.getAddressTel(), a.getAddressProvince(), a.getAddressCity(), a.getAddressDistrict(), address);
                if (null != address) {
                    address.setModifier(String.valueOf(userId));
                    myBatisDao.update(address);
                }
            }
        }
        String orderBy = "createTime asc";
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, userId));
        List<AddressImportSearchRespVo> resultVoList = this.toImportAddressVoList(addressList);
        return new CommonNoPageListResponse(resultVoList);
    }


    @Override
    public CommonNoPageListResponse dropAddresses(Long userId, DomainsDropRequest dropRequest) {
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
            address.setModifier(String.valueOf(userId));
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
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, userId));
        List<AddressImportSearchRespVo> addressVoList = this.toImportAddressVoList(addressList);
        return new CommonNoPageListResponse(addressVoList);
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

    private PagingInfo fetchPagingInfo(OrderImportSearchRequest searchRequest, Long userId, boolean ifFind) {
        PagingInfo pagingInfo = searchRequest.fetchPagingInfo();
        if(pagingInfo != null) {
            int counts = 0;
            if (ifFind) {
                counts = this.myBatisDao.getRowCountByCondition(Order.class, searchRequest.fetchFilter(userId, null));
            } else {
                counts = this.myBatisDao.getQueryCountByCondition("SearchImportOrderList", searchRequest.fetchCondition(), null);//Order.class, searchRequest.fetchFilter(userId, null));
            }
            pagingInfo.setTotalRows(counts);
        }
        return pagingInfo;
    }

    private void checkNull(DomainType domain, BaseModel model, Long domainId) {
        if (null == model) {
            throw new RecordNotFoundException(domain, domainId, logger);
        }
    }


    private OrderImportAllResponse generateAll(Order order, Long userId) {
        OrderImportDetailRespVo orderVo = new OrderImportDetailRespVo(order.getOrdeId(), order.getOrdeBillNo(), order.getOrdeState(), order.getOrdeWharf(), order.getOrdeWharfCode(), order.getOrdeShipName(), order.getOrdeShipNameCode(), order.getOrdeSailing(), order.getOrdeSailingCode(),order.getOrdeOwnerCode(), order.getOrdeOwner(), order.getOrdeGoodOwner(), order.getOrdeGoodOwnerCode(), order.getOrdeShipAgency(), order.getOrdeArrivalDate(), order.getOrdeTotalCase(), order.getOrdeVerifierContent(), order.getOrdeRemark(), order.getOrdeConnects(), order.getOrdeConnectEmail(), order.getOrdeConnectPhone(), sdf);
        String orderBy =  "createTime asc";
        Long containerId = null;
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(order.getOrdeId(), userId));
        List<ContainerImportSearchRespVo> containerVoList = this.toImportContainerVoList(containerList);
        List<AddressImportSearchRespVo> addressVoList;
        if (null == containerList || containerList.size() == 0) {
            addressVoList = new ArrayList<AddressImportSearchRespVo>(0);
        } else {
            containerId = containerList.get(0).getContId();
            List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, userId));
            addressVoList = this.toImportAddressVoList(addressList);
        }
        return new OrderImportAllResponse(orderVo, containerVoList, containerId, addressVoList);
    }


    private Order compareModifyOrder(String orderBillNo, String orderWharf, String orderWharfCode, String orderShipName, String orderShipNameCode, String orderSailing, String orderSailingCode, String orderOwnerCode, String orderOwner,
                                     String orderGoodOwnerCode, String orderGoodOwner, String orderShipAgency, String orderArrivalDate, String orderTotalCase, String orderRemark, String orderConnects, String orderConnectEmail, String orderConnectPhone, Order order) {

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

    private Container compareModifyContainer(String containerOwner, String containerOwnerCode, String containerCaseNo, String containerCount, String containerDrayageDepot, String containerDrayageDepotCode, String containerReturnDepot, String containerReturnDepotCode, String containerSealNo, String containerTradeName, String containerType, String containerCartonSize, String containerGrossWeight,
                                             String containerRfTemp, String containerTemperUnit, String containerDynamo, String containerIsMoreUnload, String containerCost, String containerOther, String containerRemark, Container container) {
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

    private Address compareModifyAddress(String addressPlace, String addressDeliveryTime, String addressContacts, String addressTel, String addressProvince, String addressCity, String addressDistrict, Address address) {
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

    private List<OrderImportSearchRespVo> toImportVoList(List<Order> orderList) {
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


    private List<FilterCondition> fetchCustomerUserFilter(Long userId, boolean checkStatus) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("custUserId", userId, "="));//likeAnywhere
        if (checkStatus) {
            filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));
        }
        return filter;
    }

    @Override
    public RestResponse textXml(String filePath) {

        Element element = null;
        // 可以使用绝对路劲
        File f = new File(filePath);
        // documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        try {
            // 返回documentBuilderFactory对象
            dbf = DocumentBuilderFactory.newInstance();
            // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
            db = dbf.newDocumentBuilder();
            // 得到一个DOM并返回给document对象
            Document dt = db.parse(f);
            // 得到一个elment根元素
            element = dt.getDocumentElement();
            // 获得根节点
            System.out.println("根元素：" + element.getNodeName());
            // 获得根元素下的子节点
            NodeList childNodes = element.getChildNodes();
            Node node1;
            for (int i = 0; i < childNodes.getLength(); i ++) {
                node1 = childNodes.item(i);
                if ("Account".equals(node1.getNodeName())) {
                    // 如果节点的名称为"Account"，则输出Account元素属性type
                    System.out.println("\r\n找到一篇账号. 所属区域: " + node1.getAttributes().getNamedItem("type").getNodeValue() + ". ");
                    // 获得<Accounts>下的节点
                    NodeList nodeDetail = node1.getChildNodes();
                    // 遍历<Accounts>下的节点
                    for (int j = 0; j < nodeDetail.getLength(); j++) {
                        // 获得<Accounts>元素每一个节点
                        Node detail = nodeDetail.item(j);
                        if ("code".equals(detail.getNodeName())) // 输出code
                            System.out.println("卡号: " + detail.getTextContent());
                        else if ("pass".equals(detail.getNodeName())) // 输出pass
                            System.out.println("密码: " + detail.getTextContent());
                        else if ("name".equals(detail.getNodeName())) // 输出name
                            System.out.println("姓名: " + detail.getTextContent());
                        else if ("money".equals(detail.getNodeName())) // 输出money
                            System.out.println("余额: " + detail.getTextContent());
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RestResponse();
    }
}
