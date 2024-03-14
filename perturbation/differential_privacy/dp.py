# 函数库
import exponential
import gaussian
import laplace
import noisy_hist1
import onetimerappor
import rappor
import report_noisy_max1
import report_noisy_max2
import snapping
import sparse_vector_technique1
import sparse_vector_technique2
import sparse_vector_technique_numerical

import sys
import numpy as np


np.set_printoptions(threshold=np.inf, linewidth=np.inf)


if __name__ == '__main__':
    # 参数
    epsilon = 0.1
    sensitivity = 1

    # 参数名
    algName = sys.argv[1]

    # 输入数组
    string_input = sys.argv[2]
    # 使用逗号分隔字符串并创建数组
    data_array = string_input.split(',')
    data_array = np.array(data_array, dtype=float)

    # 采样次数
    samples = int(sys.argv[3])
    # algName = "exponential"
    # data_array = np.array([1, 2, 3, 4, 5], float)
    # samples = 5

    if (algName == "exponential"):
        mechanism = exponential.ExponentialMechanism(epsilon, sensitivity)
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "laplace"):
        mechanism = laplace.LaplaceMechanism()
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "gaussian"):
        mechanism = gaussian.GaussianMechanism()
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "noisy_hist1"):
        mechanism = noisy_hist1.NoisyHist1(epsilon, sensitivity)
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "onetimerappor"):
        mechanism = onetimerappor.OneTimeRappor()
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "rappor"):
        mechanism = rappor.Rappor()
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "report_noisy_max1"):
        mechanism = report_noisy_max1.ReportNoisyMax1(epsilon)
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "report_noisy_max2"):
        mechanism = report_noisy_max2.ReportNoisyMax2(epsilon)
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "snapping"):
        mechanism = snapping.SnappingMechanism()
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "sparse_vector_technique1"):
        c = int(sys.argv[3])
        t = float(sys.argv[4])
        mechanism = sparse_vector_technique1.SparseVectorTechnique1(epsilon, c, t)
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "sparse_vector_technique2"):
        c = int(sys.argv[3])
        t = float(sys.argv[4])
        mechanism = sparse_vector_technique2.SparseVectorTechnique2(epsilon, c, t)
        result = mechanism.m(data_array, samples)
        print(result)
    elif (algName == "sparse_vector_technique_numerical"):
        c = int(sys.argv[3])
        t = float(sys.argv[4])
        mechanism = sparse_vector_technique_numerical.NumericalSVT(epsilon, c, t)
        result = mechanism.m(data_array, samples)
        print(result)
