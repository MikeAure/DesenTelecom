import random
import os

class InnerProduct():
    def __init__(self):
        # stime = datetime.datetime.now()
        # print('服务端的安全内积模块的参数开始初始化：', stime)

        self.k1 = 512
        self.k2 = 200
        self.k3 = 128
        self.k4 = 128  # or 64
        # 选大素数 p,x
        #         self.p = self.RandomPrime(self.k1)
        #         self.x = self.RandomPrime(self.k2)

        script_dir = os.path.dirname(os.path.abspath(__file__))
        p_path = os.path.join(script_dir, 'p.txt')
        x_path = os.path.join(script_dir, 'x.txt')

        with open(p_path) as f1:
            self.p = int(f1.read())

        with open(x_path) as f2:
            self.x = int(f2.read())

        # select base and Max
        self.base = 0
        self.Max = 1000
        # print("p",self.p)
        # print("x",self.x)
        # etime = datetime.datetime.now()
        # print(etime)
        # print('服务端的安全内积模块的参数初始化完成：', (etime - stime).microseconds)

    def ChangeVariable(self, base, Max):
        self.base = base
        self.Max = Max

    def getVariable(self):
        return self.k1, self.k2, self.k3, self.k4, self.p, self.x

    def float2int(self, ls):
        ret = []
        for item in ls:
            ret.append(int(item * self.Max))
        return ret

    def negative2positive(self, ls):
        ret = []
        for item in ls:
            ret.append(item + self.base)
        return ret

    def mul_correct(self, num, x, y):
        n = len(x)
        ret = num / self.Max ** 2 - n * self.base ** 2
        for i in range(n):
            ret -= self.base * (x[i] + y[i])
        return ret

    def correct(self, num):
        return num / self.Max ** 2

    # 十进制转二进制函数之后计算该二进制数的位数
    def BinCount(self, x):
        return len(bin(x)) - 2  # bin(）结果是二进制的一个字符串表示，支持正负数，不过前面多了两位'0b'

    def RabinMiller(self, num):
        s = num - 1
        t = 0
        while s % 2 == 0:
            s = s // 2
            t += 1

        for trials in range(5):
            a = random.randrange(2, num - 1)
            v = pow(a, s, num)
            if v != 1:
                i = 0
                while v != (num - 1):
                    if i == t - 1:
                        return False
                    else:
                        i = i + 1
                        v = (v ** 2) % num
        return True

    def IsPrime(self, num):
        # 排除0,1和负数
        if num < 2:
            return False

        # 创建小素数的列表,可以大幅加快速度
        # 如果是小素数,那么直接返回true
        small_primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
                        101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193,
                        197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307,
                        311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421,
                        431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547,
                        557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659,
                        661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797,
                        809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929,
                        937, 941, 947, 953, 967, 971, 977, 983, 991, 997]
        if num in small_primes:
            return True

        # 如果大数是这些小素数的倍数,那么就是合数,返回false
        for prime in small_primes:
            if num % prime == 0:
                return False

        # 如果这样没有分辨出来,就一定是大整数,那么就调用rabin算法
        return self.RabinMiller(num)

    # 得到大整数,默认位数为1024  产生相应二进制位数的大素数
    def RandomPrime(self, key_size=1024):
        while True:
            num = random.randrange(2 ** (key_size - 1), 2 ** key_size)
            if self.IsPrime(num):
                return num

    #
    # #产生相应二进制位数的大素数
    # def RandomPrime(self,length):
    #     ubound = 2**length - 1
    #     lbound = 2**(length - 1)
    #     #return random_prime(ubound, False, lbound)

    # 扩展欧几里得
    def ExGcd(self, a, b, arr):
        if b == 0:
            arr[0] = 1
            arr[1] = 0
            return a
        g = self.ExGcd(b, a % b, arr)
        t = arr[0]
        arr[0] = arr[1]
        arr[1] = t - int(a / b) * arr[1]
        return g

    # 求乘法逆元
    def ModReverse(self, a, n):  # ax=1(mod n) 求a模n的乘法逆x
        arr = [0, 1]
        gcd = self.ExGcd(a, n, arr)
        if gcd == 1:
            return (arr[0] % n + n) % n
        else:
            return -1

    # 第一步，Alice 计算
    def Step1(self, a):
        n = len(a)

        # an+1 = an+2 = 0
        a.append(0)
        a.append(0)

        # 选一个随机数s属于Zp
        s = random.randint(0, self.p - 1)
        # 选 n+2 个随机数 |ci| = k3
        c = []
        for i in range(n + 2):
            c.append(random.randint(2 ^ (self.k3 - 1), 2 ^ self.k3 - 1))

        # 计算Ci
        C = []
        for i in range(n + 2):
            if a[i] == 0:
                C.append(s * c[i] % self.p)
            else:
                C.append(s * (a[i] * self.x + c[i]) % self.p)
        # 计算 |A|
        # A = 0
        # for i in range(n):
        #     A += a[i]^2

        # print('C',C)
        # 发送 <p,x,C1,C2...Cn+2> 给 Bob
        return s, C

    # 第二步,Bob计算
    def Step2(self, b, C):
        # bn+1 = bn+2 = 0

        n = len(b)
        b.append(0)
        b.append(0)

        # 产生 n+2 个随机数 |ri| = k4
        r = []
        for i in range(n + 2):
            r.append(random.randint(2 ^ (self.k4 - 1), 2 ^ self.k4 - 1))

        # 计算Di
        D = []
        for i in range(n + 2):
            if b[i] == 0:
                D.append(r[i] * C[i] % self.p)
            else:
                D.append(b[i] * self.x * C[i] % self.p)

        # 计算 |B|
        # B = 0
        # for i in range(n):
        #     B += b[i]^2

        # 计算 DSum
        DSum = 0
        for i in range(n):
            DSum += D[i]

        # Bob 将 <|B|,DSum>发送给Alice   只发送DSum
        return DSum, D

    # 第三步 Alice计算  两向量内积
    def Step3(self, DSum, s):
        # 计算 s-1 mod p
        secret = self.ModReverse(s, self.p)
        E = ((secret % self.p) * (DSum % self.p)) % self.p
        InnerProduct = (E - (E % self.x ** 2)) / self.x ** 2
        # cos = InnerProduct/(sqrt(A)*sqrt(B))
        return InnerProduct
