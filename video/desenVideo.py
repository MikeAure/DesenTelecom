import numpy as np
import sys
import cv2           # Importing the OpenCV library for computer vision tasks
from cvzone.SelfiSegmentationModule import SelfiSegmentation
import ffmpegcv
import argparse


def adjust_region(frame, x, y, w, h):
    # 确保坐标不越界
    height, width, _  = frame.shape
    x = max(0, x)
    y = max(0, y)
    w = max(0, min(width - x, w))
    h = max(0, min(height - y, h))
    return x, y, w, h


def parse_param(input_string):
    """Parse input parameter as either an integer or a tuple."""
    if "," in input_string:
        # Split the string by ',' and convert to integers
        try:
            return tuple(map(int, input_string.split(",")))
        except ValueError:
            raise argparse.ArgumentTypeError(
                "Invalid tuple format. Use comma-separated integers, e.g., 100,100,200,200"
            )
    else:
        # Parse as a single integer
        try:
            return int(input_string)
        except ValueError:
            raise argparse.ArgumentTypeError("Parameter must be an integer or a tuple.")


def add_color_offset(img, offset: int):
    offset = offset % 256

    image_array = np.array(img, dtype=np.uint8)
    image_array = np.mod(image_array + offset, 256).astype(np.uint8)

    return image_array
def pixelate_frame(frame, block_size: int = 5):
    height, width, _ = frame.shape
    small_frame = cv2.resize(frame, (width // block_size, height // block_size))
    pixelated_frame = cv2.resize(small_frame, (width, height), interpolation=cv2.INTER_NEAREST)
    return pixelated_frame


def pixelate_region_frame(frame, region_x, region_y, region_w, region_h, block_size: int = 5):
    x, y, w, h = region_x, region_y, region_w, region_h
    region = frame[y:y + h, x:x + w]
    pixelated_region = pixelate_frame(region, block_size)
    frame[y:y + h, x:x + w] = pixelated_region
    return frame


def gaussian_blur_frame(frame, radius: int = 2):
    return cv2.GaussianBlur(frame, (radius * 2 + 1, radius * 2 + 1), 0)


def gaussian_blur_region_frame(frame, region_x, region_y, region_w, region_h, radius: int = 2):
    x, y, w, h = region_x, region_y, region_w, region_h
    region = frame[y:y + h, x:x + w]
    blurred_region = gaussian_blur_frame(region, radius)
    frame[y:y + h, x:x + w] = blurred_region
    return frame


def box_blur_frame(frame, radius: int = 2):
    return cv2.blur(frame, (radius * 2 + 1, radius * 2 + 1))


def box_blur_region_frame(frame, region_x, region_y, region_w, region_h, radius: int = 2):
    x, y, w, h = region_x, region_y, region_w, region_h
    region = frame[y:y + h, x:x + w]
    blurred_region = box_blur_frame(region, radius)
    frame[y:y + h, x:x + w] = blurred_region
    return frame


def replace_frame(frame):
    return np.zeros_like(frame)


def replace_region_frame(frame, region_x, region_y, region_w, region_h):
    frame[region_y:region_y + region_h, region_x:region_x + region_w] = replace_frame(
        frame[region_y:region_y + region_h, region_x:region_x + region_w])
    return frame


def pixelate_video(input_video, output_path, block_size: int = 5):
    cap = cv2.VideoCapture(input_video)
    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS)

    # fourcc = cv2.VideoWriter.fourcc('H', '2', '6', '4')
    # fourcc = cv2.VideoWriter_fourcc('V', 'P', '9', '0')
    fourcc = cv2.VideoWriter.fourcc(*'avc1')

    out = ffmpegcv.VideoWriter(output_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        processed_frame = pixelate_frame(frame, block_size)
        out.write(processed_frame)

    cap.release()
    out.release()
    return output_path


def gaussian_blur_video(input_video, output_path, radius: int = 2):
    cap = cv2.VideoCapture(input_video)
    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS)

    # fourcc = cv2.VideoWriter.fourcc('H', '2', '6', '4')
    # fourcc = cv2.VideoWriter.fourcc('V', 'P', '9', '0')
    fourcc = cv2.VideoWriter.fourcc(*'avc1')

    out = ffmpegcv.VideoWriter(output_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        processed_frame = gaussian_blur_frame(frame, radius)
        out.write(processed_frame)

    cap.release()
    out.release()
    return output_path


def box_blur_video(input_video, output_path, radius: int = 2):
    cap = cv2.VideoCapture(input_video)
    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS)

    # fourcc = cv2.VideoWriter.fourcc('H', '2', '6', '4'
    # fourcc = cv2.VideoWriter.fourcc('V', 'P', '9', '0')
    fourcc = cv2.VideoWriter.fourcc(*'avc1')


    out = ffmpegcv.VideoWriter(output_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        processed_frame = box_blur_frame(frame, radius)
        out.write(processed_frame)

    cap.release()
    out.release()
    return output_path


def replace_video(input_video, output_path):
    cap = cv2.VideoCapture(input_video)
    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS)

    # fourcc = cv2.VideoWriter.fourcc('H', '2', '6', '4')
    # fourcc = cv2.VideoWriter.fourcc('V', 'P', '9', '0')
    fourcc = cv2.VideoWriter.fourcc(*'avc1')


    out = ffmpegcv.VideoWriter(output_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        processed_frame = replace_frame(frame)
        out.write(processed_frame)

    cap.release()
    out.release()
    return output_path


def pixelate_region_video(input_video, output_path, region_x, region_y, region_w, region_h, block_size: int = 5):
    cap = cv2.VideoCapture(input_video)
    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS)

    # fourcc = cv2.VideoWriter.fourcc('H', '2', '6', '4')
    # fourcc = cv2.VideoWriter.fourcc('V', 'P', '9', '0')
    fourcc = cv2.VideoWriter.fourcc(*'avc1')


    out = ffmpegcv.VideoWriter(output_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        processed_frame = pixelate_region_frame(frame, region_x, region_y, region_w, region_h, block_size)
        out.write(processed_frame)

    cap.release()
    out.release()
    return output_path


def gaussian_blur_region_video(input_video, output_path, region_x, region_y, region_w, region_h, radius: int = 2):
    cap = cv2.VideoCapture(input_video)
    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS)

    # fourcc = cv2.VideoWriter.fourcc('H', '2', '6', '4')
    # fourcc = cv2.VideoWriter.fourcc('V', 'P', '9', '0')
    fourcc = cv2.VideoWriter.fourcc(*'avc1')


    out = ffmpegcv.VideoWriter(output_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        processed_frame = gaussian_blur_region_frame(frame, region_x, region_y, region_w, region_h, radius)
        out.write(processed_frame)

    cap.release()
    out.release()
    return output_path


def box_blur_region_video(input_video, output_path, region_x, region_y, region_w, region_h, radius: int = 2):
    cap = cv2.VideoCapture(input_video)
    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS)

    # fourcc = cv2.VideoWriter.fourcc('H', '2', '6', '4')
    # fourcc = cv2.VideoWriter.fourcc('V', 'P', '9', '0')
    fourcc = cv2.VideoWriter.fourcc(*'avc1')


    out = ffmpegcv.VideoWriter(output_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        processed_frame = box_blur_region_frame(frame, region_x, region_y, region_w, region_h, radius)
        out.write(processed_frame)

    cap.release()
    out.release()
    return output_path


def replace_region_video(input_video, output_path, region_x, region_y, region_w, region_h):
    cap = cv2.VideoCapture(input_video)
    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS)

    # fourcc = cv2.VideoWriter.fourcc('H', '2', '6', '4')
    # fourcc = cv2.VideoWriter.fourcc('V', 'P', '9', '0')
    fourcc = cv2.VideoWriter.fourcc(*'avc1')


    out = ffmpegcv.VideoWriter(output_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        processed_frame = replace_region_frame(frame, region_x, region_y, region_w, region_h)
        out.write(processed_frame)

    cap.release()
    out.release()
    return output_path

def meanValueVideo(input_video_path, output_video_path, kernel_size):
    # 打开输入视频
    input_video = cv2.VideoCapture(input_video_path)
    # 获取输入视频的基本信息
    fps = input_video.get(cv2.CAP_PROP_FPS)
    width = int(input_video.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(input_video.get(cv2.CAP_PROP_FRAME_HEIGHT))
    # frameCount = int(input_video.get(cv2.CAP_PROP_FRAME_COUNT))

    # 创建用于写入输出视频的对象   'V','P','9','0'
    # fourcc = cv2.VideoWriter.fourcc('V', 'P', '9', '0')

    out = ffmpegcv.VideoWriter(output_video_path, fps=fps, resize=(width, height))

    while True:
        ret, frame = input_video.read()

        if not ret:
            break

        # 对每一帧进行均值滤波
        blurred_frame = cv2.blur(frame, (kernel_size, kernel_size))

        # 将滤波后的帧写入输出视频
        out.write(blurred_frame)
    print("success")

    # 释放资源
    input_video.release()
    out.release()
    return output_video_path

def video_add_color_offset(video: str, new_video: str, offset: int):
    cap = cv2.VideoCapture(video)
    height, width = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT)), int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    fps = cap.get(cv2.CAP_PROP_FPS)
    # video_writer = cv2.VideoWriter(new_video, cv2.VideoWriter_fourcc('H', '2', '6', '4'), fps, (width, height))

    out = ffmpegcv.VideoWriter(new_video, fps=fps, resize=(width, height))

    while True :
        success, frame = cap.read()
        if not success:
            break
        img = add_color_offset(frame, offset)
        out.write(img)

    out.release()
    cap.release()
    return new_video

def substitude_background(video: str, new_video:str, background: str, cut_threshold: float) :
    segmentor = SelfiSegmentation(model=0)

    segmentor = SelfiSegmentation()

    new_background = cv2.imread(background)
    video = cv2.VideoCapture(video)

    fps = video.get(cv2.CAP_PROP_FPS)

    video_width, video_height = int(video.get(cv2.CAP_PROP_FRAME_WIDTH)), int(video.get(cv2.CAP_PROP_FRAME_HEIGHT))
    # out = cv2.VideoWriter(new_video, cv2.VideoWriter_fourcc('V', 'P', '9', '0'), fps, (video_width, video_height))

    out = ffmpegcv.VideoWriter(new_video, fps=fps, resize=(video_width, video_height))

    new_background = cv2.resize(new_background, (video_width, video_height), interpolation=cv2.INTER_AREA)

    while True:
        success, frame = video.read()
        if not success:
            break
        frame_out = segmentor.removeBG(frame, imgBg=new_background, cutThreshold=cut_threshold)
        out.write(frame_out)
        # cv2.imshow("replace bg", frame_out)
        # if cv2.waitKey(1) & 0xFF == ord('q'):
        #     break

    video.release()
    out.release()
    return new_video

if __name__ == '__main__':
    parser = argparse.ArgumentParser()

    parser.add_argument(
        "algName",
        type=str,
        choices=[
            "pixelate_video",
            "pixelate_region_video",
            "gaussian_blur_video",
            "gaussian_blur_region_video",
            "box_blur_video",
            "box_blur_region_video",
            "replace_video",
            "replace_region_video",
            "meanValueVideo",
            "video_add_color_offset",
        ],
    )
    parser.add_argument("input_file", type=str)
    parser.add_argument("out_file", type=str)
    parser.add_argument("param", type=parse_param, default=0, nargs="?")
    parser.add_argument("area", type=parse_param, default=0, nargs="?")
    args = parser.parse_args()
    # Choose parameters for each effect
    pixelize_block_size = args.param
    blur_radius = args.param
    kernel_size = args.param
    color_offsets = args.param
    outline_params = args.param
    replace_region_video_rectangle_range = args.param
    rectangle_range = args.area # Example rectangle range (x, y, width, height)

    # 执行算法,并保存
    if args.algName == "pixelate_video":
        pixelate_video(args.input_file, args.out_file, block_size=pixelize_block_size)
    elif args.algName == "pixelate_region_video":
        pixelate_region_video(args.input_file, args.out_file, *rectangle_range, block_size=pixelize_block_size)
    elif args.algName == "gaussian_blur_video":
        gaussian_blur_video(args.input_file, args.out_file, radius=blur_radius)
    elif args.algName == "gaussian_blur_region_video":
        gaussian_blur_region_video(args.input_file, args.out_file, *rectangle_range, radius=blur_radius)
    elif args.algName == "box_blur_video":
        box_blur_video(args.input_file, args.out_file, radius=blur_radius)
    elif args.algName == "box_blur_region_video":
        box_blur_region_video(args.input_file, args.out_file, *rectangle_range, radius=blur_radius)
    elif args.algName == "replace_video":
        replace_video(args.input_file, args.out_file)
    elif args.algName == "replace_region_video":
        replace_region_video(args.input_file, args.out_file, *replace_region_video_rectangle_range)
    elif args.algName == "meanValueVideo":
        meanValueVideo(args.input_file, args.out_file, kernel_size)
    elif args.algName == "video_add_color_offset":
        video_add_color_offset(args.input_file, args.out_file, color_offsets)

    # Destroy OpenCV windows
    cv2.destroyAllWindows()
