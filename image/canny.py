import cv2
import numpy as np
import os
import sys
import math
import pandas as pd
import copy
import tqdm
from multiprocessing import Pool
import imageio


class ImageClass:
    """Stores the paths to images for a given class"""

    def __init__(self, name, image_paths):
        self.name = name
        self.image_paths = image_paths

    def __str__(self):
        return self.name + ", " + str(len(self.image_paths)) + " images"

    def __len__(self):
        return len(self.image_paths)


def get_dataset(path, has_class_directories=True):
    dataset = []
    # 简单理解就是规范化linux和windows下的路径名
    path_exp = os.path.expanduser(path)
    classes = [
        path
        for path in os.listdir(path_exp)
        if os.path.isdir(os.path.join(path_exp, path))
    ]
    classes.sort()
    # 把姓名都放在列表class这个列表里
    nrof_classes = len(classes)
    for i in range(nrof_classes):
        class_name = classes[i]
        facedir = os.path.join(path_exp, class_name)
        image_paths = get_image_paths(facedir)
        dataset.append(ImageClass(class_name, image_paths))

    return dataset


def get_image_paths(facedir):
    image_paths = []  # 有几张图片就有几个元素
    if os.path.isdir(facedir):
        # 如果这个姓名下有图像，则读取图像列表
        images = os.listdir(facedir)
        image_paths = [os.path.join(facedir, img) for img in images]
    return image_paths


# 去除噪音 - 使用 5x5 的高斯滤波器
def smooth(img_gray):

    # 生成高斯滤波器
    """
    要生成一个 (2k+1)x(2k+1) 的高斯滤波器，滤波器的各个元素计算公式如下：

    H[i, j] = (1/(2*pi*sigma**2))*exp(-1/2*sigma**2((i-k-1)**2 + (j-k-1)**2))
    """
    sigma1 = sigma2 = 1.4
    gau_sum = 0
    gaussian = np.zeros([5, 5])
    for i in range(5):
        for j in range(5):
            gaussian[i, j] = math.exp(
                (-1 / (2 * sigma1 * sigma2)) * (np.square(i - 3) + np.square(j - 3))
            ) / (2 * math.pi * sigma1 * sigma2)
            gau_sum = gau_sum + gaussian[i, j]

    # 归一化处理
    gaussian = gaussian / gau_sum

    # 高斯滤波
    W, H = img_gray.shape
    new_gray = np.zeros([W - 5, H - 5])

    for i in range(W - 5):
        for j in range(H - 5):
            new_gray[i, j] = np.sum(img_gray[i : i + 5, j : j + 5] * gaussian)

    return new_gray


def gradients(new_gray):
    """
    :type: image which after smooth
    :rtype:
        dx: gradient in the x direction
        dy: gradient in the y direction
        M: gradient magnitude
        theta: gradient direction
    """

    W, H = new_gray.shape
    dx = np.zeros([W - 1, H - 1])
    dy = np.zeros([W - 1, H - 1])
    M = np.zeros([W - 1, H - 1])
    theta = np.zeros([W - 1, H - 1])

    for i in range(W - 1):
        for j in range(H - 1):
            dx[i, j] = int(new_gray[i + 1, j]) - int(new_gray[i, j])
            dy[i, j] = int(new_gray[i, j + 1]) - int(new_gray[i, j])
            # 图像梯度幅值作为图像强度值
            M[i, j] = np.sqrt(np.square(dx[i, j]) + np.square(dy[i, j]))
            # 计算  θ - artan(dx/dy)
            theta[i, j] = math.atan(dx[i, j] / (dy[i, j] + 0.000000001))

    return dx, dy, M, theta


def NMS(M, dx, dy):

    d = np.copy(M)
    W, H = M.shape
    NMS = np.copy(d)
    NMS[0, :] = NMS[W - 1, :] = NMS[:, 0] = NMS[:, H - 1] = 0

    for i in range(1, W - 1):
        for j in range(1, H - 1):

            # 如果当前梯度为0，该点就不是边缘点
            if M[i, j] == 0:
                NMS[i, j] = 0

            else:
                gradX = dx[i, j]  # 当前点 x 方向导数
                gradY = dy[i, j]  # 当前点 y 方向导数
                gradTemp = d[i, j]  # 当前梯度点

                # 如果 y 方向梯度值比较大，说明导数方向趋向于 y 分量
                if np.abs(gradY) > np.abs(gradX):
                    weight = np.abs(gradX) / np.abs(gradY)  # 权重
                    grad2 = d[i - 1, j]
                    grad4 = d[i + 1, j]

                    # 如果 x, y 方向导数符号一致
                    # 像素点位置关系
                    # g1 g2
                    #    c
                    #    g4 g3
                    if gradX * gradY > 0:
                        grad1 = d[i - 1, j - 1]
                        grad3 = d[i + 1, j + 1]

                    # 如果 x，y 方向导数符号相反
                    # 像素点位置关系
                    #    g2 g1
                    #    c
                    # g3 g4
                    else:
                        grad1 = d[i - 1, j + 1]
                        grad3 = d[i + 1, j - 1]

                # 如果 x 方向梯度值比较大
                else:
                    weight = np.abs(gradY) / np.abs(gradX)
                    grad2 = d[i, j - 1]
                    grad4 = d[i, j + 1]

                    # 如果 x, y 方向导数符号一致
                    # 像素点位置关系
                    #      g3
                    # g2 c g4
                    # g1
                    if gradX * gradY > 0:

                        grad1 = d[i + 1, j - 1]
                        grad3 = d[i - 1, j + 1]

                    # 如果 x，y 方向导数符号相反
                    # 像素点位置关系
                    # g1
                    # g2 c g4
                    #      g3
                    else:
                        grad1 = d[i - 1, j - 1]
                        grad3 = d[i + 1, j + 1]

                # 利用 grad1-grad4 对梯度进行插值
                gradTemp1 = weight * grad1 + (1 - weight) * grad2
                gradTemp2 = weight * grad3 + (1 - weight) * grad4

                # 当前像素的梯度是局部的最大值，可能是边缘点
                if gradTemp >= gradTemp1 and gradTemp >= gradTemp2:
                    NMS[i, j] = gradTemp

                else:
                    # 不可能是边缘点
                    NMS[i, j] = 0

    return NMS


def image_detect(image_1, k):
    W, H = image_1.shape
    k_half = int((k - 1) / 2)
    img_pad = np.pad(image_1, ((k_half, k_half), (k_half, k_half)), "edge")
    ave_i = np.zeros([W, H])
    for i in range(W):
        for j in range(H):
            roi = img_pad[i : i + k, j : j + k].copy()
            ave_i[i, j] = np.sum(roi)
            ave_i[i, j] = ave_i[i, j] - (k * k - 1) * img_pad[i + k_half, j + k_half]

    return ave_i


def add_noise1(dx, dy, image_1):
    W, H = dx.shape
    noise_x = np.zeros(W * H)
    noise_y = np.zeros(W * H)
    eps = 2  # 隐私预算
    for i in range(W * H):  # 生成噪声
        noise_x[i] = np.random.laplace(0, (dx.max() - dx.min()) / eps, 1)
        noise_y[i] = np.random.laplace(0, (dy.max() - dy.min()) / eps, 1)
    dx_list = dx.flatten()  # 拉平差值矩阵
    dy_list = dy.flatten()
    dx_index = np.argsort(dx_list)  # 排序索引
    dy_index = np.argsort(dy_list)
    noise_x_index = np.argsort(noise_x)
    noise_y_index = np.argsort(noise_y)
    for i in range(W * H):  # 将对应大小的噪声添加到差值上
        x1, y1 = divmod(np.array(dx_index)[i], W)
        x2, y2 = divmod(np.array(dy_index)[i], H)
        dx[x1, y1] = dx[x1, y1] + noise_x[np.array(noise_x_index)[i]]
        dy[x2, y2] = dy[x2, y2] + noise_y[np.array(noise_y_index)[i]]
    image_noise = image_1[:W, :H] + dx + dy
    print("image_1:", image_1[30:40, 30:40])
    print("image_nosie", image_noise[30:40, 30:40])
    image_noise = cv2.normalize(image_noise, None, 0, 255, cv2.NORM_MINMAX)
    image_noise = image_noise.astype(np.uint8)
    return image_noise


def add_noise2(image_1, k=7, epsilon=10):
    print(f"epsilon: {epsilon}")
    ave_1 = image_detect(image_1, k)  # 设置k值
    W, H = ave_1.shape
    image_1 = image_1.astype(int)
    noise_x = np.zeros(W * H)
    eps = 10  # 隐私预算
    for i in range(W * H):  # 生成噪声
        noise_x[i] = np.random.laplace(0, (image_1.max() - image_1.min()) / epsilon, 1)
    ave_1_list = ave_1.flatten()
    ave_1_index = np.argsort(ave_1_list)
    noise_x_index = np.argsort(noise_x)
    for i in range(W * H):
        x1, y1 = divmod(np.array(ave_1_index)[i], W)
        image_1[x1, y1] = image_1[x1, y1] + noise_x[np.array(noise_x_index)[i]]
    image_noise = cv2.normalize(image_1, None, 0, 255, cv2.NORM_MINMAX)
    image_noise = image_noise.astype(np.uint8)
    print(f"image_noise: {image_noise}")
    return image_noise


def double_threshold(NMS):

    W, H = NMS.shape
    DT = np.zeros([W, H])

    # 定义高低阈值
    TL = 0.2 * np.max(NMS)
    TH = 0.4 * np.max(NMS)

    for i in range(1, W - 1):
        for j in range(1, H - 1):
            # 双阈值选取
            if NMS[i, j] < TL:
                DT[i, j] = 0

            elif NMS[i, j] > TH:
                DT[i, j] = 255

            # 连接
            elif (NMS[i - 1, j - 1 : j + 1] < TH).any() or (
                NMS[i + 1, j - 1 : j + 1].any() or (NMS[i, [j - 1, j + 1]] < TH).any()
            ):
                DT[i, j] = 255

    return DT


def to_rgb(img):
    w, h = img.shape
    ret = np.empty((w, h, 3), dtype=np.uint8)
    ret[:, :, 0] = ret[:, :, 1] = ret[:, :, 2] = img
    return ret


def noise_dataset(input_dir, output_dir="./"):
    dataset = get_dataset(input_dir)
    dataset_list = []
    for sample in dataset:  # 数据集中的每个文件夹
        image_path_list = []
        for image_path in sample.image_paths:
            image_path_list.append(image_path)
        dataset_list.append(
            [sample.name, image_path_list, output_dir]
        )  # cls是[姓名，[图片1路径，图片2路径。。。]]
    with Pool(10) as p:
        # muti_result = p.starmap(
        #     mutiprocess, tqdm.tqdm(dataset_list, total=len(dataset_list))
        # )
        muti_result = p.starmap(mutiprocess, dataset_list)


def mutiprocess(name, path_list, output_dir):

    nrof_images_total = 0  # 图片总数计数
    nrof_successfully_aligned = 0  # 成功处理的图片计数
    output_class_dir = os.path.join(output_dir, name)

    if not os.path.exists(output_class_dir):
        os.makedirs(output_class_dir)
    for image_path in path_list:  # 文件夹中的每张图片
        nrof_images_total += 1
        filename = os.path.splitext(os.path.split(image_path)[1])[0]
        output_filename = os.path.join(output_class_dir, filename + ".png")
        if not os.path.exists(output_filename):
            try:
                img = imageio.imread(image_path)  # 读取图片
            except (IOError, ValueError, IndexError) as e:
                errorMessage = "{}: {}".format(image_path, e)
                print(errorMessage)
                continue
            else:
                if img.ndim < 2:
                    print('Unable to align "%s"' % image_path)
                    # text_file.write('%s\n' % (output_filename))
                    continue
                if img.ndim == 2:
                    img = to_rgb(img)
                img = img[:, :, 0:3]
                # plt.imshow(img)
                # plt.show()
            for i in range(3):
                img[:, :, i] = add_noise2(img[:, :, i])
            nrof_successfully_aligned += 1
            # print('count:',nrof_successfully_aligned)
            filename_base, file_extension = os.path.splitext(output_filename)
            # if args.detect_multiple_faces:
            #     output_filename_n = "{}_{}{}".format(filename_base, i, file_extension)
            # else:
            output_filename_n = "{}{}".format(filename_base, file_extension)
            imageio.imsave(output_filename_n, img)


if __name__ == "__main__":
    
    if len(sys.argv) != 4:
        print("Usage: python your_script.py input_file out_file param")
        sys.exit(1)
    

    img_path = sys.argv[1]
    output_path = sys.argv[2]
    param = sys.argv[3]
    
    epsilon = [10, 1, 0.1][int(param)]
    image_color = cv2.imread(img_path,cv2.IMREAD_COLOR)
    image_color_noise = copy.copy(image_color)
    # edges = cv2.Canny(image_1, 138, 200)
    for i in range(3):
        image_smooth = smooth(image_color[:,:,i])
        dx,dy,M,theta = gradients(image_smooth)
        NMS_1 = NMS(M,dx,dy)
        edges = double_threshold(NMS_1)
        #image_noise = add_noise1(dx,dy,image_smooth)
        image_color_noise[:,:,i] = add_noise2(image_color[:,:,i], epsilon=epsilon)
        #cv2.imshow('smooth', image_smooth)
        # cv2.imshow('edges', edges)
        # cv2.imshow('image_noise', image_color_noise[:,:,i]) #图片矩阵数据格式得是unit8才能show
    # cv2.imshow('origin', image_color)
    # cv2.imshow('noise',image_color_noise)
    # cv2.waitKey()
    #np.savetxt("matrix.txt", image_color_noise,fmt ='%f')
    cv2.imwrite(output_path, image_color_noise)
    #file1.writelines(noise)
    #file1.close()
    # input_path = "./test_images"
    # output_path = "../facenet/understand_facenet-master/understand_facenet/data/lfw_lap"
    # noise_dataset(input_path)
