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
    <style>
        th {
            text-align: center;
        }

        /*标题*/
        /*.ibox-title {*/
        /*    height: 200px;*/
        /*    border-color: #edf1f2;*/
        /*    background-color: #dbeafe;*/
        /*    color: black;*/
        /*    display: flex;*/
        /*    align-items: center;*/
        /*    justify-content: center;*/
        /*}*/

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
        #videoAesEncrypt button,
        #selectRawVideo,
        #selectEncryptedVideo,
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

        /*#pre {*/
        /*    text-align: center;*/
        /*}*/

        /*#pre video {*/
        /*    display: inline-block;*/
        /*    max-width: 50%;*/
        /*    height: auto*/
        /*}*/

        #pre,
        #showRawVideo,
        #showDecryptedVideo,
        #after {
            text-align: center;
        }

        #pre video,
        #showRawVideo video,
        #showDecryptedVideo video,
        #after video {
            display: inline-block;
            max-width: 50%;
            height: auto
        }

        .showFile {
            display: flex;
            justify-content: center;
        }

        .tabs-container ul {
            display: flex;
            flex-direction: row;
            justify-content: center;
        }

        #mid-image img {
            display: inline-block;
            max-width: 50%;
            height: auto;
        }
    </style>
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
            document.getElementById("nodistortionAlg").addEventListener("change", function () {
                let selection = document.getElementById("nodistortionAlg").value;

                const videoAesEncryptModule = document.getElementById("videoAesEncrypt");
                const defaultModule = document.getElementById("default-module");
                // const voiceprint_module = document.getElementById("voiceprint-module");

                if (selection === "trace") {
                    // voiceprint_module.style.visibility = "hidden";
                    videoAesEncryptModule.style.visibility = "visible";
                    defaultModule.style.visibility = "hidden";
                } else {
                    // voiceprint_module.style.visibility = "hidden";
                    videoAesEncryptModule.style.visibility = "hidden";
                    defaultModule.style.visibility = "visible";
                }
            });
            let submitButton = document.getElementById("submit");
            let fileUpload = document.getElementById("fileUpload");
            let distortionTypes = document.getElementById("distortion-types");
            let preArea = document.getElementById("pre");
            let afterArea = document.getElementById("after");
            let midImageArea = document.getElementById("mid-image");
            let defaultBtns = document.getElementById("default-choosefile-btn");
            let videoBgBtns = document.getElementById("video-bg-sub-btns");
            let videoFaceSubBtns = document.getElementById("video-face-sub-btns");
            let privacyLevel = document.getElementById("privacyLevel");

            distortionTypes.addEventListener("change", displacePart)

            fileUpload.addEventListener("change", chooseFile);
            $("#submit").on("click", originalSubmit);

            // 非失真脱敏，为选择代价密视频文件绑定事件
            // document.getElementById("selectEncryptedVideo").addEventListener("change", (event) => {
            //     nonDistortionVideoShowFile(event, "selectEncryptedVideo", "showDecryptedVideo")
            // });

            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                preArea.innerHTML = "";
                afterArea.innerHTML = "";
                midImageArea.innerHTML = "";
                defaultBtns.style.display = "flex";
                videoBgBtns.style.display = "none";
                videoFaceSubBtns.style.display = "none"
                privacyLevel.style.display = "block";
                $("#distortion-types").val("meanValueVideo");
            })
            //提交脱敏参数，请求脱敏

            // 视频移除背景
            document.getElementById("video_remove_bg_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("pre").innerHTML = "";
                document.getElementById("after").innerHTML = "";
                // document.getElementById("video_remove_bg_img").innerHTML = "";
                // 视频格式
                const videoType = ['mp4']
                // const imageType = ['jpg', 'jpeg', 'png', 'gif'];
                //读取文件
                const videoFile = event.target.files[0]
                // 文件名，扩展名
                if (videoFile) {
                    const fileName = videoFile.name;
                    const fileExtension = fileName.split('.').pop().toLowerCase();
                    console.log(fileExtension)

                    if (videoType.includes(fileExtension)) {
                        let pre = document.getElementById("pre");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let video = document.createElement("video");
                            video.src = e.target.result;
                            /*video.style.width = '300px';*/
                            video.controls = true;
                            pre.appendChild(video);
                        };
                        reader.readAsDataURL(videoFile);
                    } else {
                        alert("请选择视频文件");
                    }
                }
            });
            document.getElementById("video_remove_bg_img_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("mid-image").innerHTML = "";
                // 图片格式
                const imageType = ['jpg', 'jpeg', 'png'];
                //读取文件
                const imageFile = event.target.files[0]
                // 文件名，扩展名
                if (imageFile) {
                    const fileName = imageFile.name;
                    const fileExtension = fileName.split('.').pop().toLowerCase();
                    console.log(fileExtension)

                    if (imageType.includes(fileExtension)) {
                        let midImage = document.getElementById("mid-image");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let img = new Image();
                            img.src = e.target.result;
                            midImage.appendChild(img);
                        };
                        reader.readAsDataURL(imageFile);
                        //提交脱敏参数，请求脱敏
                    } else {
                        alert("请选择图片文件")
                    }
                }
            });
            // 视频人脸替换
            document.getElementById("video_face_sub_src_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("mid-image").innerHTML = "";
                // document.getElementById("video_remove_bg_img").innerHTML = "";
                const imageType = ['jpg', 'jpeg', 'png'];
                //读取文件
                const imageFile = event.target.files[0]
                // 文件名，扩展名
                if (imageFile) {
                    const fileName = imageFile.name;
                    const fileExtension = fileName.split('.').pop().toLowerCase();
                    console.log(fileExtension)

                    if (imageType.includes(fileExtension)) {
                        let pre = document.getElementById("mid-image");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let img = new Image();
                            img.src = e.target.result;
                            pre.appendChild(img);
                        };
                        reader.readAsDataURL(imageFile);
                    }
                }
            });
            document.getElementById("video_face_sub_target_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("pre").innerHTML = "";
                // 图片格式
                const videoType = ['mp4']
                //读取文件
                const videoFile = event.target.files[0]
                // 文件名，扩展名
                if (videoFile) {
                    const fileName = videoFile.name;
                    const fileExtension = fileName.split('.').pop().toLowerCase();
                    console.log(fileExtension)

                    if (videoType.includes(fileExtension)) {
                        let pre = document.getElementById("pre");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let video = document.createElement("video");
                            //console.log(data)
                            video.src = e.target.result;
                            video.type = "video/mp4";
                            video.style.maxWidth = "50%";
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

            // 非失真脱敏，为选择待加密视频文件绑定事件
            document.getElementById("selectRawVideo").addEventListener("change", (event) => {
                // 清空
                document.getElementById("aesRawVideoInfo").innerHTML = "";
                //读取文件
                const file = event.target.files[0]
                // 文件名，扩展名
                const fileName = file.name;
                document.getElementById("aesRawVideoInfo").innerHTML = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                    "<strong>" + fileName + "文件</strong>上传成功" + "</span>" + "</div>";

                nonDistortionVideoShowFile(event, "selectRawVideo", "showRawVideo")
            });

            document.getElementById("encryptVideo").onclick = function () {
                let encryptKey = document.getElementById("encryptKey").value;
                let file = document.getElementById("selectRawVideo").files[0];
                if (!file) {
                    alert("未选择待加密文件");
                    return;
                }
                if (!encryptKey) {
                    alert("未指定加密密钥");
                    return;
                }
                let formData = new FormData();
                formData.set("file", file);
                formData.set("password", encryptKey);

                fetch('/Encrypt/aesVideoEnc', {
                    method: 'POST',
                    body: formData
                })
                    .then(response => response.blob())
                    .then(data => {
                        const downloadLink = document.createElement('a');
                        downloadLink.href = URL.createObjectURL(data);
                        downloadLink.download = Date.now().toString() + ".mp4"; // 下载的文件名
                        downloadLink.click();
                    })
                    .catch(error => {
                        console.log("Error:", error);
                    })
            }

            document.getElementById("selectEncryptedVideo").addEventListener("change", (event) => {
                // 清空
                document.getElementById("aesDecryptedVideoInfo").innerHTML = "";
                //读取文件
                const file = event.target.files[0]
                // 文件名，扩展名
                const fileName = file.name;
                document.getElementById("aesDecryptedVideoInfo").innerHTML = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                    "<strong>" + fileName + "文件</strong>上传成功" + "</span>" + "</div>";

            });

            document.getElementById("decryptVideo").onclick = function () {
                let showDecryptedVideo = document.getElementById("showDecryptedVideo");
                showDecryptedVideo.innerHTML = "";
                let decryptKey = document.getElementById("decryptKey").value;
                let file = document.getElementById("selectEncryptedVideo").files[0];

                if (!file) {
                    alert("未选择待解密文件");
                    return;
                }
                if (!decryptKey) {
                    alert("未指定解密密钥");
                    return;
                }
                let formData = new FormData();
                formData.set("file", file);
                formData.set("password", decryptKey);

                fetch('/Encrypt/aesVideoDec', {
                    method: 'POST',
                    body: formData
                })
                    .then(response => response.blob())
                    .then(data => {
                        const video = document.createElement("video");
                        //console.log(data)
                        video.src = URL.createObjectURL(data);
                        video.type = "video/mp4";
                        video.controls = true;
                        showDecryptedVideo.appendChild(video);

                    })
                    .catch(error => {
                        console.log("Error:", error);
                    })
            }
        }

        function faceSubSubmit() {
            document.getElementById("after").innerHTML = "";
            // debugger
            // 清空
            const imgSrcFile = document.getElementById("video_face_sub_src_fileupload").files[0];
            const videoTargetFile = document.getElementById("video_face_sub_target_fileupload").files[0];

            // document.getElementById("video_remove_bg_img").innerHTML = "";
            // 视频格式
            const videoType = ['mp4']
            const imageType = ['jpg', 'jpeg', 'png'];
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
                    //提交脱敏参数，请求脱敏
                    let after = document.getElementById("after");
                    after.innerHTML = "";
                    // 获取保护级别
                    let param = "1";

                    let formData = new FormData();
                    formData.set("file", videoTargetFile);
                    formData.set("params", param);
                    formData.set("algName", "video_face_sub");
                    formData.set("sheet", imgSrcFile);

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
                            video.style.maxWidth = "50%";
                            video.style.height = "auto";
                            video.controls = true;
                            after.appendChild(video);
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert(error)
                        });
                } else {
                    alert("请选择视频文件");
                }
            }
        }

        function removeBgSubmit() {
            // 清空
            const videoFile = document.getElementById("video_remove_bg_fileupload").files[0];
            const imageFile = document.getElementById("video_remove_bg_img_fileupload").files[0];
            // document.getElementById("video_remove_bg_img").innerHTML = "";
            // 视频格式
            const videoType = ['mp4', 'avi']
            const imageType = ['jpg', 'jpeg', 'png'];
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
                    //提交脱敏参数，请求脱敏
                    let after = document.getElementById("after");
                    after.innerHTML = "";
                    // 获取保护级别
                    let param = document.getElementById("privacy-levels").value;

                    let formData = new FormData();
                    formData.set("file", videoFile);
                    formData.set("params", param);
                    formData.set("algName", "video_remove_bg");
                    formData.set("sheet", imageFile);

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
        }

        function chooseFile(event) {
            // 清空
            document.getElementById("pre").innerHTML = "";
            document.getElementById("after").innerHTML = "";
            document.getElementById("mid-image").innerHTML = "";

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
                    displayVideo("fileUpload", "pre")

                } else {
                    alert("请选择视频文件");
                }
            }
        }

        function nonDistortionVideoShowFile(event, inputElement, showVideoArea) {
            // 清空

            document.getElementById(showVideoArea).innerHTML = "";

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
                    displayVideo(inputElement, showVideoArea)
                } else {
                    alert("请选择视频文件");
                }
            }
        }

        function displacePart(event) {
            let algoName = event.target.value;
            console.log("Selected algoName: " + algoName);
            let defaultBtns = document.getElementById("default-choosefile-btn");
            let videoBgBtns = document.getElementById("video-bg-sub-btns");
            let videoFaceSubBtns = document.getElementById("video-face-sub-btns");
            let privacyLevel = document.getElementById("privacyLevel");
            let submitBtn = $('#submit');

            switch (algoName) {
                case "video_remove_bg":
                    defaultBtns.style.display = "none";
                    videoBgBtns.style.display = "flex";
                    videoFaceSubBtns.style.display = "none"
                    privacyLevel.style.display = "block";
                    // submitBtn.removeEventListener("click", originalSubmit);
                    // submitBtn.removeEventListener("click", faceSubSubmit);
                    submitBtn.off('click').on("click", removeBgSubmit);

                    break;

                case "video_face_sub":
                    defaultBtns.style.display = "none";
                    videoBgBtns.style.display = "none";
                    videoFaceSubBtns.style.display = "flex"
                    privacyLevel.style.display = "none";
                    // submitBtn.removeEventListener("click", originalSubmit);
                    // submitBtn.removeEventListener("click", removeBgSubmit);
                    submitBtn.off('click').on("click", faceSubSubmit);
                    break;

                default:
                    defaultBtns.style.display = "flex";
                    videoBgBtns.style.display = "none";
                    videoFaceSubBtns.style.display = "none"
                    privacyLevel.style.display = "block";
                    // submitBtn.addEventListener("click", originalSubmit);
                    // submitBtn.removeEventListener("click", removeBgSubmit);
                    submitBtn.off('click').on("click", originalSubmit);
                    break;
            }

        }

        // 展示视频，从元素中获取输入的原视频文件，展示在指定区域
        function displayVideo(inputElement, showArea) {
            // let input = document.getElementById("fileUpload");
            // let pre = document.getElementById("pre");
            let input = document.getElementById(inputElement);
            let pre = document.getElementById(showArea);
            let file = input.files[0];
            let reader = new FileReader();
            reader.readAsDataURL(file);

            reader.onload = function (e) {
                let video = document.createElement("video");
                video.src = e.target.result;
                video.controls = true;
                pre.appendChild(video);
            };
        }

        // 用于使用图片参数的算法的提交
        function originalSubmit() {
            let after = document.getElementById("after");
            after.innerHTML = "";
            // 获取保护级别
            let privacyLevels = document.getElementById("privacy-levels")
            let param = privacyLevels.value;
            let file = document.getElementById("fileUpload").files[0];
            if (!file) {
                alert("未选择待脱敏文件");
                return;
            }
            let formData = new FormData();
            formData.set("file", file);
            formData.set("params", param);

            let idx = $("ul .active").index();
            if (idx === 0) {
                let distortionTypes = document.getElementById("distortion-types")
                let type1 = distortionTypes.value;
                formData.set("sheet", type1);
                formData.set("algName", type1);
            } else {
                let nondistortionTypes = document.getElementById("nondistortion-types")
                let type2 = nondistortionTypes.value;
                formData.set("sheet", type2);
                formData.set("algName", type1);
            }
            // 获取算法类型
            fetch('/File/desenFile', {
                method: 'POST',
                body: formData
            })
                .then(response => response.blob())
                .then(data => {
                    let video = document.createElement("video");
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

    </script>

</head>
<body>

<div class="row">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
            </div>

            <div class="tabs-container">
                <ul id="tab-type" class="nav nav-tabs" style="left: 50%;font-size: 20px;">
                    <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true"> 失真 </a>
                    </li>
                    <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false"> 非失真 </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div id="tab-1" class="tab-pane active" style="text-align: center;">
                        <div>
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th> 请选择视频失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody id="table1">
                                <tr>
                                    <td>
                                        <select id="distortion-types">
                                            <option value="meanValueVideo">泛化-基于均值滤波器的视频帧像素替换方法
                                            </option>
                                            <option value="pixelate_video">泛化-基于像素化滤波器的视频帧像素替换方法
                                            </option>
                                            <option value="gaussian_blur_video">泛化-基于高斯滤波器的视频帧像素替换方法
                                            </option>
                                            <option value="box_blur_video">泛化-基于盒式滤波器的视频帧像素替换方法
                                            </option>
                                            <option value="replace_region_video">泛化-基于像素块的视频帧像素替换方法
                                            </option>
                                            <option value="video_add_color_offset">替换-基于像素块的视频帧像素颜色偏移方法
                                            </option>
                                            <option value="video_remove_bg">替换-视频背景替换算法
                                            </option>
                                            <option value="video_face_sub">替换-视频人脸替换算法
                                            </option>
                                        </select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                        <div id="privacyLevel">
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th>请选择隐私保护等级</th>
                                </tr>
                                </thead>
                                <tbody id="table3">
                                <tr>
                                    <td><select id="privacy-levels">
                                            <option value="0"> 低程度</option>
                                            <option value="1" selected> 中程度</option>
                                            <option value="2"> 高程度</option>
                                        </select></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                        <div id="default-choosefile-btn" class="midtile">
                            <div class="col-sm-5 m-b-xs">
                                <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                    <input type="file" id="fileUpload" style="display: none;" accept=".mp4">
                                    <label for="fileUpload" class="upload-btn">
                                        选择文件
                                    </label>
                                </form>
                            </div>
                        </div>
                        <#--用于视频背景替换文件上传的按钮组 -->
                        <div id="video-bg-sub-btns" class="midtile" style="display: none">
                            <div class="align-items-center">
                                <form id="uploadForm" enctype="multipart/form-data">
                                    <input type="file" id="video_remove_bg_fileupload" style="display: none;"
                                           accept=".mp4">
                                    <label for="video_remove_bg_fileupload" class="upload-btn">
                                        选择视频文件
                                    </label>
                                    <input type="file" id="video_remove_bg_img_fileupload" style="display: none;"
                                           accept=".jpg, .jpeg, .png">
                                    <label for="video_remove_bg_img_fileupload" class="upload-btn">
                                        选择背景图片
                                    </label>
                                </form>
                            </div>
                        </div>
                        <#--            用于视频人脸替换算法文件上传的按钮组-->
                        <div id="video-face-sub-btns" class="midtile" style="display: none">
                            <div class="align-items-center">
                                <form id="uploadForm" enctype="multipart/form-data">
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
                        <div id=fileInfo>
                        </div>

                        <div class="btn2">
                            <button type="button" class="btn btn-sm btn-primary" id="submit"> 提交脱敏</button>
                        </div>
                        <!--文件上传信息-->

                        <div class="showFile">
                            <!--前后文件-->
                            <div id=pre>
                            </div>
                            <div id=mid-image>
                            </div>
                            <div id=after>
                            </div>
                        </div>

                    </div>
                    <div id="tab-2" class="tab-pane" style="text-align: center;">
                        <div>
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th>请选择视频非失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody id="table2">
                                <tr>
                                    <td>
                                        <select id="nodistortionAlg">

                                            <option value="videoAesEncrypt" selected> AES视频加密</option>
                                        </select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="default-module" style="visibility: visible"></div>

                        <div class="m-t" id="videoAesEncrypt">
                            <div class="ibox">
                                <div class="ibox-title">
                                    <h5>视频加密</h5>
                                </div>
                                <div class="ibox-content">

                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">请输入加密密钥</label>

                                        <div class="col-sm-10">
                                            <input type="text" class="form-control" id="encryptKey"/>
                                        </div>
                                    </div>

                                    <div class="row m-t">
                                        <p>

                                            <input type="file" accept=".mp4" id="selectRawVideo"
                                                   style="display: none;">
                                            <label for="selectRawVideo" class="btn-primary upload-btn">
                                                选择待加密视频文件
                                            </label>
                                            <button type="button" id="encryptVideo"
                                                    class="btn btn-sm btn-primary">加密视频
                                            </button>

                                        </p>

                                    </div>
                                    <div class="row m-t">
                                        <div id="aesRawVideoInfo">

                                        </div>
                                    </div>
                                    <div class="row m-t">
                                        <div id="showRawVideo">

                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="ibox ">
                                <div class="ibox-title">
                                    <h5>视频解密</h5>
                                </div>
                                <div class="ibox-content">

                                    <div class="form-group m-b">
                                        <label class="col-sm-2 control-label">请输入解密密钥</label>

                                        <div class="col-sm-10">
                                            <input type="text" class="form-control" id="decryptKey"/>
                                        </div>
                                    </div>
                                    <div class="row m-t">
                                        <p>

                                            <input type="file" accept=".mp4" id="selectEncryptedVideo"
                                                   style="display: none;">
                                            <label for="selectEncryptedVideo" class="btn-primary upload-btn">
                                                选择待解密视频文件
                                            </label>
                                            <button type="button" id="decryptVideo"
                                                    class="btn btn-sm btn-primary">解密视频
                                            </button>

                                        </p>
                                    </div>

                                    <div class="row m-t">
                                        <div id="aesDecryptedVideoInfo">

                                        </div>
                                    </div>

                                    <div class="row m-t">
                                        <div id="showDecryptedVideo">

                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

</div>
</body>

</html>
