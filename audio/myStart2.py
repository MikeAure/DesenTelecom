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
class D2E_NetL1L(torch.nn.Module):
    def __init__(self, in_dim, n_hidden_1, n_hidden_2, out_dim):
        super(D2E_NetL1L, self).__init__()
        self.layer1 = torch.nn.Sequential(torch.nn.Linear(in_dim, n_hidden_1), torch.nn.ReLU(True))
        self.layer2 = torch.nn.Sequential(torch.nn.Linear(n_hidden_1, n_hidden_2), torch.nn.ReLU(True))
        self.layer3 = torch.nn.Sequential(torch.nn.Linear(n_hidden_2, out_dim))

    def forward(self, x):
        x = x / torch.norm(x, p=2)
        x = self.layer1(x)
        x = self.layer2(x)
        x = self.layer3(x)
        x = x / torch.norm(x, p=2)
        return x


def save_syn(lines, template, filesynHx):
    encoder_weights = Path(os.path.dirname(
        os.path.dirname(filesynHx)) + os.sep + 'audio' + os.sep + "saved_models/default/encoder.pt")  # 没有用到
    encoder.load_model(encoder_weights)
    vocoder_weights = Path(
        os.path.dirname(os.path.dirname(filesynHx)) + os.sep + 'audio' + os.sep + "saved_models/default/vocoder.pt")
    syn_dir = Path(
        os.path.dirname(os.path.dirname(filesynHx)) + os.sep + 'audio' + os.sep + "saved_models/default/synthesizer.pt")
    encoder.load_model(encoder_weights)
    synthesizer = Synthesizer(syn_dir)
    vocoder.load_model(vocoder_weights)
    model = D2E_NetL1L(512, 400, 300, 256)
    checkpoint = torch.load(os.path.dirname(
        os.path.dirname(filesynHx)) + os.sep + 'audio' + os.sep + 'checkpointD2E/modelMSE_epoch_148600.ckpt')
    model.load_state_dict(checkpoint['model'])

    data = lines[0].strip().replace('[', '').replace(']', '').strip().split(' ')   #data/lines是文件名+声纹

    out = list(map(float, data))
    out = torch.tensor(out)
    pred = model(out)       #声纹进映射模型
    pred = pred.tolist()
    print(len(pred))
    text = template
    print("Synthesizing predict voice..."+":"+text)
    specs = synthesizer.synthesize_spectrograms([text], [pred])     #文件: 嵌入向量   #合成器？
    generated_wav = vocoder.infer_waveform(specs[0])    #声码器？
    generated_wav = np.pad(generated_wav, (0, synthesizer.sample_rate), mode="constant")
    sf.write(filesynHx, generated_wav, synthesizer.sample_rate)

def synVoice():
    dirname = r'D:\LUPANPAN\Paper_Code\DP\lbspeech\test-clean'
    filter = [".txt"]
    for _, subdir, _ in os.walk(dirname):
        for sub1 in subdir:
            subdirPath1 = os.path.join(dirname, sub1)
            for _, subdir2, _ in os.walk(subdirPath1):
                for sub2 in subdir2:
                    subdirPath2 = os.path.join(subdirPath1, sub2)
                    for _, _, file_name_list in os.walk(subdirPath2):
                        txt_name=sub1+'-'+sub2+'.trans.txt'
                        texts = open(os.path.join(subdirPath2,txt_name)).readlines()
                        dicts={}
                        for line in texts:
                            data = line.strip().split()
                            dicts[data[0]]=' '.join(data[1:]).strip()
                        for filename in file_name_list:
                            if filename.endswith('-dp01.txt'):
                                apath = os.path.join(subdirPath2, filename)  # 合并成一个完整路径
                                lines = open(apath).readlines()
                                cTime=int(len(lines)/3)
                                # p1 = multiprocessing.Process(target=save_syn, name='p1', args=(lines[:cTime],dicts,))
                                # p2 = multiprocessing.Process(target=save_syn, name='p2', args=(lines[cTime:2*cTime],dicts,))
                                # p3 = multiprocessing.Process(target=save_syn, name='p3',args=(lines[2*cTime:],dicts,))
                                # p1.start()
                                # p2.start()
                                # p3.start()
                                save_syn(lines,dicts)
#获取文本
def get_texts(filename):
    texts= open(r'D:\LUPANPAN\Paper_Code\DP\lbspeech\SuperWav\human_text.txt').readlines()
    for line in texts:
        data=line.strip().split()
        if filename== data[0]:
            return ' '.join(data[1:]).strip()
        else:
            assert '无该用户的文本信息'


#获取文本字典
def get_texts2():
    id=open(r'D:\LUPANPAN\Paper_Code\DP\LBStogether\txt\lbs_trial.txt').readlines()
    texts= open(r'D:\LUPANPAN\Paper_Code\DP\LBStogether\txt\orginalTxt.txt').readlines()
    dict={}
    for i in range(len(id)):
        dict[id[i].strip()]=texts[i].strip()
    return dict

# if __name__=='__main__':
#     # #批量合成语音，输入声纹文件
#     file=open(r'D:\QianBJ\DP\voiceDP1\myresults\0001vector.txt')
#     lines=file.readlines()
#     save_syn(lines)
#     print('主程序')
#     # synVoice()
