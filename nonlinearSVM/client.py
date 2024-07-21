import sys
import time
import json
import math
import random
import socket
import pandas as pd
from typing import Tuple
from Crypto.Util import number
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from sklearn.metrics import accuracy_score, f1_score

random.seed(42)
tolerance = 1e-10
scaling_factor = 1e10
k1, k2, k3, k4 = 512, 200, 128, 128
test_features_path = 'test_features_part_2.csv'
# test_labels_path = 'test_labels_part_2.csv'


def load_csv_data(path: str) -> Tuple[list, list]:
    data = pd.read_csv(path)
    features = data.iloc[:, :-1].values.tolist()
    labels = data.iloc[:, -1].tolist()
    return features, labels


def encrypt_query(query: list, modulus: int, s: int) -> Tuple[int, int, list]:
    A = sum(x ** 2 for x in query)
    alpha = number.getPrime(k2)
    query.extend([0, 0, 1])
    dim = len(query)
    enc_query = [0] * dim

    for i in range(dim):
        c = number.getRandomNBitInteger(k3)
        # 选一个近似0的极小数
        if query[i] < tolerance:
            enc_query[i] = (s * c) % modulus
        else:
            enc_query[i] = (s * (query[i] * alpha + c)) % modulus

    return A, alpha, enc_query


def send_ciphertext_query(sock: socket, A: int, alpha: int, modulus: int, enc_query: list, rsa_cipher) -> None:
    enc_query.extend([A, alpha, modulus])

    ciphertexts = []
    for x in enc_query:
        message = x.to_bytes((x.bit_length() + 7) // 8, 'big')
        ciphertext = rsa_cipher.encrypt(message)
        ciphertexts.append(ciphertext)
    ciphertexts_bytes = b''.join(ciphertexts)
    ciphertexts_length = len(ciphertexts_bytes)
    sock.sendall(ciphertexts_length.to_bytes(16, 'big'))
    sock.sendall(ciphertexts_bytes)


def first_round_receive(sock: socket, modulus: int, s: int, inv_s: int, alpha: int) -> Tuple[list, list, list, list, int]:
    response_length_bytes = sock.recv(16)
    response_length = int.from_bytes(response_length_bytes, 'big')
    response_bytes = sock.recv(response_length)
    response = json.loads(response_bytes.decode('utf-8'))

    paraB = response['paraB']
    paraD = response['paraD']
    epsilon = response['epsilon']

    paraE, paraF, paraK, paraU = [], [], [], []
    for x in paraD:
        e = (inv_s * x) % modulus
        paraE.append(e)

    square_alpha = int(pow(alpha, 2))
    for i in range(len(paraB)):
        tmp1 = paraB[i]
        tmp2 = (paraE[i] - (paraE[i] % square_alpha)) / (square_alpha * epsilon)
        paraF.append(tmp1 - tmp2)

    for x in paraF:
        t = random.randint(10000, int(scaling_factor))
        z = number.getRandomNBitInteger(k3)
        k = math.exp(-x) * scaling_factor + t
        paraK.append(k)
        u = (s * (t * alpha + z)) % modulus
        paraU.append(u)

    for _ in range(4):
        z = number.getRandomNBitInteger(k3)
        paraU.append((s * z) % modulus)

    return paraK, paraU, paraB, paraD, epsilon


def send_intermediate_data(sock: socket, paraK: list, paraU: list):
    response = {'paraK': paraK, 'paraU': paraU}
    response_bytes = json.dumps(response).encode('utf-8')
    response_length = len(response_bytes)
    response_length_bytes = response_length.to_bytes(16, 'big')
    sock.sendall(response_length_bytes)
    sock.sendall(response_bytes)


def get_result(sock: socket, modulus: int, inv_s: int, alpha: int) -> Tuple[int, float, float, int, int, float]:
    result_length_bytes = sock.recv(16)
    result_length = int.from_bytes(result_length_bytes, 'big')
    result_bytes = sock.recv(result_length)
    result = json.loads(result_bytes.decode('utf-8'))

    posN, negN, posM, negM = (result[k] for k in ['posN', 'negN', 'posM', 'negM'])
    posW = (inv_s * posM) % modulus
    negW = (inv_s * negM) % modulus

    square_alpha = int(pow(alpha, 2))
    tmp1 = posN - negN
    tmp2 = negW - (negW % square_alpha) - posW + (posW % square_alpha)
    t = tmp1 + (tmp2 / square_alpha)
    res = 1 if t > 0 else -1 if t < 0 else 0
    return res, posN, negN, posM, negM, t


if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Usage: python client.py <path_to_test_data> <csv_output_path>")
        exit(-1)
        
    input_file_path = sys.argv[1]
    csv_output_path =  sys.argv[2]

    test_features_df = pd.read_csv(input_file_path)
    test_features = test_features_df.values.tolist()

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    host = socket.gethostname()
    port = 9070
    client_socket.connect((host, port))

    pubkey_pem = client_socket.recv(2048)
    pubkey = RSA.importKey(pubkey_pem)
    encryptor = PKCS1_OAEP.new(pubkey)
    client_socket.send(len(test_features).to_bytes(4, 'big'))
    
    results = []

    predicts = []
    start_time = time.time()
    for idx, query_list in enumerate(test_features):
        p = number.getPrime(k1)
        paraS = random.randint(int(1e10), p - 1)
        inverse_s = pow(paraS, -1, p)
        paraA, para_alpha, encrypt_list = encrypt_query(query_list, p, paraS)
        send_ciphertext_query(client_socket, paraA, para_alpha, p, encrypt_list, encryptor)
        responseK, responseU, receiveB, receiveD, para_epsilon = first_round_receive(client_socket, p, paraS, inverse_s, para_alpha)
        send_intermediate_data(client_socket, responseK, responseU)
        predict, n_pos, n_neg, m_pos, m_neg, paraT = get_result(client_socket, p, inverse_s, para_alpha)
        predicts.append(predict)
        
        if idx == 0:
            print("\n展示第一条密文查询相关参数: ")
            print("模数p:", p)
            print("加密参数s:", paraS)
            print("明文查询请求:", query_list[0])
            print("密文查询请求:", encrypt_list[0])
            print("客户端查询参数A:", paraA)
            print("客户端查询参数alpha:", para_alpha)
            print("\n服务器第一次返回的密文参数B:", receiveB[0])
            print("服务器第一次返回的密文参数D:", receiveD[0])
            print("服务器第一次返回的密文参数epsilon:", para_epsilon)
            print("客户端第二次发送的密文参数K:", responseK[0])
            print("客户端第二次发送的密文参数U:", responseU[0])
            print("\n服务器第二次返回的中间结果N+:", n_pos)
            print("服务器第二次返回的中间结果N-:", n_neg)
            print("服务器第二次返回的中间结果M+:", m_pos)
            print("服务器第二次返回的中间结果M-:", m_neg)
            print("查询计算结果:", paraT)
            print("预测结果:", predict)
        
        results.append(
            {
                "Index": idx,
                "p": p,
                "paraS": paraS,
                "query_list[0]": query_list[0],
                "encrypt_list[0]": encrypt_list[0],
                "paraA": paraA,
                "para_alpha": para_alpha,
                "receiveB[0]": receiveB[0],
                "receiveD[0]": receiveD[0],
                "para_epsilon": para_epsilon,
                "responseK[0]": responseK[0],
                "responseU[0]": responseU[0],
                "n_pos": n_pos,
                "n_neg": n_neg,
                "m_pos": m_pos,
                "m_neg": m_neg,
                "paraT": paraT,
                "predict": predict
            }
        )

    client_socket.close()
    end_time = time.time()
    resultdf = pd.DataFrame(results)
    resultdf.to_csv(csv_output_path, index=False)
    print("文件已保存")
    elapsed_time = end_time - start_time
    single_time = elapsed_time / len(test_features)
    print(f"\n查询服务总用时: {elapsed_time:.2f}s")
    print(f"单次查询服务响应时间: {single_time:.2f}s")

    # test_labels_df = pd.read_csv(test_labels_path)
    # test_labels = test_labels_df.values.flatten().tolist()
    # print("label:", test_labels)
    # print("prediction:", predicts)
    # accuracy = accuracy_score(test_labels, predicts)
    # print(f"Accuracy on ciphertext test set: {accuracy * 100:.2f}%")
    # f1 = f1_score(test_labels, predicts)
    # print(f"F1 score on ciphertext test set: {f1 * 100:.2f}%\n")
