
import numpy as np
import random
import sys
sys.path.append(".")
import helper as helper
import time

class PMDC(object):
    def __init__(self, D):
        n = len(D[0])
        self.dimension = n + 2
        self.M1 = np.random.random(size = (n + 2, n + 2))
        self.M2 = np.random.random(size = (n + 2, n + 2))
        self.inv_M1 = np.linalg.inv(self.M1)
        self.inv_M2 = np.linalg.inv(self.M2)

    def oldDataEnc(self,D):
        self.cov_D = np.cov(D, rowvar=False)
        self.inv_cov_D = np.linalg.inv(self.cov_D)
        E_wave = list()
        E_bar = list()
        for si in D:
            # step 1
            #print("step 1")
            Wi = si.dot(self.inv_cov_D).dot(si.T)
            si_wave = si.dot(self.inv_cov_D.T).tolist()
            si_bar = si.dot(self.inv_cov_D).tolist()
            # step 2
            #print("step 2")
            alpha = random.random()
            si_wave.append(alpha)
            si_wave.append(1)
            si_bar.append(Wi)
            si_bar.append(-alpha)
            # step 3
            #print("step 3")
            Si_wave = np.diag(si_wave)
            Si_bar = np.diag(si_bar)
            # step 4
            #print("step 4")
            Qi_wave = np.tril(np.random.random(size=(self.dimension, self.dimension)))
            for i in range(self.dimension):
                Qi_wave[i][i] = 1
            Qi_bar = np.tril(np.random.random(size=(self.dimension, self.dimension)))
            for i in range(self.dimension):
                Qi_bar[i][i] = 1
            E_wave.append(self.M1.dot(Qi_wave.dot(Si_wave)).dot(self.M2))
            E_bar.append(self.M1.dot(Qi_bar.dot(Si_bar)).dot(self.M2))
        return E_wave,E_bar

    def DataEnc(self,D,cov_Matrix):
        inv_cov_Matrix = np.linalg.inv(cov_Matrix)
        dimension = self.dimension
        E_wave = list()
        E_bar = list()
        for si in D:
            # step 1
            #print("step 1")
            Wi = si.dot(inv_cov_Matrix).dot(si.T)
            si_wave = si.dot(inv_cov_Matrix.T).tolist()
            si_bar = si.dot(inv_cov_Matrix).tolist()
            # step 2
            #print("step 2")
            alpha = random.random()
            si_wave.append(alpha)
            si_wave.append(1)
            si_bar.append(Wi)
            si_bar.append(-alpha)
            # step 3
            #print("step 3")
            Si_wave = np.diag(si_wave)
            Si_bar = np.diag(si_bar)
            # step 4
            #print("step 4")
            Qi_wave = np.tril(np.random.random(size=(dimension, dimension)))
            for i in range(self.dimension):
                Qi_wave[i][i] = 1
            Qi_bar = np.tril(np.random.random(size=(dimension, dimension)))
            for i in range(dimension):
                Qi_bar[i][i] = 1
            E_wave.append(self.M1.dot(Qi_wave).dot(Si_wave).dot(self.M2))
            E_bar.append(self.M1.dot(Qi_bar).dot(Si_bar).dot(self.M2))
        return E_wave,E_bar

    def CenterEnc(self,center_vector,cov_Matrix):
        inv_cov_Matrix = np.linalg.inv(cov_Matrix)
        dimension = self.dimension

        # step 1
        #print("step 1")
        Wi = center_vector.dot(inv_cov_Matrix).dot(center_vector.T)
        center_wave = center_vector.dot(inv_cov_Matrix.T).tolist()
        center_bar = center_vector.dot(inv_cov_Matrix).tolist()
        # step 2
        #print("step 2")
        alpha = random.random()
        center_wave.append(alpha)
        center_wave.append(1)
        center_bar.append(Wi)
        center_bar.append(-alpha)
        # step 3
        #print("step 3")
        Si_wave = np.diag(center_wave)
        Si_bar = np.diag(center_bar)
        # step 4
        #print("step 4")
        Qi_wave = np.tril(np.random.random(size=(dimension, dimension)))
        for i in range(self.dimension):
            Qi_wave[i][i] = 1
        Qi_bar = np.tril(np.random.random(size=(dimension, dimension)))
        for i in range(dimension):
            Qi_bar[i][i] = 1
        E_wave = self.M1.dot(Qi_wave).dot(Si_wave).dot(self.M2)
        E_bar = self.M1.dot(Qi_bar).dot(Si_bar).dot(self.M2)
        return E_wave,E_bar

    def TrapGen(self,q):
        beta = random.random()
        r_q = random.random()
        q_wave = (-q).tolist()
        q_wave.append(beta)
        q_wave.append(r_q)
        q_bar = (-q).tolist()
        q_bar.append(1)
        q_bar.append(beta)
        _Q_wave = np.diag(q_wave)
        _Q_bar = np.diag(q_bar)
        Q_wave = np.tril(np.random.random(size=(self.dimension, self.dimension)))
        for i in range(self.dimension):
            Q_wave[i][i] = 1
        Q_bar = np.tril(np.random.random(size=(self.dimension, self.dimension)))
        for i in range(self.dimension):
            Q_bar[i][i] = 1
        Eq_wave = self.inv_M2.dot(_Q_wave).dot(Q_wave).dot(self.inv_M1)
        Eq_bar = self.inv_M2.dot(_Q_bar).dot(Q_bar).dot(self.inv_M1)
        return Eq_wave,Eq_bar

    def DisCompare(self,E_wave,E_bar,Eq_wave,Eq_bar,labels):
        D = list()
        for i in range(len(E_wave)):
            #print(E_wave)
            Di_wave = 0
            Di_bar = 0
            for j in range(len(E_wave[0])):
                s_wave = E_wave[i][j, :].dot(Eq_wave[:, j])

                s_bar = E_bar[i][j, :].dot(Eq_bar[:, j])
                # print(M1[i,:])
                # print(M2[:,i])
                Di_wave += s_wave
                Di_bar += s_bar
            D_i = Di_wave + Di_bar
            #Pi_wave = E_wave[i].dot(Eq_wave)
            #Pi_bar = E_bar[i].dot(Eq_bar)
            #D_i = np.diag(Pi_wave).sum()+np.diag(Pi_bar).sum()
            D.append(D_i.tolist())
        sorted_D, sorted_labels = helper.sort_with_label(D,labels)
        #D.sort()
        return sorted_D, sorted_labels


    def DisCompare1(self,E_wave,E_bar,Eq_wave,Eq_bar,labels):
        D = list()
        for i in range(len(E_wave)):
            Pi_wave = E_wave[i].dot(Eq_wave)
            Pi_bar = E_bar[i].dot(Eq_bar)
            D_i = np.diag(Pi_wave).sum()+np.diag(Pi_bar).sum()
            D.append(D_i.tolist())
        sorted_D, sorted_labels = helper.sort_with_label(D,labels)
        #D.sort()
        return sorted_D, sorted_labels


if __name__ =='__main__':

    Dataset = np.array([[7, 4, 3], [8, 41, 32], [13, 26, 13], [18, 56, 42]])
    q = np.array([3, 8, 6])
    labels = [1,1,0,0]
    PMDC1 = PMDC(Dataset)
    E_wave, E_bar = PMDC1.oldDataEnc(Dataset)
    Eq_wave, Eq_bar = PMDC1.TrapGen(q)
    print("type Eq",type(E_wave[0]))
    t0 = time.time()
    for i in range(10000):
        D,labels = PMDC1.DisCompare(E_wave, E_bar,Eq_wave, Eq_bar, labels)
    t1 = time.time()
    print("dataset = ", Dataset, "q = ", q)
    print("Dis = ",D)
    print(t1-t0)

    t2 = time.time()
    for i in range(10000):
        D, labels = PMDC1.DisCompare1(E_wave, E_bar, Eq_wave, Eq_bar, labels)
    t3 = time.time()
    print("Dis1 = ", D)
    print(t3-t2)

    MDis = list()
    cov_D = np.cov(Dataset, rowvar=False)
    inv_cov_D = np.linalg.inv(cov_D)
    for xi in Dataset:
        MDis.append((q-xi).dot(inv_cov_D).dot((q-xi).T))
    print("MDis = ", MDis)

    """
    

    #E_wave = np.array([[7, 4, 3, 4], [8, 41, 32, 5], [13, 26, 13, 6], [18, 56, 42, 7]])
    #Eq_wave = np.array([[7, 4, 3, 2], [8, 41, 32, 6], [13, 26, 13, 2], [18, 56, 42, 6]])

    E_wave = np.array([[7, 4, 3], [41, 32, 5], [26, 13, 6]])
    Eq_wave = np.array([[7, 4, 2], [8, 41, 32], [13, 26, 2]])

    print("matrix A:", E_wave)
    print("matrix B:", Eq_wave)

    print("A matmul B:", E_wave.dot(Eq_wave))

    #print(E_wave.__matmul__(Eq_wave))

    #print(np.matmul(E_wave, Eq_wave))

    print("diag vector:", np.diag(E_wave.dot(Eq_wave)))



    
    t0 = time.time()
    for i in range(1000000):
        Pi_wave = E_wave.dot(Eq_wave)
        D_i = np.diag(Pi_wave).sum()
    t1 = time.time()
    print(np.diag(Pi_wave))
    print(t1-t0)
    print(D_i)

    t2 = time.time()
    




    for x in range(1):
        m = 0
        s = 0
        v = 0
        diag = list()
        for i in range(len(E_wave)):
            for j in range(len(E_wave)):
                    m = E_wave[i][j]*Eq_wave[j][i]
                    print(E_wave[i][j], "*", Eq_wave[j][i])
                    s += m
            print("=",s)
            diag.append(s)
            #v += s
    #t3 = time.time()
    #print(t3-t2)
    #print(v)

    print("for diag :", diag)
    

    M1 = np.array([[1, 2], [2, 1]])
    M2 = np.array([[2, 0], [1, 1]])

    M1 = np.array([[7, 4, 3, 4], [8, 41, 32, 5], [13, 26, 13, 6], [18, 56, 42, 7]])
    M2 = np.array([[7, 4, 3, 2], [8, 41, 32, 6], [13, 26, 13, 2], [18, 56, 42, 6]])
    print("M1 = ", M1)
    print("M2 = ", M2)

    M = M1.dot(M2)
    print("M1 matmul M2:", M)

    t0 = time.time()
    for i in range(10):
        Pi_wave = M1.dot(M2)
        D_i = np.diag(Pi_wave).sum()
    t1 = time.time()
    print(np.diag(Pi_wave))
    print(t1 - t0)
    print(D_i)

    t2 = time.time()

    for x in range(10):
        m = 0
        v = 0
        diag = list()
        for i in range(len(M1)):
            s = 0
            for j in range(len(M1)):
                    m = M1[i][j]*M2[j][i]
                    #print(M1[i][j], "*", M2[j][i])
                    s += m
            #print("=",s)
            diag.append(s)
            v += s
    t3 = time.time()
    print(t3-t2)
    print(v)

    print("for diag :", diag)

    t4 = time.time()
    for x in range(10):
        v = 0
        diag = list()
        for i in range(len(M1)):
            s = M1[i,:].dot(M2[:,i])
            #print(M1[i,:])
            #print(M2[:,i])
            diag.append(s)
            v += s
    t5 = time.time()
    print(t5-t4)
    print(v)

    print("vector dot diag :", diag)
    """









