package com.lu.gademo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponse<T> {
    private String status;
    private T message;

    public ServerResponse(String status) {
        this.status = status;
    }

}
