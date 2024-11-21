
import noisy_hist2

import report_noisy_max3
import report_noisy_max4
import sparse_vector_technique3
import sparse_vector_technique4
import sparse_vector_technique5
import sparse_vector_technique6

import sys
import numpy as np


np.set_printoptions(threshold=np.inf, linewidth=np.inf)


if __name__ == '__main__':
    # 参数
    epsilon = 0.1
    sensitivity = 1
    param = 2

    # 参数名
    algName = sys.argv[1]

    # 输入数组
    string_input = sys.argv[2]
    # 使用逗号分隔字符串并创建数组
    data_array = string_input.split(',')
    data_array = np.array(data_array, dtype=float)

    # 采样次数
    samples = int(sys.argv[3])
    epsilon_list = [10, 1, 0.1]

    if len(sys.argv) > 4:
        param = float(sys.argv[4])

    if (algName == "noisy_hist2"):
        mechanism = noisy_hist2.NoisyHist2(param)
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "report_noisy_max3"):
        mechanism = report_noisy_max3.ReportNoisyMax3(param)
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "report_noisy_max4"):
        mechanism = report_noisy_max4.ReportNoisyMax4(param)
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "sparse_vector_technique3"):
        c = int(sys.argv[3])
        t = float(sys.argv[4])
        if len(sys.argv) > 5:
            param = float(sys.argv[5])
        mechanism = sparse_vector_technique3.SparseVectorTechnique3(param, c, t)
        result = mechanism.m(data_array)
        print(result)
    elif (algName == "sparse_vector_technique4"):
        c = int(sys.argv[3])
        t = float(sys.argv[4])
        if len(sys.argv) > 5:
            param = float(sys.argv[5])
        mechanism = sparse_vector_technique4.SparseVectorTechnique4(param, c, t)
        result = mechanism.m(data_array)
        print(result)
    elif (algName == "sparse_vector_technique5"):
        c = int(sys.argv[3])
        t = float(sys.argv[4])
        if len(sys.argv) > 5:
            param = float(sys.argv[5])
        mechanism = sparse_vector_technique5.SparseVectorTechnique5(param, c, t)
        result = mechanism.m(data_array)
        print(result)
    elif (algName == "sparse_vector_technique6"):
        c = int(sys.argv[3])
        t = float(sys.argv[4])
        if len(sys.argv) > 5:
            param = float(sys.argv[5])
        mechanism = sparse_vector_technique6.SparseVectorTechnique6(param, c, t)
        result = mechanism.m(data_array)
        print(result)
    elif (algName == "report_noisy_max1"):
        c = int(sys.argv[3])
        t = float(sys.argv[4])
        if len(sys.argv) > 5:
            param = float(sys.argv[5])
        mechanism = sparse_vector_technique6.SparseVectorTechnique6(param, c, t)
        result = mechanism.m(data_array)
        print(result)

