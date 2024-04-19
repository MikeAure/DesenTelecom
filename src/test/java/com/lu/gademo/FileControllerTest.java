package com.lu.gademo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void imageControllerDpImageTest() throws Exception {
        String url = "/File/desenFile";
        byte[] imageBytes = Files.readAllBytes(Paths.get("D:\\test_data\\image\\test.png"));
        MockMultipartFile file = new MockMultipartFile("file", "test.png", String.valueOf(MediaType.IMAGE_PNG), imageBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "dpImage")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void imageControllerMeanValueImageTest() throws Exception {
        String url = "/File/desenFile";
        byte[] imageBytes = Files.readAllBytes(Paths.get("D:\\test_data\\image\\test.png"));
        MockMultipartFile file = new MockMultipartFile("file", "test.png", String.valueOf(MediaType.IMAGE_PNG), imageBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "meanValueImage")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void imageControllerGaussianBlurImageTest() throws Exception {
        String url = "/File/desenFile";
        byte[] imageBytes = Files.readAllBytes(Paths.get("D:\\test_data\\image\\test.png"));
        MockMultipartFile file = new MockMultipartFile("file", "test.png", String.valueOf(MediaType.IMAGE_PNG), imageBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "gaussian_blur")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void imageControllerPixelateImageTest() throws Exception {
        String url = "/File/desenFile";
        byte[] imageBytes = Files.readAllBytes(Paths.get("D:\\test_data\\image\\test.png"));
        MockMultipartFile file = new MockMultipartFile("file", "test.png", String.valueOf(MediaType.IMAGE_PNG), imageBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "pixelate")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void imageControllerBoxBlurImageTest() throws Exception {
        String url = "/File/desenFile";
        byte[] imageBytes = Files.readAllBytes(Paths.get("D:\\test_data\\image\\test.png"));
        MockMultipartFile file = new MockMultipartFile("file", "test.png", String.valueOf(MediaType.IMAGE_PNG), imageBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "box_blur")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void imageControllerReplaceRegionImageTest() throws Exception {
        String url = "/File/desenFile";
        byte[] imageBytes = Files.readAllBytes(Paths.get("D:\\test_data\\image\\test.png"));
        MockMultipartFile file = new MockMultipartFile("file", "test.png", String.valueOf(MediaType.IMAGE_PNG), imageBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "replace_region")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void imageControllerMeanValueVideoTest() throws Exception {
        String url = "/File/desenFile";
        byte[] videoBytes = Files.readAllBytes(Paths.get("D:\\test_data\\video\\3.mp4"));
        MockMultipartFile file = new MockMultipartFile("file", "3.mp4", "video/mp4", videoBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "meanValueVideo")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void imageControllerGaussianBlurVideoTest() throws Exception {
        String url = "/File/desenFile";
        byte[] videoBytes = Files.readAllBytes(Paths.get("D:\\test_data\\video\\3.mp4"));
        MockMultipartFile file = new MockMultipartFile("file", "3.mp4", "video/mp4", videoBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "gaussian_blur_video")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void imageControllerPixelateVideoTest() throws Exception {
        String url = "/File/desenFile";
        byte[] videoBytes = Files.readAllBytes(Paths.get("D:\\test_data\\video\\3.mp4"));
        MockMultipartFile file = new MockMultipartFile("file", "3.mp4", "video/mp4", videoBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "pixelate_video")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }
    @Test
    public void imageControllerBoxBlurVideoTest() throws Exception {
        String url = "/File/desenFile";
        byte[] videoBytes = Files.readAllBytes(Paths.get("D:\\test_data\\video\\3.mp4"));
        MockMultipartFile file = new MockMultipartFile("file", "3.mp4", "video/mp4", videoBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "box_blur_video")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void imageControllerReplaceRegionVideoTest() throws Exception {
        String url = "/File/desenFile";
        byte[] videoBytes = Files.readAllBytes(Paths.get("D:\\test_data\\video\\3.mp4"));
        MockMultipartFile file = new MockMultipartFile("file", "3.mp4", "video/mp4", videoBytes);

        // 模拟multipart/form-data请求
        mvc.perform(multipart(url) // 使用你的实际请求路径
                        .file(file)
                        .param("params", "1")
                        .param("algName", "replace_region_video")
                        .param("sheet", "yourSheet"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

}
