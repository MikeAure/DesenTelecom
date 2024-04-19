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
    <!-- 全局js -->
    <script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctx!}/js/bootstrap.min.js?v=3.3.6"></script>
    <script src="${ctx!}/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="${ctx!}/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="${ctx!}/js/plugins/layer/layer.min.js"></script>

    <!-- 自定义js -->
    <script src="${ctx!}/js/hAdmin.js?v=4.1.0"></script>
    <script type="text/javascript" src="${ctx!}/js/index.js"></script>

    <script type="text/javascript" defer>
        window.onload = function () {
            document.getElementById("passReplace_submitBtn").addEventListener("click", function () {
                let textInput = $("#passReplace_textInput").val();
                var textType = "name"
                var algName = "passReplace"
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
                        document.getElementById("passReplace_outputText").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            })
            // document.getElementById("replace_region_fileUpload").addEventListener("change", function (event) {
            //     // 清空
            //     document.getElementById("replace_region_pre").innerHTML = "";
            //     document.getElementById("replace_region_after").innerHTML = "";
            //     // 视频格式
            //     const imageType = ['jpg', 'jpeg', 'png', 'gif'];
            //
            //     //读取文件
            //     const file = event.target.files[0]
            //     // 文件名，扩展名
            //     if (file) {
            //         const fileName = file.name;
            //         const fileExtension = fileName.split('.').pop().toLowerCase();
            //         console.log(fileExtension)
            //
            //         if (imageType.includes(fileExtension)) {
            //             var pre = document.getElementById("replace_region_pre");
            //             var reader = new FileReader();
            //             reader.onload = function (e) {
            //                 var img = new Image();
            //                 img.src = e.target.result;
            //
            //                 pre.appendChild(img);
            //             };
            //             reader.readAsDataURL(file);
            //             //提交脱敏参数，请求脱敏
            //             document.getElementById("replace_region_submit").onclick = function () {
            //                 var after = document.getElementById("replace_region_after");
            //                 after.innerHTML = "";
            //                 // 获取保护级别
            //                 var param = document.getElementById("replace_region_privacyLevel").value;
            //
            //                 var formData = new FormData();
            //                 formData.append("file", file);
            //                 formData.append("params", param);
            //                 formData.append("algName", "replace_region");
            //                 formData.append("sheet", "replace_region");
            //
            //                 fetch('/File/desenFile', {
            //                     method: 'POST',
            //                     body: formData
            //                 })
            //                     .then(response => response.blob())
            //                     .then(blob => {
            //                         var dealedImg = new Image();
            //                         dealedImg.src = URL.createObjectURL(blob);
            //                         after.appendChild(dealedImg);
            //                     })
            //                     .catch(error => console.error('Error:', error));
            //
            //             }
            //         } else {
            //             alert("请选择图像文件");
            //         }
            //     }
            // })
            // document.getElementById("replace_region_video_fileUpload").addEventListener("change", function (event) {
            //     // 清空
            //     document.getElementById("replace_region_video_pre").innerHTML = "";
            //     document.getElementById("replace_region_video_after").innerHTML = "";
            //     // 视频格式
            //     const videoType = ['mp4', 'avi']
            //
            //     //读取文件
            //     const file = event.target.files[0]
            //     // 文件名，扩展名
            //     if (file) {
            //         const fileName = file.name;
            //         const fileExtension = fileName.split('.').pop().toLowerCase();
            //         console.log(fileExtension)
            //
            //         if (videoType.includes(fileExtension)) {
            //             var pre = document.getElementById("replace_region_video_pre");
            //             var reader = new FileReader();
            //             reader.onload = function (e) {
            //                 var video = document.createElement("video");
            //                 video.src = e.target.result;
            //                 /*video.style.width = '300px';*/
            //                 video.controls = true;
            //                 pre.appendChild(video);
            //             };
            //             reader.readAsDataURL(file);
            //             //提交脱敏参数，请求脱敏
            //             document.getElementById("replace_region_video_submit").onclick = function () {
            //                 var after = document.getElementById("replace_region_video_after");
            //                 after.innerHTML = "";
            //                 // 获取保护级别
            //                 var param = document.getElementById("replace_region_video_privacyLevel").value;
            //
            //                 var formData = new FormData();
            //                 formData.append("file", file);
            //                 formData.append("params", param);
            //                 formData.append("algName", "replace_region_video");
            //                 formData.append("sheet", "replace_region_video");
            //
            //                 fetch('/File/desenFile', {
            //                     method: 'POST',
            //                     body: formData
            //                 })
            //                     .then(response => response.blob())
            //                     .then(blob => {
            //                         var video = document.createElement("video");
            //                         //console.log(data)
            //                         video.src = URL.createObjectURL(blob);
            //                         video.type = "video/mp4";
            //                         video.controls = true;
            //                         after.appendChild(video);
            //                     })
            //                     .catch(error => console.error('Error:', error));
            //
            //             }
            //         } else {
            //             alert("请选择视频文件");
            //         }
            //     }
            // })
            document.getElementById("audio_reshuffle_fileUpload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("audio_reshuffle_pre").innerHTML = "";
                document.getElementById("audio_reshuffle_after").innerHTML = "";
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
                        let pre = document.getElementById("audio_reshuffle_pre");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            var video = document.createElement("audio");
                            video.src = e.target.result;
                            video.controls = true;
                            pre.appendChild(video);
                        };
                        reader.readAsDataURL(file);
                        //提交脱敏参数，请求脱敏
                        document.getElementById("audio_reshuffle_submit").onclick = function () {
                            let after = document.getElementById("audio_reshuffle_after");
                            after.innerHTML = "";
                            // 获取保护级别
                            let sheet = document.getElementById("audio_reshuffle_block_num").value;

                            let formData = new FormData();
                            formData.append("file", file);
                            formData.append("params", "0");
                            formData.append("algName", "audio_reshuffle");
                            formData.append("sheet", sheet);

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
            })
            document.getElementById("SHA512_submitBtn").addEventListener("click", function () {
                let textInput = $("#SHA512_input").val();
                var privacyLevel = 1
                var textType = "address"
                var algName = "SHA512"
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
                        document.getElementById("SHA512_output").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            })
            document.getElementById("value_hide_submitBtn").addEventListener("click", function () {
                let textInput = $("#value_hide_input").val();
                var privacyLevel = 1
                var textType = "address"
                var algName = "value_hide"
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
                        document.getElementById("value_hide_output").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            })
            document.getElementById("valueMapping_submitBtn").addEventListener("click", function () {
                let textInput = $("#valueMapping_input").val();
                var privacyLevel = 1
                var textType = "address"
                var algName = "valueMapping"
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
                        document.getElementById("valueMapping_output").value = data;
                    })
                    .catch(error => console.error('Error:', error));
            })

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
                        var pre = document.getElementById("image_exchange_channel_pre");
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            var img = new Image();
                            img.src = e.target.result;

                            pre.appendChild(img);
                        };
                        reader.readAsDataURL(file);
                        //提交脱敏参数，请求脱敏
                        document.getElementById("image_exchange_channel_submit").onclick = function () {
                            var after = document.getElementById("image_exchange_channel_after");
                            after.innerHTML = "";
                            // 获取保护级别
                            var param = "1";

                            var formData = new FormData();
                            formData.append("file", file);
                            formData.append("params", param);
                            formData.append("algName", "image_exchange_channel");
                            formData.append("sheet", "image_exchange_channel");

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
                        var pre = document.getElementById("image_add_color_offset_pre");
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            var img = new Image();
                            img.src = e.target.result;

                            pre.appendChild(img);
                        };
                        reader.readAsDataURL(file);
                        //提交脱敏参数，请求脱敏
                        document.getElementById("image_add_color_offset_submit").onclick = function () {
                            var after = document.getElementById("image_add_color_offset_after");
                            after.innerHTML = "";
                            // 获取保护级别
                            var param = document.getElementById("image_add_color_offset_privacyLevel").value;

                            var formData = new FormData();
                            formData.append("file", file);
                            formData.append("params", param);
                            formData.append("algName", "image_add_color_offset");
                            formData.append("sheet", "image_add_color_offset");

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
                        var pre = document.getElementById("video_add_color_offset_pre");
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
                        document.getElementById("video_add_color_offset_submit").onclick = function () {
                            var after = document.getElementById("video_add_color_offset_after");
                            after.innerHTML = "";
                            // 获取保护级别
                            var param = document.getElementById("video_add_color_offset_privacyLevel").value;

                            var formData = new FormData();
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
                        var pre = document.getElementById("video_remove_bg_pre");
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            var video = document.createElement("video");
                            video.src = e.target.result;
                            /*video.style.width = '300px';*/
                            video.controls = true;
                            pre.appendChild(video);
                        };
                        reader.readAsDataURL(videoFile);
                        //提交脱敏参数，请求脱敏
                        // document.getElementById("video_remove_bg_submit").onclick = function () {
                        //     var after = document.getElementById("video_remove_bg_after");
                        //     after.innerHTML = "";
                        //     // 获取保护级别
                        //     var param = document.getElementById("video_remove_bg_privacyLevel").value;
                        //
                        //     var formData = new FormData();
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
                        //             var video = document.createElement("video");
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
            })
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
                        var pre = document.getElementById("video_remove_bg_img");
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            var img = new Image();
                            img.src = e.target.result;
                            img.setAttribute("width", "500px");
                            img.setAttribute("height", "300px");
                            pre.appendChild(img);
                        };
                        reader.readAsDataURL(imageFile);
                        //提交脱敏参数，请求脱敏
                        // document.getElementById("video_remove_bg_submit").onclick = function () {
                        //     var after = document.getElementById("video_remove_bg_after");
                        //     after.innerHTML = "";
                        //     // 获取保护级别
                        //     var param = document.getElementById("video_remove_bg_privacyLevel").value;
                        //
                        //     var formData = new FormData();
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
                        //             var video = document.createElement("video");
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
            })
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
                        var after = document.getElementById("video_remove_bg_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        var param = document.getElementById("video_remove_bg_privacyLevel").value;

                        var formData = new FormData();
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
            })
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
                        var pre = document.getElementById("img_face_sub_pre");
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
                const imgSrcFile = document.getElementById("img_face_sub_src_fileupload").files[0];
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
                        let after = document.getElementById("img_face_sub_after");
                        after.innerHTML = "";
                        // 获取保护级别
                        let param = "1";

                        let formData = new FormData();
                        formData.append("file", imgSrcFile);
                        formData.append("params", param);
                        formData.append("algName", "video_remove_bg");
                        formData.append("sheet", imgTargetFile);

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
            })

            document.getElementById("video_face_sub_target_fileupload").addEventListener("change", function (event) {

                // 清空
                document.getElementById("img_face_sub_target_img").innerHTML = "";
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
                        let pre = document.getElementById("video_face_sub_target_img");
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
                    }
                }
            })

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
                        let param = "4";

                        let formData = new FormData();
                        formData.append("file", imgSrcFile);
                        formData.append("params", param);
                        formData.append("algName", "video_face_sub");
                        formData.append("sheet", videoTargetFile);

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
            })

            document.getElementById("voice_replace_src_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("voice_replace_pre").innerHTML = "";
                document.getElementById("voice_replace_after").innerHTML = "";
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
                        let pre = document.getElementById("voice_replace_pre");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let video = document.createElement("audio");
                            video.src = e.target.result;
                            video.controls = true;
                            pre.appendChild(video);
                        };
                        reader.readAsDataURL(file);
                        //提交脱敏参数，请求脱敏
                        document.getElementById("voice_replace_submit").onclick = function () {
                            let after = document.getElementById("voice_replace_after");
                            after.innerHTML = "";
                            // 获取保护级别
                            let param = "1";

                            let formData = new FormData();
                            formData.append("file", file);
                            formData.append("params", param);
                            formData.append("algName", "replace_voice_print");
                            formData.append("sheet", "replace_voice_print");

                            fetch('/File/desenFile', {
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

            document.getElementById("apply_audio_effects_src_fileupload").addEventListener("change", function (event) {
                // 清空
                document.getElementById("apply_audio_effects_pre").innerHTML = "";
                document.getElementById("apply_audio_effects_after").innerHTML = "";
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
                        let pre = document.getElementById("apply_audio_effects_pre");
                        let reader = new FileReader();
                        reader.onload = function (e) {
                            let video = document.createElement("audio");
                            video.src = e.target.result;
                            video.controls = true;
                            pre.appendChild(video);
                        };
                        reader.readAsDataURL(file);
                        //提交脱敏参数，请求脱敏
                        document.getElementById("apply_audio_effects_submit").onclick = function () {
                            let after = document.getElementById("apply_audio_effects_after");
                            after.innerHTML = "";
                            // 获取保护级别
                            let param = document.getElementById("apply_audio_effects_privacyLevel").value;

                            let formData = new FormData();
                            formData.append("file", file);
                            formData.append("params", param);
                            formData.append("algName", "apply_audio_effects");
                            formData.append("sheet", "apply_audio_effects");

                            fetch('/File/desenFile', {
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


<div class="ibox-title">
</div>

<div class="panel panel-default">
    <div class="panel-heading" style="text-align: center;">
        <h1 class="panel-title"><b style="font-size: 2em">数据置换算法</b></h1>
    </div>

    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                1.密码置换算法</p>
            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em; text-align: justify;">
                        说明：对于密码数据等字段，生成随机长度的随机字符串
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
                            <input type="text" id="passReplace_textInput" class="form-control" placeholder="请输入文本"
                                   style="font-size: 20px">
                            <span class="input-group-btn">
                    <button class="btn btn-default" id="passReplace_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                        </div>
                        <div class="text-center">
                            <label for="passReplace_outputText"
                                   style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <textarea id="passReplace_outputText" rows="2" cols="50" readonly
                                          style="margin-top: 10px;"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <#--    <div class="panel-body">-->
    <#--        <div class="row">-->
    <#--            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">-->
    <#--                2.基于像素块的图像像素替换方法</p>-->
    <#--            <div &lt;#&ndash;class="col-sm-6"&ndash;&gt;-->
    <#--                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">-->
    <#--                &lt;#&ndash; <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>&ndash;&gt;-->
    <#--                <div>-->
    <#--                    <p style="font-size: 1.5em;text-align: justify;">-->
    <#--                        说明：对图像进行马赛克处理-->
    <#--                    </p>-->
    <#--                    <p style="font-size: 1.5em;text-align: justify;">-->
    <#--                        输入：图像-->
    <#--                    </p>-->
    <#--                    <p style="font-size: 1.5em;text-align: justify;">-->
    <#--                        输出：图像-->
    <#--                    </p>-->
    <#--                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>-->
    <#--                    <div class="midtile">-->
    <#--                        <div class="&lt;#&ndash;col-sm-5 &ndash;&gt; align-items-center">-->
    <#--                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">-->
    <#--                                <input type="file" id="replace_region_fileUpload" style="display: none;">-->
    <#--                                <label for="replace_region_fileUpload" class="upload-btn">-->
    <#--                                    选择文件-->
    <#--                                </label>-->
    <#--                            </form>-->
    <#--                        </div>-->
    <#--                    </div>-->
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
    <#--                </div>-->
    <#--            </div>-->

    <#--        </div>-->
    <#--        <div class="showFile">-->
    <#--            <!--前后文件&ndash;&gt;-->
    <#--            <div id="replace_region_pre" style="margin-right: 20px;">-->
    <#--            </div>-->
    <#--            <div id="replace_region_after">-->
    <#--            </div>-->
    <#--        </div>-->
    <#--        <div class="btn2" style="text-align: center;">-->
    <#--            <button type="button" class="btn btn-sm btn-primary" id="replace_region_submit"> 提交脱敏</button>-->
    <#--        </div>-->
    <#--    </div>-->
    <#--    <hr>-->
    <#--    <div class="panel-body">-->
    <#--        <div class="row">-->
    <#--            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">-->
    <#--                3.基于像素块的视频帧像素替换方法</p>-->
    <#--            <div &lt;#&ndash;class="col-sm-6"&ndash;&gt;-->
    <#--                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">-->
    <#--                &lt;#&ndash; <p style="font-size: 1.5em;">3.基于像素块的视频帧像素替换方法</p>&ndash;&gt;-->
    <#--                <div>-->
    <#--                    <p style="font-size: 1.5em;text-align: justify;">-->
    <#--                        说明：对视频进行马赛克处理-->
    <#--                    </p>-->
    <#--                    <p style="font-size: 1.5em;text-align: justify;">-->
    <#--                        输入：视频对象-->
    <#--                    </p>-->
    <#--                    <p style="font-size: 1.5em;text-align: justify;">-->
    <#--                        输出：视频对象-->
    <#--                    </p>-->
    <#--                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>-->
    <#--                    <div class="midtile">-->
    <#--                        <div class="&lt;#&ndash;col-sm-5&ndash;&gt;  align-items-center">-->
    <#--                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">-->
    <#--                                <input type="file" id="replace_region_video_fileUpload" style="display: none;">-->
    <#--                                <label for="replace_region_video_fileUpload" class="upload-btn">-->
    <#--                                    选择文件-->
    <#--                                </label>-->
    <#--                            </form>-->
    <#--                        </div>-->
    <#--                    </div>-->
    <#--                    <div &lt;#&ndash;class="ibox-content"&ndash;&gt; style="text-align: center;">-->
    <#--                        <div style="margin: auto; font-size: 20px">-->
    <#--                            请选择隐私保护等级-->
    <#--                            <select id="replace_region_video_privacyLevel">-->
    <#--                                <option value="0"> 低程度</option>-->
    <#--                                <option value="1" selected> 中程度</option>-->
    <#--                                <option value="2"> 高程度</option>-->
    <#--                            </select>-->
    <#--                        </div>-->
    <#--                    </div>-->
    <#--                </div>-->
    <#--            </div>-->

    <#--        </div>-->
    <#--        <div class="showFile">-->
    <#--            <!--前后文件&ndash;&gt;-->
    <#--            <div id="replace_region_video_pre" style="margin-right: 20px;">-->
    <#--            </div>-->
    <#--            <div id="replace_region_video_after">-->
    <#--            </div>-->
    <#--        </div>-->
    <#--        <div class="btn2" style="text-align: center;">-->
    <#--            <button type="button" class="btn btn-sm btn-primary" id="replace_region_video_submit"> 提交脱敏</button>-->
    <#--        </div>-->
    <#--    </div>-->
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                2.数值替换</p>
            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将字符串中的数字替换成一个常量
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="value_hide_input" class="form-control" placeholder="请输入文本"
                                       style="font-size: 20px">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" id="value_hide_submitBtn" type="button"
                                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                        提交脱敏
                                    </button>
                                </span>
                            </div>
                            <div class="text-center">
                                <label for="value_hide_output"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="value_hide_output" rows="2" cols="50" readonly
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
                3.假名化-哈希算法</p>
            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将数据映射为定长hash值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：字符串
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="SHA512_input" class="form-control" placeholder="请输入文本"
                                       style="font-size: 20px">
                                <span class="input-group-btn">
                    <button class="btn btn-default" id="SHA512_submitBtn" type="button"
                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                        提交脱敏
                    </button>
                        </span>
                            </div>
                            <div class="text-center">
                                <label for="SHA512_output"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="SHA512_output" rows="2" cols="50" readonly
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
                4.数值映射</p>
            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：将数据映射为新值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：数值
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：数值
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                </div>
                <div class="container">
                    <div class="row justify-content-center" style="display: grid; place-items: center;">
                        <div class="col-lg-5">
                            <div class="input-group">
                                <input type="text" id="valueMapping_input" class="form-control" placeholder="请输入文本"
                                       style="font-size: 20px">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" id="valueMapping_submitBtn" type="button"
                                            style="font-size: 20px;height: 30px;display: flex; justify-content: center; align-items: center; ">
                                        提交脱敏
                                    </button>
                                </span>
                            </div>
                            <div class="text-center">
                                <label for="valueMapping_output"
                                       style="display: block; font-size: 20px;justify-content: center; align-items: center; ">脱敏结果:</label>
                                <div style="display: flex; flex-direction: column; align-items: center;">
                                    <textarea id="valueMapping_output" rows="2" cols="50" readonly
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
                5.图像颜色随机替换方法</p>
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
                6.图像颜色偏移</p>
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
                7.图像人脸替换算法</p>
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
                                    选择换脸源文件
                                </label>
                                <input type="file" id="img_face_sub_target_fileupload" style="display: none;">
                                <label for="img_face_sub_target_fileupload" class="upload-btn">
                                    选择目标图片
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
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                8.基于像素块的视频帧像素颜色偏移方法</p>
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
                9.视频背景替换算法</p>
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
                10.视频人脸替换算法</p>
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
            <div id="video_face_sub_target_img" style="margin-right: 20px;">
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
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                11.声纹替换算法</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：用固定声纹替换原始音频的声纹
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：原音频文件路径
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="voice_replace_src_fileupload" style="display: none;">
                                <label for="voice_replace_src_fileupload" class="upload-btn">
                                    选择音频源文件
                                </label>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id="voice_replace_pre" style="margin-right: 20px;">
            </div>
            <div id="voice_replace_after">
            </div>

        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="voice_replace_submit"> 提交脱敏</button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                12.音频变形</p>
            <div <#--class="col-sm-6"-->
                    style="display: flex; flex-wrap: wrap; justify-content:  center; width: 50%; margin: 0 auto; ">
                <#-- <p style="font-size: 1.5em;">2.基于像素块的图像像素替换方法</p>-->
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对音频进行拉伸、移位和增益
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：原音频文件路径
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="align-items-center">
                            <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                <input type="file" id="apply_audio_effects_src_fileupload" style="display: none;">
                                <label for="apply_audio_effects_src_fileupload" class="upload-btn">
                                    选择音频源文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div <#--class="ibox-content"--> style="text-align: center;">
                        <div style="margin: auto; font-size: 20px">
                            请选择人物轮廓边缘距离（程度越高越贴近人物轮廓）：
                            <select id="apply_audio_effects_privacyLevel">
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
            <div id="apply_audio_effects_pre" style="margin-right: 20px;">
            </div>
            <div id="apply_audio_effects_after">
            </div>

        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="apply_audio_effects_submit"> 提交脱敏
            </button>
        </div>
    </div>
    <hr>
    <div class="panel-body">
        <div class="row">
            <p style="font-size: 1.5em;display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                13.音频重排</p>
            <div style="display: flex; flex-wrap: wrap; justify-content: center; width: 50%; margin: 0 auto;">
                <div>
                    <p style="font-size: 1.5em;text-align: justify;">
                        说明：对音频进行分块，随机重排所有分块后合并为一个音频
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输入：音频文件
                    </p>
                    <p style="font-size: 1.5em;text-align: justify;">
                        输出：音频对象（保存到指定音频文件路径）
                    </p>
                    <p style="font-size: 1.5em;text-align: center;">算法测试</p>
                    <div class="midtile">
                        <div class="col-sm-5 align-items-center">
                            <form id="uploadForm" method="post" enctype="multipart/form-data">
                                <input type="file" id="audio_reshuffle_fileUpload" style="display: none;">
                                <label for="audio_reshuffle_fileUpload" class="upload-btn">
                                    选择文件
                                </label>
                            </form>
                        </div>
                    </div>
                    <div style="text-align: center;">
                        <div style=" font-size: 20px">
                            <label for="audio_reshuffle_block_num">音频分块数量:</label>
                            <input type="text" id="audio_reshuffle_block_num">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="showFile">
            <!--前后文件-->
            <div id=audio_reshuffle_pre style="margin-right: 20px;">
            </div>
            <div id=audio_reshuffle_after>
            </div>
        </div>
        <div class="btn2" style="text-align: center;">
            <button type="button" class="btn btn-sm btn-primary submit-btn" id="audio_reshuffle_submit">提交脱敏
            </button>
        </div>
    </div>

</div>
</body>
</html>
