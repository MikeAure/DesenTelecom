<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/multiple-select.min.css">
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/GA.css">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">

</head>
<body>
<!-- 全局js -->
<script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx!}/js/xlsx.full.min.js"></script>

<!-- Bootstrap table -->
<script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${ctx!}/js/plugins/bootstrap-table/bootstrap-table-mobile.min.js"></script>
<script src="${ctx!}/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>

<!-- Peity -->
<script src="${ctx!}/js/plugins/peity/jquery.peity.min.js"></script>

<script src="${ctx!}/js/plugins/layer/layer.min.js"></script>
<script src="${ctx!}/js/multiple-select.min.js"></script>


<!-- 自定义js -->
<script src="${ctx!}/js/content.js?v=1.0.0"></script>
<script type="text/javascript">
    window.onload = function () {
        document.getElementById("fileUpload").addEventListener("change", choose_file)
    }
    choose_file = function (event) {
        // 清空
        document.getElementById("pre").innerHTML = "";
        document.getElementById("after").innerHTML = "";
        document.getElementById("fileInfo").innerHTML = "";
        document.getElementById('algType').innerHTML = "";
        document.getElementById("privacyLevel").innerHTML = "";
        // 图像格式
        const imageType = ['jpg', 'jpeg', 'png', 'gif'];
        // 音频格式
        const audioType = ['wav', 'mp3'];
        // 视频格式
        const videoType = ['mp4', 'avi']
        //读取文件
        const file = event.target.files[0]
        // 文件名，扩展名
        if (file) {
            const fileName = file.name;
            var fileLoad = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                "<strong>" + fileName + "文件</strong>上传成功"
            "</span>" +
            "</div>";
            document.getElementById("fileInfo").innerHTML = fileLoad
            const fileExtension = fileName.split('.').pop().toLowerCase();
            console.log(fileExtension)

            //构建formData,发送给后端
            var formData = new FormData();
            formData.append("file", file);
            formData.append("sheet", "media");

            var html = ""
            if (imageType.includes(fileExtension)) {
                uploadImage()
                let alg = "<div>" +
                    "<table style=\'margin: auto; font-size: 20px\'>" +
                    "<thead>" +
                    "<tr>" +
                    "<th>请选择图像脱敏算法</th>" +
                    "</tr>" +
                    "</thead>" +
                    "<tbody id=\"table1\">" +
                    "</tr>" +
                    "<td><select>" +
                    "<option value = \"lvbo\">滤波算法</option>" +
                    "<option value = \"dp\" selected>加噪算法</option>" +
                    "</select></td>" +
                    "</tr>" +
                    "</tbody>" +
                    "</table>" +
                    "</div>";
                html += "<div>" +
                    "<table style=\'margin: auto; font-size: 20px\'>" +
                    "<thead>" +
                    "<tr>" +
                    "<th>请选择隐私保护等级</th>" +
                    "</tr>" +
                    "</thead>" +
                    "<tbody id=\"table2\">" +
                    "</tr>" +
                    "<td><select>" +
                    "<option value = " + 0 + ">低程度</option>" +
                    "<option value = " + 1 + " selected>中程度</option>" +
                    "<option value = " + 2 + ">高程度</option>" +
                    "</select></td>" +
                    "</tr>" +
                    "</tbody>" +
                    "</table>" +
                    "</div>";
                document.getElementById("algType").innerHTML = alg;
                document.getElementById("privacyLevel").innerHTML = html;
                //提交脱敏参数，请求脱敏
                document.getElementById("submit").onclick = function () {
                    var after = document.getElementById("after");
                    after.innerHTML = "";
                    // 获取保护级别
                    var table_body = document.getElementById("table2")
                    var tr = table_body.rows[0];
                    var param = tr.childNodes[0].firstChild.value;
                    formData.append("params", param);

                    // 获取算法类型
                    table_body = document.getElementById("table1")
                    tr = table_body.rows[0];
                    var type = tr.childNodes[0].firstChild.value;
                    formData.append("sheet", type);
                    fetch('File/desenFile', {
                        method: 'POST',
                        body: formData
                    })
                        .then(response => response.blob())
                        .then(blob => {
                            var dealedImg = new Image();
                            dealedImg.src = URL.createObjectURL(blob);
                            after.appendChild(dealedImg);
                        })

                }
            } else if (audioType.includes(fileExtension)) {
                uploadAudio();
                html += "<div>" +
                    "<table style=\'margin: auto; font-size: 20px\'>" +
                    "<thead>" +
                    "<tr>" +
                    "<th>请选择隐私保护等级</th>" +
                    "</tr>" +
                    "</thead>" +
                    "<tbody id=\"table2\">" +
                    "</tr>" +
                    "<td><select>" +
                    "<option value = " + 0 + ">低程度</option>" +
                    "<option value = " + 1 + " selected>中程度</option>" +
                    "<option value = " + 2 + ">高程度</option>" +
                    "</select></td>" +
                    "</tr>" +
                    "</tbody>" +
                    "</table>" +
                    "</div>";
                document.getElementById("table_body").innerHTML = html;

                //提交脱敏参数，请求脱敏
                document.getElementById("submit").onclick = function () {
                    var after = document.getElementById("after");
                    after.innerHTML = "";
                    // 获取保护级别
                    var table_body = document.getElementById("table2")
                    var tr = table_body.rows[0];
                    var param = tr.childNodes[0].firstChild.value;
                    formData.append("params", param);
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
            } else if (videoType.includes(fileExtension)) {
                uploadVideo()
                html += "<div>" +
                    "<table style=\'margin: auto; font-size: 20px\'>" +
                    "<thead>" +
                    "<tr>" +
                    "<th>请选择隐私保护等级</th>" +
                    "</tr>" +
                    "</thead>" +
                    "<tbody id=\"table2\">" +
                    "</tr>" +
                    "<td><select>" +
                    "<option value = " + 0 + ">低程度</option>" +
                    "<option value = " + 1 + " selected>中程度</option>" +
                    "<option value = " + 2 + ">高程度</option>" +
                    "</select></td>" +
                    "</tr>" +
                    "</tbody>" +
                    "</table>" +
                    "</div>";
                document.getElementById("table_body").innerHTML = html;

                //提交脱敏参数，请求脱敏
                document.getElementById("submit").onclick = function () {
                    var after = document.getElementById("after");
                    after.innerHTML = "";
                    // 获取保护级别
                    var table_body = document.getElementById("table2")
                    var tr = table_body.rows[0];
                    var param = tr.childNodes[0].firstChild.value;
                    formData.append("params", param);
                    fetch('File/desenFile', {
                        method: 'POST',
                        body: formData
                    })
                        .then(response => response.blob())
                        .then(data => {
                            var video = document.createElement("video");
                            console.log(data)
                            video.src = URL.createObjectURL(data);
                            video.type = "video/mp4";
                            video.controls = true;
                            after.appendChild(video);

                        })
                        .catch(error => {
                            console.log("Error:", error);
                        })
                }
            } else {
                html += "<div>" +
                    "<table style=\'margin: auto; font-size: 20px\'>" +
                    "<thead>" +
                    "<tr>" +
                    "<th>请选择隐私保护等级</th>" +
                    "</tr>" +
                    "</thead>" +
                    "<tbody id=\"table2\">" +
                    "</tr>" +
                    "<td><select>" +
                    "<option value = " + 0 + ">低程度</option>" +
                    "<option value = " + 1 + " selected>中程度</option>" +
                    "<option value = " + 2 + ">高程度</option>" +
                    "</select></td>" +
                    "</tr>" +
                    "</tbody>" +
                    "</table>" +
                    "</div>";
                document.getElementById("table_body").innerHTML = html;

                //提交脱敏参数，请求脱敏
                document.getElementById("submit").onclick = function () {
                    var after = document.getElementById("after");
                    after.innerHTML = ""
                    // 获取保护级别
                    var table_body = document.getElementById("table2")
                    var tr = table_body.rows[0];
                    var param = tr.childNodes[0].firstChild.value;
                    formData.append("params", param);
                    fetch('/File/desenFile', {
                        method: 'POST',
                        body: formData
                    })
                        .then(response => response.blob())
                        .then(blob => {
                            // 创建一个下载链接
                            const downloadLink = document.createElement('a');
                            downloadLink.href = URL.createObjectURL(blob);
                            downloadLink.download = "graph" + Date.now().toString(); // 下载的文件名
                            downloadLink.click();
                            after.appendChild(downloadLink);
                        })
                        .catch(error => console.error('Error:', error));
                }
            }
        }
    }
    uploadImage = function () {
        var input = document.getElementById("fileUpload");
        var pre = document.getElementById("pre");
        var file = input.files[0];
        var reader = new FileReader();
        reader.onload = function (e) {
            var img = new Image();
            img.src = e.target.result;
            //img.style.width = '500px';
            pre.appendChild(img);
        };
        reader.readAsDataURL(file);
    }
    uploadVideo = function () {
        var input = document.getElementById("fileUpload");
        var pre = document.getElementById("pre");
        var file = input.files[0];
        var reader = new FileReader();
        reader.onload = function (e) {
            var video = document.createElement("video");
            video.src = e.target.result;
            /*video.style.width = '300px';*/
            video.controls = true;
            pre.appendChild(video);
        };
        reader.readAsDataURL(file);
    }
    uploadAudio = function () {
        var input = document.getElementById("fileUpload");
        var pre = document.getElementById("pre");
        var file = input.files[0];
        var reader = new FileReader();
        reader.onload = function (e) {
            var video = document.createElement("audio");
            video.src = e.target.result;
            /*video.style.width = '300px';*/
            video.controls = true;
            pre.appendChild(video);
        };
        reader.readAsDataURL(file);
    }
</script>
<div class="row">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
            </div>
            <div class="midtile">
                <div class="col-sm-5 m-b-xs">
                    <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                        <input type="file" id="fileUpload" style="display: none;">
                        <label for="fileUpload" class="upload-btn">
                            上传文件
                        </label>
                    </form>
                </div>
            </div>
            <!--文件上传信息-->
            <div id=fileInfo>
            </div>

            <div class="showFile">
                <!--前后文件-->
                <div id=pre>
                </div>
                <div id=after>
                </div>
            </div>

            <div class="ibox-content">
                <div id="algType"></div>
            </div>

            <div class="ibox-content">
                <div id="privacyLevel"></div>
            </div>

            <div class="btn2">
                <button type="button" class="btn btn-sm btn-primary" id="submit"> 提交脱敏</button>
            </div>

            <div id="result">
            </div>
        </div>
    </div>

</div>
</body>
<style>
    /*标题*/
    .ibox-title {
        height: 200px;
        border-color: #edf1f2;
        background-color: #dbeafe;
        color: black;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .ibox-title span {
        font-size: 50px;
    }

    #submit {
        background-color: #347aa9;
        padding: 5px 20px;
        cursor: pointer;
        color: black;
        font-size: 20px;
        display: inline-block;
        text-align: center;
        /*margin-right: 50px;*/

    }

    .btn2 {
        line-height: 30px;
        text-align: center;
        display: flex;
        justify-content: center;
    }

    /*选择框居中*/
    .midtile {
        line-height: 30px;
        text-align: center;
        display: flex;
        justify-content: center;
    }

    /*上传按钮*/
    .upload-btn {
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }

    #pre {
        text-align: center;
    }

    #pre img {
        display: inline-block;
        max-width: 50%;
        height: auto;
    }

    #pre video {
        display: inline-block;
        max-width: 50%;
        height: auto
    }

    #after {
        text-align: center;
    }

    #after img {
        display: inline-block;
        max-width: 50%;
        height: auto;
    }

    #after video {
        display: inline-block;
        max-width: 50%;
        height: auto
    }

    .showFile {
        display: flex;
        justify-content: center;
    }
</style>
</html>
