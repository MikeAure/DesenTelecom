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
        // mixzone_1
        document.getElementById("mixzone_1_submitBtn").addEventListener("click", function () {
            let position = $("#mixzone_1_position").val();
            let id = $("#mixzone_1_id").val();
            let time = $("#mixzone_1_time").val();
            let points = $("#mixzone_1_points").val();

            if (position === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/Location/mixzone_1", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:
                    '&position=' + encodeURIComponent(position) +
                    '&id=' + encodeURIComponent(id) +
                    '&time=' + encodeURIComponent(time) +
                    '&points=' + encodeURIComponent(points)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("mixzone_1_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })

        //mixzone_3
        document.getElementById("mixzone_3_submitBtn").addEventListener("click", function () {
            let position = $("#mixzone_3_position").val();
            let id = $("#mixzone_3_id").val();
            let time = $("#mixzone_3_time").val();
            let points = $("#mixzone_3_points").val();

            if (position === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/Location/mixzone_3", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:
                    '&position=' + encodeURIComponent(position) +
                    '&id=' + encodeURIComponent(id) +
                    '&time=' + encodeURIComponent(time) +
                    '&points=' + encodeURIComponent(points)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("mixzone_3_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        //Accuracy_reduction
        document.getElementById("Accuracy_reduction").addEventListener("click", function () {
            let position = $("#Accuracy_reduction_position").val();

            if (position === "") {
                alert("请输入文本");
                return; // Stop further execution if the text input is empty
            }

            fetch("/Location/Accuracy_reduction", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body:
                    '&rawData=' + encodeURIComponent(position)
            })
                .then(response => response.text())
                .then(data => {
                    document.getElementById("Accuracy_reduction_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
    }

</script>
<div class="ibox-title">
</div>

<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                6.mixzone_1</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：根据用户不同的位置信息生成合适的假名位置信息。该算法根据用户位置判断其是否在虚拟圆的混合区域，若在混合区域，则服务器随机生成假名；若判断用户不在混合区，则直接返回用户真实位置信息。
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：经度、纬度、用户id、进入区域的时间、区域点集
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：字符串（用户假名id）
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <!-- 新加的两个文本输入框 -->
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="mixzone_1_position" class="form-control"
                                       placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="mixzone_1_id" class="form-control" placeholder="请输入用户id"
                                       style="font-size: 20px">
                            </div>
                            <div class="input-group">
                                <input type="text" id="mixzone_1_time" class="form-control"
                                       placeholder="请输入进入区域的时间，格式：X.X" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="mixzone_1_points" class="form-control"
                                       placeholder="请输入用户点集, 格式 x1,y1;x2,y2 ..." style="font-size: 20px">
                                <span class="input-group-btn">
                                        <button class="btn btn-default" id="mixzone_1_submitBtn" type="button"
                                                style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                </span>
                            </div>

                            <div class="text-center">
                                <label for="mixzone_1_outputText"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="mixzone_1_outputText" rows="4" cols="100" readonly
                                              style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                7.mixzone_3</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：首先根据用户位置判断其是否在虚拟圆的混合区域，若在混合区域，则客户端直接从假名数据库中得到随机假名；若判断用户不在混合区，则直接返回用户真实位置信息。
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：经度、纬度、用户id、进入区域的时间、区域点集
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：字符串（用户假名id）
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <!-- 新加的两个文本输入框 -->
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="mixzone_3_position" class="form-control"
                                       placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="mixzone_3_id" class="form-control" placeholder="请输入用户id"
                                       style="font-size: 20px">
                            </div>
                            <div class="input-group">
                                <input type="text" id="mixzone_3_time" class="form-control"
                                       placeholder="请输入进入区域的时间，格式：X.X" style="font-size: 20px">
                            </div>
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="mixzone_3_points" class="form-control"
                                       placeholder="请输入用户点集, 格式 x1,y1;x2,y2 ..." style="font-size: 20px">
                                <span class="input-group-btn">
                                        <button class="btn btn-default" id="mixzone_3_submitBtn" type="button"
                                                style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                </span>
                            </div>

                            <div class="text-center">
                                <label for="mixzone_3_outputText"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="mixzone_3_outputText" rows="4" cols="100" readonly
                                              style="margin-top: 10px;"></textarea>
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                8.Accuracy_reduction</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：根据用户不同的位置信息生成合适的假名位置信息。该算法根据用户位置判断其是否在虚拟圆的混合区域，若在混合区域，则服务器随机生成假名；若判断用户不在混合区，则直接返回用户真实位置信息。
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：经度、纬度
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：经度、纬度
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>

                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <!-- 新加的两个文本输入框 -->
                            <div class="input-group" style="margin-top: 10px;">
                                <input type="text" id="Accuracy_reduction_position" class="form-control"
                                       placeholder="请输入经纬度，以,分隔" style="font-size: 20px">
                                <span class="input-group-btn">
                                        <button class="btn btn-default" id="Accuracy_reduction" type="button"
                                                style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                            提交脱敏
                                        </button>
                                </span>
                            </div>

                            <div class="text-center">
                                <label for="Accuracy_reduction_outputText"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="Accuracy_reduction_outputText" rows="4" cols="100" readonly
                                              style="margin-top: 10px;"></textarea>
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
