from PIL import Image, ImageFilter
import sys
import cv2
import random
import numpy as np
import os
import argparse

def adjust_region(image, x, y, w, h):
    # 确保坐标不越界
    x = max(0, x)
    y = max(0, y)
    w = max(0, min(image.width - x, w))
    h = max(0, min(image.height - y, h))
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


def pixelate(image: Image.Image, block_size: int = 5):
    width, height = image.size
    image = image.resize(
        (width // block_size, height // block_size), resample=Image.NEAREST
    )
    image = image.resize((width, height), resample=Image.NEAREST)
    return image


def pixelate_region(
    image: Image.Image, region_x, region_y, region_w, region_h, block_size: int = 5
):
    x, y, w, h = adjust_region(image, region_x, region_y, region_w, region_h)
    region = image.crop((x, y, x + w, y + h))
    region = pixelate(region, block_size)
    image.paste(region, (x, y, x + w, y + h))
    return image


def gaussian_blur(image: Image.Image, radius: int = 2):
    # return cv2.GaussianBlur(image, (radius * 2 + 1, radius * 2 + 1), 0)
    # image_np = np.array(image)
    # blurred_image_np = cv2.GaussianBlur(image_np, (radius*2+1, radius*2+1), 0)
    # blurred_image = Image.fromarray(blurred_image_np)
    return image.filter(ImageFilter.GaussianBlur(radius=radius))
    # return blurred_image

def gaussian_blur_region(
    image: Image.Image, region_x, region_y, region_w, region_h, radius: int = 2
):
    x, y, w, h = adjust_region(image, region_x, region_y, region_w, region_h)
    region = image.crop((x, y, x + w, y + h))
    region = gaussian_blur(region, radius)
    image.paste(region, (x, y, x + w, y + h))
    return image


def box_blur(image: Image.Image, radius: int = 2):
    return image.filter(ImageFilter.BoxBlur(radius=radius))


def box_blur_region(
    image: Image.Image, region_x, region_y, region_w, region_h, radius: int = 2
):
    x, y, w, h = adjust_region(image, region_x, region_y, region_w, region_h)
    region = image.crop((x, y, x + w, y + h))
    region = box_blur(region, radius)
    image.paste(region, (x, y, x + w, y + h))
    return image


def replace(image: Image.Image):
    width, height = image.size
    blank_image = Image.new("RGB", (width, height), color="black")
    return blank_image


def replace_region(image: Image.Image, region_x, region_y, region_w, region_h):
    x, y, w, h = adjust_region(image, region_x, region_y, region_w, region_h)
    region = image.crop((x, y, x + w, y + h))
    region = replace(region)
    image.paste(region, (x, y, x + w, y + h))
    return image


def add_color_offset(img, offset: int):
    offset = offset % 256

    image_array = np.array(img, dtype=np.uint8)
    image_array = np.mod(image_array + offset, 256).astype(np.uint8)

    return image_array


def image_add_color_offset(image: str, new_image: str, offset: int):
    img = cv2.imread(image)
    img = add_color_offset(img, offset)
    cv2.imwrite(new_image, img)


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
            new_order = random.choice(
                [
                    (b, r, g),  # 将G值替换为R值，将B值替换为G值，将R值替换为B值
                    (g, b, r),  # 其他可能的排列
                    (r, g, b),
                    (b, g, r),
                    (g, r, b),
                    (r, b, g),
                ]
            )

            # 将像素的BGR值设置为新的排列
            img[y, x] = new_order
    return img


def image_exchange_channel(image_path: str, new_image_path: str):
    img = cv2.imread(image_path)
    img = exchange_channel(img)

    # 保存处理后的图片
    cv2.imwrite(new_image_path, img)


def mean_filter(input_image_path, output_file, kernel_size):
    # 读取图片
    image = cv2.imread(input_image_path)

    if image is None:
        print("Error: Unable to read the image.")
        return
    # 进行均值滤波
    blurred_image = cv2.blur(image, (kernel_size, kernel_size))

    # 保存处理后的图像
    print(output_file)
    cv2.imwrite(output_file, blurred_image)


if __name__ == "__main__":

    # if len(sys.argv) != 5:
    #     print("Usage: python your_script.py algName input_file out_file param")
    #     sys.exit(1)

    parser = argparse.ArgumentParser()

    parser.add_argument(
        "algName",
        type=str,
        choices=[
            "pixelate",
            "pixelate_region",
            "gaussian_blur",
            "gaussian_blur_region",
            "box_blur",
            "box_blur_region",
            "replace",
            "replace_region",
            "image_add_color_offset",
            "image_exchange_channel",
            "meanValueImage",
        ],
    )
    parser.add_argument("input_file", type=str)
    parser.add_argument("out_file", type=str)
    parser.add_argument("param", type=parse_param, default=0, nargs="?")
    parser.add_argument("area", type=parse_param, default=0, nargs="?")
    args = parser.parse_args()

    # algName
    algName = args.algName

    # Load the input image
    input_image_path = args.input_file
    original_image = Image.open(input_image_path)

    # output image
    output_image_path = args.out_file

    # 操作参数
    # param = int(sys.argv[4].split(",")[-1])

    # Choose parameters for each effect
    pixelate_block_size = args.param
    gaussian_blur_radius = args.param
    box_blur_radius = args.param
    replace_region_rectangle_range = args.param
    rectangle_range = args.area
    
    color_offset = args.param
    mean_filter_kernel_size = args.param
    # Example rectangle range (x, y, width, height)

    # 执行算法,并保存
    if algName == "pixelate":
        pixelated_image = pixelate(original_image.copy(), pixelate_block_size)
        pixelated_image.save(output_image_path)
    elif algName == "pixelate_region":
        region_pixelated_image = pixelate_region(
            original_image.copy(), *rectangle_range, pixelate_block_size
        )
        region_pixelated_image.save(output_image_path)
    elif algName == "gaussian_blur":
        gaussian_blurred_image = gaussian_blur(
            original_image.copy(), gaussian_blur_radius
        )
        gaussian_blurred_image.save(output_image_path)
    elif algName == "gaussian_blur_region":
        region_gaussian_blurred_image = gaussian_blur_region(
            original_image.copy(), *rectangle_range, gaussian_blur_radius
        )
        region_gaussian_blurred_image.save(output_image_path)
    elif algName == "box_blur":
        box_blurred_image = box_blur(original_image.copy(), box_blur_radius)
        box_blurred_image.save(output_image_path)
    elif algName == "box_blur_region":
        region_box_blurred_image = box_blur_region(
            original_image.copy(), *rectangle_range, box_blur_radius
        )
        region_box_blurred_image.save(output_image_path)
    elif algName == "replace":
        replaced_image = replace(original_image.copy())
        replaced_image.save(output_image_path)
    elif algName == "replace_region":
        region_replaced_image = replace_region(original_image.copy(), *replace_region_rectangle_range)
        region_replaced_image.save(output_image_path)
    elif algName == "image_add_color_offset":
        image_add_color_offset(input_image_path, output_image_path, color_offset)
    elif algName == "image_exchange_channel":
        image_exchange_channel(input_image_path, output_image_path)
    elif algName == "meanValueImage":
        mean_filter(input_image_path, output_image_path, mean_filter_kernel_size)
