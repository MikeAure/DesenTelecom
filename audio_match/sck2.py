import socket

import InnerProduct
import extraction

# 建立一个服务端

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# server.bind(('localhost',19090)) #绑定要监听的端口
server.bind(('192.168.1.117', 19090))
# server.bind(('127.0.0.1',19090))
# server.bind(('10.173.169.159',19090)) #绑定要监听的端口
server.listen(5)  # 开始监听 表示可以使用五个链接排队
# conn就是客户端链接过来而在服务端为期生成的一个链接实例
secu = InnerProduct.InnerProduct()
while True:
    print("waiting message")
    conn, addr = server.accept()  # 等待链接,多个链接的时候就会出现问题,其实返回了两个值
    # print(conn, addr)
    # conn.setblocking(0)
    conn.settimeout(4)
    title = conn.recv(1024).decode("utf8", "ignore")  # 接收id
    title = title.replace('\n', '')
    identifier = title.split("&&")

    filename = str(title) + ".jpg"

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

    print("receive done")
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
    voiceprint_a = extraction.ertract_voiceprint(filename, sr=16000)
    # print("encryption of voiceprint_a:",torch.round(voiceprint_a*10000+10000))

    a = [4, 3, 2, 1]
    s, C = secu.Step1(a)
    # print('C', C)
    if (flag == '0'):
        # torch.Tensor -> list a,取模
        # a = [1,2,3,4]
        # s, C = secu.Step1(a)
        # save(identifier[0],voiceprint=voiceprint_a)
        # save(identifier[0],C)
        file_s = identifier[0] + '.txt'
        s = str(s)
        with open(file_s, "w") as f:
            f.write(s)

        result1 = 'Y'
    else:
        # torch.Tensor -> list b，取模
        print('identifier id:', identifier[0])
        # voiceprint_e = findvoice(identifier[0])
        # print("encryption of voiceprint_e:",torch.round(10000-voiceprint_a*10000))
        # Euclideandist = PairwiseDistance(2)
        # distance = Euclideandist(voiceprint_e, voiceprint_a)
        b = [1, 2, 3, 4]
        # 从数据库读取C
        # C =
        # 从文件中读取s
        # with open(identifier[0] + ".txt") as f:
        #     s = int(f.read())
        DSum = secu.Step2(b, C)
        innerproduct = secu.Step3(DSum, s)
        print('innerproduct', innerproduct)
        # distance = innerproduct/(A*B)
        # print("distance:",distance.item())
        # if distance <= 0.43:
        #     result1 = "Y"
        # else:
        #     result1 = "N"
        #     success=False
        # login_add(identifier[0],success)
    print(result1)
    conn.send(result1.encode())
    conn.close()
    # os.remove(filename)
