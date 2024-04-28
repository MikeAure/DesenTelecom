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
        // 图片通道随机打乱
        document.getElementById("image_exchange_channel_fileupload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("image_exchange_channel_pre").innerHTML = "";
            document.getElementById("image_exchange_channel_after").innerHTML = "";
            // 视频格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (imageType.includes(fileExtension)) {
                    let pre = document.getElementById("image_exchange_channel_pre");
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        let img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("image_exchange_channel_submit").onclick = function () {
                        let after = document.getElementById("image_exchange_channel_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        let param = "1";
                        let formData = new FormData();
                        formData.set("file", file);
                        formData.set("params", param);
                        formData.set("algName", "image_exchange_channel");
                        formData.set("sheet", "image_exchange_channel");

                        fetch('/File/desenFile', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                let dealtImg = new Image();
                                dealtImg.src = URL.createObjectURL(blob);
                                dealtImg.style.maxWidth = '80%';
                                dealtImg.style.height = 'auto';
                                after.appendChild(dealtImg);
                            })
                            .catch(error => console.error('Error:', error));

                    }
                } else {
                    alert("请选择图像文件");
                }
            }
        })
        // 图片逐通道添加颜色偏移
        document.getElementById("image_add_color_offset_fileupload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("image_add_color_offset_pre").innerHTML = "";
            document.getElementById("image_add_color_offset_after").innerHTML = "";
            // 视频格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (imageType.includes(fileExtension)) {
                    let pre = document.getElementById("image_add_color_offset_pre");
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        let img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("image_add_color_offset_submit").onclick = function () {
                        let after = document.getElementById("image_add_color_offset_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        let param = document.getElementById("image_add_color_offset_privacyLevel").value;

                        let formData = new FormData();
                        formData.set("file", file);
                        formData.set("params", param);
                        formData.set("algName", "image_add_color_offset");
                        formData.set("sheet", "image_add_color_offset");

                        fetch('/File/desenFile', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.blob())
                            .then(blob => {
                                let dealedImg = new Image();
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
        // 图片人脸替换
        document.getElementById("img_face_sub_src_fileupload").addEventListener("change", function (event) {
            // 清空
            document.getElementById("img_face_sub_pre").innerHTML = "";
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
                    let pre = document.getElementById("img_face_sub_pre");
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        let img = new Image();
                        img.src = e.target.result;
                        img.style.maxWidth = '80%';
                        img.style.height = 'auto';
                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(imageFile);
                }
            }
        })
        document.getElementById("img_face_sub_target_fileupload").addEventListener("change", function (event) {

            // 清空
            document.getElementById("img_face_sub_target_img").innerHTML = "";
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
                    let pre = document.getElementById("img_face_sub_target_img");
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        let img = new Image();
                        img.src = e.target.result;
                        img.style.maxWidth = '80%';
                        img.style.height = 'auto';
                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(imageFile);
                }
            }
        })
        document.getElementById("img_face_sub_submit").addEventListener("click", function (event) {
            document.getElementById("img_face_sub_after").innerHTML = "";
            // debugger
            // 清空
            // 目标面孔
            const imgSrcFile = document.getElementById("img_face_sub_src_fileupload").files[0];
            // 被换脸的文件
            const imgTargetFile = document.getElementById("img_face_sub_target_fileupload").files[0];

            // document.getElementById("video_remove_bg_img").innerHTML = "";
            // 图片格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];
            //读取文件
            // const videoFile = event.target.files[0]
            // const imageFile =
            // 文件名，扩展名
            if (imgSrcFile && imgTargetFile) {
                const imgSrcFileName = imgSrcFile.name;
                const imgSrcFileNameExtension = imgSrcFileName.split('.').pop().toLowerCase();
                const imgTargetFileName = imgTargetFile.name;
                const imgTargetFileNameExtension = imgTargetFileName.split('.').pop().toLowerCase();

                console.log(imgSrcFileNameExtension);
                console.log(imgTargetFileNameExtension);

                if (imageType.includes(imgSrcFileNameExtension) && imageType.includes(imgTargetFileNameExtension)) {
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
                    let after = document.getElementById("img_face_sub_after");
                    after.innerHTML = "";
                    // 获取保护级别
                    let param = "1";

                    let formData = new FormData();
                    formData.set("file", imgTargetFile);
                    formData.set("params", param);
                    formData.set("algName", "image_face_sub");
                    formData.set("sheet", imgSrcFile);

                    fetch('/File/replaceFace', {
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
                            let dealtImg = new Image();
                            dealtImg.src = URL.createObjectURL(blob);
                            dealtImg.style.maxWidth = '80%';
                            dealtImg.style.height = 'auto';
                            after.appendChild(dealtImg);
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert(error)
                        });
                } else {
                    alert("请选择图片文件");
                }
            }
        })
    }

</script>
<div class="ibox-title">
</div>

<div class="panel panel-default">
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                1.图像颜色随机替换方法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将图像的每个像素的RGB通道值随机打乱后作为当前像素新的RGB值并生成新的图片
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：图像
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：图像
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="<#--col-sm-5 --> align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="image_exchange_channel_fileupload" style="display: none;">
                                <label for="image_exchange_channel_fileupload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <#--                    <div &lt;#&ndash;class="ibox-content"&ndash;&gt; style="text-align: center;">-->
                    <#--                        <div style="margin: auto; font-size: 20px">-->
                    <#--                            请选择隐私保护等级-->
                    <#--                            <select id="replace_region_privacyLevel">-->
                    <#--                                <option value="0"> 低程度</option>-->
                    <#--                                <option value="1" selected> 中程度</option>-->
                    <#--                                <option value="2"> 高程度</option>-->
                    <#--                            </select>-->
                    <#--                        </div>-->
                    <#--                    </div>-->
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id="image_exchange_channel_pre" style="margin-right: 20px;">
            </div>
            <div id="image_exchange_channel_after">
            </div>
        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="image_exchange_channel_submit">
                提交脱敏
            </button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                2.图像颜色偏移</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将图像的每个像素的RGB通道值加上一个偏移量后作为当前像素新的RGB值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：图像
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：图像
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="<#--col-sm-5 --> align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="image_add_color_offset_fileupload" style="display: none;">
                                <label for="image_add_color_offset_fileupload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="image_add_color_offset_privacyLevel">
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
            <div id="image_add_color_offset_pre" style="margin-right: 20px;">
            </div>
            <div id="image_add_color_offset_after">
            </div>
        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="image_add_color_offset_submit">
                提交脱敏
            </button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                3.图像人脸替换算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将图片中的人脸进行替换
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：将要替换成的人脸，需要替换人脸的照片
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：替换人脸后的照片
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="img_face_sub_src_fileupload" style="display: none;">
                                <label for="img_face_sub_src_fileupload" class="upload-btn">
                                    选择目标面孔
                                </label>
                                <input type="file" id="img_face_sub_target_fileupload" style="display: none;">
                                <label for="img_face_sub_target_fileupload" class="upload-btn">
                                    选择源文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <#--                    <div &lt;#&ndash;class="ibox-content"&ndash;&gt; style="text-align: center;">-->
                    <#--                        <div style="margin: auto; font-size: 20px">-->
                    <#--                            请选择人物轮廓边缘距离（程度越高越贴近人物轮廓）：-->
                    <#--                            <select id="video_remove_bg_privacyLevel">-->
                    <#--                                <option value="0"> 低程度</option>-->
                    <#--                                <option value="1" selected> 中程度</option>-->
                    <#--                                <option value="2"> 高程度</option>-->
                    <#--                            </select>-->
                    <#--                        </div>-->
                    <#--                    </div>-->
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id="img_face_sub_pre" style="margin-right: 20px;">
            </div>
            <div id="img_face_sub_target_img" style="margin-right: 20px;">
            </div>
            <div id="img_face_sub_after">
            </div>
        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="img_face_sub_submit"> 提交脱敏</button>
        </div>
    </div>
    <hr>
</div>
</body>


</html>
