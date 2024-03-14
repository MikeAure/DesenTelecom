package com.lu.gademo.service;

import com.lu.gademo.controller.ServerResponse;

import java.io.IOException;

public interface AudioMatchService {

    public ServerResponse getMessage(String wavFile, String email, String template) throws IOException;

    public void shutdown() throws IOException;
}
