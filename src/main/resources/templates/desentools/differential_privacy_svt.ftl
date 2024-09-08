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
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
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
            document.getElementById("sparse_vector_technique1_submitBtn").addEventListener("click", function () {
                let textInput = $("#sparse_vector_technique1_textInput").val();
                let c = $("#sparse_vector_technique1_c").val()
                let t = $("#sparse_vector_technique1_t").val()
                let privacyLevel = document.getElementById("sparse_vector_technique1_privacyLevel").value
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
                    body: '&rawData=' + encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        '&samples=' + encodeURIComponent(1) +
                        '&algName=' + encodeURIComponent(algName) +
                        '&c=' + encodeURIComponent(c) +
                        '&t=' + encodeURIComponent(t) +
                        '&params=' + encodeURIComponent(privacyLevel)

                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("sparse_vector_technique1_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            });

            document.getElementById("sparse_vector_technique2_submitBtn").addEventListener("click", function () {
                let textInput = $("#sparse_vector_technique2_textInput").val();
                let c = $("#sparse_vector_technique2_c").val();
                let t = $("#sparse_vector_technique2_t").val();
                let privacyLevel = document.getElementById("sparse_vector_technique2_privacyLevel").value;
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
                    body: '&rawData=' + encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        '&samples=' + encodeURIComponent(1) +
                        '&algName=' + encodeURIComponent(algName) +
                        '&c=' + encodeURIComponent(c) +
                        '&t=' + encodeURIComponent(t)+
                        '&params=' + encodeURIComponent(privacyLevel)
                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("sparse_vector_technique2_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            });

            document.getElementById("sparse_vector_technique3_submitBtn").addEventListener("click", function () {
                let textInput = $("#sparse_vector_technique3_textInput").val();
                let c = $("#sparse_vector_technique3_c").val()
                let t = $("#sparse_vector_technique3_t").val()
                let privacyLevel = document.getElementById("sparse_vector_technique3_privacyLevel").value;
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
                    body: '&rawData=' + encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        '&samples=' + encodeURIComponent(1) +
                        '&algName=' + encodeURIComponent(algName) +
                        '&c=' + encodeURIComponent(c) +
                        '&t=' + encodeURIComponent(t) +
                        '&params=' + encodeURIComponent(privacyLevel)
                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("sparse_vector_technique3_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            });

            document.getElementById("sparse_vector_technique4_submitBtn").addEventListener("click", function () {
                let textInput = $("#sparse_vector_technique4_textInput").val();
                let c = $("#sparse_vector_technique4_c").val()
                let t = $("#sparse_vector_technique4_t").val()
                let privacyLevel = document.getElementById("sparse_vector_technique4_privacyLevel").value;
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
                    body: '&rawData=' + encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        '&samples=' + encodeURIComponent(1) +
                        '&algName=' + encodeURIComponent(algName) +
                        '&c=' + encodeURIComponent(c) +
                        '&t=' + encodeURIComponent(t) +
                        '&params=' + encodeURIComponent(privacyLevel)
                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("sparse_vector_technique4_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            });

            document.getElementById("sparse_vector_technique5_submitBtn").addEventListener("click", function () {
                let textInput = $("#sparse_vector_technique5_textInput").val();
                let c = $("#sparse_vector_technique5_c").val()
                let t = $("#sparse_vector_technique5_t").val()
                let privacyLevel = document.getElementById("sparse_vector_technique5_privacyLevel").value;
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
                    body: '&rawData=' + encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        '&samples=' + encodeURIComponent(1) +
                        '&algName=' + encodeURIComponent(algName) +
                        '&c=' + encodeURIComponent(c) +
                        '&t=' + encodeURIComponent(t) +
                        '&params=' + encodeURIComponent(privacyLevel)
                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("sparse_vector_technique5_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            });

            document.getElementById("sparse_vector_technique6_submitBtn").addEventListener("click", function () {
                let textInput = $("#sparse_vector_technique6_textInput").val();
                let c = $("#sparse_vector_technique6_c").val();
                let t = $("#sparse_vector_technique6_t").val();
                let privacyLevel = document.getElementById("sparse_vector_technique6_privacyLevel").value;
                let algName = "sparse_vector_technique6";
                if (textInput === "") {
                    alert("请输入数据");
                    return; // Stop further execution if the text input is empty
                }

                fetch("/RandomNoise/desenValue2", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: '&rawData=' + encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        '&samples=' + encodeURIComponent(1) +
                        '&algName=' + encodeURIComponent(algName) +
                        '&c=' + encodeURIComponent(c) +
                        '&t=' + encodeURIComponent(t) +
                        '&params=' + encodeURIComponent(privacyLevel)
                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("sparse_vector_technique6_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            });

            document.getElementById("sparse_vector_technique_numerical_submitBtn").addEventListener("click", function () {
                let textInput = $("#sparse_vector_technique_numerical_textInput").val();
                let c = $("#sparse_vector_technique_numerical_c").val()
                let t = $("#sparse_vector_technique_numerical_t").val()
                let privacyLevel = document.getElementById("sparse_vector_technique_numerical_privacyLevel").value;
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
                    body: '&rawData=' + encodeURIComponent(textInput) +
                        /*'&privacyLevel=' + encodeURIComponent(privacyLevel) +*/
                        '&samples=' + encodeURIComponent(1) +
                        '&algName=' + encodeURIComponent(algName) +
                        '&c=' + encodeURIComponent(c) +
                        '&t=' + encodeURIComponent(t) +
                        '&params=' + encodeURIComponent(privacyLevel)
                })
                    .then(response => response.text())
                    .then(data => {
                        document.getElementById("sparse_vector_technique_numerical_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            });
        }
    </script>
</head>

<body>
<div class="container wrapper wrapper-content">
<div class="sparse_vector_technique1 panel">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

            13.Sparse Vector Technique1
            </p>

            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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

                </div>
            </div>
        </div>

        <div class="row">
            <p style="font-size: 1.5em;text-align: center;"><strong>算法测试</strong></p>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-8">
                    <label for="sparse_vector_technique1_privacyLevel"
                           class="col-sm-offset-4 col-sm-3"
                           style="font-size: 1.5em;">
                        请选择隐私预算
                    </label>

                    <div class="col-sm-5">
                        <select class="text-center"
                                id="sparse_vector_technique1_privacyLevel"
                                style="font-size: 1.5em;">
                            <option value="0">epsilon=10</option>
                            <option value="1">epsilon=1</option>
                            <option value="2">epsilon=0.1</option>
                        </select>
                    </div>
                    <input type="text" id="sparse_vector_technique1_t" class="form-control m-b input-lg"
                           placeholder="请输入阈值t" style="font-size: 20px">

                    <input type="text" id="sparse_vector_technique1_c" class="form-control m-b input-lg"
                           placeholder="请输入数值c" style="font-size: 20px">
                    <div class="input-group m-b">
                        <input type="text" id="sparse_vector_technique1_textInput" class="form-control"
                               placeholder="请输入数组，以,分隔数字"
                               style="font-size: 20px">
                        <span class="input-group-btn">
                                <button class="btn btn-default" id="sparse_vector_technique1_submitBtn"
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
                <label for="sparse_vector_technique1_outputText"
                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                <div style="display: flex; flex-direction: column; align-items: center;">
                        <textarea
                                class="col-sm-8 m-t"
                                id="sparse_vector_technique1_outputText"
                                rows="4" readonly>
                        </textarea>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="sparse_vector_technique2 panel">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
            14.Sparse Vector Technique2
            </p>
            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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


                </div>
            </div>
        </div>

        <div class="row">
            <p style="font-size: 1.5em;text-align: center;"><strong>算法测试</strong></p>
            <div class="form-group">
                <label for="sparse_vector_technique2_privacyLevel"
                       class="col-sm-offset-4 col-sm-3"
                       style="font-size: 1.5em;">
                    请选择隐私预算
                </label>

                <div class="col-sm-5">
                    <select class="text-center"
                            id="sparse_vector_technique2_privacyLevel"
                            style="font-size: 1.5em;">
                        <option value="0">epsilon=10</option>
                        <option value="1">epsilon=1</option>
                        <option value="2">epsilon=0.1</option>
                    </select>
                </div>
                <div class="col-sm-offset-2 col-sm-8">
                    <input type="text" id="sparse_vector_technique2_t" class="form-control m-b input-lg"
                           placeholder="请输入阈值t" style="font-size: 20px">

                    <input type="text" id="sparse_vector_technique2_c" class="form-control m-b input-lg"
                           placeholder="请输入数值c" style="font-size: 20px">
                    <div class="input-group m-b">
                        <input type="text" id="sparse_vector_technique2_textInput" class="form-control"
                               placeholder="请输入数组，以,分隔数字"
                               style="font-size: 20px">
                        <span class="input-group-btn">
                                <button class="btn btn-default" id="sparse_vector_technique2_submitBtn"
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
                <label for="sparse_vector_technique2_outputText"
                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                <div style="display: flex; flex-direction: column; align-items: center;">
                        <textarea
                                class="col-sm-8 m-t"
                                id="sparse_vector_technique2_outputText"
                                rows="4" readonly>
                        </textarea>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="sparse_vector_technique3 panel">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
            15.Sparse Vector Technique3
            </p>

            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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

                </div>
            </div>
        </div>

        <div class="row">
            <p style="font-size: 1.5em;text-align: center;"><strong>算法测试</strong></p>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-8">
                    <label for="sparse_vector_technique3_privacyLevel"
                           class="col-sm-offset-4 col-sm-3"
                           style="font-size: 1.5em;">
                        请选择隐私预算
                    </label>

                    <div class="col-sm-5">
                        <select class="text-center"
                                id="sparse_vector_technique3_privacyLevel"
                                style="font-size: 1.5em;">
                            <option value="0">epsilon=10</option>
                            <option value="1">epsilon=1</option>
                            <option value="2">epsilon=0.1</option>
                        </select>
                    </div>
                    <input type="text" id="sparse_vector_technique3_t" class="form-control m-b input-lg"
                           placeholder="请输入阈值t" style="font-size: 20px">

                    <input type="text" id="sparse_vector_technique3_c" class="form-control m-b input-lg"
                           placeholder="请输入数值c" style="font-size: 20px">
                    <div class="input-group m-b">
                        <input type="text" id="sparse_vector_technique3_textInput" class="form-control"
                               placeholder="请输入数组，以,分隔数字"
                               style="font-size: 20px">
                        <span class="input-group-btn">
                                <button class="btn btn-default" id="sparse_vector_technique3_submitBtn"
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
                <label for="sparse_vector_technique3_outputText"
                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                <div style="display: flex; flex-direction: column; align-items: center;">
                        <textarea
                                class="col-sm-8 m-t"
                                id="sparse_vector_technique3_outputText"
                                rows="4" readonly>
                        </textarea>
                </div>
            </div>
        </div>
    </div>

</div>
<div class="sparse_vector_technique4 panel">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

            16.Sparse Vector Technique4
            </p>

            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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


                </div>
            </div>
        </div>

        <div class="row">
            <p style="font-size: 1.5em;text-align: center;"><strong>算法测试</strong></p>
            <div class="form-group">
                <label for="sparse_vector_technique4_privacyLevel"
                       class="col-sm-offset-4 col-sm-3"
                       style="font-size: 1.5em;">
                    请选择隐私预算
                </label>

                <div class="col-sm-5">
                    <select class="text-center"
                            id="sparse_vector_technique4_privacyLevel"
                            style="font-size: 1.5em;">
                        <option value="0">epsilon=10</option>
                        <option value="1">epsilon=1</option>
                        <option value="2">epsilon=0.1</option>
                    </select>
                </div>
                <div class="col-sm-offset-2 col-sm-8">
                    <input type="text" id="sparse_vector_technique4_t" class="form-control m-b input-lg"
                           placeholder="请输入阈值t" style="font-size: 20px">

                    <input type="text" id="sparse_vector_technique4_c" class="form-control m-b input-lg"
                           placeholder="请输入数值c" style="font-size: 20px">
                    <div class="input-group m-b">
                        <input type="text" id="sparse_vector_technique4_textInput" class="form-control"
                               placeholder="请输入数组，以,分隔数字"
                               style="font-size: 20px">
                        <span class="input-group-btn">
                                <button class="btn btn-default" id="sparse_vector_technique4_submitBtn"
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
                <label for="sparse_vector_technique4_outputText"
                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                <div style="display: flex; flex-direction: column; align-items: center;">
                        <textarea
                                class="col-sm-8 m-t"
                                id="sparse_vector_technique4_outputText"
                                rows="4" readonly>
                        </textarea>
                </div>
            </div>

        </div>
    </div>

</div>
<div class="sparse_vector_technique5 panel">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

            17.Sparse Vector Technique5
            </p>

            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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


                </div>
            </div>
        </div>

        <div class="row">
            <p style="font-size: 1.5em;text-align: center;"><strong>算法测试</strong></p>
            <div class="form-group">
                <label for="sparse_vector_technique5_privacyLevel"
                       class="col-sm-offset-4 col-sm-3"
                       style="font-size: 1.5em;">
                    请选择隐私预算
                </label>

                <div class="col-sm-5">
                    <select class="text-center"
                            id="sparse_vector_technique5_privacyLevel"
                            style="font-size: 1.5em;">
                        <option value="0">epsilon=10</option>
                        <option value="1">epsilon=1</option>
                        <option value="2">epsilon=0.1</option>
                    </select>
                </div>
                <div class="col-sm-offset-2 col-sm-8">
                    <input type="text" id="sparse_vector_technique5_t" class="form-control m-b input-lg"
                           placeholder="请输入阈值t" style="font-size: 20px">

                    <input type="text" id="sparse_vector_technique5_c" class="form-control m-b input-lg"
                           placeholder="请输入数值c" style="font-size: 20px">
                    <div class="input-group m-b">
                        <input type="text" id="sparse_vector_technique5_textInput" class="form-control"
                               placeholder="请输入数组，以,分隔数字"
                               style="font-size: 20px">
                        <span class="input-group-btn">
                                <button class="btn btn-default" id="sparse_vector_technique5_submitBtn"
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
                <label for="sparse_vector_technique5_outputText"
                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                <div style="display: flex; flex-direction: column; align-items: center;">
                        <textarea
                                class="col-sm-8 m-t"
                                id="sparse_vector_technique5_outputText"
                                rows="4" readonly>
                        </textarea>
                </div>
            </div>
        </div>
    </div>

</div>
<div class="sparse_vector_technique6 panel">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

            18.Sparse Vector Technique6
            </p>

            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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


                </div>
            </div>
        </div>

        <div class="row">
            <p style="font-size: 1.5em;text-align: center;"><strong>算法测试</strong></p>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-8">
                    <label for="sparse_vector_technique6_privacyLevel"
                           class="col-sm-offset-4 col-sm-3"
                           style="font-size: 1.5em;">
                        请选择隐私预算
                    </label>

                    <div class="col-sm-5">
                        <select class="text-center"
                                id="sparse_vector_technique6_privacyLevel"
                                style="font-size: 1.5em;">
                            <option value="0">epsilon=10</option>
                            <option value="1">epsilon=1</option>
                            <option value="2">epsilon=0.1</option>
                        </select>
                    </div>
                    <input type="text" id="sparse_vector_technique6_t" class="form-control m-b input-lg"
                           placeholder="请输入阈值t" style="font-size: 20px">

                    <input type="text" id="sparse_vector_technique6_c" class="form-control m-b input-lg"
                           placeholder="请输入数值c" style="font-size: 20px">
                    <div class="input-group m-b">
                        <input type="text" id="sparse_vector_technique6_textInput" class="form-control"
                               placeholder="请输入数组，以,分隔数字"
                               style="font-size: 20px">
                        <span class="input-group-btn ">
                                <button class="btn btn-default" id="sparse_vector_technique6_submitBtn"
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
                <label for="sparse_vector_technique6_outputText"
                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                <div style="display: flex; flex-direction: column; align-items: center;">
                        <textarea
                                class="col-sm-8 m-t"
                                id="sparse_vector_technique6_outputText"
                                rows="4" readonly>
                        </textarea>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="numerical_sparse_vector_technique panel">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

            19.Numerical Sparse Vector Technique
            </p>

            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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
                </div>
            </div>
        </div>

        <div class="row">
            <p style="font-size: 1.5em;text-align: center;"><strong>算法测试</strong></p>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-8">
                    <label for="sparse_vector_technique_numerical_privacyLevel"
                           class="col-sm-offset-4 col-sm-3"
                           style="font-size: 1.5em;">
                        请选择隐私预算
                    </label>

                    <div class="col-sm-5">
                        <select class="text-center"
                                id="sparse_vector_technique_numerical_privacyLevel"
                                style="font-size: 1.5em;">
                            <option value="0">epsilon=10</option>
                            <option value="1">epsilon=1</option>
                            <option value="2">epsilon=0.1</option>
                        </select>
                    </div>
                    <input type="text" id="sparse_vector_technique_numerical_t" class="form-control m-b input-lg"
                           placeholder="请输入阈值t" style="font-size: 20px">

                    <input type="text" id="sparse_vector_technique_numerical_c" class="form-control m-b input-lg"
                           placeholder="请输入数值c" style="font-size: 20px">
                    <div class="input-group m-b">
                        <input type="text" id="sparse_vector_technique_numerical_textInput" class="form-control"
                               placeholder="请输入数组，以,分隔数字"
                               style="font-size: 20px">
                        <span class="input-group-btn">
                                <button class="btn btn-default" id="sparse_vector_technique_numerical_submitBtn"
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
                <label for="sparse_vector_technique_numerical_outputText"
                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                <div style="display: flex; flex-direction: column; align-items: center;">
                        <textarea
                                class="col-sm-8 m-t"
                                id="sparse_vector_technique_numerical_outputText"
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

    textarea {
        font-size: 1.5em;
        resize: none;
        overflow-y: scroll;
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

</style>

</html>
