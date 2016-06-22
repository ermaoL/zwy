package com.sinotrans.transport.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by emi on 2016/6/1.
 */
public class TestMessage {

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<ServiceRequest> request = new HttpEntity<ServiceRequest>(new ServiceRequest("SEND_SMS", new ServiceParam("15710605079", "发短信了呵呵呵呵")), headers);
        ResponseEntity<MessageResponse> response = restTemplate.postForEntity("http://172.21.180.56:8088/msgserver/service/RemoteService", request, MessageResponse.class);
        MessageResponse messageResponse = response.getBody();
        System.out.println(messageResponse);
    }
}

class ServiceRequest {

    private String serviceName;

    private ServiceParam serviceParam;

    public ServiceRequest(String serviceName, ServiceParam serviceParam) {
        this();
        this.serviceName = serviceName;
        this.serviceParam = serviceParam;
    }

    public ServiceRequest() {
        super();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceParam getServiceParam() {
        return serviceParam;
    }

    public void setServiceParam(ServiceParam serviceParam) {
        this.serviceParam = serviceParam;
    }
}

class ServiceParam {

    private String receive;

    private String message;

    public ServiceParam(String receive, String message) {
        this();
        this.receive = receive;
        this.message = message;
    }

    public ServiceParam() {
        super();
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

class MessageResponse{

    private String message;

    private String data;

    private boolean isSuccess;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
