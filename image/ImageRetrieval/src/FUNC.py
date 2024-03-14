
import CNN
import PMDC
import csv
import sys
from sklearn import decomposition
import shutil
import os


def get_data_set_features(res_model):
    features = []
    paths = []
    image_path = "../data/IDRID/a. Training Set"
    for i in range(1, 414):
        image_path = image_path + "/IDRid_" + str(i).zfill(3) + ".jpg"
        features.append(CNN.extract_feature(res_model, image_path))
        paths.append(image_path)
    with open("../data/FEATURES", "w") as csvfile:
        writer = csv.writer(csvfile)
        for i in range(len(features)):
            a = features[i].tolist()
            a.append(paths[i])
            writer.writerow(a)
    return features, paths


def read_features(image_path):
    with open(image_path) as csvfile:
        rows = csv.reader(csvfile.read().splitlines(), delimiter=',')
        features = []
        paths = []
        for row in rows:
            if len(row) != 0:
                features.append(row[:-1])
                paths.append(row[len(row) - 1])
    return features, paths


def get_similar_image(res_model, path, image):
    features, paths = read_features(path + "\\image\\ImageRetrieval\\data\\FEATURES")
    q = CNN.extract_feature(res_model, image)
    features.append(q)
    paths.append("image")
    pca = decomposition.PCA(n_components=64)
    pca_features = pca.fit_transform(features)
    p = PMDC.PMDC(pca_features)
    e_wave, e_bar = p.oldDataEnc(pca_features)
    eq_wave, eq_bar = p.TrapGen(pca_features[-1])
    d, paths = p.DisCompare(e_wave, e_bar, eq_wave, eq_bar, paths)
    for image_path in paths:
        if image_path != "image":
            return image_path


if __name__ == '__main__':
    model = CNN.make_model()
    current_path = os.getcwd();
    print(current_path)
    path = get_similar_image(model, current_path, sys.argv[1])
    shutil.copy(current_path + "\\image\\ImageRetrieval\\" + path, sys.argv[2])
