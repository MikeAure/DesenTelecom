<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta name="referrer" content="no-referrer">

    <title>脱敏</title>
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
        document.getElementById("rappor_submitBtn").addEventListener("click", function () {
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
                body: '&rawData=' + encodeURIComponent(textInput) +
                    /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                    '&samples=' + encodeURIComponent(1) +
                    '&algName=' + encodeURIComponent(algName) +
                    '&params=' + encodeURIComponent(1)

            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("rappor_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("onetimerappor_submitBtn").addEventListener("click", function () {
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
                body: '&rawData=' + encodeURIComponent(textInput) +
                    /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                    '&samples=' + encodeURIComponent(1) +
                    '&algName=' + encodeURIComponent(algName) +
                    '&params=' + encodeURIComponent(1)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("onetimerappor_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("dpCode_submitBtn").addEventListener("click", function () {
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
                body: '&textInput=' + encodeURIComponent(textInput) +
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
    }
</script>

<div class="container wrapper wrapper-content">
    <div class="rappor panel">
        <div class="panel-body">
            <div class="row">
                <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

                20.Rappor
                </p>

                <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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

                    </div>
                </div>
            </div>

            <div class="row">
                <p style="font-size: 1.5em;text-align: center;"><strong>算法测试</strong></p>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-8">
                        <div class="input-group m-b">
                            <input type="text" id="rappor_textInput" class="form-control"
                                   placeholder="请输入数组，以,分隔数字"
                                   style="font-size: 20px">
                            <span class="input-group-btn">
                                <button class="btn btn-default" id="rappor_submitBtn"
                                        type="button"
                                        style="
                    font-size: 20px;
                    height: 30px;
                    display: flex;
                    justify-content: center;
                    align-items: center;"
                                >
                                    提交脱敏
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="text-center m-b">
                    <label for="rappor_outputText"
                           style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                    <div style="display: flex; flex-direction: column; align-items: center;">
                        <textarea
                                class="col-sm-8 m-t"
                                id="rappor_outputText"
                                rows="4" readonly>
                        </textarea>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <div class="panel">
        <div class="panel-body">
            <div class="row">
                <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

                21.One Time Rappor
                </p>

                <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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

                    </div>
                </div>
            </div>

            <div class="row">
                <p style="font-size: 1.5em;text-align: center;"><strong>算法测试</strong></p>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-8">
                        <div class="input-group m-b">
                            <input type="text" id="onetimerappor_textInput" class="form-control"
                                   placeholder="请输入数组，以,分隔数字"
                                   style="font-size: 20px">
                            <span class="input-group-btn">
                                <button class="btn btn-default" id="onetimerappor_submitBtn"
                                        type="button"
                                        style="
                    font-size: 20px;
                    height: 30px;
                    display: flex;
                    justify-content: center;
                    align-items: center;"
                                >
                                    提交脱敏
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="text-center m-b">
                    <label for="onetimerappor_outputText"
                           style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                    <div style="display: flex; flex-direction: column; align-items: center;">
                        <textarea
                                class="col-sm-8 m-t"
                                id="onetimerappor_outputText"
                                rows="4" readonly>
                        </textarea>
                    </div>
                </div>

            </div>
        </div>

    </div>
    <div class="panel">
        <div class="panel-body">
            <div class="row">
                <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

                22.编码型数据差分隐私脱敏算法
                </p>
                <div class="algo-description">
                    <div class="description-item">
                        <p>
                            说明：对编码型数据进行差分隐私扰动
                        </p>
                        <p>
                            输入：编码型数据数组
                        </p>
                        <p>
                            输出：编码型数据数组
                        </p>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="text-center">
                    <label for="dpDate_privacyLevel"
                           style="font-size: 1.5em;
                    text-align: center;">算法测试</label>
                </div>

                <label for="dpCode_privacyLevel"
                       class="col-sm-offset-4 col-sm-3"
                       style="font-size: 1.5em;">
                    请选择隐私保护等级
                </label>

                <div class="col-sm-5">
                    <select class="text-center"
                            id="dpCode_privacyLevel"
                            style="font-size: 1.5em;">
                        <option value="1">epsilon=3.6</option>
                        <option value="2" selected>epsilon=2</option>
                        <option value="3">epsilon=0.7</option>
                    </select>
                </div>

            </div>


            <div class="row m-t">

                <div class="col-sm-offset-2 col-sm-8 input-group">
                    <input type="text" id="dpCode_textInput" class="form-control"
                           placeholder="请输入文本" style="font-size: 20px"/>
                    <span class="input-group-btn">
                    <button class="btn btn-default"
                            id="dpCode_submitBtn"
                            type="button"
                            style="
                            font-size: 20px;
                            height: 30px;
                            display: flex;
                            justify-content: center;
                            align-items: center;"
                    >
                        提交脱敏
                    </button>
                </span>
                </div>
            </div>
            <hr class="hr-line-dashed">
            <div class="row">
                <div class="text-center m-b">
                    <label for="dpCode_outputText"
                           style="display: block; font-size: 1.5em;justify-content: center; align-items: center; ">
                        脱敏结果:
                    </label>
                    <div style="display: flex; flex-direction: column; align-items: center;">
                            <textarea
                                    class="col-sm-8 m-t"
                                    id="dpCode_outputText"
                                    rows="4" readonly>
                            </textarea>
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

    /*选择框居中*/
    .midtile {
        line-height: 30px;
        text-align: center;
        display: flex;
        justify-content: center;
    }

    #dpImage_pre, #dpImage_after {
        text-align: center;
    }

    #dpImage_pre image, #dpImage_after image, #dpAudio_pre, #dpAudio_after {
        display: inline-block;
        max-width: 50%;
        height: auto
    }

    /*上传按钮*/
    .upload-btn, #dpImage_submit, #dpAudio_submit, #dpGraph_submit {
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }

    .showFile {
        display: flex;
        justify-content: center;
    }

    .tabs-container ul {
        height: 60px;
        display: flex;
        flex-direction: row;
        justify-content: center;
    }

    .algo-description {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        width: 50%;
        margin: 0 auto;
    }

    .description-item > p {
        font-size: 1.5em;
        text-align: justify;
    }

</style>

</html>
