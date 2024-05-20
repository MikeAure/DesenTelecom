import sys
from pathlib import Path

sys.path.append('./face_modules/')
import torch
import torchvision.transforms as transforms
import torch.nn.functional as F
from face_modules.model import Backbone, Arcface, MobileFaceNet, Am_softmax, l2_norm
from network.AEI_Net import *
from face_modules.mtcnn import *
import cv2
import PIL.Image as Image
import numpy as np
import glob
import configparser
import os
import time
import argparse
import ffmpegcv

time1 = time.time()

# config = configparser.ConfigParser()
# config.read("config.txt")
# source_image_path = config.get("video_inference", "source_image_path")
# target_video_path = config.get("video_inference", "target_video_path")
# target_frames_path = config.get("video_inference", "target_frames_path")
# result_frames_path = config.get("video_inference", "result_frames_path")
# result_video_save_path = config.get("video_inference", "result_video_save_path")
# fps = int(config.get("video_inference", "fps"))

argparser = argparse.ArgumentParser(description='video_inference')
argparser.add_argument("target_video_path", type=str)
argparser.add_argument("source_image_path", type=str)
argparser.add_argument("result_video_save_path", type=str)
argparser.add_argument("-tf", "--target_frames", type=str, default="./target_frames/")
argparser.add_argument("-rf", "--result_frames", type=str, default="./result_frames/")

args = argparser.parse_args()

source_image_path = args.source_image_path
target_video_path = args.target_video_path
target_frames_path = args.target_frames
result_frames_path = args.result_frames
result_video_save_path = args.result_video_save_path
# fps = 30

if not Path(result_video_save_path).parent.is_dir():
    Path.mkdir(Path(result_video_save_path).parent)

if not os.path.isdir(result_frames_path) or not os.path.exists(result_frames_path):
    os.mkdir(result_frames_path)

if not os.path.isdir(target_frames_path) or not os.path.exists(target_frames_path):
    os.mkdir(target_frames_path)

print("start transfer video to frames")
# transfer video to frames
cap = cv2.VideoCapture(target_video_path)
success = cap.isOpened()
frame_count = 0
if success == False:
    print("error opening video stream or file!")
    exit(-1)
fps = cap.get(cv2.CAP_PROP_FPS)
try:
    while success:
        success, frame = cap.read()
        frame_path = os.path.join(target_frames_path, '%08d.jpg' % frame_count)
        cv2.imwrite(frame_path, frame)
        if (frame_count % 500 == 0):
            print("%dth frame has been processed" % frame_count)
        frame_count += 1
except Exception as e:
    print("video has been prcessed to frames")
cap.release()

print("start load models")
# load models
is_cuda = torch.cuda.is_available()
detector = MTCNN()
device = torch.device("cuda" if is_cuda else "cpu")
G = AEI_Net(c_id=512)
G.eval()
G.load_state_dict(torch.load('./saved_models/G_latest.pth', map_location=torch.device('cpu')))
# G = G.cuda()
G = G.cuda() if is_cuda else G.cpu()
arcface = Backbone(50, 0.6, 'ir_se').to(device)
arcface.eval()
arcface.load_state_dict(torch.load('./face_modules/model_ir_se50.pth', map_location=device), strict=False)

test_transform = transforms.Compose([
    transforms.ToTensor(),
    transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))
])

# load source image
Xs_raw = cv2.imread(source_image_path)
Xs = detector.align(Image.fromarray(Xs_raw[:, :, ::-1]), crop_size=(256, 256))
Xs_raw = np.array(Xs)[:, :, ::-1]
Xs = test_transform(Xs)
Xs = Xs.unsqueeze(0).cuda() if is_cuda else Xs.unsqueeze(0)

# calculate embedding
with torch.no_grad():
    embeds = arcface(F.interpolate(Xs[:, :, 19:237, 19:237], (112, 112), mode='bilinear', align_corners=True))

# load frames
files = glob.glob(os.path.join(target_frames_path, '*.*g'))
files.sort()
ind = 0

# generate mask
mask = np.zeros([256, 256], dtype=float)
for i in range(256):
    for j in range(256):
        dist = np.sqrt((i - 128) ** 2 + (j - 128) ** 2) / 128
        dist = np.minimum(dist, 1)
        mask[i, j] = 1 - dist
mask = cv2.dilate(mask, None, iterations=20)
size = ()
# inference
print("start inference")
for file in files:
    Xt_path = file
    Xt_raw = cv2.imread(Xt_path)
    try:
        Xt, trans_inv = detector.align(Image.fromarray(Xt_raw[:, :, ::-1]), crop_size=(256, 256), return_trans_inv=True)
    except Exception as e:
        continue

    if Xt is None:
        continue

    Xt_raw = Xt_raw.astype(float) / 255.0

    size = (Xt_raw.shape[1], Xt_raw.shape[0])

    Xt = test_transform(Xt)

    Xt = Xt.unsqueeze(0).cuda() if is_cuda else Xt.unsqueeze(0)
    
    with torch.no_grad():
        Yt, _ = G(Xt, embeds)
        Yt = Yt.squeeze().detach().cpu().numpy().transpose([1, 2, 0]) * 0.5 + 0.5
        Yt = Yt[:, :, ::-1]
        Yt_trans_inv = cv2.warpAffine(Yt, trans_inv, (np.size(Xt_raw, 1), np.size(Xt_raw, 0)), borderValue=(0, 0, 0))
        mask_ = cv2.warpAffine(mask, trans_inv, (np.size(Xt_raw, 1), np.size(Xt_raw, 0)), borderValue=(0, 0, 0))
        mask_ = np.expand_dims(mask_, 2)
        Yt_trans_inv = mask_ * Yt_trans_inv + (1 - mask_) * Xt_raw
        save_path = os.path.join(result_frames_path, '%08d.jpg' % ind)
        cv2.imwrite(save_path, Yt_trans_inv * 255)
        if (ind % 500 == 0):
            print("%dth frame has been processed" % ind)
        ind += 1

print("start generate video")
# videowriter = cv2.VideoWriter(result_video_save_path, cv2.VideoWriter_fourcc('M', 'J', 'P', 'G'), fps, size)
# videowriter = cv2.VideoWriter(result_video_save_path, cv2.VideoWriter_fourcc('H', '2', '6', '4'), fps, size)
videowriter = ffmpegcv.VideoWriter(result_video_save_path, fps=fps, resize=size)

files = glob.glob(os.path.join(result_frames_path, '*.*g'))
files.sort()
count = 0
for file in files:
    img = cv2.imread(file)
    videowriter.write(img)
    if (count % 500 == 0):
        print("%dth frames has been convert to video" % count)
    count += 1

time2 = time.time()
print("total processing time is %f" % (time2 - time1))


