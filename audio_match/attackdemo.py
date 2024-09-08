import extraction
import os
from model import *
import librosa
from funcs import *

# dim_voiceprint = 512
# attack_length = 16000 * 1.25  # 三秒
# device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
#
#
# class BNReLUConv(nn.Sequential):
#     def __init__(self, in_channels, channels, k=3, s=1, p=1, inplace=True):
#         super(BNReLUConv, self).__init__()
#         self.add_module('bn', nn.BatchNorm1d(in_channels))
#         self.add_module('relu', nn.ReLU(inplace=inplace))
#         self.add_module('conv', nn.Conv1d(in_channels, channels, k, s, p, bias=False))
#
#
# class Generator(nn.Module):
#     def __init__(self):
#         super(Generator, self).__init__()
#         # self.ngpu = ngpu
#         self.fc = nn.Sequential(
#             nn.Linear(dim_voiceprint, 1024),
#             nn.ReLU(True),
#             nn.BatchNorm1d(1024),
#             nn.Linear(1024, int(attack_length / 8 * 128)),  # 4000*3
#             nn.ReLU(True),
#             nn.BatchNorm1d(int(attack_length / 8 * 128))
#         )
#
#         self.dconv1 = nn.Sequential(
#             nn.ConvTranspose1d(128, 64, 4, 2, padding=1),
#             # nn.ReLU(True),
#             # nn.BatchNorm2d(64),
#
#             # nn.Tanh()
#         )
#         self.conv11 = nn.Sequential(
#             nn.Conv1d(64, 64, kernel_size=1, stride=1, padding=0),
#             # nn.ReLU(),
#             # nn.BatchNorm2d(64),
#             # nn.MaxPool2d(kernel_size=2, stride=2)),
#             # nn.Tanh()
#         )
#         self.conv12 = nn.Sequential(
#             nn.Conv1d(64, 64, kernel_size=1, stride=1, padding=0),
#             # nn.ReLU(),
#             # nn.BatchNorm2d(64),
#             # nn.MaxPool2d(kernel_size=2, stride=2)),
#             # nn.Tanh()
#         )
#         self.conv13 = nn.Sequential(
#             nn.Conv1d(64, 64, kernel_size=1, stride=1, padding=0),
#             # nn.ReLU(),
#             # nn.BatchNorm2d(64),
#             # nn.MaxPool2d(kernel_size=2, stride=2)),
#             # nn.Tanh()
#         )
#         self.conv14 = nn.Conv1d(64, 64, kernel_size=1, stride=1, padding=0)
#
#         # 聚合层：将4个层的输出通道聚合成一个
#         self.final1 = BNReLUConv(4 * 64, 64, 1, 1, 0)
#
#         self.dconv2 = nn.Sequential(
#             nn.ConvTranspose1d(64, 32, 4, 2, padding=1),
#             nn.ReLU(True),
#             nn.BatchNorm1d(32),
#
#             # nn.Tanh()
#         )
#         self.conv21 = nn.Sequential(
#             nn.Conv1d(32, 32, kernel_size=1, stride=1, padding=0),
#             nn.ReLU(),
#             nn.BatchNorm1d(32),
#             # nn.MaxPool2d(kernel_size=2, stride=2)),
#             # nn.Tanh()
#         )
#         self.conv22 = nn.Sequential(
#             nn.Conv1d(32, 32, kernel_size=1, stride=1, padding=0),
#             nn.ReLU(),
#             nn.BatchNorm1d(32),
#             # nn.MaxPool2d(kernel_size=2, stride=2)),
#             # nn.Tanh()
#         )
#         self.conv23 = nn.Sequential(
#             nn.Conv1d(32, 32, kernel_size=1, stride=1, padding=0),
#             nn.ReLU(),
#             nn.BatchNorm1d(32),
#             # nn.MaxPool2d(kernel_size=2, stride=2)),
#             # nn.Tanh()
#         )
#
#         self.final2 = nn.Sequential(
#             BNReLUConv(4 * 32, 32, 1, 1, 0),
#             # nn.MaxPool2d(kernel_size=2, stride=2)
#         )
#
#         self.dconv3 = nn.Sequential(
#             nn.ConvTranspose1d(32, 16, 4, 2, padding=1),
#             nn.ReLU(True),
#             nn.BatchNorm1d(16),
#
#             # nn.Tanh()
#         )
#         self.conv31 = nn.Sequential(
#             nn.Conv1d(16, 16, kernel_size=1, stride=1, padding=0),
#             nn.ReLU(),
#             nn.BatchNorm1d(16),
#             # nn.MaxPool2d(kernel_size=2, stride=2)),
#             # nn.Tanh()
#         )
#         self.conv32 = nn.Sequential(
#             nn.Conv1d(16, 16, kernel_size=1, stride=1, padding=0),
#             nn.ReLU(),
#             nn.BatchNorm1d(16),
#             # nn.MaxPool2d(kernel_size=2, stride=2)),
#             # nn.Tanh()
#         )
#         self.conv33 = nn.Sequential(
#             nn.Conv1d(16, 16, kernel_size=1, stride=1, padding=0),
#             nn.ReLU(),
#             nn.BatchNorm1d(16),
#             # nn.MaxPool2d(kernel_size=2, stride=2)),
#             # nn.Tanh()
#         )
#
#         self.final3 = nn.Sequential(
#             # nn.Conv2d(32*4,32,1,1,0),
#             # nn.Conv2d(32,16,1,1,0),
#             BNReLUConv(4 * 16, 1, 1, 1, 0),
#             # nn.MaxPool2d(kernel_size=2, stride=2),
#             # nn.ConvTranspose2d(32*4, 1, 4, 2, padding=1),
#             nn.Tanh()
#         )
#
#     def forward(self, x):
#         # print(x.size())
#         x = self.fc(x)
#         # print(x.size())
#         # x = x.view(x.shape[0], 128, 1, int(attack_length/8)) # reshape 通道是 128，大小是 7x7
#         x = x.view(x.shape[0], 128, int(attack_length / 8))
#         # print(x.size())
#         out1 = self.dconv1(x)
#         # print(x.size())
#         out2 = self.conv11(out1)
#         out3 = self.conv12(out1 + out2)
#         out4 = self.conv13(out1 + out2 + out3)
#         out12 = torch.cat((out1, out2), 1)
#         out34 = torch.cat((out3, out4), 1)
#         out1234 = torch.cat((out12, out34), 1)
#
#         # print(out1234.size())
#         x = self.final1(out1234)
#
#         # print(x.size())
#
#         out1 = self.dconv2(x)
#         # print(out1.size())
#         out2 = self.conv21(out1)
#         out3 = self.conv22(out2)
#         out4 = self.conv23(out3)
#         out12 = torch.cat((out1, out2), 1)
#         out34 = torch.cat((out3, out4), 1)
#         out1234 = torch.cat((out12, out34), 1)
#
#         # print(out1234.size())
#         x = self.final2(out1234)
#         # print(x.size())
#
#         out1 = self.dconv3(x)
#         out2 = self.conv31(out1)
#         out3 = self.conv32(out2)
#         out4 = self.conv33(out3)
#         out12 = torch.cat((out1, out2), 1)
#         out34 = torch.cat((out3, out4), 1)
#         out1234 = torch.cat((out12, out34), 1)
#
#         # print(out1234.size())
#         x = self.final3(out1234)
#         # print(x.size())
#
#         return x
#
#
# attacker = torch.load('attackspeaker2.pkl')
# Euclideandist = PairwiseDistance(2)
# result = 0
# speakermodel.eval()
# with torch.no_grad():
#     for i in range(0, 943):
#         mfcc_e = extract_mfcc(torch.Tensor(testdata_e[i]))
#         # mfcc_a = extract_mfcc(torch.Tensor(testdata_a[i]))
#         # label = torch.Tensor(labels[i]).cuda()
#         # print(mfcc_e)
#         # print(mfcc_a)
#         voiceprint_e = speakermodel(mfcc_e)
#         fake_noise = attacker(voiceprint_e).squeeze()
#         fake_speech = torch.cat((torch.cat((fake_noise, fake_noise), 1), torch.cat((fake_noise, fake_noise), 1)),
#                                 1) + torch.Tensor(testdata_a[i]).cuda()
#
#         mfcc_a = extract_mfcc(fake_speech)
#         voiceprint_a = speakermodel(mfcc_a)
#         distance = Euclideandist(voiceprint_e, voiceprint_a)
#         # print(distance)
#         # print(label)
#         # print(label)
#         result += torch.sum((distance <= 0.57))  # 判断正确
#         # break
# accuracy = 200 * result.item() / 37720
# print('attackermodel2:{:.2f}%'.format(accuracy))

filename_e = './AttackSample/zzl1.wav'
filename_a = './AttackSample/liqi&&0.wav'
y1,sr = librosa.load(filename_e,sr=16000,mono=True)
y2,sr = librosa.load(filename_a,sr=16000,mono=True)
print('y1:',len(y1))
print('y2:',len(y2))
#print('================')
#y变成80000
#print(len(y1))
#with torch.no_grad():
#    voiceprint_e = extraction.ertract_voiceprint(filename_e,sr=16000)
#    voiceprint_a = extraction.ertract_voiceprint(filename_a,sr=16000)
#print(voiceprint_e)

Euclideandist = PairwiseDistance()
distance = 0
for i in range(20):
    voiceprint_e = extraction.extract_voiceprint(filename_e, sr=16000)
    voiceprint_a = extraction.extract_voiceprint(filename_a, sr=16000)
    distance += Euclideandist.apply(voiceprint_e, voiceprint_a, 2).item()
print("distance:", distance/20)
#fake_noise = attacker(voiceprint_e).squeeze()

#while len(y2)>len(fake_noise):
#    fake_noise = np.concatenate((y1,y1),axis = 0)#y1 = y1 + y1
#    y1 = y1[0:80000]
#    while len(y2)<80000:
#        y2 = np.concatenate((y2,y2),axis = 0)
#            #print(len(y2))
#        y2 = y2[0:80000]