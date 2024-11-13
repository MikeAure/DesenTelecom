// 初始化地图
function initMap(containerId, options) {
    return new AMap.Map(containerId, {
        center: options.center, // 地图中心点
        zoom: options.zoom // 地图显示的缩放级别
    });
}

// 添加可拖动的Marker
function addDraggableMarker(map, position, markerId, iconUrl) {
    var marker = new AMap.Marker({
        position: position,
        draggable: true,
        cursor: 'move',
        raiseOnDrag: true,
        map: map,
        anchor: "bottom-center"
    });

    if (iconUrl !== 'Default') {
        const icon = new AMap.Icon({
            image: iconUrl,
            imageOffset: new AMap.Pixel(-0.5, 0)
        })
        marker.setIcon(icon);
    }
    // 监听Marker拖动事件更新对应的经纬度显示
    marker.on('dragend', function (e) {
        document.getElementById(markerId).innerHTML = "<span>经度：" + e.lnglat.getLng() + ", 纬度：" + e.lnglat.getLat() + "</span>";
    });

    return marker;
}

// 锁定Marker
function lockMarkerDriver1(marker, buttonId) {
    document.getElementById(buttonId).addEventListener('click', function () {
        marker.setDraggable(false); // 设置Marker不可拖动
        var position = marker.getPosition();
        var latitude = position.lat; // 纬度
        var longitude = position.lng; // 经度
        driver1SendCoordinatesToBackend(latitude, longitude);
    });
}

function lockMarkerDriver2(marker, buttonId) {
    document.getElementById(buttonId).addEventListener('click', function () {
        marker.setDraggable(false); // 设置Marker不可拖动
        var position = marker.getPosition();
        var latitude = position.lat; // 纬度
        var longitude = position.lng; // 经度
        driver2SendCoordinatesToBackend(latitude, longitude);
    });
}

function lockMarkerCustomer(marker, buttonId, startOrEnd) {
    document.getElementById(buttonId).addEventListener('click', function () {
        marker.setDraggable(false); // 设置Marker不可拖动

        // 获取Marker的当前位置
        let position = marker.getPosition();
        let latitude = position.lat; // 纬度
        let longitude = position.lng; // 经度

        // // 根据是起点还是终点，准备发送请求
        // if (startOrEnd === 'start') {
        //     // 保存起点坐标
        //     window.customerStartLatitude = latitude;
        //     window.customerStartLongitude = longitude;
        // } else if (startOrEnd === 'end') {
        //     // 保存终点坐标
        //     window.customerEndLatitude = latitude;
        //     window.customerEndLongitude = longitude;
        // }
        //
        // // 如果起点和终点坐标都已经设置，则发送数据到后端
        // if (window.customerStartLatitude && window.customerStartLongitude && window.customerEndLatitude && window.customerEndLongitude) {
        //     sendCoordinatesToBackend(window.customerStartLatitude, window.customerStartLongitude, window.customerEndLatitude, window.customerEndLongitude);
        // }

        sendCoordinatesToBackend(latitude, longitude, startOrEnd);
    });
}

// 发送坐标到后端的函数
function sendCoordinatesToBackend(latitude, longitude, startOrEnd) {
    if (startOrEnd === 'start') {
        fetch('/Encrypt/customerSetStartCoordinate?startLatitude=' + latitude + '&startLongitude=' + longitude, {
            method: 'GET', // 或者 'POST' 如果后端期望POST请求
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                console.log(latitude, longitude);
            })
            .catch(error => console.error('Error:', error));
    } else if (startOrEnd === 'end') {
        fetch('/Encrypt/customerSetEndCoordinate?endLatitude=' + latitude + '&endLongitude=' + longitude, {
            method: 'GET', // 或者 'POST' 如果后端期望POST请求
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
            .then(response => response.json())
            .then(data => console.log(data))
            .catch(error => console.error('Error:', error));
    }

}

function driver1SendCoordinatesToBackend(startLatitude, startLongitude) {
    const logArea = document.getElementById('driver1LogArea');
    fetch('/Encrypt/traceDriver1Login?startLatitude=' + startLatitude + '&startLongitude=' + startLongitude, {
        method: 'GET', // 或者 'POST' 如果后端期望POST请求
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(response => response.json())
        .then(data => {

            if (data.status === "ok") {
                logArea.value += 'driver1发送位置信息成功\n';
            } else if (data.status === "error") {
                logArea.value += 'driver1发送位置信息失败\n';
            }
        })
        .catch(error => console.error('Error:', error));
}

function driver2SendCoordinatesToBackend(startLatitude, startLongitude) {
    const logArea = document.getElementById('driver2LogArea');
    fetch('/Encrypt/traceDriver2Login?startLatitude=' + startLatitude + '&startLongitude=' + startLongitude, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(response => response.json())
        .then(data => {

            if (data.status === "ok") {
                logArea.value += 'driver2发送位置信息成功\n';
            } else if (data.status === "error") {
                logArea.value += 'driver2发送位置信息失败\n';
            }
        })
        .catch(error => console.error('Error:', error));
}

// 初始化乘客地图
var customerMap = initMap('customerMap', {center: [108.945, 34.285], zoom: 11});
customerMap.setLimitBounds(new AMap.Bounds([108.77, 34.14], [109.12, 34.43]));
var startMarker = addDraggableMarker(customerMap, [108.945, 34.285], 'startCoord', 'http://webapi.amap.com/theme/v1.3/markers/n/start.png');
var endMarker = addDraggableMarker(customerMap, [108.946, 34.285], 'endCoord', 'http://webapi.amap.com/theme/v1.3/markers/n/end.png');
// 修改初始化Marker的调用
lockMarkerCustomer(startMarker, 'lockStart', 'start');
lockMarkerCustomer(endMarker, 'lockEnd', 'end');

// 初始化司机地图1
var driverMap1 = initMap('driverMap1', {center: [108.945, 34.285], zoom: 11});
driverMap1.setLimitBounds(new AMap.Bounds([108.77, 34.14], [109.12, 34.43]))
var driver1Marker = addDraggableMarker(driverMap1, [108.945, 34.285], 'driver1Coord', 'Default');
lockMarkerDriver1(driver1Marker, 'lockDriver1');

// 初始化司机地图2
var driverMap2 = initMap('driverMap2', {center: [108.945, 34.285], zoom: 11});
driverMap2.setLimitBounds(new AMap.Bounds([108.77, 34.14], [109.12, 34.43]));
var driver2Marker = addDraggableMarker(driverMap2, [108.945, 34.285], 'driver2Coord', 'Default');
lockMarkerDriver2(driver2Marker, 'lockDriver2');

const startButton = document.getElementById('serverStart');
const stopButton = document.getElementById('serverStop');
const customerStopBtn = document.getElementById('customerStop');
const driver1StopBtn = document.getElementById('driver1Stop');
const driver2StopBtn = document.getElementById('driver2Stop');
const cusReqBtn = document.getElementById('sendRequest');

// 监听按钮点击事件
startButton.addEventListener('click', function () {
    const logArea = document.getElementById('serverLogArea');
    // 调用API
    fetch('/Encrypt/serverStart', {
        method: 'GET', // 或者是 'POST', 取决于API的要求
        // 这里可以添加请求头部等信息，如果需要的话
        headers: {
            'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
        },
    })
        .then(response => response.json()) // 处理JSON响应
        .then(data => {
            // 根据返回值更新日志区域

            if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                logArea.value += 'trace服务器启动成功\n'; // 添加一行日志
            } else if (data.status === 'error') {
                logArea.value += 'trace服务器启动失败\n'; // 添加一行日志
            }
        })
        .catch(error => {
            console.error('请求失败:', error);
            // 处理请求失败的情况
            logArea.value += 'trace服务器启动请求失败\n'; // 添加一行日志
        });
});

stopButton.addEventListener('click', function () {
    const logArea = document.getElementById('serverLogArea');
    // 调用API
    fetch('/Encrypt/serverStop', {
        method: 'GET', // 或者是 'POST', 取决于API的要求
        // 这里可以添加请求头部等信息，如果需要的话
        headers: {
            'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
        },
    })
        .then(response => response.json()) // 处理JSON响应
        .then(data => {
            // 根据返回值更新日志区域

            if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                logArea.value += 'trace服务器停止成功\n'; // 添加一行日志
            } else if (data.status === 'error') {
                logArea.value += 'trace服务器停止失败\n'; // 添加一行日志
            }
        })
        .catch(error => {
            console.error('请求失败:', error);
            // 处理请求失败的情况

            logArea.value += 'trace服务器启动请求失败\n'; // 添加一行日志
        });
});

customerStopBtn.addEventListener('click', function () {
    const logArea = document.getElementById('customerLogArea');
    // 调用API
    fetch('/Encrypt/customerStop', {
        method: 'GET', // 或者是 'POST', 取决于API的要求
        // 这里可以添加请求头部等信息，如果需要的话
        headers: {
            'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
        },
    })
        .then(response => response.json()) // 处理JSON响应
        .then(data => {
            // 根据返回值更新日志区域

            if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                logArea.value += 'customer已停止\n'; // 添加一行日志
                startMarker.setDraggable(true);
                endMarker.setDraggable(true);
            } else if (data.status === 'error') {
                logArea.value += 'customer停止失败\n'; // 添加一行日志
            }
        })
        .catch(error => {
            console.error('请求失败:', error);
            // 处理请求失败的情况
            logArea.value += 'customer停止请求失败\n'; // 添加一行日志
        });
});

driver1StopBtn.addEventListener('click', function () {
    const logArea = document.getElementById('driver1LogArea');
    // 调用API
    fetch('/Encrypt/driver1Stop', {
        method: 'GET', // 或者是 'POST', 取决于API的要求
        // 这里可以添加请求头部等信息，如果需要的话
        headers: {
            'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
        },
    })
        .then(response => response.json()) // 处理JSON响应
        .then(data => {
            // 根据返回值更新日志区域

            if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                logArea.value += 'driver1已停止\n'; // 添加一行日志
                driver1Marker.setDraggable(true);
            } else if (data.status === 'error') {
                logArea.value += 'driver1停止失败\n'; // 添加一行日志
            }
        })
        .catch(error => {
            console.error('请求失败:', error);
            // 处理请求失败的情况

            logArea.value += 'driver1停止请求失败\n'; // 添加一行日志
        });
});
driver2StopBtn.addEventListener('click', function () {
    const logArea = document.getElementById('driver2LogArea');
    // 调用API
    fetch('/Encrypt/driver2Stop', {
        method: 'GET', // 或者是 'POST', 取决于API的要求
        // 这里可以添加请求头部等信息，如果需要的话
        headers: {
            'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
        },
    })
        .then(response => response.json()) // 处理JSON响应
        .then(data => {
            // 根据返回值更新日志区域

            if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                logArea.value += 'driver2已停止\n'; // 添加一行日志
                driver2Marker.setDraggable(true);
            } else if (data.status === 'error') {
                logArea.value += 'driver2停止失败\n'; // 添加一行日志
            }
        })
        .catch(error => {
            console.error('请求失败:', error);
            // 处理请求失败的情况
            logArea.value += 'driver2停止请求失败\n'; // 添加一行日志
        });
});

cusReqBtn.addEventListener('click', function () {
    const logArea = document.getElementById('customerLogArea');
    // 调用API
    fetch('/Encrypt/traceCustomerLogin', {
        method: 'GET', // 或者是 'POST', 取决于API的要求
        // 这里可以添加请求头部等信息，如果需要的话
        headers: {
            'Content-Type': 'application/json', // 假设API期望JSON格式的请求头
        },
    })
        .then(response => response.json()) // 处理JSON响应
        .then(data => {
            // 根据返回值更新日志区域

            if (data.status === 'ok') { // 假设返回的JSON对象包含一个名为status的字段
                logArea.value += "customer成功发送打车请求\n";
                logArea.scrollTop = logArea.scrollHeight;

            } else if (data.status === 'error') {
                logArea.value += "customer发送打车请求失败\n"; // 添加一行日志
            }
        })
        .catch(error => {
            console.error('请求失败:', error);
            // 处理请求失败的情况

            logArea.value += "customer发送打车请求失败\n"; // 添加一行日志
        });
});

let pollingInterval; // 移到外面以便控制开始和停止
let counter = 0; // 计数器也应该在外面以保持状态

function pollServerForStatus() {
    if (!pollingInterval) { // 确保轮询只被启动一次
        pollingInterval = setInterval(() => {
            fetch('/Encrypt/orderResult')
                .then(response => response.json())
                .then(data => {
                    let customerLogArea = document.getElementById('customerLogArea');
                    if (data.status === 'ok') {
                        clearInterval(pollingInterval);
                        pollingInterval = null; // 重置轮询间隔控制变量
                        customerLogArea.value += data.message + "\n";
                    } else if (data.status === "error") {
                        if (counter >= 5) {
                            clearInterval(pollingInterval);
                            pollingInterval = null; // 重置轮询间隔控制变量
                            customerLogArea.value += "寻找司机失败" + "\n";
                            counter = 0;
                        } else {
                            customerLogArea.value += data.message + "\n";
                            counter++;
                        }
                    }
                })
                .catch(error => console.error('Error polling server:', error));
        }, 2000);
    }
}

cusReqBtn.addEventListener("click", pollServerForStatus);