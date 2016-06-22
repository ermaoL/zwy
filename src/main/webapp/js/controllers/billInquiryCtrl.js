/**
 * Created by Administrator on 2016/5/26.
 */
/**
 * Created by cookie on 2016/5/11.
 */
myApp.controller('billInquiryCtrl', ['$scope', '$http', 'BillInquiryService', function($scope, $http, BillInquiryService) {
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
        $(this).parents('[name="nice-select"]').find('input').val(val);
        $('[name="nice-select"] ul').hide();
        if ($('#billSailingYear').val() != "" && $('#billSailingMonth').val() != "") {
            var year = $('#billSailingYear').val();
            var month = $('#billSailingMonth').val();
            var day = "";
            var isLeap = isLeapYear(year);
            if (month == "1" || month == "3" || month == "5" || month == "7" || month == "8" || month == "10" || month == "12") {
                day = "31";
            } else if (month == "4" || month == "6" || month == "9" || month == "11") {
                day = "30";
            } else if (month == "2") {
                if (isLeap) {
                    day = "29";
                } else {
                    day = "28";
                }
            }

            $('#billSailingTime1').val(year + "-" + month + "-" + "01");
            $('#billSailingTime2').val(year + "-" + month + "-" + day);
        } else {
            $('#billSailingTime1').val("");
            $('#billSailingTime2').val("");
        }
        e.stopPropagation();
    });
    $(document).click(function(){
        $('[name="nice-select"] ul').hide();
    });
    

    var billList = [
        {
            "index":1,
            "kaiHangDate": "2015/11/7",
            "tiDanHao": "EGLV146500654560",
            "xiangHao": "EMCU5230955 ",
            "xiangXing": "40RH",
            "tiXiangDiDian": "海沧裕丰堆场",
            "fangXiangDiDian": "海天码头",
            "baoGanFei": "2150",
            "luyuntuoyunfei":"0",
            "chongdianfei":"1330",
            "duicunfei":"18",
            "yindongfei":"0",
            "guijianfei":"0",
            "heji":"3498",
            "jinChuKou": "进口",
            "diDian": "永春蓬壶",
            "chuangDai": "外运"
        },
        {
            "index":2,
            "kaiHangDate": "2015/12/1",
            "tiDanHao": "EGLV146500698371",
            "xiangHao": "TRIU8881560 ",
            "xiangXing": "40RH",
            "tiXiangDiDian": "胜狮货柜",
            "fangXiangDiDian": "海天码头",
            "baoGanFei": "2250",
            "luyuntuoyunfei":"0",
            "chongdianfei":"570",
            "duicunfei":"72",
            "yindongfei":"0",
            "guijianfei":"0",
            "heji":"2892",
            "jinChuKou": "进口",
            "diDian": "永春",
            "chuangDai": "外运"
        },
        {
            "index":3,
            "kaiHangDate": "2015/12/1",
            "tiDanHao": "EGLV146500698371",
            "xiangHao": "EGSU5021764 ",
            "xiangXing": "20RF",
            "tiXiangDiDian": "胜狮货柜",
            "fangXiangDiDian": "海天码头",
            "baoGanFei": "2200",
            "luyuntuoyunfei":"0",
            "chongdianfei":"380",
            "duicunfei":"54",
            "yindongfei":"0",
            "guijianfei":"0",
            "heji":"2634",
            "jinChuKou": "出口",
            "diDian": "永春",
            "chuangDai": "外运"
        },
        {
            "index":4,
            "kaiHangDate": "2015/12/4",
            "tiDanHao": "NYKS2401578270",
            "xiangHao": "NYKU7167350 ",
            "xiangXing": "40RH",
            "tiXiangDiDian": "裕利象屿",
            "fangXiangDiDian": "海天码头",
            "baoGanFei": "2150",
            "luyuntuoyunfei":"100",
            "chongdianfei":"350",
            "duicunfei":"0",
            "yindongfei":"0",
            "guijianfei":"0",
            "heji":"2600",
            "jinChuKou": "出口",
            "diDian": "永春",
            "chuangDai": "外运"
        },
        {
            "index":5,
            "kaiHangDate": "2015/12/4",
            "tiDanHao": "OOLU2021881590",
            "xiangHao": "OOLU6427255 ",
            "xiangXing": "40RH",
            "tiXiangDiDian": "海天货柜",
            "fangXiangDiDian": "海天码头",
            "baoGanFei": "2150",
            "luyuntuoyunfei":"0",
            "chongdianfei":"0",
            "duicunfei":"50",
            "yindongfei":"0",
            "guijianfei":"0",
            "heji":"2200",
            "jinChuKou": "进口",
            "diDian": "永春",
            "chuangDai": "外运"
        },
        {
            "index":6,
            "kaiHangDate": "2015/12/5",
            "tiDanHao": "EGLV146500703781",
            "xiangHao": "EMCU5349780 ",
            "xiangXing": "30RH",
            "tiXiangDiDian": "胜狮货柜",
            "fangXiangDiDian": "海天码头",
            "baoGanFei": "2300",
            "luyuntuoyunfei":"0",
            "chongdianfei":"0",
            "duicunfei":"0",
            "yindongfei":"0",
            "guijianfei":"0",
            "heji":"2300",
            "jinChuKou": "进口",
            "diDian": "永春蓬壶",
            "chuangDai": "外运"
        },
        {
            "index":7,
            "kaiHangDate": "2015/12/5",
            "tiDanHao": "EGLV146500708121",
            "xiangHao": "TCLU1217703 ",
            "xiangXing": "20GP",
            "tiXiangDiDian": "裕利象屿",
            "fangXiangDiDian": "海天码头",
            "baoGanFei": "2150",
            "luyuntuoyunfei":"0",
            "chongdianfei":"380",
            "duicunfei":"0",
            "yindongfei":"0",
            "guijianfei":"0",
            "heji":"2530",
            "jinChuKou": "出口",
            "diDian": "永春",
            "chuangDai": "外运"
        }
    ];

    $scope.billList = billList;

    $scope.selectBillListAll = function () {
        var temp = $('#bill-inquiry-select-all').prop('checked');
        if (temp) {
            $('input[name=billListCheck]').prop("checked", true);
        } else {
            $('input[name=billListCheck]').prop("checked", false);
        }
    }

      /** 查询 */
     $scope.billSearch = function () {
     getAllBillList();
     }

     /** 重置 */
     $scope.billReset = function () {
     $scope.importOrderBillNo = "";
     $scope.importOrderShipName = "";
     $scope.importOrderSailing = "";
     $('#billSailingTime1').val("");
     $('#billSailingTime2').val("");
     getAllBillList();
     }

     /** 获取账单查询列表并分页 */
     var getAllBillList = function () {

     var billSailingDateFrom = $('#billSailingTime1').val();
     var billSailingDateTo = $('#billSailingTime2').val();

     var sailingDateFrom = billSailingDateFrom.split('-');
     var sailingDateTo = billSailingDateTo.split('-');
     var isCorrectDate = true;
     for (var i = 0; i < sailingDateFrom.length; i++) {
     if (sailingDateFrom[i] > sailingDateTo[i]) {
     isCorrectDate = false;
     }
     }

     if (isCorrectDate) {
     var billInquiryData = {
     orderBillNo: $scope.importOrderBillNo,
     orderShipName: $scope.importOrderShipName,
     orderSailing: $scope.importOrderSailing,
     orderArrivalDateFrom: billSailingDateFrom,
     orderArrivalDateTo: billSailingDateTo,
     currentPage: $scope.paginationConf.currentPage,
     pageSize: $scope.paginationConf.itemsPerPage
     }
     BillInquiryService.list(billInquiryData).success(function (data) {
     if (data.success) {
     $scope.paginationConf.totalItems = data.pagingInfo.totalRows;
     // $scope.billList = data.data;
     } else {
     alert(data.errorMsg);
     }

     });
     } else {
     alert("请正确选择开航时间");
     }



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
    // $scope.$watch('paginationConf.currentPage + paginationConf.itemsPerPage', getAllBillList);

}]);

//业务类
myApp.factory('BillInquiryService', ['$http', function ($http) {
    var list = function (billInquiryData) {
        return $http({
            url:'/trans/api/import',
            method:'GET',
            params: billInquiryData,
            headers: {"userName": $.cookie("userName"), "token": $.cookie("token")}
        });
    }

    return {
        list: function (importListData) {
            return list(importListData);
        }
    }
}]);

function cleanBillYearAndMonth() {
    $('#billSailingYear').val("");
    $('#billSailingMonth').val("");
}