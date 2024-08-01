package com.lu.gademo.utils;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.io.IOException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * 非失真视频脱敏工具
 */
public class VideoUtil {

    static SecretKey genAESKey(String password) throws NoSuchAlgorithmException {
        byte[] words = password.getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(words);
        return new SecretKeySpec(Arrays.copyOf(hash, 16), "AES");
    }

    public static void videoAESEncOrDec(String inputFilePath, String outputFilePath, String key, int mode) {
        try {
            SecretKey aesKey = genAESKey(key);
            IvParameterSpec ivParameterSpec = getIvParameterSpec(inputFilePath, mode);

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(mode, aesKey, ivParameterSpec);

            try (FileInputStream fis = new FileInputStream(inputFilePath);
                 FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                if (mode == Cipher.ENCRYPT_MODE) {
                    fos.write(ivParameterSpec.getIV());
                } else fis.skip(16);

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    byte[] output = cipher.update(buffer, 0, bytesRead);
                    if (output != null) {
                        fos.write(output);
                    }
                }

                byte[] outputBytes = cipher.doFinal();
                if (outputBytes != null) {
                    fos.write(outputBytes);
                }
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | IOException |
                 BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

    }

    private static IvParameterSpec getIvParameterSpec(String inputFilePath, int mode) throws IOException {
        IvParameterSpec ivParameterSpec;

        if (mode == Cipher.ENCRYPT_MODE) {
            SecureRandom random = new SecureRandom();
            byte[] ivBytes = new byte[16];
            random.nextBytes(ivBytes);
            ivParameterSpec = new IvParameterSpec(ivBytes);
        } else {
            try (FileInputStream fis = new FileInputStream(inputFilePath)) {
                byte[] ivBytes = new byte[16];
                if (fis.read(ivBytes) == 16) {
                    ivParameterSpec = new IvParameterSpec(ivBytes);
                } else {
                    throw new IOException("");
                }
            }
        }
        return ivParameterSpec;
    }

    static void swapPixel(UByteIndexer src, UByteIndexer dst, long rowOfA, long colOfA, long rowOfB, long colOfB) {
        int[] pixel = new int[3];
        src.get(rowOfA, colOfA, pixel);
        dst.put(rowOfB, colOfB, pixel);
    }

    static Mat swapPixelsOfMat(Mat mat, SecureRandom secureRandom, boolean isEnc) {
        int row = mat.rows();
        int col = mat.cols();
        Mat dst = mat.clone();

        if (!(row == 0 || col == 0)) {
            UByteIndexer srcUByteIndexer = mat.createIndexer();
            UByteIndexer dstUByteIndexer = dst.createIndexer();

            int randRow = secureRandom.nextInt(row);
            for (int i = 0; i < row; i++) {
                int newRow = ((i + randRow) >= row) ? i + randRow - row : i + randRow;
                int randCol = secureRandom.nextInt(col);
                for (int j = 0; j < col; j++) {
                    int newCol = ((j + randCol) >= col) ? j + randCol - col : j + randCol;
                    if (isEnc) {
                        swapPixel(srcUByteIndexer, dstUByteIndexer, i, j, newRow, newCol);
                    } else {
                        swapPixel(srcUByteIndexer, dstUByteIndexer, newRow, newCol, i, j);
                    }
                }
            }
        }

        return dst;
    }

    public static void videoUtilEncOrDec(String inputFilePath, String outputFilePath, String key, boolean isEnc) throws IOException {
        try (FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(inputFilePath)) {
            fFmpegFrameGrabber.start();
            try (FFmpegFrameRecorder fFmpegFrameRecorder = new FFmpegFrameRecorder(
                    outputFilePath,
                    fFmpegFrameGrabber.getImageWidth(),
                    fFmpegFrameGrabber.getImageHeight(),
                    fFmpegFrameGrabber.getAudioChannels())) {
                fFmpegFrameRecorder.setFrameNumber(fFmpegFrameGrabber.getFrameNumber());
                fFmpegFrameRecorder.setVideoCodec(fFmpegFrameGrabber.getVideoCodec());
                fFmpegFrameRecorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
                fFmpegFrameRecorder.setVideoQuality(0);
                fFmpegFrameRecorder.start();

                try (OpenCVFrameConverter.ToMat openCVFrameConverter = new OpenCVFrameConverter.ToMat()) {
                    Frame frame;
                    SecureRandom secureRandom = new SecureRandom(key.getBytes());
                    while ((frame = fFmpegFrameGrabber.grabFrame()) != null) {
                        Mat mat = openCVFrameConverter.convert(frame);
                        if (mat != null) {
                            Mat dst = swapPixelsOfMat(mat, secureRandom, isEnc);
                            Frame dstFrame = openCVFrameConverter.convert(dst);
                            fFmpegFrameRecorder.record(dstFrame);
                        }
                    }
                    fFmpegFrameRecorder.stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                fFmpegFrameGrabber.stop();
            }
        }
    }
}
