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
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
</head>

<body >

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
    window.onload = function (){
        document.getElementById("meanValueImage_fileUpload").addEventListener("change",function (event){
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

                if(imageType.includes(fileExtension)){
                    var pre = document.getElementById("meanValueImage_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("meanValueImage_submit").onclick = function(){
                        var after = document.getElementById("meanValueImage_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("meanValueImage_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "meanValueImage");
                        formData.append("sheet", "meanValueImage");

                        fetch('/File/desenFile',{
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
                }
                else {
                    alert("请选择图像文件");
                }
            }
        })
        document.getElementById("pixelate_fileUpload").addEventListener("change",function (event){
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

                if(imageType.includes(fileExtension)){
                    var pre = document.getElementById("pixelate_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("pixelate_submit").onclick = function(){
                        var after = document.getElementById("pixelate_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("pixelate_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "pixelate");
                        formData.append("sheet", "pixelate");

                        fetch('/File/desenFile',{
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
                }
                else {
                    alert("请选择图像文件");
                }
            }
        })
        document.getElementById("gaussian_blur_fileUpload").addEventListener("change",function (event){
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

                if(imageType.includes(fileExtension)){
                    var pre = document.getElementById("gaussian_blur_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("gaussian_blur_submit").onclick = function(){
                        var after = document.getElementById("gaussian_blur_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("gaussian_blur_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "gaussian_blur");
                        formData.append("sheet", "gaussian_blur");

                        fetch('/File/desenFile',{
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
                }
                else {
                    alert("请选择图像文件");
                }
            }
        })
        document.getElementById("box_blur_fileUpload").addEventListener("change",function (event){
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

                if(imageType.includes(fileExtension)){
                    var pre = document.getElementById("box_blur_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("box_blur_submit").onclick = function(){
                        var after = document.getElementById("box_blur_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("box_blur_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "box_blur");
                        formData.append("sheet", "box_blur");

                        fetch('/File/desenFile',{
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
                }
                else {
                    alert("请选择图像文件");
                }
            }
        })
        document.getElementById("replace_region_fileUpload").addEventListener("change",function (event){
            // 清空
            document.getElementById("replace_region_pre").innerHTML = "";
            document.getElementById("replace_region_after").innerHTML = "";
            // 视频格式
            const imageType = ['jpg', 'jpeg', 'png', 'gif'];

            //读取文件
            const file = event.target.files[0]
            // 文件名，扩展名
            if (file) {
                const fileName = file.name;
                const fileExtension = fileName.split('.').pop().toLowerCase();
                console.log(fileExtension)

                if(imageType.includes(fileExtension)){
                    var pre = document.getElementById("replace_region_pre");
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        pre.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                    //提交脱敏参数，请求脱敏
                    document.getElementById("replace_region_submit").onclick = function(){
                        var after = document.getElementById("replace_region_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("replace_region_privacyLevel").value;

                        var formData = new FormData();
                        formData.append("file", file);
                        formData.append("params", param);
                        formData.append("algName", "replace_region");
                        formData.append("sheet", "replace_region");

                        fetch('/File/desenFile',{
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
                }
                else {
                    alert("请选择图像文件");
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
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">9.基于像素化滤波器的图像像素替换方法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="pixelate_fileUpload"  style="display: none;">
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
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id = "pixelate_pre" style="margin-right: 20px;">
            </div>
            <div id = "pixelate_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="pixelate_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">10.基于高斯滤波器的图像像素替换方法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="gaussian_blur_fileUpload"  style="display: none;">
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
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id = "gaussian_blur_pre" style="margin-right: 20px;">
            </div>
            <div id = "gaussian_blur_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="gaussian_blur_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">11.基于盒式滤波器的图像像素替换方法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
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
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="box_blur_fileUpload"  style="display: none;">
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
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id = "box_blur_pre" style="margin-right: 20px;">
            </div>
            <div id = "box_blur_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="box_blur_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">12.均值滤波图像脱敏算法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
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
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="meanValueImage_fileUpload"  style="display: none;">
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
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id = "meanValueImage_pre" style="margin-right: 20px;">
            </div>
            <div id = "meanValueImage_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="meanValueImage_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">13.基于像素块的图像像素替换方法</p>
            <div <#--class="col-sm-6"--> style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将图像的一部分像素替换颜色
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
                            <form id = "uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="replace_region_fileUpload"  style="display: none;">
                                <label for="replace_region_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择隐私保护等级
                            <select id="replace_region_privacyLevel">
                                <option value="0"> 低程度 </option>
                                <option value="1" selected> 中程度 </option>
                                <option value="2"> 高程度 </option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id = "replace_region_pre" style="margin-right: 20px;">
            </div>
            <div id = "replace_region_after">
            </div>
        </div>

        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary" id="replace_region_submit"> 提交脱敏</button>
        </div>

    </div>
    <hr>
</div>


</body>
<style>
    .showFile{
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 0;
    }
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
    .showFile{
        display: flex;
        justify-content: center;
    }
    /*上传按钮*/
    .upload-btn, #meanValueImage_submit, #pixelate_submit, #box_blur_submit,
    #gaussian_blur_submit {
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }
    #meanValueImage_pre, #meanValueImage_after{
        text-align: center;
    }
    #meanValueImage_pre img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }
    #meanValueImage_after img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }
    #pixelate_pre, #pixelate_after{
        text-align: center;
    }
    #pixelate_pre img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }
    #pixelate_after img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }
    #gaussian_blur_pre, #gaussian_blur_after{
        text-align: center;
    }
    #gaussian_blur_pre img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }
    #gaussian_blur_after img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }
    #box_blur_pre, #box_blur_after{
        text-align: center;
    }
    #box_blur_pre img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }
    #box_blur_after img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }
    image, video {
        display: inline-block;
        max-width: 50%;
        height: auto
    }
    /*选择框居中*/
    .midtile{
        line-height: 30px;
        text-align: center;
        display:flex;
        justify-content: center;
    }
    /*上传按钮*/
    .upload-btn, #replace_region_submit{
        background-color: #347aa9;
        color: white;
        cursor: pointer;
        padding: 5px 20px;
        text-align: center;
        font-size: 20px;
        display: inline-block;
        margin: 30px;
    }
    #replace_region_after, #replace_region_pre{
        text-align: center;
    }
    #replace_region_pre img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }
    #replace_region_after img{
        display: inline-block;
        max-width: 50%;
        height: auto;
    }


</style>

</html>
