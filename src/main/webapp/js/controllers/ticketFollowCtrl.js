/**
 * Created by Administrator on 2016/5/26.
 */
myApp.controller('ticketFollowCtrl',['$scope', '$http', 'TicketFollowService', function ($scope, $http, TicketFollowService) {
    var width = $('.follow-info-container').width();
    $('.follow-msg-container').css("width", width-400);

    $(window).resize(function() {
        var width = $('.follow-info-container').width();
        $('.follow-msg-container').css("width", width-400);
    });

    var marker, lineArr = [];
    var map = new AMap.Map("ticket-follow-info", {
        resizeEnable: true,
        center: [116.397428, 39.90923],
        zoom: 17
    });
    map.on("complete", completeEventHandler);
    AMap.event.addDomListener(document.getElementById('start'), 'click', function() {
        marker.moveAlong(lineArr, 500);
    }, false);
    AMap.event.addDomListener(document.getElementById('stop'), 'click', function() {
        marker.stopMove();
    }, false);

    // 地图图块加载完毕后执行函数
    function completeEventHandler() {
        marker = new AMap.Marker({
            map: map,
            position: [118.0894534342, 24.4795595205],
            icon: "http://webapi.amap.com/images/car.png",
            offset: new AMap.Pixel(-26, -13),
            autoRotation: true
        });
        var lngX = 118.0894534342, latY = 24.4795595205;
        lineArr.push([lngX, latY]);
        var lngX1 = 118.1285341528, latY1 = 24.4888381022;
        lineArr.push([lngX1, latY1]);
        var lngX2 = 118.1140638886, latY2 = 24.5208065057;
        lineArr.push([lngX2, latY2]);
        var lngX3 = 118.1019603587, latY3 = 24.5292338776;
        lineArr.push([lngX3, latY3]);
        var lngX4 = 118.0944717371, latY4 = 24.5280179992;
        lineArr.push([lngX4, latY4]);
        /*for (var i = 1; i < 3; i++) {
         lngX = lngX + Math.random() * 0.05;
         if (i % 2) {
         latY = latY + Math.random() * 0.0001;
         } else {
         latY = latY + Math.random() * 0.06;
         }
         lineArr.push([lngX, latY]);
         }*/
        // 绘制轨迹
        var polyline = new AMap.Polyline({
            map: map,
            path: lineArr,
            strokeColor: "#00A",  //线颜色
            strokeOpacity: 1,     //线透明度
            strokeWeight: 3,      //线宽
            strokeStyle: "solid"  //线样式
        });
        map.setFitView();
    }

    /** 查询 */
    $scope.billSearch = function () {
        getAllTicketStatus();
    }

    /** 重置 */
    $scope.billReset = function () {
        $scope.importOrderBillNo = "";
        $scope.importOrderShipName = "";
        $scope.importOrderSailing = "";
        $('#billSailingTime1').val("");
        $('#billSailingTime2').val("");
        getAllTicketStatus();
    }
    
    $scope.ticketFollow = function () {
        window.location.href = "index.html#/single/ticket/follow";
    }

    /** 获取整票跟踪列表 */
    function getAllTicketStatus() {
        var ticketFollowData = {
            orderBillNo: $scope.importOrderBillNo,
            orderShipName: $scope.importOrderShipName,
            orderSailing: $scope.importOrderSailing,
            currentPage: $scope.paginationConf.currentPage,
            pageSize: $scope.paginationConf.itemsPerPage
        }
        TicketFollowService.list(ticketFollowData).success(function (data) {
            if (data.success) {
                $scope.paginationConf.totalItems = data.pagingInfo.totalRows;
                $scope.listData = data.data;
            } else {
                errorMsgHint(data.errorCode, data.errorMsg);
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
    $scope.$watch('paginationConf.currentPage + paginationConf.itemsPerPage', getAllTicketStatus);

}]);

//业务类
myApp.factory('TicketFollowService', ['$http', function ($http) {
    var list = function (ticketFollowData) {
        return $http({
            url:'/trans/api/import',
            method:'GET',
            params: ticketFollowData,
            headers: {
                "userName": $.cookie("userName"),
                "token": $.cookie("token")}
        });
    }

    return {
        list: function (ticketFollowData) {
            return list(ticketFollowData);
        }
    }
}]);