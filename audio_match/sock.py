import socket

import extraction
from model import *

# 建立一个服务端

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# server.bind(('localhost',19090)) #绑定要监听的端口
server.bind(('192.168.1.117', 19090))
# server.bind(('10.173.49.47',19090)) #绑定要监听的端口
server.listen(5)  # 开始监听 表示可以使用五个链接排队
# conn就是客户端链接过来而在服务端为期生成的一个链接实例
voice = ''
while True:
    print("wait message")
    conn, addr = server.accept()  # 等待链接,多个链接的时候就会出现问题,其实返回了两个值
    print(conn, addr)
    # conn.setblocking(0)
    conn.settimeout(7)
    title = conn.recv(1024).decode("utf8", "ignore")  # 接收id
    title = title.replace('\n', '')
    identifier = title.split("&&")

    filename = str(title) + ".wav"

    data = conn.recv(1024)  # 接收数据
    total_data = b''
    total_data += data
    print('total_data:', total_data)
    count = num = len(data)
    # file = open(filename,"wb")

    while True:
        try:
            data = conn.recv(1024)
            total_data += data
            count = len(data)
            # print(data)
            # print(count)
            num += count
        except:
            break

    print("receive done")
    print(id)
    print(total_data)
    print(num)

    with open(filename, "wb") as f:
        f.write(total_data)

    # print(total_data.decode())
    # total_data = total_data.decode()
    # total_data = total_data.split('+@+')
    # if len(total_data) == 2:
    #     result = "N"
    # else:
    #     result = "N"
    # print(result)
    # conn.send(result.encode()) #然后再发送数据
    result1 = ''

    flag = identifier[1]
    voiceprint_a = extraction.extract_voiceprint(filename, sr=16000)

    if (flag == '0'):
        # save(identifier[0],voiceprint=voiceprint_a)
        voice = voiceprint_a
        print('注册')
        result1 = 'Y'
    else:
        print('identifier', identifier[0])
        # voiceprint_e = findvoice(identifier[0])
        Euclideandist = PairwiseDistance(2)
        print('voice', type(voice))
        distance = Euclideandist(voice, voiceprint_a)
        print(distance)
        if distance <= 0.4:
            result1 = "Y"
        else:
            result1 = "N"

        # login_add(identifier[0],success)
    print(result1)
    conn.send(result1.encode())
    conn.close()
    # os.remove(filename)
