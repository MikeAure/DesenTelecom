package com.lu.gademo.ControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerExcelTest {

    private static final List<String> FIFTY_SCENE = Arrays.asList("map", "onlinetaxi", "communication", "community",
            "onlinepayment", "onlineshopping", "takeaway", "express", "transportationticket", "marry", "employment",
            "onlinelending", "house", "usedcar", "consultation", "travel", "hotel", "game", "education", "locallife",
            "woman", "usecar", "investment", "bank", "mailbox", "meeting", "webcast", "onlinemovie", "shortvideo",
            "news", "sports", "browser", "input", "security", "ebook", "capture", "appstore", "tools", "performanceticket",
            "networkaccess", "telecommunication", "monitor", "pay", "customerservice", "schoolservice", "smarthome",
            "autonomousdriving", "telemedicine", "vr", "onlinevoting");

//        private static final List<String> FIFTY_SCENE = Arrays.asList("pay", "customerservice", "schoolservice", "smarthome",
//            "autonomousdriving", "telemedicine", "vr", "onlinevoting");

//    private static final List<String> FIFTY_SCENE = Arrays.asList("pay");


    private static final List<String> EXCEL_FILE_NAMES = Arrays.asList("appStore.xlsx", "autonomousdriving.xlsx", "bank.xlsx",
            "browser.xlsx", "capture.xlsx", "communication.xlsx", "community.xlsx", "consultation.xlsx", "customerservice.xlsx",
            "ebook.xlsx", "education.xlsx", "employment.xlsx", "express.xlsx", "game.xlsx", "hotel.xlsx", "house.xlsx", "input.xlsx",
            "investment.xlsx", "localLife.xlsx", "mailbox.xlsx", "map.xlsx", "marry.xlsx", "meeting.xlsx", "monitor.xlsx",
            "networkaccess.xlsx", "news.xlsx", "onlineLending.xlsx", "onlineMovie.xlsx", "onlinePayment.xlsx", "onlineShopping.xlsx",
            "onlineTaxi.xlsx", "onlinevoting.xlsx",
            "pay.xlsx", "performanceTicket.xlsx", "schoolservice.xlsx", "security.xlsx", "shortVideo.xlsx", "smarthome.xlsx",
            "sports.xlsx", "takeaway.xlsx", "telecommunication.xlsx", "telemedicine.xlsx", "tools.xlsx", "transportationTicket.xlsx",
            "travel.xlsx", "useCar.xlsx", "usedCar.xlsx", "vr.xlsx", "webcast.xlsx", "woman.xlsx"
    );
    private static final String URL = "/File/desenFile";
    private static final String PARAMS = "1";
    //    private static final String SHEET = "yourSheet";
    private static final String EXCEL_FILE_PATH = "src/test/resources/test_data/sheets/Table/";
    private static final String VIDEO_FILE_NAME = "3.mp4";
    private static final String VIDEO_FILE_TYPE = "video/mp4";
    private static final String RAW_FORM_FIELD = "file";
    private static final String FACE_FORM_FIELD = "sheet";
    private static final String FACE_IMAGE_FILE_PATH = "src/test/resources/test_data/image/3.png";
    private static final String FACE_IMAGE_FILE_NAME = "3.png";
    private static final String FACE_IMAGE_FILE_TYPE = String.valueOf(MediaType.IMAGE_PNG);
    private final MockMvc mvc;
    MockMultipartFile videoFile;
    MockMultipartFile faceImageFile;
    Path excelPath;

    @Autowired
    public FileControllerExcelTest(MockMvc mvc) throws IOException {
        this.mvc = mvc;
        this.excelPath = Paths.get(EXCEL_FILE_PATH);

    }

    // 模板参数获取
    @Test
    public void getExcelParam() throws Exception {
        System.out.println(FIFTY_SCENE.size());
        for (String sceneName : FIFTY_SCENE) {

            String url = "/" + sceneName + "param/list";
            // 模拟multipart/form-data请求
            MvcResult result = mvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));


        }

    }

    @Test
    public void excelFilesTest() throws Exception {
        for (String sceneName : FIFTY_SCENE) {

            String url = "/" + sceneName + "param/list";
            // 模拟multipart/form-data请求
            MvcResult excelParam = mvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

            String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
            System.out.println("Params: " + params);
            String fileName = EXCEL_FILE_NAMES.stream()
                    .filter(name -> name.substring(0, name.lastIndexOf("."))
                            .equalsIgnoreCase(sceneName))
                    .findFirst().get();
            System.out.println(fileName);
            byte[] excelBytes = Files.readAllBytes(excelPath.resolve(fileName));
            MockMultipartFile excelFile = new MockMultipartFile("file", fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelBytes);

            mvc.perform(multipart(URL)
                        .file(excelFile)
                        .param("sheet", sceneName)
                        .param("params", params)
                        .param("algName", "distortion"))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

        }

    }


    // 基于像素化滤波器的视频帧像素替换方法
//    @Test
//    public void imageControllerPixelateVideoTest() throws Exception {
//        // 模拟multipart/form-data请求
//        mvc.perform(multipart(URL) // 使用你的实际请求路径
//                        .file(videoFile)
//                        .param("params", PARAMS)
//                        .param("algName", "pixelate_video")
//                        .param("sheet", SHEET))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//
//    }
//
//    // 基于高斯滤波器的视频帧像素替换方法
//    @Test
//    public void imageControllerGaussianBlurVideoTest() throws Exception {
//
//        // 模拟multipart/form-data请求
//        mvc.perform(multipart(URL) // 使用你的实际请求路径
//                        .file(videoFile)
//                        .param("params", PARAMS)
//                        .param("algName", "gaussian_blur_video")
//                        .param("sheet", SHEET))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//
//    }
//
//    // 基于盒式滤波器的视频帧像素替换方法
//    @Test
//    public void imageControllerBoxBlurVideoTest() throws Exception {
//        // 模拟multipart/form-data请求
//        mvc.perform(multipart(URL) // 使用你的实际请求路径
//                        .file(videoFile)
//                        .param("params", PARAMS)
//                        .param("algName", "box_blur_video")
//                        .param("sheet", SHEET))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//
//    }
//
//    // 基于像素块的视频帧像素替换方法
//    @Test
//    public void imageControllerReplaceRegionVideoTest() throws Exception {
//        // 模拟multipart/form-data请求
//        mvc.perform(multipart(URL) // 使用你的实际请求路径
//                        .file(videoFile)
//                        .param("params", PARAMS)
//                        .param("algName", "replace_region_video")
//                        .param("sheet", SHEET))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//
//    }
//
//    // 基于像素块的视频帧像素颜色偏移方法
//    @Test
//    public void imageControllerVideoAddColorOffsetTest() throws Exception {
//        // 模拟multipart/form-data请求
//        mvc.perform(multipart(URL) // 使用你的实际请求路径
//                        .file(videoFile)
//                        .param("params", PARAMS)
//                        .param("algName", "video_add_color_offset")
//                        .param("sheet", SHEET))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
//
//    }
}
