import numpy as np
import sys
import time
sys.path.append(".")
import lib.helper as helper
from PMDC import PMDC
from TAMMIE import TAMMIE


def testalldata_TAMMIE(dataset_size, data_size, cluster_num):
    print("------------------test TAMMIE--------------------")
    dataset, labels = helper.generate_random_dataset(dataset_size, data_size)
    TAMMIE1 = TAMMIE(dataset, labels)
    clustered_data, clustered_labels, C, Cov_cluster = TAMMIE1.GenPlainMFCM(cluster_num)
    for i in range(cluster_num):
        print("clustered_data:", i, len(clustered_data[i]))
    t0 = time.time()
    TAMMIE1.EncMFCM(clustered_data, C, Cov_cluster)
    t1 = time.time()
    q = helper.generate_random_query(data_size)
    Eq_wave, Eq_bar, EPhi = TAMMIE1.EncTrapGen(q)
    t2 = time.time()
    D = TAMMIE1.SimSearch(Eq_wave, Eq_bar, EPhi)
    t3 = time.time()
    print("Data Enc time :", t1 - t0, "s")
    print("Query Enc time :", t2 - t1, "s")
    print("Search time :", t3 - t2, "s")

def testalldata_PMDC(dataset_size, data_size):
    print("----------------test PMDC--------------------")
    dataset, labels = helper.generate_random_dataset(dataset_size, data_size)
    q = helper.generate_random_query(data_size)
    PMDC1 = PMDC(dataset)
    t0 = time.time()
    E_wave, E_bar = PMDC1.oldDataEnc(dataset)
    t1 = time.time()
    Eq_wave, Eq_bar = PMDC1.TrapGen(q)
    t2 = time.time()
    D = PMDC1.DisCompare(E_wave, E_bar, Eq_wave, Eq_bar, labels)
    t3 = time.time()
    print("Data Enc time :", t1 - t0, "s")
    print("Query Enc time :", t2 - t1, "s")
    print("Search time :", t3 - t2, "s")


if __name__ == '__main__':
    dataset_size = 2000
    data_size = 128
    for i in range(5):
        testalldata_PMDC(dataset_size, data_size)
        #testalldata_TAMMIE(dataset_size, data_size, 10)
        dataset_size += 2000
"""
    dataset_size = 2000
    data_size = 4
    for i in range(5):
        testalldata_PMDC(dataset_size, data_size)
        data_size += 2

"""
