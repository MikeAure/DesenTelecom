import numpy as np

from abstract import Mechanism


class SparseVectorTechnique3(Mechanism):
    """
    Alg. 3 from:
        M. Lyu, D. Su, and N. Li. 2017.
        Understanding the Sparse Vector Technique for Differential Privacy.
        Proceedings of the VLDB Endowment.
    INCORRECT
    """

    def __init__(self, eps: float = 0.1, c: int = 2, t: float = 1.0):
        self.eps1 = eps / 2.0
        self.eps2 = eps - self.eps1
        self.c = c  # maximum number of queries answered with 1
        self.t = t

    def m(self, a, n_samples: int = 1):
        """
        Args:
            a: 1d array of query results (sensitivity 1)

        Returns:
            float ndarray of shape (n_samples, a.shape[0]) with special entries
                -1000.0 = FALSE;
                -2000.0 = ABORTED;
        """

        # columns: queries
        # rows: samples
        x = np.atleast_2d(a)

        rho = np.random.laplace(scale=1 / self.eps1, size=(n_samples, 1))
        nu = np.random.laplace(scale=self.c / self.eps2, size=(n_samples, a.shape[0]))

        m = nu + x  # broadcasts x vertically
        cmp = m >= (rho + self.t)   # broadcasts rho horizontally
        count = np.zeros(n_samples)
        aborted = np.full(n_samples, False)

        res = m
        res[np.logical_not(cmp)] = -1000.0

        col_idx = 0
        for column in cmp.T:
            res[aborted, col_idx] = -2000.0
            count = count + column
            aborted = np.logical_or(aborted, count == self.c)
            col_idx = col_idx + 1
        return res
