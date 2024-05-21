package com.lu.gademo.controller;

import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.Dp;
import com.lu.gademo.utils.Util;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 随机加噪算法
 */
@Controller
@RequestMapping("/RandomNoise")
@AllArgsConstructor
@Slf4j
public class RandomNoiseController {
    Dp dp;
    Util util;

    @ResponseBody
    @RequestMapping(value = "/desenValue", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenValue(@RequestParam String rawData,
                             @RequestParam String samples,
                             @RequestParam String algName) {
        String[] types = algName.split(",");
        algName = types[types.length - 1];
        log.info(algName);
        int algNum = 0;
        switch (algName) {
            //
            case "report_noisy_max4": {
                algNum = 10;
                break;
            }
            case "report_noisy_max3": {
                algNum = 3;
                break;
            }
            //
            case "noisy_hist2": {
                algNum = 25;
                break;
            }
            default:
                throw new RuntimeException("Unkown algorithm: " + algName);

        }
        List<Double> rawDataList = Arrays.stream(rawData.split(",")).filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());
        DSObject resultDS = null;
        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);

        if (algName.equals("report_noisy_max3")) {
            resultDS = dp.service(rawObject, algNum, Integer.parseInt(samples), 1);
        } else {
            resultDS = dp.service(rawObject, algNum, Integer.parseInt(samples));
        }

        StringBuilder resultString = new StringBuilder();
        for (Object s : resultDS.getList()) {
            resultString.append(s).append("\n");
        }
        return resultString.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/desenValue2", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String desenValue2(@RequestParam String rawData,
                              @RequestParam String samples,
                              @RequestParam String algName,
                              @RequestParam String c,
                              @RequestParam String t) {

        String[] types = algName.split(",");
        algName = types[types.length - 1];
        log.info(algName);
        int algNum;
        switch (algName) {
            //
            case "sparse_vector_technique3": {
                algNum = 13;
                break;
            }
            case "sparse_vector_technique4": {
                algNum = 14;
                break;
            }
            //
            case "sparse_vector_technique5": {
                algNum = 15;
                break;
            }
            //
            case "sparse_vector_technique6": {
                algNum = 16;
                break;
            }
            default:
                throw new RuntimeException("Unkown algorithm: " + algName);

        }

        List<Double> rawDataList = Arrays.stream(rawData.split(",")).filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());

        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);
        DSObject resultDS = dp.service(rawObject, algNum, Integer.parseInt(c), Integer.parseInt(t));
        StringBuilder resultString = new StringBuilder();
        for (Object s : resultDS.getList()) {
            resultString.append(s).append("\n");
        }
        return resultString.toString();
    }
}


