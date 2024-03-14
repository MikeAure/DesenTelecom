from rappor import Rappor
from utils.zero import ZeroNoisePrng


class OneTimeRappor(Rappor):
    """
    Steps 1--2 from:
        Ulfar Erlingsson, Vasyl Pihur, and Aleksandra Korolova. 2014.
        RAPPOR: Randomized Aggregatable Privacy-Preserving Ordinal Response. CCS 2014.
    """
    def __init__(self, n_hashes: int = 4, filter_size: int = 20, f: float = 0.95, prng=None):
        super().__init__(n_hashes=n_hashes, filter_size=filter_size, f=f, prng=prng)

    def m(self, a, n_samples=1):
        assert (a.shape == (1,) or a.shape == ())
        val = a.item(0)

        # populate bloom filter
        filter = self._populate_bloom_filter(val, n_samples)

        if isinstance(self.prng, ZeroNoisePrng):
            # don't perform any randomization
            return filter

        # permanent randomized response
        self._apply_permanent_randomized_response(filter)

        return filter
