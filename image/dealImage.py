from PIL import Image, ImageFilter
import sys

def pixelate(image: Image.Image, block_size: int = 5):
    width, height = image.size
    image = image.resize((width // block_size, height // block_size), resample=Image.NEAREST)
    image = image.resize((width, height), resample=Image.NEAREST)
    return image


def pixelate_region(image: Image.Image, region_x, region_y, region_w, region_h, block_size: int = 5):
    x, y, w, h = region_x, region_y, region_w, region_h
    region = image.crop((x, y, x + w, y + h))
    region = pixelate(region, block_size)
    image.paste(region, (x, y, x + w, y + h))
    return image

def gaussian_blur(image: Image.Image, radius: int = 2):
    return image.filter(ImageFilter.GaussianBlur(radius=radius))


def gaussian_blur_region(image: Image.Image, region_x, region_y, region_w, region_h, radius: int = 2):
    x, y, w, h = region_x, region_y, region_w, region_h
    region = image.crop((x, y, x + w, y + h))
    region = gaussian_blur(region, radius)
    image.paste(region, (x, y, x + w, y + h))
    return image


def box_blur(image: Image.Image, radius: int = 2):
    return image.filter(ImageFilter.BoxBlur(radius=radius))


def box_blur_region(image: Image.Image, region_x, region_y, region_w, region_h, radius: int = 2):
    x, y, w, h = region_x, region_y, region_w, region_h
    region = image.crop((x, y, x + w, y + h))
    region = box_blur(region, radius)
    image.paste(region, (x, y, x + w, y + h))
    return image


def replace(image: Image.Image):
    width, height = image.size
    blank_image = Image.new("RGB", (width, height), color="black")
    return blank_image


def replace_region(image: Image.Image, region_x, region_y, region_w, region_h):
    x, y, w, h = region_x, region_y, region_w, region_h
    region = image.crop((x, y, x + w, y + h))
    region = replace(region)
    image.paste(region, (x, y, x + w, y + h))
    return image


if __name__ == '__main__':

    if len(sys.argv) != 5:
        print("Usage: python your_script.py algName input_file out_file param")
        sys.exit(1)

    # algName
    algName = sys.argv[1]

    # Load the input image
    input_image_path = sys.argv[2]
    original_image = Image.open(input_image_path)

    # output image
    output_image_path = sys.argv[3]

    # 操作参数
    param = int(sys.argv[4].split(",")[-1])

    # Choose parameters for each effect
    pixelate_block_size = [5, 10, 15][param]
    gaussian_blur_radius = [2, 4, 8][param]
    box_blur_radius = [2, 4, 8][param]
    rectangle_range = [(100, 100, 200, 200), (50, 50, 300, 300), (25, 25, 400, 400)][param]  # Example rectangle range (x, y, width, height)

    # 执行算法,并保存
    if algName == "pixelate":
        pixelated_image = pixelate(original_image.copy(), pixelate_block_size)
        pixelated_image.save(output_image_path)
    elif algName == "pixelate_region":
        region_pixelated_image = pixelate_region(original_image.copy(), *rectangle_range, pixelate_block_size)
        region_pixelated_image.save(output_image_path)
    elif algName == "gaussian_blur":
        gaussian_blurred_image = gaussian_blur(original_image.copy(), gaussian_blur_radius)
        gaussian_blurred_image.save(output_image_path)
    elif algName == "gaussian_blur_region":
        region_gaussian_blurred_image = gaussian_blur_region(original_image.copy(), *rectangle_range,
                                                             gaussian_blur_radius)
        region_gaussian_blurred_image.save(output_image_path)
    elif algName == "box_blur":
        box_blurred_image = box_blur(original_image.copy(), box_blur_radius)
        box_blurred_image.save(output_image_path)
    elif algName == "box_blur_region":
        region_box_blurred_image = box_blur_region(original_image.copy(), *rectangle_range, box_blur_radius)
        region_box_blurred_image.save(output_image_path)
    elif algName == "replace":
        replaced_image = replace(original_image.copy())
        replaced_image.save(output_image_path)
    elif algName == "replace_region":
        region_replaced_image = replace_region(original_image.copy(), *rectangle_range)
        region_replaced_image.save(output_image_path)
















