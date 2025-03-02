// ��ʼ����ͼ
function initMap(containerId, options) {
    return new AMap.Map(containerId, {
        center: options.center, // ��ͼ���ĵ�
        zoom: options.zoom // ��ͼ��ʾ�����ż���
    });
}

// ��ӿ��϶���Marker
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
    // ����Marker�϶��¼����¶�Ӧ�ľ�γ����ʾ
    marker.on('dragend', function (e) {
        document.getElementById(markerId).innerHTML = "<span>���ȣ�" + e.lnglat.getLng() + ", γ�ȣ�" + e.lnglat.getLat() + "</span>";
    });

    return marker;
}

// ����Marker
function lockMarkerDriver1(marker, buttonId) {
    document.getElementById(buttonId).addEventListener('click', function () {
        marker.setDraggable(false); // ����Marker�����϶�
        var position = marker.getPosition();
        var latitude = position.lat; // γ��
        var longitude = position.lng; // ����
        driver1SendCoordinatesToBackend(latitude, longitude);
    });
}

function lockMarkerDriver2(marker, buttonId) {
    document.getElementById(buttonId).addEventListener('click', function () {
        marker.setDraggable(false); // ����Marker�����϶�
        var position = marker.getPosition();
        var latitude = position.lat; // γ��
        var longitude = position.lng; // ����
        driver2SendCoordinatesToBackend(latitude, longitude);
    });
}

function lockMarkerCustomer(marker, buttonId, startOrEnd) {
    document.getElementById(buttonId).addEventListener('click', function () {
        marker.setDraggable(false); // ����Marker�����϶�

        // ��ȡMarker�ĵ�ǰλ��
        var position = marker.getPosition();
        var latitude = position.lat; // γ��
        var longitude = position.lng; // ����

        // // ��������㻹���յ㣬׼����������
        // if (startOrEnd === 'start') {
        //     // �����������
        //     window.customerStartLatitude = latitude;
        //     window.customerStartLongitude = longitude;
        // } else if (startOrEnd === 'end') {
        //     // �����յ�����
        //     window.customerEndLatitude = latitude;
        //     window.customerEndLongitude = longitude;
        // }
        //
        // // ��������յ����궼�Ѿ����ã��������ݵ����
        // if (window.customerStartLatitude && window.customerStartLongitude && window.customerEndLatitude && window.customerEndLongitude) {
        //     sendCoordinatesToBackend(window.customerStartLatitude, window.customerStartLongitude, window.customerEndLatitude, window.customerEndLongitude);
        // }

        sendCoordinatesToBackend(latitude, longitude, startOrEnd);
    });
}

// �������굽��˵ĺ���
function sendCoordinatesToBackend(latitude, longitude, startOrEnd) {
    if (startOrEnd === 'start') {
        fetch('/Encrypt/customerSetStartCoordinate?startLatitude=' + latitude + '&startLongitude=' + longitude, {
            method: 'GET', // ���� 'POST' ����������POST����
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
            method: 'GET', // ���� 'POST' ����������POST����
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
        method: 'GET', // ���� 'POST' ����������POST����
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
        .then(response => response.json())
        .then(data => {

            if (data.status === "ok") {
                logArea.value += 'driver1����λ����Ϣ�ɹ�\n';
            } else if (data.status === "error") {
                logArea.value += 'driver1����λ����Ϣʧ��\n';
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
                logArea.value += 'driver2����λ����Ϣ�ɹ�\n';
            } else if (data.status === "error") {
                logArea.value += 'driver2����λ����Ϣʧ��\n';
            }
        })
        .catch(error => console.error('Error:', error));
}

// ��ʼ���˿͵�ͼ
var customerMap = initMap('customerMap', {center: [108.945, 34.285], zoom: 11});
customerMap.setLimitBounds(new AMap.Bounds([108.77, 34.14], [109.12, 34.43]));
var startMarker = addDraggableMarker(customerMap, [108.945, 34.285], 'startCoord', 'http://webapi.amap.com/theme/v1.3/markers/n/start.png');
var endMarker = addDraggableMarker(customerMap, [108.946, 34.285], 'endCoord', 'http://webapi.amap.com/theme/v1.3/markers/n/end.png');
// �޸ĳ�ʼ��Marker�ĵ���
lockMarkerCustomer(startMarker, 'lockStart', 'start');
lockMarkerCustomer(endMarker, 'lockEnd', 'end');

// ��ʼ��˾����ͼ1
var driverMap1 = initMap('driverMap1', {center: [108.945, 34.285], zoom: 11});
driverMap1.setLimitBounds(new AMap.Bounds([108.77, 34.14], [109.12, 34.43]))
var driver1Marker = addDraggableMarker(driverMap1, [108.945, 34.285], 'driver1Coord', 'Default');
lockMarkerDriver1(driver1Marker, 'lockDriver1');

// ��ʼ��˾����ͼ2
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

// ������ť����¼�
startButton.addEventListener('click', function () {
    const logArea = document.getElementById('serverLogArea');
    // ����API
    fetch('/Encrypt/serverStart', {
        method: 'GET', // ������ 'POST', ȡ����API��Ҫ��
        // ��������������ͷ������Ϣ�������Ҫ�Ļ�
        headers: {
            'Content-Type': 'application/json', // ����API����JSON��ʽ������ͷ
        },
    })
        .then(response => response.json()) // ����JSON��Ӧ
        .then(data => {
            // ���ݷ���ֵ������־����

            if (data.status === 'ok') { // ���践�ص�JSON�������һ����Ϊstatus���ֶ�
                logArea.value += 'trace�����������ɹ�\n'; // ���һ����־
            } else if (data.status === 'error') {
                logArea.value += 'trace����������ʧ��\n'; // ���һ����־
            }
        })
        .catch(error => {
            console.error('����ʧ��:', error);
            // ��������ʧ�ܵ����
            logArea.value += 'trace��������������ʧ��\n'; // ���һ����־
        });
});

stopButton.addEventListener('click', function () {
    const logArea = document.getElementById('serverLogArea');
    // ����API
    fetch('/Encrypt/serverStop', {
        method: 'GET', // ������ 'POST', ȡ����API��Ҫ��
        // ��������������ͷ������Ϣ�������Ҫ�Ļ�
        headers: {
            'Content-Type': 'application/json', // ����API����JSON��ʽ������ͷ
        },
    })
        .then(response => response.json()) // ����JSON��Ӧ
        .then(data => {
            // ���ݷ���ֵ������־����

            if (data.status === 'ok') { // ���践�ص�JSON�������һ����Ϊstatus���ֶ�
                logArea.value += 'trace������ֹͣ�ɹ�\n'; // ���һ����־
            } else if (data.status === 'error') {
                logArea.value += 'trace������ֹͣʧ��\n'; // ���һ����־
            }
        })
        .catch(error => {
            console.error('����ʧ��:', error);
            // ��������ʧ�ܵ����

            logArea.value += 'trace��������������ʧ��\n'; // ���һ����־
        });
});

customerStopBtn.addEventListener('click', function () {
    const logArea = document.getElementById('customerLogArea');
    // ����API
    fetch('/Encrypt/customerStop', {
        method: 'GET', // ������ 'POST', ȡ����API��Ҫ��
        // ��������������ͷ������Ϣ�������Ҫ�Ļ�
        headers: {
            'Content-Type': 'application/json', // ����API����JSON��ʽ������ͷ
        },
    })
        .then(response => response.json()) // ����JSON��Ӧ
        .then(data => {
            // ���ݷ���ֵ������־����

            if (data.status === 'ok') { // ���践�ص�JSON�������һ����Ϊstatus���ֶ�
                logArea.value += 'customer��ֹͣ\n'; // ���һ����־
                startMarker.setDraggable(true);
                endMarker.setDraggable(true);
            } else if (data.status === 'error') {
                logArea.value += 'customerֹͣʧ��\n'; // ���һ����־
            }
        })
        .catch(error => {
            console.error('����ʧ��:', error);
            // ��������ʧ�ܵ����
            logArea.value += 'customerֹͣ����ʧ��\n'; // ���һ����־
        });
});

driver1StopBtn.addEventListener('click', function () {
    const logArea = document.getElementById('driver1LogArea');
    // ����API
    fetch('/Encrypt/driver1Stop', {
        method: 'GET', // ������ 'POST', ȡ����API��Ҫ��
        // ��������������ͷ������Ϣ�������Ҫ�Ļ�
        headers: {
            'Content-Type': 'application/json', // ����API����JSON��ʽ������ͷ
        },
    })
        .then(response => response.json()) // ����JSON��Ӧ
        .then(data => {
            // ���ݷ���ֵ������־����

            if (data.status === 'ok') { // ���践�ص�JSON�������һ����Ϊstatus���ֶ�
                logArea.value += 'driver1��ֹͣ\n'; // ���һ����־
                driver1Marker.setDraggable(true);
            } else if (data.status === 'error') {
                logArea.value += 'driver1ֹͣʧ��\n'; // ���һ����־
            }
        })
        .catch(error => {
            console.error('����ʧ��:', error);
            // ��������ʧ�ܵ����

            logArea.value += 'driver1ֹͣ����ʧ��\n'; // ���һ����־
        });
});
driver2StopBtn.addEventListener('click', function () {
    const logArea = document.getElementById('driver2LogArea');
    // ����API
    fetch('/Encrypt/driver2Stop', {
        method: 'GET', // ������ 'POST', ȡ����API��Ҫ��
        // ��������������ͷ������Ϣ�������Ҫ�Ļ�
        headers: {
            'Content-Type': 'application/json', // ����API����JSON��ʽ������ͷ
        },
    })
        .then(response => response.json()) // ����JSON��Ӧ
        .then(data => {
            // ���ݷ���ֵ������־����

            if (data.status === 'ok') { // ���践�ص�JSON�������һ����Ϊstatus���ֶ�
                logArea.value += 'driver2��ֹͣ\n'; // ���һ����־
                driver2Marker.setDraggable(true);
            } else if (data.status === 'error') {
                logArea.value += 'driver2ֹͣʧ��\n'; // ���һ����־
            }
        })
        .catch(error => {
            console.error('����ʧ��:', error);
            // ��������ʧ�ܵ����
            logArea.value += 'driver2ֹͣ����ʧ��\n'; // ���һ����־
        });
});

cusReqBtn.addEventListener('click', function () {
    const logArea = document.getElementById('customerLogArea');
    // ����API
    fetch('/Encrypt/traceCustomerLogin', {
        method: 'GET', // ������ 'POST', ȡ����API��Ҫ��
        // ��������������ͷ������Ϣ�������Ҫ�Ļ�
        headers: {
            'Content-Type': 'application/json', // ����API����JSON��ʽ������ͷ
        },
    })
        .then(response => response.json()) // ����JSON��Ӧ
        .then(data => {
            // ���ݷ���ֵ������־����

            if (data.status === 'ok') { // ���践�ص�JSON�������һ����Ϊstatus���ֶ�
                logArea.value += "customer�ɹ����ʹ�����\n";
                logArea.scrollTop = logArea.scrollHeight;

            } else if (data.status === 'error') {
                logArea.value += "customer���ʹ�����ʧ��\n"; // ���һ����־
            }
        })
        .catch(error => {
            console.error('����ʧ��:', error);
            // ��������ʧ�ܵ����

            logArea.value += "customer���ʹ�����ʧ��\n"; // ���һ����־
        });
});

let pollingInterval; // �Ƶ������Ա���ƿ�ʼ��ֹͣ
let counter = 0; // ������ҲӦ���������Ա���״̬

function pollServerForStatus() {
    if (!pollingInterval) { // ȷ����ѯֻ������һ��
        pollingInterval = setInterval(() => {
            fetch('/Encrypt/orderResult')
                .then(response => response.json())
                .then(data => {
                    let customerLogArea = document.getElementById('customerLogArea');
                    if (data.status === 'ok') {
                        clearInterval(pollingInterval);
                        pollingInterval = null; // ������ѯ������Ʊ���
                        customerLogArea.value += data.message + "\n";
                    } else if (data.status === "error") {
                        if (counter >= 5) {
                            clearInterval(pollingInterval);
                            pollingInterval = null; // ������ѯ������Ʊ���
                            customerLogArea.value += "Ѱ��˾��ʧ��" + "\n";
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