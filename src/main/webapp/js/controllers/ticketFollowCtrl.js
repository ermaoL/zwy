/**
 * Created by Administrator on 2016/5/26.
 */
myApp.controller('ticketFollowCtrl',['$scope', '$state', '$http', '$stateParams', 'TicketFollowService', function ($scope, $state, $http, $stateParams, TicketFollowService) {
    var width = $('.follow-info-container').width();
    $('.follow-msg-container').css("width", width-360);

    $(window).resize(function() {
        var width = $('.follow-info-container').width();
        $('.follow-msg-container').css("width", width-360);
    });

    // 图片滚动条
    var oBtnLeft = document.getElementById("goleft");
    var oBtnRight = document.getElementById("goright");
    var oDiv = document.getElementById("indexmaindiv");
    var oUl = oDiv.getElementsByTagName("ul")[0];
    var aLi = oUl.getElementsByTagName("li");
    var offSet = 0;

    
    $scope.conStatus = true;

    var where = sessionStorage.getItem("isImOrEx");
    // 返回订单中心
    $scope.backList = function () {
        if (where == "1") {
            $state.go('index.importList');
            changeMenuType('#import-list-menu');
            sessionStorage.setItem("menuStatus", 1);
        } else {
            $state.go('index.exportList');
            changeMenuType('#export-list-menu');
            sessionStorage.setItem("menuStatus", 3);
        }
    }

    // 返回订单维护
    $scope.backDetail = function () {
        if (where == "1") {
            $state.go('index.importDetail');
            sessionStorage.setItem("menuStatus", 2);
        } else {
            $state.go('index.exportDetail');
            sessionStorage.setItem("menuStatus", 4);
        }
    }

    // 跳转单票
    $scope.toSingleTicket = function () {
        var containerId = $('#container-follow').attr("containerId");
        $state.go('index.singleTicketFollow', {containerId: containerId});
    }

    $scope.getContainerMsg = function ($event, containerId, containerGbsId) {
        var item = $event.target;
        $(item).parents('tr').css("background", "#d7f1f2");
        $(item).parents('tr').siblings('tr').css("background", "#ffffff");
        var timestamp = (new Date()).valueOf();
        var url = "/trans/api/position/search/container/" + containerId + "/" + containerGbsId + "?" + timestamp;
        $http({
            url: url,
            method:'GET',
            headers: {"userName": $.cookie("userName"), "token": $.cookie("token")}
        }).success(function (data) {
            if (data.success) {
                $scope.infoVoList = [];
                if (data.infoVoList.length > 1) {
                    for (var i = data.infoVoList.length - 2; i >= 0; i--) {
                        $scope.infoVoList.push(data.infoVoList[i]);
                    }
                    $scope.infoVoLastList = data.infoVoList[data.infoVoList.length-1];
                } else {
                    $scope.infoVoLastList = data.infoVoList;
                }

                if (data.imgList.length > 0) {
                    bindClickImgList(data.imgList);
                }

                loadTicketFollowMap(data.histVoList);

                $('#container-follow').attr("containerId", containerId);
                $('#container-follow').attr("containerGbsId", containerGbsId);
            } else {
                errorMsgHint(data.errorCode, data.errorMsg);
            }
        });

    }

    
    //基本地图加载
    var map = new AMap.Map("ticket-follow-info", {
        resizeEnable: true,
        zoom: 13
    });
    $('.amap-container').css("position", "absolute");
    //构造路线导航类
    var driving = new AMap.Driving({
        map: map
    });
    var histVoList = [{'longitude': '117.9903373563', 'latitude': '24.4715940141'}, {'longitude': '117.4375935162', 'latitude': '25.3247724749'}, {'longitude': '118.4932453229', 'latitude': '24.9512863238'}, {'longitude': '118.0846349228', 'latitude': '24.5040938985'}];
    loadTicketFollowMap(histVoList);

    /** 获取整票跟踪列表 */
    function getAllTicketStatus() {
        var timestamp = (new Date()).valueOf();
        var orderId = sessionStorage.getItem("followOrderId");
        var url = '/trans/api/position/search/order/' + orderId + '?' + timestamp;
        var ticketFollowData = {
            currentPage: $scope.paginationConf.currentPage,
            pageSize: $scope.paginationConf.itemsPerPage
        }
        TicketFollowService.list(url, ticketFollowData).success(function (data) {
            if (data.success) {
                // $scope.paginationConf.totalItems = data.pagingInfo.totalRows;

                $scope.orderVo = data.orderVo;
                $scope.stateVoList = data.stateVoList;
                $scope.infoVoList = [];
                if (data.infoVoList.length > 1) {
                    for (var i = data.infoVoList.length - 2; i >= 0; i--) {
                        $scope.infoVoList.push(data.infoVoList[i]);
                    }
                    $scope.infoVoLastList = data.infoVoList[data.infoVoList.length-1];
                } else {
                    $scope.infoVoLastList = data.infoVoList;
                }

                if (data.imgList.length > 0) {
                    bindClickImgList(data.imgList);
                }
                data.histVoList = [{'longitude': '117.9903373563', 'latitude': '24.4715940141'}, {'longitude': '118.4932453229', 'latitude': '24.9512863238'}, {'longitude': '118.0846349228', 'latitude': '24.5040938985'}];
                // loadTicketFollowMap(data.histVoList);

                if (data.orderVo.orderType == 1) {
                    $scope.isImportOrExport = true;
                } else if (data.orderVo.orderType == 2) {
                    $scope.isImportOrExport = false;
                }
                $('#container-follow').attr("containerId", data.stateVoList[0].containerId);
                $('#container-follow').attr("containerGbsId", data.stateVoList[0].containerGbsId);
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
    
    function loadTicketFollowMap(histVoList) {
        var lnglatXY = [], tracks;
        var length = histVoList.length;
        var start = new AMap.LngLat(histVoList[0].longitude, histVoList[0].latitude);
        var end = new AMap.LngLat(histVoList[length-1].longitude, histVoList[length-1].latitude);
        for (var i = 1; i < length - 1; i++) {
            lnglatXY.push([histVoList[i].longitude, histVoList[i].latitude]);
        }
        tracks = {waypoints: lnglatXY};
        driving.search(start, end, tracks, function(status, result) {

        });
    }

    function bindClickImgList(imgData) {
        var result = "";
        for (var i = 0; i < imgData.length; i++) {
            result += "<li><a href='" + imgData[i] + "' class='swipebox'><img src='"
                + imgData[i] + "' width='100' height='70'></a></li>";
        }
        $('#count1').html(result);
        $('#count1').css("width", imgData.length * 110 + 60);
        $(".swipebox").swipebox();
        oBtnLeft.onclick = function () {
            if (offSet < 0) {
                oUl.style['left'] = offSet + 60 + 'px';
                offSet = offSet + 60;
            }
        }
        oBtnRight.onclick = function () {
            var width = imgData.length * 110 - 300;
            if (offSet >= -width) {
                oUl.style['left'] = offSet - 60 + 'px';
                offSet = offSet - 60;
            }
        }
    }

}]);

//业务类
myApp.factory('TicketFollowService', ['$http', function ($http) {
    var list = function (url, ticketFollowData) {
        return $http({
            url: url,
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


