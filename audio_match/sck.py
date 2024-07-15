import socket
import time
import warnings
import os

import InnerProduct
import extraction
from funcs import *
# import os
from model import *
from server_message import ServerResponse
import argparse
import json

warnings.filterwarnings('ignore')

def interceptString(s, r):
    ret = ""
    for i in s:
        if i == r:
            return ret
        ret += i

def add_args(parser: argparse.ArgumentParser):
    parser.add_argument("name", type=str)
    parser.add_argument("file", type=str)
    parser.add_argument("-d", "--distance", type=float, default=0.43)
    parser.add_argument("-m", "--mode", choices=["register", "login"], default="register")
    return parser
def formatPrint(C):
    print("[", end="")
    for i in range(len(C)):
        if (i == len(C) / 2):
            print()
        if (i == len(C) - 1):
            print(C[i], end="]\n")
        else:
            print(C[i], end=",")

def cli_main():

    file_save_location = "./voiceprinttxt/"
    if not os.path.exists(file_save_location):
        os.mkdir(file_save_location)
    argparser = add_args(argparse.ArgumentParser(description="Voiceprint Server"))
    args = argparser.parse_args()
    secu = InnerProduct.InnerProduct()

    k1, k2, k3, k4, p, x = secu.getVariable()
    # print("参数为：")
    # print("k1:", k1, end=", ")
    # print("k2:", k2, end=", ")
    # print("k3:", k3, end=", ")
    # print("k4:", k4)
    # print("p:", p)
    # print("x:", x)

    stime = time.time()
    # print('当前时间：', str(stime) + '秒')

    tmp_data = args.name  # 接收id
    # print(tmp_data)

    tmp_list = tmp_data.split("@@")
    # print(tmp_list)
    title = tmp_list[0]
    # print(str(title))

    identifier = title.split("&&")
    # print(str(identifier))

    filename = str(title) + ".wav"
    # print("args.file: " + str(args.file))
    file_path = os.path.join(file_save_location, filename)
    with open(args.file, "rb") as f1:
        with open(file_path, "wb") as f:
            f.write(f1.read())

    result1 = ServerResponse()
    success = False
    user_name = identifier[0]
    flag = identifier[1]
    if (flag == '0'):
        try:
            query_exist(user_name)
        except IOError as e:
            # print(e)
            result1.status = "error"
            result1.message = str(e)
        else:
            # print('注册声纹模板提取...')
            s2 = time.time()
            voiceprint_a = extraction.extract_voiceprint(file_path, sr=16000)
            e2 = time.time()
            # print('注册声纹模板提取结束，用时：', str(e2 - s2) + '秒')

            a = voiceprint_a.cpu().numpy().tolist()[0]
            # print("提取的原始声纹注册模板,模板长度为512:")
            formatPrint(a[0:10])

            a = secu.float2int(a)
            # print("乘以放大倍数的原始声纹注册模板,模板长度为512:")
            formatPrint(a[0:10])

            A = torch.norm(voiceprint_a).item()
            # print("提取的原始声纹注册模板向量的模: ", A)

            s3 = time.time()

            s, C = secu.Step1(a)
            e3 = time.time()
            # print('认证声纹加密结束，用时：', str(e3 - s3) + '秒')

            # print("提取的加密后的声纹注册模板,模板长度为512:")
            formatPrint(C[0:10])

            s3 = time.time()
            # print("将声纹模板密文存入数据库并将密钥写入文件")
            try:
                save(user_name, C, A)
            except IOError as e:
                # print(e)
                result1.status = "error"
                result1.message = str(e)
            else:
                file_s = user_name + '.txt'
                s = str(s)
                # print("该注册用户的密钥: ", s)
                file_txt_path = os.path.join(file_save_location, file_s)
                with open(file_txt_path, "w") as f:
                    f.write(s)

                e3 = time.time()
                # print('存储结束，用时：', str(e3 - s3) + '秒')
                result1.status = 'ok'
    else:
        # print('登录声纹模板提取...')
        s2 = time.time()
        voice_print_b = extraction.extract_voiceprint(file_path, sr=16000)
        e2 = time.time()
        # print('登录声纹模板提取结束，用时：', str(e2 - s2) + '秒')

        b = voice_print_b.cpu().numpy().tolist()[0]

        # print("提取的原始声纹登录模板,模板长度为512:")
        formatPrint(b[0:10])

        b = secu.float2int(b)
        # print("乘以放大倍数的原始声纹登录模板,模板长度为512:")
        formatPrint(b[0:10])

        B = torch.norm(voice_print_b).item()
        # print("提取的原始声纹登录模板向量的模: ", B)

        s3 = time.time()
        # 从数据库读取C,A
        try:
            C, A = findvoice(identifier[0])
        except Exception as e:
            # print(e)
            result1.status = "error"
            result1.message = str(e)
        else:
            # 从文件中读取s
            file_txt_path = os.path.join(file_save_location, identifier[0] + ".txt")
            with open(file_txt_path) as f:
                s = int(f.read())
            DSum, D = secu.Step2(b, C)
            e3 = time.time()

            # print("提取的加密后的声纹登录模板向量的参数: ", DSum)

            # print("提取的加密后的声纹登录模板,模板长度为512:")
            formatPrint(D[0:10])

            # print('认证声纹加密结束，用时：', str(e3 - s3) + '秒')

            s1 = time.time()
            inner_product = secu.Step3(DSum, s)
            # print("乘以放大倍数的注册模板向量与认证模板向量之间的内积: ", inner_product)

            inner_product = secu.correct(inner_product)
            # print("注册模板向量与认证模板向量之间的内积: ", inner_product)

            distance = A ** 2 + B ** 2 - 2 * inner_product
            # print("注册模板向量与认证模板向量之间的欧式距离: ", distance)

            e1 = time.time()
            # print("计算注册模板向量与认证模板向量之间的欧式距离结束，用时：", str(e1 - s1) + '秒')

            if distance <= args.distance:
                result1.status = "ok"
                success = True
            else:
                result1.status = "error"
                result1.message = "distance too large"
            login_add(identifier[0], success)

    print(json.dumps(result1.__dict__)).encode('utf-8')
    return json.dumps(result1.__dict__).encode('utf-8')

if __name__ == "__main__" :
    cli_main()