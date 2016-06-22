/**
 * Created by Administrator on 2016/5/26.
 */
myApp.controller('ticketFollowCtrl',['$scope', '$state', '$http', 'TicketFollowService', function ($scope, $state, $http, TicketFollowService) {
    var width = $('.follow-info-container').width();
    $('.follow-msg-container').css("width", width-360);

    $(window).resize(function() {
        var width = $('.follow-info-container').width();
        $('.follow-msg-container').css("width", width-360);
    });
    
    $scope.conStatus = true;

    var where = sessionStorage.getItem("isImOrEx");
    if (where == "1") {
        $scope.isImportOrExport = true;
    } else {
        $scope.isImportOrExport = false;
    }

    $scope.listData = [{"index": "1", "type": "GP40", "containerNo": "APZU3674567", "sealNo": "AH60119916", "status": "2"},
        {"index": "2", "type": "HQ53", "containerNo": "APZU3822690", "sealNo": "AH60119917", "status": "3"}];

    $scope.backList = function () {
        if (where == "1") {
            $state.go('import/list');
            changeMenuType('#import-list-menu');
            sessionStorage.setItem("menuStatus", 1);
        } else {
            $state.go('export/list');
            changeMenuType('#export-list-menu');
            sessionStorage.setItem("menuStatus", 3);
        }
    }

    $scope.backDetail = function () {
        if (where == "1") {
            $state.go('import/detail');
            sessionStorage.setItem("menuStatus", 2);
        } else {
            $state.go('export/detail');
            sessionStorage.setItem("menuStatus", 4);
        }
    }

    $scope.toSingleTicket = function () {
        $state.go('single/ticket/follow');
    }

    $scope.getContainerMsg = function ($event, id) {
        var item = $event.target;
        $(item).parents('tr').css("background", "#d7f1f2");
        $(item).parents('tr').siblings('tr').css("background", "#ffffff");
        if (id == "1") {
            $scope.conStatus = true;
        } else {
            $scope.conStatus = false;
        }

    }

    var marker, lineArr = [];
    var map = new AMap.Map("ticket-follow-info", {
        resizeEnable: true,
        center: [117.9903373563, 24.4715940141],
        zoom: 17
    });

    AMap.service(["AMap.Driving"], function() {
        var driving = new AMap.Driving({
            map: map
        }); //构造路线导航类
        // 根据起终点坐标规划步行路线
        driving.search([
            {keyword: '福建省厦门市裕雄堆场'},
            {keyword: '福建省南安大霞美滨江机械制造基地宏盛兴机械'},
            {keyword: '福建省厦门市海天码头'}
        ]);
    });

    /*map.on("complete", completeEventHandler);
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
            position: [117.9903373563, 24.4715940141],
            icon: "http://webapi.amap.com/images/car.png",
            offset: new AMap.Pixel(-26, -13),
            autoRotation: true
        });
        var lngX = 117.9903373563, latY = 24.4715940141;
        lineArr.push([lngX, latY]);
        var lngX1 = 118.4932443229, latY1 = 24.9512853238;
        lineArr.push([lngX1, latY1]);
        var lngX2 = 118.0846359228, latY2 = 24.5040938985;
        lineArr.push([lngX2, latY2]);
        /!*for (var i = 1; i < 3; i++) {
         lngX = lngX + Math.random() * 0.05;
         if (i % 2) {
         latY = latY + Math.random() * 0.0001;
         } else {
         latY = latY + Math.random() * 0.06;
         }
         lineArr.push([lngX, latY]);
         }*!/
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
    }*/

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
                // $scope.listData = data.data;
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

function changeMenuType(item) {
    $(item).addClass('menu-active');
    $(item).find('.home-arrow').css("display", "block");
    $(item).siblings().removeClass('menu-active');
    $(item).siblings().find('.home-arrow').css("display", "none");
}
