import numpy as np

from abstract import Mechanism


class SparseVectorTechnique2(Mechanism):
    """
    Alg. 2 from:
        M. Lyu, D. Su, and N. Li. 2017.
        Understanding the Sparse Vector Technique for Differential Privacy.
        Proceedings of the VLDB Endowment.
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
                -1 = ABORTED;
        """

        # columns: queries
        # rows: samples
        x = np.atleast_2d(a)
        print(f"array: {x}")
        n_queries = a.shape[0]

        rho = np.random.laplace(scale=self.c / self.eps1, size=(n_samples,))
        print(f"noise adds to T: {rho}")
        nu = np.random.laplace(
            scale=2 * self.c / self.eps2, size=(n_samples, n_queries)
        )
        print(f"noise adds to array: {nu}")

        m = nu + x  # broadcasts x vertically

        count = np.zeros(n_samples)
        aborted = np.full(n_samples, False)
        res = np.empty(shape=m.shape, dtype=int)
        for col_idx in range(0, n_queries):
            cmp = m[:, col_idx] >= (rho + self.t)
            res[:, col_idx] = cmp.astype(int)
            res[aborted, col_idx] = -1
            count = count + cmp

            # update rho whenever we answer TRUE
            new_rho = np.random.laplace(scale=self.c / self.eps1, size=(n_samples,))
            rho[cmp] = new_rho[cmp]

            aborted = np.logical_or(aborted, count == self.c)
        # print(res)
        return res
