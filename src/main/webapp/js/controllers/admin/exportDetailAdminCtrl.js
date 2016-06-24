/**
 * Created by Administrator on 2016/5/27.
 */

myAdminApp.controller('exportDetailAdminCtrl', function($scope, $http, $timeout) {

    laydate({
        elem: '#_eDetailArriveTime'
    });
    $('#laydate_box').css("display", "none");
    $('#_eDetailLadingBillNum').focus();

    $('[name="nice-select"]').click(function(e){
        $('[name="nice-select"]').find('ul').hide();
        $(this).find('ul').show();
        e.stopPropagation();
    });
    $('[name="nice-select"] li').hover(function(e){
        $(this).toggleClass('on');
        e.stopPropagation();
    });
    $('[name="nice-select"] li').click(function(e){
        var val = $(this).text();
        $(this).parents('[name="nice-select"]').find('input').val(val);
        $('[name="nice-select"] ul').hide();
        e.stopPropagation();
    });
    $(document).click(function(){
        $('[name="nice-select"] ul').hide();
    });
    $('#exportOrderWharf li').click(function(e){
        var val = $.trim($(this).find('.boat-item-name').text());
        var code = $.trim($(this).find('.boat-item').text());
        $(this).parents('[name="nice-select"]').find('input').val(val);
        $(this).parents('[name="nice-select"]').find('input').attr("wharfCode", code);
        $('[name="nice-select"] ul').hide();
        e.stopPropagation();
    });

    $('#export-detail-back').click(function() {
        $('#export-list-menu').addClass('menu-active');
        $('#export-list-menu').find('.home-arrow').css("display", "block");
        $('#export-list-menu').siblings().removeClass('menu-active');
        $('#export-list-menu').siblings().find('.home-arrow').css("display", "none");
        sessionStorage.setItem("menuStatus", 3);
    });

    $scope.containerExportType = $.parseJSON(sessionStorage.getItem("queryContainerType"));

    $scope.containerSize = $.parseJSON(sessionStorage.getItem("queryContainerSize"));

    $scope.eDetailBindCompany = $.cookie("userCompany");

    var admin = $.cookie("adminCode");
    if (admin == 'admin') {
        $scope.isAdmin = false;
    } else {
        $scope.isAdmin = true;
    }

    $scope.examExportOrder = function () {
        $('#exam-dialogBox').dialogBox({
            type: 'correct',  //three type:'normal'(default),'correct','error',
            width: 300,
            height: 200,
            hasMask: true,
            hasClose: true,
            hasBtn: true,
            confirmValue: "通过",
            cancelValue: "不通过",
            confirm: function () {
                var orderId = sessionStorage.getItem("exportOrderId");
                var url = "/trans/base/admin/order/" + orderId + "/verifier";
                var requestData = {
                    "ifPass": 1
                };
                var transform = function (data) {
                    return $.param(data);
                }
                $http.post(url, requestData, {
                    headers: {
                        "adminCode": $.cookie("adminCode"),
                        "token": $.cookie("token"),
                        "authType": $.cookie("authType"),
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                    transformRequest: transform
                }).success(function(data) {
                    if (data.success) {
                        Dialog.alert("订单审核通过");
                        window.location.href = "admin.html#/admin/main/export/list";
                    }else{
                        errorAdminMsgHint(data.errorCode, data.errorMsg);
                    }
                });
                // alert("审核通过！");
            },
            cancel: function(){
                $timeout(function sendRejectReason() {
                        $('#reject-dialogBox').dialogBox({
                            type: 'normal',  //three type:'normal'(default),'correct','error',
                            width: 300,
                            height: 350,
                            hasMask: true,
                            hasClose: true,
                            hasBtn: true,
                            confirmValue: "发送",
                            cancelValue: "取消",
                            confirm: function () {
                                var exportRejectReason = $('#exportRejectReason').val();
                                var orderId = sessionStorage.getItem("exportOrderId");
                                var url = "/trans/base/admin/order/" + orderId + "/verifier";
                                var requestData = {
                                    "ifPass": 0,
                                    "failedReason": exportRejectReason
                                };
                                var transform = function (data) {
                                    return $.param(data);
                                }
                                $http.post(url, requestData, {
                                    headers: {
                                        "adminCode": $.cookie("adminCode"),
                                        "token": $.cookie("token"),
                                        "authType": $.cookie("authType"),
                                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                                    transformRequest: transform
                                }).success(function(data) {
                                    if (data.success) {
                                        Dialog.alert("订单审核不通过");
                                        window.location.href = "admin.html#/admin/main/export/list";
                                    }else{
                                        errorAdminMsgHint(data.errorCode, data.errorMsg);
                                    }
                                });
                                // alert("发送   " + exportRejectReason);
                            },
                            effect: '',
                            title: '   ',
                            content: '<div style="text-align: center;"><div style="font-size: 16px; margin-bottom: 10px; margin-top: -10px;">审核不通过</div><textarea id="exportRejectReason" style="width: 260px; height: 200px;" placeholder="请输入理由..." /></div>'
                        });
                    }, 300);
            },
            effect: 'sign',
            title: '提示',
            content: '是否通过该订单的审核'
        });
    }

    $scope.endExportBoxTrans = function (id) {
        $('#finish-dialogBox').dialogBox({
            type: 'correct',  //three type:'normal'(default),'correct','error',
            width: 300,
            height: 200,
            hasMask: true,
            hasClose: true,
            hasBtn: true,
            confirmValue: "确定",
            cancelValue: "取消",
            confirm: function () {
                Dialog.alert("结束");
            },
            cancel: function(){
                Dialog.alert("不结束");
            },
            effect: 'sign',
            title: '提示',
            content: '是否结束该箱子'
        });

    }

    $scope.detailData1 = [];
    $scope.detailData3 = [];

    var eOperate = sessionStorage.getItem("eOperate");
    var exportOrderId = sessionStorage.getItem("exportOrderId");

    // console.log("detail:  "+exportOrderId);

    if (eOperate == "1" && exportOrderId != "") {
        var url = "/trans/base/admin/export/" + exportOrderId + "/all";

        $http({
            url: url,
            method: 'GET',
            params: {"orderId": exportOrderId},
            headers: {  "adminCode": $.cookie("adminCode"),
                        "token": $.cookie("token"),
                        "authType": $.cookie("authType")}
        }).success(function (data) {
            var getExportDetail = data;
            var orderVo = getExportDetail.orderVo;
            $scope.exportDetailOrderBillNo = orderVo.orderBillNo;
            $scope.exportDetailOrderStateDec = orderVo.orderStateDesc;
            $scope.exportDetailOrderState = orderVo.orderState;
            $scope.exportDetailOrderWharf = orderVo.orderWharf;
            $scope.exportDetailOrderShipName = orderVo.orderShipName;
            $scope.exportDetailOrderSailing = orderVo.orderSailing;
            $scope.exportDetailOrderShipAgency = orderVo.orderShipAgency;
            $scope.exportDetailOrderSailingDate = orderVo.orderSailingDate;
            $scope.exportDetailOrderPort = orderVo.orderPort;
            $scope.exportDetailOrderEnterPort = orderVo.orderEnterPort;
            $scope.exportDetailOrderDestinationPort = orderVo.orderDestinationPort;
            $scope.exportDetailShippingCode = orderVo.orderShippingCode;
            $scope.exportDetailBookingNumber = orderVo.orderBookingNumber;
            $scope.exportDetailTotalCase = getExportDetail.containerVoList.length;
            $scope.exportDetailOwner = orderVo.orderOwner;
            $scope.exportDetailOrderFreeUseDay = orderVo.orderFreeUseDay;
            $scope.exportDetailVerifierContent = orderVo.verifierContent;
            $scope.exportDetailOrderGoodOwner = orderVo.orderGoodOwner;
            $scope.exportDetailRemark = orderVo.orderRemark;
            $('#_eDetailOwner').attr("ownerCode", orderVo.orderOwnerCode);
            $('#_eDetailGoodOwner').attr("ownerCode", orderVo.orderGoodOwnerCode);
            $('#_eDetailWharf').attr("wharfCode", orderVo.orderWharfCode);
            $('#_eDetailBoatName').attr("boatCode", orderVo.orderShipNameCode);
            $('#_eDetailVoyage').attr("sailCode", orderVo.orderSailingCode);
            $('#_eDetailStartPort').attr("wharfCode", orderVo.orderPortCode);
            $('#_eDetailEnterPort').attr("wharfCode", orderVo.orderEnterPortCode);
            $('#_eDetailDestinationPort').attr("wharfCode", orderVo.orderDestinationPortCode);

            if (orderVo.orderCompany == "裕国(FZZZ)") {
                $scope.exportDetailOrderCompany = "裕国(FZZZ)";
            } else {
                $scope.exportDetailOrderCompany = "裕雄(XMZZ)";
            }

            if (orderVo.orderSystemNo == "") {
                $scope.exportDetailOrderSystemNo = "";
            } else {
                $scope.exportDetailOrderSystemNo = orderVo.orderSystemNo;
            }

            if (orderVo.orderCheckContainer == null) {
                $scope.exportDetailOrderCheckContainer = "否";
            } else {
                if (orderVo.orderCheckContainer == "0") {
                    $scope.exportDetailOrderCheckContainer = "否";
                } else {
                    $scope.exportDetailOrderCheckContainer = "是";
                }

            }

            if (orderVo.orderConnects == "null") {
                $scope.exportDetailOrderContact = "";
            } else {
                $scope.exportDetailOrderContact = orderVo.orderConnects;
            }
            if (orderVo.orderConnectPhone == "null") {
                $scope.exportDetailOrderContactPhone = "";
            } else {
                $scope.exportDetailOrderContactPhone = orderVo.orderConnectPhone;
            }
            if (orderVo.orderConnectEmail == "null") {
                $scope.exportDetailOrderContactEmail = "";
            } else {
                $scope.exportDetailOrderContactEmail = orderVo.orderConnectEmail;
            }
            sessionStorage.setItem("exportFirstContainerId", getExportDetail.firstContainerId);
            $scope.detailData1 = getExportDetail.containerVoList;
            $scope.detailData3 = getExportDetail.addressVoList;


        });

        /* $.ajax({
         type: "GET",
         url: url,
         params: {orderId: importOrderId},
         success: function (data) {
         sessionStorage.setItem("importDetail", data);
         }
         });*/
    } else if (eOperate == "0") {
        $scope.exportDetailOrderBillNo = "";
        $scope.exportDetailOrderState = "1";
        $scope.exportDetailOrderStateDec = "已下发";
        $scope.exportDetailOrderWharf = "";
        $scope.exportDetailOrderShipName = "";
        $scope.exportDetailOrderSailing = "";
        $scope.exportDetailOrderShipAgency = "";
        $scope.exportDetailOrderSailingDate = "";
        $scope.exportDetailOrderPort = "";
        $scope.exportDetailOrderEnterPort = "";
        $scope.exportDetailOrderDestinationPort = "";
        $scope.exportDetailShippingCode = "";
        $scope.exportDetailBookingNumber = "";
        $scope.exportDetailTotalCase = "0";
        $scope.exportDetailOwner = "";
        $scope.exportDetailOrderFreeUseDay = "";
        $scope.exportDetailOrderCheckContainer = "否";
        $scope.exportDetailOrderCompany = "裕雄(XMZZ)";
        $scope.exportDetailOrderSystemNo = "";
        $scope.exportDetailOrderGoodOwner = "";
        $scope.exportDetailRemark = "";
    }

    /*// 回车键
     $scope.enterExport = function (ev) {
     if (ev.keyCode !== 13) {
     return;
     } else {
     alert("enter export");
     }
     }

     $('#_eDetailLadingBillNum').blur(function () {
     alert("search export");
     });*/

    $('#export-detail-first-table').css("max-height", 450);
    $('#export-detail-first-table').css("overflow-y", "auto");
    $('#export-detail-second-table').css("max-height", 450);
    $('#export-detail-second-table').css("overflow-y", "auto");

    $scope.getExportAddressList = function ($event, id) {
        var item = $event.target;
        $(item).parents('tr').css("background", "#d7f1f2");
        $(item).parents('tr').siblings('tr').css("background", "#ffffff");
        // console.log("containerId:   "+ id);
        sessionStorage.setItem("exportFirstContainerId", id);
        var url = "/trans/base/admin/export/" + id + "/address";
        $http({
            url: url,
            method:'GET',
            params: {containerId: id},
            headers: {
                "adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType")}
        }).success(function (data) {
            if (data.success) {
                $scope.detailData3 = data.data;
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        });
    }

    $scope.saveExportOrder = function () {
        var billNo = $scope.exportDetailOrderBillNo;
        var totalCaseNum = $scope.exportDetailTotalCase;
        var contact = $scope.exportDetailOrderContact;
        var contactPhone = $scope.exportDetailOrderContactPhone;
        var contactEmail = $scope.exportDetailOrderContactEmail;
        var orderGood = $('#_eDetailGoodOwner').val();
        var orderGoodCode = $('#_eDetailGoodOwner').attr("ownerCode");
        var orderOwner = $('#_eDetailOwner').val();
        var orderOwnerCode = $('#_eDetailOwner').attr("ownerCode");
        var orderWharf = $('#_eDetailWharf').val();
        var orderWharfCode = $('#_eDetailWharf').attr("wharfCode");
        var orderShipName = $('#_eDetailBoatName').val();
        var orderShipNameCode = $('#_eDetailBoatName').attr("boatCode");
        var orderSailing = $('#_eDetailVoyage').val();
        var orderSailingCode = $('#_eDetailVoyage').attr("sailCode");
        var orderEnterPort = $('#_eDetailEnterPort').val();
        var orderEnterPortCode = $('#_eDetailEnterPort').attr("wharfCode");
        var orderDestinationPort = $('#_eDetailDestinationPort').val();
        var orderDestinationPortCode = $('#_eDetailDestinationPort').attr("wharfCode");
        if (billNo == "" || billNo == null) {
            alert("提单号不能为空！");
            return false;
        } else if (totalCaseNum === "" || totalCaseNum === null) {
            alert("总箱量不能为空！");
            return false;
        } else if (contact === "" || contact === null) {
            alert("联系人不能为空！");
            return false;
        } else if (contactPhone === "" || contactPhone === null) {
            alert("联系电话不能为空！");
            return false;
        } else if (contactEmail === "" || contactEmail === null) {
            alert("联系邮箱不能为空！");
            return false;
        } else if (orderGood != "" && orderGoodCode == "") {
            alert("货主信息错误！");
            return false;
        } else if (orderOwner != "" && orderOwnerCode == "") {
            alert("箱主信息错误！");
            return false;
        } else if (orderWharf != "" && orderWharfCode == "") {
            alert("码头信息错误！");
            return false;
        } else if (orderShipName != "" && orderShipNameCode == "") {
            alert("船名信息错误！");
            return false;
        } else if (orderSailing != "" && orderSailingCode == "") {
            alert("航次信息错误！");
            return false;
        } else if (orderEnterPort != "" && (orderEnterPortCode == "" || orderEnterPortCode == undefined)) {
            alert("中转港信息错误！");
            return false;
        } else if (orderDestinationPort != "" && (orderDestinationPortCode == "" || orderDestinationPortCode == undefined)) {
            alert("目的港信息错误！");
            return false;
        }
        var datas;
        var orderId = sessionStorage.getItem("exportOrderId");
        var company = $("#exportOrderCompany").val();
        var isValidContainer = $("#exportOrderValidContainer").val();
        if (company == "裕雄(XMZZ)") {
            company = 1;
        } else if (company == "裕国(FZZZ)") {
            company = 2;
        }
        if (isValidContainer == "否") {
            isValidContainer = "0";
        } else if (isValidContainer == "是") {
            isValidContainer = "1";
        }
        if (orderId == "") {
            datas = {
                "orderBillNo": $scope.exportDetailOrderBillNo,
                "orderSystemNo": $scope.exportDetailOrderSystemNo,
                "orderState": $scope.exportDetailOrderState,
                "orderCompany": company,
                "orderWharf": $('#_eDetailWharf').val(),
                "orderShipName": $('#_eDetailBoatName').val(),
                "orderSailing": $('#_eDetailVoyage').val(),
                "orderShipAgency": $('#_eDetailBoatAgent').val(),
                "orderSailingDate": $('#_eDetailArriveTime').val(),
                "orderPort": $('#_eDetailStartPort').val(),
                "orderEnterPort": $('#_eDetailEnterPort').val(),
                "orderDestinationPort": $('#_eDetailDestinationPort').val(),
                "orderShippingCode": $scope.exportDetailShippingCode,
                "orderBookingNumber": $scope.exportDetailBookingNumber,
                "orderTotalCase": $scope.exportDetailTotalCase,
                "orderOwner": $('#_eDetailOwner').val(),
                "orderFreeUseDay": $scope.exportDetailOrderFreeUseDay,
                "orderCheckContainer": isValidContainer,
                "orderOwnerCode": $('#_eDetailOwner').attr("ownerCode"),
                "orderWharfCode": $('#_eDetailWharf').attr("wharfCode"),
                "orderShipNameCode": $('#_eDetailBoatName').attr("boatCode"),
                "orderSailingCode": $('#_eDetailVoyage').attr("sailCode"),
                "orderPortCode": $('#_eDetailStartPort').attr("wharfCode"),
                "orderEnterPortCode": $('#_eDetailEnterPort').attr("wharfCode"),
                "orderDestinationPortCode": $('#_eDetailDestinationPort').attr("wharfCode"),
                "orderGoodOwner": $('#_eDetailGoodOwner').val(),
                "orderGoodOwnerCode": $('#_eDetailGoodOwner').attr("ownerCode"),
                "orderConnects": $scope.exportDetailOrderContact,
                "orderConnectPhone": $scope.exportDetailOrderContactPhone,
                "orderConnectEmail": $scope.exportDetailOrderContactEmail,
                "orderRemark": $scope.exportDetailRemark
            };
        } else {
            datas = {
                "orderId": sessionStorage.getItem("exportOrderId"),
                "orderBillNo": $scope.exportDetailOrderBillNo,
                "orderSystemNo": $scope.exportDetailOrderSystemNo,
                "orderState": $scope.exportDetailOrderState,
                "orderCompany": company,
                "orderWharf": $('#_eDetailWharf').val(),
                "orderShipName": $('#_eDetailBoatName').val(),
                "orderSailing": $('#_eDetailVoyage').val(),
                "orderShipAgency": $('#_eDetailBoatAgent').val(),
                "orderSailingDate": $('#_eDetailArriveTime').val(),
                "orderPort": $('#_eDetailStartPort').val(),
                "orderEnterPort": $('#_eDetailEnterPort').val(),
                "orderDestinationPort": $('#_eDetailDestinationPort').val(),
                "orderShippingCode": $scope.exportDetailShippingCode,
                "orderBookingNumber": $scope.exportDetailBookingNumber,
                "orderTotalCase": $scope.exportDetailTotalCase,
                "orderOwner": $('#_eDetailOwner').val(),
                "orderFreeUseDay": $scope.exportDetailOrderFreeUseDay,
                "orderCheckContainer": isValidContainer,
                "orderOwnerCode": $('#_eDetailOwner').attr("ownerCode"),
                "orderWharfCode": $('#_eDetailWharf').attr("wharfCode"),
                "orderShipNameCode": $('#_eDetailBoatName').attr("boatCode"),
                "orderSailingCode": $('#_eDetailVoyage').attr("sailCode"),
                "orderPortCode": $('#_eDetailStartPort').attr("wharfCode"),
                "orderEnterPortCode": $('#_eDetailEnterPort').attr("wharfCode"),
                "orderDestinationPortCode": $('#_eDetailDestinationPort').attr("wharfCode"),
                "orderGoodOwner": $('#_eDetailGoodOwner').val(),
                "orderGoodOwnerCode": $('#_eDetailGoodOwner').attr("ownerCode"),
                "orderConnects": $scope.exportDetailOrderContact,
                "orderConnectPhone": $scope.exportDetailOrderContactPhone,
                "orderConnectEmail": $scope.exportDetailOrderContactEmail,
                "orderRemark": $scope.exportDetailRemark};
        }

        var transform = function (data) {
            return $.param(data);
        }

        $http.post("/trans/base/admin/export", datas, {
            headers: {
                "adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType"),
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            transformRequest: transform
        }).success(function(data) {
            if (data.success) {
                Dialog.alert("单据主信息保存成功");
                sessionStorage.setItem("exportOrderId", data.orderId);
                $scope.exportDetailOrderSystemNo = data.orderSystemNo;
            }else{
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        });
    }

    $scope.saveExportContainer = function () {
        var data = [];
        $("#exportContainerTable tr:not(:first)").each(function(i) {
            $(this).children("td").each(function(j) {
                if(!data[i]){
                    data[i] = new Array();
                }
                data[i][j]= $(this).children().val();

                if (j == 4) {
                    data[i][4] = $('#_eDetailOwner').val();
                    data[i][22] = $.trim($('#_eDetailOwner').attr("ownerCode"));
                }
                /*if (j == 5) {
                 var val = parseInt($(this).children().val());
                 var typeCode = $scope.containerExportType[val].typeCode;
                 data[i][5] = typeCode;
                 }*/
                /*if (j == 11) {
                 if ($(this).children().val() == null || $(this).children().val() == "" || $(this).children().val() == undefined || $(this).children().val() == "? string:null ?") {
                 data[i][11]= "0";
                 } else {
                 data[i][11]= $(this).children().val();
                 }
                 }*/
                if (j == 6) {
                    var val = $(this).children().val();
                    if (val.length > 2) {
                        data[i][6] = "";
                    }

                }
                if (j == 7) {
                    var val = $(this).children().val();
                    if (val.length > 2) {
                        data[i][7] = "";
                    }

                }
                if (j == 8) {
                    var draDepot = $.trim($(this).children().attr("containerAddressCode"));
                    data[i][20] = draDepot;
                }
                if (j == 9) {
                    var reDepot = $.trim($(this).children().attr("containerAddressCode"));
                    data[i][21] = reDepot;
                }
                if (j == 12) {
                    var isChecked = $(this).children().prop('checked');
                    if (isChecked) {
                        data[i][12] = "是";
                    } else {
                        data[i][12] = "否";
                    }
                }
                if (j == 13) {
                    var isChecked = $(this).children().prop('checked');
                    if (isChecked) {
                        data[i][13] = "是";
                    } else {
                        data[i][13] = "否";
                    }
                }
                if (j == 16) {
                    var isChecked = $(this).children().prop('checked');
                    if (isChecked) {
                        data[i][16] = "是";
                    } else {
                        data[i][16] = "否";
                    }
                }
                if (j == 19) {
                    var isChecked = $(this).children().prop('checked');
                    if (isChecked) {
                        data[i][19] = "是";
                    } else {
                        data[i][19] = "否";
                    }
                }
            });
        });

        var alterContainer = new Array();
        for (var i = 0; i < data.length; i++) {
            var containerInfo = {
                /*"containerId": data[i][2],
                 "containerOwner": data[i][3],
                 "containerCaseNo": data[i][4],
                 "containerType": data[i][5],
                 "containerCartonSize": data[i][6],
                 "containerCount": data[i][7],
                 "containerGrossWeight": data[i][8],
                 "containerDynamo": data[i][9],
                 "containerRfTemp": data[i][10],
                 "containerTemperUnit": data[i][11],
                 "containerIsOverWeight": data[i][12],
                 "containerIsCheck": data[i][13],
                 "containerIsCheckliest": data[i][14],
                 "containerIsReplace": data[i][15],
                 "containerOwnerCode": data[i][17]*/
                "containerId": data[i][2],
                "containerOwner": data[i][4],
                "containerCaseNo": data[i][5],
                "containerType": data[i][6],
                "containerCartonSize": data[i][7],
                "containerCount": data[i][10],
                "containerDrayageDepot": data[i][8],
                "containerReturnDepot": data[i][9],
                "containerGrossWeight": data[i][11],
                "containerIsOverWeight": data[i][12],
                "containerDynamo": data[i][13],
                "containerRfTemp": data[i][14],
                "containerTemperUnit": "1",
                "containerIsCheck": data[i][16],
                "containerIsCheckliest": "",
                "containerIsReplace": "",
                "containerIsMoreUnload": data[i][19],
                "containerOwnerCode": data[i][22],
                "containerDrayageDepotCode": data[i][20],
                "containerReturnDepotCode": data[i][21]
            };
            alterContainer.push(containerInfo);
        }

        var addData = {
            "containerOrderId": sessionStorage.getItem("exportOrderId"),
            "containerVoList": alterContainer
        };

        addData = JSON.stringify(addData);

        $http.post("/trans/base/admin/export/container", addData, {
            headers: {
                "adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType")}}).success(function(data) {
            if(data.success){
                $scope.detailData1 = data.data;
                if (data.data.length > 0) {
                    sessionStorage.setItem("exportFirstContainerId", data.data[0].containerId);
                }

                $scope.exportDetailTotalCase = data.data.length;
                Dialog.alert("箱信息保存成功");
            }else{
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        });

        /*$.ajax({
         type: "POST",
         url: "/trans/api/export/container",
         contentType:'application/json; charset=UTF-8',
         dataType:'json',
         data: addData,
         success: function(data1) {
         if (data1.success) {
         var firstContainerId = data1.data[0].containerId;
         sessionStorage.setItem("exportFirstContainerId", firstContainerId);
         $scope.detailData1 = data1.data;
         alert("箱信息保存成功");
         }else{
         console.log(data1.errorMsg);
         }
         }
         });*/

    }

    $scope.saveExportAddress = function () {
        var data = [];
        $("#exportAddressTable tr:not(:first)").each(function(i) {
            $(this).children("td").each(function(j) {
                if(!data[i]){
                    data[i] = new Array();
                }
                data[i][j]= $(this).children().val();
                if (j == 7) {
                    var addr = $.trim($(this).children().val());
                    if (addr.indexOf('?') > -1) {
                        data[i][7] = "";
                    }
                } else if (j == 8) {
                    var addr = $.trim($(this).children().val());
                    if (addr.indexOf('?') > -1) {
                        data[i][8] = "";
                    }
                } else if (j == 9) {
                    var addr = $.trim($(this).children().val());
                    if (addr.indexOf('?') > -1) {
                        data[i][9] = "";
                    }
                }

            });
        });

        var alterAddress = new Array();
        for (var i = 0; i < data.length; i++) {
            var addressInfo = {
                "addressId": data[i][2],
                "addressPlace": data[i][3],
                "addressArriveTime": data[i][4],
                "addressContacts": data[i][5],
                "addressTel": data[i][6],
                "addressProvince": data[i][7],
                "addressCity": data[i][8],
                "addressDistrict": data[i][9]
            };
            alterAddress.push(addressInfo);
        }

        var addData = {
            "containerId": sessionStorage.getItem("exportFirstContainerId"),
            "addressVoList": alterAddress
        };

        addData = JSON.stringify(addData);

        $http.post("/trans/base/admin/export/address", addData, {
            headers: {"adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType")}
        }).success(function(data) {
            if(data.success){
                $scope.detailData3 = data.data;
                Dialog.alert("地址信息保存成功");
            }else{
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        });

    }

    /******************** 出口订单维护表格操作 ************************/
    // 新增
    $scope.addExportBox = function () {
        $scope.inserted = {
            "index":$scope.detailData1.length+1,
            "containerId":"",
            "containerOwner":"",
            "containerType":$scope.containerExportType[0].typeCode,
            "containerCartonSize":$scope.containerSize[0].typeCode,
            "containerCount":"",
            "containerGrossWeight":"",
            "containerDynamo":"否",
            "containerRfTemp":"",
            "containerTemperUnit":0,
            "containerIsOverWeight":"否",
            "containerIsCheck":"否",
            "containerIsCheckliest":"是",
            "containerIsReplace":"是",
            "containerStateTypeDesc":"已下单",
            "containerIsMoreUnload":"否"
        };
        $scope.detailData1.push($scope.inserted);
    }

    // 新增
    $scope.addExportBoxInfo = function () {
        $scope.inserted = {
            "index":$scope.detailData3.length+1,
            "addressId":"",
            "addressPlace":"",
            "addressDeliveryTime":"",
            "addressContacts":"",
            "addressTel":"",
            "addressProvince":"",
            "addressCity":"",
            "addressDistrict":""
        };
        $scope.detailData3.push($scope.inserted);
    }

    // 初始化批量新增
    $scope.exportMultiContainerType = $scope.containerExportType[0].typeCode;
    $scope.exportMultiContainerSize = $scope.containerSize[0].typeCode;
    $scope.exportMultiContainerDynamo = '否';
    $scope.exportMultiContainerTemperUnit = '0';
    $scope.exportMultiContainerIsOverWeight = '否';
    $scope.exportMultiContainerIsCheck = '否';
    $scope.exportMultiContainerIsCheckliest = '否';
    $scope.exportMultiContainerIsReplace = '否';
    $scope.exportMultiUpload = '否';

    // 批量新增
    $scope.addExportBoxes = function () {
        var num = $scope.exportMultiAdd.numbers;
        for (var i = 0; i < num; i++) {
            $scope.inserted = {
                "index":$scope.detailData1.length+1,
                "containerId":"",
                "containerCaseNo":$scope.exportMultiContainerNo,
                "containerOwner": "",
                "containerOwnerCode": "",
                "containerType":$scope.exportMultiContainerType,
                "containerCartonSize":$scope.exportMultiContainerSize,
                "containerDrayageDepot": $('#_eBoxDrayages').val(),
                "containerReturnDepot": $('#_eBoxReturns').val(),
                "containerDrayageDepotCode": $('#_eBoxDrayages').attr("containerAddressCode"),
                "containerReturnDepotCode": $('#_eBoxReturns').attr("containerAddressCode"),
                "containerCount": "",
                "containerGrossWeight":$scope.exportMultiContainerGrossWeight,
                "containerDynamo":$scope.exportMultiContainerDynamo,
                "containerRfTemp": "",
                "containerTemperUnit": "",
                "containerIsOverWeight":$scope.exportMultiContainerIsOverWeight,
                "containerIsCheck":$scope.exportMultiContainerIsCheck,
                "containerIsCheckliest": "",
                "containerIsReplace": "",
                "containerIsMoreUnload": $scope.exportMultiUpload,
                "containerStateTypeDesc":"已下单"
            };
            $scope.detailData1.push($scope.inserted);
        }
    }

    $scope.selectExportAll1 = function () {
        var temp = $('#export-select-all1').prop('checked');
        if (temp) {
            $('input[name=export-box]:checkbox').prop("checked", true);
        } else {
            $('input[name=export-box]:checkbox').prop("checked", false);
        }
    }

    $scope.selectExportAll2 = function () {
        var temp = $('#export-select-all2').prop('checked');
        if (temp) {
            $('input[name=export-boxInfo]:checkbox').prop("checked",true);
        } else {
            $('input[name=export-boxInfo]:checkbox').prop("checked",false);
        }
    }

    $scope.selectExportAll3 = function () {
        var temp = $('#export-select-all3').prop('checked');
        if (temp) {
            $('input[name=export-box]:checkbox').prop("checked", true);
        } else {
            $('input[name=export-box]:checkbox').prop("checked", false);
        }
    }

    $scope.selectExportAll4 = function () {
        var temp = $('#export-select-all4').prop('checked');
        if (temp) {
            $('input[name=export-boxInfo]:checkbox').prop("checked",true);
        } else {
            $('input[name=export-boxInfo]:checkbox').prop("checked",false);
        }
    }

    $scope.delExportBoxes = function () {
        var box = $("input[name=export-box]:checked");
        if(box.size()==0){
            Dialog.alert("要删除指定行，需选中要删除的行");
            return;
        }
        var containerIdArr = new Array();
        box.each(function(){
            var delId = $(this).parent().parent().find('.exportContainerId').val();
            // console.log(delId);
            if (delId == "") {
                $(this).parent().parent().remove();
            } else {
                containerIdArr.push(delId);
            }

        });

        var delInfo = {
            "idList": containerIdArr
        };

        delInfo = JSON.stringify(delInfo);

        if (containerIdArr.length != 0) {
            $http.post("/trans/base/admin/export/container/drop", delInfo, {
                headers: {
                    "adminCode": $.cookie("adminCode"),
                    "token": $.cookie("token"),
                    "authType": $.cookie("authType")}}).success(function(data) {
                if(data.success){
                    $scope.detailData1 = data.containerVoList;
                    $scope.exportDetailTotalCase = data.containerVoList.length;
                    sessionStorage.setItem("exportFirstContainerId", data.firstContainerId);
                    $scope.detailData3 = data.addressVoList;
                    Dialog.alert("删除成功");
                }else{
                    errorAdminMsgHint(data.errorCode, data.errorMsg);
                }
            });
            /*$.ajax({
             type: "POST",
             url: "/trans/api/export/container/drop",
             contentType:'application/json; charset=UTF-8',
             dataType:'json',
             data: delInfo,
             success: function(data) {
             if (data.success) {
             alert("删除成功");
             }else{
             console.log(data.errorMsg);
             }
             }
             });*/
        }
    }

    $scope.delExportBoxInfo = function () {
        var boxInfo = $("input[name=export-boxInfo]:checked");
        if(boxInfo.size()==0){
            Dialog.alert("要删除指定行，需选中要删除的行");
            return;
        }

        var addressIdArr = new Array();
        boxInfo.each(function(){
            var delId = $(this).parent().parent().find('.exportAddressId').val();
            // console.log(delId);
            if (delId == "") {
                $(this).parent().parent().remove();
            } else {
                addressIdArr.push(delId);
            }
        });

        var delInfo = {
            "idList": addressIdArr
        };

        delInfo = JSON.stringify(delInfo);

        if (addressIdArr.length != 0) {
            $http.post("/trans/base/admin/export/address/drop", delInfo, {
                headers: {
                    "adminCode": $.cookie("adminCode"),
                    "token": $.cookie("token"),
                    "authType": $.cookie("authType")}}).success(function(data) {
                if(data.success){
                    $scope.detailData3 = data.data;
                    Dialog.alert("删除成功");
                }else{
                    errorAdminMsgHint(data.errorCode, data.errorMsg);
                }
            });
            /*$.ajax({
             type: "POST",
             url: "/trans/api/export/container/drop",
             contentType:'application/json; charset=UTF-8',
             dataType:'json',
             data: delInfo,
             success: function(data) {
             if (data.success) {
             alert("删除成功");
             }else{
             console.log(data.errorMsg);
             }
             }
             });*/
        }

    }

});

function getContainerOwnerInfo(item) {
    var owner = $(item).val();
    var url = '/trans/api/queryCrmTypeIdIsCW?codeOrName='+owner;
    $.ajax({
        url:url,
        method:'GET',
        headers:{"userName": $.cookie("userName"), "token": $.cookie("token")},
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "<ul class='owner-list'>";
                for (var i = 0; i < data.crms.length; i++) {
                    result += "<li class='owner-item' onclick='setExportOwnerName(this)'>" +
                        "<span style='display: block; width: 55px; float: left;' class='owner-code'>" + data.crms[i].crmCode + "</span>" +
                        "<span class='owner-company-name'>" + data.crms[i].companyName + "</span></li>";
                }
                result += "</ul>";
                $(item).siblings('.owner-container').css("display","block");
                $(item).siblings('.owner-container').html(result);
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });

}

function setExportOwnerName(item) {
    var owner = $.trim($(item).find('.owner-company-name').text());
    var code = $.trim($(item).find('.owner-code').text());
    $(item).parent().parent().siblings('.exportOwnerInfo').val(owner);
    $(item).parent().parent().siblings('.exportOwnerInfo').attr("ownerCode", code);
    $(item).parent().parent().siblings('.table-div').val(owner);
    $(item).parent().parent().css("display", "none");
}

function getExportContainerAddress(item) {
    var owner = $(item).val();
    var url = '/trans/api/queryDepot?codeOrName='+owner;
    $.ajax({
        url:url,
        method:'GET',
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "<ul class='owner-list'>";
                if (data.depots.length > 0) {
                    for (var i = 0; i < data.depots.length; i++) {
                        result += "<li class='owner-item' onclick='setExportContainerAddress(this)'>" +
                            "<span style='display: block; width: 55px; float: left;' class='owner-code'>" + data.depots[i].depotCode + "</span>" +
                            "<span class='owner-company-name'>" + data.depots[i].cnName + "</span></li>";
                    }
                    result += "</ul>";
                    $(item).siblings('.owner-container').css("display","block");
                    $(item).siblings('.owner-container').html(result);
                }

                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });

}

function setExportContainerAddress(item) {
    var owner = $.trim($(item).find('.owner-company-name').text());
    var code = $.trim($(item).find('.owner-code').text());
    $(item).parent().parent().siblings('.exportOwnerInfo').val(owner);
    $(item).parent().parent().siblings('.exportOwnerInfo').attr("containerAddressCode", code);
    $(item).parent().parent().siblings('.table-div').val(owner);
    $(item).parent().parent().css("display", "none");
}

function setOwnerListHide(item) {
    setTimeout(function () {
        $(item).siblings('.owner-container').css("display", "none");
    }, 300);
}

function getExportOrderOwner(item) {
    var owner = $(item).val();
    var url = '/trans/api/queryCrmTypeIdIsCW?codeOrName='+owner;
    $.ajax({
        url:url,
        method:'GET',
        headers:{"userName": $.cookie("userName"), "token": $.cookie("token")},
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "";
                for (var i = 0; i < data.crms.length; i++) {
                    result += "<li onclick='setExportOrderOwner(this)'><div style='width: 150%; height: 30px;'>" +
                        "<span class='owner-code' style='display: block; width: 55px; float: left;'>" + data.crms[i].crmCode + "</span>" +
                        "<span class='owner-company-name'>" + data.crms[i].companyName + "</span></div></li>";
                }
                $(item).siblings('.exportOrderOwner').html(result);
                $(item).siblings('.exportOrderOwner').show();
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setExportOrderOwner(item) {
    var owner = $(item).find('.owner-company-name').text();
    var code = $(item).find('.owner-code').text();
    $(item).parent().siblings('.exportOwnerInfo').val(owner);
    $(item).parent().siblings('.exportOwnerInfo').attr("ownerCode", code);
    $(item).parent().css("display", "none");
    event.stopPropagation();
}

function getExportOrderWharf(item) {
    var wharf = $(item).val();
    var url = '/trans/api/queryDepot?codeOrName='+wharf;
    $.ajax({
        url:url,
        method:'GET',
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "";
                for (var i = 0; i < data.depots.length; i++) {
                    result += "<li onclick='setExportWharfItem(this)'><div style='width: 150%; height: 30px;'>" +
                        "<span class='boat-item'>" + data.depots[i].depotCode + "</span>" +
                        "<span class='boat-item-name' style='width: 140px;'>" + data.depots[i].cnName + "</span></div></li>";
                }
                $(item).siblings('ul').html(result);
                $(item).siblings('ul').show();
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function RndNum(n){
    var rnd="";
    for(var i=0;i<n;i++)
        rnd+=Math.floor(Math.random()*10);
    return rnd;
}

function setExportWharfItem(item) {
    var enName = $(item).find('.boat-item-name').text();
    $(item).parent().siblings('.importWharfEnName').val(enName);
    $(item).parent().siblings('.importWharfEnName').attr("wharfCode", $(item).find('.boat-item').text());
    $(item).parent().css("display", "none");
    event.stopPropagation();
}

// 获取订单中的港口列表
function getExportOrderPort(item) {
    var wharf = $(item).val();
    var url = '/trans/api/queryPortCode?codeOrEnname='+wharf;
    $.ajax({
        url:url,
        method:'GET',
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "";
                for (var i = 0; i < data.portCodes.length; i++) {
                    result += "<li onclick='setExportPortItem(this)'><div style='width: 150%; height: 30px;'>" +
                        "<span class='boat-item'>" + data.portCodes[i].portCode + "</span>" +
                        "<span class='boat-item-name' style='width: 140px;'>" + data.portCodes[i].enName + "</span></div></li>";
                }
                $(item).siblings('ul').html(result);
                $(item).siblings('ul').show();
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setExportPortItem(item) {
    var enName = $(item).find('.boat-item-name').text();
    $(item).parent().siblings('.importWharfEnName').val(enName);
    $(item).parent().siblings('.importWharfEnName').attr("wharfCode", $(item).find('.boat-item').text());
    $(item).parent().css("display", "none");
    event.stopPropagation();
}

function getExportOrderBoat(item) {
    var boat = $(item).val();
    var url = '/trans/api/queryVessel?codeOrEnname='+boat;
    $.ajax({
        url:url,
        method:'GET',
        headers:{"userName": $.cookie("userName"), "token": $.cookie("token")},
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "";
                for (var i = 0; i < data.vessels.length; i++) {
                    result += "<li onclick='setExportBoatItem(this)'><div style='width: 150%; height: 30px;'>" +
                        "<span class='boat-item boat-item-code'>" + data.vessels[i].vesselCode + "</span>" +
                        "<span class='boat-item-name' style='width: 160px;'>" + data.vessels[i].enName + "</span>" +
                        "<span class='boat-item' style='left: 195px; top: -60px;'>" + data.vessels[i].cnName + "</span></div></li>";
                }
                $('#exportOrderBoat').html(result);
                $('#exportOrderBoat').show();
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setExportBoatItem(item) {
    var owner = $.trim($(item).find('.boat-item-name').text());
    $('#_eDetailBoatName').val(owner);
    $('#_eDetailBoatName').attr("boatCode", $.trim($(item).find('.boat-item-code').text()));
    $(item).parent().css("display", "none");
    event.stopPropagation();
}

function getExportOrderSail(item) {
    var boat = $('#_eDetailBoatName').attr("boatCode");
    boat = $.trim(boat);
    var sail = $(item).val();
    var url = '/trans/api/queryVesselEtd?vesselCode='+boat+'&voyage='+sail;
    $.ajax({
        url:url,
        method:'GET',
        headers:{"userName": $.cookie("userName"), "token": $.cookie("token")},
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "";
                for (var i = 0; i < data.vesselEtds.length; i++) {
                    result += "<li onclick='setExportSailItem(this)'>" +
                        "<span class='boat-item'>" + data.vesselEtds[i].vesselCode + "</span>" +
                        "<span class='boat-item-name' style='width: 120px;'>" + data.vesselEtds[i].voyage + "</span></li>";
                }
                $('#exportOrderSail').html(result);
                $('#exportOrderSail').show();
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setExportSailItem(item) {
    var sail = $.trim($(item).find('.boat-item-name').text());
    var code = $.trim($(item).find('.boat-item').text());
    $('#_eDetailVoyage').val(sail);
    $('#_eDetailVoyage').attr("sailCode", code);
    $(item).parent().css("display", "none");
    event.stopPropagation();
}

// 获取批量新增的提箱点和返箱点的列表
function getExportMultiOrderDepot(item) {
    var owner = $(item).val();
    var url = '/trans/api/queryDepot?codeOrName='+owner;
    $.ajax({
        url:url,
        method:'GET',
        headers:{"userName": $.cookie("userName"), "token": $.cookie("token")},
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "";
                if (data.depots.length > 0) {
                    for (var i = 0; i < data.depots.length; i++) {
                        result += "<li onclick='setExportMultiOrderDepot(this)'><div style='width: 150%; height: 30px;'>" +
                            "<span class='owner-code' style='display: block; width: 55px; float: left;'>" + data.depots[i].depotCode + "</span>" +
                            "<span class='owner-company-name'>" + data.depots[i].cnName + "</span></div></li>";
                    }
                    $(item).siblings('.exportMultiOrderDepot').html(result);
                }

                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setExportMultiOrderDepot(item) {
    var owner = $(item).find('.owner-company-name').text();
    var code = $(item).find('.owner-code').text();
    $(item).parent().siblings('.exportDepot').val(owner);
    $(item).parent().siblings('.exportDepot').attr("containerAddressCode", code);
    $(item).parent().css("display", "none");
    event.stopPropagation();
}