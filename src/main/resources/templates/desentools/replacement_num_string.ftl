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
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
</head>

<body >

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
    window.onload = function (){
        document.getElementById("passReplace_submitBtn").addEventListener("click", function (){
            let textInput = $("#passReplace_textInput").val();
            var textType = "name"
            var algName = "passReplace"
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
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("passReplace_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("valueShift_submitBtn").addEventListener("click", function (){
            let textInput = $("#valueShift_input").val();
            let privacyLevel = document.getElementById("valueShift_privacyLevel").value
            var textType = "value"
            var algName = "valueShift"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("valueShift_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("SHA512_submitBtn").addEventListener("click", function (){
            let textInput = $("#SHA512_input").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "SHA512"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("SHA512_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("value_hide_submitBtn").addEventListener("click", function (){
            let textInput = $("#value_hide_input").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "value_hide"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("value_hide_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("valueMapping_submitBtn").addEventListener("click", function (){
            let textInput = $("#valueMapping_input").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "valueMapping"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("valueMapping_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("numberHide_submitBtn").addEventListener("click", function (){
            let textInput = $("#numberHide_textInput").val();
            let privacyLevel = 1
            let textType = "number"
            let algName = "numberHide"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("numberHide_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("nameHide_submitBtn").addEventListener("click", function (){
            let textInput = $("#nameHide_textInput").val();
            let textType = "name"
            let algName = "nameHide"
            let privacyLevel = 1
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("nameHide_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("suppressEmail_submitBtn").addEventListener("click", function (){
            let textInput = $("#suppressEmail_input").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "suppressEmail"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("suppressEmail_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("suppressAllIp_submitBtn").addEventListener("click", function (){
            let textInput = $("#suppressAllIp_input").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "suppressAllIp"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("suppressAllIp_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("suppressIpRandomParts_submitBtn").addEventListener("click", function (){
            let textInput = $("#suppressIpRandomParts_input").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "suppressIpRandomParts"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/File/desenText", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&textInput=' + encodeURIComponent(textInput) +
                    '&textType=' + encodeURIComponent(textType) +
                    '&privacyLevel=' + encodeURIComponent(privacyLevel) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("suppressIpRandomParts_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
    }

</script>
<div class="ibox-title">
</div>

<div class="panel panel-default">

    <div class="panel-body" >
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">1.数值替换</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将字符串中的数字替换成一个常量
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="value_hide_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="value_hide_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="value_hide_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="value_hide_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="panel-body" >
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">2.Shift</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给数值增加一个固定的偏移量
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：数值
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="valueShift_privacyLevel">
                                <option value="1"> 低程度 </option>
                                <option value="2" selected> 中程度 </option>
                                <option value="3"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="valueShift_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                                    <button class="btn btn-default" id="valueShift_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                        提交脱敏
                                    </button>
                                </span>
                            </div>
                            <div class="text-center">
                                <label for="valueShift_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="valueShift_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="panel-body" >
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">3.假名化-哈希算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将数据映射为定长hash值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="SHA512_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="SHA512_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="SHA512_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="SHA512_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="panel-body" >
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">4.数值映射</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将数据映射为新值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：数值
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="valueMapping_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="valueMapping_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="valueMapping_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="valueMapping_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">5.密码置换算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

                <div>
                    <p style="font-size: 1.5em; text-align: justify;">
                        说明：对于密码数据等字段，生成随机长度的随机字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
            </div>
            <div class="container" >
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5" >
                        <div class="input-group" >
                            <input type="text" id="passReplace_textInput" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                            <span class="input-group-btn" >
                    <button class="btn btn-default" id="passReplace_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                        </div>
                        <div class="text-center">
                            <label for="passReplace_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="passReplace_outputText" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">6.名称抑制算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">6.名称抑制算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：从第2个字符用*代替
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
            </div>
            <div class="container" >
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5" >
                        <div class="input-group" >
                            <input type="text" id="nameHide_textInput" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                            <span class="input-group-btn" >
                    <button class="btn btn-default" id="nameHide_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                        </div>
                        <div class="text-center">
                            <label for="nameHide_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="nameHide_outputText" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">7.编号抑制算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">7.编号抑制算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：隐藏中间部分的数据，用*代替
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：字符串
                    </p>

                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
            </div>
            <div class="container" >
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5" >
                        <div class="input-group" >
                            <input type="text" id="numberHide_textInput" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                            <span class="input-group-btn" >
                                <button class="btn btn-default" id="numberHide_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                    提交脱敏
                                </button>
                            </span>
                        </div >
                        <div class="text-center">
                            <label for="numberHide_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="numberHide_outputText" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="panel-body" >
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">8.邮箱抑制算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：用*@*代替邮箱地址
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：邮箱地址
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：邮箱地址
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="suppressEmail_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="suppressEmail_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="suppressEmail_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="suppressEmail_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="panel-body" >
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">9.IP地址全抑制</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：用*替代ip地址各个部分
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：IP地址
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：IP地址
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="suppressAllIp_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="suppressAllIp_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="suppressAllIp_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="suppressAllIp_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="panel-body" >
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">10.IP地址随机抑制</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：随机将IP的一部分用*代替
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：IP地址
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：IP地址
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="suppressIpRandomParts_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="suppressIpRandomParts_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="suppressIpRandomParts_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="suppressIpRandomParts_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
    .showFile{
        display: flex;
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
    .upload-btn, #replace_region_submit, #replace_region_video_submit, #add_beep_submit{
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }
    #replace_region_after, #replace_region_pre, #add_beep_after, #add_beep_pre{
        text-align: center;
    }
    image, video {
        display: inline-block;
        max-width: 50%;
        height: auto
    }

</style>

</html>
