# -*- coding: utf-8 -*-
import cv2
import sys
import os
import time
import ffmpegcv


def desenVideo(input_video_path, output_video_path, kernel_size):
    # 输出文件路径
    print(output_video_path)
    # 打开输入视频
    input_video = cv2.VideoCapture(input_video_path)
    # 获取输入视频的基本信息
    fps = input_video.get(cv2.CAP_PROP_FPS)
    width = int(input_video.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(input_video.get(cv2.CAP_PROP_FRAME_HEIGHT))
    # frameCount = int(input_video.get(cv2.CAP_PROP_FRAME_COUNT))

    # 创建用于写入输出视频的对象   'V','P','9','0'
    # fourcc = cv2.VideoWriter_fourcc('H', '2', '6', '4')
    fourcc = cv2.VideoWriter_fourcc('V', 'P', '9', '0')
    output_video = ffmpegcv.VideoWriter(output_video_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = input_video.read()

        if not ret:
            break

        # 对每一帧进行均值滤波
        blurred_frame = cv2.blur(frame, (kernel_size, kernel_size))

        # 将滤波后的帧写入输出视频
        output_video.write(blurred_frame)
    print("success")

    # 释放资源
    input_video.release()
    output_video.release()


if __name__ == "__main__":
    if len(sys.argv) != 4:
        print("Usage: python your_script.py input_file output_file param")
        sys.exit(1)

    param_list = [9, 16, 25]
    # 脱敏前文件
    input_video_path = sys.argv[1]
    # 脱敏参数
    level = int(sys.argv[3].split(",")[-1])
    kernel_size = param_list[level]

    # 脱敏
    desenVideo(input_video_path, sys.argv[2], kernel_size)
    # input_video_path = r"C:\Users\admin\Desktop\file\gademo4\raw_files\2.mp4"
    # desenVideo(input_video_path, 9)
