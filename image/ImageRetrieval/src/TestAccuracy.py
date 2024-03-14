import time
import sys
sys.path.append(".")
import lib.helper as helper
import numpy as np
from PMDC import PMDC
from TAMMIE import TAMMIE


def Maknnclisserfier(q, dataset, labels, k):
    PMDC1 = PMDC(dataset)
    E_wave, E_bar = PMDC1.oldDataEnc(dataset)
    Eq_wave, Eq_bar = PMDC1.TrapGen(q)
    D, newlabels = PMDC1.DisCompare(E_wave, E_bar, Eq_wave, Eq_bar, labels)
    count_0 = 0
    count_1 = 0
    count_2 = 0
    final_class = 0
    for i in range(k):
        if newlabels[i] == 0:
            count_0 += 1
        elif newlabels[i] == 1:
            count_1 += 1
        elif newlabels[i] == 2:
            count_2 += 1
    if count_0 > count_1 and count_0 > count_2:
        final_class = 0
    elif count_1 > count_0 and count_1 > count_2:
        final_class = 1
    else:
        final_class = 2

    return final_class

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

def TAMMIE_PatK(filename, k):
    cluster_num = 10
    print(filename)
    dataset, labels = helper.import_data_from_CXR(filename)
    Datapath = "../../data/iris.txt"
    dataset, labels = helper.import_data_format_iris(Datapath)
    # print(type(dataset))
    TAMMIE1 = TAMMIE(dataset, labels)
    clustered_data, clustered_labels, C, Cov_cluster = TAMMIE1.GenPlainMFCM(cluster_num)
    for i in range(cluster_num):
        print("clustered_data:", i, len(clustered_data[i]))
    TAMMIE1.EncMFCM(clustered_data, C, Cov_cluster)
    for ki in range(k):
        print("------------- k=", ki+1, "--------------")
        Total_PK = 0
        for i in range(len(dataset)):
            q = dataset[i]
            q_label = labels[i]
            Eq_wave, Eq_bar, EPhi = TAMMIE1.EncTrapGen(q)
            D, sorted_labels = TAMMIE1.SimSearch(Eq_wave, Eq_bar, EPhi, clustered_labels, ki+1)
            correct_count = 0
            for j in range(ki+1):
                if q_label == sorted_labels[j]:
                    correct_count += 1
            print(i)
            Total_PK += correct_count / (ki+1)
        TAMMIE_PK = Total_PK/len(dataset)
        print("P@k:", TAMMIE_PK)
    return TAMMIE_PK

def test_all_Patk(filename, k):
    data, old_labels = helper.import_data_from_CXR(filename)
    print(filename)
    labels = list()
    for label in old_labels:
        if label == 1:
            labels.append(1)
        else:
            labels.append(0)

    Total_PK = 0.0
    for i in range(len(data)):
        Pk = helper.Ma_Patk(data[i], labels[i], data, labels, k)
        Total_PK += Pk
    Ma_PK = Total_PK/len(data)
    print("Ma_PK", Ma_PK)

    Total_PK = 0.0
    for i in range(len(data)):
        Pk = helper.Eu_Patk(data[i], labels[i], data, labels, k)
        Total_PK += Pk
    Eu_PK = Total_PK / len(data)
    print("Eu_PK", Eu_PK)



def test_all_accuracy(filename, k):
    data, labels = helper.import_data_from_CXR(filename)
    print(len(data))
    print(len(labels))
    newlabels = list()
    for label in labels:
        if label == 1:
            newlabels.append(1)
        else:
            newlabels.append(0)

    accuracy = Ma_accuracy(data, newlabels, data, newlabels, k)
    print(filename)
    print("PMDC accuracy:", accuracy)

    accuracy = helper.Ma_accuracy(data, newlabels, data, newlabels, k)
    print("plaintext accuracy:", accuracy)

    accuracy = helper.Eu_accuracy(data, newlabels, data, newlabels, k)
    print("plaintext Eu_accuracy:", accuracy)

    accuracy = helper.Cos_accuracy(data, newlabels, data, newlabels, k)
    print("plaintext Cosaccuracy:", accuracy)


if __name__ == '__main__':
    #filename = "/Users/po/Desktop/blue/MaImage/data/IDRiD_8"
    #TAMMIE_PatK(filename, 9)



    #filename = "/Users/po/Desktop/blue/MaImage/data/IDRiD_16"
    #TAMMIE_PatK(filename, 9)

    filename = "../../data/IDRiD_12"
    TAMMIE_PatK(filename, 9)

    filename = "../../data/IDRiD_20"
    TAMMIE_PatK(filename, 9)

    filename = "../../data/IDRiD_24"
    TAMMIE_PatK(filename, 9)
    filename = "../../data/IDRiD_28"
    TAMMIE_PatK(filename, 9)






    #test_all_accuracy("/Users/po/Desktop/blue/MaImage/data/IDRiD_16", 9)
    #test_all_accuracy("/Users/po/Desktop/blue/MaImage/data/IDRiD_32", 9)
    #test_all_accuracy("/Users/po/Desktop/blue/MaImage/data/IDRiD_48", 9)

    """
    filename = "/Users/po/Desktop/blue/MaImage/data/FIRE_8"
    k = 1
    for i in range(5):
        print("-----------------k =", k,"----------------")
        test_all_accuracy(filename, k)
        k += 2

    filename = "/Users/po/Desktop/blue/MaImage/data/FIRE_24"
    k = 1
    for i in range(5):
        print("-----------------k =", k, "----------------")
        test_all_accuracy(filename, k)
        k += 2
    
    filename = "/Users/po/Desktop/blue/MaImage/data/CXR_48"
    k = 1
    for i in range(5):
        print("-----------------k =", k, "----------------")
        test_all_accuracy(filename, k)
        k += 2

    filename = "/Users/po/Desktop/blue/MaImage/data/CXR_64"
    k = 1
    for i in range(5):
        print("-----------------k =", k, "----------------")
        test_all_accuracy(filename, k)
        k += 2
    """



