import numpy as np
import sys
import time
sys.path.append(".")
import lib.helper as helper
from PMDC import PMDC
from newMFCM import MFCM


class TAMMIE(object):
    def __init__(self, dataset, labels):
        self.MFCM = MFCM()
        self.dataset = dataset
        self.labels = labels
        n = len(dataset[0])
        self.dimension = n + 2
        self.M3 = np.random.random(size=(n + 2, n + 2))
        self.M4 = np.random.random(size=(n + 2, n + 2))
        self.inv_M3 = np.linalg.inv(self.M3)
        self.inv_M4 = np.linalg.inv(self.M4)
        self.PMDC1 = PMDC(self.dataset)
        self.Matrix_Sigma = np.eye(self.dimension)



    def GenPlainMFCM(self, cluster_num):
        self.cluster_num = cluster_num
        final_U, C, Cov_cluster = MFCM.fuzzy(self.MFCM, self.dataset, cluster_num, 2)
        final_location = MFCM.final_class_from_U(MFCM, final_U)
        #print("final location:",final_location)
        clustered_data = list()
        clustered_labels = list()
        for k in range(self.cluster_num):
            cluster_subdata = list()
            cluster_sublabel = list()
            for i in range(len(self.dataset)):
                if (final_location[i] == k):
                    cluster_subdata.append(self.dataset[i])
                    cluster_sublabel.append(self.labels[i])
            # 把聚类后的数据和其label分到对应的中心
            clustered_data.append(cluster_subdata)
            clustered_labels.append(cluster_sublabel)
        return clustered_data, clustered_labels, C, Cov_cluster

    def EncMFCM(self, clustered_data, C, Cov_cluster):
        E_clustered_data_wave = list()
        E_clustered_data_bar = list()
        E_clustered_center_wave = list()
        E_clustered_center_bar = list()
        E_clustered_center_pi = list()

        for k in range(self.cluster_num):
            # 用PMDC加密每一个聚类中心下的索引
            E_wave, E_bar = self.PMDC1.DataEnc(clustered_data[k],Cov_cluster[k])
            E_clustered_data_wave.append(E_wave)
            E_clustered_data_bar.append(E_bar)

            # 加密聚类中心
            E_C_wave, E_C_bar = self.PMDC1.CenterEnc(C[k],Cov_cluster[k])
            E_clustered_center_wave.append(E_C_wave)
            E_clustered_center_bar.append(E_C_bar)

            inv_Cov_cluster = np.linalg.inv(Cov_cluster[k])
            #print(inv_Cov_cluster)
            newCol = np.random.randint(1, 2, size=(self.dimension-2, 2))
            #print(newCol)
            extend_inv_Cov = np.column_stack((inv_Cov_cluster, newCol))
            #print(extend_inv_Cov)
            newRow = np.random.randint(1, 2, size=(2, (self.dimension)))
            extend_inv_Cov = np.row_stack((extend_inv_Cov, newRow))
            E_extend_inv_Cov = self.M3.dot(extend_inv_Cov).dot(self.Matrix_Sigma).dot(self.M4)
            E_clustered_center_pi.append(E_extend_inv_Cov)
        self.E_D_wave = E_clustered_data_wave
        self.E_D_bar = E_clustered_data_bar
        self.E_C_wave = E_clustered_center_wave
        self.E_C_bar = E_clustered_center_bar
        self.E_C_pi = E_clustered_center_pi


    def EncTrapGen(self, q_vector):
        phi = list()
        for j in range (len(q_vector)):
            phi.append((np.multiply(q_vector[j],q_vector)))

        #print(type(phi))
        Phi = phi
        newCol = np.random.randint(1, 2, size=(self.dimension - 2, 2))
        # print(newCol)
        extend_Phi = np.column_stack((Phi, newCol))
        # print(extend_inv_Cov)
        newRow = np.random.randint(1, 2, size=(2, (self.dimension)))
        extend_Phi = np.row_stack((extend_Phi, newRow))

        Eq_wave, Eq_bar = self.PMDC1.TrapGen(q_vector)
        EPhi = self.inv_M4.dot(self.Matrix_Sigma).dot(extend_Phi).dot(self.inv_M3)

        return Eq_wave, Eq_bar, EPhi

    def SimSearch(self, Eq_wave, Eq_bar, EPhi, clustered_labels, k):

        # 与每一个簇中心计算距离
        PC = list()
        TrPC = list()

        for i in range(len(self.E_C_bar)):
            PCi_wave = self.E_C_wave[i].dot(Eq_wave)
            PCi_bar = self.E_C_bar[i].dot(Eq_bar)
            PCPhi = EPhi.dot(self.E_C_pi[i])
            TrPCi = np.diag(PCi_wave).sum() + np.diag(PCi_bar).sum() + np.diag(PCPhi).sum()
            TrPC.append(TrPCi)
        #print("Dis to center:",TrPC)

        # 找到距离最近的簇

        finded_center = list()
        finded_labels = list()
        finded_wave = list()
        finded_bar = list()
        num_center = 0
        while True:
            MinCenter = 0
            tempvalue = TrPC[0]
            for i in range(len(TrPC)-1):
                if tempvalue > TrPC[i+1] and len(self.E_D_wave[i+1]) > 0 and (not(i+1 in finded_center)):
                    MinCenter = i+1
                    tempvalue = TrPC[i+1]
            finded_center.append(MinCenter)
            finded_labels += clustered_labels[MinCenter]
            finded_wave += self.E_D_wave[MinCenter]
            finded_bar += self.E_D_bar[MinCenter]
            print("datalen:",len(finded_wave))
            if len(finded_wave) > len(self.dataset)/1.3:
                break
            #num_center += 1
            #if num_center >= 2:
            #    break


        #print("最近的簇：", MinCenter)
        # 搜索簇内所有数据
        D, sorted_labels = self.PMDC1.DisCompare(finded_wave, finded_bar, Eq_wave, Eq_bar, finded_labels)
        #print(D)
        #print(len(sorted_labels))

        #print(D)
        return D, sorted_labels









if __name__ == '__main__':

    cluster_num = 3
    Datapath = "/Users/po/PycharmProjects/MaImage/data/iris.txt"
    dataset, labels = helper.import_data_format_iris(Datapath)
    #print(type(dataset))
    TAMMIE = TAMMIE(dataset,labels)
    clustered_data, clustered_labels, C, Cov_cluster = TAMMIE.GenPlainMFCM(cluster_num)
    for i in range(3):
        print("clustered_data:",i, len(clustered_data[i]))
    t0 = time.time()
    TAMMIE.EncMFCM(clustered_data, C, Cov_cluster)
    t1 = time.time()
    q = np.array([1, 1, 1, 1])
    Eq_wave, Eq_bar, EPhi = TAMMIE.EncTrapGen(q)
    t2 = time.time()
    D, sorted_labels = TAMMIE.SimSearch(Eq_wave, Eq_bar, EPhi, clustered_labels, 70)
    t3 = time.time()
    print(sorted_labels)
    print("Data Enc time :", t1 - t0, "s")
    print("Query Enc time :", t2 - t1, "s")
    print("Search time :", t3 - t2, "s")








