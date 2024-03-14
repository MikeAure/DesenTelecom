package com.lu.gademo.trace.server.gui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        File input = new File("d:\\staticmap.png");
        BufferedImage image = ImageIO.read(input);
        int length = image.getWidth() > image.getHeight() ? image.getHeight() : image.getWidth();
        BufferedImage outputImage = image.getSubimage(0, 0, length, length);
        File output = new File("img/output.png");
        ImageIO.write(outputImage, "png", output);
    }

}
