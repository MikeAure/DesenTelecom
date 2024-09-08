<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">

    <title>Performance Test</title>

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

        /*.ibox-title {*/
        /*    height: 200px;*/
        /*    border-color: #edf1f2;*/
        /*    background-color: #dbeafe;*/
        /*    color: black;*/
        /*    display: flex;*/
        /*    align-items: center;*/
        /*    justify-content: center;*/
        /*}*/

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

        .performance-test button,
        .performance-test label,
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
                    // distortion_alg_list.options.add(new Option("基于高斯机制差分隐私的数值加噪算法", "gaussianToValue"));
                    distortion_alg_list.options.add(new Option("基于拉普拉斯差分隐私的数值加噪算法", "laplaceToValue"));
                    distortion_alg_list.options.add(new Option("基于随机均匀噪声的数值加噪算法", "randomUniformToValue"));
                    distortion_alg_list.options.add(new Option("基于随机拉普拉斯噪声的数值加噪算法", "randomLaplaceToValue"));
                    distortion_alg_list.options.add(new Option("基于随机高斯噪声的数值加噪算法", "randomGaussianToValue"));
                    distortion_alg_list.options.add(new Option("数值偏移", "valueShift"));
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
            // document.getElementById("fileUpload").addEventListener("change", choose_file);

            // 生成非失真脱敏的测试文件
            document.getElementById('generateTestData').addEventListener('click', function () {
                let fileName = Date.now() + "testData.txt";
                fetch('/Encrypt/generateTestData')
                    .then(response => response.json())
                    .then(data => {
                        if (data.status === 'ok') {
                            const message = data.message;
                            downloadTestFile(message, fileName, 'text/plain');
                        } else {
                            console.error('Failed to generate test data:', data.message);
                        }
                    })
                    .catch(error => console.error('Error:', error));
            });

            document.getElementById('generateDistortionTestData').addEventListener('click', function () {
                let fileName = Date.now() + "testData.txt";
                let totalNumber = 500000;
                fetch('/File/generateTextTestData?totalNumber=' + totalNumber)
                    .then(response => {
                        if (response.status === 500) {
                            // Handle server error
                            return response.text().then(failedMsg => {
                                alert(failedMsg);
                                throw new Error(failedMsg); // Throw an error to stop further processing
                            });
                        }
                        // console.log(response.headers.get('Content-Disposition'));
                        // let fileName = response.headers.get('Content-Disposition').split('filename=')[1].split(';')[0];
                        // fileName = fileName.replaceAll('"', '')
                        // console.log(fileName);
                        // return response.blob().then(blob => ({ blob, fileName }));  // If the status is not 500, proceed to handle the file blob
                        return response.blob();
                    })
                    .then(blob => {
                        const url = URL.createObjectURL(blob);

                        // 创建一个隐藏的a标签并点击它来触发下载
                        const a = document.createElement('a');
                        a.style.display = 'none';
                        a.href = url;
                        a.download = fileName;

                        // 将a标签添加到DOM并触发点击
                        document.body.appendChild(a);
                        a.click();

                        // 下载完成后移除a标签和URL对象
                        document.body.removeChild(a);
                        URL.revokeObjectURL(url);
                    })
                    .catch(error => console.error('Error:', error));
            });

            // 获取失真脱敏的测试文件
            document.getElementById('selectDistortionTestData').addEventListener('change', function (event) {
                const file = event.target.files[0];
                if (file && file.type === 'text/plain') {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        // const textArea = document.getElementById('testData');
                        // textArea.value = e.target.result;
                        document.getElementById('distortionTestFileName').textContent = file.name + '已选择';
                    };
                    reader.readAsText(file);
                } else {
                    alert('请选择一个txt格式的文件');
                }
            });

            document.getElementById('startDistortionPerformanceTest').addEventListener('click', function () {
                const fileInput = document.getElementById('selectDistortionTestData');
                const file = fileInput.files[0];
                let formData = new FormData();
                let privacyLevel = document.getElementById("table3-selections").value;
                // 算法名
                let algName = document.getElementById("distortionAlg").value;
                // formData.append("params", privacyLevel)
                // 保证params字段唯一值唯一
                formData.set("params", privacyLevel);
                formData.set("algName", algName);
                formData.set("file", file);

                if (!file) {
                    alert('请先选择一个测试文件。');
                    return;
                }
                fetch('/File/textFilePerformenceTest', {
                    method: 'POST',
                    body: formData,
                })
                    .then(response => response.blob())
                    .then(blob => {
                        const fileName = Date.now() + 'desenResult.txt';
                        const fileSizeInBytes = blob.size;
                        const fileSizeInMegabytes = fileSizeInBytes / (1024 * 1024);

                        // 输出文件大小（以字节为单位）
                        console.log("File size: " + fileSizeInBytes + " bytes");
                        console.log("File size: " + fileSizeInMegabytes.toFixed(2) + " MB");

                        const url = URL.createObjectURL(blob);

                        // 创建一个隐藏的a标签并点击它来触发下载
                        const a = document.createElement('a');
                        a.style.display = 'none';
                        a.href = url;
                        a.download = fileName;

                        // 将a标签添加到DOM并触发点击
                        document.body.appendChild(a);
                        a.click();

                        // 下载完成后移除a标签和URL对象
                        document.body.removeChild(a);
                        URL.revokeObjectURL(url);

                    })
                    .catch(error => console.error('Error:', error));
            });

            function downloadTestFile(content, fileName, contentType) {
                const blob = new Blob([content], {type: contentType});
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = fileName;
                document.body.appendChild(a);
                a.click();
                setTimeout(() => {
                    document.body.removeChild(a);
                    window.URL.revokeObjectURL(url);
                }, 0);
            }

            // 获取非失真脱敏的测试文件
            document.getElementById('selectTestData').addEventListener('change', function (event) {
                const file = event.target.files[0];
                if (file && file.type === 'text/plain') {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        const textArea = document.getElementById('testData');
                        textArea.value = e.target.result;
                        document.getElementById('fileName').textContent = file.name + '已选择';
                    };
                    reader.readAsText(file);
                } else {
                    alert('请选择一个txt格式的文件');
                }
            });

            document.getElementById('startPerformanceTest').addEventListener('click', function () {
                const fileInput = document.getElementById('selectTestData');
                const file = fileInput.files[0];

                if (!file) {
                    alert('请先选择一个测试文件。');
                    return;
                }

                const reader = new FileReader();
                reader.onload = function (e) {
                    const content = e.target.result.trim();
                    const points = content.split('\n').map(line => line.split(' ').map(Number));

                    fetch('/Encrypt/performenceTest', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json;charset=UTF-8'
                        },
                        body: JSON.stringify(points)
                    })
                        .then(response => response.blob())
                        .then(blob => {
                            const fileSizeInBytes = blob.size;
                            const fileSizeInMegabytes = fileSizeInBytes / (1024 * 1024);

                            // 输出文件大小（以字节为单位）
                            console.log("File size: " + fileSizeInBytes + " bytes");
                            console.log("File size: " + fileSizeInBytes.toFixed(2) + " MB");

                            // 判断文件大小以执行不同的操作
                            if (fileSizeInMegabytes > 100) {
                                // 如果文件大于100M，仅保存文件内容，不显示
                                alert('文件过大 (>100MB)，仅保存文件内容，不显示。');
                                window.downloadBlob = blob;
                            } else if (fileSizeInMegabytes > 10) {
                                // 如果文件大于10M但小于等于100M，只显示前100行
                                const fileReader = new FileReader();
                                fileReader.onload = function (event) {
                                    const content = event.target.result;
                                    const lines = content.split('\n');
                                    const first100Lines = lines.slice(0, 100).join('\n');
                                    document.getElementById('desenTestData').value = first100Lines;

                                    // 保存blob用于下载
                                    window.downloadBlob = blob;
                                };
                                fileReader.readAsText(blob);
                            } else {
                                // 文件小于等于10M，完整显示内容（或者根据需求调整显示方式）
                                const fileReader = new FileReader();
                                fileReader.onload = function (event) {
                                    document.getElementById('desenTestData').value = event.target.result;
                                    // 保存blob用于下载
                                    window.downloadBlob = blob;
                                };
                                fileReader.readAsText(blob);
                            }
                        })
                        .catch(error => console.error('Error:', error));
                };
                reader.readAsText(file);
            });

            document.getElementById('downloadTestResult').addEventListener('click', function () {
                if (window.downloadBlob) {
                    const url = window.URL.createObjectURL(window.downloadBlob);
                    const a = document.createElement('a');
                    a.style.display = 'none';
                    a.href = url;
                    a.download = 'result.txt';
                    document.body.appendChild(a);
                    a.click();
                    setTimeout(() => {
                        document.body.removeChild(a);
                        window.URL.revokeObjectURL(url);
                    }, 0);
                } else {
                    alert('请先进行性能测试以生成结果文件。');
                }
            });

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
                        "<strong>" + fileName + "文件</strong>上传成功" + "</span>" + "</div>";
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
                            let selections = document.getElementById("distortionAlg");
                            alg_name = selections.value;
                            //formData.append("sheet", type1);
                        }

                        // 发送算法名、隐私保护等级、脱敏文件
                        formData.set("algName", alg_name)
                        fetch('/File/desenSingleColumn', {
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

                                        $('#pagination a').off("click").on("click", function (e) {
                                            e.preventDefault();
                                            let page = $(this).data('page');
                                            console.log(page)
                                            if (page === 'prev') {
                                                currentPage = Math.max(1, currentPage - 1);
                                            } else if (page === 'next') {
                                                currentPage = Math.min(pageCount, currentPage + 1);
                                            }
                                            displayTable(currentPage);
                                            $("#totalPages").text(currentPage + "/" + pageCount);
                                        });

                                        let info = "共" + pageCount + "页"
                                        console.log(info)
                                        $("#totalPages").text(currentPage + "/" + pageCount);
                                    }

                                    $('#paginationContainer').show();
                                    renderPagination();

                                    $('#goToPage').off("click").on("click", function () {
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

                                        $('#pagination1 a').off("click").on("click", function (e) {
                                            e.preventDefault();
                                            let page = $(this).data('page');
                                            console.log(page)
                                            if (page === 'prev1') {
                                                currentPage1 = Math.max(1, currentPage1 - 1);
                                            } else if (page === 'next1') {
                                                currentPage1 = Math.min(pageCount, currentPage1 + 1);
                                            }
                                            displayTable1(currentPage1);
                                            $("#totalPages1").text(currentPage1 + "/" + pageCount);
                                        });

                                        let info = "共" + pageCount + "页"
                                        console.log(info)
                                        $("#totalPages1").text(currentPage1 + "/" + pageCount);
                                    }

                                    $('#paginationContainer1').show();
                                    renderPagination1();

                                    $('#goToPage1').off("click").on("click", function () {
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
                                    <option value="value" selected>数值</option>
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
                                                <option value="laplaceToValue" selected>
                                                    基于拉普拉斯差分隐私的数值加噪算法
                                                </option>
                                                <option value="randomUniformToValue">基于随机均匀噪声的数值加噪算法
                                                </option>
                                                <option value="randomLaplaceToValue">
                                                    基于随机拉普拉斯噪声的数值加噪算法
                                                </option>
                                                <option value="randomGaussianToValue">基于随机高斯噪声的数值加噪算法
                                                </option>
                                                <option value="valueShift">数值偏移</option>
                                                <option value="SHA512">假名化-哈希</option>
                                                <option value="floor">数值取整</option>
                                                <option value="valueMapping">数值映射</option>
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

                            <#--                            <div id="fileInfo"></div>-->

                            <#--                            <div class="btn1">-->
                            <#--                                <input type="file" accept=".xlsx" id="fileUpload" style="display: none;">-->
                            <#--                                <label for="fileUpload" class="btn btn-sm btn-primary upload-btn">-->
                            <#--                                    选择文件-->
                            <#--                                </label>-->
                            <#--                            </div>-->

                            <#--                            <div class="btn2">-->
                            <#--                                <button type="button" class="btn btn-sm btn-primary" id="submitBtn">提交脱敏-->
                            <#--                                </button>-->
                            <#--                            </div>-->

                            <div class="row m-t">
                                <p>
                                    <button type="button" id="generateDistortionTestData"
                                            class="btn btn-sm btn-primary">
                                        生成测试文件
                                    </button>
                                    <input type="file" accept=".txt" id="selectDistortionTestData"
                                           style="display: none;">
                                    <label for="selectDistortionTestData" class="btn btn-sm btn-primary">
                                        选择测试文件
                                    </label>
                                    <button type="button" id="startDistortionPerformanceTest"
                                            class="btn btn-sm btn-primary"> 进行性能测试
                                    </button>
                                </p>
                                <p id="distortionTestFileName" class="text-success"></p>
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
                            <div id="nondistortion-module-container" class="tab-pane">
                                <div class="performance-test">
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="ibox">
                                                <div class="ibox-title float-e-margins">
                                                    <h5>待测试数据</h5>
                                                </div>
                                            </div>
                                            <div class="ibox-content">
                                                    <textarea id="testData" rows="18" class="col-sm-12"
                                                              readonly></textarea>
                                            </div>
                                        </div>

                                        <div class="col-sm-6">

                                            <div class="ibox">
                                                <div class="ibox-title float-e-margins">
                                                    <h5>脱敏后的数据</h5>
                                                </div>

                                            </div>
                                            <div class="ibox-content">
                                                <textarea id="desenTestData" rows="18" class="col-sm-12"
                                                          readonly></textarea>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row m-t">
                                        <p>
                                            <button type="button" id="generateTestData"
                                                    class="btn btn-sm btn-primary">
                                                生成测试文件
                                            </button>
                                            <input type="file" accept=".txt" id="selectTestData"
                                                   style="display: none;">
                                            <label for="selectTestData" class="btn btn-sm btn-primary">
                                                选择测试文件
                                            </label>
                                            <button type="button" id="startPerformanceTest"
                                                    class="btn btn-sm btn-primary"> 进行性能测试
                                            </button>
                                            <button type="button" id="downloadTestResult"
                                                    class="btn btn-sm btn-primary"> 下载测试结果
                                            </button>

                                        </p>
                                        <p id="fileName" class="text-success"></p>
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
</body>

</html>
