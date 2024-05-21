package com.lu.gademo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Result<T> implements Serializable {

    String message;
    T data;
}
