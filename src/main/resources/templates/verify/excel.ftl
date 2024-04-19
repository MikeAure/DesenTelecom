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
        // 场景模板
        document.getElementById("choose_template_sheet").addEventListener("change", function () {
            // 清空
            document.getElementById("fileInfo").innerHTML = "";
            /* document.getElementById("table_list").innerHTML = ""
             document.getElementById("table_list2").innerHTML = ""*/
            document.getElementById("table_body").innerHTML = ""
            // 重置需求模板
            document.getElementById("choose_sheet").value = "111"

            // 场景名
            let sheet = document.getElementById("choose_template_sheet").value;
            let html = "";
            console.log("sheet:" + sheet)

            let xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.status === 200 && xhr.readyState === 4) {
                    let data_str = xhr.responseText;
                    let data = JSON.parse(data_str);
                    console.log(data.length)

                    html += "<div class=\"table-responsive\">" +
                        "<table class=\"table table-striped\">" +
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
                        "<tbody id=\"table2\">";
                    for (let i = 0; i < data.length; i++) {
                        s = data[i];
                        //console.log(s)
                        html += "<tr id = " + "row_" + i + ">";
                        html += "<td>" + s.id + "</td>";
                        html += "<td>" + s.fieldName + "</td>";
                        html += "<td>" + s.columnName + "</td>";
                        html += "<td>"
                        switch (s.dataType) {
                            case 0:
                                html += "数值型数据"
                                break;
                            case 1:
                                html += "编码型数据"
                                break;
                            case 3:
                                html += "文本型数据"
                                break;
                            case 4:
                                html += "时间数据"
                                break;
                        }
                        html += "</td>"
                        html += "</tr>";
                    }
                    document.getElementById("table_body").innerHTML = html
                }
            }

            xhr.open("get", "/" + sheet + "param/list", false);
            xhr.send();
            document.getElementById("table_body").innerHTML = html;
        })
        //需求模板
        document.getElementById("fileUpload").addEventListener("change", choose_file)
    }
    choose_file = function (event) {
        // 清空
        document.getElementById("fileInfo").innerHTML = "";

        document.getElementById("choose_template_sheet").value = "111";
        //document.getElementById('choose_sheet').selectedIndex = 0;
        // 场景名
        let sheet = document.getElementById("choose_sheet").value;
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
                // TODO: 改成fetch发送
                let formData = new FormData();
                formData.set("file", file);
                formData.set("sheet", sheet);
                let after = document.getElementById("after");
                let html = "";
                console.log("sheet:" + sheet)
                let xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.status === 200 && xhr.readyState === 4) {
                        let data_str = xhr.responseText;
                        let data = JSON.parse(data_str);
                        console.log(data.length)


                        html += "<div class=\"table-responsive\">" +
                            "<table class=\"table table-striped\">" +
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
                            "<tbody id=\"table2\">";
                        for (let i = 0; i < data.length; i++) {
                            s = data[i];
                            //console.log(s)
                            html += "<tr id = " + "row_" + i + ">";
                            html += "<td>" + s.id + "</td>";
                            html += "<td>" + s.fieldName + "</td>";
                            html += "<td>" + s.columnName + "</td>";
                            /* html += "<td>"
                             // 字段类型
                             switch (s.dataType) {
                                 case 0:
                                     html += "数值型数据"
                                     break;
                                 case 1:
                                     html += "编码型数据"
                                     break;
                                 case 3:
                                     html += "文本型数据"
                                     break;
                                 case 4:
                                     html += "时间数据"
                                     break;
                             }
                             html += "</td>"*/
                            html += "<td><select>"
                            /*html += "<option selected value = " + -1 + ">请选择字段数据类型</option>"*/
                            switch (s.dataType) {
                                case 0:
                                    html += "<option value = " + 0 + " selected>数值型数据</option>"
                                    /*html += "<option value = " + 1 + ">编码型数据</option>"
                                    html += "<option value = " + 3 + ">文本型数据</option>"
                                    html += "<option value = " + 4 + ">时间数据</option>"*/
                                    break;
                                case 1:
                                    /*html += "<option value = " + 0 + ">数值型数据</option>"*/
                                    html += "<option value = " + 1 + " selected>编码型数据</option>"
                                    /*html += "<option value = " + 3 + ">文本型数据</option>"
                                    html += "<option value = " + 4 + ">时间数据</option>"*/
                                    break;

                                case 3:
                                    /* html += "<option value = " + 0 + ">数值型数据</option>"
                                     html += "<option value = " + 1 + ">编码型数据</option>"*/
                                    html += "<option value = " + 3 + " selected>文本型数据</option>"
                                    /*html += "<option value = " + 4 + ">时间数据</option>"*/
                                    break;
                                case 4:
                                    /* html += "<option value = " + 0 + ">数值型数据</option>"
                                     html += "<option value = " + 1 + ">编码型数据</option>"
                                     html += "<option value = " + 3 + ">文本型数据</option>"*/
                                    html += "<option value = " + 4 + " selected>时间数据</option>"
                                    break;
                            }
                            html += "</select></td>"

                            //算法
                            html += "<td><select>"
                            switch (s.dataType) {
                                case 0:
                                    switch (s.k) {
                                        case 3:
                                            html += "<option value = " + 3 + " selected>基于拉普拉斯差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 5 + ">基于随机均匀噪声的数值加噪算法</option>"
                                            html += "<option value = " + 6 + ">基于随机拉普拉斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 7 + ">基于随机高斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 8 + ">数值偏移</option>"
                                            html += "<option value = " + 9 + ">数值取整</option>"
                                            html += "<option value = " + 10 + ">数值映射</option>"
                                            break;
                                        case 4:
                                            html += "<option value = " + 3 + ">基于拉普拉斯差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 4 + " selected>基于高斯机制差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 5 + ">基于随机均匀噪声的数值加噪算法</option>"
                                            html += "<option value = " + 6 + ">基于随机拉普拉斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 7 + ">基于随机高斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 8 + ">数值偏移</option>"
                                            html += "<option value = " + 9 + ">数值取整</option>"
                                            html += "<option value = " + 10 + ">数值映射</option>"
                                            break;
                                        case 5:
                                            html += "<option value = " + 3 + ">基于拉普拉斯差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 5 + " selected>基于随机均匀噪声的数值加噪算法</option>"
                                            html += "<option value = " + 6 + ">基于随机拉普拉斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 7 + ">基于随机高斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 8 + ">数值偏移</option>"
                                            html += "<option value = " + 9 + ">数值取整</option>"
                                            html += "<option value = " + 10 + ">数值映射</option>"
                                            break;
                                        case 6:
                                            html += "<option value = " + 3 + ">基于拉普拉斯差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 5 + ">基于随机均匀噪声的数值加噪算法</option>"
                                            html += "<option value = " + 6 + " selected>基于随机拉普拉斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 7 + ">基于随机高斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 8 + ">数值偏移</option>"
                                            html += "<option value = " + 9 + ">数值取整</option>"
                                            html += "<option value = " + 10 + ">数值映射</option>"
                                            break;
                                        case 7:
                                            html += "<option value = " + 3 + ">基于拉普拉斯差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 5 + ">基于随机均匀噪声的数值加噪算法</option>"
                                            html += "<option value = " + 6 + ">基于随机拉普拉斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 7 + " selected>基于随机高斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 8 + ">数值偏移</option>"
                                            html += "<option value = " + 9 + ">数值取整</option>"
                                            html += "<option value = " + 10 + ">数值映射</option>"
                                            break;
                                        case 8:
                                            html += "<option value = " + 3 + ">基于拉普拉斯差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 5 + ">基于随机均匀噪声的数值加噪算法</option>"
                                            html += "<option value = " + 6 + ">基于随机拉普拉斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 7 + ">基于随机高斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 8 + " selected>数值偏移</option>"
                                            html += "<option value = " + 9 + ">数值取整</option>"
                                            html += "<option value = " + 10 + ">数值映射</option>"
                                            break;
                                        case 9:
                                            html += "<option value = " + 3 + ">基于拉普拉斯差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 5 + ">基于随机均匀噪声的数值加噪算法</option>"
                                            html += "<option value = " + 6 + ">基于随机拉普拉斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 7 + ">基于随机高斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 8 + ">数值偏移</option>"
                                            html += "<option value = " + 9 + " selected>数值取整</option>"
                                            html += "<option value = " + 10 + ">数值映射</option>"
                                            break;
                                        case 10:
                                            html += "<option value = " + 3 + ">基于拉普拉斯差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 5 + ">基于随机均匀噪声的数值加噪算法</option>"
                                            html += "<option value = " + 6 + ">基于随机拉普拉斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 7 + ">基于随机高斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 8 + ">数值偏移</option>"
                                            html += "<option value = " + 9 + ">数值取整</option>"
                                            html += "<option value = " + 10 + " selected>数值映射</option>"
                                            break;
                                        default:
                                            html += "<option value = " + 3 + " selected>基于拉普拉斯差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 4 + ">基于高斯机制差分隐私的数值加噪算法</option>"
                                            html += "<option value = " + 5 + ">基于随机均匀噪声的数值加噪算法</option>"
                                            html += "<option value = " + 6 + ">基于随机拉普拉斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 7 + ">基于随机高斯噪声的数值加噪算法</option>"
                                            html += "<option value = " + 8 + ">数值偏移</option>"
                                            html += "<option value = " + 9 + ">数值取整</option>"
                                            html += "<option value = " + 10 + ">数值映射</option>"
                                            break;
                                    }
                                    break;
                                case 1:
                                    html += "<option value = " + 2 + " selected>编码型数据差分隐私脱敏算法</option>"
                                    break;

                                case 3:
                                    switch (s.k) {
                                        case 11:
                                            html += "<option value = " + 11 + " selected>尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + ">编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                        case 13:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + " selected>邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + ">编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                        case 14:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + " selected>地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + ">编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                        case 15:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + " selected>名称抑制算法</option>"
                                            html += "<option value = " + 16 + ">编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                        case 16:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + " selected>编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                        case 17:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + ">编号抑制算法</option>"
                                            html += "<option value = " + 17 + " selected>假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                        case 19:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + ">编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + " selected>密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                        case 20:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + ">编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + " selected>数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                        case 21:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + ">编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + " selected>IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                        case 22:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + ">编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + " selected>IP地址随机抑制</option>"
                                            break;
                                        default:
                                            html += "<option value = " + 11 + ">尾部截断</option>"
                                            html += "<option value = " + 13 + ">邮箱抑制算法</option>"
                                            html += "<option value = " + 14 + ">地址抑制算法</option>"
                                            html += "<option value = " + 15 + ">名称抑制算法</option>"
                                            html += "<option value = " + 16 + " selected>编号抑制算法</option>"
                                            html += "<option value = " + 17 + ">假名化-哈希算法</option>"
                                            html += "<option value = " + 19 + ">密码随机置换</option>"
                                            html += "<option value = " + 20 + ">数值替换</option>"
                                            html += "<option value = " + 21 + ">IP地址全抑制</option>"
                                            html += "<option value = " + 22 + ">IP地址随机抑制</option>"
                                            break;
                                    }
                                    break;
                                case 4:
                                    switch (s.k) {
                                        case 1:
                                            html += "<option value = " + 1 + " selected>基于差分隐私的日期加噪算法</option>"
                                            html += "<option value = " + 18 + ">日期分组置换</option>"
                                            /*html += "<option value = " + 12 + ">时间取整</option>"*/
                                            break;
                                        case 18:
                                            html += "<option value = " + 1 + ">基于差分隐私的日期加噪算法</option>"
                                            html += "<option value = " + 18 + " selected>日期分组置换</option>"
                                            /*html += "<option value = " + 12 + ">时间取整</option>"*/
                                            break;
                                        default:
                                            html += "<option value = " + 1 + " selected>基于差分隐私的日期加噪算法</option>"
                                            html += "<option value = " + 18 + ">日期分组置换</option>"
                                            /*html += "<option value = " + 12 + ">时间取整</option>"*/
                                            break;
                                    }
                                    break;
                            }
                            html += "</select></td>"

                            // 隐私级别
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
                                case 3:
                                    html += "<option value = " + 0 + ">无隐私保护处理</option>"
                                    html += "<option value = " + 1 + ">低程度</option>"
                                    html += "<option value = " + 2 + ">中程度</option>"
                                    html += "<option value = " + 3 + " selected>高程度</option>"
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

                xhr.open("get", "/" + sheet + "param/list", false);
                xhr.send();
                document.getElementById("table_body").innerHTML = html;

                //提交脱敏参数，请求脱敏
                document.getElementById("submit").onclick = function () {
                    let tr;
                    let dataArray = [];
                    let table_body = document.getElementById("table2")
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
                    formData.set("params", JSON.stringify(dataArray));
                    formData.set("algName", "distortion");
                    for (let [key, value] of formData) {
                        console.log("formData:" + key + " " + value);
                    }
                    console.log(dataArray.length);
                    fetch('/File/desenFile', {
                        method: 'POST',
                        body: formData
                    })
                        .then(response => response.blob())
                        .then(blob => {
                            // 脱敏前
                            document.getElementById("preData").innerHTML = "脱敏前数据"
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

                                    $('#totalPages').text(pageCount);
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
                            document.getElementById("afterData").innerHTML = "脱敏后数据"
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

                                    $('#totalPages1').text(pageCount);
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
                            after.appendChild(downloadLink);
                        })
                        .catch(error => console.error('Error:', error));
                }
            } else {
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
                    <label for="choose_template_sheet" style="font-size: 20px">应用场景模板选择</label>
                    <select name="template_sheet" id="choose_template_sheet"
                            style=" display: inline-block; height: 30px; font-size: 20px; text-align: center">
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
                        <option value="marry">婚恋相亲场景</option>
                        <option value="employment">求职招聘场景</option>
                        <option value="onlinelending">网络借贷场景</option>
                        <option value="house">房屋租售场景</option>
                        <option value="usedcar">二手车交易场景</option>
                        <option value="consultation">问诊挂号场景</option>
                        <option value="travel">旅游服务类场景</option>
                        <option value="hotel">酒店服务类场景</option>
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
                        <option value="onlinemovie">在线影音类场景</option>
                        <option value="shortvideo">短视频类场景</option>
                        <option value="news">新闻资讯类场景</option>
                        <option value="sports">运动健身类场景</option>
                        <option value="browser">浏览器类场景</option>
                        <option value="input">输入法类场景</option>
                        <option value="security">安全管理类场景</option>
                        <option value="ebook">电子图书场景</option>
                        <option value="capture">拍摄美化场景</option>
                        <option value="appstore">应用商店场景</option>
                        <option value="tools">实用工具场景</option>
                        <option value="performanceticket">演出票务场景</option>
                        <option value="networkaccess">电话/有线电视入网类场景</option>
                        <option value="telecommunication">电信业务使用类场景</option>
                        <option value="monitor">安防监控类场景</option>
                        <option value="pay">生活缴费类场景</option>
                        <option value="customerservice">客服类场景</option>
                        <option value="schoolservice">校园服务类场景</option>
                        <option value="smarthome">智慧家居类场景</option>
                        <option value="autonomousdriving">自动驾驶类场景</option>
                        <option value="telemedicine">远程诊疗类场景</option>
                        <option value="vr">虚拟现实类场景</option>
                        <option value="onlinevoting">网上投票类场景</option>
                    </select>
                </div>
            </div>
            <div class="midtile">
                <div class="col-sm-5 m-b-xs">
                    <label for="choose_sheet"
                           style="font-size: 20px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;设置需求模板</label>
                    <select name="sheet" id="choose_sheet"
                            style=" display: inline-block; height: 30px; font-size: 20px; text-align: center">
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
                        <option value="marry">婚恋相亲场景</option>
                        <option value="employment">求职招聘场景</option>
                        <option value="onlinelending">网络借贷场景</option>
                        <option value="house">房屋租售场景</option>
                        <option value="usedcar">二手车交易场景</option>
                        <option value="consultation">问诊挂号场景</option>
                        <option value="travel">旅游服务类场景</option>
                        <option value="hotel">酒店服务类场景</option>
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
                        <option value="onlinemovie">在线影音类场景</option>
                        <option value="shortvideo">短视频类场景</option>
                        <option value="news">新闻资讯类场景</option>
                        <option value="sports">运动健身类场景</option>
                        <option value="browser">浏览器类场景</option>
                        <option value="input">输入法类场景</option>
                        <option value="security">安全管理类场景</option>
                        <option value="ebook">电子图书场景</option>
                        <option value="capture">拍摄美化场景</option>
                        <option value="appstore">应用商店场景</option>
                        <option value="tools">实用工具场景</option>
                        <option value="performanceticket">演出票务场景</option>
                        <option value="networkaccess">电话/有线电视入网类场景</option>
                        <option value="telecommunication">电信业务使用类场景</option>
                        <option value="monitor">安防监控类场景</option>
                        <option value="pay">生活缴费类场景</option>
                        <option value="customerservice">客服类场景</option>
                        <option value="schoolservice">校园服务类场景</option>
                        <option value="smarthome">智慧家居类场景</option>
                        <option value="autonomousdriving">自动驾驶类场景</option>
                        <option value="telemedicine">远程诊疗类场景</option>
                        <option value="vr">虚拟现实类场景</option>
                        <option value="onlinevoting">网上投票类场景</option>
                    </select>
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
            <div id="after">
            </div>
            <div class="ibox-content">
                <div id=table_body></div>
            </div>
            <div class="button1">
                <div class="btn2">
                    <button type="button" class="btn btn-sm btn-primary" id="submit"> 提交脱敏</button>
                </div>
            </div>
            <div id="showTable">
            </div>
        </div>
    </div>
</div>
<div>
    <div>
        <span id="preData" class="center-text"></span>
    </div>
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
    <div>
        <span id="afterData" class="center-text"></span>
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
        width: 100%;
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
        width: 100%;
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
</style>
</html>
