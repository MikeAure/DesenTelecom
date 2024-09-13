import numpy as np
from scipy.spatial import distance
from scipy.stats import matrix_normal
import cv2
import sys


def coder_distance(eps, x, z):
    hist_x, _ = np.histogram(x, bins=1024)
    hist_z, _ = np.histogram(z, bins=1024)
    d = np.linalg.norm(x - z) / (np.prod(x.shape)) + distance.jensenshannon(
        hist_x, hist_z
    )
    return np.exp(-eps * d)


def add_noise(img, eps):
    print(f"epsilon: {eps}")
    distra = matrix_normal(mean=img)

    while True:
        z = np.random.normal(0, 10 / eps, size=img.shape)
        print(f"noise add to image: {z}")
        noisy_img = img + z
        u = np.random.uniform(0, 1)
        ratio = coder_distance(eps, img, noisy_img) / (2.5 * distra.pdf(noisy_img))
        print(f"ratio: {ratio}")
        if u <= ratio:
            noisy_img = noisy_img
            break

    return noisy_img


def gray_to_color(src, src_gray):
    B = src[:, :, 0]
    G = src[:, :, 1]
    R = src[:, :, 2]
    g = src_gray[:]
    p = 0.2989
    q = 0.5870
    t = 0.1140
    B_new = (g - p * R - q * G) / t
    B_new = np.uint8(B_new)
    src_new = np.zeros((src.shape)).astype("uint8")
    src_new[:, :, 0] = B_new
    src_new[:, :, 1] = G
    src_new[:, :, 2] = R

    return src_new


def main(file_path, selection):
    params = [1.0, 0.5, 0.1]
    img = cv2.imread(file_path, cv2.IMREAD_UNCHANGED)

    if len(img.shape) == 2:
        return add_noise(img, params[selection])
    else:
        arr1 = img[:, :, 0]
        arr2 = img[:, :, 1]
        arr3 = img[:, :, 2]

        arr1_noise = add_noise(arr1, params[selection])
        arr2_noise = add_noise(arr2, params[selection])
        arr3_noise = add_noise(arr3, params[selection])

        return np.concatenate(
            (
                arr1_noise[:, :, np.newaxis],
                arr2_noise[:, :, np.newaxis],
                arr3_noise[:, :, np.newaxis],
            ),
            axis=2,
        )


if __name__ == "__main__":
    if len(sys.argv) != 4:
        print("Usage: python your_script.py input_file out_file param")
        sys.exit(1)

    noisy_img = main(sys.argv[1], int(sys.argv[3]))
    cv2.imwrite(sys.argv[2], noisy_img)
    print("dp over")
