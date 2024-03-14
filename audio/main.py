from myextraction import *
from myDP import *
from myStart2 import *
from mystt1 import *
import os

# 获取音频文件、提取出声纹
print ("请输入原始音频文件路径")
directory_path = input()
prefix_to_exclude = 'desen_'
temp = "temp"
new_path = os.path.join(directory_path, temp)
# 使用列表推导式获取目录下的文件名
file_names = [f for f in os.listdir(directory_path) if os.path.isfile(os.path.join(directory_path, f)) and not f.startswith(prefix_to_exclude)]
# file_names = [f for f in os.listdir(directory_path) if not f.startswith(prefix_to_exclude)]

# 可选：仅获取文件，而非目录
file_paths = [os.path.join(directory_path, filename) for filename in file_names if os.path.isfile(os.path.join(directory_path, filename))]

# 获取文件名
file_name_parts = [filename.split('.')[0] for filename in file_names]
# 提前写好后缀
for temp in file_name_parts:
    vector_file_names = [os.path.join(temp,'_vector.txt') for filename in file_names]
    vector_dp_names = [os.path.join(temp,'_vector_dp.txt') for filename in file_names]


# 提取声纹列表
vp_lists = []   # voiceprint_list
for file_name in file_paths:
    # print(file_name)
    vp_lists.append(extract_voiceprint(file_name, sr=16000).squeeze(dim=0).tolist())

# 将声纹写入文件里
for file_name, content in zip(file_name_parts, vp_lists):
    with open(os.path.join(new_path,file_name+'_vector.txt'), 'w') as file:
        file.write(str(content))
# 将声纹脱敏得到脱敏列表
print("请输入不同的隐私预算，数字0-3，0表示不脱敏，1-3表示脱敏程度逐渐增大")
l = int(input())
if l != 0 : l += 1
for file in file_name_parts:
    vector = os.path.join(new_path, (file+'_vector.txt'))
    vector_dp = os.path.join(new_path, (file+'_vector_dp.txt'))
    dpVector2(vector, vector_dp, l)

# 提取文字
for file in file_name_parts:
    with open(os.path.join(new_path,file+'_content.txt'), 'w') as file_content:
        # print(directory_path +'\\'+ file+'.wav')
        content = stt(os.path.join(directory_path,file+'.wav'))
        file_content.write(content)

# 提取声纹、文字、合成新语音
for file in file_name_parts:
    with open(os.path.join(new_path,file+'_vector_dp.txt'), 'r') as file1:
        data1 = file1.readlines()
    with open(os.path.join(new_path,file+'_content.txt'), 'r') as file2:
        data2 = file2.readlines()
        data2 = data2[0]
    save_syn(data1, data2, os.path.join(directory_path,prefix_to_exclude+file+'.wav'))


#
# test = extract_voiceprint('myresults/0001.wav', sr=16000).squeeze(dim=0).tolist()
# print('原始：', test)
# fo = open("myresults/0001vector.txt", "w")
# fo.write(str(test))
# fo.close()
#
# # 调用dpVector2函数，对存储的声纹向量进行格式处理，调用DPfun函数对声纹进行脱敏计算，最后存储脱敏声纹
# dpVector2()
#
# # 加载模型提取文字，写入txt文件中
#     # path=r'D:\LUPANPAN\Paper_Code\DP\lbspeech\VSvoice\61\70968'
#     # txtpath=r'D:\LUPANPAN\Paper_Code\DP\lbspeech\VSvoice\61\61-70968-dstxt.txt'
#
#     # STT1(path,txtpath)
# stt()
#
# # 加载脱敏声纹向量，调用save_syn函数，加载声码器、合成器、映射模型，将声纹和文本合成语音
#
#     # #批量合成语音，输入声纹文件
# file=open(r'D:\QianBJ\DP\voiceDP1\myresults\0001vector.txt')
# lines=file.readlines()
# save_syn(lines)
# print('主程序')
#
#
