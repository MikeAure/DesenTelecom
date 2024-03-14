# x = np.array([3,4])
# print(np.linalg.norm(x))

# print("image file path /storage/emulated/0/data/io.github.note286.agree/files/Db.jpg")
# x = np.random.random(10)
# y = np.random.random(10)
# print(x)
# print(y)
# print(type(x))
#
# # 根据公式求解欧氏距离
# d1 = np.sqrt(np.sum(np.square(x - y)))
# print(d1)

def formatPrint(C):
    print("[", end="")
    for i in range(len(C)):
        if (i == len(C) / 2):
            print()
        if (i == len(C) - 1):
            print(C[i], end="]\n")
        else:
            print(C[i], end=",")


def interceptString(s):
    ret = ""
    for i in s:
        if (i == "@"):
            return ret
        ret += i


a = [1, 2, 3, 4, 5, 6, 7, 8]
formatPrint(a)

s = "123"
print(interceptString(s))
