"""
MIT License, Copyright (c) 2021 SRI Lab, ETH Zurich
"""
import numpy as np

from abstract import Mechanism


class RandomNoiseGaussian(Mechanism):

    def m(self, a: float, n_samples: int = 1):
        return np.random.normal(loc=a, size=n_samples)
