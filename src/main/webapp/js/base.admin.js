/**
 * Created by Administrator on 2016/6/6.
 */
(function () {
    var height = $(window).height();
    $('body').css("height", height);
    $('body').css("overflow", "hidden");
    $('.aside').css("height", height - 40);
    $('.section').css("height", height - 40);

    sessionStorage.setItem("menuStatus", 1);

    $(window).resize(function() {
        var height = $(window).height();
        $('.aside').css("height", height - 60);
        $('.section').css("height", height - 60);
        $('body').css("height", height);
        $('body').css("overflow", "hidden");
    });

    

    // 监听浏览器前进回退按钮
    window.onhashchange = function () {
        var url1 = window.location.hash;
        if (url1 == "#/admin/main/import/detail") {
            changeMenuType('#import-list-menu');
            sessionStorage.setItem("menuStatus", 1);
        } else if (url1 == "#/admin/main/export/list") {
            changeMenuType('#export-list-menu');
            sessionStorage.setItem("menuStatus", 3);
        } else if (url1 == "#/admin/main/export/detail") {
            changeMenuType('#export-list-menu');
            sessionStorage.setItem("menuStatus", 3);
        } else if (url1 == "#/admin/main/ticket/follow" || url1 == "#/admin/main/single/ticket/follow") {
            changeMenuType('#ticket-follow-menu');
            sessionStorage.setItem("menuStatus", 5);
        } else if (url1 == "#/admin/main/transit/order") {
            changeMenuType('#transit-order-menu');
            sessionStorage.setItem("menuStatus", 6);
        } else if (url1 == "#/admin/main/bill/inquiry") {
            changeMenuType('#bill-inquiry-menu');
            sessionStorage.setItem("menuStatus", 7);
        } else if (url1 == "#/admin/main/user/manage") {
            changeMenuType('#user-manage-menu');
            sessionStorage.setItem("menuStatus", 8);
        } else if (url1 == "#/admin/main/import/list") {
            changeMenuType('#import-list-menu');
            sessionStorage.setItem("menuStatus", 1);
        }
    }
})();

function changeMenuType(item) {
    $(item).addClass('menu-active');
    $(item).find('.home-arrow').css("display", "block");
    $(item).siblings().removeClass('menu-active');
    $(item).siblings().find('.home-arrow').css("display", "none");
}

function showQuit() {
    $('.show-quit').css("display", "block");
}

function hideQuit() {
    $('.show-quit').css("display", "none");
}

// 公共方法——错误提示
function errorAdminMsgHint(errorCode, errorMsg) {
    if (errorCode >= 100101&&errorCode <= 100107) {
        Dialog.alert(errorMsg);
        window.location.href = "login-admin.html";
    } else {
        Dialog.alert(errorMsg);
    }
}

// 验证手机和座机
function checkTel(item){
    var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
    var isMob=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
    var value=item.value.trim();
    if(isMob.test(value)||isPhone.test(value)){
        $(item).css("color", "#333");
        return true;
    }
    else{
        $(item).css("color", "#ff0000");
        return false;
    }
}

// 判断是否为非负浮点数
function checkNum(item) {
    var isNum = /^\d+(\.\d+)?$/;
    if (isNum.test(item.value)) {
        $(item).css("color", "#333");
        return true;
    }
    else{
        $(item).css("color", "#ff0000");
        return false;
    }
}

// 判断是否为正确邮箱
function isEmail(item){
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    if (reg.test(item.value)) {
        $(item).css("color", "#333");
        return true;
    } else {
        $(item).css("color", "#ff0000");
        return false;
    }
}

// 判断一个年份是否为闰年
function isLeapYear(year) {
    return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
}

//去除字符串的空格
function gf_trim(as_string)
{
    while(as_string.length > 0 && as_string.indexOf(" ")==0) as_string = as_string.substr(1);
    while(as_string.length > 0 && as_string.lastIndexOf(" ")==(as_string.length-1)) as_string = as_string.substr(0,as_string.length-1);
    return as_string;
}

//集装箱箱号验证
//功能：验证集装箱箱号：
//参数：
//   as_cntrno 是否符合国际标准，
//返回值：True 符合国际标准或强行通过(特殊箱号)
//举例：gf_chkcntrno( 'TEXU2982987', 0 )
function chkcntrno(item,ai_choice) {
    var as_cntrno = $(item).val();
    var fi_ki;
    var fi_numsum;
    var fi_nummod;
    var fai_num = new Array(11);
    var fb_errcntrno=false;

    if (as_cntrno==null) return true; //null不进行验证
    if (gf_trim(as_cntrno)=="") return true; //空不进行验证

    if (as_cntrno.length == 11)   //国际标准为11位，最后一位是校验位，若不是11位肯定不是标准箱
    { for(fi_ki=1;fi_ki<=11;fi_ki++)
        fai_num[fi_ki] = 0;
        for(fi_ki=1;fi_ki<=4;fi_ki++)     //根据国际标准验证法则处理箱号前面的4个英文字母
        {
            fch_char=as_cntrno.charAt(fi_ki-1).toUpperCase();
            switch(true)
            { case (fch_char=="A"):{fai_num[fi_ki] = 10;break;}
                case (fch_char>="V" && fch_char<="Z"):{fai_num[fi_ki] = fch_char.charCodeAt() - 52;break;}
                case (fch_char>="L" && fch_char<="U"):{fai_num[fi_ki] = fch_char.charCodeAt() - 53;break;}
                default:{fai_num[fi_ki] = fch_char.charCodeAt() - 54;break;}
            }
        }
        for(fi_ki=5;fi_ki<=11;fi_ki++)
        {  fch_char=as_cntrno.charAt(fi_ki-1);
            fai_num[fi_ki] = parseInt(fch_char); //ctype((mid(as_cntrno, fi_ki, 1)), integer)
        }
        fi_numsum = 0

        for(fi_ki=1;fi_ki<=10;fi_ki++)
        {
            fi_sqr = 1;
            for(i=1;i<fi_ki;i++){fi_sqr *=2;}
            fi_numsum += fai_num[fi_ki] * fi_sqr;
        }

        if (as_cntrno.substr(0,4) == "HLCU") fi_numsum = fi_numsum - 2; //hapaq lloyd的柜号与国际标准相差2
        fi_nummod = fi_numsum % 11;
        if (fi_nummod == 10) fi_nummod = 0;
        if (fi_nummod == fai_num[11]) fb_errcntrno = true;
        $(item).css("color", "#333");
        return fb_errcntrno;
    }else{
        $(item).css("color", "#ff0000");
        return fb_errcntrno;
    }
} 