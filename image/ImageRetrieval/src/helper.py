from copy import deepcopy

import numpy as np
from sklearn import neighbors, datasets
from sklearn.model_selection import cross_val_score

def import_data_format_iris(file):
    """
    格式化数据，前四列为data，最后一列为cluster_location
    数据地址 http://archive.ics.uci.edu/ml/machine-learning-databases/iris/
    """
    data = []
    cluster_location = []
    with open(str(file), 'r') as f:
        for line in f:
            current = line.strip().split(",")
            current_dummy = []
            for j in range(0, len(current) - 1):
                current_dummy.append(float(current[j]))
            j += 1
            if current[j] == "Iris-setosa":
                cluster_location.append(0)
            elif current[j] == "Iris-versicolor":
                cluster_location.append(1)
            else:
                cluster_location.append(2)
            data.append(current_dummy)
    print("加载数据完毕")

    return np.array(data), cluster_location

def import_data_from_CXR(filename):
    data = []
    labels = []
    with open(str(filename), 'r') as f:
        for line in f:
            current = line.strip().split(",")
            current_dummy = []
            for j in range(0, len(current) - 1):
                current_dummy.append(float(current[j]))
            j += 1
            if current[j] == "0":
                labels.append(0)
            elif current[j] == "1":
                labels.append(1)
            elif current[j] == "2":
                labels.append(2)
            data.append(current_dummy)
    #print("加载数据完毕")

    return np.array(data), labels



def print_matrix(list):
    """
    以可重复的方式打印矩阵
    """
    for i in range(0, len(list)):
        print(list[i])


def checker_iris(final_location):
    """
    和真实的iris聚类结果进行校验比对
    """
    right = 0.0
    for k in range(0, 3):
        checker = [0, 0, 0]
        for i in range(0, 50):
            for j in range(0, len(final_location[0])):
                if final_location[i + (50 * k)][j] == 1:
                    checker[j] += 1
        right += max(checker)
        print("分类正确结果数:",right)
    answer = right / 150 * 100
    return "准确度：" + str(answer) + "%"

def generate_random_dataset(dataset_size, data_size):
    """
    :param dataset_size: 数据条数
    :param data_size: 每条数据的维度
    :return: 随机数据集， 随机标签集
    """
    labels = np.random.randint(0,4,(1, dataset_size))[0]
    dataset = np.random.randint(1, 50, (dataset_size, data_size))
    print("dataset generation done!")
    print("dataset_size = ", dataset_size," datasize = ",data_size)

    return dataset, labels

def generate_random_query(data_size):
    """
    :param dataset_size: 数据条数
    :param data_size: 每条数据的维度
    :return: 随机数据集， 随机标签集
    """
    query = np.random.randint(10,20,(1, data_size))[0]

    return query

def sort_with_label(old_array,old_labels):
    """
    带label的快速排序
    :param array: 数据集
    :param labels:数据对应的labels
    :return: 排序后的数据集和labels
    """
    array = deepcopy(old_array)
    n = len(array)
    labels = deepcopy(old_labels)
    for i in range(n):
        # Last i elements are already in place
        for j in range(0, n - i - 1):
            if array[j] > array[j + 1]:
                array[j], array[j + 1] = array[j + 1], array[j]
                labels[j], labels[j + 1] = labels[j + 1], labels[j]

    return array,labels

def MaDis(q, dataset):
    MDis = list()
    cov_D = np.cov(dataset, rowvar=False)
    inv_cov_D = np.linalg.inv(cov_D)
    for xi in dataset:
        MDis.append((q - xi).dot(inv_cov_D).dot((q - xi).T))

    return MDis

def EuDis(q, dataset):
    EuDis = list()
    for xi in dataset:
        EuDis.append(np.linalg.norm(q - xi))

    return EuDis

def CosDis(x, dataset):
    CosDis = list()
    """ 计算两个向量x和y的余弦相似度 """
    for y in dataset:
        CosDis.append(np.dot(x, y) / (np.linalg.norm(x) * np.linalg.norm(y)))

    return CosDis

def Ma_Patk(q, q_label, dataset, labels, k):
    MaDislist = MaDis(q, dataset)
    DisList, newlabels = sort_with_label(MaDislist, labels)
    correct_count = 0
    for i in range(k):
        if q_label == newlabels[i]:
            correct_count += 1

    return correct_count/k


def Eu_Patk(q, q_label, dataset, labels, k):
    MaDislist = EuDis(q, dataset)
    DisList, newlabels = sort_with_label(MaDislist, labels)
    correct_count = 0
    for i in range(k):
        if q_label == newlabels[i]:
            correct_count += 1

    return correct_count / k

def Cos_Patk(q, q_label, dataset, labels, k):
    MaDislist = CosDis(q, dataset)
    DisList, newlabels = sort_with_label(MaDislist, labels)
    correct_count = 0
    for i in range(k):
        if q_label == newlabels[i]:
            correct_count += 1

    return correct_count / k







def Maknnclisserfier(q, dataset, labels, k):

    MaDislist = MaDis(q, dataset)
    DisList, newlabels = sort_with_label(MaDislist, labels)
    count_0 = 0
    count_1 = 0
    count_2 = 0
    final_class = 0
    for i in range(k):
        if newlabels[i] == 0:
            count_0 += 1
        elif newlabels[i] == 1:
            count_1 += 1
        elif newlabels[i] == 1:
            count_2 += 1
    if count_0 > count_1 and count_0 > count_2:
        final_class = 0
    elif count_1 > count_0 and count_1 > count_2:
        final_class = 1
    else:
        final_class = 2

    return final_class

def knnclisserfier(q, dataset, labels, k):

    EuDislist = EuDis(q, dataset)
    DisList, newlabels = sort_with_label(EuDislist, labels)
    count_0 = 0
    count_1 = 0
    count_2 = 0
    final_class = 0
    for i in range(k):
        if newlabels[i] == 0:
            count_0 += 1
        elif newlabels[i] == 1:
            count_1 += 1
        elif newlabels[i] == 1:
            count_2 += 1
    if count_0 > count_1 and count_0 > count_2:
        final_class = 0
    elif count_1 > count_0 and count_1 > count_2:
        final_class = 1
    else:
        final_class = 2

    return final_class

def Cosknnclisserfier(q, dataset, labels, k):

    EuDislist = CosDis(q, dataset)
    DisList, newlabels = sort_with_label(EuDislist, labels)
    count_0 = 0
    count_1 = 0
    count_2 = 0
    final_class = 0
    for i in range(k):
        if newlabels[i] == 0:
            count_0 += 1
        elif newlabels[i] == 1:
            count_1 += 1
        elif newlabels[i] == 1:
            count_2 += 1
    if count_0 > count_1 and count_0 > count_2:
        final_class = 0
    elif count_1 > count_0 and count_1 > count_2:
        final_class = 1
    else:
        final_class = 2

    return final_class


def Eu_accuracy(queries, q_labels, dataset, labels, k):
    """
    :param queries: 查询集合
    :param feature: 特征向量集合
    :param labels: 特征向量集合对应的labels
    :param k: top k 的 k
    :return:
    """
    final_classes = list()
    for q in queries:
        final_classes.append(knnclisserfier(q, dataset, labels, k))
    count_right = 0
    for i in range(len(q_labels)):
        if q_labels[i] == final_classes[i]:
            count_right += 1
    accuracy = count_right/len(q_labels)

    return accuracy

def Ma_accuracy(queries, q_labels, dataset, labels, k):
    """
    :param queries: 查询集合
    :param feature: 特征向量集合
    :param labels: 特征向量集合对应的labels
    :param k: top k 的 k
    :return:
    """
    final_classes = list()
    for q in queries:
        final_classes.append(Maknnclisserfier(q, dataset, labels, k))
    count_right = 0
    for i in range(len(q_labels)):
        if q_labels[i] == final_classes[i]:
            count_right += 1
    accuracy = count_right/len(q_labels)

    return accuracy

def Cos_accuracy(queries, q_labels, dataset, labels, k):
    """
    :param queries: 查询集合
    :param feature: 特征向量集合
    :param labels: 特征向量集合对应的labels
    :param k: top k 的 k
    :return:
    """
    final_classes = list()
    for q in queries:
        final_classes.append(Cosknnclisserfier(q, dataset, labels, k))
    count_right = 0
    for i in range(len(q_labels)):
        if q_labels[i] == final_classes[i]:
            count_right += 1
    accuracy = count_right/len(q_labels)

    return accuracy

def testAccuracy(filename):
    data, labels = import_data_from_CXR(filename)
    print(filename)
    #print(len(data))
    for i in range(10):
        print("k =", i + 2)
        Eu_accutacy = Eu_accuracy(data, labels, data, labels, i + 2)
        print("Edis accuracy:", Eu_accutacy)
        Ma_accutacy = Ma_accuracy(data, labels, data, labels, i + 2)
        print("MaDis accuracy:", Ma_accutacy)
        print("------------------------------------")


if __name__ == '__main__':

    filename = "/Users/po/Desktop/blue/MaImage/data/CXR_64"
    data,labels = import_data_from_CXR(filename)
    print(len(data))
    #print(labels)

    filename = "/Users/po/Desktop/blue/MaImage/data/iris.txt"
    data, labels = import_data_format_iris(filename)

    Eu_accutacy = Eu_accuracy(data, labels, data, labels, 1)
    print("Edis accuracy:", Eu_accutacy)
    Ma_accutacy = Ma_accuracy(data, labels, data, labels, 1)
    print("MaDis accuracy:", Ma_accutacy)
    print("------------------------------------")


    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_16")
    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_32")
    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_64")
    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_80")
    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_108")
    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_original")

    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_64")
    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_128")
    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_256")
    #testAccuracy("/Users/po/Desktop/blue/MaImage/data/CXR_512")






