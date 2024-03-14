package com.lu.gademo.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageUtil {
    // 计算PSNR的方法
    Double calculatePSNR(BufferedImage img1, BufferedImage img2);

    // 计算SSIM的方法

    double calculateSSIM(String imagePath1, String imagePath2);

    //计算信息熵的方法
    double calculateImageEntropy(String imagePath) throws IOException;
}
