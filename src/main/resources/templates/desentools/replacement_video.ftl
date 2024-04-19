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

        /*选择框居中*/
        .midtile {
            line-height: 30px;
            text-align: center;
            display: flex;
            justify-content: center;
        }

        /*上传按钮*/
        .upload-btn, .submit-btn, #audio_reshuffle_submit #image_exchange_channel_submit {
            background-color: #347aa9;
            color: white;
            cursor: pointer;
            padding: 5px 20px;
            text-align: center;
            font-size: 20px;
            display: inline-block;
            margin: 30px;
        }

        #replace_region_after, #replace_region_pre, #audio_reshuffle_after, #audio_reshuffle_pre {
            text-align: center;
        }

        image, video {
            display: inline-block;
            max-width: 50%;
            height: auto
        }

    </style>
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
            // 视频逐帧添加颜色偏移量
            document.getElementById("video_add_color_offset_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("video_add_color_offset_pre").innerHTML = "";
                document.getElementById("video_add_color_offset_after").innerHTML = "";
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
                        let pre = document.getElementById("video_add_color_offset_pre");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let video = document.createElement("video");
                            video.src = e.target.result;
                            /*video.style.width = '300px';*/
                            video.controls = true;
                            pre.appendChild(video);
                        };
                        reader.readAsDataURL(file);
                        //提交脱敏参数，请求脱敏
                        document.getElementById("video_add_color_offset_submit").onclick = function () {
                            let after = document.getElementById("video_add_color_offset_after");
                            after.innerHTML = "";
                            // 获取保护级别
                            let param = document.getElementById("video_add_color_offset_privacyLevel").value;

                            let formData = new FormData();
                            formData.append("file", file);
                            formData.append("params", param);
                            formData.append("algName", "video_add_color_offset");
                            formData.append("sheet", "video_add_color_offset");

                            fetch('/File/desenFile', {
                                method: 'POST',
                                body: formData
                            })
                                .then(response => response.blob())
                                .then(blob => {
                                    let video = document.createElement("video");
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
            });
            // 视频移除背景
            document.getElementById("video_remove_bg_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("video_remove_bg_pre").innerHTML = "";
                document.getElementById("video_remove_bg_after").innerHTML = "";
                // document.getElementById("video_remove_bg_img").innerHTML = "";
                // 视频格式
                const videoType = ['mp4', 'avi']
                // const imageType = ['jpg', 'jpeg', 'png', 'gif'];
                //读取文件
                const videoFile = event.target.files[0]
                // 文件名，扩展名
                if (videoFile) {
                    const fileName = videoFile.name;
                    const fileExtension = fileName.split('.').pop().toLowerCase();
                    console.log(fileExtension)

                    if (videoType.includes(fileExtension)) {
                        let pre = document.getElementById("video_remove_bg_pre");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let video = document.createElement("video");
                            video.src = e.target.result;
                            /*video.style.width = '300px';*/
                            video.controls = true;
                            pre.appendChild(video);
                        };
                        reader.readAsDataURL(videoFile);
                        //提交脱敏参数，请求脱敏
                        // document.getElementById("video_remove_bg_submit").onclick = function () {
                        //     let after = document.getElementById("video_remove_bg_after");
                        //     after.innerHTML = "";
                        //     // 获取保护级别
                        //     let param = document.getElementById("video_remove_bg_privacyLevel").value;
                        //
                        //     let formData = new FormData();
                        //     formData.append("file", videoFile);
                        //     formData.append("params", param);
                        //     formData.append("algName", "video_remove_bg");
                        //     formData.append("sheet", "video_remove_bg");
                        //
                        //     fetch('/File/desenFile', {
                        //         method: 'POST',
                        //         body: formData
                        //     })
                        //         .then(response => response.blob())
                        //         .then(blob => {
                        //             let video = document.createElement("video");
                        //             //console.log(data)
                        //             video.src = URL.createObjectURL(blob);
                        //             video.type = "video/mp4";
                        //             video.controls = true;
                        //             after.appendChild(video);
                        //         })
                        //         .catch(error => console.error('Error:', error));
                        // }
                    }
                    // else {
                    //     alert("请选择视频文件");
                    // }
                }
            });
            document.getElementById("video_remove_bg_img_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("video_remove_bg_img").innerHTML = "";
                // 图片格式
                const imageType = ['jpg', 'jpeg', 'png', 'gif'];
                //读取文件
                const imageFile = event.target.files[0]
                // 文件名，扩展名
                if (imageFile) {
                    const fileName = imageFile.name;
                    const fileExtension = fileName.split('.').pop().toLowerCase();
                    console.log(fileExtension)

                    if (imageType.includes(fileExtension)) {
                        let pre = document.getElementById("video_remove_bg_img");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let img = new Image();
                            img.src = e.target.result;
                            img.setAttribute("width", "500px");
                            img.setAttribute("height", "300px");
                            pre.appendChild(img);
                        };
                        reader.readAsDataURL(imageFile);
                        //提交脱敏参数，请求脱敏
                        // document.getElementById("video_remove_bg_submit").onclick = function () {
                        //     let after = document.getElementById("video_remove_bg_after");
                        //     after.innerHTML = "";
                        //     // 获取保护级别
                        //     let param = document.getElementById("video_remove_bg_privacyLevel").value;
                        //
                        //     let formData = new FormData();
                        //     formData.append("file", imageFile);
                        //     formData.append("params", param);
                        //     formData.append("algName", "video_remove_bg");
                        //     formData.append("sheet", "video_remove_bg");
                        //
                        //     fetch('/File/desenFile', {
                        //         method: 'POST',
                        //         body: formData
                        //     })
                        //         .then(response => response.blob())
                        //         .then(blob => {
                        //             let video = document.createElement("video");
                        //             //console.log(data)
                        //             video.src = URL.createObjectURL(blob);
                        //             video.type = "video/mp4";
                        //             video.controls = true;
                        //             after.appendChild(video);
                        //         })
                        //         .catch(error => console.error('Error:', error));
                        // }
                    }
                }
            });
            document.getElementById("video_remove_bg_submit").addEventListener("click", function (event) {
                // debugger
                // 清空
                const videoFile = document.getElementById("video_remove_bg_fileupload").files[0];
                const imageFile = document.getElementById("video_remove_bg_img_fileupload").files[0];

                // document.getElementById("video_remove_bg_img").innerHTML = "";
                // 视频格式
                const videoType = ['mp4', 'avi']
                const imageType = ['jpg', 'jpeg', 'png', 'gif'];
                //读取文件
                // const videoFile = event.target.files[0]
                // const imageFile =
                // 文件名，扩展名
                if (videoFile && imageFile) {
                    const videoFileName = videoFile.name;
                    const videoFileExtension = videoFileName.split('.').pop().toLowerCase();
                    const imageFileName = imageFile.name;
                    const imageFileExtension = imageFileName.split('.').pop().toLowerCase();

                    console.log(videoFileExtension);
                    console.log(imageFileExtension);

                    if (videoType.includes(videoFileExtension) && imageType.includes(imageFileExtension)) {
                        // let pre = document.getElementById("video_remove_bg_pre");
                        // let reader = new FileReader();
                        // reader.onload = function (e) {
                        //     let video = document.createElement("video");
                        //     video.src = e.target.result;
                        //     /*video.style.width = '300px';*/
                        //     video.controls = true;
                        //     pre.appendChild(video);
                        // };
                        // reader.readAsDataURL(videoFile);
                        //提交脱敏参数，请求脱敏
                        let after = document.getElementById("video_remove_bg_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        let param = document.getElementById("video_remove_bg_privacyLevel").value;

                        let formData = new FormData();
                        formData.append("file", videoFile);
                        formData.append("params", param);
                        formData.append("algName", "video_remove_bg");
                        formData.append("sheet", imageFile);

                        fetch('/File/removeBackground', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                const video = document.createElement("video");
                                //console.log(data)
                                video.src = URL.createObjectURL(blob);
                                video.type = "video/mp4";
                                video.controls = true;
                                after.appendChild(video);
                            })
                            .catch(error => console.error('Error:', error));
                    } else {
                        alert("请选择视频文件");
                    }
                }
            });
            // 视频人脸替换
            document.getElementById("video_face_sub_src_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("video_face_sub_pre").innerHTML = "";
                // document.getElementById("video_remove_bg_img").innerHTML = "";
                const imageType = ['jpg', 'jpeg', 'png', 'gif'];
                //读取文件
                const imageFile = event.target.files[0]
                // 文件名，扩展名
                if (imageFile) {
                    const fileName = imageFile.name;
                    const fileExtension = fileName.split('.').pop().toLowerCase();
                    console.log(fileExtension)

                    if (imageType.includes(fileExtension)) {
                        var pre = document.getElementById("video_face_sub_pre");
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            var img = new Image();
                            img.src = e.target.result;
                            img.style.maxWidth = '80%';
                            img.style.height = 'auto';
                            pre.appendChild(img);
                        };
                        reader.readAsDataURL(imageFile);
                    }
                }
            });
            document.getElementById("video_face_sub_target_fileupload").addEventListener("change", function (event) {

                // 清空
                document.getElementById("video_face_sub_target_video").innerHTML = "";
                // 图片格式
                const videoType = ['mp4', 'avi']
                //读取文件
                const videoFile = event.target.files[0]
                // 文件名，扩展名
                if (videoFile) {
                    const fileName = videoFile.name;
                    const fileExtension = fileName.split('.').pop().toLowerCase();
                    console.log(fileExtension)

                    if (videoType.includes(fileExtension)) {
                        let pre = document.getElementById("video_face_sub_target_video");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let video = document.createElement("video");
                            //console.log(data)
                            video.src = e.target.result;
                            video.type = "video/mp4";
                            video.style.maxWidth = "80%";
                            video.style.height = "auto";
                            video.controls = true;

                            pre.appendChild(video);
                        };
                        reader.readAsDataURL(videoFile);
                    } else {
                        alert("请选择视频文件");
                    }
                }
            });
            document.getElementById("video_face_sub_submit").addEventListener("click", function (event) {
                document.getElementById("video_face_sub_after").innerHTML = "";
                // debugger
                // 清空
                const imgSrcFile = document.getElementById("video_face_sub_src_fileupload").files[0];
                const videoTargetFile = document.getElementById("video_face_sub_target_fileupload").files[0];

                // document.getElementById("video_remove_bg_img").innerHTML = "";
                // 视频格式
                const videoType = ['mp4', 'avi']
                const imageType = ['jpg', 'jpeg', 'png', 'gif'];
                //读取文件
                // const videoFile = event.target.files[0]
                // const imageFile =
                // 文件名，扩展名
                if (imgSrcFile && videoTargetFile) {
                    const imgSrcFileName = imgSrcFile.name;
                    const imgSrcFileNameExtension = imgSrcFileName.split('.').pop().toLowerCase();
                    const videoTargetFileName = videoTargetFile.name;
                    const videoTargetFileNameExtension = videoTargetFileName.split('.').pop().toLowerCase();

                    console.log(imgSrcFileNameExtension);
                    console.log(videoTargetFileNameExtension);

                    if (imageType.includes(imgSrcFileNameExtension) && videoType.includes(videoTargetFileNameExtension)) {
                        // var pre = document.getElementById("video_remove_bg_pre");
                        // var reader = new FileReader();
                        // reader.onload = function (e) {
                        //     var video = document.createElement("video");
                        //     video.src = e.target.result;
                        //     /*video.style.width = '300px';*/
                        //     video.controls = true;
                        //     pre.appendChild(video);
                        // };
                        // reader.readAsDataURL(videoFile);
                        //提交脱敏参数，请求脱敏
                        let after = document.getElementById("video_face_sub_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        let param = "1";

                        let formData = new FormData();
                        formData.append("file", videoTargetFile);
                        formData.append("params", param);
                        formData.append("algName", "video_face_sub");
                        formData.append("sheet", imgSrcFile);

                        fetch('/File/replaceFaceVideo', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => {
                                if (response.status === 200) {
                                    return response.blob();
                                } else {
                                    throw new Error("Python script executes failed");
                                }
                            })
                            .then(blob => {
                                let video = document.createElement("video");
                                //console.log(data)
                                video.src = URL.createObjectURL(blob);
                                video.type = "video/mp4";
                                video.style.maxWidth = "80%";
                                video.style.height = "auto";
                                video.controls = true;
                                after.appendChild(video);
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert(error)
                            });
                    } else {
                        alert("请选择文件");
                    }
                }
            });
        }

    </script>
</head>
<body>
<div class="ibox-title">
</div>
<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                1.基于像素块的视频帧像素颜色偏移方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将每一帧的每个像素RGB通道值加上一个固定的偏移量后作为当前像素新的RGB值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="<#--col-sm-5 --> align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="video_add_color_offset_fileupload" style="display: none;">
                                <label for="video_add_color_offset_fileupload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="video_add_color_offset_privacyLevel">
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
            <div id="video_add_color_offset_pre" style="margin-right: 20px;">
            </div>
            <div id="video_add_color_offset_after">
            </div>
        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="video_add_color_offset_submit">
                提交脱敏
            </button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                2.视频背景替换算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将视频背景进行替换
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：视频，将要替换的新背景
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：视频
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="video_remove_bg_fileupload" style="display: none;">
                                <label for="video_remove_bg_fileupload" class="upload-btn">
                                    选择视频文件
                                </label>
                                <input type="file" id="video_remove_bg_img_fileupload" style="display: none;">
                                <label for="video_remove_bg_img_fileupload" class="upload-btn">
                                    选择背景图片
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择人物轮廓边缘距离（程度越高越贴近人物轮廓）：
                            <select id="video_remove_bg_privacyLevel">
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
            <div id="video_remove_bg_pre" style="margin-right: 20px;">
            </div>
            <div id="video_remove_bg_img" style="margin-right: 20px;">
            </div>
            <div id="video_remove_bg_after">
            </div>
        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="video_remove_bg_submit"> 提交脱敏
            </button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                3.视频人脸替换算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将视频中的人脸进行替换
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：将要替换成的人脸图片，需要替换人脸的视频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：替换人脸后的视频
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="video_face_sub_src_fileupload" style="display: none;">
                                <label for="video_face_sub_src_fileupload" class="upload-btn">
                                    选择换脸源文件（图片）
                                </label>
                                <input type="file" id="video_face_sub_target_fileupload" style="display: none;">
                                <label for="video_face_sub_target_fileupload" class="upload-btn">
                                    选择目标视频
                                </label>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id="video_face_sub_pre" style="margin-right: 20px;">
            </div>
            <div id="video_face_sub_target_video" style="margin-right: 20px;">
            </div>
            <div id="video_face_sub_after">
            </div>

        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="video_face_sub_submit"> 提交脱敏
            </button>
        </div>
    </div>
    <hr>
</div>

</body>
</html>
