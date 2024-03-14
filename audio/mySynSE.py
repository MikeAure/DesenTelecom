import torch
import os
from RTVCnew.synthesizer.inference import Synthesizer
from RTVCnew.encoder import inference as encoder
from RTVCnew.vocoder import inference as vocoder
from pathlib import Path
import numpy as np
import librosa
import soundfile as sf
import multiprocessing
#明文向量重构语音


def save_syn(lines):
    vocoder_weights = Path("saved_models\default\\vocoder.pt")
    syn_dir = Path("saved_models\default\synthesizer.pt")
    synthesizer = Synthesizer(syn_dir)
    vocoder.load_model(vocoder_weights)
    # dict = get_texts2()
    dict = 'a golden fortune and a happy life and i love you'
    filesynHx = r'D:\QianBJ\DP\voiceDP1\0002-dp-40.wav'
    # filesynHx = r'D:\LUPANPAN\Paper_Code\DP\LBStogether\SEDPwav2\100'
    # if not os.path.exists(filesynHx):
    #     os.makedirs(filesynHx)e
    # for line in lines:
    data = lines[0].strip().replace('[ ', '').replace(' ]', '').split()

    # filename = data[0]
    # panduan=os.path.join(filesynHx,filename+'.wav')
    # #判断是否有遗漏的
    # if os.path.exists(panduan):
    #     continue
    # data = data[1:]

    out = list(map(float, data))
    text = dict
    # text=dict[filename]
    print("Synthesizing 预测语音..."+":"+str(text))
    specs = synthesizer.synthesize_spectrograms([text], [out])
    generated_wav = vocoder.infer_waveform(specs[0])
    generated_wav = np.pad(generated_wav, (0, synthesizer.sample_rate), mode="constant")
    sf.write(filesynHx, generated_wav, synthesizer.sample_rate)

#获取文本字典
def get_texts2():
    id=open(r'D:\LUPANPAN\Paper_Code\DP\LBStogether\txt\lbs_trial.txt').readlines()
    texts= open(r'D:\LUPANPAN\Paper_Code\DP\LBStogether\txt\orginalTxt.txt').readlines()
    dict={}
    for i in range(len(id)):
        dict[id[i].strip()]=texts[i].strip()
    return dict

if __name__=='__main__':
    # #批量合成语音，输入声纹文件
    file=open(r'D:\QianBJ\DP\voiceDP1\0002vector-dp.txt')
    lines=file.readlines()
    save_syn(lines)
    print('主程序')
    # synVoice()
