<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Insert title here</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet"/>
    <link href="${ctx!}/css/font-awesome.css?v=4.4.0" rel="stylesheet"/>
    <link href="${ctx!}/css/plugins/iCheck/custom.css" rel="stylesheet"/>
    <link href="${ctx!}/css/animate.css" rel="stylesheet"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="${ctx!}/css/multiple-select.min.css"
    />
    <link rel="stylesheet" type="text/css" href="${ctx!}/css/GA.css"/>
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet"/>
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

        .btn2 > button{
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

        #replace-result img,
        #after img,
        #pre img {
            display: inline-block;
            max-width: 50%;
            height: auto;
        }

        #after {
            text-align: center;
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
        let defaultOption = 'meanValueImage'; // 预设的默认选项
        document.addEventListener('DOMContentLoaded', function() {
            let privacy_level = document.getElementById("privacyLevel");
            let uploadForm = document.getElementById("uploadForm");
            let facesubUploadControlGroup = document.getElementById("facesub-upload");
            let submitBtn = $("#submit");
            // 失真算法选择控件
            let selectedAlgoName = document.getElementById('algonames');
            fetch('/toolset/getDefaultSelection?toolsetName=' + 'image')
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Failed to fetch');
                    }
                })
                .then(data => {
                    if (data.code === 200 && data.message) {
                        setDefaultOption(data.data);
                        defaultOption = data.data;

                        if ($("ul .active").index() === 0) {
                            switch (defaultOption) {
                                case "image_face_sub":
                                    console.log("image_face_sub");
                                    privacy_level.style.display = "none";
                                    facesubUploadControlGroup.style.display = "block";
                                    uploadForm.style.display = "none";
                                    // submitBtn.removeEventListener("click", submitOriginMethod);
                                    submitBtn.off("click").on("click", () => {
                                        imageFaceSubUpload();
                                    });
                                    break;
                                case "image_exchange_channel":
                                    privacy_level.style.display = "none";
                                    facesubUploadControlGroup.style.display = "none";
                                    uploadForm.style.display = "block";
                                    submitBtn.off("click").on("click", () => {
                                        submitOriginMethod();
                                    });
                                    break;
                                default:
                                    privacy_level.style.display = "block";
                                    facesubUploadControlGroup.style.display = "none";
                                    uploadForm.style.display = "block";
                                    submitBtn.off("click").on("click", () => {
                                        submitOriginMethod();
                                    });
                                    break;
                            }
                        } else {
                            // privacy_level.style.display = "none";
                            facesubUploadControlGroup.style.display = "none";
                            uploadForm.style.display = "block";
                            document.getElementById("algonames").value = defaultOption;
                            submitBtn.off("click").on("click", () => {
                                submitOriginMethod();
                            });
                        }
                    } else {
                        setDefaultOption(defaultOption);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert("获取默认算法失败");
                    setDefaultOption(defaultOption);
                });

            function setDefaultOption(value) {
                selectedAlgoName.value = value;
            }
        });

        window.onload = function () {
            let preArea = document.getElementById("pre");
            let afterArea = document.getElementById("after");
            let replaceResultArea = document.getElementById("replace-result");
            let selectedAlgoName = document.getElementById('algonames');

            let submitBtn = $("#submit");
            submitBtn.off("click").on("click", () => {
                submitOriginMethod();
            });
            // 非换脸算法的文件上传入口
            const originalFileUpload = document.getElementById("fileUpload");
            originalFileUpload.addEventListener("change", chooseFile);
            // 原始文件
            const faceSrc = document.getElementById("image_face_sub_src_fileupload");
            // 换脸目标
            const faceTarget = document.getElementById("image_face_sub_target_fileupload");
            let privacy_level = document.getElementById("privacyLevel");
            let uploadForm = document.getElementById("uploadForm");
            let facesubUploadControlGroup = document.getElementById("facesub-upload");

            $("#downloadEigenvector").hide();

            $("#image_face_sub_src_fileupload")
                .on("change", function (event) {
                    // 清空
                    document.getElementById("pre").innerHTML = "";
                    // document.getElementById("video_remove_bg_img").innerHTML = "";
                    const imageType = ["jpg", "jpeg", "png"];
                    //读取文件
                    const imageFile = event.target.files[0];
                    // 文件名，扩展名
                    if (imageFile) {
                        const fileName = imageFile.name;
                        const fileExtension = fileName.split(".").pop().toLowerCase();
                        console.log(fileExtension);

                        if (imageType.includes(fileExtension)) {
                            let input = document.getElementById(
                                "image_face_sub_src_fileupload"
                            );
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
                    }
                });

            $("#image_face_sub_target_fileupload")
                .on("change", function (event) {
                    // 清空
                    afterArea.innerHTML = "";
                    // 图片格式
                    const imageType = ["jpg", "jpeg", "png"];
                    //读取文件
                    const imageFile = event.target.files[0];
                    // 文件名，扩展名
                    if (imageFile) {
                        const fileName = imageFile.name;
                        const fileExtension = fileName.split(".").pop().toLowerCase();
                        console.log(fileExtension);

                        if (imageType.includes(fileExtension)) {
                            let input = document.getElementById(
                                "image_face_sub_target_fileupload"
                            );
                            let file = input.files[0];
                            let after = document.getElementById("after");
                            let reader = new FileReader();
                            reader.readAsDataURL(file);
                            reader.onload = function (e) {
                                let img = new Image();
                                img.src = e.target.result;
                                after.appendChild(img);
                            };
                        }
                    }
                });

            $("#algonames")
                .on("change", function (event) {
                    console.log("algoname-change event triggered");

                    faceSrc.value = "";
                    faceTarget.value = "";
                    originalFileUpload.value = "";

                    preArea.innerHTML = "";
                    afterArea.innerHTML = "";
                    replaceResultArea.innerHTML = "";

                    if ($("ul .active").index() === 0) {
                        switch (event.target.value) {
                            case "image_face_sub":
                                console.log("image_face_sub");
                                privacy_level.style.display = "none";
                                facesubUploadControlGroup.style.display = "block";
                                uploadForm.style.display = "none";
                                // submitBtn.removeEventListener("click", submitOriginMethod);
                                submitBtn.off("click").on("click", () => {
                                    imageFaceSubUpload();
                                });
                                break;
                            case "image_exchange_channel":
                                privacy_level.style.display = "none";
                                facesubUploadControlGroup.style.display = "none";
                                uploadForm.style.display = "block";
                                submitBtn.off("click").on("click", () => {
                                    submitOriginMethod();
                                });
                                // submitBtn.removeEventListener("click", image_face_sub_upload);
                                break;
                            default:
                                privacy_level.style.display = "block";
                                facesubUploadControlGroup.style.display = "none";
                                uploadForm.style.display = "block";
                                submitBtn.off("click").on("click", () => {
                                    submitOriginMethod();
                                });
                                // submitBtn.removeEventListener("click", image_face_sub_upload);
                                break;
                        }
                    } else {
                        // privacy_level.style.display = "none";
                        facesubUploadControlGroup.style.display = "none";
                        uploadForm.style.display = "block";

                        selectedAlgoName.value = defaultOption;
                        submitBtn.off("click").on("click", () => {
                            submitOriginMethod();
                        });
                        // submitBtn.removeEventListener("click", image_face_sub_upload);
                    }
                });

            $('a[data-toggle="tab"]').on("shown.bs.tab", function (e) {
                console.log("shown.bs.tab event triggered");

                let isSelected = $("#tab-2").hasClass("active");
                // 清空显示区域的图片
                preArea.innerHTML = "";
                afterArea.innerHTML = "";
                replaceResultArea.innerHTML = "";

                // 清空失真脱敏算法中所有文件上传控件中的文件
                originalFileUpload.value = "";
                faceSrc.value = "";
                faceTarget.value = "";

                console.log("defaultOption: " + defaultOption);

                if (defaultOption !== "image_exchange_channel" && defaultOption !== "image_face_sub") {
                    privacy_level.style.display = "block";
                } else {
                    privacy_level.style.display = "none";
                }

                if (defaultOption === "image_face_sub") {
                    facesubUploadControlGroup.style.display = "block";
                    uploadForm.style.display = "none";
                    submitBtn.off("click").on("click", () => {
                        imageFaceSubUpload();
                    });

                } else {
                    facesubUploadControlGroup.style.display = "none";
                    uploadForm.style.display = "block";

                }
                $("#downloadEigenvector").hide();
                $("#setDefaultAlgorithm").show();

                // 设置非失真脱敏算法选项卡显示默认值
                selectedAlgoName.value = defaultOption;
                if (isSelected) {
                    // 设置展示的控件组
                    facesubUploadControlGroup.style.display = "none";
                    uploadForm.style.display = "block";
                    $("#downloadEigenvector").show();
                    $("#setDefaultAlgorithm").hide();
                    console.log("Tab 2 is now active!");
                    submitBtn.off("click").on("click", () => {
                        submitOriginMethod();
                    });
                    // 在这里编写你想在切换到 Tab 2 时执行的代码
                }
            });

            $("#downloadEigenvector").on("click", function (e) {
                fetch("/imageRetrieval/downloadEigenVector")
                    .then((response) => {
                        if (response.status === 200) {
                            return response.blob();
                        } else {
                            throw new Error("Receive file failed");
                        }
                    })
                    .then((blob) => {
                        let timeStamp = Date.now();
                        const url = URL.createObjectURL(blob);
                        const a = document.createElement('a');
                        a.style.display = 'none';
                        a.href = url;
                        a.download = timeStamp + 'result.txt';
                        document.body.appendChild(a);
                        a.click();
                        setTimeout(() => {
                            document.body.removeChild(a);
                            window.URL.revokeObjectURL(url);
                        }, 0);
                    })
                    .catch((error) => {
                        console.error("Error:", error);
                        alert(error);
                    });
            });

            // 设置默认算法
            $("#setDefaultAlgorithm").on("click", function (e) {
               let postData = new URLSearchParams();
               postData.set("toolsetName", "image");
               postData.set("defaultAlgName", $("#algonames").val());
               console.log("postData: " + postData);

               fetch ("/toolset/setDefaultToolset", {
                   method: "POST",
                   body: postData,
               })
                   .then(response => response.json())
                   .then(data => {
                       if (data.code === 200) {
                           alert("设置默认算法成功！");
                           defaultOption = $("#algonames").val();
                       } else {
                           throw new Error("设置默认算法失败！");
                       }
                   })
                   .catch((error) => {
                       alert(error);
                       console.log(error);
                   })
            });
            // $("#downloadEigenvector").on("click", function (e) {
            //         fetch("/imageRetrieval/downloadEigenVector")
            //             .then((response) => {
            //                 if (response.status === 200) {
            //                     return response.json();
            //                 } else {
            //                     throw new Error("Receive file failed");
            //                 }
            //             })
            //             .then((data) => {
            //                 let imageFilename = data.imageFileName;
            //                 let eigenVectorFileName = data.eigenVectorFileName;
            //
            //                 fetch("/imageRetrieval/downloadFile") {
            //
            //                 }
            //                 let timeStamp = Date.now();
            //                 const url = URL.createObjectURL(data);
            //                 const a = document.createElement('a');
            //                 a.style.display = 'none';
            //                 a.href = url;
            //                 a.download = timeStamp + 'result.txt';
            //                 document.body.appendChild(a);
            //                 a.click();
            //                 setTimeout(() => {
            //                     document.body.removeChild(a);
            //                     window.URL.revokeObjectURL(url);
            //                 }, 0);
            //             })
            //             .catch((error) => {
            //                 console.error("Error:", error);
            //                 alert(error);
            //             });
            //     })
        };

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
            let url = "";
            if (idx === 0) {
                // 获取保护级别
                url = "/File/desenFile";
                let level_selections;
                let param;
                let algonames = document.getElementById("algonames");
                let type1 = algonames.value;
                if (type1 !== "img_face_sub") {
                    if (type1 === "image_exchange_channel") {
                        param = 1;
                    } else {
                        level_selections = document.getElementById("level-selection");
                        param = level_selections.value;
                    }
                    formData.set("params", param);
                    formData.set("sheet", type1);
                    formData.set("algName", type1);
                }
            } else {
                url = "/imageRetrieval/getImage";
                formData.set("params", 0);
                let tableBody = document.getElementById("non-distortion-algos");
                let type2 = tableBody.value;
                formData.set("sheet", type2);
                formData.set("algName", type2);
            }
            // 打印测试
            formData.forEach((value, key) => {
                console.log(key, value);
            });
            fetch(url, {
                method: "POST",
                body: formData,
            })
                .then((response) => {
                    if (response.status === 500) {
                        // Handle server error
                        return response.text().then(failedMsg => {
                            alert(failedMsg);
                            throw new Error(failedMsg); // Throw an error to stop further processing
                        });
                    }
                    return response.blob();
                })
                .then((blob) => {
                    let dealedImg = new Image();
                    dealedImg.src = URL.createObjectURL(blob);
                    after.appendChild(dealedImg);
                    console.log("Image Retrieval");
                })
                .catch((error) => {
                    console.error("Error:", error);
                    alert(error);
                });
        }

        function chooseFile(event) {
            // 清空
            document.getElementById("pre").innerHTML = "";
            document.getElementById("after").innerHTML = "";

            // 图像格式
            const imageType = ["jpg", "jpeg", "png"];

            //读取文件
            const file = event.target.files[0];
            if (file) {
                const fileName = file.name;
                // 文件名，扩展名
                const fileExtension = fileName.split(".").pop().toLowerCase();
                console.log(fileExtension);

                if (imageType.includes(fileExtension)) {
                    displayImage();
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
        };

        function imageFaceSubUpload() {
            document.getElementById("replace-result").innerHTML = "";
            // debugger
            // 清空
            // 目标面孔
            const imgSrcFile = document.getElementById(
                "image_face_sub_src_fileupload"
            ).files[0];
            // 被换脸的文件
            const imgTargetFile = document.getElementById(
                "image_face_sub_target_fileupload"
            ).files[0];

            // document.getElementById("video_remove_bg_img").innerHTML = "";
            // 图片格式
            const imageType = ["jpg", "jpeg", "png"];
            //读取文件
            // const videoFile = event.target.files[0]
            // const imageFile =
            // 文件名，扩展名
            if (imgSrcFile && imgTargetFile) {
                const imgSrcFileName = imgSrcFile.name;
                const imgSrcFileNameExtension = imgSrcFileName
                    .split(".")
                    .pop()
                    .toLowerCase();
                const imgTargetFileName = imgTargetFile.name;
                const imgTargetFileNameExtension = imgTargetFileName
                    .split(".")
                    .pop()
                    .toLowerCase();

                console.log(imgSrcFileNameExtension);
                console.log(imgTargetFileNameExtension);

                if (
                    imageType.includes(imgSrcFileNameExtension) &&
                    imageType.includes(imgTargetFileNameExtension)
                ) {

                    //提交脱敏参数，请求脱敏
                    let replaceResult = document.getElementById("replace-result");
                    replaceResult.innerHTML = "";
                    // 获取保护级别
                    let param = "1";

                    let formData = new FormData();
                    formData.set("file", imgTargetFile);
                    formData.set("params", param);
                    formData.set("algName", "image_face_sub");
                    formData.set("sheet", imgSrcFile);

                    fetch("/File/replaceFace", {
                        method: "POST",
                        body: formData,
                    })
                        .then((response) => {
                            if (response.status === 200) {
                                return response.blob();
                            } else {
                                throw new Error("Python script executes failed");
                            }
                        })
                        .then((blob) => {
                            let dealtImg = new Image();
                            dealtImg.src = URL.createObjectURL(blob);
                            replaceResult.appendChild(dealtImg);
                        })
                        .catch((error) => {
                            console.error("Error:", error);
                            alert(error);
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
            <div class="ibox-title"></div>

            <div class="tabs-container">
                <ul
                        id="tab-type"
                        class="nav nav-tabs"
                        style="left: 50%; font-size: 20px"
                >
                    <li class="active">
                        <a data-toggle="tab" href="#tab-1" aria-expanded="true">
                            失真
                        </a>
                    </li>
                    <li class="">
                        <a data-toggle="tab" href="#tab-2" aria-expanded="false">
                            非失真
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div
                            id="tab-1"
                            class="tab-pane active"
                            style="text-align: center"
                    >
                        <div>
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th>请选择图像失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody id="table1">
                                <tr>
                                    <td>
                                        <select id="algonames">
                                            <option value="dpImage">
                                                DP-基于差分隐私的图像加噪方法
                                            </option>
                                            <option value="im_coder2">
                                                DP-基于差分隐私的图像加噪方法2
                                            </option>
                                            <option value="meanValueImage">
                                                泛化-基于均值滤波器的图像加噪方法
                                            </option>
                                            <option value="pixelate">
                                                泛化-基于像素化滤波器的图像加噪方法
                                            </option>
                                            <option value="gaussian_blur">
                                                泛化-基于高斯滤波器的图像加噪方法
                                            </option>
                                            <option value="box_blur">
                                                泛化-基于盒式滤波器的图像加噪方法
                                            </option>
                                            <option value="replace_region">
                                                泛化-基于像素块的图像区域替换方法
                                            </option>
                                            <option value="image_exchange_channel">
                                                替换-图像颜色随机替换方法
                                            </option>
                                            <option value="image_add_color_offset">
                                                替换-图像颜色偏移
                                            </option>
                                            <option value="image_face_sub">
                                                替换-图像人脸替换算法
                                            </option>
                                        </select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="ibox-content" style="text-align: center">
                            <div id="privacyLevel">
                                <table style="margin: auto; font-size: 20px">
                                    <thead>
                                    <tr>
                                        <th>请选择隐私保护等级</th>
                                    </tr>
                                    </thead>
                                    <tbody id="table3">
                                    <tr>
                                        <td>
                                            <select id="level-selection">
                                                <option value="0">低程度</option>
                                                <option value="1" selected>中程度</option>
                                                <option value="2">高程度</option>
                                            </select>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>
                    <div id="tab-2" class="tab-pane" style="text-align: center">
                        <div>
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th>请选择图像非失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody id="table2">
                                <tr>
                                    <td>
                                        <select id="non-distortion-algos">
                                            <option value="retrieval">
                                                相似医疗图像安全检索
                                            </option>
                                        </select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="midtile">
                <div class="col-sm-5 m-b-xs">
                    <div id="uploadForm">
                        <input type="file" id="fileUpload" accept=".jpg, .jpeg, .png" style="display: none"/>
                        <label for="fileUpload" class="upload-btn"> 选择文件 </label>
                    </div>
                    <div id="facesub-upload" style="display: none">
                        <input
                                type="file"
                                id="image_face_sub_src_fileupload"
                                accept=".jpg, .jpeg, .png"
                                style="display: none"
                        />
                        <label for="image_face_sub_src_fileupload" class="upload-btn">
                            选择目标面孔
                        </label>
                        <input
                                type="file"
                                id="image_face_sub_target_fileupload"
                                accept=".jpg, .jpeg, .png"
                                style="display: none"
                        />
                        <label for="image_face_sub_target_fileupload" class="upload-btn">
                            选择源文件
                        </label>
                    </div>
                </div>
                <div id="fileInfo"></div>
            </div>
            <div class="btn2">
                <button type="button" class="btn btn-sm btn-primary" id="submit">
                    提交脱敏
                </button>
                <button type="button" class="btn btn-sm btn-primary m-l" id="setDefaultAlgorithm"> 设置当前算法为默认算法</button>
                <button type="button" class="btn btn-sm btn-primary m-l" id="downloadEigenvector">
                    获取原始图片特征向量文件
                </button>
            </div>
            <div class="showFile">
                <!--前后文件-->
                <div id="pre"></div>
                <div id="after"></div>
                <div id="replace-result"></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
