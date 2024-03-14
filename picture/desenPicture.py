# -*- coding: utf-8 -*-
import sys
import os
import time
import cv2

def mean_filter(input_image_path, output_file, kernel_size):
    # 读取图片
    image = cv2.imread(input_image_path)

    if image is None:
        print("Error: Unable to read the image.")
        return
    # 进行均值滤波
    blurred_image = cv2.blur(image, (kernel_size, kernel_size))
    list = input_image_path.split(os.sep)
    imageName = list[-1]

    # 新文件存放路径
    #t = time.time()
    #newImagePath = os.path.dirname(os.path.dirname(input_image_path)) + os.sep + "desen_files" + os.sep + "desen_" + str(int(t)) + imageName
    # 保存处理后的图像
    print(output_file)
    cv2.imwrite(output_file, blurred_image)


if __name__ == "__main__":

    if len(sys.argv) != 4:
        print("Usage: python your_script.py input_file out_file param")
        sys.exit(1)

    param_list = [5, 10, 20]
    # 脱敏前文件
    input_file = sys.argv[1]
    # 脱敏参数
    level = int(sys.argv[3].split(",")[-1])
    kernel_size = param_list[level]
    output_file = sys.argv[2]
    # 调用
    mean_filter(input_file, output_file, kernel_size)
    # input_image_path = r"C:\Users\admin\Desktop\file\gademo4\raw_files\1.png"
    # mean_filter(input_image_path, 9)
