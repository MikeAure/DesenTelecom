
import CNN
import PMDC
import csv
import sys
from sklearn import decomposition
import shutil
import os
from pathlib import Path


def get_data_set_features(res_model):
    features = []
    paths = []
    training_path = "./data/IDRID/a. Training Set"
    for i in range(1, 414):
        image_path = training_path + "/IDRiD_" + str(i).zfill(3) + ".jpg"
        features.append(CNN.extract_feature(res_model, image_path))
        paths.append(image_path)
    with open("./data/FEATURES", "w") as csvfile:
        writer = csv.writer(csvfile)
        for i in range(len(features)):
            a = features[i].tolist()
            a.append(paths[i])
            writer.writerow(a)
    return features, paths


#　获取图片特征和对应的图片信息
def read_features(image_path):
    with open(image_path) as csvfile:
        rows = csv.reader(csvfile.read().splitlines(), delimiter=',')
        features = []
        paths = []
        for row in rows:
            if len(row) != 0:
                features.append(row[:-1])
                # print(row[len(row) - 1])
                paths.append(row[len(row) - 1])
    
    return features, paths


def get_similar_image(res_model, path, image, eigen_vector_file_name="encrypted_eigen_vector.txt"):
    features, paths = read_features(os.path.join(path, "data", "FEATURES"))
    q = CNN.extract_feature(res_model, image)
    features.append(q)
    # print(paths)
    paths.append("image")
    pca = decomposition.PCA(n_components=64)
    pca_features = pca.fit_transform(features)
    p = PMDC.PMDC(pca_features)
    e_wave, e_bar = p.oldDataEnc(pca_features)
    # 加密后的图片特征向量
    eq_wave, eq_bar = p.TrapGen(pca_features[-1])
    
    print(type(eq_wave[0]))
    print(f"eq_wave: {eq_wave}")
    print(f"eq_bar: {eq_bar}")
    
    with open(eigen_vector_file_name, "w") as f:
        f.write(f"eq_wave: {eq_wave.tolist()}\n")
        f.write(f"eq_bar: {eq_bar.tolist()}\n")
    
    d, paths = p.DisCompare(e_wave, e_bar, eq_wave, eq_bar, paths)
    # print(f"Paths: {paths}")
    for image_path in paths:
        if image_path != "image":
            return image_path


if __name__ == '__main__':
    # get_data_set_features(CNN.make_model())
    if len(sys.argv) != 4:
        print("Usage: python your_script.py input_file out_file eigen_vector_file_name")
        exit(-1)
    model = CNN.make_model()
    current_path = os.getcwd()
    print(current_path)

    eigen_vector_file_name = sys.argv[3]
    path = get_similar_image(model, current_path, sys.argv[1], eigen_vector_file_name)
    # print(os.path.join(current_path, path))
    shutil.copy(os.path.join(current_path, path), sys.argv[2])
