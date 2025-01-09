package com.lu.gademo.ControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerAudioTest {
    private static final String URL = "/File/desenFile";
    private static final String AUDIO_FILE_NAME = "english_0001.wav";
    private static final String AUDIO_FILE_TYPE = "audio/wav";
    private static final String AUDIO_FILE_PATH = "src/test/resources/test_data/audio/distortion/english_0001.wav";
    private static final String AUDIO_FILE_DIR = "src/test/resources/test_data/audio/distortion";
    private static final String PARAMS = "1";
    private static final String SHEET = "yourSheet";
    private MockMvc mvc;
    private MockMultipartFile audioFile;

    @Autowired
    public FileControllerAudioTest(MockMvc mvc) throws IOException {
        this.audioFile = new MockMultipartFile("file", AUDIO_FILE_NAME, AUDIO_FILE_TYPE,
                Files.readAllBytes(Paths.get(AUDIO_FILE_PATH)));
        this.mvc = mvc;
    }

    @Test
    // 基于差分隐私的声纹特征脱敏算法
    public void testDpAudio() throws Exception {
        try (Stream<Path> pathList = Files.list(Paths.get(AUDIO_FILE_DIR))) {
            pathList.forEach(path -> {
                try {
                    if (path.getFileName().toString().endsWith("mp3")) return;
                    MockMultipartFile audioFile = new MockMultipartFile("file", path.getFileName().toString(), AUDIO_FILE_TYPE,
                            Files.readAllBytes(path));
                    System.out.println("Current File Name: " + path.getFileName().toString());
                    mvc.perform(multipart(URL)
                                    .file(audioFile)
                                    .param("params", PARAMS)
                                    .param("algName", "dpAudio")
                                    .param("sheet", SHEET))
                            .andDo(MockMvcResultHandlers.print())
                            .andExpect(status().isOk())
                            .andReturn();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    @Test
    // 声纹替换算法
    public void testVoiceReplace() throws Exception {
        try (Stream<Path> pathList = Files.list(Paths.get(AUDIO_FILE_DIR))) {
            pathList.forEach(path -> {
                try {
                    MockMultipartFile audioFile = null;
                    if (path.getFileName().toString().endsWith("mp3")) return;

                    audioFile = new MockMultipartFile("file", path.getFileName().toString(), AUDIO_FILE_TYPE,
                            Files.readAllBytes(path));

                    System.out.println("Current File Name: " + path.getFileName().toString());
                    mvc.perform(multipart(URL)
                                    .file(audioFile)
                                    .param("params", PARAMS)
                                    .param("algName", "voice_replace")
                                    .param("sheet", SHEET))
                            .andDo(MockMvcResultHandlers.print())
                            .andExpect(status().isOk())
                            .andReturn();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
//        mvc.perform(multipart(URL)
//                        .file(audioFile)
//                        .param("params", PARAMS)
//                        .param("algName", "voice_replace")
//                        .param("sheet", SHEET))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn();
    }

    @Test
    // 音频变形
    public void testApplyAudioEffects() throws Exception {
        try (Stream<Path> pathList = Files.list(Paths.get(AUDIO_FILE_DIR))) {
            pathList.forEach(path -> {
                try {
                    MockMultipartFile audioFile = null;
                    if (path.getFileName().toString().endsWith("mp3")) {
                        audioFile = new MockMultipartFile("file", path.getFileName().toString(), "audio/mp3",
                                Files.readAllBytes(path));
                    } else {
                        audioFile = new MockMultipartFile("file", path.getFileName().toString(), AUDIO_FILE_TYPE,
                                Files.readAllBytes(path));
                    }

                    System.out.println("Current File Name: " + path.getFileName().toString());
                    for (int i = 0; i < 3; i++) {
                        mvc.perform(multipart(URL)
                                        .file(audioFile)
                                        .param("params", String.valueOf(i))
                                        .param("algName", "apply_audio_effects")
                                        .param("sheet", SHEET))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(status().isOk())
                                .andReturn();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
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
