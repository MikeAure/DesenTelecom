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
            document.getElementById("audio_floor_fileupload").addEventListener("change", function(event) {
                chooseFile(event, "audio_floor", true)
            });

            document.getElementById("audio_spec_fileupload").addEventListener("change", function(event) {
                chooseFile(event, "audio_spec", true)
            });

            document.getElementById("audio_augmentation_fileUpload").addEventListener("change", function(event) {
                chooseFile(event, "audio_augmentation", false)
            });

            document.getElementById("audio_median_fileUpload").addEventListener("change", function(event) {
                chooseFile(event, "audio_median", true)
            });


        }


        let chooseFile = function (event, algName, hasPrivacyLevel) {
            // 清空
            document.getElementById(algName + "_pre").innerHTML = "";
            document.getElementById(algName + "_after").innerHTML = "";
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
                    let pre = document.getElementById(algName + "_pre");
                    let audio = document.createElement("audio");
                    // let blob = new Blob([e.target.result], {type: 'audio/mp3'});
                    audio.src = URL.createObjectURL(file);
                    audio.controls = true;
                    pre.appendChild(audio);
                    // let reader = new FileReader();
                    // reader.onload = function (e) {
                    //
                    // };
                    // reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById(algName + "_submit").onclick = function () {
                        let after = document.getElementById(algName + "_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        let params;
                        if (hasPrivacyLevel) {
                            params = document.getElementById(algName + "_privacyLevel").value;
                        } else {
                            params = "1"
                        }

                        let formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", params);
                        formData.append("algName", algName);
                        formData.append("sheet", "sheet");

                        fetch('/File/desenFile', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => {
                                if (response.status === 200) {
                                    return response.blob()
                                } else {
                                    throw new Error("Python script executes failed")
                                }
                            })
                            .then(blob => {
                                // 创建音频标签
                                const audioElement = document.createElement('audio');
                                audioElement.controls = true;
                                // 使用Blob URL设置音频数据
                                audioElement.src = URL.createObjectURL(blob);
                                // 添加到页面中
                                after.appendChild(audioElement);
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert(error);
                            });

                    }
                } else {
                    alert("请选择音频文件");
                }
            }
        }

    </script>
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
        .upload-btn, .submit-btn, #audio_reshuffle_submit {
            background-color: #347aa9;
            color: white;
            cursor: pointer;
            padding: 5px 20px;
            text-align: center;
            font-size: 20px;
            display: inline-block;
            margin: 30px;
        }

        #audio_floor_pre, #audio_floor_after, #audio_spec_pre,
        #audio_spec_after, #audio_augmentation_pre, #audio_augmentation_after,
        #audio_median_pre, #audio_median_after {
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


<div class="panel panel-default">

    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                19. 音频取整</p>
            <div style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对采样点数据进行取整操作
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：原音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：脱敏后的音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="audio_floor_fileupload" style="display: none;">
                                <label for="audio_floor_fileupload" class="upload-btn">
                                    选择音频源文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            <label for="audio_floor_privacyLevel">请选择隐私保护程度：</label>
                            <select id="audio_floor_privacyLevel">
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
            <div id="audio_floor_pre" style="margin-right: 20px;">
            </div>
            <div id="audio_floor_after">
            </div>

        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="audio_floor_submit"> 提交脱敏</button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                20.频域遮掩</p>
            <div style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：删除特定频域段音频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：原音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：脱敏后音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="audio_spec_fileupload" style="display: none;">
                                <label for="audio_spec_fileupload" class="upload-btn">
                                    选择音频源文件
                                </label>
                            </form>
                        </div>
                    </div>

                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            <label for="audio_spec_privacyLevel">请选择隐私保护程度：</label>
                            <select id="audio_spec_privacyLevel">
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
            <div id="audio_spec_pre" style="margin-right: 20px;">
            </div>
            <div id="audio_spec_after">
            </div>

        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="audio_spec_submit"> 提交脱敏
            </button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                21.音频失真</p>
            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：使用滤波器等对音频进行失真处理，操作包括低通滤波器、高通滤波器、归一化、双曲正切失真等。
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：原音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：脱敏后音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="col-sm-5 align-items-center">
                            <form id="uploadForm" method="post" enctype="multipart/form-data">
                                <input type="file" id="audio_augmentation_fileUpload" style="display: none;">
                                <label for="audio_augmentation_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id=audio_augmentation_pre style="margin-right: 20px;">
            </div>
            <div id=audio_augmentation_after>
            </div>
        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="audio_augmentation_submit">提交脱敏
            </button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                22.基于均值的采样点替换</p>
            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对采样点进行分块，块内采样点的均值作为新的采样点。
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：原音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：脱敏后音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="col-sm-5 align-items-center">
                            <form id="uploadForm" method="post" enctype="multipart/form-data">
                                <input type="file" id="audio_median_fileUpload" style="display: none;">
                                <label for="audio_median_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div style="text-align: center;">
                        <div style=" font-size: 20px">
                            <label for="audio_median_privacyLevel">请选择隐私保护等级:</label>
                            <#--                                    <input type="text" id="audio_reshuffle_block_num">-->
                            <select id="audio_median_privacyLevel">
                                <option value="0">低程度</option>
                                <option value="1" selected>中程度</option>
                                <option value="2">高程度</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id=audio_median_pre style="margin-right: 20px;">
            </div>
            <div id=audio_median_after>
            </div>
        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="audio_median_submit">提交脱敏
            </button>
        </div>

    </div>
</div>


</body>


</html>
