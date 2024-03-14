"""
MIT License, Copyright (c) 2021 SRI Lab, ETH Zurich
"""
import numpy as np

from abstract import Mechanism


class RandomNoiseLaplace(Mechanism):

    def m(self, a: float, n_samples: int = 1):
        return np.random.laplace(loc=a, size=n_samples)
