"""
MIT License, Copyright (c) 2021 SRI Lab, ETH Zurich
"""
import numpy as np
from abstract import Mechanism


class SnappingMechanism(Mechanism):

    def __init__(self, eps: float = 0.1, B: int = 100):
        """
        Create a safe variant of the Laplace mechanism implementing the Snapping mechanism [1] to prevent
        vulnerabilities arising from floating-point arithmetic.

        [1] Mironov, Ilya. "On Significance of the Least Significant Bits for Differential Privacy."
            In Proceedings of the 2012 ACM Conference on Computer and Communications Security - CCS ’12.
            https://doi.org/10.1145/2382196.2382264.

        Args:
            fun: The function performed before adding noise. The function must accept a 1d array and produce a scalar.
            eps: target epsilon
            B: parameter restricting the output of fun to [-B, B]
        """
        self.B = B
        print(f"B: {B}")
        # compute scale such that the resulting mechanism is eps-DP according to Theorem 1 in [1]
        print(f"eps: {eps}")
        self.scale = (1.0 + (self.B * (2 ** (-49)))) / eps
        print(f"scale: {self.scale}")
        assert(self.scale < self.B < ((2.0 ** 46) * self.scale))

        # find smallest power of 2 greater than or equal to scale
        self.Lambda = 2.0**(-20)
        while self.Lambda < self.scale:
            self.Lambda = self.Lambda * 2
        print(f"find smallest power of 2 greater than or equal to scale: {self.Lambda}")

    def clamp(self, x):
        if isinstance(x, float) or isinstance(x, int):
            return min(self.B, max(-self.B, x))
        else:
            return np.clip(x, -self.B, self.B)

    def round_to_Lambda(self, x):
        x = x / self.Lambda
        x = np.round(x)
        return x * self.Lambda

    def m(self, a: float, n_samples: int = 1):
        print(f"Snapping: {a}")
        
        loc = self.clamp(a)
        print(f"After clamp: {loc}")
        sign = 1 - (np.random.randint(2, size=n_samples) * 2)
        u = np.random.uniform(size=n_samples)
        print()
        assert(u.dtype == np.float64)   # ensure machine epsilon is 2^(-53) as required by Theorem 1 in [1]
        print(f"Intermediate Variables: {sign * self.scale * np.log(u)}")
        intermediate = loc + sign * self.scale * np.log(u)

        return self.clamp(self.round_to_Lambda(intermediate))



