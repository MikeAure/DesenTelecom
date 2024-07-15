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
        document.getElementById("pixelate_video_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("pixelate_video_pre").innerHTML = "";
            document.getElementById("pixelate_video_after").innerHTML = "";
            // 视频格式
            const videoType = ['mp4', 'avi']

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (videoType.includes(fileExtension)) {
                    var pre = document.getElementById("pixelate_video_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var video = document.createElement("video");
                        video.src = e.target.result;
                        /*video.style.width = '300px';*/
                        video.controls = true;
                        pre.appendChild(video);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("pixelate_video_submit").onclick = function () {
                        var after = document.getElementById("pixelate_video_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("pixelate_video_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "pixelate_video");
                        formData.append("sheet", "pixelate_video");

                        fetch('/File/desenFile', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                var video = document.createElement("video");
                                //console.log(data)
                                video.src = URL.createObjectURL(blob);
                                video.type = "video/mp4";
                                video.controls = true;
                                after.appendChild(video);
                            })
                            .catch(error => console.error('Error:', error));

                    }
                } else {
                    alert("请选择视频文件");
                }
            }
        })
        document.getElementById("gaussian_blur_video_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("gaussian_blur_video_pre").innerHTML = "";
            document.getElementById("gaussian_blur_video_after").innerHTML = "";
            // 视频格式
            const videoType = ['mp4', 'avi']

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (videoType.includes(fileExtension)) {
                    var pre = document.getElementById("gaussian_blur_video_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var video = document.createElement("video");
                        video.src = e.target.result;
                        /*video.style.width = '300px';*/
                        video.controls = true;
                        pre.appendChild(video);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("gaussian_blur_video_submit").onclick = function () {
                        var after = document.getElementById("gaussian_blur_video_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("gaussian_blur_video_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "gaussian_blur_video");
                        formData.append("sheet", "gaussian_blur_video");

                        fetch('/File/desenFile', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                var video = document.createElement("video");
                                //console.log(data)
                                video.src = URL.createObjectURL(blob);
                                video.type = "video/mp4";
                                video.controls = true;
                                after.appendChild(video);
                            })
                            .catch(error => console.error('Error:', error));

                    }
                } else {
                    alert("请选择视频文件");
                }
            }
        })
        document.getElementById("box_blur_video_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("box_blur_video_pre").innerHTML = "";
            document.getElementById("box_blur_video_after").innerHTML = "";
            // 视频格式
            const videoType = ['mp4', 'avi']

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (videoType.includes(fileExtension)) {
                    var pre = document.getElementById("box_blur_video_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var video = document.createElement("video");
                        video.src = e.target.result;
                        /*video.style.width = '300px';*/
                        video.controls = true;
                        pre.appendChild(video);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("box_blur_video_submit").onclick = function () {
                        var after = document.getElementById("box_blur_video_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("box_blur_video_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "box_blur_video");
                        formData.append("sheet", "box_blur_video");

                        fetch('/File/desenFile', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                var video = document.createElement("video");
                                //console.log(data)
                                video.src = URL.createObjectURL(blob);
                                video.type = "video/mp4";
                                video.controls = true;
                                after.appendChild(video);
                            })
                            .catch(error => console.error('Error:', error));

                    }
                } else {
                    alert("请选择视频文件");
                }
            }
        })
        document.getElementById("meanValueVideo_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("meanValueVideo_pre").innerHTML = "";
            document.getElementById("meanValueVideo_after").innerHTML = "";
            // 视频格式
            const videoType = ['mp4', 'avi']

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (videoType.includes(fileExtension)) {
                    var pre = document.getElementById("meanValueVideo_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var video = document.createElement("video");
                        video.src = e.target.result;
                        /*video.style.width = '300px';*/
                        video.controls = true;
                        pre.appendChild(video);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("meanValueVideo_submit").onclick = function () {
                        var after = document.getElementById("meanValueVideo_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("meanValueVideo_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "meanValueVideo");
                        formData.append("sheet", "meanValueVideo");

                        fetch('/File/desenFile', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                var video = document.createElement("video");
                                //console.log(data)
                                video.src = URL.createObjectURL(blob);
                                video.type = "video/mp4";
                                video.controls = true;
                                after.appendChild(video);
                            })
                            .catch(error => console.error('Error:', error));

                    }
                } else {
                    alert("请选择视频文件");
                }
            }
        })
        document.getElementById("replace_region_video_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("replace_region_video_pre").innerHTML = "";
            document.getElementById("replace_region_video_after").innerHTML = "";
            // 视频格式
            const videoType = ['mp4', 'avi']

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (videoType.includes(fileExtension)) {
                    var pre = document.getElementById("replace_region_video_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var video = document.createElement("video");
                        video.src = e.target.result;
                        /*video.style.width = '300px';*/
                        video.controls = true;
                        pre.appendChild(video);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("replace_region_video_submit").onclick = function () {
                        var after = document.getElementById("replace_region_video_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("replace_region_video_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "replace_region_video");
                        formData.append("sheet", "replace_region_video");

                        fetch('/File/desenFile', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                var video = document.createElement("video");
                                //console.log(data)
                                video.src = URL.createObjectURL(blob);
                                video.type = "video/mp4";
                                video.controls = true;
                                after.appendChild(video);
                            })
                            .catch(error => console.error('Error:', error));

                    }
                } else {
                    alert("请选择视频文件");
                }
            }
        })
    }

</script>

<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                14.基于像素化滤波器的视频帧像素替换方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">9.基于像素化滤波器的视频帧像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        参数：对视频帧进行像素化滤波
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class=" align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="pixelate_video_fileUpload" style="display: none;">
                                <label for="pixelate_video_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="pixelate_video_privacyLevel">
                                <option value="0"> 低程度</option>
                                <option value="1" selected> 中程度</option>
                                <option value="2"> 高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id="pixelate_video_pre" style="margin-right: 20px;">
            </div>
            <div id="pixelate_video_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="pixelate_video_submit"> 提交脱敏</button>
        </div>

        <div class="text-center">
            <label for="pixelate_video_outputText"
                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏日志:</label>
            <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="pixelate_video_outputText" rows="4" cols="50" readonly
                                              style="margin-top: 10px;"></textarea>
            </div>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                15.基于高斯滤波器的视频帧像素替换方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">9.基于像素化滤波器的视频帧像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        参数：对视频帧进行高斯滤波
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class=" align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="gaussian_blur_video_fileUpload" style="display: none;">
                                <label for="gaussian_blur_video_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="gaussian_blur_video_privacyLevel">
                                <option value="0"> 低程度</option>
                                <option value="1" selected> 中程度</option>
                                <option value="2"> 高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id="gaussian_blur_video_pre" style="margin-right: 20px;">
            </div>
            <div id="gaussian_blur_video_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="gaussian_blur_video_submit"> 提交脱敏</button>
        </div>

        <div class="text-center">
            <label for="gaussian_blur_video_outputText"
                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏日志:</label>
            <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="gaussian_blur_video_outputText" rows="4" cols="50" readonly
                                              style="margin-top: 10px;"></textarea>
            </div>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                16.基于盒式滤波器的视频帧像素替换方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">9.基于像素化滤波器的视频帧像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        参数：对视频帧进行盒式滤波
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class=" align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="box_blur_video_fileUpload" style="display: none;">
                                <label for="box_blur_video_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="box_blur_video_privacyLevel">
                                <option value="0"> 低程度</option>
                                <option value="1" selected> 中程度</option>
                                <option value="2"> 高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id="box_blur_video_pre" style="margin-right: 20px;">
            </div>
            <div id="box_blur_video_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="box_blur_video_submit"> 提交脱敏</button>
        </div>
        <div class="text-center">
            <label for="box_blur_video_outputText"
                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏日志:</label>
            <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="box_blur_video_outputText" rows="4" cols="50" readonly
                                              style="margin-top: 10px;"></textarea>
            </div>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                17.基于均值滤波器的视频帧像素替换方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        参数：滤波核大小
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class=" align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="meanValueVideo_fileUpload" style="display: none;">
                                <label for="meanValueVideo_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="meanValueVideo_privacyLevel">
                                <option value="0"> 低程度</option>
                                <option value="1" selected> 中程度</option>
                                <option value="2"> 高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id="meanValueVideo_pre" style="margin-right: 20px;">
            </div>
            <div id="meanValueVideo_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="meanValueVideo_submit"> 提交脱敏</button>
        </div>
        <div class="text-center">
            <label for="meanValueVideo_outputText"
                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏日志:</label>
            <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="meanValueVideo_outputText" rows="4" cols="50" readonly
                                              style="margin-top: 10px;"></textarea>
            </div>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                18.基于像素块的视频帧像素替换方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将一部分像素替换颜色
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class=" align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="replace_region_video_fileUpload" style="display: none;">
                                <label for="replace_region_video_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="replace_region_video_privacyLevel">
                                <option value="0"> 低程度</option>
                                <option value="1" selected> 中程度</option>
                                <option value="2"> 高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id="replace_region_video_pre" style="margin-right: 20px;">
            </div>
            <div id="replace_region_video_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="replace_region_video_submit"> 提交脱敏</button>
        </div>

        <div class="text-center">
            <label for="replace_region_video_outputText"
                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏日志:</label>
            <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="replace_region_video_outputText" rows="4" cols="50" readonly
                                              style="margin-top: 10px;"></textarea>
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

    .showFile {
        display: flex;
        justify-content: center;
    }

    /*上传按钮*/
    .upload-btn, #meanValueVideo_submit, #pixelate_video_submit, #gaussian_blur_video_submit, #box_blur_video_submit, #replace_region_video_submit {
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }

    #meanValueVideo_pre, #meanValueVideo_after, #replace_region_video_pre, #replace_region_video_after {
        text-align: center;
    }

    #pixelate_video_after, #pixelate_video_pre, #gaussian_blur_video_after, #gaussian_blur_video_pre, #box_blur_video_after, #box_blur_video_pre {
        text-align: center;
    }

    #meanValueVideo_pre, #meanValueVideo_after, #replace_region_video_pre, #replace_region_video_after,
    #pixelate_video_after, #pixelate_video_pre, #gaussian_blur_video_after, #gaussian_blur_video_pre, #box_blur_video_after, #box_blur_video_pre video {
        display: inline-block;
        max-width: 50%;
        height: auto
    }

    /*选择框居中*/
    .midtile {
        line-height: 30px;
        text-align: center;
        display: flex;
        justify-content: center;
    }

</style>

</html>
