package com.lu.gademo.utils.impl;

import com.lu.gademo.utils.AudioUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

//音频评测工具集
public class AudioUtilImpl implements AudioUtil {

    public double[] readVectorFromFile_1(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                String[] values = line.replace("[", "").replace("]", "").replace(",", " ").split("\\s+");    //.split(",");

                double[] doubleArray = Arrays.stream(values)
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                return doubleArray;
            }
        }
        throw new IOException("Failed to read vector from file: " + filePath);
    }

    public double[] readVectorFromFile_2(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                String[] values = line.replace("[", "").replace("]", "").replace(",", " ").trim().split("\\s+");    //.split(",");

                double[] doubleArray = Arrays.stream(values)
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                return doubleArray;
            }
        }
        throw new IOException("Failed to read vector from file: " + filePath);
    }


    @Override
    public double cosineSimilarity(String file1, String file2) throws IOException {
        double[] vectorA = readVectorFromFile_1(file1);
        double[] vectorB = readVectorFromFile_2(file2);

        double dotProduct = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            magnitude1 += Math.pow(vectorA[i], 2);
            magnitude2 += Math.pow(vectorB[i], 2);
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);


        DecimalFormat df = new DecimalFormat("#.000");
        return Double.parseDouble(df.format(dotProduct / (magnitude1 * magnitude2)));
    }

    @Override
    public double euclideanDistance(String file1, String file2) throws IOException {
        double[] vectorA = readVectorFromFile_1(file1);
        double[] vectorB = readVectorFromFile_2(file2);

        double sum = 0;

        for (int i = 0; i < vectorA.length; i++) {
            sum += Math.pow(vectorA[i] - vectorB[i], 2);
        }

        DecimalFormat df = new DecimalFormat("#.000");
        return Double.parseDouble(df.format(Math.sqrt(sum)));
    }






}
