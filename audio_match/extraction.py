import librosa

from audio_processing import read_audiotoMFB, truncatedinputfromMFB, totensor
from model import *
import os

# import wave

# import sck

# def init_model():
dim_voiceprint = 512
num_class = 1211
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
# print("loading")
# the_model = DeepSpeakerModel()

speakermodel = DeepSpeakerModel(dim_voiceprint, num_class)
script_dir = os.path.dirname(os.path.abspath(__file__))
pkl_path = os.path.join(script_dir, 'new_deepspeaker_dict.pkl')
speakermodel.load_state_dict(
    torch.load(pkl_path, map_location='cpu'))  # .to(device)) #, map_location='cpu')
speakermodel = speakermodel.to(device)
speakermodel.eval()

embedding_size = 512


# print(device)
# print(speakermodel)
def extract_voiceprint(audiofilename, sr):
    # ==========提取声纹代码(封装成函数，输入audiofilename，输出voiceprint)=========
    # wav文件名
    audio_data, sr = librosa.load(audiofilename, sr=16000, mono=True)
    # 这里增加删除语音文件代码
    frames_features = read_audiotoMFB(audio_data)
    # print(frames_features.shape)
    extract_input = truncatedinputfromMFB(1)
    network_inputs_np = extract_input(frames_features)
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
