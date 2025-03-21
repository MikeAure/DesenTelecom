package com.lu.gademo.ControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerVideoTest {

    private MockMvc mvc;

    private static final String URL = "/File/desenFile";
    private static final String PARAMS = "1";
    private static final String SHEET = "yourSheet";
    private static final String VIDEO_FILE_PATH = "src/test/resources/test_data/video/test_1.mp4";
    private static final String VIDEO_FILE_NAME = "test_1.mp4";
    private static final String VIDEO_FILE_TYPE = "video/mp4";
    private static final String RAW_FORM_FIELD = "file";
    private static final String FACE_FORM_FIELD = "sheet";

    private static final String FACE_IMAGE_FILE_PATH = "src/test/resources/test_data/image/replace_face2.png";
    private static final String FACE_IMAGE_FILE_NAME = "replace_face2.png";
    private static final String FACE_IMAGE_FILE_TYPE = String.valueOf(MediaType.IMAGE_PNG);

    MockMultipartFile videoFile;
    MockMultipartFile faceImageFile;

    @Autowired
    public FileControllerVideoTest(MockMvc mvc) throws IOException {
        this.mvc = mvc;

        byte[] videoBytes = Files.readAllBytes(Paths.get(VIDEO_FILE_PATH));
        this.videoFile = new MockMultipartFile(RAW_FORM_FIELD, VIDEO_FILE_NAME, VIDEO_FILE_TYPE, videoBytes);

        this.faceImageFile = new MockMultipartFile(FACE_FORM_FIELD, FACE_IMAGE_FILE_NAME, FACE_IMAGE_FILE_TYPE,
                Files.readAllBytes(Paths.get(FACE_IMAGE_FILE_PATH)));
    }

    // 基于均值滤波器的视频帧像素替换方法
    @Test
    public void videoControllerMeanValueVideoTest() throws Exception {
        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(videoFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "meanValueVideo")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // 基于高斯滤波器的视频帧像素替换方法
    @Test
    public void videoControllerGaussianBlurVideoTest() throws Exception {

        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(videoFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "gaussian_blur_video")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }
    }

    // 基于像素化滤波器的视频帧像素替换方法
    @Test
    public void videoControllerPixelateVideoTest() throws Exception {
        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(videoFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "pixelate_video")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }



    // 基于盒式滤波器的视频帧像素替换方法
    @Test
    public void videoControllerBoxBlurVideoTest() throws Exception {
        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(videoFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "box_blur_video")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // 基于像素块的视频帧像素替换方法
    @Test
    public void videoControllerReplaceRegionVideoTest() throws Exception {
        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(videoFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "replace_region_video")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // 基于像素块的视频帧像素颜色偏移方法
    @Test
    public void videoControllerVideoAddColorOffsetTest() throws Exception {
        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(videoFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "video_add_color_offset")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }


//     视频背景替换算法
    @Test
    public void videoControllerVideoRemoveBgTest() throws Exception {
        // 模拟multipart/form-data请求
            mvc.perform(multipart("/File/removeBackground") // 使用你的实际请求路径
                            .file(videoFile)
                            .file(faceImageFile)
                            .param("params", PARAMS)
                            .param("algName", "video_remove_bg"))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

    }

    // 视频人脸替换算法
    @Test
    public void videoControllerVideoFaceSubTest() throws Exception {
        // 模拟multipart/form-data请求
            mvc.perform(multipart("/File/replaceFaceVideo") // 使用你的实际请求路径
                            .file(videoFile)
                            .file(faceImageFile)
                            .param("params", PARAMS)
                            .param("algName", "video_face_sub"))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print()).andReturn();


    }

}
