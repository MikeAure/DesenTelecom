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

        #after {
            text-align: center;
        }

        #after img {
            display: inline-block;
            max-width: 50%;
            height: auto;
        }

        .showFile {
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0;
        }

        .tabs-container ul {
            height: 50px;
            display: flex;
            flex-direction: row;
            justify-content: center;
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
            let preArea = document.getElementById("pre");
            let afterArea = document.getElementById("after");
            let replaceArea = document.getElementById("replace-result");
            let submitBtn = document.getElementById("submit");
            submitBtn.addEventListener("click", submitOriginMethod);
            const fileUploadBtn = document.getElementById("fileUpload");
            fileUploadBtn.addEventListener("change", chooseFile);
            let privacy_level = document.getElementById("privacyLevel");
            let uploadForm = document.getElementById("uploadForm");
            let facesubUpload = document.getElementById("facesub-upload");

            document.getElementById("img_face_sub_src_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("pre").innerHTML = "";
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
                        let input = document.getElementById("img_face_sub_src_fileupload");
                        let pre = document.getElementById("pre");
                        let file = input.files[0];
                        let reader = new FileReader();
                        reader.readAsDataURL(file);
                        reader.onload = function (e) {
                            let img = new Image();
                            img.src = e.target.result;
                            img.style.maxWidth = '80%';
                            img.style.height = 'auto';
                            pre.appendChild(img);
                        };
                    }
                }
            })
            document.getElementById("img_face_sub_target_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("after").innerHTML = "";
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
                        let input = document.getElementById("img_face_sub_target_fileupload");
                        let file = input.files[0];
                        let after = document.getElementById("after");
                        let reader = new FileReader();
                        reader.readAsDataURL(file);
                        reader.onload = function (e) {
                            let img = new Image();
                            img.src = e.target.result;
                            img.style.maxWidth = '80%';
                            img.style.height = 'auto';
                            after.appendChild(img);
                        };
                    }
                }
            })
            document.getElementById("algonames").addEventListener("change", function (event) {

                if ($("ul .active").index() === 0) {
                    switch (event.target.value) {
                        case "img_face_sub":
                            privacy_level.style.display = "none";
                            facesubUpload.style.display = "block";
                            uploadForm.style.display = "none";

                            submitBtn.removeEventListener("click", submitOriginMethod);
                            submitBtn.addEventListener("click", img_face_sub_upload);
                            break;
                        case "image_exchange_channel":
                            privacy_level.style.display = "none";
                            facesubUpload.style.display = "none";
                            uploadForm.style.display = "block";
                            submitBtn.addEventListener("click", submitOriginMethod);
                            submitBtn.removeEventListener("click", img_face_sub_upload);
                            break;
                        default:
                            privacy_level.style.display = "block";
                            facesubUpload.style.display = "none";
                            uploadForm.style.display = "block";
                            submitBtn.addEventListener("click", submitOriginMethod);
                            submitBtn.removeEventListener("click", img_face_sub_upload);
                            break;
                    }
                } else {
                    privacy_level.style.display = "none";
                    facesubUpload.style.display = "none";
                    uploadForm.style.display = "block";

                    document.getElementById("algonames").value = "dpImage";
                    submitBtn.addEventListener("click", submitOriginMethod);
                    submitBtn.removeEventListener("click", img_face_sub_upload);
                }
            })
            let tabs = document.querySelectorAll('a[data-toggle="tab"]');

            // tabs.forEach(function(tab) {
            //     tab.addEventListener('shown.bs.tab', function(event) {
            //         let activeTab = event.target; // 当前被激活的Tab
            //         let activePaneId = activeTab.getAttribute('href'); // 激活的Tab对应的面板ID，如#tab-1, #tab-2
            //
            //         if (activePaneId === '#tab-2') {
            //             console.log('Tab 2 is now active!');
            //             // 在这里编写你想在切换到 Tab 2 时执行的代码
            //         }
            //     });
            // });
            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                preArea.innerHTML = "";
                afterArea.innerHTML = "";
                replaceArea.innerHTML = "";
                privacy_level.style.display = "none";
                facesubUpload.style.display = "none";
                uploadForm.style.display = "block";
                $("#algonames").val("dpImage");
            })
        }

        function submitOriginMethod(event) {
            let after = document.getElementById("after");
            after.innerHTML = "";
            let replaceResult = document.getElementById("replace-result");
            replaceResult.innerHTML = "";
            let formData = new FormData();

            let fileInput = document.getElementById("fileUpload");
            if (fileInput.files.length === 0) {
                alert("未选择文件");
                return;
            } else {
                let file = fileInput.files[0];
                formData.set("file", file);
            }

            /*formData.append("sheet", "media");*/

            let idx = $("ul .active").index();
            if (idx === 0) {
                // 获取保护级别
                let level_selections;
                let param;
                let algonames = document.getElementById("algonames")
                let type1 = algonames.value;
                if (type1 !== "img_face_sub") {
                    if (type1 === "image_exchange_channel") {
                        param = 1;
                    } else {
                        level_selections = document.getElementById("level-selection")
                        param = level_selections.value;
                    }
                    formData.set("params", param);
                    formData.set("sheet", type1);
                    formData.set("algName", type1);
                }
            } else {
                formData.set("params", 0);
                let tableBody = document.getElementById("non-distortion-algos")
                let type2 = tableBody.value;
                formData.set("sheet", type2);
                formData.set("algName", type2);
            }
            // 打印测试
            formData.forEach((value, key) => {
                console.log(key, value);
            });
            fetch('/File/desenFile', {
                method: 'POST',
                body: formData
            })
                .then(response => response.blob())
                .then(blob => {
                    let dealedImg = new Image();
                    dealedImg.src = URL.createObjectURL(blob);
                    after.appendChild(dealedImg);
                    console.log("return")
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert(error)
                });

        }

        function chooseFile(event) {
            // 清空
            document.getElementById("pre").innerHTML = "";
            document.getElementById("after").innerHTML = "";

            // 图像格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];

            //读取文件
            const file = event.target.files[0]
            if (file) {
                const fileName = file.name;
                // 文件名，扩展名
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if (imageType.includes(fileExtension)) {
                    displayImage()
                    //提交脱敏参数，请求脱敏
                } else {
                    alert("请选择图像文件");
                }
            }
        }

        let displayImage = function () {
            let input = document.getElementById("fileUpload");
            let pre = document.getElementById("pre");
            let file = input.files[0];
            let reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = function (e) {
                let img = new Image();
                img.src = e.target.result;
                pre.appendChild(img);
            };
        }

        function img_face_sub_upload() {
            document.getElementById("replace-result").innerHTML = "";
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
                    let after = document.getElementById("replace-result");
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
        }
    </script>

</head>
<body>

<div class="row">
    <div class="col-sm-12">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
            </div>
            <div class="midtile">
                <div class="col-sm-5 m-b-xs">
                    <div id="uploadForm">
                        <input type="file" id="fileUpload" style="display: none;">
                        <label for="fileUpload" class="upload-btn">
                            选择文件
                        </label>
                    </div>
                    <div id="facesub-upload" style="display: none">
                        <input type="file" id="img_face_sub_src_fileupload" accept=".jpg, .jpeg, .png, .gif"
                               style="display: none;">
                        <label for="img_face_sub_src_fileupload" class="upload-btn">
                            选择目标面孔
                        </label>
                        <input type="file" id="img_face_sub_target_fileupload" accept=".jpg, .jpeg, .png, .gif"
                               style="display: none;">
                        <label for="img_face_sub_target_fileupload" class="upload-btn">
                            选择源文件
                        </label>
                    </div>
                </div>
            </div>

            <div class="showFile">
                <!--前后文件-->
                <div id="pre">
                </div>
                <div id="after">
                </div>
                <div id="replace-result">
                </div>
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
                                    <th>请选择图像失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody id="table1">
                                <tr>
                                    <td><select id="algonames">

                                            <option value="dpImage" selected> DP-基于差分隐私的图像加噪方法</option>
                                            <option value="im_coder2" selected> DP-基于差分隐私的图像加噪方法2</option>
                                            <option value="meanValueImage"> 泛化-基于均值滤波器的图像加噪方法</option>
                                            <option value="pixelate"> 泛化-基于像素化滤波器的图像加噪方法</option>
                                            <option value="gaussian_blur"> 泛化-基于高斯滤波器的图像加噪方法</option>
                                            <option value="box_blur"> 泛化-基于盒式滤波器的图像加噪方法</option>
                                            <option value="replace_region"> 泛化-基于像素块的图像区域替换方法</option>
                                            <option value="image_exchange_channel"> 替换-图像颜色随机替换方法</option>
                                            <option value="image_add_color_offset"> 替换-图像颜色偏移</option>
                                            <option value="img_face_sub"> 替换-图像人脸替换算法
                                            </option>

                                        </select></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="ibox-content" style="text-align: center;">
                            <div id="privacyLevel">
                                <table style="margin: auto; font-size: 20px">
                                    <thead>
                                    <tr>
                                        <th>请选择隐私保护等级</th>
                                    </tr>
                                    </thead>
                                    <tbody id="table3">
                                    <tr>
                                        <td><select id="level-selection">
                                                <option value="0"> 低程度</option>
                                                <option value="1" selected> 中程度</option>
                                                <option value="2"> 高程度</option>
                                            </select></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div id="tab-2" class="tab-pane" style="text-align: center;">
                        <div>
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th>请选择图像非失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody id="table2">
                                <tr>
                                    <td><select id="non-distortion-algos">
                                            <option value="retrieval"> 相似医疗图像安全检索</option>
                                        </select></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>


            <div class="btn2">
                <button type="button" class="btn btn-sm btn-primary" id="submit"> 提交脱敏</button>
            </div>

        </div>
    </div>

</div>
</body>

</html>
