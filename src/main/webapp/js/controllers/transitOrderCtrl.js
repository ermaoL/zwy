/**
 * Created by Administrator on 2016/6/15.
 */
myApp.controller('transitOrderCtrl', function ($scope, $http) {

    var height = $(window).height()-200;
    $('#order-map').css("height", height);

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
        e.stopPropagation();
    });
    $(document).click(function(){
        $('[name="nice-select"] ul').hide();
    });

    var mapObj = new AMap.Map('order-map', {
        resizeEnable: true,
        zoom: 14,
        center: [118.0886175733, 24.5106460463]
    });
    var createMarker = function(data,hide) {
        var div = document.createElement('div');
        div.className = 'circle';
        var r = Math.floor(data.count / 1024);
        div.style.backgroundColor = hide?'#393':'#09f';
        div.innerHTML = data.count || 0;
        var marker = new AMap.Marker({
            content: div,
            title:data.name,
            position: data.center.split(','),
            offset: new AMap.Pixel(-24, 5),
            zIndex: data.count
        });
        marker.subMarkers = [];

        if(!hide){
            marker.setMap(mapObj)
        }
        if(data.subDistricts&&data.subDistricts.length){
            for(var i = 0 ; i<data.subDistricts.length;i+=1){
                marker.subMarkers.push(createMarker(data.subDistricts[i],true));
            }
        }
        return marker;
    }
    var createMarker1 = function(data,hide) {
        var div = document.createElement('div');
        div.className = 'circle-red';
        var r = Math.floor(data.count / 1024);
        div.style.backgroundColor = hide?'#393':'#f00';
        div.innerHTML = data.count || 0;
        var marker = new AMap.Marker({
            content: div,
            title:data.name,
            position: data.center.split(','),
            offset: new AMap.Pixel(-24, 5),
            zIndex: data.count
        });
        marker.subMarkers = [];

        if(!hide){
            marker.setMap(mapObj)
        }
        if(data.subDistricts&&data.subDistricts.length){
            for(var i = 0 ; i<data.subDistricts.length;i+=1){
                marker.subMarkers.push(createMarker(data.subDistricts[i],true));
            }
        }
        return marker;
    }
    
    var markers = []; //province见Demo引用的JS文件
    var address1 = {"center":"118.0886175733, 24.5106460463", "count": "进", "name": ""};
    var address2 = {"center":"118.1059208136, 24.5182545316", "count": "进", "name": ""};
    var address3 = {"center":"118.1184026898, 24.5298816484", "count": "进", "name": ""};
    var address4 = {"center":"118.1035878137, 24.5246304897", "count": "进", "name": ""};
    var address5 = {"center":"118.0928727371, 24.5240729992", "count": "进", "name": ""};
    var address6 = {"center":"118.1055723587, 24.5336708776", "count": "进", "name": ""};
    var marker1 = createMarker(address1);
    var marker2 = createMarker(address2);
    var marker3 = createMarker(address3);
    var marker4 = createMarker(address4);
    var marker5 = createMarker(address5);
    var marker6 = createMarker(address6);
    markers.push(marker1);
    markers.push(marker2);
    markers.push(marker3);
    markers.push(marker4);
    markers.push(marker5);
    markers.push(marker6);
    var address7 = {"center":"118.1162018886, 24.5248495057", "count": "出", "name": ""};
    var address8 = {"center":"118.1213060441, 24.5214242957", "count": "出", "name": ""};
    var address9 = {"center":"118.1023308137, 24.5200944897", "count": "出", "name": ""};
    var address10 = {"center":"118.1105164751, 24.5185346292", "count": "出", "name": ""};
    var marker7 = createMarker1(address7);
    var marker8 = createMarker1(address8);
    var marker9 = createMarker1(address9);
    var marker10 = createMarker1(address10);
    markers.push(marker7);
    markers.push(marker8);
    markers.push(marker9);
    markers.push(marker10);

    var circle = new AMap.Circle({
        center: [118.1082568136, 24.5182875316],
        redius: 100,
        fillOpacity:0.1,
        fillColor:'#09f',
        strokeColor:'#09f',
        strokeWeight:1
    })
    circle.setMap(mapObj);
    mapObj.setFitView();

    //mapObj.setFitView();

});