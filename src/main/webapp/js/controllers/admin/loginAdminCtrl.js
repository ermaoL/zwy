/**
 * Created by Administrator on 2016/6/2.
 */
var loginAdminApp = angular.module('loginAdminApp', [
    'ui.router']);
loginAdminApp.controller('loginAdminCtrl', ['$scope', '$http', '$state', function($scope, $http, $state) {
    var height = $(window).height();
    $('.reg-content').css("height", height);
    $('.reg-input-container').css("height", height-120);
    $('.reg-input-container').css("overflow-y", "auto");
    $(window).resize(function() {
        var height = $(window).height();
        $('.reg-content').css("height", height);
        $('.reg-input-container').css("height", height-120);
        $('.reg-input-container').css("overflow-y", "auto");
    });

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
        var type = $(this).attr("data-value");
        $(this).parents('[name="nice-select"]').find('input').val(val);
        $(this).parents('[name="nice-select"]').find('input').attr("admin-type", type);
        $('[name="nice-select"] ul').hide();
        e.stopPropagation();
    });
    $(document).click(function(){
        $('[name="nice-select"] ul').hide();
    });

    // bind here all data from the form
    $scope.account = {};
    // place the message if something goes wrong
    $scope.authMsg = '';

    $scope.sendAdminLogin = function() {
        $scope.authMsg = '';

        if($scope.loginAdminForm.$valid) {

            var transform = function (data) {
                return $.param(data);
            }
            var adminType = $('#_logAdminMember').attr("admin-type");
            

            var requestData = {"adminName": $scope.loginAdminAccount, "adminPass": $scope.loginAdminPwd, "adminType": adminType};

            $http.post("/trans/base/admin/login", requestData, {
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                transformRequest: transform
            }).success(function(data) {
                if(data.success){
                    $.cookie('adminCode',data.adminCode);
                    $.cookie('token',data.token);
                    $.cookie('authType',data.authType);
                    $.cookie('nickName',data.nickName);
                    window.location.href = "/admin-home.html";
                }else{
                    alert(data.errorMsg);
                }
            });

            /*$.ajax({
             type: "POST",
             url: "/trans/api/user/login",
             data: {"userName": $scope.loginUser, "userPass": $scope.loginPwd},
             success: function(data) {
             var status = $.parseJSON(data);
             if(status.success){
             $.cookie('userName',status.userName);
             $.cookie('accessToken',status.token);
             /!*$cookieStore.put("userName", status.userName);
             $cookieStore.put("accessToken", status.token);*!/
             $.cookie("loginStatus", 1);
             window.location.href = "/index.html";
             }else{
             alert(status.errorMsg);
             }
             }
             });*/
        }
        else {
            alert("用户名或密码不能为空");
        }
    };

}]);