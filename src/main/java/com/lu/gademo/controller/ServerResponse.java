package com.lu.gademo.controller;

public class ServerResponse {
    private String status;
    private String message;

    public ServerResponse() {}

    public ServerResponse(String status) {
        this.status = status;
    }

    public ServerResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {
        this.message = message;
    }
}
