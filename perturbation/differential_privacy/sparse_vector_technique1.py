"""
MIT License, Copyright (c) 2021 SRI Lab, ETH Zurich
"""

import numpy as np
from abstract import Mechanism


class SparseVectorTechnique1(Mechanism):
    """
    Alg. 1 from:
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
        print(f"x: {x}")
        rho = np.random.laplace(scale=1 / self.eps1, size=(n_samples, 1))
        print(f"rho: {rho}")
        nu = np.random.laplace(
            scale=2 * self.c / self.eps2, size=(n_samples, a.shape[0])
        )
        print(f"nu: {nu}")

        m = nu + x  # broadcasts x vertically
        print(f"m: {m}")
        cmp = m >= (rho + self.t)  # broadcasts rho horizontally
        count = np.zeros(n_samples)
        aborted = np.full(n_samples, False)
        res = cmp.astype(int)
        
        # print(f"res: {res}")

        col_idx = 0
        # Campare的结果，对于采样值大于一的情况，对矩阵的每列进行统计
        # 如当前的res为：
        # res: [[1 0 1 0 0 1]
        # [0 0 1 0 1 1]
        # [0 1 0 0 1 1]]
        for column in cmp.T:
            # print(f"column: {column}")
            # 逐列遍历，column中的每个元素代表一行
            # 如果每行中1的出现次数等于c值则置为-1
            res[aborted, col_idx] = -1
            # 统计每行出现1的次数
            count = count + column
            # print(f"count: {count}")
            # 计算该行是否丢弃
            # 若为True则丢弃，否则保持原值
            aborted = np.logical_or(aborted, count == self.c)
            # print(f"aborted: {aborted}")
            col_idx = col_idx + 1
        return res


if __name__ == "__main__":
    svt = SparseVectorTechnique1()
    print(svt.m(np.array([1.2, 2.3, 3.4, 4.4, 5.4, 6.4])))
