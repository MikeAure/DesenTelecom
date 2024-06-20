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

</head>
<body>
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
        document.getElementById("k_anonymity_fileUpload").addEventListener("change", choose_file)

        document.getElementById("CirDummy_submitBtn").addEventListener("click", function () {
            let position = $("#CirDummy_position").val();
            let params = new URLSearchParams(
                {
                    position: $("#CirDummy_position").val(),
                    k: $("#CirDummy_k").val(),
                    s_cd: $("#CirDummy_s_cd").val(),
                    rho: $("#CirDummy_rho").val(),
                }
            )


            if (position === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/Location/CirDummy", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:
                    params
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("CirDummy_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })

        document.getElementById("GridDummy_submitBtn").addEventListener("click", function () {
            let position = $("#GridDummy_position").val();
            let k = $("#GridDummy_k").val();
            let s_cd = $("#GridDummy_s_cd").val();

            if (position === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/Location/GridDummy", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:
                    '&position=' + encodeURIComponent(position) +
                    '&k=' + encodeURIComponent(k) +
                    '&s_cd=' + encodeURIComponent(s_cd)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("GridDummy_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })

        document.getElementById("adaptiveIntervalCloakingWrapper_submitBtn").addEventListener("click", function () {
            let position = $("#adaptiveIntervalCloakingWrapper_position").val();
            let k = $("#adaptiveIntervalCloakingWrapper_k").val();
            let min = $("#adaptiveIntervalCloakingWrapper_min").val();
            let max = $("#adaptiveIntervalCloakingWrapper_max").val();

            if (position === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            let params = new URLSearchParams({
                rawData: position,
                k: k,
                min: min,
                max: max
            });

            fetch("/Location/adaptiveIntervalCloakingWrapper", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: params
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("adaptiveIntervalCloakingWrapper_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })

        document.getElementById("CaDSA_submitBtn").addEventListener("click", function () {
            let op = $("#CaDSA_id").val();
            let position = $("#CaDSA_position").val();

            if (position === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/Location/CaDSA", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:
                    '&rawData=' + encodeURIComponent(position) +
                    '&op=' + encodeURIComponent(op)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("CaDSA_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })

        document.getElementById("K_anonymity_position_submitBtn").addEventListener("click", function () {
            let position = $("#K_anonymity_position").val();
            let k = $("#K_anonymity_position_k").val();

            if (position === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/Location/K_anonymity_position", {
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
                    document.getElementById("K_anonymity_position_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
    }
    choose_file = function (event) {
        // 清空
        document.getElementById("fileInfo").innerHTML = "";
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
                formData.append("sheet", "k_anonymity");
                formData.append("algName", "k_anonymity");
                formData.append("params", document.getElementById("k_anonymity_privacyLevel").value);
                document.getElementById("k_anonymity_submit").onclick = function () {
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
<div class="ibox-title">
</div>

<div class="panel panel-default">
    <div class="panel-heading" style="text-align: center;">
        <h1 class="panel-title"><b style="font-size: 2em">K-匿名</b></h1>
    </div>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                1.K-匿名</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对csv文件进行K-匿名处理
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
                                <input type="file" id="k_anonymity_fileUpload" style="display: none;">
                                <label for="k_anonymity_fileUpload" class="upload-btn">
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
                            <select id="k_anonymity_privacyLevel">
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
            <button type="button" class="btn btn-sm btn-primary" id="k_anonymity_submit"> 提交脱敏</button>
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
    <hr>

    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                2.CirDummy</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：基于包含用户真实位置的虚拟圆产生匿名位置。该算法首先根据匿名区域的面积来随机选取一个满足要求的圆心，
                        然后根据匿名度k来确定每一个扇形的角度，最后根据偏移系数将位于扇形顶点处的匿名位置进行适当的偏移，从而生成了
                        包含k-1个匿名位置和1个真实位置的匿名位置集，并且位置集中位置点的分布近似于圆形
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：经度、纬度 匿名度k、匿名区域的面积s_cd、确定圆环内径的系数rho
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
                                <input type="text" id="CirDummy_position" class="form-control"
                                       placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="CirDummy_k" class="form-control" placeholder="请输入匿名度k"
                                       style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="CirDummy_s_cd" class="form-control" placeholder="匿名区域的面积"
                                       style="font-size: 20px">
                            </div>
                            <div class="input-group">
                                <input type="text" id="CirDummy_rho" class="form-control"
                                       placeholder="请输入圆环内径系数" style="font-size: 20px">
                                <span class="input-group-btn">
                                        <button class="btn btn-default" id="CirDummy_submitBtn" type="button"
                                                style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                </span>
                            </div>

                            <div class="text-center">
                                <label for="CirDummy_outputText"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="CirDummy_outputText" rows="4" cols="100" readonly
                                              style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>

    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                3.GridDummy</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：基于包含用户真实位置的虚拟方格产生虚拟位置。基于覆盖用户位置的虚拟网格生成k个虚拟位置，并返回k个虚拟位置的集合。同时，生成的虚拟方格还需要满足用户预先定义的匿名区域的面积的要求。
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：经度、纬度 匿名度k、匿名区域的面积s_cd
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
                                <input type="text" id="GridDummy_position" class="form-control"
                                       placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="GridDummy_k" class="form-control" placeholder="请输入匿名度k"
                                       style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="GridDummy_s_cd" class="form-control" placeholder="匿名区域的面积"
                                       style="font-size: 20px">
                                <button class="btn btn-default" id="GridDummy_submitBtn" type="button"
                                        style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                    提交脱敏
                                </button>
                            </div>
                        </div>

                        <div class="text-center">
                            <label for="GridDummy_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="GridDummy_outputText" rows="4" cols="100" readonly
                                          style="margin-top: 10px;"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<hr>

<div class="panel-body">
    <div class="row">
        <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
            4.adaptiveIntervalCloakingWrapper</p>
        <div <#--class="col-sm-6"-->
                style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
            <div>
                <p style="font-size: 1.5em;text-align: justify;">
                    说明：简单理解为四分法，由坐标边界可以形成一个包含所有缓存位置信息的矩形区域，然后根据用户的真实位置以及用户设置的最小匿名度，来将矩形区域进行划分。每次将矩形区域划分为四部分，
                    分别为第一象限、第二象限、第三象限和第四象限。首先判断用户的真实位置所在的象限，然后判断该象限中包含的缓存位置信息数量是否大于最小匿名度，若大于，则继续划分；否则，将上一次的划
                    分结果作为最终的矩形区域，输出该区域中所包含的缓存位置信息以及真实的用户位置信息。最终为用户形成一个包含用户数量刚好大于最小匿名度、且不可再分的隐藏区域。
                </p>
                <p style="font-size: 1.5em;text-align: justify;">
                    输入：经度、纬度 匿名度k 矩形区域左下角和右上角坐标
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
                            <input type="text" id="adaptiveIntervalCloakingWrapper_position" class="form-control"
                                   placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                        </div>
                        <div class="input-group" style="margin-top: 10px;">
                            <input type="text" id="adaptiveIntervalCloakingWrapper_k" class="form-control"
                                   placeholder="请输入匿名度k" style="font-size: 20px">
                        </div>
                        <div class="input-group" style="margin-top: 10px;">
                            <input type="text" id="adaptiveIntervalCloakingWrapper_min" class="form-control"
                                   placeholder="矩形左下角坐标" style="font-size: 20px">
                        </div>
                        <div class="input-group" style="margin-top: 10px;">
                            <input type="text" id="adaptiveIntervalCloakingWrapper_max" class="form-control"
                                   placeholder="矩形右上角坐标" style="font-size: 20px">
                            <span class="input-group-btn">
                                        <button class="btn btn-default" id="adaptiveIntervalCloakingWrapper_submitBtn"
                                                type="button"
                                                style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                </span>
                        </div>

                        <div class="text-center">
                            <label for="adaptiveIntervalCloakingWrapper_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="adaptiveIntervalCloakingWrapper_outputText" rows="4" cols="100" readonly
                                          style="margin-top: 10px;"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<hr>

<div class="panel-body">
    <div class="row">
        <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
            5.CaDSA</p>
        <div <#--class="col-sm-6"-->
                style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
            <div>
                <p style="font-size: 1.5em;text-align: justify;">
                    说明：该算法基于缓存选择虚拟位置。该算法首先选择4k个查询概率与虚拟位置Cr最接近的单元格，然后从中随机选出2k个单元格，由此可以为当前查询实现高熵。
                    最后从上述2k个单元格中选出对缓存贡献最大的k-1个单元格，注意当此子集个数较多时，只随机选择S个子集。从而生成了包含k-1个单元格和1个包含真实位置的单元格的匿名位置集。
                </p>
                <p style="font-size: 1.5em;text-align: justify;">
                    输入：经度、纬度、算法类型（1表示CaDSA、2表示enhanced CaDSA）
                </p>
                <p style="font-size: 1.5em;text-align: justify;">
                    输出：经度向量、纬度向量
                </p>
                <p style="font-size: 1.5em;text-align: center;">算法测试</p>

            </div>
            <div class="container">
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5">
                        <!-- 新加的两个文本输入框 -->
                        <div class="input-group" style="margin-top: 10px;">
                            <input type="text" id="CaDSA_id" class="form-control" placeholder="请输入算法类型"
                                   style="font-size: 20px">
                        </div>
                        <#--<div class="input-group" style="margin-top: 10px;">
                            <input type="text" id="mixzone_3_id" class="form-control" placeholder="请输入用户id" style="font-size: 20px">
                        </div>-->
                        <div class="input-group">
                            <input type="text" id="CaDSA_position" class="form-control"
                                   placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                            <span class="input-group-btn">
                                        <button class="btn btn-default" id="CaDSA_submitBtn" type="button"
                                                style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                    </span>
                        </div>


                        <div class="text-center">
                            <label for="CaDSA_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="CaDSA_outputText" rows="4" cols="100" readonly
                                          style="margin-top: 10px;"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<hr>

<div class="panel-body">
    <div class="row">
        <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
            6.K_anonymity_position</p>
        <div <#--class="col-sm-6"-->
                style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
            <div>
                <p style="font-size: 1.5em;text-align: justify;">
                    说明：从预先生成的虚拟位置库中随机选择K-1个匿名位置，与真实位置一起形成K-匿名位置集合。
                </p>
                <p style="font-size: 1.5em;text-align: justify;">
                    输入：经度、纬度 匿名度 k
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
                            <input type="text" id="K_anonymity_position" class="form-control"
                                   placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                        </div>
                        <div class="input-group" style="margin-top: 10px;">
                            <input type="text" id="K_anonymity_position_k" class="form-control"
                                   placeholder="请输入匿名度k" style="font-size: 20px">
                            <span class="input-group-btn">
                                        <button class="btn btn-default" id="K_anonymity_position_submitBtn"
                                                type="button"
                                                style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                </span>
                        </div>

                        <div class="text-center">
                            <label for="K_anonymity_position_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="K_anonymity_position_outputText" rows="4" cols="100" readonly
                                          style="margin-top: 10px;"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<hr>


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
    .upload-btn, #k_anonymity_submit {
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
