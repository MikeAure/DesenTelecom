import torch

import InnerProduct
import extraction

secu = InnerProduct.InnerProduct()

voiceprint_a = extraction.ertract_voiceprint("Hjjgg&&0.wav", sr=16000)
voiceprint_b = extraction.ertract_voiceprint("Hjjgg&&1.wav", sr=16000)
A = torch.norm(voiceprint_a).item()
B = torch.norm(voiceprint_b).item()
print(voiceprint_a)
print('torch.norm(voiceprint_a)', torch.norm(voiceprint_a))
print('A', A)
print('type(A)', type(A))
a = voiceprint_a.cpu().numpy().tolist()[0]
print(len(a))
print(type(a))
print('a', a)
b = voiceprint_b.cpu().numpy().tolist()[0]
b = list(b)
print(len(b))
print(type(b))
print('b', b)
print(max(b))
print(min(b))
s, C = secu.Step1(a)
DSum = secu.Step2(b, C)
innerproduct = secu.Step3(DSum, s)
print('innerproduct\n', innerproduct)
cos = innerproduct / (A * B)
print('cos', cos)
