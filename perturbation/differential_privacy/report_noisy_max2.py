import numpy as np

from abstract import Mechanism


class ReportNoisyMax2(Mechanism):
    """
    Alg. 6 from:
        Zeyu Ding, YuxinWang, GuanhongWang, Danfeng Zhang, and Daniel Kifer. 2018.
        Detecting Violations of Differential Privacy. CCS 2018.
    """

    def __init__(self, eps: float = 0.1, sensitivity: float = 1):
        self.sensitivity = sensitivity
        self.eps = eps

    def m(self, a, n_samples: int = 1):
        v = np.atleast_2d(a)

        # each row in m is one sample
        m = v + np.random.exponential(scale=self.sensitivity/self.eps, size=(n_samples, a.shape[0]))
        return np.argmax(m, axis=1)
