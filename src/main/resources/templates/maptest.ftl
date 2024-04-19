<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>map</title>
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/multiple-select.min.css">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/GA.css">
    <#-- <link rel="stylesheet" href="${ctx!}/css/leaflet.css">-->
    <#--<link href="https://cdn.jsdelivr.net/npm/leaflet@1.7.1/dist/leaflet.css" rel="stylesheet">-->
    <#--<link rel="stylesheet" href="https://unpkg.com/leaflet-polylinedecorator/dist/leaflet.polylineDecorator.css" />-->
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
    <link href="${ctx!}/css/GA.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main1119.css"/>

</head>
<body>
<div id="mapid" style="height: 1000px;"></div>

<script type="text/javascript">
    window._AMapSecurityConfig = {
        securityJsCode: "919c4ab28e79692cb9fac5abcb3ae1b0",
    };
</script>
<!-- 全局js -->
<script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx!}/js/xlsx.full.min.js"></script>
<#--<script src="${ctx!}/js/leaflet/leaflet.js"></script>-->
<script type="text/javascript"
        src="https://webapi.amap.com/maps?v=1.4.15&key=9017e2042639d8873323dcd7d7539611&plugin=AMap.PlaceSearch"></script>
<script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>
<script src="${ctx!}/js/plugins/chosen/chosen.jquery.js"></script>
<script src="${ctx!}/js/bootstrap.min.js"></script>
<script src="${ctx!}/js/echarts.min.js"></script>

<!-- Bootstrap table -->
<script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
<script src="${ctx!}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

<!-- Peity -->
<script src="${ctx!}/js/plugins/peity/jquery.peity.min.js"></script>

<script src="${ctx!}/js/plugins/layer/layer.min.js"></script>
<script src="${ctx!}/js/multiple-select.min.js"></script>

<!-- 自定义js -->
<script src="${ctx!}/js/content.js?v=1.0.0"></script>

<script>

    // 初始化map
    var lnglat = new AMap.LngLat(116.326936, 40.003213);
    var map = new AMap.Map("mapid", {
        resizeEnable: true,  // 自适应地图容器变化
        center: lnglat,      // 地图中心点坐标
        key: "9017e2042639d8873323dcd7d7539611",
        /* key:"a356710cf98fab9a753bceb3f2dc3c96",*/
        viewMode: '3D',   // 地图显示模式，'3D' 表示三维地图
        /*  showBuildingBlock: true, // 是否显示楼块
          showIndoorMap: true,  // 是否显示室内地图
          pitch: 45,              // 地图俯仰角度，单位为度，范围 [0, 60]
          rotation: 10,           // 地图旋转角度，单位为度，范围 [0, 360]*/
        willReadFrequently: true, //
        zoom: 14
    });
    // 路径
    var line = "B000A863SV,141300;B000A84SLI,120201;";

    // 分割每个点
    let points = line.split(';');
    // 去除数组中的空字符串元素
    points = points.filter(item => item.trim() !== '');
    console.log(points.length)

    // 使用PlaceSearch类来获取POI信息
    const placeSearch = new AMap.PlaceSearch({
        /* city: 'beijing', // 兴趣点城市*/
        pageSize: 1,
        pageIndex: 1,
        children: 0, //不展示子节点数据
        extensions: 'base' //返回基本地址信息
        //map: map
    });

    // 保存路径节点经纬度
    var path = []
    // 遍历每个点
    for (const point of points) {
        // 分割每个点的ID和属性
        const [poiId, attributes] = point.split(',');

        // 根据POI ID进行POI搜索
        placeSearch.getDetails(poiId, function (status, result) {
            if (status === 'complete' && result.info === 'OK') {
                // 获取POI的经纬度
                let location = result.poiList.pois[0].location;
                console.log(result.poiList.pois[0])
                console.log(location.lng + " " + location.lat)
                // 节点
                //var point_lnglat = new AMap.LngLat(location.lng, location.lat)
                path.push([location.lng, location.lat])
                // 判断是否已经获取到全部的经纬度信息
                if (path.length === points.length) {
                    // 创建折线实例
                    var polyline = new AMap.Polyline({
                        path: path,
                        isOutline: true,
                        outlineColor: '#ffeeff',
                        borderWeight: 3,
                        strokeColor: "#3366FF",
                        strokeOpacity: 1,
                        strokeWeight: 6,
                        strokeStyle: "solid",
                        strokeDasharray: [10, 5],
                        lineJoin: 'round',
                        lineCap: 'round',
                        zIndex: 50,
                    });

                    // 将折线添加至地图实例
                    polyline.setMap(map);
                }
            } else {
                console.log(`获取POI信息失败，POI ID:` + poiId);
            }
        });

    }
    console.log(path)

</script>

</body>
<style>
    #mapid {
        width: 100%; /* 设置地图容器宽度为页面宽度的百分之百 */
        height: 400px; /* 设置地图容器高度，可以根据需要调整 */
    }
</style>
</html>
