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

        document.getElementById("addressHide_submitBtn").addEventListener("click", function () {
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
                body: '&textInput=' + encodeURIComponent(textInput) +
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
        document.getElementById("numberHide_submitBtn").addEventListener("click", function () {
            let textInput = $("#numberHide_textInput").val();
            var privacyLevel = 1
            var textType = "number"
            var algName = "numberHide"
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
                    document.getElementById("numberHide_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("nameHide_submitBtn").addEventListener("click", function () {
            let textInput = $("#nameHide_textInput").val();
            var textType = "name"
            var algName = "nameHide"
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
                    document.getElementById("nameHide_outputText").value = data;
                })
                .catch(error => console.error('Error:', error));
        })
        document.getElementById("meanValueImage_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("meanValueImage_pre").innerHTML = "";
            document.getElementById("meanValueImage_after").innerHTML = "";
            // 图像格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];
            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (imageType.includes(fileExtension)) {
                    var pre = document.getElementById("meanValueImage_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("meanValueImage_submit").onclick = function () {
                        var after = document.getElementById("meanValueImage_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("meanValueImage_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "meanValueImage");
                        formData.append("sheet", "meanValueImage");

                        fetch('/File/desenFile', {
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
                } else {
                    alert("请选择图像文件");
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
                        formData.append("sheet", "dp");

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
        document.getElementById("pixelate_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("pixelate_pre").innerHTML = "";
            document.getElementById("pixelate_after").innerHTML = "";
            // 图像格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];
            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (imageType.includes(fileExtension)) {
                    var pre = document.getElementById("pixelate_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("pixelate_submit").onclick = function () {
                        var after = document.getElementById("pixelate_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("pixelate_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "pixelate");
                        formData.append("sheet", "pixelate");

                        fetch('/File/desenFile', {
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
                } else {
                    alert("请选择图像文件");
                }
            }
        })
        document.getElementById("gaussian_blur_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("gaussian_blur_pre").innerHTML = "";
            document.getElementById("gaussian_blur_after").innerHTML = "";
            // 图像格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];
            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (imageType.includes(fileExtension)) {
                    var pre = document.getElementById("gaussian_blur_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("gaussian_blur_submit").onclick = function () {
                        var after = document.getElementById("gaussian_blur_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("gaussian_blur_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "gaussian_blur");
                        formData.append("sheet", "gaussian_blur");

                        fetch('/File/desenFile', {
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
                } else {
                    alert("请选择图像文件");
                }
            }
        })
        document.getElementById("box_blur_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("box_blur_pre").innerHTML = "";
            document.getElementById("box_blur_after").innerHTML = "";
            // 图像格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];
            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (imageType.includes(fileExtension)) {
                    var pre = document.getElementById("box_blur_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("box_blur_submit").onclick = function () {
                        var after = document.getElementById("box_blur_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("box_blur_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "box_blur");
                        formData.append("sheet", "box_blur");

                        fetch('/File/desenFile', {
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
                } else {
                    alert("请选择图像文件");
                }
            }
        })
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
        document.getElementById("remove_audio_fileUpload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("remove_audio_pre").innerHTML = "";
            document.getElementById("remove_audio_after").innerHTML = "";
            // 音频格式
            const audioType = ['wav', 'mp3'];
            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (audioType.includes(fileExtension)) {
                    var pre = document.getElementById("remove_audio_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var video = document.createElement("audio");
                        video.src = e.target.result;
                        video.controls = true;
                        pre.appendChild(video);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("remove_audio_submit").onclick = function () {
                        var after = document.getElementById("remove_audio_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("remove_audio_start").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "remove_audio");
                        formData.append("sheet", document.getElementById("remove_audio_chixu").value);

                        fetch('/File/desenFile', {
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
                } else {
                    alert("请选择音频文件");
                }
            }
        })
        document.getElementById("suppressEmail_submitBtn").addEventListener("click", function () {
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
                body: '&textInput=' + encodeURIComponent(textInput) +
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
        document.getElementById("suppressAllIp_submitBtn").addEventListener("click", function () {
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
                body: '&textInput=' + encodeURIComponent(textInput) +
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
        document.getElementById("suppressIpRandomParts_submitBtn").addEventListener("click", function () {
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
                body: '&textInput=' + encodeURIComponent(textInput) +
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
    <div class="panel-heading" style="text-align: center;">
        <h1 class="panel-title"><b style="font-size: 2em">数据抑制</b></h1>
    </div>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                1.地址抑制算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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
            <div class="container">
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5">
                        <div class="input-group">
                            <input type="text" id="addressHide_textInput" class="form-control" placeholder="请输入文本"
                                   style="font-size: 20px">
                            <span class="input-group-btn">
                    <button class="btn btn-default" id="addressHide_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                        </div>
                        <div class="text-center">
                            <label for="addressHide_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="addressHide_outputText" rows="2" cols="50" readonly
                                          style="margin-top: 10px;"></textarea>
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
                2.编号抑制算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">2.编号抑制算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：，隐藏中间部分的数据，用*代替
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
            <div class="container">
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5">
                        <div class="input-group">
                            <input type="text" id="numberHide_textInput" class="form-control" placeholder="请输入文本"
                                   style="font-size: 20px">
                            <span class="input-group-btn">
                                <button class="btn btn-default" id="numberHide_submitBtn" type="button"
                                        style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                    提交脱敏
                                </button>
                            </span>
                        </div>
                        <div class="text-center">
                            <label for="numberHide_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="numberHide_outputText" rows="2" cols="50" readonly
                                          style="margin-top: 10px;"></textarea>
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
                3.名称抑制算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">3.名称抑制算法</p>-->
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
            <div class="container">
                <div class="row justify-content-center" style="display: grid; place-items: center;">
                    <div class="col-lg-5">
                        <div class="input-group">
                            <input type="text" id="nameHide_textInput" class="form-control" placeholder="请输入文本"
                                   style="font-size: 20px">
                            <span class="input-group-btn">
                    <button class="btn btn-default" id="nameHide_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                        </div>
                        <div class="text-center">
                            <label for="nameHide_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="nameHide_outputText" rows="2" cols="50" readonly
                                          style="margin-top: 10px;"></textarea>
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
                4.均值滤波图像脱敏算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#-- <p style="font-size: 1.5em;">4.均值滤波图像脱敏算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：图像的像素进行均值滤波
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
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="meanValueImage_fileUpload" style="display: none;">
                                <label for="meanValueImage_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="meanValueImage_privacyLevel">
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
            <div id="meanValueImage_pre" style="margin-right: 20px;">
            </div>
            <div id="meanValueImage_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="meanValueImage_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                5.均值滤波视频脱敏算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#-- <p style="font-size: 1.5em;">4.均值滤波图像脱敏算法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：像素进行均值滤波
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

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                6.基于像素化滤波器的图像像素替换方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#--<p style="font-size: 1.5em;">6.基于像素化滤波器的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对图像进行像素化滤波
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
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="pixelate_fileUpload" style="display: none;">
                                <label for="pixelate_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="pixelate_privacyLevel">
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
            <div id="pixelate_pre" style="margin-right: 20px;">
            </div>
            <div id="pixelate_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="pixelate_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                7.基于高斯滤波器的图像像素替换方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <#-- <p style="font-size: 1.5em;">7.基于高斯滤波器的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对图像进行高斯滤波
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
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="gaussian_blur_fileUpload" style="display: none;">
                                <label for="gaussian_blur_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="gaussian_blur_privacyLevel">
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
            <div id="gaussian_blur_pre" style="margin-right: 20px;">
            </div>
            <div id="gaussian_blur_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="gaussian_blur_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                8.基于盒式滤波器的图像像素替换方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#--<p style="font-size: 1.5em;">8.基于盒式滤波器的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对图像进行盒式滤波
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
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="box_blur_fileUpload" style="display: none;">
                                <label for="box_blur_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="box_blur_privacyLevel">
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
            <div id="box_blur_pre" style="margin-right: 20px;">
            </div>
            <div id="box_blur_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="box_blur_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                9.基于像素化滤波器的视频帧像素替换方法</p>
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

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                10.基于高斯滤波器的视频帧像素替换方法</p>
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

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                11.基于盒式滤波器的视频帧像素替换方法</p>
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

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                12.音频剪裁</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">

                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：对音频进行剪裁
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：音频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：音频
                    </p>
                    <#-- <div &lt;#&ndash;class="col-sm-6"&ndash;&gt; style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                         <div>
                             <p style="font-size: 1.5em;text-align: justify;">
                                 输入：对音频进行剪裁
                             </p>
                             <p style="font-size: 1.5em;text-align: justify;">
                                 输入：音频
                             </p>
                             <p style="font-size: 1.5em;text-align: justify;">
                                 输出：音频
                             </p>-->
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="remove_audio_fileUpload" style="display: none;">
                                <label for="remove_audio_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style=" font-size: 20px">
                            <form>
                                <label for="remove_audio_start">开始时间:</label>
                                <input type="text" id="remove_audio_start" name="input1" placeholder="单位：秒">
                                <label for="remove_audio_chixu">持续时间:</label>
                                <input type="text" id="remove_audio_chixu" name="input2" placeholder="单位：秒">
                            </form>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="showFile">
            <!--前后文件-->
            <div id=remove_audio_pre style="margin-right: 20px;">
            </div>
            <div id="remove_audio_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="remove_audio_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                13.邮箱抑制算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="suppressEmail_input" class="form-control"
                                       placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn">
                    <button class="btn btn-default" id="suppressEmail_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="suppressEmail_output"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="suppressEmail_output" rows="2" cols="50" readonly
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
                14.IP地址全抑制</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="suppressAllIp_input" class="form-control"
                                       placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn">
                    <button class="btn btn-default" id="suppressAllIp_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="suppressAllIp_output"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="suppressAllIp_output" rows="2" cols="50" readonly
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
                15.IP地址随机抑制</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="suppressIpRandomParts_input" class="form-control"
                                       placeholder="请输入文本" style="font-size: 20px">
                                <span class="input-group-btn">
                    <button class="btn btn-default" id="suppressIpRandomParts_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="suppressIpRandomParts_output"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="suppressIpRandomParts_output" rows="2" cols="50" readonly
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

    .showFile {
        display: flex;
        justify-content: center;
    }

    /*上传按钮*/
    .upload-btn, #meanValueImage_submit, #meanValueVideo_submit, #pixelate_submit, #box_blur_submit,
    #gaussian_blur_submit, #pixelate_video_submit, #gaussian_blur_video_submit, #box_blur_video_submit, #remove_audio_submit {
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }

    #meanValueImage_pre, #meanValueImage_pre, #remove_audio_after, #remove_audio_pre {
        text-align: center;
    }

    #meanValueVideo_pre, #meanValueVideo_pre {
        text-align: center;
    }

    #pixelate_pre, #pixelate_pre {
        text-align: center;
    }

    #gaussian_blur_pre, #gaussian_blur_pre {
        text-align: center;
    }

    #box_blur_pre, #box_blur_pre {
        text-align: center;
    }

    #pixelate_video_after, #pixelate_video_pre, #gaussian_blur_video_after, #gaussian_blur_video_pre, #box_blur_video_after, #box_blur_video_pre {
        text-align: center;
    }

    image, video {
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
