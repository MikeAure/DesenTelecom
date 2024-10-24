import os
from deepspeech import Model
from scipy.io import wavfile


def stt(file_path):
    fs1, data1 = wavfile.read(file_path)
    model_path = os.path.dirname(os.path.dirname(
        file_path)) + os.sep + 'audio' + os.sep + "deepspeech-0.9.3-models.pbmm"  # 已下载的模型地址（正确的模型文件中有以.pb结尾的文件）
    ars = Model(model_path)  # 1024应该是指窗长，这个是在源码中看到了
    translate_txt1 = ars.stt(data1)
    return translate_txt1
