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
import com.sinotrans.transport.dto.vo.*;
import com.sinotrans.transport.model.*;
import com.sinotrans.transport.service.OrderExportService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
public class OrderExportServiceImpl extends MybatisManagerImpl implements OrderExportService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH");
    Logger logger = Logger.getLogger(OrderExportServiceImpl.class);

    @Override
    public CommonListResponse search(Long userId, OrderExportSearchRequest searchRequest) {

        String orderBy = "ordeId desc";//
        PagingInfo pagingInfo;
        List<OrderExportSearchRespVo> exportSearchVoList;
        if (StringUtils.isEmpty(searchRequest.getContainerCaseNo())) {
            pagingInfo = this.fetchPagingInfo(searchRequest, userId, true);
            List<Order> orderList = this.myBatisDao.findByCondition(Order.class, orderBy, pagingInfo, searchRequest.fetchFilter(userId, null));
            exportSearchVoList = this.toExportVoList(orderList);
        } else {
            pagingInfo = this.fetchPagingInfo(searchRequest, userId, false);
            List<Map<String,Object>> result = this.myBatisDao.queryByCondition("SearchExportOrderList", searchRequest.fetchCondition(), orderBy, pagingInfo, null);
            exportSearchVoList = this.mapToExportVoList(result);
        }
        return new CommonListResponse(pagingInfo, exportSearchVoList);
    }

    @Override
    public OrderExportAllResponse searchAll(Long userId, Long orderId) {
        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkBelongCreator(userId, logger);

        OrderExportAllResponse response = this.generateAll(order, userId);
        return response;
    }

    @Override
    public OrderExportAlterResponse create(Long userId, OrderExportAlterRequest request) {

        //todo: 那些字段是unique？，先不考虑验证
        Order order = null;
        String systemNo = "SNE" + System.currentTimeMillis() + CommonUtils.generateRandom(5, 1);
        User user = myBatisDao.get(User.class, userId);
        this.checkNull(DomainType.User, user, userId);
        try {
            List<Customer> customerList = myBatisDao.findByCondition(Customer.class, null, null, this.fetchCustomerUserFilter(userId, true));
            if (1 != customerList.size()) {
                throw new RecordSizeException(DomainType.Customer, 1, customerList.size(), "custUserId", logger);
            }
            order = new Order(request.getOrderBillNo(), systemNo, customerList.get(0).getCustType(), request.getOrderWharf(), request.getOrderWharfCode(), request.getOrderShipName(), request.getOrderShipNameCode(), request.getOrderSailing(), request.getOrderSailingCode(), request.getOrderShipAgency(), request.getOrderSailingDate(), request.getOrderEnterPort(), request.getOrderEnterPortCode(), request.getOrderDestinationPort(), request.getOrderDestinationPortCode(), request.getOrderShippingCode(), request.getOrderBookingNumber(), request.getOrderTotalCase(), request.getOrderOwner(), request.getOrderOwnerCode(), request.getOrderGoodOwner(), request.getOrderGoodOwnerCode() ,user.getUserCompanyCode(), user.getUserCompany(), request.getOrderRemark(), request.getOrderConnects(), request.getOrderConnectEmail(), request.getOrderConnectPhone(), userId, sdf);
        } catch (ParseException e) {
            throw new DateFormatException(sdf, logger);
        }
        myBatisDao.save(order);
        return new OrderExportAlterResponse(order.getOrdeId());
    }

    @Override
    public OrderExportAlterResponse alter(Long userId, Long orderId, OrderExportAlterRequest request) {

        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkState(logger, OrderStateType.Create, OrderStateType.Failed);
        order.checkBelongCreator(userId, logger);
        order = this.compareModifyOrder(request.getOrderBillNo(), request.getOrderWharf(), request.getOrderWharfCode(), request.getOrderShipName(), request.getOrderShipNameCode(), request.getOrderSailing(), request.getOrderSailingCode(), request.getOrderShipAgency(), request.getOrderSailingDate(), request.getOrderEnterPort(), request.getOrderEnterPortCode(), request.getOrderDestinationPort(), request.getOrderDestinationPortCode(), request.getOrderShippingCode(), request.getOrderBookingNumber(), request.getOrderTotalCase(), request.getOrderOwner(), request.getOrderOwnerCode(), request.getOrderGoodOwner(), request.getOrderGoodOwnerCode(), request.getOrderRemark(), request.getOrderConnects(), request.getOrderConnectEmail(), request.getOrderConnectPhone(), order);
        if (null != order) {
            order.setModifier(String.valueOf(userId));
            myBatisDao.update(order);
        }
        return new OrderExportAlterResponse(orderId);
    }

    @Override
    public OrderExportAllResponse sendOrder(Long userId, Long orderId) {

        Order order = myBatisDao.get(Order.class, orderId);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkDelete(logger);
        order.checkState(logger, OrderStateType.Create, OrderStateType.Failed);
        order.checkBelongCreator(userId, logger);

        List<Container> containerList = myBatisDao.findByCondition(Container.class, null, null, this.fetchContainerOrderIdFilter(orderId, null));
        PositionTimeLog positionTimeLog;
        for (Container c : containerList) {
            c.setContState(ContainerStateType.ExportSend.getValue());
            c.setModifier(String.valueOf(userId));
            myBatisDao.update(c);

            positionTimeLog = new PositionTimeLog(c.getContId(), PositionLogType.ExSet, PositionLogType.ExSet.getDesc());
            myBatisDao.save(positionTimeLog);
        }
        order.setOrdeState(OrderStateType.Send.getValue());
        order.setModifier(String.valueOf(userId));
        myBatisDao.update(order);

        OrderExportAllResponse response = this.generateAll(order, userId);
        return response;
    }

    @Override
    public CommonNoPageListResponse searchContainer(Long userId, Long orderId) {

        String orderBy = "createTime asc";//
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, userId));
        List<ContainerExportSearchRespVo> containerVoList = this.toExportContainerVoList(containerList);
        return new CommonNoPageListResponse(containerVoList);
    }

    @Override
    public CommonNoPageListResponse modifyContainer(Long userId, ContainerExportAlterRequest request) {
        List<ContainerExportReqVo> containVoList = request.getContainerVoList();
        if (null == containVoList || containVoList.size() == 0) {
            return new CommonNoPageListResponse(new ArrayList<ContainerExportSearchRespVo>(0));
        }
        if (StringUtils.isEmpty(request.getContainerOrderId())) {
            throw new CommonException(ErrorCode.COMMON_ERROR, "请先创建订单再操作箱子", null, null);
        }
        Long orderId = Long.parseLong(request.getContainerOrderId());
        Order order = myBatisDao.get(Order.class, orderId);
        order.checkDelete(logger);
        this.checkNull(DomainType.Order, order, orderId);
        order.checkState(logger, OrderStateType.Create, OrderStateType.Failed);
        order.checkBelongCreator(userId, logger);
//        boolean ifOrderChange = order.checkOrderTotalCase(containVoList);
//        if (ifOrderChange) {
//            myBatisDao.update(order);
//        }

        String containerIdStr;
        Container container;
        for (ContainerExportReqVo c : containVoList) {
            containerIdStr = c.getContainerId();
            if (StringUtils.isEmpty(containerIdStr) || "0".equals(containerIdStr)) {
                container = new Container(c.getContainerOwner(), c.getContainerOwnerCode(), orderId, c.getContainerCaseNo(), c.getContainerCount(), c.getContainerDrayageDepot(), c.getContainerDrayageDepotCode(), c.getContainerReturnDepot(), c.getContainerReturnDepotCode(), c.getContainerGrossWeight(), c.getContainerRfTemp(), c.getContainerTemperUnit(), c.getContainerType(), c.getContainerCartonSize(), c.getContainerDynamo(), c.getContainerIsMoreUnload(), c.getContainerIsOverWeight(), c.getContainerIsCheck(), c.getContainerIsCheckliest(), c.getContainerIsReplace(), c.getContainerOther(), c.getContainerRemark(), String.valueOf(userId));
                myBatisDao.save(container);
            } else {
                container = myBatisDao.get(Container.class, Long.parseLong(containerIdStr));
                this.checkNull(DomainType.Container, container, container.getContId());
                container.checkDelete(logger);
//                container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
                container = this.compareModifyContainer(c.getContainerOwner(), c.getContainerOwnerCode(), c.getContainerCaseNo(), c.getContainerCount(), c.getContainerDrayageDepot(), c.getContainerDrayageDepotCode(), c.getContainerReturnDepot(), c.getContainerReturnDepotCode(), c.getContainerGrossWeight(), c.getContainerRfTemp(), c.getContainerTemperUnit(), c.getContainerType(), c.getContainerCartonSize(), c.getContainerIsOverWeight(), c.getContainerIsCheck(), c.getContainerIsCheckliest(), c.getContainerIsReplace(), c.getContainerDynamo(), c.getContainerIsMoreUnload(), c.getContainerOther(), c.getContainerRemark(), container);
                if (null != container) {
                    order.setModifier(String.valueOf(userId));
                    myBatisDao.update(container);
                }
            }
        }

        String orderBy = "createTime asc";//
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, userId));
        List<ContainerExportSearchRespVo> containerVoList = this.toExportContainerVoList(containerList);
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
            container.setModifier(String.valueOf(userId));
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
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(orderId, userId));
        List<ContainerExportSearchRespVo> containerVoList = this.toExportContainerVoList(containerList);
        containerId = null;//first container Id
        List<AddressExportSearchRespVo> addressVoList;
        if (null == containerList || containerList.size() == 0) {
            addressVoList = new ArrayList<AddressExportSearchRespVo>(0);
        } else {
            containerId = containerList.get(0).getContId();
            List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, userId));
            addressVoList = this.toExportAddressVoList(addressList);
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
        List<AddressExportSearchRespVo> addressVoList = this.toExportAddressVoList(addressList);
        return new CommonNoPageListResponse(addressVoList);
    }

    @Override
    public CommonNoPageListResponse modifyAddress(Long userId, AddressExportAlterRequest request) {

        List<AddressExportReqVo> addressVoList = request.getAddressVoList();
        if (null == addressVoList || addressVoList.size() == 0) {
            return new CommonNoPageListResponse(new ArrayList<AddressExportSearchRespVo>(0));
        }
        if (StringUtils.isEmpty(request.getContainerId())) {
            throw new CommonException(ErrorCode.COMMON_ERROR, "请先创建箱子再操作地址", null, null);
        }
        Long containerId = Long.parseLong(request.getContainerId());
        Container container = myBatisDao.get(Container.class, containerId);
        this.checkNull(DomainType.Container, container, containerId);
        container.checkDelete(logger);
        container.checkState(logger, ContainerStateType.ExportCreate, ContainerStateType.ExportCheckFailed);
//        container.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽

        String addressIdStr;
        Address address;
        for (AddressExportReqVo a : addressVoList) {
            addressIdStr = a.getAddressId();
            if (StringUtils.isEmpty(addressIdStr) || "0".equals(addressIdStr)) {
                address = new Address(sdf3, containerId, a.getAddressPlace(), a.getAddressArriveTime(), a.getAddressContacts(), a.getAddressTel(), a.getAddressProvince(), a.getAddressCity(), a.getAddressDistrict(), String.valueOf(userId));
                myBatisDao.save(address);
            } else {
                address = myBatisDao.get(Address.class, Long.parseLong(addressIdStr));
                this.checkNull(DomainType.Address, address, address.getAddrId());
                address.checkDelete(logger);
//                address.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
                address = this.compareModifyAddress(a.getAddressPlace(), a.getAddressArriveTime(), a.getAddressContacts(), a.getAddressTel(), a.getAddressProvince(), a.getAddressCity(), a.getAddressDistrict(), address);
                if (null != address) {
                    address.setModifier(String.valueOf(userId));
                    myBatisDao.update(address);
                }
            }
        }
        String orderBy = "createTime asc";
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, userId));
        List<AddressExportSearchRespVo> resultVoList = this.toExportAddressVoList(addressList);
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
            this.checkNull(DomainType.Address, address, address.getAddrId());
//            address.checkBelongCreator(userId, logger);//现在管理员也能创建了,所以屏蔽
            address.setModifier(String.valueOf(userId));
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
        List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, userId));
        List<AddressExportSearchRespVo> addressVoList = this.toExportAddressVoList(addressList);
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


    private List<FilterCondition> fetchCustomerUserFilter(Long userId, boolean checkStatus) {
        List filter = new ArrayList();
        filter.add(new FilterCondition("custUserId", userId, "="));//likeAnywhere
        if (checkStatus) {
            filter.add(new FilterCondition("recStatus", DomainStatus.Normal.getValue(), "="));
        }
        return filter;
    }

    private PagingInfo fetchPagingInfo(OrderExportSearchRequest searchRequest, Long userId, boolean ifFind) {
        PagingInfo pagingInfo = searchRequest.fetchPagingInfo();
        if(pagingInfo != null) {
            int counts = 0;
            if (ifFind) {
                counts = this.myBatisDao.getRowCountByCondition(Order.class, searchRequest.fetchFilter(userId, null));
            } else {
                counts = this.myBatisDao.getQueryCountByCondition("SearchExportOrderList", searchRequest.fetchCondition(), null);//Order.class, searchRequest.fetchFilter(userId, null));
            }
//            int counts = this.myBatisDao.getRowCountByCondition(Order.class, searchRequest.fetchFilter(userId, null));
            pagingInfo.setTotalRows(counts);
        }

        return pagingInfo;
    }

    private void checkNull(DomainType domain, BaseModel model, Long domainId) {
        if (null == model) {
            throw new RecordNotFoundException(domain, domainId, logger);
        }
    }

    private OrderExportAllResponse generateAll(Order order, Long userId) {

        OrderExportDetailRespVo orderVo = new OrderExportDetailRespVo(order.getOrdeId(), order.getOrdeBillNo(), order.getOrdeState(), order.getOrdeWharf(),order.getOrdeWharfCode(), order.getOrdeShipName(), order.getOrdeShipNameCode(), order.getOrdeSailing(), order.getOrdeSailingCode(), order.getOrdeShipAgency(), order.getOrdeSailingDate(), order.getOrdeEnterPort(), order.getOrdeEnterPortCode(), order.getOrdeDestinationPort(), order.getOrdeDestinationPortCode(), order.getOrdeShippingCode(), order.getOrdeBookingNumber(), order.getOrdeTotalCase(), order.getOrdeOwner(), order.getOrdeOwnerCode(), order.getOrdeGoodOwner(), order.getOrdeGoodOwnerCode(), order.getOrdeVerifierContent(), order.getOrdeRemark(), order.getOrdeConnects(), order.getOrdeConnectEmail(), order.getOrdeConnectPhone(), sdf);
        String orderBy =  "createTime asc";
        Long containerId = null;
        List<Container> containerList = myBatisDao.findByCondition(Container.class, orderBy, null, this.fetchContainerOrderIdFilter(order.getOrdeId(), userId));
        List<ContainerExportSearchRespVo> containerVoList = this.toExportContainerVoList(containerList);
        List<AddressExportSearchRespVo> addressVoList;
        if (null == containerList || containerList.size() == 0) {
            addressVoList = new ArrayList<AddressExportSearchRespVo>(0);
        } else {
            containerId = containerList.get(0).getContId();
            List<Address> addressList = myBatisDao.findByCondition(Address.class, orderBy, null, this.fetchAddressContainerId(containerId, userId));
            addressVoList = this.toExportAddressVoList(addressList);
        }
        return new OrderExportAllResponse(orderVo, containerVoList, containerId, addressVoList);
    }

    private Order compareModifyOrder(String orderBillNo, String orderWharf, String orderWharfCode, String orderShipName, String orderShipNameCode, String orderSailing, String orderSailingCode, String orderShipAgency, String orderSailingDate, String orderEnterPort, String orderEnterPortCode,
                                     String orderDestinationPort, String orderDestinationPortCode, String orderShippingCode, String orderBookingNumber, String orderTotalCase, String orderOwner, String orderOwnerCode, String orderGoodOwner, String orderGoodOwnerCode, String orderRemark, String orderConnects, String orderConnectEmail, String orderConnectPhone, Order order) {
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
        } else if (!(Integer.parseInt(orderTotalCase) ==  order.getOrdeTotalCase())) {
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


    private Container compareModifyContainer(String containerOwner, String containerOwnerCode, String containerCaseNo, String containerCount, String containerDrayageDepot, String containerDrayageDepotCode, String containerReturnDepot, String containerReturnDepotCode, String containerGrossWeight, String containerRfTemp, String containerTemperUnit, String containerType,
                                             String containerCartonSize, String containerIsOverWeight, String containerIsCheck, String containerIsCheckliest, String containerIsReplace, String containerDynamo, String containerIsMoreUnload,  String containerOther, String containerRemark, Container container) {
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

    private Address compareModifyAddress(String addressPlace, String addressArriveTime, String addressContacts, String addressTel, String addressProvince, String addressCity, String addressDistrict, Address address) {
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

    private List<OrderExportSearchRespVo> toExportVoList(List<Order> orderList) {

        if (null == orderList || 0 == orderList.size()) {
            return new ArrayList<OrderExportSearchRespVo>(0);
        }
        Order order;
        OrderExportSearchRespVo exportVo;
        List<OrderExportSearchRespVo> exportSearchVoList = new ArrayList<OrderExportSearchRespVo>(orderList.size());
        for (int i = 0; i < orderList.size(); i ++) {
            order = orderList.get(i);
            exportVo = new OrderExportSearchRespVo(i + 1, order.getOrdeId(), order.getOrdeBillNo(), order.getOrdeWharf(), order.getOrdeShipName(), order.getOrdeSailing(), order.getOrdeState(), order.getOrdeSailingDate(), order.getOrdeEnterPort(), order.getOrdeDestinationPort(), order.getOrdeShippingCode(), sdf);
            exportSearchVoList.add(exportVo);
        }
        return exportSearchVoList;
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

}
