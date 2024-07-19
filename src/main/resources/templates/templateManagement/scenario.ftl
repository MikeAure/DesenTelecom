<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>应用场景模板管理</title>
    <meta name="keywords" content="">
    <meta name="description" content="">
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/bootstrap-table/bootstrap-table.min.css" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/css/style.css" rel="stylesheet">
    <link href="${ctx!}/css/GA.css" rel="stylesheet">
</head>
<!-- 全局js -->
<script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/js/bootstrap.min.js?v=3.3.6"></script>

<!-- Bootstrap table -->
<script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
<script src="${ctx!}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

<!-- Peity -->
<script src="${ctx!}/js/plugins/peity/jquery.peity.min.js"></script>
<script src="${ctx!}/js/plugins/layer/layer.min.js"></script>

<!-- 自定义js -->
<script src="${ctx!}/js/content.js"></script>
<style>
    .bootstrap-table .fixed-table-container th {
        text-align: center;
    }
</style>
<script type="text/javascript">
    // 保存原有数据，假设表格初始时就有一些数据
    let existingData;

    const fieldTypeOptions = [
        { value: '0', label: "数值型数据" },
        { value: '1', label: "编码型数据" },
        { value: '3', label: "文本型数据" },
        { value: '4', label: "时间数据" },
    ];

    const maskAlgorithmOptions = {
        '0': [
            { value: '3', label: "基于拉普拉斯差分隐私的数值加噪算法" },
            { value: '5', label: "基于随机均匀噪声的数值加噪算法" },
            { value: '6', label: "基于随机拉普拉斯噪声的数值加噪算法" },
            { value: '7', label: "基于随机高斯噪声的数值加噪算法" },
            { value: '8', label: "数值偏移" },
            { value: '9', label: "数值取整" },
            { value: '10', label: "数值映射" },
            // Add options based on fieldType if needed
        ],
        '1': [
            { value: '2', label: "编码型数据差分隐私脱敏算法" },

            // Add options based on fieldType if needed
        ],
        '3': [
            { value: '11', label: "尾部截断" },
            { value: '13', label: "邮箱抑制算法" },
            { value: '14', label: "地址抑制算法" },
            { value: '15', label: "名称抑制算法" },
            { value: '16', label: "编号抑制算法" },
            { value: '17', label: "假名化-哈希算法" },
            { value: '19', label: "密码随机置换" },
            { value: '20', label: "数值替换" },
            { value: '21', label: "IP地址全抑制" },
            { value: '22', label: "IP地址随机抑制" },
            // Add options based on fieldType if needed
        ],
        '4': [
            { value: '1', label: "基于差分隐私的日期加噪算法" },
            { value: '18', label: "日期分组置换" },
            // Add options based on fieldType if needed
        ],
    };

    const privacyLevelOptions = [
        { value: '0', label: "无隐私保护处理" },
        { value: '1', label: "低程度" },
        { value: '2', label: "中程度" },
        { value: '3', label: "高程度" },
    ];

    let currentSceneValue = "";
    let sheet = "";
    let suffix = "";
    let sceneName = "";


    window.onload = function () {
        currentSceneValue = document.getElementById("choose_transfer_scene").value;
        sheet = document.getElementById("choose_sheet").value;
        document.getElementById("choose_transfer_scene").addEventListener("change", function() {
            currentSceneValue = this.value;
            console.log("Selected value:", currentSceneValue);

            switch (currentSceneValue) {
                case "1":
                    suffix = "_low";
                    break;
                case "2":
                    suffix = "_medium";
                    break;
                case "3":
                    suffix = "_high"
                    break;
                default:
                    suffix = "";
                    break;
            }
            sceneName = sheet + suffix;
            console.log(suffix);
            console.log("Current scene name: " + sceneName);

        });

        // 选择场景数据表
        document.getElementById("choose_sheet").addEventListener("change", function() {
            sheet = this.value;
            sceneName = sheet + suffix;
            console.log("Selected template:", sheet);
            console.log("Current scene name: " + sceneName);
            console.log(document.getElementById("choose_sheet").value);
            document.getElementById('tabletitle').style.display = 'block';
            document.getElementById('button').style.display = 'block';
            choose_sheet();
        });


        // 添加一行到表格
        document.getElementById("add_row").onclick = function () {
            var tableBody = document.getElementById("table_body");

            // 全局变量用于追踪id的值
            var nextId = tableBody.rows.length + 1;

            // 插入新行
            var newRow = tableBody.insertRow(tableBody.rows.length);

            // 插入id列
            var idCell = newRow.insertCell(0);
            idCell.textContent = nextId;

            // 根据你的数据结构进行相应的定制
            for (var i = 1; i < 3; i++) {
                var cell = newRow.insertCell(i);
                cell.innerHTML = "<span contenteditable='true'>请填入数据</span>";
            }

            // 添加下拉框作为 "dataType" 单元格
            var dataTypeCell = newRow.insertCell(3);
            var dataTypeDropdown = document.createElement("select");
            dataTypeDropdown.innerHTML = "<option value='0'>数值型数据</option>" +
                "<option value='1'>编码型数据</option>" +
                "<option value='3'>文本型数据</option>" +
                "<option value='4'>时间数据</option>";

            dataTypeCell.appendChild(dataTypeDropdown);

            // 添加删除按钮
            var deleteCell = newRow.insertCell(4);
            deleteCell.innerHTML = "<button type='button' onclick='deleteRow(this)'>删除</button>";
            newRow.classList.add("new-row");
            // 更新全局id变量
            nextId++;

        };

    }

    // 通过 fieldName 查找 existingData 中的数据
    function findExistingDataByFieldName(fieldName) {
        for (var i = 0; i < existingData.length; i++) {
            if (existingData[i].fieldName === fieldName) {
                return existingData[i];
            }
        }
        return null; // 如果找不到对应的数据，返回 null
    }


    // 保存按钮点击事件
    function saveData() {
        var tableBody = document.getElementById("table_body");
        var dataToSend = [];

        for (var i = 0; i < tableBody.rows.length; i++) {
            var rowData = {};
            var tr = tableBody.rows[i];

            // 获取每一行的数据
            rowData.id = tr.cells[0].textContent; // 设置 'id' 值
            rowData.fieldName = tr.cells[1].innerText;
            rowData.columnName = tr.cells[2].innerText;

            // 获取dataType的值
            var dataTypeCell = tr.cells[3];
            var dataTypeDropdown = dataTypeCell.querySelector('select');
            // 调试输出
            if (dataTypeDropdown) {
                rowData.dataType = dataTypeDropdown.value;
            } else {
                rowData.dataType=getDataTypeValue(tr.cells[3].innerText)
            }


            // 判断是否是新添加的行
            if (tr.classList.contains("new-row")) {
                rowData.tmParam = 1;
                switch(rowData.dataType) {
                    case '0':
                        rowData.k = 3;
                        break;
                    case '1':
                        rowData.k = 2;
                        break;
                    case '3':
                        rowData.k = 11;
                        break
                    case '4':
                        rowData.k = 1;
                        break;

                }
                // rowData.k = 0;
                // rowData.tmParam = 0;
            } else {
                // 如果不是新添加的行，通过 fieldName 查找对应的值
                let existingDataRow = findExistingDataByFieldName(rowData.fieldName);

                if (existingDataRow) {
                    // 找到对应的值，保留原有的 rowData.k 和 rowData.tmParam 值
                    rowData.k = existingDataRow.k;
                    rowData.tmParam = existingDataRow.tmParam;
                } else {
                    console.error("Error: Could not find existing data for fieldName: " + rowData.fieldName);
                }
            }
            // 将行数据添加到待发送数据数组
            dataToSend.push(rowData);
        }

        // 发送数据到后端
        // var selectedSheet = document.getElementById("choose_sheet").value;
        fetch('/Param/saveData?tableName='+ sceneName +'_param', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
            },
            body: JSON.stringify(dataToSend),
        })
            .then(response => {
                if (response.ok) {
                    alert("数据保存成功！");
                } else {
                    alert("数据保存失败，请重试。");
                }
            })
            .catch(error => console.error("Failed to save data:", error));
    }

    // 获取数据类型值
    function getDataTypeValue(dataTypeText) {
        switch (dataTypeText) {
            case "数值型数据":
                return 0;
            case "编码型数据":
                return 1;
            case "文本型数据":
                return 3;
            case "时间数据":
                return 4;
            default:
                return -1;
        }
    }

    // 删除行的函数
    function deleteRow(button) {
        var rowToDelete = button.parentNode.parentNode; // 获取要删除的行
        var tableBody = document.getElementById("table_body");

        // 发送数据到后端
        var selectedSheet = document.getElementById("choose_sheet").value;

        // 获取被删除行的id值
        var deletedId = parseInt(rowToDelete.cells[0].textContent, 10);

        // 如果是新添加的行，直接删除
        if (rowToDelete.classList.contains("new-row")) {
            rowToDelete.parentNode.removeChild(rowToDelete);
            // 更新剩余行的id列的值
            for (var i = deletedId - 1; i < tableBody.rows.length; i++) {
                tableBody.rows[i].cells[0].textContent = i + 1;
            }


        } else {
            // 否则，弹出确认框
            var confirmDelete = confirm("是否确定要删除此行数据？");

            if (confirmDelete) {

                // 用户点击确定，执行删除操作

                fetch('/Param/deleteData/' + deletedId+'?tableName='+sceneName+'_param', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8',
                    },
                })
                .then(response => {
                    if (response.ok) {
                        // 删除成功，从页面上删除行
                        rowToDelete.parentNode.removeChild(rowToDelete);

                        // 更新剩余行的id列的值
                        for (let i = deletedId - 1; i < tableBody.rows.length; i++) {
                            tableBody.rows[i].cells[0].textContent = i + 1;
                        }
                        alert("数据删除成功！");
                        saveData();
                    } else {
                        alert("数据删除失败，请重试。");
                    }
                })
                .catch(error => console.error("Failed to delete data:", error));

            }
            // 如果用户点击取消，则不执行删除操作
        }
    }

    // function createDropdown(options, selectedValue, onChangeHandler) {
    //     let dropdown = '<select' + (onChangeHandler ? ' onchange="' + onChangeHandler + '"' : '') + '>';
    //     for (let i = 0; i < options.length; i++) {
    //         let option = options[i];
    //         dropdown += '<option value="' + option.value + '"' + (option.value == selectedValue ? ' selected' : '') + '>' + option.label + '</option>';
    //     }
    //     dropdown += '</select>';
    //     return dropdown;
    // }

    // function updateAlgorithmOptions(rowId, dataType) {
    //     let algorithmSelect = document.getElementById('algorithm_' + rowId);
    //     let options = maskAlgorithmOptions[dataType] || [];
    //     algorithmSelect.innerHTML = '';
    //     for (let i = 0; i < options.length; i++) {
    //         let option = document.createElement('option');
    //         option.value = options[i].value;
    //         option.text = options[i].label;
    //         algorithmSelect.add(option);
    //     }
    // }

    // function choose_sheet() {
    //     let selectedSheet = document.getElementById("choose_sheet").value;
    //     let requestUrl = '/Param/tablelist?tableName=' + selectedSheet + '_param';
    //     fetch(requestUrl, {
    //         method: 'GET',
    //         headers: {
    //             'Content-Type': 'application/json;charset=UTF-8',
    //         },
    //     })
    //         .then(function(response) {
    //             if (response.ok) {
    //                 return response.json();
    //             } else {
    //                 console.error("Failed to fetch data. Status code: " + response.status);
    //             }
    //         })
    //         .then(function(data) {
    //             let html = "";
    //             for (let i = 0; i < data.length; i++) {
    //                 let s = data[i];
    //                 html += '<tr id="row_' + i + '">';
    //                 html += '<td>' + s.id + '</td>';
    //                 html += '<td>' + s.fieldName + '</td>';
    //                 html += '<td>' + s.columnName + '</td>';
    //                 html += '<td>' + createDropdown(fieldTypeOptions, s.dataType, 'updateAlgorithmOptions(' + i + ', this.value)') + '</td>';
    //                 html += '<td><select id="algorithm_' + i + '">' + createDropdown(maskAlgorithmOptions[s.dataType] || [], s.k) + '</select></td>';
    //                 html += '<td>' + createDropdown(privacyLevelOptions, s.tmParam) + '</td>';
    //                 html += '<td><button type="button" onclick="deleteRow(this)">删除</button></td>';
    //                 html += '</tr>';
    //             }
    //             document.getElementById("table_body").innerHTML = html;
    //         })
    //         .catch(function(error) {
    //             console.error("Failed to fetch data:", error);
    //         });
    // }


    // 选择表渲染页面
     function choose_sheet () {
        // 发送请求的代码
        let selectedSheet = document.getElementById("choose_sheet").value;
        // xhr.open('GET', '/Param/' + selectedSheet + 'list', true); // 根据选择的场景动态设置请求路径
    
    
        // 构建请求路径
        let requestUrl = '/Param/tablelist?tableName=' + sceneName+'_param';
        fetch(requestUrl, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8',
            },
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    console.error("Failed to fetch data. Status code: " + response.status);
                }
            })
            .then(data => {
                existingData = data;
                let html = ""
                for (let i = 0; i < data.length; i++) {
                    s = data[i];
                    html += "<tr id =" + "row_" + i + ">";
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
                        default:
                            html += "未知数据类型";
                            break;
                    }
                    html += "</td>";
                    // 添加删除按钮列
                    html += "<td><button type='button' onclick='deleteRow(this)'>删除</button></td>";
                    html += "</tr>";
    
                }
                document.getElementById("table_body").innerHTML = html
            })
            .catch(error => console.error("Failed to fetch data:", error));
        // var xhr = new XMLHttpRequest();
        // xhr.onreadystatechange = function () {
        //     if (xhr.status === 200 && xhr.readyState === 4) {
        //         var data_str = xhr.responseText;
        //         var data = JSON.parse(data_str);
        //         existingData=data;
        //         var html = ""
        //         for (var i = 0; i < data.length; i++) {
        //             s = data[i];
        //             html += "<tr id =" + "row_" + i + ">";
        //             html += "<td>" + s.id + "</td>";
        //             html += "<td>" + s.fieldName + "</td>";
        //             html += "<td>" + s.columnName + "</td>";
        //             html += "<td>";
        //             switch (s.dataType) {
        //                 case 0:
        //                     html += "数值型数据";
        //                     break;
        //                 case 1:
        //                     html += "编码型数据";
        //                     break;
        //                 case 3:
        //                     html += "文本型数据";
        //                     break;
        //                 case 4:
        //                     html += "时间数据";
        //                     break;
        //                 default:
        //                     html += "未知数据类型";
        //             }
        //             html += "</td>";
        //             // 添加删除按钮列
        //             html += "<td><button type='button' onclick='deleteRow(this)'>删除</button></td>";
        //             html += "</tr>";
        //
        //         }
        //         document.getElementById("table_body").innerHTML = html
        //     } else {
        //         console.error("Failed to fetch data. Status code: " + xhr.status);
        //     }
        // }
        //
        // xhr.open('GET', requestUrl, true); // 根据选择的场景动态设置请求路径
        // xhr.send();
    }


</script>
<div class="row">
    <div class="col-sm-12">
        <div class="ibox">
            <div class="ibox-title">
                <h5>应用场景模板管理</h5>
            </div>
            <div class="ibox-content" style="text-align: center">
                <div style="margin-left:0px;margin-top:0px;width:100%;">
                    <form id="fileForm" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="choose_transfer_scene" >请选择流转场景：</label>
                            <select name="transfer_scene" id="choose_transfer_scene"
                                    >
                                <option value="" selected>请选择流转场景</option>
                                <option value="1">用户本地</option>
                                <option value="2">同机构不同系统</option>
                                <option value="3">不同机构不同系统</option>
                            </select>
                        </div>
                        <div class="form-group"  style="margin-bottom: 20px;">
                            <label for="choose_sheet">请选择应用场景：</label>
                            <select name="sheet" id="choose_sheet">
                                <#-- 这里有50个按钮，对应50个场景-->
                                <option value="" selected>请选择应用场景</option>
                                <option value="appStore">应用商店场景</option>
                                <option value="autonomousdriving">自动驾驶类场景</option>
                                <option value="bank">手机银行类场景</option>
                                <option value="browser">浏览器类场景</option>
                                <option value="capture">拍摄美化场景</option>
                                <option value="communication">即时通信场景</option>
                                <option value="community">网络社区类场景</option>
                                <option value="consultation">问诊挂号场景</option>
                                <option value="customerservice">客服类场景</option>
                                <option value="ebook">电子图书场景</option>
                                <option value="education">学习教育类场景</option>
                                <option value="employment">求职招聘场景</option>
                                <option value="express">邮件快件寄递场景</option>
                                <option value="game">网络游戏类场景</option>
                                <option value="hotel">酒店服务类场景</option>
                                <option value="house">房屋租售场景</option>
                                <option value="input">输入法类场景</option>
                                <option value="investment">投资理财类场景</option>
                                <option value="localLife">本地生活类场景</option>
                                <option value="mailbox">邮箱云盘类场景</option>
                                <option value="marry">婚恋相亲场景</option>
                                <option value="map">地图导航类场景</option>
                                <option value="meeting">远程会议类场景</option>
                                <option value="monitor">安防监控类场景</option>
                                <option value="networkaccess">电话/有线电视入网类场景</option>
                                <option value="news">新闻资讯类场景</option>
                                <option value="onlineLending">网络借贷场景</option>
                                <option value="onlineMovie">在线影音类场景</option>
                                <option value="onlinePayment">网络支付类场景</option>
                                <option value="onlineShopping">网上购物类场景</option>
                                <option value="onlineTaxi">网络约车类场景</option>
                                <option value="onlinevoting">网上投票类场景</option>
                                <option value="pay">生活缴费类场景</option>
                                <option value="performanceTicket">演出票务场景</option>
                                <option value="schoolservice">校园服务类场景</option>
                                <option value="security">安全管理类场景</option>
                                <option value="shortVideo">短视频类场景</option>
                                <option value="smarthome">智慧家居类场景</option>
                                <option value="sports">运动健身类场景</option>
                                <option value="takeaway">餐饮外卖类场景</option>
                                <option value="telecommunication">电信业务使用类场景</option>
                                <option value="telemedicine">远程诊疗类场景</option>
                                <option value="tools">实用工具场景</option>
                                <option value="transportationTicket">交通票务场景</option>
                                <option value="travel">旅游服务类场景</option>
                                <option value="useCar">用车服务类场景</option>
                                <option value="usedCar">二手车交易场景</option>
                                <option value="vr">虚拟现实类场景</option>
                                <option value="webcast">网络直播类场景</option>
                                <option value="woman">女性健康类场景</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="table-responsive" id="tabletitle" style="display: none">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th style="text-align: center">编号</th>
                            <th style="text-align: center">字段缩写</th>
                            <th style="text-align: center">字段名</th>
                            <th style="text-align: center">字段类型</th>

                        </tr>
                        </thead>
                        <tbody id="table_body">

                        </tbody>
                    </table>
                </div>
            </div>
            <div id="button" style="width: 100%; margin-bottom: 20px; text-align: center; display: none">
                <button id="add_row" type="button" style="text-align: center">添加</button>
                <button id="save_button" type="button" style="text-align: center" onclick="saveData()">保存</button>
            </div>

        </div>
    </div>
</div>
</body>
</html>
