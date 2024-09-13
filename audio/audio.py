import librosa
import numpy
import soundfile as sf
import sys
from audiomentations import Compose, Gain, SpecFrequencyMask
from audiomentations import LowPassFilter, HighPassFilter, PolarityInversion, Reverse, Normalize, TanhDistortion


def apply_audio_effects(input_signal, sample_rate):
    # 应用音高移位
    augmented_signal = librosa.effects.pitch_shift(input_signal, sr=sample_rate, n_steps=-6)
    # 应用时间拉伸
    augmented_signal = librosa.effects.time_stretch(augmented_signal, rate=0.75)
    # 应用增益调整
    transform = Compose([Gain(min_gain_in_db=-20, max_gain_in_db=0, p=1)])
    augmented_signal = transform(samples=augmented_signal, sample_rate=int(sample_rate))
    return augmented_signal


# def audio_augmentation(input_signal, sample_rate, param_chosen=2):
#     # 定义增广效果列表
#     augmentations = []
#
#     if param_chosen == 0:
#         print("param_chosen == 0")
#         augmentations = [
#             # 低通滤波
#             LowPassFilter(min_cutoff_freq=0, max_cutoff_freq=1000, p=1),
#             # 高通滤波
#             HighPassFilter(min_cutoff_freq=5000, max_cutoff_freq=8000, p=1),
#
#         ]
#     elif param_chosen == 1:
#         print("param_chosen == 1")
#         augmentations = [
#             # 低通滤波
#             LowPassFilter(min_cutoff_freq=0, max_cutoff_freq=1000, p=1),
#             # 高通滤波
#             HighPassFilter(min_cutoff_freq=5000, max_cutoff_freq=8000, p=1),
#             # 极性反转
#             PolarityInversion(p=1),
#             # 反向
#             Reverse(p=1),
#
#         ]
#     elif param_chosen == 2:
#         print("param_chosen == 2")
#         augmentations = [
#             # 低通滤波
#             LowPassFilter(min_cutoff_freq=0, max_cutoff_freq=1000, p=1),
#             # 高通滤波
#             HighPassFilter(min_cutoff_freq=5000, max_cutoff_freq=8000, p=1),
#             # 极性反转
#             PolarityInversion(p=1),
#             # 反向
#             Reverse(p=1),
#             # 归一化
#             Normalize(p=1),
#             # 双曲正切失真
#             TanhDistortion(min_distortion=0.01, max_distortion=0.5, p=1),
#             TanhDistortion(min_distortion=0.5, max_distortion=1, p=1),
#         ]
#     # 创建一个 Compose 实例，包含所有的增广效果
#     augmenter = Compose(augmentations)
#     # 应用所有的增广效果
#     augmented_signal = augmenter(samples=input_signal, sample_rate=sample_rate)
#     return augmented_signal

def audio_augmentation(input_signal, sample_rate, param_chosen=2):
    # 定义增广效果列表
    augmentations = []

    if param_chosen == 0:
        print("param_chosen == 0")
        augmentations = [
            # 低通滤波 (上限频率设置为 3000 Hz)
            LowPassFilter(min_cutoff_freq=0, max_cutoff_freq=3000, p=1),

            # 高通滤波 (下限频率设置为 300 Hz)
            HighPassFilter(min_cutoff_freq=300, max_cutoff_freq=8000, p=1),
        ]
    elif param_chosen == 1:
        print("param_chosen == 1")
        augmentations = [
            # 低通滤波 (上限频率设置为 4000 Hz)
            LowPassFilter(min_cutoff_freq=0, max_cutoff_freq=4000, p=1),

            # 高通滤波 (下限频率设置为 200 Hz)
            HighPassFilter(min_cutoff_freq=200, max_cutoff_freq=8000, p=1),

            # 极性反转
            PolarityInversion(p=1),

            # 反向 (控制概率，避免所有音频都反向)
            Reverse(p=0.5),
        ]
    elif param_chosen == 2:
        print("param_chosen == 2")
        augmentations = [
            # 低通滤波 (上限频率设置为 5000 Hz)
            LowPassFilter(min_cutoff_freq=0, max_cutoff_freq=5000, p=1),

            # 高通滤波 (下限频率设置为 100 Hz)
            HighPassFilter(min_cutoff_freq=100, max_cutoff_freq=8000, p=1),

            # 极性反转
            PolarityInversion(p=1),

            # 反向 (控制概率，避免所有音频都反向)
            Reverse(p=0.5),

            # 归一化 (不适用百分之百的概率，以避免无声的情况)
            Normalize(p=0.8),

            # 双曲正切失真 (减小失真程度)
            TanhDistortion(min_distortion=0.01, max_distortion=0.2, p=0.7),
        ]
    # 创建一个 Compose 实例，包含所有的增广效果
    augmenter = Compose(augmentations)

    # 应用所有的增广效果
    augmented_signal = augmenter(samples=input_signal, sample_rate=sample_rate)

    return augmented_signal

def spec_augmentation(input_signal, sample_rate, param_chosen=1):
    params_group = [(0.1, 0.3), (0.3, 0.5), (0.5, 0.7)]
    input_spectrogram = librosa.feature.melspectrogram(y=input_signal, sr=sample_rate, n_mels=64)
    # 使用audiomentations库创建一个频率遮罩增强
    min_mask, max_mask = params_group[param_chosen]
    transform = SpecFrequencyMask(
        fill_mode="mean",
        min_mask_fraction=min_mask,
        max_mask_fraction=max_mask,
        p=1)
    output_spectrogram = transform(input_spectrogram)
    # 返回频率遮罩后的梅尔频谱图
    return input_spectrogram, output_spectrogram


def spec_augmentation_audio(input_audio, output_audio_spec, param=1):
    # 使用librosa库加载音频文件，并返回音频信号和采样率
    signal, sr = librosa.load(input_audio)
    _, augmented_spectrogram = spec_augmentation(signal, sr, param)

    log_augmented_spectrogram = librosa.power_to_db(augmented_spectrogram, ref=numpy.max)
    inverse_mel_signal = librosa.feature.inverse.mel_to_audio(augmented_spectrogram, sr=sr)
    # # 确保频谱图长度与 STFT 变换长度一致
    # n_fft = 2048  # 你可以根据需要调整这个值
    # fixed_length_spectrogram = librosa.util.fix_length(augmented_spectrogram, n_fft)
    #
    # # 执行逆变换
    # inverse_mel_signal = librosa.feature.inverse.mel_to_audio(fixed_length_spectrogram, sr=sr)
    # # numpy.save(output_audio_spec, augmented_spectrogram)
    # 确保频谱图的帧数（时间轴上的长度）与期望长度一致
    # num_frames = augmented_spectrogram.shape[1]
    # desired_length = int(numpy.ceil(num_frames / n_fft) * n_fft)
    # fixed_length_spectrogram = librosa.util.fix_length(augmented_spectrogram, size=desired_length, axis=1)

    # 执行逆变换
    # inverse_mel_signal = librosa.feature.inverse.mel_to_audio(fixed_length_spectrogram, sr=sr)
    sf.write(output_audio_spec, inverse_mel_signal, sr)


def audio_augmentation_audio(input_audio, output_audio, param_chosen=2):
    # 使用librosa库加载音频文件，并返回音频信号和采样率
    signal, sample_rate = librosa.load(input_audio)
    augmented_signal = audio_augmentation(signal, sample_rate, param_chosen)
    sf.write(output_audio, augmented_signal, sample_rate)


def audio_floor(audio, decimals, path):
    wave_form, sample_rate = librosa.load(audio)
    waveform_round = numpy.round(wave_form, decimals=decimals)
    sf.write(path, waveform_round, sample_rate)


def audio_median(audio, block_length, path):
    wave_form, sample_rate = librosa.load(audio)
    waveform_splits = numpy.array_split(wave_form, int(len(wave_form) / block_length))
    wave = []
    for waveform_split in waveform_splits:
        wave.append(waveform_split.mean())
    sf.write(path, wave, sample_rate)


if __name__ == '__main__':

    if len(sys.argv) != 5:
        print("Usage: python your_script.py algName input_file out_file param")
        sys.exit(1)

    algName = sys.argv[1]
    input_video = sys.argv[2]
    output_video = sys.argv[3]
    param = sys.argv[4]

    if algName == "floor":
        floor_params = [3, 2, 1][int(param)]
        audio_floor(input_video, floor_params, output_video)
    elif algName == "median":
        block_num = [5, 10, 15][int(param)]
        audio_median(input_video, block_num, output_video)
    elif algName == "augmentation":
        audio_augmentation_audio(input_video, output_video, int(param))
    elif algName == "spec":
        spec_augmentation_audio(input_video, output_video, int(param))
