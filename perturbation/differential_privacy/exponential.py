"""
MIT License, Copyright (c) 2021 SRI Lab, ETH Zurich
"""
import numpy as np
from abstract import Mechanism


class ExponentialMechanism(Mechanism):

    def __init__(self, eps: float, sensitivity: float = 1):
        self.eps = eps
        self.sensitivity = sensitivity

    def m(self, a: float, n_samples: int = 1):
        """返回选择该元素的概率"""
        return np.array([np.e ** (self.eps * a / (2 * self.sensitivity))] * n_samples)



