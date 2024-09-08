package com.lu.gademo.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class AlgorithmsFactory {
    private Dp dp;
    private Generalization generalization;
    private Anonymity anonymity;
    private Replace replacement;

    private Map<String, AlgorithmInfo> algorithmInfoMap;

    @Autowired
    public AlgorithmsFactory(Dp dp, Generalization generalization, Anonymity anonymity, Replace replacement) {
        this.dp = dp;
        this.generalization = generalization;
        this.anonymity = anonymity;
        this.replacement = replacement;
        this.algorithmInfoMap = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        // 文本/表格
        algorithmInfoMap.put("dpDate", new AlgorithmInfo("dpDate", 1, AlgorithmType.DP, 26, Arrays.asList("0.1", "0.01", "0.001"), this.dp));
        algorithmInfoMap.put("dpCode", new AlgorithmInfo("dpCode", 2, AlgorithmType.DP, 20, Arrays.asList("3.6", "2", "0.7"), this.dp));
        algorithmInfoMap.put("laplaceToValue", new AlgorithmInfo("laplaceToValue", 3, AlgorithmType.DP, 1, Arrays.asList("10", "1", "0.1"), this.dp));
        algorithmInfoMap.put("randomUniformToValue", new AlgorithmInfo("randomUniformToValue", 5, AlgorithmType.DP, 21, Arrays.asList("2.0", "10.0", "20.0"), this.dp));
        algorithmInfoMap.put("randomLaplaceToValue", new AlgorithmInfo("randomLaplaceToValue", 6, AlgorithmType.DP, 22, Arrays.asList("1.0", "5.0", "10.0"), this.dp));
        algorithmInfoMap.put("randomGaussianToValue", new AlgorithmInfo("randomGaussianToValue", 7, AlgorithmType.DP, 23, Arrays.asList("1.0", "5.0", "10.0"), this.dp));
        algorithmInfoMap.put("valueShift", new AlgorithmInfo("valueShift", 8, AlgorithmType.REPLACEMENT, 2, Arrays.asList("2.3", "11.3", "23.1"), this.replacement));
        algorithmInfoMap.put("floor", new AlgorithmInfo("floor", 9, AlgorithmType.GENERALIZATION, 2, Arrays.asList("1", "2", "3"), this.generalization));
        algorithmInfoMap.put("valueMapping", new AlgorithmInfo("valueMapping", 10, AlgorithmType.REPLACEMENT, 4, Arrays.asList("20", "30", "50"), this.replacement));
        algorithmInfoMap.put("truncation", new AlgorithmInfo("truncation", 11, AlgorithmType.GENERALIZATION, 1, Arrays.asList("3", "2", "1"), this.generalization));
        algorithmInfoMap.put("floorTime", new AlgorithmInfo("floorTime", 12, AlgorithmType.GENERALIZATION, 3, null, this.generalization));
        algorithmInfoMap.put("suppressEmail", new AlgorithmInfo("suppressEmail", 13, AlgorithmType.REPLACEMENT, 8, Arrays.asList("1", "2", "3"), this.replacement));
        algorithmInfoMap.put("addressHide", new AlgorithmInfo("addressHide", 14, AlgorithmType.GENERALIZATION, 4, Arrays.asList("district", "city", "province"), this.generalization));
        algorithmInfoMap.put("nameHide", new AlgorithmInfo("nameHide", 15, AlgorithmType.REPLACEMENT, 6, Arrays.asList("headtail", "first", "none"), this.replacement));
        algorithmInfoMap.put("numberHide", new AlgorithmInfo("numberHide", 16, AlgorithmType.REPLACEMENT, 7, Arrays.asList("3", "4", "5"), this.replacement));
        algorithmInfoMap.put("SHA512", new AlgorithmInfo("SHA512", 17, AlgorithmType.REPLACEMENT, 3, Arrays.asList("MD5", "SHA1", "SHA512"), this.replacement));
        algorithmInfoMap.put("date_group_replace", new AlgorithmInfo("date_group_replace", 18, AlgorithmType.GENERALIZATION, 5, Arrays.asList("10", "30", "50"), this.generalization));
        algorithmInfoMap.put("passReplace", new AlgorithmInfo("passReplace", 19, AlgorithmType.REPLACEMENT, 5, Arrays.asList("15", "20", "25"), this.replacement));
        algorithmInfoMap.put("value_hide", new AlgorithmInfo("value_hide", 20, AlgorithmType.REPLACEMENT, 1, Arrays.asList("0", "X", "*"), this.replacement));
        algorithmInfoMap.put("suppressAllIp", new AlgorithmInfo("suppressAllIp", 21, AlgorithmType.REPLACEMENT, 9, Arrays.asList("1", "12", "1234"), this.replacement));
        algorithmInfoMap.put("suppressIpRandomParts", new AlgorithmInfo("suppressIpRandomParts", 22, AlgorithmType.REPLACEMENT, 10, Arrays.asList("1", "2", "3"), this.replacement));
        algorithmInfoMap.put("Noisy_Histogram2", new AlgorithmInfo("Noisy_Histogram2", 23, AlgorithmType.DP, 25, null, this.dp));
        algorithmInfoMap.put("Noisy_Histogram1", new AlgorithmInfo("Noisy_Histogram1", 24, AlgorithmType.DP, 24, null, this.dp));
        // 图片脱敏
        algorithmInfoMap.put("meanValueImage", new AlgorithmInfo("meanValueImage", 40, AlgorithmType.GENERALIZATION, 12, Arrays.asList("15", "21", "27"), this.generalization));
        algorithmInfoMap.put("gaussian_blur", new AlgorithmInfo("gaussian_blur", 41, AlgorithmType.GENERALIZATION, 10, Arrays.asList("16", "32", "64"), this.generalization));
        algorithmInfoMap.put("pixelate", new AlgorithmInfo("pixelate", 42, AlgorithmType.GENERALIZATION, 9, Arrays.asList("15", "25", "35"), this.generalization));
        algorithmInfoMap.put("box_blur", new AlgorithmInfo("box_blur", 43, AlgorithmType.GENERALIZATION, 11, Arrays.asList("4", "8", "16"), this.generalization));
        algorithmInfoMap.put("dpImage", new AlgorithmInfo("dpImage", 44, AlgorithmType.DP, 5, Arrays.asList("1.0", "0.5", "0.1"), this.dp));
        algorithmInfoMap.put("im_coder2", new AlgorithmInfo("im_coder2", 45, AlgorithmType.DP, 27, Arrays.asList("10", "1", "0.1"), this.dp));
        algorithmInfoMap.put("replace_region", new AlgorithmInfo("replace_region", 46, AlgorithmType.GENERALIZATION, 13,
                Arrays.asList(Arrays.asList("100", "100", "200", "200"), Arrays.asList("50", "50", "300", "300"), Arrays.asList("25", "25", "400", "400")), this.generalization));
        algorithmInfoMap.put("image_exchange_channel", new AlgorithmInfo("image_exchange_channel", 47, AlgorithmType.REPLACEMENT, 11, null, this.replacement));
        algorithmInfoMap.put("image_add_color_offset", new AlgorithmInfo("image_add_color_offset", 48, AlgorithmType.REPLACEMENT, 12, Arrays.asList("20", "50", "100"), this.replacement));
        algorithmInfoMap.put("image_face_sub", new AlgorithmInfo("image_face_sub", 49, AlgorithmType.REPLACEMENT, 13, null, this.replacement));
        // 视频脱敏
        algorithmInfoMap.put("meanValueVideo", new AlgorithmInfo("meanValueVideo", 50, AlgorithmType.GENERALIZATION, 17, Arrays.asList("9", "15", "21"), this.generalization));
        algorithmInfoMap.put("gaussian_blur_video", new AlgorithmInfo("gaussian_blur_video", 51, AlgorithmType.GENERALIZATION, 15, Arrays.asList("5", "10", "15"), this.generalization));
        algorithmInfoMap.put("pixelate_video", new AlgorithmInfo("pixelate_video", 52, AlgorithmType.GENERALIZATION, 14, Arrays.asList("5", "10", "15"), this.generalization));
        algorithmInfoMap.put("box_blur_video", new AlgorithmInfo("box_blur_video", 53, AlgorithmType.GENERALIZATION, 16, Arrays.asList("2", "4", "8"), this.generalization));
        algorithmInfoMap.put("replace_region_video", new AlgorithmInfo("replace_region_video", 54, AlgorithmType.GENERALIZATION, 18, Arrays.asList(
                Arrays.asList("100", "100", "200", "200"), Arrays.asList("50", "50", "300", "300"), Arrays.asList("25", "25", "400", "400")), this.generalization));
        algorithmInfoMap.put("video_add_color_offset", new AlgorithmInfo("video_add_color_offset", 55, AlgorithmType.REPLACEMENT, 14, Arrays.asList("20", "50", "100"), this.replacement));
        algorithmInfoMap.put("video_remove_bg", new AlgorithmInfo("video_remove_bg", 56, AlgorithmType.REPLACEMENT, 16, null, this.replacement));
        algorithmInfoMap.put("video_face_sub", new AlgorithmInfo("video_face_sub", 57, AlgorithmType.REPLACEMENT, 15, null, this.replacement));
        // 图形脱敏
        algorithmInfoMap.put("dpGraph", new AlgorithmInfo("dpGraph", 60, AlgorithmType.DP, 7, Arrays.asList("5.0", "1.0", "0.2"), this.dp));
        // 音频脱敏
        algorithmInfoMap.put("dpAudio", new AlgorithmInfo("dpAudio", 70, AlgorithmType.DP, 6, Arrays.asList("5.0", "1.0", "0.2"), this.dp));
        algorithmInfoMap.put("voice_replace", new AlgorithmInfo("voice_replace", 71, AlgorithmType.REPLACEMENT, 19, Arrays.asList("3.6", "2", "0.7"), this.replacement));
        algorithmInfoMap.put("apply_audio_effects", new AlgorithmInfo("apply_audio_effects", 72, AlgorithmType.REPLACEMENT, 18, Arrays.asList(
                Arrays.asList("-3", "0.7", "-5", "5"), Arrays.asList("2", "0.8", "-10", "10"), Arrays.asList("5", "0.9", "-20", "20")), this.replacement));
        algorithmInfoMap.put("audio_reshuffle", new AlgorithmInfo("audio_reshuffle", 73, AlgorithmType.REPLACEMENT, 17, Arrays.asList("5", "10", "15"), this.replacement));
        algorithmInfoMap.put("audio_floor", new AlgorithmInfo("audio_floor", 74, AlgorithmType.GENERALIZATION, 19, Arrays.asList("3", "2", "1"), this.generalization));
        algorithmInfoMap.put("audio_spec", new AlgorithmInfo("audio_spec", 75, AlgorithmType.GENERALIZATION, 20,
                Arrays.asList(Arrays.asList("0.1", "0.3"), Arrays.asList("0.3", "0.5"), Arrays.asList("0.5", "0.7")), this.generalization));
        algorithmInfoMap.put("audio_augmentation", new AlgorithmInfo("audio_augmentation", 76, AlgorithmType.GENERALIZATION, 21, Arrays.asList("2", "4", "7"), this.generalization));
        algorithmInfoMap.put("audio_median", new AlgorithmInfo("audio_median", 77, AlgorithmType.GENERALIZATION, 22, Arrays.asList("5", "10", "15"), this.generalization));
    }

    public AlgorithmInfo getAlgorithmInfoFromName(String name) {
        return algorithmInfoMap.get(name);
    }

    public AlgorithmInfo getAlgorithmInfoFromId(int id) {
        return algorithmInfoMap.values().stream()
                .filter(algorithmInfo -> algorithmInfo.getId() == id)
                .findFirst().orElse(null);
//        for (Map.Entry<String, AlgorithmInfo> entry : algorithmInfoMap.entrySet()) {
//            if (entry.getValue().getId() == id) {
//                return entry.getValue();
//            }
//        }
//        return null;
    }
}
