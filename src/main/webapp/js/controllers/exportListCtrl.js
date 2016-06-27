/**
 * Created by cookie on 2016/5/11.
 */

myApp.controller('exportListCtrl', ['$scope', '$state', '$http', 'ExportListService', function($scope, $state, $http, ExportListService) {

    /*laydate({
        elem: '#exportArriveTime1'
    });

    laydate({
        elem: '#exportArriveTime2'
    });*/
    
    $('#laydate_box').css("display", "none");

    $scope.selectExportListAll = function () {
        var temp = $('#export-list-select-all').prop('checked');
        if (temp) {
            $('input[name=exportListCheck]').prop("checked", true);
        } else {
            $('input[name=exportListCheck]').prop("checked", false);
        }
    }

    $scope.addExportOrder = function () {
        sessionStorage.setItem("menuStatus", 4);
        $('#export-detail-menu').addClass('menu-active');
        $('#export-detail-menu').find('.home-arrow').css("display", "block");
        $('#export-detail-menu').siblings().removeClass('menu-active');
        $('#export-detail-menu').siblings().find('.home-arrow').css("display", "none");

        sessionStorage.setItem("eOperate", "0");
        sessionStorage.setItem("exportOrderId", "");
        // window.location.href = "index.html#/export/detail";
        $state.go('index.exportDetail');
    }

   /* $scope.toEDetail = function () {
        window.location.href = "index.html#/export/detail";
        sessionStorage.setItem("menuStatus", 4);
        $('#export-detail-menu').addClass('menu-active');
        $('#export-detail-menu').find('.home-arrow').css("display", "block");
        $('#export-detail-menu').siblings().removeClass('menu-active');
        $('#export-detail-menu').siblings().find('.home-arrow').css("display", "none");
    };*/

    $scope.toExportDetail = function (orderId) {
        sessionStorage.setItem("menuStatus", 4);
        $('#export-detail-menu').addClass('menu-active');
        $('#export-detail-menu').find('.home-arrow').css("display", "block");
        $('#export-detail-menu').siblings().removeClass('menu-active');
        $('#export-detail-menu').siblings().find('.home-arrow').css("display", "none");

        // var exportOrderId = $(item).find('span').text();
        sessionStorage.setItem("eOperate", "1");
        sessionStorage.setItem("exportOrderId", orderId);
        // console.log("list:  "+orderId);
        // window.location.href = "index.html#/export/detail";
        $state.go('index.exportDetail');
    }



    /*$scope.paginationConf = {
        currentPage: 1,
        totalItems: 8000,
        itemsPerPage: 15,
        pagesLength: 15,
        perPageOptions: [10, 20, 50, 100],
        onChange: function(){
        }
    };*/
    /** 查询 */
    $scope.exportListSearch = function () {
        GetAllExportList();
    }

    /** 重置 */
    $scope.exportListReset = function () {
        $scope.exportOrderBillNo = "";
        $scope.exportOrderShipName = "";
        $scope.exportOrderSailing = "";
        $scope.exportOrderContainerNo = "";
        $('#exportArriveTime1').val("");
        $('#exportArriveTime2').val("");
        GetAllExportList();
    }

    /** 获取出口订单列表并分页 */
    var GetAllExportList = function () {

        var orderArrivalDateFrom = $('#exportArriveTime1').val();
        var orderArrivalDateTo = $('#exportArriveTime2').val();

        var arriveDateFrom = orderArrivalDateFrom.split('-');
        var arriveDateTo = orderArrivalDateTo.split('-');
        var isCorrectDate = true;
        for (var i = 0; i < arriveDateFrom.length; i++) {
            if (arriveDateFrom[i] > arriveDateTo[i]) {
                isCorrectDate = false;
            }
        }

        if (isCorrectDate) {
            var exportListData = {
                orderBillNo: $scope.exportOrderBillNo,
                orderShipName: $scope.exportOrderShipName,
                orderSailing: $scope.exportOrderSailing,
                containerCaseNo: $scope.exportOrderContainerNo,
                orderSailingDateFrom: orderArrivalDateFrom,
                orderSailingDateTo: orderArrivalDateTo,
                currentPage: $scope.paginationConf.currentPage,
                pageSize: $scope.paginationConf.itemsPerPage
            }
            ExportListService.list(exportListData).success(function (data) {
                if (data.success) {
                    $scope.paginationConf.totalItems = data.pagingInfo.totalRows;
                    $scope.listData = data.data;
                } else {
                    errorMsgHint(data.errorCode, data.errorMsg);
                }

            });
        } else {
            alert("请正确选择开航时间");
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
    $scope.$watch('paginationConf.currentPage + paginationConf.itemsPerPage', GetAllExportList);

}]);

//业务类
myApp.factory('ExportListService', ['$http', function ($http) {
    var list = function (exportListData) {
        return $http({
            url:'/trans/api/export',
            method:'GET',
            params: exportListData,
            headers: {
                "userName": $.cookie("userName"),
                "token": $.cookie("token")}
        });
    }

    return {
        list: function (exportListData) {
            return list(exportListData);
        }
    }
}]);