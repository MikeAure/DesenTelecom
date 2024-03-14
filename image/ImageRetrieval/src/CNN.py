import csv
import os
import torchvision.models as models
from torch.autograd import Variable
import torchvision.transforms as transforms
from PIL import Image
import numpy as np
from sklearn import decomposition
import pandas as pd


TARGET_IMG_SIZE = 224
transform_list = [transforms.ToTensor(),
                  transforms.Normalize(mean=[0.485, 0.456, 0.406],
                                       std=[0.229, 0.224, 0.225])]
img_to_tensor = transforms.Compose(transform_list)


def make_model():
    resmodel = models.vgg16(pretrained=True)
    return resmodel

# 特征提取
def extract_feature(resmodel, imgpath):
    resmodel.eval()

    img = Image.open(imgpath).convert("RGB")
    img = img.resize((TARGET_IMG_SIZE, TARGET_IMG_SIZE))
    tensor = img_to_tensor(img)

    tensor = tensor.resize_(1, 3, TARGET_IMG_SIZE, TARGET_IMG_SIZE)

    result = resmodel(Variable(tensor))
    result_npy = result.data.cpu().numpy()

    return result_npy[0]

def extract_all_feature(resmodel, imgroot):
    fs, labels = get_fileNames(imgroot)
    all_feature = list()
    i = 0
    for imgpath in fs:
            all_feature.append(extract_feature(resmodel, imgpath))

    return np.array(all_feature), labels

def extract_all_feature_IDRiD(resmodel, imgroot):
    label_filename = '../../data/IDRiD/a. IDRiD_Disease Grading_Training Labels.csv'
    fs, labels = get_fileNames_from_IDRiD(imgroot, label_filename)
    all_feature = list()
    i = 0
    for imgpath in fs:
            all_feature.append(extract_feature(resmodel, imgpath))

    return np.array(all_feature), labels


def feature_PCA_store(filename, features, labels, PCA_dim):
    pca = decomposition.PCA(n_components = PCA_dim)
    PCA_features = pca.fit_transform(features)
    print(len(PCA_features))
    print(len(labels))
    #PCA_features = features
    with open(filename, "w") as csvfile:
        writer = csv.writer(csvfile)
        for i in range(len(PCA_features)):
            print(i)
            a = PCA_features[i].tolist()
            a.append(labels[i])
            writer.writerow(a)

def feature_store(filename, features, labels):

    with open(filename, "w") as csvfile:
        writer = csv.writer(csvfile)
        for i in range(len(features)):
            print(i)
            a = features[i].tolist()
            a.append(labels[i])
            writer.writerow(a)

def get_fileNames(rootdir):
    fs = list()
    labels = list()
    for root, dirs, files in os.walk(rootdir,topdown = True):
        for name in files:
            _, ending = os.path.splitext(name)
            if ending == ".jpg":
                m=os.path.join(root,name)
                #m=m.replace('\\','/')
                fs.append(m)
                if m[-5] == '0':
                    labels.append(0)
                elif m[-5] == '1':
                    labels.append(1)
                elif m[-5] == '2':
                    labels.append(2)
    return fs, labels

def get_fileNames_from_IDRiD(rootdir, label_filename):
    fs = list()
    labels = list()

    for root, dirs, files in os.walk(rootdir, topdown=True):
        for name in files:
            _, ending = os.path.splitext(name)
            if ending == ".jpg":
                m = os.path.join(root, name)
                # m=m.replace('\\','/')
                #print(m)
                fs.append(m)
                imgname = m[-13:-4]
                #print(imgname)
                with open(label_filename, 'r') as f:
                    for line in f.readlines():
                        print(line)
                        if imgname == line[:9]:
                            print(imgname)
                            print(line)
                            if line[12:13] == '0':
                                labels.append(0)
                            elif line[12:13] == '1':
                                labels.append(1)
                            elif line[12:13] == '2':
                                labels.append(2)

    return fs, labels


if __name__ == "__main__":
    model = make_model()
    imgpath = '../../data/IDRiD/a. Training Set'
    all_feature, labels = extract_all_feature_IDRiD(model, imgpath)
    print(len(all_feature))
    print(len(labels))

    #feature_PCA_store("../../data/IDRiD_12", all_feature, labels, 12)
    feature_PCA_store("../../data/IDRiD_20", all_feature, labels, 20)
    #feature_PCA_store("../../data/IDRiD_28", all_feature, labels, 28)

    """
    feature_store('../../data/IDRiD_original', all_feature, labels)

    feature_PCA_store("../../data/IDRiD_16", all_feature, labels, 16)

    feature_PCA_store("../../data/IDRiD_32", all_feature, labels, 32)
    feature_PCA_store("../../data/IDRiD_48", all_feature, labels, 48)
    feature_PCA_store("../../data/IDRiD_64", all_feature, labels, 64)
    feature_PCA_store("../../data/IDRiD_80", all_feature, labels, 80)
    feature_PCA_store("../../data/IDRiD_96", all_feature, labels, 96)
    feature_PCA_store("../../data/IDRiD_108", all_feature, labels, 108)
    

    imgpath = '../../data/FIRE'
    all_feature, labels = extract_all_feature(model, imgpath)
    feature_PCA_store("../../data/FIRE_8", all_feature, labels, 8)
    feature_PCA_store("../../data/FIRE_24", all_feature, labels, 24)

    
    feature_store('../../data/FIRE_original', all_feature, labels)

    feature_PCA_store("../../data/FIRE_16", all_feature, labels, 16)
    feature_PCA_store("../../data/FIRE_32", all_feature, labels, 32)
    feature_PCA_store("../../data/FIRE_48", all_feature, labels, 48)
    feature_PCA_store("../../data/FIRE_64", all_feature, labels, 64)
    feature_PCA_store("../../data/FIRE_80", all_feature, labels, 80)
    feature_PCA_store("../../data/FIRE_96", all_feature, labels, 96)
    feature_PCA_store("../../data/FIRE_108", all_feature, labels, 108)
    

    imgpath = '../../data/CXR_png'
    all_feature, labels = extract_all_feature(model, imgpath)
    feature_PCA_store("../../data/CXR_12", all_feature, labels, 12)
    feature_PCA_store("../../data/CXR_20", all_feature, labels, 20)

    
    feature_PCA_store("../../data/CXR_80", all_feature, labels, 80)
    feature_PCA_store("../../data/CXR_96", all_feature, labels, 96)
    feature_PCA_store("../../data/CXR_108", all_feature, labels, 108)

    
    feature_PCA_store("../../data/CXR_64",  all_feature, labels, 64)
    feature_PCA_store("../../data/CXR_128", all_feature, labels, 128)
    feature_PCA_store("../../data/CXR_256", all_feature, labels, 256)
    feature_PCA_store("../../data/CXR_512", all_feature, labels, 512)
    """

    #print(all_feature)
    #print(labels)


