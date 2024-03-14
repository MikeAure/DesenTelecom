"""
MIT License, Copyright (c) 2021 SRI Lab, ETH Zurich
"""
import numpy as np
from abstract import Mechanism


class GaussianMechanism(Mechanism):

    def __init__(self, eps: float = 0.1, delta: float = 0.001, sensitivity: float = 1):
        """
        Create a Laplace mechanism.

        Args:
            fun: The function performed before adding noise. The function must accept a 1d array and produce a scalar.
            eps: target epsilon
        """
        self.sigma = np.sqrt(2 * np.log(1.25 / delta)) * sensitivity / eps

    def m(self, a: float, n_samples: int = 1):
        loc = a
        return np.random.normal(loc=loc, scale=self.sigma, size=n_samples)
