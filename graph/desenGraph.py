import sys

import numpy as np
import os
from pymysql import NULL
from dbutils_Geolife import *
import datetime
from multiprocessing import Pool
import math


def pickone(scores):
    # if len(scores) == 1:
    #     return list(scores)[0]
    total_sum = sum(scores.values())
    if total_sum == 0:
        return NULL
    for co in scores:
        scores[co] = scores[co] / total_sum
    random = np.random.uniform(0, 1)  # 在0至sum之间随机取一个数
    s = 0
    for co in scores:
        s += scores[co]
        if s > random:
            return co


def get_mid_node_score(category_all, prefix, father, level, attribute1, attribute2, attribute3):
    """中间语义结点评分函数"""
    # logger.warning(category_all,prefix,father,level)
    scores = {}
    if level == 3:
        for a3 in attribute3:
            scores[a3] = category_all[attribute3.index(prefix), attribute3.index(a3)]
    elif level == 2:
        for a2 in attribute2:
            if a2[:2] == father:
                scores[a2] = category_all[attribute2.index(prefix), attribute2.index(a2)]
    else:
        for a1 in attribute1:
            if a1[:4] == father:
                scores[a1] = category_all[attribute1.index(prefix), attribute1.index(a1)]
    if scores:
        return scores


def syntheticTrajectory(num, length_all, start_all, category1_all, category2_all, category3_all, corrMatrix, pois,
                        poiId, attribute1, attribute2, attribute3, attribute1ToPoi):
    # synfile = "./synTrj2_2_1_f=1_test6/"+str(budget)+"_"+str(times)
    # if not os.path.exists(os.path.dirname(synfile)):
    #     os.makedirs(os.path.dirname(synfile))
    # spfile_handle = open(synfile, 'a+')
    trjs = []
    lengthDict = {k: length_all[k] for k in range(2, 21)}
    startDict = {k: start_all[k] for k in range(len(start_all))}
    i = 0
    while i < num:
        l = pickone(lengthDict)
        trj = []
        while len(trj) < l:
            startAttribute = attribute1[pickone(startDict)]
            startPoi = pickone({k: corrMatrix[poiId.index(k), poiId.index(k)] for k in attribute1ToPoi[startAttribute]})
            if startPoi == NULL:
                trj.clear()
                continue
            trj.append(startPoi)
            lastPoi = startPoi
            lastAttribute = pois[startPoi]
            while len(trj) < l:
                score = get_mid_node_score(category3_all, lastAttribute[:2], None, 3, attribute1, attribute2,
                                           attribute3)
                # print(score)
                category3 = pickone(score)
                if category3 == NULL:
                    # print("category3, null")
                    trj.clear()
                    break
                # print(category3)
                score = get_mid_node_score(category2_all, lastAttribute[:4], category3, 2, attribute1, attribute2,
                                           attribute3)
                category2 = pickone(score)
                if category2 == NULL:
                    # print("category2, null")
                    trj.clear()
                    break
                # print(category2)
                score = get_mid_node_score(category1_all, lastAttribute, category2, 1, attribute1, attribute2,
                                           attribute3)
                category1 = pickone(score)
                if category1 == NULL:
                    # print("category1, null")
                    trj.clear()
                    break
                # print(category1)
                score = {k: corrMatrix[poiId.index(lastPoi), poiId.index(k)] for k in attribute1ToPoi[category1]}
                poi = pickone(score)
                if poi == NULL:
                    # print("poi, null")
                    trj.clear()
                    break
                # print(poi)
                trj.append(poi)
                lastPoi = poi
                lastAttribute = category1  # update attribute
        # for sp in trj[i]:
        #     spfile_handle.write(sp+","+pois[sp]+";")
        # spfile_handle.write('\n')
        trjs.append(trj)
        i = i + 1
    return trjs
    # spfile_handle.close()


def getGraph(trjs, budget, attribute1, attribute2, attribute3):
    category1 = np.zeros((len(attribute1), len(attribute1)), dtype=float)
    category2 = np.zeros((len(attribute2), len(attribute2)), dtype=float)
    category3 = np.zeros((len(attribute3), len(attribute3)), dtype=float)
    start = np.zeros((len(attribute1),), dtype=float)

    for trj in trjs:
        points = trj.split(";")[:-1]
        for i in range(len(points)):
            attribute = points[i].split(",")[-1]
            if i == 0:
                start[attribute1.index(attribute)] = start[attribute1.index(attribute)] + 1 / len(points)
            else:
                category1[attribute1.index(last_attribute), attribute1.index(attribute)] = category1[attribute1.index(last_attribute), attribute1.index(attribute)] + 1 / len(points)
                category2[attribute2.index(last_attribute[:4]), attribute2.index(attribute[:4])] = category2[ attribute2.index(last_attribute[:4]), attribute2.index(attribute[:4])] + 1
                category3[attribute3.index(last_attribute[:2]), attribute3.index(attribute[:2])] = category3[attribute3.index(last_attribute[:2]), attribute3.index(attribute[:2])] + 1
            last_attribute = attribute
    print(f"GetGraph: {budget}")
    noise1 = np.random.laplace(0, 1 / budget, len(start))
    start = start + noise1
    start = np.maximum(start, 0)
    noise2 = np.random.laplace(0, 1 / budget, (len(attribute1), len(attribute1)))
    category1 = category1 + noise2
    category1 = np.maximum(category1, 0)
    return start, category1, category2, category3


def chooseRandomPoi(k, input_file):
    file = os.path.dirname(os.path.dirname(input_file)) + os.sep + "graph" + os.sep + "not_in_geolife_poi.txt"
    with open(file, 'r') as f:
        pois = f.readlines()[:k]
    poi_append = {}
    for p in pois:
        id = p.strip().split(",")[0]
        attr = p.strip().split(",")[1]
        poi_append[id] = attr
    return poi_append


def getCorrMatrix(trjs, budget, poiId, pois, attribute1ToPoi, input_file):
    corrMatrix = np.zeros((len(poiId), len(poiId)), dtype=float)
    # pois = {}
    for trj in trjs:
        points = trj.split(";")[:-1]
        l = len(points)
        poi = []
        for i in range(l):
            n = points[i].split(",")[0]
            poi.append(n)
        # print(poi)
        l = len(poi)
        m = l * (l - 1) / 2 + l
        for i in range(l):
            for j in range(i, l):
                corrMatrix[poiId.index(poi[i]), poiId.index(poi[j])] = corrMatrix[poiId.index(poi[i]), poiId.index(
                    poi[j])] + 1 / m
                corrMatrix[poiId.index(poi[j]), poiId.index(poi[i])] = corrMatrix[poiId.index(poi[j]), poiId.index(
                    poi[i])] + 1 / m
    # print(corrMatrix)
    print(f"CorrMatrixBudget: {budget}")
    noise = np.random.laplace(0, budget, (len(poiId), len(poiId)))
    corrMatrix = corrMatrix + noise
    corrMatrix = np.maximum(corrMatrix, 0)
    # print(corrMatrix)
    j = 0
    poi_selected_id = []
    file = os.path.dirname(os.path.dirname(input_file)) + os.sep + "graph" + os.sep + "not_in_geolife_poi.txt"
    with open(file, 'r') as f:
        pois_not = f.readlines()
    for i in range(len(poiId)):
        if corrMatrix[i, i] == 0:
            p = pois_not[j].split(",")[0]
            p_a = pois_not[j].split(",")[1][:-1]
            poi_selected_id.append(pois_not[j].split(",")[0])
            if p_a not in attribute1ToPoi:
                attribute1ToPoi[p_a] = []
            attribute1ToPoi[p_a].append(p)
            pois[p] = p_a
            j = j + 1
            corrMatrix[i, i] = 2 / (l * (l - 1) / 2 + l)
        else:
            poi_selected_id.append(poiId[i])
            attribute1ToPoi[pois[poiId[i]]].append(poiId[i])
    print(j)
    return poi_selected_id, attribute1ToPoi, corrMatrix


def getLenDistribution(trjs, budget):
    length = np.zeros((21,), dtype=int)
    for trj in trjs:
        points = trj.split(";")[:-1]
        l = len(points)
        length[l] = length[l] + 1

    print(f"LenDistribution Budget: {budget}")
    noise = np.random.laplace(0, 1 / budget, (21,))
    length = length + noise
    length = np.maximum(length, 0)
    length = np.around(length)
    # print(length)
    return length


def main(input_file, newFilePath, param):
    db = connect_db()
    pois = {}  # 存poi的Id与对应的属性
    poiId = []  # 存poi的序号与Id
    attribute1 = []  # 存level 1 属性的序号与 id
    attribute2 = []  # 存level 2 属性的序号与 id
    attribute3 = []  # 存level 3 属性的序号与 id
    attribute1ToPoi = {}  # 存属性 对应的poi列表（用的POI id）
    cursor = query_all_poi(db)
    results = cursor.fetchall()
    for row in results:
        _id = row[0]
        _attribute = row[1]
        pois[_id] = _attribute
        if _attribute not in attribute1ToPoi:
            attribute1ToPoi[_attribute] = []
        # attribute1ToPoi[_attribute].append(_id)
        # 为各个id标序号
        poiId.append(_id)
        if _attribute not in attribute1:
            attribute1.append(_attribute)
        if _attribute[:4] not in attribute2:
            attribute2.append(_attribute[:4])
        if _attribute[:2] not in attribute3:
            attribute3.append(_attribute[:2])
    close_db(db)
    # budgets = [0.5,1,1.5,2]

    budget = param
    print(f"User select epsilon: {budget}")
    # 读取文件
    # file = open("./preprocessed_trajectory0", 'r')
    file = open(input_file, 'r')
    trjs0 = file.readlines()[:]
    # 原文件名
    list = input_file.split(os.sep)
    fileName = list[-1]

    # 新文件存放路径
    #newFilePath = os.path.dirname(os.path.dirname(input_file)) + os.sep + "desen_files" + os.sep + "desen_" + fileName
    # 参数
    budget1 = budget * 4 / 10
    budget2 = budget * 4 / 10
    budget3 = budget * 2 / 10
    print("开始建模与加噪")
    starttime = datetime.datetime.now()
    start, category1, category2, category3 = getGraph(trjs0, budget1, attribute1, attribute2, attribute3)
    poi_selected, attribute1ToPoi, corrMatrix = getCorrMatrix(trjs0, budget2, poiId, pois, attribute1ToPoi, input_file)
    length = getLenDistribution(trjs0, budget3)
    endtime = datetime.datetime.now()
    print("建模时间：", (endtime - starttime).seconds, "s")
    multi_parm = [(1147, length, start, category1, category2, category3, corrMatrix, pois, poi_selected,
                   attribute1, attribute2, attribute3, attribute1ToPoi) for j in range(8)]
    print("开始合成轨迹")
    starttime = datetime.datetime.now()
    # 进行多进程运算
    with Pool() as pool:
        multi_result_list = pool.starmap(syntheticTrajectory, multi_parm)
    # syntheticTrajectory(9173,length,start,category1,category2,category3,corrMatrix,budget,i+1)
    endtime = datetime.datetime.now()
    print("轨迹合成时间：", (endtime - starttime).seconds, "s")

    if not os.path.exists(os.path.dirname(newFilePath)):
        os.makedirs(os.path.dirname(newFilePath))
    spfile_handle = open(newFilePath, 'a+')
    # print(multi_result_list)
    # 写入文件
    for trjs in multi_result_list:
        for trj in trjs:
            for sp in trj:
                spfile_handle.write(sp + "," + pois[sp] + ";")
            spfile_handle.write('\n')
    spfile_handle.close()


if __name__ == '__main__':
    if len(sys.argv) != 4:
        print("Usage: python your_script.py input_file param")
        sys.exit(1)
    # 参数列表
    param_list = [5, 1, 0.2]
    # 脱敏参数
    param = param_list[int(sys.argv[3])]
    # 脱敏前文件
    input_file = sys.argv[1]
    # input_file = r"C:\Users\admin\OneDrive\课题3\Trajectory_synthesis\preprocessed_trajectory0"
    # param = 2
    main(input_file, sys.argv[2], param)