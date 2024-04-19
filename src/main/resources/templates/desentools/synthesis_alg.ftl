<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta name="referrer" content="no-referrer">

    <title> 脱敏</title>

    <meta name="keywords" content="">
    <meta name="description" content="">

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
</head>

<body>

<!-- 全局js -->
<script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx!}/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="${ctx!}/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="${ctx!}/js/plugins/layer/layer.min.js"></script>

<!-- 自定义js -->
<script src="${ctx!}/js/hAdmin.js?v=4.1.0"></script>
<script type="text/javascript" src="${ctx!}/js/index.js"></script>
<script type="text/javascript">
    window.onload = function () {
        document.getElementById("submitBtn").addEventListener("click", function () {
            let textInput = $("#textInput").val();
            let privacyLevel = document.getElementById("privacyLevel").value
            var textType = "value"
            var algName = "text"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("time_submitBtn").addEventListener("click", function () {
            let textInput = $("#time_textInput").val();
            let privacyLevel = document.getElementById("time_privacyLevel").value
            var textType = "time"
            var algName = "text"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("time_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("code_submitBtn").addEventListener("click", function () {
            let textInput = $("#code_textInput").val();
            let privacyLevel = document.getElementById("code_privacyLevel").value
            var textType = "code"
            var algName = "text"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("code_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("address_submitBtn").addEventListener("click", function () {
            let textInput = $("#address_textInput").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "text"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("address_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("number_submitBtn").addEventListener("click", function () {
            let textInput = $("#number_textInput").val();
            var privacyLevel = 1
            var textType = "number"
            var algName = "text"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("number_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("name_submitBtn").addEventListener("click", function () {
            let textInput = $("#name_textInput").val();
            var textType = "name"
            var algName = "text"
            var privacyLevel = 1
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("name_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
    }

</script>
<div class="ibox-title">
</div>

<div class="panel panel-default">
    <div class="panel-heading" style="text-align: center;">
        <h1 class="panel-title"><b style="font-size: 2em">差分隐私算法</b></h1>
    </div>
    <div class="panel-body">
        <div class="row">
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: space-between; width: 50%; margin: 0 auto;">
                <p style="font-size: 1.5em;">1.数值型数据差分隐私脱敏算法</p>
                <div>
                    <p style="font-size: 1.5em; text-indent: 2em; text-align: justify;">
                        数值型数据差分隐私脱敏算法，主要是通过向数值数据中添加噪声实现的。该算法的输入是待脱敏数值数据、隐私保护级别，输出是加噪后的数值数据，
                        不同的隐私级别可以设置不同的隐私预算，具体来说，低、中和高隐私保护级别对应的隐私预算分别为10、1和0.1。
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="privacyLevel">
                                <option value="0"> 低程度</option>
                                <option value="1" selected> 中程度</option>
                                <option value="2"> 高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="textInput" class="form-control" placeholder="请输入文本"
                                       style="font-size: 20px">
                                <span class="input-group-btn">
                    <button class="btn btn-default" id="submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="outputText"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="outputText" rows="2" cols="50" readonly
                                              style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="row">
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: space-between; width: 50%; margin: 0 auto;">
                <p style="font-size: 1.5em;">2.时间型数据差分隐私脱敏算法</p>
                <div>
                    <p style="font-size: 1.5em; text-indent: 2em; text-align: justify;">
                        时间型数据差分隐私脱敏算法，主要是通过向时间数据中添加噪声实现的。该算法的输入是待脱敏时间数据、隐私保护级别，输出是加噪后的时间数据，
                        不同的隐私级别可以设置不同的隐私预算，具体来说，低、中和高隐私保护级别对应的隐私预算分别为0.1、0.01和0.001。
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="time_privacyLevel">
                                <option value="0"> 低程度</option>
                                <option value="1" selected> 中程度</option>
                                <option value="2"> 高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="time_textInput" class="form-control" placeholder="请输入文本"
                                       style="font-size: 20px">
                                <span class="input-group-btn">
                    <button class="btn btn-default" id="time_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="time_outputText"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="time_outputText" rows="2" cols="50" readonly
                                              style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="row">
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: space-between; width: 50%; margin: 0 auto;">
                <p style="font-size: 1.5em;">3.编码型数据差分隐私脱敏算法</p>
                <div>
                    <p style="font-size: 1.5em; text-indent: 2em; text-align: justify;">
                        编码型数据差分隐私脱敏算法，该算法的输入是待脱敏编码型数据、隐私保护级别，输出是加噪后的时间数据，
                        不同的隐
                        私级别可以设置不同的隐私预算，具体来说，低、中和高隐私保护级别对应的隐私预算分别为3.7、2和0.7。
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="code_privacyLevel">
                                <option value="0"> 低程度</option>
                                <option value="1" selected> 中程度</option>
                                <option value="2"> 高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="code_textInput" class="form-control" placeholder="请输入文本"
                                       style="font-size: 20px">
                                <span class="input-group-btn">
                    <button class="btn btn-default" id="code_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="code_outputText"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="code_outputText" rows="2" cols="50" readonly
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
    <div class="panel-heading" style="text-align: center;">
        <h1 class="panel-title"><b style="font-size: 2em">文本匿名化算法</b></h1>
    </div>
    <div class="panel-body">
        <div class="row">
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: space-between; width: 50%; margin: 0 auto;">
                <p style="font-size: 1.5em;">1.地址型数据匿名化算法</p>
                <div>
                    <p style="font-size: 1.5em; text-indent: 2em; text-align: justify;">
                        地址型数据匿名化算法，该算法的输入是待脱敏地址数据，输出是匿名化后的数据。
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
            </div>
            <div class="container">
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5">
                        <div class="input-group">
                            <input type="text" id="address_textInput" class="form-control" placeholder="请输入文本"
                                   style="font-size: 20px">
                            <span class="input-group-btn">
                    <button class="btn btn-default" id="address_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                        </div>
                        <div class="text-center">
                            <label for="address_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="address_outputText" rows="2" cols="50" readonly
                                          style="margin-top: 10px;"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="row">
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: space-between; width: 50%; margin: 0 auto;">
                <p style="font-size: 1.5em;">2.编号型数据匿名化算法</p>
                <div>
                    <p style="font-size: 1.5em; text-indent: 2em; text-align: justify;">
                        编号型数据匿名化算法，该算法的输入是待脱敏编号型数据，输出是匿名化后的数据。
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
            </div>
            <div class="container">
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5">
                        <div class="input-group">
                            <input type="text" id="number_textInput" class="form-control" placeholder="请输入文本"
                                   style="font-size: 20px">
                            <span class="input-group-btn">
                                <button class="btn btn-default" id="number_submitBtn" type="button"
                                        style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                    提交脱敏
                                </button>
                            </span>
                        </div>
                        <div class="text-center">
                            <label for="number_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="number_outputText" rows="2" cols="50" readonly
                                          style="margin-top: 10px;"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-body">
        <div class="row">
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: space-between; width: 50%; margin: 0 auto;">
                <p style="font-size: 1.5em;">3.名称类数据匿名化算法</p>
                <div>
                    <p style="font-size: 1.5em; text-indent: 2em; text-align: justify;">
                        名称类数据匿名化算法，该算法的输入是待脱敏名称类数据，输出是匿名化后的数据，
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
            </div>
            <div class="container">
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5">
                        <div class="input-group">
                            <input type="text" id="name_textInput" class="form-control" placeholder="请输入文本"
                                   style="font-size: 20px">
                            <span class="input-group-btn">
                    <button class="btn btn-default" id="name_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                        </div>
                        <div class="text-center">
                            <label for="name_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="name_outputText" rows="2" cols="50" readonly
                                          style="margin-top: 10px;"></textarea>
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
    .ibox-title {
        height: 200px;
        border-color: #edf1f2;
        background-color: #dbeafe;
        color: black;
        display: flex;
    }

    textarea {
        font-size: 1.5em;
    }

</style>

</html>
