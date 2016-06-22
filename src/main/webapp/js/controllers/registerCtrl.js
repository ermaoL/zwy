/**
 * Created by cookie on 2016/5/12.
 */


var registerApp = angular.module('registerApp', [
    'ui.router']);

registerApp.controller('registerCtrl', ['$scope', '$http', '$state', function($scope, $http, $state) {
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
        $(this).parents('[name="nice-select"]').find('input').attr("companyId", type);
        $('[name="nice-select"] ul').hide();
        e.stopPropagation();
    });
    $(document).click(function(){
        $('[name="nice-select"] ul').hide();
    });

    $scope.sendRegister = function() {
        $scope.authMsg = '';

        if($scope.registerForm.$valid) {

            $.ajax({
                type: "POST",
                url: "/trans/api/user/register",
                data: {"userName": $scope.registerUser, "userTel": $scope.registerPhone, "userPass": $scope.registerPwd, "userPassConfirm": $scope.registerPwd, "userCompany": $('#_regCompanyName').val()},
                success: function(data) {
                    var status = $.parseJSON(data);
                    if(status.success){
                        console.log(status.token);
                        sessionStorage.setItem("token", status.token);
                        window.location.href = "/login.html";
                    }else{
                        alert(status.errorMsg);
                    }
                }
            });
        }
        else {
            // set as dirty if the user click directly to login so we show the validation messages
            $scope.registerForm.registerUser.$dirty = true;
            $scope.registerForm.registerPhone.$dirty = true;
            $scope.registerForm.registerPwd.$dirty = true;
            $scope.registerForm.registerCompany.$dirty = true;
        }
    };

}]);
