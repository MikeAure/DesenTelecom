"""
MIT License, Copyright (c) 2021 SRI Lab, ETH Zurich
"""
import numpy as np
from abstract import Mechanism


class ExponentialMechanism(Mechanism):

    def __init__(self, eps: float, sensitivity: float = 1):
        self.eps = eps
        self.sensitivity = sensitivity

    def m(self, a, n_samples: int = 1):
        """返回选择该元素的概率"""
        # exp(epsilon * a / (2 * sensitivity)), 此时打分函数u = 1
        probabilities = np.array([np.e ** (self.eps * a / (2 * self.sensitivity))] * n_samples)
        probabilities_length = probabilities.shape[0]
        # print(probabilities_length)
        norm_probabilities = probabilities / np.linalg.norm(probabilities[probabilities.shape[0] - 1], ord=1)
        return norm_probabilities



