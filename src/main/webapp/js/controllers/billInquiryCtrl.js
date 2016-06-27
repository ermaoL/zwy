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


    var billList2 = {
        "billTotalPrice": "163534.00",
        "data": [
            {
                "containerId": "XMHT16060601580645",
                "bills": [
                    {
                        "containerType": "HQ",
                        "containerSize": "40",
                        "drayagePickup": "HTD ",
                        "importExport": "进口",
                        "w15Price": 0,
                        "etd": "2016-06-06 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "HTH ",
                        "w22Price": 0,
                        "containerNo": "OOLU8833546 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "同安",
                        "docCode": "XMHT16060601580645",
                        "claimNo": "OOLU4024123110",
                        "containerId": "XMHT16060601059042",
                        "w21Price": 0,
                        "totalPrice": 0
                    }
                ]
            },
            {
                "containerId": "XMHT16060701580699",
                "bills": [
                    {
                        "containerType": "HQ",
                        "containerSize": "40",
                        "drayagePickup": "YHMT ",
                        "importExport": "进口",
                        "w15Price": 0,
                        "etd": "2016-06-07 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "GWSH ",
                        "w22Price": 0,
                        "containerNo": "BMOU5152630 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "湖里区",
                        "docCode": "XMHT16060701580699",
                        "claimNo": "TRGXMH000687",
                        "containerId": "XMHT16060701059120",
                        "w21Price": 0,
                        "totalPrice": 0
                    },
                    {
                        "containerType": "HQ",
                        "containerSize": "40",
                        "drayagePickup": "YHMT ",
                        "importExport": "进口",
                        "w15Price": 0,
                        "etd": "2016-06-07 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "GWSH ",
                        "w22Price": 0,
                        "containerNo": "SEGU4231650 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "湖里区",
                        "docCode": "XMHT16060701580699",
                        "claimNo": "TRGXMH000687",
                        "containerId": "XMHT16060701059121",
                        "w21Price": 0,
                        "totalPrice": 0
                    }
                ]
            },
            {
                "containerId": "XMHT16060601580644",
                "bills": [
                    {
                        "containerType": "HQ",
                        "containerSize": "40",
                        "drayagePickup": "XSL ",
                        "importExport": "出口",
                        "w15Price": 1500,
                        "etd": "2016-06-09 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "YLMT ",
                        "w22Price": 0,
                        "containerNo": "FSCU9347915 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "惠安惠南工业区",
                        "docCode": "XMHT16060601580644",
                        "claimNo": "CXMU8000728220",
                        "containerId": "XMHT16060601059041",
                        "w21Price": 0,
                        "totalPrice": 1500
                    }
                ]
            },
            {
                "containerId": "XMHT16060701580694",
                "bills": [
                    {
                        "containerType": "RH",
                        "containerSize": "40",
                        "drayagePickup": "YLX ",
                        "importExport": "出口",
                        "w15Price": 2200,
                        "etd": "2016-06-11 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "HTD ",
                        "w22Price": 380,
                        "containerNo": "TRIU8672853 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "石狮祥芝",
                        "docCode": "XMHT16060701580694",
                        "claimNo": "SNL6XMPL803786",
                        "containerId": "XMHT16060701059114",
                        "w21Price": 0,
                        "totalPrice": 2580
                    }
                ]
            },
            {
                "containerId": "XMHT16060901580766",
                "bills": [
                    {
                        "containerType": "HQ",
                        "importExport": "出口",
                        "w15Price": 0,
                        "containerSize": "40",
                        "etd": "2016-06-09 00:00:00",
                        "w02Price": 0,
                        "w22Price": 0,
                        "containerNo": "TGHU9058886 ",
                        "w06Price": 32535.8,
                        "w11Price": 0,
                        "custAdd": "集美",
                        "docCode": "XMHT16060901580766",
                        "claimNo": "CC20160609",
                        "containerId": "XMHT16060901059259",
                        "w21Price": 0,
                        "totalPrice": 32535.8
                    }
                ]
            },
            {
                "containerId": "XMHT16060701580683",
                "bills": [
                    {
                        "containerType": "HQ",
                        "containerSize": "40",
                        "drayagePickup": "BJLQ ",
                        "importExport": "出口",
                        "w15Price": 1500,
                        "etd": "2016-06-15 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "HTD ",
                        "w22Price": 0,
                        "containerNo": "PONU7928162 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "泉州洛江区",
                        "docCode": "XMHT16060701580683",
                        "claimNo": "569908255",
                        "containerId": "XMHT16060701059098",
                        "w21Price": 0,
                        "totalPrice": 1500
                    }
                ]
            },
            {
                "containerId": "XMHT16060701580674",
                "bills": [
                    {
                        "containerType": "GP",
                        "containerSize": "20",
                        "drayagePickup": "XSL ",
                        "importExport": "出口",
                        "w15Price": 1330,
                        "etd": "2016-06-11 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "SYT ",
                        "w22Price": 0,
                        "containerNo": "BMOU2660047 ",
                        "w06Price": 200,
                        "w11Price": 0,
                        "custAdd": "泉州北峰工业区",
                        "docCode": "XMHT16060701580674",
                        "claimNo": "COSU6125558530",
                        "containerId": "XMHT16060701059085",
                        "w21Price": 0,
                        "totalPrice": 1530
                    }
                ]
            },
            {
                "containerId": "XMHT16060701580692",
                "bills": [
                    {
                        "containerType": "GP",
                        "containerSize": "20",
                        "drayagePickup": "HLT ",
                        "importExport": "进口",
                        "w15Price": 0,
                        "etd": "2016-06-07 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "HTH ",
                        "w22Price": 0,
                        "containerNo": "CAIU2373453 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "泉州东海镇",
                        "docCode": "XMHT16060701580692",
                        "claimNo": "HDMUUYXI1591554",
                        "containerId": "XMHT16060701059112",
                        "w21Price": 0,
                        "totalPrice": 0
                    },
                    {
                        "containerType": "GP",
                        "containerSize": "20",
                        "drayagePickup": "HLT ",
                        "importExport": "进口",
                        "w15Price": 0,
                        "etd": "2016-06-07 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "HTH ",
                        "w22Price": 0,
                        "containerNo": "TCKU2723247 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "泉州东海镇",
                        "docCode": "XMHT16060701580692",
                        "claimNo": "HDMUUYXI1591554",
                        "containerId": "XMHT16060701059111",
                        "w21Price": 0,
                        "totalPrice": 0
                    },
                    {
                        "containerType": "GP",
                        "containerSize": "20",
                        "drayagePickup": "HLT ",
                        "importExport": "进口",
                        "w15Price": 0,
                        "etd": "2016-06-07 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "HTH ",
                        "w22Price": 0,
                        "containerNo": "AMFU3011384 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "泉州东海镇",
                        "docCode": "XMHT16060701580692",
                        "claimNo": "HDMUUYXI1591554",
                        "containerId": "XMHT16060701059110",
                        "w21Price": 0,
                        "totalPrice": 0
                    }
                ]
            },
            {
                "containerId": "XMHT16060801580712",
                "bills": [
                    {
                        "containerType": "HQ",
                        "importExport": "进口",
                        "w15Price": 0,
                        "containerSize": "40",
                        "etd": "2016-06-08 00:00:00",
                        "w02Price": 0,
                        "w22Price": 0,
                        "containerNo": "TGHU9058886 ",
                        "w06Price": 32535.8,
                        "w11Price": 0,
                        "custAdd": "湖里区",
                        "docCode": "XMHT16060801580712",
                        "claimNo": "CC20160608",
                        "containerId": "XMHT16060801059131",
                        "w21Price": 0,
                        "totalPrice": 32535.8
                    }
                ]
            },
            {
                "containerId": "XMHT16060701580679",
                "bills": [
                    {
                        "containerType": "HQ",
                        "containerSize": "40",
                        "drayagePickup": "HTD ",
                        "importExport": "进口",
                        "w15Price": 1005,
                        "etd": "2016-06-07 00:00:00",
                        "w02Price": 0,
                        "returnLocation": "HTH ",
                        "w22Price": 0,
                        "containerNo": "OOLU9445294 ",
                        "w06Price": 0,
                        "w11Price": 0,
                        "custAdd": "海沧",
                        "docCode": "XMHT16060701580679",
                        "claimNo": "OOLU4038350110",
                        "containerId": "XMHT16060701059091",
                        "w21Price": 0,
                        "totalPrice": 1005
                    }
                ]
            }
        ],
        "pagingInfo": {
            "totalRows": 100,
            "pageSize": 10,
            "currentRow": 0,
            "pageLastRow": 10,
            "totalPages": 10,
            "currentPage": 1
        },
        "errorCode": 0,
        "success": true,
        "errorMsg": ""
    };

    var listData = billList2.data;
    $('#total').text(billList2.billTotalPrice);
    var list = "";
    $.each(listData,function(n){
        $.each(listData[n].bills,function(i){
            if(i==0){
                if(listData[n].bills.length>1){
                    list += '<tr class="parent" id="row'+n+'">'
                        +'<td><input name="billListCheck" type="checkbox"/></td>'
                        +'<td class="id-display"><span class="billOrderId"></span></td>'//订单号
                        +'<td>'+n+'</td>'//序号
                        +'<td>'+listData[n].bills[i].claimNo+'<button class="add" style="width: 15px;height: 15px;line-height: 7px;padding: 0;margin-left: 4px;">+</button></td>'//提单号
                        +'<td>'+listData[n].bills[i].etd.substr(0,10)+'</td>'//开航日期
                        +'<td>'+listData[n].bills[i].etd.substr(0,10)+'</td>'//操作日期
                        +'<td>'+listData[n].bills[i].containerNo+'</td>'//箱号
                        +'<td>'+listData[n].bills[i].containerType+'</td>'//箱型
                        +'<td>'+listData[n].bills[i].drayagePickup+'</td>'//提箱地点
                        +'<td>'+listData[n].bills[i].returnLocation+'</td>'//返箱地点
                        +'<td>'+listData[n].bills[i].custAdd+'</td>'//地点
                        +'<td>'+listData[n].bills[i].importExport+'</td>'//进出口
                        +'<td>外运</td>'//船代
                        +'<td>'+listData[n].bills[i].w15Price+'</td>'//包干费
                        +'<td>'+listData[n].bills[i].w06Price+'</td>'//陆路托运费
                        +'<td>'+listData[n].bills[i].w22Price+'</td>'//充电费
                        +'<td>'+listData[n].bills[i].w21Price+'</td>'//堆存费
                        +'<td>'+listData[n].bills[i].w11Price+'</td>'//移动费
                        +'<td>'+listData[n].bills[i].w02Price+'</td>'//柜检费
                        +'</tr>';
                }else {
                    list += '<tr class="parent" id="row'+n+'">'
                        +'<td><input name="billListCheck" type="checkbox"/></td>'
                        +'<td class="id-display"><span class="billOrderId"></span></td>'//订单号
                        +'<td>'+n+'</td>'//序号
                        +'<td>'+listData[n].bills[i].claimNo+'</td>'//提单号
                        +'<td>'+listData[n].bills[i].etd.substr(0,10)+'</td>'//开航日期
                        +'<td>'+listData[n].bills[i].etd.substr(0,10)+'</td>'//操作日期
                        +'<td>'+listData[n].bills[i].containerNo+'</td>'//箱号
                        +'<td>'+listData[n].bills[i].containerType+'</td>'//箱型
                        +'<td>'+listData[n].bills[i].drayagePickup+'</td>'//提箱地点
                        +'<td>'+listData[n].bills[i].returnLocation+'</td>'//返箱地点
                        +'<td>'+listData[n].bills[i].custAdd+'</td>'//地点
                        +'<td>'+listData[n].bills[i].importExport+'</td>'//进出口
                        +'<td>外运</td>'//船代
                        +'<td>'+listData[n].bills[i].w15Price+'</td>'//包干费
                        +'<td>'+listData[n].bills[i].w06Price+'</td>'//陆路托运费
                        +'<td>'+listData[n].bills[i].w22Price+'</td>'//充电费
                        +'<td>'+listData[n].bills[i].w21Price+'</td>'//堆存费
                        +'<td>'+listData[n].bills[i].w11Price+'</td>'//移动费
                        +'<td>'+listData[n].bills[i].w02Price+'</td>'//柜检费
                        +'</tr>';
                }

            }else {
                list += '<tr class="child-row'+n+'" style="display: none;background-color: #f5f5f5;">'
                    +'<td><input name="billListCheck" type="checkbox"/></td>'
                    +'<td class="id-display"><span class="billOrderId"></span></td>'//订单号
                    +'<td>'+ n +'-'+ i +'</td>'//序号
                    +'<td>'+listData[n].bills[i].claimNo+'</td>'//提单号
                    +'<td>'+listData[n].bills[i].etd.substr(0,10)+'</td>'//开航日期
                    +'<td>'+listData[n].bills[i].etd.substr(0,10)+'</td>'//操作日期
                    +'<td>'+listData[n].bills[i].containerNo+'</td>'//箱号
                    +'<td>'+listData[n].bills[i].containerType+'</td>'//箱型
                    +'<td>'+listData[n].bills[i].drayagePickup+'</td>'//提箱地点
                    +'<td>'+listData[n].bills[i].returnLocation+'</td>'//返箱地点
                    +'<td>'+listData[n].bills[i].custAdd+'</td>'//地点
                    +'<td>'+listData[n].bills[i].importExport+'</td>'//进出口
                    +'<td>外运</td>'//船代
                    +'<td>'+listData[n].bills[i].w15Price+'</td>'//包干费
                    +'<td>'+listData[n].bills[i].w06Price+'</td>'//陆路托运费
                    +'<td>'+listData[n].bills[i].w22Price+'</td>'//充电费
                    +'<td>'+listData[n].bills[i].w21Price+'</td>'//堆存费
                    +'<td>'+listData[n].bills[i].w11Price+'</td>'//移动费
                    +'<td>'+listData[n].bills[i].w02Price+'</td>'//柜检费
                    +'</tr>';
            }

        });
    });
    //alert(list);
    $('#billListTable').html(list);
    $('tr.parent')
        .css("cursor","pointer")
        .attr("title","点击这里展开/关闭")
        .click(function(){
            $(this).siblings('.child-'+this.id).toggle(
                function(){
                }
            );
            if($(this).find("button").attr("class")=="add"){
                $(this).find("button").text("-");
                $(this).find("button").removeClass().addClass("subtract");
            }else {
                $(this).find("button").text("+");
                $(this).find("button").removeClass().addClass("add");
            }
        });

    /*var billList = [
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

    $scope.billList = billList;*/

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