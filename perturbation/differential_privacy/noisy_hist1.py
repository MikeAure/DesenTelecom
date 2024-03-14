"""
MIT License, Copyright (c) 2021 SRI Lab, ETH Zurich
"""
import numpy as np
from abstract import Mechanism


class NoisyHist1(Mechanism):
    """
    Alg. 9 from:
        Zeyu Ding, YuxinWang, GuanhongWang, Danfeng Zhang, and Daniel Kifer. 2018.
        Detecting Violations of Differential Privacy. CCS 2018.
    """

    def __init__(self, eps: float = 0.1, sensitivity: float = 1):
        self.eps = eps
        self.sensitivity = sensitivity

    def m(self, a, n_samples: int = 1):
        l = a.shape[0]
        v = np.atleast_2d(a)

        # each row in m is one sample
        m = v + np.random.laplace(scale=self.sensitivity/self.eps, size=(n_samples, l))
        return m


