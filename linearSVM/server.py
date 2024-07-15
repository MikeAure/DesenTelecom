import json
import socket
import random
import warnings
import numpy as np
import pandas as pd
from sklearn import svm
from typing import Tuple
from Crypto.Util import number
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from sklearn.exceptions import ConvergenceWarning
from sklearn.metrics import accuracy_score, f1_score

random.seed(42)
warnings.filterwarnings("ignore", category=ConvergenceWarning)

tolerance = 1e-10
scaling_factor = 1e10
k1, k2, k3, k4 = 512, 200, 128, 128
train_path = 'train_data.csv'
test_path = 'test_data.csv'


def receive_and_parse(sock: socket) -> Tuple[list, int]:
    modulus_length_bytes = sock.recv(4)
    modulus_length = int.from_bytes(modulus_length_bytes, 'big')
    modulus_bytes = sock.recv(modulus_length)
    modulus = int.from_bytes(modulus_bytes, 'big')

    ciphertexts_length_bytes = sock.recv(4)
    ciphertexts_length = int.from_bytes(ciphertexts_length_bytes, 'big')
    ciphertexts_bytes = sock.recv(ciphertexts_length)

    queries = []
    step = key.size_in_bytes()
    for i in range(0, ciphertexts_length, step):
        ciphertext = ciphertexts_bytes[i: i + step]
        message = int.from_bytes(cipher.decrypt(ciphertext), 'big')
        queries.append(message)

    return queries, modulus


def train_and_preprocess(path: str) -> Tuple[np.ndarray, np.ndarray, np.ndarray]:
    train_data = pd.read_csv(path)
    x_train = train_data.iloc[:, :-1].values
    y_train = train_data.iloc[:, -1].values

    model = svm.SVC(kernel='linear', max_iter=800)
    model.fit(x_train, y_train)

    test_data = pd.read_csv(test_path)
    x_test = test_data.iloc[:, :-1].values
    y_test = test_data.iloc[:, -1].values
    y_pred = model.predict(x_test)

    # print("label:", y_test.tolist())
    # print("prediction:", y_pred.tolist())
    # accuracy = accuracy_score(y_test, y_pred)
    # print(f"Accuracy on plaintext test set: {accuracy * 100:.2f}%")
    # f1 = f1_score(y_test, y_pred)
    # print(f"F1 score on plaintext test set: {f1 * 100:.2f}%\n")

    alpha_yi = model.dual_coef_
    support_indices = model.support_
    y_support = y_train[support_indices]
    alpha_i = alpha_yi.ravel() / y_support

    bias = model.intercept_ * scaling_factor
    support_vectors = model.support_vectors_

    positive_sv_indices = y_support == 1
    negative_sv_indices = y_support == -1

    positive_support_vectors = support_vectors[positive_sv_indices]
    positive_alpha = alpha_i[positive_sv_indices]
    negative_support_vectors = support_vectors[negative_sv_indices]
    negative_alpha = alpha_i[negative_sv_indices]

    p_b = (positive_support_vectors.T @ positive_alpha) * scaling_factor
    n_b = (negative_support_vectors.T @ negative_alpha) * scaling_factor

    p_b = p_b.astype(np.int64)
    n_b = n_b.astype(np.int64)
    bias = bias.astype(np.int64)

    return p_b, n_b, bias


def plaintext_verify(p_b: np.ndarray, n_b: np.ndarray, bias: np.ndarray) -> None:
    df = pd.read_csv(test_path)
    features = df.iloc[:, :-1].values.tolist()
    labels = df.iloc[:, -1].tolist()
    predicts = []
    for query_list in features:
        res1 = np.sum(np.multiply(p_b, np.array(query_list)))
        res2 = np.sum(np.multiply(n_b, np.array(query_list)))
        res = res1 - res2 + bias
        predict = 1 if res > 0 else -1 if res < 0 else 0
        predicts.append(predict)
    accuracy = accuracy_score(labels, predicts)
    print(f"Accuracy on plaintext test set: {accuracy * 100:.2f}%\n")


def classify(p_b: np.ndarray, n_b: np.ndarray, bias: np.ndarray, modulus: int, query: list) -> Tuple[int, int]:
    beta = number.getRandomNBitInteger(k2)
    dim = len(p_b)
    positive_d, negative_d = [], []

    for i in range(dim):
        if p_b[i] < tolerance:
            r = number.getRandomNBitInteger(k4)
            d = (r * query[i]) % modulus
            positive_d.append(d)
        else:
            d = (p_b[i] * beta * query[i]) % modulus
            positive_d.append(d)

    for i in range(dim):
        if n_b[i] < tolerance:
            r = number.getRandomNBitInteger(k4)
            d = (r * query[i]) % modulus
            negative_d.append(d)
        else:
            d = (n_b[i] * beta * query[i]) % modulus
            negative_d.append(d)

    b0 = bias[0]
    r = number.getRandomNBitInteger(k4)
    if b0 > 0:
        p_d = (b0 * beta * query[dim]) % modulus
        positive_d.append(p_d)
        n_d = (r * query[dim]) % modulus
        negative_d.append(n_d)
    else:
        p_d = (r * query[dim]) % modulus
        positive_d.append(p_d)
        n_d = (-b0 * beta * query[dim]) % modulus
        negative_d.append(n_d)

    r = number.getRandomNBitInteger(k4)
    p_d = (r * query[dim + 1]) % modulus
    positive_d.append(p_d)
    r = number.getRandomNBitInteger(k4)
    n_d = (r * query[dim + 1]) % modulus
    negative_d.append(n_d)

    p_sum = sum(positive_d) % modulus
    n_sum = sum(negative_d) % modulus

    return p_sum, n_sum


if __name__ == '__main__':
    key = RSA.generate(1024)
    cipher = PKCS1_OAEP.new(key)
    pubkey = key.publickey().exportKey('PEM')

    positive_b, negative_b, b = train_and_preprocess(train_path)
    # plaintext_verify(positive_b, negative_b, b)

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    host = socket.gethostname()
    port = 7789
    server_socket.bind((host, port))
    server_socket.listen(5)
    print("等待客户端的连接...")

    client_socket, addr = server_socket.accept()
    print(f"连接地址: {addr}")
    client_socket.sendall(pubkey)
    length_bytes = client_socket.recv(4)
    test_length = int.from_bytes(length_bytes, 'big')

    for i in range(test_length):
        enc_query, p = receive_and_parse(client_socket)
        positive_sum, negative_sum = classify(positive_b, negative_b, b, p, enc_query)
        data = {'positive_sum': positive_sum, 'negative_sum': negative_sum}
        json_data = json.dumps(data)
        client_socket.sendall(json_data.encode('utf-8'))
        if i == 0:
            print("\n展示第一条密文查询相关参数: ")
            print("密文查询请求:", enc_query)
            print("模数p:", p)
            print("中间结果D+:", positive_sum)
            print("中间结果D-:", negative_sum)

    client_socket.close()
    server_socket.close()
