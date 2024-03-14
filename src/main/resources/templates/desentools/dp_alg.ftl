<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta name="referrer" content="no-referrer">

    <title>脱敏</title>

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
        document.getElementById("laplaceToValue_submitBtn").addEventListener("click", function (){
            let textInput = $("#laplaceToValue_textInput").val();
            let privacyLevel = document.getElementById("laplaceToValue_privacyLevel").value
            var textType = "value"
            var algName = "laplaceToValue"
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
                    document.getElementById("laplaceToValue_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("gaussianToValue_submitBtn").addEventListener("click", function (){
            let textInput = $("#gaussianToValue_textInput").val();
            let privacyLevel = document.getElementById("gaussianToValue_privacyLevel").value
            var textType = "value"
            var algName = "gaussianToValue"
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
                    document.getElementById("gaussianToValue_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("dpDate_submitBtn").addEventListener("click", function (){
            let textInput = $("#dpDate_textInput").val();
            let privacyLevel = document.getElementById("dpDate_privacyLevel").value
            var textType = "date"
            var algName = "dpDate"
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
                    document.getElementById("dpDate_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("dpCode_submitBtn").addEventListener("click", function (){
            let textInput = $("#dpCode_textInput").val();
            let privacyLevel = document.getElementById("dpCode_privacyLevel").value
            var textType = "code"
            var algName = "dpCode"
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
                    document.getElementById("dpCode_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("dpImage_fileUpload").addEventListener("change",function (event){
            // 清空
            document.getElementById("dpImage_pre").innerHTML = "";
            document.getElementById("dpImage_after").innerHTML = "";
            // 图像格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];
            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if(imageType.includes(fileExtension)){
                    var pre = document.getElementById("dpImage_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("dpImage_submit").onclick = function(){
                        var after = document.getElementById("dpImage_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("dpImage_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "dpImage");
                        formData.append("sheet", "dp");

                        fetch('/File/desenFile',{
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                var dealedImg = new Image();
                                dealedImg.src = URL.createObjectURL(blob);
                                after.appendChild(dealedImg);
                            })
                            .catch(error => console.error('Error:', error));

                    }
                }
                else {
                    alert("请选择图像文件");
                }
            }
        })
        document.getElementById("dpAudio_fileUpload").addEventListener("change",function (event){
            // 清空
            document.getElementById("dpAudio_pre").innerHTML = "";
            document.getElementById("dpAudio_after").innerHTML = "";
            // 音频格式
            const audioType = ['wav', 'mp3'];
            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if(audioType.includes(fileExtension)){
                    var pre = document.getElementById("dpAudio_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var video = document.createElement("audio");
                        video.src = e.target.result;
                        /*video.style.width = '300px';*/
                        video.controls = true;
                        pre.appendChild(video);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("dpAudio_submit").onclick = function(){
                        var after = document.getElementById("dpAudio_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("dpAudio_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "dpAudio");
                        formData.append("sheet", "type");

                        fetch('/File/desenFile',{
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                // 创建音频标签
                                const audioElement = document.createElement('audio');
                                audioElement.controls = true;
                                // 使用Blob URL设置音频数据
                                audioElement.src = URL.createObjectURL(blob);
                                // 添加到页面中
                                after.appendChild(audioElement);
                            })
                            .catch(error => console.error('Error:', error));

                    }
                }
                else {
                    alert("请选择音频文件");
                }
            }
        })
        document.getElementById("dpGraph_fileUpload").addEventListener("change",function (event){
            // 清空
            document.getElementById("dpGraph_pre").innerHTML = "";
            document.getElementById("dpGraph_after").innerHTML = "";
            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                var fileLoad = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                    "<strong>" + fileName + "文件</strong>已选择"
                "</span>" +
                "</div>";
                document.getElementById("dpGraph_fileInfo").innerHTML = fileLoad
                document.getElementById("dpGraph_submit").onclick = function(){
                    var after = document.getElementById("dpGraph_after");
                    after.innerHTML = "";
                    // 获取保护级别
                    var param = document.getElementById("dpGraph_privacyLevel").value;

                    var formData = new FormData();
                    formData.append("file", file);
                    formData.append("params", param);
                    formData.append("algName", "dpGraph");
                    formData.append("sheet", "type1");

                    fetch('/File/desenFile',{
                        method: 'POST',
                        body: formData
                    })
                        .then(response => response.blob())
                        .then(blob => {
                            // 创建一个下载链接
                            const downloadLink = document.createElement('a');
                            downloadLink.href = URL.createObjectURL(blob);
                            downloadLink.download = Date.now().toString() + "graph" ; // 下载的文件名
                            downloadLink.click();
                            after.appendChild(downloadLink);
                        })
                        .catch(error => console.error('Error:', error));

                }

            }
        })

        document.getElementById("noisy_hist1_submitBtn").addEventListener("click", function (){
            let textInput = $("#noisy_hist1_textInput").val();
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "noisy_hist1"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/DP/desenValue", {
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
                    document.getElementById("noisy_hist1_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("report_noisy_max1_submitBtn").addEventListener("click", function (){
            let textInput = $("#report_noisy_max1_textInput").val();
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "report_noisy_max1"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/DP/desenValue", {
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
                    document.getElementById("report_noisy_max1_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("report_noisy_max2_submitBtn").addEventListener("click", function (){
            let textInput = $("#report_noisy_max2_textInput").val();
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "report_noisy_max2"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/DP/desenValue", {
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
                    document.getElementById("report_noisy_max2_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("sparse_vector_technique1_submitBtn").addEventListener("click", function (){
            let textInput = $("#sparse_vector_technique1_textInput").val();
            let c = $("#sparse_vector_technique1_c").val()
            let t = $("#sparse_vector_technique1_t").val()
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "sparse_vector_technique1"
            if (textInput === "") {
                alert("请输入数据");
                return; // Stop further execution if the text input is empty
            }

            fetch("/DP/desenValue2", {
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
                    document.getElementById("sparse_vector_technique1_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("sparse_vector_technique2_submitBtn").addEventListener("click", function (){
            let textInput = $("#sparse_vector_technique2_textInput").val();
            let c = $("#sparse_vector_technique2_c").val()
            let t = $("#sparse_vector_technique2_t").val()
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "sparse_vector_technique2"
            if (textInput === "") {
                alert("请输入数据");
                return; // Stop further execution if the text input is empty
            }

            fetch("/DP/desenValue2", {
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
                    document.getElementById("sparse_vector_technique2_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("sparse_vector_technique_numerical_submitBtn").addEventListener("click", function (){
            let textInput = $("#sparse_vector_technique_numerical_textInput").val();
            let c = $("#sparse_vector_technique_numerical_c").val()
            let t = $("#sparse_vector_technique_numerical_t").val()
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "sparse_vector_technique_numerical"
            if (textInput === "") {
                alert("请输入数据");
                return; // Stop further execution if the text input is empty
            }

            fetch("/DP/desenValue2", {
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
                    document.getElementById("sparse_vector_technique_numerical_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("rappor_submitBtn").addEventListener("click", function (){
            let textInput = $("#rappor_textInput").val();
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "rappor"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/DP/desenValue", {
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
                    document.getElementById("rappor_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("onetimerappor_submitBtn").addEventListener("click", function (){
            let textInput = $("#onetimerappor_textInput").val();
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "onetimerappor"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/DP/desenValue", {
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
                    document.getElementById("onetimerappor_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("snapping_submitBtn").addEventListener("click", function (){
            let textInput = $("#snapping_textInput").val();
            /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
            let algName = "snapping"
            if (textInput === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/DP/desenValue", {
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
                    document.getElementById("snapping_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
    }
</script>
<div class="ibox-title">
</div>

<div class="panel panel-default">
    <div class="panel-heading"  style="text-align: center;">
        <h1 class="panel-title"><b style="font-size: 2em">差分隐私算法</b></h1>
    </div>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">1.基于拉普拉斯差分隐私的数值加噪算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">1.基于拉普拉斯差分隐私的数值加噪算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向数值中加入满足差分隐私的拉普拉斯噪声
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
                            <select id="laplaceToValue_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="laplaceToValue_textInput" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="laplaceToValue_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="laplaceToValue_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="laplaceToValue_outputText" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">2.基于高斯机制差分隐私的数值加噪算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
               <#-- <p style="font-size: 1.5em;">2.基于高斯机制差分隐私的数值加噪算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向数值中加入符合差分隐私的高斯噪声
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
                            <select id="gaussianToValue_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="gaussianToValue_textInput" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="gaussianToValue_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="gaussianToValue_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="gaussianToValue_outputText" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">3.基于差分隐私的日期加噪算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">2.基于差分隐私的日期加噪算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向数值中加入符合差分隐私的拉普拉斯噪声
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：日期
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：日期
                    </p>

                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="dpDate_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="dpDate_textInput" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="dpDate_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="dpDate_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="dpDate_outputText" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">4.编码型数据差分隐私脱敏算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">4.编码型数据差分隐私脱敏算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对编码型数据进行差分隐私扰动
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：编码型数据数组
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：编码型数据数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="dpCode_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="dpCode_textInput" class="form-control" placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="dpCode_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="dpCode_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="dpCode_outputText" rows="2" cols="50" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">5.图像差分隐私脱敏算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">5.图像差分隐私脱敏算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向图像中加入差分隐私噪声
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：图像
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：图像
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class=" align-items-center">
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="dpImage_fileUpload"  style="display: none;">
                                <label for="dpImage_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="dpImage_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id = "dpImage_pre" style="margin-right: 20px;">
            </div>
            <div id = "dpImage_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="dpImage_submit"> 提交脱敏</button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">6.声纹特征脱敏算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
               <#-- <p style="font-size: 1.5em;">6.声纹特征脱敏算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向声纹中加入差分隐私噪声
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：音频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：音频
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class=" align-items-center">
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="dpAudio_fileUpload"  style="display: none;">
                                <label for="dpAudio_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style=" font-size: 20px">
                            <p>请选择隐私保护等级</p>
                            <select id="dpAudio_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="showFile">
            <!--前后文件-->
            <div id = "dpAudio_pre" style="margin-right: 20px;">
            </div>
            <div id = "dpAudio_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="dpAudio_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">7.图形差分隐私脱敏算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
               <#-- <p style="font-size: 1.5em;">6.图形差分隐私脱敏算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：向轨迹数据中加入差分隐私噪声
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：轨迹文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：轨迹文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class=" align-items-center">
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="dpGraph_fileUpload"  style="display: none;">
                                <label for="dpGraph_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="dpGraph_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="showFile">
            <!--文件上传信息-->
            <div id = "dpGraph_fileInfo">
            </div>
            <!--前后文件-->
            <div id = "dpGraph_pre" style="margin-right: 20px;">
            </div>
            <div id = "dpGraph_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="dpGraph_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">8.Noisy Histogram1</p>
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
                                <input type="text" id="noisy_hist1_textInput" class="form-control" placeholder="请输入，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="noisy_hist1_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="noisy_hist1_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="noisy_hist1_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">9.Report Noisy Max1-Laplace</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给数组加拉普拉斯噪声后返回最大值的下标
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
                                <input type="text" id="report_noisy_max1_textInput" class="form-control" placeholder="请输入，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="report_noisy_max1_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="report_noisy_max1_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="report_noisy_max1_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">10.Report Noisy Max2-Exponential</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：基于指数机制返回一维数组最大值的下标
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
                                <input type="text" id="report_noisy_max2_textInput" class="form-control" placeholder="请输入，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="report_noisy_max2_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="report_noisy_max2_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="report_noisy_max2_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">11.Sparse Vector Technique1</p>
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
                                <input type="text" id="sparse_vector_technique1_t" class="form-control" placeholder="请输入阈值t" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique1_c" class="form-control" placeholder="请输入数值c" style="font-size: 20px">
                            </div>
                            <div class="input-group" >
                                <input type="text" id="sparse_vector_technique1_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                    <span class="input-group-btn" >
                                        <button class="btn btn-default" id="sparse_vector_technique1_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                    </span>
                            </div>

                            <div class="text-center">
                                <label for="sparse_vector_technique1_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="sparse_vector_technique1_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">12.Sparse Vector Technique2</p>
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
                                <input type="text" id="sparse_vector_technique2_t" class="form-control" placeholder="请输入阈值t" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique2_c" class="form-control" placeholder="请输入数值c" style="font-size: 20px">
                            </div>
                            <div class="input-group" >
                                <input type="text" id="sparse_vector_technique2_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                                        <button class="btn btn-default" id="sparse_vector_technique2_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                    </span>
                            </div>

                            <div class="text-center">
                                <label for="sparse_vector_technique2_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="sparse_vector_technique2_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">13.Numerical Sparse Vector Technique</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：给数组值和阈值t加噪后，前c个元素如果取值大于t则返回加噪后的值，否则返回False
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
                                <input type="text" id="sparse_vector_technique_numerical_t" class="form-control" placeholder="请输入阈值t" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="sparse_vector_technique_numerical_c" class="form-control" placeholder="请输入数值c" style="font-size: 20px">
                            </div>
                            <div class="input-group" >
                                <input type="text" id="sparse_vector_technique_numerical_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                                        <button class="btn btn-default" id="sparse_vector_technique_numerical_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                    </span>
                            </div>

                            <div class="text-center">
                                <label for="sparse_vector_technique_numerical_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="sparse_vector_technique_numerical_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">14.Rappor</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：基于随机响应统计用户某一特征的直方图（频次）信息
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：一维二进制数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="rappor_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="rappor_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="rappor_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="rappor_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">15.One Time Rappor</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：基于随机响应统计用户某一特征的直方图（频次）信息
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：一维二进制数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="onetimerappor_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="onetimerappor_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="onetimerappor_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="onetimerappor_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">15.Snapping Mechanism</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：拉普拉斯机制的变体，对数值和结果进行clip处理。
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值或一维数组
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：一维二进制数组
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container" >
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5" >
                            <div class="input-group" >
                                <input type="text" id="snapping_textInput" class="form-control" placeholder="请输入数组，以,分隔数字" style="font-size: 20px">
                                <span class="input-group-btn" >
                    <button class="btn btn-default" id="snapping_submitBtn" type="button" style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="snapping_outputText" style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="snapping_outputText" rows="4" cols="100" readonly style="margin-top: 10px;"></textarea>
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
    /*选择框居中*/
    .midtile{
        line-height: 30px;
        text-align: center;
        display:flex;
        justify-content: center;
    }
    #dpImage_pre, #dpImage_after{
        text-align: center;
    }
    #dpImage_pre image, #dpImage_after image, #dpAudio_pre , #dpAudio_after  {
        display: inline-block;
        max-width: 50%;
        height: auto
    }
    /*上传按钮*/
    .upload-btn, #dpImage_submit, #dpAudio_submit, #dpGraph_submit{
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }
    .showFile{
        display: flex;
        justify-content: center;
    }

</style>

</html>
