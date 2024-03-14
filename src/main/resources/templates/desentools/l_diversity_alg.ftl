<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="shortcut icon" href="favicon.ico"> <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
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
        document.getElementById("l_diversity_fileUpload").addEventListener("change", choose_file)
    }
    choose_file = function (event){
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
                formData.append("sheet", "l_diversity");
                formData.append("algName", "l_diversity");
                formData.append("params", document.getElementById("l_diversity_privacyLevel").value);
                document.getElementById("l_diversity_submit").onclick = function (){
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
                                var workbook = XLSX.read(data, { type: 'array' });

                                var sheetName = workbook.SheetNames[0];
                                var sheet = workbook.Sheets[sheetName];

                                var jsonData = XLSX.utils.sheet_to_json(sheet, { header: 1 });

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
                            reader1.onload = function(event) {
                                const data = event.target.result;
                                const workbook = XLSX.read(data, { type: 'binary' });
                                const sheetName = workbook.SheetNames[0];
                                const sheet = workbook.Sheets[sheetName];
                                const jsonData = XLSX.utils.sheet_to_json(sheet, { header: 1 });
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
            }
            else{
                alert("请提交csv文件")
            }
        }

    }

</script>
<div class="ibox-title">
</div>

<div class="panel panel-default">
    <div class="panel-heading"  style="text-align: center;">
        <h1 class="panel-title"><b style="font-size: 2em">l-多样性</b></h1>
    </div>

    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">1.l-多样性</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对csv文件进行l-多样性处理
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
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="l_diversity_fileUpload"  style="display: none;">
                                <label for="l_diversity_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <!--文件上传信息-->
                    <div id = "fileInfo">
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="l_diversity_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="l_diversity_submit"> 提交脱敏</button>
        </div>
        <div id = "after">

        </div>

    </div>
    <hr>
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
    .midtile{
        line-height: 30px;
        text-align: center;
        display:flex;
        justify-content: center;
    }
    /*上传按钮*/
    .upload-btn, #l_diversity_submit{
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
