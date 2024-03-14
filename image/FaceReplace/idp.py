import sys
import time

import torch

sys.path.append('./face_modules/')
import torchvision.transforms as transforms
import torch.nn.functional as F
from face_modules.model import Backbone, Arcface, MobileFaceNet, Am_softmax, l2_norm
from network.AEI_Net import *
from face_modules.mtcnn import *
import cv2
import PIL.Image as Image
import numpy as np


def selectU(n):
    ur = np.random.randn(n)
    l2 = np.linalg.norm(ur, keepdims=True)
    unit = ur / l2
    return unit


# step2
def selectR(n, l):
    r = np.random.gamma(n, l, 1)
    return r


def laplace_mechanism(data, sensitivity, epsilon):
    # data = data.cpu().detach().numpy()
    # beta = sensitivity / epsilon
    # noise = np.random.laplace(0, beta, len(data))
    # print(noise)
    noise = selectU(512) * selectR(512, epsilon)
    #print(noise)
    return torch.from_numpy((data.cpu().numpy().reshape((512,)) + noise).reshape((1, 512)))


Xs_path = "./dataset/image/1.png"
save_path = "./result/" + str(time.time()) + ".jpg"
sensitivity = 3
epsilon = 3

detector = MTCNN()
device = torch.device('cuda')
G = AEI_Net(c_id=512)
G.eval()
G.load_state_dict(torch.load('./saved_models/G_latest.pth', map_location=torch.device('cuda')))
G = G.cuda()

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
Xs = Xs.unsqueeze(0).cuda()

with torch.no_grad():
    embeds = arcface(F.interpolate(Xs[:, :, 19:237, 19:237], (112, 112), mode='bilinear', align_corners=True))
    print(embeds.shape)
    print(embeds)
    # embeds = arcface(F.interpolate(Xs, (112, 112), mode='bilinear', align_corners=True))

embeds = laplace_mechanism(embeds, sensitivity, epsilon)
if embeds.device != G.parameters().__next__().device:
    embeds = embeds.to(G.parameters().__next__().device)
print(embeds.shape)
print(embeds)

# print(embeds)
Xt_raw = cv2.imread(Xs_path)
try:
    Xt, trans_inv = detector.align(Image.fromarray(Xt_raw[:, :, ::-1]), crop_size=(256, 256), return_trans_inv=True)
except Exception as e:
    print('the target image is wrong, please change the image')
Xt_raw = Xt_raw.astype(np.float) / 255.0
Xt = test_transform(Xt)
Xt = Xt.unsqueeze(0).cuda()
print(Xt.shape)

mask = np.zeros([256, 256], dtype=np.float)
for i in range(256):
    for j in range(256):
        dist = np.sqrt((i - 128) ** 2 + (j - 128) ** 2) / 128
        dist = np.minimum(dist, 1)
        mask[i, j] = 1 - dist
mask = cv2.dilate(mask, None, iterations=20)

if embeds.dtype != Xt.dtype:
    embeds = embeds.to(Xt.dtype)

with torch.no_grad():
    Yt, _ = G(Xt, embeds)
    Yt = Yt.squeeze().detach().cpu().numpy().transpose([1, 2, 0]) * 0.5 + 0.5
    Yt = Yt[:, :, ::-1]
    Yt_trans_inv = cv2.warpAffine(Yt, trans_inv, (np.size(Xt_raw, 1), np.size(Xt_raw, 0)), borderValue=(0, 0, 0))
    mask_ = cv2.warpAffine(mask, trans_inv, (np.size(Xt_raw, 1), np.size(Xt_raw, 0)), borderValue=(0, 0, 0))
    mask_ = np.expand_dims(mask_, 2)
    Yt_trans_inv = mask_ * Yt_trans_inv + (1 - mask_) * Xt_raw
    cv2.imshow('image', Yt_trans_inv)
    cv2.imwrite(save_path, Yt_trans_inv * 255)

    Xt_raw = cv2.imread(Xs_path)
    try:
        Xt = detector.align(Image.fromarray(Xt_raw[:, :, ::-1]), crop_size=(256, 256))
    except Exception as e:
        print('the source1 image is wrong, please change the image')
    Xt_raw = np.array(Xt)[:, :, ::-1]
    Xt = test_transform(Xt)
    Xt = Xt.unsqueeze(0).cuda()

    Yt_raw = cv2.imread(save_path)
    try:
        Yt = detector.align(Image.fromarray(Yt_raw[:, :, ::-1]), crop_size=(256, 256))
    except Exception as e:
        print('the source2 image is wrong, please change the image')
    # print(Yt.shape)
    #Yt_raw = np.array(Yt)[:, :, ::-1]
    Yt = test_transform(Yt)
    Yt = Yt.unsqueeze(0).cuda()
    with torch.no_grad():
        embeds = arcface(F.interpolate(Yt[:, :, 19:237, 19:237], (112, 112), mode='bilinear', align_corners=True))
        embeds1 = arcface(F.interpolate(Xt[:, :, 19:237, 19:237], (112, 112), mode='bilinear', align_corners=True))
    print(embeds1.shape)
    print(embeds1)
    print(embeds1.reshape((512,)).dot(embeds.reshape((512,))).item())
    # cv2.imshow('image',Yt)
    # cv2.imwrite(save_path,Yt*255)

    print("the result image has been saved")
    cv2.waitKey(0)
