/**
 * Created by Administrator on 2016/5/27.
 */

myAdminApp.controller('exportListAdminCtrl', ['$scope', '$state', '$http', 'ExportListService', function($scope, $state, $http, ExportListService) {
    /*
     var list = {"supply": [{"ladingNum":"123456", "wharf":"厦门", "boatName":"和谐号", "voyage":"123", "billStatus":"已完成", "arriveDate":"2016.05.09", "freeUseDate":"3个月", "shipAgent":"***", "totalNum":"78", "surplusNum":"67"},
     {"ladingNum":"223456", "wharf":"厦门", "boatName":"和谐号", "voyage":"123", "billStatus":"已完成", "arriveDate":"2016.05.09", "freeUseDate":"3个月", "shipAgent":"***", "totalNum":"78", "surplusNum":"67"},
     {"ladingNum":"323456", "wharf":"厦门", "boatName":"和谐号", "voyage":"123", "billStatus":"已完成", "arriveDate":"2016.05.09", "freeUseDate":"3个月", "shipAgent":"***", "totalNum":"78", "surplusNum":"67"},
     {"ladingNum":"423456", "wharf":"厦门", "boatName":"和谐号", "voyage":"123", "billStatus":"已完成", "arriveDate":"2016.05.09", "freeUseDate":"3个月", "shipAgent":"***", "totalNum":"78", "surplusNum":"67"},
     {"ladingNum":"523456", "wharf":"厦门", "boatName":"和谐号", "voyage":"123", "billStatus":"已完成", "arriveDate":"2016.05.09", "freeUseDate":"3个月", "shipAgent":"***", "totalNum":"78", "surplusNum":"67"}]};
     $scope.listData = list.supply;*/

    laydate({
        elem: '#exportArriveTime1'
    });

    laydate({
        elem: '#exportArriveTime2'
    });



    $scope.selectExportListAll = function () {
        var temp = $('#export-list-select-all').prop('checked');
        if (temp) {
            $('input[name=exportListCheck]').prop("checked", true);
        } else {
            $('input[name=exportListCheck]').prop("checked", false);
        }
    }

    $scope.toAdminExportDetail = function (orderId) {
        sessionStorage.setItem("menuStatus", 4);
        $('#export-list-menu').addClass('menu-active');
        $('#export-list-menu').find('.home-arrow').css("display", "block");
        $('#export-list-menu').siblings().removeClass('menu-active');
        $('#export-list-menu').siblings().find('.home-arrow').css("display", "none");

        // var exportOrderId = $(item).find('span').text();
        sessionStorage.setItem("eOperate", "1");
        sessionStorage.setItem("exportOrderId", orderId);
        // console.log("list:  "+orderId);
        // window.location.href = "main-admin.html#/export/detail";
        $state.go('mainAdmin.exportDetail');
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
                    errorAdminMsgHint(data.errorCode, data.errorMsg);
                }

            });
        } else {
            Dialog.alert("请正确选择开航时间");
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
myAdminApp.factory('ExportListService', ['$http', function ($http) {
    var list = function (exportListData) {
        return $http({
            url:'/trans/base/admin/export',
            method:'GET',
            params: exportListData,
            headers: {
                "adminCode": $.cookie("adminCode"),
                "token": $.cookie("token"),
                "authType": $.cookie("authType")}
        });
    }

    return {
        list: function (exportListData) {
            return list(exportListData);
        }
    }
}]);