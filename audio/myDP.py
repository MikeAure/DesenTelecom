# dp实现，分为两步，step1：通过高斯变量来得出在N为球体表面的均匀采取一个点；step2：通过伽马分布来得出一个半径
# step1
import numpy as np
import os


def selectU(n):
    ur = np.random.randn(n)
    l2 = np.linalg.norm(ur, keepdims=True)
    unit = ur / l2
    return unit


# step2
def selectR(n, l):
    r = np.random.gamma(n, l, 1)
    return r


# L即为差分中的隐私预算
def DPfun(x, n, l):
    u = selectU(n)
    r = selectR(n, 1 / l)
    print("半径长度：", r)
    randx = x + u * r
    # randx=randx/np.linalg.norm(randx,keepdims=True)
    return randx


# print(DPfun([0,0,0,0,0],5,0.1))

# def gamma():
#     #次随机分布函数与论文中的相同
#     r = np.random.gamma(256,1, 1)
#     print(r)
# gamma()
def protect(vectorpath, dppath):
    vectors = open(vectorpath).readlines()
    file = open(dppath, mode='a')
    for line in vectors:
        data = line.strip().replace('[', '').replace(']', '').split()
        filename = data[0]
        vector = np.array(list(map(float, data[1:])))
        dp_vector = DPfun(vector, 512, 1)
        dp_vector = list(np.around(dp_vector, 7))
        a = filename + '  ' + str(dp_vector).replace(',', '').replace('[', '[ ').replace(']', ' ]') + '\n'
        file.write(a)
    file.close()


def dpVector():
    dirname = r'D:\LUPANPAN\Paper_Code\DP\lbspeech\test-clean'
    filter = [".txt"]
    for _, subdir, _ in os.walk(dirname):
        for sub1 in subdir:
            subdirPath1 = os.path.join(dirname, sub1)
            for _, subdir2, _ in os.walk(subdirPath1):
                for sub2 in subdir2:
                    subdirPath2 = os.path.join(subdirPath1, sub2)
                    for _, _, file_name_list in os.walk(subdirPath2):
                        dp_vector_txt_path = os.path.join(subdirPath2, sub1 + '-' + sub2 + '-dp1.txt')
                        dp_vector_txt = open(dp_vector_txt_path, mode='a')
                        for filename in file_name_list:
                            if filename.endswith('-vector.txt'):
                                apath = os.path.join(subdirPath2, filename)  # 合并成一个完整路径
                                vectors = open(apath).readlines()
                                for line in vectors:
                                    data = line.strip().replace('[', '').replace(']', '').split()
                                    filename = data[0]
                                    vector = np.array(list(map(float, data[1:])))
                                    dp_vector = DPfun(vector, 512, 1)
                                    dp_vector = list(dp_vector)
                                    # dp_vector = list(np.around(dp_vector, 7))
                                    a = filename + '  ' + str(dp_vector).replace(',', '').replace('[', '[ ').replace(
                                        ']', ' ]') + '\n'
                                    dp_vector_txt.write(a)
                        dp_vector_txt.close()


def dpVector2(vectorPath, dp_vector_txt_path, budget):
    dp_vector_txt = open(dp_vector_txt_path, mode='w+')
    vector_txt = open(vectorPath, mode='r')
    vectors = vector_txt.readlines()
    # for line in vectors:
    data = vectors[0].strip().replace('[', '').replace(']', '').replace(',', '').split()
    # filename = data[0]
    # print(data)
    # print(type(data))
    # data = list[data]
    vector = np.array(list(map(float, data)))
    dp_vector = list(DPfun(vector, 512, budget))
    # dp_vector = list(np.around(dp_vector, 7))
    # b = str(dp_vector)
    a = str(dp_vector).replace('\n', '').replace(',', '').replace('[', '[ ').replace(']', ' ]')
    # print(type(a))
    vector_txt.close()
    dp_vector_txt.write(a)
    dp_vector_txt.close()
    
def preprocess(vectorPath, dp_vector_txt_path):
    dp_vector_txt = open(dp_vector_txt_path, mode='w+')
    vector_txt = open(vectorPath, mode='r')
    vectors = vector_txt.readlines()
    # for line in vectors:
    data = vectors[0].strip().replace('[', '').replace(']', '').replace(',', '').split()
    # filename = data[0]
    # print(data)
    # print(type(data))
    # data = list[data]
    vector = np.array(list(map(float, data)))
    dp_vector = list(vector)
    # dp_vector = list(np.around(dp_vector, 7))
    # b = str(dp_vector)
    a = str(dp_vector).replace('\n', '').replace(',', '').replace('[', '[ ').replace(']', ' ]')
    # print(type(a))
    vector_txt.close()
    dp_vector_txt.write(a)
    dp_vector_txt.close()


if __name__ == "__main__":
    print(selectU(512))
    print(selectR(512, 1))
