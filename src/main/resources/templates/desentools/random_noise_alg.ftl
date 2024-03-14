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
        document.getElementById("randomUniformToValue_submitBtn").addEventListener("click", function (){
            let textInput = $("#randomUniformToValue_input").val();
            let privacyLevel = document.getElementById("randomUniformToValue_privacyLevel").value
            var textType = "value"
            var algName = "randomUniformToValue"
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
                    document.getElementById("randomUniformToValue_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("randomLaplaceToValue_submitBtn").addEventListener("click", function (){
            let textInput = $("#randomLaplaceToValue_input").val();
            let privacyLevel = document.getElementById("randomLaplaceToValue_privacyLevel").value
            var textType = "value"
            var algName = "randomLaplaceToValue"
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
                    document.getElementById("randomLaplaceToValue_output").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("randomGaussianToValue_submitBtn").addEventListener("click", function (){
            let textInput = $("#randomGaussianToValue_input").val();
            let privacyLevel = document.getElementById("randomGaussianToValue_privacyLevel").value
            var textType = "value"
            var algName = "randomGaussianToValue"
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
                    document.getElementById("randomGaussianToValue_output").value = data;
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

        document.getElementById("noisy_hist2_submitBtn").addEventListener("click", function (){
            let textInput = $("#noisy_hist2_textInput").val();
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "noisy_hist2"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/RandomNoise/desenValue", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&rawData=' + encodeURIComponent(textInput) +
                    /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                    '&samples=' + encodeURIComponent(1) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("noisy_hist2_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("report_noisy_max3_submitBtn").addEventListener("click", function (){
            let textInput = $("#report_noisy_max3_textInput").val();
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "report_noisy_max3"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/RandomNoise/desenValue", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&rawData=' + encodeURIComponent(textInput) +
                    /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                    '&samples=' + encodeURIComponent(1) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("report_noisy_max3_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("report_noisy_max4_submitBtn").addEventListener("click", function (){
            let textInput = $("#report_noisy_max4_textInput").val();
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "report_noisy_max4"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/RandomNoise/desenValue", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&rawData=' + encodeURIComponent(textInput) +
                    /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                    '&samples=' + encodeURIComponent(1) +
                    '&algName=' + encodeURIComponent(algName)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("report_noisy_max4_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("sparse_vector_technique3_submitBtn").addEventListener("click", function (){
            let textInput = $("#sparse_vector_technique3_textInput").val();
            let c = $("#sparse_vector_technique3_c").val()
            let t = $("#sparse_vector_technique3_t").val()
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "sparse_vector_technique3"
            if (textInput === "") {
                alert("请输入数据");
                return; // Stop further execution if the text input is empty
            }

            fetch("/RandomNoise/desenValue2", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&rawData=' + encodeURIComponent(textInput) +
                    /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                    '&samples=' + encodeURIComponent(1) +
                    '&algName=' + encodeURIComponent(algName) +
                    '&c=' + encodeURIComponent(c) +
                    '&t=' + encodeURIComponent(t)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("sparse_vector_technique3_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("sparse_vector_technique4_submitBtn").addEventListener("click", function (){
            let textInput = $("#sparse_vector_technique4_textInput").val();
            let c = $("#sparse_vector_technique4_c").val()
            let t = $("#sparse_vector_technique4_t").val()
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "sparse_vector_technique4"
            if (textInput === "") {
                alert("请输入数据");
                return; // Stop further execution if the text input is empty
            }

            fetch("/RandomNoise/desenValue2", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&rawData=' + encodeURIComponent(textInput) +
                    /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                    '&samples=' + encodeURIComponent(1) +
                    '&algName=' + encodeURIComponent(algName) +
                    '&c=' + encodeURIComponent(c) +
                    '&t=' + encodeURIComponent(t)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("sparse_vector_technique4_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("sparse_vector_technique5_submitBtn").addEventListener("click", function (){
            let textInput = $("#sparse_vector_technique5_textInput").val();
            let c = $("#sparse_vector_technique5_c").val()
            let t = $("#sparse_vector_technique5_t").val()
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "sparse_vector_technique5"
            if (textInput === "") {
                alert("请输入数据");
                return; // Stop further execution if the text input is empty
            }

            fetch("/RandomNoise/desenValue2", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&rawData=' + encodeURIComponent(textInput) +
                    /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                    '&samples=' + encodeURIComponent(1) +
                    '&algName=' + encodeURIComponent(algName) +
                    '&c=' + encodeURIComponent(c) +
                    '&t=' + encodeURIComponent(t)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("sparse_vector_technique5_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("sparse_vector_technique6_submitBtn").addEventListener("click", function (){
            let textInput = $("#sparse_vector_technique6_textInput").val();
            let c = $("#sparse_vector_technique6_c").val()
            let t = $("#sparse_vector_technique6_t").val()
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "sparse_vector_technique6"
            if (textInput === "") {
                alert("请输入数据");
                return; // Stop further execution if the text input is empty
            }

            fetch("/RandomNoise/desenValue2", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:  '&rawData=' + encodeURIComponent(textInput) +
                    /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                    '&samples=' + encodeURIComponent(1) +
                    '&algName=' + encodeURIComponent(algName) +
                    '&c=' + encodeURIComponent(c) +
                    '&t=' + encodeURIComponent(t)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("sparse_vector_technique6_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })

    }

</script>
<div class="ibox-title">
</div>

<div class="panel panel-default">
    <div class="panel-heading"  style="text-align: center;">
        <h1 class="panel-title"><b style="font-size: 2em">随机加噪算法</b></h1>
    </div>
    <div class="panel-body" >
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">1.基于随机均匀噪声的数值加噪算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向数值中加入均匀分布的随机噪声
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
                            <select id="randomUniformToValue_privacyLevel">
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
                                <input type="text" id="randomUniformToValue_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="randomUniformToValue_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="randomUniformToValue_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="randomUniformToValue_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">2.基于随机高斯噪声的数值加噪算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向数值中加入高斯噪声
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
                            <select id="randomGaussianToValue_privacyLevel">
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
                                <input type="text" id="randomGaussianToValue_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="randomGaussianToValue_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="randomGaussianToValue_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="randomGaussianToValue_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">3.基于随机拉普拉斯噪声的数值加噪算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向数值中加入拉普拉斯噪声
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
                            <select id="randomLaplaceToValue_privacyLevel">
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
                                <input type="text" id="randomLaplaceToValue_input" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="randomLaplaceToValue_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="randomLaplaceToValue_output" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="randomLaplaceToValue_output" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">4.数值偏移</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向数值中加入偏移量
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
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">5.Noisy Histogram2</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给直方图的每个值加噪
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值一维数组（直方图）
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：数值一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="noisy_hist2_textInput" class="form-control" placeholder="请输入，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="noisy_hist2_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="noisy_hist2_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="noisy_hist2_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">6.Report Noisy Max3</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给数组加拉普拉斯噪声后返回最大值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值一维数组
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
                                <input type="text" id="report_noisy_max3_textInput" class="form-control" placeholder="请输入，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="report_noisy_max3_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="report_noisy_max3_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="report_noisy_max3_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">7.Report Noisy Max4</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给数组加指数噪声后返回最大值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值一维数组
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
                                <input type="text" id="report_noisy_max4_textInput" class="form-control" placeholder="请输入，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="report_noisy_max4_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="report_noisy_max4_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="report_noisy_max4_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">8.Sparse Vector Technique3</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给数组值和阈值t加噪后，返回数组中前c个元素取值是否大于t
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：数值一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <!-- 新加的两个文本输入框 -->
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique3_t" class="form-control" placeholder="请输入阈值t" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique3_c" class="form-control" placeholder="请输入数值c" style="font-size: 20px">
                            </div>
                            <div class="input-group" >
                                <input type="text" id="sparse_vector_technique3_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                                        <button class="btn btn-default" id="sparse_vector_technique3_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                    </span>
                            </div>

                            <div class="text-center">
                                <label for="sparse_vector_technique3_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="sparse_vector_technique3_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">9.Sparse Vector Technique4</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给数组值和阈值t加噪后，返回数组中前c个元素取值是否大于t
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：数值一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <!-- 新加的两个文本输入框 -->
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique4_t" class="form-control" placeholder="请输入阈值t" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique4_c" class="form-control" placeholder="请输入数值c" style="font-size: 20px">
                            </div>
                            <div class="input-group" >
                                <input type="text" id="sparse_vector_technique4_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                                        <button class="btn btn-default" id="sparse_vector_technique4_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                    </span>
                            </div>

                            <div class="text-center">
                                <label for="sparse_vector_technique4_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="sparse_vector_technique4_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">10.Sparse Vector Technique5</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给数组值和阈值t加噪后，返回数组中前c个元素取值是否大于t
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：数值一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <!-- 新加的两个文本输入框 -->
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique5_t" class="form-control" placeholder="请输入阈值t" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique5_c" class="form-control" placeholder="请输入数值c" style="font-size: 20px">
                            </div>
                            <div class="input-group" >
                                <input type="text" id="sparse_vector_technique5_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                                        <button class="btn btn-default" id="sparse_vector_technique5_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                    </span>
                            </div>

                            <div class="text-center">
                                <label for="sparse_vector_technique5_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="sparse_vector_technique5_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">11.Sparse Vector Technique6</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给数组值和阈值t加噪后，返回数组中前c个元素取值是否大于t
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：数值一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <!-- 新加的两个文本输入框 -->
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique6_t" class="form-control" placeholder="请输入阈值t" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique6_c" class="form-control" placeholder="请输入数值c" style="font-size: 20px">
                            </div>
                            <div class="input-group" >
                                <input type="text" id="sparse_vector_technique6_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                                        <button class="btn btn-default" id="sparse_vector_technique6_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                    </span>
                            </div>

                            <div class="text-center">
                                <label for="sparse_vector_technique6_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="sparse_vector_technique6_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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

</style>

</html>
