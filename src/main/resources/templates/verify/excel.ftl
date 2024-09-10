<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Insert title here</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet"/>
    <link href="${ctx!}/css/font-awesome.css?v=4.4.0" rel="stylesheet"/>
    <link href="${ctx!}/css/plugins/iCheck/custom.css" rel="stylesheet"/>
    <link href="${ctx!}/css/animate.css" rel="stylesheet"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="${ctx!}/css/multiple-select.min.css"
    />
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet"/>
    <link href="${ctx!}/css/plugins/chosen/chosen.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/GA.css"/>
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet"/>
    <link
            href="${ctx!}/css/plugins/bootstrap-table/bootstrap-table.min.css"
            rel="stylesheet"
    />
    <link href="${ctx!}/css/GA.css" rel="stylesheet"/>
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
    <#-- 用于处理csv-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/PapaParse/5.3.0/papaparse.min.js"></script>

    <!-- 自定义js -->
    <script src="${ctx!}/js/content.js?v=1.0.0"></script>
    <script type="text/javascript">
        let defaultOption = "onlinepayment_low";
        let sheet = "";
        let suffix = "";
        let sceneName = "";
        document.addEventListener('DOMContentLoaded', function () {
            let selecteTemplate = document.getElementById("choose_template_sheet");
            let selectTranferScene = document.getElementById("choose_transfer_scene");
            fetch('/toolset/getDefaultSelection?toolsetName=' + 'excel')
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Failed to fetch');
                    }
                })
                .then(data => {
                    if (data.code === 200 && data.message) {
                        defaultOption = data.data;
                        console.log("defaultOption: " + defaultOption);
                        setDefaultOption(defaultOption)

                    } else {
                        setDefaultOption(defaultOption);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert("获取默认算法失败");
                    setDefaultOption(defaultOption);
                });


        });

        function setDefaultOption(value) {
            let selecteTemplate = document.getElementById("choose_template_sheet");
            let selectTranferScene = document.getElementById("choose_transfer_scene");
            const parts = value.split("_");
            const templateName = parts.slice(0, -1).join("_");
            console.log("sceneName: " + templateName);
            const transferScene = parts.slice(-1);
            console.log("transferScene: " + transferScene);

            selecteTemplate.value = templateName;
            selectTranferScene.value = transferScene;
            sheet = templateName;
            suffix = "_" + transferScene;
            sceneName = sheet + suffix;
        }

        window.onload = function () {
            let selectedFile; // 全局变量，用于存储选择的文件
            let currentSceneValue = document.getElementById(
                "choose_transfer_scene"
            ).value;

            document
                .getElementById("choose_transfer_scene")
                .addEventListener("change", function () {
                    currentSceneValue = this.value;
                    console.log("Selected value:", currentSceneValue);

                    switch (currentSceneValue) {
                        case "low":
                            suffix = "_low";
                            break;
                        case "medium":
                            suffix = "_medium";
                            break;
                        case "high":
                            suffix = "_high";
                            break;
                        default:
                            suffix = "";
                            break;
                    }

                    sceneName = sheet + suffix;
                    console.log(suffix);
                    console.log("Current scene name: " + sceneName);
                });

            // 在切换选项卡之后保持非失真算法的默认选项不变
            $('a[data-toggle="tab"]').on("shown.bs.tab", function (e) {
                console.log("defaultOption: " + defaultOption);
                setDefaultOption(defaultOption);
            });

            document
                .getElementById("choose_template_sheet")
                .addEventListener("change", function () {
                    sheet = this.value;
                    sceneName = sheet + suffix;
                    console.log("Selected template:", sheet);
                    console.log("Current scene name: " + sceneName);
                });

            document
                .getElementById("showTemplate")
                .addEventListener("click", function () {
                    // 清空
                    // document.getElementById("fileInfo").innerHTML = "";
                    // document.getElementById("table_list").innerHTML = ""
                    // document.getElementById("table_list2").innerHTML = ""

                    if (sheet === "111") {
                        alert("请选择有效的场景");
                        return;
                    }
                    document.getElementById("table_body").innerHTML = "";
                    // 拼接html
                    let html = "";

                    console.log("sheet:" + sheet);
                    let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function () {
                        if (xhr.status === 200 && xhr.readyState === 4) {
                            let data_str = xhr.responseText;
                            let data = JSON.parse(data_str);
                            console.log(data.length);

                            html +=
                                '<div class="table-responsive">' +
                                '<table class="table table-striped">' +
                                "<thead>" +
                                "<tr>" +
                                "<th>编号</th>" +
                                "<th>字段缩写</th>" +
                                "<th>字段名</th>" +
                                "<th>字段类型</th>" +
                                /*  "<th>脱敏算法</th>" +
                                          "<th>隐私保护等级</th>" +*/
                                "</tr>" +
                                "</thead>" +
                                '<tbody id="table2">';
                            for (let i = 0; i < data.length; i++) {
                                s = data[i];
                                //console.log(s)
                                html += "<tr id = " + "row_" + i + ">";
                                html += "<td>" + s.id + "</td>";
                                html += "<td>" + s.fieldName + "</td>";
                                html += "<td>" + s.columnName + "</td>";
                                html += "<td>";
                                switch (s.dataType) {
                                    case 0:
                                        html += "数值型数据";
                                        break;
                                    case 1:
                                        html += "编码型数据";
                                        break;
                                    case 3:
                                        html += "文本型数据";
                                        break;
                                    case 4:
                                        html += "时间数据";
                                        break;
                                }
                                html += "</td>";
                                html += "</tr>";
                            }
                            document.getElementById("table_body").innerHTML = html;
                        }
                    };

                    console.log("sheet: " + sheet + suffix);
                    xhr.open("get", "/" + sheet + suffix + "param/list", false);
                    xhr.send();
                    document.getElementById("table_body").innerHTML = html;
                });
            document
                .getElementById("setTemplate")
                .addEventListener("click", function () {
                    // 清空
                    // document.getElementById("fileInfo").innerHTML = "";
                    // document.getElementById("table_list").innerHTML = "";
                    // document.getElementById("table_list2").innerHTML = "";
                    sheet = document.getElementById("choose_template_sheet").value;
                    document.getElementById("table_body").innerHTML = "";
                    // 拼接html
                    let html = "";
                    console.log("Set Template sheet: " + sheet + suffix);

                    let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function () {
                        if (xhr.status === 200 && xhr.readyState === 4) {
                            let data_str = xhr.responseText;
                            let data = JSON.parse(data_str);
                            console.log(data.length);

                            html +=
                                '<div class="table-responsive">' +
                                '<table class="table table-striped">' +
                                "<thead>" +
                                "<tr>" +
                                "<th>编号</th>" +
                                "<th>字段缩写</th>" +
                                "<th>字段名</th>" +
                                "<th>字段类型</th>" +
                                "<th>脱敏算法</th>" +
                                "<th>隐私保护等级</th>" +
                                "</tr>" +
                                "</thead>" +
                                '<tbody id="table2">';
                            for (let i = 0; i < data.length; i++) {
                                s = data[i];
                                console.log(s);
                                html += "<tr id = " + "row_" + i + ">";
                                html += "<td>" + s.id + "</td>";
                                html += "<td>" + s.fieldName + "</td>";
                                html += "<td>" + s.columnName + "</td>";
                                html += "<td><select>";
                                /*html += "<option selected value = " + -1 + ">请选择字段数据类型</option>"*/
                                switch (s.dataType) {
                                    case 0:
                                        html +=
                                            "<option value = " +
                                            0 +
                                            " selected>数值型数据</option>";
                                        /*html += "<option value = " + 1 + ">编码型数据</option>"
                                                    html += "<option value = " + 3 + ">文本型数据</option>"
                                                    html += "<option value = " + 4 + ">时间数据</option>"*/
                                        break;
                                    case 1:
                                        /*html += "<option value = " + 0 + ">数值型数据</option>"*/
                                        html +=
                                            "<option value = " +
                                            1 +
                                            " selected>编码型数据</option>";
                                        /*html += "<option value = " + 3 + ">文本型数据</option>"
                                                    html += "<option value = " + 4 + ">时间数据</option>"*/
                                        break;

                                    case 3:
                                        /* html += "<option value = " + 0 + ">数值型数据</option>"
                                                     html += "<option value = " + 1 + ">编码型数据</option>"*/
                                        html +=
                                            "<option value = " +
                                            3 +
                                            " selected>文本型数据</option>";
                                        /*html += "<option value = " + 4 + ">时间数据</option>"*/
                                        break;
                                    case 4:
                                        /* html += "<option value = " + 0 + ">数值型数据</option>"
                                                     html += "<option value = " + 1 + ">编码型数据</option>"
                                                     html += "<option value = " + 3 + ">文本型数据</option>"*/
                                        html +=
                                            "<option value = " + 4 + " selected>时间数据</option>";
                                        break;
                                }
                                html += "</select></td>";

                                //算法
                                html += "<td><select>";
                                switch (s.dataType) {
                                    case 0:
                                        switch (s.k) {
                                            case 3:
                                                html +=
                                                    "<option value = " +
                                                    3 +
                                                    " selected>基于拉普拉斯差分隐私的数值加噪算法</option>";
                                                // html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                                html +=
                                                    "<option value = " +
                                                    5 +
                                                    ">基于随机均匀噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    6 +
                                                    ">基于随机拉普拉斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    7 +
                                                    ">基于随机高斯噪声的数值加噪算法</option>";
                                                html += "<option value = " + 8 + ">数值偏移</option>";
                                                html += "<option value = " + 9 + ">数值取整</option>";
                                                html +=
                                                    "<option value = " + 10 + ">数值映射</option>";
                                                break;
                                            case 4:
                                                html +=
                                                    "<option value = " +
                                                    3 +
                                                    " selected>基于拉普拉斯差分隐私的数值加噪算法</option>";
                                                // html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                                html +=
                                                    "<option value = " +
                                                    5 +
                                                    ">基于随机均匀噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    6 +
                                                    ">基于随机拉普拉斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    7 +
                                                    ">基于随机高斯噪声的数值加噪算法</option>";
                                                html += "<option value = " + 8 + ">数值偏移</option>";
                                                html += "<option value = " + 9 + ">数值取整</option>";
                                                html +=
                                                    "<option value = " + 10 + ">数值映射</option>";
                                                break;
                                            case 5:
                                                html +=
                                                    "<option value = " +
                                                    3 +
                                                    ">基于拉普拉斯差分隐私的数值加噪算法</option>";
                                                // html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                                html +=
                                                    "<option value = " +
                                                    5 +
                                                    " selected>基于随机均匀噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    6 +
                                                    ">基于随机拉普拉斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    7 +
                                                    ">基于随机高斯噪声的数值加噪算法</option>";
                                                html += "<option value = " + 8 + ">数值偏移</option>";
                                                html += "<option value = " + 9 + ">数值取整</option>";
                                                html +=
                                                    "<option value = " + 10 + ">数值映射</option>";
                                                break;
                                            case 6:
                                                html +=
                                                    "<option value = " +
                                                    3 +
                                                    ">基于拉普拉斯差分隐私的数值加噪算法</option>";
                                                // html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                                html +=
                                                    "<option value = " +
                                                    5 +
                                                    ">基于随机均匀噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    6 +
                                                    " selected>基于随机拉普拉斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    7 +
                                                    ">基于随机高斯噪声的数值加噪算法</option>";
                                                html += "<option value = " + 8 + ">数值偏移</option>";
                                                html += "<option value = " + 9 + ">数值取整</option>";
                                                html +=
                                                    "<option value = " + 10 + ">数值映射</option>";
                                                break;
                                            case 7:
                                                html +=
                                                    "<option value = " +
                                                    3 +
                                                    ">基于拉普拉斯差分隐私的数值加噪算法</option>";
                                                // html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                                html +=
                                                    "<option value = " +
                                                    5 +
                                                    ">基于随机均匀噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    6 +
                                                    ">基于随机拉普拉斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    7 +
                                                    " selected>基于随机高斯噪声的数值加噪算法</option>";
                                                html += "<option value = " + 8 + ">数值偏移</option>";
                                                html += "<option value = " + 9 + ">数值取整</option>";
                                                html +=
                                                    "<option value = " + 10 + ">数值映射</option>";
                                                break;
                                            case 8:
                                                html +=
                                                    "<option value = " +
                                                    3 +
                                                    ">基于拉普拉斯差分隐私的数值加噪算法</option>";
                                                // html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                                html +=
                                                    "<option value = " +
                                                    5 +
                                                    ">基于随机均匀噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    6 +
                                                    ">基于随机拉普拉斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    7 +
                                                    ">基于随机高斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    8 +
                                                    " selected>数值偏移</option>";
                                                html += "<option value = " + 9 + ">数值取整</option>";
                                                html +=
                                                    "<option value = " + 10 + ">数值映射</option>";
                                                break;
                                            case 9:
                                                html +=
                                                    "<option value = " +
                                                    3 +
                                                    ">基于拉普拉斯差分隐私的数值加噪算法</option>";
                                                // html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                                html +=
                                                    "<option value = " +
                                                    5 +
                                                    ">基于随机均匀噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    6 +
                                                    ">基于随机拉普拉斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    7 +
                                                    ">基于随机高斯噪声的数值加噪算法</option>";
                                                html += "<option value = " + 8 + ">数值偏移</option>";
                                                html +=
                                                    "<option value = " +
                                                    9 +
                                                    " selected>数值取整</option>";
                                                html +=
                                                    "<option value = " + 10 + ">数值映射</option>";
                                                break;
                                            case 10:
                                                html +=
                                                    "<option value = " +
                                                    3 +
                                                    ">基于拉普拉斯差分隐私的数值加噪算法</option>";
                                                // html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                                html +=
                                                    "<option value = " +
                                                    5 +
                                                    ">基于随机均匀噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    6 +
                                                    ">基于随机拉普拉斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    7 +
                                                    ">基于随机高斯噪声的数值加噪算法</option>";
                                                html += "<option value = " + 8 + ">数值偏移</option>";
                                                html += "<option value = " + 9 + ">数值取整</option>";
                                                html +=
                                                    "<option value = " +
                                                    10 +
                                                    " selected>数值映射</option>";
                                                break;
                                            default:
                                                html +=
                                                    "<option value = " +
                                                    3 +
                                                    " selected>基于拉普拉斯差分隐私的数值加噪算法</option>";
                                                // html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                                html +=
                                                    "<option value = " +
                                                    5 +
                                                    ">基于随机均匀噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    6 +
                                                    ">基于随机拉普拉斯噪声的数值加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    7 +
                                                    ">基于随机高斯噪声的数值加噪算法</option>";
                                                html += "<option value = " + 8 + ">数值偏移</option>";
                                                html += "<option value = " + 9 + ">数值取整</option>";
                                                html +=
                                                    "<option value = " + 10 + ">数值映射</option>";
                                                break;
                                        }
                                        break;
                                    case 1:
                                        html +=
                                            "<option value = " +
                                            2 +
                                            " selected>编码型数据差分隐私脱敏算法</option>";
                                        break;

                                    case 3:
                                        switch (s.k) {
                                            case 11:
                                                html +=
                                                    "<option value = " +
                                                    11 +
                                                    " selected>尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " + 16 + ">编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                            case 13:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " +
                                                    13 +
                                                    " selected>邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " + 16 + ">编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                            case 14:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    14 +
                                                    " selected>地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " + 16 + ">编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                            case 15:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    15 +
                                                    " selected>名称抑制算法</option>";
                                                html +=
                                                    "<option value = " + 16 + ">编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                            case 16:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    16 +
                                                    " selected>编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                            case 17:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " + 16 + ">编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    " selected>假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                            case 19:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " + 16 + ">编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    19 +
                                                    " selected>密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                            case 20:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " + 16 + ">编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " +
                                                    20 +
                                                    " selected>数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                            case 21:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " + 16 + ">编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " +
                                                    21 +
                                                    " selected>IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                            case 22:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " + 16 + ">编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    " selected>IP地址随机抑制</option>";
                                                break;
                                            default:
                                                html +=
                                                    "<option value = " + 11 + ">尾部截断</option>";
                                                html +=
                                                    "<option value = " + 13 + ">邮箱抑制算法</option>";
                                                html +=
                                                    "<option value = " + 14 + ">地址抑制算法</option>";
                                                html +=
                                                    "<option value = " + 15 + ">名称抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    16 +
                                                    " selected>编号抑制算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    17 +
                                                    ">假名化-哈希算法</option>";
                                                html +=
                                                    "<option value = " + 19 + ">密码随机置换</option>";
                                                html +=
                                                    "<option value = " + 20 + ">数值替换</option>";
                                                html +=
                                                    "<option value = " + 21 + ">IP地址全抑制</option>";
                                                html +=
                                                    "<option value = " +
                                                    22 +
                                                    ">IP地址随机抑制</option>";
                                                break;
                                        }
                                        break;
                                    case 4:
                                        switch (s.k) {
                                            case 1:
                                                html +=
                                                    "<option value = " +
                                                    1 +
                                                    " selected>基于差分隐私的日期加噪算法</option>";
                                                html +=
                                                    "<option value = " + 18 + ">日期分组置换</option>";
                                                /*html += "<option value = " + 12 + ">时间取整</option>"*/
                                                break;
                                            case 18:
                                                html +=
                                                    "<option value = " +
                                                    1 +
                                                    ">基于差分隐私的日期加噪算法</option>";
                                                html +=
                                                    "<option value = " +
                                                    18 +
                                                    " selected>日期分组置换</option>";
                                                /*html += "<option value = " + 12 + ">时间取整</option>"*/
                                                break;
                                            default:
                                                html +=
                                                    "<option value = " +
                                                    1 +
                                                    " selected>基于差分隐私的日期加噪算法</option>";
                                                html +=
                                                    "<option value = " + 18 + ">日期分组置换</option>";
                                                /*html += "<option value = " + 12 + ">时间取整</option>"*/
                                                break;
                                        }
                                        break;
                                }
                                html += "</select></td>";

                                // 隐私级别
                                html += "<td><select>";
                                html +=
                                    "<option value = " + -1 + ">请选择隐私保护程度</option>";
                                switch (s.tmParam) {
                                    case 0:
                                        html +=
                                            "<option value = " +
                                            0 +
                                            " selected>无隐私保护处理</option>";
                                        html += "<option value = " + 1 + ">低程度</option>";
                                        html += "<option value = " + 2 + ">中程度</option>";
                                        html += "<option value = " + 3 + ">高程度</option>";
                                        break;
                                    case 1:
                                        html +=
                                            "<option value = " + 0 + ">无隐私保护处理</option>";
                                        html +=
                                            "<option value = " + 1 + " selected>低程度</option>";
                                        html += "<option value = " + 2 + ">中程度</option>";
                                        html += "<option value = " + 3 + ">高程度</option>";
                                        break;
                                    case 2:
                                        html +=
                                            "<option value = " + 0 + ">无隐私保护处理</option>";
                                        html += "<option value = " + 1 + ">低程度</option>";
                                        html +=
                                            "<option value = " + 2 + " selected>中程度</option>";
                                        html += "<option value = " + 3 + ">高程度</option>";
                                        break;

                                    case 3:
                                        html +=
                                            "<option value = " + 0 + ">无隐私保护处理</option>";
                                        html += "<option value = " + 1 + ">低程度</option>";
                                        html += "<option value = " + 2 + ">中程度</option>";
                                        html +=
                                            "<option value = " + 3 + " selected>高程度</option>";
                                        break;
                                    default:
                                        html +=
                                            "<option value = " + 0 + ">无隐私保护处理</option>";
                                        html += "<option value = " + 1 + ">低程度</option>";
                                        html +=
                                            "<option value = " + 2 + " selected>中程度</option>";
                                        html += "<option value = " + 3 + ">高程度</option>";
                                        break;
                                }

                                html += "</select></td>";

                                html += "</tr>";
                            }
                            document.getElementById("table_body").innerHTML = html;
                        }
                    };

                    xhr.open("get", "/" + sheet + suffix + "param/list", false);
                    xhr.send();
                    document.getElementById("table_body").innerHTML = html;
                });

            // 需求模板
            document
                .getElementById("fileUpload")
                .addEventListener("change", (event) => {
                    choose_file(event);
                });
            document.getElementById("submitExcelParams").onclick = function () {
                let tr;
                let dataArray = [];
                let table_body = document.getElementById("table2");
                if (!table_body) {
                    alert("请先设置模板参数");
                    return;
                }
                for (let i = 0; i < table_body.rows.length; i++) {
                    data = {};
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

                let formData = new FormData();
                formData.set("tableName", sceneName);
                formData.set("params", JSON.stringify(dataArray));
                console.log("Scenename in formData: " + sceneName);
                console.log(dataArray.length);

                //构建formData,发送给后端

                fetch("/File/saveExcelParams", {
                    method: "POST",
                    body: formData,
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.code === 200) {
                            alert("保存模板参数成功")
                        } else {
                            alert("保存模板参数失败")
                            throw new Error(data.msg)
                        }
                    })
                    .catch((error) => console.error("Error:", error));
            };

            document.getElementById("submit").onclick = function () {
                let tr;
                let dataArray = [];
                let table_body = document.getElementById("table2");
                if (!table_body) {
                    alert("请先设置模板参数");
                    return;
                }
                for (let i = 0; i < table_body.rows.length; i++) {
                    data = {};
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

                if (!selectedFile) {
                    alert("请选择文件");
                    return;
                }

                let formData = new FormData();
                formData.set("file", selectedFile);
                formData.set("sheet", sceneName);
                formData.set("algName", "distortion");
                formData.set("params", JSON.stringify(dataArray));
                console.log("Scenename in formData: " + sceneName);
                console.log(dataArray.length);

                //构建formData,发送给后端

                fetch("/File/desenFile", {
                    method: "POST",
                    body: formData,
                })
                    .then((response) => {
                        if (response.status === 500) {
                            // Handle server error
                            return response.text().then(failedMsg => {
                                alert(failedMsg);
                                throw new Error(failedMsg); // Throw an error to stop further processing
                            });
                        }
                        return response.blob();
                    })
                    .then((blob) => {
                        // 脱敏后
                        document.getElementById("afterData").innerHTML = "脱敏后数据";
                        const reader1 = new FileReader();
                        reader1.onload = function (event) {
                            const data = event.target.result;
                            const workbook = XLSX.read(data, {type: "binary"});
                            const sheetName = workbook.SheetNames[0];
                            const sheet = workbook.Sheets[sheetName];
                            const jsonData = XLSX.utils.sheet_to_json(sheet, {header: 1});
                            let pageSize = 10;
                            let pageCount = Math.ceil((jsonData.length - 1) / pageSize);
                            let currentPage1 = 1;

                            function displayTable1(page1) {
                                let startIndex1 = (page1 - 1) * pageSize + 1; // 跳过表头
                                let endIndex = Math.min(
                                    startIndex1 + pageSize,
                                    jsonData.length
                                );

                                let tableContent1 = "<thead><tr>";
                                let headers1 = jsonData[0];
                                headers1.forEach(function (header1) {
                                    tableContent1 +=
                                        '<th style="white-space: nowrap;">' + header1 + "</th>";
                                });
                                tableContent1 += "</tr></thead><tbody>";

                                for (let i = startIndex1; i < endIndex; i++) {
                                    tableContent1 += "<tr>";
                                    for (let j = 0; j < headers1.length; j++) {
                                        let cellValue =
                                            jsonData[i][j] !== undefined ? jsonData[i][j] : "";
                                        tableContent1 += "<td>" + cellValue + "</td>";
                                    }
                                    tableContent1 += "</tr>";
                                }

                                tableContent1 += "</tbody>";

                                $("#dataTable1").html(tableContent1);
                            }

                            displayTable1(currentPage1);

                            function renderPagination1() {
                                let pagination1 =
                                    '<li class="page-item"><a class="page-link" href="#" data-page="prev1">Prev</a></li>';
                                pagination1 +=
                                    '<li class="page-item"><a class="page-link" href="#" data-page="next1">Next</a></li>';

                                $("#pagination1").html(pagination1);

                                $("#pagination1 a")
                                    .off("click")
                                    .on("click", function (e) {
                                        e.preventDefault();
                                        let page = $(this).data("page");
                                        console.log(page);
                                        if (page === "prev1") {
                                            currentPage1 = Math.max(1, currentPage1 - 1);
                                        } else if (page === "next1") {
                                            currentPage1 = Math.min(pageCount, currentPage1 + 1);
                                        }
                                        displayTable1(currentPage1);
                                        $("#totalPages1").text(currentPage1 + "/" + pageCount);
                                        // renderPagination1();
                                    });

                                $("#totalPages1").text(currentPage1 + "/" + pageCount);
                            }

                            $("#paginationContainer1").show();
                            renderPagination1();

                            $("#goToPage1")
                                .off("click")
                                .on("click", function () {
                                    let pageNumber1 = parseInt($("#pageInput1").val());
                                    if (pageNumber1 >= 1 && pageNumber1 <= pageCount) {
                                        console.log(pageCount);
                                        currentPage1 = pageNumber1;
                                        displayTable1(currentPage1);
                                        renderPagination1();
                                    } else {
                                        alert("请输入有效页数！");
                                    }
                                });
                        };

                        reader1.readAsBinaryString(blob);

                        // 创建一个下载链接
                        const downloadLink = document.createElement("a");
                        downloadLink.href = URL.createObjectURL(blob);
                        downloadLink.download = Date.now().toString() + ".xlsx"; // 下载的文件名
                        downloadLink.click();
                        // after.appendChild(downloadLink);
                    })
                    .catch((error) => console.error("Error:", error));
            };

            // document.getElementById('fileDirUpload').addEventListener('change', function(event) {
            //     const files = Array.from(event.target.files);
            //     let tr;
            //     let dataArray = [];
            //     let table_body = document.getElementById("table2");
            //     if (!table_body) {
            //         alert("请先设置模板参数");
            //         return;
            //     }
            //     for (let i = 0; i < table_body.rows.length; i++) {
            //         let data = {};
            //         tr = table_body.rows[i];
            //         //console.log(tr);
            //         data.id = tr.childNodes[0].innerHTML;
            //         data.fieldName = tr.childNodes[1].innerHTML;
            //         data.columnName = tr.childNodes[2].innerHTML;
            //         data.dataType = tr.childNodes[3].firstChild.value;
            //
            //         data.k = tr.childNodes[4].firstChild.value;
            //         data.tmParam = tr.childNodes[5].firstChild.value;
            //         dataArray.push(JSON.stringify(data));
            //         //console.log(dataArray);
            //     }
            //     let dataArrayJsonString = JSON.stringify(dataArray);
            //
            //     // 筛选出以 .xlsx 结尾的文件
            //     const filteredFiles = files.filter(file => file.name.endsWith('.xlsx'));
            //
            //     const maxParallelUploads = 2; // 每次并行上传的文件数
            //     let fileQueue = [...filteredFiles];
            //
            //     function uploadFile(file) {
            //         console.log("Uploading file:", file.name);
            //         return new Promise((resolve, reject) => {
            //             const formData = new FormData();
            //             formData.set("file", file);
            //             formData.set("sheet", sceneName); // 这里替换为实际的 sceneName
            //             formData.set("algName", "distortion");
            //             formData.set("params", dataArrayJsonString); // 这里替换为实际的 params
            //
            //             fetch("/File/desenFile", {
            //                 method: "POST",
            //                 body: formData,
            //             })
            //                 .then(response => {
            //                     if (response.status === 500) {
            //                         return response.text().then(failedMsg => {
            //                             alert(failedMsg);
            //                             throw new Error(failedMsg); // Throw an error to stop further processing
            //                         });
            //                     }
            //                     return response.blob();
            //                 })
            //                 .then(blob => {
            //                     if (blob.size === 0) {
            //                         const errorMsg = "Received empty response from the server for file: " + file.name;
            //                         alert(errorMsg);
            //                         throw new Error(errorMsg); // Throw an error to stop further processing
            //                     }
            //                     // Proceed with blob processing if not empty
            //                     console.log("Blob received for file:", file.name);
            //                     // 创建一个下载链接
            //                     const downloadLink = document.createElement("a");
            //                     downloadLink.href = URL.createObjectURL(blob);
            //                     downloadLink.download = Date.now().toString() + ".xlsx"; // 下载的文件名
            //                     downloadLink.click();
            //                     resolve(); // Mark this file as successfully uploaded
            //                 })
            //                 .catch(error => {
            //                     console.error("Error uploading file:", file.name, error);
            //                     reject(error); // Reject the promise on error
            //                 });
            //         });
            //     }
            //
            //     function processQueue() {
            //         if (fileQueue.length === 0) return;
            //
            //         const currentBatch = fileQueue.splice(0, maxParallelUploads);
            //         const uploadPromises = currentBatch.map(file => uploadFile(file));
            //
            //         Promise.all(uploadPromises)
            //             .then(() => {
            //                 console.log('一批文件上传完成');
            //                 processQueue(); // 继续处理队列中的下一批文件
            //             })
            //             .catch(error => {
            //                 console.error('上传过程中出现错误', error);
            //             });
            //     }
            //
            //     processQueue(); // 开始处理队列
            // });

            function choose_file(event) {
                //读取文件
                selectedFile = event.target.files[0];
                // 文件名，扩展名
                const fileName = selectedFile.name;
                const fileExtension = fileName.split(".").pop().toLowerCase();

                if (selectedFile) {
                    if ("xlsx" === fileExtension) {
                        console.log("Selected file:", selectedFile);
                        console.log(fileExtension);
                        // 这里可以执行其他操作，例如显示文件名或预览文件

                        let fileInfoMsg =
                            '<div  style="font-size: 20px; text-align: center"> <span>' +
                            "<strong>" +
                            fileName +
                            "文件</strong>已选择" +
                            "</span>" +
                            "</div>";
                        document.getElementById("fileInfo").innerHTML = fileInfoMsg;
                        // 脱敏前
                        document.getElementById("preData").innerHTML = "脱敏前数据";
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let data = new Uint8Array(e.target.result);
                            let workbook = XLSX.read(data, {type: "array"});

                            let sheetName = workbook.SheetNames[0];
                            let sheet = workbook.Sheets[sheetName];

                            let jsonData = XLSX.utils.sheet_to_json(sheet, {header: 1});

                            let pageSize = 10;
                            let pageCount = Math.ceil((jsonData.length - 1) / pageSize);
                            let currentPage = 1;

                            function displayTable(page) {
                                let startIndex = (page - 1) * pageSize + 1; // 跳过表头
                                let endIndex = Math.min(
                                    startIndex + pageSize,
                                    jsonData.length
                                );

                                let tableContent = "<thead><tr>";
                                let headers = jsonData[0];
                                headers.forEach(function (header) {
                                    tableContent +=
                                        '<th style="white-space: nowrap;">' + header + "</th>";
                                });
                                tableContent += "</tr></thead><tbody>";

                                for (let i = startIndex; i < endIndex; i++) {
                                    tableContent += "<tr>";
                                    for (let j = 0; j < headers.length; j++) {
                                        let cellValue =
                                            jsonData[i][j] !== undefined ? jsonData[i][j] : "";
                                        tableContent += "<td>" + cellValue + "</td>";
                                    }
                                    tableContent += "</tr>";
                                }

                                tableContent += "</tbody>";

                                $("#dataTable").html(tableContent);
                            }

                            displayTable(currentPage);

                            function renderPagination() {
                                let pagination =
                                    '<li class="page-item"><a class="page-link" href="#" data-page="prev">Prev</a></li>';
                                pagination +=
                                    '<li class="page-item"><a class="page-link" href="#" data-page="next">Next</a></li>';

                                $("#pagination").html(pagination);

                                $("#pagination a")
                                    .off("click")
                                    .on("click", function (e) {
                                        e.preventDefault();
                                        let page = $(this).data("page");
                                        console.log(page);
                                        if (page === "prev") {
                                            currentPage = Math.max(1, currentPage - 1);
                                        } else if (page === "next") {
                                            currentPage = Math.min(pageCount, currentPage + 1);
                                        }
                                        displayTable(currentPage);
                                        $("#totalPages").text(currentPage + "/" + pageCount);
                                        // renderPagination();
                                    });

                                $("#totalPages").text(currentPage + "/" + pageCount);
                            }

                            $("#paginationContainer").show();
                            renderPagination();

                            $("#goToPage")
                                .off("click")
                                .on("click", function () {
                                    let pageNumber = parseInt($("#pageInput").val());
                                    if (pageNumber >= 1 && pageNumber <= pageCount) {
                                        console.log(pageCount);
                                        currentPage = pageNumber;
                                        displayTable(currentPage);
                                        renderPagination();
                                    } else {
                                        alert("请输入有效页数！");
                                    }
                                });
                        };
                        reader.readAsArrayBuffer(selectedFile);
                    } else {
                        alert("请提交excel文件");
                    }
                } else {
                    document.getElementById("fileInfo").innerHTML = "";
                }
            }

            // 非失真脱敏相关
            let nondistortionPreviewFile = document.getElementById("preview-btn");
            let nondistortionUploadFile = document.getElementById("submit-btn");
            let nonDistortionFile = "";
            let nonDistortionFileName = "";

            function nondistortion_choose_file(event) {
                // 清空
                document.getElementById("nonDistortionFileInfo").innerHTML = "";
                // document.getElementById("preview-container").style.display = "none";
                document.querySelector(".raw-csv").style.display = "none";
                document.querySelector(".result-csv").style.display = "none";

                //读取文件
                if (nonDistortionFile !== event.target.files[0]) {
                    nonDistortionFile = event.target.files[0];
                    nonDistortionFileName = nonDistortionFile.name;
                }

                // 文件名，扩展名
                if (nonDistortionFile) {
                    const fileExtension = nonDistortionFileName
                        .split(".")
                        .pop()
                        .toLowerCase();
                    if (fileExtension !== "csv") {
                        alert("请上传csv文件");
                        return;
                    }
                    const fileName = nonDistortionFile.name;
                    let fileLoad =
                        '<div  style="font-size: 20px; text-align: center"> <span>' +
                        "<strong>" +
                        fileName +
                        "文件</strong>已选择" +
                        "</span>" +
                        "</div>";
                    console.log(fileName);
                    document.getElementById("nonDistortionFileInfo").innerHTML =
                        fileLoad;

                    console.log(fileExtension);

                    nondistortionUploadFile.disabled = false;
                    nondistortionPreviewFile.disabled = false;
                }
            }

            function previewCsv(file) {
                Papa.parse(file, {
                    complete: function (results) {
                        const data = results.data;
                        displayTable(data, 1);
                        setupPagination(data);
                    },
                    header: true,
                });
            }

            function uploadCsvFile(csvFile) {
                let formData = new FormData();
                formData.set("file", csvFile);
                fetch("/encryptMedical/receiveMedicalCsv", {
                    method: "POST",
                    body: formData,
                })
                    .then((response) => response.json())
                    .then((content) => {
                        if (content.status === "error") {
                            alert(content.data);
                        } else {
                            // 处理字节数组的CSV文件内容
                            const binaryData = atob(content.data); // Base64解码
                            const byteArray = new Uint8Array(binaryData.length);
                            for (let i = 0; i < binaryData.length; i++) {
                                byteArray[i] = binaryData.charCodeAt(i);
                            }
                            const csvContent = new TextDecoder("utf-8").decode(byteArray);
                            // 使用PapaParse解析CSV内容
                            const parsedData = Papa.parse(csvContent, {
                                header: true,
                            });

                            console.log(parsedData);

                            // 获取最后一列
                            const lastColumnHeader =
                                parsedData.meta.fields[parsedData.meta.fields.length - 1];
                            const lastColumnData = parsedData.data.map(
                                (row) => row[lastColumnHeader]
                            );

                            // 显示在前端界面
                            displayLastColumn(lastColumnData);

                            // 启用下载按钮
                            const downloadButton = $("#download-result-btn");
                            downloadButton.prop("disabled", false);
                            downloadButton
                                .off("click")
                                .on("click", () =>
                                    downloadCsv(csvContent, Date.now() + "_processed_file.csv")
                                );
                        }
                    })
                    .catch((error) => {
                        console.error("Error:", error);
                        alert(error);
                    });
            }

            nondistortionPreviewFile.addEventListener("click", () => {
                previewCsv(nonDistortionFile);
            });
            nondistortionUploadFile.addEventListener("click", () => {
                uploadCsvFile(nonDistortionFile);
            });

            function displayLastColumn(data, pageSize = 10) {
                let resultContainer = document.querySelector(".result-csv");
                let paginationContainer = document.getElementById(
                    "desenFilePaginationContainer"
                );

                let totalPages = Math.ceil(data.length / pageSize);
                let currentPage = 1;

                function renderTable(page) {
                    let startIndex = (page - 1) * pageSize;
                    let endIndex = Math.min(startIndex + pageSize, data.length);
                    let rows = "";
                    for (let i = startIndex; i < endIndex; i++) {
                        rows += "<tr><td>" + data[i] + "</td></tr>";
                    }
                    document.getElementById("desen-csv-tbody").innerHTML = rows;
                    resultContainer.style.display = "block";
                    updatePaginationInfo();
                }

                function updatePaginationInfo() {
                    document.getElementById("desenFilePaginationInfo").textContent =
                        "Page " + currentPage + " of " + totalPages;
                }

                function renderPaginationNonDistortion() {
                    document.getElementById("desenFilePrevPage").onclick = function (
                        event
                    ) {
                        event.preventDefault();
                        if (currentPage > 1) {
                            currentPage--;
                            renderTable(currentPage);
                            document.getElementById("desenFilePageInput").value =
                                currentPage;
                        }
                    };

                    document.getElementById("desenFileNextPage").onclick = function (
                        event
                    ) {
                        event.preventDefault();
                        if (currentPage < totalPages) {
                            currentPage++;
                            renderTable(currentPage);
                            document.getElementById("desenFilePageInput").value =
                                currentPage;
                        }
                    };

                    document.getElementById("desenFilePageInput").onchange =
                        function () {
                            let page = parseInt(this.value);
                            if (page >= 1 && page <= totalPages) {
                                currentPage = page;
                                renderTable(currentPage);
                            } else {
                                alert("请输入有效页数！");
                            }
                        };

                    document.getElementById("desenFilePageInput").value = currentPage;
                    paginationContainer.style.display = "flex";
                }

                renderTable(currentPage);
                renderPaginationNonDistortion();
            }

            // 设置默认算法
            $("#setDefaultAlgorithm").on("click", function (e) {
                let postData = new URLSearchParams();
                let transferScene = $("#choose_transfer_scene").val();
                let template = $("#choose_template_sheet").val();
                postData.set("toolsetName", "excel");
                postData.set("defaultAlgName", template + "_" + transferScene);
                console.log("postData: " + postData);

                fetch("/toolset/setDefaultToolset", {
                    method: "POST",
                    body: postData,
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.code === 200) {
                            alert("设置默认算法成功！");
                            defaultOption = template + "_" + transferScene;
                        } else {
                            throw new Error("设置默认算法失败！");
                        }
                    })
                    .catch((error) => {
                        alert(error);
                        console.log(error);
                    })
            });

            // document.getElementById('fileDirUpload').addEventListener('change', function(event) {
            //     const files = event.target.files;
            //     const allowedExtensions = ['.xlsx']; // 需要筛选的文件后缀
            //
            //     const filteredFiles = Array.from(files).filter(file => {
            //         return allowedExtensions.some(extension => file.name.endsWith(extension));
            //     });
            //     console.log(filteredFiles.length)
            //     // 输出筛选后的文件列表
            //     filteredFiles.forEach(file => {
            //         console.log(file.name);
            //     });
            //
            //     // 如果需要进一步处理筛选后的文件，可以在这里编写逻辑
            // });

            function displayTable(data, page, pageSize = 10) {
                const startIndex = (page - 1) * pageSize;
                const endIndex = Math.min(startIndex + pageSize, data.length);

                const headers = Object.keys(data[0]);

                let thead = "<tr>";
                headers.forEach((header) => {
                    thead += "<th>" + header + "</th>";
                });
                thead += "</tr>";

                let tbody = "";
                for (let i = startIndex; i < endIndex; i++) {
                    tbody += "<tr>";
                    headers.forEach((header) => {
                        tbody +=
                            "<td>" +
                            (data[i][header] !== undefined ? data[i][header] : "") +
                            "</td>";
                    });
                    tbody += "</tr>";
                }

                document.getElementById("csv-thead").innerHTML = thead;
                document.getElementById("csv-tbody").innerHTML = tbody;
                document.querySelector(".raw-csv").style.display = "block";
            }

            function setupPagination(data, pageSize = 10) {
                let totalPages = Math.ceil(data.length / pageSize);
                let currentPage = 1;
                const paginationContainer = document.getElementById(
                    "nonDistortionPagination"
                );

                function updatePaginationInfo() {
                    document.getElementById("nonDistortionPaginationInfo").textContent =
                        "Page " + currentPage + " of " + totalPages;
                }

                document.getElementById("nonDistortionPrevPage").onclick = function (
                    event
                ) {
                    event.preventDefault();
                    if (currentPage > 1) {
                        currentPage--;
                        displayTable(data, currentPage, pageSize);
                        document.getElementById("nonDistortionPageInput").value =
                            currentPage;
                        updatePaginationInfo();
                    }
                };

                document.getElementById("nonDistortionNextPage").onclick = function (
                    event
                ) {
                    event.preventDefault();
                    if (currentPage < totalPages) {
                        currentPage++;
                        displayTable(data, currentPage, pageSize);
                        document.getElementById("nonDistortionPageInput").value =
                            currentPage;
                        updatePaginationInfo();
                    }
                };

                document.getElementById("nonDistortionPageInput").onchange =
                    function () {
                        let page = parseInt(this.value);
                        if (page >= 1 && page <= totalPages) {
                            currentPage = page;
                            displayTable(data, currentPage, pageSize);
                            updatePaginationInfo();
                        } else {
                            alert("请输入有效页数！");
                        }
                    };

                document.getElementById("nonDistortionPageInput").value = currentPage;
                displayTable(data, currentPage, pageSize);
                updatePaginationInfo();
                paginationContainer.style.display = "flex";
            }

            function downloadCsv(content, fileName) {
                let blob = new Blob([content], {type: "text/csv;charset=utf-8;"});
                let link = document.createElement("a");
                let url = URL.createObjectURL(blob);

                link.setAttribute("href", url);
                link.setAttribute("download", fileName);
                link.style.visibility = "hidden";
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }

            document.getElementById("nonDistortionFileUpload").onchange = (
                event
            ) => {
                nondistortion_choose_file(event);
            };
        };
    </script>

    <style>
        .tabs-container ul {
            height: 70px;
            display: flex;
            flex-direction: row;
            justify-content: center;
        }

        /* 设置表格样式 */
        #dataTableContainer {
            width: 100%;
            overflow-x: auto;
        }

        #dataTable {
            width: 100%;
            margin: 0 auto;
        }

        #paginationInfo1,
        #paginationInfo2,
        #paginationInfo3,
        #paginationInfo {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        #paginationInfo1 input,
        #paginationInfo2 input,
        #paginationInfo3 input,
        #paginationInfo input {
            width: 5em;
            text-align: center;
        }

        .pagination-container {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .pagination-container ul {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-left: 10px;
        }

        #desenFilePagination {
            margin-right: auto; /* 推动至左端 */
        }

        #desenFilePaginationInfo {
            margin-left: auto; /* 推动至右端 */
        }

        /* 设置表格样式 */
        #dataTableContainer1 {
            width: 100%;
            overflow-x: auto;
        }

        #dataTable1 {
            width: 100%;
            margin: 0 auto;
        }

        /*标题*/
        /*.ibox-title {*/
        /*    height: 200px;*/
        /*    border-color: #edf1f2;*/
        /*    background-color: #dbeafe;*/
        /*    color: black;*/
        /*    display: flex;*/
        /*    align-items: center;*/
        /*    justify-content: center;*/
        /*}*/

        .btn2 > button {
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
        #uploadForm > button,
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

        .fixed-width {
            width: 200px;
        }

        #table_body th {
            text-align: center;
        }
    </style>
</head>
<body>
<div class="row">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title"></div>

            <div class="tabs-container">
                <ul id="tab-type" class="nav nav-tabs" style="font-size: 20px">
                    <li class="active">
                        <a data-toggle="tab" href="#tab-1" aria-expanded="true">
                            失真
                        </a>
                    </li>
                    <li class="">
                        <a data-toggle="tab" href="#tab-2" aria-expanded="false">
                            非失真
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div
                            id="tab-1"
                            class="tab-pane active"
                            style="text-align: center"
                    >
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label for="choose_transfer_scene" style="font-size: 20px"
                                >选择流转场景</label
                                >
                                <select
                                        name="transfer_scene"
                                        id="choose_transfer_scene"
                                        style="font-size: 20px; text-align: center"
                                >
                                    <option value="111" selected>请选择流转场景</option>
                                    <option value="low">用户本地</option>
                                    <option value="medium">同机构不同系统</option>
                                    <option value="high">不同机构不同系统</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="choose_template_sheet" style="font-size: 20px"
                                >应用场景模板选择</label
                                >
                                <select
                                        name="template_sheet"
                                        id="choose_template_sheet"
                                        style="font-size: 20px; text-align: center"
                                >
                                    <option value="111" selected>请选择应用场景</option>
                                    <option value="map">地图导航类场景</option>
                                    <option value="onlinetaxi">网络约车类场景</option>
                                    <option value="communication">即时通信场景</option>
                                    <option value="community">网络社区类场景</option>
                                    <option value="onlinepayment">网络支付类场景</option>
                                    <option value="onlineshopping">网上购物类场景</option>
                                    <option value="transportationticket">交通票务类场景</option>
                                    <option value="marry">婚恋相亲类场景</option>
                                    <option value="employment">求职招聘类场景</option>
                                    <option value="onlinelending">网络借贷类场景</option>
                                    <option value="house">房屋租售类场景</option>
                                    <option value="usedcar">二手车交易类场景</option>
                                    <option value="consultation">问诊挂号类场景</option>
                                    <option value="travel">旅游服务类场景</option>
                                    <option value="hotel">酒店服务类场景</option>
                                    <option value="express">邮件快件寄递类场景</option>
                                    <option value="game">网络游戏类场景</option>
                                    <option value="education">学习教育类场景</option>
                                    <option value="locallife">本地生活类场景</option>
                                    <option value="woman">女性健康类场景</option>
                                    <option value="usecar">用车服务类场景</option>
                                    <option value="investment">投资理财类场景</option>
                                    <option value="bank">手机银行类场景</option>
                                    <option value="mailbox">邮箱云盘类场景</option>
                                    <option value="meeting">远程会议类场景</option>
                                    <option value="webcast">网络直播类场景</option>
                                    <option value="takeaway">餐饮外卖类场景</option>
                                    <option value="onlinemovie">在线影音类场景</option>
                                    <option value="shortvideo">短视频类场景</option>
                                    <option value="news">新闻资讯类场景</option>
                                    <option value="sports">运动健身类场景</option>
                                    <option value="browser">浏览器类场景</option>
                                    <option value="input">输入法类场景</option>
                                    <option value="security">安全管理类场景</option>
                                    <option value="ebook">电子图书类场景</option>
                                    <option value="capture">拍摄美化类场景</option>
                                    <option value="appstore">应用商店类场景</option>
                                    <option value="tools">实用工具类场景</option>
                                    <option value="performanceticket">演出票务类场景</option>
                                    <option value="networkaccess">
                                        电话/有线电视入网类场景
                                    </option>
                                    <option value="telecommunication">
                                        电信业务使用类场景
                                    </option>
                                    <option value="monitor">安防监控类场景</option>
                                    <option value="pay">生活缴费类场景</option>
                                    <option value="customerservice">客服类场景</option>
                                    <option value="schoolservice">校园服务类场景</option>
                                    <option value="smarthome">智慧家居类场景</option>
                                    <option value="autonomousdriving">自动驾驶类场景</option>
                                    <option value="telemedicine">远程诊疗类场景</option>
                                    <option value="vr">虚拟现实类场景</option>
                                    <option value="onlinevoting">网上投票类场景</option>
<#--                                    <option value="telecomclient">电信客户信息类数据</option>-->

                                </select>
                            </div>
                            <button
                                    type="button"
                                    class="btn btn-sm btn-primary upload-btn"
                                    id="showTemplate"
                            >
                                场景模板展示
                            </button>
                            <button
                                    type="button"
                                    class="btn btn-sm btn-primary upload-btn"
                                    id="setTemplate"
                            >
                                设置需求模板
                            </button>
                            <div class="form-group" id="uploadForm">
                                <input type="file" id="fileUpload" accept=".xlsx" style="display: none"/>
                                <label for="fileUpload" class="upload-btn">
                                    选择文件
                                </label>
<#--                                <input type="file" id="fileDirUpload" webkitdirectory multiple style="display: none"/>-->
<#--                                <label for="fileDirUpload" class="upload-btn m-l">选择文件夹</label>-->
                                <button type="button" class="btn btn-sm btn-primary m-l" id="submitExcelParams">
                                    保存模板参数
                                </button>
                            </div>

                            <div class="btn2">
                                <button type="button" class="btn btn-sm btn-primary" id="submit"> 提交脱敏</button>
                                <button type="button" class="btn btn-sm btn-primary m-l" id="setDefaultAlgorithm">
                                    设置当前算法为默认算法
                                </button>
                            </div>
                        </form>

                        <!--文件上传信息-->
                        <div id="fileInfo"></div>
                        <div id="after"></div>
                        <div class="ibox-content">
                            <div id="table_body"></div>
                        </div>

                        <div class="table-area">
                            <div>
                                <span id="preData" class="center-text"></span>
                            </div>
                            <div id="dataTableContainer">
                                <table id="dataTable" class="table table-bordered">
                                    <!-- 这里将用 JavaScript 动态创建表格内容 -->
                                </table>
                            </div>
                            <div
                                    id="paginationContainer"
                                    class="mt-3"
                                    style="display: none"
                            >
                                <nav>
                                    <div
                                            id="paginationInfo"
                                            class="d-flex justify-content-between align-items-center"
                                    >
                                        <ul class="pagination mb-0" id="pagination"></ul>
                                        <div class="form-group mb-0 text-center">
                                            <label for="pageInput">跳转至：</label>
                                            <input
                                                    type="number"
                                                    class="form-control"
                                                    id="pageInput"
                                                    min="1"
                                            />
                                            <button class="btn btn-primary mt-2" id="goToPage">
                                                跳转
                                            </button>
                                        </div>
                                        <div id="totalPages"></div>
                                    </div>
                                </nav>
                            </div>
                            <div>
                                <span id="afterData" class="center-text"></span>
                            </div>
                            <div id="dataTableContainer1">
                                <table id="dataTable1" class="table table-bordered">
                                    <!-- 这里将用 JavaScript 动态创建表格内容 -->
                                </table>
                            </div>
                            <div
                                    id="paginationContainer1"
                                    class="mt-3"
                                    style="display: none"
                            >
                                <nav>
                                    <div
                                            id="paginationInfo1"
                                            class="d-flex justify-content-between align-items-center"
                                    >
                                        <ul class="pagination mb-0" id="pagination1"></ul>
                                        <div class="form-group mb-0 text-center">
                                            <label for="pageInput1">跳转至：</label>
                                            <input
                                                    type="number"
                                                    class="form-control"
                                                    id="pageInput1"
                                                    min="1"
                                            />
                                            <button class="btn btn-primary mt-2" id="goToPage1">
                                                跳转
                                            </button>
                                        </div>
                                        <div id="totalPages1"></div>
                                    </div>
                                </nav>
                            </div>
                        </div>
                    </div>
                    <div id="tab-2" class="tab-pane">
                        <div class="row">
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th>请选择表格非失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody id="table1">
                                <tr>
                                    <td>
                                        <select id="nondistortionexcel_algName">
                                            <option value="linearsvm" selected>
                                                医疗数据加密查询算法
                                            </option>
                                        </select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="row">
                            <div class="midtile">
                                <div class="col-sm-5 m-b-xs">
                                    <form
                                            id="uploadForm"
                                            action="/upload"
                                            method="post"
                                            enctype="multipart/form-data"
                                    >
                                        <input
                                                type="file"
                                                id="nonDistortionFileUpload"
                                                accept=".csv"
                                                style="display: none"
                                        />
                                        <label for="nonDistortionFileUpload" class="upload-btn">
                                            选择将要上传的csv文件
                                        </label>
                                    </form>
                                </div>
                            </div>

                            <div id="nonDistortionFileInfo"></div>
                        </div>

                        <div class="container">
                            <div class="row text-center">
                                <div class="col-sm-offset-3 col-sm-2">
                                    <button
                                            type="button"
                                            class="btn btn-sm btn-primary upload-btn"
                                            id="preview-btn"
                                            disabled
                                    >
                                        预览文件
                                    </button>
                                </div>
                                <div class="col-sm-2">
                                    <button
                                            type="button"
                                            class="btn btn-sm btn-primary upload-btn"
                                            id="submit-btn"
                                            disabled
                                    >
                                        提交脱敏
                                    </button>
                                </div>
                                <div class="col-sm-2">
                                    <button
                                            type="button"
                                            class="btn btn-sm btn-primary upload-btn"
                                            id="download-result-btn"
                                            disabled
                                    >
                                        下载脱敏后文件
                                    </button>
                                </div>
                            </div>

                            <#-- 显示原始医疗数据--> <#--
                  <div class="row raw-csv" style="display: none">
                    --> <#--
                    <div class="col-md-12">
                      --> <#--
                      <h3>原始医疗数据</h3>
                      --> <#--
                      <div id="preview-container" class="table-bordered">
                        --> <#--
                        <table id="csv-table" class="table table-striped">
                          --> <#--
                          <thead id="csv-thead"></thead>
                          --> <#--
                          <tbody id="csv-tbody"></tbody>
                          --> <#--
                        </table>
                        --> <#--
                        <nav aria-label="Page navigation">
                          --> <#--
                          <ul
                            class="pagination"
                            id="nonDistortionPagination"
                          ></ul>
                          --> <#--
                        </nav>
                        --> <#--
                      </div>
                      --> <#--
                    </div>
                    --> <#--
                  </div>
                  --> <#--
                  <div class="row result-csv" style="display: none">
                    --> <#--
                    <div class="col-sm6">
                      --> <#--
                      <h3>脱敏后的医疗数据</h3>
                      --> <#--
                      <div id="result-container" class="table-bordered">
                        --> <#--
                        <table id="csv-table" class="table table-striped">
                          --> <#--
                          <thead id="desen-csv-thead" class="fixed-width">
                            <th>查询结果</th>
                          </thead>
                          --> <#--
                          <tbody
                            id="desen-csv-tbody"
                            class="fixed-width"
                          ></tbody>
                          --> <#--
                        </table>
                        --> <#--
                      </div>
                      --> <#--
                      <nav
                        aria-label="Page navigation"
                        id="desenFilePaginationContainer"
                        --
                      >
                        <#-- style="display: none;">--> <#--
                        <ul class="pagination" id="desenFilePagination"></ul>
                        --> <#--
                      </nav>
                      --> <#--
                    </div>
                    --> <#--
                  </div>
                  -->

                            <div class="row raw-csv" style="display: none">
                                <div class="col-md-12">
                                    <h3>原始医疗数据</h3>
                                    <div id="preview-container" class="table-bordered">
                                        <table id="csv-table" class="table table-striped">
                                            <thead id="csv-thead"></thead>
                                            <tbody id="csv-tbody"></tbody>
                                        </table>
                                    </div>
                                    <div class="pagination-container">
                                        <nav aria-label="Page navigation">
                                            <ul
                                                    class="pagination"
                                                    id="nonDistortionPagination"
                                                    style="display: flex"
                                            >
                                                <li class="page-item">
                                                    <a
                                                            class="page-link"
                                                            href="#"
                                                            aria-label="Previous"
                                                            id="nonDistortionPrevPage"
                                                    >
                                                        <span aria-hidden="true">&laquo;</span>
                                                    </a>
                                                </li>
                                                <li class="page-item">
                                                    <input
                                                            type="number"
                                                            id="nonDistortionPageInput"
                                                            class="form-control"
                                                            style="width: 70px"
                                                            min="1"
                                                    />
                                                </li>
                                                <li class="page-item">
                                                    <a
                                                            class="page-link"
                                                            href="#"
                                                            aria-label="Next"
                                                            id="nonDistortionNextPage"
                                                    >
                                                        <span aria-hidden="true">&raquo;</span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </nav>
                                        <div id="nonDistortionPaginationInfo"></div>
                                    </div>
                                </div>
                            </div>

                            <div class="row result-csv" style="display: none">
                                <div class="col-sm-12">
                                    <h3>脱敏后的医疗数据</h3>
                                    <div id="result-container" class="table-bordered">
                                        <table id="csv-table" class="table table-striped">
                                            <thead id="desen-csv-thead" class="fixed-width">
                                            <th>查询结果</th>
                                            </thead>
                                            <tbody
                                                    id="desen-csv-tbody"
                                                    class="fixed-width"
                                            ></tbody>
                                        </table>
                                    </div>
                                    <div
                                            class="pagination-container"
                                            id="desenFilePaginationContainer"
                                    >
                                        <nav aria-label="Page navigation">
                                            <ul class="pagination" id="desenFilePagination">
                                                <li class="page-item">
                                                    <a
                                                            class="page-link"
                                                            href="#"
                                                            aria-label="Previous"
                                                            id="desenFilePrevPage"
                                                    >
                                                        <span aria-hidden="true">&laquo;</span>
                                                    </a>
                                                </li>
                                                <li class="page-item">
                                                    <input
                                                            type="number"
                                                            id="desenFilePageInput"
                                                            class="form-control"
                                                            style="width: 70px"
                                                            min="1"
                                                    />
                                                </li>
                                                <li class="page-item">
                                                    <a
                                                            class="page-link"
                                                            href="#"
                                                            aria-label="Next"
                                                            id="desenFileNextPage"
                                                    >
                                                        <span aria-hidden="true">&raquo;</span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </nav>
                                        <div id="desenFilePaginationInfo"></div>
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
