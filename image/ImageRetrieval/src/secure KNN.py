import numpy as np
from random import *
import lib.helper as helper
import time
            
def M_inv_matix(d):
    M=np.random.rand(d+1,d+1)
    while 1:
        M_abs=np.linalg.det(M)
        if M_abs!=0:
            M_inv=np.linalg.inv(M)
            break
    return M,M_inv

def SK(d):
    S=np.random.randint(0,2,d+1)
    M1,M1_inv=M_inv_matix(d)
    M2,M2_inv=M_inv_matix(d)
    return S,M1,M2,M1_inv,M2_inv

def IndexGen(S,M1,M2,Dataset):
    D1 = list()
    D2 = list()
    d=len(Dataset[0])
    for p in Dataset:
        p_2 = np.linalg.norm(p)
        p = p.tolist()
        p.append(-0.5 * p_2 ** 2)
        p = np.array(p)
        p1=np.zeros(shape=(d+1))
        p2=np.zeros(shape=(d+1))
        for i in range(d+1):
            if S[i]==0:
                p1[i]=p[i]
                p2[i]=p[i]
            else:
                p1[i]=random()
                p2[i]=p[i]-p1[i]
        I1=p1.dot(M1)
        I2=p2.dot(M2)
        D1.append(I1)
        D2.append(I2)
    return D1,D2

def Trapdoor(S,M1_inv,M2_inv,q):
    d=len(q)
    q = q.tolist()
    q.append(1)
    q = np.multiply(np.array(q), random())
    q1=np.zeros(shape=(d+1))
    q2=np.zeros(shape=(d+1))
    for i in range(d+1):
        if S[i]==1:
            q1[i]=q[i]
            q2[i]=q[i]
        else:
            q1[i]=random()
            q2[i]=q[i]-q1[i]
    T1=M1_inv.dot(q1)
    T2=M2_inv.dot(q2)
    return T1,T2

def Compare(D1,D2,T1,T2):
    result = list()
    for i in range (len(D1)):
        result.append(D1[i].dot(T1) + D2[i].dot(T2))
    result.sort()
    return result


if __name__ == '__main__':

    dataset_size = 100000
    data_size = 512
    dataset, labels = helper.generate_random_dataset(dataset_size, data_size)
    q = helper.generate_random_query(data_size)

    S,M1,M2,M1_inv,M2_inv = SK(data_size)
    t0 = time.time()
    D1,D2 = IndexGen(S,M1,M2,dataset)
    t1 = time.time()
    T1,T2 = Trapdoor(S, M1_inv, M2_inv, q)
    t2 = time.time()
    result = Compare(D1,D2,T1,T2)
    t3 = time.time()
    print("Enc time:",t1-t0,"s")
    print("TrapGen time:", t2 - t1, "s")
    print("Search time:", t3 - t2, "s")

