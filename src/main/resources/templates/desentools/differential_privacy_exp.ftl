<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta name="renderer" content="webkit"/>
    <meta name="referrer" content="no-referrer"/>

    <title>脱敏</title>

    <meta name="keywords" content=""/>
    <meta name="description" content=""/>

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->
    <link rel="shortcut icon" href="favicon.ico"/>
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet"/>
    <link href="${ctx!}/css/font-awesome.min.css?v=4.4.0" rel="stylesheet"/>
    <link href="${ctx!}/css/animate.css" rel="stylesheet"/>
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet"/>
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
        document
            .getElementById("exponential_submitBtn")
            .addEventListener("click", function () {
                let textInput = $("#exponential_textInput").val();
                /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
                let algName = "exponential";
                if (textInput === "") {
                    alert("请输入文本");
                    return; // Stop further execution if the text input is empty
                }

                fetch("/DP/desenValue", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    body:
                        "&rawData=" +
                        encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        "&samples=" +
                        encodeURIComponent(1) +
                        "&algName=" +
                        encodeURIComponent(algName),
                })
                    .then((response) => response.text())
                    .then((data) => {
                        document.getElementById("exponential_outputText").value =
                            data;
                    })
                    .catch((error) => console.error("Error:", error));
            });
        document
            .getElementById("report_noisy_max2_submitBtn")
            .addEventListener("click", function () {
                let textInput = $("#report_noisy_max2_textInput").val();
                /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
                let algName = "report_noisy_max2";
                if (textInput === "") {
                    alert("请输入文本");
                    return; // Stop further execution if the text input is empty
                }

                fetch("/DP/desenValue", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    body:
                        "&rawData=" +
                        encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        "&samples=" +
                        encodeURIComponent(1) +
                        "&algName=" +
                        encodeURIComponent(algName),
                })
                    .then((response) => response.text())
                    .then((data) => {
                        document.getElementById("report_noisy_max2_outputText").value =
                            data;
                    })
                    .catch((error) => console.error("Error:", error));
            });
        document
            .getElementById("report_noisy_max4_submitBtn")
            .addEventListener("click", function () {
                let textInput = $("#report_noisy_max4_textInput").val();
                /*let privacyLevel = document.getElementById("noisy_hist1_privacyLevel").value*/
                let algName = "report_noisy_max4";
                if (textInput === "") {
                    alert("请输入文本");
                    return; // Stop further execution if the text input is empty
                }

                fetch("/RandomNoise/desenValue", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    body:
                        "&rawData=" +
                        encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        "&samples=" +
                        encodeURIComponent(1) +
                        "&algName=" +
                        encodeURIComponent(algName),
                })
                    .then((response) => response.text())
                    .then((data) => {
                        document.getElementById("report_noisy_max4_outputText").value =
                            data;
                    })
                    .catch((error) => console.error("Error:", error));
            });

    };
</script>
<div class="ibox-title"></div>

<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <p
                    style="
              font-size: 1.5em;
              display: flex;
              flex-wrap: wrap;
              justify-content: center;
              width: 50%;
              margin: 0 auto;
            "
            >
                9.Exponential Mechanism
            </p>
            <div <#--class="col-sm-6" -->
                    style="display: flex; flex-wrap: wrap; justify-content: center;
            width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em; text-align: justify">
                        说明：一组数值中某个数值被选中的概率
                    </p>
                    <p style="font-size: 1.5em; text-align: justify">
                        输入：数值一维数组
                    </p>
                    <p style="font-size: 1.5em; text-align: justify">输出：数值一维数组</p>
                    <p style="font-size: 1.5em; text-align: center">算法测试</p>
                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input
                                        type="text"
                                        id="exponential_textInput"
                                        class="form-control"
                                        placeholder="请输入，以,分隔数字"
                                        style="font-size: 20px"
                                />
                                <span class="input-group-btn">
                      <button
                              class="btn btn-default"
                              id="exponential_submitBtn"
                              type="button"
                              style="
                          font-size: 20px;
                          height: 30px;
                          display: flex;
                          justify-content: center;
                          align-items: center;
                        "
                      >
                        提交脱敏
                      </button>
                    </span>
                            </div>
                            <div class="text-center">
                                <label
                                        for="exponential_outputText"
                                        style="
                        display: block;
                        font-size: 20px;
                        justify-content: center;
                        align-items: center;
                      "
                                >
                                    脱敏结果:</label
                                >
                                <div
                                        style="
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                      "
                                >
                      <textarea
                              id="exponential_outputText"
                              rows="4"
                              cols="100"
                              readonly
                              style="margin-top: 10px"
                      ></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr/>
    <div class="panel-body">
        <div class="row">
            <p
                    style="
              font-size: 1.5em;
              display: flex;
              flex-wrap: wrap;
              justify-content: center;
              width: 50%;
              margin: 0 auto;
            "
            >
                10.Report Noisy Max2-Exponential
            </p>
            <div <#--class="col-sm-6" -->
                    style="display: flex; flex-wrap: wrap; justify-content: center;
            width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em; text-align: justify">
                        说明：基于指数机制返回一维数组最大值的下标
                    </p>
                    <p style="font-size: 1.5em; text-align: justify">
                        输入：数值一维数组
                    </p>
                    <p style="font-size: 1.5em; text-align: justify">输出：数值</p>
                    <p style="font-size: 1.5em; text-align: center">算法测试</p>
                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input
                                        type="text"
                                        id="report_noisy_max2_textInput"
                                        class="form-control"
                                        placeholder="请输入，以,分隔数字"
                                        style="font-size: 20px"
                                />
                                <span class="input-group-btn">
                      <button
                              class="btn btn-default"
                              id="report_noisy_max2_submitBtn"
                              type="button"
                              style="
                          font-size: 20px;
                          height: 30px;
                          display: flex;
                          justify-content: center;
                          align-items: center;
                        "
                      >
                        提交脱敏
                      </button>
                    </span>
                            </div>
                            <div class="text-center">
                                <label
                                        for="report_noisy_max2_outputText"
                                        style="
                        display: block;
                        font-size: 20px;
                        justify-content: center;
                        align-items: center;
                      "
                                >
                                    脱敏结果:</label
                                >
                                <div
                                        style="
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                      "
                                >
                      <textarea
                              id="report_noisy_max2_outputText"
                              rows="4"
                              cols="100"
                              readonly
                              style="margin-top: 10px"
                      ></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr/>
    <div class="panel-body">
        <div class="row">
            <p
                    style="
              font-size: 1.5em;
              display: flex;
              flex-wrap: wrap;
              justify-content: center;
              width: 50%;
              margin: 0 auto;
            "
            >
                11.Report Noisy Max4
            </p>
            <div <#--class="col-sm-6" -->
                    style="display: flex; flex-wrap: wrap; justify-content: center;
            width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em; text-align: justify">
                        说明：给数组加指数噪声后返回最大值
                    </p>
                    <p style="font-size: 1.5em; text-align: justify">
                        输入：数值一维数组
                    </p>
                    <p style="font-size: 1.5em; text-align: justify">输出：数值</p>
                    <p style="font-size: 1.5em; text-align: center">算法测试</p>
                </div>
                <div class="container">
                    <div
                            class="row justify-content-center"
                            style="display: grid; place-items: center"
                    >
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input
                                        type="text"
                                        id="report_noisy_max4_textInput"
                                        class="form-control"
                                        placeholder="请输入，以,分隔数字"
                                        style="font-size: 20px"
                                />
                                <span class="input-group-btn">
                      <button
                              class="btn btn-default"
                              id="report_noisy_max4_submitBtn"
                              type="button"
                              style="
                          font-size: 20px;
                          height: 30px;
                          display: flex;
                          justify-content: center;
                          align-items: center;
                        "
                      >
                        提交脱敏
                      </button>
                    </span>
                            </div>
                            <div class="text-center">
                                <label
                                        for="report_noisy_max4_outputText"
                                        style="
                        display: block;
                        font-size: 20px;
                        justify-content: center;
                        align-items: center;
                      "
                                >
                                    脱敏结果:</label
                                >
                                <div
                                        style="
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                      "
                                >
                      <textarea
                              id="report_noisy_max4_outputText"
                              rows="4"
                              cols="100"
                              readonly
                              style="margin-top: 10px"
                      ></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr/>
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

    #dpImage_pre,
    #dpImage_after {
        text-align: center;
    }

    #dpImage_pre image,
    #dpImage_after image,
    #dpAudio_pre,
    #dpAudio_after {
        display: inline-block;
        max-width: 50%;
        height: auto;
    }

    /*上传按钮*/
    .upload-btn,
    #dpImage_submit,
    #dpAudio_submit,
    #dpGraph_submit {
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
</style>
</html>
