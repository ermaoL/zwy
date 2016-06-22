/**
 * Created by Administrator on 2016/5/26.
 */
myApp.controller('singleTicketFollowCtrl', function ($scope, $http) {

    var height = $(window).height()-260;
    $('#single-map').css("height", height);

    var marker, lineArr = [];
    var map = new AMap.Map("single-map", {
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
});
