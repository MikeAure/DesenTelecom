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
                uploadVideo()
                //提交脱敏参数，请求脱敏
                document.getElementById("submit").onclick = function () {
                    let after = document.getElementById("after");
                    after.innerHTML = "";
                    // 获取保护级别
                    let table_body = document.getElementById("table3")
                    let tr = table_body.rows[0];
                    let param = tr.childNodes[0].firstChild.value;

                    let formData = new FormData();
                    formData.set("file", file);
                    formData.set("params", param);

                    let idx = $("ul .active").index();
                    if (idx === 0) {
                        table_body = document.getElementById("table1")
                        tr = table_body.rows[0];
                        let type1 = tr.childNodes[0].firstChild.value;
                        formData.set("sheet", type1);
                        formData.set("algName", type1);
                    } else {
                        table_body = document.getElementById("table2")
                        tr = table_body.rows[0];
                        let type2 = tr.childNodes[0].firstChild.value;
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
            } else {
                alert("请选择视频文件");
            }
        }
    }
    uploadVideo = function () {
        let input = document.getElementById("fileUpload");
        let pre = document.getElementById("pre");
        let file = input.files[0];
        let reader = new FileReader();
        reader.onload = function (e) {
            let video = document.createElement("video");
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
                            选择文件
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
                                    <td><select>
                                            <option value="meanValueVideo"> 基于均值滤波器的视频帧像素替换方法</option>
                                            <option value="pixelate_video"> 基于像素化滤波器的视频帧像素替换方法
                                            </option>
                                            <option value="gaussian_blur_video"> 基于高斯滤波器的视频帧像素替换方法
                                            </option>
                                            <option value="box_blur_video"> 基于盒式滤波器的视频帧像素替换方法</option>
                                            <option value="replace_region_video"> 基于像素块的视频帧像素替换方法
                                            </option>

                                        </select></td>
                                </tr>
                                </tbody>
                            </table>
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
                                    <td><select>
                                            <option value="test3"> 视频非失真脱敏算法A</option>
                                            <option value="test4" selected> 视频非失真脱敏算法B</option>
                                        </select></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
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
                            <td><select>
                                    <option value="0"> 低程度</option>
                                    <option value="1" selected> 中程度</option>
                                    <option value="2"> 高程度</option>
                                </select></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="btn2">
                <button type="button" class="btn btn-sm btn-primary" id="submit"> 提交脱敏</button>
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

    #pre video {
        display: inline-block;
        max-width: 50%;
        height: auto
    }

    #after {
        text-align: center;
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

    .tabs-container ul {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }
</style>
</html>
