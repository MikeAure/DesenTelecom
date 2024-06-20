import numpy as np

from abstract import Mechanism


class NumericalSVT(Mechanism):
    """
    Numerical Sparse Vector Technique from:
        Y. Wang, Z. Ding, G. Wang, D. Kifer, D. Zhang. 2019.
        Proving differential privacy with shadow execution.
        PLDI 2019.
    """

    def __init__(self, eps: float = 0.1, c: int = 2, t: float = 1.0):
        self.eps = eps
        self.c = c
        self.t = t

    def m(self, a, n_samples: int = 1):
        """
        Args:
            a: 1d array of query results (sensitivity 1)

        Returns:
            float ndarray of shape (n_samples, a.shape[0]) with special entry
                -1000.0 = ABORTED
        """

        # columns: queries
        # rows: samples
        x = np.atleast_2d(a)

        rho1 = np.random.laplace(scale=3 / self.eps, size=(n_samples, 1))
        rho2 = np.random.laplace(
            scale=6 * self.c / self.eps, size=(n_samples, a.shape[0])
        )
        rho3 = np.random.laplace(
            scale=3 * self.c / self.eps, size=(n_samples, a.shape[0])
        )

        m = rho2 + x  # broadcasts x vertically
        cmp = m >= (self.t + rho1)  # broadcasts rho1 horizontally
        z = rho3 + x  # broadcasts x vertically

        count = np.zeros(n_samples)
        aborted = np.full(n_samples, False)
        res = np.zeros(shape=(n_samples, a.shape[0]))

        for col_idx in range(0, a.shape[0]):
            above = cmp[:, col_idx]
            res[above, col_idx] = z[above, col_idx]
            count = count + above
            res[aborted, col_idx] = -1000.0
            aborted = np.logical_or(aborted, count == self.c)
        return res
