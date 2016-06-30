/**
 * Created by Administrator on 2016/5/26.
 */
myApp.controller('singleTicketFollowCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {

    var where = sessionStorage.getItem("isImOrEx");
    if (where == "1") {
        changeMenuType('#import-detail-menu');
        sessionStorage.setItem("menuStatus", 2);
    } else {
        changeMenuType('#export-detail-menu');
        sessionStorage.setItem("menuStatus", 4);
    }

    $('#start').removeAttr("disabled");
    $scope.speedOfCar = "0";


    var timestamp = (new Date()).valueOf();
    var containerId = $stateParams.containerId;
    if (containerId == "" || containerId == null || containerId == undefined) {
        singleMapLoad("");
    } else {
        var url = '/trans/api/position/container/' + containerId + '?' + timestamp;
        $http({
            url: url,
            method:'GET',
            params: {containerId: containerId},
            headers: {"userName": $.cookie("userName"), "token": $.cookie("token")}
        }).success(function (data) {
            if (data.success) {

                $scope.containerVo = data.containerVo;
                singleMapLoad(data);
            } else {
                errorMsgHint(data.errorCode, data.errorMsg);
            }
        });
    }


    function singleMapLoad(data) {
        var firstLongitude = "118.1186274498";
        var firstLatitude = "24.4714926999";
        var preLongitude = "", preLatitude = "", nextLongitude = "", nextLatitude = "", twoPointTime = 1;

        var state = data.currentState;
        if (state == 2 || state == 3) {
            firstLongitude = data.containerVo.firstLongitude;
            firstLatitude = data.containerVo.firstLatitude;
            nextLongitude = data.containerVo.nextLongitude;
            nextLatitude = data.containerVo.nextLatitude;
            if (state == 2) {
                preLongitude = data.containerVo.preLongitude;
                preLatitude = data.containerVo.preLatitude;
                twoPointTime = data.containerVo.twoPointTime;
            }
        }

        var marker, lineArr = [];
        //基本地图加载
        var map = new AMap.Map("single-map", {
            resizeEnable: true,
            center: [firstLongitude, firstLatitude],
            zoom: 12
        });
        if (state == 2 || state == 3) {
            //构造路线导航类
            var driving = new AMap.Driving({
                map: map
            });
            var driving1 = new AMap.Driving();
            // 根据起终点经纬度规划驾车导航路线
            driving.search(new AMap.LngLat(firstLongitude, firstLatitude), new AMap.LngLat(nextLongitude, nextLatitude));
            $('#start').attr('disabled', 'disabled');
            if (state == 2) {
                driving1.search(new AMap.LngLat(preLongitude, preLatitude), new AMap.LngLat(nextLongitude, nextLatitude), function () {
                    var distance = result.routes[0].distance/1000;
                    twoPointTime = twoPointTime/3600;
                    $scope.speedOfCar = distance/twoPointTime;
                });
            }
        } else if (state == 4) {
            var histVoList = data.histVoList;
            //构造路线导航类
            var driving4 = new AMap.Driving({
                map: map
            });
            var lnglatXY = [], tracks;
            var length = histVoList.length;
            var start = new AMap.LngLat(histVoList[0].longitude, histVoList[0].latitude);
            var end = new AMap.LngLat(histVoList[length-1].longitude, histVoList[length-1].latitude);
            for (var i = 1; i < length - 1; i++) {
                lnglatXY.push([histVoList[i].longitude, histVoList[i].latitude]);
            }
            tracks = {waypoints: lnglatXY};

            var latX = 0, latY = 0;
            // 根据起终点经纬度规划驾车导航路线
            driving4.search(start, end, tracks, function(status, result) {
                var arr = result.routes[0].steps;
                for (var i = 0; i < arr.length; i++) {
                    latX = arr[i].path[0].lng;
                    latY = arr[i].path[0].lat;
                    lineArr.push([latX, latY]);
                }
            });
            map.on("complete", completeEventHandler);
            var moveStatus = 0;
            AMap.event.addDomListener(document.getElementById('start'), 'click', function() {
                if (moveStatus == 0) {
                    marker.moveAlong(lineArr, 500);
                    moveStatus = 1;
                } else {
                    marker.stopMove();
                    moveStatus = 0;
                }
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

                // 绘制轨迹
                var polyline = new AMap.Polyline({
                    map: map,
                    path: lineArr,
                    strokeColor: "#00A",  //线颜色
                    strokeOpacity: 0,     //线透明度
                    strokeWeight: 0,      //线宽
                    strokeStyle: "solid"  //线样式
                });
                map.setFitView();
            }
        }

    }
}]);

function changeMenuType(item) {
    $(item).addClass('menu-active');
    $(item).find('.home-arrow').css("display", "block");
    $(item).siblings().removeClass('menu-active');
    $(item).siblings().find('.home-arrow').css("display", "none");
}