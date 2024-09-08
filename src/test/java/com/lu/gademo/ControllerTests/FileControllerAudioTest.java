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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
public class FileControllerAudioTest {
    private static final String URL = "/File/desenFile";
    private static final String AUDIO_FILE_NAME = "0001.wav";
    private static final String AUDIO_FILE_TYPE = "audio/wav";
    private static final String AUDIO_FILE_PATH = "src/test/resources/test_data/audio/0001.wav";
    private static final String PARAMS = "1";
    private static final String SHEET = "yourSheet";
    private MockMvc mvc;
    private MockMultipartFile audioFile;

    @Autowired
    public FileControllerAudioTest (MockMvc mvc) throws IOException {
        this.audioFile = new MockMultipartFile("file", AUDIO_FILE_NAME, AUDIO_FILE_TYPE,
                Files.readAllBytes(Paths.get(AUDIO_FILE_PATH)));
        this.mvc = mvc;
    }

    @Test
    // 基于差分隐私的声纹特征脱敏算法
    public void testDpAudio () throws Exception {
        mvc.perform(multipart(URL)
                .file(audioFile)
                    .param("params", PARAMS)
                    .param("algName", "dpAudio")
                    .param("sheet", SHEET))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    // 声纹替换算法
    public void testVoiceReplace() throws Exception {
        mvc.perform(multipart(URL)
                        .file(audioFile)
                        .param("params", PARAMS)
                        .param("algName", "voice_replace")
                        .param("sheet", SHEET))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    // 音频变形
    public void testApplyAudioEffects() throws Exception {
        mvc.perform(multipart(URL)
                        .file(audioFile)
                        .param("params", PARAMS)
                        .param("algName", "apply_audio_effects")
                        .param("sheet", SHEET))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    // 音频重排
    public void testAudioReshuffle() throws Exception {
        mvc.perform(multipart(URL)
                        .file(audioFile)
                        .param("params", PARAMS)
                        .param("algName", "audio_reshuffle")
                        .param("sheet", SHEET))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    // 音频取整
    public void testAudioFloor() throws Exception {
        mvc.perform(multipart(URL)
                        .file(audioFile)
                        .param("params", PARAMS)
                        .param("algName", "audio_floor")
                        .param("sheet", SHEET))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    // 频域遮掩
    public void testAudioSpec() throws Exception {
        mvc.perform(multipart(URL)
                        .file(audioFile)
                        .param("params", PARAMS)
                        .param("algName", "audio_spec")
                        .param("sheet", SHEET))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    // 音频失真
    public void testAudioAugmentation() throws Exception {
        mvc.perform(multipart(URL)
                        .file(audioFile)
                        .param("params", PARAMS)
                        .param("algName", "audio_augmentation")
                        .param("sheet", SHEET))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    // 基于均值的采样点替换
    public void testAudioMedian() throws Exception {
        mvc.perform(multipart(URL)
                        .file(audioFile)
                        .param("params", PARAMS)
                        .param("algName", "audio_median")
                        .param("sheet", SHEET))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
