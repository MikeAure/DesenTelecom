from model import *
import torch
from voiceprintplt import draw_voiceprint1, draw_voiceprint2
import librosa
from scipy.fftpack import fft, ifft, dct, idct
import numpy as np
import wave
import os


# print(device)
# print(speakermodel)
def extract_voiceprint(audiofilename, sr):
    dim_voiceprint = 512
    num_class = 1211
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    # print("loading")
    # the_model = DeepSpeakerModel()

    speakermodel = DeepSpeakerModel(dim_voiceprint, num_class)
    speakermodel.load_state_dict(
        torch.load(
            os.path.dirname(os.path.dirname(audiofilename)) + os.sep + 'audio' + os.sep + 'new_deepspeaker_dict.pkl',
            map_location='cpu'))  # .to(device)) #, map_location='cpu')
    speakermodel = speakermodel.to(device)
    speakermodel.eval()


    embedding_size = 512
    # ==========提取声纹代码(封装成函数，输入audiofilename，输出voiceprint)=========
    # wav文件名
    audio_data, sr = librosa.load(audiofilename, sr=16000, mono=True)
    # 这里增加删除语音文件代码，提取的filter_banks特征
    frames_features = read_audiotoMFB(audio_data)
    # print(frames_features.shape)
    extract_input = truncatedinputfromMFB(1)
    network_inputs_np = extract_input(frames_features)  # frames_slice
    # print(network_inputs_np.shape)
    transformTensor = totensor()
    network_inputs = transformTensor(network_inputs_np)
    # print(network_inputs.size())
    fbank = torch.FloatTensor(network_inputs).to(device)
    fbank = torch.unsqueeze(fbank, 0)
    # print(mfcc)
    with torch.no_grad():
        voiceprint = speakermodel(fbank)
    # 随机生成一个512维的tensor数组
    # voiceprint = torch.randn(1,512)
    # print(voiceprint.size())
    return voiceprint


# def judge():
# flag = sck.identifier[1]
# voiceprint_a = ertract_voiceprint(sck.filename,sr=16000)
# if(flag == 0):
#     save(sck.identifier[0],voiceprint=voiceprint_a)
# else:
#     voiceprint_e = findvoice(sck.identifier[0])
#     Euclideandist = PairwiseDistance(2)
#     distance = Euclideandist(voiceprint_e, voiceprint_a)
#     if distance <= 0.71:
#         ret = True
#     else:
#         ret = False
# 欧式距离
def oup(a, b):
    sum = 0
    for i, item in enumerate(a):
        sum = sum + (item - b[i]) * (item - b[i])
    return sum

# #提取经过映射合成的声纹向量
# test=extract_voiceprint('I2E_syn_voice\\id10272.wav',sr=16000).squeeze(dim=0).tolist()
# testa=extract_voiceprint('I2E_syn_voice\\id10272.wav',sr=16000).squeeze(dim=0).tolist()
# testb=extract_voiceprint('I2E_syn_voice\\id10272b.wav',sr=16000).squeeze(dim=0).tolist()
# testb1=extract_voiceprint('I2E_syn_voice\\id10270b.wav',sr=16000).squeeze(dim=0).tolist()
# lingnoise=extract_voiceprint('I2E_syn_voice\\id10270.wav',sr=16000).squeeze(dim=0).tolist()
# one=extract_voiceprint('0303.wav',sr=16000).squeeze(dim=0).tolist()
# two=extract_voiceprint('stft100.wav',sr=16000).squeeze(dim=0).tolist()
# print(test)
# print(testa)
# print(testb)
# res=oup(test,testb)
# resa=oup(test,testa)
# resab=oup(testa,testb)
# resbb=oup(testb,testb1)
# print('vs与原始：',res)
# print('vs与I2E：',resa)
# print('I2E与原始：',resab)
# print('原始与原始：',resbb)
# DP处理结果
# 提取经过DP处理合成的声纹向量
# test=extract_voiceprint('DP_syn_voice\\id10270.wav',sr=16000).squeeze(dim=0).tolist()

# testS=extract_voiceprint('DP_syn_voice\\id10270_raws.wav',sr=16000).squeeze(dim=0).tolist()
# testDP=extract_voiceprint('DP_syn_voice\\id10270_DPraw1.wav',sr=16000).squeeze(dim=0).tolist()
# ress=oup(test,testS)
# resdp=oup(test,testDP)
# ressp=oup(testS,testDP)

# print('合成：',testS)
# print('DP:',testDP)
# print('VS与原始：',ress)
# print('DP与原始：',resdp)
# print('vs与DP：',ressp)

# mymymymymymymymymymymymy
# test = extract_voiceprint('myresults/0001.wav', sr=16000).squeeze(dim=0).tolist()
# print('原始：', test)
# fo = open("myresults/0001vector.txt", "w")
# fo.write(str(test))
# fo.close()


# print('傅里叶加噪后结果1：',res1)
# print('直接在数值上加噪结果2：',res2)
# print('结果3：',res3)
# print('ling4：',res4)
# dcty=dct(res)
# print("离散余弦变换：")
# print(dcty)
# 加噪,scale值越大，加的噪声就越大，0.001距离比较小
# s=np.random.laplace(0,0.1,512)
# print("噪声：")
# print(s)
# noise=s+dcty
# print("加噪后的离散傅里叶系数：")
# print(noise)
# print("逆离散余弦变换：")
# # ires=idct(dcty)/1000
# # print(idct(dcty)/1000)
# ires=idct(noise)/1000
# print(ires)
# draw_voiceprint1(res)
# draw_voiceprint1(ires)
# 检验合成后的数据

# resn=res+s
# print("加噪后的数据：",resn)
# draw_voiceprint1(res)
# draw_voiceprint1(resn)
