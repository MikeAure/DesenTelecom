import copy
import math
import random
import time
import sys
sys.path.append(".")
import lib.helper as helper
import numpy as np


class MFCM(object):
    def __init__(self):
        # 用于初始化隶属度矩阵U
        self.MAX = 10000.0
        # 用于结束条件
        self.Epsilon = 0.00000001

    def initialise_U(self, data, cluster_number):
        """
        这个函数是隶属度矩阵U的每行加起来都为1. 此处需要一个全局变量MAX.
        """
        U = []
        for i in range(0, len(data)):
            current = []
            rand_sum = 0.0
            for j in range(0, cluster_number):
                dummy = random.randint(1, int(self.MAX))
                current.append(dummy)
                rand_sum += dummy
            for j in range(0, cluster_number):
                current[j] = current[j] / rand_sum
            U.append(current)
        return U

    def MaDistance(self, point, center, cov_D):
        if len(point) != len(center):
            return -1
        inv_cov_D = np.linalg.inv(cov_D)
        arraydelta = np.array(point) - np.array(center)
        MaDis = arraydelta.T.dot(inv_cov_D).dot(arraydelta)
        return(MaDis)


    def end_conditon(self, U, U_old):
        """
    	结束条件。当U矩阵随着连续迭代停止变化时，触发结束
    	"""
        for i in range(0, len(U)):
            for j in range(0, len(U[0])):
                if abs(U[i][j] - U_old[i][j]) > self.Epsilon:
                    return False
        return True

    def normalise_U(self, U):
        """
        在聚类结束时使U模糊化。每个样本的隶属度最大的为1，其余为0
        """
        for i in range(0, len(U)):
            maximum = max(U[i])
            for j in range(0, len(U[0])):
                if U[i][j] != maximum:
                    U[i][j] = 0
                else:
                    U[i][j] = 1
        return U

    # m的最佳取值范围为[1.5，2.5]
    def fuzzy(self, data, cluster_number, m):
        """
        这是主函数，它将计算所需的聚类中心，并返回最终的归一化隶属矩阵U.
        参数是：簇数(cluster_number)和隶属度的因子(m)
        """
        print("开始聚类")
        # 初始化隶属度矩阵U
        U = self.initialise_U(data, cluster_number)
        #helper.print_matrix(U)

        # 初始化簇参数
        U_old = copy.deepcopy(U)
        # 计算每个簇的聚类中心
        C = list()

        for i in range(0, cluster_number):
            current_cluster_center = []
            for k in range(0, len(data[0])):
                dummy_sum_num = 0.0
                dummy_sum_dum = 0.0
                for j in range(0, len(data)):
                    # c_i 分子
                    dummy_sum_num += (U[j][i]) * data[j][k]
                    # 分母
                    dummy_sum_dum += (U[j][i])
                # 第k列的聚类中心点值
                current_cluster_center.append(dummy_sum_num / dummy_sum_dum)
            # 第i簇的聚类中心
            C.append(current_cluster_center)

        # 计算D
        D = 0.0
        for i in range(0, cluster_number):
            for j in range(0,len(data)):
                D += (U[j][i]**m) * (np.dot((np.matrix(data[j]) - np.array(C[i])),(np.matrix(data[j]) -
                                                                np.array(C[i])).T))
        #print('D = ', D)

        # 计算每个簇的协方差矩阵
        Cov_C = []
        for i in range(0, cluster_number):
            dummy_sum_cov = np.cov(data, rowvar=False)
            dummy_sum_dum = 0.0
            for j in range(0, len(data)):
                # 分母
                dummy_sum_dum += (U[j][i])
                # 分子
                dummy_sum_cov += (U[j][i]) * (np.dot((np.matrix(data[j]) -
                                                                np.array(C[i])).T,
                                                               (np.matrix(data[j]) - np.array(C[i]))))
            dummy_Sigma = dummy_sum_cov / dummy_sum_dum
            #print (np.linalg.det(dummy_Sigma))
            if 1/D < np.linalg.det(dummy_Sigma) < D:
                Cov_C.append(dummy_sum_cov / dummy_sum_dum)
            else:
                Cov_C.append(np.eye(len(data[0])))

        update_times = 0
        # 循环更新U
        while (True):
            # 创建它的副本，以检查结束条件
            U_old = copy.deepcopy(U)
            # 计算聚类中心
            C = []

            for i in range(0, cluster_number):
                current_cluster_center = []
                for k in range(0, len(data[0])):
                    dummy_sum_num = 0.0
                    dummy_sum_dum = 0.0
                    for j in range(0, len(data)):
                        # c_i 分子
                        dummy_sum_num += (U[j][i] ** m) * data[j][k]
                        # 分母
                        dummy_sum_dum += (U[j][i] ** m)
                    # 第k列的聚类中心点值
                    dummy_Sigma = dummy_sum_num / dummy_sum_dum
                    current_cluster_center.append(dummy_sum_num / dummy_sum_dum)
                # 第i簇的聚类中心
                C.append(current_cluster_center)

            #计算每个簇的协方差矩阵
            Cov_C = []
            for i in range(0,cluster_number):
                dummy_sum_cov = np.cov(data, rowvar=False)
                dummy_sum_dum = 0.0
                for j in range(0,len(data)):
                    # 分母
                    dummy_sum_dum += (U[j][i] ** m)
                    # 分子
                    dummy_sum_cov += (U[j][i] ** m) * (np.dot((np.matrix(data[j]) -
                                        np.array(C[i])).T, (np.matrix(data[j])-np.array(C[i]))))
                dummy_Sigma = dummy_sum_cov / dummy_sum_dum
                if 1 / D < np.linalg.det(dummy_Sigma) < D:
                    Cov_C.append(dummy_sum_cov / dummy_sum_dum)
                else:
                    Cov_C.append(np.eye(len(data[0])))

            # 创建一个距离向量, 用于计算U矩阵。
            distance_matrix = []
            for j in range(0, len(data)):
                current = []
                for i in range(0, cluster_number):
                    current.append(self.MaDistance(data[j], C[i], Cov_C[i]))
                distance_matrix.append(current)

            # 更新U
            for i in range(0, cluster_number):
                for j in range(0, len(data)):
                    dummy = 0.0
                    for s in range(0, cluster_number):
                        # 分母

                        dummy += ((distance_matrix[j][i] - math.log(np.linalg.det(np.linalg.pinv(Cov_C[i])), math.e)) /
                                  (distance_matrix[j][s] - math.log(np.linalg.det(np.linalg.pinv(Cov_C[s])),
                                                                    math.e))) ** (1 / (m - 1))
                        """
                        dummy += ((distance_matrix[j][i]) /
                                  (distance_matrix[j][s])) ** (1 / (m - 1))
                        """
                    U[j][i] = 1 / dummy
            update_times += 1
            print("迭代次数：",update_times)
            if self.end_conditon(U, U_old):
                print("结束聚类")
                break
        print("标准化 U")
        U = self.normalise_U(U)
        return U, np.array(C), Cov_C

    def final_class_from_U(self,U):
        final_class = []
        for u in U:
            #print(u)
            for i in range(len(u)):
                if (u[i] == 1):
                    final_class.append(i)
        return final_class

if __name__ == '__main__':
    # 加载数据
    Datapath = "../../data/iris.txt"
    FCM = MFCM()
    data, cluster_location = helper.import_data_format_iris(Datapath)
    #helper.print_matrix(data)

    start = time.time()
    # 调用模糊C均值函数
    final_U, C, Cov_C = FCM.fuzzy(data, 3, 2)
    #helper.print_matrix(final_U)

    final_class = FCM.final_class_from_U(final_U)
    print(final_class)

    # 准确度分析
    print("分类结果",helper.checker_iris(final_U))
    print("用时：{0}".format(time.time() - start))

