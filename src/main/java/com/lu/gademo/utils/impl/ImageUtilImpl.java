package com.lu.gademo.utils.impl;

import com.lu.gademo.utils.ImageUtil;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

//图片处理工具集
public class ImageUtilImpl implements ImageUtil {

    static {
        // 在 Windows 上设置本地库路径
            URL url = ImageUtilImpl.class.getClassLoader().getResource("lib/opencv/opencv_java455.dll");
            System.load(url.getPath());
    }
    // 计算PSNR的方法
    @Override
    public Double calculatePSNR(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();

        long mse = 0; // 均方误差

        // 计算每个像素的均方误差
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);

                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;

                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;

                int dr = r1 - r2;
                int dg = g1 - g2;
                int db = b1 - b2;

                mse += (dr * dr + dg * dg + db * db);
            }
        }

        // 计算均方误差的平均值
        mse /= width * height;

        // 计算PSNR
        double maxPixelValue = 255.0;
        double psnr = 10 * Math.log10((maxPixelValue * maxPixelValue) / mse);

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(psnr));
        return formattedValue;
    }

    //计算SSIM，将图片转为了灰度图
    @Override
    public double calculateSSIM(String imagePath1, String imagePath2) {
//         读取图像
        Mat image1 = Imgcodecs.imread(imagePath1);
        Mat image2 = Imgcodecs.imread(imagePath2);

        // 转换图像为灰度图
        Imgproc.cvtColor(image1, image1, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(image2, image2, Imgproc.COLOR_BGR2GRAY);

        // 转换图像为浮点数类型
        image1.convertTo(image1, CvType.CV_32F);
        image2.convertTo(image2, CvType.CV_32F);

        // 计算 SSIM
        MatOfFloat ssim = new MatOfFloat();
        Imgproc.matchTemplate(image1, image2, ssim, Imgproc.TM_CCOEFF_NORMED);

        // 获取 SSIM 值
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(ssim);
        double ssimValue = minMaxLocResult.maxVal;
//         释放资源
        image1.release();
        image2.release();
        ssim.release();


        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(ssimValue));
        return formattedValue;

    }

    //计算图片信息熵
    @Override
    public double calculateImageEntropy(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));

        // 将图像转为灰度
        BufferedImage grayImage = convertToGray(image);

        // 统计像素值的频率
        int[] pixelCounts = countPixelValues(grayImage);

        // 计算概率分布
        double[] probabilities = calculateProbabilities(pixelCounts, grayImage.getWidth() * grayImage.getHeight());

        // 计算信息熵
        double entropy = calculateEntropy(probabilities);

        return entropy;
    }

    private static BufferedImage convertToGray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color = new Color(image.getRGB(i, j));
                int grayValue = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
                int grayPixel = new Color(grayValue, grayValue, grayValue).getRGB();
                grayImage.setRGB(i, j, grayPixel);
            }
        }

        return grayImage;
    }

    private static int[] countPixelValues(BufferedImage image) {
        int[] pixelCounts = new int[256];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int grayValue = new Color(image.getRGB(i, j)).getRed();
                pixelCounts[grayValue]++;
            }
        }

        return pixelCounts;
    }

    private static double[] calculateProbabilities(int[] pixelCounts, int totalPixels) {
        double[] probabilities = new double[256];

        for (int i = 0; i < 256; i++) {
            probabilities[i] = (double) pixelCounts[i] / totalPixels;
        }

        return probabilities;
    }

    private static double calculateEntropy(double[] probabilities) {
        double entropy = 0.0;

        for (double probability : probabilities) {
            if (probability > 0) {
                entropy -= probability * Math.log(probability) / Math.log(2);
            }
        }


        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(entropy));

        return formattedValue;
    }




}
