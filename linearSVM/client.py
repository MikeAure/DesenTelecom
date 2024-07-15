import sys
import time
import json
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
k1, k2, k3, k4 = 512, 200, 128, 128
# test_path = "test_data.csv"

def load_csv_data(path: str) -> Tuple[list, list]:
    data = pd.read_csv(path)
    features = data.iloc[:, :-1].values.tolist()
    labels = data.iloc[:, -1].tolist()
    return features, labels


def encrypt_query(query: list, modulus: int, s: int) -> list:
    alpha = number.getPrime(k2)
    dim = len(query)
    enc_query = [0] * (dim + 2)
    for i in range(dim):
        c = number.getRandomNBitInteger(k3)
        if query[i] < tolerance:
            enc_query[i] = (s * c) % modulus
        else:
            enc_query[i] = (s * (query[i] * alpha + c)) % modulus

    c = number.getRandomNBitInteger(k3)
    enc_query[dim] = (s * (alpha + c)) % modulus
    c = number.getRandomNBitInteger(k3)
    enc_query[dim + 1] = (s * c) % modulus
    return enc_query


def send_ciphertext_query(
    sock: socket, enc_query: list, modulus: int, rsa_cipher
) -> None:
    modulus_bytes = modulus.to_bytes((modulus.bit_length() + 7) // 8, "big")
    modulus_length = len(modulus_bytes)
    sock.sendall(modulus_length.to_bytes(4, "big"))
    sock.sendall(modulus_bytes)

    ciphertexts = []
    for x in enc_query:
        message = x.to_bytes((x.bit_length() + 7) // 8, "big")
        ciphertext = rsa_cipher.encrypt(message)
        ciphertexts.append(ciphertext)
    ciphertexts_bytes = b"".join(ciphertexts)
    ciphertexts_length = len(ciphertexts_bytes)
    sock.sendall(ciphertexts_length.to_bytes(4, "big"))
    sock.sendall(ciphertexts_bytes)


def get_result(sock: socket, modulus: int, s: int) -> Tuple[int, int, int, int]:
    inverse_s = pow(s, -1, modulus)
    received_data = sock.recv(2048)
    data = json.loads(received_data.decode("utf-8"))
    positive_sum = data["positive_sum"]
    negative_sum = data["negative_sum"]
    t = (inverse_s * positive_sum) % modulus - (inverse_s * negative_sum) % modulus
    res = 1 if t > 0 else -1 if t < 0 else 0
    return positive_sum, negative_sum, t, res


if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python client.py <path_to_test_data> <csv_output_path>")
        exit(-1)
        
    test_path = sys.argv[1]
    csv_output_path =  sys.argv[2]
    test_features, test_labels = load_csv_data(test_path)

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    host = socket.gethostname()
    port = 7789
    client_socket.connect((host, port))

    pubkey_pem = client_socket.recv(2048)
    pubkey = RSA.importKey(pubkey_pem)
    cipher = PKCS1_OAEP.new(pubkey)
    client_socket.send(len(test_labels).to_bytes(4, "big"))

    results = []

    predicts = []
    start_time = time.time()
    for idx, query_list in enumerate(test_features):
        p = number.getPrime(k1)
        paraS = random.randint(int(1e10), p - 1)
        encrypt_list = encrypt_query(query_list, p, paraS)
        send_ciphertext_query(client_socket, encrypt_list, p, cipher)
        p_sum, n_sum, paraT, predict = get_result(client_socket, p, paraS)
        predicts.append(predict)

        results.append(
            {
                "Index": idx,
                "p": p,
                "paraS": paraS,
                "query_list": query_list,
                "encrypt_list": encrypt_list,
                "p_sum": p_sum,
                "n_sum": n_sum,
                "paraT": paraT,
                "predict": predict,
            }
        )
        if idx == 0:
            print("\n展示第一条密文查询相关参数: ")
            print("模数p:", p)
            print("加密参数s:", paraS)
            print("明文查询请求:", query_list)
            print("密文查询请求:", encrypt_list)
            print("中间结果D+:", p_sum)
            print("中间结果D-:", n_sum)
            print("查询计算结果:", paraT)
            print("预测结果:", predict)
            
    results_df = pd.DataFrame(results)     
    results_df.to_csv(csv_output_path, index=False)

    client_socket.close()
    end_time = time.time()
    elapsed_time = end_time - start_time
    single_time = elapsed_time / len(test_labels)
    print(f"\n查询服务总用时: {elapsed_time:.2f}s")
    print(f"单次查询服务响应时间: {single_time:.2f}s")

    print("label:", test_labels)
    print("prediction:", predicts)
    accuracy = accuracy_score(test_labels, predicts)
    print(f"Accuracy on ciphertext test set: {accuracy * 100:.2f}%")
    f1 = f1_score(test_labels, predicts)
    print(f"F1 score on ciphertext test set: {f1 * 100:.2f}%\n")
