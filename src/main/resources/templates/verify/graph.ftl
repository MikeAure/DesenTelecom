<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/multiple-select.min.css">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/GA.css">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main1119.css"/>
    <style>
        .amap-icon img,
        .amap-marker-content img {
            width: 25px;
            height: 34px;
        }

        .marker {
            position: absolute;
            top: -20px;
            right: -118px;
            color: #fff;
            padding: 4px 10px;
            box-shadow: 1px 1px 1px rgba(10, 10, 10, .2);
            white-space: nowrap;
            font-size: 12px;
            font-family: "";
            background-color: #25A5F7;
            border-radius: 3px;
        }
        /*标题*/
        .ibox-title {
            height: 200px;
            border-color: #edf1f2;
            background-color: #dbeafe;
            color: black;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .ibox-title span {
            font-size: 50px;
        }

        #submit {
            background-color: #347aa9;
            padding: 5px 20px;
            cursor: pointer;
            color: black;
            font-size: 20px;
            display: inline-block;
            text-align: center;
            /*margin-right: 50px;*/

        }

        .btn2 {
            line-height: 30px;
            text-align: center;
            display: flex;
            justify-content: center;
        }

        /*选择框居中*/
        .midtile {
            line-height: 30px;
            text-align: center;
            display: flex;
            justify-content: center;
        }

        /*上传按钮*/
        .upload-btn {
            background-color: #347aa9;
            color: white;
            cursor: pointer;
            padding: 5px 20px;
            text-align: center;
            font-size: 20px;
            display: inline-block;
            margin: 30px;
        }

        #pre {
            text-align: center;
        }

        #pre img {
            display: inline-block;
            max-width: 50%;
            height: auto;
        }

        #after {
            text-align: center;
        }

        #after img {
            display: inline-block;
            max-width: 50%;
            height: auto;
        }

        .tabs-container ul {
            height: 50px;
            display: flex;
            flex-direction: row;
            justify-content: center;
        }
    </style>
<#--    <script type="text/javascript">-->
<#--        window._AMapSecurityConfig = {-->
<#--            securityJsCode: "919c4ab28e79692cb9fac5abcb3ae1b0",-->
<#--        };-->
<#--    </script>-->

    <script type="text/javascript">
        window._AMapSecurityConfig = {
            securityJsCode: "120a27c95fe9d758fa109e700d2bfb97",
        };
    </script>
    <!-- 全局js -->
    <script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctx!}/js/bootstrap.min.js?v=3.3.6"></script>
    <script src="${ctx!}/js/xlsx.full.min.js"></script>
<#--    <script type="text/javascript"-->
<#--            src="https://webapi.amap.com/maps?v=1.4.15&key=9017e2042639d8873323dcd7d7539611&plugin=AMap.PlaceSearch"></script>-->
    <script type="text/javascript"
            src="https://webapi.amap.com/maps?v=1.4.15&key=3ec301cfc0baed14e8a4c2d1b61b1706&plugin=AMap.PlaceSearch"></script>
    <script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>

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
    <script type="text/javascript">
        const default_marker_image = "https://a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png";

        window.onload = function () {
            document.getElementById("fileUpload").addEventListener("change", choose_file)
            document.getElementById("encryptGraph").addEventListener("click", function () {
                // 获取输入文本
                let inputText = document.getElementById("textInput").value;

                fetch("/Encrypt/desenGraph", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: '&rawData=' + encodeURIComponent(inputText)

                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));

            })

        }

        function initMap() {
            let lnglat = new AMap.LngLat(116.326936, 40.003213);
            let map = new AMap.Map("mapid", {
                resizeEnable: true,  // 自适应地图容器变化
                center: lnglat,      // 地图中心点坐标
                key: "3ec301cfc0baed14e8a4c2d1b61b1706",
                /* key:"a356710cf98fab9a753bceb3f2dc3c96",*/
                viewMode: '3D',   // 地图显示模式，'3D' 表示三维地图
                /*  showBuildingBlock: true, // 是否显示楼块
                  showIndoorMap: true,  // 是否显示室内地图
                  pitch: 45,              // 地图俯仰角度，单位为度，范围 [0, 60]
                  rotation: 10,           // 地图旋转角度，单位为度，范围 [0, 360]*/
                willReadFrequently: true, //
                zoom: 10
            });
            return map;
        }

        function displayTrack(e, map, outlineColor = '#f50b0b', strokeColor = 'red', markerImage = default_marker_image, windowContent="原始轨迹") {
            const content = e.target.result;

            // 使用PlaceSearch类来获取POI信息
            const placeSearch = new AMap.PlaceSearch({
                /* city: 'beijing', // 兴趣点城市*/
                pageSize: 1,
                pageIndex: 1,
                children: 0, //不展示子节点数据
                extensions: 'base' //返回基本地址信息
                //map: map
            });
            // 将文件内容分割成行
            const lines = content.split('\n');
            console.log("Total line number:" + lines.length);
            let index = Math.floor(Math.random() * lines.length);
            console.log("Line index: " + index);
            // 获取第二行数据
            let raw_line = lines[index];
            console.log("Line:", raw_line);
            // 分割原始轨迹每个点
            let raw_points = raw_line.split(';');
            // 去除数组中的空字符串元素
            raw_points = raw_points.filter(item => item.trim() !== '');
            console.log("raw_points length: " + raw_points.length);
            // 保存原路径节点经纬度
            let raw_path = []

            let promises = raw_points.map(point => {
                return new Promise((resolve, reject) => {
                    let [poiId, attributes] = point.split(',');
                    placeSearch.getDetails(poiId, function (status, result) {
                        if (status === 'complete' && result.info === 'OK') {
                            // if (result.poiList.pois && result.poiList.pois.length > 0) {
                            //     let location = result.poiList.pois[0].location;
                            //     console.log(result.poiList.pois[0])
                            //     console.log(location.lng + " " + location.lat)
                            //     resolve([location.lng, location.lat]);
                            // } else {
                            //     console.log("选取的该行POI没有位置数据");
                            //     reject("选取的该行POI没有位置数据");
                            // }
                                let location = result.poiList.pois[0].location;
                                console.log(result.poiList.pois[0])
                                console.log(location.lng + " " + location.lat)
                                resolve([location.lng, location.lat]);

                        } else {
                            console.log("获取POI信息失败，POI ID:" + poiId);
                        }
                    });
                });
            });

            // 使用Promise.all等待所有的Promise都完成
            Promise.all(promises).then(raw_path => {
                for (let [lng, lat] of raw_path) {
                    let marker = new AMap.Marker({
                        icon: markerImage,
                        position: [lng, lat],
                        offset: new AMap.Pixel(-13, -30)
                    });
                    marker.setMap(map)
                }

                // 创建折线实例
                let polyline = new AMap.Polyline({
                    path: raw_path,         // 折线的路径，`desen_path` 应该是包含经纬度坐标的数组
                    isOutline: true,         // 是否显示折线的轮廓线
                    outlineColor: outlineColor, // 轮廓线颜色
                    borderWeight: 1,         // 轮廓线宽度
                    strokeColor: strokeColor,     // 折线颜色
                    strokeOpacity: 1,        // 折线透明度，范围 [0, 1]
                    strokeWeight: 6,         // 折线宽度
                    strokeStyle: "solid",    // 折线样式，可选值："solid"（实线）、"dashed"（虚线）、"dotted"（点线）
                    strokeDasharray: [10, 5], // 虚线样式，数组中的数字表示虚线和空白段的长度
                    lineJoin: 'round',       // 折线交汇处的连接方式，可选值："round"、"bevel"、"miter"
                    lineCap: 'round',        // 折线两端的线帽样式，可选值："butt"、"round"、"square"
                    zIndex: 50,              // 折线的层级
                });

                // 将折线添加至地图实例
                polyline.setMap(map);
                // 选择折线的第一个点作为信息窗体的位置
                let infoWindowPosition = raw_path[0];
                // 创建信息窗体
                let infoWindow = new AMap.InfoWindow({
                    content: windowContent,
                    position: infoWindowPosition,
                    isCustom: true,
                    autoMove: true,
                });

                // 打开信息窗体
                infoWindow.open(map, infoWindowPosition);

                // ...其他代码
            }).catch(error => console.error('Error:', error));
            // // 遍历每个点
            // for (let point of raw_points) {
            //     // 分割每个点的ID和属性
            //     let [poiId, attributes] = point.split(',');
            //
            //     // 根据POI ID进行POI搜索
            //     placeSearch.getDetails(poiId, function (status, result) {
            //         if (status === 'complete' && result.info === 'OK') {
            //             // 获取POI的经纬度
            //             let location = result.poiList.pois[0].location;
            //             console.log(result.poiList.pois[0])
            //             console.log(location.lng + " " + location.lat)
            //             // 节点
            //             //let point_lnglat = new AMap.LngLat(location.lng, location.lat)
            //             raw_path.push([location.lng, location.lat])
            //
            //         } else {
            //             console.log(`获取POI信息失败，POI ID:` + poiId);
            //         }
            //     });
            // }

            // for (let [lng, lat] of raw_path) {
            //     console.log(lng + " " + lat);
            //
            //     let marker = new AMap.Marker({
            //         icon: "https//a.amap.com/jsapi_demos/static/demo-center/icons/poi-marker-default.png",
            //         position: [lng, lat],
            //         offset: new AMap.Pixel(-13, -30)
            //     });
            //     marker.setMap(map)
            //
            // }


        }

        function choose_file(event, map) {
            // 清空
            document.getElementById("fileInfo").innerHTML = "";
            //读取文件
            const file = event.target.files[0]


            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                let fileLoad = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                    "<strong>" + fileName + "文件</strong>已选择"
                "</span>" +
                "</div>";
                console.log(fileName)
                document.getElementById("fileInfo").innerHTML = fileLoad
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                // 读取文件内容绘制原轨迹

                // 初始化map
                let map = initMap();

                //绘制原轨迹
                const reader = new FileReader();
                reader.onload = function (e) {
                    displayTrack(e, map)
                };
                reader.readAsText(file);


                /*if(graphType.includes(fileExtension)){*/
                //提交脱敏参数，请求脱敏
                document.getElementById("submit").onclick = function () {
                    // 获取保护级别
                    let selections = document.getElementById("privacyLevel-selections")
                    let param = selections.value;

                    let formData = new FormData();
                    formData.set("file", file);
                    formData.set("params", param);
                    formData.set("algName", "dpGraph")

                    let idx = $("ul .active").index();
                    if (idx === 0) {
                        formData.set("distortion", "distortion");
                        let distortionAlgs = document.getElementById("distortion-algs")
                        let type1 = distortionAlgs.value;
                        formData.set("sheet", type1);
                    }
                    // else {
                    //     formData.set("distortion", "nodistortion");
                    //     table_body = document.getElementById("table2")
                    //     tr = table_body.rows[0];
                    //     let type2 = tr.childNodes[0].firstChild.value;
                    //     formData.set("sheet", "nodistortion");
                    // }

                    fetch('/File/desenFile', {
                        method: 'POST',
                        body: formData
                    })
                        .then(response => response.blob())
                        .then(blob => {
                            // 创建一个新的FileReader对象
                            const desen_reader = new FileReader();
                            //处理后的轨迹
                            let desen_line;
                            // 读取返回的Blob数据
                            desen_reader.onload = function (e) {
                                displayTrack(e, map, '#49ff00', 'green', default_marker_image, "脱敏后轨迹")
                            };

                            // 将Blob数据传递给FileReader
                            desen_reader.readAsText(blob);

                            // 创建一个下载链接
                            const downloadLink = document.createElement('a');
                            downloadLink.href = URL.createObjectURL(blob);
                            downloadLink.download = "graph" + Date.now().toString(); // 下载的文件名
                            downloadLink.click();
                            // 释放URL对象
                            URL.revokeObjectURL(downloadLink.href);

                        })
                        .catch(error => console.error('Error:', error));

                }

            }
        }

    </script>
</head>
<body>
<div class="row">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
            </div>
            <div class="midtile">
                <div class="col-sm-5 m-b-xs">
                    <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                        <input type="file" id="fileUpload" style="display: none;">
                        <label for="fileUpload" class="upload-btn">
                            选择文件
                        </label>
                    </form>
                </div>
            </div>
            <!--文件上传信息-->
            <div id="fileInfo">
            </div>


            <div class="tabs-container">
                <ul id="tab-type" class="nav nav-tabs" style="left: 50%;font-size: 20px;">
                    <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true"> 失真 </a>
                    </li>
                    <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false"> 非失真 </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div id="tab-1" class="tab-pane active" style="text-align: center;">
                        <div>
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th>请选择图形失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody id="table1">
                                <tr>
                                    <td><select id="distortion-algs">
                                            <option value="dp" selected> 差分算法</option>
                                            <#-- <option value="graph2" selected> 图形失真脱敏算法B </option>-->
                                        </select></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="ibox-content" style="text-align: center;">
                            <div id="privacyLevel">
                                <table style="margin: auto; font-size: 20px">
                                    <thead>
                                    <tr>
                                        <th>请选择隐私保护等级</th>
                                    </tr>
                                    </thead>
                                    <tbody id="table3">
                                    <tr>
                                        <td><select id="privacyLevel-selections">
                                                <option value="0"> 低程度</option>
                                                <option value="1" selected> 中程度</option>
                                                <option value="2"> 高程度</option>
                                            </select></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div id="tab-2" class="tab-pane fade">
                        <div style="text-align: center; font-size: 20px; margin-top: 20px;">
                            <input type="text" id="textInput" placeholder="输入数值时间序列，以,分隔">
                            <button id="encryptGraph">提交</button>
                            <br>
                            <p>返回相似的序列ID</p>
                            <textarea id="outputText" rows="4" cols="50" readonly></textarea>
                        </div>
                    </div>
                </div>
            </div>


            <div class="btn2">
                <button type="button" class="btn btn-sm btn-primary" id="submit"> 提交脱敏</button>
            </div>

            <div id="result">
            </div>
        </div>
    </div>

</div>
<div id="mapid" style="height: 1000px;"></div>

</body>
</html>
