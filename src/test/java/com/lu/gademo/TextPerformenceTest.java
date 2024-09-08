package com.lu.gademo;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TextPerformenceTest {
    @Test
    public void testGenerate500000Data() {
        String filePath = "output.txt"; // 定义输出文件的路径
        int numberOfEntries = 500000;   // 要生成的数据条数

        // 使用随机数生成器生成数值数据
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < numberOfEntries; i++) {
                double randomNumber = 1000 * random.nextDouble(); // 生成随机整数
                writer.write(String.valueOf(randomNumber));
                writer.newLine(); // 每个数字后写入一个新行
            }
            System.out.println("数据生成完毕并写入到文件 " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
