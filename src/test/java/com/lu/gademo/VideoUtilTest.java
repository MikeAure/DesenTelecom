package com.lu.gademo;

import org.junit.jupiter.api.Test;
import com.lu.gademo.utils.VideoUtil;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class VideoUtilTest {
    @Test
    void videoUtilEncTest() {
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String iPath = Paths.get(currentPath, "video", "yewen1.mp4").toString();
        String oPath = Paths.get(currentPath, "video", "3_video_util.mp4").toString();
        boolean isEnc = true;
        try {
            VideoUtil.videoUtilEncOrDec(iPath, oPath, "Test", isEnc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void videoUtilDecTest() {
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String iPath = Paths.get(currentPath, "video", "3_video_util_o.mp4").toString();
        String oPath = Paths.get(currentPath, "video", "3_video_util.mp4").toString();
        boolean isEnc = false;
        try {
            VideoUtil.videoUtilEncOrDec(oPath, iPath, "Test", isEnc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void videoAESEncTest() {
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String iPath = Paths.get(currentPath, "video", "yewen1.mp4").toString();
        String oPath = Paths.get(currentPath, "video", "3_video_util.mp4").toString();
        VideoUtil.videoAESEncOrDec(iPath, oPath, "Test", Cipher.ENCRYPT_MODE);
    }

    @Test
    void videoAESDecTest() {
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String iPath = Paths.get(currentPath, "video", "3_video_util_o.mp4").toString();
        String oPath = Paths.get(currentPath, "video", "3_video_util.mp4").toString();
        VideoUtil.videoAESEncOrDec(oPath, iPath, "Test", Cipher.DECRYPT_MODE);
    }
}
