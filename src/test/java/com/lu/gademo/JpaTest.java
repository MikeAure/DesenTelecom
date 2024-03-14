package com.lu.gademo;


import com.lu.gademo.utils.AudioUtil;
import com.lu.gademo.utils.ImageUtil;
import com.lu.gademo.utils.impl.AudioUtilImpl;
import com.lu.gademo.utils.impl.ImageUtilImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SpringBootTest
public class JpaTest {
    @Test
    public void test() throws IOException {
        ImageUtil imageUtil=new ImageUtilImpl();
        String imagePath = "C:\\Users\\admin\\Pictures\\111.png";
        String path2 = "C:\\Users\\admin\\Desktop\\demo1\\raw_files\\1.png";
        BufferedImage originalImage = ImageIO.read(new File(imagePath));
        BufferedImage compressedImage = ImageIO.read(new File(path2));
        double entropy =imageUtil.calculateSSIM(imagePath, path2);
        System.out.println("Image Entropy: " + entropy);
    }

    @Test
    public void test1() throws IOException {
        AudioUtil audioUtil = new AudioUtilImpl();
        String path1 =  "C:\\Users\\admin\\Desktop\\demo1\\audio\\temp\\0001_vector.txt";
        String path2 = "C:\\Users\\admin\\Desktop\\demo1\\audio\\temp\\0001_vector_dp.txt";

        System.out.println(audioUtil.cosineSimilarity(path1, path2));
        System.out.println(audioUtil.euclideanDistance(path1, path2));
    }


}
