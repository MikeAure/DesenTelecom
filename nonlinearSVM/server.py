import json
import socket
import random
import time
import numpy as np
import pandas as pd
from sklearn import svm
from typing import Tuple
from Crypto.Util import number
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from sklearn.metrics import accuracy_score, f1_score

random.seed(42)

increment = int(1e5)
tolerance = 1e-10
scaling_factor = 1e10
k1, k2, k3, k4 = 512, 200, 128, 128
train_path = 'train_data.csv'
# test_features_path = 'test_features_part_1.csv'
# test_labels_path = 'test_labels_part_1.csv'

def recv_all(sock, length):
    data = b''
    while len(data) < length:
        more = sock.recv(length - len(data))
        if not more:
            raise EOFError('Socket closed before receiving all data')
        data += more
    return data



def receive_and_parse(sock: socket) -> Tuple[list, int, int, int]:
    ciphertexts_length_bytes = sock.recv(16)
    ciphertexts_length = int.from_bytes(ciphertexts_length_bytes, 'big')
    ciphertexts_bytes = recv_all(sock, ciphertexts_length)

    queries = []
    step = key.size_in_bytes()
    for i in range(0, ciphertexts_length, step):
        ciphertext = ciphertexts_bytes[i: i + step]
        message = int.from_bytes(cipher.decrypt(ciphertext), 'big')
        queries.append(message)

    modulus = queries.pop()
    alpha = queries.pop()
    A = queries.pop()

    return queries, modulus, alpha, A


def train_and_preprocess(path: str) -> Tuple[np.ndarray, np.ndarray, np.ndarray, int, float, float, float]:
    train_data = pd.read_csv(path)
    x_train = train_data.iloc[:, :-1].values
    y_train = train_data.iloc[:, -1].values
    start_time = time.time()
    model = svm.SVC(kernel='rbf')
    end_time = time.time()
    print(f"Finish training in {end_time - start_time}")
    model.fit(x_train, y_train)

    variance = np.array(x_train).var()
    average = np.array(x_train).mean()
    n_features = x_train.shape[1]
    gamma = 1 / n_features

    # test_features = pd.read_csv(test_features_path)
    # test_labels = pd.read_csv(test_labels_path)
    # x_test = test_features.values
    # y_test = test_labels.values
    # y_pred = model.predict(x_test)
    #
    # print("label:", y_test.flatten().tolist())
    # print("prediction:", y_pred.tolist())
    # accuracy = accuracy_score(y_test, y_pred)
    # print(f"Accuracy on plaintext test set: {accuracy * 100:.2f}%")
    # f1 = f1_score(y_test, y_pred)
    # print(f"F1 score on plaintext test set: {f1 * 100:.2f}%\n")

    alpha_yi = model.dual_coef_
    support_indices = model.support_
    y_support = y_train[support_indices]
    alpha_i = alpha_yi.ravel() / y_support

    bias = model.intercept_
    support_vectors = model.support_vectors_
    positive_sv_indices = y_support == 1
    split_idx = np.argmax(positive_sv_indices)

    support_vectors = np.concatenate((support_vectors[split_idx:], support_vectors[:split_idx]))
    alpha_i = np.concatenate((alpha_i[split_idx:], alpha_i[:split_idx]))
    split_idx = support_vectors.shape[0] - split_idx

    return alpha_i, bias, support_vectors, split_idx, variance, average, gamma


def quantization(gamma: float, variance: float) -> Tuple[int, int]:
    epsilon = 1
    while True:
        epsilon *= increment
        product = epsilon * (gamma / variance)
        if product > scaling_factor:
            break

    return int(product), epsilon


def first_round_process(query: list, support_vectors: np.ndarray, modulus: int, gamma: float, variance: float,
                        alpha: int, A: int) -> Tuple[list, list, int]:
    paraB, paraD = [], []
    lam, epsilon = quantization(gamma, variance)
    support_vectors = support_vectors.astype(np.int64)

    for idx, sv in enumerate(support_vectors):
        sv = sv.tolist()
        theta = random.randint(10000, int(scaling_factor))

        tmp1 = sum(x ** 2 for x in sv) + A
        tmp2 = (gamma / variance) * tmp1 + (theta / epsilon)
        paraB.append(tmp2)

        sum_list = []
        sv.extend([0, 0, theta])
        for i in range(len(sv)):
            if sv[i] < tolerance:
                r = number.getRandomNBitInteger(k4)
                d = (r * query[i]) % modulus
            elif i == len(sv) - 1:
                d = (sv[i] * alpha * query[i]) % modulus
            else:
                d = (2 * lam * sv[i] * alpha * query[i]) % modulus
            sum_list.append(d)

        sumD = sum(sum_list) % modulus
        paraD.append(sumD)

    return paraB, paraD, epsilon


def first_round_respond(sock: socket, paraB: list, paraD: list, epsilon: int) -> None:
    data = {'paraB': paraB, 'paraD': paraD, 'epsilon': epsilon}
    json_data = json.dumps(data)
    # print(json_data)
    response_bytes = json_data.encode('utf-8')
    response_length = len(response_bytes)
    sock.sendall(response_length.to_bytes(16, 'big'))
    sock.sendall(response_bytes)


def receive_intermediate_data(sock: socket) -> Tuple[list, list]:
    response_length_bytes = sock.recv(16)
    response_length = int.from_bytes(response_length_bytes, 'big')
    response_bytes = recv_all(sock, response_length)
    response = json.loads(response_bytes.decode('utf-8'))

    paraK = response['paraK']
    paraU = response['paraU']

    return paraK, paraU


def second_round_process(supports: np.ndarray, bias: np.ndarray, split_idx: int, paraK: list, paraU: list,
                         alpha: int, modulus: int) -> Tuple[float, float, int, int]:
    # 获得最优偏置项
    b0 = bias[0] * scaling_factor
    dim = len(supports)
    supports = (supports * scaling_factor).astype(np.int64).tolist()
    beta = random.randint(increment, int(scaling_factor))
    paraN, paraM = [], []
    for i in range(dim):
        n = beta * supports[i] * paraK[i]
        paraN.append(n)

    posN = sum(paraN[:split_idx])
    negN = sum(paraN[split_idx:])

    if b0 > 0:
        posN += beta * b0
    else:
        negN -= beta * b0

    for i in range(dim):
        if supports[i] < tolerance:
            w = number.getRandomNBitInteger(k4)
            m = (w * paraU[i]) % modulus
            paraM.append(m)
        else:
            m = (alpha * beta * supports[i] * paraU[i]) % modulus
            paraM.append(m)

    mask = []
    for i in range(4):
        w = number.getRandomNBitInteger(k4)
        m = (w * paraU[dim + i]) % modulus
        mask.append(m)

    posM = (sum(paraM[:split_idx]) + sum(mask[:2])) % modulus
    negM = (sum(paraM[split_idx:]) + sum(mask[2:])) % modulus

    return posN, negN, posM, negM


def query_result_respond(sock: socket, ciphertext_result: Tuple) -> None:
    posN, negN, posM, negM = ciphertext_result
    data = {'posN': posN, 'negN': negN, 'posM': posM, 'negM': negM}
    json_data = json.dumps(data)
    result_bytes = json_data.encode('utf-8')
    result_length = len(result_bytes)
    sock.sendall(result_length.to_bytes(16, 'big'))
    sock.sendall(result_bytes)


if __name__ == '__main__':

    key = RSA.generate(1024)
    cipher = PKCS1_OAEP.new(key)
    pubkey = key.publickey().exportKey('PEM')
    svm_supports, b, svm_support_vectors, split_indices, var, avg, kernel_gamma = train_and_preprocess(train_path)

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    host = socket.gethostname()
    port = 9070
    server_socket.bind((host, port))

    server_socket.listen(5)
        
    print("等待客户端的连接...")

    client_socket, addr = server_socket.accept()
    print(f"连接地址: {addr}")
    client_socket.sendall(pubkey)
    length_bytes = client_socket.recv(4)
    test_length = int.from_bytes(length_bytes, 'big')
    
    start_time = time.time()

    for i in range(test_length):
        enc_query, p, para_alpha, paraA = receive_and_parse(client_socket)
        responseB, responseD, para_epsilon = first_round_process(enc_query, svm_support_vectors, p, kernel_gamma,
                                                                var, para_alpha, paraA)
        first_round_respond(client_socket, responseB, responseD, para_epsilon)
        K, U = receive_intermediate_data(client_socket)
        ciphertext_query_result = second_round_process(svm_supports, b, split_indices, K, U, para_alpha, p)
        query_result_respond(client_socket, ciphertext_query_result)
        # if i == 0:
        #     print("\n展示第一条密文查询相关参数: ")
        #     print("密文查询请求:", enc_query[0])
        #     print("模数p:", p)
        #     print("客户端查询参数A:", paraA)
        #     print("客户端查询参数alpha:", para_alpha)
        #     print("\n服务器第一次发送的密文参数B:", responseB[0])
        #     print("服务器第一次发送的密文参数D:", responseD[0])
        #     print("服务器第一次发送的密文参数epsilon:", para_epsilon)
        #     print("客户端第二次返回的密文参数K:", K[0])
        #     print("客户端第二次返回的密文参数U:", U[0])
        #     print("\n服务器第二次发送的中间结果N+:", ciphertext_query_result[0])
        #     print("服务器第二次发送的中间结果N-:", ciphertext_query_result[1])
        #     print("服务器第二次发送的中间结果M+:", ciphertext_query_result[2])
        #     print("服务器第二次发送的中间结果M-:", ciphertext_query_result[3])
    end_time = time.time()
    print(f"Communication finished in {end_time - start_time}")
    client_socket.close()
    server_socket.close()
