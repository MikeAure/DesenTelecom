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
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/GA.css">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
    <link href="${ctx!}/css/GA.css" rel="stylesheet">

    <!-- 全局js -->
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

    <script src="${ctx!}/js/plugins/layer/layer.min.js"></script>
    <script src="${ctx!}/js/multiple-select.min.js"></script>


    <!-- 自定义js -->
    <script src="${ctx!}/js/content.js?v=1.0.0"></script>
    <script type="text/javascript">
        window.onload = function () {
            // 提交
            document.getElementById("t_closeness_fileUpload").addEventListener("change", choose_file)

            document.getElementById("Hilbert_submitBtn").addEventListener("click", function () {
                let position = $("#Hilbert_position").val();
                let k = $("#Hilbert_k").val();

                if (position === "") {
                    alert("请输入文本");
                    return; // Stop further execution if the text input is empty
                }

                fetch("/Location/Hilbert", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body:
                        '&rawData=' + encodeURIComponent(position) +
                        '&k=' + encodeURIComponent(k)
                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("Hilbert_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            })

            document.getElementById("SpaceTwist_submitBtn").addEventListener("click", function () {
                let position = $("#SpaceTwist_position").val();
                let k = $("#SpaceTwist_k").val();
                let poi = $("#SpaceTwist_poi").val();

                if (position === "") {
                    alert("请输入文本");
                    return; // Stop further execution if the text input is empty
                }

                fetch("/Location/SpaceTwist", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body:
                        '&rawData=' + encodeURIComponent(position) +
                        '&k=' + encodeURIComponent(k) +
                        '&poi=' + encodeURIComponent(poi)
                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("SpaceTwist_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            })
        }
        choose_file = function (event) {
            // 清空
            document.getElementById("fileInfo").innerHTML = "";
            document.getElementById("dataTable").innerHTML = "";
            document.getElementById("dataTable1").innerHTML = ""
            document.getElementById("after").innerHTML = "";

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            const fileName = file.name;
            const fileExtension = fileName.split('.').pop().toLowerCase();
            if (file) {
                if ("csv" === fileExtension) {
                    var fileLoad = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                        "<strong>" + fileName + "文件</strong>上传成功"
                    "</span>" +
                    "</div>";
                    document.getElementById("fileInfo").innerHTML = fileLoad
                    //console.log(fileExtension)
                    //构建formData,发送给后端
                    const formData = new FormData();
                    formData.append("file", file);
                    formData.append("sheet", "t_closeness");
                    formData.append("algName", "t_closeness");
                    formData.append("params", document.getElementById("t_closeness_privacyLevel").value);
                    document.getElementById("t_closeness_submit").onclick = function () {
                        fetch('/File/desenFile', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                // 脱敏前
                                var reader = new FileReader();
                                reader.onload = function (e) {
                                    var data = new Uint8Array(e.target.result);
                                    var workbook = XLSX.read(data, {type: 'array'});

                                    var sheetName = workbook.SheetNames[0];
                                    var sheet = workbook.Sheets[sheetName];

                                    var jsonData = XLSX.utils.sheet_to_json(sheet, {header: 1});

                                    var pageSize = 10;
                                    var pageCount = Math.ceil((jsonData.length - 1) / pageSize);
                                    var currentPage = 1;

                                    function displayTable(page) {
                                        var startIndex = (page - 1) * pageSize + 1; // 跳过表头
                                        var endIndex = Math.min(startIndex + pageSize, jsonData.length);

                                        var tableContent = '<thead><tr>';
                                        var headers = ['age', 'work_class', 'fin_weight', 'education', 'edu_num', 'mar_status', 'occupation', 'relaship',
                                            'race', 'gender', 'cap_gain', 'cap_loss', 'hours_pweek', 'country', 'income'];

                                        headers.forEach(function (header) {
                                            tableContent += '<th style=\"white-space: nowrap;\">' + header + '</th>';
                                        });
                                        tableContent += '</tr></thead><tbody>';

                                        for (var i = startIndex; i < endIndex; i++) {
                                            tableContent += '<tr>';
                                            for (var j = 0; j < headers.length; j++) {
                                                var cellValue = (jsonData[i][j] !== undefined) ? jsonData[i][j] : '';
                                                tableContent += '<td>' + cellValue + '</td>';
                                            }
                                            tableContent += '</tr>';
                                        }

                                        tableContent += '</tbody>';

                                        $('#dataTable').html(tableContent);
                                    }

                                    displayTable(currentPage);

                                    function renderPagination() {
                                        var pagination = '<li class="page-item"><a class="page-link" href="#" data-page="prev">Prev</a></li>';
                                        pagination += '<li class="page-item"><a class="page-link" href="#" data-page="next">Next</a></li>';

                                        $('#pagination').html(pagination);

                                        $('#pagination a').click(function (e) {
                                            e.preventDefault();
                                            var page = $(this).data('page');
                                            console.log(page)
                                            if (page === 'prev') {
                                                currentPage = Math.max(1, currentPage - 1);
                                            } else if (page === 'next') {
                                                currentPage = Math.min(pageCount, currentPage + 1);
                                            }
                                            displayTable(currentPage);
                                            renderPagination();
                                        });

                                        $('#totalPages').text(pageCount);
                                    }

                                    $('#paginationContainer').show();
                                    renderPagination();

                                    $('#goToPage').click(function () {
                                        var pageNumber = parseInt($('#pageInput').val());
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
                                const reader1 = new FileReader();
                                reader1.onload = function (event) {
                                    const data = event.target.result;
                                    const workbook = XLSX.read(data, {type: 'binary'});
                                    const sheetName = workbook.SheetNames[0];
                                    const sheet = workbook.Sheets[sheetName];
                                    const jsonData = XLSX.utils.sheet_to_json(sheet, {header: 1});
                                    var pageSize = 10;
                                    var pageCount = Math.ceil((jsonData.length - 1) / pageSize);
                                    var currentPage1 = 1;

                                    function displayTable1(page1) {
                                        var startIndex1 = (page1 - 1) * pageSize + 1; // 跳过表头
                                        var endIndex = Math.min(startIndex1 + pageSize, jsonData.length);

                                        var tableContent1 = '<thead><tr>';
                                        var headers1 = jsonData[0];
                                        headers1.forEach(function (header1) {
                                            tableContent1 += '<th style=\"white-space: nowrap;\">' + header1 + '</th>';
                                        });
                                        tableContent1 += '</tr></thead><tbody>';

                                        for (var i = startIndex1; i < endIndex; i++) {
                                            tableContent1 += '<tr>';
                                            for (var j = 0; j < headers1.length; j++) {
                                                var cellValue = (jsonData[i][j] !== undefined) ? jsonData[i][j] : '';
                                                tableContent1 += '<td>' + cellValue + '</td>';
                                            }
                                            tableContent1 += '</tr>';
                                        }

                                        tableContent1 += '</tbody>';

                                        $('#dataTable1').html(tableContent1);
                                    }

                                    displayTable1(currentPage1);

                                    function renderPagination1() {
                                        var pagination1 = '<li class="page-item"><a class="page-link" href="#" data-page="prev1">Prev</a></li>';
                                        pagination1 += '<li class="page-item"><a class="page-link" href="#" data-page="next1">Next</a></li>';

                                        $('#pagination1').html(pagination1);

                                        $('#pagination1 a').click(function (e) {
                                            e.preventDefault();
                                            var page = $(this).data('page');
                                            console.log(page)
                                            if (page === 'prev1') {
                                                currentPage1 = Math.max(1, currentPage1 - 1);
                                            } else if (page === 'next1') {
                                                currentPage1 = Math.min(pageCount, currentPage1 + 1);
                                            }
                                            displayTable1(currentPage1);
                                            renderPagination1();
                                        });

                                        $('#totalPages1').text(pageCount);
                                    }

                                    $('#paginationContainer1').show();
                                    renderPagination1();

                                    $('#goToPage1').click(function () {
                                        var pageNumber1 = parseInt($('#pageInput1').val());
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
                                downloadLink.download = Date.now().toString() + ".csv"; // 下载的文件名
                                downloadLink.click();
                                var after = document.getElementById("after");
                                after.appendChild(downloadLink);
                            })
                            .catch(error => console.error('Error:', error));
                    }
                } else {
                    alert("请提交csv文件")
                }
            }

        }

    </script>

</head>
<body>

<div class="panel panel-default">
<#--    <div class="panel-heading" style="text-align: center;">-->
<#--        <h1 class="panel-title"><b style="font-size: 2em">t-接近性</b></h1>-->
<#--    </div>-->

    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                1.t-接近性</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对csv文件进行t-接近性处理
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：csv文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：csv文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="<#--col-sm-5 m-b-xs d-flex--> align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="t_closeness_fileUpload" style="display: none;">
                                <label for="t_closeness_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <!--文件上传信息-->
                    <div id="fileInfo">
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="t_closeness_privacyLevel">
                                <option value="0"> 低程度</option>
                                <option value="1" selected> 中程度</option>
                                <option value="2"> 高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="t_closeness_submit"> 提交脱敏</button>
        </div>
        <div id="after">

        </div>

    </div>
    <div class="container mt-5">
        <div id="dataTableContainer">
            <table id="dataTable" class="table table-bordered">
                <!-- 这里将用 JavaScript 动态创建表格内容 -->
            </table>
        </div>
        <div id="paginationContainer" class="mt-3" style="display: none;">
            <nav>
                <div id="paginationInfo" class="d-flex justify-content-between align-items-center">
                    <ul class="pagination mb-0" id="pagination"></ul>
                    <div class="form-group mb-0 text-center">
                        <label for="pageInput">跳转至：</label>
                        <input type="number" class="form-control" id="pageInput" min="1">
                        <button class="btn btn-primary mt-2" id="goToPage">跳转</button>
                    </div>
                    <div id="totalPages"></div>
                </div>
            </nav>
        </div>
        <div id="dataTableContainer1">
            <table id="dataTable1" class="table table-bordered">
                <!-- 这里将用 JavaScript 动态创建表格内容 -->
            </table>
        </div>
        <div id="paginationContainer1" class="mt-3" style="display: none;">
            <nav>
                <div id="paginationInfo1" class="d-flex justify-content-between align-items-center">
                    <ul class="pagination mb-0" id="pagination1"></ul>
                    <div class="form-group mb-0 text-center">
                        <label for="pageInput1">跳转至：</label>
                        <input type="number" class="form-control" id="pageInput1" min="1">
                        <button class="btn btn-primary mt-2" id="goToPage1">跳转</button>
                    </div>
                    <div id="totalPages1"></div>
                </div>
            </nav>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                2.Hilbert</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将用户的真实位置转换为Hilbert曲线上的值，然后找到Hilbert曲线上相邻的K个点，转换为真实坐标后输出。
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：经度、纬度
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：坐标数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <!-- 新加的两个文本输入框 -->
                            <#--  <div class="input-group" style="margin-top: 10px;">
                                  <input type="text" id="mixzone_3_position" class="form-control" placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                              </div>
                              <div class="input-group" style="margin-top: 10px;">
                                  <input type="text" id="mixzone_3_id" class="form-control" placeholder="请输入用户id" style="font-size: 20px">
                              </div>-->
                            <div class="input-group">
                                <input type="text" id="Hilbert_position" class="form-control"
                                       placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="Hilbert_k" class="form-control" placeholder="请输入匿名度k"
                                       style="font-size: 20px">
                                <span class="input-group-btn">
                                        <button class="btn btn-default" id="Hilbert_submitBtn" type="button"
                                                style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                </span>
                            </div>

                            <div class="text-center">
                                <label for="Hilbert_outputText"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="Hilbert_outputText" rows="4" cols="100" readonly
                                              style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                3.SpaceTwist</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：用于返回距离用户最近的k个POI，并且不暴露用户的真实位置。该算法可以概括为：首先生成一个虚拟位置qfake，将所有检索点按照距离qfake从近到远排序；
                        然后将排序后的检索点中的前k个放进结果集中，遍历后续所有点，若与真实位置q间的距离小于结果集中与q距离最大的点，则替换。结束条件为检索点集合遍历完毕或者
                        当前点到真实位置q的距离大于distance(q,qfake)与结果集中的点与q的最远距离之和。
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：经度、纬度
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：坐标数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="SpaceTwist_position" class="form-control"
                                       placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="SpaceTwist_k" class="form-control" placeholder="请输入匿名度k"
                                       style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="SpaceTwist_poi" class="form-control"
                                       placeholder="请输入poi: x1,y1;x2,y2;..." style="font-size: 20px">
                                <span class="input-group-btn">
                                        <button class="btn btn-default" id="SpaceTwist_submitBtn" type="button"
                                                style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                </span>
                            </div>

                            <div class="text-center">
                                <label for="SpaceTwist_outputText"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="SpaceTwist_outputText" rows="4" cols="100" readonly
                                              style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>


</body>
<style>
    /* 设置表格样式 */
    #dataTableContainer {
        width: 100%;
        overflow-x: auto;
    }

    #dataTable {
        width: max-content;
        margin: 0 auto;
    }

    #paginationInfo {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    #paginationInfo input {
        width: 5em;
        text-align: center;
    }

    /* 设置表格样式 */
    #dataTableContainer1 {
        width: 100%;
        overflow-x: auto;
    }

    #dataTable1 {
        width: max-content;
        margin: 0 auto;
    }

    #paginationInfo1 {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    #paginationInfo1 input {
        width: 5em;
        text-align: center;
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

    /*选择框居中*/
    .midtile {
        line-height: 30px;
        text-align: center;
        display: flex;
        justify-content: center;
    }

    /*上传按钮*/
    .upload-btn, #t_closeness_submit {
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }
</style>
</html>
