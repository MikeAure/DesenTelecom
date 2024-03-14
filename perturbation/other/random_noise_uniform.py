"""
MIT License, Copyright (c) 2021 SRI Lab, ETH Zurich
"""
import numpy as np

from abstract import Mechanism


class RandomNoiseUniform(Mechanism):

    def m(self, a: float, n_samples: int = 1):
        return a + np.random.random(size=n_samples)
