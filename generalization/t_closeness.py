#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# @Time    : 2022-03-22 18:12
# @Software: PyCharm

import pandas as pd
import sys

def get_spans(data, partition, scale=None):
    spans = {}
    for column in data.columns:
        if column in category:  # 判断是否为类别型数据
            span = len(data[column][partition].unique())
        else:
            span = data[column][partition].max() - data[column][partition].min()  # 最大值-最小值
        if scale is not None:
            span = span / scale[column]
        spans[column] = span
    return spans


def split(data, partition, column):
    data_pt = data[column][partition]
    if column in category:  # 类别型数据根据类别长度的中位值分为两个子集合
        values = data_pt.unique()
        lv = set(values[:len(values) // 2])
        rv = set(values[len(values) // 2:])
        return data_pt.index[data_pt.isin(lv)], data_pt.index[data_pt.isin(rv)]
    else:  # 数值型数据根据列数值中位数分为两个子集合
        median = data_pt.median()
        data_l = data_pt.index[data_pt < median]
        data_r = data_pt.index[data_pt >= median]
        return (data_l, data_r)


# 判断构造的匿名区是否满足k匿名要求
def is_k_anonymity(data, partition, sensitive, k=3):
    if len(partition) < k:
        return False
    return True


# 构造匿名区
def partition_dataset(data, quasi_id, sensitive, scale, is_valid):
    finished_partitions = []
    # 获取分区索引
    partitions = [data.index]
    while partitions:
        # 弹出分区
        partition = partitions.pop(0)
        # 获取分区跨度
        spans = get_spans(data[quasi_id], partition, scale)
        for column, span in sorted(spans.items(), key=lambda x: -x[1]):
            # 切割分区
            lv, rv = split(data, partition, column)
            # 判断是否符合匿名条件
            if not is_valid(data, lv, sensitive) or not is_valid(data, rv, sensitive):
                continue
            partitions.extend((lv, rv))
            break
        else:
            finished_partitions.append(partition)
    # 返回完成好的分区集合
    return finished_partitions


def agg_category_column(series):
    return series.iloc[0]


def agg_numerical_column(series):
    return series.mean()


def build_anonymized_dataset(data, partitions, quasi_id, sensitive, max_partitions=None):
    rows = []
    for i, partition in enumerate(partitions):
        if max_partitions is not None and i > max_partitions:
            break

        # 从原始数据集中获取对应分区的数据
        anonymized_data = data.loc[partition].copy()
        rows.append(anonymized_data)

    # 合并数据，并确保保留了原始数据集的所有列
    result = pd.concat(rows, ignore_index=True)
    return result[data.columns]


def t_closeness(data, partition, column, global_freqs):
    # 计算分区的长度
    total_count = float(len(partition))
    d_max = None
    # 分组并统计数据出现的频数
    group_counts = data.loc[partition].groupby(column)[column].agg('count')
    for value, count in group_counts.to_dict().items():
        p = count / total_count
        # 计算两个分布的距离
        d = abs(p - global_freqs[value])
        if d_max is None or d > d_max:
            d_max = d
    return d_max


def is_t_closeness(data, partition, sensitive, global_freqs, t=0.3):
    return t_closeness(data, partition, sensitive, global_freqs) <= t

if __name__ == "__main__":
    columns = ['age', 'work_class', 'fin_weight', 'education', 'edu_num', 'mar_status', 'occupation', 'relaship',
               'race', 'gender', '    cap_gain', 'cap_loss', 'hours_pweek', 'country', 'income']
    category = set(
        ('work_class', 'education', 'mar_status', 'occupation', 'relaship', 'gender', 'country', 'race', 'income',))
    quasi_id = ['age', 'edu_num']
    sensitive = 'income'

    data = pd.read_csv(sys.argv[1], sep=", ", header=None, names=columns, index_col=False,
                       engine='python')

    data = data[~data.isin(['?']).any(axis=1)]

    print(data.head())

    for name in category:
        data[name] = data[name].astype('category')

    t = float(sys.argv[3])

    # 获得分区
    full_spans = get_spans(data, data.index)

    global_freqs = {}
    # 计算数据集的长度
    total_count = float(len(data))
    # 分组并统计数据出现的频数
    group_counts = data.groupby(sensitive)[sensitive].agg('count')
    for value, count in group_counts.to_dict().items():
        # 计算敏感属性在整个数据集的分布
        p = count / total_count
        global_freqs[value] = p

    column_x, column_y = quasi_id[:2]

    finished_t_closeness_partitions = partition_dataset(data, quasi_id, sensitive, full_spans,
                                                    lambda *args: is_k_anonymity(*args) and is_t_closeness(*args,
                                                                                                           global_freqs))

    data_closeness = build_anonymized_dataset(data, finished_t_closeness_partitions, quasi_id, sensitive)

    data_closeness.sort_values([column_x, column_y, sensitive])

    data_closeness.to_csv(sys.argv[2], index = False)
    print("===============t-closeness===============")
    print(data_closeness)
