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
public class FileControllerImageTest {

    private MockMvc mvc;

    private static final String URL = "/File/desenFile";
    private static final String PARAMS = "1";
    private static final String SHEET = "yourSheet";
    private static final String IMAGE_FILE_PATH = "src/test/resources/test_data/image/test.png";
    private static final String IMAGE_FILE_PATH2 = "src/test/resources/test_data/image/square.jpg";
    private static final String IMAGE_FILE_NAME = "test.png";
    private static final String IMAGE_FILE_TYPE = String.valueOf(MediaType.IMAGE_PNG);

    private static final String FACE_IMAGE_FILE_PATH = "src/test/resources/test_data/image/3.png";
    private static final String FACE_IMAGE_FILE_NAME = "3.png";
    private static final String FACE_IMAGE_FILE_TYPE = String.valueOf(MediaType.IMAGE_PNG);

    private static final String RAW_FORM_FIELD = "file";

    private static final String FACE_FORM_FIELD = "sheet";


    MockMultipartFile imageFile;
    MockMultipartFile faceImageFile;
    @Autowired
    public FileControllerImageTest(MockMvc mvc) throws IOException {
        this.mvc = mvc;
        byte[] imageBytes = Files.readAllBytes(Paths.get(IMAGE_FILE_PATH));
        this.imageFile = new MockMultipartFile(RAW_FORM_FIELD, IMAGE_FILE_NAME, IMAGE_FILE_TYPE,
                Files.readAllBytes(Paths.get(IMAGE_FILE_PATH)));
        this.faceImageFile = new MockMultipartFile(FACE_FORM_FIELD, FACE_IMAGE_FILE_NAME, FACE_IMAGE_FILE_TYPE,
                Files.readAllBytes(Paths.get(FACE_IMAGE_FILE_PATH)));

    }

    // 基于均值滤波器的图像加噪方法
    @Test
    public void imageControllerMeanValueImageTest() throws Exception {
        for (int i = 0; i < 3; i++) {
            // 模拟multipart/form-data请求
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(imageFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "meanValueImage")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // 基于高斯滤波器的图像加噪方法
    @Test
    public void imageControllerGaussianBlurImageTest() throws Exception {
        for (int i = 0; i < 3; i++) {
            // 模拟multipart/form-data请求
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(imageFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "gaussian_blur")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // 基于像素化滤波器的图像加噪方法
    @Test
    public void imageControllerPixelateImageTest() throws Exception {
        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(imageFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "pixelate")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // 基于盒式滤波器的图像加噪方法
    @Test
    public void imageControllerBoxBlurImageTest() throws Exception {
        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(imageFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "box_blur")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // 基于差分隐私的图像加噪方法
    @Test
    public void imageControllerDpImageTest() throws Exception {
        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(imageFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "dpImage")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // imcoder2
    @Test
    public void imageControllerDpImage2Test() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(RAW_FORM_FIELD, IMAGE_FILE_NAME, IMAGE_FILE_TYPE,
                Files.readAllBytes(Paths.get(IMAGE_FILE_PATH2)));
        // 模拟multipart/form-data请求
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL) // 使用你的实际请求路径
                            .file(mockMultipartFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "im_coder2")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // 基于像素块的图像区域替换方法
    @Test
    public void imageControllerReplaceRegionImageTest() throws Exception {
        for (int i = 0; i < 3; i++) {
            mvc.perform(multipart(URL)
                            .file(imageFile)
                            .param("params", String.valueOf(i))
                            .param("algName", "replace_region")
                            .param("sheet", SHEET))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        }

    }

    // 图像颜色随机替换方法
    @Test
    public void imageControllerImageExchangeChannelTest() throws Exception {
        mvc.perform(multipart(URL)
                        .file(imageFile)
                        .param("params", PARAMS)
                        .param("algName", "image_exchange_channel")
                        .param("sheet", SHEET))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }


    // 图像颜色偏移
    @Test
    public void imageControllerImageAddColorOffsetTest() throws Exception {
        mvc.perform(multipart(URL)
                        .file(imageFile)
                        .param("params", PARAMS)
                        .param("algName", "image_add_color_offset")
                        .param("sheet", SHEET))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    // 图像人脸替换算法
    @Test
    public void imageControllerImgFaceSubTest() throws Exception {
        mvc.perform(multipart("/File/replaceFace")
                        .file(imageFile)
                        .file(faceImageFile)
                        .param("params", PARAMS)
                        .param("algName", "image_face_sub")
                        )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }




}
