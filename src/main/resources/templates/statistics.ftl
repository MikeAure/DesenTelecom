<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>统计展示</title>
    <link href="${ctx!}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx!}/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="${ctx!}/css/style.css?v=4.1.0" rel="stylesheet">
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content">
    <div class="row animated fadeInDown">
        <div class="col-sm-3">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <strong>选择要对比的数据</strong>
                </div>
                <div class="ibox-content">
                    <form method="get" class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">数据表：</label>
                            <div class="col-sm-8">
                                <select class="form-control" id="tableSelecter">
                                    <option value="-1" selected>请选择数据表</option>
                                    <option value="hotel">旅店住宿表</option>
                                    <option value="huji">户籍信息表</option>
                                    <option value="cheguan">车管信息表</option>
                                    <option value="churujing">出入境列表</option>
                                </select>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">数据类型：</label>
                            <div class="col-sm-8">
                                <select class="form-control" id="typeSelecter">
                                    <option value="-1" selected>请选择数据类型</option>
                                    <option value="0">数值类型数据</option>
                                    <option value="1">单意义编码型</option>
                                    <!-- <#--<option value="4">时间类型数据</option>--> -->
                                </select>
                            </div>
                        </div>
                        <div class="hr-line-dashed" id="typeDash" style=""></div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">数据字段:</label>
                            <div class="col-sm-8">
                                <select class="form-control" multiple id="keySelecter">
                                </select>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">展示类型：</label>
                            <div class="col-sm-8">
                                <select class="form-control" id="showSelecter">
                                    <option id="showType" value="-1">请选择展示类型</option>
                                </select>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-1 col-sm-offset-4">
                                <button class="btn btn-primary" id="submit">确定</button>
                            </div>
                            <div class="col-sm-1 col-sm-offset-1">
                                <button class="btn btn-white" id="cancel">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-sm-8">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5 id="chartTitle">对比结果展示</h5>
                </div>
                <div class="ibox-content">
                    <div id="chartBody"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 全局js -->
<script src="${ctx!}/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/js/plugins/chosen/chosen.jquery.js"></script>
<script src="${ctx!}/js/bootstrap.min.js"></script>
<script src="${ctx!}/js/echarts.min.js"></script>

<script type="text/javascript">
    // ***************************获取页面元素*******************************
    $(function () {
        // 数据表
        var tableSelecter = $("#tableSelecter")[0]
        // 数据类型
        var typeSelecter = $("#typeSelecter")[0]
        // 数据字段选择
        var keySelecter = $("#keySelecter")[0]
        // 展示类型
        var showSelecter = $("#showSelecter")[0]
        // 确定按钮
        var submitBtn = $("#submit")[0]
        // 取消按钮
        var cancelBtn = $("#cancel")[0]
        // 图表名称
        var chartTitle = $("#chartTitle")[0]
        // 图表
        var chartBody = $("#chartBody")[0]
        // 定义全局变量
        // var res = []
        // *********************************************************************
        // 初始化keySelecter，展示的字段
        $("#keySelecter").chosen({
            width: "100%",
            //设置最大选择数量
            max_selected_options: 4,
            no_results_text: "没有找到",
            disable_search_threshold: 4,
            single_backstroke_delete: false,
            placeholder_text_multiple: "   请选择至多四个数据字段"
        })
        $(window).resize(function () {
            chartBody.style.height = window.innerHeight * 0.8 + "px"
        })
        chartBody.style.height = window.innerHeight * 0.8 + "px"
        // *****************************值映射**********************************
        // 用于生成图表名称
        tableName = {
            "hotel": "酒店住宿表",
            "huji": "户籍信息表",
            "cheguan": "车管信息表",
            "churujing": "出入境列表"
        }
        dataType = {
            "0": "数值类型数据",
            "1": "单意义编码型",
            "4": "时间类型数据"
        }
        showType = {
            "0": "平均数",
            "1": "方差",
            "2": "中位数",
            "3": "柱状图",
            "4": "折线图",
            "5": "散点图"
        }
        // *****************************绑定事件**********************************
        tableSelecter.onchange = function () {
            //每次数据表改变，重新获取所有数据
            //当数据表被选择时，重新填充数据字段选择器
            if (typeSelecter.value != -1) {
                fillKeySelecter()
            }
        }
        typeSelecter.onchange = function () {
            if (tableSelecter.value == -1) {
                typeSelecter.value = -1
                alert("请先选择数据表！")
            } else {
                fillKeySelecter()
                fillShowSelecter()
            }
        }
        submitBtn.onclick = function () {
            if (tableSelecter.value == -1) {
                alert('请选择数据表！')
                return false
            } else if (showSelecter.value == -1) {
                alert('请选择展示类型！')
                return false
            } else if ($(keySelecter).val() == null) {
                alert('请选择数据字段！')
                return false
            } else {
                // getQueryResult()
                // chartTitle.innerHTML = getChartName()
                chartDraw()
                return false
            }
        }
        cancelBtn.onclick = function () {
            tableSelecter.value = -1
            typeSelecter.value = -1
            $(keySelecter).empty()
            $(keySelecter).trigger("chosen:updated")
            $(showSelecter).empty()
            $(showSelecter).trigger("chosen:updated")
            $(showSelecter).append('<option value="-1">请选择展示类型</option>')
            return false
        }
        // ****************************方法实现**********************************
        // 填充数据字段
        var fillKeySelecter = function () {
            var str = ""
            var url = tableSelecter.value + "param/statisticParam?query=" + typeSelecter.value

            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'json',
                success: function (result) {
                    if (result.length == 0) {
                        alert(tableName[tableSelecter.value] + "中没有" + dataType[typeSelecter.value] + "字段，请重新选择！")
                        typeSelecter.value = -1
                    } else {
                        $(keySelecter).empty()
                        $.each(result, function (index, el) {
                            str = "<option value=" + el.filedName + ">" + el.columnName + "</option>"
                            $(keySelecter).append(str)
                        })
                        $(keySelecter).trigger("chosen:updated")
                    }
                }
            })
        }
        // 填充展示类型字段
        var fillShowSelecter = function () {
            switch (typeSelecter.value) {
                case '0':
                    $(showSelecter).empty()
                    $(showSelecter).append('<option value="-1" selected>请选择展示类型</option>')
                    $(showSelecter).append('<option value= "0">平均数</option>')
                    $(showSelecter).append('<option value= "1">方差</option>')
                    $(showSelecter).append('<option value= "2">中位数</option>')
                    $(keySelecter).trigger("chosen:updated")
                    break
                case '1':
                    $(showSelecter).empty()
                    $(showSelecter).append('<option value="-1" selected >请选择展示类型</option>')
                    $(showSelecter).append('<option value= "3">柱状图</option>')
                    $(showSelecter).append('<option value= "4">折线图</option>')
                    $(showSelecter).append('<option value= "5">散点图</option>')
                    $(showSelecter).append('<option value= "6">饼图</option>')
                    $(keySelecter).trigger("chosen:updated")
                    break
                case '4':
                    $(showSelecter).empty()
                    $(showSelecter).append('<option value="-1" selected>请选择展示类型</option>')
                    $(showSelecter).append('<option value= "3">柱状图</option>')
                    $(showSelecter).append('<option value= "4">折线图</option>')
                    $(showSelecter).append('<option value= "5">散点图</option>')
                    $(showSelecter).append('<option value= "6">饼图</option>')
                    $(keySelecter).trigger("chosen:updated")
            }
        }
        // 处理服务器返回的单意义编码数据
        var result_process = function (result) {
            var res = []
            var res_json = {}
            var tmp_val
            var tmp_pre
            var tmp_post
            var tmp_min
            var tmp_max

            $.each(result, function (index, el) {
                var tmp_json = {}
                res_json = {}
                tmp_val = []
                tmp_pre = []
                tmp_post = []

                $.each(el.tongMap, function (i, e) {
                    tmp_val.push(i)
                    tmp_pre.push(e.preNums)
                    tmp_post.push(e.postNums)
                })
                tmp_min = tmp_max = tmp_pre[0]
                $.each(tmp_pre.concat(tmp_post), function (index, el) {
                    if (el < tmp_min) {
                        tmp_min = el
                    }
                    if (el > tmp_max) {
                        tmp_max = el
                    }
                })
                tmp_json.val = tmp_val
                tmp_json.pre = tmp_pre
                tmp_json.post = tmp_post
                tmp_json.min = tmp_min
                tmp_json.max = tmp_max
                res_json.name = el.colName
                res_json.result = tmp_json
                res.push(res_json)
            })

            return res
        }
        // 得到查询结果
        var getQueryResult = function () {
            var res = []
            // 无BUG单条数据展示代码
            // var res_tmp = []
            var url = "/" + tableSelecter.value + "//statistic"

            switch (typeSelecter.value) {
                // 数值
                case '0':
                    $.each($(keySelecter).val(), function (index, el) {
                        url = "/" + tableSelecter.value + "//statistic"
                        // 无BUG单条数据展示代码
                        url += "/num?query1=" + el + "&query2=" + showSelecter.value
                        $.ajax({
                            url: url,
                            type: 'GET',
                            async: false,
                            dataType: 'json',
                            success: function (result) {
                                // res_tmp.push(result)
                                // 无BUG单条数据展示代码
                                res.push(result)
                            }
                        })
                    })
                    break
                // 单意义
                case '1':
                    url += "/single?query1="
                    $.each($(keySelecter).val(), function (index, el) {
                        url += el
                        url += "+"
                    })
                    url = url.substring(0, url.length - 1)
                    url += "&query2=3"
                    $.ajax({
                        url: url,
                        type: 'GET',
                        async: false,
                        dataType: 'json',
                        success: function (result) {
                            res = result_process(result)
                        }
                    })
                    break
                // 时间
                case '4':
                    alert("尚未实现！")
            }

            return res
        }
        // 生成图表数量对应的格子
        var getGrid = function (chartNumber) {
            var grid = []

            switch (chartNumber) {
                case 1:
                    grid = [{left: '7%', top: '7%', width: '86%', height: '86%'}]
                    break;
                case 2:
                    grid = [{left: '7%', top: '7%', width: '38%', height: '86%'},
                        {right: '7%', top: '7%', width: '38%', height: '86%'}
                    ]
                    break;
                case 3:
                    grid = [{left: '7%', top: '7%', width: '38%', height: '38%'},
                        {left: '7%', bottom: '7%', width: '38%', height: '38%'},
                        {right: '7%', top: '7%', width: '38%', height: '86%'}
                    ]
                    break;
                case 4:
                    grid = [{left: '7%', top: '7%', width: '38%', height: '38%'},
                        {left: '7%', bottom: '7%', width: '38%', height: '38%'},
                        {right: '7%', top: '7%', width: '38%', height: '38%'},
                        {right: '7%', bottom: '7%', width: '38%', height: '38%'}
                    ]
                    break;
            }

            return grid
        }
        /**
         * [getXAxis description]
         * @param  {[type]} queryResult [返回的查询结果，JSON格式]
         * @param  {[type]} showType   [表格类型，可选值 "3": "柱状图", "4": "折线图", "5": "散点图"]
         * @param  {[type]} chartNumber [表格数量，最大为4，数值型为1]
         * @param  {[type]} dataType    [数据类型，具体分为 "0": "数值类型数据", "1": "单意义编码型", "4": "时间类型数据"]
         * @return {[type]}             [数值型直接返回横坐标的filedName,其他类型返回JSON数组]
         */
        var getXAxis = function (queryResult, dataType, showType) {
            var xAxis = []
            // showType: // "0": "平均数", // "1": "方差", // "2": "中位数", // "3": "柱状图", // "4": "折线图", // "5": "散点图"
            // 数值类型
            if (dataType == 0) {
                var tmp = []
                for (i in queryResult) {
                    tmp.push(queryResult[i][0].colNname)
                }
                var x_tmp = {
                    gridIndex: 0,
                }
                x_tmp.data = tmp
                xAxis.push(x_tmp)
            } else if (dataType == 1) {
                // 散点图
                if (showType == 5) {
                    var tmp
                    for (i in queryResult) {
                        tmp = {}
                        tmp.gridIndex = i
                        tmp.max = Math.ceil(queryResult[i].result.max / 10) * 10
                        tmp.min = Math.floor(queryResult[i].result.min / 10) * 10
                        tmp.axisLabel = {}
                        xAxis.push(tmp)
                    }
                } else {
                    var tmp
                    for (i in queryResult) {
                        tmp = {}
                        tmp.gridIndex = i
                        tmp.data = queryResult[i].result.val
                        xAxis.push(tmp)
                    }
                }
            } else {
                console.log('时间类型数据未实现')
            }

            return xAxis
        }
        // 格式同getXAxis()
        var getYAxis = function (queryResult, dataType, showType) {
            var yAxis = []

            if (dataType == 0) {
                yAxis.push({gridIndex: 0})
            } else if (dataType == 1) {
                // 散点图
                if (showType == 5) {
                    for (i in queryResult) {
                        tmp = {}
                        tmp.gridIndex = i
                        tmp.min = Math.floor(queryResult[i].result.min / 10) * 10
                        tmp.max = Math.ceil(queryResult[i].result.max / 10) * 10
                        yAxis.push(tmp)
                    }
                } else {
                    var tmp
                    for (i in queryResult) {
                        tmp = {}
                        tmp.gridIndex = i
                        // tmp.min = Math.floor(queryResult[i].result.min / 10) * 10
                        tmp.min = 0
                        tmp.max = Math.ceil(queryResult[i].result.max / 10) * 10
                        yAxis.push(tmp)
                    }
                }
            } else {
                console.log("时间类型未实现")
                return null
            }

            return yAxis
        }
        /**
         * [getSeries description]
         * @param  {[type]} queryResult [返回的查询结果，JSON格式]
         * @param  {[type]} charNumber  [表格数量]
         * @param  {[type]} showType   [showSelecter.value]
         * @return {[type]}             [description]
         */
            //获取图像数据各部分的值以及图像类型（脱敏前后）
        var getSeries = function (queryResult, dataType, showType) {
                var series = []
                // 数值类型
                if (dataType == 0) {
                    var tmp_pre = []
                    var tmp_post = []
                    $.each(queryResult, function (index, el) {
                        tmp_pre.push(el[0].pre)
                        tmp_post.push(el[0].post)
                    })
                    var data_pre = []
                    var data_post = []
                    var data_tmp
                    $.each(tmp_pre, function (index, el) {
                        // 判断是否得0，如果都是零，则全置零
                        if (el == 0 && tmp_post[index] == 0) {
                            data_tmp = {}
                            data_tmp.value = el
                            data_tmp.pre = el
                            data_pre.push(data_tmp)
                            data_tmp = {}
                            data_tmp.value = el
                            data_tmp.post = tmp_post[index]
                            data_post.push(data_tmp)
                        } else {
                            data_tmp = {}
                            data_tmp.value = el
                            data_tmp.pre = el
                            data_pre.push(data_tmp)
                            data_tmp = {}
                            data_tmp.value = tmp_post[index]
                            data_tmp.post = tmp_post[index]
                            data_post.push(data_tmp)
                        }
                    })
                    series.push({
                        // 后期改动name
                        name: 'pre',
                        type: 'bar',
                        data: data_pre,
                        label: {
                            show: true, // 开启显示
                            position: 'top', // 在上方显示
                            labelLine: {
                                show: true,
                            },
                            color: 'black',
                            fontSize: 13,
                            formatter: function (params) {
                                return params.data.pre.toFixed(2)
                            }
                        }
                    })
                    series.push({
                        // 后期改动name
                        name: 'post',
                        type: 'bar',
                        data: data_post,
                        label: {
                            show: true, // 开启显示
                            position: 'top', // 在上方显示
                            color: 'black',
                            fontSize: 13,
                            formatter: function (params) {
                                return params.data.post.toFixed(2)
                            }
                        }
                    })
                } else {
                    // 单意义编码型数据
                    if (dataType == 1) {

                        // 散点图
                        if (showType == 5) {
                            var markLine_arr = []

                            for (i in queryResult) {
                                var min = Math.floor(queryResult[i].result.min / 10) * 10
                                var max = Math.ceil(queryResult[i].result.max / 10) * 10
                                var tmp_mark_line = {
                                    animation: false,
                                    label: {
                                        formatter: 'y = x',
                                        align: 'right'
                                    },
                                    lineStyle: {
                                        type: 'solid'
                                    },
                                    tooltip: {
                                        formatter: 'y = x'
                                    },
                                    data: [
                                        [{
                                            coord: [min, min],
                                            symbol: 'none'
                                        }, {
                                            coord: [max, max],
                                            symbol: 'none'
                                        }]
                                    ]
                                }
                                markLine_arr.push(tmp_mark_line)
                            }

                            for (i in queryResult) {
                                var group
                                var total = []
                                for (l in queryResult[i].result.pre) {
                                    group = []
                                    group.push(queryResult[i].result.pre[l])
                                    group.push(queryResult[i].result.post[l])
                                    total.push(group)
                                }
                                tmp = {}
                                tmp.name = queryResult[i].name
                                tmp.type = 'scatter'
                                tmp.data = total
                                tmp.xAxisIndex = i
                                tmp.yAxisIndex = i
                                tmp.markLine = markLine_arr[i]
                                series.push(tmp)
                            }
                        } else if (showType != 6) {
                            // 非饼图,即柱状图或者折线图
                            var tmp
                            var type
                            showType == 3 ? type = "bar" : type = "line"
                            for (i in queryResult) {
                                tmp = {}
                                tmp.name = 'pre'
                                tmp.type = type
                                tmp.data = queryResult[i].result.pre
                                tmp.xAxisIndex = i
                                tmp.yAxisIndex = i
                                series.push(tmp)
                                tmp = {}
                                tmp.name = 'post'
                                tmp.type = type
                                tmp.data = queryResult[i].result.post
                                tmp.xAxisIndex = i
                                tmp.yAxisIndex = i
                                series.push(tmp)
                            }
                        } else {
                            // 饼图
                            var tmp_series = []
                            var label = {}
                            label.show = false
                            $.each(queryResult, function (index, el) {
                                var tmp_series_json = {}
                                var tmp_preData_arr = []
                                var tmp_preData_json = {}
                                var tmp_postData_arr = []
                                var tmp_postData_json = {}
                                $.each(el.result.val, function (i, e) {
                                    tmp_preData_json = {}
                                    tmp_preData_json.name = e
                                    tmp_preData_json.value = el.result.pre[i]
                                    tmp_preData_arr.push(tmp_preData_json)
                                    tmp_postData_json = {}
                                    tmp_postData_json.name = e
                                    tmp_postData_json.value = el.result.post[i]
                                    tmp_postData_arr.push(tmp_postData_json)
                                })
                                tmp_series_json.type = 'pie'
                                tmp_series_json.radius = '75%'
                                tmp_series_json.center = ['50%', '50%']
                                tmp_series_json.data = tmp_preData_arr
                                tmp_series_json.label = label
                                tmp_series_json.left = 0
                                tmp_series_json.right = 0
                                tmp_series_json.top = 0
                                tmp_series_json.bottom = 0
                                tmp_series.push(tmp_series_json)
                                tmp_series_json = {}
                                tmp_series_json.type = 'pie'
                                tmp_series_json.radius = '75%'
                                tmp_series_json.center = ['50%', '50%']
                                tmp_series_json.data = tmp_postData_arr
                                tmp_series_json.label = label
                                tmp_series_json.left = 0
                                tmp_series_json.right = 0
                                tmp_series_json.top = 0
                                tmp_series_json.bottom = 0
                                tmp_series.push(tmp_series_json)
                            })
                            switch (tmp_series.length / 2) {
                                case 1:
                                    tmp_series[0].top = '3%'
                                    tmp_series[0].right = '50%'
                                    tmp_series[1].top = '3%'
                                    tmp_series[1].left = '50%'
                                    break;
                                case 2:
                                    tmp_series[0].top = '3%'
                                    tmp_series[0].right = '50%'
                                    tmp_series[0].bottom = '50%'
                                    tmp_series[1].top = '53%'
                                    tmp_series[1].right = '50%'
                                    tmp_series[2].top = '3%'
                                    tmp_series[2].left = '50%'
                                    tmp_series[2].bottom = '50%'
                                    tmp_series[3].top = '53%'
                                    tmp_series[3].left = '50%'
                                    break;
                                case 3:
                                    tmp_series[0].right = '66%'
                                    tmp_series[0].bottom = '50%'
                                    tmp_series[1].right = '66%'
                                    tmp_series[1].top = '53%'
                                    tmp_series[2].left = '33%'
                                    tmp_series[2].right = '33%'
                                    tmp_series[2].bottom = '50%'
                                    tmp_series[3].left = '33%'
                                    tmp_series[3].right = '33%'
                                    tmp_series[3].top = '53%'
                                    tmp_series[4].left = '66%'
                                    tmp_series[4].bottom = '50%'
                                    tmp_series[5].left = '66%'
                                    tmp_series[5].top = '53%'
                                    break;
                                default:
                                    tmp_series[0].right = '75%'
                                    tmp_series[0].bottom = '50%'
                                    tmp_series[1].left = '25%'
                                    tmp_series[1].right = '50%'
                                    tmp_series[1].bottom = '50%'
                                    tmp_series[2].left = '50%'
                                    tmp_series[2].right = '25%'
                                    tmp_series[2].bottom = '50%'
                                    tmp_series[3].left = '75%'
                                    tmp_series[3].bottom = '50%'
                                    tmp_series[4].right = '75%'
                                    tmp_series[4].top = '53%'
                                    tmp_series[5].left = '25%'
                                    tmp_series[5].right = '50%'
                                    tmp_series[5].top = '53%'
                                    tmp_series[6].left = '50%'
                                    tmp_series[6].right = '25%'
                                    tmp_series[6].top = '53%'
                                    tmp_series[7].left = '75%'
                                    tmp_series[7].top = '53%'
                                    break;
                            }
                            series = tmp_series
                        }
                    } else {
                        console.log('时间类型数据未实现')
                        return null
                    }
                }

                return series
            }

        // 前后 title
        var getTitle = function (queryResult, chartNumber, dataType, showType) {
            var title = []
            // 数值类型
            if (dataType == 0) {

                return title
            }
            var tmp_title = []
            var tmp_json = {}
            $.each(queryResult, function (index, el) {
                tmp_title.push(el.name)
            })
            // 饼状图
            if (showType == 6) {
                switch (chartNumber) {
                    case 1:
                        tmp_json.test = tmp_title[0]
                        tmp_json.left = 'center'
                        tmp_json.top = 20
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.left = '23%'
                        tmp_json.bottom = '15%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.right = '23%'
                        tmp_json.bottom = '15%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        break;
                    case 2:
                        tmp_json.text = tmp_title[0]
                        tmp_json.left = '25%'
                        tmp_json.top = 20
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.left = '23%'
                        tmp_json.bottom = '50%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.left = '23%'
                        tmp_json.bottom = '2%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[1]
                        tmp_json.left = '73%'
                        tmp_json.top = 20
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.right = '23%'
                        tmp_json.bottom = '50%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.right = '23%'
                        tmp_json.bottom = '2%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        break;
                    case 3:
                        tmp_json.text = tmp_title[0]
                        tmp_json.left = '16%'
                        tmp_json.top = 20
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.left = '16%'
                        tmp_json.bottom = '50%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.left = '16%'
                        tmp_json.bottom = '2%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[1]
                        tmp_json.left = '49%'
                        tmp_json.top = 20
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.left = '49%'
                        tmp_json.bottom = '50%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.left = '49%'
                        tmp_json.bottom = '2%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[2]
                        tmp_json.right = '10%'
                        tmp_json.top = 20
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.right = '16%'
                        tmp_json.bottom = '50%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.right = '16%'
                        tmp_json.bottom = '2%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        break;
                    default:
                        tmp_json.text = tmp_title[0]
                        tmp_json.left = '25%'
                        tmp_json.top = 20
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.left = '12%'
                        tmp_json.bottom = '50%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.left = '37%'
                        tmp_json.bottom = '50%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[1]
                        tmp_json.left = '75%'
                        tmp_json.top = 20
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.right = '37%'
                        tmp_json.bottom = '50%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.right = '12%'
                        tmp_json.bottom = '50%'
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[2]
                        tmp_json.left = '25%'
                        tmp_json.top = '53%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.left = '12%'
                        tmp_json.bottom = 30
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.left = '37%'
                        tmp_json.bottom = 30
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[3]
                        tmp_json.left = '75%'
                        tmp_json.top = '53%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'pre'
                        tmp_json.left = '62%'
                        tmp_json.bottom = 30
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.subtext = 'post'
                        tmp_json.right = '12%'
                        tmp_json.bottom = 30
                        tmp_json.textLlign = 'center'
                        title.push(tmp_json)
                        break;
                }
            } else {
                switch (chartNumber) {
                    case 1:
                        break;
                    case 2:
                        tmp_json.text = tmp_title[0]
                        tmp_json.left = '25%'
                        tmp_json.top = '1%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[1]
                        tmp_json.left = '73%'
                        tmp_json.top = '1%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        break;
                    case 3:
                        tmp_json.text = tmp_title[0]
                        tmp_json.left = '25%'
                        tmp_json.top = '1%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[1]
                        tmp_json.left = '25%'
                        tmp_json.top = '50%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[2]
                        tmp_json.left = '73%'
                        tmp_json.top = '1%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        break;
                    default:
                        tmp_json.text = tmp_title[0]
                        tmp_json.left = '25%'
                        tmp_json.top = '1%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[1]
                        tmp_json.left = '25%'
                        tmp_json.top = '50%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[2]
                        tmp_json.left = '73%'
                        tmp_json.top = '1%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        tmp_json = {}
                        tmp_json.text = tmp_title[3]
                        tmp_json.left = '73%'
                        tmp_json.top = '50%'
                        tmp_json.textAlign = 'center'
                        title.push(tmp_json)
                        break;
                }
            }

            return title
        }
        // ****************************画图*************************************
        var chartDraw = function () {
            var myChart = echarts.init(document.getElementById("chartBody"))
            // 动态调整画布大小
            $(window).resize(myChart.resize)
            var option = {}
            var res = getQueryResult()
            var chartNumber = typeSelecter.value == 0 ? 1 : $(keySelecter).val().length
            console.log($(keySelecter).val())
            var title = getTitle(res, chartNumber, typeSelecter.value, showSelecter.value)
            var series = getSeries(res, typeSelecter.value, showSelecter.value)
            if (showSelecter.value == 6) {
                option = {
                    title: title,
                    tooltip: {
                        trigger: 'item',
                        formatter: function (params) {
                            var res = ""
                            var pre_value = 0
                            var tmp_change
                            var now_name = params.name
                            var now_value = params.value
                            var now_index = params.seriesIndex

                            if (now_index % 2 == 1) {
                                $.each(series[now_index - 1].data, function (i, e) {
                                    if (e.name == now_name) {
                                        pre_value = e.value
                                    }
                                })
                                tmp_change = ((now_value - pre_value) / pre_value * 100).toFixed(2)
                                res += "name:  "
                                res += now_name
                                res += "<br>"
                                res += "count:  "
                                res += now_value
                                res += "<br>"
                                res += "change:  "
                                if (tmp_change > 0) res += '+'
                                res += tmp_change
                                res += "%"
                                return res
                            }
                            res += "name:  "
                            res += now_name
                            res += "<br>"
                            res += "count:  "
                            res += now_value
                            return res
                        }
                    },
                    series: series
                }
            } else {
                var grid = getGrid(chartNumber)
                var xAxis = getXAxis(res, typeSelecter.value, showSelecter.value)
                var yAxis = getYAxis(res, typeSelecter.value, showSelecter.value)

                option = {
                    legend: {
                        data: ['pre', 'post']
                    },
                    grid: grid,
                    title: title,
                    xAxis: xAxis,
                    yAxis: yAxis,
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    series: series
                }
            }
            myChart.clear()
            myChart.setOption(option)
        }
    })
</script>
</body>
<style>
    .btn-primary {
        background-color: #347aa9;
    }

</style>
