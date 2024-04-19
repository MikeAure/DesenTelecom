
import os
import sys
import time
from pydub import AudioSegment
from pydub.generators import Sine
import librosa
from audiomentations import Compose, Gain
import soundfile as sf
import numpy
import random

def dpAudio(file_path, newFilePath, budget):
    # 文件名(含后缀）
    list_file = file_path.split(os.sep)
    fileName = list_file[-1]
    # 不含后缀
    file_name_parts = os.path.splitext(fileName)[0]

    # 脚本路径
    pythonPath = os.path.dirname(os.path.dirname(file_path)) + os.sep + 'audio' + os.sep + 'temp'
    # 原音频声纹路径
    vector = os.path.join(pythonPath, (file_name_parts + '_vector.txt'))
    # 扰动音频声纹路径
    vector_dp = os.path.join(pythonPath, (file_name_parts + '_vector_dp.txt'))
    # 音频内容保存路径
    contentPath = os.path.join(pythonPath, file_name_parts + '_content.txt')
    # 开始时间
    start_time = time.perf_counter()
    # 提取声纹列表并保存
    vp_lists = [extract_voiceprint(file_path, sr=16000).squeeze(dim=0).tolist()]
    with open(vector, 'w+') as file:
        file.write(str(vp_lists))
    # 扰动
    dpVector2(vector, vector_dp, budget)

    # 提取文字,保存
    with open(contentPath, 'w+') as file_content:
        content = stt(file_path)
        file_content.write(content)

    # 读取声纹、文字、合成新语音
    with open(vector_dp, 'r') as file1:
        data1 = file1.readlines()
    with open(contentPath, 'r') as file2:
        data2 = file2.readlines()
        data2 = data2[0]

    save_syn(data1, data2, newFilePath)
    end_time = time.perf_counter()
    execution_time_ms = (end_time - start_time) * 1000
    print("程序执行时间（毫秒）：", execution_time_ms)


def replace_voice_print(file_path, template_file, newFilePath):
    # 文件名(含后缀）
    list_file = file_path.split(os.sep)
    fileName = list_file[-1]
    # 不含后缀
    file_name_parts = os.path.splitext(fileName)[0]

    # 脚本路径
    pythonPath = os.path.dirname(os.path.dirname(file_path)) + os.sep + 'audio' + os.sep + 'temp'
    # 原音频声纹路径
    vector = os.path.join(pythonPath, (file_name_parts + '_vector.txt'))
    # 扰动音频声纹路径
    template_vector = os.path.join(pythonPath, (file_name_parts + "_template_vector.txt"))
    # 处理过的模板声纹
    processed_template_vector = os.path.join(pythonPath, (file_name_parts + "_processed_template_vector.txt"))

    # 音频内容保存路径
    contentPath = os.path.join(pythonPath, file_name_parts + '_content.txt')
    # 开始时间
    start_time = time.perf_counter()
    # 提取声纹列表并保存
    vp_lists = [extract_voiceprint(file_path, sr=16000).squeeze(dim=0).tolist()]
    tp_lists = [extract_voiceprint(template_file, sr=16000).squeeze(dim=0).tolist()]
    with open(vector, 'w+') as file:
        file.write(str(vp_lists))
    
    with open(template_vector, 'w+') as file:
        file.write(str(tp_lists))
    
    # 预处理
    preprocess(template_vector, processed_template_vector)

    # 提取文字,保存
    with open(contentPath, 'w+') as file_content:
        content = stt(file_path)
        file_content.write(content)

    # 读取声纹、文字、合成新语音
    with open(processed_template_vector, 'r') as file1:
        data1 = file1.readlines()
    with open(contentPath, 'r') as file2:
        data2 = file2.readlines()
        data2 = data2[0]

    save_syn(data1, data2, newFilePath)
    end_time = time.perf_counter()
    execution_time_ms = (end_time - start_time) * 1000
    print("程序执行时间（毫秒）：", execution_time_ms)
    

def replace_voice_print_fixed(file_path, newFilePath):
    # 文件名(含后缀）
    list_file = file_path.split(os.sep)
    fileName = list_file[-1]
    # 不含后缀
    file_name_parts = os.path.splitext(fileName)[0]

    # 脚本路径
    pythonPath = os.path.dirname(os.path.dirname(file_path)) + os.sep + 'audio' + os.sep + 'temp'
    # 原音频声纹路径
    vector = os.path.join(pythonPath, (file_name_parts + '_vector.txt'))
    # 扰动音频声纹路径
    # template_vector = os.path.join(pythonPath, (file_name_parts + "_template_vector.txt"))
    # 处理过的模板声纹
    fixed_template_vector = os.path.join(pythonPath, "template_vector.txt")

    # 音频内容保存路径
    contentPath = os.path.join(pythonPath, file_name_parts + '_content.txt')
    # 开始时间
    start_time = time.perf_counter()
    # 提取声纹列表并保存
    vp_lists = [extract_voiceprint(file_path, sr=16000).squeeze(dim=0).tolist()]
    # tp_lists = [extract_voiceprint(template_file, sr=16000).squeeze(dim=0).tolist()]
    with open(vector, 'w+') as file:
        file.write(str(vp_lists))
    
    # with open(template_vector, 'w+') as file:
    #     file.write(str(tp_lists))
    
    # 预处理
    # preprocess(template_vector, processed_template_vector)

    # 提取文字,保存
    with open(contentPath, 'w+') as file_content:
        content = stt(file_path)
        file_content.write(content)

    # 读取声纹、文字、合成新语音
    with open(fixed_template_vector, 'r') as file1:
        data1 = file1.readlines()
    with open(contentPath, 'r') as file2:
        data2 = file2.readlines()
        data2 = data2[0]

    save_syn(data1, data2, newFilePath)
    end_time = time.perf_counter()
    execution_time_ms = (end_time - start_time) * 1000
    print("程序执行时间（毫秒）：", execution_time_ms)
    
    
def add_beep(input_file, input_format, output_file, output_format, start_time_sec, duration_sec, beep_frequency=1000):
    # Load the input audio file
    audio = AudioSegment.from_file(input_file, format=input_format)

    # Generate beep audio
    beep = Sine(beep_frequency).to_audio_segment(duration=duration_sec * 1000)

    # Add the beep to the specified start time
    start_time_ms = int(start_time_sec * 1000)
    audio = audio.overlay(beep, position=start_time_ms)

    # Export the result to a new file
    audio.export(output_file, format=output_format)


def remove_audio(input_file, input_format, output_file, output_format, start_time_sec, duration_sec):
    # Load the input audio file
    audio = AudioSegment.from_file(input_file, format=input_format)

    # Convert start and end times to milliseconds
    start_time_ms = int(start_time_sec * 1000)
    end_time_ms = int((start_time_sec + duration_sec) * 1000)

    # Cut out the specified part of the audio
    final_audio = audio[:start_time_ms] + audio[end_time_ms:]

    # Export the result to a new file
    final_audio.export(output_file, format=output_format)


def apply_audio_effects(input_file, output_file, n_steps, rate, min_gain_db, max_gain_db):
    input_signal, sample_rate = librosa.load(input_file)
    # 应用音高移位
    augmented_signal = librosa.effects.pitch_shift(input_signal, sr=sample_rate, n_steps=n_steps)
    # 应用时间拉伸
    augmented_signal = librosa.effects.time_stretch(augmented_signal, rate=rate)
    # 应用增益调整
    transform = Compose([Gain(min_gain_db=min_gain_db, max_gain_db=max_gain_db, p=1)])
    augmented_signal = transform(samples=augmented_signal, sample_rate=int(sample_rate))
    sf.write(output_file, augmented_signal, sample_rate)

def audio_reshuffle(audio, output_path, block_num):
    wave_form, sample_rate = librosa.load(audio)
    waveform_splits = numpy.array_split(wave_form, block_num)
    random.shuffle(waveform_splits)
    reshuffle_wave = numpy.concatenate(waveform_splits)
    sf.write(output_path, reshuffle_wave, sample_rate)


if __name__ == '__main__':
    # 算法名
    algName = sys.argv[1]
    # 音频文件路径及脱敏参数
    file_path = sys.argv[2]
    newFilePath = sys.argv[3]
    param = int(sys.argv[4])
    # 隐私预算
    budgets = [5, 1, 0.2]
    budget = budgets[param]

    # 音频分块数量
    # block_num = [5, 10, 15]

    format = 'aac'
    if algName == "dpAudio":
        from myextraction import *
        from myDP import *
        from myStart2 import *
        from mystt1 import *
        dpAudio(file_path, newFilePath, budget)
    elif algName == "add_beep":
        dealTime = int(sys.argv[5])
        add_beep(file_path, format, newFilePath, "wav", param, dealTime)
    elif algName == "remove_audio":
        dealTime = int(sys.argv[5])
        remove_audio(file_path, format, newFilePath, "wav", param, dealTime)
    elif algName == "voice_replace":
        from myextraction import *
        from myDP import *
        from myStart2 import *
        from mystt1 import *
        replace_voice_print_fixed(file_path, newFilePath)       
    elif algName == "apply_audio_effects":
        effects_param = [(-3, 0.7, -5, 5), (2, 0.8, -10, 10), (5, 0.9, -20, 20)][param]
        apply_audio_effects(file_path, newFilePath, *effects_param)

    elif algName == "audio_reshuffle":
        block = [5, 10, 15][int(sys.argv[5])]
        # block = int(sys.argv[5])
        audio_reshuffle(file_path, newFilePath, block)
    
    # from myextraction import *
    # from myDP import *
    # from myStart2 import *
    # from mystt1 import *
    
    # # dpAudio("D:\\Programming\\Desen\\raw_files\\1710382610951audiotest.wav", 
    # #         "D:\\Programming\\Desen\\desen_files\\desen_1710382610951audiotest.wav", 
    # #         0.5)

    # replace_voice_print_fixed("D:\\Programming\\Desen\\raw_files\\17127445577770001.wav", 
    #                   "D:\\Programming\\Desen\\desen_files\\desen_17127445577770001.wav")





