package com.lu.gademo.service;

public interface ToolsetService {
    String getDefaultTool(String toolsetName);
    boolean setDefaultTool(String toolsetName, String defaultAlgName);
}
