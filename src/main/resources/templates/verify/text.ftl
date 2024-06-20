<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">

    <title>Insert title here</title>


    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="${ctx!}/css/multiple-select.min.css">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/GA.css">
    <link rel="shortcut icon" href="${ctx!}/favicon.ico">

    <style>
        /*标题*/
        th {
            text-align: center;
        }

        .ibox-title {
            height: 200px;
            border-color: #edf1f2;
            background-color: #dbeafe;
            color: black;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        /*选择框居中*/
        .midtile {
            line-height: 50px;
            text-align: center;
            display: flex;
            justify-content: center;
        }

        /*#privacyLevel {*/
        /*    margin-left: 2.5em;*/
        /*}*/

        .tabs-container ul {
            height: 70px;
            display: flex;
            flex-direction: row;
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

        /*#submitBtn {*/
        /*    background-color: #347aa9;*/
        /*    padding: 5px 20px;*/
        /*    cursor: pointer;*/
        /*    color: black;*/
        /*    font-size: 20px;*/
        /*    display: inline-block;*/
        /*    text-align: center;*/
        /*    !*margin-right: 50px;*!*/
        /*}*/

        #submitBtn {
            background-color: #347aa9;
            color: white;
            cursor: pointer;
            padding: 5px 20px;
            text-align: center;
            font-size: 20px;
            display: inline-block;
            margin: 30px;
        }

        .btn2 {
            line-height: 30px;
            text-align: center;
            display: flex;
            justify-content: center;
        }

        .map-info button,
        .server-content button {
            background-color: #347aa9;
            color: white;
            cursor: pointer;
            padding: 5px 20px;
            text-align: center;
            font-size: 15px;
            display: inline-block;
            margin: auto;
        }

        /*#container {*/
        /*    width: 100%;*/
        /*    height: 100%;*/
        /*}*/

        .tab-pane {
            text-align: center;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        #selection-area table {
            margin: auto;
            font-size: 20px;
        }

        .server-container, .map-container {
            display: flex;
            flex-direction: column; /* 保持子元素垂直排列 */
            align-items: center; /* 确保标题居中 */
            margin-bottom: 20px; /* 为每个地图容器添加底部间距 */
        }

        .server-title, .map-title {
            margin-bottom: 10px; /* 为标题和下面的内容添加间距 */
            font-size: 20px; /* 根据需要调整字体大小 */
            text-align: center; /* 确保标题文本居中 */
        }

        .map-content { /* 新增一个包裹map和map-info的容器的样式 */
            display: flex;
            width: 100%; /* 确保容器宽度充满父容器 */
            justify-content: space-between; /* 使map和map-info在水平方向上分布 */
            /*align-items: start; !* 对齐方式 *!*/
            flex-direction: row; /* 子元素水平排列 */
            align-items: stretch; /* 子元素高度拉伸以匹配最高的子元素 */
        }

        .server-content {
            display: flex;
            width: 100%; /* 确保容器宽度充满父容器 */
            justify-content: center; /* 使map和map-info在水平方向上分布 */
            /*align-items: start; !* 对齐方式 *!*/
            flex-direction: column; /* 子元素水平排列 */
            align-items: center; /* 子元素高度拉伸以匹配最高的子元素 */
        }

        .map, .map-info {
            /*width: 500px; !* 原有大小 *!*/
            /*height: 300px; !* 原有大小 *!*/
            /*background-color: #EEE; !* 仅为了可视化，实际使用时应去除 *!*/
            font-size: 20px;
            flex: 1; /* 使.map和.map-info元素占据相同的空间比例 */
            display: flex;
            flex-direction: column; /* 如果需要，确保其内部内容也能垂直排列 */
            padding: 10px; /* 根据需要调整 */
            box-sizing: border-box; /* 确保内边距不会影响到定义的宽度和高度 */
            min-height: 300px;
        }

        .map {
            min-width: 500px;
        }

        /*.map-info {*/
        /*    margin-left: 20px; !* 信息区域与地图的间距 *!*/
        /*}*/

        .coordinates {
            margin-bottom: 10px; /* 经纬度行之间的间距 */
        }

        /*.log-area {*/
        /*    width: 300px; !* 日志区域宽度 *!*/
        /*    height: 150px; !* 日志区域高度 *!*/
        /*    border: 1px solid #CCC; !* 日志区域边框 *!*/
        /*    margin-top: 10px; !* 日志区域与上方元素的间距 *!*/
        /*}*/

        #serverLog {
            width: 500px; /* 日志区域宽度 */
            height: 300px; /* 日志区域高度 */
            border: 1px solid #CCC; /* 日志区域边框 */
            margin: 10px; /* 日志区域与上方元素的间距 */
        }

        #serverLog textarea {
            width: 100%; /* 使textarea填充整个父容器的宽度 */
            height: 100%; /* 使textarea填充整个父容器的高度 */
            border: none; /* 可选：移除边框 */
            margin: 0; /* 移除外边距 */
            padding: 0; /* 移除内边距 */
            box-sizing: border-box; /* 确保宽度和高度包含边框和内边距 */
            font-size: 20px;
        }

    </style>
    <script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctx!}/js/bootstrap.min.js?v=3.3.6"></script>
    <script src="${ctx!}/js/xlsx.full.min.js"></script>
    <script src="${ctx!}/js/plugins/chosen/chosen.jquery.js"></script>
    <script src="${ctx!}/js/bootstrap.min.js"></script>
    <script src="${ctx!}/js/echarts.min.js"></script>

    <!-- Bootstrap table -->
    <script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
    <script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
    <script src="${ctx!}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

    <!-- Peity -->
    <script src="${ctx!}/js/plugins/peity/jquery.peity.min.js"></script>

    <!-- Map -->
    <script src="${ctx!}/js/plugins/layer/layer.min.js" defer></script>
    <script src="${ctx!}/js/multiple-select.min.js" defer></script>
    <script src="https://webapi.amap.com/maps?v=2.0&key=8a74dc88024b50b5b4d94a407ab49b8d" defer></script>
    <!-- 自定义js -->
    <script src="${ctx!}/js/content.js?v=1.0.0"></script>
    <script type="text/javascript">
        window.onload = function () {
            // debugger
            document.getElementById("nodistortionAlg").addEventListener("change", function () {
                let selection = document.getElementById("nodistortionAlg").value;

                const trace_module = document.getElementById("trace-module");
                const default_module = document.getElementById("default-module");
                // const voiceprint_module = document.getElementById("voiceprint-module");

                if (selection === "trace") {
                    // voiceprint_module.style.visibility = "hidden";
                    trace_module.style.visibility = "visible";
                    default_module.style.visibility = "hidden";
                } else {
                    // voiceprint_module.style.visibility = "hidden";
                    trace_module.style.visibility = "hidden";
                    default_module.style.visibility = "visible";
                }
            });

            document.getElementById("textType").addEventListener("change", function () {
                // 获取文本类型和算法列表
                let text_type = document.getElementById("textType").value;
                let distortion_alg_list = document.getElementById("distortionAlg");

                // 清空算法列表
                distortion_alg_list.innerHTML = "";

                if (text_type === "date") {
                    distortion_alg_list.options.add(new Option("基于差分隐私的日期加噪算法", "dpDate"));
                    distortion_alg_list.options.add(new Option("假名化-哈希", "SHA512"));
                } else if (text_type === "time") {
                    distortion_alg_list.options.add(new Option("时间取整", "floorTime"));
                    distortion_alg_list.options.add(new Option("假名化-哈希", "SHA512"));

                } else if (text_type === "address") {
                    distortion_alg_list.options.add(new Option("地址抑制算法", "addressHide"));
                    distortion_alg_list.options.add(new Option("假名化-哈希", "SHA512"));
                    distortion_alg_list.options.add(new Option("尾部截断", "truncation"));
                    distortion_alg_list.options.add(new Option("数值替换", "value_hide"));

                } else if (text_type === "number") {
                    distortion_alg_list.options.add(new Option("编号抑制算法", "numberHide"));
                    distortion_alg_list.options.add(new Option("假名化-哈希", "SHA512"));
                    distortion_alg_list.options.add(new Option("尾部截断", "truncation"));
                    distortion_alg_list.options.add(new Option("数值替换", "value_hide"));
                    distortion_alg_list.options.add(new Option("随机置换", "passReplace"));

                } else if (text_type === "value") {
                    distortion_alg_list.options.add(new Option("基于高斯机制差分隐私的数值加噪算法", "gaussianToValue"));
                    distortion_alg_list.options.add(new Option("基于拉普拉斯差分隐私的数值加噪算法", "laplaceToValue"));
                    distortion_alg_list.options.add(new Option("基于随机均匀噪声的数值加噪算法", "randomUniformToValue"));
                    distortion_alg_list.options.add(new Option("基于随机拉普拉斯噪声的数值加噪算法", "randomLaplaceToValue"));
                    distortion_alg_list.options.add(new Option("基于随机高斯噪声的数值加噪算法", "randomGaussianToValue"));
                    distortion_alg_list.options.add(new Option("数值偏移", "valueShift"));
                    distortion_alg_list.options.add(new Option("假名化-哈希", "SHA512"));
                    distortion_alg_list.options.add(new Option("数值取整", "floor"));
                    distortion_alg_list.options.add(new Option("数值映射", "valueMapping"));


                } else if (text_type === "name") {
                    distortion_alg_list.options.add(new Option("名称抑制算法", "nameHide"));
                    distortion_alg_list.options.add(new Option("假名化-哈希", "SHA512"));

                } else if (text_type === "email") {
                    distortion_alg_list.options.add(new Option("名称抑制算法", "nameHide"));
                    distortion_alg_list.options.add(new Option("假名化-哈希", "SHA512"));
                    distortion_alg_list.options.add(new Option("邮箱抑制算法", "suppressEmail"));
                    distortion_alg_list.options.add(new Option("编号抑制算法", "numberHide"));

                } else if (text_type === "code") {
                    distortion_alg_list.options.add(new Option("编码型数据差分隐私脱敏算法", "dpCode"));

                } else {
                    distortion_alg_list.options.add(new Option("请选择文本类型", "default"))
                }

            });
            document.getElementById("fileUpload").addEventListener("change", choose_file);

            window._AMapSecurityConfig = {
                securityJsCode: "dd8ae1e880d8bcf447281c3bed5f3c91",
            };
        }

        let choose_file = function (event) {
            // 清空
            document.getElementById("fileInfo").innerHTML = "";

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            const fileName = file.name;
            const fileExtension = fileName.split('.').pop().toLowerCase();
            if (file) {
                if ("xlsx" === fileExtension) {
                    let fileLoad = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                        "<strong>" + fileName + "文件</strong>上传成功"
                    "</span>" +
                    "</div>";
                    document.getElementById("fileInfo").innerHTML = fileLoad
                    //console.log(fileExtension)
                    //构建formData,发送给后端
                    let formData = new FormData();
                    formData.set("file", file);

                    //提交脱敏参数，请求脱敏
                    document.getElementById("submitBtn").onclick = function () {
                        // 选择脱敏程度
                        let table_body = document.getElementById("table3")
                        let tr = table_body.rows[0];
                        console.log(tr);
                        //let privacyLevel = tr.childNodes[0].firstChild.value;
                        let privacyLevel = document.getElementById("table3-selections").value;

                        // formData.append("params", privacyLevel)
                        // 保证params字段唯一值唯一
                        formData.set("params", privacyLevel)

                        // 算法名
                        let alg_name;
                        let idx = $("ul .active").index();
                        if (idx === 0) {
                            //formData.append("distortion", "distortion");
                            let selections = document.getElementById("distortionAlg")
                            alg_name = selections.value;
                            //formData.append("sheet", type1);
                        }

                        // 发送算法名、隐私保护等级、脱敏文件
                        formData.set("algName", alg_name)
                        fetch('/File/desenSingleExcel', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                // 脱敏前
                                /*  document.getElementById("preData").innerHTML="脱敏前数据"*/
                                let reader = new FileReader();
                                reader.onload = function (e) {
                                    let data = new Uint8Array(e.target.result);
                                    let workbook = XLSX.read(data, {type: 'array'});

                                    let sheetName = workbook.SheetNames[0];
                                    let sheet = workbook.Sheets[sheetName];

                                    let jsonData = XLSX.utils.sheet_to_json(sheet, {header: 1});

                                    let pageSize = 10;
                                    let pageCount = Math.ceil((jsonData.length - 1) / pageSize);
                                    let currentPage = 1;

                                    function displayTable(page) {
                                        let startIndex = (page - 1) * pageSize + 1; // 跳过表头
                                        let endIndex = Math.min(startIndex + pageSize, jsonData.length);

                                        let tableContent = '<thead><tr>';
                                        let headers = jsonData[0];
                                        headers.forEach(function (header) {
                                            tableContent += '<th style=\"white-space: nowrap;\">' + header + '</th>';
                                        });
                                        tableContent += '</tr></thead><tbody>';

                                        for (let i = startIndex; i < endIndex; i++) {
                                            tableContent += '<tr>';
                                            for (let j = 0; j < headers.length; j++) {
                                                let cellValue = (jsonData[i][j] !== undefined) ? jsonData[i][j] : '';
                                                tableContent += '<td>' + cellValue + '</td>';
                                            }
                                            tableContent += '</tr>';
                                        }

                                        tableContent += '</tbody>';

                                        $('#dataTable').html(tableContent);
                                    }

                                    displayTable(currentPage);

                                    function renderPagination() {
                                        let pagination = '<li class="page-item"><a class="page-link" href="#" data-page="prev">Prev</a></li>';
                                        pagination += '<li class="page-item"><a class="page-link" href="#" data-page="next">Next</a></li>';

                                        $('#pagination').html(pagination);

                                        $('#pagination a').click(function (e) {
                                            e.preventDefault();
                                            let page = $(this).data('page');
                                            console.log(page)
                                            if (page === 'prev') {
                                                currentPage = Math.max(1, currentPage - 1);
                                            } else if (page === 'next') {
                                                currentPage = Math.min(pageCount, currentPage + 1);
                                            }
                                            displayTable(currentPage);
                                            renderPagination();
                                        });

                                        let info = "共" + pageCount + "页"
                                        console.log(info)
                                        $('#totalPages').text(info);
                                    }

                                    $('#paginationContainer').show();
                                    renderPagination();

                                    $('#goToPage').click(function () {
                                        let pageNumber = parseInt($('#pageInput').val());
                                        if (pageNumber >= 1 && pageNumber <= pageCount) {
                                            currentPage = pageNumber;
                                            displayTable(currentPage);
                                            renderPagination();
                                        } else {
                                            alert('请输入有效页数！');
                                        }
                                    });
                                };
                                reader.readAsArrayBuffer(file);

                                // 脱敏后
                                /* document.getElementById("afterData").innerHTML="脱敏后数据"*/
                                const reader1 = new FileReader();
                                reader1.onload = function (event) {
                                    const data = event.target.result;
                                    const workbook = XLSX.read(data, {type: 'binary'});
                                    const sheetName = workbook.SheetNames[0];
                                    const sheet = workbook.Sheets[sheetName];
                                    const jsonData = XLSX.utils.sheet_to_json(sheet, {header: 1});
                                    let pageSize = 10;
                                    let pageCount = Math.ceil((jsonData.length - 1) / pageSize);
                                    let currentPage1 = 1;

                                    function displayTable1(page1) {
                                        let startIndex1 = (page1 - 1) * pageSize + 1; // 跳过表头
                                        let endIndex = Math.min(startIndex1 + pageSize, jsonData.length);

                                        let tableContent1 = '<thead><tr>';
                                        let headers1 = jsonData[0];
                                        headers1.forEach(function (header1) {
                                            tableContent1 += '<th style=\"white-space: nowrap;\">' + header1 + '</th>';
                                        });
                                        tableContent1 += '</tr></thead><tbody>';

                                        for (let i = startIndex1; i < endIndex; i++) {
                                            tableContent1 += '<tr>';
                                            for (let j = 0; j < headers1.length; j++) {
                                                let cellValue = (jsonData[i][j] !== undefined) ? jsonData[i][j] : '';
                                                tableContent1 += '<td>' + cellValue + '</td>';
                                            }
                                            tableContent1 += '</tr>';
                                        }

                                        tableContent1 += '</tbody>';

                                        $('#dataTable1').html(tableContent1);
                                    }

                                    displayTable1(currentPage1);

                                    function renderPagination1() {
                                        let pagination1 = '<li class="page-item"><a class="page-link" href="#" data-page="prev1">Prev</a></li>';
                                        pagination1 += '<li class="page-item"><a class="page-link" href="#" data-page="next1">Next</a></li>';

                                        $('#pagination1').html(pagination1);

                                        $('#pagination1 a').click(function (e) {
                                            e.preventDefault();
                                            let page = $(this).data('page');
                                            console.log(page)
                                            if (page === 'prev1') {
                                                currentPage1 = Math.max(1, currentPage1 - 1);
                                            } else if (page === 'next1') {
                                                currentPage1 = Math.min(pageCount, currentPage1 + 1);
                                            }
                                            displayTable1(currentPage1);
                                            renderPagination1();
                                        });

                                        let info = "共" + pageCount + "页"
                                        console.log(info)
                                        $('#totalPages1').text(info);
                                    }

                                    $('#paginationContainer1').show();
                                    renderPagination1();

                                    $('#goToPage1').click(function () {
                                        let pageNumber1 = parseInt($('#pageInput1').val());
                                        if (pageNumber1 >= 1 && pageNumber1 <= pageCount) {
                                            currentPage1 = pageNumber1;
                                            displayTable1(currentPage1);
                                            renderPagination1();
                                        } else {
                                            alert('请输入有效页数！');
                                        }
                                    });
                                };
                                reader1.readAsBinaryString(blob);

                                // 创建一个下载链接
                                const downloadLink = document.createElement('a');
                                downloadLink.href = URL.createObjectURL(blob);
                                downloadLink.download = Date.now().toString() + ".xlsx"; // 下载的文件名
                                downloadLink.click();
                                // after.appendChild(downloadLink);
                            })
                            .catch(error => console.error('Error:', error));
                    }
                } else {
                    alert("请提交excel文件")
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

            <div class="ibox-content">
                <div class="tabs-container">
                    <ul id="tab-type" class="nav nav-tabs" style="font-size: 20px">
                        <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true"> 失真 </a>
                        </li>
                        <li class="">
                            <a data-toggle="tab" href="#tab-2" aria-expanded="false"> 非失真 </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="tab-1" class="tab-pane active" style="text-align: center;">
                            <div class="form-group">

                                <label class="control-label block" for="textType"
                                       style="font-size: 20px">请选择文本类型</label>

                                <select name="type" id="textType"
                                        style="font-size: 20px">
                                    <option value="111" selected>请选择文本类型</option>
                                    <option value="date">日期</option>
                                    <option value="time">时间</option>
                                    <option value="address">地址</option>
                                    <option value="number">编号</option>
                                    <option value="value">数值</option>
                                    <option value="name">名称</option>
                                    <option value="email">邮箱</option>
                                    <option value="code">编码</option>
                                </select>

                            </div>

                            <div class="form-group">
                                <table style="margin: auto; font-size: 20px">
                                    <thead>
                                    <tr>
                                        <th>请选择文本失真脱敏算法</th>
                                    </tr>
                                    </thead>
                                    <tbody id="table1">
                                    <tr>
                                        <td>
                                            <select id="distortionAlg">
                                                <option value="default">请选择脱敏算法</option>
                                            </select>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                            <div class="form-group">
                                <div id="privacyLevel">
                                    <table style="margin: auto; font-size: 20px">
                                        <thead>
                                        <tr>
                                            <th>请选择隐私保护等级</th>
                                        </tr>
                                        </thead>
                                        <tbody id="table3">
                                        <tr>
                                            <td>
                                                <label for="table3-selections"></label>
                                                <select id="table3-selections">
                                                    <option value="1"> 低程度</option>
                                                    <option value="2" selected> 中程度</option>
                                                    <option value="3"> 高程度</option>
                                                </select>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <div id="fileInfo"></div>

                            <div class="btn1">
                                <input type="file" id="fileUpload" style="display: none;">
                                <label for="fileUpload" class="btn btn-sm btn-primary upload-btn">
                                    选择文件
                                </label>
                            </div>

                            <div class="btn2">
                                <button type="button" class="btn btn-sm btn-primary" id="submitBtn">提交脱敏
                                </button>
                            </div>

                            <div style="display: flex; flex-wrap: wrap; justify-content: center;">
                                <!-- 第一个表格 -->
                                <div id="dataTableContainer" style="width: 50%; overflow-x: auto;">
                                    <table id="dataTable" class="table table-bordered">
                                        <!-- 这里将用 JavaScript 动态创建表格内容 -->
                                    </table>
                                    <div id="paginationContainer" class="mt-3" style="display: none;">
                                        <nav>
                                            <div id="paginationInfo"
                                                 class="d-flex justify-content-between align-items-center">
                                                <ul class="pagination mb-0" id="pagination"></ul>
                                                <div class="form-group mb-0 text-center">
                                                    <span id="totalPages"></span>
                                                    <label for="pageInput">跳转至：</label>
                                                    <input type="number" class="form-control" id="pageInput" min="1">
                                                    <button class="btn btn-primary" id="goToPage">跳转</button>
                                                </div>
                                            </div>
                                        </nav>
                                    </div>
                                </div>

                                <!-- 第二个表格 -->
                                <div id="dataTableContainer1" style="width: 50%; overflow-x: auto;">
                                    <table id="dataTable1" class="table table-bordered">
                                        <!-- 这里将用 JavaScript 动态创建表格内容 -->
                                    </table>
                                    <div id="paginationContainer1" class="mt-3" style="display: none;">
                                        <nav>
                                            <div id="paginationInfo1"
                                                 class="d-flex justify-content-between align-items-center">
                                                <ul class="pagination mb-0" id="pagination1"></ul>
                                                <div class="form-group mb-0 text-center">
                                                    <span id="totalPages1"></span>
                                                    <label for="pageInput1">跳转至：</label>
                                                    <input type="number" class="form-control" id="pageInput1" min="1">
                                                    <button class="btn btn-primary" id="goToPage1">跳转</button>

                                                </div>
                                            </div>
                                        </nav>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="tab-2" class="tab-pane">
                            <div id="selection-area">
                                <table>
                                    <thead>
                                    <tr>
                                        <th>请选择文本非失真脱敏算法</th>
                                    </tr>
                                    </thead>
                                    <tbody id="table2">
                                    <tr>
                                        <td>
                                            <select id="nodistortionAlg">
                                                <option value="default" selected>请选择脱敏算法</option>
                                                <option value="trace">在线打车外包查询算法</option>
                                                <#--                                            <option value="voice-print">TestItem</option>-->
                                            </select>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                            <div id="nondistortion-module-container" class="tab-pane">
                                <div id="default-module" style="visibility: visible"></div>
                                <#--                            <div id="voiceprint-module" class="tab-pane" style="visibility: hidden">Test div2</div>-->
                                <div id="trace-module" style="visibility: hidden">
                                    <div class="map-container" id="driverMap1-container">
                                        <div class="map-title">司机1</div> <!-- 标题元素 -->
                                        <div class="map-content"> <!-- 新增的包裹容器 -->
                                            <div class="map" id="driverMap1"></div>
                                            <div class="map-info">
                                                <span>当前经纬度: <div id="driver1Coord"></div></span>
                                                <button type="button" class="btn btn-sm btn-primary" id="lockDriver1">确定
                                                </button>
                                                <div class="log-area" id="driver1Log">
                                                    <textarea id="driver1LogArea" rows="5" cols="33"></textarea>
                                                </div>
                                                <button type="button" class="btn btn-sm btn-primary" id="driver1Stop">停止
                                                </button>

                                            </div>
                                        </div>
                                    </div>

                                    <div class="map-container" id="driverMap2-container">
                                        <div class="map-title">司机2</div> <!-- 标题元素 -->
                                        <div class="map-content"> <!-- 新增的包裹容器 -->
                                            <div class="map" id="driverMap2"></div>
                                            <div class="map-info">
                                                <span>当前经纬度: <div id="driver2Coord"></div></span>
                                                <button type="button" class="btn btn-sm btn-primary" id="lockDriver2">确定
                                                </button>
                                                <div class="log-area" id="driver2Log">
                                                    <textarea id="driver2LogArea" rows="5" cols="33"></textarea>
                                                </div>
                                                <button type="button" class="btn btn-sm btn-primary" id="driver2Stop">停止
                                                </button>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="map-container" id="customerMap-container">
                                        <div class="map-title">乘客</div> <!-- 标题元素 -->
                                        <div class="map-content"> <!-- 新增的包裹容器 -->
                                            <div class="map" id="customerMap"></div>
                                            <div class="map-info">
                                                <div class="coordinates">
                                                    <span>出发点经纬度: <div id="startCoord"></div></span>
                                                    <button type="button" class="btn btn-sm btn-primary" id="lockStart">
                                                        确定
                                                    </button>
                                                </div>
                                                <div class="coordinates">
                                                    <span>目的地经纬度: <div id="endCoord"></div></span>
                                                    <button type="button" class="btn btn-sm btn-primary" id="lockEnd">确定
                                                    </button>
                                                </div>
                                                <button type="button" class="btn btn-sm btn-primary" id="sendRequest">
                                                    发送打车请求
                                                </button>
                                                <div class="log-area" id="customerLog">
                                                    <textarea id="customerLogArea" rows="5" cols="33"></textarea>
                                                </div>
                                                <button type="button" class="btn btn-sm btn-primary" id="customerStop">
                                                    停止
                                                </button>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="server-container" id="serverInfo-container">
                                        <div class="server-title">服务器</div>
                                        <div class="server-content">
                                            <div class="log-area" id="serverLog">
                                                <textarea id="serverLogArea" rows="5" cols="33"></textarea>
                                            </div>
                                            <div class="buttons-row">
                                                <button type="button" class="btn btn-sm btn-primary" id="serverStart">启动
                                                </button>
                                                <button type="button" class="btn btn-sm btn-primary" id="serverStop">停止
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<#--<script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>-->
<#--<script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>-->
<#--<script type="text/javascript">-->
<#--    let socket = new SockJS('/ws');-->
<#--    let stompClient = Stomp.over(socket);-->

<#--    stompClient.connect({}, function(frame) {-->
<#--        console.log('Connected: ' + frame);-->
<#--        stompClient.subscribe('/topic/customerLog', function(customerLog) {-->
<#--            let customerLogArea = document.getElementById('customerLogArea');-->
<#--            let isDriverAccepted = JSON.parse(customerLog.body).status;-->
<#--            if (isDriverAccepted === 'ok') {-->
<#--                let driverInfo = JSON.parse(customerLog.body).message;-->
<#--                customerLogArea.value += "司机" + driverInfo + "接单\n";-->
<#--            } else {-->
<#--                customerLogArea.value += "无人接单\n";-->
<#--            }-->
<#--        });-->
<#--    });-->

<#--    // 你可能需要修改按钮的点击事件处理器，以通过WebSocket发送消息-->
<#--</script>-->
<!-- 全局js -->
<script type="text/javascript">
    let startMarker;
    let driver1Marker;
    let driver2Marker;
    let endMarker;


    document.addEventListener("DOMContentLoaded", (event) => {
        // 初始化乘客地图
        let customerMap = initMap('customerMap', {center: [108.945, 34.285], zoom: 11});
        customerMap.setLimitBounds(new AMap.Bounds([108.77, 34.14], [109.12, 34.43]));
        startMarker = addDraggableMarker(customerMap, [108.945, 34.285], 'startCoord', 'http://webapi.amap.com/theme/v1.3/markers/n/start.png');
        endMarker = addDraggableMarker(customerMap, [108.946, 34.285], 'endCoord', 'http://webapi.amap.com/theme/v1.3/markers/n/end.png');
        // 修改初始化Marker的调用
        lockMarkerCustomer(startMarker, 'lockStart', 'start');
        lockMarkerCustomer(endMarker, 'lockEnd', 'end');

        // 初始化司机地图1
        let driverMap1 = initMap('driverMap1', {center: [108.945, 34.285], zoom: 11});
        driverMap1.setLimitBounds(new AMap.Bounds([108.77, 34.14], [109.12, 34.43]))
        driver1Marker = addDraggableMarker(driverMap1, [108.945, 34.285], 'driver1Coord', 'Default');
        lockMarkerDriver1(driver1Marker, 'lockDriver1');

        // 初始化司机地图2
        let driverMap2 = initMap('driverMap2', {center: [108.945, 34.285], zoom: 11});
        driverMap2.setLimitBounds(new AMap.Bounds([108.77, 34.14], [109.12, 34.43]));
        driver2Marker = addDraggableMarker(driverMap2, [108.945, 34.285], 'driver2Coord', 'Default');
        lockMarkerDriver2(driver2Marker, 'lockDriver2');
    })

    // 初始化地图
    function initMap(containerId, options) {
        return new AMap.Map(containerId, {
            center: options.center, // 地图中心点
            zoom: options.zoom // 地图显示的缩放级别
        });
    }

    // 添加可拖动的Marker
    function addDraggableMarker(map, position, markerId, iconUrl) {
        let marker = new AMap.Marker({
            position: position,
            draggable: true,
            cursor: 'move',
            raiseOnDrag: true,
            map: map,
            anchor: "bottom-center"
        });

        if (iconUrl !== 'Default') {
            const icon = new AMap.Icon({
                image: iconUrl,
                imageOffset: new AMap.Pixel(-0.5, 0)
            })
            marker.setIcon(icon);
        }
        // 监听Marker拖动事件更新对应的经纬度显示
        marker.on('dragend', function (e) {
            document.getElementById(markerId).innerHTML = "<span>经度：" + e.lnglat.getLng() + ", 纬度：" + e.lnglat.getLat() + "</span>";
        });

        return marker;
    }

    // 锁定Marker
    function lockMarkerDriver1(marker, buttonId) {
        document.getElementById(buttonId).addEventListener('click', function () {
            marker.setDraggable(false); // 设置Marker不可拖动
            let position = marker.getPosition();
            let latitude = position.lat; // 纬度
            let longitude = position.lng; // 经度
            driver1SendCoordinatesToBackend(latitude, longitude);
        });
    }

    function lockMarkerDriver2(marker, buttonId) {
        document.getElementById(buttonId).addEventListener('click', function () {
            marker.setDraggable(false); // 设置Marker不可拖动
            let position = marker.getPosition();
            let latitude = position.lat; // 纬度
            let longitude = position.lng; // 经度
            driver2SendCoordinatesToBackend(latitude, longitude);
        });
    }

    function lockMarkerCustomer(marker, buttonId, startOrEnd) {
        document.getElementById(buttonId).addEventListener('click', function () {
            marker.setDraggable(false); // 设置Marker不可拖动

            // 获取Marker的当前位置
            let position = marker.getPosition();
            let latitude = position.lat; // 纬度
            let longitude = position.lng; // 经度

            // // 根据是起点还是终点，准备发送请求
            // if (startOrEnd === 'start') {
            //     // 保存起点坐标
            //     window.customerStartLatitude = latitude;
            //     window.customerStartLongitude = longitude;
            // } else if (startOrEnd === 'end') {
            //     // 保存终点坐标
            //     window.customerEndLatitude = latitude;
            //     window.customerEndLongitude = longitude;
            // }
            //
            // // 如果起点和终点坐标都已经设置，则发送数据到后端
            // if (window.customerStartLatitude && window.customerStartLongitude && window.customerEndLatitude && window.customerEndLongitude) {
            //     sendCoordinatesToBackend(window.customerStartLatitude, window.customerStartLongitude, window.customerEndLatitude, window.customerEndLongitude);
            // }

            sendCoordinatesToBackend(latitude, longitude, startOrEnd);
        });
    }

    // 发送坐标到后端的函数
    function sendCoordinatesToBackend(latitude, longitude, startOrEnd) {
        if (startOrEnd === 'start') {
            fetch('/Encrypt/customerSetStartCoordinate?startLatitude=' + latitude + '&startLongitude=' + longitude, {
                method: 'GET', // 或者 'POST' 如果后端期望POST请求
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            })
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    console.log(latitude, longitude);
                })
                .catch(error => console.error('Error:', error));
        } else if (startOrEnd === 'end') {
            fetch('/Encrypt/customerSetEndCoordinate?endLatitude=' + latitude + '&endLongitude=' + longitude, {
                method: 'GET', // 或者 'POST' 如果后端期望POST请求
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                }
            })
                .then(response => response.json())
                .then(data => console.log(data))
                .catch(error => console.error('Error:', error));
        }

    }

    function driver1SendCoordinatesToBackend(startLatitude, startLongitude) {
        const logArea = document.getElementById('driver1LogArea');
        fetch('/Encrypt/traceDriver1Login?startLatitude=' + startLatitude + '&startLongitude=' + startLongitude, {
            method: 'GET', // 或者 'POST' 如果后端期望POST请求
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
            .then(response => response.json())
            .then(data => {

                if (data.status === "ok") {
                    logArea.value += 'driver1发送位置信息成功\n';
                } else if (data.status === "error") {
                    logArea.value += 'driver1发送位置信息失败\n';
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function driver2SendCoordinatesToBackend(startLatitude, startLongitude) {
        const logArea = document.getElementById('driver2LogArea');
        fetch('/Encrypt/traceDriver2Login?startLatitude=' + startLatitude + '&startLongitude=' + startLongitude, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
            .then(response => response.json())
            .then(data => {

                if (data.status === "ok") {
                    logArea.value += 'driver2发送位置信息成功\n';
                } else if (data.status === "error") {
                    logArea.value += 'driver2发送位置信息失败\n';
                }
            })
            .catch(error => console.error('Error:', error));
    }


    const startButton = document.getElementById('serverStart');
    const stopButton = document.getElementById('serverStop');
    const customerStopBtn = document.getElementById('customerStop');
    const driver1StopBtn = document.getElementById('driver1Stop');
    const driver2StopBtn = document.getElementById('driver2Stop');
    const cusReqBtn = document.getElementById('sendRequest');

    // 监听按钮点击事件
    startButton.addEventListener('click', function () {
        const logArea = document.getElementById('serverLogArea');
        // 调用API
        fetch('/Encrypt/serverStart', {
            method: 'GET', // 或者是 'POST', 取决于API的要求
            // 这里可以添加请求头部等信息，如果需要的话
            headers: {
                'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
            },
        })
            .then(response => response.json()) // 处理JSON响应
            .then(data => {
                // 根据返回值更新日志区域

                if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                    logArea.value += 'trace服务器启动成功\n'; // 添加一行日志
                } else if (data.status === 'error') {
                    logArea.value += 'trace服务器启动失败\n'; // 添加一行日志
                }
            })
            .catch(error => {
                console.error('请求失败:', error);
                // 处理请求失败的情况
                logArea.value += 'trace服务器启动请求失败\n'; // 添加一行日志
            });
    });

    stopButton.addEventListener('click', function () {
        const logArea = document.getElementById('serverLogArea');
        // 调用API
        fetch('/Encrypt/serverStop', {
            method: 'GET', // 或者是 'POST', 取决于API的要求
            // 这里可以添加请求头部等信息，如果需要的话
            headers: {
                'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
            },
        })
            .then(response => response.json()) // 处理JSON响应
            .then(data => {
                // 根据返回值更新日志区域

                if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                    logArea.value += 'trace服务器停止成功\n'; // 添加一行日志
                } else if (data.status === 'error') {
                    logArea.value += 'trace服务器停止失败\n'; // 添加一行日志
                }
            })
            .catch(error => {
                console.error('请求失败:', error);
                // 处理请求失败的情况

                logArea.value += 'trace服务器启动请求失败\n'; // 添加一行日志
            });
    });

    customerStopBtn.addEventListener('click', function () {
        const logArea = document.getElementById('customerLogArea');
        // 调用API
        fetch('/Encrypt/customerStop', {
            method: 'GET', // 或者是 'POST', 取决于API的要求
            // 这里可以添加请求头部等信息，如果需要的话
            headers: {
                'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
            },
        })
            .then(response => response.json()) // 处理JSON响应
            .then(data => {
                // 根据返回值更新日志区域

                if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                    logArea.value += 'customer已停止\n'; // 添加一行日志
                    startMarker.setDraggable(true);
                    endMarker.setDraggable(true);
                } else if (data.status === 'error') {
                    logArea.value += 'customer停止失败\n'; // 添加一行日志
                }
            })
            .catch(error => {
                console.error('请求失败:', error);
                // 处理请求失败的情况
                logArea.value += 'customer停止请求失败\n'; // 添加一行日志
            });
    });

    driver1StopBtn.addEventListener('click', function () {
        const logArea = document.getElementById('driver1LogArea');
        // 调用API
        fetch('/Encrypt/driver1Stop', {
            method: 'GET', // 或者是 'POST', 取决于API的要求
            // 这里可以添加请求头部等信息，如果需要的话
            headers: {
                'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
            },
        })
            .then(response => response.json()) // 处理JSON响应
            .then(data => {
                // 根据返回值更新日志区域

                if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                    logArea.value += 'driver1已停止\n'; // 添加一行日志
                    driver1Marker.setDraggable(true);
                } else if (data.status === 'error') {
                    logArea.value += 'driver1停止失败\n'; // 添加一行日志
                }
            })
            .catch(error => {
                console.error('请求失败:', error);
                // 处理请求失败的情况

                logArea.value += 'driver1停止请求失败\n'; // 添加一行日志
            });
    });
    driver2StopBtn.addEventListener('click', function () {
        const logArea = document.getElementById('driver2LogArea');
        // 调用API
        fetch('/Encrypt/driver2Stop', {
            method: 'GET', // 或者是 'POST', 取决于API的要求
            // 这里可以添加请求头部等信息，如果需要的话
            headers: {
                'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
            },
        })
            .then(response => response.json()) // 处理JSON响应
            .then(data => {
                // 根据返回值更新日志区域

                if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                    logArea.value += 'driver2已停止\n'; // 添加一行日志
                    driver2Marker.setDraggable(true);
                } else if (data.status === 'error') {
                    logArea.value += 'driver2停止失败\n'; // 添加一行日志
                }
            })
            .catch(error => {
                console.error('请求失败:', error);
                // 处理请求失败的情况
                logArea.value += 'driver2停止请求失败\n'; // 添加一行日志
            });
    });

    cusReqBtn.addEventListener('click', function () {
        const logArea = document.getElementById('customerLogArea');
        // 调用API
        fetch('/Encrypt/traceCustomerLogin', {
            method: 'GET', // 或者是 'POST', 取决于API的要求
            // 这里可以添加请求头部等信息，如果需要的话
            headers: {
                'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
            },
        })
            .then(response => response.json()) // 处理JSON响应
            .then(data => {
                // 根据返回值更新日志区域

                if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                    logArea.value += "customer成功发送打车请求\n";
                    logArea.scrollTop = logArea.scrollHeight;

                } else if (data.status === 'error') {
                    logArea.value += "customer发送打车请求失败\n"; // 添加一行日志
                }
            })
            .catch(error => {
                console.error('请求失败:', error);
                // 处理请求失败的情况

                logArea.value += "customer发送打车请求失败\n"; // 添加一行日志
            });
    });

    let pollingInterval; // 移到外面以便控制开始和停止
    let counter = 0; // 计数器也应该在外面以保持状态

    function pollServerForStatus() {
        if (!pollingInterval) { // 确保轮询只被启动一次
            pollingInterval = setInterval(() => {
                fetch('/Encrypt/orderResult')
                    .then(response => response.json())
                    .then(data => {
                        let customerLogArea = document.getElementById('customerLogArea');
                        if (data.status === 'ok') {
                            clearInterval(pollingInterval);
                            pollingInterval = null; // 重置轮询间隔控制变量
                            customerLogArea.value += data.message + "\n";
                        } else if (data.status === "error") {
                            if (counter >= 5) {
                                clearInterval(pollingInterval);
                                pollingInterval = null; // 重置轮询间隔控制变量
                                customerLogArea.value += "寻找司机失败" + "\n";
                            } else {
                                customerLogArea.value += data.message + "\n";
                                counter++;
                            }
                        }
                    })
                    .catch(error => console.error('Error polling server:', error));
            }, 2000);
        }
    }

    cusReqBtn.addEventListener("click", pollServerForStatus);

</script>
</body>

</html>
