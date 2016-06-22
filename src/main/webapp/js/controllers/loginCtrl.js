/**
 * Created by cookie on 2016/5/13.
 */

var loginApp = angular.module('loginApp', [
    'ui.router']);
loginApp.controller('loginCtrl', ['$scope', '$http', '$state', function($scope, $http, $state) {
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
    // bind here all data from the form
    $scope.account = {};
    // place the message if something goes wrong
    $scope.authMsg = '';

    /*$('#toLogin').click(function () {
        $.ajax({
            type: "POST",
            url: "/api/user/login",
            data: {"userName": "emi", "userPass": "123456"},
            success: function(data) {
                alert("123  "+data);
                if(data.success){
                    alert("success  "+data);
                    // toastr["warning"]('登录成功');
                    var userId = data.userShowVo.id,
                        mobile = data.userShowVo.mobile;
                    /!*$.cookie('userId',userId);
                     $.cookie('mobile',mobile);
                     window.location.href = "/views/index.html";*!/
                }else{
                    alert("error  "+data);
                    // toastr["warning"](data.errorMessage);
                    //alert(data.errorMessage);
                }
            }
        });
    });*/

    $scope.sendLogin = function() {
        $scope.authMsg = '';

        if($scope.loginForm.$valid) {

            var transform = function (data) {
                return $.param(data);
            }

            var requestData = {"userName": $scope.loginUser, "userPass": $scope.loginPwd};

            $http.post("/trans/api/user/login", requestData, {
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                transformRequest: transform
            }).success(function(data) {
                if(data.success){
                    $.cookie('userName',data.userName);
                    $.cookie('token',data.token);
                    $.cookie('userCompany', data.userCompany);
                    $.cookie('userCompanyName', data.userCompanyName);
                    $.cookie('userCompanyCode', data.userCompanyCode);
                    $.cookie('userConnects', data.userConnects);
                    $.cookie('userConnectPhone', data.userConnectPhone);
                    $.cookie('userConnectEmail', data.userConnectEmail);
                    window.location.href = "/index.html";
                    // window.location.href = "/admin-home.html";
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