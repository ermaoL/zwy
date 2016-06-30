/**
 * Created by Administrator on 2016/5/27.
 */

myAdminApp.controller('importDetailAdminCtrl', function($scope, $http,$timeout) {

    $scope.containerCaseType = $.parseJSON(sessionStorage.getItem("queryContainerType"));

    $scope.containerSize = $.parseJSON(sessionStorage.getItem("queryContainerSize"));

    $scope.iDetailBindCompany = $.cookie("userCompany");

    $('form').validate({
        onFocus: function() {
            this.parent().addClass('active');
            return false;
        },
        onBlur: function() {
            var $parent = this.parent();
            var _status = parseInt(this.attr('data-status'));
            $parent.removeClass('active');
            if (!_status) {
                $parent.addClass('error');
            }
            return false;
        }
    });
    var admin = $.cookie("adminCode");
    if (admin == 'admin') {
        $scope.isAdmin = false;
    } else {
        $scope.isAdmin = true;
    }
    $('#_detailLadingBillNum').focus();

    $('#laydate_box').css("display", "none");

    // 下拉框显示隐藏
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

    $('.importOrderWharf li').click(function(e){
        var val = $.trim($(this).find('.boat-item-name').text());
        var code = $.trim($(this).find('.boat-item').text());
        $(this).parents('[name="nice-select"]').find('input').val(val);
        $(this).parents('[name="nice-select"]').find('input').attr("wharfCode", code);
        $('[name="nice-select"] ul').hide();
        e.stopPropagation();
    });

    $scope.getImportAddressList = function ($event, id) {
        var item = $event.target;
        $(item).parents('tr').css("background", "#d7f1f2");
        $(item).parents('tr').siblings('tr').css("background", "#ffffff");
        // console.log("containerId:   "+ id);
        sessionStorage.setItem("importFirstContainerId", id);
        var timestamp = (new Date()).valueOf();
        var url = "/trans/base/admin/import/" + id + "/address?" + timestamp;
        $http({
            url: url,
            method:'GET',
            headers: {  "adminCode": $.cookie("adminCode"),
                        "token": $.cookie("token"),
                        "authType": $.cookie("authType")}
        }).success(function (data) {
            if (data.success) {
                $scope.detailData2 = data.data;
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        });
    }

    $('#import-back').click(function() {
        $('#import-list-menu').addClass('menu-active');
        $('#import-list-menu').find('.home-arrow').css("display", "block");
        $('#import-list-menu').siblings().removeClass('menu-active');
        $('#import-list-menu').siblings().find('.home-arrow').css("display", "none");
        sessionStorage.setItem("menuStatus", 1);
    });

    $scope.containerImportType = $.parseJSON(sessionStorage.getItem("queryContainerType"));

    $scope.containerSize = $.parseJSON(sessionStorage.getItem("queryContainerSize"));
    
    $scope.examImportOrder = function () {
        $('#exam-dialogBox').dialogBox({
            type: 'correct',  //three type:'normal'(default),'correct','error',
            width: 300,
            height: 200,
            hasMask: true,
            hasClose: true,
            hasBtn: true,
            time: 100,
            confirmValue: "通过",
            cancelValue: "不通过",
            confirm: function () {
                var orderId = sessionStorage.getItem("importOrderId");
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
                        window.location.href = "admin.html#/admin/main/import/list";
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
                            var importRejectReason = $('#importRejectReason').val();
                            var orderId = sessionStorage.getItem("importOrderId");
                            var url = "/trans/base/admin/order/" + orderId + "/verifier";
                            var requestData = {
                                "ifPass": 0,
                                "failedReason": importRejectReason
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
                                    window.location.href = "admin.html#/admin/main/import/list";
                                }else{
                                    errorAdminMsgHint(data.errorCode, data.errorMsg);
                                }
                            });
                            // alert("发送");
                        },
                        effect: '',
                        title: '   ',
                        content: '<div style="text-align: center;"><div style="font-size: 16px; margin-bottom: 10px; margin-top: -10px;">审核不通过</div><textarea id="importRejectReason" style="width: 260px; height: 200px;" placeholder="请输入理由..." /></div>'
                    });

                }, 300);
            },
            effect: '',
            title: '提示',
            content: '是否通过该订单的审核'
        });
    }
    
    $scope.endImportBoxTrans = function (id) {
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
                /*$http.post("/trans/api/import/address", addData, {
                    headers: {"userName": $.cookie("userName"), "token": $.cookie("token")}
                }).success(function(data) {
                    if(data.success){
                        $scope.detailData2 = data.data;
                        alert("地址信息保存成功");
                    }else{
                        alert(data.errorMsg);
                    }
                });*/
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

    $scope.detailData = [];
    $scope.detailData2 = [];

    var operate = sessionStorage.getItem("operate");
    var importOrderId = sessionStorage.getItem("importOrderId");

    if (operate == "1" && importOrderId != "") {
        var timestamp = (new Date()).valueOf();
        var url = "/trans/base/admin/import/" + importOrderId + "/all?" + timestamp;

        $http({
            url: url,
            method: 'GET',
            params: {"orderId": importOrderId},
            headers: {"adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType")}
        }).success(function (data) {
            var getImportDetail = data;
            var orderVo = getImportDetail.orderVo;
            $scope.importDetailOrderBillNo = orderVo.orderBillNo;
            $scope.importDetailOrderStateDec = orderVo.orderStateDesc;
            $scope.importDetailOrderState = orderVo.orderState;
            $scope.importDetailOwner = orderVo.orderOwner;
            $scope.importDetailOrderWharf = orderVo.orderWharf;
            $scope.importDetailOrderShipName = orderVo.orderShipName;
            $scope.importDetailOrderSailing = orderVo.orderSailing;
            $scope.importDetailOrderShipAgency = orderVo.orderShipAgency;
            $scope.importDetailOrderArrivalDate = orderVo.orderArrivalDate;
            $scope.importDetailOrderFreeUseDay = orderVo.orderFreeUseDay;
            $scope.importDetailOrderTotalCase = orderVo.orderTotalCase;
            $scope.importDetailOrderSurplusTank = orderVo.orderSurplusTank;
            $scope.importDetailOrderGoodOwner = orderVo.orderGoodOwner;
            $scope.importDetailRemark = orderVo.orderRemark;
            $('#_iDetailOwner').attr("ownerCode", orderVo.orderOwnerCode);
            $('#_detailGoodOwner').attr("ownerCode", orderVo.orderGoodOwnerCode);
            $('#_detailWharf').attr("wharfCode", orderVo.orderWharfCode);
            $('#_detailBoatName').attr("boatCode", orderVo.orderShipNameCode);
            $('#_detailVoyage').attr("sailCode", orderVo.orderSailingCode);
            $('#importDetailVerifierContent').text(orderVo.verifierContent);
            if (orderVo.orderConnects == "null") {
                $scope.importDetailOrderContact = "";
            } else {
                $scope.importDetailOrderContact = orderVo.orderConnects;
            }
            if (orderVo.orderConnectPhone == "null") {
                $scope.importDetailOrderContactPhone = "";
            } else {
                $scope.importDetailOrderContactPhone = orderVo.orderConnectPhone;
            }
            if (orderVo.orderConnectEmail == "null") {
                $scope.importDetailOrderContactEmail = "";
            } else {
                $scope.importDetailOrderContactEmail = orderVo.orderConnectEmail;
            }

            if (orderVo.orderCompanyDesc == "裕国(FZZZ)") {
                $scope.importDetailOrderCompany = "裕国(FZZZ)";
            } else {
                $scope.importDetailOrderCompany = "裕雄(XMZZ)";
            }

            if (orderVo.orderSystemNo == "") {
                $scope.importDetailOrderSystemNo = "";
            } else {
                $scope.importDetailOrderSystemNo = orderVo.orderSystemNo;
            }

            // 把第一个箱子Id存进session以便地址加载
            sessionStorage.setItem("importFirstContainerId", getImportDetail.firstContainerId);

            // console.log(getImportDetail.firstContainerId);

            $scope.detailData = getImportDetail.containerVoList;
            $scope.detailData2 = getImportDetail.addressVoList;

            if (orderVo.orderState != 2) {
                $('.list-input').attr("readonly", "readonly");
                $('#_detailRemark').attr("readonly", "readonly");
                $('.red-btn').attr("disabled", "disabled");
                $('.blue-btn').attr("disabled", "disabled");
                $('.second-table-top-operate').attr("disabled", "disabled");
            } else {
                $('.list-input').removeAttr("readonly");
                $('#_detailRemark').removeAttr("readonly");
                $('.red-btn').removeAttr("disabled");
                $('.blue-btn').removeAttr("disabled");
                $('.second-table-top-operate').removeAttr("disabled");
            }

        });

        /* $.ajax({
         type: "GET",
         url: url,
         params: {orderId: importOrderId},
         success: function (data) {
         sessionStorage.setItem("importDetail", data);
         }
         });*/
    } else if (operate == "0") {
        $scope.importDetailOrderBillNo = "";
        $scope.importDetailOrderState = "新增";
        $scope.importDetailOrderWharf = "";
        $scope.importDetailOrderShipName = "";
        $scope.importDetailOrderSailing = "";
        $scope.importDetailOrderShipAgency = "";
        $scope.importDetailOrderArrivalDate = "";
        $scope.importDetailOrderFreeUseDay = "";
        $scope.importDetailOrderTotalCase = "";
        $scope.importDetailOrderSurplusTank = "";
        $scope.importDetailOrderCompany = "1";
        $scope.importDetailOrderSystemNo = "";
        
    }

    $scope.saveImportOrder = function () {
        $('form').validate('submitValidate'); //return boolean;
        var errorHint = "";
        var billNo = $scope.importDetailOrderBillNo;
        var totalCaseNum = $scope.importDetailOrderTotalCase;
        var contact = $scope.importDetailOrderContact;
        var contactPhone = $scope.importDetailOrderContactPhone;
        var contactEmail = $scope.importDetailOrderContactEmail;
        var orderGood = $('#_detailGoodOwner').val();
        var orderGoodCode = $('#_detailGoodOwner').attr("ownerCode");
        var orderOwner = $('#_iDetailOwner').val();
        var orderOwnerCode = $('#_iDetailOwner').attr("ownerCode");
        var orderWharf = $('#_detailWharf').val();
        var orderWharfCode = $('#_detailWharf').attr("wharfCode");
        var orderShipName = $('#_detailBoatName').val();
        var orderShipNameCode = $('#_detailBoatName').attr("boatCode");
        var orderSailing = $('#_detailVoyage').val();
        var orderSailingCode = $('#_detailVoyage').attr("sailCode");
        if (billNo == "" || billNo == null) {
            errorHint += "提单号不能为空！";
        } else if (totalCaseNum === "" || totalCaseNum === null) {
            errorHint += "总箱量不能为空！";
        } else if (contact === "" || contact === null) {
            errorHint += "联系人不能为空！";
        } else if (contactPhone === "" || contactPhone === null) {
            errorHint += "联系电话不能为空！";
        } else if (contactEmail === "" || contactEmail === null) {
            errorHint += "联系邮箱不能为空！";
        }
        if (orderOwner != "" && (orderOwnerCode == ""||orderOwnerCode == undefined)) {
            errorHint += "  箱主信息错误！";
        }
        if (orderWharf != "" && (orderWharfCode == ""||orderWharfCode == undefined)) {
            errorHint += "  码头信息错误！";
        }
        if (orderShipName != "" && (orderShipNameCode == ""||orderShipNameCode == undefined)) {
            errorHint += "  船名信息错误！";
        }
        if (orderSailing != "" && (orderSailingCode == ""||orderSailingCode == undefined)) {
            errorHint += "  航次信息错误！";
        }
        if (orderGood != "" && (orderGoodCode == ""||orderGoodCode == undefined)) {
            errorHint += "  货主信息错误！";
        }
        if (errorHint != "") {
            $('#importDetailErrorHint').text(errorHint);
            return false;
        } else {
            $('#importDetailErrorHint').text("");
        }
        var datas;
        var orderId = sessionStorage.getItem("importOrderId");
        var company = $("#importOrderCompany").val();
        if (company == "裕雄(XMZZ)") {
            company = 1;
        } else if (company == "裕国(FZZZ)") {
            company = 2;
        }
        if (orderId == "") {
            datas = {
                "orderBillNo": $scope.importDetailOrderBillNo,
                "orderCompany": company,
                "orderOwner": $('#_iDetailOwner').val(),
                "orderOwnerCode": $('#_iDetailOwner').attr("ownerCode"),
                "orderGoodOwner": $('#_detailGoodOwner').val(),
                "orderGoodOwnerCode": $('#_detailGoodOwner').attr("ownerCode"),
                "orderWharf": $('#_detailWharf').val(),
                "orderWharfCode": $('#_detailWharf').attr("wharfCode"),
                "orderShipName": $('#_detailBoatName').val(),
                "orderShipNameCode": $('#_detailBoatName').attr("boatCode"),
                "orderSailing": $('#_detailVoyage').val(),
                "orderSailingCode": $('#_detailVoyage').attr("sailCode"),
                "orderShipAgency": $('#_detailBoatAgent').val(),
                "orderArrivalDate": $('#_detailArriveTime').val(),
                "orderFreeUseDay": $scope.importDetailOrderFreeUseDay,
                "orderTotalCase": $scope.importDetailOrderTotalCase,
                "orderSurplusTank": $scope.importDetailOrderSurplusTank,
                "orderConnects": $scope.importDetailOrderContact,
                "orderConnectPhone": $scope.importDetailOrderContactPhone,
                "orderConnectEmail": $scope.importDetailOrderContactEmail,
                "orderRemark": $scope.importDetailRemark};
        } else {
            datas = {
                "orderId": orderId,
                "orderBillNo": $scope.importDetailOrderBillNo,
                "orderCompany": company,
                "orderOwner": $('#_iDetailOwner').val(),
                "orderOwnerCode": $('#_iDetailOwner').attr("ownerCode"),
                "orderWharf": $('#_detailWharf').val(),
                "orderWharfCode": $('#_detailWharf').attr("wharfCode"),
                "orderShipName": $('#_detailBoatName').val(),
                "orderShipNameCode": $('#_detailBoatName').attr("boatCode"),
                "orderSailing": $('#_detailVoyage').val(),
                "orderSailingCode": $('#_detailVoyage').attr("sailCode"),
                "orderShipAgency": $scope.importDetailOrderShipAgency,
                "orderArrivalDate": $('#_detailArriveTime').val(),
                "orderFreeUseDay": $scope.importDetailOrderFreeUseDay,
                "orderTotalCase": $scope.importDetailOrderTotalCase,
                "orderSurplusTank": $scope.importDetailOrderSurplusTank,
                "orderGoodOwner": $('#_detailGoodOwner').val(),
                "orderGoodOwnerCode": $('#_detailGoodOwner').attr("ownerCode"),
                "orderConnects": $scope.importDetailOrderContact,
                "orderConnectPhone": $scope.importDetailOrderContactPhone,
                "orderConnectEmail": $scope.importDetailOrderContactEmail,
                "orderRemark": $scope.importDetailRemark};
        }
        var transform = function (data) {
            return $.param(data);
        }

        // 保存订单
        $http.post("/trans/base/admin/import", datas, {
            headers: {
                "adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType"),
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
            transformRequest: transform
        }).success(function(data) {
            if (data.success) {
                Dialog.alert("单据主信息保存成功");
                sessionStorage.setItem("importOrderId", data.orderId);
                $scope.importDetailOrderSystemNo = data.orderSystemNo;
            }else{
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        });
        /*$.ajax({
         type: "POST",
         url: "/trans/api/import",
         data: datas,
         success: function(data) {
         var status = $.parseJSON(data);
         if (status.success) {
         alert("单据主信息保存成功");
         }else{
         console.log(status.errorMsg);
         }
         }
         });*/
    }

    // 保存箱子
    $scope.saveImportContainer = function () {
        var data = [];
        $("#importContainerTable tr:not(:first)").each(function(i) {
            $(this).children("td").each(function(j) {
                if(!data[i]){
                    data[i] = new Array();
                }
                /*data[i][j]= $(this).children().val();
                 if (j == 3) {
                 data[i][17] = $(this).children().attr("ownerCode");
                 }*/
                data[i][j]= $(this).children().val();
                if (j == 4) {
                    data[i][4] = $('#_iDetailOwner').val();
                    data[i][20] = $('#_iDetailOwner').attr("ownerCode");
                }

                if (j == 8) {
                    var val1 = $(this).children().find('option:selected').text();
                    if (val1.indexOf('?') > -1) {
                        data[i][8] = "";
                    } else {
                        data[i][8] = val1;
                    }
                }
                if (j == 9) {
                    var val = $(this).children().find('option:selected').text();
                    if (val.indexOf('?') > -1) {
                        data[i][9] = "";
                    } else {
                        data[i][9] = val;
                    }

                }
                /*if (j == 8) {
                 var val = parseInt($(this).children().val());
                 var typeCode = $scope.containerCaseType[val].typeCode;
                 data[i][8] = typeCode;
                 }*/
                /*if (j == 13) {
                 if ($(this).children().val() == null || $(this).children().val() == "" || $(this).children().val() == undefined || $(this).children().val() == "? string:null ?") {
                 data[i][13]= "0";
                 } else {
                 data[i][13]= $(this).children().val();
                 }
                 }*/
                if (j == 10) {
                    var draDepot = $.trim($(this).children().attr("containerAddressCode"));
                    data[i][21] = draDepot;
                }
                if (j == 11) {
                    var reDepot = $.trim($(this).children().attr("containerAddressCode"));
                    data[i][22] = reDepot;
                }
                if (j == 14) {
                    var isChecked = $(this).children().prop('checked');
                    if (isChecked) {
                        data[i][14] = "是";
                    } else {
                        data[i][14] = "否";
                    }
                }
                if (j == 17) {
                    var isChecked = $(this).children().prop('checked');
                    if (isChecked) {
                        data[i][17] = "是";
                    } else {
                        data[i][17] = "否";
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
                 "containerCount": data[i][5],
                 "containerSealNo": data[i][6],
                 "containerTradeName": data[i][7],
                 "containerType": data[i][8],
                 "containerCartonSize": data[i][9],
                 "containerGrossWeight": data[i][10],
                 "containerDynamo": data[i][11],
                 "containerRfTemp": data[i][12],
                 "containerTemperUnit": data[i][13],
                 "containerIsMoreUnload": data[i][14],
                 "containerCost": data[i][15],
                 "containerOwnerCode": data[i][17]*/
                "containerId": data[i][2],
                "containerOwner": data[i][4],// 没有
                "containerCaseNo": data[i][5],
                "containerCount": "1",// 没有
                "containerSealNo": data[i][7],
                "containerType": data[i][8],
                "containerCartonSize": data[i][9],
                "containerDrayageDepot": data[i][10],
                "containerReturnDepot": data[i][11],
                "containerTradeName": data[i][12],
                "containerGrossWeight": data[i][13],
                "containerDynamo": data[i][14],
                "containerRfTemp": "",// 没有
                "containerTemperUnit": "1",// 没有
                "containerIsMoreUnload": data[i][17],
                "containerCost": "1",// 没有
                "containerOwnerCode": data[i][20],// 没有
                "containerDrayageDepotCode": data[i][21],
                "containerReturnDepotCode": data[i][22]
            };
            alterContainer.push(containerInfo);
        }

        var addData = {
            "containerOrderId": sessionStorage.getItem("importOrderId"),
            "containerVoList": alterContainer
        };

        var requestData = JSON.stringify(addData);

        $http.post("/trans/base/admin/import/container", requestData, {
            headers: {"adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType")}
        }).success(function(data) {
            if(data.success){
                $scope.detailData = data.data;
                if (data.data.length > 0) {
                    sessionStorage.setItem("importFirstContainerId", data.data[0].containerId);
                }
                $scope.importDetailOrderTotalCase = data.data.length;
                Dialog.alert("箱信息保存成功");
            }else{
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        });

        /*$.ajax({
         type: "POST",
         url: "/trans/api/import/container",
         contentType:'application/json; charset=UTF-8',
         dataType:'json',
         data: addData,
         success: function(data1) {
         if (data1.success) {
         alert("箱信息保存成功");
         }else{
         console.log(data1.errorMsg);
         }
         }
         });*/

    }

    // 保存地址
    $scope.saveImportAddress = function () {
        var data = [];
        $("#importAddressTable tr:not(:first)").each(function(i) {
            $(this).children("td").each(function(j) {
                if(!data[i]){
                    data[i] = new Array();
                }
                data[i][j]= $(this).children().val();
                if (j == 7) {
                    var addr1 = $.trim($(this).children().val());
                    if (addr1.indexOf('?') > -1) {
                        data[i][7] = "";
                    }
                } else if (j == 8) {
                    var addr2 = $.trim($(this).children().val());
                    if (addr2.indexOf('?') > -1) {
                        data[i][8] = "";
                    }
                } else if (j == 9) {
                    var addr3 = $.trim($(this).children().find('option:selected').text());
                    if (addr3.indexOf('?')  > -1) {
                        data[i][9] = "";
                    } else {
                        data[i][9] = addr3;
                    }
                }
            });
        });

        var alterAddress = new Array();
        for (var i = 0; i < data.length; i++) {
            var addressInfo = {
                "addressId": data[i][2],
                "addressPlace": data[i][3],
                "addressDeliveryTime": data[i][4],
                "addressContacts": data[i][5],
                "addressTel": data[i][6],
                "addressProvince": data[i][7],
                "addressCity": data[i][8],
                "addressDistrict": data[i][9]
            };
            alterAddress.push(addressInfo);
        }

        var addData = {
            "containerId": sessionStorage.getItem("importFirstContainerId"),
            "addressVoList": alterAddress
        };

        addData = JSON.stringify(addData);

        $http.post("/trans/base/admin/import/address", addData, {
            headers: {"adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType")}
        }).success(function(data) {
            if(data.success){
                $scope.detailData2 = data.data;
                Dialog.alert("地址信息保存成功");
            }else{
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        });

        /*$.ajax({
         type: "POST",
         url: "/trans/api/import/address",
         contentType:'application/json; charset=UTF-8',
         dataType:'json',
         data: addData,
         success: function(data1) {
         if (data1.success) {
         alert("地址信息保存成功");
         }else{
         console.log(data1.errorMsg);
         }
         }
         });*/
    }

    /******************** 进口订单维护表格操作 ************************/
    // 新增箱子
    $scope.addImportBox = function () {
        $scope.inserted = {
            "index":"",
            "containerId":"",
            "containerOwner":"",
            "containerDrayageDepot":"",
            "containerReturnDepot":"",
            "containerCaseNo":"",
            "containerCount":"",
            "containerSealNo":"",
            "containerTradeName":"",
            "containerType":"",
            "containerCartonSize":"",
            "containerGrossWeight":"",
            "containerDynamo":"否",
            "containerRfTemp":"",
            "containerTemperUnit": 0,
            "containerIsMoreUnload":"否",
            "containerCost":"",
            "containerStateDesc":"已下单"
        };
        $scope.detailData.push($scope.inserted);
    }

    // 新增某个箱子信息
    $scope.addImportBoxInfo = function () {
        $scope.inserted = {
            "index": "",
            "addressId":"",
            "addressPlace":"",
            "addressDeliveryTime":"",
            "addressContacts":"",
            "addressTel":"",
            "addressProvince":"",
            "addressCity":"",
            "addressDistrict":""
        };
        $scope.detailData2.push($scope.inserted);
    }

    // 初始化批量新增
    $scope.importMultiContainerType = "";
    $scope.importMultiContainerSize = "";
    $scope.importMultiContainerIsMoreUnload = "否";
    $scope.importMultiContainerDynamo = "否";
    $scope.importMultiContainerTemperUnit = "0";

    // 批量新增箱子
    $scope.addImportBoxes = function () {
        var num = $scope.importMultiAdd.numbers;
        for (var i = 0; i < num; i++) {
            $scope.inserted = {
                "index": "",
                "containerId": "",
                "containerOwner": "",
                "containerOwnerCode": "",
                "containerCaseNo": $scope.importMultiContainerCaseNo,
                "containerCount": "",
                "containerSealNo": $scope.importMultiContainerSealNo,
                "containerDrayageDepot": $('#_iBoxDrayages').val(),
                "containerReturnDepot": $('#_iBoxReturns').val(),
                "containerDrayageDepotCode": $('#_iBoxDrayages').attr("containerAddressCode"),
                "containerReturnDepotCode": $('#_iBoxReturns').attr("containerAddressCode"),
                "containerTradeName": $scope.importMultiContainerTradeName,
                "containerType": $scope.importMultiContainerType,
                "containerCartonSize": $scope.importMultiContainerSize,
                "containerGrossWeight": $scope.importMultiContainerGrossWeight,
                "containerDynamo": $scope.importMultiContainerDynamo,
                "containerRfTemp": "",
                "containerTemperUnit": "",
                "containerIsMoreUnload": $scope.importMultiContainerIsMoreUnload,
                "containerCost": "",
                "containerStateDesc":"已下单"
            };
            $scope.detailData.push($scope.inserted);
        }

    }

    $scope.selectAll1 = function () {
        var temp = $('#import-select-all1').prop('checked');
        if (temp) {
            $('.checkbox-first').prop("checked", true);
        } else {
            $('.checkbox-first').prop("checked", false);
        }
    }

    $scope.selectAll2 = function () {
        var temp = $('#import-select-all2').prop('checked');
        if (temp) {
            $('.checkbox-second').prop("checked",true);
        } else {
            $('.checkbox-second').prop("checked",false);
        }
    }

    $scope.selectAll3 = function () {
        var temp = $('#import-select-all3').prop('checked');
        if (temp) {
            $('.checkbox-first').prop("checked", true);
        } else {
            $('.checkbox-first').prop("checked", false);
        }
    }

    $scope.selectAll4 = function () {
        var temp = $('#import-select-all4').prop('checked');
        if (temp) {
            $('.checkbox-second').prop("checked",true);
        } else {
            $('.checkbox-second').prop("checked",false);
        }
    }

    $scope.delImportBoxes = function () {
        var box = $("input[name=box]:checked");
        if(box.size()==0){
            Dialog.alert("要删除指定行，需选中要删除的行");
            return;
        }
        var containerIdArr = new Array();
        box.each(function(){
            var delId = $(this).parent().parent().find('.importContainerId').val();
            if (delId == "") {
                var index = $(this).parent().parent()[0].rowIndex;
                $scope.detailData.splice(index - 1, 1);
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
            $http.post("/trans/base/admin/import/container/drop", delInfo, {
                headers: {"adminCode": $.cookie("adminCode"),
                    "token": $.cookie("token"),
                    "authType": $.cookie("authType")}
            }).success(function(data) {
                if(data.success){
                    $scope.detailData = data.containerVoList;
                    $scope.importDetailOrderTotalCase = data.containerVoList.length;
                    sessionStorage.setItem("importFirstContainerId", data.firstContainerId);
                    $scope.detailData2 = data.addressVoList;
                    Dialog.alert("删除成功");
                }else{
                    errorAdminMsgHint(data.errorCode, data.errorMsg);
                }
            });
        }
        $('#import-select-all1').prop('checked', false);
    }

    $scope.delImportBoxInfo = function () {
        var boxInfo = $("input[name=boxInfo]:checked");
        if(boxInfo.size()==0){
            Dialog.alert("要删除指定行，需选中要删除的行");
            return;
        }
        var addressIdArr = new Array();
        boxInfo.each(function(){
            var delId = $(this).parent().parent().find('.importAddressId').val();
            if (delId == "") {
                var index = $(this).parent().parent()[0].rowIndex;
                $scope.detailData2.splice(index - 1, 1);
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
            $http.post("/trans/base/admin/import/address/drop", delInfo, {
                headers: {"adminCode": $.cookie("adminCode"),
                    "token": $.cookie("token"),
                    "authType": $.cookie("authType")}
            }).success(function(data) {
                if(data.success){
                    $scope.detailData2 = data.data;
                    Dialog.alert("删除成功");
                }else{
                    errorAdminMsgHint(data.errorCode, data.errorMsg);
                }
            });
        }
        $('#import-select-all2').prop('checked', false);
    }

});

function getAdminImportOrderOwner(item) {
    var owner = $(item).val();
    owner = encodeURI(owner);
    var url = '/trans/api/queryCrmTypeIdIsCW?codeOrName='+owner;
    $.ajax({
        url:url,
        method:'GET',
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "";
                for (var i = 0; i < data.crms.length; i++) {
                    result += "<li onclick='setAdminImportOrderOwner(this)'><div style='width: 150%; height: 30px;'>" +
                        "<span class='owner-code' style='display: block; width: 55px; float: left;'>" + data.crms[i].crmCode + "</span>" +
                        "<span class='owner-company-name'>" + data.crms[i].companyName + "</span></div></li>";
                }
                $(item).siblings('.importOrderOwner').html(result);
                $(item).siblings('.importOrderOwner').show();
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setAdminImportOrderOwner(item) {
    var owner = $(item).find('.owner-company-name').text();
    var code = $.trim($(item).find('.owner-code').text());
    $(item).parent().siblings('.exportOwnerInfo').val(owner);
    $(item).parent().siblings('.exportOwnerInfo').attr("ownerCode", code);
    $(item).parent().css("display", "none");
    bubbling();
}

// 获取箱列表的提箱点和返箱点
function getImportContainerAddress(item) {
    var owner = $(item).val();
    owner = encodeURI(owner);
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
                        result += "<li class='owner-item' onclick='setImportContainerAddress(this)'>" +
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

function setImportContainerAddress(item) {
    var owner = $(item).find('.owner-company-name').text();
    var code = $.trim($(item).find('.owner-code').text());
    $(item).parent().parent().siblings('.importOwnerInfo').val(owner);
    $(item).parent().parent().siblings('.importOwnerInfo').attr("containerAddressCode", code);
    $(item).parent().parent().siblings('.table-div').val(owner);
    $(item).parent().parent().css("display", "none");
}
// 获取订单的码头下拉列表
function getImportOrderWharf(item) {
    var wharf = $(item).val();
    wharf = encodeURI(wharf);
    var url = '/trans/api/queryDepot?codeOrName='+wharf;
    $.ajax({
        url:url,
        method:'GET',
        headers:{"userName": $.cookie("userName"), "token": $.cookie("token")},
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "";
                for (var i = 0; i < data.depots.length; i++) {
                    result += "<li onclick='setAdminImportWharfItem(this)'><div style='width: 150%; height: 30px;'>" +
                        "<span class='boat-item'>" + data.depots[i].depotCode + "</span>" +
                        "<span class='boat-item-name' style='width: 140px;'>" + data.depots[i].cnName + "</span></div></li>";
                }
                $('#importOrderWharf').html(result);
                $('#importOrderWharf').show();
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setAdminImportWharfItem(item) {
    var enName = $(item).find('.boat-item-name').text();
    $('#_detailWharf').val(enName);
    $('#_detailWharf').attr("wharfCode", $(item).find('.boat-item').text());
    // console.log($(item).find('.boat-item').text());
    $(item).parent().css("display", "none");
    bubbling();
}

function getImportOrderBoat(item) {
    var boat = $(item).val();
    boat = encodeURI(boat);
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
                    result += "<li onclick='setAdminImportBoatItem(this)'><div style='width: 150%; height: 30px;'>" +
                        "<span class='boat-item boat-item-code'>" + data.vessels[i].vesselCode + "</span>" +
                        "<span class='boat-item-name' style='width: 160px;'>" + data.vessels[i].enName + "</span>" +
                        "<span class='boat-item' style='left: 195px; top: -60px;'>" + data.vessels[i].cnName + "</span></div></li>";
                }
                $('#importOrderBoat').html(result);
                $('#importOrderBoat').show();
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setAdminImportBoatItem(item) {
    var boat = $(item).find('.boat-item-name').text();
    boat = $.trim(boat);
    $('#_detailBoatName').val(boat);
    $('#_detailBoatName').attr("boatCode", $(item).find('.boat-item-code').text());
    $(item).parent().css("display", "none");
    bubbling();
}

function getImportOrderSail(item) {
    var boat = $('#_detailBoatName').attr("boatCode");
    boat = $.trim(boat);
    boat = encodeURI(boat);
    var sail = $(item).val();
    sail = encodeURI(sail);
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
                    result += "<li onclick='setAdminImportSailItem(this)'>" +
                        "<span class='boat-item'>" + data.vesselEtds[i].vesselCode + "</span>" +
                        "<span class='boat-item-name' style='width: 120px;'>" + data.vesselEtds[i].voyage + "</span></li>";
                }
                $('#importOrderSail').html(result);
                $('#importOrderSail').show();
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setAdminImportSailItem(item) {
    var sail = $.trim($(item).find('.boat-item-name').text());
    var code = $.trim($(item).find('.boat-item').text());
    $('#_detailVoyage').val(sail);
    $('#_detailVoyage').attr("sailCode", code);
    $(item).parent().css("display", "none");
    bubbling();
}

// 获取批量新增的提箱点和返箱点的列表
function getImportMultiOrderDepot(item) {
    var owner = $(item).val();
    owner = encodeURI(owner);
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
                        result += "<li onclick='setImportMultiOrderDepot(this)'><div style='width: 150%; height: 30px;'>" +
                            "<span class='owner-code' style='display: block; width: 55px; float: left;'>" + data.depots[i].depotCode + "</span>" +
                            "<span class='owner-company-name'>" + data.depots[i].cnName + "</span></div></li>";
                    }
                    $(item).siblings('.importMultiOrderDepot').html(result);
                }

                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setImportMultiOrderDepot(item) {
    var owner = $(item).find('.owner-company-name').text();
    var code = $(item).find('.owner-code').text();
    $(item).parent().siblings('.importDepot').val(owner);
    $(item).parent().siblings('.importDepot').attr("containerAddressCode", code);
    $(item).parent().css("display", "none");
    bubbling();
}

