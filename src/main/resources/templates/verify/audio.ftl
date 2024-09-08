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

        .btn2 > button {
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

        /*.content-container {*/
        /*    display: flex;*/
        /*    flex-direction: column;*/
        /*    align-items: center;*/
        /*    width: 400px;*/
        /*    padding: 40px;*/
        /*}*/
        /*.section {*/
        /*    width: 100%;*/
        /*    max-width: 600px; !* 设置最大宽度以避免组件过宽 *!*/
        /*    display: flex;*/
        /*    flex-direction: column;*/
        /*    align-items: center;*/
        /*    margin-bottom: 20px;*/
        /*}*/
        /*.section {*/
        /*    !*弹性布局 让子元素称为弹性项目*!*/
        /*    display: flex;*/
        /*    !*让弹性项目垂直排列  原理是改变弹性盒子的主轴方向*/
        /*    父元素就是弹性盒子  现在改变后的主轴方向是向下了*!*/
        /*    flex-direction: column;*/
        /*    !*让弹性项目在交叉轴方向水平居中  现在主轴的方向是向下*/
        /*    交叉轴的方向是与主轴垂直 交叉轴的方向是向右*!*/
        /*    align-items: center;*/
        /*    width: 400px;*/
        /*    padding: 40px;*/

        /*}*/
        /*.form-group {*/
        /*    display: flex;*/
        /*    align-items: center; !* 垂直居中对齐 *!*/
        /*    !*margin-top: 10px;*!*/
        /*    !*margin-bottom: 10px;*!*/
        /*    !*width: 100%;*!*/
        /*}*/
        /*.form-group label {*/
        /*    margin-right: 10px;*/
        /*}*/

        /*.form-group input {*/
        /*    margin-right: 10px;*/
        /*}*/

        /*input[type="text"], textarea, input[type="file"] {*/
        /*    width: calc(100% - 120px); !* 调整宽度，预留出标签的空间 *!*/
        /*    min-width: 100px;*/
        /*    max-width: 400px; !* 限制最大宽度 *!*/
        /*    padding: 10px;*/
        /*    box-sizing: border-box;*/
        /*}*/
        /*textarea {*/
        /*    resize: vertical; !* 允许垂直方向调整大小 *!*/
        /*}*/
        /*.form-group button {*/
        /*    display: flex;*/
        /*    gap: 10px;*/
        /*    justify-content: center; !* 按钮居中 *!*/
        /*}*/
        /*.buttons {*/
        /*    margin: 10px;*/
        /*}*/


        /* 整体内容容器居中显示 */
        .content-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            /*justify-content: center;*/
            min-height: 100vh; /* 使其至少高度填满视口 */
            width: 80%; /* 根据需要调整宽度 */
            margin: 0 auto; /* 水平居中 */
        }

        .section {
            width: 100%; /* 让每个section占满容器的宽度 */
            margin-bottom: 20px; /* section之间的间距 */
        }

        .section-box {
            display: flex;
            flex-direction: column;
            align-items: flex-start; /* 子元素向左对齐 */
            width: 50%; /* 或者根据需要调整 */
            margin: auto; /* 容器居中 */

        }

        .section-box input[type="text"],
        .section-box textarea {
            width: 100%; /* 输入框和文本域宽度占满其父容器 */
            margin-bottom: 10px; /* 元素之间的间距 */
            align-self: center;
        }

        /* 使按钮水平排列 */
        .section-box .buttons {
            display: flex;
            flex-direction: row;
            justify-content: center;
            margin-top: 10px; /* 按钮与上方元素的间距 */
            width: auto;
            align-self: center;
        }

        .section-box button {
            background-color: #347aa9;
            color: white;
            cursor: pointer;
            padding: 5px 20px;
            text-align: center;
            font-size: 15px;
            border-radius: 5px;
            border: none; /* 移除边框 */
            transition: background-color 0.3s; /* 平滑背景色变化 */
        }

        .section-box button:disabled {
            background-color: #cccccc; /* 灰色背景表示禁用状态 */
            cursor: not-allowed; /* 更改鼠标指针为不允许图标 */
            color: #666666; /* 可选：更改文字颜色以增强禁用状态的效果 */
        }

        .audio-match-button {
            margin-left: 10px; /* 文件选择按钮与注册/登录按钮之间的间距 */
        }

        /* 可能需要的额外样式 */
        input[type="file"] {
            margin-bottom: 10px; /* 为隐藏的文件输入添加间距，如果它变为可见的 */
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
        let defaultOption = 'dpAudio'; // 预设的默认选项
        document.addEventListener('DOMContentLoaded', function() {
            let selectElement = document.getElementById('distortionAudioAlgName');
            fetch('/toolset/getDefaultSelection?toolsetName=' + 'audio')
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Failed to fetch');
                    }
                })
                .then(data => {
                    if (data.code === 200 && data.data) {
                        setDefaultOption(data.data);
                        defaultOption = data.data;
                        if (defaultOption === "voice_replace") {
                            document.getElementById("privacyLevel").style.display = "none";
                        } else {
                            document.getElementById("privacyLevel").style.display = "block";
                        }
                    } else {
                        setDefaultOption(defaultOption);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    setDefaultOption(defaultOption);
                });

            function setDefaultOption(value) {
                selectElement.value = value;
            }

            // 初始化时禁用注册和登录按钮
            document.getElementById('register-button').disabled = true;
            document.getElementById('login-button').disabled = true;

            // 注册文件选择器的change事件监听器
            document.getElementById('registerFileSelector').addEventListener('change', function () {
                // 当文件被选择时，启用注册按钮
                if (this.files.length > 0) {
                    document.getElementById('register-button').disabled = false;
                } else {
                    document.getElementById('register-button').disabled = true;
                }
            });

            // 登录文件选择器的change事件监听器
            document.getElementById('loginFileSelector').addEventListener('change', function () {
                // 当文件被选择时，启用登录按钮
                if (this.files.length > 0) {
                    document.getElementById('login-button').disabled = false;
                } else {
                    document.getElementById('login-button').disabled = true;
                }
            });
        });

        window.onload = function () {
            document.getElementById("fileUpload").addEventListener("change", chooseFile);
            // 切换选项卡时实现恢复到选择的默认算法，并清空脱敏算法部分的文件控件文件和预览部分的内容
            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                // 清空文件预览部分中的内容
                document.getElementById("after").innerHTML = "";
                document.getElementById("pre").innerHTML = "";
                // 清空文件控件中的文件
                document.getElementById("fileUpload").value = "";

                document.getElementById("distortionAudioAlgName").value = defaultOption;
                if (defaultOption === "voice_replace") {
                    document.getElementById("privacyLevel").style.display = "none";
                } else {
                    document.getElementById("privacyLevel").style.display = "block";
                }
            });
            // 更改算法选项
            document.getElementById("distortionAudioAlgName").addEventListener("change", function () {
                // 清空文件预览部分中的内容
                document.getElementById("after").innerHTML = "";
                document.getElementById("pre").innerHTML = "";
                // 清空文件控件中的文件
                document.getElementById("fileUpload").value = "";

                // 失真算法名
                let distortionAudioAlgName = document.getElementById("distortionAudioAlgName").value;
                if (distortionAudioAlgName === "voice_replace") {
                    document.getElementById("privacyLevel").style.display = "none";
                } else {
                    document.getElementById("privacyLevel").style.display = "block";
                }
            });
            // 设置默认算法
            $("#setDefaultAlgorithm").on("click", function (e) {
                let postData = new URLSearchParams();
                postData.set("toolsetName", "audio");
                postData.set("defaultAlgName", $("#distortionAudioAlgName").val());
                console.log("postData: " + postData);

                fetch ("/toolset/setDefaultToolset", {
                    method: "POST",
                    body: postData,
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.code === 200) {
                            alert("设置默认算法成功！");
                            defaultOption = $("#distortionAudioAlgName").val();
                        } else {
                            throw new Error("设置默认算法失败！");
                        }
                    })
                    .catch((error) => {
                        alert(error);
                        console.log(error);
                    })
            });
            document.getElementById("submit").addEventListener("click", originalSubmit);

            let registerBtn = document.getElementById("register-button");
            let loginBtn = document.getElementById("login-button");
            let startServerBtn = document.getElementById("startServer");
            let stopServerBtn = document.getElementById("stopServer");
            let signUpMessage = document.getElementById("registrationMessage");
            let signInMessage = document.getElementById("loginMessage");
            let downloadFileBtn = document.getElementById("download-encrypted-voice");

            let choose_audio_file = function (e, fileElementId, nameElementId, backendInterface) {
                // e.preventDefault(); // 阻止表单的默认提交行为

                const fileInput = document.getElementById(fileElementId);
                console.log("file name: " + fileInput.files[0].name);
                const name = document.getElementById(nameElementId);
                console.log("user name: " + name.value);
                const formData = new FormData();
                formData.set('file', fileInput.files[0]);
                formData.set("name", name.value);

                fetch(backendInterface, {
                    method: 'POST',
                    body: formData,
                })
                    .then(response => response.json())
                    .then(data => {
                        let messageArea = signUpMessage;
                        let success = "注册成功\n";
                        if (nameElementId.includes("login")) {
                            messageArea = signInMessage;
                            success = "登录成功\n";
                        }
                        if (data.status === "ok") {
                            messageArea.value += success;
                        } else {
                            messageArea.value += "失败："
                            messageArea.value += data.message + "\n"
                        }
                    }) // 处理响应数据
                    .catch(error => console.error(error)); // 处理错误情况

            }
            registerBtn.onclick = (e) => {
                choose_audio_file(e, "registerFileSelector", "registerUsername", "/audioMatch/signUp");
            };

            loginBtn.onclick = (e) => {
                choose_audio_file(e, "loginFileSelector", "loginUsername", "/audioMatch/signIn");
            };

            downloadFileBtn.onclick = (e) => {
                let registerUserName = document.getElementById("registerUsername").value;
                console.log(registerUserName);
                const params = new URLSearchParams({
                    name: registerUserName,
                });
                fetch("/audioMatch/downloadEncryptedVoice", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: params.toString()
                }).then(response => {
                    if (response.status === 500) {
                        // Handle server error
                        return response.text().then(failedMsg => {
                            alert(failedMsg);
                            throw new Error(failedMsg); // Throw an error to stop further processing
                        });
                    }
                    return response.blob();
                }) .then (blob => {
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = registerUserName + '_encrypted_voice_template.txt';
                    a.click();
                    window.URL.revokeObjectURL(url);
                }).catch(error => console.error('Error:', error));

            }
        }

        function originalSubmit() {
            let file = document.getElementById("fileUpload").files[0];
            if (!file) {
                alert("未选择待脱敏文件");
                return;
            }
            // 清空after
            let after = document.getElementById("after");
            after.innerHTML = "";
            // 构建formdata
            let formData = new FormData();
            formData.set("file", file);
            //formData.append("params", param);
            let idx = $("ul .active").index();
            console.log(idx);
            let privacyLevelTable = document.getElementById("privacyLevel");
            let privacyLevel = document.getElementById("distortionaudio_privacyLevel").value;
            if (idx === 0) {
                let distortionAudioAlgName = document.getElementById("distortionAudioAlgName").value;
                switch (distortionAudioAlgName) {
                    case "voice_replace":
                        formData.set("sheet", distortionAudioAlgName);
                        formData.set("params", "1");
                        formData.set("algName", distortionAudioAlgName);
                        privacyLevelTable.style.display = "none";
                        break;

                    default:
                        formData.set("sheet", distortionAudioAlgName);
                        formData.set("params", privacyLevel);
                        formData.set("algName", distortionAudioAlgName);
                        privacyLevelTable.style.display = "block";
                        break;
                }
            }
            console.log(formData)
            fetch('/File/desenFile', {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.status === 500) {
                        // Handle server error
                        return response.text().then(failedMsg => {
                            alert(failedMsg);
                            throw new Error(failedMsg); // Throw an error to stop further processing
                        });
                    }
                    return response.blob();
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

        function chooseFile(event) {
            // 清空
            document.getElementById("pre").innerHTML = "";
            document.getElementById("after").innerHTML = "";

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
                    // 文件名，扩展名
                    let fileInfo = "<div  style=\"font-size: 20px; text-align: center\"> <span>" +
                        "<strong>" + fileName + "文件</strong>上传成功" + "</span>" + "</div>";
                    document.getElementById("fileInfo").innerHTML = fileInfo;
                    const fileExtension = fileName.split(".").pop().toLowerCase();
                    displayAudio()
                } else {
                    alert("请选择音频文件");
                }
            }
        }

        function displayAudio() {
            let input = document.getElementById("fileUpload");
            let pre = document.getElementById("pre");
            let file = input.files[0];
            let reader = new FileReader();
            reader.onload = function (e) {
                // 创建一个新的audio元素
                let audioElement = document.createElement("audio");
                audioElement.src = e.target.result; // 设置audio的源为FileReader的结果
                audioElement.controls = true; // 显示音频控制器
                // 检查`pre`元素是否已定义
                if (pre) {
                    pre.appendChild(audioElement); // 将audio元素添加到`pre`元素中
                } else {
                    console.error("`pre`元素未找到.");
                }
            };
            reader.readAsDataURL(file);
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
                <ul id="tab-type" class="nav nav-tabs" style="left: 50%; font-size: 20px;">
                    <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">失真</a>
                    </li>
                    <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">非失真</a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div id="tab-1" class="tab-pane active" style="text-align: center;">
                        <div>
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th>请选择音频失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody id="table1">
                                <tr>
                                    <td><select id="distortionAudioAlgName">
                                            <option value="dpAudio">差分-基于差分隐私的声纹特征脱敏算法
                                            </option>
                                            <option value="voice_replace">置换-声纹替换算法</option>
                                            <option value="apply_audio_effects">置换-音频变形</option>
                                            <option value="audio_reshuffle">置换-音频重排</option>
                                            <option value="audio_floor">泛化-音频取整</option>
                                            <option value="audio_spec">泛化-频域遮掩</option>
                                            <option value="audio_augmentation">泛化-音频失真</option>
                                            <option value="audio_median">泛化-基于均值的采样点替换</option>
                                            <#--                                           <option value="add_beep"> 基于正弦波的音频替换方法</option>-->
                                        </select></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="ibox-content" id="privacyLevel" style="text-align: center; ">
                            <div>
                                <table style="margin: auto; font-size: 20px">
                                    <thead>
                                    <tr>
                                        <th>请选择隐私保护等级</th>
                                    </tr>
                                    </thead>
                                    <tbody id="table3">
                                    <tr>
                                        <td><select id="distortionaudio_privacyLevel">
                                                <option value="0"> 低程度</option>
                                                <option value="1" selected> 中程度</option>
                                                <option value="2"> 高程度</option>
                                            </select></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
<#--                        <div &lt;#&ndash;class="ibox-content"&ndash;&gt; id="audioTime" style="text-align: center; display: none">-->
<#--                            <div style=" font-size: 20px">-->
<#--                                <form>-->
<#--                                    <label for="start">开始时间:</label>-->
<#--                                    <input type="text" id="start" name="input1" placeholder="单位：秒">-->
<#--                                    <label for="chixu">持续时间:</label>-->
<#--                                    <input type="text" id="chixu" name="input2" placeholder="单位：秒">-->
<#--                                </form>-->
<#--                            </div>-->
<#--                        </div>-->

                        <div class="midtile">
                            <div class="col-sm-5 m-b-xs">
                                <form id="uploadForm" action="/upload" method="post" enctype="multipart/form-data">
                                    <input type="file" id="fileUpload" accept=".mp3, .wav" style="display: none;">
                                    <label for="fileUpload" class="upload-btn">
                                        选择文件
                                    </label>
                                </form>
                            </div>
                        </div>
                        <div id="fileInfo"></div>

                        <div class="showFile">
                            <!--前后文件-->
                            <div id=pre>
                            </div>
                            <div id=after>
                            </div>
                        </div>

                        <div class="btn2">
                            <button type="button" class="btn btn-sm btn-primary" id="submit"> 提交脱敏</button>
                            <button type="button" class="btn btn-sm btn-primary m-l" id="setDefaultAlgorithm"> 设置当前算法为默认算法</button>

                        </div>
                    </div>
                    <div id="tab-2" class="tab-pane" style="text-align: center;">
                        <div>
                            <table style="margin: auto; font-size: 20px">
                                <thead>
                                <tr>
                                    <th>请选择音频非失真脱敏算法</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>
                                        <select id="audio-nondistortion">
                                            <option value="voice-similarity" selected> 密文声纹相似度计算</option>
                                        </select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="audio-match" class="content-container" style="visibility: visible">
                            <div class="section" id="registration">
                                <h2>用户注册</h2>
                                <div class="section-box">
                                    <label for="username">用户名:</label>
                                    <input type="text" id="registerUsername" name="username">
                                    <label for="fileUpload">选择文件:</label>
                                    <input type="text" id="registerFilePath" placeholder="文件路径" readonly>
                                    <input type="file" id="registerFileSelector" accept=".mp3, .wav"
                                           style="display:none"
                                           onchange="document.getElementById('registerFilePath').value = this.value">
                                    <label for="registrationMessage">消息:</label>
                                    <textarea id="registrationMessage" rows="4" readonly></textarea>
                                    <div class="buttons">
                                        <button type="button" id="register-button">注册</button>
                                        <button type="button" class="audio-match-button"
                                                onclick="document.getElementById('registerFileSelector').click();">选择文件
                                        </button>

                                        <button class="m-l" type="button" id="download-encrypted-voice">下载加密后的声纹模板</button>
                                    </div>

                                </div>
                            </div>
                            <div class="section" id="login">
                                <h2>用户登录</h2>
                                <div class="section-box">
                                    <label for="loginUsername">用户名:</label>
                                    <input type="text" id="loginUsername" name="loginUsername">
                                    <label for="loginFilePath">选择文件:</label>
                                    <input type="text" id="loginFilePath" placeholder="文件路径" readonly>
                                    <input type="file" id="loginFileSelector" accept=".mp3, .wav" style="display:none"
                                           onchange="document.getElementById('loginFilePath').value = this.value">
                                    <label for="loginMessage">消息:</label>
                                    <textarea id="loginMessage" rows="4" readonly></textarea>
                                    <div class="buttons">
                                        <button type="button" id="login-button">登录</button>
                                        <button type="button" class="audio-match-button"
                                                onclick="document.getElementById('loginFileSelector').click();">选择文件
                                        </button>
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

<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function () {

    });



</script>
</body>

</html>
