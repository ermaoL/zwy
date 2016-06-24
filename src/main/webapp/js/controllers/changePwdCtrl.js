/**
 * Created by cookie on 2016/5/13.
 */
var changePwdApp = angular.module('changePwdApp', [
    'ui.router']);

changePwdApp.controller('changePwdCtrl', ['$scope', '$http', '$state', function($scope, $http, $state) {
    var height = $(window).height();
    $('.reg-content').css("height", height);
    $('.reg-input-container').css("height", height-120);
    $('.reg-input-container').css("overflow-y", "auto");
    // bind here all data from the form
    $scope.account = {};
    // place the message if something goes wrong
    $scope.authMsg = '';

    $scope.sendChangePwd = function() {
        $scope.authMsg = '';

        if($scope.changePwdForm.$valid) {

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
                    $.cookie('accessToken',data.token);
                    $.cookie("loginStatus", 1);
                    // window.location.href = "/index.html";
                    window.location.href = "/main-admin.html";
                }else{
                    alert(data.errorMsg);
                }
            });
        }
        else {
            // set as dirty if the user click directly to login so we show the validation messages
            $scope.changePwdForm.oldPwd.$dirty = true;
             $scope.changePwdForm.newPwd.$dirty = true;

        }
    };

}]);