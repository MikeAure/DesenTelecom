import sys
import torch

sys.path.append('./face_modules/')
import torchvision.transforms as transforms
from face_modules.model import Backbone
from network.AEI_Net import *
from face_modules.mtcnn import *
import cv2
import PIL.Image as Image
import numpy as np
import configparser
import argparse
from pathlib import Path
# config = configparser.ConfigParser()
# config.read("config.txt")
# Xs_path = config.get("image_inference", "source_image_path")
# Xt_path = config.get("image_inference", "target_image_path")
# save_path = config.get("image_inference", "result_image_save_path")

argparser = argparse.ArgumentParser(description='image_inference')
argparser.add_argument("target_image_path", type=str)
argparser.add_argument("source_image_path", type=str)
argparser.add_argument("result_image_save_path", type=str)

args = argparser.parse_args()

Xs_path = args.source_image_path
Xt_path = args.target_image_path
save_path = args.result_image_save_path

if not Path(save_path).parent.is_dir():
    Path.mkdir(Path(save_path).parent)

detector = MTCNN()
is_cuda = torch.cuda.is_available()
device = torch.device("cuda" if is_cuda else "cpu")
G = AEI_Net(c_id=512)
G.eval()
G.load_state_dict(torch.load('./saved_models/G_latest.pth', map_location=torch.device('cpu')))
G = G.cuda() if is_cuda else G.cpu()

arcface = Backbone(50, 0.6, 'ir_se').to(device)
arcface.eval()
arcface.load_state_dict(torch.load('./face_modules/model_ir_se50.pth', map_location=device), strict=False)

test_transform = transforms.Compose([
    transforms.ToTensor(),
    transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))
])

Xs_raw = cv2.imread(Xs_path)
try:
    Xs = detector.align(Image.fromarray(Xs_raw[:, :, ::-1]), crop_size=(256, 256))
except Exception as e:
    print('the source image is wrong, please change the image')
Xs_raw = np.array(Xs)[:, :, ::-1]
Xs = test_transform(Xs)
Xs = Xs.unsqueeze(0).cuda() if is_cuda else Xs.unsqueeze(0)

with torch.no_grad():
    embeds = arcface(F.interpolate(Xs[:, :, 19:237, 19:237], (112, 112), mode='bilinear', align_corners=True))
    # embeds = arcface(F.interpolate(Xs, (112, 112), mode='bilinear', align_corners=True))

Xt_raw = cv2.imread(Xt_path)
try:
    Xt, trans_inv = detector.align(Image.fromarray(Xt_raw[:, :, ::-1]), crop_size=(256, 256), return_trans_inv=True)
except Exception as e:
    print('the target image is wrong, please change the image')
Xt_raw = Xt_raw.astype(float) / 255.0
Xt = test_transform(Xt)
Xt = Xt.unsqueeze(0).cuda() if is_cuda else Xt.unsqueeze(0)

mask = np.zeros([256, 256], dtype=float)
for i in range(256):
    for j in range(256):
        dist = np.sqrt((i - 128) ** 2 + (j - 128) ** 2) / 128
        dist = np.minimum(dist, 1)
        mask[i, j] = 1 - dist
mask = cv2.dilate(mask, None, iterations=20)

with torch.no_grad():
    Yt, _ = G(Xt, embeds)
    Yt = Yt.squeeze().detach().cpu().numpy().transpose([1, 2, 0]) * 0.5 + 0.5
    Yt = Yt[:, :, ::-1]
    Yt_trans_inv = cv2.warpAffine(Yt, trans_inv, (np.size(Xt_raw, 1), np.size(Xt_raw, 0)), borderValue=(0, 0, 0))
    mask_ = cv2.warpAffine(mask, trans_inv, (np.size(Xt_raw, 1), np.size(Xt_raw, 0)), borderValue=(0, 0, 0))
    mask_ = np.expand_dims(mask_, 2)
    Yt_trans_inv = mask_ * Yt_trans_inv + (1 - mask_) * Xt_raw
    # cv2.imshow('image', Yt_trans_inv)
    cv2.imwrite(save_path, Yt_trans_inv * 255)

    Xt_raw = cv2.imread(Xt_path)
    try:
        Xt = detector.align(Image.fromarray(Xt_raw[:, :, ::-1]), crop_size=(256, 256))
    except Exception as e:
        print('the source image is wrong, please change the image')
    Xt_raw = np.array(Xt)[:, :, ::-1]
    Xt = test_transform(Xt)
    Xt = Xt.unsqueeze(0).cuda() if is_cuda else Xt.unsqueeze(0)

    Yt_raw = cv2.imread(save_path)
    try:
        Yt = detector.align(Image.fromarray(Yt_raw[:, :, ::-1]), crop_size=(256, 256))
    except Exception as e:
        print('the source image is wrong, please change the image')
    Yt_raw = np.array(Yt)[:, :, ::-1]
    Yt = test_transform(Yt)
    Yt = Yt.unsqueeze(0).cuda() if is_cuda else Yt.unsqueeze(0)
    with torch.no_grad():
        embeds_Yt = arcface(F.interpolate(Yt[:, :, 19:237, 19:237], (112, 112), mode='bilinear', align_corners=True))
        embeds_Xt = arcface(F.interpolate(Xt[:, :, 19:237, 19:237], (112, 112), mode='bilinear', align_corners=True))
    # print(embeds1.shape)
    # print(embeds1)
    print(embeds_Xt.reshape((512,)).dot(embeds_Yt.reshape((512,))).item())
    print(embeds_Yt.reshape((512,)).dot(embeds.reshape((512,))).item())
    # cv2.imshow('image',Yt)
    # cv2.imwrite(save_path,Yt*255)

    print("the result image has been saved")
    # cv2.waitKey(0)
