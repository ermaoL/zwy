/**
 * Created by Administrator on 2016/5/27.
 */
myAdminApp.controller('userManageCtrl', ['$scope', '$http', 'UserManageService', function($scope, $http, UserManageService) {

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

    $scope.examUserInfoFail = function (id) {
        $('#fail-dialogBox').dialogBox({
            type: 'normal',  //three type:'normal'(default),'correct','error',
            width: 300,
            height: 280,
            hasMask: true,
            hasClose: true,
            hasBtn: true,
            confirmValue: "发送",
            cancelValue: "取消",
            confirm: function () {
                var userRejectReason = $('#userRejectReason').val();
                var url = "/trans/base/admin/user/" + id + "/verifier";

                var requestData = {
                    "ifPass": 0,
                    "failedReason": userRejectReason
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
                        Dialog.alert("审核不通过");
                        getAllUserInfoList();
                    }else{
                        errorAdminMsgHint(data.errorCode, data.errorMsg);
                    }
                });
            },
            effect: '',
            title: '   ',
            content: '<div style="text-align: center; margin-left: 10px;"><div style="font-size: 16px; margin-bottom: 10px; margin-top: -10px;">审核不通过</div><textarea id="userRejectReason" style="width: 240px; height: 130px; padding: 5px;" placeholder="请输入理由..." /></div>'
        });
    }

    $scope.examUserInfoSuccess = function (id) {
        $('#success-dialogBox').dialogBox({
            type: 'normal',  //three type:'normal'(default),'correct','error',
            width: 300,
            height: 480,
            hasMask: true,
            hasClose: true,
            hasBtn: true,
            confirmValue: "发送",
            cancelValue: "取消",
            confirm: function () {
                var url = "/trans/base/admin/user/" + id + "/verifier";
                var company = 1;
                var company1 = $('#xmzzCheck').is(":checked");
                var company2= $ ('#fzzzCheck').is(":checked");

                if (company1 == true) {
                    company = 1;
                } else if (company2 == true) {
                    company = 2;
                }

                var requestData = {
                    "ifPass": 1,
                    "company": company
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
                        Dialog.alert("审核通过");
                        getAllUserInfoList();
                    }else{
                        errorAdminMsgHint(data.errorCode, data.errorMsg);
                    }
                });
            },
            effect: '',
            title: '   ',
            content: '<div style="text-align: center; margin-left: 10px;">' +
            '<div style="font-size: 16px; margin-bottom: 10px; margin-top: -10px;">审核通过</div>' +
            '<div style="float: left;">请选择绑定运输公司</div>' +
            '<table class="table table-bordered" style="margin-top: 40px; width: 240px;">' +
            '<tr><td style="width: 40px; vertical-align: middle;"><input id="xmzzCheck" type="radio" name="bindCompany" checked></td>' +
            '<td>裕雄(XMZZ)</td></tr>' +
            '<tr><td style="width: 40px; vertical-align: middle;"><input id="fzzzCheck" type="radio" name="bindCompany"></td>' +
            '<td>裕国(FZZZ)</td></tr></table></div>'
        });
    }

    $scope.selectUserInfoListAll = function () {
        var temp = $('#user-list-select-all').prop('checked');
        if (temp) {
            $('input[name=userListCheck]').prop("checked", true);
        } else {
            $('input[name=userListCheck]').prop("checked", false);
        }
    }
    /*
     $scope.paginationConf = {
     currentPage: 1,
     totalItems: 8000,
     itemsPerPage: 15,
     pagesLength: 15,
     perPageOptions: [10, 20, 50, 100],
     onChange: function(){
     }
     };*/

    $scope.setCompanyName = function (id, name)
    {
        $scope.examUserId = id;
        $scope.userManageCompany = name;
    }

    $scope.passUserExam = function () {
        var id = $scope.examUserId;
        var url = "/trans/base/admin/user/" + id + "/verifier";
        var company = 1;
        var company1 = $('#xmzzCheck').is(":checked");
        var company2= $ ('#fzzzCheck').is(":checked");

        if (company1 == true) {
            company = 1;
        } else if (company2 == true) {
            company = 2;
        }

        var userCompany = $('#_userCompanyName').val();
        var userCompanyId = $('#_userCompanyName').attr("companyId");

        if (userCompany == "" || userCompany == null || userCompany == undefined) {
            Dialog.alert("用户所属公司不能为空");
            return false;
        } else if (userCompany != ""&&(userCompanyId == "" || userCompanyId == null || userCompanyId == undefined)) {
            Dialog.alert("用户所属公司信息错误");
            return false;
        }

        var requestData = {
            "ifPass": 1,
            "transCompany": company,
            "belongCompany": userCompany,
            "belongCompanyCode": userCompanyId
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
                $('#passModal').modal('hide');
                Dialog.alert("审核通过");
                getAllUserInfoList();
            }else{
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        });
    }

    /** 查询 */
    $scope.userListSearch = function () {
        getAllUserInfoList();
    }

    /** 重置 */
    $scope.userListReset = function () {
        $scope.userName = "";
        $scope.phoneNum = "";
        getAllUserInfoList();
    }

    /** 用户信息列表并分页 */
    var getAllUserInfoList = function () {

        var userManageData = {
            userName: $scope.userName,
            userTel: $scope.phoneNum,
            currentPage: $scope.paginationConf.currentPage,
            pageSize: $scope.paginationConf.itemsPerPage
        }
        UserManageService.list(userManageData).success(function (data) {
            if (data.success) {
                $scope.paginationConf.totalItems = data.pagingInfo.totalRows;
                $scope.listData = data.data;
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }

        });

    }

    //配置分页基本参数
    $scope.paginationConf = {
        currentPage: 1,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 50, 100]
    };

    /***************************************************************
     当页码和页面记录数发生变化时监控后台查询
     如果把currentPage和itemsPerPage分开监控的话则会触发两次后台事件。
     ***************************************************************/
    $scope.$watch('paginationConf.currentPage + paginationConf.itemsPerPage', getAllUserInfoList);

}]);

//业务类
myAdminApp.factory('UserManageService', ['$http', function ($http) {
    var list = function (userManageData) {
        return $http({
            url:'/trans/base/admin/user/checking/search',
            method:'GET',
            params: userManageData,
            headers: {
                "adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType")}
        });
    }

    return {
        list: function (userManageData) {
            return list(userManageData);
        }
    }
}]);

function getRegisterCompany(item) {
    var company = $(item).val();
    var url = '/trans/api/queryCrmTypeIdIsCU?codeOrName='+company;

    if ($('#_regCompanyName').val() == "") {
        $('#reg-company-hint').css("display", "block");
    } else {
        $('#reg-company-hint').css("display", "none");
    }
    $.ajax({
        url:url,
        method:'GET',
        success: function (data) {
            data = $.parseJSON(data);
            if (data.success) {
//                $scope.importOwnerList = data.crms;
                var result = "";
                for (var i = 0; i < data.crms.length; i++) {
                    result += "<li onclick='setRegisterCompany(this)'><div style='width: 150%; height: 30px;'>" +
                        "<span class='owner-code' style='display: block; width: 55px; float: left;'>" + data.crms[i].crmCode + "</span>" +
                        "<span class='owner-company-name'>" + data.crms[i].companyName + "</span></div></li>";
                }
                $('#reg-company').html(result);
                // alert("data.crms    "+data);
            } else {
                errorAdminMsgHint(data.errorCode, data.errorMsg);
            }
        }
    });
}

function setRegisterCompany(item) {
    var owner = $(item).find('.owner-company-name').text();
    var code = $.trim($(item).find('.owner-code').text());
    $('#_userCompanyName').val(owner);
    $('#_userCompanyName').attr("companyId", code);
    $(item).parent().css("display", "none");
    $('#reg-company-hint').css("display", "none");
    event.stopPropagation();
}