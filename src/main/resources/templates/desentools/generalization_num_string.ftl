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
        // 时间取整
        document.getElementById("floorTime_submitBtn").addEventListener("click", function (){
            let textInput = $("#floorTime_input").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "floorTime"
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
                    document.getElementById("floorTime_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        // 数值取整
        document.getElementById("floor_submitBtn").addEventListener("click", function (){
            let textInput = $("#floor_input").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "floor"
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
                    document.getElementById("floor_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        // 截断
        document.getElementById("truncation_submitBtn").addEventListener("click", function (){
            let textInput = $("#truncation_input").val();
            var privacyLevel = 1
            var textType = "address"
            var algName = "truncation"
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
                    document.getElementById("truncation_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("addressHide_submitBtn").addEventListener("click", function (){
            let textInput = $("#addressHide_textInput").val();
            var privacyLevel = 1
            var textType = "addressHide"
            var algName = "addressHide"
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
                    document.getElementById("addressHide_outputText").value = data;
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">1.尾部截断</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：尾部截断只保留前3位
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
                                <input type="text" id="truncation_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="truncation_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="truncation_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="truncation_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">2.数值取整</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对数值取整
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
                                <input type="text" id="floor_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="floor_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="floor_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="floor_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">3.时间取整</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对时间取整，格式为12:30:45
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：时间字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：时间字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="floorTime_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="floorTime_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="floorTime_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="floorTime_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">4.地址抑制算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">1.地址抑制算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：隐藏具体地址信息
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：地址
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：地址
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
            </div>
            <div class="container" >
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5" >
                        <div class="input-group" >
                            <input type="text" id="addressHide_textInput" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                            <span class="input-group-btn" >
                    <button class="btn btn-default" id="addressHide_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                        </div>
                        <div class="text-center">
                            <label for="addressHide_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="addressHide_outputText" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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

</style>

</html>
