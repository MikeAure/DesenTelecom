import hashlib
import math
import re
from datetime import datetime


def hide_number(text: str, repl_num=0):
    text = re.sub(r'\d', str(repl_num), text)
    return text


def hashing_md5(s: str) -> str:
    """返回值为小写16进制字符串"""
    return hashlib.md5(s.encode()).hexdigest()


def hashing_sha1(s: str) -> str:
    """返回值为小写16进制字符串"""
    return hashlib.sha1(s.encode()).hexdigest()


def hashing_sha224(s: str) -> str:
    """返回值为小写16进制字符串"""
    return hashlib.sha224(s.encode()).hexdigest()


def hashing_sha256(s: str) -> str:
    """返回值为小写16进制字符串"""
    return hashlib.sha256(s.encode()).hexdigest()


def hashing_sha384(s: str) -> str:
    """返回值为小写16进制字符串"""
    return hashlib.sha384(s.encode()).hexdigest()


def hashing_sha512(s: str) -> str:
    """返回值为小写16进制字符串"""
    return hashlib.sha512(s.encode()).hexdigest()


def shift(num, shift_by=1000):
    """为数量值增加一个固定的偏移量，隐藏数值部分特征。"""
    return num + shift_by


def enumeration(num):
    """将数据映射为新值，同时保持数据顺序"""
    return num * 50


def truncation(text: str):
    return text[:3]


def floor_num(num):
    return math.floor(num)


def floor_time(text):
    input_datetime = datetime.strptime(text, "%H:%M:%S")
    floor_datetime = input_datetime.replace(minute=0, second=0, microsecond=0)
    result_text = floor_datetime.strftime("%H:%M:%S")
    return result_text
