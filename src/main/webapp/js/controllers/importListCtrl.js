/**
 * Created by cookie on 2016/5/11.
 */
myApp.controller('importListCtrl', ['$scope', '$state', '$http', 'ImportListService', function($scope, $state, $http, ImportListService) {

    laydate({
        elem: '#importArriveTime1'
    });

    laydate({
        elem: '#importArriveTime2'
    });

    /*$('tr').dblclick(function () {
        window.location.href = "index.html#/import/detail";
        sessionStorage.setItem("menuStatus", 2);
        $('#import-detail-menu').addClass('menu-active');
        $('#import-detail-menu').find('.home-arrow').css("display", "block");
        $('#import-detail-menu').siblings().removeClass('menu-active');
        $('#import-detail-menu').siblings().find('.home-arrow').css("display", "none");

        var importOrderId = $(this).find('.importOrderId').val();
        alert(importOrderId);
    });*/

    $scope.toImportDetail = function (orderId) {
        sessionStorage.setItem("menuStatus", 2);
        $('#import-detail-menu').addClass('menu-active');
        $('#import-detail-menu').find('.home-arrow').css("display", "block");
        $('#import-detail-menu').siblings().removeClass('menu-active');
        $('#import-detail-menu').siblings().find('.home-arrow').css("display", "none");

        // var importOrderId = $(item).find('span').text();
        sessionStorage.setItem("operate", "1");
        sessionStorage.setItem("importOrderId", orderId);
        console.log("list:  "+orderId);
        // window.location.href = "index.html#/import/detail";
        $state.go("import/detail");
    }

    $scope.selectImportListAll = function () {
        var temp = $('#import-list-select-all').prop('checked');
        if (temp) {
            $('input[name=importListCheck]').prop("checked", true);
        } else {
            $('input[name=importListCheck]').prop("checked", false);
        }
    }

    $scope.addImportOrder = function () {
        sessionStorage.setItem("menuStatus", 2);
        $('#import-detail-menu').addClass('menu-active');
        $('#import-detail-menu').find('.home-arrow').css("display", "block");
        $('#import-detail-menu').siblings().removeClass('menu-active');
        $('#import-detail-menu').siblings().find('.home-arrow').css("display", "none");

        sessionStorage.setItem("operate", "0");
        sessionStorage.setItem("importOrderId", "");
        // window.location.href = "index.html#/import/detail";
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

    /** 查询 */
    $scope.importListSearch = function () {
        GetAllImportList();
    }
    
    /** 重置 */
    $scope.importListReset = function () {
        $scope.importOrderBillNo = "";
        $scope.importOrderShipName = "";
        $scope.importOrderSailing = "";
        $scope.importOrderContainerNo = "";
        $('#importArriveTime1').val("");
        $('#importArriveTime2').val("");
        GetAllImportList();
    }

    /** 获取进口订单列表并分页 */
    var GetAllImportList = function () {

        var orderArrivalDateFrom = $('#importArriveTime1').val();
        var orderArrivalDateTo = $('#importArriveTime2').val();

        var arriveDateFrom = orderArrivalDateFrom.split('-');
        var arriveDateTo = orderArrivalDateTo.split('-');
        var isCorrectDate = true;
        for (var i = 0; i < arriveDateFrom.length; i++) {
            if (arriveDateFrom[i] > arriveDateTo[i]) {
                isCorrectDate = false;
            }
        }

        if (isCorrectDate) {
            var importListData = {
                orderBillNo: $scope.importOrderBillNo,
                orderShipName: $scope.importOrderShipName,
                orderSailing: $scope.importOrderSailing,
                containerCaseNo: $scope.importOrderContainerNo,
                orderArrivalDateFrom: orderArrivalDateFrom,
                orderArrivalDateTo: orderArrivalDateTo,
                currentPage: $scope.paginationConf.currentPage,
                pageSize: $scope.paginationConf.itemsPerPage
            }
            ImportListService.list(importListData).success(function (data) {
                if (data.success) {
                    $scope.paginationConf.totalItems = data.pagingInfo.totalRows;
                    $scope.listData = data.data;
                } else {
                    errorMsgHint(data.errorCode, data.errorMsg);
                }

            });
        } else {
            alert("请正确选择抵港时间");
        }



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
    $scope.$watch('paginationConf.currentPage + paginationConf.itemsPerPage', GetAllImportList);

}]);

//业务类
myApp.factory('ImportListService', ['$http', function ($http) {
    var list = function (importListData) {
        return $http({
            url:'/trans/api/import',
            method:'GET',
            params: importListData,
            headers: {
                "userName": $.cookie("userName"),
                "token": $.cookie("token")}
        });
    }

    return {
        list: function (importListData) {
            return list(importListData);
        }
    }
}]);