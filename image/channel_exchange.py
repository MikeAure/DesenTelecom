import cv2
import random
import numpy as np
import os
import argparse

def exchange_channel(img):
    random.seed(os.urandom(128))

    # 获取图片的高度和宽度
    height, width = img.shape[:2]

    # 遍历每个像素并随机替换BGR值
    for y in range(height):
        for x in range(width):
            # 获取当前像素的BGR值
            b, g, r = img[y, x]
            # 随机选择BGR值的新排列
            new_order = random.choice([
                (b, r, g),  # 将G值替换为R值，将B值替换为G值，将R值替换为B值
                (g, b, r),  # 其他可能的排列
                (r, g, b),
                (b, g, r),
                (g, r, b),
                (r, b, g),
            ])

            # 将像素的BGR值设置为新的排列
            img[y, x] = new_order
    return img

def image_exchange_channel(image_path: str, new_image_path: str):
    img = cv2.imread(image_path)
    img = exchange_channel(img)

    # 保存处理后的图片
    cv2.imwrite(new_image_path, img)

    
def add_color_offset(img, offset: int):
    offset = offset % 256
    
    image_array = np.array(img, dtype=np.uint8)
    image_array = np.mod(image_array + offset, 256).astype(np.uint8)

    return image_array

def image_add_color_offset(image: str, new_image: str, offset: int):
    img = cv2.imread(image)
    img = add_color_offset(img, offset)
    cv2.imwrite(new_image, img)

 
def video_exchange_channel(video: str, new_video: str):
    cap = cv2.VideoCapture(video)
    height, width = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT)), int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    fps = cap.get(cv2.CAP_PROP_FPS)
    video_writer = cv2.VideoWriter(new_video, cv2.VideoWriter_fourcc(*'mp4v'), fps, (width, height))
    while True :
        success, frame = cap.read()
        if not success:
            break
        img = exchange_channel(frame)
        video_writer.write(img)
        
    video_writer.release()
    cap.release()
    
def video_add_color_offset(video: str, new_video: str, offset: int):
    cap = cv2.VideoCapture(video)
    height, width = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT)), int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    fps = cap.get(cv2.CAP_PROP_FPS)
    video_writer = cv2.VideoWriter(new_video, cv2.VideoWriter_fourcc(*'mp4v'), fps, (width, height))
    
    while True :
        success, frame = cap.read()
        if not success:
            break
        img = add_color_offset(frame, offset)
        video_writer.write(img)
        
    video_writer.release()
    cap.release()
    
        
    
if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("algo_name", type=str, choices=["image_exchange_channel", "image_add_color_offset", "video_exchange_channel", "video_add_color_offset"])
    parser.add_argument("input_file_path", type=str)
    parser.add_argument("output_file_path", type=str)
    parser.add_argument("-p", "--params", default=100, type=int)

    args = parser.parse_args()

    if args.algo_name == "image_exchange_channel":
        image_exchange_channel(args.input_file_path, args.output_file_path)
    elif args.algo_name == "image_add_color_offset":
        image_add_color_offset(args.input_file_path, args.output_file_path, args.params)
    elif args.algo_name == "video_exchange_channel":
        video_exchange_channel(args.input_file_path, args.output_file_path)
    elif args.algo_name == "video_add_color_offset":
        video_add_color_offset(args.input_file_path, args.output_file_path, args.params)