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
        document.getElementById("fileUpload").addEventListener("change", choose_file)
    }
    choose_file = function (event){
        // 清空
        document.getElementById("fileInfo").innerHTML = "";
        document.getElementById("table_list").innerHTML = ""
        document.getElementById("table_list2").innerHTML = ""
        //document.getElementById('choose_sheet').selectedIndex = 0;
        let sheet = document.getElementById("choose_sheet").value;
        //读取文件
        const file = event.target.files[0]
        // 文件名，扩展名
        const fileName = file.name;
        const fileExtension = fileName.split('.').pop().toLowerCase();
        if (file) {
            if ("xlsx" === fileExtension) {var fileLoad = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                "<strong>" + fileName + "文件</strong>上传成功"
                "</span>" +
                "</div>";
                document.getElementById("fileInfo").innerHTML = fileLoad
                //console.log(fileExtension)
                //构建formData,发送给后端
                const formData = new FormData();
                formData.append("file", file);
                formData.append("sheet", sheet);
                let html = "";
                console.log("sheet:"+sheet)
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.status == 200 && xhr.readyState == 4) {
                        var data_str = xhr.responseText;
                        var data = JSON.parse(data_str);
                        console.log(data.length)


                        html += "<div class=\"table-responsive\">" +
                            "<table class=\"table table-striped\">" +
                            "<thead>" +
                            "<tr>" +
                            "<th>编号</th>" +
                            "<th>字段缩写</th>" +
                            "<th>字段名</th>" +
                            "<th>字段分类</th>" +
                            "<th>脱敏算法</th>" +
                            "<th>隐私保护等级</th>" +
                            /*"<th id=\"relate_head\">关联字段选择</th>" +*/
                            "</tr>" +
                            "</thead>" +
                            "<tbody id=\"table2\">";
                        for (var i = 0; i < data.length; i++) {
                            s = data[i];
                            //console.log(s)
                            html += "<tr id = " + "row_" + i + ">";
                            html += "<td>" + s.id + "</td>";
                            html += "<td>" + s.fieldName + "</td>";
                            html += "<td>" + s.columnName + "</td>";
                            html += "<td><select value>"
                            html += "<option selected value = " + -1 + ">请选择字段数据类型</option>"
                            switch (s.dataType) {
                                case 0:
                                    html += "<option value = " + 0 + " selected>数值型数据</option>"
                                    html += "<option value = " + 1 + ">单意义编码型数据</option>"
                                    html += "<option value = " + 3 + ">文本型数据</option>"
                                    html += "<option value = " + 4 + ">时间数据</option>"
                                    break;
                                case 1:
                                    html += "<option value = " + 0 + ">数值型数据</option>"
                                    html += "<option value = " + 1 + " selected>单意义编码型数据</option>"
                                    html += "<option value = " + 3 + ">文本型数据</option>"
                                    html += "<option value = " + 4 + ">时间数据</option>"
                                    break;

                                case 3:
                                    html += "<option value = " + 0 + ">数值型数据</option>"
                                    html += "<option value = " + 1 + ">单意义编码型数据</option>"
                                    html += "<option value = " + 3 + " selected>文本型数据</option>"
                                    html += "<option value = " + 4 + ">时间数据</option>"
                                    break;
                                case 4:
                                    html += "<option value = " + 0 + ">数值型数据</option>"
                                    html += "<option value = " + 1 + ">单意义编码型数据</option>"
                                    html += "<option value = " + 3 + ">文本型数据</option>"
                                    html += "<option value = " + 4 + " selected>时间数据</option>"
                                    break;
                            }
                            html += "</select></td>"

                            switch (s.k) {
                                case 0:
                                    if (s.dataType === 0 || s.dataType === 4) {
                                        html += "<td><select>"
                                        html += "<option value = " + 0 + " selected>加噪</option>"
                                        html += "<option value = " + 1 + ">k-匿名</option>"
                                    } else {
                                        html += "<td><select disabled=\"true\">"
                                        html += "<option value = " + 0 + " selected>默认处理</option>"
                                    }
                                    break;
                                case 1:
                                    if (s.dataType === 0 || s.dataType === 4) {
                                        html += "<td><select>"
                                        html += "<option value = " + 0 + ">加噪</option>"
                                        html += "<option value = " + 1 + " selected>k-匿名</option>"
                                    } else {
                                        html += "<td><select disabled=\"true\">"
                                        html += "<option value = " + 0 + " selected>默认处理</option>"
                                    }

                                    break;
                            }
                            html += "</select></td>"

                            html += "<td><select>"
                            html += "<option value = " + -1 + ">请选择隐私保护程度</option>"
                            switch (s.tmParam) {
                                case 0:
                                    html += "<option value = " + 0 + " selected>无隐私保护处理</option>"
                                    html += "<option value = " + 1 + ">低程度</option>"
                                    html += "<option value = " + 2 + ">中程度</option>"
                                    html += "<option value = " + 3 + ">高程度</option>"
                                    break;
                                case 1:
                                    html += "<option value = " + 0 + ">无隐私保护处理</option>"
                                    html += "<option value = " + 1 + " selected>低程度</option>"
                                    html += "<option value = " + 2 + ">中程度</option>"
                                    html += "<option value = " + 3 + ">高程度</option>"
                                    break;
                                case 2:
                                    html += "<option value = " + 0 + ">无隐私保护处理</option>"
                                    html += "<option value = " + 1 + ">低程度</option>"
                                    html += "<option value = " + 2 + " selected>中程度</option>"
                                    html += "<option value = " + 3 + ">高程度</option>"
                                    break;

                                default:
                                    html += "<option value = " + 0 + ">无隐私保护处理</option>"
                                    html += "<option value = " + 1 + ">低程度</option>"
                                    html += "<option value = " + 2 + ">中程度</option>"
                                    html += "<option value = " + 3 + " selected>高程度</option>"
                                    break;
                            }

                            html += "</select></td>"
                            html += "</tr>";
                        }
                        document.getElementById("table_body").innerHTML = html
                    }
                }

                xhr.open("get", sheet + "param/list", false);
                xhr.send();
                document.getElementById("table_body").innerHTML = html;

                //提交脱敏参数，请求脱敏
                document.getElementById("submit").onclick = function () {
                    var tr;
                    var dataArray = new Array();
                    var table_body = document.getElementById("table2")
                    for (var i = 0; i < table_body.rows.length; i++) {
                        data = new Object();
                        tr = table_body.rows[i];
                        //console.log(tr);
                        data.id = tr.childNodes[0].innerHTML;
                        data.fieldName = tr.childNodes[1].innerHTML;
                        data.columnName = tr.childNodes[2].innerHTML;
                        data.dataType = tr.childNodes[3].firstChild.value;
                        data.k = tr.childNodes[4].firstChild.value;
                        data.tmParam = tr.childNodes[5].firstChild.value;
                        dataArray.push(JSON.stringify(data));
                        //console.log(dataArray);
                    }
                    formData.append("params", JSON.stringify(dataArray));
                    console.log(dataArray.length);
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
                                    var headers = jsonData[0];
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
                            downloadLink.download = Date.now().toString() + ".xlsx"; // 下载的文件名
                            downloadLink.click();
                            after.appendChild(downloadLink);
                        })
                        .catch(error => console.error('Error:', error));
                }
            }
            else{
                alert("请提交excel文件")
            }
        }

    }

</script>
<div class="row">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
            </div>
            <div class="midtile">
                <div class="col-sm-5 m-b-xs">
                    <label for="choose_sheet">选择应用场景：</label>
                    <select name="sheet" id="choose_sheet" style=" display: inline-block; height: 30px; font-size: 20px; text-align: center">
                        <option value="111" selected>请选择应用场景</option>
                        <option value="map">地图导航类场景</option>
                        <option value="onlinetaxi">网络约车类场景</option>
                        <option value="communication">即时通信场景</option>
                        <option value="community">网络社区类场景</option>
                        <option value="onlinepayment">网络支付类场景</option>
                        <option value="onlineshopping">网上购物类场景</option>
                        <option value="takeaway">餐饮外卖类场景</option>
                        <option value="express">邮件快件寄递场景</option>
                        <option value="transportationticket">交通票务场景</option>
                        <option value="hotel">酒店服务类场景</option>
                    </select>
                    <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                        <input type="file" id="fileUpload"  style="display: none;">
                        <label for="fileUpload" class="upload-btn">
                            选择文件
                        </label>
                    </form>
                </div>
            </div>
            <!--文件上传信息-->
            <div id = fileInfo>
            </div>
            <div id = "after">

            </div>
            <div class="ibox-content">
                <div id=table_body ></div>
            </div>
            <div class="button1">
                <div class="btn2">
                    <button type="button" class="btn btn-sm btn-primary" id="submit"> 提交脱敏</button>
                </div>
            </div>
            <div id = "showTable">

            </div>
        </div>
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

<div class="wrapper wrapper-content  animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox ">
                    <div class="row row-lg">
                        <div class="col-sm-12">
                            <div class="example-wrap">
                                <div class="example">
                                    <table id="table_list"></table>
                                </div>
                            </div>
                            <div class="example-wrap">
                                <div class="example">
                                    <table id="table_list2"></table>
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

     #submit{
         background-color: #347aa9;
         padding: 5px 20px;
         cursor: pointer;
         color: black;
         font-size: 20px;
         display: inline-block;
         text-align: center;
         /*margin-right: 50px;*/
     }
     .btn2{
         line-height: 30px;
         text-align: center;
         display:flex;
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
    .upload-btn{
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
