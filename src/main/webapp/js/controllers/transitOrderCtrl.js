/**
 * Created by Administrator on 2016/6/15.
 */
myApp.controller('transitOrderCtrl', function ($scope, $http) {

    var height = $(window).height()-200;
    $('#order-map').css("height", height);

    var map = new AMap.Map("order-map", {
        resizeEnable: true,
        center: [116.397428, 39.90923],
        zoom: 17
    });
    
});