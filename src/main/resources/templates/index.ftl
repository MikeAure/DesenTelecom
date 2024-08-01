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
    <link href="${ctx!}/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
    <link rel="shortcut icon" href="${ctx!}/favicon.ico">


</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
<div id="wrapper">
    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="nav-close"><i class="fa fa-times-circle"></i>
        </div>
        <div class="sidebar-collapse">
            <ul class="nav" id="side-menu">
                <#--脱敏算法验证-->
                <li>
                    <a href="#" style="font-size: 20px;">
                        <i class="fa fa fa-home"></i>
                        <span class="nav-label" style="font-size: 20px;">脱敏算法验证</span>
                        <span class="fa arrow"></span>
                    </a>

                    <ul class="nav nav-second-level">

                        <li>
                            <a class="J_menuItem" href="${ctx!}/verify/text" style="font-size: 15px;">文本</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/verify/excel" style="font-size: 15px;">表格</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/verify/image" style="font-size: 15px;">图像</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/verify/video" style="font-size: 15px;">视频</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/verify/audio" style="font-size: 15px;">音频</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/verify/graph" style="font-size: 15px;">图形</a>
                        </li>
                    </ul>
                </li>
                <#--50个应用场景-->
                <li>
                    <a href="#" style="font-size: 20px;">
                        <i class="fa fa fa-cog"></i>
                        <span class="nav-label" style="font-size: 20px;">典型场景验证</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/map" style="font-size: 15px;">地图导航类</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/onlinetaxi"
                               style="font-size: 15px;">网络约车类</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/communication" style="font-size: 15px;">即时通信类</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/community"
                               style="font-size: 15px;">网络社区类</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/onlinepayment" style="font-size: 15px;">网络支付类</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/onlineshopping" style="font-size: 15px;">网上购物类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/takeaway"
                               style="font-size: 15px;">餐饮外卖类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/express"
                               style="font-size: 15px;">邮件快件寄递场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/transportationticket"
                               style="font-size: 15px;">交通票务场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/marry"
                               style="font-size: 15px;">婚恋相亲场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/employment"
                               style="font-size: 15px;">求职招聘场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/onlinelending" style="font-size: 15px;">网络借贷场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/house"
                               style="font-size: 15px;">房屋租售场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/usedcar"
                               style="font-size: 15px;">二手车交易场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/consultation" style="font-size: 15px;">问诊挂号场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/travel"
                               style="font-size: 15px;">旅游服务类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/hotel"
                               style="font-size: 15px;">酒店服务类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/game"
                               style="font-size: 15px;">网络游戏类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/education"
                               style="font-size: 15px;">学习教育类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/locallife"
                               style="font-size: 15px;">本地生活类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/woman"
                               style="font-size: 15px;">女性健康类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/usecar"
                               style="font-size: 15px;">用车服务类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/investment"
                               style="font-size: 15px;">投资理财类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/bank"
                               style="font-size: 15px;">手机银行类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/mailbox"
                               style="font-size: 15px;">邮箱云盘类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/meeting"
                               style="font-size: 15px;">远程会议类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/webcast"
                               style="font-size: 15px;">网络直播类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/onlinemovie" style="font-size: 15px;">在线影音类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/shortvideo"
                               style="font-size: 15px;">短视频类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/news"
                               style="font-size: 15px;">新闻资讯类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/sports"
                               style="font-size: 15px;">运动健身类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/browser"
                               style="font-size: 15px;">浏览器类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/input"
                               style="font-size: 15px;">输入法类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/security"
                               style="font-size: 15px;">安全管理类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/ebook"
                               style="font-size: 15px;">电子图书场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/capture"
                               style="font-size: 15px;">拍摄美化场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/appstore"
                               style="font-size: 15px;">应用商店场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/tools"
                               style="font-size: 15px;">实用工具场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/performanceticket" style="font-size: 15px;">演出票务场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/networkaccess" style="font-size: 15px;">电话/有线电视入网类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/telecommunication" style="font-size: 15px;">电信业务使用类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/monitor"
                               style="font-size: 15px;">安防监控类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/pay"
                               style="font-size: 15px;">生活缴费类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/customerservice" style="font-size: 15px;">客服类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/schoolservice" style="font-size: 15px;">校园服务类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/smarthome"
                               style="font-size: 15px;">智慧家居类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/autonomousdriving" style="font-size: 15px;">自动驾驶类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/telemedicine" style="font-size: 15px;">远程诊疗类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/vr"
                               style="font-size: 15px;">虚拟现实类场景</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/onlinevoting" style="font-size: 15px;">网上投票类场景</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/fifty_scene/telecomclient" style="font-size: 15px;">电信客户信息数据</a>
                        </li>

<#--                        <li>-->
<#--                            <a class="J_menuItem" href="${ctx!}/fifty_scene/onlinetaxi2" style="font-size: 15px;">网上打车场景（接收数据）</a>-->
<#--                        </li>-->

                    </ul>
                </li>
                <#--脱敏工具设置-->
                <li>
                    <a href="#" style="font-size: 20px;">
                        <i class="fa fa fa-cog"></i>
                        <span class="nav-label" style="font-size: 20px;">脱敏工具设置</span>
                        <span class="fa arrow"></span>
                    </a>
                    <#--模态分类-->
                    <#--<ul class="nav nav-second-level">
                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/distortiontext" style="font-size: 15px;">失真文本脱敏工具</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/distortionimage" style="font-size: 15px;">失真图像脱敏工具</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/distortionvideo" style="font-size: 15px;">失真视频脱敏工具</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/distortionaudio" style="font-size: 15px;">失真音频脱敏工具</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/distortiongraph" style="font-size: 15px;">失真图形脱敏工具</a>
                        </li>
                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/nodistortiontext" style="font-size: 15px;">非失真文本脱敏工具</a>
                        </li>


                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/nodistortionimage" style="font-size: 15px;">非失真图像脱敏工具</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/nodistortionvideo" style="font-size: 15px;">非失真视频脱敏工具</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/nodistortionaudio" style="font-size: 15px;">非失真音频脱敏工具</a>
                        </li>

                        <li>
                            <a class="J_menuItem" href="${ctx!}/desentools/nodistortiongraph" style="font-size: 15px;">非失真图形脱敏工具</a>
                        </li>

                    </ul>-->
                    <ul class="nav nav-second-level">
                        <li class="active">
                            <a href="#" style="font-size: 15px;">
                                差分隐私
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-third-level collapse in" aria-expanded="true" style="font-size: 13px;">
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/differential_privacy_laplace">
                                        拉普拉斯 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/differential_privacy_exp"> 指数 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/differential_privacy_svt"> SVT
                                        稀疏向量 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/differential_privacy_ldp">
                                        随机响应 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/differential_privacy_random_noise">
                                        随机加噪 </a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#" style="font-size: 15px;">
                                数据泛化
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-third-level collapse in" aria-expanded="true" style="font-size: 13px;">
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/generalization_num_string">
                                        数值与字符泛化 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/generalization_location">
                                        位置数据泛化 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/generalization_image"> 图像泛化 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/generalization_video"> 视频泛化 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/generalization_audio"> 音频泛化 </a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#" style="font-size: 15px;">
                                匿名
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-third-level collapse in" aria-expanded="true" style="font-size: 13px;">
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/k_anonymity_alg"> K 匿名 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/l_diversity_alg"> L 多样性 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/t_closeness_alg"> T 接近 </a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#" style="font-size: 15px;">
                                数据置换
                                <span class="fa arrow"></span>
                            </a>
                            <ul class="nav nav-third-level collapse in" , aria-expanded="true" style="font-size: 13px;">
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/replacement_num_string">
                                        数值与字符置换 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/replacement_image"> 图像置换 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/replacement_video"> 视频置换 </a>
                                </li>
                                <li>
                                    <a class="J_menuItem" href="${ctx!}/desentools/replacement_audio"> 音频置换 </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
                <li>
                    <a class="J_menuItem" href="${ctx!}/templateManagement" style="font-size: 20px;">
                        <i class="fa fa fa-cog"></i>
                        <span class="nav-label">应用场景模板管理</span>

                    </a>
                </li>
            </ul>
        </div>
    </nav>
    <!--左侧导航结束-->
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row J_mainContent" id="content-main">
            <iframe id="J_iframe" width="100%" height="100%" src="${ctx!}/home" data-id="index_v1.html"
                    seamless></iframe>
        </div>

    </div>
    <!--右侧部分结束-->
</div>

<!-- 全局js -->
<script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx!}/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="${ctx!}/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="${ctx!}/js/plugins/layer/layer.min.js"></script>

<!-- 自定义js -->
<script src="${ctx!}/js/hAdmin.js?v=4.1.0"></script>
<script type="text/javascript" src="${ctx!}/js/index.js"></script>
</body>
<style>
    .navbar-static-side {
        background-color: #1f2937;
    }

</style>

</html>
