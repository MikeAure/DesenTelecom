from abc import ABC, abstractmethod
from typing import Union

import numpy as np


class Mechanism(ABC):
    """
    Base class for differentially private mechanisms.
    """

    @abstractmethod
    def m(self, a: Union[float, np.ndarray], n_samples: int = 1) -> np.ndarray:
        """
        Runs the mechanism for the given input multiple times to get multiple output samples.
        Return value is the perturbation of input.

        Args:
            a: 1d array representing the mechanism input (number of components depends on mechanism)
            n_samples: number of output samples to produce

        Returns:
            1d array of shape (n_samples,) if mechanism output is 1-dimensional
            nd array of shape (n_samples, d) if mechanism output is d-dimensional
        """
        pass
