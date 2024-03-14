import socket
import time

import extraction
from funcs import *
from model import *


def Edistance(x, y):
    x = np.array(x)
    y = np.array(y)
    return np.sqrt(np.sum(np.square(x - y)))


# 建立一个服务端

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# server.bind(('localhost',19090)) #绑定要监听的端口
server.bind(('0.0.0.0', 19090))
# server.bind(('127.0.0.1',19090))
# server.bind(('10.173.169.159',19090)) #绑定要监听的端口
server.listen(5)  # 开始监听 表示可以使用五个链接排队
# conn就是客户端链接过来而在服务端为期生成的一个链接实例
while True:
    print("waiting message")
    conn, addr = server.accept()  # 等待链接,多个链接的时候就会出现问题,其实返回了两个值
    # print(conn, addr)
    # conn.setblocking(0)
    conn.settimeout(7)
    stime = time.time()
    print('当前服务器时间：', str(stime) + '秒')

    print('start receiving...')
    print('开始采集客户端发送过来的秘钥数据...')
    s1 = time.time()
    title = conn.recv(1024).decode("utf8", "ignore")  # 接收id
    title = title.replace('\n', '')
    identifier = title.split("&&")

    filename = str(title) + ".wav"
    print(filename)

    data = conn.recv(1024)  # 接收数据
    total_data = b''
    total_data += data
    # print('total_data:',total_data)
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

    e1 = time.time()
    print('receive done')
    print('采集时间为：', str(e1 - s1) + '秒')
    print('采集到的的数据为：', total_data)
    print('采集到的的数据存储位置为：', filename)
    # print(id)
    # print(total_data)
    # print(num)

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
    success = True
    flag = identifier[1]
    print('声纹模板提取...')
    s2 = time.time()

    voiceprint_a = extraction.extract_voiceprint(filename, sr=16000)
    # voiceprint_a = voiceprint_a.cpu().numpy().tolist()[0]
    print("提取出来的原始声纹模板为：\n", voiceprint_a)

    e2 = time.time()
    print('声纹模板提取结束，用时：', str(e2 - s2) + '秒')

    if (flag == '0'):
        s3 = time.time()
        print("将声纹模板明文存入数据库")

        save(identifier[0], voiceprint=voiceprint_a, modulus=1)

        e3 = time.time()
        print('存储结束，用时：', str(e3 - s3) + '秒')
        result1 = 'Y'
    else:
        s3 = time.time()

        print('identifier id:', identifier[0])
        voiceprint_e, A = findvoice(identifier[0])
        print("11111111111111111111", type(voiceprint_e))
        # voiceprint_e = list(voiceprint_e)
        # print("encryption of voiceprint_e:",torch.round(10000-voiceprint_a*10000))

        # distance = Edistance(voiceprint_a,voiceprint_e)
        Euclideandist = PairwiseDistance(2)
        distance = Euclideandist(voiceprint_e, voiceprint_a)

        e3 = time.time()
        print("计算注册模板向量与认证模板向量之间的欧式距离结束，用时：", str(e3 - s3) + '秒')
        print("注册模板向量与认证模板向量之间的欧式距离:", distance.item())
        if distance <= 0.43:
            result1 = "Y"
        else:
            result1 = "N"
            success = False
        login_add(identifier[0], success)
    print(result1)
    conn.send(result1.encode())
    conn.close()
    # os.remove(filename)
