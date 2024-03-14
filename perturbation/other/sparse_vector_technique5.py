import numpy as np

from abstract import Mechanism


class SparseVectorTechnique5(Mechanism):
    """
    Alg. 5 from:
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
            ndarray of shape (n_samples, a.shape[0]) with entries
                1 = TRUE;
                0 = FALSE;
        """

        # columns: queries
        # rows: samples
        x = np.atleast_2d(a)

        rho = np.random.laplace(scale=1 / self.eps1, size=(n_samples, 1))

        cmp = x >= (rho + self.t)   # broadcasts rho horizontally, x vertically
        return cmp.astype(int)
